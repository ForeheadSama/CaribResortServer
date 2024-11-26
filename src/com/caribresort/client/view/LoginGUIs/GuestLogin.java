/**
     * Handles the guest login process, allowing guests to select their armband color
     * Different armband colors indicate different access levels and age restrictions
 * COMPLETED BY: RHOOMS 
 */

 // Package declaration
package com.caribresort.client.view.LoginGUIs;

// Import necessary GUI components
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Import the necessary classes
import com.caribresort.client.view.DrinkOrderForm;
import com.caribresort.client.view.Utilities.RoundedBorder;

// ---------- GUEST LOGIN CLASS
public class GuestLogin extends JFrame {
    
    private JPanel MainPagePanel; // Main panel to hold all components   
    private DrinkOrderForm drinkOrderForm; // Reference to the drink order form

    /**
     * Constructor: Initializes the guest selection window
     */
    public GuestLogin() {
        super("Guest Selection");  // Set window title
        drinkOrderForm = new DrinkOrderForm();  // Initialize drink order form
        setSize(400, 450);  // Set window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close window behavior
        setLocationRelativeTo(null);  // Center window on screen
        getContentPane().setBackground(new Color(173, 216, 230));  // Set light blue background
        setLayout(null);  // Use absolute positioning
        
        createGuestSelectionPanel();  // Initialize the main panel
    }

    /**
     * Creates and configures the guest selection panel with armband color options
     */
    private void createGuestSelectionPanel() {
        // Initialize the main panel
        MainPagePanel = new JPanel();
        MainPagePanel.setName("GuestBadgeSignPanel");
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

        // Create and configure the badge selection label
        JLabel lblBadge = new JLabel("Choose your Armband Color:");
        lblBadge.setName("LblBadge");
        lblBadge.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 0;
        gbc.gridy = 0;

        MainPagePanel.add(lblBadge, gbc);

        // Create dropdown with armband color options
        String[] GuestBadges = {"Yellow", "Orange", "Red", "Green"};  // Available colors
        JComboBox<String> cbxOptions = new JComboBox<>(GuestBadges);
        cbxOptions.setName("Badges");
        cbxOptions.setPreferredSize(new Dimension(150, 25));
        cbxOptions.setBorder(new RoundedBorder(10, Color.black, 2));
        gbc.gridx = 1;
        gbc.gridy = 0;
        MainPagePanel.add(cbxOptions, gbc);

        // Create and configure the Next button
        JButton Next = new JButton("Next");
        Next.setName("GuestBadgeSelect");
        Next.setPreferredSize(new Dimension(150, 25));
        Next.setBorder(new RoundedBorder(10, Color.black, 2));
        gbc.gridx = 0;
        gbc.gridy = 1;
        MainPagePanel.add(Next, gbc);

        add(MainPagePanel);  // Add panel to window

        // Add click handler for the Next button
        Next.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String Badge = cbxOptions.getSelectedItem().toString();  // Get selected badge color

                // Determine if guest is underage based on badge color
                boolean isUnderAge = Badge.equals("Yellow") 
                                    || Badge.equals("Red") 
                                    || Badge.equals("Green");
                
                dispose();  // Close current window
                drinkOrderForm.openDrinkBar(isUnderAge);  // Open drink order form with age restriction
            }; 
        }); // End of action listener
    } // End of createGuestSelectionPanel method
    
} // End of GuestLogin class