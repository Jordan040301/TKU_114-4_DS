interface PaymentMethod {
    boolean pay(int amount);
    String getName();
}

class CardPayment implements PaymentMethod {
    private int limit;

    CardPayment(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean pay(int amount) {
        return amount > 0 && amount <= limit;
    }

    @Override
    public String getName() {
        return "Card";
    }
}

class WalletPayment implements PaymentMethod {
    private int balance;

    WalletPayment(int balance) {
        this.balance = balance;
    }

    @Override
    public boolean pay(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    @Override
    public String getName() {
        return "Wallet";
    }
}

public class PaymentInterfaceDemo {
    static void checkout(PaymentMethod method, int amount) {
        System.out.println(method.getName() + " pay " + amount + "：" +
            method.pay(amount));
    }

    public static void main(String[] args) {
        PaymentMethod card = new CardPayment(3000);
        PaymentMethod wallet = new WalletPayment(800);

        checkout(card, 1200);
        checkout(wallet, 500);
        checkout(wallet, 500);
    }
}