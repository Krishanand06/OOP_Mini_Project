

import javax.swing.*;
import java.awt.*;

import java.util.List;

/**
 * FeedbackScreen
 * Allows students to submit feedback and view their past submissions.
 * NOW INTEGRATED WITH DATABASE.
 */

public class FeedbackScreen extends JFrame {
    private JComboBox<FeedbackCategory> categoryCombo;
    private JComboBox<String> ratingCombo;
    private JTextArea commentsArea;

    public FeedbackScreen() {
        setTitle("Feedback – Campus Café");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildGui();
        setVisible(true);
    }

    private void buildGui() {
        // Title
        JLabel titleLabel = new JLabel("Submit Feedback");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Category
        JLabel categoryLabel = new JLabel("Category:");
        categoryCombo = new JComboBox<>(FeedbackCategory.values());

        // Rating
        JLabel ratingLabel = new JLabel("Rating (1–5):");
        ratingCombo = new JComboBox<>(new String[] { "1", "2", "3", "4", "5" });
        ratingCombo.setSelectedIndex(4); // Default to 5

        // Comments
        JLabel commentsLabel = new JLabel("Comments:");
        commentsArea = new JTextArea(5, 30);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        JScrollPane commentsScroll = new JScrollPane(commentsArea);

        // Buttons
        JButton submitButton = new JButton("Submit Feedback");
        JButton viewButton = new JButton("View My Feedback");
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

        // Style View button (primary)
        viewButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        viewButton.setBackground(Color.WHITE);
        viewButton.setForeground(new Color(55, 55, 55));
        viewButton.setFocusPainted(false);
        viewButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        UIUtils.addHoverEffect(viewButton, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(backButton, new Color(245, 245, 245), new Color(235, 235, 235));

        // Layout form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(categoryCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(ratingLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(ratingCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(commentsLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(commentsScroll, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(submitButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(backButton);

        // Main layout
        setLayout(new BorderLayout(10, 10));
        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        submitButton.addActionListener(e -> handleSubmit());
        viewButton.addActionListener(e -> handleViewFeedback());
        backButton.addActionListener(e -> {
            new StudentMenuScreen();
            dispose();
        });
    }

    // ----------------- Logic -----------------

    private void handleSubmit() {
        FeedbackCategory category = (FeedbackCategory) categoryCombo.getSelectedItem();
        String ratingStr = (String) ratingCombo.getSelectedItem();
        String comments = commentsArea.getText().trim();

        if (comments.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your comments.",
                    "Comments required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid rating.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String id = FeedbackContext.generateFeedbackId();

            // Use actual logged-in student ID
            Student currentStudent = StudentMenuScreen.currentStudent;
            String studentId = (currentStudent != null) ? currentStudent.getUserId() : "UNKNOWN";

            Feedback fb = new Feedback(id, studentId, category, rating, comments);

            // Save to Database
            DatabaseManager.insertFeedback(fb);

            JOptionPane.showMessageDialog(this,
                    "Thank you! Your feedback has been submitted.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            commentsArea.setText("");
            ratingCombo.setSelectedIndex(4); // Reset to 5
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error submitting feedback: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleViewFeedback() {
        Student currentStudent = StudentMenuScreen.currentStudent;
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "No student logged in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Feedback> list = DatabaseManager.loadStudentFeedback(currentStudent.getUserId());

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "You haven't submitted any feedback yet.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Feedback f : list) {
            sb.append("ID: ").append(f.getFeedbackId()).append("\n");
            sb.append("Category: ").append(f.getCategory()).append("\n");
            sb.append("Rating: ").append(f.getRating()).append("/5\n");
            sb.append("Comments: ").append(f.getComments()).append("\n");
            sb.append("--------------------------------------------------\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "My Submitted Feedback",
                JOptionPane.INFORMATION_MESSAGE);
    }

}
