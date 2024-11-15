package com.caribresort.client.model;

import java.io.Serializable;

public class Drink implements Serializable{
    private static final long serialVersionUID = 1L;

    private String drinkID; // Drink ID
    private String drinkName; // Drink name
    private double unitPrice; // Unit price
    private Boolean isAlcoholic; // Alcoholic or non-alcoholic
    private int quantity; // Quantity

    // ---------- CONSTRUCTOR
    public Drink(String drinkID, String drinkName, double unitPrice, Boolean isAlcoholic, int quantity) {
        this.drinkID = drinkID;
        this.drinkName = drinkName;
        this.unitPrice = unitPrice;
        this.isAlcoholic = isAlcoholic;
        this.quantity = quantity;
    }

    // ---------- CONSTRUCTOR
    public Drink(){
        this.drinkID = "";
        this.drinkName = "";
        this.unitPrice = 0.0;
        this.isAlcoholic = false;
        this.quantity = 0;
    }

    // Getters and Setters
    public String getDrinkID() {
        return drinkID;
    }

    public void setDrinkID(String drinkID) {
        this.drinkID = drinkID;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getIsAlcoholic() {
        return isAlcoholic;
    }

    public void setIsAlcoholic(Boolean isAlcoholic) {
        this.isAlcoholic = isAlcoholic;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // toString method to display drink details
    @Override
    public String toString() {
        return "Drink ID: " + drinkID + "\nDrink Name: " + drinkName + 
               "\nUnit Price: $" + unitPrice + "\nAlcoholic: " + (isAlcoholic ? "Yes" : "No");
    } // End toString
}
