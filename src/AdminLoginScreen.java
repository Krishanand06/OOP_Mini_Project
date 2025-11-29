
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AdminLoginScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLoginScreen() {
        setTitle("Admin Login – Campus Café");
        setSize(450, 500); // Increased height to prevent cut-off
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

        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Administrative Access");
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
        gbc.weightx = 1.0; // Allow stretching

        // Username Label
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        userLabel.setForeground(new Color(52, 58, 64));

        // Password Label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        passLabel.setForeground(new Color(52, 58, 64));

        // Text Fields with enhanced styling - Wider (30 columns)
        usernameField = new JTextField(30);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
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
        formPanel.add(userLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 15, 10);
        formPanel.add(usernameField, gbc);

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
        JButton btnBack = new JButton("Back to Main Menu");

        // Style Login button (primary)
        btnLogin.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnLogin.setPreferredSize(new Dimension(140, 38));
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setForeground(new Color(55, 55, 55));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        UIUtils.addHoverEffect(btnBack, new Color(245, 245, 245), new Color(235, 235, 235));

        buttonPanel.add(btnLogin);

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

        // Login button: check credentials against database
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Allow Enter key to trigger login
        passwordField.addActionListener(e -> handleLogin());

        // Back to main menu
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    MainMenuScreen mainMenu = new MainMenuScreen();
                    mainMenu.setVisible(true);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            AdminLoginScreen.this,
                            "Error opening Main Menu:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Authenticate against database
        Admin admin = DatabaseManager.authenticateAdmin(user, pass);

        if (admin != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login successful.\nWelcome " + admin.getName() + "!",
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE);

            try {
                AdminDashboardScreen dashboard = new AdminDashboardScreen();
                dashboard.setVisible(true);
                dispose(); // close login window
            } catch (Throwable ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error opening Admin Dashboard:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText(""); // Clear password field
        }
    }

}
