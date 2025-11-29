

import java.time.LocalDateTime;

public class Transaction
{
    private String transactionId;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime timestamp;

    public Transaction(String transactionId, TransactionType type,
                       double amount, String description) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return timestamp + " - " + type + " - " + amount + " AED - " + description;
    }
}
