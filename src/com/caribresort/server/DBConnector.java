/**
 *      * Creates the database connection used throughout the program
 * 
    * COMPLETED BY: EDWARDS
 */

package com.caribresort.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConnector {
    
    private static Connection connection = null;
    private static final Logger logger = LogManager.getLogger(DBConnector.class); // Logger object

    public static Connection getDatabaseConnection() {
        // Check if the connection is null
        if (connection == null) {
            try {
                // The database URL, username, and password
                String url = "jdbc:mysql://localhost:3306/caribresort"; 
                String username = "root";
                String password = "";

                // Initialize the connection
                connection = DriverManager.getConnection(url, username, password);

                // Log the successful connection
                logger.info("[DATABASE] CONNECTION SUCCESSFUL");
                
            } catch (SQLException e) {
                e.printStackTrace();
                // Log the failed connection
                logger.error("[DATABASE] CONNECTION FAILED");
            }
        }

        // Return the connection (either new or already existing)
        return connection;
    }
}