/**
     * DRINK ORDER CONTROLLER CLASS
     * 
     * THIS CLASS HANDLES THE LOGIC FOR PLACING DRINK ORDERS AT CARIB RESORT. IT MANAGES DRINK INVENTORY, 
     * GENERATES UNIQUE ORDER AND SEAT NUMBERS, CALCULATES ORDER TOTALS, AND INTERACTS WITH THE SERVER-SIDE 
     * FUNCTIONS TO LOAD DRINK DATA AND UPDATE INVENTORY AFTER AN ORDER. THE CLASS ALSO HANDLES VALIDATION 
     * FOR DRINK AVAILABILITY AND AGE RESTRICTIONS BASED ON ARM-BAND COLOR.
     * 
     * AUTHOR: BRITNEY VASSELL
 */

/* ------------------ !!!!!!!!!!! ------------------ 
 * FIX:
    * 1. ORDER TOTAL NOT PASSING TO DB
    * 2. ORDER DETAILS NOT SAVING TO DB
    * 3. TIME STAMP BEING PASSED TO DB 3 HOURS AHEAD
  -------------------------------------------------- 
  */
package com.caribresort.client.controller;

// Import necessary Java Swing and AWT libraries for GUI components
import com.caribresort.client.model.Order; 
import com.caribresort.client.model.OrderDetails;
import com.caribresort.client.model.Drink;
import com.caribresort.server.ManagerFunctions.DrinkManagement;
import com.caribresort.server.ManagerFunctions.OrderManagement.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DrinkOrderController {
    
    // STORES USED SEAT NUMBERS TO ENSURE EACH SEAT NUMBER IS UNIQUE
    private static Set<Integer> usedSeatNumbers = new HashSet<>();
    
    // MAP TO STORE DRINK NAMES AND THEIR PRICES
    private static Map<String, Double> drinks = new HashMap<>();
    
    // MAP TO STORE DRINK OBJECTS RETRIEVED FROM DATABASE
    private static Map<String, Drink> drinksList = new HashMap<>();
    
    // MAP TO STORE DRINK NAMES AND THEIR AVAILABLE QUANTITIES
    private static Map<String, Integer> drinkQuantities = new HashMap<>();
    
    // LOGGER OBJECT FOR LOGGING EVENTS AND ERRORS
    private static final Logger logger = LogManager.getLogger(DrinkOrderController.class);
    
    // RANDOM OBJECT FOR GENERATING RANDOM ORDER AND SEAT NUMBERS
    private static final Random random = new Random();

    /**
     * GETS A SPECIFIC DRINK BY ITS ID.
     */
    public static Drink getDrink(String drinkID) {
        return drinksList.get(drinkID); // RETURN DRINK OBJECT FROM MAP BASED ON ID
    }

    /**
     * RETURNS A MAP OF DRINK NAMES TO THEIR PRICES.
     */
    public static Map<String, Double> getDrinks() {
        return drinks;
    }

    /**
     * GETS THE LIST OF DRINKS BASED ON AGE RESTRICTION. IF THE USER IS UNDERAGE, FILTERS OUT ALCOHOLIC DRINKS.
     */
    public static Map<String, Drink> getDrinkList(Boolean isUnderAge) {
        if (drinksList.isEmpty()) loadDrinksFromDatabase(); // LOAD DRINKS FROM DATABASE IF NOT ALREADY LOADED
        
        // CREATE A TEMPORARY MAP TO STORE DRINKS BASED ON AGE RESTRICTION
        if (isUnderAge) {
            Map<String, Drink> tempDrinks = new HashMap<>();
            
            // FILTER OUT ALCOHOLIC DRINKS FOR UNDERAGE USERS
            for (Drink drink : drinksList.values()) {
                if (!drink.getIsAlcoholic()) {
                    tempDrinks.put(drink.getDrinkID(), drink); // ADD NON-ALCOHOLIC DRINKS TO TEMP MAP
                } // END IF

            } // END FOR LOOP

            return tempDrinks; // RETURN FILTERED DRINK LIST FOR UNDERAGE USERS

        } else {
            return drinksList; // RETURN COMPLETE DRINK LIST IF USER IS NOT UNDERAGE
        } // END IF-ELSE
    } // END METHOD

    /**
     * LOADS DRINK DATA FROM THE DATABASE INTO THE DRINKS LIST MAP.
     */
    public static void loadDrinksFromDatabase() {
        drinksList.clear(); // CLEAR THE DRINKS LIST MAP BEFORE LOADING NEW DATA
        List<Drink> tempDrinks = DrinkManagement.getAllDrinks(); // GET ALL DRINKS FROM DATABASE

        // POPULATE THE DRINKS LIST MAP WITH DRINK OBJECTS
        for (Drink drink : tempDrinks) {
            drinksList.put(drink.getDrinkID(), drink); // ADD DRINK OBJECT TO MAP
        } // END FOR LOOP
    } // END METHOD

    /**
     * LOADS A MAP OF DRINK PRICES BASED ON AGE RESTRICTION AND POPULATES QUANTITIES.
     */
    public static Map<String, Double> loadDrinksPriceMap(boolean isUnderAge) {
        drinks.clear(); // CLEAR THE DRINKS MAP BEFORE LOADING NEW DATA
        drinkQuantities.clear(); // CLEAR THE DRINK QUANTITIES MAP BEFORE LOADING NEW DATA

        if (drinksList.isEmpty()) loadDrinksFromDatabase(); // LOAD DRINKS FROM DATABASE IF NOT ALREADY LOADED

        // POPULATE THE DRINKS MAP WITH DRINK NAMES AND PRICES
        for (Drink drink : drinksList.values()) {
            if (isUnderAge && drink.getIsAlcoholic()) continue; // SKIP ALCOHOLIC DRINKS IF USER IS UNDERAGE
            
            drinks.put(drink.getDrinkName(), drink.getUnitPrice()); // ADD DRINK NAME AND PRICE TO MAP
            drinkQuantities.put(drink.getDrinkName(), drink.getQuantity()); // ADD DRINK NAME AND QUANTITY TO MAP
        } // END FOR LOOP

        return drinks; // RETURN MAP OF DRINKS AND PRICES
    } // END METHOD

    /**
     * CHECKS IF THE ORDERED DRINK QUANTITIES ARE AVAILABLE IN INVENTORY.
     */
    public static Map<String, String> checkOrderAvailability(Map<String, Integer> orderQuantities) {
        if (orderQuantities.isEmpty()) {
            Map<String, String> errorMap = new HashMap<>(); // CREATE A MAP TO STORE ERROR MESSAGES
            errorMap.put("ERROR", "Please select at least one drink."); // ADD ERROR MESSAGE TO MAP
            return errorMap; // RETURN ERROR MAP
        } // END IF

        // DELEGATE AVAILABILITY CHECK TO SERVER-SIDE FUNCTION
        return DrinkManagement.checkDrinkAvailability(orderQuantities);
    } // END METHOD

    /**
     * CALCULATES THE TOTAL COST OF AN ORDER BASED ON DRINK PRICES AND QUANTITIES.
     */
    public static double calculateTotal(Map<String, Integer> orderQuantities) {
        double totalCost = 0.0; // INITIALIZE TOTAL COST TO ZERO

        // ITERATE THROUGH ORDERED DRINKS AND CALCULATE TOTAL COST
        for (Map.Entry<String, Integer> entry : orderQuantities.entrySet()) {
            
            String drinkName = entry.getKey(); // GET DRINK NAME
            int quantity = entry.getValue(); // GET DRINK QUANTITY
            Double price = drinks.get(drinkName); // GET DRINK PRICE FROM MAP

            // ADD PRICE * QUANTITY TO TOTAL COST
            if (price != null) {
                totalCost += price * quantity;
            } // END IF
        } // END FOR LOOP
        return totalCost; // RETURN TOTAL COST
    } // END METHOD

    /**
     * GENERATES A UNIQUE ORDER ID NOT ALREADY IN USE.
     */
    private static int generateUniqueOrderId() {
        int orderId; // VARIABLE TO STORE GENERATED ORDER ID
        boolean isUnique = false; // FLAG TO CHECK IF ORDER ID IS UNIQUE
        
        // GENERATE RANDOM ORDER ID AND CHECK IF IT IS UNIQUE
        do {
            orderId = random.nextInt(9999) + 1; // GENERATE NUMBER BETWEEN 1 AND 9999
            
            try {
                // CHECK IF ORDER ID ALREADY EXISTS IN DATABASE
                isUnique = !ReadOrder.checkOrderIdExists(orderId);

            } catch (Exception e) {
                logger.error("[ORDER] Error checking order ID uniqueness: " + e.getMessage());
                continue; // CONTINUE GENERATING IF CHECK FAILS
            } // END TRY-CATCH
        } while (!isUnique); // REPEAT UNTIL UNIQUE ORDER ID IS GENERATED
        
        return orderId; // RETURN UNIQUE ORDER ID
    } // END METHOD

    /**
     * PROCESSES A NEW DRINK ORDER, UPDATES INVENTORY, AND RETURNS THE ORDER RESULT.
     */
    public static OrderResult processOrder(Map<String, Integer> orderQuantities, double totalCost) {
        OrderResult result = new OrderResult(); // CREATE NEW ORDER RESULT OBJECT
        int orderId = generateUniqueOrderId(); // GENERATE UNIQUE ORDER ID
        Order order = new Order(orderId); // CREATE NEW ORDER OBJECT

        StringBuilder orderSummary = new StringBuilder("ORDER CONFIRMED!\n\nDETAILS:\n"); // INITIALIZE ORDER SUMMARY

        // ITERATE THROUGH ORDERED DRINKS AND UPDATE INVENTORY
        for (Map.Entry<String, Integer> entry : orderQuantities.entrySet()) {
            String drinkName = entry.getKey(); // GET DRINK NAME
            int quantity = entry.getValue(); // GET DRINK QUANTITY
            Double unitPrice = drinks.get(drinkName); // GET DRINK PRICE FROM MAP

            orderSummary.append("- ")
                        .append(drinkName)
                        .append(": ")
                        .append(quantity)
                        .append("\n"); // ADD DRINK TO ORDER SUMMARY
            
                        order.createOrderDetails(drinkName, quantity, unitPrice); // CREATE ORDER DETAILS OBJECT
                        //order.addOrderDetail(new OrderDetails(orderId, 123, drinkName, quantity, unitPrice)); // STATIC GUEST ID USED
        } // END FOR LOOP

        // ADD ORDER DETAILS TO DATABASE AND UPDATE ORDER SUMMARY
        orderSummary.append("TOTAL: $").append(String.format("%.2f", totalCost));
        orderSummary.append("\n\nYour Seat Number is: #").append(generateUniqueSeatNumber());

        // UPDATE DRINK INVENTORY
        boolean updateSuccess = DrinkManagement.updateDrinkQuantities(orderQuantities);

        // CHECK IF INVENTORY UPDATE WAS SUCCESSFUL
        if (!updateSuccess) {
            result.setSuccess(false); // SET SUCCESS FLAG TO FALSE
            result.setErrorMessage("Error updating inventory. Please try again."); // SET ERROR MESSAGE
            
            logger.error("[ORDER] ERROR UPDATING INVENTORY FOR ORDER [" + orderId + "]"); // LOG ERROR
            
            return result; // RETURN RESULT
        } 
           
        // Save order to database
        boolean saveSuccess = InsertOrder.insertOrder(order); // INSERT ORDER INTO DATABASE

        if (!saveSuccess) {
            result.setSuccess(false);
            result.setErrorMessage("Error saving order. Please try again.");
            
            logger.error("[ORDER] ERROR SAVING ORDER TO DATABASE [" + order.getOrderID() + "]");
            
            return result;
        }
        

        // If everything succeeded
        logger.info("[ORDER] NEW ORDER RECEIVED ID[" + order.getOrderID() + "]");
        result.setSuccess(true);
        result.setOrderSummary(orderSummary.toString());
        return result;
    
    } // END METHOD

    /**
     * GENERATES A UNIQUE SEAT NUMBER BETWEEN 1 AND 100.
     */
    private static int generateUniqueSeatNumber() {
        int seatNumber; // VARIABLE TO STORE GENERATED SEAT NUMBER

        // GENERATE RANDOM SEAT NUMBER AND ENSURE IT IS UNIQUE
        do {
            seatNumber = random.nextInt(100) + 1; // GENERATE NUMBER BETWEEN 1 AND 100
        } while (usedSeatNumbers.contains(seatNumber)); // ENSURE SEAT NUMBER IS UNIQUE
        
        usedSeatNumbers.add(seatNumber); // ADD SEAT NUMBER TO USED SET
        return seatNumber; // RETURN UNIQUE SEAT NUMBER
    } // END METHOD

    /**
     * CLEARS THE SET OF USED SEAT NUMBERS.
     */
    public static void clearUsedSeatNumbers() {
        usedSeatNumbers.clear(); 
    }

    // INNER CLASS TO HANDLE ORDER RESULTS
    public static class OrderResult {
        private boolean success; 
        private String errorMessage;
        private String orderSummary;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public String getOrderSummary() { return orderSummary; }
        public void setOrderSummary(String orderSummary) { this.orderSummary = orderSummary; }
    } // END INNER CLASS
} // END CLASS
