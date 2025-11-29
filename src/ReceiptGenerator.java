
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ReceiptGenerator - Utility
 class for generating receipt files
 * Demonstrates file handling (FileWriter, BufferedWriter)
 */
public class ReceiptGenerator {

    private static final String RECEIPTS_DIR = "receipts";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Generates a receipt file for the given order
     * 
     * @param order      The order to generate receipt for
     * @param student    The student who placed the order
     * @param amountPaid The amount paid
     * @return The path to the generated receipt file, or null if failed
     */
    public static String generateReceipt(Order order, Student student, double amountPaid) {
        try {
            // Ensure receipts directory exists
            ensureReceiptsDirectory();

            // Generate filename
            String timestamp = LocalDateTime.now().format(FILE_FORMATTER);
            String filename = String.format("%s/receipt_%s_%s.txt",
                    RECEIPTS_DIR,
                    order.getOrderId(),
                    timestamp);

            // Write receipt to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                String receiptContent = formatReceipt(order, student, amountPaid);
                writer.write(receiptContent);
            }

            return filename;

        } catch (IOException e) {
            System.err.println("Error generating receipt: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Formats the receipt content as a string
     */
    private static String formatReceipt(Order order, Student student, double amountPaid) {
        StringBuilder receipt = new StringBuilder();
        String dateTime = LocalDateTime.now().format(DATE_FORMATTER);

        // Header
        receipt.append("========================================\n");
        receipt.append("        CAMPUS CAFÉ RECEIPT\n");
        receipt.append("========================================\n\n");

        // Order details
        receipt.append(String.format("Order ID    : %s\n", order.getOrderId()));
        receipt.append(String.format("Date        : %s\n", dateTime));
        receipt.append(String.format("Student     : %s (%s)\n",
                student.getName(),
                student.getUserId()));
        receipt.append(String.format("Roll No     : %s\n", student.getRollNo()));
        receipt.append("\n");

        // Items
        receipt.append("----------------------------------------\n");
        receipt.append("ITEMS:\n");
        receipt.append("----------------------------------------\n");

        List<MenuItem> items = order.getItems();
        List<Integer> quantities = order.getQuantities();

        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            int qty = quantities.get(i);
            double subtotal = item.getUnitPrice() * qty;

            receipt.append(String.format("%-3d. %-25s x %-3d %8.2f AED\n",
                    (i + 1),
                    item.getItemName(),
                    qty,
                    subtotal));
        }

        // Total
        receipt.append("----------------------------------------\n");
        receipt.append(String.format("%-35s %8.2f AED\n", "TOTAL", order.calculateBillAmount()));
        receipt.append(String.format("%-35s %8.2f AED\n", "PAID VIA WALLET", amountPaid));
        receipt.append("----------------------------------------\n\n");

        // Footer
        receipt.append("Thank you for your order!\n");
        receipt.append("Visit us again at Campus Café\n");
        receipt.append("========================================\n");

        return receipt.toString();
    }

    /**
     * Ensures the receipts directory exists, creates it if not
     */
    private static void ensureReceiptsDirectory() throws IOException {
        File dir = new File(RECEIPTS_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create receipts directory");
            }
        }
    }

    /**
     * Opens the receipt file in the default text editor (optional utility method)
     */
    public static void openReceipt(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                desktop.open(file);
            }
        } catch (Exception e) {
            System.err.println("Could not open receipt file: " + e.getMessage());
        }
    }
}
