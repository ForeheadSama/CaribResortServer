package com.caribresort.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caribresort.client.model.Drink;
import com.caribresort.client.model.Order;
import com.caribresort.database.ManagerFunctions.DrinkManagement;
import com.caribresort.database.ManagerFunctions.OrderManagement.*; // Add this import statement

public class NetworkReceiver implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private static final Logger logger = LogManager.getLogger(NetworkReceiver.class);


    public NetworkReceiver(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.getStream();
    }

    public void getStream() {
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error getting streams: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request) { 
        switch (request.getAction().toString()) {
            
            case "NEW_ORDER":        
                Order orderObject = (Order) request.getData();
                boolean isSuccess = InsertOrder.insertOrder(orderObject);

                if (isSuccess) {
                    return new Response(true, "[SERVER] NEW ORDER PROCESSED", null);
                } else {
                    return new Response(false, "[SERVER] FAILED TO PROCESS NEW ORDER: ", null);
                }

            case "REMOVE_ORDER": 
                int orderId1 = (Integer) request.getData();
                boolean isRemoved = DeleteOrder.deleteOrder(orderId1);

                if (isRemoved) {
                    return new Response(true, "[SERVER] ORDER WAS REMOVED", null);
                } else {
                    return new Response(false, "[SERVER] FAILED TO REMOVED ORDER: ", null);
                }

            case "MODIFY_ORDER": 
                Order modifyOrderObject = (Order) request.getData();
                boolean modifySuccess = UpdateOrder.updateOrder(modifyOrderObject);

                if (modifySuccess) {
                    return new Response(true, "[SERVER] NEW ORDER PROCESSED", null);
                } else {
                    return new Response(false, "[SERVER] FAILED TO PROCESS NEW ORDER: ", null);
                }
            case "PULL_ORDER":
                int orderid = (Integer) request.getData();
                var order = ReadOrder.readOrder(orderid);

                if (order != null) {
                    return new Response(true, "[SERVER] NEW ORDER PROCESSED", order);
                } else {
                    return new Response(false, "[SERVER] FAILED TO PROCESS NEW ORDER: ", null);
                }
            case "PULL_ORDERS":
                var orders = ReadOrder.readAllOrders();

                if (orders != null) {
                    return new Response(true, "[SERVER] NEW ORDER PROCESSED", orders);
                } else {
                    return new Response(false, "[SERVER] FAILED TO PROCESS NEW ORDER: ", null);
                }
            case "PULL_ORDERS_BY_Date":
                var orders_by_date = ReadOrder.readAllOrders();

                if (orders_by_date != null) {
                    return new Response(true, "[SERVER] NEW ORDER PROCESSED", orders_by_date);
                } else {
                    return new Response(false, "[SERVER] FAILED TO PROCESS NEW ORDER: ", null);
                }
            case "PULL_ORDERS_BY_Drink":
                var orders_by_drink = ReadOrder.readAllOrders();

                if (orders_by_drink != null) {
                    return new Response(true, "[SERVER] NEW ORDER PROCESSED", orders_by_drink);
                } else {
                    return new Response(false, "[SERVER] FAILED TO PROCESS NEW ORDER: ", null);
                }
            case "ADD_DRINK": 
                Drink drinkObject = (Drink) request.getData();
                boolean isDrinkSuccess = DrinkManagement.insertDrink(drinkObject);

                if(isDrinkSuccess){
                    return new Response(true, "[SERVER] NEW DRINK ADDED", null);
                }else{
                    return new Response(false, "[SERVER] FAILED TO ADD NEW DRINK", null);
                }

            case "REMOVE_DRINK": 
                var drinkId = (String) request.getData();
                boolean isDrinkRemoveSuccess = DrinkManagement.removeDrink(drinkId);

                if(isDrinkRemoveSuccess){
                    return new Response(true, "[SERVER] DRINK REMOVED FROM DATABASE", null);
                }else{
                    return new Response(false, "[SERVER] FAILED TO REMOVE DRINK FROM DATABASE", null);
                }

            case "MODIFY_DRINK": 
                Drink drinkObjectModify = (Drink) request.getData();
                boolean isDrinkModifySuccess = DrinkManagement.updateDrink(drinkObjectModify);

                if(isDrinkModifySuccess){
                    return new Response(true, "[SERVER] DRINK MODIFIED SUCCESSFULLY", null);
                }else{
                    return new Response(false, "[SERVER] FAILED TO MODIFY DRINK", null);
                }

            case "PULL_DRINKS": 
                var drinks = DrinkManagement.getAllDrinks();
                
                if (drinks.isEmpty()) {
                    logger.info("[SERVER] FAILED TO PULL DRINKS OR NO DRINKS AVAILABLE.");
                    return new Response(false, "[Server] FAILED TO PULL DRINKS OR NO DRINKS AVAILABLE.", null);
                    
                }else{
                    logger.info("[SERVER] ALL DRINKS RECEIVED.");
                    return new Response(true, "[Server] NEW DRINK PULLED", drinks); 
                }    
            
            // case "PULL_ORDERS": return new Response(true, "[Server] NEW ORDER PULLED");
            case "GENERATE_ORDER_ID":
                int orderId = ReadOrder.generateUniqueOrderId();

                if(orderId != 0){
                    return new Response(true, "[SERVER] NEW ORDER ID GENERATED ", orderId);
                }
                else {
                    return new Response(false, "[SERVER] FAILED TO PULL ORDER ID OR NO ORDER ID AVAILABLE", null);
                }

            case "PULL_DRINK_BY_NAME":
                String drinkName = (String) request.getData();
                String drink = DrinkManagement.getDrinkIDByName(drinkName);

                if(drink != null){
                    return new Response(true, "[SERVER] NEW ORDER DETAIL ID GENERATED FOR DRINK " + drink, drink);
                }
                else {
                    return new Response(false, "[SERVER] FAILED TO PULL DRINK NAME OR NO DRINK NAME AVAILABLE", null);
                }
                // hey :D
            default:
                return new Response(false, "[Server] UNKNOWN ACTION", null);
        
        }// END SWITCH
   
    } // END HANDLE REQUEST

    public void sendClient(Response response) {
        try {
            output.writeObject(response);
        }catch(IOException e){
            System.err.println("Error sending response: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while(true){
            try{
            // Read request from the client
            Request request = (Request) input.readObject();

            Response response = handleRequest(request);
            
            // Send the response back to the client
            output.writeObject(response);

            } catch (ClassNotFoundException e) {
				System.out.println("[CLASSNOTFOUND]: ");
				e.printStackTrace();
				
			} catch(SocketException e) {
				break;
					
			} catch(EOFException e) {
				break;
					
			} catch (IOException e) {
				System.out.println("[IOEXCEPTION]: ");
				e.printStackTrace();
			} //ends try/catch
        }
    }
} // END CLASS
