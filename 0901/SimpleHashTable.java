import java.util.ArrayList;
import java.util.List;

/**
 * 簡單雜湊表實作
 * 使用鏈結法處理碰撞，支援泛型
 * 相容 Java 8-11
 */
public class SimpleHashTable<K, V> {
    
    /**
     * 條目類別 (取代 Record 以相容 Java 11)
     */
    private static class Entry<K, V> {
        private final K key;
        private final V value;
        
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return key;
        }
        
        public V getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            return String.format("(%s, %s)", key, value);
        }
    }
    
    private final List<List<Entry<K, V>>> buckets;
    private int size;
    
    /**
     * 建構子
     * @param bucketCount 桶的數量
     */
    public SimpleHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount 必須大於 0");
        }
        buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
        size = 0;
    }
    
    /**
     * 計算索引
     * @param key 鍵
     * @return 桶索引
     */
    private int index(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key 不能為 null");
        }
        return Math.floorMod(key.hashCode(), buckets.size());
    }
    
    /**
     * 插入或更新鍵值對
     * @param key 鍵
     * @param value 值
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key 不能為 null");
        }
        
        int bucketIndex = index(key);
        List<Entry<K, V>> chain = buckets.get(bucketIndex);
        
        // 檢查是否已存在相同的 key
        for (Entry<K, V> entry : chain) {
            if (entry.getKey().equals(key)) {
                // 更新現有條目 (由於 Entry 是不可變的，我們需要替換)
                // 為了簡化，這裡我們直接替換整個條目
                // 但更正確的做法是使用可變的 Entry，或移除舊的再加入新的
                chain.remove(entry);
                chain.add(new Entry<>(key, value));
                System.out.printf("🔄 更新: key=%s, value=%s%n", key, value);
                return;
            }
        }
        
        // 新增條目
        chain.add(new Entry<>(key, value));
        size++;
        System.out.printf("✅ 插入: key=%s, value=%s (size=%d)%n", key, value, size);
    }
    
    /**
     * 取得指定鍵的值
     * @param key 鍵
     * @return 值，若不存在則回傳 null
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key 不能為 null");
        }
        
        int bucketIndex = index(key);
        List<Entry<K, V>> chain = buckets.get(bucketIndex);
        
        for (Entry<K, V> entry : chain) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    /**
     * 檢查是否包含指定的鍵
     * @param key 鍵
     * @return true 如果存在
     */
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        
        int bucketIndex = index(key);
        List<Entry<K, V>> chain = buckets.get(bucketIndex);
        
        for (Entry<K, V> entry : chain) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 刪除指定鍵的條目
     * @param key 鍵
     * @return 被刪除的值，若不存在則回傳 null
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key 不能為 null");
        }
        
        int bucketIndex = index(key);
        List<Entry<K, V>> chain = buckets.get(bucketIndex);
        
        for (int i = 0; i < chain.size(); i++) {
            Entry<K, V> entry = chain.get(i);
            if (entry.getKey().equals(key)) {
                V removedValue = entry.getValue();
                chain.remove(i);
                size--;
                System.out.printf("🗑️ 刪除: key=%s, value=%s (size=%d)%n", key, removedValue, size);
                return removedValue;
            }
        }
        
        System.out.printf("⚠️ 找不到 key=%s%n", key);
        return null;
    }
    
    /**
     * 取得元素數量
     * @return 元素數量
     */
    public int size() {
        return size;
    }
    
    /**
     * 檢查是否為空
     * @return true 如果為空
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * 取得桶的數量
     * @return 桶的數量
     */
    public int getBucketCount() {
        return buckets.size();
    }
    
    /**
     * 清空雜湊表
     */
    public void clear() {
        for (List<Entry<K, V>> chain : buckets) {
            chain.clear();
        }
        size = 0;
        System.out.println("🔄 已清空雜湊表");
    }
    
    /**
     * 印出所有鍵值對
     */
    public void printAllEntries() {
        if (isEmpty()) {
            System.out.println("雜湊表為空");
            return;
        }
        
        System.out.println("\n=== 所有鍵值對 ===");
        int count = 0;
        for (int i = 0; i < buckets.size(); i++) {
            List<Entry<K, V>> chain = buckets.get(i);
            for (Entry<K, V> entry : chain) {
                System.out.printf("  [%d] %s -> %s%n", i, entry.getKey(), entry.getValue());
                count++;
            }
        }
        System.out.printf("總計: %d 個條目%n", count);
        System.out.println();
    }
    
    /**
     * 印出桶報告
     */
    public void printBucketReport() {
        System.out.println("\n=== 桶報告 ===");
        System.out.printf("桶數量: %d%n", buckets.size());
        System.out.printf("元素數量: %d%n", size);
        System.out.printf("負載因數: %.2f%n", (double) size / buckets.size());
        System.out.println("\n桶內容:");
        System.out.println("桶索引 | 鏈長度 | 內容");
        System.out.println("-------|--------|------------------------------");
        
        for (int i = 0; i < buckets.size(); i++) {
            List<Entry<K, V>> chain = buckets.get(i);
            int chainLength = chain.size();
            
            StringBuilder content = new StringBuilder();
            if (chainLength == 0) {
                content.append("空");
            } else {
                for (int j = 0; j < chain.size(); j++) {
                    if (j > 0) content.append(" → ");
                    content.append(chain.get(j).toString());
                }
            }
            
            System.out.printf("%6d | %6d | %s%n", i, chainLength, content.toString());
        }
        System.out.println();
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 簡單雜湊表測試 ===\n");
        
        // 測試 1：基本功能
        testBasicOperations();
        
        // 測試 2：更新和刪除
        testUpdateAndDelete();
        
        // 測試 3：邊界情況
        testEdgeCases();
        
        // 測試 4：不同型別
        testDifferentTypes();
        
        // 測試 5：完整功能展示
        testComprehensive();
    }
    
    /**
     * 測試基本操作
     */
    private static void testBasicOperations() {
        System.out.println("--- 測試 1: 基本操作 ---");
        
        SimpleHashTable<String, String> table = new SimpleHashTable<>(5);
        
        System.out.println("插入資料:");
        table.put("A001", "Apple");
        table.put("B002", "Banana");
        table.put("C003", "Cherry");
        table.put("D004", "Date");
        table.put("E005", "Elderberry");
        
        System.out.println("\n查詢:");
        System.out.println("  get('A001') = " + table.get("A001"));
        System.out.println("  get('C003') = " + table.get("C003"));
        System.out.println("  get('F006') = " + table.get("F006"));
        
        System.out.println("\n存在性檢查:");
        System.out.println("  containsKey('B002') = " + table.containsKey("B002"));
        System.out.println("  containsKey('Z999') = " + table.containsKey("Z999"));
        
        System.out.println("\nsize = " + table.size());
        System.out.println("isEmpty = " + table.isEmpty());
        
        table.printAllEntries();
        table.printBucketReport();
    }
    
    /**
     * 測試更新和刪除
     */
    private static void testUpdateAndDelete() {
        System.out.println("--- 測試 2: 更新和刪除 ---");
        
        SimpleHashTable<Integer, String> table = new SimpleHashTable<>(3);
        
        System.out.println("插入資料:");
        table.put(1, "One");
        table.put(2, "Two");
        table.put(3, "Three");
        table.put(4, "Four");
        table.put(5, "Five");
        
        table.printAllEntries();
        
        System.out.println("\n更新 key=3:");
        table.put(3, "Three (更新)");
        
        System.out.println("\n更新 key=1:");
        table.put(1, "One (更新)");
        
        System.out.println("\n刪除 key=4:");
        table.remove(4);
        
        System.out.println("\n刪除 key=6 (不存在):");
        table.remove(6);
        
        System.out.println("\n最終狀態:");
        table.printAllEntries();
        table.printBucketReport();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 3: 邊界情況 ---");
        
        // 測試 3.1: 空表
        System.out.println("測試 3.1: 空表");
        SimpleHashTable<String, String> table = new SimpleHashTable<>(5);
        System.out.println("  size = " + table.size());
        System.out.println("  isEmpty = " + table.isEmpty());
        table.printAllEntries();
        System.out.println();
        
        // 測試 3.2: 單個元素
        System.out.println("測試 3.2: 單個元素");
        table.put("single", "Only One");
        System.out.println("  size = " + table.size());
        System.out.println("  get('single') = " + table.get("single"));
        table.printAllEntries();
        System.out.println();
        
        // 測試 3.3: 大量元素 (碰撞)
        System.out.println("測試 3.3: 大量元素");
        SimpleHashTable<Integer, String> table2 = new SimpleHashTable<>(3);
        for (int i = 0; i < 20; i++) {
            table2.put(i, "Value" + i);
        }
        table2.printBucketReport();
        
        // 測試 3.4: null 處理
        System.out.println("測試 3.4: null 處理");
        try {
            table.put(null, "Null Key");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲 null key 例外: " + e.getMessage());
        }
        
        try {
            table.get(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲 null key 例外: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * 測試不同型別
     */
    private static void testDifferentTypes() {
        System.out.println("--- 測試 4: 不同型別 ---");
        
        // 測試 4.1: String -> String
        System.out.println("測試 4.1: String -> String");
        SimpleHashTable<String, String> stringTable = new SimpleHashTable<>(5);
        stringTable.put("name", "John");
        stringTable.put("city", "Taipei");
        stringTable.printAllEntries();
        System.out.println();
        
        // 測試 4.2: Integer -> String
        System.out.println("測試 4.2: Integer -> String");
        SimpleHashTable<Integer, String> intTable = new SimpleHashTable<>(5);
        intTable.put(100, "Hundred");
        intTable.put(200, "Two Hundred");
        intTable.printAllEntries();
        System.out.println();
        
        // 測試 4.3: String -> Integer
        System.out.println("測試 4.3: String -> Integer");
        SimpleHashTable<String, Integer> scoreTable = new SimpleHashTable<>(5);
        scoreTable.put("Alice", 95);
        scoreTable.put("Bob", 87);
        scoreTable.put("Charlie", 92);
        scoreTable.printAllEntries();
        System.out.println();
    }
    
    /**
     * 測試完整功能展示
     */
    private static void testComprehensive() {
        System.out.println("--- 測試 5: 完整功能展示 ---");
        System.out.println("模擬學生成績管理系統");
        
        SimpleHashTable<String, Integer> gradeTable = new SimpleHashTable<>(7);
        
        // 插入學生資料
        String[] students = {"張小明", "李小華", "王大明", "陳美玲", "林建國", "吳美麗", "周志強", "劉佳欣"};
        int[] grades = {85, 92, 78, 95, 88, 76, 90, 83};
        
        for (int i = 0; i < students.length; i++) {
            gradeTable.put(students[i], grades[i]);
        }
        
        gradeTable.printAllEntries();
        gradeTable.printBucketReport();
        
        // 查詢
        System.out.println("\n成績查詢:");
        String[] queryStudents = {"張小明", "王大明", "林建國", "不存在"};
        for (String name : queryStudents) {
            Integer grade = gradeTable.get(name);
            if (grade != null) {
                System.out.println("  " + name + ": " + grade + " 分");
            } else {
                System.out.println("  " + name + ": 找不到學生");
            }
        }
        
        // 更新成績
        System.out.println("\n更新成績:");
        gradeTable.put("張小明", 88);
        gradeTable.put("陳美玲", 97);
        
        // 刪除學生
        System.out.println("\n刪除學生:");
        gradeTable.remove("吳美麗");
        
        // 最終狀態
        gradeTable.printAllEntries();
        
        // 統計
        System.out.println("\n統計:");
        System.out.println("  學生總數: " + gradeTable.size());
        System.out.println("  桶數量: " + gradeTable.getBucketCount());
        System.out.println("  平均鏈長度: " + String.format("%.2f", 
                          (double) gradeTable.size() / gradeTable.getBucketCount()));
        System.out.println("  是否為空: " + gradeTable.isEmpty());
    }
}