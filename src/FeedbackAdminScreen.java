
import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

/**
 * FeedbackAdminScreen
 * -------------------
 * Admin view for feedback.txt
 * - Left: list of feedback entries
 * - Right: full details of selected entry
 */

public class FeedbackAdminScreen extends JFrame {

    // Simple holder class for one feedback record
    private static class FeedbackRecord {
        String id;
        String studentId;
        String category;
        int rating;
        String comments;
        String timestamp;
    }

    private JList<String> feedbackList;
    private DefaultListModel<String> listModel;
    private JTextArea detailsArea;

    private List<FeedbackRecord> feedbackData = new ArrayList<>();

    public FeedbackAdminScreen() {
        setTitle("Feedback Management – Admin View");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initComponents();
        loadFeedbackFromDatabase();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Feedback Records (from Database)");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(titleLabel, BorderLayout.NORTH);

        // ----- centre: list (left) + details (right) -----
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left: list of feedback entries
        listModel = new DefaultListModel<>();
        feedbackList = new JList<>(listModel);
        feedbackList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScroll = new JScrollPane(feedbackList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Feedback Entries"));
        centerPanel.add(listScroll);

        // Right: details area
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Feedback Details"));
        centerPanel.add(detailsScroll);

        add(centerPanel, BorderLayout.CENTER);

        // ----- bottom buttons -----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClose = new JButton("Close");

        // Style Refresh button (primary)
        btnRefresh.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setForeground(new Color(55, 55, 55));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
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
        UIUtils.addHoverEffect(btnRefresh, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(btnClose, new Color(245, 245, 245), new Color(235, 235, 235));

        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnClose);

        add(bottomPanel, BorderLayout.SOUTH);

        // ----- button actions -----
        btnRefresh.addActionListener(e -> loadFeedbackFromDatabase());
        btnClose.addActionListener(e -> dispose());

        // ----- list selection listener: auto-show details -----
        feedbackList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedFeedback();
            }
        });
    }

    /**
     * Loads feedback from database with student names.
     */
    private void loadFeedbackFromDatabase() {
        feedbackData.clear();
        listModel.clear();
        detailsArea.setText("");

        // Load feedback from database
        List<Feedback> feedbackFromDB = DatabaseManager.loadAllFeedback();

        if (feedbackFromDB.isEmpty()) {
            detailsArea.setText("No feedback records found in database.");
            return;
        }

        // Convert to FeedbackRecord and populate list
        for (Feedback fb : feedbackFromDB) {
            FeedbackRecord fr = new FeedbackRecord();
            fr.id = fb.getFeedbackId();
            fr.studentId = fb.getStudentId();
            fr.category = fb.getCategory().name();
            fr.rating = fb.getRating();
            fr.comments = fb.getComments();
            fr.timestamp = java.time.LocalDateTime.now().toString(); // Will be from created_at in real scenario

            feedbackData.add(fr);

            // Get student name from database
            String studentName = DatabaseManager.getStudentName(fb.getStudentId());

            // Prepare a short preview for the list
            String commentPreview;
            if (fr.comments == null || fr.comments.trim().isEmpty()) {
                commentPreview = "(no comment)";
            } else {
                commentPreview = fr.comments.length() > 50 ? fr.comments.substring(0, 47) + "..." : fr.comments;
            }

            String listText = fr.id + " | " + studentName + " (" + fr.studentId + ") | " +
                    fr.category + " | Rating: " + fr.rating + " | " + commentPreview;

            listModel.addElement(listText);
        }

        if (!feedbackData.isEmpty()) {
            feedbackList.setSelectedIndex(0);
        }
    }

    /**
     * Shows details of currently selected feedback entry in the right text area.
     */
    private void showSelectedFeedback() {
        int index = feedbackList.getSelectedIndex();
        if (index < 0 || index >= feedbackData.size()) {
            return;
        }

        FeedbackRecord fr = feedbackData.get(index);

        // Get student name from database
        String studentName = DatabaseManager.getStudentName(fr.studentId);

        String comments = fr.comments;
        if (comments == null || comments.trim().isEmpty()) {
            comments = "(no comments provided)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Feedback ID    : ").append(fr.id).append("\n");
        sb.append("Student ID     : ").append(fr.studentId).append("\n");
        sb.append("Student Name   : ").append(studentName).append("\n");
        sb.append("Category       : ").append(fr.category).append("\n");
        sb.append("Rating         : ").append(fr.rating).append("/5").append("\n\n");
        sb.append("Comments:\n").append(comments);

        detailsArea.setText(sb.toString());
        detailsArea.setCaretPosition(0);
    }

}
