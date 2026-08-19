class TransferAccount {
    private String id;
    private int balance;

    TransferAccount(String id, int balance) {
        this.id = id;
        this.balance = Math.max(0, balance);
    }

    boolean withdraw(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    @Override
    public String toString() {
        return id + " balance=" + balance;
    }
}

public class ObjectParameterDemo {
    static boolean transfer(TransferAccount source,
                            TransferAccount target,
                            int amount) {
        if (source == null || target == null || source == target) {
            return false;
        }
        if (!source.withdraw(amount)) {
            return false;
        }
        target.deposit(amount);
        return true;
    }

    static void replaceLocally(TransferAccount account) {
        account = new TransferAccount("LOCAL", 9999);
        System.out.println("method 內：" + account);
    }

    public static void main(String[] args) {
        TransferAccount a = new TransferAccount("A", 1000);
        TransferAccount b = new TransferAccount("B", 200);

        System.out.println("transfer=" + transfer(a, b, 300));
        System.out.println(a);
        System.out.println(b);

        replaceLocally(a);
        System.out.println("method 外：" + a);
    }
}