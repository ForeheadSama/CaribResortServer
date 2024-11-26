/**
    * Provides a factory method to create and display different forms 
    * for drink order operations in the CaribResort project. Depending on the 
    * specified operation, it initializes the corresponding form: 
    * 
    * - "Add" - Displays a form to add a new order with drink and quantity fields.
    * - "Modify" - Displays a form to modify an existing order, requiring an order ID, drink, and quantity.
    * - "Remove" - Displays a form to remove an order based on an order ID.
    * - "Pull" - Displays a form to pull orders by specific criteria (order ID, drink, or date).

 * GUI: DAYSHAUN RHOOMS
 * POLYMORPHISM: BRITNEY VASSELL

**/

package com.caribresort.client.view.ManagerGUIs;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import com.caribresort.client.model.*;
import com.caribresort.client.controller.MangerController;
import com.caribresort.client.view.Utilities.RoundedBorder;

// Abstract base class for all drink management operations
abstract class DrinkManagementForm extends JFrame {
    protected JPanel mainPanel;
    protected GridBagConstraints gbc;
    protected HashMap<String, Component > components = new HashMap<>(); // used to reference components added by addFormField

    private static final Color LIGHT_GREEN = new Color(144, 238, 144);

    public DrinkManagementForm(String title) {
        setTitle(title);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        
        int parentWidth = this.getWidth(); // Get the width of the parent container
        int labelWidth = 290; // The width you want for the label
        int x = (parentWidth - (labelWidth/2)) / 2; // Calculate the x coordinate to center the label

        // Add title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setName("TitleLabel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(x, 25, labelWidth, 25);
        add(titleLabel);
        
        // Initialize main panel with rounded border
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setName("MainPagePanel");
        mainPanel.setBounds(65, 75, 350, 350);
        mainPanel.setBorder(new RoundedBorder(10, Color.black, 2));
        mainPanel.setBackground(LIGHT_GREEN);
        add(mainPanel);
        
        // Initialize common GridBagConstraints
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Add back button
        JButton backButton = new JButton("Back");
        backButton.setName("Back");
        backButton.setBorder(new RoundedBorder(10, Color.black, 2));
        backButton.setBounds(5, 435, 50, 25);
        backButton.addActionListener(e -> handleBackAction());
        add(backButton);
        
        setupForm();
        addSubmitButton();
        
        // Add window closing handler
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleBackAction();
            }
        });
    }
 
    protected abstract void setupForm(); // Abstract method to set up form fields
    protected abstract void handleSubmit(); // Abstract method to handle form submission
    protected abstract String getSubmitButtonText(); // Abstract method to get the submit button text

    // Method to add form fields to the main panel
    protected void addFormField(String labelText, String fieldName, int yPosition) {
        // Create label for the field
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 0;
        gbc.gridy = yPosition;
        mainPanel.add(label, gbc);

        // Create text field with rounded border
        JTextField textField = new JTextField();
        textField.setBorder(new RoundedBorder(5, Color.BLACK, 1));
        textField.setName(fieldName);
        textField.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 1;
        mainPanel.add(textField, gbc);
        components.put(fieldName, textField); // the text field to  the components so it can be referenced later in the HanadleSubmit
    }

    // Method to add an alcoholic option to the form
    protected void addAlcoholicOption(int yPosition) {
        JLabel alcoholicLabel = new JLabel("Alcoholic");
        alcoholicLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        alcoholicLabel.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 0;
        gbc.gridy = yPosition;
        mainPanel.add(alcoholicLabel, gbc);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radioPanel.setOpaque(false);

        JRadioButton yesButton = new JRadioButton("Yes");
        yesButton.setOpaque(false);
        components.put("Yes", yesButton);

        JRadioButton noButton = new JRadioButton("No");
        noButton.setOpaque(false);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(yesButton);
        buttonGroup.add(noButton);

        radioPanel.add(yesButton);
        radioPanel.add(noButton);

        gbc.gridx = 1;
        mainPanel.add(radioPanel, gbc);
    }

    // Method to add the submit button to the form
    private void addSubmitButton() {
        JButton submitButton = new JButton(getSubmitButtonText());
        submitButton.setBorder(new RoundedBorder(10, Color.black, 2));
        submitButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1;
        gbc.gridy = 10;
        submitButton.addActionListener(e -> handleSubmit());
        mainPanel.add(submitButton, gbc);
    }

    // Method to handle back button action
    protected void handleBackAction() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?",
            "Return to Inventory Page",
            JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new DrinkManagementDashboard().setVisible(true);
        }
    }
}

// Concrete implementation for adding drinks
class AddDrinkForm extends DrinkManagementForm {
    public AddDrinkForm() {
        super("Add New Drink");
    }

    @Override
    // Set up form fields for adding a new drink
    protected void setupForm() {
        addFormField("DrinkId", "drinkIdField", 0);
        addFormField("Drink Name", "drinkNameField", 1);
        addAlcoholicOption(2);
        addFormField("Unit Cost", "unitCostField", 3);
        addFormField("Quantity", "quantityField", 4);
    }

    @Override
    // Handle form submission for adding a new drink
    protected void handleSubmit() {
        // -------------------------------------------------------- //
        JTextField drinkIdField = (JTextField) components.get("drinkIdField");
        String drinkId = drinkIdField.getText();

        JTextField drinkNameField = (JTextField) components.get("drinkNameField");
        String drinkName = drinkNameField.getText();

        JRadioButton yesButton = (JRadioButton) components.get("Yes");
        boolean isAlcoholic = yesButton.isSelected() == true ? true : false;

        JTextField unitCostField = (JTextField) components.get("unitCostField");
        int unitCost = Integer.parseInt(unitCostField.getText());

        JTextField quantityField = (JTextField) components.get("quantityField");
        int quantity = Integer.parseInt(quantityField.getText()) ;

        if (MangerController.addDrink(drinkId, drinkName, unitCost, isAlcoholic, quantity)) {
            JOptionPane.showMessageDialog(this, "Drink was Added.");
        }else{
            JOptionPane.showMessageDialog(this, "Failed to Add drink");
        };
    }

    @Override
    // Get the text for the submit button
    protected String getSubmitButtonText() {
        return "Add Drink";
    }
}

// Concrete implementation for modifying drinks
class ModifyDrinkForm extends DrinkManagementForm {

    public ModifyDrinkForm() {
        super("Modify Drink");
    }

    @Override
    // Set up form fields for modifying an existing drink
    protected void setupForm() {
        addFormField("DrinkId", "drinkIdField", 0);
        addFormField("Drink Name", "drinkNameField", 1);
        addAlcoholicOption(2);
        addFormField("Unit Cost", "unitCostField", 3);
        addFormField("Quantity", "quantityField", 4);
    }

    @Override
    // Handle form submission for modifying an existing drink
    protected void handleSubmit() {
        // -------------------------------------------------------- //
        JTextField drinkIdField = (JTextField) components.get("drinkIdField");
        String drinkId = drinkIdField.getText();

        JTextField drinkNameField = (JTextField) components.get("drinkNameField");
        String drinkName = drinkNameField.getText();

        JRadioButton yesButton = (JRadioButton) components.get("Yes");
        boolean isAlcoholic = yesButton.isSelected() == true ? true : false;

        JTextField unitCostField = (JTextField) components.get("unitCostField");
        int unitCost = Integer.parseInt(unitCostField.getText());

        JTextField quantityField = (JTextField) components.get("quantityField");
        int quantity = Integer.parseInt(quantityField.getText());

        if (MangerController.updateDrink(drinkId, drinkName, unitCost, isAlcoholic, quantity)) {
            JOptionPane.showMessageDialog(this, "Drink with ID " + drinkId + " has been Updated.");
        }else{
            JOptionPane.showMessageDialog(this, "Failed to Updated drink with ID " + drinkId);
        };
    }

    @Override
    protected String getSubmitButtonText() {
        return "Modify Drink";
    }
}

// Concrete implementation for removing drinks
class RemoveDrinkForm extends DrinkManagementForm {
    public RemoveDrinkForm() {
        super("Remove Drink");
    }

    @Override
    protected void setupForm() {
        // Only need drink ID for removal
        addFormField("DrinkId", "drinkIdField", 0);
    }

    @Override
    protected void handleSubmit() {
        // -------------------------------------------------------- //
        JTextField drinkIdField = (JTextField) components.get("drinkIdField");
        String drinkId = drinkIdField.getText();

        if(drinkId.isEmpty()){
            JOptionPane.showMessageDialog(this, "Drink ID field cannot be empty.");
            return;
        }

        if (MangerController.deleteDrink(drinkId)) {
            JOptionPane.showMessageDialog(this, "Drink with ID " + drinkId + " has been removed.");
        }else{
            JOptionPane.showMessageDialog(this, "Failed to delete drink with ID " + drinkId);
        };
    }

    @Override
    protected String getSubmitButtonText() {
        return "Remove Drink";
    }
}

// Concrete implementation for removing drinks
class PullDrinkForm extends DrinkManagementForm {
    public PullDrinkForm() {
        super("Pull Drink");
    }

    @Override
    protected void setupForm() {
        // Only need drink ID for removal
        addFormField("DrinkId", "drinkIdField", 0);
    }

    @Override
    protected void handleSubmit() {
        // -------------------------------------------------------- //
        JTextField drinkIdField = (JTextField) components.get("drinkIdField");
        String drinkId = drinkIdField.getText();
        Map<String, Drink> drinks;
        if (drinkId.isEmpty()) { // if it's empty  then get all the drink
            drinks = MangerController.getDrinks(false);

            if (drinks == null) {
                JOptionPane.showMessageDialog(this, "No Drinks were found.");
                return;
            }
        }else{
            Drink drink = MangerController.getDrink(drinkId); // get the drink
            if (drink == null) {
                JOptionPane.showMessageDialog(this, "Drink not found.");
                return;
            }else {
                drinks = new HashMap<String, Drink>();
                drinks.put(drink.getDrinkID(), drink); // add it the drinks map, so it can displayed by the table
            }
        }

        displayDrinks(drinks);
    }

    protected void displayDrinks(Map<String, Drink> drinks) {
        Color LIGHT_GREEN = new Color(144, 238, 144);
        // Create a DefaultTableModel with column names
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{
            "ID", "Name", "Price", "isAlcoholic", "Quantity"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };
    

        // Populate the DefaultTableModel with drink data
        for (Map.Entry<String, Drink> entry : drinks.entrySet()) {
            String iD = entry.getValue().getDrinkID();
            String name = entry.getValue().getDrinkName();
            Boolean isAlcoholic = entry.getValue().getIsAlcoholic();
            double price = entry.getValue().getUnitPrice();
            double quantity = entry.getValue().getQuantity();
            tableModel.addRow(new Object[]{iD, name, isAlcoholic, price, quantity});
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

        mainPanel.setVisible(false);
        scrollPane.setVisible(true);
    }

    @Override
    protected String getSubmitButtonText() {
        return "Pull Drink";
    }
}

// Factory class to create appropriate form instances
public class DrinkManagementFormFactory {
    public static DrinkManagementForm createForm(String operation) {
        return switch (operation.toLowerCase()) {
            case "add" -> new AddDrinkForm();
            case "modify" -> new ModifyDrinkForm();
            case "remove" -> new RemoveDrinkForm();
            case "pull" -> new PullDrinkForm();
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
    } // End of createForm method
} // End of DrinkManagementFormFactory class