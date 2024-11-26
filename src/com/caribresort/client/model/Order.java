/**
    * COMPLETED BY: VASSELL
 */

// Package declaration
 package com.caribresort.client.model;

 // Import necessary packages
import java.util.List;
import java.util.logging.Logger;

import com.caribresort.server.ManagerFunctions.DrinkManagement;

import java.util.ArrayList;

// ---------- ORDER CLASS
public class Order {
    private static final Logger logger = Logger.getLogger(Order.class.getName());
    private int orderID; // Order ID
    private Date orderDate; // Order date
    private OrderStatus status; // Order status
    private List<OrderDetails> orderDetailsList = new ArrayList<>(); // One-to-many relationship
    private double orderTotal; // Order total

    // Enum to represent the order status (successful, canceled, isPrepare, unknown)
    public static enum OrderStatus {successful, canceled, isPrepare, unknown,};


    // ---------- CONSTRUCTORS
    public Order() {
        this.orderID = 0;
        this.orderDate = new Date();
        this.status = OrderStatus.isPrepare;
        this.orderTotal = 0.0;
    }

    public Order(int orderID) {
        this.orderID = orderID;
        this.orderDate = new Date();
        this.status = OrderStatus.isPrepare;
        this.orderTotal = 0.0;
    }

    public Order(int orderID, Date orderDate, OrderStatus status, double orderTotal) {
        this.orderID = orderID;
        this.orderDate = orderDate == null ? new Date() : orderDate;
        this.status = status;
        this.orderTotal = orderTotal;
    }

    public Order(int orderID, Date orderDate, OrderStatus status, List<OrderDetails> orderDetailsList, double orderTotal) {
        this.orderID = orderID;
        this.orderDate = orderDate == null ? new Date() : orderDate;
        this.status = status;
        this.orderDetailsList = orderDetailsList;
        this.orderTotal = orderTotal;
        calculateOrderTotal();
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }

    public void setOrderDetailsList(List<OrderDetails> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

    public void addOrderDetail(OrderDetails orderDetails) {
        this.orderDetailsList.add(orderDetails);
        calculateOrderTotal();
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void calculateOrderTotal() {
        this.orderTotal = 0.0;
        for (OrderDetails orderDetails : orderDetailsList) {
            this.orderTotal += orderDetails.getDetailCost();
        }
    }

    // Methods to add, remove, update, and get order details
    public void removeOrderDetail(OrderDetails orderDetails) {
        this.orderDetailsList.remove(orderDetails);
    }
    public void removeOrderDetail(int index) {
        this.orderDetailsList.remove(index);
    }
    public void clearOrderDetails() {
        this.orderDetailsList.clear();
    }
    public OrderDetails getOrderDetail(int index) {
        return this.orderDetailsList.get(index);
    }
    public int getOrderDetailsCount() {
        return this.orderDetailsList.size();
    }
    public void updateOrderDetail(int index, OrderDetails orderDetails) {
        this.orderDetailsList.set(index, orderDetails);
    }

    public void updateOrderTotal() {
        calculateOrderTotal();
    }

    public void updateOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    // toString method to display order details
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();
        details.append("Order {")
               .append("\n  Order ID: ").append(orderID)
               .append(",\n  Date: ").append(orderDate)
               .append(",\n  Status: ").append(status)
               .append(",\n  Order Total: $").append(String.format("%.2f", orderTotal));
    
        // Adding order details
        if (!orderDetailsList.isEmpty()) {
            details.append(",\n  Order Details: [");
            for (OrderDetails detail : orderDetailsList) {
                details.append("\n    ").append(detail.toString()).append(",");
            }
            // Remove the last comma and close the bracket
            details.setLength(details.length() - 1); // Remove the last comma
            details.append("\n  ]");
        } else {
            details.append(",\n  Order Details: []");
        }
    
        details.append("\n}"); // Close the order details
        return details.toString(); // Return the order details
    } // End toString

    /**
     * Creates an OrderDetails object with the correct drink ID from the database
     * @param drinkName The name of the drink to look up
     * @param quantity The quantity ordered
     * @param unitPrice The price per unit
     * @return OrderDetails object if successful, null if drink not found
     */
    public OrderDetails createOrderDetails(String drinkName, int quantity, double unitPrice) {
        OrderDetails details = new OrderDetails();
        
        // Get the correct drinkID from the database using the drink name
        String drinkID = DrinkManagement.getDrinkIDByName(drinkName);
        
        if (drinkID != null) {
            details.setDrinkID(drinkID);
        } else {
            logger.severe("[DATABASE] FAILED TO FIND ID FOR DRINK: " + drinkName);
            return null;
        }
        
        details.setQuantity(quantity);
        details.setDetailCost(details.calculateDetailCost(unitPrice));
        
        return details;
    }

    /**
     * Creates an OrderDetails object and adds it to the order
     * @param drinkName The name of the drink to look up
     * @param quantity The quantity ordered
     * @param unitPrice The price per unit
     * @return true if successful, false if creation failed
     */
    public boolean addNewOrderDetails(String drinkName, int quantity, double unitPrice) {
        OrderDetails details = createOrderDetails(drinkName, quantity, unitPrice);
        
        if (details != null) {
            addOrderDetail(details);
            return true;
        }
        
        return false;
    }


} // End Order class
