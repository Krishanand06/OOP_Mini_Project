
import javax.swing.*;
import java.awt.*;

/**
 * Student Registration Screen
 * SIMPLIFIED: No mess plan selection (hostellers choose later)
 */

public class StudentRegistrationScreen extends JFrame {

    private JTextField rollNoField;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JRadioButton rbHosteller;
    private JRadioButton rbDayScholar;

    public StudentRegistrationScreen() {
        setTitle("Student Registration – Campus Café");
        setSize(500, 600);
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

        JLabel titleLabel = new JLabel("New Student Registration");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Join the Campus Café Community");
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

        // Helper to create styled labels
        Font labelFont = new Font("SansSerif", Font.BOLD, 13);
        Color labelColor = new Color(52, 58, 64);

        // Row 0: Roll Number
        JLabel lblRoll = new JLabel("Roll Number:");
        lblRoll.setFont(labelFont);
        lblRoll.setForeground(labelColor);

        rollNoField = createStyledTextField();

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblRoll, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 15, 10);
        formPanel.add(rollNoField, gbc);

        // Row 1: Name
        JLabel lblName = new JLabel("Full Name:");
        lblName.setFont(labelFont);
        lblName.setForeground(labelColor);

        nameField = createStyledTextField();

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        formPanel.add(lblName, gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 15, 10);
        formPanel.add(nameField, gbc);

        // Row 2: Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(labelColor);

        emailField = createStyledTextField();

        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 5, 10);
        formPanel.add(lblEmail, gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 10, 15, 10);
        formPanel.add(emailField, gbc);

        // Row 3: Password
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(labelFont);
        lblPass.setForeground(labelColor);

        passwordField = createStyledPasswordField();

        gbc.gridy = 6;
        gbc.insets = new Insets(10, 10, 5, 10);
        formPanel.add(lblPass, gbc);
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 10, 15, 10);
        formPanel.add(passwordField, gbc);

        // Row 4: Confirm Password
        JLabel lblConfirm = new JLabel("Confirm Password:");
        lblConfirm.setFont(labelFont);
        lblConfirm.setForeground(labelColor);

        confirmPasswordField = createStyledPasswordField();

        gbc.gridy = 8;
        gbc.insets = new Insets(10, 10, 5, 10);
        formPanel.add(lblConfirm, gbc);
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 10, 15, 10);
        formPanel.add(confirmPasswordField, gbc);

        // Row 5: Student Type
        JLabel lblType = new JLabel("Student Type:");
        lblType.setFont(labelFont);
        lblType.setForeground(labelColor);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        typePanel.setBackground(Color.WHITE);

        rbHosteller = new JRadioButton("Hosteller");
        rbDayScholar = new JRadioButton("Day Scholar");

        rbHosteller.setBackground(Color.WHITE);
        rbDayScholar.setBackground(Color.WHITE);
        rbHosteller.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rbDayScholar.setFont(new Font("SansSerif", Font.PLAIN, 13));

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(rbHosteller);
        typeGroup.add(rbDayScholar);
        rbDayScholar.setSelected(true); // Default

        typePanel.add(rbHosteller);
        typePanel.add(rbDayScholar);

        gbc.gridy = 10;
        gbc.insets = new Insets(10, 10, 5, 10);
        formPanel.add(lblType, gbc);
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 10, 15, 10);
        formPanel.add(typePanel, gbc);

        // Info message
        JLabel infoLabel = new JLabel("Note: Hostellers can choose mess plan after login");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        infoLabel.setForeground(new Color(108, 117, 125));

        gbc.gridy = 12;
        formPanel.add(infoLabel, gbc);

        // --- Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 247, 250));

        // Create flat design buttons
        JButton btnRegister = new JButton("Register");
        JButton btnCancel = new JButton("Cancel");

        // Style Register button (primary)
        btnRegister.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnRegister.setPreferredSize(new Dimension(140, 40));
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setForeground(new Color(55, 55, 55));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Cancel button (secondary)
        btnCancel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.setBackground(new Color(245, 245, 245));
        btnCancel.setForeground(new Color(85, 85, 85));
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        UIUtils.addHoverEffect(btnRegister, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(btnCancel, new Color(245, 245, 245), new Color(235, 235, 235));

        btnRegister.addActionListener(e -> handleRegistration());

        btnCancel.addActionListener(e -> {
            dispose();
            StudentLoginScreen loginScreen = new StudentLoginScreen();
            loginScreen.setVisible(true);
        });

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        // --- Assemble Layout ---
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Wrap form in scroll pane to allow scrolling
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null); // Remove default border
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Only vertical scrolling

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(25);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(25);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }

    private void handleRegistration() {
        // Get form values
        String rollNo = rollNoField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        boolean isHosteller = rbHosteller.isSelected();

        // Validation
        if (rollNo.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match. Please try again.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            passwordField.setText("");
            confirmPasswordField.setText("");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must be at least 6 characters long.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Generate user ID
        String userId = "STU" + String.format("%03d", (int) (Math.random() * 1000));

        // Create student object
        Student student;
        if (isHosteller) {
            student = new Hosteller(userId, name, email, rollNo, null); // No mess plan yet
        } else {
            student = new DayScholar(userId, name, email, rollNo);
        }

        // Register in database (no mess plan parameter)
        boolean success = DatabaseManager.registerStudent(student, password);

        if (success) {
            String message = "Registration successful!\n\nYour details:\nRoll No: " + rollNo +
                    "\nUser ID: " + userId + "\n\nYou can now log in.";

            if (isHosteller) {
                message += "\n\nNote: Choose your mess plan after logging in.";
            }

            JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Return to login screen
            dispose();
            StudentLoginScreen loginScreen = new StudentLoginScreen();
            loginScreen.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed. Roll number or email may already exist.\nPlease try again.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
