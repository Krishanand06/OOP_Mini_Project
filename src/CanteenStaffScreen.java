

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Staff view for canteen orders (queue management)
 * NOW LOADS FROM DATABASE with order items
 */

public class CanteenStaffScreen extends JFrame {

    // Order data class
    private static class OrderData {
        String id;
        String studentId;
        double total;
        Timestamp time;
        String status;
        List<String> items;

        OrderData(String id, String studentId, double total, Timestamp time, String status) {
            this.id = id;
            this.studentId = studentId;
            this.total = total;
            this.time = time;
            this.status = status;
            this.items = new ArrayList<>();
        }
    }

    private JList<String> orderList;
    private DefaultListModel<String> listModel;
    private JTextArea detailsArea;
    private JComboBox<String> filterCombo;
    private JLabel summaryLabel;

    private List<OrderData> allOrders = new ArrayList<>();
    private List<OrderData> filteredOrders = new ArrayList<>();

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

    public CanteenStaffScreen() {
        setTitle("Canteen Orders – Staff Queue");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadOrdersFromDatabase();
        applyFilterAndRefreshList();
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Top: filter + summary
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterCombo = new JComboBox<>(new String[] { "All Orders", "Today Only" });
        filterCombo.addActionListener(e -> applyFilterAndRefreshList());
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterCombo);
        topPanel.add(filterPanel, BorderLayout.WEST);

        summaryLabel = new JLabel("Loading...");
        topPanel.add(summaryLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center: list + details
        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedOrderDetails();
            }
        });

        JScrollPane listScroll = new JScrollPane(orderList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Orders (queue)"));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Order Details"));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        centerPanel.add(listScroll);
        centerPanel.add(detailsScroll);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom: buttons (NO View Details button)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton btnMarkPending = new JButton("Mark as PENDING");
        JButton btnMarkCompleted = new JButton("Mark as COMPLETED");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClose = new JButton("Close");

        // Style Mark Pending button (warning)
        btnMarkPending.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnMarkPending.setBackground(new Color(255, 243, 224)); // Light orange
        btnMarkPending.setForeground(new Color(230, 81, 0)); // Dark orange
        btnMarkPending.setFocusPainted(false);
        btnMarkPending.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 204, 128), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnMarkPending.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Mark Completed button (success)
        btnMarkCompleted.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnMarkCompleted.setBackground(new Color(232, 245, 233)); // Light green
        btnMarkCompleted.setForeground(new Color(46, 125, 50)); // Dark green
        btnMarkCompleted.setFocusPainted(false);
        btnMarkCompleted.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(165, 214, 167), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnMarkCompleted.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Refresh button (secondary)
        btnRefresh.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnRefresh.setBackground(new Color(245, 245, 245));
        btnRefresh.setForeground(new Color(85, 85, 85));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Close button (secondary)
        btnClose.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnClose.setBackground(new Color(245, 245, 245));
        btnClose.setForeground(new Color(85, 85, 85));
        btnClose.setFocusPainted(false);
        btnClose.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        UIUtils.addHoverEffect(btnMarkPending, new Color(255, 243, 224), new Color(255, 224, 178));
        UIUtils.addHoverEffect(btnMarkCompleted, new Color(232, 245, 233), new Color(200, 230, 201));
        UIUtils.addHoverEffect(btnRefresh, new Color(245, 245, 245), new Color(235, 235, 235));
        UIUtils.addHoverEffect(btnClose, new Color(245, 245, 245), new Color(235, 235, 235));

        bottomPanel.add(btnMarkPending);
        bottomPanel.add(btnMarkCompleted);
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnClose);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        btnMarkPending.addActionListener(e -> updateOrderStatus("PENDING"));
        btnMarkCompleted.addActionListener(e -> updateOrderStatus("COMPLETED"));
        btnRefresh.addActionListener(e -> {
            loadOrdersFromDatabase();
            applyFilterAndRefreshList();
        });
        btnClose.addActionListener(e -> dispose());
    }

    /**
     * Load all orders from database with items
     */
    private void loadOrdersFromDatabase() {
        allOrders.clear();

        String sql = "SELECT order_id, student_id, total_amount, created_at, status " +
                "FROM orders ORDER BY created_at ASC";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String orderId = rs.getString("order_id");
                String studentId = rs.getString("student_id");
                double total = rs.getDouble("total_amount");
                Timestamp time = rs.getTimestamp("created_at");
                String status = rs.getString("status");

                OrderData order = new OrderData(orderId, studentId, total, time, status);

                // Load order items
                order.items = DatabaseManager.loadOrderItems(orderId);

                allOrders.add(order);
            }

        } catch (SQLException e) {
            System.out.println("Error loading orders: " + e.getMessage());
            detailsArea.setText("Error loading orders from database:\n" + e.getMessage());
        }
    }

    private void applyFilterAndRefreshList() {
        filteredOrders.clear();

        boolean todayOnly = filterCombo.getSelectedIndex() == 1;
        LocalDate today = LocalDate.now();

        for (OrderData order : allOrders) {
            if (todayOnly) {
                LocalDate orderDate = order.time.toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                if (orderDate.equals(today)) {
                    filteredOrders.add(order);
                }
            } else {
                filteredOrders.add(order);
            }
        }

        // Update list
        listModel.clear();
        for (OrderData order : filteredOrders) {
            String timeStr = order.time.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime().format(DISPLAY_FORMAT);
            String display = order.id + " | " + order.total + " AED | " + timeStr + " | " + order.status;
            listModel.addElement(display);
        }

        updateSummaryLabel();

        // Auto-select first order
        if (filteredOrders.size() > 0) {
            orderList.setSelectedIndex(0);
        } else {
            detailsArea.setText("No orders found.");
        }
    }

    private void updateSummaryLabel() {
        int total = filteredOrders.size();
        int pending = 0;
        int completed = 0;

        for (OrderData order : filteredOrders) {
            if ("COMPLETED".equals(order.status)) {
                completed++;
            } else {
                pending++;
            }
        }

        summaryLabel.setText("Total: " + total + "   Pending: " + pending + "   Completed: " + completed);
    }

    private void showSelectedOrderDetails() {
        int index = orderList.getSelectedIndex();
        if (index < 0 || index >= filteredOrders.size()) {
            detailsArea.setText("Select an order to view details.");
            return;
        }

        OrderData order = filteredOrders.get(index);
        StringBuilder sb = new StringBuilder();

        String timeStr = order.time.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().format(DISPLAY_FORMAT);

        sb.append("Order ID: ").append(order.id).append("\n");
        sb.append("Student ID: ").append(order.studentId).append("\n");
        sb.append("Total Amount: ").append(order.total).append(" AED\n");
        sb.append("Time: ").append(timeStr).append("\n");
        sb.append("Status: ").append(order.status).append("\n\n");

        // Show items
        sb.append("=== Order Items ===\n");
        if (order.items.isEmpty()) {
            sb.append("  (No items found)\n");
        } else {
            for (String item : order.items) {
                sb.append("  • ").append(item).append("\n");
            }
        }

        sb.append("\nQueue Info: ");
        if ("COMPLETED".equals(order.status)) {
            sb.append("This order has been SERVED.");
        } else {
            sb.append("This order is PENDING.");
        }

        detailsArea.setText(sb.toString());
    }

    private void updateOrderStatus(String newStatus) {
        int index = orderList.getSelectedIndex();
        if (index < 0 || index >= filteredOrders.size()) {
            JOptionPane.showMessageDialog(this,
                    "Please select an order first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrderData order = filteredOrders.get(index);

        // Update in database
        boolean success = DatabaseManager.updateOrderStatus(order.id, OrderStatus.valueOf(newStatus));

        if (success) {
            order.status = newStatus;
            applyFilterAndRefreshList();

            // Keep same order selected if possible
            if (index < listModel.size()) {
                orderList.setSelectedIndex(index);
            }
            showSelectedOrderDetails();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update order status in database.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
