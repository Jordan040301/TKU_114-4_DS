import java.util.*;

/**
 * 服務請求系統
 * 使用 HashMap 依 id 查詢請求，PriorityQueue 追蹤優先權
 * 取消時兩份結構必須一致
 */
public class ServiceRequestSystem {
    
    /**
     * 服務請求類別
     */
    public static class ServiceRequest implements Comparable<ServiceRequest> {
        private final String id;
        private final String description;
        private final int priority;  // 數字越大越優先
        private final long timestamp;
        private String status;  // PENDING, PROCESSING, COMPLETED, CANCELLED
        private String assignedTo;
        
        public ServiceRequest(String id, String description, int priority) {
            this.id = id;
            this.description = description;
            this.priority = priority;
            this.timestamp = System.currentTimeMillis();
            this.status = "PENDING";
            this.assignedTo = null;
        }
        
        public String getId() {
            return id;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getPriority() {
            return priority;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public String getAssignedTo() {
            return assignedTo;
        }
        
        public void setAssignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
        }
        
        public boolean isActive() {
            return !status.equals("CANCELLED") && !status.equals("COMPLETED");
        }
        
        @Override
        public int compareTo(ServiceRequest other) {
            // 優先權高的先 (數字大)
            if (this.priority != other.priority) {
                return Integer.compare(other.priority, this.priority);
            }
            // 優先權相同時，時間早的先
            return Long.compare(this.timestamp, other.timestamp);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ServiceRequest that = (ServiceRequest) obj;
            return Objects.equals(id, that.id);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %s (優先權: %d) - %s", 
                               id, description, priority, status);
        }
        
        public String toDetailedString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== 請求詳細資訊 ===\n");
            sb.append("ID: ").append(id).append("\n");
            sb.append("描述: ").append(description).append("\n");
            sb.append("優先權: ").append(priority).append("\n");
            sb.append("狀態: ").append(status).append("\n");
            sb.append("時間: ").append(new Date(timestamp)).append("\n");
            if (assignedTo != null) {
                sb.append("指派給: ").append(assignedTo).append("\n");
            }
            return sb.toString();
        }
    }
    
    // HashMap: ID → 請求 (快速查詢)
    private Map<String, ServiceRequest> requestMap;
    
    // PriorityQueue: 依優先權排序 (追蹤最高優先權)
    private PriorityQueue<ServiceRequest> requestQueue;
    
    // 請求數量統計
    private int totalRequests;
    private int completedRequests;
    private int cancelledRequests;
    
    /**
     * 建構子
     */
    public ServiceRequestSystem() {
        this.requestMap = new HashMap<>();
        this.requestQueue = new PriorityQueue<>();
        this.totalRequests = 0;
        this.completedRequests = 0;
        this.cancelledRequests = 0;
    }
    
    /**
     * 新增服務請求
     * @param id 請求 ID
     * @param description 描述
     * @param priority 優先權 (1-10，數字越大越優先)
     * @return 新增的請求
     */
    public ServiceRequest addRequest(String id, String description, int priority) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("請求 ID 不能為空");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("請求描述不能為空");
        }
        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("優先權必須在 1 到 10 之間");
        }
        
        String normalizedId = id.trim();
        
        if (requestMap.containsKey(normalizedId)) {
            System.out.printf("⚠️ 請求 ID '%s' 已存在%n", normalizedId);
            return null;
        }
        
        ServiceRequest request = new ServiceRequest(normalizedId, description, priority);
        requestMap.put(normalizedId, request);
        requestQueue.offer(request);
        totalRequests++;
        
        System.out.printf("✅ 新增請求: %s (優先權: %d)%n", normalizedId, priority);
        return request;
    }
    
    /**
     * 查詢請求 (依 ID)
     * @param id 請求 ID
     * @return 請求物件，若不存在則回傳 null
     */
    public ServiceRequest getRequest(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return requestMap.get(id.trim());
    }
    
    /**
     * 取得下一個最高優先權請求
     * @return 最高優先權請求，若無則回傳 null
     */
    public ServiceRequest getNextRequest() {
        // 清理已取消或已完成的請求
        cleanQueue();
        
        if (requestQueue.isEmpty()) {
            System.out.println("⚠️ 目前沒有待處理的請求");
            return null;
        }
        
        ServiceRequest next = requestQueue.peek();
        System.out.printf("📌 下一個請求: %s (優先權: %d)%n", 
                         next.getId(), next.getPriority());
        return next;
    }
    
    /**
     * 取出並處理下一個請求
     * @return 被處理的請求
     */
    public ServiceRequest processNext() {
        // 清理已取消或已完成的請求
        cleanQueue();
        
        if (requestQueue.isEmpty()) {
            System.out.println("⚠️ 目前沒有待處理的請求");
            return null;
        }
        
        ServiceRequest request = requestQueue.poll();
        request.setStatus("PROCESSING");
        
        System.out.printf("🔄 處理請求: %s (優先權: %d)%n", 
                         request.getId(), request.getPriority());
        return request;
    }
    
    /**
     * 完成請求
     * @param id 請求 ID
     * @return 是否成功完成
     */
    public boolean completeRequest(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        ServiceRequest request = requestMap.get(id.trim());
        if (request == null) {
            System.out.printf("⚠️ 請求 '%s' 不存在%n", id);
            return false;
        }
        
        if (request.getStatus().equals("COMPLETED")) {
            System.out.printf("⚠️ 請求 '%s' 已完成%n", id);
            return false;
        }
        
        if (request.getStatus().equals("CANCELLED")) {
            System.out.printf("⚠️ 請求 '%s' 已取消%n", id);
            return false;
        }
        
        request.setStatus("COMPLETED");
        completedRequests++;
        
        System.out.printf("✅ 完成請求: %s%n", id);
        return true;
    }
    
    /**
     * 取消請求 (兩份結構必須一致)
     * @param id 請求 ID
     * @return 是否成功取消
     */
    public boolean cancelRequest(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        ServiceRequest request = requestMap.get(id.trim());
        if (request == null) {
            System.out.printf("⚠️ 請求 '%s' 不存在%n", id);
            return false;
        }
        
        if (request.getStatus().equals("COMPLETED")) {
            System.out.printf("⚠️ 請求 '%s' 已完成，無法取消%n", id);
            return false;
        }
        
        if (request.getStatus().equals("CANCELLED")) {
            System.out.printf("⚠️ 請求 '%s' 已取消%n", id);
            return false;
        }
        
        // 更新狀態
        request.setStatus("CANCELLED");
        cancelledRequests++;
        
        // 從 PriorityQueue 中移除 (延遲移除)
        // 下次 cleanQueue() 時會被移除
        
        System.out.printf("🗑️ 取消請求: %s%n", id);
        
        // 驗證兩份結構一致性
        verifyConsistency();
        
        return true;
    }
    
    /**
     * 清理佇列 (移除已取消或已完成的請求)
     */
    private void cleanQueue() {
        List<ServiceRequest> toRemove = new ArrayList<>();
        
        for (ServiceRequest request : requestQueue) {
            if (!request.isActive()) {
                toRemove.add(request);
            }
        }
        
        for (ServiceRequest request : toRemove) {
            requestQueue.remove(request);
        }
    }
    
    /**
     * 驗證 HashMap 和 PriorityQueue 的一致性
     */
    private void verifyConsistency() {
        // 檢查：所有在 queue 中的請求都應該在 map 中且是 active 狀態
        for (ServiceRequest request : requestQueue) {
            ServiceRequest mapRequest = requestMap.get(request.getId());
            if (mapRequest == null) {
                System.out.println("⚠️ 一致性錯誤: Queue 中的請求不在 Map 中");
                return;
            }
            if (!mapRequest.isActive()) {
                System.out.println("⚠️ 一致性錯誤: Queue 中的請求已非 active 狀態");
                return;
            }
        }
        
        // 檢查：所有 map 中 active 的請求都應該在 queue 中
        for (ServiceRequest request : requestMap.values()) {
            if (request.isActive() && !requestQueue.contains(request)) {
                System.out.println("⚠️ 一致性錯誤: Map 中的 active 請求不在 Queue 中");
                return;
            }
        }
        
        System.out.println("✅ 一致性驗證通過: HashMap 和 PriorityQueue 同步");
    }
    
    /**
     * 指派請求給服務人員
     * @param id 請求 ID
     * @param assignee 服務人員名稱
     * @return 是否成功指派
     */
    public boolean assignRequest(String id, String assignee) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        if (assignee == null || assignee.trim().isEmpty()) {
            throw new IllegalArgumentException("服務人員名稱不能為空");
        }
        
        ServiceRequest request = requestMap.get(id.trim());
        if (request == null) {
            System.out.printf("⚠️ 請求 '%s' 不存在%n", id);
            return false;
        }
        
        if (!request.isActive()) {
            System.out.printf("⚠️ 請求 '%s' 已%s，無法指派%n", id, 
                             request.getStatus().toLowerCase());
            return false;
        }
        
        request.setAssignedTo(assignee.trim());
        System.out.printf("👤 指派請求 %s 給 %s%n", id, assignee);
        return true;
    }
    
    /**
     * 取得請求統計
     */
    public void printStatistics() {
        System.out.println("\n=== 服務請求統計 ===");
        System.out.printf("總請求數: %d%n", totalRequests);
        System.out.printf("已完成: %d%n", completedRequests);
        System.out.printf("已取消: %d%n", cancelledRequests);
        System.out.printf("處理中: %d%n", 
                         totalRequests - completedRequests - cancelledRequests);
        System.out.printf("待處理: %d%n", requestQueue.size());
        System.out.println();
    }
    
    /**
     * 印出所有請求
     */
    public void printAllRequests() {
        System.out.println("\n=== 所有請求 ===");
        if (requestMap.isEmpty()) {
            System.out.println("尚無請求");
            return;
        }
        
        List<ServiceRequest> requests = new ArrayList<>(requestMap.values());
        requests.sort((a, b) -> a.getId().compareTo(b.getId()));
        
        System.out.printf("%-10s | %-20s | %-8s | %-12s | %-10s%n", 
                         "ID", "描述", "優先權", "狀態", "指派給");
        System.out.println("-----------|----------------------|----------|-------------|-----------");
        
        for (ServiceRequest request : requests) {
            System.out.printf("%-10s | %-20s | %8d | %-12s | %-10s%n",
                             request.getId(),
                             truncate(request.getDescription(), 20),
                             request.getPriority(),
                             request.getStatus(),
                             request.getAssignedTo() != null ? request.getAssignedTo() : "-");
        }
        System.out.println();
    }
    
    /**
     * 印出待處理佇列 (依優先權排序)
     */
    public void printQueue() {
        System.out.println("\n=== 待處理佇列 (依優先權) ===");
        
        cleanQueue();
        
        if (requestQueue.isEmpty()) {
            System.out.println("目前沒有待處理的請求");
            return;
        }
        
        // 複製佇列以顯示
        PriorityQueue<ServiceRequest> tempQueue = new PriorityQueue<>(requestQueue);
        
        System.out.printf("%-10s | %-20s | %-8s | %-12s%n", 
                         "ID", "描述", "優先權", "狀態");
        System.out.println("-----------|----------------------|----------|-------------");
        
        while (!tempQueue.isEmpty()) {
            ServiceRequest request = tempQueue.poll();
            System.out.printf("%-10s | %-20s | %8d | %-12s%n",
                             request.getId(),
                             truncate(request.getDescription(), 20),
                             request.getPriority(),
                             request.getStatus());
        }
        System.out.println();
    }
    
    /**
     * 截斷字串
     */
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * 清空系統
     */
    public void clear() {
        requestMap.clear();
        requestQueue.clear();
        totalRequests = 0;
        completedRequests = 0;
        cancelledRequests = 0;
        System.out.println("🔄 已清空服務請求系統");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 服務請求系統測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：優先權佇列
        testPriorityQueue();
        
        // 測試 3：取消請求 (一致性驗證)
        testCancelRequest();
        
        // 測試 4：邊界情況
        testEdgeCases();
        
        // 測試 5：實際應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- 測試 1: 基本功能 ---");
        
        ServiceRequestSystem system = new ServiceRequestSystem();
        
        // 新增請求
        System.out.println("新增請求:");
        system.addRequest("SR001", "電腦無法開機", 5);
        system.addRequest("SR002", "印表機卡紙", 3);
        system.addRequest("SR003", "網路連線問題", 7);
        system.addRequest("SR004", "軟體安裝需求", 2);
        system.addRequest("SR005", "系統當機", 9);
        
        system.printAllRequests();
        system.printQueue();
        
        // 查詢
        System.out.println("🔍 查詢請求:");
        ServiceRequest req = system.getRequest("SR003");
        if (req != null) {
            System.out.println(req.toDetailedString());
        }
        
        System.out.println("查詢不存在的請求: " + system.getRequest("SR999"));
    }
    
    /**
     * 測試優先權佇列
     */
    private static void testPriorityQueue() {
        System.out.println("\n--- 測試 2: 優先權佇列 ---");
        
        ServiceRequestSystem system = new ServiceRequestSystem();
        
        // 新增各種優先權的請求
        system.addRequest("P001", "緊急維修", 10);
        system.addRequest("P002", "一般諮詢", 3);
        system.addRequest("P003", "系統更新", 6);
        system.addRequest("P004", "資料備份", 4);
        system.addRequest("P005", "緊急救援", 10);
        system.addRequest("P006", "軟體安裝", 2);
        system.addRequest("P007", "網路設定", 7);
        system.addRequest("P008", "硬體檢測", 5);
        
        system.printQueue();
        
        // 依序處理請求
        System.out.println("📋 依序處理請求 (依優先權):");
        int count = 0;
        while (system.requestQueue.size() > 0) {
            ServiceRequest req = system.processNext();
            if (req != null) {
                count++;
                system.completeRequest(req.getId());
            }
        }
        System.out.printf("共處理 %d 個請求%n", count);
        
        system.printStatistics();
    }
    
    /**
     * 測試取消請求 (一致性驗證)
     */
    private static void testCancelRequest() {
        System.out.println("\n--- 測試 3: 取消請求 (一致性驗證) ---");
        
        ServiceRequestSystem system = new ServiceRequestSystem();
        
        system.addRequest("C001", "測試請求1", 5);
        system.addRequest("C002", "測試請求2", 8);
        system.addRequest("C003", "測試請求3", 3);
        system.addRequest("C004", "測試請求4", 6);
        
        system.printQueue();
        
        // 取消請求
        System.out.println("\n🗑️ 取消請求 C002:");
        system.cancelRequest("C002");
        
        System.out.println("\n🗑️ 取消請求 C004:");
        system.cancelRequest("C004");
        
        // 驗證一致性
        system.printQueue();
        system.printAllRequests();
        system.printStatistics();
        
        // 嘗試取消已完成或已取消的請求
        System.out.println("\n嘗試取消已完成請求:");
        system.completeRequest("C001");
        system.cancelRequest("C001");
        
        System.out.println("\n嘗試取消已取消請求:");
        system.cancelRequest("C002");
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("\n--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空系統
        System.out.println("測試 4.1: 空系統");
        ServiceRequestSystem system = new ServiceRequestSystem();
        system.printQueue();
        system.getNextRequest();
        system.processNext();
        system.printStatistics();
        System.out.println();
        
        // 測試 4.2: 單一請求
        System.out.println("測試 4.2: 單一請求");
        system.addRequest("S001", "單一請求", 5);
        system.printQueue();
        system.processNext();
        system.completeRequest("S001");
        system.printStatistics();
        System.out.println();
        
        // 測試 4.3: 相同優先權
        System.out.println("測試 4.3: 相同優先權 (依時間排序)");
        ServiceRequestSystem system2 = new ServiceRequestSystem();
        system2.addRequest("A001", "請求A", 5);
        system2.addRequest("A002", "請求B", 5);
        system2.addRequest("A003", "請求C", 5);
        system2.printQueue();
        
        System.out.println("\n依序取出 (應依時間順序):");
        while (!system2.requestQueue.isEmpty()) {
            ServiceRequest req = system2.processNext();
            if (req != null) {
                System.out.println("  取出: " + req.getId());
            }
        }
        System.out.println();
        
        // 測試 4.4: 無效參數
        System.out.println("測試 4.4: 無效參數");
        try {
            system.addRequest("", "空ID", 5);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲空ID例外: " + e.getMessage());
        }
        
        try {
            system.addRequest("T001", "測試", 0);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲無效優先權例外: " + e.getMessage());
        }
        
        try {
            system.addRequest("T002", "測試", 11);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲無效優先權例外: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("\n--- 測試 5: 實際應用場景 ---");
        System.out.println("🏢 企業 IT 服務台系統");
        
        ServiceRequestSystem itHelpdesk = new ServiceRequestSystem();
        
        // 模擬用戶提交請求
        System.out.println("\n📝 用戶提交請求:");
        String[][] requests = {
            {"IT-001", "無法連接到公司 VPN", "8"},
            {"IT-002", "電子郵件無法收發", "7"},
            {"IT-003", "需要安裝 Adobe 軟體", "3"},
            {"IT-004", "筆記型電腦無法開機", "9"},
            {"IT-005", "忘記密碼", "4"},
            {"IT-006", "印表機無法列印", "5"},
            {"IT-007", "系統嚴重當機", "10"},
            {"IT-008", "需要帳號權限設定", "6"},
            {"IT-009", "軟體更新建議", "2"},
            {"IT-010", "網路速度異常緩慢", "6"},
            {"IT-011", "資料庫連線錯誤", "8"},
            {"IT-012", "螢幕顯示異常", "4"}
        };
        
        for (String[] req : requests) {
            itHelpdesk.addRequest(req[0], req[1], Integer.parseInt(req[2]));
        }
        
        itHelpdesk.printStatistics();
        itHelpdesk.printQueue();
        
        // 服務台處理流程
        System.out.println("\n🔄 IT 服務台處理流程:");
        
        // 1. 查看下一個請求
        System.out.println("1. 查看最高優先權請求:");
        itHelpdesk.getNextRequest();
        
        // 2. 指派給工程師
        System.out.println("\n2. 指派工程師:");
        itHelpdesk.assignRequest("IT-007", "張工程師");
        itHelpdesk.assignRequest("IT-001", "李工程師");
        itHelpdesk.assignRequest("IT-004", "王工程師");
        
        // 3. 處理請求
        System.out.println("\n3. 處理請求:");
        ServiceRequest req = itHelpdesk.processNext();
        if (req != null) {
            itHelpdesk.completeRequest(req.getId());
        }
        
        req = itHelpdesk.processNext();
        if (req != null) {
            itHelpdesk.completeRequest(req.getId());
        }
        
        // 4. 取消某些請求
        System.out.println("\n4. 取消請求:");
        itHelpdesk.cancelRequest("IT-009");
        itHelpdesk.cancelRequest("IT-012");
        
        // 5. 顯示當前狀態
        itHelpdesk.printQueue();
        itHelpdesk.printAllRequests();
        itHelpdesk.printStatistics();
        
        // 6. 處理剩餘請求
        System.out.println("\n6. 處理剩餘請求:");
        int processed = 0;
        while (itHelpdesk.requestQueue.size() > 0) {
            ServiceRequest r = itHelpdesk.processNext();
            if (r != null) {
                processed++;
                itHelpdesk.completeRequest(r.getId());
            }
        }
        System.out.printf("共處理 %d 個請求%n", processed);
        
        // 最終統計
        System.out.println("\n📊 最終統計:");
        itHelpdesk.printStatistics();
        
        // 服務水準分析
        System.out.println("\n📈 服務水準分析:");
        System.out.printf("  請求完成率: %.1f%%%n", 
                         (double) itHelpdesk.completedRequests / 
                         itHelpdesk.totalRequests * 100);
        System.out.printf("  請求取消率: %.1f%%%n", 
                         (double) itHelpdesk.cancelledRequests / 
                         itHelpdesk.totalRequests * 100);
        
        // 優先權分布
        System.out.println("\n  優先權分布:");
        Map<Integer, Integer> priorityDist = new TreeMap<>();
        for (ServiceRequest r : itHelpdesk.requestMap.values()) {
            int p = r.getPriority();
            priorityDist.put(p, priorityDist.getOrDefault(p, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : priorityDist.entrySet()) {
            System.out.printf("    優先權 %d: %d 個請求%n", 
                             entry.getKey(), entry.getValue());
        }
    }
}