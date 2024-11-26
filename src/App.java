
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        // logger.debug("debug message");
        // logger.error("error message");
        // logger.trace("trace message");
        // logger.fatal("fatal message");
        // logger.warn("warn message");


        // JTextField textField = new JTextField(20);
        // int maxChars = 10;  // Set maximum number of characters
        
        // // Apply the custom DocumentFilter
        // ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
        //     @Override
        //     public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        //         if (fb.getDocument().getLength() + string.length() <= maxChars) {
        //             super.insertString(fb, offset, string, attr);
        //         }
        //     }
            
        //     @Override
        //     public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        //         if (fb.getDocument().getLength() - length + text.length() <= maxChars) {
        //             super.replace(fb, offset, length, text, attrs);
        //         }
        //     }
        // });

        // JFrame frame = new JFrame("Limited JTextField Example");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(300, 100);
        // frame.setLayout(new java.awt.FlowLayout());
        // frame.add(textField);
        // frame.setVisible(true);


        // Get the current year
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        
        // Set the starting year (e.g., 1900)
        int startYear = 1900;
        
        // Create a JComboBox with the years
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        for (int year = startYear; year <= currentYear; year++) {
            yearComboBox.addItem(year);
        }
        
        // Set up the JFrame
        JFrame frame = new JFrame("Year Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setLayout(new java.awt.FlowLayout());
        frame.add(new JLabel("Select a Year:"));
        frame.add(yearComboBox);
        frame.setVisible(true);
    }
}
