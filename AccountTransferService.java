public class AccountTransferService {
    
    public static void main(String[] args) {
        System.out.println("========== 建立帳戶 ==========");
        Account account1 = new Account("A001", "王小明", 10000.0);
        Account account2 = new Account("A002", "張小美", 5000.0);
        Account account3 = new Account("A003", "李大華", 3000.0);
        
        System.out.println(account1);
        System.out.println(account2);
        System.out.println(account3);
        
        TransferService service = new TransferService();
        
        System.out.println("\n========== 測試 1：成功轉帳 ==========");
        System.out.println("轉帳前：");
        System.out.println("  " + account1);
        System.out.println("  " + account2);
        
        boolean result1 = service.transfer(account1, account2, 3000);
        System.out.println("轉帳結果：" + (result1 ? "成功" : "失敗"));
        
        System.out.println("轉帳後：");
        System.out.println("  " + account1);
        System.out.println("  " + account2);
        
        System.out.println("\n========== 測試 2：餘額不足 ==========");
        System.out.println("轉帳前：");
        System.out.println("  " + account2);
        System.out.println("  " + account3);
        
        boolean result2 = service.transfer(account2, account3, 8000);
        System.out.println("轉帳結果：" + (result2 ? "成功" : "失敗"));
        
        System.out.println("轉帳後（帳戶不應有任何變動）：");
        System.out.println("  " + account2);
        System.out.println("  " + account3);
        
        System.out.println("\n========== 測試 3：同帳戶轉帳 ==========");
        System.out.println("轉帳前：");
        System.out.println("  " + account1);
        
        boolean result3 = service.transfer(account1, account1, 1000);
        System.out.println("轉帳結果：" + (result3 ? "成功" : "失敗"));
        
        System.out.println("轉帳後（帳戶不應有任何變動）：");
        System.out.println("  " + account1);
        
        System.out.println("\n========== 測試 4：null 目標帳戶 ==========");
        System.out.println("轉帳前：");
        System.out.println("  " + account1);
        
        boolean result4 = service.transfer(account1, null, 1000);
        System.out.println("轉帳結果：" + (result4 ? "成功" : "失敗"));
        
        System.out.println("轉帳後（帳戶不應有任何變動）：");
        System.out.println("  " + account1);
        
        System.out.println("\n========== 測試 5：null 來源帳戶 ==========");
        boolean result5 = service.transfer(null, account2, 1000);
        System.out.println("轉帳結果：" + (result5 ? "成功" : "失敗"));
        System.out.println("帳戶2 不應有任何變動：");
        System.out.println("  " + account2);
        
        System.out.println("\n========== 測試 6：金額為零或負數 ==========");
        System.out.println("轉帳前：");
        System.out.println("  " + account2);
        System.out.println("  " + account3);
        
        boolean result6 = service.transfer(account2, account3, 0);
        System.out.println("轉帳 0 元結果：" + (result6 ? "成功" : "失敗"));
        
        boolean result7 = service.transfer(account2, account3, -500);
        System.out.println("轉帳 -500 元結果：" + (result7 ? "成功" : "失敗"));
        
        System.out.println("轉帳後（帳戶不應有任何變動）：");
        System.out.println("  " + account2);
        System.out.println("  " + account3);
        
        System.out.println("\n========== 最終狀態 ==========");
        System.out.println(account1);
        System.out.println(account2);
        System.out.println(account3);
    }
}

/**
 * 帳戶類別 - 儲存帳戶基本資訊與餘額
 */
class Account {
    private final String accountId;      // 帳戶編號
    private final String owner;           // 戶名
    private double balance;               // 餘額
    
    /**
     * 建構子
     */
    public Account(String accountId, String owner, double initialBalance) {
        this.accountId = accountId;
        this.owner = owner;
        this.balance = (initialBalance < 0) ? 0 : initialBalance;
    }
    
    /**
     * 存款
     * @param amount 存款金額（必須為正數）
     * @return 存款是否成功
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }
    
    /**
     * 提款
     * @param amount 提款金額（必須為正數且不超過餘額）
     * @return 提款是否成功
     */
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }
    
    /**
     * Getter 方法
     */
    public String getAccountId() {
        return accountId;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public double getBalance() {
        return balance;
    }
    
    @Override
    public String toString() {
        return String.format("帳戶編號：%s，戶名：%s，餘額：%.2f 元",
                           accountId, owner, balance);
    }
}

/**
 * 轉帳服務類別 - 負責跨帳戶轉帳
 */
class TransferService {
    
    /**
     * 轉帳服務
     * @param source 來源帳戶
     * @param target 目標帳戶
     * @param amount 轉帳金額
     * @return 轉帳是否成功
     */
    public boolean transfer(Account source, Account target, int amount) {
        // 驗證 1：來源與目標不是 null
        if (source == null) {
            System.out.println("轉帳失敗：來源帳戶為 null");
            return false;
        }
        if (target == null) {
            System.out.println("轉帳失敗：目標帳戶為 null");
            return false;
        }
        
        // 驗證 2：來源和目標不是同一個物件
        if (source == target) {
            System.out.println("轉帳失敗：來源與目標為同一個帳戶");
            return false;
        }
        
        // 驗證 3：金額大於 0
        if (amount <= 0) {
            System.out.println("轉帳失敗：轉帳金額必須大於 0");
            return false;
        }
        
        // 驗證 4：來源餘額足夠
        if (source.getBalance() < amount) {
            System.out.printf("轉帳失敗：來源帳戶餘額不足（需要 %d 元，目前餘額 %.2f 元）%n",
                            amount, source.getBalance());
            return false;
        }
        
        // 所有驗證通過，執行轉帳
        // 先從來源扣款
        boolean withdrawSuccess = source.withdraw(amount);
        if (!withdrawSuccess) {
            // 理論上不會發生，因為已經驗證過餘額
            System.out.println("轉帳失敗：來源帳戶扣款失敗");
            return false;
        }
        
        // 再存入目標帳戶
        boolean depositSuccess = target.deposit(amount);
        if (!depositSuccess) {
            // 如果存入失敗，必須把錢還給來源帳戶（確保原子性）
            source.deposit(amount);
            System.out.println("轉帳失敗：目標帳戶存款失敗，已執行退款");
            return false;
        }
        
        System.out.printf("轉帳成功：從 %s 轉帳 %d 元至 %s%n",
                        source.getOwner(), amount, target.getOwner());
        return true;
    }
}