final class WalletTransaction {
    private final int sequence;
    private final String type;
    private final int amount;
    private final int balanceAfter;

    WalletTransaction(int sequence, String type, int amount, int balanceAfter) {
        this.sequence = sequence;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return sequence + " " + type + " " + amount
                + " balance=" + balanceAfter;
    }
}

class DigitalWallet {
    private final String walletId;
    private final String owner;
    private int balance;
    private final WalletTransaction[] transactions;
    private int transactionCount;

    DigitalWallet(String walletId, String owner, int historyCapacity) {
        this.walletId = walletId == null || walletId.isBlank()
                ? "UNKNOWN" : walletId;
        this.owner = owner == null || owner.isBlank() ? "Unknown" : owner;
        this.balance = 0;
        this.transactions = new WalletTransaction[Math.max(1, historyCapacity)];
        this.transactionCount = 0;
    }

    boolean deposit(int amount) {
        if (amount <= 0 || transactionCount >= transactions.length) {
            return false;
        }
        balance += amount;
        record("DEPOSIT", amount);
        return true;
    }

    boolean pay(int amount) {
        if (amount <= 0 || amount > balance
                || transactionCount >= transactions.length) {
            return false;
        }
        balance -= amount;
        record("PAY", amount);
        return true;
    }

    boolean refund(int amount) {
        if (amount <= 0 || transactionCount >= transactions.length) {
            return false;
        }
        balance += amount;
        record("REFUND", amount);
        return true;
    }

    private void record(String type, int amount) {
        transactions[transactionCount] = new WalletTransaction(
                transactionCount + 1, type, amount, balance);
        transactionCount++;
    }

    void printStatement() {
        System.out.println(walletId + " owner=" + owner
                + " balance=" + balance);
        for (int i = 0; i < transactionCount; i++) {
            System.out.println(transactions[i]);
        }
    }
}

public class WalletTransactionSystem {
    public static void main(String[] args) {
        DigitalWallet wallet = new DigitalWallet("W001", "Amy", 5);

        System.out.println("deposit=" + wallet.deposit(1000));
        System.out.println("pay 250=" + wallet.pay(250));
        System.out.println("pay 900=" + wallet.pay(900));
        System.out.println("refund=" + wallet.refund(50));
        wallet.printStatement();
    }
}