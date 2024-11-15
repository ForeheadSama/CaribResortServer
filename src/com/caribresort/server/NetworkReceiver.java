package com.caribresort.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
                //Assuming payload is an Order object
                Order order = (Order) request.getData();
                boolean isSuccess = InsertOrder.insertOrder(order);

                if (isSuccess) {
                    return new Response(true, "[Server] NEW ORDER PROCESSED", null);
                } else {
                    return new Response(false, "[Server] FAILED TO PROCESS NEW ORDER: ", null);
                }

            // Handle other cases like "add_drink", "modify_drink", etc.
            // case "ADD_DRINK": return new Response(true, "[Server] NEW ORDER PROCESSED");
            // case "REMOVE_DRINK": return new Response(true, "[Server] NEW DRINK REMOVED");
            // case "MODIFY_DRINK": return new Response(true, "[Server] NEW DRINK MODIFIED");
            // case "REMOVE_ORDER": return new Response(true, "[Server] NEW ORDER REMOVED");
            // case "MODIFY_ORDER": return new Response(true, "[Server] NEW ORDER MODIFIED");
            
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
