import java.util.*;

/**
 * 急診候診佇列
 * 根據危急程度、到院順序與病歷號建立穩定的優先佇列
 */
class Patient {
    private String medicalRecord;  // 病歷號
    private int severity;          // 危急程度 (數字越大越緊急)
    private int arrivalOrder;      // 到院順序
    private String name;           // 病人姓名
    
    public Patient(String medicalRecord, int severity, int arrivalOrder, String name) {
        this.medicalRecord = medicalRecord;
        this.severity = severity;
        this.arrivalOrder = arrivalOrder;
        this.name = name;
    }
    
    public String getMedicalRecord() {
        return medicalRecord;
    }
    
    public int getSeverity() {
        return severity;
    }
    
    public int getArrivalOrder() {
        return arrivalOrder;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return String.format("病歷號:%s | 姓名:%s | 危急程度:%d | 到院順序:%d",
                           medicalRecord, name, severity, arrivalOrder);
    }
    
    /**
     * 簡短顯示格式
     */
    public String toShortString() {
        return String.format("%s(%s)[%d]", name, medicalRecord, severity);
    }
}

/**
 * 急診候診佇列管理系統
 */
public class EmergencyTriageQueue {
    private PriorityQueue<Patient> queue;
    private int nextArrivalOrder;  // 下一個到院順序號碼
    private int totalCalled;       // 總叫號次數
    
    /**
     * 建構子：初始化優先佇列
     */
    public EmergencyTriageQueue() {
        // 比較器：危急程度降序（越大越優先），到院順序升序（越早越優先）
        Comparator<Patient> comparator = Comparator
            .comparingInt(Patient::getSeverity)
            .reversed()  // 危急程度降序
            .thenComparingInt(Patient::getArrivalOrder);  // 到院順序升序
        
        this.queue = new PriorityQueue<>(comparator);
        this.nextArrivalOrder = 1;
        this.totalCalled = 0;
    }
    
    /**
     * 病人報到
     * @param medicalRecord 病歷號
     * @param severity 危急程度 (1-5，數字越大越緊急)
     * @param name 病人姓名
     */
    public void checkIn(String medicalRecord, int severity, String name) {
        // 驗證危急程度
        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException("危急程度必須介於 1 到 5 之間");
        }
        
        Patient patient = new Patient(medicalRecord, severity, nextArrivalOrder, name);
        queue.offer(patient);
        nextArrivalOrder++;
        
        System.out.printf("✅ 病人報到成功: %s (病歷號:%s, 危急程度:%d)%n",
                         name, medicalRecord, severity);
        System.out.println("   目前候診人數: " + queue.size());
    }
    
    /**
     * 查看下一位病人（不叫號）
     * @return 下一位病人的資訊，若無病人則回傳 null
     */
    public Patient peekNext() {
        if (queue.isEmpty()) {
            System.out.println("⚠️ 目前沒有候診病人");
            return null;
        }
        
        Patient next = queue.peek();
        System.out.println("👀 下一位候診病人: " + next.toShortString());
        return next;
    }
    
    /**
     * 叫號：取出並回傳最高優先權的病人
     * @return 被叫號的病人，若無病人則回傳 null
     */
    public Patient callNext() {
        if (queue.isEmpty()) {
            System.out.println("⚠️ 目前沒有候診病人，無法叫號");
            return null;
        }
        
        Patient patient = queue.poll();
        totalCalled++;
        
        System.out.printf("🔔 叫號 #%d: %s (病歷號:%s, 危急程度:%d)%n",
                         totalCalled, patient.getName(), 
                         patient.getMedicalRecord(), patient.getSeverity());
        System.out.println("   剩餘候診人數: " + queue.size());
        
        return patient;
    }
    
    /**
     * 查詢目前佇列狀態
     */
    public void showQueueStatus() {
        System.out.println("\n=== 急診候診佇列狀態 ===");
        System.out.println("候診總人數: " + queue.size());
        System.out.println("累計叫號次數: " + totalCalled);
        System.out.println("下一個到院順序號碼: " + nextArrivalOrder);
        
        if (queue.isEmpty()) {
            System.out.println("目前沒有候診病人");
        } else {
            System.out.println("\n目前候診病人清單 (依優先權排序):");
            System.out.println("順序 | 姓名 | 病歷號 | 危急程度 | 到院順序");
            System.out.println("------|------|--------|----------|----------");
            
            // 建立暫時佇列來顯示所有病人（不影響原始佇列）
            PriorityQueue<Patient> tempQueue = new PriorityQueue<>(queue);
            int order = 1;
            while (!tempQueue.isEmpty()) {
                Patient p = tempQueue.poll();
                System.out.printf("%4d  | %-4s | %-6s | %8d | %6d%n",
                                 order++, p.getName(), p.getMedicalRecord(),
                                 p.getSeverity(), p.getArrivalOrder());
            }
        }
        System.out.println();
    }
    
    /**
     * 檢查佇列是否為空
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    /**
     * 取得佇列大小
     */
    public int size() {
        return queue.size();
    }
    
    /**
     * 清空佇列（用於測試）
     */
    public void clear() {
        queue.clear();
        nextArrivalOrder = 1;
        totalCalled = 0;
        System.out.println("🔄 佇列已清空");
    }
    
    /**
     * 取得所有病人的清單（按優先權排序）
     */
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        PriorityQueue<Patient> tempQueue = new PriorityQueue<>(queue);
        while (!tempQueue.isEmpty()) {
            patients.add(tempQueue.poll());
        }
        return patients;
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 急診候診佇列系統測試 ===\n");
        
        EmergencyTriageQueue triage = new EmergencyTriageQueue();
        
        // 測試場景 1：基本報到與叫號
        testBasicCheckInAndCall(triage);
        
        // 測試場景 2：查看下一位
        testPeekNext(triage);
        
        // 測試場景 3：危急程度排序
        testSeveritySorting();
        
        // 測試場景 4：空佇列處理
        testEmptyQueueHandling();
        
        // 測試場景 5：穩定性測試（相同危急程度，按到院順序）
        testStability();
        
        // 測試場景 6：大量病人測試
        testLargeScale();
    }
    
    /**
     * 測試基本報到與叫號
     */
    private static void testBasicCheckInAndCall(EmergencyTriageQueue triage) {
        System.out.println("--- 測試 1: 基本報到與叫號 ---");
        
        triage.checkIn("A001", 3, "王大明");
        triage.checkIn("A002", 5, "李小華");
        triage.checkIn("A003", 2, "張美玲");
        triage.checkIn("A004", 4, "陳志強");
        
        triage.showQueueStatus();
        
        // 連續叫號
        System.out.println("開始叫號:");
        triage.callNext();
        triage.callNext();
        triage.callNext();
        triage.callNext();
        triage.callNext();  // 空佇列測試
        
        System.out.println();
    }
    
    /**
     * 測試查看下一位
     */
    private static void testPeekNext(EmergencyTriageQueue triage) {
        System.out.println("--- 測試 2: 查看下一位 ---");
        
        // 清空並重新加入病人
        triage.clear();
        triage.checkIn("B001", 4, "林美華");
        triage.checkIn("B002", 2, "黃志明");
        
        triage.peekNext();  // 查看但不叫號
        triage.showQueueStatus();
        
        triage.callNext();  // 實際叫號
        triage.peekNext();  // 查看下一位
        
        System.out.println();
    }
    
    /**
     * 測試危急程度排序
     */
    private static void testSeveritySorting() {
        System.out.println("--- 測試 3: 危急程度排序測試 ---");
        
        EmergencyTriageQueue triage = new EmergencyTriageQueue();
        
        System.out.println("報到病人 (危急程度 1-5):");
        triage.checkIn("C005", 1, "劉建國");
        triage.checkIn("C001", 5, "吳美麗");
        triage.checkIn("C003", 3, "林志玲");
        triage.checkIn("C002", 4, "周杰倫");
        triage.checkIn("C004", 2, "蔡依林");
        
        triage.showQueueStatus();
        
        System.out.println("叫號順序 (應依危急程度降序):");
        while (!triage.isEmpty()) {
            triage.callNext();
        }
        System.out.println();
    }
    
    /**
     * 測試空佇列處理
     */
    private static void testEmptyQueueHandling() {
        System.out.println("--- 測試 4: 空佇列處理 ---");
        
        EmergencyTriageQueue triage = new EmergencyTriageQueue();
        
        System.out.println("新建立的空佇列:");
        triage.showQueueStatus();
        
        System.out.println("測試空佇列操作:");
        triage.peekNext();
        triage.callNext();
        
        // 加入一個病人後再測試
        triage.checkIn("D001", 3, "測試病人");
        triage.callNext();
        triage.peekNext();  // 空佇列查看
        
        System.out.println();
    }
    
    /**
     * 測試穩定性（相同危急程度時按到院順序）
     */
    private static void testStability() {
        System.out.println("--- 測試 5: 穩定性測試 (相同危急程度) ---");
        
        EmergencyTriageQueue triage = new EmergencyTriageQueue();
        
        System.out.println("報到 5 位相同危急程度 (severity=3) 的病人:");
        triage.checkIn("E001", 3, "病人甲");
        triage.checkIn("E002", 3, "病人乙");
        triage.checkIn("E003", 3, "病人丙");
        triage.checkIn("E004", 3, "病人丁");
        triage.checkIn("E005", 3, "病人戊");
        
        System.out.println("\n叫號順序 (應按到院順序):");
        System.out.println("叫號 | 姓名 | 病歷號 | 危急程度 | 到院順序");
        System.out.println("-----|------|--------|----------|----------");
        int callCount = 0;
        while (!triage.isEmpty()) {
            Patient p = triage.callNext();
            callCount++;
            System.out.printf("%4d | %-4s | %-6s | %8d | %6d%n",
                            callCount, p.getName(), p.getMedicalRecord(),
                            p.getSeverity(), p.getArrivalOrder());
        }
        System.out.println();
    }
    
    /**
     * 測試大量病人
     */
    private static void testLargeScale() {
        System.out.println("--- 測試 6: 大量病人測試 ---");
        
        EmergencyTriageQueue triage = new EmergencyTriageQueue();
        Random random = new Random(42);  // 固定種子確保可重現
        
        System.out.println("隨機報到 20 位病人 (危急程度 1-5):");
        for (int i = 0; i < 20; i++) {
            String medicalRecord = String.format("F%03d", i + 1);
            int severity = random.nextInt(5) + 1;
            String name = "病人" + (char)('A' + i % 26) + (i / 26 + 1);
            triage.checkIn(medicalRecord, severity, name);
        }
        
        triage.showQueueStatus();
        
        // 叫號統計
        System.out.println("開始叫號 (取前 10 位):");
        for (int i = 0; i < 10 && !triage.isEmpty(); i++) {
            triage.callNext();
        }
        
        System.out.println("\n剩餘候診人數: " + triage.size());
        System.out.println();
    }
}