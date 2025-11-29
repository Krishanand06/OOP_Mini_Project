
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MessPlanScreen extends JFrame {

    private JRadioButton basicRadio, premiumRadio, weekendRadio;
    private ButtonGroup planGroup;
    private JLabel priceLabel, durationLabel, descLabel;
    private JButton subscribeButton, backButton;

    // Plan objects to get details
    private BasicPlan basicPlan = new BasicPlan();
    private PremiumPlan premiumPlan = new PremiumPlan();
    private WeekendPlan weekendPlan = new WeekendPlan();

    private MealPlan selectedPlan = basicPlan; // Default

    public MessPlanScreen() {
        setTitle("Mess Plan Subscription – Campus Café");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        buildUI();
        updateDetails(); // Show default details

        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("Subscribe to a Mess Plan");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel: Plan Selection & Details
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 1. Selection Panel
        JPanel selectionPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Choose a Plan"));

        basicRadio = new JRadioButton("Basic Plan", true);
        premiumRadio = new JRadioButton("Premium Plan");
        weekendRadio = new JRadioButton("Weekend Plan");

        planGroup = new ButtonGroup();
        planGroup.add(basicRadio);
        planGroup.add(premiumRadio);
        planGroup.add(weekendRadio);

        selectionPanel.add(basicRadio);
        selectionPanel.add(premiumRadio);
        selectionPanel.add(weekendRadio);

        centerPanel.add(selectionPanel);

        // 2. Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Plan Details"));

        priceLabel = new JLabel("Price: ");
        durationLabel = new JLabel("Duration: 30 Days");
        descLabel = new JLabel("Description: ");

        detailsPanel.add(priceLabel);
        detailsPanel.add(durationLabel);
        detailsPanel.add(descLabel);

        centerPanel.add(detailsPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        subscribeButton = new JButton("Subscribe");
        backButton = new JButton("Back to Student Menu");

        // Style Subscribe button (primary)
        subscribeButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subscribeButton.setBackground(Color.WHITE);
        subscribeButton.setForeground(new Color(55, 55, 55));
        subscribeButton.setFocusPainted(false);
        subscribeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        subscribeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        UIUtils.addHoverEffect(subscribeButton, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(backButton, new Color(245, 245, 245), new Color(235, 235, 235));

        buttonPanel.add(subscribeButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        ActionListener radioListener = e -> {
            if (basicRadio.isSelected())
                selectedPlan = basicPlan;
            else if (premiumRadio.isSelected())
                selectedPlan = premiumPlan;
            else if (weekendRadio.isSelected())
                selectedPlan = weekendPlan;
            updateDetails();
        };

        basicRadio.addActionListener(radioListener);
        premiumRadio.addActionListener(radioListener);
        weekendRadio.addActionListener(radioListener);

        subscribeButton.addActionListener(e -> handleSubscribe());

        backButton.addActionListener(e -> {
            new StudentMenuScreen();
            dispose();
        });
    }

    private void updateDetails() {
        if (selectedPlan != null) {
            priceLabel.setText("Price: " + selectedPlan.getMonthlyCost() + " AED");
            descLabel.setText("<html>Description: " + selectedPlan.getDescription() + "</html>");
        }
    }

    private void handleSubscribe() {
        // Validate Hosteller
        Student current = StudentMenuScreen.currentStudent;
        if (current == null || !(current instanceof Hosteller)) {
            JOptionPane.showMessageDialog(this,
                    "Only Hostellers can subscribe to a mess plan.",
                    "Access Denied",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get plan ID
        String planId;
        if (selectedPlan instanceof BasicPlan) {
            planId = "BASIC";
        } else if (selectedPlan instanceof PremiumPlan) {
            planId = "PREMIUM";
        } else {
            planId = "WEEKEND";
        }

        // Save to database
        boolean success = DatabaseManager.subscribeToMessPlan(current.getUserId(), planId);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Subscription Successful!\nPlan: " + selectedPlan.getPlanName() +
                            "\nPrice: " + selectedPlan.getMonthlyCost() + " AED/month" +
                            "\nValid for: 30 days",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error saving subscription to database.\nPlease try again.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
