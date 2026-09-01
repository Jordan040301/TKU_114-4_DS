import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 學生鍵值對應示範
 * 使用 StudentKey 作為 Map 的鍵
 * 相容 Java 8-11
 */
public class StudentKeyMapDemo {

    /**
     * StudentKey 類別
     * 使用 department 和 studentId 作為複合鍵
     * 取代 Record 以相容 Java 11
     */
    public static class StudentKey {
        private final String department;
        private final String studentId;
        
        /**
         * 建構子：自動標準化輸入
         * @param department 系所代碼
         * @param studentId 學號
         */
        public StudentKey(String department, String studentId) {
            // 標準化參數
            this.department = normalize(department, "department");
            this.studentId = normalize(studentId, "studentId");
            
            // 驗證 department 格式 (2-4 個大寫字母)
            if (!this.department.matches("[A-Z]{2,4}")) {
                throw new IllegalArgumentException("系所代碼必須為 2-4 個大寫字母");
            }
            
            // 驗證 studentId 格式 (8 位數字)
            if (!this.studentId.matches("[0-9]{8}")) {
                throw new IllegalArgumentException("學號必須為 8 位數字");
            }
        }
        
        /**
         * 標準化字串：去除前後空白，轉為大寫
         * @param value 要標準化的字串
         * @param field 欄位名稱 (用於錯誤訊息)
         * @return 標準化後的字串
         */
        private static String normalize(String value, String field) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(field + " 不能為 null 或空白");
            }
            return value.trim().toUpperCase();
        }
        
        public String getDepartment() {
            return department;
        }
        
        public String getStudentId() {
            return studentId;
        }
        
        /**
         * 簡短格式
         */
        public String toShortString() {
            return department + "-" + studentId;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            StudentKey that = (StudentKey) obj;
            return Objects.equals(department, that.department) &&
                   Objects.equals(studentId, that.studentId);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(department, studentId);
        }
        
        @Override
        public String toString() {
            return String.format("StudentKey{department='%s', studentId='%s'}", 
                               department, studentId);
        }
    }
    
    /**
     * 顯示 Map 內容
     */
    public static void printMap(Map<StudentKey, String> map, String title) {
        System.out.println("\n=== " + title + " ===");
        if (map.isEmpty()) {
            System.out.println("  (空)");
            return;
        }
        System.out.println("  Map 大小: " + map.size());
        System.out.println("  內容:");
        for (Map.Entry<StudentKey, String> entry : map.entrySet()) {
            System.out.printf("    %s → %s%n", 
                             entry.getKey().toShortString(), 
                             entry.getValue());
        }
    }
    
    /**
     * 顯示所有學生的詳細資訊
     */
    public static void printStudentDetails(Map<StudentKey, String> map) {
        System.out.println("\n=== 學生詳細資訊 ===");
        System.out.printf("%-12s | %-12s | %s%n", "系所", "學號", "姓名");
        System.out.println("------------|------------|----------------");
        
        for (Map.Entry<StudentKey, String> entry : map.entrySet()) {
            StudentKey key = entry.getKey();
            System.out.printf("%-12s | %-12s | %s%n", 
                             key.getDepartment(), 
                             key.getStudentId(), 
                             entry.getValue());
        }
    }
    
    /**
     * 查詢學生
     */
    public static void queryStudent(Map<StudentKey, String> map, 
                                   String department, String studentId) {
        try {
            StudentKey key = new StudentKey(department, studentId);
            String name = map.get(key);
            if (name != null) {
                System.out.printf("✅ 找到學生: %s → %s%n", key.toShortString(), name);
            } else {
                System.out.printf("❌ 找不到學生: %s%n", key.toShortString());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️  查詢失敗: " + e.getMessage());
        }
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 學生鍵值對應示範 ===\n");
        
        // 測試 1：建立並使用 StudentKey
        testStudentKeyCreation();
        
        // 測試 2：Map 操作
        testMapOperations();
        
        // 測試 3：驗證和錯誤處理
        testValidation();
        
        // 測試 4：相等性與雜湊碼
        testEqualityAndHashCode();
        
        // 測試 5：實務應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試 StudentKey 建立
     */
    private static void testStudentKeyCreation() {
        System.out.println("--- 測試 1: StudentKey 建立 ---");
        
        // 正常建立
        StudentKey key1 = new StudentKey("CS", "20241001");
        StudentKey key2 = new StudentKey("ee", "20241002");  // 會自動轉大寫
        StudentKey key3 = new StudentKey("  MATH  ", "20241003  "); // 會自動 trim
        
        System.out.println("key1: " + key1);
        System.out.println("key2: " + key2);
        System.out.println("key3: " + key3);
        System.out.println("key1 簡短格式: " + key1.toShortString());
        
        // 顯示欄位
        System.out.println("key1.getDepartment(): " + key1.getDepartment());
        System.out.println("key1.getStudentId(): " + key1.getStudentId());
        System.out.println();
    }
    
    /**
     * 測試 Map 操作
     */
    private static void testMapOperations() {
        System.out.println("--- 測試 2: Map 操作 ---");
        
        Map<StudentKey, String> studentMap = new HashMap<>();
        
        // 新增學生
        System.out.println("新增學生:");
        studentMap.put(new StudentKey("CS", "20241001"), "王小明");
        studentMap.put(new StudentKey("CS", "20241002"), "李小華");
        studentMap.put(new StudentKey("EE", "20241003"), "張大志");
        studentMap.put(new StudentKey("EE", "20241004"), "陳美玲");
        studentMap.put(new StudentKey("MATH", "20241005"), "林建國");
        studentMap.put(new StudentKey("MATH", "20241006"), "吳美麗");
        
        printMap(studentMap, "學生 Map");
        printStudentDetails(studentMap);
        
        // 查詢測試
        System.out.println("\n查詢測試:");
        queryStudent(studentMap, "CS", "20241001");
        queryStudent(studentMap, "ee", "20241003");  // 不區分大小寫
        queryStudent(studentMap, "CS", "99999999");  // 不存在的學號
        
        // 更新測試
        System.out.println("\n更新學生姓名:");
        studentMap.put(new StudentKey("CS", "20241001"), "王大明 (更新)");
        System.out.println("更新後: " + studentMap.get(new StudentKey("CS", "20241001")));
        
        // 刪除測試
        System.out.println("\n刪除學生:");
        studentMap.remove(new StudentKey("MATH", "20241006"));
        System.out.println("刪除後 Map 大小: " + studentMap.size());
        
        printMap(studentMap, "最終學生 Map");
        System.out.println();
    }
    
    /**
     * 測試驗證和錯誤處理
     */
    private static void testValidation() {
        System.out.println("--- 測試 3: 驗證和錯誤處理 ---");
        
        // 測試 3.1: null 值
        System.out.println("測試 3.1: null 值");
        try {
            new StudentKey(null, "20241001");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        
        // 測試 3.2: 空白字串
        System.out.println("\n測試 3.2: 空白字串");
        try {
            new StudentKey("  ", "20241001");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        
        // 測試 3.3: 系所代碼格式錯誤
        System.out.println("\n測試 3.3: 系所代碼格式錯誤 (不是字母)");
        try {
            new StudentKey("CS123", "20241001");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        
        // 測試 3.4: 學號格式錯誤
        System.out.println("\n測試 3.4: 學號格式錯誤 (不是 8 位數字)");
        try {
            new StudentKey("CS", "2024100");  // 只有 7 位
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        
        try {
            new StudentKey("CS", "2024100A");  // 包含字母
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * 測試相等性與雜湊碼
     */
    private static void testEqualityAndHashCode() {
        System.out.println("--- 測試 4: 相等性與雜湊碼 ---");
        
        StudentKey key1 = new StudentKey("CS", "20241001");
        StudentKey key2 = new StudentKey("cs", "20241001");  // 小寫，應該相等
        StudentKey key3 = new StudentKey("CS", "20241002");
        StudentKey key4 = new StudentKey("EE", "20241001");
        
        System.out.println("key1: " + key1);
        System.out.println("key2: " + key2);
        System.out.println("key3: " + key3);
        System.out.println("key4: " + key4);
        
        System.out.println("\n相等性測試:");
        System.out.println("key1.equals(key2): " + key1.equals(key2) + " (應該為 true)");
        System.out.println("key1.equals(key3): " + key1.equals(key3) + " (應該為 false)");
        System.out.println("key1.equals(key4): " + key1.equals(key4) + " (應該為 false)");
        
        System.out.println("\n雜湊碼測試:");
        System.out.println("key1.hashCode(): " + key1.hashCode());
        System.out.println("key2.hashCode(): " + key2.hashCode() + " (應該相同)");
        System.out.println("key3.hashCode(): " + key3.hashCode());
        System.out.println("key4.hashCode(): " + key4.hashCode());
        
        // 使用 Map 驗證相等性
        Map<StudentKey, String> map = new HashMap<>();
        map.put(key1, "測試學生");
        System.out.println("\nMap 使用 key2 查詢: " + map.get(key2) + " (應該找到)");
        System.out.println();
    }
    
    /**
     * 測試實務應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 5: 實務應用場景 ---");
        
        Map<StudentKey, Map<String, Object>> studentData = new HashMap<>();
        
        // 模擬學生資料
        addStudentData(studentData, "CS", "20241001", "王小明", 85.5, 3);
        addStudentData(studentData, "CS", "20241002", "李小華", 92.0, 4);
        addStudentData(studentData, "EE", "20241003", "張大志", 78.0, 2);
        addStudentData(studentData, "EE", "20241004", "陳美玲", 95.5, 4);
        addStudentData(studentData, "MATH", "20241005", "林建國", 88.0, 3);
        addStudentData(studentData, "MATH", "20241006", "吳美麗", 76.5, 2);
        
        // 顯示學生資料
        System.out.println("學生資料庫:");
        System.out.printf("%-12s | %-12s | %-8s | %6s | %s%n", 
                         "系所", "學號", "姓名", "成績", "年級");
        System.out.println("------------|------------|--------|--------|------");
        
        for (Map.Entry<StudentKey, Map<String, Object>> entry : studentData.entrySet()) {
            StudentKey key = entry.getKey();
            Map<String, Object> data = entry.getValue();
            System.out.printf("%-12s | %-12s | %-8s | %6.1f | %d%n",
                             key.getDepartment(),
                             key.getStudentId(),
                             data.get("name"),
                             data.get("grade"),
                             data.get("year"));
        }
        
        // 查詢特定學生
        System.out.println("\n查詢學生 CS-20241002:");
        StudentKey searchKey = new StudentKey("CS", "20241002");
        Map<String, Object> result = studentData.get(searchKey);
        if (result != null) {
            System.out.println("  姓名: " + result.get("name"));
            System.out.println("  成績: " + result.get("grade"));
            System.out.println("  年級: " + result.get("year"));
        }
        
        // 統計各系所人數
        System.out.println("\n各系所學生人數統計:");
        Map<String, Integer> deptCount = new HashMap<>();
        for (StudentKey key : studentData.keySet()) {
            String dept = key.getDepartment();
            deptCount.put(dept, deptCount.getOrDefault(dept, 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : deptCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " 人");
        }
        
        System.out.println();
    }
    
    /**
     * 輔助方法：新增學生資料
     */
    private static void addStudentData(Map<StudentKey, Map<String, Object>> map,
                                      String department, String studentId,
                                      String name, double grade, int year) {
        StudentKey key = new StudentKey(department, studentId);
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("grade", grade);
        data.put("year", year);
        map.put(key, data);
    }
}