
import javax.swing.*;
import java.awt.*;

/**
 * AdminDashboardScreen
 * --------------------
 * From here Admin can:
 * - View Canteen Orders / Queue (CanteenStaffScreen)
 * - Manage Catering Requests (CateringAdminScreen)
 * - View Feedback (FeedbackAdminScreen)
 */

public class AdminDashboardScreen extends JFrame {

    private JButton btnViewOrders;
    private JButton btnManageCatering;
    private JButton btnViewFeedback;
    private JButton btnClose;

    // Multithreading components
    private PopularityAnalyzer analyzer;
    private TrendingItemsPanel trendingPanel;

    public AdminDashboardScreen() {
        setTitle("Admin Dashboard – Campus Café");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Initialize analyzer with listener
        analyzer = new PopularityAnalyzer();

        initComponents();

        // Set up analyzer listener after UI is built
        analyzer.setListener(items -> {
            trendingPanel.updateTrendingItems(items);
        });

        // Auto-start analyzer (updates every 60 seconds)
        analyzer.start(60);

        // Cleanup on window close
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (analyzer != null) {
                    analyzer.stop();
                }
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Admin Dashboard – Campus Café");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Main panel with buttons and trending items side by side
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left side: Admin buttons
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Admin Functions"));

        btnViewOrders = new JButton("View Canteen Orders / Queue");
        btnManageCatering = new JButton("Manage Catering Requests");
        btnViewFeedback = new JButton("View Feedback");
        JButton btnViewMessPlans = new JButton("View Mess Plan Subscriptions"); // New Button
        btnClose = new JButton("Back to Main Menu"); // Changed text

        // Style View Orders button (primary)
        btnViewOrders.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnViewOrders.setBackground(Color.WHITE);
        btnViewOrders.setForeground(new Color(55, 55, 55));
        btnViewOrders.setFocusPainted(false);
        btnViewOrders.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnViewOrders.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Manage Catering button (primary)
        btnManageCatering.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnManageCatering.setBackground(Color.WHITE);
        btnManageCatering.setForeground(new Color(55, 55, 55));
        btnManageCatering.setFocusPainted(false);
        btnManageCatering.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnManageCatering.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style View Feedback button (primary)
        btnViewFeedback.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnViewFeedback.setBackground(Color.WHITE);
        btnViewFeedback.setForeground(new Color(55, 55, 55));
        btnViewFeedback.setFocusPainted(false);
        btnViewFeedback.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnViewFeedback.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style View Mess Plans button (primary)
        btnViewMessPlans.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnViewMessPlans.setBackground(Color.WHITE);
        btnViewMessPlans.setForeground(new Color(55, 55, 55));
        btnViewMessPlans.setFocusPainted(false);
        btnViewMessPlans.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnViewMessPlans.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        UIUtils.addHoverEffect(btnViewOrders, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(btnManageCatering, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(btnViewFeedback, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(btnViewMessPlans, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(btnClose, new Color(245, 245, 245), new Color(235, 235, 235));

        centerPanel.add(btnViewOrders);
        centerPanel.add(btnManageCatering);
        centerPanel.add(btnViewFeedback);
        centerPanel.add(btnViewMessPlans);

        // Right side: Trending Items Panel (with multithreading)
        trendingPanel = new TrendingItemsPanel();

        mainPanel.add(centerPanel);
        mainPanel.add(trendingPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Bottom panel with refresh and close buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton btnRefreshTrending = new JButton("🔄 Refresh Trending Items");
        btnRefreshTrending.setToolTipText("Manually refresh the trending items list");

        // Style Refresh button (secondary)
        btnRefreshTrending.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnRefreshTrending.setBackground(new Color(245, 245, 245));
        btnRefreshTrending.setForeground(new Color(85, 85, 85));
        btnRefreshTrending.setFocusPainted(false);
        btnRefreshTrending.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnRefreshTrending.setCursor(new Cursor(Cursor.HAND_CURSOR));
        UIUtils.addHoverEffect(btnRefreshTrending, new Color(245, 245, 245), new Color(235, 235, 235));

        bottomPanel.add(btnRefreshTrending);
        bottomPanel.add(btnClose);
        add(bottomPanel, BorderLayout.SOUTH);

        // ---- Button actions ----

        // 1) Canteen orders queue (CanteenStaffScreen)
        btnViewOrders.addActionListener(e -> {
            try {
                CanteenStaffScreen staffScreen = new CanteenStaffScreen();
                staffScreen.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error opening Canteen Staff screen:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2) Catering requests (CateringAdminScreen)
        btnManageCatering.addActionListener(e -> {
            try {
                CateringAdminScreen cas = new CateringAdminScreen();
                cas.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error opening Catering Admin screen:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // 3) Feedback admin view (FeedbackAdminScreen)
        btnViewFeedback.addActionListener(e -> {
            try {
                FeedbackAdminScreen fas = new FeedbackAdminScreen();
                fas.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error opening Feedback Admin screen:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // 4) Mess Plan admin view (MessPlanAdminScreen)
        btnViewMessPlans.addActionListener(e -> {
            try {
                MessPlanAdminScreen mpas = new MessPlanAdminScreen();
                mpas.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error opening Mess Plan Admin screen:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // 5) Refresh Trending Items
        btnRefreshTrending.addActionListener(e -> {
            trendingPanel.setStatus("Refreshing...");
            analyzer.analyzeNow();
        });

        // 6) Back to Main Menu
        btnClose.addActionListener(e -> {
            new MainMenuScreen().setVisible(true);
            dispose();
        });
    }

}
