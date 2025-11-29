

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private String ownerId; // studentId or clubId
    private double balance;
    private List<Transaction> transactions;

    public Wallet(String ownerId) {
        this.ownerId = ownerId;
        this.balance = 0.0;
        this.transactions = new ArrayList<Transaction>();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<Transaction>(transactions);
    }

    public void credit(double amount, String description)
            throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Credit amount must be positive.");
        }
        balance += amount;
        Transaction t = new Transaction(generateTransactionId(),
                TransactionType.CREDIT, amount, description);
        transactions.add(t);
    }

    public void debit(double amount, String description)
            throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Debit amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Not enough balance in wallet. Required: " + amount +
                            ", Available: " + balance);
        }
        balance -= amount;
        Transaction t = new Transaction(generateTransactionId(),
                TransactionType.DEBIT, amount, description);
        transactions.add(t);
    }

    private String generateTransactionId() {
        return ownerId + "-TX-" + System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "ownerId='" + ownerId + '\'' +
                ", balance=" + balance +
                ", txCount=" + transactions.size() +
                '}';
    }
}
