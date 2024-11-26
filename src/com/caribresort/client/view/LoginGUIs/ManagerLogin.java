/**
     * Handles the manager login process
     * Validates credentials and provides access to the manager dashboard
 * COMPLETED BY: RHOOMS
 */

 // Package declaration
package com.caribresort.client.view.LoginGUIs;

// Import necessary classes and libraries
import com.caribresort.client.controller.LoginController;
import com.caribresort.client.view.Utilities.RoundedBorder;
import javax.swing.*;
import java.awt.*;

public class ManagerLogin extends JFrame {
    // Main panel to hold all components
    private JPanel MainPagePanel;
    private LoginController controller; // Reference to the login controller

    /**
     * Constructor: Initializes the manager login window
     */
    public ManagerLogin() {

        controller = new LoginController();  // Initialize the login controller

        // Configure the main window properties
        super("Manager Login");  // Set window title
        setSize(400, 450);  // Set window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close window behavior
        setLocationRelativeTo(null);  // Center window on screen
        getContentPane().setBackground(new Color(173, 216, 230));  // Set light blue background
        setLayout(null);  // Use absolute positioning

        createManagerLoginPanel();  // Initialize the main panel
    }

    /**
     * Creates and configures the manager login panel with username and password fields
     */
    private void createManagerLoginPanel() {
        // Initialize the main panel
        MainPagePanel = new JPanel();
        MainPagePanel.setName("ManagerLoginPanel");
        MainPagePanel.setBounds(15, 55, 350, 350);  // Set position and size
        MainPagePanel.setLayout(new GridBagLayout());  // Use GridBag layout
        
        // Configure GridBagLayout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;  // Don't resize components
        gbc.insets = new Insets(10, 0, 10, 0);  // Add padding
        gbc.anchor = GridBagConstraints.CENTER;  // Center align components
        
        // Configure panel appearance
        MainPagePanel.setBorder(new RoundedBorder(10, Color.black, 2));  // Add rounded border
        MainPagePanel.setBackground(new Color(144, 238, 144));  // Set light green background
        MainPagePanel.setVisible(true);

        // Create and configure username label
        JLabel UserNameLabel = new JLabel("UserName");
        UserNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        UserNameLabel.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 0;
        gbc.gridy = 0;
        MainPagePanel.add(UserNameLabel, gbc);

        // Create and configure username input field
        JTextField userNameField = new JTextField("");
        userNameField.setName("Username");
        userNameField.setBorder(new RoundedBorder(10, Color.black, 2));
        userNameField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        MainPagePanel.add(userNameField, gbc);

        // Create and configure password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 0;
        gbc.gridy = 1;
        MainPagePanel.add(passwordLabel, gbc);

        // Create and configure password input field
        JTextField passwordField = new JTextField("");
        passwordField.setName("Password");
        passwordField.setBorder(new RoundedBorder(10, Color.black, 2));
        passwordField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        MainPagePanel.add(passwordField, gbc);

        // Create and configure login button
        JButton login = new JButton("Login");
        login.setName("adminLogin");
        login.setBorder(new RoundedBorder(10, Color.black, 2));
        login.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 1;
        gbc.gridy = 2;
        MainPagePanel.add(login, gbc);

        // Add click handler for login button
        login.addActionListener(e -> controller.handleLogin(userNameField, passwordField));

        add(MainPagePanel);  // Add panel to window

    } // End of createManagerLoginPanel method
} // End of ManagerLogin class