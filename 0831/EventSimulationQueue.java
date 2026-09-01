import java.util.*;

/**
 * 活動事件模擬器
 * 事件包含時間、類型與序列，依時間先後執行，時間相同依序列
 */
class Event {
    private int time;          // 事件時間
    private String type;       // 事件類型
    private int sequence;      // 事件序列號
    private String description; // 事件描述
    private boolean isCancelled; // 是否已取消
    
    public Event(int time, String type, int sequence, String description) {
        this.time = time;
        this.type = type;
        this.sequence = sequence;
        this.description = description;
        this.isCancelled = false;
    }
    
    public int getTime() {
        return time;
    }
    
    public String getType() {
        return type;
    }
    
    public int getSequence() {
        return sequence;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isCancelled() {
        return isCancelled;
    }
    
    public void cancel() {
        this.isCancelled = true;
    }
    
    /**
     * 執行事件
     */
    public void execute() {
        System.out.printf("⏰ [時間 %d] 執行事件: %s (序列:%d) - %s%n",
                         time, type, sequence, description);
    }
    
    @Override
    public String toString() {
        return String.format("Event{時間=%d, 類型='%s', 序列=%d, 描述='%s', 已取消=%s}",
                           time, type, sequence, description, isCancelled);
    }
    
    /**
     * 簡短顯示格式
     */
    public String toShortString() {
        return String.format("[%d] %s(#%d) %s", time, type, sequence, 
                           isCancelled ? "[已取消]" : "");
    }
}

/**
 * 事件模擬器主類別
 */
public class EventSimulationQueue {
    private PriorityQueue<Event> eventQueue;
    private List<Event> executedEvents;    // 已執行事件記錄
    private List<Event> cancelledEvents;   // 已取消事件記錄
    private int nextSequence;               // 下一個序列號
    private int currentTime;                // 目前模擬時間
    private boolean isRunning;              // 是否執行中
    
    /**
     * 建構子
     */
    public EventSimulationQueue() {
        // 比較器：時間升序，時間相同則序列升序
        Comparator<Event> comparator = Comparator
            .comparingInt(Event::getTime)
            .thenComparingInt(Event::getSequence);
        
        this.eventQueue = new PriorityQueue<>(comparator);
        this.executedEvents = new ArrayList<>();
        this.cancelledEvents = new ArrayList<>();
        this.nextSequence = 1;
        this.currentTime = 0;
        this.isRunning = false;
    }
    
    /**
     * 新增事件
     * @param time 事件時間
     * @param type 事件類型
     * @param description 事件描述
     */
    public void addEvent(int time, String type, String description) {
        if (time < 0) {
            throw new IllegalArgumentException("事件時間不能為負數");
        }
        
        Event event = new Event(time, type, nextSequence, description);
        eventQueue.offer(event);
        nextSequence++;
        
        System.out.printf("📌 新增事件: [時間 %d] %s (序列:%d) - %s%n",
                         time, type, event.getSequence(), description);
    }
    
    /**
     * 批量新增事件
     */
    public void addEvents(EventData... events) {
        for (EventData data : events) {
            addEvent(data.time, data.type, data.description);
        }
    }
    
    /**
     * 取消指定事件
     * @param time 事件時間
     * @param sequence 事件序列號
     * @return 是否成功取消
     */
    public boolean cancelEvent(int time, int sequence) {
        // 檢查是否已在執行中
        if (isRunning) {
            System.out.println("⚠️ 模擬器執行中，無法取消事件");
            return false;
        }
        
        // 尋找並取消事件
        for (Event event : eventQueue) {
            if (event.getTime() == time && event.getSequence() == sequence && !event.isCancelled()) {
                event.cancel();
                cancelledEvents.add(event);
                System.out.printf("❌ 取消事件: [時間 %d] %s (序列:%d)%n",
                                 time, event.getType(), sequence);
                return true;
            }
        }
        
        System.out.printf("⚠️ 找不到事件: 時間=%d, 序列=%d%n", time, sequence);
        return false;
    }
    
    /**
     * 取消指定時間的所有事件
     */
    public int cancelEventsAtTime(int time) {
        int cancelledCount = 0;
        for (Event event : eventQueue) {
            if (event.getTime() == time && !event.isCancelled()) {
                event.cancel();
                cancelledEvents.add(event);
                cancelledCount++;
                System.out.printf("❌ 取消事件: [時間 %d] %s (序列:%d)%n",
                                 time, event.getType(), event.getSequence());
            }
        }
        return cancelledCount;
    }
    
    /**
     * 取消指定類型的所有事件
     */
    public int cancelEventsByType(String type) {
        int cancelledCount = 0;
        for (Event event : eventQueue) {
            if (event.getType().equals(type) && !event.isCancelled()) {
                event.cancel();
                cancelledEvents.add(event);
                cancelledCount++;
                System.out.printf("❌ 取消事件: [時間 %d] %s (序列:%d)%n",
                                 event.getTime(), type, event.getSequence());
            }
        }
        return cancelledCount;
    }
    
    /**
     * 執行下一個事件
     */
    public Event executeNextEvent() {
        // 移除已取消的事件
        removeCancelledEvents();
        
        if (eventQueue.isEmpty()) {
            System.out.println("⚠️ 沒有待執行的事件");
            return null;
        }
        
        isRunning = true;
        Event event = eventQueue.poll();
        currentTime = event.getTime();
        
        // 執行事件
        event.execute();
        executedEvents.add(event);
        
        isRunning = false;
        return event;
    }
    
    /**
     * 執行所有事件
     */
    public void executeAllEvents() {
        System.out.println("\n🚀 開始執行所有事件...");
        System.out.println("═══════════════════════════════════════════");
        
        int executedCount = 0;
        int skippedCount = 0;
        
        while (!eventQueue.isEmpty()) {
            // 移除已取消的事件
            removeCancelledEvents();
            
            if (eventQueue.isEmpty()) {
                break;
            }
            
            Event event = eventQueue.poll();
            
            // 檢查是否已被取消（雙重檢查）
            if (event.isCancelled()) {
                skippedCount++;
                continue;
            }
            
            currentTime = event.getTime();
            event.execute();
            executedEvents.add(event);
            executedCount++;
        }
        
        System.out.println("═══════════════════════════════════════════");
        System.out.printf("✅ 執行完成: 共執行 %d 個事件，跳過 %d 個已取消事件%n",
                         executedCount, skippedCount);
        System.out.println();
    }
    
    /**
     * 移除已取消的事件
     */
    private void removeCancelledEvents() {
        // 使用迭代器移除已取消的事件
        Iterator<Event> iterator = eventQueue.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.isCancelled()) {
                iterator.remove();
            }
        }
    }
    
    /**
     * 查看下一個事件（不移除）
     */
    public Event peekNextEvent() {
        // 移除已取消的事件
        removeCancelledEvents();
        
        if (eventQueue.isEmpty()) {
            System.out.println("⚠️ 沒有待執行的事件");
            return null;
        }
        
        Event next = eventQueue.peek();
        System.out.println("👀 下一個事件: " + next.toShortString());
        return next;
    }
    
    /**
     * 查看目前佇列狀態
     */
    public void showQueueStatus() {
        // 移除已取消的事件
        removeCancelledEvents();
        
        System.out.println("\n=== 事件佇列狀態 ===");
        System.out.println("待執行事件數: " + eventQueue.size());
        System.out.println("已執行事件數: " + executedEvents.size());
        System.out.println("已取消事件數: " + cancelledEvents.size());
        System.out.println("目前時間: " + currentTime);
        System.out.println("下一個序列號: " + nextSequence);
        
        if (!eventQueue.isEmpty()) {
            System.out.println("\n待執行事件清單 (依時間與序列排序):");
            System.out.println("時間 | 類型 | 序列 | 描述");
            System.out.println("-----|------|------|------------------------------");
            
            PriorityQueue<Event> tempQueue = new PriorityQueue<>(eventQueue);
            while (!tempQueue.isEmpty()) {
                Event e = tempQueue.poll();
                System.out.printf("%4d | %-4s | %4d | %s%n",
                                 e.getTime(), e.getType(), e.getSequence(), e.getDescription());
            }
        }
        System.out.println();
    }
    
    /**
     * 顯示完整執行記錄
     */
    public void showExecutionLog() {
        System.out.println("\n=== 完整執行記錄 ===");
        
        if (executedEvents.isEmpty()) {
            System.out.println("尚無執行記錄");
            return;
        }
        
        System.out.println("執行順序 | 時間 | 類型 | 序列 | 描述");
        System.out.println("---------|------|------|------|------------------------------");
        
        for (int i = 0; i < executedEvents.size(); i++) {
            Event e = executedEvents.get(i);
            System.out.printf("%8d | %4d | %-4s | %4d | %s%n",
                             i + 1, e.getTime(), e.getType(), e.getSequence(), e.getDescription());
        }
        System.out.println();
    }
    
    /**
     * 顯示已取消事件記錄
     */
    public void showCancelledLog() {
        System.out.println("\n=== 已取消事件記錄 ===");
        
        if (cancelledEvents.isEmpty()) {
            System.out.println("尚無已取消事件");
            return;
        }
        
        System.out.println("取消順序 | 時間 | 類型 | 序列 | 描述");
        System.out.println("---------|------|------|------|------------------------------");
        
        for (int i = 0; i < cancelledEvents.size(); i++) {
            Event e = cancelledEvents.get(i);
            System.out.printf("%8d | %4d | %-4s | %4d | %s%n",
                             i + 1, e.getTime(), e.getType(), e.getSequence(), e.getDescription());
        }
        System.out.println();
    }
    
    /**
     * 重置模擬器
     */
    public void reset() {
        eventQueue.clear();
        executedEvents.clear();
        cancelledEvents.clear();
        nextSequence = 1;
        currentTime = 0;
        isRunning = false;
        System.out.println("🔄 模擬器已重置");
    }
    
    /**
     * 事件資料輔助類
     */
    public static class EventData {
        public int time;
        public String type;
        public String description;
        
        public EventData(int time, String type, String description) {
            this.time = time;
            this.type = type;
            this.description = description;
        }
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 活動事件模擬器測試 ===\n");
        
        // 測試場景 1：基本事件排程與執行
        testBasicScheduling();
        
        // 測試場景 2：時間相同序列排序
        testSequenceOrdering();
        
        // 測試場景 3：取消指定事件
        testCancelEvent();
        
        // 測試場景 4：批量取消事件
        testBatchCancel();
        
        // 測試場景 5：完整執行記錄
        testExecutionLog();
        
        // 測試場景 6：邊界情況
        testEdgeCases();
    }
    
    /**
     * 測試基本事件排程與執行
     */
    private static void testBasicScheduling() {
        System.out.println("--- 測試 1: 基本事件排程與執行 ---");
        
        EventSimulationQueue simulator = new EventSimulationQueue();
        
        simulator.addEvent(3, "檢查", "定期檢查");
        simulator.addEvent(1, "初始化", "系統初始化");
        simulator.addEvent(2, "處理", "資料處理");
        simulator.addEvent(4, "完成", "系統完成");
        
        simulator.showQueueStatus();
        
        simulator.executeAllEvents();
        simulator.showExecutionLog();
        
        System.out.println();
    }
    
    /**
     * 測試時間相同序列排序
     */
    private static void testSequenceOrdering() {
        System.out.println("--- 測試 2: 時間相同序列排序 ---");
        
        EventSimulationQueue simulator = new EventSimulationQueue();
        
        System.out.println("新增時間相同的事件 (時間=5):");
        simulator.addEvent(5, "事件A", "時間相同的事件 A");
        simulator.addEvent(5, "事件B", "時間相同的事件 B");
        simulator.addEvent(5, "事件C", "時間相同的事件 C");
        simulator.addEvent(5, "事件D", "時間相同的事件 D");
        
        simulator.showQueueStatus();
        
        System.out.println("執行順序 (應依序列號 1,2,3,4):");
        simulator.executeAllEvents();
        
        System.out.println();
    }
    
    /**
     * 測試取消指定事件
     */
    private static void testCancelEvent() {
        System.out.println("--- 測試 3: 取消指定事件 ---");
        
        EventSimulationQueue simulator = new EventSimulationQueue();
        
        simulator.addEvent(2, "任務A", "重要任務 A");
        simulator.addEvent(1, "任務B", "次要任務 B");
        simulator.addEvent(3, "任務C", "緊急任務 C");
        simulator.addEvent(2, "任務D", "普通任務 D");
        
        simulator.showQueueStatus();
        
        // 取消特定事件
        System.out.println("取消事件: 時間=2, 序列=3");
        simulator.cancelEvent(2, 3);
        
        System.out.println("\n取消後佇列狀態:");
        simulator.showQueueStatus();
        
        // 執行所有事件
        simulator.executeAllEvents();
        simulator.showExecutionLog();
        
        System.out.println();
    }
    
    /**
     * 測試批量取消事件
     */
    private static void testBatchCancel() {
        System.out.println("--- 測試 4: 批量取消事件 ---");
        
        EventSimulationQueue simulator = new EventSimulationQueue();
        
        simulator.addEvent(1, "登入", "使用者登入");
        simulator.addEvent(2, "處理", "訂單處理");
        simulator.addEvent(3, "登出", "使用者登出");
        simulator.addEvent(4, "處理", "資料處理");
        simulator.addEvent(5, "登入", "管理員登入");
        simulator.addEvent(6, "處理", "報表處理");
        
        simulator.showQueueStatus();
        
        // 取消所有「處理」類型的事件
        System.out.println("取消所有 '處理' 類型的事件:");
        simulator.cancelEventsByType("處理");
        
        System.out.println("\n取消後佇列狀態:");
        simulator.showQueueStatus();
        
        // 執行所有事件
        simulator.executeAllEvents();
        simulator.showExecutionLog();
        
        System.out.println();
    }
    
    /**
     * 測試完整執行記錄
     */
    private static void testExecutionLog() {
        System.out.println("--- 測試 5: 完整執行記錄 ---");
        
        EventSimulationQueue simulator = new EventSimulationQueue();
        
        simulator.addEvent(10, "開始", "專案開始");
        simulator.addEvent(15, "階段一", "需求分析");
        simulator.addEvent(20, "階段二", "系統設計");
        simulator.addEvent(25, "階段三", "程式開發");
        simulator.addEvent(30, "階段四", "測試驗證");
        simulator.addEvent(35, "完成", "專案完成");
        
        simulator.executeAllEvents();
        
        System.out.println("完整執行記錄:");
        simulator.showExecutionLog();
        
        System.out.println("已取消事件記錄:");
        simulator.showCancelledLog();
        
        System.out.println();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 6: 邊界情況 ---");
        
        // 測試 6.1: 空佇列操作
        System.out.println("測試 6.1: 空佇列操作");
        EventSimulationQueue simulator = new EventSimulationQueue();
        simulator.showQueueStatus();
        simulator.peekNextEvent();
        simulator.executeNextEvent();
        simulator.executeAllEvents();
        System.out.println();
        
        // 測試 6.2: 取消不存在的事件
        System.out.println("測試 6.2: 取消不存在的事件");
        simulator.addEvent(1, "測試", "測試事件");
        simulator.cancelEvent(999, 999);
        simulator.executeAllEvents();
        System.out.println();
        
        // 測試 6.3: 取消已取消的事件
        System.out.println("測試 6.3: 取消已取消的事件");
        simulator.reset();
        simulator.addEvent(1, "事件A", "第一次取消");
        simulator.addEvent(2, "事件B", "第二次取消");
        simulator.cancelEvent(1, 1);
        simulator.cancelEvent(1, 1);  // 再次取消
        simulator.executeAllEvents();
        simulator.showExecutionLog();
        System.out.println();
        
        // 測試 6.4: 混合場景
        System.out.println("測試 6.4: 混合場景 (新增、取消、執行)");
        simulator.reset();
        simulator.addEvent(3, "任務1", "第一批任務");
        simulator.addEvent(1, "任務2", "第二批任務");
        simulator.addEvent(2, "任務3", "第三批任務");
        simulator.addEvent(4, "任務4", "第四批任務");
        simulator.addEvent(3, "任務5", "第五批任務");
        
        simulator.cancelEvent(3, 1);  // 取消任務1
        simulator.cancelEvent(2, 3);  // 取消任務3
        
        System.out.println("執行剩餘事件:");
        simulator.executeAllEvents();
        simulator.showExecutionLog();
        simulator.showCancelledLog();
        
        System.out.println();
    }
}