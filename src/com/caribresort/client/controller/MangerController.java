package com.caribresort.client.controller;

import java.util.List;
import java.util.Map;

import com.caribresort.client.model.Date;
import com.caribresort.client.model.Drink;
import com.caribresort.client.model.Order;
import com.caribresort.client.model.Order.OrderStatus;
import com.caribresort.client.model.OrderDetails;
import com.caribresort.server.ManagerFunctions.*;
import com.caribresort.server.ManagerFunctions.OrderManagement.*;


public class MangerController {
    DrinkManagement drinkManagement;
    private static InsertOrder insert;
    private static ReadOrder read;
    private static UpdateOrder update;
    private static DeleteOrder delete;

    public MangerController() {
        insert = new InsertOrder();
        read = new ReadOrder();
        update = new UpdateOrder();
        delete = new DeleteOrder();
        // drinkManagement = new DrinkManagement();
        // orderManagement = new OrderManagement();
    }

    //Add A drink to the Database
    public static Boolean addDrink(String drinkID, String drinkName, double unitPrice, Boolean isAlcoholic, int quantity) {
        // Implement logic to add drink to the database
        // Return true if successful, false otherwise

        Drink drink = new Drink(drinkID, drinkName, unitPrice, isAlcoholic, quantity);

        Boolean isSuccess = DrinkManagement.insertDrink(drink);
        if (isSuccess) DrinkOrderController.loadDrinksFromDatabase(); // refresh the drinkList so it has the new drink
        return isSuccess; // success
    }

    //Update a drink's details in the Database
    public static Boolean updateDrink(String drinkID, String drinkName, double unitPrice, Boolean isAlcoholic, int quantity) {
        // Implement logic to update drink details in the database
        // Return true if successful, false otherwise
        Drink drink = new Drink(drinkID, drinkName, unitPrice, isAlcoholic, quantity);   
        Boolean isSuccess = DrinkManagement.updateDrink(drink);
        if (isSuccess) DrinkOrderController.loadDrinksFromDatabase(); // refresh the drinkList so it has the new drink
        return isSuccess; // success
    }

    //Delete a drink from the Database
    public static Boolean deleteDrink(String drinkID) {
        // Implement logic to delete drink from the database
        // Return true if successful, false otherwise
        Boolean isSuccess = DrinkManagement.removeDrink(drinkID);
        if (isSuccess) DrinkOrderController.loadDrinksFromDatabase(); // refresh the drinkList so it has the new drink
        return isSuccess; // return if successful and not successful
    }

    //pull a drink from the database
    public static Drink getDrink(String drinkID) {
        // Implement logic to pull drink from the database
        // Return the drink object if found, null otherwise
        try {
            return DrinkManagement.getADrink(drinkID);
        } catch (Exception e) {
            // TODO: handle exception
            return null; // error
        }
    }

    //pull a drink from the database
    public static Map<String, Drink> getDrinks(Boolean isUnderAge) {
        // Implement logic to pull drink from the database
        // Return the drink object if found, null otherwise
        try {
            return DrinkOrderController.getDrinkList(isUnderAge);
        } catch (Exception e) {
            // TODO: handle exception
            return null; // error
        }
    }

    ///////////////////////////////////////////////////////////Orders////////////////////////////////////////////////////////////////////////////

    //Add A drink to the Database
    public static Boolean addOrder(int orderID, Date orderDate, OrderStatus status, List<OrderDetails> orderDetailsList, double orderTotal) {
        // Implement logic to add drink to the database
        // Return true if successful, false otherwise
        Order order;

        if (orderDetailsList != null) {
            order = new Order(orderID, orderDate, status, orderDetailsList, orderTotal);
        }else{
            order = new Order(orderID, orderDate, status, orderTotal);
        }
    
        return insert.insertOrder(order); // success
    }

    //Update a drink's details in the Database
    public static Boolean updateOrder(int orderID, Date orderDate, OrderStatus status, List<OrderDetails> orderDetailsList, double orderTotal) {
        // Implement logic to update drink details in the database
        // Return true if successful, false otherwise
        Order order;

        if (orderDetailsList != null) {
            order = new Order(orderID, orderDate, status, orderDetailsList, orderTotal);
        }else{
            order = new Order(orderID, orderDate, status, orderTotal);
        }

        return  update.updateOrder(order); // success
    }

    //Delete a drink from the Database
    public static Boolean deleteOrder(int orderID) {
        // Implement logic to delete drink from the database
        // Return true if successful, false otherwise
        return delete.deleteOrder(orderID); // success
    }

    //pull a drink from the database
     public static Order getOrder(int orderID) {
        // Implement logic to pull drink from the database
        // Return the drink object if found, null otherwise
        try {
            return read.readOrder(orderID); // error
        } catch (Exception e) {
            // TODO: handle exception
            return null; // error
        }
    }

    //pull a drink from the database
    public static List<Order> getOrder(String drinkID, double quantity) {
        // Implement logic to pull drink from the database
        // Return the drink object if found, null otherwise
        try {
            return read.getOrdersByDrink(drinkID); // error
        } catch (Exception e) {
            // TODO: handle exception
            return null; // error
        }
    }


    //pull a Order from the database by the date of the order
    public static List<Order> getOrders(int year, int month, int day) {
        // Implement logic to pull drink from the database
        // Return the drink object if found, null otherwise
        try {
            return read.selectOrdersByDate(year, month, day);
        } catch (Exception e) {
            // TODO: handle exception
            return null; // error
        }
    }

    //pull a drink from the database
    public static List<Order> getOrders() {
        // Implement logic to pull drink from the database
        // Return the drink object if found, null otherwise
        try {
            return read.readAllOrders();
        } catch (Exception e) {
            // TODO: handle exception
            return null; // error
        }
    }
}
