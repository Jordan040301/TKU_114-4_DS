import java.util.*;

/**
 * 整數雜湊表
 * 使用單獨的鏈結法處理碰撞
 */
class HashNode {
    int key;
    String value;
    HashNode next;
    
    public HashNode(int key, String value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %s)", key, value);
    }
}

/**
 * 整數字串雜湊表實作
 */
public class IntegerStringHashTable {
    private HashNode[] buckets;  // 桶陣列
    private int size;            // 元素數量
    private int capacity;        // 桶容量
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    
    /**
     * 建構子：使用預設容量
     */
    public IntegerStringHashTable() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * 建構子：指定初始容量
     * @param initialCapacity 初始容量
     */
    public IntegerStringHashTable(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("容量必須大於 0");
        }
        this.capacity = initialCapacity;
        this.buckets = new HashNode[capacity];
        this.size = 0;
    }
    
    /**
     * 雜湊函數
     * @param key 整數鍵
     * @return 桶索引
     */
    private int hash(int key) {
        // 使用 Java 的 hash 函數並取絕對值
        return Math.abs(Integer.hashCode(key)) % capacity;
    }
    
    /**
     * 插入或更新鍵值對
     * @param key 鍵
     * @param value 值
     */
    public void put(int key, String value) {
        if (value == null) {
            throw new IllegalArgumentException("值不能為 null");
        }
        
        int index = hash(key);
        HashNode current = buckets[index];
        
        // 檢查是否已存在相同的 key
        while (current != null) {
            if (current.key == key) {
                // 找到相同 key，更新值
                current.value = value;
                System.out.printf("🔄 更新: key=%d, value='%s' (更新現有項目)%n", key, value);
                return;
            }
            current = current.next;
        }
        
        // 不存在相同 key，新增節點（使用頭插法）
        HashNode newNode = new HashNode(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;
        
        System.out.printf("✅ 插入: key=%d, value='%s' (size=%d)%n", key, value, size);
        
        // 檢查是否需要擴容
        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }
    }
    
    /**
     * 取得指定鍵的值
     * @param key 鍵
     * @return 值，若不存在則回傳 null
     */
    public String get(int key) {
        int index = hash(key);
        HashNode current = buckets[index];
        
        while (current != null) {
            if (current.key == key) {
                return current.value;
            }
            current = current.next;
        }
        
        return null;  // 找不到
    }
    
    /**
     * 檢查是否包含指定的鍵
     * @param key 鍵
     * @return true 如果存在
     */
    public boolean containsKey(int key) {
        int index = hash(key);
        HashNode current = buckets[index];
        
        while (current != null) {
            if (current.key == key) {
                return true;
            }
            current = current.next;
        }
        
        return false;
    }
    
    /**
     * 刪除指定鍵的項目
     * @param key 鍵
     * @return 被刪除的值，若不存在則回傳 null
     */
    public String remove(int key) {
        int index = hash(key);
        HashNode current = buckets[index];
        HashNode previous = null;
        
        while (current != null) {
            if (current.key == key) {
                // 找到要刪除的節點
                if (previous == null) {
                    // 刪除頭節點
                    buckets[index] = current.next;
                } else {
                    // 刪除非頭節點
                    previous.next = current.next;
                }
                size--;
                System.out.printf("🗑️  刪除: key=%d, value='%s' (size=%d)%n", 
                                 key, current.value, size);
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        
        System.out.printf("⚠️  找不到 key=%d，無法刪除%n", key);
        return null;  // 找不到
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
     * 擴容：容量變為兩倍
     */
    private void resize() {
        int newCapacity = capacity * 2;
        HashNode[] newBuckets = new HashNode[newCapacity];
        
        System.out.printf("  📊 擴容: %d → %d (負載因數: %.2f)%n", 
                         capacity, newCapacity, (double) size / capacity);
        
        // 重新雜湊所有元素
        for (int i = 0; i < capacity; i++) {
            HashNode current = buckets[i];
            while (current != null) {
                HashNode next = current.next;
                
                // 重新計算索引
                int newIndex = Math.abs(Integer.hashCode(current.key)) % newCapacity;
                
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
     * 生成桶報告
     * @return 格式化的桶報告字串
     */
    public String bucketReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("\n=== 桶報告 (Bucket Report) ===\n");
        report.append("容量: ").append(capacity).append("\n");
        report.append("元素數: ").append(size).append("\n");
        report.append("負載因數: ").append(String.format("%.2f", (double) size / capacity)).append("\n");
        report.append("\n");
        
        // 統計資訊
        int maxChainLength = 0;
        int emptyBuckets = 0;
        int totalChainLength = 0;
        List<Integer> chainLengths = new ArrayList<>();
        
        for (int i = 0; i < capacity; i++) {
            int length = 0;
            HashNode current = buckets[i];
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
        
        report.append("統計資訊:\n");
        report.append("  最長鏈結長度: ").append(maxChainLength).append("\n");
        report.append("  空桶數量: ").append(emptyBuckets).append("\n");
        report.append("  平均鏈結長度: ").append(String.format("%.2f", (double) totalChainLength / capacity)).append("\n");
        report.append("\n");
        
        // 顯示每個桶的內容
        report.append("桶內容:\n");
        report.append("桶索引 | 鏈結長度 | 內容\n");
        report.append("-------|----------|------------------------------\n");
        
        for (int i = 0; i < capacity; i++) {
            HashNode current = buckets[i];
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
            
            report.append(String.format("%6d | %8d | %s%n", i, length, content.toString()));
        }
        
        return report.toString();
    }
    
    /**
     * 顯示所有鍵值對
     */
    public void printAllEntries() {
        if (isEmpty()) {
            System.out.println("雜湊表為空");
            return;
        }
        
        System.out.println("\n=== 所有鍵值對 ===");
        for (int i = 0; i < capacity; i++) {
            HashNode current = buckets[i];
            while (current != null) {
                System.out.printf("  key=%d, value='%s'%n", current.key, current.value);
                current = current.next;
            }
        }
        System.out.println();
    }
    
    /**
     * 清空雜湊表
     */
    public void clear() {
        Arrays.fill(buckets, null);
        size = 0;
        System.out.println("🔄 已清空雜湊表");
    }
    
    /**
     * 取得所有鍵
     */
    public Set<Integer> keySet() {
        Set<Integer> keys = new HashSet<>();
        for (int i = 0; i < capacity; i++) {
            HashNode current = buckets[i];
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }
        return keys;
    }
    
    /**
     * 取得所有值
     */
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            HashNode current = buckets[i];
            while (current != null) {
                values.add(current.value);
                current = current.next;
            }
        }
        return values;
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 整數雜湊表測試 ===\n");
        
        // 測試 1：基本功能測試
        testBasicFunctionality();
        
        // 測試 2：更新相同 Key
        testUpdateSameKey();
        
        // 測試 3：刪除功能測試
        testRemoveFunctionality();
        
        // 測試 4：碰撞處理測試
        testCollisionHandling();
        
        // 測試 5：擴容測試
        testResizing();
        
        // 測試 6：邊界情況測試
        testEdgeCases();
        
        // 測試 7：完整功能測試
        testComprehensiveFunctionality();
    }
    
    /**
     * 測試基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- 測試 1: 基本功能測試 ---");
        
        IntegerStringHashTable table = new IntegerStringHashTable(8);
        
        System.out.println("插入資料:");
        table.put(1, "Apple");
        table.put(2, "Banana");
        table.put(3, "Cherry");
        table.put(4, "Date");
        table.put(5, "Elderberry");
        
        System.out.println("\n查詢測試:");
        System.out.println("get(1) = " + table.get(1));
        System.out.println("get(3) = " + table.get(3));
        System.out.println("get(6) = " + table.get(6));
        
        System.out.println("\ncontainsKey 測試:");
        System.out.println("containsKey(2) = " + table.containsKey(2));
        System.out.println("containsKey(7) = " + table.containsKey(7));
        
        System.out.println("size() = " + table.size());
        
        table.printAllEntries();
        System.out.println(table.bucketReport());
    }
    
    /**
     * 測試更新相同 Key
     */
    private static void testUpdateSameKey() {
        System.out.println("--- 測試 2: 更新相同 Key (size 不增加) ---");
        
        IntegerStringHashTable table = new IntegerStringHashTable(8);
        
        System.out.println("第一次插入:");
        table.put(1, "First Value");
        System.out.println("size = " + table.size());
        
        System.out.println("\n更新相同 key:");
        table.put(1, "Second Value");
        System.out.println("size = " + table.size());
        
        System.out.println("\n再次更新:");
        table.put(1, "Third Value");
        System.out.println("size = " + table.size());
        
        System.out.println("\nget(1) = " + table.get(1));
        System.out.println("containsKey(1) = " + table.containsKey(1));
        
        table.printAllEntries();
        System.out.println(table.bucketReport());
    }
    
    /**
     * 測試刪除功能
     */
    private static void testRemoveFunctionality() {
        System.out.println("--- 測試 3: 刪除功能測試 ---");
        
        IntegerStringHashTable table = new IntegerStringHashTable(8);
        
        table.put(1, "Apple");
        table.put(2, "Banana");
        table.put(3, "Cherry");
        table.put(4, "Date");
        
        System.out.println("\n刪除前 size = " + table.size());
        table.printAllEntries();
        
        System.out.println("\n刪除 key=2:");
        table.remove(2);
        System.out.println("size = " + table.size());
        
        System.out.println("\n刪除 key=5 (不存在):");
        table.remove(5);
        
        System.out.println("\n刪除 key=1 (頭節點):");
        table.remove(1);
        System.out.println("size = " + table.size());
        
        System.out.println("\n刪除後狀態:");
        table.printAllEntries();
        
        System.out.println("containsKey(2) = " + table.containsKey(2));
        System.out.println("containsKey(3) = " + table.containsKey(3));
        
        System.out.println(table.bucketReport());
    }
    
    /**
     * 測試碰撞處理
     */
    private static void testCollisionHandling() {
        System.out.println("--- 測試 4: 碰撞處理測試 ---");
        
        // 使用較小的容量來製造碰撞
        IntegerStringHashTable table = new IntegerStringHashTable(4);
        
        System.out.println("插入可能產生碰撞的 keys:");
        table.put(1, "One");
        table.put(5, "Five");   // 可能與 1 碰撞
        table.put(9, "Nine");   // 可能與 1 碰撞
        table.put(2, "Two");
        table.put(6, "Six");    // 可能與 2 碰撞
        
        System.out.println("\nsize = " + table.size());
        table.printAllEntries();
        System.out.println(table.bucketReport());
        
        // 測試碰撞時的查詢
        System.out.println("\n碰撞情況下查詢:");
        System.out.println("get(1) = " + table.get(1));
        System.out.println("get(5) = " + table.get(5));
        System.out.println("get(9) = " + table.get(9));
        System.out.println("get(2) = " + table.get(2));
        System.out.println("get(6) = " + table.get(6));
    }
    
    /**
     * 測試擴容
     */
    private static void testResizing() {
        System.out.println("--- 測試 5: 擴容測試 ---");
        
        IntegerStringHashTable table = new IntegerStringHashTable(4);
        
        System.out.println("初始容量: 4");
        System.out.println("插入資料觸發擴容:");
        
        for (int i = 1; i <= 10; i++) {
            table.put(i, "Value" + i);
        }
        
        System.out.println("\n最終狀態:");
        System.out.println("size = " + table.size());
        System.out.println("capacity = " + table.capacity);
        table.printAllEntries();
        System.out.println(table.bucketReport());
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 6: 邊界情況測試 ---");
        
        // 測試 6.1: 空表操作
        System.out.println("測試 6.1: 空表操作");
        IntegerStringHashTable table = new IntegerStringHashTable();
        System.out.println("isEmpty() = " + table.isEmpty());
        System.out.println("size() = " + table.size());
        System.out.println("get(1) = " + table.get(1));
        System.out.println("containsKey(1) = " + table.containsKey(1));
        System.out.println("remove(1) = " + table.remove(1));
        System.out.println();
        
        // 測試 6.2: 負數鍵
        System.out.println("測試 6.2: 負數鍵");
        table.put(-1, "Negative One");
        table.put(-2, "Negative Two");
        table.put(-3, "Negative Three");
        System.out.println("get(-1) = " + table.get(-1));
        System.out.println("get(-2) = " + table.get(-2));
        System.out.println("get(-3) = " + table.get(-3));
        System.out.println("size = " + table.size());
        table.printAllEntries();
        System.out.println();
        
        // 測試 6.3: 大量相同 key 更新
        System.out.println("測試 6.3: 大量相同 key 更新");
        IntegerStringHashTable table2 = new IntegerStringHashTable(4);
        for (int i = 0; i < 10; i++) {
            table2.put(1, "Version " + i);
        }
        System.out.println("size = " + table2.size());
        System.out.println("get(1) = " + table2.get(1));
        System.out.println("containsKey(1) = " + table2.containsKey(1));
        System.out.println("containsKey(2) = " + table2.containsKey(2));
        System.out.println();
        
        // 測試 6.4: 特殊字串值
        System.out.println("測試 6.4: 特殊字串值");
        table2.put(2, "");
        table2.put(3, " ");
        table2.put(4, "Hello World!");
        table2.put(5, "中文測試");
        System.out.println("get(2) = '" + table2.get(2) + "'");
        System.out.println("get(3) = '" + table2.get(3) + "'");
        System.out.println("get(4) = '" + table2.get(4) + "'");
        System.out.println("get(5) = '" + table2.get(5) + "'");
        System.out.println("size = " + table2.size());
        table2.printAllEntries();
    }
    
    /**
     * 測試完整功能
     */
    private static void testComprehensiveFunctionality() {
        System.out.println("--- 測試 7: 完整功能測試 ---");
        
        IntegerStringHashTable table = new IntegerStringHashTable(8);
        
        System.out.println("執行各種操作:");
        
        // 插入
        table.put(10, "Ten");
        table.put(20, "Twenty");
        table.put(30, "Thirty");
        table.put(40, "Forty");
        table.put(50, "Fifty");
        table.put(60, "Sixty");
        
        System.out.println("\n目前狀態:");
        System.out.println("size = " + table.size());
        System.out.println("isEmpty = " + table.isEmpty());
        System.out.println("keySet = " + table.keySet());
        System.out.println("values = " + table.values());
        
        // 查詢
        System.out.println("\n查詢:");
        System.out.println("get(20) = " + table.get(20));
        System.out.println("get(30) = " + table.get(30));
        System.out.println("containsKey(40) = " + table.containsKey(40));
        System.out.println("containsKey(100) = " + table.containsKey(100));
        
        // 更新
        System.out.println("\n更新:");
        table.put(20, "Twenty Updated");
        table.put(50, "Fifty Updated");
        System.out.println("get(20) = " + table.get(20));
        System.out.println("get(50) = " + table.get(50));
        
        // 刪除
        System.out.println("\n刪除:");
        table.remove(30);
        table.remove(60);
        
        // 最終狀態
        System.out.println("\n最終狀態:");
        table.printAllEntries();
        System.out.println("size = " + table.size());
        System.out.println("keySet = " + table.keySet());
        
        // 桶報告
        System.out.println(table.bucketReport());
        
        // 清空
        System.out.println("清空雜湊表:");
        table.clear();
        System.out.println("size = " + table.size());
        System.out.println("isEmpty = " + table.isEmpty());
        table.printAllEntries();
    }
}