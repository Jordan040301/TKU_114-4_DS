import java.util.Arrays;

public class WalletHistoryManager {
    
    public static void main(String[] args) {
        System.out.println("========== 建立電子錢包 ==========");
        Wallet wallet1 = new Wallet("W001", "王小明", 10000.0);
        Wallet wallet2 = new Wallet("W002", "張小美", 5000.0);
        Wallet wallet3 = new Wallet("W003", "李大華", 3000.0);
        
        System.out.println(wallet1);
        System.out.println(wallet2);
        System.out.println(wallet3);
        
        System.out.println("\n========== 測試 1：儲值 ==========");
        wallet1.topUp(3000.0);
        wallet2.topUp(2000.0);
        wallet3.topUp(1000.0);
        
        System.out.println("\n========== 測試 2：付款 ==========");
        wallet1.pay(1500.0);
        wallet2.pay(1000.0);
        wallet3.pay(500.0);
        
        System.out.println("\n========== 測試 3：轉帳（成功） ==========");
        wallet1.transferTo(wallet2, 2000.0);
        
        System.out.println("\n========== 測試 4：轉帳（餘額不足） ==========");
        wallet2.transferTo(wallet3, 10000.0);
        
        System.out.println("\n========== 測試 5：轉帳（同錢包） ==========");
        wallet1.transferTo(wallet1, 1000.0);
        
        System.out.println("\n========== 測試 6：轉帳（負數金額） ==========");
        wallet1.transferTo(wallet2, -500.0);
        
        System.out.println("\n========== 測試 7：findTransaction 功能 ==========");
        System.out.println("尋找序號 1 的交易：" + wallet1.findTransaction(1));
        System.out.println("尋找序號 5 的交易：" + wallet1.findTransaction(5));
        System.out.println("尋找序號 99 的交易：" + wallet1.findTransaction(99));
        
        System.out.println("\n========== 測試 8：totalByType 功能 ==========");
        System.out.printf("錢包1 總儲值金額：%.2f 元%n", wallet1.totalByType("儲值"));
        System.out.printf("錢包1 總付款金額：%.2f 元%n", wallet1.totalByType("付款"));
        System.out.printf("錢包1 總轉帳收入：%.2f 元%n", wallet1.totalByType("轉帳收入"));
        System.out.printf("錢包1 總轉帳支出：%.2f 元%n", wallet1.totalByType("轉帳支出"));
        System.out.printf("錢包1 總退款金額：%.2f 元%n", wallet1.totalByType("退款"));
        
        System.out.println("\n========== 測試 9：交易陣列滿時不得改變餘額 ==========");
        // 建立一個小容量錢包來測試
        Wallet wallet4 = new Wallet("W004", "陳小芳", 1000.0, 3); // 容量只有 3
        System.out.println("建立容量為 3 的錢包：");
        System.out.println(wallet4);
        
        System.out.println("\n執行多筆交易測試容量限制：");
        wallet4.topUp(500.0);   // 交易 1
        wallet4.pay(200.0);     // 交易 2
        wallet4.pay(100.0);     // 交易 3（已滿）
        wallet4.pay(50.0);      // 嘗試第 4 筆（應失敗）
        wallet4.topUp(100.0);   // 嘗試第 5 筆（應失敗）
        
        System.out.println("\n最終狀態（第 4、5 筆交易不應被執行）：");
        System.out.println(wallet4);
        
        System.out.println("\n========== 完整交易記錄 ==========");
        System.out.println("錢包1 交易記錄：");
        wallet1.printFullStatement();
        
        System.out.println("\n錢包2 交易記錄：");
        wallet2.printFullStatement();
        
        System.out.println("\n錢包3 交易記錄：");
        wallet3.printFullStatement();
    }
}

/**
 * 交易記錄類別 - 儲存單筆交易資訊
 */
class Transaction {
    private final int sequence;      // 交易序號
    private final String type;       // 交易類型（儲值、付款、轉帳收入、轉帳支出、退款）
    private final double amount;     // 交易金額
    private final double balance;    // 交易後餘額
    private final String timestamp;  // 交易時間
    
    /**
     * 建構子
     */
    public Transaction(int sequence, String type, double amount, double balance) {
        this.sequence = sequence;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
    
    /**
     * Getter 方法
     */
    public int getSequence() {
        return sequence;
    }
    
    public String getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("#%d %s %+.2f 元，餘額：%.2f 元 (%s)",
                           sequence, type, amount, balance, 
                           timestamp.substring(0, 19));
    }
}

/**
 * 電子錢包類別 - 包含交易記錄管理功能
 */
class Wallet {
    private final String walletId;          // 錢包 ID
    private final String owner;             // 所有者
    private double balance;                 // 餘額
    private Transaction[] transactions;     // 交易記錄陣列
    private int transactionCount;           // 實際交易筆數
    private int nextSequence;               // 下一個交易序號
    
    // 預設最大交易筆數
    private static final int DEFAULT_MAX_TRANSACTIONS = 50;
    
    /**
     * 建構子 - 使用預設容量
     */
    public Wallet(String walletId, String owner, double initialBalance) {
        this(walletId, owner, initialBalance, DEFAULT_MAX_TRANSACTIONS);
    }
    
    /**
     * 建構子 - 自訂容量
     */
    public Wallet(String walletId, String owner, double initialBalance, int maxTransactions) {
        this.walletId = walletId;
        this.owner = owner;
        this.balance = (initialBalance < 0) ? 0 : initialBalance;
        this.transactions = new Transaction[maxTransactions];
        this.transactionCount = 0;
        this.nextSequence = 1;
    }
    
    /**
     * 儲值功能
     */
    public boolean topUp(double amount) {
        if (amount <= 0) {
            System.out.println("儲值失敗：金額必須為正數");
            return false;
        }
        
        // 檢查交易陣列是否已滿
        if (isTransactionArrayFull()) {
            System.out.println("儲值失敗：交易記錄已滿，無法記錄此筆交易");
            return false;
        }
        
        // 執行儲值
        balance += amount;
        addTransaction("儲值", amount);
        System.out.printf("儲值成功：存入 %.2f 元%n", amount);
        return true;
    }
    
    /**
     * 付款功能
     */
    public boolean pay(double amount) {
        if (amount <= 0) {
            System.out.println("付款失敗：金額必須為正數");
            return false;
        }
        
        if (amount > balance) {
            System.out.printf("付款失敗：餘額不足（需要 %.2f 元，目前餘額 %.2f 元）%n",
                            amount, balance);
            return false;
        }
        
        // 檢查交易陣列是否已滿
        if (isTransactionArrayFull()) {
            System.out.println("付款失敗：交易記錄已滿，無法記錄此筆交易");
            return false;
        }
        
        // 執行付款
        balance -= amount;
        addTransaction("付款", -amount);
        System.out.printf("付款成功：支付 %.2f 元%n", amount);
        return true;
    }
    
    /**
     * 退款功能
     */
    public boolean refund(double amount) {
        if (amount <= 0) {
            System.out.println("退款失敗：金額必須為正數");
            return false;
        }
        
        // 檢查交易陣列是否已滿
        if (isTransactionArrayFull()) {
            System.out.println("退款失敗：交易記錄已滿，無法記錄此筆交易");
            return false;
        }
        
        // 執行退款
        balance += amount;
        addTransaction("退款", amount);
        System.out.printf("退款成功：退回 %.2f 元%n", amount);
        return true;
    }
    
    /**
     * 轉帳功能 - 來源與目標同時留下記錄
     */
    public boolean transferTo(Wallet target, double amount) {
        // 驗證 1：目標不是 null
        if (target == null) {
            System.out.println("轉帳失敗：目標錢包為 null");
            return false;
        }
        
        // 驗證 2：來源和目標不是同一個物件
        if (this == target) {
            System.out.println("轉帳失敗：來源與目標為同一個錢包");
            return false;
        }
        
        // 驗證 3：金額大於 0
        if (amount <= 0) {
            System.out.println("轉帳失敗：轉帳金額必須大於 0");
            return false;
        }
        
        // 驗證 4：來源餘額足夠
        if (this.balance < amount) {
            System.out.printf("轉帳失敗：餘額不足（需要 %.2f 元，目前餘額 %.2f 元）%n",
                            amount, this.balance);
            return false;
        }
        
        // 驗證 5：檢查雙方交易陣列是否已滿
        if (this.isTransactionArrayFull()) {
            System.out.println("轉帳失敗：來源錢包交易記錄已滿");
            return false;
        }
        if (target.isTransactionArrayFull()) {
            System.out.println("轉帳失敗：目標錢包交易記錄已滿");
            return false;
        }
        
        // 所有驗證通過，執行轉帳
        // 先從來源扣款
        this.balance -= amount;
        this.addTransaction("轉帳支出", -amount);
        
        // 再存入目標帳戶
        target.balance += amount;
        target.addTransaction("轉帳收入", amount);
        
        System.out.printf("轉帳成功：從 %s 轉帳 %.2f 元至 %s%n",
                        this.owner, amount, target.owner);
        return true;
    }
    
    /**
     * 依序號尋找交易
     * @param sequence 交易序號
     * @return 找到時回傳交易，否則回傳 null
     */
    public Transaction findTransaction(int sequence) {
        // 檢查序號是否在有效範圍內
        if (sequence <= 0 || sequence > transactionCount) {
            return null;
        }
        
        // 因為交易是按序號順序儲存的，可以直接索引
        for (int i = 0; i < transactionCount; i++) {
            if (transactions[i].getSequence() == sequence) {
                return transactions[i];
            }
        }
        return null;
    }
    
    /**
     * 計算指定交易類型的總金額
     * @param type 交易類型（儲值、付款、轉帳收入、轉帳支出、退款）
     * @return 指定類型的總金額
     */
    public double totalByType(String type) {
        double total = 0.0;
        for (int i = 0; i < transactionCount; i++) {
            Transaction t = transactions[i];
            if (t.getType().equals(type)) {
                total += t.getAmount();
            }
        }
        return total;
    }
    
    /**
     * 檢查交易陣列是否已滿
     */
    private boolean isTransactionArrayFull() {
        return transactionCount >= transactions.length;
    }
    
    /**
     * 新增交易記錄
     */
    private void addTransaction(String type, double amount) {
        if (isTransactionArrayFull()) {
            return; // 不應該發生，因為呼叫前已檢查
        }
        transactions[transactionCount] = new Transaction(nextSequence, type, amount, balance);
        transactionCount++;
        nextSequence++;
    }
    
    /**
     * 輸出完整交易記錄
     */
    public void printFullStatement() {
        System.out.println("錢包 ID：" + walletId);
        System.out.println("所有者：" + owner);
        System.out.printf("目前餘額：%.2f 元%n", balance);
        System.out.println("交易記錄：");
        
        if (transactionCount == 0) {
            System.out.println("  （無交易記錄）");
        } else {
            for (int i = 0; i < transactionCount; i++) {
                System.out.println("  " + transactions[i]);
            }
        }
        
        // 統計資訊
        double totalTopUp = totalByType("儲值");
        double totalPay = totalByType("付款");
        double totalTransferIn = totalByType("轉帳收入");
        double totalTransferOut = totalByType("轉帳支出");
        double totalRefund = totalByType("退款");
        
        System.out.printf("統計：儲值 %.2f 元，付款 %.2f 元，轉帳收入 %.2f 元，轉帳支出 %.2f 元，退款 %.2f 元%n",
                        totalTopUp, Math.abs(totalPay), totalTransferIn, 
                        Math.abs(totalTransferOut), totalRefund);
    }
    
    /**
     * Getter 方法
     */
    public String getWalletId() {
        return walletId;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public int getTransactionCount() {
        return transactionCount;
    }
    
    public Transaction[] getTransactions() {
        return Arrays.copyOf(transactions, transactionCount);
    }
    
    @Override
    public String toString() {
        return String.format("錢包 ID：%s，所有者：%s，餘額：%.2f 元，交易筆數：%d 筆（容量：%d）",
                           walletId, owner, balance, transactionCount, transactions.length);
    }
}