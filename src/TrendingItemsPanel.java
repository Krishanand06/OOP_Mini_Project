
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * TrendingItemsPanel - UI component to display popular menu items
 * Updates automatically via PopularityAnalyzer thread
 */

public class TrendingItemsPanel extends JPanel {

    private JTextArea displayArea;
    private JLabel statusLabel;

    public TrendingItemsPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "🔥 Trending Items",
                0,
                0,
                new Font("SansSerif", Font.BOLD, 14)));

        // Status label at top
        statusLabel = new JLabel("Loading...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        statusLabel.setForeground(Color.GRAY);

        // Display area for trending items
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        displayArea.setText("Waiting for data...");

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        add(statusLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Update the display with new popularity data
     * Called from PopularityAnalyzer thread via SwingUtilities.invokeLater()
     */
    public void updateTrendingItems(List<PopularityAnalyzer.PopularItemData> items) {
        if (items == null || items.isEmpty()) {
            displayArea.setText("No order data available yet.\n\nPlace some orders to see trending items!");
            statusLabel.setText("No data");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Top ").append(items.size()).append(" Popular Items:\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

        for (int i = 0; i < items.size(); i++) {
            PopularityAnalyzer.PopularItemData item = items.get(i);

            // Add ranking emoji
            String emoji = "";
            if (i == 0)
                emoji = "🥇 ";
            else if (i == 1)
                emoji = "🥈 ";
            else if (i == 2)
                emoji = "🥉 ";
            else
                emoji = (i + 1) + ". ";

            sb.append(emoji).append(item.getItemName()).append("\n");
            sb.append("   Category: ").append(item.getCategory()).append("\n");
            sb.append("   Orders: ").append(item.getTotalOrdered()).append("\n\n");
        }

        displayArea.setText(sb.toString());
        displayArea.setCaretPosition(0);

        // Update status
        java.time.LocalTime now = java.time.LocalTime.now();
        statusLabel.setText("Last updated: " + now.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    /**
     * Set status message
     */
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
}
