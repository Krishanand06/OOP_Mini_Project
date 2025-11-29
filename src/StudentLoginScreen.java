
import javax.swing.*;
import java.awt.*;

/**
 * Student Login Screen
 * Allows students to log in using their roll number and password.
 */

public class StudentLoginScreen extends JFrame {

    private JTextField rollNofield;
    private JPasswordField passwordField;

    public StudentLoginScreen() {
        setTitle("Student Login  Campus Café");
        setSize(450, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        JLabel titleLabel = new JLabel("Student Login");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Welcome to Campus Café");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Roll Number Label
        JLabel rollLabel = new JLabel("Roll Number:");
        rollLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        rollLabel.setForeground(new Color(52, 58, 64));

        // Password Label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        passLabel.setForeground(new Color(52, 58, 64));

        // Text Fields with enhanced styling
        rollNofield = new JTextField(30);
        rollNofield.setFont(new Font("SansSerif", Font.PLAIN, 14));
        rollNofield.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        passwordField = new JPasswordField(30);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        // Add components to form
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(rollLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 15, 10);
        formPanel.add(rollNofield, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        formPanel.add(passLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 10, 10);
        formPanel.add(passwordField, gbc);

        // --- Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.setBackground(new Color(245, 247, 250));

        // Create flat design buttons
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("New Student? Register");
        JButton btnBack = new JButton("Back to Main Menu");

        // Style Login button (primary)
        btnLogin.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnLogin.setPreferredSize(new Dimension(120, 38));
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setForeground(new Color(55, 55, 55));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Register button (primary)
        btnRegister.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnRegister.setPreferredSize(new Dimension(180, 38));
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setForeground(new Color(55, 55, 55));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Back button (secondary)
        btnBack.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnBack.setPreferredSize(new Dimension(160, 35));
        btnBack.setBackground(new Color(245, 245, 245));
        btnBack.setForeground(new Color(85, 85, 85));
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        UIUtils.addHoverEffect(btnLogin, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(btnRegister, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(btnBack, new Color(245, 245, 245), new Color(235, 235, 235));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBackground(new Color(245, 247, 250));
        backPanel.add(btnBack);

        // --- Assemble Layout ---
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(245, 247, 250));
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(backPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // --------- Button Actions ---------

        // Login button
        btnLogin.addActionListener(e -> handleLogin());

        // Allow Enter key to trigger login
        passwordField.addActionListener(e -> handleLogin());

        // Register button
        btnRegister.addActionListener(e -> {
            dispose();
            StudentRegistrationScreen regScreen = new StudentRegistrationScreen();
            regScreen.setVisible(true);
        });

        // Back button
        btnBack.addActionListener(e -> {
            dispose();
            MainMenuScreen mainMenu = new MainMenuScreen();
            mainMenu.setVisible(true);
        });
    }

    private void handleLogin() {
        String rollNo = rollNofield.getText().trim();
        String password = new String(passwordField.getPassword());

        if (rollNo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both roll number and password.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Authenticate against database
        Student student = DatabaseManager.authenticateStudent(rollNo, password);

        if (student != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login successful!\nWelcome " + student.getName(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Set current student
            StudentMenuScreen.currentStudent = student;

            // Load wallet from database
            WalletContext.setStudentWallet(student.getUserId());

            StudentMenuScreen sms = new StudentMenuScreen();
            sms.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid roll number or password.\nPlease try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText(""); // Clear password field
        }
    }

}
