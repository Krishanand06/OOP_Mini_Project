


public class WalletPayment extends PaymentMethod
{
    private Wallet wallet;

    public WalletPayment(Wallet wallet) {
        this.wallet = wallet;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public void pay(Payable payable)
            throws InvalidAmountException, InsufficientBalanceException {
        if (wallet == null) {
            throw new InvalidAmountException("Wallet is not set for payment.");
        }

        double amount = payable.getAmountToPay();
        if (amount <= 0) {
            throw new InvalidAmountException("Amount to pay must be positive.");
        }

        String description = "Payment for: " + payable.getClass().getSimpleName() +
                             " (payer: " + payable.getPayerId() + ")";
        wallet.debit(amount, description);
    }
}
