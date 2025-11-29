
import javax.swing.*;
import java.awt.*;

import java.util.*;
import java.util.List;

/**
 * Admin screen to view, approve and reject catering requests.
 * NOW LOADS FROM DATABASE via DatabaseManager.
 */

public class CateringAdminScreen extends JFrame {

    private DefaultListModel<String> listModel;
    private JList<String> requestList;
    private List<String> allLines = new ArrayList<>();

    public CateringAdminScreen() {
        setTitle("Admin – Manage Catering Requests");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        buildGui();
        loadRequests();

        setVisible(true);
    }

    private void buildGui() {
        listModel = new DefaultListModel<>();
        requestList = new JList<>(listModel);
        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(requestList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Catering Requests (ID | Event | Date | Status)"));

        JButton btnViewDetails = new JButton("View Details");
        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClose = new JButton("Close");

        // Style View Details button (primary)
        btnViewDetails.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnViewDetails.setBackground(Color.WHITE);
        btnViewDetails.setForeground(new Color(55, 55, 55));
        btnViewDetails.setFocusPainted(false);
        btnViewDetails.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnViewDetails.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Approve button (success)
        btnApprove.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnApprove.setBackground(new Color(232, 245, 233)); // Light green background
        btnApprove.setForeground(new Color(46, 125, 50)); // Dark green text
        btnApprove.setFocusPainted(false);
        btnApprove.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(165, 214, 167), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnApprove.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Reject button (danger)
        btnReject.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnReject.setBackground(new Color(255, 235, 238)); // Light red background
        btnReject.setForeground(new Color(198, 40, 40)); // Dark red text
        btnReject.setFocusPainted(false);
        btnReject.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(239, 154, 154), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnReject.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        UIUtils.addHoverEffect(btnViewDetails, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(btnApprove, new Color(232, 245, 233), new Color(200, 230, 201));
        UIUtils.addHoverEffect(btnReject, new Color(255, 235, 238), new Color(255, 205, 210));
        UIUtils.addHoverEffect(btnRefresh, new Color(245, 245, 245), new Color(235, 235, 235));
        UIUtils.addHoverEffect(btnClose, new Color(245, 245, 245), new Color(235, 235, 235));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnViewDetails);
        buttonPanel.add(btnApprove);
        buttonPanel.add(btnReject);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClose);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // ---- Actions ----
        btnViewDetails.addActionListener(e -> handleViewDetails());
        btnApprove.addActionListener(e -> handleChangeStatus("APPROVED"));
        btnReject.addActionListener(e -> handleChangeStatus("REJECTED"));
        btnRefresh.addActionListener(e -> loadRequests());
        btnClose.addActionListener(e -> dispose());
    }

    /**
     * Load all requests from database into the list.
     */
    private void loadRequests() {
        listModel.clear();
        allLines.clear();

        // Load from DatabaseManager
        allLines = DatabaseManager.loadCateringRequests();

        if (allLines.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No catering requests found in database.",
                    "No Requests",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (String line : allLines) {
            // Format: ID|EventName|Date|Time|Venue|Attendees|Items|Status|Timestamp
            String[] parts = line.split("\\|");
            String id = parts.length > 0 ? parts[0] : "UNKNOWN_ID";
            String event = parts.length > 1 ? parts[1] : "UNKNOWN_EVENT";
            String date = parts.length > 2 ? parts[2] : "?";
            String status = parts.length > 7 ? parts[7] : "PENDING";

            String display = id + " | " + event + " | " + date + " | " + status;
            listModel.addElement(display);
        }
    }

    /**
     * Show full record in a dialog.
     */
    private void handleViewDetails() {
        int index = requestList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a request first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String line = allLines.get(index);
        String[] parts = line.split("\\|");

        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(parts.length > 0 ? parts[0] : "").append("\n");
        sb.append("Event: ").append(parts.length > 1 ? parts[1] : "").append("\n");
        sb.append("Date: ").append(parts.length > 2 ? parts[2] : "").append("\n");
        sb.append("Time: ").append(parts.length > 3 ? parts[3] : "").append("\n");
        sb.append("Venue: ").append(parts.length > 4 ? parts[4] : "").append("\n");
        sb.append("Attendees: ").append(parts.length > 5 ? parts[5] : "").append("\n");
        sb.append("Items: ").append(parts.length > 6 ? parts[6] : "").append("\n");
        sb.append("Status: ").append(parts.length > 7 ? parts[7] : "").append("\n");
        sb.append("Created: ").append(parts.length > 8 ? parts[8] : "").append("\n");

        JTextArea area = new JTextArea(sb.toString(), 10, 40);
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);

        JOptionPane.showMessageDialog(this, sp, "Catering Request Details", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Change status to APPROVED or REJECTED, then update database.
     */
    private void handleChangeStatus(String newStatus) {
        int index = requestList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a request first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String line = allLines.get(index);
        String[] parts = line.split("\\|");
        if (parts.length < 8) {
            JOptionPane.showMessageDialog(this,
                    "This record is not in the expected format and cannot be updated.",
                    "Format Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String requestId = parts[0];
        String oldStatus = parts[7];

        if (oldStatus.equalsIgnoreCase(newStatus)) {
            JOptionPane.showMessageDialog(this,
                    "Request is already " + newStatus + ".",
                    "No Change",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Change status from " + oldStatus + " to " + newStatus + "?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Update in Database
        DatabaseManager.updateCateringStatus(requestId, newStatus);

        JOptionPane.showMessageDialog(this,
                "Status updated to " + newStatus + ".",
                "Status Changed",
                JOptionPane.INFORMATION_MESSAGE);

        loadRequests(); // refresh list
    }

}
