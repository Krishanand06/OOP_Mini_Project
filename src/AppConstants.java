// Combined constants - enums and interfaces

enum OrderStatus {
    PENDING, PREPARING, READY, COMPLETED, CANCELLED
}

enum CateringStatus {
    PLANNED, SCHEDULED, COMPLETED, CANCELLED
}

enum FeedbackCategory {
    CANTEEN, MESS, EVENT_CATERING
}

enum TransactionType {
    CREDIT, DEBIT
}

interface Payable {
    double getAmountToPay();

    String getPayerId();
}

interface Billable {
    double calculateBillAmount();

    String getBillDescription();
}

interface Orderable {
    String getItemName();

    double getUnitPrice();
}

// Custom exceptions
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}

// Payment method base class
abstract class PaymentMethod {
    public abstract void pay(Payable payable)
            throws InvalidAmountException, InsufficientBalanceException;
}
