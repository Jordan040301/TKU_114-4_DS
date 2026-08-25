import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 課堂作業題三：櫃檯等候隊列
 * 指定檔名：CounterWaitingQueue.java
 * 
 * 使用 Deque<Customer> 管理一般顧客先進先出（FIFO）隊列，完成：
 * 1. 加入（顧客報到／排隊）
 * 2. 查看下一位（顯示隊首顧客，但不移除）
 * 3. 服務下一位（移除隊首顧客，進行服務）
 * 4. 顯示等候人數
 * 5. 空隊列處理（不得拋出例外）
 */
public class CounterWaitingQueue {

    // 使用 Deque 作為先進先出（FIFO）隊列
    private Deque<Customer> 等候隊列;

    public CounterWaitingQueue() {
        等候隊列 = new ArrayDeque<>();
    }

    /**
     * 1. 加入顧客：將顧客加入隊列尾端
     */
    public void 加入顧客(Customer 顧客) {
        等候隊列.offer(顧客); // offer 在隊列滿時回傳 false，但 ArrayDeque 無容量限制
        System.out.println("✅ 顧客「" + 顧客.getName() + "」已加入等候隊列（編號：" + 顧客.getNumber() + "）");
        顯示等候人數();
    }

    /**
     * 2. 查看下一位：顯示隊首顧客（不移除）
     * 若隊列為空，顯示提示訊息，不拋出例外
     */
    public void 查看下一位() {
        Customer 下一位 = 等候隊列.peek(); // peek 在空隊列時回傳 null
        if (下一位 == null) {
            System.out.println("ℹ 目前沒有顧客在等候（隊列為空）");
        } else {
            System.out.println("👀 下一位等候顧客：「" + 下一位.getName() + "」（編號：" + 下一位.getNumber() + "）");
        }
        顯示等候人數();
    }

    /**
     * 3. 服務下一位：移除並傳回隊首顧客
     * 若隊列為空，顯示提示訊息，不拋出例外
     */
    public void 服務下一位() {
        Customer 被服務顧客 = 等候隊列.poll(); // poll 在空隊列時回傳 null
        if (被服務顧客 == null) {
            System.out.println("⚠ 無法服務：沒有顧客在等候（隊列為空）");
        } else {
            System.out.println("🛎 正在服務顧客：「" + 被服務顧客.getName() + "」（編號：" + 被服務顧客.getNumber() + "）");
        }
        顯示等候人數();
    }

    /**
     * 4. 顯示等候人數
     */
    public void 顯示等候人數() {
        int 人數 = 等候隊列.size();
        System.out.println("   📊 目前等候人數：" + 人數);
        System.out.println("   ------------------------------------");
    }

    /**
     * 顯示完整隊列內容（便於觀察）
     */
    public void 顯示完整隊列() {
        if (等候隊列.isEmpty()) {
            System.out.println("   📋 隊列內容：（空）");
        } else {
            System.out.print("   📋 隊列內容（由前至後）：");
            for (Customer 顧客 : 等候隊列) {
                System.out.print(" 「" + 顧客.getName() + "(" + 顧客.getNumber() + ")」");
            }
            System.out.println();
        }
        System.out.println("   ------------------------------------");
    }

    /**
     * 主程式：連續測試多種情境
     */
    public static void main(String[] args) {
        System.out.println("=== 櫃檯等候隊列測試 ===\n");

        CounterWaitingQueue 櫃檯 = new CounterWaitingQueue();

        // 測試空隊列操作（驗證不拋出例外）
        System.out.println("--- 測試空隊列操作 ---");
        櫃檯.查看下一位();
        櫃檯.服務下一位();
        System.out.println();

        // 加入顧客
        System.out.println("--- 加入顧客 ---");
        櫃檯.加入顧客(new Customer("王小明", 1));
        櫃檯.加入顧客(new Customer("陳小華", 2));
        櫃檯.加入顧客(new Customer("李小英", 3));
        櫃檯.加入顧客(new Customer("張大偉", 4));
        櫃檯.顯示完整隊列();
        System.out.println();

        // 查看下一位（不移除）
        System.out.println("--- 查看下一位 ---");
        櫃檯.查看下一位();
        櫃檯.顯示完整隊列(); // 確認隊列未被移除
        System.out.println();

        // 服務顧客（移除）
        System.out.println("--- 服務顧客 ---");
        櫃檯.服務下一位(); // 服務王小明
        櫃檯.服務下一位(); // 服務陳小華
        櫃檯.顯示完整隊列();
        System.out.println();

        // 再查看下一位
        System.out.println("--- 查看下一位 ---");
        櫃檯.查看下一位(); // 應顯示李小英
        System.out.println();

        // 繼續服務剩餘顧客
        System.out.println("--- 繼續服務 ---");
        櫃檯.服務下一位(); // 服務李小英
        櫃檯.服務下一位(); // 服務張大偉
        櫃檯.顯示完整隊列();
        System.out.println();

        // 再次測試空隊列操作
        System.out.println("--- 再次測試空隊列操作 ---");
        櫃檯.查看下一位();
        櫃檯.服務下一位();
        櫃檯.顯示完整隊列();
        System.out.println();

        // 再次加入新顧客（驗證隊列可繼續使用）
        System.out.println("--- 重新加入顧客 ---");
        櫃檯.加入顧客(new Customer("林小美", 5));
        櫃檯.加入顧客(new Customer("吳大志", 6));
        櫃檯.顯示完整隊列();
        櫃檯.查看下一位();

        System.out.println("\n=== 測試完成 ===");
    }
}

/**
 * 顧客類別：儲存顧客基本資料
 */
class Customer {
    private String 姓名;
    private int 編號;

    public Customer(String 姓名, int 編號) {
        this.姓名 = 姓名;
        this.編號 = 編號;
    }

    public String getName() {
        return 姓名;
    }

    public int getNumber() {
        return 編號;
    }
}