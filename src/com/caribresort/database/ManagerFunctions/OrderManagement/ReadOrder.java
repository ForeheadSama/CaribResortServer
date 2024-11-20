package com.caribresort.database.ManagerFunctions.OrderManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caribresort.client.model.*;
import com.caribresort.database.DBConnector;
import com.caribresort.server.Request;
import com.caribresort.server.Response;


public class ReadOrder {
    private static Connection connection = DBConnector.getDatabaseConnection(); // ESTABLISH DATABASE CONNECTION
    private static final Logger logger = LogManager.getLogger(ReadOrder.class); // LOGGER FOR THIS CLASS

    public ReadOrder() {
        // ESTABLISH THE CONNECTION TO THE DATABASE USING DBConnector
        connection = DBConnector.getDatabaseConnection();
    }

    // FUNCTION TO CHECK IF AN ORDER ID EXISTS
    public static boolean checkOrderIdExists(int orderId) {
        String query = "SELECT COUNT(*) FROM orders WHERE orderID = ?"; // SQL QUERY TO CHECK ORDER ID EXISTENCE
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, orderId); // SET ORDER ID IN QUERY
            
            try (ResultSet rs = pstmt.executeQuery()) {
               if (rs.next() && rs.getInt(1) > 0) {
                    return false; // RETURN TRUE IF ORDER ID EXISTS
               }
               else {
                return true;
               }
            }
            
        } catch (SQLException e) {
            logger.error("[DATABASE] ERROR CHECKING ORDER ID EXISTENCE: " + e.getMessage()); // LOG ERROR MESSAGE
            return false;
        }
    } // END FUNCTION

    /**
     * GENERATES A UNIQUE ORDER ID NOT ALREADY IN USE.
     */
    public static int generateUniqueOrderId() {
        int orderId; // VARIABLE TO STORE GENERATED ORDER ID
        Random random = new Random(); // CREATE INSTANCE OF RANDOM CLASS
        boolean isUnique = false; // FLAG TO CHECK IF ORDER ID IS UNIQUE
        
        // GENERATE RANDOM ORDER ID AND CHECK IF IT IS UNIQUE
        do {
            
            orderId = random.nextInt(99999) + 1; // GENERATE NUMBER BETWEEN 1 AND 99999
            isUnique = checkOrderIdExists(orderId);

        } while (!isUnique); // REPEAT UNTIL UNIQUE ORDER ID IS GENERATED
        
        return orderId; // RETURN UNIQUE ORDER ID
    } // END METHOD

//     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // FUNCTION TO READ A SINGLE ORDER BASED ON ORDERID
    public static Order readOrder(int orderId) {
        Order order = null; // INITIALIZE ORDER OBJECT TO NULL
        String orderSelectSQL = "SELECT orderID, orderDate, status, orderTotal FROM orders WHERE orderID = ?"; // SQL QUERY FOR SELECTING ORDER
        String orderDetailsSelectSQL = "SELECT orderDetailsID, drinkID, quantity, totalCost FROM orderDetails WHERE orderID = ?"; // SQL QUERY FOR SELECTING ORDER DETAILS
 
        // RETRIEVE ORDER DETAILS
        try (PreparedStatement orderStatement = connection.prepareStatement(orderSelectSQL)) {
            orderStatement.setInt(1, orderId); // SET ORDER ID IN QUERY
            ResultSet orderResultSet = orderStatement.executeQuery(); // EXECUTE QUERY
 
            if (orderResultSet.next()) { // CHECK IF RESULT SET HAS DATA
                // CREATE ORDER OBJECT
                order = new Order();
                order.setOrderID(orderResultSet.getInt("orderID")); // SET ORDER ID
                order.setOrderDate(Date.convertFromDatetimeString(orderResultSet.getString("orderDate"))); // CONVERT STRING TO DATE
                order.setStatus(Order.OrderStatus.valueOf(orderResultSet.getString("status"))); // SET ORDER STATUS
                order.setOrderTotal(orderResultSet.getDouble("orderTotal")); // SET ORDER TOTAL
 
                // RETRIEVE ORDER DETAILS
                List<OrderDetails> orderDetailsList = new ArrayList<>(); // INITIALIZE LIST FOR ORDER DETAILS
                
                try (PreparedStatement detailsStatement = connection.prepareStatement(orderDetailsSelectSQL)) {
                    detailsStatement.setInt(1, orderId); // SET ORDER ID IN QUERY
                    ResultSet detailsResultSet = detailsStatement.executeQuery(); // EXECUTE QUERY FOR ORDER DETAILS
 
                    while (detailsResultSet.next()) { // LOOP THROUGH RESULT SET
                        OrderDetails details = new OrderDetails(); // CREATE ORDER DETAILS OBJECT
                        details.setOrderDetailsID(detailsResultSet.getInt("orderDetailsID")); // SET ORDER DETAILS ID
                        details.setOrderID(orderId); // SET ORDER ID
                        details.setDrinkID(detailsResultSet.getString("drinkID")); // SET DRINK ID
                        details.setQuantity(detailsResultSet.getInt("quantity")); // SET QUANTITY
                        details.setDetailCost(detailsResultSet.getDouble("totalCost")); // SET TOTAL COST
                        orderDetailsList.add(details); // ADD TO LIST
                    } // END WHILE
                
                } // END TRY
                
                order.setOrderDetailsList(orderDetailsList); // SET ORDER DETAILS LIST
            
            } // END IF
         
            } catch (SQLException e) {
                logger.error("[ORDER] ERROR READING ORDER: " + e.getMessage(), e); // LOG ERROR
            
            } // END TRY/CATCH
 
        return order; // RETURN ORDER OBJECT
    } // END FUNCTION

// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
    
    // FUNCTION TO READ ALL ORDERS FROM THE DATABASE
    public static List<Order> readAllOrders() {
        List<Order> orders = new ArrayList<>(); // INITIALIZE LIST FOR ORDERS
        String orderSelectSQL = "SELECT orderID, orderDate, status, orderTotal FROM orders"; // SQL QUERY FOR SELECTING ALL ORDERS
 
        // RETRIEVE ORDERS
        try (PreparedStatement orderStatement = connection.prepareStatement(orderSelectSQL);
              ResultSet orderResultSet = orderStatement.executeQuery()) { // EXECUTE QUERY
 
            while (orderResultSet.next()) { // LOOP THROUGH RESULT SET
                // CREATE ORDER OBJECT
                Order order = new Order();
                order.setOrderID(orderResultSet.getInt("orderID")); // SET ORDER ID
                order.setOrderDate(Date.convertFromDatetimeString(orderResultSet.getString("orderDate"))); // CONVERT STRING TO DATE
                order.setStatus(Order.OrderStatus.valueOf(orderResultSet.getString("status"))); // SET ORDER STATUS
                order.setOrderTotal(orderResultSet.getDouble("orderTotal")); // SET ORDER TOTAL

                // RETRIEVE ORDER DETAILS FOR THIS ORDER
                List<OrderDetails> orderDetailsList = new ArrayList<>(); // INITIALIZE LIST FOR ORDER DETAILS
                String orderDetailsSelectSQL = "SELECT orderDetailsID, drinkID, quantity, totalCost FROM orderDetails WHERE orderID = ?"; // SQL QUERY FOR SELECTING ORDER DETAILS
 
                try (PreparedStatement detailsStatement = connection.prepareStatement(orderDetailsSelectSQL)) {
                    detailsStatement.setInt(1, order.getOrderID()); // SET ORDER ID IN QUERY
                    ResultSet detailsResultSet = detailsStatement.executeQuery(); // EXECUTE QUERY FOR ORDER DETAILS
 
                    while (detailsResultSet.next()) { // LOOP THROUGH RESULT SET
                        OrderDetails details = new OrderDetails(); // CREATE ORDER DETAILS OBJECT
                        details.setOrderDetailsID(detailsResultSet.getInt("orderDetailsID")); // SET ORDER DETAILS ID
                        details.setOrderID(order.getOrderID()); // SET ORDER ID
                        details.setDrinkID(detailsResultSet.getString("drinkID")); // SET DRINK ID
                        details.setQuantity(detailsResultSet.getInt("quantity")); // SET QUANTITY
                        details.setDetailCost(detailsResultSet.getDouble("totalCost")); // SET TOTAL COST
                        orderDetailsList.add(details); // ADD TO LIST
                    } // END WHILE
                
                    } // END TRY
                order.setOrderDetailsList(orderDetailsList); // SET ORDER DETAILS LIST
                orders.add(order); // ADD ORDER TO LIST
            
            } // END WHILE
         
            
        } catch (SQLException e) {
                logger.error("[ORDER] ERROR READING ORDERS: " + e.getMessage(), e); // LOG ERROR
         
            } // END TRY
 
        return orders; // RETURN LIST OF ORDERS
    } // END FUNCTION

// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // FUNCTION TO READ ORDERS BY DRINK ID
    public static List<Order> getOrdersByDrink(String drinkID) {
         List<Order> orders = new ArrayList<>(); // INITIALIZE LIST FOR ORDERS
         String orderSelectSQL = "SELECT od.orderID, orderDate, status, orderTotal FROM orderdetails AS od " +
                                 " INNER JOIN orders AS o ON od.orderID = o.orderID " +
                                 " WHERE od.drinkID = '" + drinkID + "'"; // SQL QUERY FOR SELECTING ORDERS BY DRINK ID
 
        try {
             Statement stat = connection.createStatement(); // CREATE STATEMENT
             ResultSet rs = stat.executeQuery(orderSelectSQL); // EXECUTE QUERY
 
            while (rs.next()) { // LOOP THROUGH RESULT SET
                 // CREATE ORDER OBJECT
                 Order order = new Order();
                 order.setOrderID(rs.getInt("orderID")); // SET ORDER ID
                 order.setOrderDate(Date.convertFromDatetimeString(rs.getString("orderDate"))); // CONVERT STRING TO DATE
                 order.setStatus(Order.OrderStatus.valueOf(rs.getString("status"))); // SET ORDER STATUS
                 order.setOrderTotal(rs.getDouble("orderTotal")); // SET ORDER TOTAL
 
                 // RETRIEVE ORDER DETAILS FOR THIS ORDER
                 List<OrderDetails> orderDetailsList = new ArrayList<>(); // INITIALIZE LIST FOR ORDER DETAILS
                 String orderDetailsSelectSQL = "SELECT orderDetailsID, drinkID, quantity, totalCost FROM orderDetails WHERE orderID = ?"; // SQL QUERY FOR SELECTING ORDER DETAILS
 
                 try (PreparedStatement detailsStatement = connection.prepareStatement(orderDetailsSelectSQL)) {
                     detailsStatement.setInt(1, order.getOrderID()); // SET ORDER ID IN QUERY
                     ResultSet detailsResultSet = detailsStatement.executeQuery(); // EXECUTE QUERY FOR ORDER DETAILS
 
                     while (detailsResultSet.next()) { // LOOP THROUGH RESULT SET
                         OrderDetails details = new OrderDetails(); // CREATE ORDER DETAILS OBJECT
                         details.setOrderDetailsID(detailsResultSet.getInt("orderDetailsID")); // SET ORDER DETAILS ID
                         details.setOrderID(order.getOrderID()); // SET ORDER ID
                         details.setDrinkID(detailsResultSet.getString("drinkID")); // SET DRINK ID
                         details.setQuantity(detailsResultSet.getInt("quantity")); // SET QUANTITY
                         details.setDetailCost(detailsResultSet.getDouble("totalCost")); // SET TOTAL COST
                         orderDetailsList.add(details); // ADD TO LIST
                    } // END WHILE
                
                } // END TRY
 
                 order.setOrderDetailsList(orderDetailsList); // SET ORDER DETAILS LIST
                 orders.add(order); // ADD ORDER TO LIST
            
                } // END WHILE
         
        } catch (SQLException e) {
            logger.error("[ORDER] ERROR READING ORDERS BY DRINK ID: " + e.getMessage() ); // LOG ERROR
        } // END TRY
 
         return orders; // RETURN LIST OF ORDERS
     } // END FUNCTION
 
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
     // FUNCTION TO SELECT ORDERS BY DATE
     public static List<Order> selectOrdersByDate(int year, int month, int day) {
         List<Order> orders = new ArrayList<>(); // INITIALIZE LIST FOR ORDERS
         String sqlQuery = "SELECT * FROM Orders WHERE YEAR(orderDate) = ? AND MONTH(orderDate) = ? AND DAY(orderDate) = ?"; // SQL QUERY FOR SELECTING ORDERS BY DATE
 
         try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
             statement.setInt(1, year); // SET YEAR IN QUERY
             statement.setInt(2, month); // SET MONTH IN QUERY
             statement.setInt(3, day); // SET DAY IN QUERY
 
             ResultSet resultSet = statement.executeQuery(); // EXECUTE QUERY
             while (resultSet.next()) { // LOOP THROUGH RESULT SET
                 int orderId = resultSet.getInt("orderID"); // GET ORDER ID
                 String orderDateStr = resultSet.getString("orderDate"); // GET ORDER DATE AS STRING
                 Order.OrderStatus status = Order.OrderStatus.valueOf(resultSet.getString("status")); // GET ORDER STATUS
                 double orderTotal = resultSet.getDouble("orderTotal"); // GET ORDER TOTAL
 
                 // CONVERT orderDateStr TO CUSTOM DATE CLASS INSTANCE
                 Date orderDate = Date.convertFromDatetimeString(orderDateStr);
 
                 // CREATE ORDER OBJECT AND ADD TO THE LIST
                 Order order = new Order(orderId, orderDate, status, orderTotal); // CREATE ORDER OBJECT
                 order.setOrderDetailsList(getOrderDetails(orderId)); // POPULATE ORDER DETAILS
                 orders.add(order); // ADD ORDER TO LIST
             } // END WHILE
         
            } catch (SQLException e) {
             e.printStackTrace(); // PRINT STACK TRACE FOR EXCEPTION
             logger.error("[ORDER] ERROR READING ORDERS BY DATE: " + e.getMessage()); // LOG ERROR
         } // END TRY
 
         return orders; // RETURN LIST OF ORDERS
     } // END FUNCTION
 
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
     // FUNCTION TO SELECT ORDERS BETWEEN TWO DATES
     public static List<Order> selectOrdersBetweenDates(Date startDate, Date endDate) {
         List<Order> orders = new ArrayList<>(); // INITIALIZE LIST FOR ORDERS
         String sqlQuery = "SELECT * FROM Orders WHERE orderDate BETWEEN ? AND ?"; // SQL QUERY FOR SELECTING ORDERS BETWEEN DATES
 
         try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
             statement.setString(1, Date.convertToDateTimeString(startDate)); // SET START DATE IN QUERY
             statement.setString(2, Date.convertToDateTimeString(endDate)); // SET END DATE IN QUERY
 
             ResultSet resultSet = statement.executeQuery(); // EXECUTE QUERY
             while (resultSet.next()) { // LOOP THROUGH RESULT SET
                 int orderId = resultSet.getInt("orderID"); // GET ORDER ID
                 String orderDateStr = resultSet.getString("orderDate"); // GET ORDER DATE AS STRING
                 Order.OrderStatus status = Order.OrderStatus.valueOf(resultSet.getString("status")); // GET ORDER STATUS
                 double orderTotal = resultSet.getDouble("orderTotal"); // GET ORDER TOTAL
 
                 // CONVERT orderDateStr TO CUSTOM DATE CLASS INSTANCE
                 Date orderDate = Date.convertFromDatetimeString(orderDateStr);
 
                 // CREATE ORDER OBJECT AND ADD TO THE LIST
                 Order order = new Order(orderId, orderDate, status, orderTotal); // CREATE ORDER OBJECT
                 order.setOrderDetailsList(getOrderDetails(orderId)); // POPULATE ORDER DETAILS
                 orders.add(order); // ADD ORDER TO LIST
             } // END WHILE
         } catch (SQLException e) {
             e.printStackTrace(); // PRINT STACK TRACE FOR EXCEPTION
             logger.error("[ORDER] ERROR READING ORDERS BETWEEN DATES: " + e.getMessage()); // LOG ERROR
         } // END TRY
 
         return orders; // RETURN LIST OF ORDERS
     } // END FUNCTION

// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Helper method to retrieve order details for a specific order ID.
    *
    * @param orderId the ID of the order to retrieve details for
    * @return a list of OrderDetails objects associated with the given order ID
    */
    private static List<OrderDetails> getOrderDetails(int orderId) {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        String sqlQuery = "SELECT * FROM OrderDetails WHERE orderID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, orderId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int orderDetailsID = resultSet.getInt("orderDetailsID");
                String drinkID = resultSet.getString("drinkID");
                int quantity = resultSet.getInt("quantity");
                double detailCost = resultSet.getDouble("totalCost");

                // Create OrderDetails object and add to the list
                OrderDetails orderDetail = new OrderDetails(orderId, orderDetailsID, drinkID, quantity, detailCost);
                orderDetailsList.add(orderDetail);
            } // END WHILE
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("[ORDER] ERROR READING ORDER DETAILS: " + e.getMessage());
        } // END TRY

        return orderDetailsList;
    }  // END FUNCTION
} // END CLASS
