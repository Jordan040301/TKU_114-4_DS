import java.util.*;

/**
 * 可擴充哈希表
 * 使用單獨鏈結法處理碰撞，負載因子大於 0.75 時自動擴容
 */
public class ResizableStringMap {
    
    /**
     * 條目節點
     */
    private static class Entry {
        private final String key;
        private String value;
        private Entry next;
        
        public Entry(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
        
        public String getKey() {
            return key;
        }
        
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return String.format("(%s, %s)", key, value);
        }
    }
    
    private Entry[] buckets;     // 桶陣列
    private int size;            // 元素數量
    private int capacity;        // 桶容量
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_MULTIPLIER = 2;  // 擴容倍數
    private static final int RESIZE_ADDITION = 1;    // 擴容時多加 1
    
    /**
     * 建構子：使用預設容量
     */
    public ResizableStringMap() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * 建構子：指定初始容量
     * @param initialCapacity 初始容量
     */
    public ResizableStringMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("容量必須大於 0");
        }
        this.capacity = initialCapacity;
        this.buckets = new Entry[capacity];
        this.size = 0;
    }
    
    /**
     * 雜湊函數
     * @param key 鍵
     * @return 桶索引
     */
    private int hash(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key 不能為 null");
        }
        return Math.abs(key.hashCode()) % capacity;
    }
    
    /**
     * 插入或更新鍵值對
     * @param key 鍵
     * @param value 值
     */
    public void put(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key 不能為 null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value 不能為 null");
        }
        
        // 檢查是否需要擴容
        if ((double) size / capacity > LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        
        int index = hash(key);
        Entry current = buckets[index];
        
        // 檢查是否已存在相同的 key
        while (current != null) {
            if (current.getKey().equals(key)) {
                // 更新現有條目
                String oldValue = current.getValue();
                current.setValue(value);
                System.out.printf("🔄 更新: key='%s', value='%s' (原值: '%s')%n", 
                                 key, value, oldValue);
                return;
            }
            current = current.next;
        }
        
        // 新增條目 (頭插法)
        Entry newEntry = new Entry(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;
        
        System.out.printf("✅ 插入: key='%s', value='%s' (size=%d, 負載=%.2f)%n", 
                         key, value, size, (double) size / capacity);
    }
    
    /**
     * 取得指定鍵的值
     * @param key 鍵
     * @return 值，若不存在則回傳 null
     */
    public String get(String key) {
        if (key == null) {
            return null;
        }
        
        int index = hash(key);
        Entry current = buckets[index];
        
        while (current != null) {
            if (current.getKey().equals(key)) {
                return current.getValue();
            }
            current = current.next;
        }
        
        return null;
    }
    
    /**
     * 檢查是否包含指定的鍵
     * @param key 鍵
     * @return true 如果存在
     */
    public boolean containsKey(String key) {
        if (key == null) {
            return false;
        }
        
        int index = hash(key);
        Entry current = buckets[index];
        
        while (current != null) {
            if (current.getKey().equals(key)) {
                return true;
            }
            current = current.next;
        }
        
        return false;
    }
    
    /**
     * 刪除指定鍵的條目
     * @param key 鍵
     * @return 被刪除的值，若不存在則回傳 null
     */
    public String remove(String key) {
        if (key == null) {
            return null;
        }
        
        int index = hash(key);
        Entry current = buckets[index];
        Entry prev = null;
        
        while (current != null) {
            if (current.getKey().equals(key)) {
                // 找到要刪除的節點
                if (prev == null) {
                    // 刪除頭節點
                    buckets[index] = current.next;
                } else {
                    // 刪除非頭節點
                    prev.next = current.next;
                }
                size--;
                System.out.printf("🗑️ 刪除: key='%s', value='%s' (size=%d)%n", 
                                 key, current.getValue(), size);
                return current.getValue();
            }
            prev = current;
            current = current.next;
        }
        
        System.out.printf("⚠️ 找不到 key='%s'%n", key);
        return null;
    }
    
    /**
     * 擴容：容量變為 2 倍 + 1
     */
    private void resize() {
        int newCapacity = capacity * RESIZE_MULTIPLIER + RESIZE_ADDITION;
        Entry[] newBuckets = new Entry[newCapacity];
        
        System.out.printf("📊 擴容: %d → %d (負載因數: %.2f > %.2f)%n", 
                         capacity, newCapacity, (double) size / capacity, LOAD_FACTOR_THRESHOLD);
        
        // 重新雜湊所有元素
        for (int i = 0; i < capacity; i++) {
            Entry current = buckets[i];
            while (current != null) {
                Entry next = current.next;
                
                // 重新計算索引
                int newIndex = Math.abs(current.getKey().hashCode()) % newCapacity;
                
                // 頭插法放入新桶
                current.next = newBuckets[newIndex];
                newBuckets[newIndex] = current;
                
                current = next;
            }
        }
        
        buckets = newBuckets;
        capacity = newCapacity;
    }
    
    /**
     * 取得元素數量
     * @return 元素數量
     */
    public int size() {
        return size;
    }
    
    /**
     * 取得目前容量
     * @return 容量
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * 取得目前負載因數
     * @return 負載因數
     */
    public double getLoadFactor() {
        return (double) size / capacity;
    }
    
    /**
     * 檢查是否為空
     * @return true 如果為空
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * 清空哈希表
     */
    public void clear() {
        Arrays.fill(buckets, null);
        size = 0;
        System.out.println("🔄 已清空哈希表");
    }
    
    /**
     * 取得所有鍵
     * @return 鍵的集合
     */
    public Set<String> keySet() {
        Set<String> keys = new HashSet<>();
        for (Entry bucket : buckets) {
            Entry current = bucket;
            while (current != null) {
                keys.add(current.getKey());
                current = current.next;
            }
        }
        return keys;
    }
    
    /**
     * 取得所有值
     * @return 值的集合
     */
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        for (Entry bucket : buckets) {
            Entry current = bucket;
            while (current != null) {
                values.add(current.getValue());
                current = current.next;
            }
        }
        return values;
    }
    
    /**
     * 取得所有條目
     * @return 條目的列表
     */
    public List<Entry> getAllEntries() {
        List<Entry> entries = new ArrayList<>();
        for (Entry bucket : buckets) {
            Entry current = bucket;
            while (current != null) {
                entries.add(current);
                current = current.next;
            }
        }
        return entries;
    }
    
    /**
     * 印出桶報告
     */
    public void printBucketReport() {
        System.out.println("\n=== 桶報告 ===");
        System.out.printf("容量: %d%n", capacity);
        System.out.printf("元素數: %d%n", size);
        System.out.printf("負載因數: %.2f%n", getLoadFactor());
        System.out.printf("臨界值: %.2f%n", LOAD_FACTOR_THRESHOLD);
        
        // 統計資訊
        int maxChainLength = 0;
        int emptyBuckets = 0;
        int totalChainLength = 0;
        List<Integer> chainLengths = new ArrayList<>();
        
        for (int i = 0; i < capacity; i++) {
            int length = 0;
            Entry current = buckets[i];
            while (current != null) {
                length++;
                current = current.next;
            }
            
            chainLengths.add(length);
            totalChainLength += length;
            
            if (length > maxChainLength) {
                maxChainLength = length;
            }
            if (length == 0) {
                emptyBuckets++;
            }
        }
        
        System.out.println("\n統計資訊:");
        System.out.printf("  最長鏈結長度: %d%n", maxChainLength);
        System.out.printf("  空桶數量: %d%n", emptyBuckets);
        System.out.printf("  平均鏈結長度: %.2f%n", (double) totalChainLength / capacity);
        
        // 顯示每個桶的內容
        System.out.println("\n桶內容:");
        System.out.println("桶索引 | 鏈長度 | 內容");
        System.out.println("-------|--------|------------------------------");
        
        for (int i = 0; i < Math.min(capacity, 30); i++) {
            Entry current = buckets[i];
            int length = 0;
            StringBuilder content = new StringBuilder();
            
            while (current != null) {
                if (length > 0) {
                    content.append(" → ");
                }
                content.append(current.toString());
                length++;
                current = current.next;
            }
            
            if (length == 0) {
                content.append("空");
            }
            
            System.out.printf("%6d | %6d | %s%n", i, length, content.toString());
        }
        
        if (capacity > 30) {
            System.out.printf("      ... (其餘 %d 個桶省略)%n", capacity - 30);
        }
        System.out.println();
    }
    
    /**
     * 印出所有鍵值對
     */
    public void printAllEntries() {
        if (isEmpty()) {
            System.out.println("哈希表為空");
            return;
        }
        
        System.out.println("\n=== 所有鍵值對 ===");
        int count = 0;
        for (Entry bucket : buckets) {
            Entry current = bucket;
            while (current != null) {
                System.out.printf("  %s → %s%n", current.getKey(), current.getValue());
                count++;
                current = current.next;
            }
        }
        System.out.printf("總計: %d 個條目%n", count);
        System.out.println();
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 可擴充哈希表測試 ===\n");
        
        // 測試 1：基本功能
        testBasicOperations();
        
        // 測試 2：擴容測試
        testResize();
        
        // 測試 3：更新和刪除
        testUpdateAndDelete();
        
        // 測試 4：邊界情況
        testEdgeCases();
        
        // 測試 5：效能測試
        testPerformance();
    }
    
    /**
     * 測試基本操作
     */
    private static void testBasicOperations() {
        System.out.println("--- 測試 1: 基本操作 ---");
        
        ResizableStringMap map = new ResizableStringMap(4);
        
        System.out.println("初始容量: 4");
        System.out.println("插入資料:");
        map.put("A001", "Apple");
        map.put("B002", "Banana");
        map.put("C003", "Cherry");
        map.put("D004", "Date");
        map.put("E005", "Elderberry");
        
        System.out.println("\n查詢:");
        System.out.println("  get('A001') = " + map.get("A001"));
        System.out.println("  get('C003') = " + map.get("C003"));
        System.out.println("  get('F006') = " + map.get("F006"));
        
        System.out.println("\n存在性檢查:");
        System.out.println("  containsKey('B002') = " + map.containsKey("B002"));
        System.out.println("  containsKey('Z999') = " + map.containsKey("Z999"));
        
        System.out.println("\nsize = " + map.size());
        System.out.println("isEmpty = " + map.isEmpty());
        System.out.println("capacity = " + map.getCapacity());
        System.out.println("loadFactor = " + String.format("%.2f", map.getLoadFactor()));
        
        map.printAllEntries();
        map.printBucketReport();
    }
    
    /**
     * 測試擴容
     */
    private static void testResize() {
        System.out.println("--- 測試 2: 擴容測試 ---");
        
        ResizableStringMap map = new ResizableStringMap(4);
        
        System.out.println("初始容量: 4");
        System.out.println("負載因數臨界值: 0.75");
        System.out.println("\n逐步插入資料觀察擴容:");
        System.out.println("插入 | Key | 容量 | 負載因數 | 動作");
        System.out.println("-----|-----|------|----------|------");
        
        String[] keys = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
        for (int i = 0; i < keys.length; i++) {
            String key = "Key" + keys[i];
            String value = "Value" + keys[i];
            double oldLoad = map.getLoadFactor();
            int oldCapacity = map.getCapacity();
            
            map.put(key, value);
            
            int newCapacity = map.getCapacity();
            double newLoad = map.getLoadFactor();
            String action = (newCapacity > oldCapacity) ? "擴容!" : "正常";
            System.out.printf("  %2d  | %-5s | %4d | %6.2f | %s%n", 
                             i + 1, key, newCapacity, newLoad, action);
        }
        
        map.printBucketReport();
    }
    
    /**
     * 測試更新和刪除
     */
    private static void testUpdateAndDelete() {
        System.out.println("--- 測試 3: 更新和刪除 ---");
        
        ResizableStringMap map = new ResizableStringMap(8);
        
        System.out.println("插入資料:");
        map.put("U001", "One");
        map.put("U002", "Two");
        map.put("U003", "Three");
        map.put("U004", "Four");
        map.put("U005", "Five");
        
        map.printAllEntries();
        
        System.out.println("\n更新:");
        map.put("U003", "Three (更新)");
        map.put("U001", "One (更新)");
        
        System.out.println("\n刪除:");
        map.remove("U004");
        map.remove("U006");  // 不存在的鍵
        
        System.out.println("\n最終狀態:");
        map.printAllEntries();
        map.printBucketReport();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空表
        System.out.println("測試 4.1: 空表");
        ResizableStringMap map = new ResizableStringMap(5);
        System.out.println("  size = " + map.size());
        System.out.println("  isEmpty = " + map.isEmpty());
        map.printAllEntries();
        System.out.println();
        
        // 測試 4.2: null 處理
        System.out.println("測試 4.2: null 處理");
        try {
            map.put(null, "Null Key");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲 null key 例外: " + e.getMessage());
        }
        
        try {
            map.put("key", null);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲 null value 例外: " + e.getMessage());
        }
        System.out.println();
        
        // 測試 4.3: 大量相同 key 更新
        System.out.println("測試 4.3: 大量相同 key 更新");
        map = new ResizableStringMap(4);
        for (int i = 0; i < 20; i++) {
            map.put("sameKey", "Version " + i);
        }
        System.out.println("  size = " + map.size());
        System.out.println("  get('sameKey') = " + map.get("sameKey"));
        map.printBucketReport();
    }
    
    /**
     * 測試效能
     */
    private static void testPerformance() {
        System.out.println("--- 測試 5: 效能測試 ---");
        
        int[] testSizes = {10, 50, 100, 200, 500};
        
        System.out.println("資料量 | 初始容量 | 最終容量 | 最終負載 | 擴容次數");
        System.out.println("-------|----------|----------|----------|----------");
        
        for (int size : testSizes) {
            ResizableStringMap map = new ResizableStringMap(8);
            int initialCapacity = map.getCapacity();
            int resizeCount = 0;
            
            for (int i = 0; i < size; i++) {
                int oldCapacity = map.getCapacity();
                map.put("Key" + i, "Value" + i);
                if (map.getCapacity() > oldCapacity) {
                    resizeCount++;
                }
            }
            
            System.out.printf("%6d | %8d | %8d | %8.2f | %6d%n",
                             size,
                             initialCapacity,
                             map.getCapacity(),
                             map.getLoadFactor(),
                             resizeCount);
        }
        
        System.out.println();
    }
}