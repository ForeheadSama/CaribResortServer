package com.caribresort.database.ManagerFunctions.OrderManagement;


import com.caribresort.database.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteOrder {
    private static Connection connection = DBConnector.getDatabaseConnection(); // ESTABLISH DATABASE CONNECTION
    private static final Logger logger = LogManager.getLogger(DeleteOrder.class); // LOGGER OBJECT

    // CONSTRUCTOR
    public DeleteOrder() {
        // ESTABLISH THE CONNECTION TO THE DATABASE USING DBConnector
        connection = DBConnector.getDatabaseConnection();
    } // END FUNCTION

    // FUNCTION TO DELETE AN ORDER FROM THE DATABASE
     public static Boolean deleteOrder(int orderId) {
        String orderDeleteSQL = "DELETE FROM orders WHERE orderID = ?"; // SQL QUERY FOR DELETING ORDER
        String orderDetailsDeleteSQL = "DELETE FROM orderDetails WHERE orderID = ?"; // SQL QUERY FOR DELETING ORDER DETAILS
 
        try {
            // START A TRANSACTION TO DELETE THE ORDER AND ITS DETAILS 
            connection.setAutoCommit(false);
 
            // DELETE THE ORDER
            try (PreparedStatement orderStatement = connection.prepareStatement(orderDeleteSQL)) {
                orderStatement.setInt(1, orderId); // SET ORDER ID IN QUERY
                orderStatement.executeUpdate(); // EXECUTE DELETE STATEMENT
            }
 
            // DELETE ALL ORDER DETAILS FOR THIS ORDER
            try (PreparedStatement detailsStatement = connection.prepareStatement(orderDetailsDeleteSQL)) {
                detailsStatement.setInt(1, orderId); // SET ORDER ID IN QUERY
                detailsStatement.executeUpdate(); // EXECUTE DELETE STATEMENT
            }
 
            // COMMIT THE TRANSACTION
            connection.commit(); 
            return true; // RETURNS TRUE IF DELETE WAS SUCCESSFUL
         
            } catch (SQLException e) {
                
                // HANDLE ANY SQL EXCEPTIONS
                logger.error("[DATABASE] ERROR DELETING ORDER WITH ID[" + orderId + "]\n" + e.getMessage());
             
            try {
                
                // ROLLBACK TRANSACTION IN CASE OF ERROR 
                if (connection != null) {
                     connection.rollback(); 
                } // END IF
                 
                return true; // RETURNS TRUE IF DELETE WAS SUCCESSFUL
             
            } catch (SQLException rollbackEx) {
                 logger.error("[DATABASE] ROLLBACK FAILED: " + rollbackEx.getMessage());
                 return false; // RETURNS FALSE IF DELETE FAILED
            }
        
        } finally {
            try {

                // RESTORE AUTO-COMMIT MODE
                if (connection != null) {
                    connection.setAutoCommit(true); // RESTORE AUTO-COMMIT
                } // END IF

            } catch (SQLException e) {
                 logger.error("[DATABASE] ERROR RESTORING AUTO-COMMIT MODE: " + e.getMessage());
            
                } // END FUNCTION
        } // END FUNCTION
    } // END FUNCTION
 
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
    // FUNCTION TO DELETE ALL ORDERS FROM THE DATABASE
    public static Boolean deleteAllOrders() {
        String orderDeleteSQL = "DELETE FROM orders"; // SQL QUERY FOR DELETING ALL ORDERS
        String orderDetailsDeleteSQL = "DELETE FROM orderDetails"; // SQL QUERY FOR DELETING ALL ORDER DETAILS
 
        try {
            // START A TRANSACTION
            connection.setAutoCommit(false);
 
            // DELETE ALL ORDERS
            try (Statement orderStatement = connection.createStatement()) {
                orderStatement.executeUpdate(orderDeleteSQL); // EXECUTE DELETE STATEMENT FOR ORDERS
            } 
 
            // DELETE ALL ORDER DETAILS
            try (Statement detailsStatement = connection.createStatement()) {
                detailsStatement.executeUpdate(orderDetailsDeleteSQL); // EXECUTE DELETE STATEMENT FOR ORDER DETAILS
            }
 
            // COMMIT THE TRANSACTION
            connection.commit(); 
            return true; // RETURNS TRUE IF DELETE WAS SUCCESSFUL
        
        } catch (SQLException e) {
            // HANDLE ANY SQL EXCEPTIONS
            logger.error("[DATABASE] ERROR DELETING ALL ORDERS: " + e.getMessage());
            
            try {
                // ROLLBACK TRANSACTION IN CASE OF ERROR
                if (connection != null) {
                     connection.rollback(); // ROLLBACK TRANSACTION
                }
                return true; // RETURNS TRUE IF DELETE WAS SUCCESSFUL
            
            } catch (SQLException rollbackEx) {
                logger.error("[DATABASE] ROLLBACK FAILED: " + rollbackEx.getMessage());
                return false; // RETURNS FALSE IF DELETE FAILED
            }
        
        } finally {
            try {
                // RESTORE AUTO-COMMIT MODE
                if (connection != null) {
                    connection.setAutoCommit(true); // RESTORE AUTO-COMMIT
                }
            
            } catch (SQLException e) {
                logger.error("[DATABASE] ERROR RESTORING AUTO-COMMIT MODE: " + e.getMessage());
            } // END CTACH
        
        }  // END FINALLY
    
    } // END FUNCTION


    
} // END CLASS
