package com.caribresort.server.ManagerFunctions.OrderManagement;

import com.caribresort.client.model.*;
import com.caribresort.server.DBConnector;
import com.caribresort.server.ManagerFunctions.DrinkManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InsertOrder {
    
    private static Connection connection = DBConnector.getDatabaseConnection(); // ESTABLISH DATABASE CONNECTION
    private static final Logger logger = LogManager.getLogger(DrinkManagement.class); // LOGGER OBJECT
 
    public InsertOrder() {
         // ESTABLISH THE CONNECTION TO THE DATABASE USING DBConnector
         connection = DBConnector.getDatabaseConnection();
    }

    // FUNCTION TO INSERT AN ORDER INTO THE DATABASE
    public static Boolean insertOrder(Order order) {
        String orderInsertSQL = "INSERT INTO orders (orderID, orderDate, status, orderTotal) VALUES (?, ?, ?, ?)"; // SQL QUERY FOR INSERTING ORDER
        String orderDetailsInsertSQL = "INSERT INTO orderDetails (orderDetailsID, orderID, drinkID, quantity, totalCost) VALUES (?, ?, ?, ?, ?)"; // SQL QUERY FOR INSERTING ORDER DETAILS

        try {   
            connection.setAutoCommit(false); // START A TRANSACTION

            // INSERT INTO THE ORDERS TABLE
            try (PreparedStatement orderStatement = connection.prepareStatement(orderInsertSQL)) {
                orderStatement.setInt(1, order.getOrderID()); // SET ORDER ID
                orderStatement.setString(2, Date.convertToDateTimeString(order.getOrderDate())); // CONVERT ORDER DATE TO STRING FORMAT
                orderStatement.setString(3, order.getStatus().name()); // GET THE ENUM NAME AS STRING
                orderStatement.setDouble(4, order.getOrderTotal()); // SET ORDER TOTAL

                orderStatement.executeUpdate(); // EXECUTE THE INSERT STATEMENT
            } // END TRY

            // INSERT INTO THE ORDERDETAILS TABLE FOR EACH DETAIL
            for (OrderDetails details : order.getOrderDetailsList()) {
                
                try (PreparedStatement detailsStatement = connection.prepareStatement(orderDetailsInsertSQL)) {
                    detailsStatement.setInt(1, details.getOrderDetailsID()); // SET ORDER DETAILS ID
                    detailsStatement.setInt(2, order.getOrderID()); // ASSOCIATE WITH THE MAIN ORDER
                    detailsStatement.setString(3, details.getDrinkID()); // SET DRINK ID

                    // ----------------------------------- DEBUGGING -----------------------------------
                    System.out.println("\n\nDRINK ID: " + details.getDrinkID() + "\n\n");
                    // ----------------------------------- DEBUGGING -----------------------------------

                    detailsStatement.setInt(4, details.getQuantity()); // SET QUANTITY
                    detailsStatement.setDouble(5, details.getDetailCost()); // SET TOTAL COST

                    detailsStatement.executeUpdate(); // EXECUTE THE INSERT STATEMENT FOR ORDER DETAILS
                } // END TRY
            
            } // END FOR

            // COMMIT THE TRANSACTION
            connection.commit(); 
            logger.info("[ORDER] NEW ORDER INSERTED ID[" + order.getOrderID() + "]"); // LOG THE NEW ORDER
            
            return true; // RETURNS TRUE IF INSERT WAS SUCCESSFUL
        
        } catch (SQLException e) {
            
            // HANDLE ANY SQL EXCEPTIONS
            logger.error("[ORDER] ERROR WITH ORDER INSERTION: " + e.getMessage());
            
            try {
                // ROLLBACK TRANSACTION IN CASE OF ERROR
                if (connection != null) {
                    connection.rollback(); // ROLLBACK TRANSACTION
                }
                
                return true; // RETURNS TRUE IF INSERT WAS SUCCESSFUL
            
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            
            return false; // RETURNS FALSE IF INSERT FAILED
        
        } finally {
            try {
                // RESTORE AUTO-COMMIT MODE
                if (connection != null) {
                    connection.setAutoCommit(true); // RESTORE AUTO-COMMIT
                }
            
            } catch (SQLException e) {
                System.err.println("Error restoring auto-commit mode: " + e.getMessage());
            } // END TRY
        
        } // END TRY
    
    } // END FUNCTION

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // FUNCTION TO CLOSE THE DATABASE CONNECTION
    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close(); // CLOSE DATABASE CONNECTION
                System.out.println("Database connection closed!"); // PRINT SUCCESS MESSAGE
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage()); // HANDLE SQL EXCEPTION
        }
    } // END FUNCTION

} // END CLASS
