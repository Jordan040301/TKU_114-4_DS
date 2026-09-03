import java.util.*;

public class Q04_ChainedHashTable {
    private List<List<Entry>> buckets;
    private int size;
    private int bucketCount;

    /**
     * 内部类：存储键值对
     */
    private static class Entry {
        int key;
        String value;

        Entry(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 构造函数：初始化哈希表
     * @param bucketCount 桶的数量，必须大于 0
     * @throws IllegalArgumentException 当 bucketCount <= 0 时抛出
     */
    public Q04_ChainedHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("Bucket count must be positive");
        }
        this.bucketCount = bucketCount;
        this.size = 0;
        this.buckets = new ArrayList<>(bucketCount);
        
        // 初始化每个桶为一个空的 ArrayList
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    /**
     * 计算键的桶索引（支持负数键）
     */
    private int getIndex(int key) {
        // 使用 Math.abs 处理负数，然后取模
        return Math.abs(key) % bucketCount;
    }

    /**
     * 插入或更新键值对
     * @param key 键
     * @param value 值
     */
    public void put(int key, String value) {
        int index = getIndex(key);
        List<Entry> bucket = buckets.get(index);
        
        // 查找是否已存在该键
        for (Entry entry : bucket) {
            if (entry.key == key) {
                // 更新现有键的值，size 不增加
                entry.value = value;
                return;
            }
        }
        
        // 键不存在，添加新 Entry，size 增加
        bucket.add(new Entry(key, value));
        size++;
    }

    /**
     * 根据键获取值
     * @param key 键
     * @return 对应的值，如果不存在返回 null
     */
    public String get(int key) {
        int index = getIndex(key);
        List<Entry> bucket = buckets.get(index);
        
        for (Entry entry : bucket) {
            if (entry.key == key) {
                return entry.value;
            }
        }
        return null;
    }

    /**
     * 根据键删除键值对
     * @param key 键
     * @return 删除成功返回 true，键不存在返回 false
     */
    public boolean remove(int key) {
        int index = getIndex(key);
        List<Entry> bucket = buckets.get(index);
        
        // 使用迭代器安全删除
        Iterator<Entry> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (entry.key == key) {
                iterator.remove();
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * 返回哈希表中键值对的数量
     */
    public int size() {
        return size;
    }

    /**
     * 返回最长链的长度（即包含 Entry 最多的桶）
     */
    public int longestChain() {
        int maxChain = 0;
        for (List<Entry> bucket : buckets) {
            int chainLength = bucket.size();
            if (chainLength > maxChain) {
                maxChain = chainLength;
            }
        }
        return maxChain;
    }

    /**
     * 获取哈希表的快照（用于调试/测试）
     * 返回每个桶的内容
     */
    public List<List<Entry>> snapshot() {
        List<List<Entry>> snapshot = new ArrayList<>();
        for (List<Entry> bucket : buckets) {
            snapshot.add(new ArrayList<>(bucket));
        }
        return snapshot;
    }

    // 测试代码
    public static void main(String[] args) {
        // 测试构造函数异常
        try {
            new Q04_ChainedHashTable(0);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        Q04_ChainedHashTable table = new Q04_ChainedHashTable(5);
        
        // 测试 put 和 get
        System.out.println("=== Test put and get ===");
        table.put(1, "one");
        table.put(2, "two");
        table.put(3, "three");
        table.put(6, "six");  // 与 1 碰撞（假设 5 个桶）
        table.put(-1, "negative one"); // 测试负数键
        
        System.out.println("Get 1: " + table.get(1));     // one
        System.out.println("Get 2: " + table.get(2));     // two
        System.out.println("Get 6: " + table.get(6));     // six
        System.out.println("Get -1: " + table.get(-1));   // negative one
        System.out.println("Get 5 (not exist): " + table.get(5)); // null
        System.out.println("Size: " + table.size());      // 5
        
        // 测试更新
        System.out.println("\n=== Test update ===");
        table.put(1, "ONE");
        System.out.println("Get 1 after update: " + table.get(1)); // ONE
        System.out.println("Size after update: " + table.size());  // 5 (不变)
        
        // 测试 remove
        System.out.println("\n=== Test remove ===");
        System.out.println("Remove 2: " + table.remove(2));  // true
        System.out.println("Get 2 after remove: " + table.get(2)); // null
        System.out.println("Remove 5 (not exist): " + table.remove(5)); // false
        System.out.println("Size after remove: " + table.size()); // 4
        
        // 测试 longestChain
        System.out.println("\n=== Test longestChain ===");
        System.out.println("Longest chain: " + table.longestChain());
        
        // 显示哈希表结构
        System.out.println("\n=== Hash table structure ===");
        List<List<Entry>> snapshot = table.snapshot();
        for (int i = 0; i < snapshot.size(); i++) {
            System.out.print("Bucket " + i + ": ");
            List<Entry> bucket = snapshot.get(i);
            for (Entry entry : bucket) {
                System.out.print("[" + entry.key + "->" + entry.value + "] ");
            }
            System.out.println();
        }
    }
}