

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Wallet screen for the demo student.
 * Uses WalletContext.getStudentWallet() so that
 * other screens (like CanteenOrderScreen) share the same wallet.
 */

public class WalletScreen extends JFrame {
    // Shared wallet for the demo student
    private Wallet demoWallet = WalletContext.getStudentWallet();

    private JLabel balanceLabel;
    private JTextArea txArea;

    public WalletScreen() {
        setTitle("Wallet – Campus Café");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Top panel: balance ---
        JLabel titleLabel = new JLabel("Student Wallet");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        balanceLabel = new JLabel();
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateBalanceLabel();

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(titleLabel);
        topPanel.add(balanceLabel);

        // --- Center: transactions area ---
        txArea = new JTextArea();
        txArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txArea);
        refreshTransactions();

        // --- Bottom: buttons ---
        JButton topUpButton = new JButton("Top-up Wallet");
        JButton backButton = new JButton("Back to Student Menu");

        // Style Top-up button (primary)
        topUpButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        topUpButton.setBackground(Color.WHITE);
        topUpButton.setForeground(new Color(55, 55, 55));
        topUpButton.setFocusPainted(false);
        topUpButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        topUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style Back button (secondary)
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backButton.setBackground(new Color(245, 245, 245));
        backButton.setForeground(new Color(85, 85, 85));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        UIUtils.addHoverEffect(topUpButton, Color.WHITE, new Color(248, 248, 248));
        UIUtils.addHoverEffect(backButton, new Color(245, 245, 245), new Color(235, 235, 235));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(topUpButton);
        buttonPanel.add(backButton);

        // --- Layout for whole screen ---
        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Button actions ---

        topUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleTopUp();
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new StudentMenuScreen();
                dispose();
            }
        });

        setVisible(true);
    }

    // ----------------- helper methods -----------------

    private void updateBalanceLabel() {
        balanceLabel.setText("Current Balance: " + demoWallet.getBalance() + " AED");
    }

    private void refreshTransactions() {
        StringBuilder sb = new StringBuilder();
        List<Transaction> list = demoWallet.getTransactions();
        if (list.isEmpty()) {
            sb.append("No transactions yet.");
        } else {
            for (Transaction t : list) {
                sb.append(t.toString()).append("\n");
            }
        }
        txArea.setText(sb.toString());
    }

    private void handleTopUp() {
        String input = JOptionPane.showInputDialog(this,
                "Enter amount to top-up:",
                "Top-up Wallet",
                JOptionPane.PLAIN_MESSAGE);

        if (input == null) {
            return; // user cancelled
        }

        try {
            double amount = Double.parseDouble(input);
            demoWallet.credit(amount, "Wallet top-up by student");

            // Save to database
            WalletContext.saveStudentWallet();

            updateBalanceLabel();
            refreshTransactions();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InvalidAmountException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Invalid Amount",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
