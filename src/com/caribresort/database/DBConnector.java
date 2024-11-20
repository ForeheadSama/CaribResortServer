/**
 *      * Creates the database connection used throughout the program
 * 
    * COMPLETED BY: EDWARDS
 */

package com.caribresort.database;

 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 
 public class DBConnector {
     
     private static Connection connection = null;
     private static final Logger logger = LogManager.getLogger(DBConnector.class); // Logger object
 
     //checks and sets the databse connection
    public static Connection getDatabaseConnection() {
         // Check if the connection is null
         if (connection == null) {
             try {
                 // The database URL, username, and password
                 String url = "jdbc:mysql://localhost:3306/caribresort"; 
                 String username = "root";
                 String password = "";
 
                 // Initialize the connection
                 connection = DriverManager.getConnection(url, username, password);
 
                 // Log the successful connection
                 logger.info("[DATABASE] CONNECTION SUCCESSFUL");
                 
             } catch (SQLException e) {
                 e.printStackTrace();
                 // Log the failed connection
                 logger.error("[DATABASE] CONNECTION FAILED");
             }
         } else {
            //check if connection is already there
            return connection;
         }
 
         // Return the connection (either new or already existing)
         return connection;
    }

    // Function to close the database connection
    public void closeConnection() {
        try {
            if (connection != null) { // Check if the connection is open
                connection.close(); // Close the connection
                System.out.println("Database connection closed!");
            }
        }  // End of try block
        catch (SQLException e) { 
            System.err.println("Error closing database connection: " + e.getMessage());
        
        } // End of catch block
   } // End of closeConnection method
 }