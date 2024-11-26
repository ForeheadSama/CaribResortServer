/**
 * Package contains manager-specific GUI components for the Carib Resort system
 */
package com.caribresort.client.view.ManagerGUIs;

// Import necessary Java GUI and utility packages
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.caribresort.client.controller.MangerController;
import com.caribresort.client.model.*;
import com.caribresort.client.model.Order.OrderStatus;
import com.caribresort.client.view.Utilities.RoundedBorder;

// Base abstract class that provides common functionality for all order management forms
abstract class OrderManagementForm extends JFrame {
    // Main panel to hold form components
    protected JPanel mainPanel;
    // Constraints for GridBagLayout positioning
    protected GridBagConstraints gbc;
    // used to reference components added by addFormField
    protected HashMap<String, Component > components = new HashMap<>(); 
    // Common color constant
    private static final Color LIGHT_GREEN = new Color(144, 238, 144);

    // Constructor sets up common form elements
    public OrderManagementForm(String title) {
        // Configure basic window properties
        setTitle(title);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        int parentWidth = this.getWidth(); // Get the width of the parent container
        int labelWidth = 290; // The width you want for the label
        int x = (parentWidth - (labelWidth/2)) / 2; // Calculate the x coordinate to center the label
        
        // Create and configure title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setName("TitleLabel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(x, 25, labelWidth, 25);
        add(titleLabel);
        
        // Create and configure main panel
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setName("MainPagePanel");
        mainPanel.setBounds(65, 75, 350, 350);
        mainPanel.setBorder(new RoundedBorder(10, Color.black, 2));
        mainPanel.setBackground(LIGHT_GREEN);
        add(mainPanel);
        
        // Initialize GridBagConstraints for component positioning
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Create and configure back button
        JButton backButton = new JButton("Back");
        backButton.setName("Back");
        backButton.setBorder(new RoundedBorder(10, Color.black, 2));
        backButton.setBounds(5, 435, 50, 25);
        backButton.addActionListener(e -> handleBackAction());
        add(backButton);
        
        // Initialize form-specific components
        setupForm();
        addSubmitButton();
        
        // Configure window closing behavior
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleBackAction();
            }
        });
    }

    // Abstract methods to be implemented by concrete classes
    protected abstract void setupForm();
    protected abstract void handleSubmit();
    protected abstract String getSubmitButtonText();

    // Helper method to add form fields with labels
    protected void addFormField(String labelText, String fieldName, String defaultText, int yPosition) {
        // Create and configure label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 0;
        gbc.gridy = yPosition;
        mainPanel.add(label, gbc);

        // Create and configure text field
        JTextField textField = new JTextField();
        textField.setBorder(new RoundedBorder(5, Color.BLACK, 1));
        textField.setName(fieldName);
        textField.setPreferredSize(new Dimension(150, 25));
        textField.setText(defaultText);
        gbc.gridx = 1;
        mainPanel.add(textField, gbc);
        components.put(fieldName, textField); // the text field to  the components so it can be referenced later in the HanadleSubmit
    }

    // Helper method to add combo boxes with labels
    protected void addComboBox(JPanel panel, String labelText, String[] items, String fieldName, int yPosition) {
        // Create and configure label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(100, 25));
        GridBagConstraints localGbc = new GridBagConstraints();
        localGbc.insets = new Insets(5, 5, 5, 5); // Add padding around each component
        localGbc.gridx = 0;
        localGbc.gridy = yPosition;
        panel.add(label, localGbc);
    
        // Create and configure combo box
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBorder(new RoundedBorder(5, Color.BLACK, 1));
        comboBox.setName(fieldName);
        comboBox.setPreferredSize(new Dimension(150, 25));
        localGbc.gridx = 1;
        panel.add(comboBox, localGbc);
        components.put(fieldName, comboBox); // the text field to  the components so it can be referenced later in the HanadleSubmit
    }

    // Adds submit button to form
    private void addSubmitButton() {
        JButton submitButton = new JButton(getSubmitButtonText());
        submitButton.setBorder(new RoundedBorder(10, Color.black, 2));
        submitButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1;
        gbc.gridy = 10;
        submitButton.addActionListener(e -> handleSubmit());
        mainPanel.add(submitButton, gbc);
    }

    // Handles back button action with confirmation dialog
    protected void handleBackAction() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?",
            "Return to ManagerPage",
            JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new OrderManagementDashboard().setVisible(true);
        }
    }
}

// Form for adding new orders
class AddOrderForm extends OrderManagementForm {
    public AddOrderForm() {
        super("Add New Order");
    }

    // Sets up form fields for adding new order
    @Override
    protected void setupForm() {
        addFormField("Order ID", "orderIdField", "", 0);
        addFormField("Day", "dayField", "1", 1);
        
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        addComboBox(mainPanel, "Month", months, "monthCombo", 2);

        // Create year combo box with dynamic range
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[currentYear - 1999];
        for (int i = 0; i < years.length; i++) {
            years[i] = String.valueOf(2000 + i);
        }
        addComboBox(mainPanel, "Year", years, "yearCombo", 3);

        String[] Period = {"AM", "PM"};
        addComboBox(mainPanel, "Period", Period, "periodCombo", 4);

        String[] orderStatus = {"successful", "canceled", "isPrepare", "unknown"};
        addComboBox(mainPanel, "OrderStatus", orderStatus, "OrderStatusCombo", 5);

        addFormField("OrderTotal", "orderTotalField", "0.0", 6);
    }

    // Handles submit action for adding order
    @Override
    protected void handleSubmit() {
        // Implementation to be added
        // Implementation to be added
        JTextField orderIdField = (JTextField) components.get("orderIdField");
        if(orderIdField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Order ID field cannot be empty.");
            return;
        }
        int orderId = Integer.parseInt(orderIdField.getText());

        JTextField dayField = (JTextField) components.get("dayField");
        if (dayField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Day field cannot be empty.");
            return;
        }
        int day = Integer.parseInt(dayField.getText());

        JComboBox monthComboBox = (JComboBox) components.get("monthCombo");
        int month = 0;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (int i = 0; i < months.length; i++) {
            if (monthComboBox.getSelectedItem().toString() == months[i]) {
                month = i;
            }
        }

        JComboBox yearComboBox = (JComboBox) components.get("yearCombo");
        int year = Integer.parseInt(yearComboBox.getSelectedItem().toString());

        JComboBox periodComboBox = (JComboBox) components.get("periodCombo");
        Date.Period period = periodComboBox.getSelectedItem().toString() == "AM" ? Date.Period.AM : Date.Period.PM;

        JComboBox orderStatusComboBox = (JComboBox) components.get("OrderStatusCombo");
        String Status = orderStatusComboBox.getSelectedItem().toString();
        Order.OrderStatus orderStatus;
        
        if (Status == "successful"){
            orderStatus = Order.OrderStatus.successful;
        }else if (Status == "isPrepare"){
            orderStatus = Order.OrderStatus.isPrepare;
        }else if (Status == "canceled"){
            orderStatus = Order.OrderStatus.canceled;
        }else{
            orderStatus = Order.OrderStatus.unknown;
        }

        JTextField orderTotalField = (JTextField) components.get("orderTotalField");
        if(orderTotalField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Order Total field cannot be empty.");
            return;
        }
        double orderTotal = Double.parseDouble(orderTotalField.getText());

        Date newDate  = new Date(day, month, year, period);

        if (MangerController.addOrder(orderId, newDate, orderStatus, null, orderTotal)) {
            JOptionPane.showMessageDialog(this, "Order was Added.");
        }else{
            JOptionPane.showMessageDialog(this, "Failed to Add Order");
        };
    }

    // Returns text for submit button
    @Override
    protected String getSubmitButtonText() {
        return "Add Order";
    }
}

// Form for modifying existing orders
class ModifyOrderForm extends OrderManagementForm {
    public ModifyOrderForm() {
        super("Modify Order");
    }

    // Sets up form fields for modifying order
    @Override
    protected void setupForm() {
        addFormField("Order ID", "orderIdField", "", 0);
        addFormField("Day", "dayField", "1", 1);
        
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        addComboBox(mainPanel, "Month", months, "monthCombo", 2);

        // Create year combo box with dynamic range
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[currentYear - 1999];
        for (int i = 0; i < years.length; i++) {
            years[i] = String.valueOf(2000 + i);
        }
        addComboBox(mainPanel, "Year", years, "yearCombo", 3);

        String[] Period = {"AM", "PM"};
        addComboBox(mainPanel, "Period", Period, "periodCombo", 4);

        String[] orderStatus = {"successful", "canceled", "isPrepare", "unknown"};
        addComboBox(mainPanel, "OrderStatus", orderStatus, "OrderStatusCombo", 5);
        
        addFormField("OrderTotal", "orderTotalField", "0.0", 6);
    }

    // Handles submit action for modifying order
    @Override
    protected void handleSubmit() {
        // Implementation to be added
        JTextField orderIdField = (JTextField) components.get("orderIdField");
        if(orderIdField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Order ID field cannot be empty.");
            return;
        }
        int orderId = Integer.parseInt(orderIdField.getText());

        JTextField dayField = (JTextField) components.get("dayField");
        if (dayField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Day field cannot be empty.");
            return;
        }
        int day = Integer.parseInt(dayField.getText());

        JComboBox monthComboBox = (JComboBox) components.get("monthCombo");
        int month = 0;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (int i = 0; i < months.length; i++) {
            if (monthComboBox.getSelectedItem().toString() == months[i]) {
                month = i;
            }
        }

        JComboBox yearComboBox = (JComboBox) components.get("yearCombo");
        int year = Integer.parseInt(yearComboBox.getSelectedItem().toString());

        JComboBox periodComboBox = (JComboBox) components.get("periodCombo");
        Date.Period period = periodComboBox.getSelectedItem().toString() == "AM" ? Date.Period.AM : Date.Period.PM;

        JComboBox orderStatusComboBox = (JComboBox) components.get("OrderStatusCombo");
        String Status = orderStatusComboBox.getSelectedItem().toString();
        Order.OrderStatus orderStatus;
        
        if (Status == "successful"){
            orderStatus = Order.OrderStatus.successful;
        }else if (Status == "isPrepare"){
            orderStatus = Order.OrderStatus.isPrepare;
        }else if (Status == "canceled"){
            orderStatus = Order.OrderStatus.canceled;
        }else{
            orderStatus = Order.OrderStatus.unknown;
        }

        JTextField orderTotalField = (JTextField) components.get("orderTotalField");
        if(orderTotalField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Order Total field cannot be empty.");
            return;
        }
        double orderTotal = Double.parseDouble(orderTotalField.getText());

        Date newDate  = new Date(day, month, year, period);

        if (MangerController.updateOrder(orderId, newDate, orderStatus, null, orderTotal)) {
            JOptionPane.showMessageDialog(this, "Order with ID " + orderId + " has been Updated.");
        }else{
            JOptionPane.showMessageDialog(this, "Failed to Updated Order with ID " + orderId);
        };
    }

    // Returns text for submit button
    @Override
    protected String getSubmitButtonText() {
        return "Modify Order";
    }
}

// Form for removing orders
class RemoveOrderForm extends OrderManagementForm {
    public RemoveOrderForm() {
        super("Remove Order");
    }

    // Sets up form field for removing order
    @Override
    protected void setupForm() {
        addFormField("Order ID", "orderIdField", "", 0);
    }

    // Handles submit action for removing order
    @Override
    protected void handleSubmit() {
        // Implementation to be added
        JTextField orderIdField = (JTextField) components.get("orderIdField");

        if(orderIdField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Order ID field cannot be empty.");
            return;
        }

        int orderId = Integer.parseInt(orderIdField.getText());

        if (MangerController.deleteOrder(orderId)) {
            JOptionPane.showMessageDialog(this, "Order with ID " + orderId + " has been removed.");
        }else{
            JOptionPane.showMessageDialog(this, "Failed to delete Order with ID " + orderId);
        };
    }

    // Returns text for submit button
    @Override
    protected String getSubmitButtonText() {
        return "Remove Order";
    }
}

// Form for pulling/retrieving orders with multiple search options
class PullOrderForm extends OrderManagementForm {
    // Panel for radio buttons
    private JPanel radioPanel;
    // Panel for input fields
    private JPanel inputPanel;
    // Layout manager for switching between input panels
    private CardLayout cardLayout;
    //Keeps a track of the pull type the user is on
    private String pullType;

    public PullOrderForm() {
        super("Pull Orders");
    }

    // Sets up the complete pull order form
    @Override
    protected void setupForm() {
        setupRadioButtons();
        setupInputPanels();
    }

    // Sets up radio buttons for different pull options
    private void setupRadioButtons() {
        // Initialize radio button panel
        radioPanel = new JPanel();
        radioPanel.setOpaque(false);
        radioPanel.setLayout(new FlowLayout());

        // Create button group and radio buttons
        ButtonGroup bg = new ButtonGroup();
        JRadioButton specificPull = new JRadioButton("Specific");
        JRadioButton drinkPull = new JRadioButton("Drink");
        JRadioButton datePull = new JRadioButton("Date");

        // Configure radio button appearance
        specificPull.setOpaque(false);
        drinkPull.setOpaque(false);
        datePull.setOpaque(false);

        // Add buttons to group
        bg.add(specificPull);
        bg.add(drinkPull);
        bg.add(datePull);

        // Add buttons to panel
        radioPanel.add(specificPull);
        radioPanel.add(drinkPull);
        radioPanel.add(datePull);

        // Position radio panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(radioPanel, gbc);
        gbc.gridwidth = 1;

        // Add listeners for switching panels
        specificPull.addActionListener(e -> showCard("specific"));
        drinkPull.addActionListener(e -> showCard("drink"));
        datePull.addActionListener(e -> showCard("date"));

        // Set default selection
        specificPull.setSelected(true);
    }

    // Sets up different input panels for each pull option
    private void setupInputPanels() {
        // Initialize input panel with card layout
        inputPanel = new JPanel(new CardLayout());
        inputPanel.setOpaque(false);
        cardLayout = (CardLayout) inputPanel.getLayout();

        // Create panel for specific order pull
        JPanel specificPanel = new JPanel(new GridBagLayout());
        specificPanel.setOpaque(false);
        addFormField(specificPanel, "Order ID", "orderIdField", "", 0);

        // Create panel for drink-based pull
        JPanel drinkPanel = new JPanel(new GridBagLayout());
        drinkPanel.setOpaque(false);
        addFormField(drinkPanel, "Drink", "drinkField", "", 0);
        addFormField(drinkPanel, "Quantity", "quantityField", "", 1);

        // Create panel for date-based pull
        JPanel datePanel = new JPanel(new GridBagLayout());
        datePanel.setOpaque(false);
        addFormField(datePanel, "Day", "dayField", "1", 0);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        addComboBox(datePanel, "Month", months, "monthCombo", 1);
        
        // Create year combo box with dynamic range
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[currentYear - 1999];
        for (int i = 0; i < years.length; i++) {
            years[i] = String.valueOf(2000 + i);
        }
        addComboBox(datePanel, "Year", years, "yearCombo", 2);

        // Add all panels to card layout
        inputPanel.add(specificPanel, "specific");
        inputPanel.add(drinkPanel, "drink");
        inputPanel.add(datePanel, "date");

        // Position input panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(inputPanel, gbc);
        gbc.gridwidth = 1;

        // Show default panel
        showCard("specific");
    }

    // Helper method to add form fields to panels
    private void addFormField(JPanel panel, String labelText, String fieldName, String defaultText, int yPosition) {
        // Create and configure label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(100, 25));
        GridBagConstraints localGbc = new GridBagConstraints();
        localGbc.fill = GridBagConstraints.NONE;
        localGbc.insets = new Insets(5, 5, 5, 5);
        localGbc.gridx = 0;
        localGbc.gridy = yPosition;
        panel.add(label, localGbc);

        // Create and configure text field
        JTextField textField = new JTextField();
        textField.setBorder(new RoundedBorder(5, Color.BLACK, 1));
        textField.setName(fieldName);
        textField.setPreferredSize(new Dimension(150, 25));
        textField.setText(defaultText);
        localGbc.gridx = 1;
        panel.add(textField, localGbc);
        components.put(fieldName, textField); // the text field to  the components so it can be referenced later in the HanadleSubmit
    }

    // Shows the selected card in card layout
    private void showCard(String cardName) {
        pullType = cardName;
        cardLayout.show(inputPanel, cardName);
    }

    // Handles submit action for pulling orders
    @Override
    protected void handleSubmit() {
        // Get selected pull option
        List<Order> orders = new ArrayList<>(); // Initialize orders list, just in case any of the if statements didn't work properly

        if (pullType == "specific") {
            JTextField orderIdField = (JTextField) components.get("orderIdField");
            if(orderIdField.getText().isEmpty()){ // if empty pull all orders
                orders = MangerController.getOrders();
            }else{
                int orderId = Integer.parseInt(orderIdField.getText());
                Order order = MangerController.getOrder(orderId);
                if (order == null) {
                    JOptionPane.showMessageDialog(this, "Couldn't find Order with ID " + orderId);
                    return;
                }
                orders.add(order); // Add the order to the list
            }
        } else if (pullType == "drink") {
            JTextField drinkField = (JTextField) components.get("drinkField");
            if (drinkField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Drink ID field cannot be empty.");
               return; //
            }
            String drinkId = drinkField.getText();

            JTextField quantityField = (JTextField) components.get("quantityField");
            int quantity = 0; // zero means no quantity

            if (!quantityField.getText().isEmpty()) {
                quantity = Integer.parseInt(quantityField.getText());
            }

            orders = MangerController.getOrder(drinkId, quantity);
        } else if (pullType == "date") {
            JTextField dayField = (JTextField) components.get("dayField");
            if (dayField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Day field cannot be empty.");
                return;
            }
            int day = Integer.parseInt(dayField.getText());

            JComboBox monthComboBox = (JComboBox) components.get("monthCombo");
            int month = 0;
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

            for (int i = 0; i < months.length; i++) {
                if (monthComboBox.getSelectedItem().toString() == months[i]) {
                    month = i;
                }
            }

            JComboBox yearComboBox = (JComboBox) components.get("yearCombo");
            int year = Integer.parseInt(yearComboBox.getSelectedItem().toString());

            orders = MangerController.getOrders(day, month, year);
        }else{
            return;
        }

        displayOrders(orders);
    }

    protected void displayOrders(List<Order> orders) {
        Color LIGHT_GREEN = new Color(144, 238, 144);
        // Create a DefaultTableModel with column names
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{
            "ID", "Date", "Status", "Total"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };
        
        Map<Integer, Order> orderMap = new HashMap<Integer, Order>();
        // Populate the DefaultTableModel with drink data
        for (Order entry : orders) {
            int iD = entry.getOrderID();

            Date date = entry.getOrderDate();
            OrderStatus status = entry.getStatus();
            double total = entry.getOrderTotal();
            tableModel.addRow(new Object[]{iD, date.toString(), status.toString(), total});
            orderMap.put(iD, entry);
        }

        // Display all drinks in a table
        JTable table = new JTable(tableModel);
        table.setBackground(LIGHT_GREEN);

        // Make the table scrollable
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setName("MainPagePanel");
        scrollPane.setBounds(65, 75, 350, 350);
        scrollPane.setBorder(new RoundedBorder(10, Color.black, 2));
        scrollPane.getViewport().setBackground(LIGHT_GREEN);
        scrollPane.setBackground(Color.white);
        add(scrollPane);

        // Add mouse listener to detect double-clicks on rows
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Check if it was a double-click
                if (e.getClickCount() == 2) return; //
                // Get the clicked row index
                int row = table.rowAtPoint(e.getPoint());
                if (row != -1) {
                    // Print row data
                    int orderId = Integer.parseInt(table.getValueAt(row, 0).toString());
                    List<OrderDetails> orderDetails = orderMap.get(orderId).getOrderDetailsList();
                    if (orderDetails == null || orderDetails.size() == 0) return;
                    displayOrderDetails(orderDetails);
                    remove(scrollPane); // Remove the JScrollPane from the panel
                    revalidate();       // Revalidate the panel layout
                    repaint();          // Repaint to update the UI
                }
            }
        });

        mainPanel.setVisible(false);
        scrollPane.setVisible(true);
    }

    protected void displayOrderDetails(List<OrderDetails> orderDetails) {
        Color LIGHT_GREEN = new Color(144, 238, 144);
        // Create a DefaultTableModel with column names
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{
            "ID", "OrderDetailsID", "DrinkID", "Quantity", "detailCost"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

        // Populate the DefaultTableModel with drink data
        for (OrderDetails entry : orderDetails) {
            int iD = entry.getOrderID();
            int orderDetailsID = entry.getOrderDetailsID();
            String drinkID = entry.getDrinkID();
            double quantity = entry.getQuantity();
            double detailCost = entry.getDetailCost();
            tableModel.addRow(new Object[]{iD, orderDetailsID, drinkID, quantity, detailCost});
        }

        // Display all drinks in a table
        JTable table = new JTable(tableModel);
        table.setBackground(LIGHT_GREEN);
        // Add mouse listener to detect double-clicks on rows

        // Make the table scrollable
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setName("MainPagePanel");
        scrollPane.setBounds(65, 75, 350, 350);
        scrollPane.setBorder(new RoundedBorder(10, Color.black, 2));
        scrollPane.getViewport().setBackground(LIGHT_GREEN);
        scrollPane.setBackground(Color.white);
        add(scrollPane);

        mainPanel.setVisible(false);
        scrollPane.setVisible(true);
    }

    // Returns text for submit button
    @Override
    protected String getSubmitButtonText() {
        return "Pull Orders";
    }
}

// Factory class for creating appropriate order management forms
public class OrderManagementFormFactory {
    // Creates and returns specific form based on operation type
    public static OrderManagementForm createForm(String operation) {
        return switch (operation.toLowerCase()) {
            case "add" -> new AddOrderForm();
            case "modify" -> new ModifyOrderForm();
            case "remove" -> new RemoveOrderForm();
            case "pull" -> new PullOrderForm();
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
    }
}