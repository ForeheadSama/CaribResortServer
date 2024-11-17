package com.caribresort.server;

import java.io.*;
import java.net.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caribresort.client.model.Date;
import com.caribresort.database.DBConnector;

public class NetworkConnector {
    private static final Logger logger = LogManager.getLogger(NetworkConnector.class);

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static int clientCount = 0; 

    // Method to set up server socket
    public static void createServerSocket(int port) {
        try {
            serverSocket = new ServerSocket(port);      
            Boolean connectionStatus = false; 

             //connect to db once server loads up
            if (DBConnector.getDatabaseConnection() != null){
                connectionStatus = true;
                logger.info("[DATABASE] DATABASE CONNECTION ESTABLISHED");
             }

            while (true) {

                try {

                    clientSocket = serverSocket.accept();
                    clientCount++;
    
                    NetworkReceiver clientHandler = new NetworkReceiver(clientSocket);
                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();

                    //PRINT FOR NEW CONNECTED CLIENT
					logger.info("[" + new Date().toString() + "] CONNECTED NEW CLIENT #" + clientCount); //print client count

                    if (connectionStatus){
                        clientHandler.sendClient(new Response(true, "[SERVER] CONNECTED TO DATABASE", null));
                    }else{
                        clientHandler.sendClient(new Response(false, "[SERVER] COULD NOT CONNECT TO DATABASE", null));
                    }
                    
                } catch (IOException e) {

                    logger.error("[NetworkConnector] Error accepting client connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            logger.error("[NetworkConnector] Error creating server socket: " + e.getMessage());
        }
    }


    // Method to close connections
    public void closeConnections() {
        try {
            // if (input != null) input.close();
            // if (output != null) output.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
            logger.info("[NetworkConnector] Connections closed");
        } catch (IOException e) {
            logger.error("[NetworkConnector] Error closing connections: " + e.getMessage());
        }
    }

    public static void main(String[] args){
        NetworkConnector.createServerSocket(8080);
    }
}
