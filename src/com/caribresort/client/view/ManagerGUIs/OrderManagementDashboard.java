/**
         * Provides a graphical interface for performing various order 
         * management operations. It includes buttons for the following functions:
         *
         * - "Add Order" - Opens a form for adding a new order with fields for 
         *   entering drink details and quantity.
         * - "Modify Order" - Opens a form to modify an existing order based on the order ID.
         * - "Remove Order" - Opens a form to delete an order by ID.
         * - "Pull Order" - Opens a form to retrieve order details based on criteria 
         *   like order ID, drink type, or date.
     
     *  GUI: RHOOMS 
     * POLYMORPHISM: VASSELL
**/

package com.caribresort.client.view.ManagerGUIs;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import com.caribresort.client.view.Utilities.RoundedBorder;
import com.caribresort.server.DBConnector;

public class OrderManagementDashboard extends JFrame implements ActionListener {
    
    private JPanel MainPagePanel; // PANEL TO HOLD ORDER PAGE COMPONENTS
    private String currentPage = "Order"; // TRACKS CURRENT PAGE

    public OrderManagementDashboard() {
        // TITLE LABEL FOR THE ORDER PANEL
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setName("TitleLabel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(125, 25, 250, 25);
        getContentPane().add(titleLabel);

        // BACK BUTTON TO RETURN TO MANAGER MAIN PAGE
        JButton back = new JButton("Back");
        back.setName("Back");
        back.setBorder(new RoundedBorder(10, Color.black, 2));
        back.setBounds(5, 435, 50, 25);
        back.addActionListener(this);
        getContentPane().add(back);

        // MAIN PANEL TO HOLD ORDER COMPONENTS
        MainPagePanel = new JPanel();
        MainPagePanel.setName("MainPagePanel");
        MainPagePanel.setBounds(65, 75, 350, 350);
        MainPagePanel.setBorder(new RoundedBorder(10, Color.black, 2));
        MainPagePanel.setBackground(new Color(144, 238, 144));
        MainPagePanel.setVisible(true);
        getContentPane().add(MainPagePanel);

        // INITIALIZE THE ORDER PAGE
        openOrderPage();

        // FRAME CONFIGURATIONS
        setTitle("Order Management");
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

    // METHOD TO SET UP THE ORDER PAGE COMPONENTS
    private void openOrderPage() {
        if (MainPagePanel != null) {
            MainPagePanel.removeAll();
        }

        MainPagePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // ADD ORDER BUTTON
        addManagementButton("Add Order", "AddOrder", 1, gbc, "add");

        // MODIFY ORDER BUTTON
        addManagementButton("Modify Order", "ModifyOrder", 2, gbc, "modify");

        // REMOVE ORDER BUTTON
        addManagementButton("Remove Order", "RemoveOrder", 3, gbc, "remove");

        // PULL DRINK BUTTON
        addManagementButton("Pull Orders", "PullOrders", 4, gbc, "pull");

        MainPagePanel.revalidate();
        MainPagePanel.repaint();
    }

// Helper method to create management buttons
    private void addManagementButton(String text, String name, int yPosition, GridBagConstraints gbc, String operation) {
        JButton button = new JButton(text);
        button.setName(name);
        button.setBorder(new RoundedBorder(10, Color.black, 2));
        button.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 0;
        gbc.gridy = yPosition;
        MainPagePanel.add(button, gbc);

        button.addActionListener(e -> {
            OrderManagementForm form = OrderManagementFormFactory.createForm(operation);
           
            this.setVisible(false);  // Hide the inventory page while form is open
            form.setVisible(true);
        });
    }

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


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button.getName().equals("Back")) {
                handleBackAction();
            }
        }
    }

    public static void main(String[] args) {
        DBConnector.getDatabaseConnection();
        SwingUtilities.invokeLater(() -> new OrderManagementDashboard());
    }
}