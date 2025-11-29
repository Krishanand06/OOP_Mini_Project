
import javax.swing.*;
import java.awt.*;

/**
 * Main menu for Campus Café system.
 * Buttons:
 * - Student Login
 * - Admin Login
 * - Exit
 */

public class MainMenuScreen extends JFrame {

    public MainMenuScreen() {
        setTitle("Campus Café – Main Menu");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        buildUI();
    }

    private void buildUI() {
        // --- Main Panel with subtle background ---
        JPanel mainPanel = new JPanel(new BorderLayout(0, 25));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // --- Header Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Campus Café");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Ordering & Food Management System");
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

        // Create buttons with minimalistic flat design
        JButton btnStudent = createStyledButton("Student Login", true);
        JButton btnAdmin = createStyledButton("Admin Login", true);
        JButton btnExit = createStyledButton("Exit", false);

        // Add buttons with spacing
        buttonPanel.add(btnStudent);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(btnAdmin);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(btnExit);

        // --- Footer ---
        JLabel footerLabel = new JLabel("Select an option to continue");
        footerLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        footerLabel.setForeground(new Color(134, 142, 150));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Assemble Layout ---
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // --- Button Actions ---

        // Student Login → opens StudentLoginScreen
        btnStudent.addActionListener(e -> {
            StudentLoginScreen sls = new StudentLoginScreen();
            sls.setVisible(true);
            dispose();
        });

        // Admin Login → opens AdminLoginScreen
        btnAdmin.addActionListener(e -> {
            AdminLoginScreen als = new AdminLoginScreen();
            als.setVisible(true);
        });

        // Exit
        btnExit.addActionListener(e -> System.exit(0));
    }

    /**
     * Helper method to create minimalistic flat buttons
     */
    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        button.setPreferredSize(new Dimension(350, 50));
        button.setMaximumSize(new Dimension(350, 50));
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
