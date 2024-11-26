/**
    * COMPLETED BY: VASSELL & RHOOMS
 */

 // Package declaration
package com.caribresort.client.controller;

// Import the necessary classes
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

// Import the necessary classes
import com.caribresort.client.view.ManagerGUIs.ManagerDashboard;

// ---------- HANDLE LOGIN FUNCTIONALITY 
public class LoginController {
	
	//Create Logger object and use the LogManager to get and assign 
  	//the object a logger for the class it is logging for
  	private static final Logger logger = LogManager.getLogger(LoginController.class);

    // Validate the login details
    public static boolean validateLogin(String userName, String password) {
        
        //Login User: admin
        //Login Password: 59967
        
        if (userName.equals("admin") && password.equals("59967")) {
            return true;
        } else {
            return false;
        }
    }

    // ---------- HANDLE LOGIN FUNCTIONALITY 
    public void handleLogin(JTextField userName, JTextField password) {
        // Get the username and password from the text fields
        String strUserName = userName.getText();
        String strPassword = password.getText();

        // Check if the username or password is empty and show an error message
        if (strUserName.isEmpty() || strPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill out all fields.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            
        } else {
            // Validate the login details
            boolean isValid = validateLogin(strUserName, strPassword);

            // If login is successful, show the manager dashboard
            if (isValid) {
                JOptionPane.showMessageDialog(null, "Login successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
                logger.info("[ NEW LOGIN ]"); // Log the new login
                new ManagerDashboard(); // Open the manager dashboard
            
            } else {
                // If login is unsuccessful, show an error message
                JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            } // end if
        
        } // end if
    
    } // end handleLogin
} // end class LoginController
