


public class WalletContext {
    // Current logged-in student's wallet
    private static Wallet studentWallet = null;

    /**
     * Sets the wallet for the current student, loading balance from database.
     * 
     * @param ownerId The user ID of the student
     */
    public static void setStudentWallet(String ownerId) {
        studentWallet = new Wallet(ownerId);

        // Load balance from database
        double balance = DatabaseManager.loadWalletBalance(ownerId);

        // Set the balance by crediting it (this adds to the transactions)
        try {
            if (balance > 0) {
                studentWallet.credit(balance, "Initial balance from database");
            }
        } catch (InvalidAmountException e) {
            System.out.println("Error loading wallet balance: " + e.getMessage());
        }
    }

    /**
     * Gets the current student's wallet.
     * 
     * @return The wallet object, or a new empty wallet if not set
     */
    public static Wallet getStudentWallet() {
        if (studentWallet == null) {
            // Fallback for demo/testing
            studentWallet = new Wallet("STUDENT_DEMO");
        }
        return studentWallet;
    }

    /**
     * Saves the current wallet balance to database.
     */
    public static void saveStudentWallet() {
        if (studentWallet != null) {
            DatabaseManager.saveWalletBalance(studentWallet);

            // Also save all new transactions
            for (Transaction tx : studentWallet.getTransactions()) {
                DatabaseManager.insertTransaction(tx);
            }
        }
    }
}
