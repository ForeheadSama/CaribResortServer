package com.caribresort.server.ManagerFunctions.OrderManagement;

import com.caribresort.client.model.*;
import com.caribresort.server.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateOrder {
    
    private static Connection connection = DBConnector.getDatabaseConnection(); // ESTABLISH DATABASE CONNECTION
    private static final Logger logger = LogManager.getLogger(UpdateOrder.class);

    public UpdateOrder() {
        // ESTABLISH THE CONNECTION TO THE DATABASE USING DBConnector
        connection = DBConnector.getDatabaseConnection();
    }

    // FUNCTION TO UPDATE AN ORDER IN THE DATABASE
     public static Boolean updateOrder(Order order) {
         String orderUpdateSQL = "UPDATE orders SET orderDate = ?, status = ?, orderTotal = ? WHERE orderID = ?"; // SQL QUERY FOR UPDATING ORDER
         String orderDetailsDeleteSQL = "DELETE FROM orderDetails WHERE orderID = ?"; // SQL QUERY FOR DELETING ORDER DETAILS
         String orderDetailsInsertSQL = "INSERT INTO orderDetails (orderDetailsID, orderID, drinkID, quantity, totalCost) VALUES (?, ?, ?, ?, ?)"; // SQL QUERY FOR INSERTING ORDER DETAILS
 
         try {
             // START A TRANSACTION
             connection.setAutoCommit(false);
 
             // UPDATE THE ORDER
             try (PreparedStatement orderStatement = connection.prepareStatement(orderUpdateSQL)) {
                 orderStatement.setString(1, Date.convertToDateTimeString(order.getOrderDate())); // SET ORDER DATE
                 orderStatement.setString(2, order.getStatus().name()); // SET ORDER STATUS
                 orderStatement.setDouble(3, order.getOrderTotal()); // SET ORDER TOTAL
                 orderStatement.setInt(4, order.getOrderID()); // SET ORDER ID
 
                 orderStatement.executeUpdate(); // EXECUTE UPDATE STATEMENT
             } // END TRY
 
             // DELETE ALL ORDER DETAILS FOR THIS ORDER
             try (PreparedStatement detailsDeleteStatement = connection.prepareStatement(orderDetailsDeleteSQL)) {
                 detailsDeleteStatement.setInt(1, order.getOrderID()); // SET ORDER ID IN QUERY
                 detailsDeleteStatement.executeUpdate(); // EXECUTE DELETE STATEMENT
             } // END TRY
 
             // INSERT NEW ORDER DETAILS
             for (OrderDetails details : order.getOrderDetailsList()) {
                 try (PreparedStatement detailsStatement = connection.prepareStatement(orderDetailsInsertSQL)) {
                     detailsStatement.setInt(1, details.getOrderDetailsID()); // SET ORDER DETAILS ID
                     detailsStatement.setInt(2, order.getOrderID()); // SET ORDER ID
                     detailsStatement.setString(3, details.getDrinkID()); // SET DRINK ID
                     detailsStatement.setInt(4, details.getQuantity()); // SET QUANTITY
                     detailsStatement.setDouble(5, details.getDetailCost()); // SET TOTAL COST
 
                     detailsStatement.executeUpdate(); // EXECUTE INSERT STATEMENT FOR ORDER DETAILS
                 } // END TRY
             } // END FOR
 
             // COMMIT THE TRANSACTION
             connection.commit(); 
             return true; // RETURNS TRUE IF UPDATE WAS SUCCESSFUL
         } catch (SQLException e) {
             // HANDLE ANY SQL EXCEPTIONS
             logger.error("[ORDER] ERROR UPDATING ORDER: " + e.getMessage());
             try {
                 // ROLLBACK TRANSACTION IN CASE OF ERROR
                 if (connection != null) {
                     connection.rollback(); // ROLLBACK TRANSACTION
                 } // END IF

                 return true; // RETURNS TRUE IF UPDATE WAS SUCCESSFUL
             } catch (SQLException rollbackEx) {
                logger.error("[DATABASE] ROLLBACK FAILED: " + rollbackEx.getMessage());
                return false; // RETURNS FALSE IF UPDATE FAILED
             } // END CATCH
         
            } finally {
             try {
                 
                // RESTORE AUTO-COMMIT MODE
                 if (connection != null) {
                     connection.setAutoCommit(true); // RESTORE AUTO-COMMIT
                 } // END IF
             
                } catch (SQLException e) {
                logger.error("[DATABASE] ERROR RESTORING AUTO-COMMIT" + e.getMessage());
             } // END TRY
         
            } // END FINALLY
     
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
    }
} // END CLASS
