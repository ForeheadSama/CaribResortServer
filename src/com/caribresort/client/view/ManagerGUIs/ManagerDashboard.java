/**
         * Serves as the main interface for managers, providing access to 
         * various management functionalities, including:
         *
         * - "Open Inventory" - Navigates to the inventory management page, where the 
         *   manager can add, edit, or remove drink items from the resort's inventory.
         * - "Order Management" - Opens the order management interface, enabling the 
         *   manager to add, modify, or delete guest orders and view order details.
         
    * GUI: RHOOMS
 **/

package com.caribresort.client.view.ManagerGUIs;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import com.caribresort.client.view.Utilities.RoundedBorder;
import com.caribresort.client.view.LoginGUIs.HomePage;

// MANAGER MAIN MENU
public class ManagerDashboard extends JFrame implements ActionListener{

    JPanel MainPagePanel; // PANEL TO HOLD MAIN PAGE COMPONENTS
    String currentPage = "Main"; // TRACKS CURRENT PAGE IN THE ADMIN PANEL

    public ManagerDashboard(){
        // TITLE LABEL FOR THE MANAGER ADMIN PANEL
        JLabel titleLabel = new JLabel("Manger Admin Panel");
        titleLabel.setName("TitleLabel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(125, 25, 250, 25);
        getContentPane().add(titleLabel); // ADD TITLE LABEL TO FRAME

        // BACK BUTTON TO RETURN TO PREVIOUS PAGE
        JButton back = new JButton("Back");
        back.setName("Back");
        back.setBorder(new RoundedBorder(10, Color.black, 2)); // ROUNDED BORDER FOR BACK BUTTON
        back.setBounds(5, 435, 50, 25); // POSITION OF BACK BUTTON
        back.setVisible(false); // BACK BUTTON IS HIDDEN INITIALLY
        back.setActionCommand("Back"); //ADDS ACTION COMMAND
        back.addActionListener(this); // ADD ACTION LISTENER TO BACK BUTTON
        getContentPane().add(back); // ADD BACK BUTTON TO FRAME

        // MAIN PAGE PANEL TO HOLD BUTTONS
        MainPagePanel = new JPanel();
        MainPagePanel.setName("MainPagePanel");
        MainPagePanel.setBounds(65, 75, 350, 350); // POSITION OF MAIN PAGE PANEL
        MainPagePanel.setBorder(new RoundedBorder(10, Color.black, 2)); // ROUNDED BORDER
        MainPagePanel.setBackground(new Color(144, 238, 144)); // LIGHT GREEN BACKGROUND
        MainPagePanel.setVisible(true); // MAKE MAIN PAGE PANEL VISIBLE
        getContentPane().add(MainPagePanel); // ADD MAIN PAGE PANEL TO FRAME

        openMainPage(); // CALL METHOD TO SET UP MAIN PAGE CONTENT

        // FRAME CONFIGURATIONS
        setTitle("Manager Page"); // SET FRAME TITLE
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // DO NOT CLOSE ON X BUTTON
        setLayout(null); // USE ABSOLUTE POSITIONING
        setSize(500, 500); // SET FRAME SIZE
        setLocationRelativeTo(null); // CENTER FRAME ON SCREEN
        setVisible(true); // MAKE FRAME VISIBLE

        // ADD WINDOW CLOSING HANDLER WITH CONFIRMATION DIALOG
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // CONFIRM EXIT BEFORE CLOSING
                int result = JOptionPane.showConfirmDialog(ManagerDashboard.this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose(); // CLOSE THE FRAME
                    new HomePage();
                    new HomePage().setVisible(true); // RETURN TO HOME PAGE
                }
            }
        });
    }

    // METHOD TO SET UP MAIN PAGE COMPONENTS
    private void openMainPage() {
        // REMOVE PREVIOUS COMPONENTS IF ANY
        if (MainPagePanel != null) {
            MainPagePanel.removeAll(); // CLEAR MAIN PAGE PANEL
        }

        MainPagePanel.setLayout(new GridBagLayout()); // USE GRID BAG LAYOUT
        GridBagConstraints gbc = new GridBagConstraints(); // GRID BAG CONSTRAINTS FOR COMPONENT PLACEMENT
        gbc.fill = GridBagConstraints.NONE; // DON’T RESIZE COMPONENTS
        gbc.insets = new Insets(10, 0, 10, 0); // ADD SOME PADDING AROUND THE COMPONENTS
        gbc.anchor = GridBagConstraints.CENTER; // CENTER ALIGN COMPONENTS

        // BUTTON TO OPEN INVENTORY PAGE
        JButton openInventoryPage = new JButton("Open Inventory");
        openInventoryPage.setName("OpenInventoryPage");
        openInventoryPage.setBorder(new RoundedBorder(10, Color.black, 2)); // ROUNDED BORDER FOR BUTTON
        openInventoryPage.setPreferredSize(new Dimension(100, 25)); // SET PREFERRED SIZE
        gbc.gridx = 0; // COLUMN POSITION
        gbc.gridy = 1; // ROW POSITION
        MainPagePanel.add(openInventoryPage, gbc); // ADD BUTTON TO MAIN PAGE PANEL
       
        // OPENS GUI
        openInventoryPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close current window
                new DrinkManagementDashboard().setVisible(true);
            }
        });

        // BUTTON TO OPEN ORDER PAGE
        JButton openOrderPage = new JButton("Order Management");
        openOrderPage.setName("OpenOrderPage");
        openOrderPage.setBorder(new RoundedBorder(10, Color.black, 2)); // ROUNDED BORDER FOR BUTTON
        openOrderPage.setPreferredSize(new Dimension(120, 25)); // SET PREFERRED SIZE
        gbc.gridx = 0; // SAME COLUMN AS PREVIOUS BUTTON
        gbc.gridy = 2; // NEXT ROW
        MainPagePanel.add(openOrderPage, gbc); // ADD BUTTON TO MAIN PAGE PANEL
        
        // OPENS GUI
        openOrderPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close current window
                new OrderManagementDashboard().setVisible(true);
            }
        });

        // HIDE BACK BUTTON
        getContentPane().getComponent(1).setVisible(false);

        // REVALIDATE AND REPAINT MAIN PAGE PANEL
        MainPagePanel.revalidate();
        MainPagePanel.repaint();
    }    

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("Back".equals(command)) {
            dispose(); // Close the ManagerMainPage
            new HomePage().setVisible(true); // Return to HomePage
        }
    }
}
