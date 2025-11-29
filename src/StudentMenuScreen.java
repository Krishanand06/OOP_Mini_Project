
import javax.swing.*;
import java.awt.*;


public class StudentMenuScreen extends JFrame {
    public static Student currentStudent; // Holds the currently logged-in student

    public StudentMenuScreen() {
        setTitle("Student Menu – Campus Café");
        setSize(500, 600); // Increased size for better spacing
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        buildUI();
    }

    private void buildUI() {
        // --- Main Panel with subtle background ---
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Student Menu");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String subtitleText = "Welcome Back";
        if (currentStudent != null) {
            titleLabel.setText("Hello, " + currentStudent.getName());
            subtitleText = currentStudent.getUserType() + " Dashboard";
        }

        JLabel subtitleLabel = new JLabel(subtitleText);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Create minimalistic flat buttons
        JButton btnCanteen = createStyledButton("Canteen Ordering", true);
        JButton btnWallet = createStyledButton("Wallet & Transactions", true);
        JButton btnFeedback = createStyledButton("Give Feedback", true);
        JButton btnCatering = createStyledButton("Event Catering (Clubs)", true);
        JButton btnMessPlan = createStyledButton("Mess Plan Subscription", true);
        JButton btnBack = createStyledButton("Back to Main Menu", false);

        // Add buttons with spacing
        buttonPanel.add(btnCanteen);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(btnWallet);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(btnFeedback);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(btnCatering);

        // Conditional Mess Plan Button
        if (currentStudent instanceof Hosteller) {
            buttonPanel.add(Box.createVerticalStrut(12));
            buttonPanel.add(btnMessPlan);
        }

        buttonPanel.add(Box.createVerticalStrut(25)); // Extra spacing before back button
        buttonPanel.add(btnBack);

        // --- Assemble Layout ---
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        // --- Event Handlers ---

        btnCanteen.addActionListener(e -> {
            new CanteenOrderScreen();
            dispose();
        });

        btnWallet.addActionListener(e -> {
            new WalletScreen();
            dispose();
        });

        btnFeedback.addActionListener(e -> {
            new FeedbackScreen();
            dispose();
        });

        btnCatering.addActionListener(e -> {
            new CateringRequestScreen();
            dispose();
        });

        btnMessPlan.addActionListener(e -> {
            new MessPlanScreen();
            dispose();
        });

        btnBack.addActionListener(e -> {
            new MainMenuScreen().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    /**
     * Helper method to create minimalistic flat buttons
     */
    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(380, 45));
        button.setMaximumSize(new Dimension(380, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (isPrimary) {
            // Primary button - white background with dark text
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(55, 55, 55));
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            UIUtils.addHoverEffect(button, Color.WHITE, new Color(248, 248, 248));
        } else {
            // Secondary button - light grey background
            button.setBackground(new Color(245, 245, 245));
            button.setForeground(new Color(85, 85, 85));
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            UIUtils.addHoverEffect(button, new Color(245, 245, 245), new Color(235, 235, 235));
        }

        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }
}
