import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

/**
 * 課後作業三：物流工作流程
 * 指定檔名：DeliveryWorkflowSystem.java
 * 
 * 使用 Map 依系列編號查詢、Queue 儲存等待系列、Stack 儲存已完成流程，
 * 完成：
 * 1. 新增（加入工作流程）
 * 2. 處理（從等待隊列取出並處理）
 * 3. Undo（撤銷最後一個已完成的工作）
 * 4. 查詢（依系列編號查詢工作狀態）
 * 5. 統計（顯示各狀態數量）
 * 
 * 重複 ID 不得加入。
 */
public class DeliveryWorkflowSystem {

    public static void main(String[] args) {
        System.out.println("=== 物流工作流程系統測試 ===\n");

        WorkflowSystem 系統 = new WorkflowSystem();

        // === 測試新增工作 ===
        System.out.println("--- 新增工作 ---");
        系統.新增工作(new WorkOrder("W001", "台北倉庫", "出貨"));
        系統.新增工作(new WorkOrder("W002", "台中倉庫", "揀貨"));
        系統.新增工作(new WorkOrder("W003", "高雄倉庫", "包裝"));
        系統.新增工作(new WorkOrder("W004", "台南倉庫", "配送"));
        系統.新增工作(new WorkOrder("W001", "桃園倉庫", "出貨")); // 重複 ID，應被拒絕
        系統.顯示統計();
        系統.顯示完整狀態();
        System.out.println();

        // === 測試查詢 ===
        System.out.println("--- 查詢工作 ---");
        系統.查詢工作("W001");
        系統.查詢工作("W003");
        系統.查詢工作("W999"); // 不存在的 ID
        System.out.println();

        // === 測試處理（從等待隊列取出） ===
        System.out.println("--- 處理工作（先進先出） ---");
        系統.處理工作(); // 處理 W001
        系統.處理工作(); // 處理 W002
        系統.處理工作(); // 處理 W003
        系統.處理工作(); // 處理 W004
        系統.處理工作(); // 空隊列測試
        系統.顯示統計();
        系統.顯示完整狀態();
        System.out.println();

        // === 測試 Undo（撤銷最後一個已完成的工作） ===
        System.out.println("--- Undo 撤銷功能 ---");
        系統.撤銷工作(); // 撤銷 W004
        系統.撤銷工作(); // 撤銷 W003
        系統.撤銷工作(); // 撤銷 W002
        系統.撤銷工作(); // 撤銷 W001
        系統.撤銷工作(); // 空堆疊測試
        系統.顯示統計();
        系統.顯示完整狀態();
        System.out.println();

        // === 混合操作測試 ===
        System.out.println("--- 混合操作測試 ---");
        系統.新增工作(new WorkOrder("W005", "新竹倉庫", "理貨"));
        系統.新增工作(new WorkOrder("W006", "彰化倉庫", "出貨"));
        系統.處理工作(); // 處理 W005
        系統.查詢工作("W005");
        系統.處理工作(); // 處理 W006
        系統.撤銷工作(); // 撤銷 W006
        系統.處理工作(); // 再次處理 W006（回到等待隊列）
        系統.顯示統計();
        系統.顯示完整狀態();

        System.out.println("\n=== 測試完成 ===");
    }
}

/**
 * 工作訂單類別
 */
class WorkOrder {
    private String 編號;
    private String 地點;
    private String 作業內容;
    private WorkStatus 狀態;

    public WorkOrder(String 編號, String 地點, String 作業內容) {
        this.編號 = 編號;
        this.地點 = 地點;
        this.作業內容 = 作業內容;
        this.狀態 = WorkStatus.等待中; // 初始狀態為等待中
    }

    public String 取得編號() {
        return 編號;
    }

    public String 取得地點() {
        return 地點;
    }

    public String 取得作業內容() {
        return 作業內容;
    }

    public WorkStatus 取得狀態() {
        return 狀態;
    }

    public void 設定狀態(WorkStatus 狀態) {
        this.狀態 = 狀態;
    }

    @Override
    public String toString() {
        return 編號 + " | " + 地點 + " | " + 作業內容 + " | " + 狀態.取得顯示名稱();
    }
}

/**
 * 工作狀態列舉
 */
enum WorkStatus {
    等待中("等待中"),
    處理中("處理中"),
    已完成("已完成"),
    已撤銷("已撤銷");

    private String 顯示名稱;

    WorkStatus(String 顯示名稱) {
        this.顯示名稱 = 顯示名稱;
    }

    public String 取得顯示名稱() {
        return 顯示名稱;
    }
}

/**
 * 物流工作流程系統
 */
class WorkflowSystem {
    // Map：依系列編號查詢工作
    private Map<String, WorkOrder> 工作地圖;
    // Queue：儲存等待中的工作（先進先出）
    private Queue<WorkOrder> 等待隊列;
    // Stack：儲存已完成流程（後進先出，用於 Undo）
    private Deque<WorkOrder> 已完成堆疊;

    // 統計資料
    private int 等待中數量;
    private int 已完成數量;
    private int 已撤銷數量;

    public WorkflowSystem() {
        工作地圖 = new HashMap<>();
        等待隊列 = new LinkedList<>();
        已完成堆疊 = new ArrayDeque<>();
        等待中數量 = 0;
        已完成數量 = 0;
        已撤銷數量 = 0;
    }

    /**
     * 1. 新增工作：將工作加入系統
     * 重複 ID 不得加入（顯示警告訊息）
     */
    public void 新增工作(WorkOrder 工作) {
        String 編號 = 工作.取得編號();
        
        // 檢查是否為重複 ID
        if (工作地圖.containsKey(編號)) {
            System.out.println("⚠ 新增失敗：系列編號「" + 編號 + "」已存在，不得重複加入");
            return;
        }

        // 加入 Map
        工作地圖.put(編號, 工作);
        // 加入等待隊列
        等待隊列.offer(工作);
        // 設定狀態為等待中
        工作.設定狀態(WorkStatus.等待中);
        等待中數量++;
        
        System.out.println("✅ 新增成功：「" + 編號 + "」" + 工作.取得地點() + " - " + 工作.取得作業內容());
    }

    /**
     * 2. 處理工作：從等待隊列取出並處理（先進先出）
     */
    public void 處理工作() {
        WorkOrder 工作 = 等待隊列.poll();
        if (工作 == null) {
            System.out.println("⚠ 處理失敗：目前沒有等待中的工作");
            return;
        }

        // 更新狀態為已完成
        工作.設定狀態(WorkStatus.已完成);
        // 從等待中數量減 1
        等待中數量--;
        // 加入已完成堆疊
        已完成堆疊.push(工作);
        已完成數量++;

        System.out.println("🔄 處理完成：「" + 工作.取得編號() + "」" + 工作.取得地點() + " - " + 工作.取得作業內容());
    }

    /**
     * 3. Undo：撤銷最後一個已完成的工作（從已完成堆疊彈出）
     * 將工作移回等待隊列
     */
    public void 撤銷工作() {
        if (已完成堆疊.isEmpty()) {
            System.out.println("⚠ 撤銷失敗：沒有已完成的工作可以撤銷");
            return;
        }

        WorkOrder 工作 = 已完成堆疊.pop();
        // 更新狀態為等待中
        工作.設定狀態(WorkStatus.等待中);
        // 從已完成數量減 1
        已完成數量--;
        // 加入等待隊列（回到隊列尾端）
        等待隊列.offer(工作);
        等待中數量++;
        已撤銷數量++;

        System.out.println("↩️ 撤銷成功：「" + 工作.取得編號() + "」已回到等待隊列");
    }

    /**
     * 4. 查詢：依系列編號查詢工作狀態
     */
    public void 查詢工作(String 編號) {
        WorkOrder 工作 = 工作地圖.get(編號);
        if (工作 == null) {
            System.out.println("🔍 查詢結果：系列編號「" + 編號 + "」不存在");
            return;
        }
        System.out.println("🔍 查詢結果：「" + 編號 + "」→ " + 工作);
    }

    /**
     * 5. 統計：顯示各狀態數量
     */
    public void 顯示統計() {
        System.out.println("📊 系統統計：");
        System.out.println("   等待中工作：" + 等待中數量 + " 筆");
        System.out.println("   已完成工作：" + 已完成數量 + " 筆");
        System.out.println("   已撤銷工作：" + 已撤銷數量 + " 筆");
        System.out.println("   總工作數：" + 工作地圖.size() + " 筆");
    }

    /**
     * 顯示完整狀態（等待隊列 + 已完成堆疊 + 所有工作）
     */
    public void 顯示完整狀態() {
        System.out.println("   📋 等待隊列（由前至後）：");
        if (等待隊列.isEmpty()) {
            System.out.println("      （空）");
        } else {
            for (WorkOrder 工作 : 等待隊列) {
                System.out.println("      「" + 工作.取得編號() + "」" + 工作.取得地點() + " - " + 工作.取得作業內容());
            }
        }

        System.out.println("   📋 已完成堆疊（由頂至底）：");
        if (已完成堆疊.isEmpty()) {
            System.out.println("      （空）");
        } else {
            for (WorkOrder 工作 : 已完成堆疊) {
                System.out.println("      「" + 工作.取得編號() + "」" + 工作.取得地點() + " - " + 工作.取得作業內容());
            }
        }

        System.out.println("   📋 所有工作（Map 內容）：");
        if (工作地圖.isEmpty()) {
            System.out.println("      （空）");
        } else {
            for (WorkOrder 工作 : 工作地圖.values()) {
                System.out.println("      " + 工作);
            }
        }
        System.out.println("   ------------------------------------");
    }
}