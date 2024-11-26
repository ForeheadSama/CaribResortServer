/**
         * Main entry point of the application that displays the initial user selection screen
         * Allows users to choose between Guest and Manager login options

    * COMPLETED BY: RHOOMS
 */

 // Package declaration
 package com.caribresort.client.view.LoginGUIs;

// Import necessary Java Swing and AWT libraries for GUI components
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.caribresort.client.view.Utilities.RoundedBorder;

// ---------- HOME PAGE CLASS
public class HomePage extends JFrame implements ActionListener {
    private JPanel MainPagePanel; // Panel to hold all the components
    private GuestLogin guestLogin; // References to login screens
    private ManagerLogin managerLogin; // References to login screens

    /**
     * Constructor: Sets up the main window and initializes the UI
     */
    public HomePage() {
        // Create and configure the main title label
        JLabel titleLabel = new JLabel("Login to Carib Resort");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));  // Set font size and style
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);  // Center the text
        titleLabel.setBounds(75, 25, 250, 25);  // Position and size the label

        add(titleLabel);  // Add the title to the window

        // Initialize the main content of the home page
        mainHomePage();

        // Configure the main window properties
        setTitle("Carib Resort Login Page");  // Set window title
        setSize(400, 450);  // Set window size
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  // Handle window closing manually
        setLocationRelativeTo(null);  // Center window on screen
        getContentPane().setBackground(new Color(173, 216, 230));  // Set light blue background
        setLayout(null);  // Use absolute positioning

        // Add window closing handler to show confirmation dialog
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(HomePage.this, 
                    "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();  // Close the window
                    System.exit(0);  // Exit the application
                }
            }
        });
    }

    /**
     * Creates and configures the main content panel with user selection buttons
     */
    public void mainHomePage() {
        // Remove existing panel if it exists
        if (MainPagePanel != null) {
            remove(MainPagePanel);
            MainPagePanel = null;
        }

        // Create and configure the main panel
        MainPagePanel = new JPanel();
        MainPagePanel.setName("HomePagePanel");
        MainPagePanel.setBounds(15, 55, 350, 350);  // Set position and size
        MainPagePanel.setLayout(new GridBagLayout());  // Use GridBag layout for component arrangement
        MainPagePanel.setBorder(new RoundedBorder(10, Color.black, 2));  // Add rounded border
        MainPagePanel.setBackground(new Color(144, 238, 144));  // Set light green background
        MainPagePanel.setVisible(true);

        // Configure GridBagLayout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;  // Don't resize components
        gbc.insets = new Insets(10, 0, 10, 0);  // Add padding
        gbc.anchor = GridBagConstraints.CENTER;  // Center align components

        // Create and configure the user selection label
        JLabel userLabel = new JLabel("Please select a user", SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setPreferredSize(new Dimension(200, 25));
        
        MainPagePanel.add(userLabel, gbc);

        // Create and configure the Guest button
        JButton Guest = new JButton("Guest");
        Guest.setName("Guest");  // Set name for action handling
        Guest.setBorder(new RoundedBorder(10, Color.black, 2));
        Guest.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 0;  // Set grid position
        gbc.gridy = 1;
        
        MainPagePanel.add(Guest, gbc); // Add the button to the panel
        Guest.addActionListener(this);  // Add click handler

        // Create and configure the Manager button
        JButton Manager = new JButton("Manager"); // Create the button
        Manager.setName("Manager"); // Set name for action handling
        Manager.setBorder(new RoundedBorder(10, Color.black, 2)); // Add rounded border
        Manager.setPreferredSize(new Dimension(100, 25)); // Set button size
        gbc.gridx = 0;
        gbc.gridy = 2;

        MainPagePanel.add(Manager, gbc); // Add the button to the panel
        Manager.addActionListener(this); // Add click handler

        add(MainPagePanel);  // Add the panel to the window

        // Refresh the display
        revalidate();
        repaint();
    }

    /**
     * Application entry point
     */
    public static void main(String[] args) {
        // Create and show the window on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new HomePage().setVisible(true));
    }

    /**
     * Handles button click events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();  // Get the clicked button
        
        // Handle different button clicks
        switch (sourceButton.getName()) {
            case "Guest":
                // Open guest login window
                guestLogin = new GuestLogin();
                guestLogin.setVisible(true);
                dispose();  // Close current window
                break;
            case "Manager":
                // Open manager login window
                managerLogin = new ManagerLogin();
                managerLogin.setVisible(true);
                dispose();  // Close current window
                break;
        } // End of switch
    } // End of actionPerformed method
    
} // End of HomePage class