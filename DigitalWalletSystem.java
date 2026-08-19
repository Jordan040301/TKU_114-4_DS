public class DigitalWalletSystem {
    
    public static void main(String[] args) {
        System.out.println("========== 建立電子錢包 ==========");
        DigitalWallet wallet = new DigitalWallet("W001", "王小明", 1000.0);
        System.out.println(wallet);
        
        System.out.println("\n========== 測試正常儲值 ==========");
        wallet.topUp(500.0);
        System.out.println(wallet);
        
        System.out.println("\n========== 測試正常付款 ==========");
        wallet.pay(300.0);
        System.out.println(wallet);
        
        System.out.println("\n========== 測試餘額不足付款 ==========");
        wallet.pay(2000.0);
        System.out.println(wallet);
        
        System.out.println("\n========== 測試負數金額 ==========");
        System.out.println("嘗試儲值 -100 元：" + wallet.topUp(-100.0));
        System.out.println("嘗試付款 -50 元：" + wallet.pay(-50.0));
        System.out.println(wallet);
        
        System.out.println("\n========== 測試退款 ==========");
        wallet.refund(150.0);
        System.out.println(wallet);
        
        System.out.println("\n========== 測試更多情境 ==========");
        System.out.println("嘗試付款 0 元：" + wallet.pay(0.0));
        System.out.println("嘗試退款 0 元：" + wallet.refund(0.0));
        System.out.println("嘗試退款負數：" + wallet.refund(-100.0));
        System.out.println(wallet);
        
        System.out.println("\n========== 最終狀態 ==========");
        System.out.println("交易次數統計：" + wallet.getTransactionCount() + " 次");
        System.out.println(wallet);
        
        System.out.println("\n========== 建立第二個錢包測試 ==========");
        DigitalWallet wallet2 = new DigitalWallet("W002", "李小美", 500.0);
        System.out.println(wallet2);
        wallet2.pay(100.0);
        wallet2.pay(200.0);
        wallet2.topUp(300.0);
        System.out.println("錢包2 最終狀態：" + wallet2);
        System.out.println("錢包2 交易次數：" + wallet2.getTransactionCount() + " 次");
    }
}

/**
 * 電子錢包類別 - 封裝錢包相關操作
 */
class DigitalWallet {
    private final String walletId;      // 錢包 ID（不可變）
    private final String owner;          // 所有者（不可變）
    private double balance;              // 餘額
    private int transactionCount;        // 交易次數統計
    
    /**
     * 建構子 - 初始化電子錢包
     * @param walletId 錢包 ID
     * @param owner 所有者姓名
     * @param initialBalance 初始餘額
     */
    public DigitalWallet(String walletId, String owner, double initialBalance) {
        this.walletId = walletId;
        this.owner = owner;
        // 確保初始餘額不為負數
        this.balance = (initialBalance < 0) ? 0 : initialBalance;
        this.transactionCount = 0;
    }
    
    /**
     * 儲值功能
     * @param amount 儲值金額（必須為正數）
     * @return 儲值是否成功
     */
    public boolean topUp(double amount) {
        // 檢查金額是否合法（正數）
        if (amount <= 0) {
            System.out.println("儲值失敗：金額必須為正數");
            return false;
        }
        
        // 執行儲值
        balance += amount;
        transactionCount++;
        System.out.printf("儲值成功：存入 %.2f 元%n", amount);
        return true;
    }
    
    /**
     * 付款功能
     * @param amount 付款金額（必須為正數且不超過餘額）
     * @return 付款是否成功
     */
    public boolean pay(double amount) {
        // 檢查金額是否合法（正數）
        if (amount <= 0) {
            System.out.println("付款失敗：金額必須為正數");
            return false;
        }
        
        // 檢查餘額是否充足
        if (amount > balance) {
            System.out.printf("付款失敗：餘額不足（需要 %.2f 元，目前餘額 %.2f 元）%n", 
                            amount, balance);
            return false;
        }
        
        // 執行付款
        balance -= amount;
        transactionCount++;
        System.out.printf("付款成功：支付 %.2f 元%n", amount);
        return true;
    }
    
    /**
     * 退款功能
     * @param amount 退款金額（必須為正數）
     * @return 退款是否成功
     */
    public boolean refund(double amount) {
        // 檢查金額是否合法（正數）
        if (amount <= 0) {
            System.out.println("退款失敗：金額必須為正數");
            return false;
        }
        
        // 執行退款（直接加入餘額）
        balance += amount;
        transactionCount++;
        System.out.printf("退款成功：退回 %.2f 元%n", amount);
        return true;
    }
    
    /**
     * 取得錢包 ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * 取得所有者姓名
     */
    public String getOwner() {
        return owner;
    }
    
    /**
     * 取得目前餘額
     */
    public double getBalance() {
        return balance;
    }
    
    /**
     * 取得交易次數統計
     */
    public int getTransactionCount() {
        return transactionCount;
    }
    
    /**
     * 覆寫 toString() 顯示錢包完整資訊
     */
    @Override
    public String toString() {
        return String.format("錢包 ID：%s，所有者：%s，餘額：%.2f 元，交易次數：%d 次",
                           walletId, owner, balance, transactionCount);
    }
}