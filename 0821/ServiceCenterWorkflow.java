import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 課後作業六：服務中心排隊與取消
 * 指定檔名：ServiceCenterWorkflow.java
 * 
 * 用途：
 * - Map<String, ServiceTicket> 依票證 ID 查詢
 * - Deque<ServiceTicket> 作為等待隊列
 * - Deque<ServiceTicket> 作為完成的 Progress Stack
 * - Set<String> 防止重複 ID
 * 
 * 功能：
 * 1. createTicket     - 建立新工單
 * 2. processNext      - 處理下一個等待工單
 * 3. cancelWaiting    - 取消等待中的工單（僅限尚未處理）
 * 4. undoLastCompletion - 撤銷最後完成的工單（放回等待隊列前置）
 * 5. findById         - 依 ID 查詢工單
 * 6. printSummary     - 印出系統摘要
 * 
 * 測試：重複 ID、空隊列、取消不存在 ID、連續兩個撤銷
 */
public class ServiceCenterWorkflow {

    public static void main(String[] args) {
        System.out.println("=== 服務中心排隊與取消系統測試 ===\n");

        ServiceCenter 中心 = new ServiceCenter();

        // ============================================
        // 測試 1：建立工單 (createTicket)
        // ============================================
        System.out.println("--- 測試 1：建立工單 ---");
        中心.createTicket("S001", "王小明", "電腦維修");
        中心.createTicket("S002", "陳小華", "印表機故障");
        中心.createTicket("S003", "李小英", "網路設定");
        中心.createTicket("S004", "張大偉", "軟體安裝");
        中心.createTicket("S005", "林小美", "資料備份");
        中心.printSummary();
        System.out.println();

        // ============================================
        // 測試 2：重複 ID 不得加入
        // ============================================
        System.out.println("--- 測試 2：重複 ID 不得加入 ---");
        中心.createTicket("S001", "重複的工單", "測試");
        中心.createTicket("S003", "重複的工單", "測試");
        中心.printSummary();
        System.out.println();

        // ============================================
        // 測試 3：處理下一個工單 (processNext)
        // ============================================
        System.out.println("--- 測試 3：處理下一個工單 (FIFO) ---");
        中心.processNext();  // 處理 S001
        中心.processNext();  // 處理 S002
        中心.processNext();  // 處理 S003
        中心.printSummary();
        System.out.println();

        // ============================================
        // 測試 4：取消等待中的工單 (cancelWaiting)
        // ============================================
        System.out.println("--- 測試 4：取消等待中的工單 ---");
        中心.cancelWaiting("S004");  // 取消等待中的 S004
        中心.cancelWaiting("S001");  // 嘗試取消已完成的 S001（應失敗）
        中心.cancelWaiting("S999");  // 取消不存在的 ID
        中心.printSummary();
        System.out.println();

        // ============================================
        // 測試 5：依 ID 查詢 (findById)
        // ============================================
        System.out.println("--- 測試 5：依 ID 查詢 ---");
        中心.findById("S002");
        中心.findById("S005");
        中心.findById("S999");
        System.out.println();

        // ============================================
        // 測試 6：撤銷最後完成的工單 (undoLastCompletion)
        // ============================================
        System.out.println("--- 測試 6：撤銷最後完成的工單 ---");
        中心.undoLastCompletion();  // 撤銷 S003（放回等待隊列前置）
        中心.undoLastCompletion();  // 撤銷 S002（放回等待隊列前置）
        中心.printSummary();
        System.out.println();

        // ============================================
        // 測試 7：連續兩個撤銷（測試完成堆疊空）
        // ============================================
        System.out.println("--- 測試 7：連續撤銷（測試空堆疊）---");
        中心.undoLastCompletion();  // 撤銷 S001（放回等待隊列前置）
        中心.undoLastCompletion();  // 嘗試撤銷（已完成堆疊為空）
        中心.printSummary();
        System.out.println();

        // ============================================
        // 測試 8：空隊列處理
        // ============================================
        System.out.println("--- 測試 8：空隊列處理 ---");
        中心.processNext();  // 處理 S001
        中心.processNext();  // 處理 S002
        中心.processNext();  // 處理 S003
        中心.processNext();  // 處理 S005（S004 已被取消）
        中心.processNext();  // 嘗試處理空隊列
        中心.printSummary();
        System.out.println();

        // ============================================
        // 測試 9：取消不存在 ID 與空隊列取消
        // ============================================
        System.out.println("--- 測試 9：取消不存在 ID 與空隊列取消 ---");
        中心.cancelWaiting("S999");  // 不存在
        中心.cancelWaiting("S004");  // 已被取消
        中心.printSummary();
        System.out.println();

        // ============================================
        // 測試 10：最終狀態驗證
        // ============================================
        System.out.println("--- 測試 10：最終狀態驗證 ---");
        中心.findById("S001");
        中心.findById("S002");
        中心.findById("S003");
        中心.findById("S005");
        中心.printSummary();

        System.out.println("\n=== 測試完成 ===");
    }
}

/**
 * 服務工單類別
 */
class ServiceTicket {
    private String id;
    private String 客戶姓名;
    private String 服務項目;
    private TicketStatus 狀態;

    public ServiceTicket(String id, String 客戶姓名, String 服務項目) {
        this.id = id;
        this.客戶姓名 = 客戶姓名;
        this.服務項目 = 服務項目;
        this.狀態 = TicketStatus.等待中;
    }

    public String getId() {
        return id;
    }

    public String 取得客戶姓名() {
        return 客戶姓名;
    }

    public String 取得服務項目() {
        return 服務項目;
    }

    public TicketStatus 取得狀態() {
        return 狀態;
    }

    public void 設定狀態(TicketStatus 狀態) {
        this.狀態 = 狀態;
    }

    @Override
    public String toString() {
        return id + " | " + 客戶姓名 + " | " + 服務項目 + " | " + 狀態.取得顯示名稱();
    }
}

/**
 * 工單狀態列舉
 */
enum TicketStatus {
    等待中("等待中"),
    處理中("處理中"),
    已完成("已完成"),
    已取消("已取消");

    private String 顯示名稱;

    TicketStatus(String 顯示名稱) {
        this.顯示名稱 = 顯示名稱;
    }

    public String 取得顯示名稱() {
        return 顯示名稱;
    }
}

/**
 * 服務中心類別
 */
class ServiceCenter {
    // Map<String, ServiceTicket> 依票證 ID 查詢
    private Map<String, ServiceTicket> 工單地圖;
    // Deque<ServiceTicket> 作為等待隊列（FIFO）
    private Deque<ServiceTicket> 等待隊列;
    // Deque<ServiceTicket> 作為完成的 Progress Stack（LIFO，用於撤銷）
    private Deque<ServiceTicket> 完成堆疊;
    // Set<String> 防止重複 ID
    private Set<String> id集合;

    // 統計計數器
    private int 等待中數量;
    private int 已完成數量;
    private int 已取消數量;

    public ServiceCenter() {
        工單地圖 = new HashMap<>();
        等待隊列 = new ArrayDeque<>();
        完成堆疊 = new ArrayDeque<>();
        id集合 = new HashSet<>();
        等待中數量 = 0;
        已完成數量 = 0;
        已取消數量 = 0;
    }

    /**
     * 1. 建立新工單 (createTicket)
     * 重複 ID 不得加入
     */
    public void createTicket(String id, String 客戶姓名, String 服務項目) {
        // 檢查重複 ID
        if (id集合.contains(id)) {
            System.out.println("⚠ 建立失敗：ID「" + id + "」已存在，不得重複加入");
            return;
        }

        ServiceTicket 工單 = new ServiceTicket(id, 客戶姓名, 服務項目);
        工單地圖.put(id, 工單);
        等待隊列.offerLast(工單);  // 加入等待隊列尾端
        id集合.add(id);
        等待中數量++;

        System.out.println("✅ 建立成功：「" + 工單 + "」");
    }

    /**
     * 2. 處理下一個工單 (processNext)
     * 從等待隊列取出（FIFO），移入完成堆疊
     */
    public void processNext() {
        if (等待隊列.isEmpty()) {
            System.out.println("⚠ 處理失敗：目前沒有等待中的工單");
            return;
        }

        ServiceTicket 工單 = 等待隊列.pollFirst();  // 從前端取出
        工單.設定狀態(TicketStatus.已完成);
        完成堆疊.push(工單);  // 推入完成堆疊（LIFO）
        等待中數量--;
        已完成數量++;

        System.out.println("🔄 處理完成：「" + 工單 + "」");
    }

    /**
     * 3. 取消等待中的工單 (cancelWaiting)
     * 只能取消尚未處理的工單（等待中狀態）
     */
    public void cancelWaiting(String id) {
        // 檢查工單是否存在
        ServiceTicket 工單 = 工單地圖.get(id);
        if (工單 == null) {
            System.out.println("⚠ 取消失敗：ID「" + id + "」不存在");
            return;
        }

        // 檢查工單狀態（只能取消等待中的工單）
        if (工單.取得狀態() != TicketStatus.等待中) {
            System.out.println("⚠ 取消失敗：ID「" + id + "」狀態為「" + 
                              工單.取得狀態().取得顯示名稱() + "」，無法取消（僅限等待中）");
            return;
        }

        // 從等待隊列中移除該工單
        // 由於 Deque 不支援直接移除指定元素，需遍歷
        Deque<ServiceTicket> 暫存隊列 = new ArrayDeque<>();
        boolean 找到 = false;

        while (!等待隊列.isEmpty()) {
            ServiceTicket 當前 = 等待隊列.pollFirst();
            if (當前.getId().equals(id)) {
                找到 = true;
                當前.設定狀態(TicketStatus.已取消);
                等待中數量--;
                已取消數量++;
                System.out.println("❌ 取消成功：「" + 當前 + "」");
            } else {
                暫存隊列.offerLast(當前);
            }
        }

        // 將暫存隊列移回等待隊列
        等待隊列 = 暫存隊列;

        if (!找到) {
            // 理論上不應發生（因為工單存在且狀態為等待中，但不在隊列中）
            System.out.println("⚠ 取消失敗：ID「" + id + "」不在等待隊列中");
        }
    }

    /**
     * 4. 撤銷最後完成的工單 (undoLastCompletion)
     * 將最後完成的工單放回等待隊列前置
     */
    public void undoLastCompletion() {
        if (完成堆疊.isEmpty()) {
            System.out.println("⚠ 撤銷失敗：沒有已完成的工單可以撤銷");
            return;
        }

        ServiceTicket 工單 = 完成堆疊.pop();  // 從完成堆疊彈出
        工單.設定狀態(TicketStatus.等待中);
        等待隊列.offerFirst(工單);  // ★ 放回等待隊列前置
        已完成數量--;
        等待中數量++;

        System.out.println("↩️ 撤銷成功：「" + 工單 + "」已放回等待隊列前置");
    }

    /**
     * 5. 依 ID 查詢工單 (findById)
     */
    public void findById(String id) {
        ServiceTicket 工單 = 工單地圖.get(id);
        if (工單 == null) {
            System.out.println("🔍 查無 ID：「" + id + "」");
            return;
        }
        System.out.println("🔍 查詢結果：「" + id + "」→ " + 工單);
    }

    /**
     * 6. 印出系統摘要 (printSummary)
     */
    public void printSummary() {
        System.out.println("📊 系統摘要：");
        System.out.println("   等待中工單：" + 等待中數量 + " 筆");
        System.out.println("   已完成工單：" + 已完成數量 + " 筆");
        System.out.println("   已取消工單：" + 已取消數量 + " 筆");
        System.out.println("   總工單數：" + 工單地圖.size() + " 筆");

        System.out.println("   📋 等待隊列（由前至後）：");
        if (等待隊列.isEmpty()) {
            System.out.println("      （空）");
        } else {
            int 序號 = 1;
            for (ServiceTicket 工單 : 等待隊列) {
                System.out.println("      " + 序號++ + ". " + 工單);
            }
        }

        System.out.println("   📋 完成堆疊（由頂至底）：");
        if (完成堆疊.isEmpty()) {
            System.out.println("      （空）");
        } else {
            int 序號 = 1;
            for (ServiceTicket 工單 : 完成堆疊) {
                System.out.println("      " + 序號++ + ". " + 工單);
            }
        }

        System.out.println("   📋 所有工單（Map 內容）：");
        if (工單地圖.isEmpty()) {
            System.out.println("      （空）");
        } else {
            for (ServiceTicket 工單 : 工單地圖.values()) {
                System.out.println("      " + 工單);
            }
        }
        System.out.println("   ------------------------------------");
    }

    /**
     * 取得等待隊列（用於測試）
     */
    public Deque<ServiceTicket> 取得等待隊列() {
        return 等待隊列;
    }

    /**
     * 取得完成堆疊（用於測試）
     */
    public Deque<ServiceTicket> 取得完成堆疊() {
        return 完成堆疊;
    }
}