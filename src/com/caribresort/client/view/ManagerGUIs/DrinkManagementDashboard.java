/**
         * Provides managers with options to manage the drink inventory, including:
         *
         * - "Add Drink" - Opens a form to add new drinks to the resort's inventory.
         * - "Modify Drink" - Opens a form to edit details of existing drinks in the inventory.
         * - "Remove Drink" - Opens a form to remove drinks from the inventory.
         * - "Pull Drinks" - A button to retrieve and display the current drink list from the database.
         *
        
    * GUI: RHOOMS
    * POLYMORPHISM: VASSELL
 */

package com.caribresort.client.view.ManagerGUIs;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import com.caribresort.client.view.Utilities.RoundedBorder;
import com.caribresort.server.DBConnector;

public class DrinkManagementDashboard extends JFrame implements ActionListener {

    private JPanel MainPagePanel; // PANEL TO HOLD INVENTORY PAGE COMPONENTS
    private String currentPage = "Inventory"; // TRACKS CURRENT PAGE

    // Constructor to bring up main GUI
    public DrinkManagementDashboard() {

        // TITLE LABEL FOR THE INVENTORY PANEL
        JLabel titleLabel = new JLabel("Inventory Management");
        titleLabel.setName("TitleLabel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(125, 25, 290, 25);
        getContentPane().add(titleLabel);

        // BACK BUTTON TO RETURN TO MANAGER MAIN PAGE
        JButton back = new JButton("Back");
        back.setName("Back");
        back.setBorder(new RoundedBorder(10, Color.black, 2));
        back.setBounds(5, 435, 50, 25);
        back.addActionListener(this);
        getContentPane().add(back);

        // MAIN PANEL TO HOLD INVENTORY COMPONENTS
        MainPagePanel = new JPanel();
        MainPagePanel.setName("MainPagePanel");
        MainPagePanel.setBounds(65, 75, 350, 350);
        MainPagePanel.setBorder(new RoundedBorder(10, Color.black, 2));
        MainPagePanel.setBackground(new Color(144, 238, 144));
        MainPagePanel.setVisible(true);
        getContentPane().add(MainPagePanel);

        // INITIALIZE THE INVENTORY PAGE
        openInventoryPage();

        // FRAME CONFIGURATIONS
        setTitle("Inventory Management");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(null);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // ADD WINDOW CLOSING HANDLER
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleBackAction();
            }
        });
    }

    // METHOD TO SET UP THE PAGE COMPONENTS
    private void openInventoryPage() {
        if (MainPagePanel != null) {
            MainPagePanel.removeAll();
        }

        MainPagePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // ADD DRINK BUTTON
        addManagementButton("Add Drink", "AddDrink", 1, gbc, "add");

        // MODIFY DRINK BUTTON
        addManagementButton("Modify Drink", "ModifyDrink", 2, gbc, "modify");

        // REMOVE DRINK BUTTON
        addManagementButton("Remove Drink", "RemoveDrink", 3, gbc, "remove");

        // PULL DRINK BUTTON
        addManagementButton("Pull Drinks", "PullDrinks", 4, gbc, "pull");

        MainPagePanel.revalidate();
        MainPagePanel.repaint();
    }

    // Helper method to create management buttons
    private void addManagementButton(String text, String name, int yPosition, GridBagConstraints gbc,
            String operation) {
        JButton button = new JButton(text);
        button.setName(name);
        button.setBorder(new RoundedBorder(10, Color.black, 2));
        button.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 0;
        gbc.gridy = yPosition;
        MainPagePanel.add(button, gbc);

        button.addActionListener(e -> {
            DrinkManagementForm form = DrinkManagementFormFactory.createForm(operation);

            this.setVisible(false); // Hide the inventory page while form is open
            form.setVisible(true); // Show the form
        }); // End of button click handler
    } // End of addManagementButton method

    // Handle back button action
    private void handleBackAction() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Return to Manager Page",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new ManagerDashboard().setVisible(true);
        }
    }

    // Handle back button action
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button.getName().equals("Back")) {
                handleBackAction();
            }
        }
    } // End of actionPerformed method

    // Application entry point
    public static void main(String[] args) {
        DBConnector.getDatabaseConnection();
        SwingUtilities.invokeLater(() -> new DrinkManagementDashboard());
    } // End of main method
} // End of DrinkManagementDashboard class