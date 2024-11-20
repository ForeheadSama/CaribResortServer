package com.caribresort.database.ManagerFunctions;

import com.caribresort.client.model.Drink;

// /**
//          * Provides core functionality for managing drinks in the CaribResort database,
//          * allowing the manager to add, view, update, and delete drink entries. It connects to the
//          * database through the DBConnector class and uses SQL queries to perform CRUD (Create, Read,
//          * Update, Delete) operations on drink records stored in the database.
//          * 
//          * Key functionalities:
//          * - insertDrink: Adds a new drink record to the database.
//          * - getAllDrinks: Retrieves all drink records and returns them as a list of Drink objects.
//          * - updateDrink: Updates the details of an existing drink.
//          * - removeDrink: Deletes a drink from the database based on its ID.
//          * - closeConnection: Closes the database connection once operations are complete.
//          *
    
//     * COMPLETED BY: EDWARDS
//  */

import com.caribresort.database.DBConnector;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.HashMap;
 import java.util.Map;
 
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 
 public class DrinkManagement {
     
    private static Connection connection = DBConnector.getDatabaseConnection(); // Connection to the database
    private static final Logger logger = LogManager.getLogger(DrinkManagement.class); // Logger object
 
    public DrinkManagement() {
         // Establish the connection to the database using DBConnectorFactory
         connection = DBConnector.getDatabaseConnection();
     } // End of constructor
 
     // Method to insert a Drink into the database
     public static Boolean insertDrink(Drink drink) {
         String sql = "INSERT INTO drinks (drinkID, drinkName, unitPrice, isAlcoholic, quantity) VALUES (?, ?, ?, ?, ?)";
         
         try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
             
             pstmt.setString(1, drink.getDrinkID());
             pstmt.setString(2, drink.getDrinkName());
             pstmt.setDouble(3, drink.getUnitPrice());
             pstmt.setBoolean(4, drink.getIsAlcoholic());
             pstmt.setInt(5, drink.getQuantity());
             pstmt.executeUpdate();  // Execute the query
 
             // Log the successful insertion
             logger.info("[DATABASE] DRINK INSERTED SUCCESSFULLY [DRINK ID: " + drink.getDrinkID() + "]");
             return true; // Returns true if update was successful
         } catch (SQLException e) {
             // Log the error if insertion fails
             logger.error("[DATABASE] ERROR INSERTING DRINK [" + drink.getDrinkName() + "]" + e.getMessage());
             return false; // Returns false if insert failed
         } // End of catch block
     
     } // End of insertDrink method

     public static String getDrinkIDByName(String drinkName) {
        String sql = "SELECT drinkID FROM drinks WHERE drinkName = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, drinkName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String drinkID = rs.getString("drinkID");
                return drinkID;
            } else {
                logger.error("[DATABASE] NO DRINK ID FOUND FOR " + drinkName);
            }
        } catch (SQLException e) {
            logger.error("[DATABASE] ERROR RETRIEVING DRINK ID FOR " + drinkName + ": " + e.getMessage());
        }
        return null;
    }
 
     // Method to get a drink from the database
     public static Drink getADrink(String drinkIDToGet) {
         String sql = "SELECT * FROM drinks WHERE `drinkID` = '" + drinkIDToGet + "'";
         
         try{
             // Execute the query and store the result in a ResultSet
             Statement stat = connection.createStatement();
             ResultSet rs = stat.executeQuery(sql);
             rs.next();
             // Retrieve the drink details from the ResultSet
             String drinkID = rs.getString("drinkID");
             String drinkName = rs.getString("drinkName");
             double unitPrice = rs.getDouble("unitPrice");
             boolean isAlcoholic = rs.getBoolean("isAlcoholic");
             int quantity = rs.getInt("quantity");
 
             // Create a new Drink object and add it to the list
             Drink drink = new Drink(drinkID, drinkName, unitPrice, isAlcoholic, quantity);
             return drink; // Return the Drink object
         } catch (SQLException e) {
             // Log the error if reading drinks fails
             logger.error("[DATABASE] ERROR RETRIEVING DRINK LIST. " + e.getMessage());
             return null; // Return null
         } // End of catch block
 
     } // End of getAllDrinks method
 
     // Method to read all drinks from the database
     public static List<Drink> getAllDrinks() {
         List<Drink> drinks = new ArrayList<>();
         String sql = "SELECT drinkID, drinkName, unitPrice, isAlcoholic, quantity FROM drinks";
 
         try (PreparedStatement pstmt = connection.prepareStatement(sql);
                 // Execute the query and store the result in a ResultSet
                 ResultSet rs = pstmt.executeQuery()) { 
                 
             // Iterate through the ResultSet and create Drink objects
             while (rs.next()) {
 
                 // Retrieve the drink details from the ResultSet
                 String drinkID = rs.getString("drinkID");
                 String drinkName = rs.getString("drinkName");
                 double unitPrice = rs.getDouble("unitPrice");
                 boolean isAlcoholic = rs.getBoolean("isAlcoholic");
                 int quantity = rs.getInt("quantity");
 
                 // Create a new Drink object and add it to the list
                 Drink drink = new Drink(drinkID, drinkName, unitPrice, isAlcoholic, quantity);
                 drinks.add(drink); // Add the drink to the list
 
             } // End of while loop
         
         } catch (SQLException e) {
             // Log the error if reading drinks fails
             logger.error("[DATABASE] ERROR RETRIEVING DRINK LIST. " + e.getMessage());
             
         } // End of catch block
 
         return drinks; // Return the list of drinks
     } // End of getAllDrinks method
 
     /**
      * Updates the details of an existing drink in the database.
      *
      * @param drink the Drink object containing updated information
      * @return true if the update was successful, false otherwise
      */
     public static boolean updateDrink(Drink drink) {
        
        String drinkID = drink.getDrinkID();
        String sqlQuery = "UPDATE Drinks SET drinkName = ?, unitPrice = ?, isAlcoholic = ?, quantity = ? WHERE drinkID = '" + drinkID + "'";
 
         try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
             // Set the parameters for the prepared statement
             statement.setString(1, drink.getDrinkName());
             statement.setDouble(2, drink.getUnitPrice());
             statement.setBoolean(3, drink.getIsAlcoholic());
             statement.setInt(4, drink.getQuantity());
 
             int rowsAffected = statement.executeUpdate(); // Execute the update query
             return rowsAffected > 0; // Returns true if update was successful
         
         } // End of try block
         catch (SQLException e) {
             // Log the error if updating the drink fails
             e.printStackTrace();
             logger.error("[DATABASE] ERROR UPDATING DRINK [" + drink.getDrinkID() + "] " + e.getMessage());
             return false; 
         
         } // End of catch block
     
     } // End of updateDrink method
 
     /**
      * Removes a drink from the database by its drinkID.
      *
      * @param drinkID the ID of the drink to remove
      * @return true if the removal was successful, false otherwise
      */
     public static boolean removeDrink(String drinkID) {
         String sqlQuery = "DELETE FROM Drinks WHERE drinkID = ?";
 
         try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
             statement.setString(1, drinkID); // Set the drinkID parameter
             int rowsAffected = statement.executeUpdate(); // Execute the delete query
 
             // Log the successful removal
             logger.info("[DATABASE] DRINK REMOVED SUCCESSFULLY <ID: " + drinkID + ">");
             
             if (rowsAffected > 0) { return true; }  // Returns true if removal was successful
                else { return false; } // Returns false if removal failed
                
         } // End of try block
         catch (SQLException e) {
             // Log the error if removing the drink fails
             e.printStackTrace();
             logger.error("[DATABASE] ERROR REMOVING DRINK <" + drinkID + "> " + e.getMessage());
             return false;
        
         } // End of catch block
     
     } // End of removeDrink method
 
     // Check if there's enough quantity for an order
     public static Map<String, String> checkDrinkAvailability(Map<String, Integer> orderQuantities) {
         Map<String, String> availability = new HashMap<>(); // Map to store drink availability
         String sql = "SELECT drinkName, quantity FROM drinks WHERE drinkName = ?";
 
         try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
             
             // Iterate through the order quantities
             for (Map.Entry<String, Integer> entry : orderQuantities.entrySet()) { 
                 pstmt.setString(1, entry.getKey()); // Set the drink name parameter
                 ResultSet rs = pstmt.executeQuery(); // Execute the query
                 
                 // Check if the drink exists in the database
                 if (rs.next()) {
                     
                     // Get the available quantity from the ResultSet
                     int availableQuantity = rs.getInt("quantity");
 
                     // Check if the available quantity is sufficient
                     if (availableQuantity < entry.getValue()) {
                         // Update the availability map with an error message
                         availability.put(entry.getKey(), 
                             "Insufficient quantity. Only " + availableQuantity + " available.");
                             logger.info("[DATABASE] INSUFFICIENT QUANTITY FOR [" + entry.getKey() + "]");
                     
                     } // End of if block
                 
                 } // End of if block
                 else { 
                     // Update the availability map with an error message
                     availability.put(entry.getKey(), "Drink not found in database.");
                     logger.info("[DATABASE] DRINK ORDERED BUT NOT FOUND IN DATABASE [" + entry.getKey() + "]");
             
                 } // End of if-else block
                 
                 rs.close(); // Close the ResultSet
             } // End of for loop
         
         } // End of try block 
         catch (SQLException e) {
             // Log the error if checking drink availability fails
             logger.error("[DATABASE] ERROR CHECKING DRINK AVAILABILITY: " + e.getMessage());
         
         } // End of catch block
 
         return availability; // Return the availability map
     } // End of checkDrinkAvailability method
 
     // Update quantities after a successful order
     public static boolean updateDrinkQuantities(Map<String, Integer> orderQuantities) {
         String sql = "UPDATE drinks SET quantity = quantity - ? WHERE drinkName = ?";
         boolean success = true; // Flag to track the success of the transaction
 
         try {
             // Start a transaction
             connection.setAutoCommit(false); // Disable auto-commit
             
             try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
 
                 // Iterate through the order quantities
                 for (Map.Entry<String, Integer> entry : orderQuantities.entrySet()) {
                     pstmt.setInt(1, entry.getValue());
                     pstmt.setString(2, entry.getKey());
                     pstmt.executeUpdate();
                 
                 } // End of for loop
                 
                 connection.commit();  // Commit transaction
             
             } // End of try block
             catch (SQLException e) {
                 connection.rollback();  // Rollback on error
                 success = false;        // Set success flag to false
                 
                 logger.error("[DATABASE] ERROR UPDATING DRINK QUANTITY " + e.getMessage()); // Log the error
             } // End of catch block
             
         } // End of try block
         catch (SQLException e) {
             success = false; // Set success flag to false
             System.out.println("Transaction error: " + e.getMessage()); // Log the error
         
         } finally {
             try {
                 connection.setAutoCommit(true);  // Reset auto-commit
             } catch (SQLException e) {
                 System.out.println("Error resetting auto-commit: " + e.getMessage()); // Log the error
             } // End of catch block
         
         } // End of finally block
 
         return success;
     } // End of updateDrinkQuantities method
  } // End of DrinkManagement class
 