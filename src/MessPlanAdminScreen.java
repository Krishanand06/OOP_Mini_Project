
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MessPlanAdminScreen - Admin view for mess plan subscriptions
 * NOW LOADS FROM DATABASE
 */

public class MessPlanAdminScreen extends JFrame {

    private JList<String> subscriptionList;
    private DefaultListModel<String> listModel;
    private JTextArea detailsArea;
    private List<String[]> subscriptionData;

    public MessPlanAdminScreen() {
        setTitle("Mess Plan Subscriptions – Admin View");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        subscriptionData = new ArrayList<>();
        buildUI();
        loadSubscriptions();

        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Active Mess Plan Subscriptions");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Center: List + Details
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: List
        listModel = new DefaultListModel<>();
        subscriptionList = new JList<>(listModel);
        subscriptionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subscriptionList.addListSelectionListener(e -> showDetails());

        JScrollPane listScroll = new JScrollPane(subscriptionList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Subscriptions"));
        centerPanel.add(listScroll);

        // Right: Details
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Subscription Details"));
        centerPanel.add(detailsScroll);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        // Style Refresh button (primary)
        refreshButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setForeground(new Color(55, 55, 55));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Close button (secondary)
        closeButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        closeButton.setBackground(new Color(245, 245, 245));
        closeButton.setForeground(new Color(85, 85, 85));
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        UIUtils.addHoverEffect(refreshButton, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(closeButton, new Color(245, 245, 245), new Color(235, 235, 235));

        refreshButton.addActionListener(e -> loadSubscriptions());
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Load subscriptions from DATABASE
     */
    private void loadSubscriptions() {
        listModel.clear();
        subscriptionData.clear();
        detailsArea.setText("");

        String sql = "SELECT sms.student_id, s.name, mp.plan_name, mp.monthly_cost, " +
                "sms.start_date, sms.end_date, sms.status " +
                "FROM student_mess_subscriptions sms " +
                "JOIN students s ON sms.student_id = s.student_id " +
                "JOIN mess_plans mp ON sms.plan_id = mp.plan_id " +
                "WHERE sms.status = 'ACTIVE' " +
                "ORDER BY sms.start_date DESC";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                detailsArea.setText("No active subscriptions found in database.");
                return;
            }

            while (rs.next()) {
                String[] data = new String[7];
                data[0] = rs.getString("student_id");
                data[1] = rs.getString("name");
                data[2] = rs.getString("plan_name");
                data[3] = String.valueOf(rs.getDouble("monthly_cost"));
                data[4] = rs.getString("start_date");
                data[5] = rs.getString("end_date");
                data[6] = rs.getString("status");

                subscriptionData.add(data);

                // Display: StudentID - Name - PlanName
                listModel.addElement(data[0] + " - " + data[1] + " - " + data[2]);
            }

            if (subscriptionData.size() > 0) {
                subscriptionList.setSelectedIndex(0);
            }

        } catch (SQLException e) {
            System.out.println("Error loading subscriptions: " + e.getMessage());
            detailsArea.setText("Error loading from database:\n" + e.getMessage());
        }
    }

    private void showDetails() {
        int index = subscriptionList.getSelectedIndex();
        if (index >= 0 && index < subscriptionData.size()) {
            String[] data = subscriptionData.get(index);
            // studentId, studentName, planName, price, startDate, endDate, status
            StringBuilder sb = new StringBuilder();
            sb.append("Student ID   : ").append(data[0]).append("\n");
            sb.append("Student Name : ").append(data[1]).append("\n");
            sb.append("Plan Name    : ").append(data[2]).append("\n");
            sb.append("Price        : ").append(data[3]).append(" AED/month\n");
            sb.append("Start Date   : ").append(data[4]).append("\n");
            sb.append("End Date     : ").append(data[5]).append("\n");
            sb.append("Status       : ").append(data[6]).append("\n");

            detailsArea.setText(sb.toString());
        }
    }

}
