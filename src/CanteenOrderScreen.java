

import javax.swing.*;
import java.awt.*;

import java.util.List;

/**
 * Canteen ordering screen.
 * Uses MenuItem + Order + WalletPayment with the shared student wallet.
 * NOW SAVES TO DATABASE via DatabaseManager.
 */

public class CanteenOrderScreen extends JFrame {

    private Wallet wallet;
    private Order currentOrder;
    private List<MenuItem> menuItems;

    private JList<String> menuList;
    private DefaultListModel<String> menuListModel;
    private JPanel cartPanel;
    private JLabel totalLabel;
    private JLabel walletLabel;
    private JTextField quantityField;

    public CanteenOrderScreen() {
        setTitle("Canteen Ordering");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        wallet = WalletContext.getStudentWallet();

        // Use logged-in student
        Student student = StudentMenuScreen.currentStudent;
        currentOrder = new Order(generateOrderId(), student);

        initialiseMenuItems();
        buildGui();

        refreshCart();
        updateWalletLabel();
        setVisible(true);
    }

    private String generateOrderId() {
        // Simple unique ID based on timestamp
        return "ORD-" + System.currentTimeMillis() % 100000;
    }

    // ---------------- GUI SETUP ----------------

    private void buildGui() {
        // ----- Top panel: title + wallet balance -----
        JLabel titleLabel = new JLabel("Canteen Ordering – Student");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        walletLabel = new JLabel();
        walletLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(titleLabel);
        topPanel.add(walletLabel);

        // ----- Left: menu list (organized by category) -----
        menuListModel = new DefaultListModel<>();

        // Group items by category
        String currentCategory = "";
        for (MenuItem item : menuItems) {
            String category = item.getCategory();
            if (!category.equals(currentCategory)) {
                menuListModel.addElement("━━━━ " + category.toUpperCase() + " ━━━━");
                currentCategory = category;
            }
            menuListModel
                    .addElement(item.getItemId() + " - " + item.getItemName() + " : " + item.getUnitPrice() + " AED");
        }

        menuList = new JList<>(menuListModel);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane menuScroll = new JScrollPane(menuList);
        menuScroll.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        menuScroll.setPreferredSize(new Dimension(320, 500));

        // ----- Middle: quantity + buttons -----
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(5, 1, 5, 5));

        JPanel qtyPanel = new JPanel();
        qtyPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField("1", 5);
        qtyPanel.add(quantityField);

        JButton addButton = new JButton("Add to Cart");
        JButton clearButton = new JButton("Clear Cart");
        JButton payButton = new JButton("Pay via Wallet");
        JButton backButton = new JButton("Back to Student Menu");

        // Style all buttons with flat design
        Dimension buttonSize = new Dimension(180, 35);

        // Add button (primary)
        addButton.setPreferredSize(buttonSize);
        addButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        addButton.setBackground(Color.WHITE);
        addButton.setForeground(new Color(55, 55, 55));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Clear button (secondary)
        clearButton.setPreferredSize(buttonSize);
        clearButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        clearButton.setBackground(new Color(245, 245, 245));
        clearButton.setForeground(new Color(85, 85, 85));
        clearButton.setFocusPainted(false);
        clearButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Pay button (primary)
        payButton.setPreferredSize(buttonSize);
        payButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        payButton.setBackground(Color.WHITE);
        payButton.setForeground(new Color(55, 55, 55));
        payButton.setFocusPainted(false);
        payButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        payButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        UIUtils.addHoverEffect(addButton, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(clearButton, new Color(245, 245, 245), new Color(235, 235, 235));
        UIUtils.addHoverEffect(payButton, Color.WHITE, new Color(248, 248, 248));

        middlePanel.add(qtyPanel);
        middlePanel.add(addButton);
        middlePanel.add(clearButton);
        middlePanel.add(payButton);

        // ----- Right: interactive cart with remove buttons -----
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(Color.WHITE);

        JScrollPane cartScroll = new JScrollPane(cartPanel);
        cartScroll.setBorder(BorderFactory.createTitledBorder("Current Order"));
        cartScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        cartScroll.setPreferredSize(new Dimension(280, 500)); // Make cart box longer

        totalLabel = new JLabel("Total: 0.0 AED");
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(cartScroll, BorderLayout.CENTER);
        rightPanel.add(totalLabel, BorderLayout.SOUTH);

        // ----- Bottom: back button -----
        // Style back button (secondary)
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backButton.setBackground(new Color(245, 245, 245));
        backButton.setForeground(new Color(85, 85, 85));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UIUtils.addHoverEffect(backButton, new Color(245, 245, 245), new Color(235, 235, 235));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);

        // ----- Layout -----
        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(menuScroll, BorderLayout.WEST);
        add(middlePanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // ----- Actions -----
        addButton.addActionListener(e -> handleAddToCart());
        clearButton.addActionListener(e -> {
            currentOrder.clearItems();
            refreshCart();
        });
        payButton.addActionListener(e -> handlePayment());
        backButton.addActionListener(e -> {
            new StudentMenuScreen();
            dispose();
        });
    }

    // --------------- MENU INITIALISATION ---------------

    private void initialiseMenuItems() {
        // Load from database instead of hardcoding
        menuItems = DatabaseManager.loadMenuItems();

        // Fallback if DB is empty
        if (menuItems.isEmpty()) {
            menuItems.add(new FoodItem(1, "Veg Sandwich", 10.0, "Snack", true, false));
            menuItems.add(new FoodItem(2, "Chicken Burger", 15.0, "Snack", false, true));
            menuItems.add(new FoodItem(3, "Paneer Thali", 20.0, "Main", true, false));
            menuItems.add(new DrinkItem(4, "Cold Coffee", 8.0, "Drink", true, false));
            menuItems.add(new DrinkItem(5, "Lemon Juice", 6.0, "Drink", true, true));
        }
    }

    // --------------- CART & PAYMENT LOGIC ---------------

    private void handleAddToCart() {
        int index = menuList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an item from the menu.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedText = menuListModel.getElementAt(index);

        // Skip category headers
        if (selectedText.contains("━━━━")) {
            JOptionPane.showMessageDialog(this,
                    "Please select an actual menu item, not a category header.",
                    "Invalid Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String qtyText = quantityField.getText().trim();
        int qty;
        try {
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Quantity must be a number.",
                    "Invalid Quantity",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (qty <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Quantity must be positive.",
                    "Invalid Quantity",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract item ID from the selected text (format: "ID - Name : Price AED")
        try {
            int itemId = Integer.parseInt(selectedText.split(" - ")[0].trim());

            // Find the MenuItem by ID
            MenuItem item = null;
            for (MenuItem mi : menuItems) {
                if (mi.getItemId() == itemId) {
                    item = mi;
                    break;
                }
            }

            if (item == null) {
                throw new Exception("Item not found");
            }

            currentOrder.addItem(item, qty);
            refreshCart();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error adding item: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshCart() {
        // Clear the cart panel
        cartPanel.removeAll();

        List<MenuItem> items = currentOrder.getItems();
        List<Integer> quantities = currentOrder.getQuantities();

        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel("Cart is empty.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            cartPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < items.size(); i++) {
                final int index = i;
                MenuItem item = items.get(i);
                int qty = quantities.get(i);
                double subtotal = item.getUnitPrice() * qty;

                // Create panel for this cart item
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BorderLayout(5, 5));
                itemPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)));
                itemPanel.setBackground(Color.WHITE);
                itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                // Item details
                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                detailsPanel.setBackground(Color.WHITE);

                JLabel nameLabel = new JLabel(item.getItemName());
                nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

                JLabel qtyLabel = new JLabel("Quantity: " + qty + " × " + item.getUnitPrice() + " AED");
                qtyLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
                qtyLabel.setForeground(Color.DARK_GRAY);

                JLabel subtotalLabel = new JLabel("Subtotal: " + String.format("%.2f", subtotal) + " AED");
                subtotalLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
                subtotalLabel.setForeground(new Color(0, 100, 0));

                detailsPanel.add(nameLabel);
                detailsPanel.add(Box.createVerticalStrut(3));
                detailsPanel.add(qtyLabel);
                detailsPanel.add(Box.createVerticalStrut(3));
                detailsPanel.add(subtotalLabel);

                // Remove button
                JButton removeBtn = new JButton("✕");
                removeBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
                removeBtn.setForeground(Color.RED);
                removeBtn.setPreferredSize(new Dimension(45, 45));
                removeBtn.setFocusPainted(false);
                removeBtn.setToolTipText("Remove 1 quantity");
                removeBtn.addActionListener(e -> {
                    currentOrder.removeItem(index);
                    refreshCart();
                });

                itemPanel.add(detailsPanel, BorderLayout.CENTER);
                itemPanel.add(removeBtn, BorderLayout.EAST);

                cartPanel.add(itemPanel);
            }
        }

        cartPanel.revalidate();
        cartPanel.repaint();
        totalLabel.setText("Total: " + String.format("%.2f", currentOrder.calculateBillAmount()) + " AED");
    }

    private void handlePayment() {
        double total = currentOrder.calculateBillAmount();
        if (total <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Your cart is empty.",
                    "No Items",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Pay " + total + " AED from wallet?",
                "Confirm Payment",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        WalletPayment payment = new WalletPayment(wallet);

        try {
            // 1. Process Payment (updates wallet object)
            payment.pay(currentOrder);

            // 2. Save Wallet Balance to DB
            WalletContext.saveStudentWallet();

            // 3. Save Order to DB
            boolean orderSaved = DatabaseManager.insertOrder(currentOrder);

            if (orderSaved) {
                // Generate receipt
                try {
                    Student student = StudentMenuScreen.currentStudent;
                    String receiptPath = ReceiptGenerator.generateReceipt(currentOrder, student, total);

                    // Show success message with receipt info
                    int openReceipt = JOptionPane.showConfirmDialog(this,
                            "Payment successful!\n" +
                                    "Order ID: " + currentOrder.getOrderId() + "\n" +
                                    "Receipt saved to: " + receiptPath + "\n\n" +
                                    "Would you like to open the receipt?",
                            "Success",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);

                    if (openReceipt == JOptionPane.YES_OPTION) {
                        ReceiptGenerator.openReceipt(receiptPath);
                    }
                } catch (Exception ex) {
                    System.err.println("Failed to generate receipt: " + ex.getMessage());
                    // Still show success even if receipt fails
                    JOptionPane.showMessageDialog(this,
                            "Payment successful. Order ID: " + currentOrder.getOrderId() + "\n" +
                                    "(Receipt generation failed)",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                updateWalletLabel();

                // Start a fresh order
                Student student = StudentMenuScreen.currentStudent;
                currentOrder = new Order(generateOrderId(), student);
                refreshCart();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Payment processed but failed to save order to database.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (InvalidAmountException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Invalid Amount",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InsufficientBalanceException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Insufficient Balance",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateWalletLabel() {
        walletLabel.setText("Wallet Balance: " + wallet.getBalance() + " AED");
    }

}
