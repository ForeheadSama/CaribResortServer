/**
    * COMPLETED BY: VASSELL
 */

 // Package declaration
package com.caribresort.client.model;

// ---------- ORDER DETAILS CLASS
public class OrderDetails {
    private int orderID;  // Reference to Order object
    private int orderDetailsID; // Unique ID for OrderDetails
    private String drinkID;  // Reference to Drink object
    private int  quantity;  // Quantity of the drink
    private double detailCost; // Cost of all the drink

    // Default Constructor
    public OrderDetails() {
        this.orderID = 0;
        this.orderDetailsID = 0;
        this.drinkID = "";
        this.quantity = 0;  // Default quantity
        this.detailCost = 0.0;
    }

    // Constructor with Parameters
    public OrderDetails(int orderID, int orderDetailsID, String drinkID, int quantity, double detailCost) {
        this.orderID = orderID;
        this.orderDetailsID = orderDetailsID;
        this.drinkID = drinkID;
        this.quantity = quantity;
        this.detailCost = detailCost;
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderDetailsID() {
        return orderDetailsID;
    }

    public void setOrderDetailsID(int orderDetailsID) {
        this.orderDetailsID = orderDetailsID;
    }

    public String getDrinkID() {
        return drinkID;
    }

    public void setDrinkID(String drinkID) {
        this.drinkID = drinkID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDetailCost() {
        return detailCost;
    }

    public void setDetailCost(double detailCost) {
        this.detailCost = detailCost;
    }
    public double calculateDetailCost(double unitPrice) {
        return unitPrice * quantity;
    }

    // Updated toString method
    @Override
    public String toString() {
        return "OrderDetails {" +
               "\n  Order ID: " + orderID +
               "\n  Order Details ID: " + orderDetailsID +
               "\n  Drink ID: " + drinkID +
               "\n  Quantity: " + quantity +
               "\n  Detail Cost: $" + String.format("%.2f", detailCost) +
               "\n}";
    } // End toString
} // End OrderDetails class
