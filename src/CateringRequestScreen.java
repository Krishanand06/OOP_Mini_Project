
import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen for club reps / students to submit event catering requests.
 * NOW SAVES TO DATABASE via DatabaseManager.
 */

public class CateringRequestScreen extends JFrame {
    private JTextField eventNameField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField venueField;
    private JTextField attendeesField;

    private JComboBox<String> itemCombo;
    private JTextField quantityField;
    private JTextArea itemsArea;

    private List<String> itemLines = new ArrayList<>();

    public CateringRequestScreen() {
        setTitle("Event Catering Request – Campus Café");
        setSize(650, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        buildGui();
        setVisible(true);
    }

    private void buildGui() {
        JLabel titleLabel = new JLabel("Event Catering Request (Club / Society)");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // ----- Event details (simple GridLayout 5x2) -----
        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        detailsPanel.add(new JLabel("Event Name:"));
        eventNameField = new JTextField(20);
        detailsPanel.add(eventNameField);

        detailsPanel.add(new JLabel("Date (e.g. 05-Dec-2025):"));
        dateField = new JTextField(15);
        detailsPanel.add(dateField);

        detailsPanel.add(new JLabel("Time (e.g. 18:00–20:00):"));
        timeField = new JTextField(15);
        detailsPanel.add(timeField);

        detailsPanel.add(new JLabel("Venue:"));
        venueField = new JTextField(15);
        detailsPanel.add(venueField);

        detailsPanel.add(new JLabel("Expected Attendees:"));
        attendeesField = new JTextField(5);
        detailsPanel.add(attendeesField);

        // ----- Catering items -----
        String[] cateringOptions = {
                "Veg Sandwich Box (10 pcs)",
                "Chicken Burger Combo (10 pcs)",
                "Pizza Slice Box (20 slices)",
                "Soft Drink Cans (24 pcs)",
                "Juice Bottles (20 pcs)",
                "Samosa Pack (25 pcs)"
        };

        itemCombo = new JComboBox<>(cateringOptions);
        quantityField = new JTextField("1", 4);
        JButton addItemButton = new JButton("Add Item");

        // Style Add Item button (primary)
        addItemButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        addItemButton.setBackground(Color.WHITE);
        addItemButton.setForeground(new Color(55, 55, 55));
        addItemButton.setFocusPainted(false);
        addItemButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        addItemButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UIUtils.addHoverEffect(addItemButton, Color.WHITE, new Color(248, 248, 248));

        JPanel itemInputPanel = new JPanel();
        itemInputPanel.add(new JLabel("Item:"));
        itemInputPanel.add(itemCombo);
        itemInputPanel.add(new JLabel("Quantity (boxes):"));
        itemInputPanel.add(quantityField);
        itemInputPanel.add(addItemButton);

        itemsArea = new JTextArea(7, 25);
        itemsArea.setEditable(false);
        itemsArea.setText("No items added.");
        JScrollPane itemsScroll = new JScrollPane(itemsArea);
        itemsScroll.setBorder(BorderFactory.createTitledBorder("Requested Items"));

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(new JLabel("Catering Items:"), BorderLayout.NORTH);
        rightPanel.add(itemInputPanel, BorderLayout.CENTER);
        rightPanel.add(itemsScroll, BorderLayout.SOUTH);

        // ----- Bottom buttons -----
        JButton submitButton = new JButton("Submit Catering Request");
        JButton clearButton = new JButton("Clear Items");
        JButton backButton = new JButton("Back to Student Menu");

        // Style Submit button (primary)
        submitButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        submitButton.setBackground(Color.WHITE);
        submitButton.setForeground(new Color(55, 55, 55));
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Clear button (secondary)
        clearButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        clearButton.setBackground(new Color(245, 245, 245));
        clearButton.setForeground(new Color(85, 85, 85));
        clearButton.setFocusPainted(false);
        clearButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Back button (secondary)
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backButton.setBackground(new Color(245, 245, 245));
        backButton.setForeground(new Color(85, 85, 85));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        UIUtils.addHoverEffect(submitButton, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(clearButton, new Color(245, 245, 245), new Color(235, 235, 235));
        UIUtils.addHoverEffect(backButton, new Color(245, 245, 245), new Color(235, 235, 235));

        JPanel bottomButtons = new JPanel();
        bottomButtons.add(submitButton);
        bottomButtons.add(clearButton);
        bottomButtons.add(backButton);

        // ----- Main layout -----
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.add(detailsPanel);
        centerPanel.add(rightPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomButtons, BorderLayout.SOUTH);

        // ----- Actions -----
        addItemButton.addActionListener(e -> handleAddItem());
        clearButton.addActionListener(e -> {
            itemLines.clear();
            refreshItemsArea();
        });
        submitButton.addActionListener(e -> handleSubmit());
        backButton.addActionListener(e -> {
            new StudentMenuScreen();
            dispose();
        });
    }

    private void handleAddItem() {
        String item = (String) itemCombo.getSelectedItem();
        String qtyText = quantityField.getText().trim();
        int qty;

        try {
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number.", "Invalid Quantity",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (qty <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be positive.", "Invalid Quantity",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String line = item + " x " + qty;
        itemLines.add(line);
        refreshItemsArea();
    }

    private void refreshItemsArea() {
        if (itemLines.isEmpty()) {
            itemsArea.setText("No items added.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : itemLines) {
                sb.append(s).append("\n");
            }
            itemsArea.setText(sb.toString());
        }
    }

    private void handleSubmit() {
        String eventName = eventNameField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String venue = venueField.getText().trim();
        String attendeesText = attendeesField.getText().trim();

        if (eventName.isEmpty() || date.isEmpty() || time.isEmpty() || venue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Event Name, Date, Time and Venue.", "Missing Details",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int attendees = 0;
        try {
            if (!attendeesText.isEmpty()) {
                attendees = Integer.parseInt(attendeesText);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Expected attendees must be a number.", "Invalid Number",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (itemLines.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one catering item.", "No Items",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String requestId = DatabaseManager.getNextCateringRequestId();
        String itemsJoined = String.join(",", itemLines);
        String status = "PENDING";

        // Save to Database
        DatabaseManager.insertCateringRequest(requestId, eventName, date, time, venue, attendees, itemsJoined, status);

        JOptionPane.showMessageDialog(this,
                "Catering request submitted.\nRequest ID: " + requestId,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        // Reset form
        eventNameField.setText("");
        dateField.setText("");
        timeField.setText("");
        venueField.setText("");
        attendeesField.setText("");
        itemLines.clear();
        refreshItemsArea();
    }

}
