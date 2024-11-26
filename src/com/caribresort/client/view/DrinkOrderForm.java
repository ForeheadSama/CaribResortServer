package com.caribresort.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.caribresort.client.controller.DrinkOrderController;
import com.caribresort.client.view.Utilities.RoundedBorder;
import com.caribresort.client.view.LoginGUIs.HomePage;

public class DrinkOrderForm extends JFrame {
    private JComboBox<Integer>[] quantityComboBoxes; // Array of quantity comboboxes
    private JLabel[] drinkPriceLabels; // Array of drink price labels
    private JLabel[] drinkNameLabels; // Array of drink name labels
    private JLabel totalLabel; // Total cost label
    private JButton orderButton; // Order button
    private JPanel headerPanel; // Header panel
    private JPanel contentPanel; // Content panel
    private JPanel footerPanel; // Footer panel
    private double totalCost = 0.0; // Total cost of the order
    private JScrollPane scrollPane; // Scroll pane for content panel
    private boolean isUnderAge;  // Added to store age status
    
    //private DrinkOrderController controller; // Drink order controller
    private Map<String, Double> drinks; // Map to store drinks and their prices
    
    private static final Logger logger = LogManager.getLogger(DrinkOrderForm.class); // Logger object
    private static final Color LIGHT_BLUE = new Color(173, 216, 230); // Light blue color
    private static final Color LIGHT_GREEN = new Color(144, 238, 144); // Light green color

    public DrinkOrderForm() {
        //controller = new DrinkOrderController();

        // Main window setup
        setTitle("Carib Resort Drink Bar");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        initializeComponents();  // Initialize components
        setupWindowClosing(); // Setup window closing behavior
    }

    private void initializeComponents() {
        // Header Panel
        setupHeaderPanel();
        
        // Content Panel
        setupContentPanel();
        
        // Footer Panel
        setupFooterPanel();
        
        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void setupHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBackground(LIGHT_BLUE);
        headerPanel.setPreferredSize(new Dimension(400, 80));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("Caribbean Drink Bar");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
    }

    private void setupContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(LIGHT_GREEN);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    private void setupFooterPanel() {
        footerPanel = new JPanel();
        footerPanel.setBackground(LIGHT_GREEN);
        footerPanel.setPreferredSize(new Dimension(400, 80));
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
    }

    private JPanel createDrinkPanel(String drinkName, double price, int index) {
        JPanel drinkPanel = new JPanel();
        drinkPanel.setBackground(LIGHT_GREEN);
        drinkPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        drinkPanel.setMaximumSize(new Dimension(380, 40));
        
        // Drink name
        drinkNameLabels[index] = new JLabel(drinkName);
        drinkNameLabels[index].setFont(new Font("Arial", Font.BOLD, 14));
        drinkNameLabels[index].setPreferredSize(new Dimension(120, 25));
        
        // Price
        drinkPriceLabels[index] = new JLabel(String.format("$%.2f", price));
        drinkPriceLabels[index].setFont(new Font("Arial", Font.PLAIN, 14));
        drinkPriceLabels[index].setPreferredSize(new Dimension(60, 25));
        
        // Quantity dropdown
        quantityComboBoxes[index] = createQuantityComboBox();
        
        drinkPanel.add(drinkNameLabels[index]);
        drinkPanel.add(drinkPriceLabels[index]);
        drinkPanel.add(quantityComboBoxes[index]);
        
        return drinkPanel;
    }

    private JComboBox<Integer> createQuantityComboBox() {
        Integer[] quantities = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JComboBox<Integer> comboBox = new JComboBox<>(quantities);
        comboBox.setPreferredSize(new Dimension(60, 25));
        comboBox.setBorder(new RoundedBorder(10, Color.black, 2));
        ((JComponent) comboBox.getRenderer()).setOpaque(true);
        
        comboBox.addActionListener(e -> updateTotal());
        
        return comboBox;
    }

    private void setupFooterComponents() {
        footerPanel.removeAll();
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        totalLabel = new JLabel("Total: $0.00 USD");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        orderButton = new JButton("Place Order");
        orderButton.setFont(new Font("Arial", Font.BOLD, 14));
        orderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderButton.setMaximumSize(new Dimension(120, 30));
        orderButton.setBorder(new RoundedBorder(10, Color.black, 2));

        orderButton.addActionListener(e -> handleOrderPlacement());

        footerPanel.add(totalLabel);
        footerPanel.add(orderButton);
    }

    private void handleOrderPlacement() {
        Map<String, Integer> orderDetails = new HashMap<>();

        for (int i = 0; i < quantityComboBoxes.length; i++) {
            int quantity = (int) quantityComboBoxes[i].getSelectedItem();
            if (quantity > 0) {
                String drinkName = drinkNameLabels[i].getText();
                orderDetails.put(drinkName, quantity);
            }
        }

        DrinkOrderController.OrderResult orderResult = DrinkOrderController.processOrder(orderDetails, totalCost);

        if (orderResult.isSuccess()) {
            // Show order summary
            JOptionPane.showMessageDialog(DrinkOrderForm.this, 
                orderResult.getOrderSummary(), 
                "Order Confirmed", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Ask if they want to make another order
            int choice = JOptionPane.showConfirmDialog(DrinkOrderForm.this,
                "Would you like to make another order?",
                "Continue Ordering",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                // Clear fields and refresh the drink bar
                clearOrderFields();
                openDrinkBar(isUnderAge);  // Reopen with same age status
            } else {
                // Return to homepage
                dispose();
                new HomePage().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(DrinkOrderForm.this,
                orderResult.getErrorMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    public void openDrinkBar(boolean underAge) {
        this.isUnderAge = underAge;  // Store the age status
        contentPanel.removeAll();
        drinks = DrinkOrderController.loadDrinksPriceMap(underAge);

        quantityComboBoxes = new JComboBox[drinks.size()];
        drinkPriceLabels = new JLabel[drinks.size()];
        drinkNameLabels = new JLabel[drinks.size()];

        int i = 0;
        for (String drink : drinks.keySet()) {
            JPanel drinkPanel = createDrinkPanel(drink, drinks.get(drink), i++);
            contentPanel.add(drinkPanel);
        }

        setupFooterComponents();

        setVisible(true);
        revalidate();
        repaint();
    }

    private void clearOrderFields() {
        // Clear the quantity comboboxes
        for (JComboBox<Integer> comboBox : quantityComboBoxes) {
            comboBox.setSelectedIndex(0);
        }

        // Clear the total cost
        totalCost = 0.0;
        totalLabel.setText("Total: $0.00 USD");
        
        // Refresh the panel
        revalidate();
        repaint();
    }

    private void setupWindowClosing() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(DrinkOrderForm.this, 
                    "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                    new HomePage().setVisible(true);
                }
            }
        });
    }

    public void clearUsedSeatNumbers() {
        DrinkOrderController.clearUsedSeatNumbers();
        logger.info("[ ALL SEATS CLEARED ]");
    }

    private void updateTotal() {
        Map<String, Integer> orderQuantities = new HashMap<>();
    
        // Collect selected quantities and corresponding drink names
        for (int i = 0; i < quantityComboBoxes.length; i++) {
            
            int quantity = (int) quantityComboBoxes[i].getSelectedItem();
            if (quantity > 0) {
                String drinkName = drinkNameLabels[i].getText(); // Remove dollar sign
                orderQuantities.put(drinkName, quantity); // Add to order quantities map
            }
        }

        // Calculate total using the controller
        totalCost = DrinkOrderController.calculateTotal(orderQuantities);

        // Update the total label with the calculated total cost
        totalLabel.setText(String.format("Total: $%.2f USD", totalCost));
    }
    // ---------------------------------------------------------------------------------- //
}