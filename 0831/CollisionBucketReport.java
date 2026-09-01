import java.util.*;

/**
 * 碰撞桶报告系统
 * 将整数键分配到固定数量的桶中，生成碰撞报告
 */
public class CollisionBucketReport {
    private int numBuckets;
    private Map<Integer, List<Integer>> bucketMap;
    
    /**
     * 构造函数
     * @param numBuckets 桶的数量（必须大于0）
     */
    public CollisionBucketReport(int numBuckets) {
        if (numBuckets <= 0) {
            throw new IllegalArgumentException("Number of buckets must be positive");
        }
        this.numBuckets = numBuckets;
        this.bucketMap = new HashMap<>();
        
        // 初始化所有桶
        for (int i = 0; i < numBuckets; i++) {
            bucketMap.put(i, new ArrayList<>());
        }
    }
    
    /**
     * 添加单个键到桶中
     * @param key 要添加的键
     */
    public void addKey(int key) {
        int bucketIndex = getBucketIndex(key);
        bucketMap.get(bucketIndex).add(key);
    }
    
    /**
     * 批量添加键
     * @param keys 键数组
     */
    public void addKeys(int... keys) {
        for (int key : keys) {
            addKey(key);
        }
    }
    
    /**
     * 批量添加键（集合）
     * @param keys 键集合
     */
    public void addKeys(Collection<Integer> keys) {
        for (int key : keys) {
            addKey(key);
        }
    }
    
    /**
     * 计算桶索引
     * 正确处理负数
     * @param key 整数键
     * @return 桶索引 (0 到 numBuckets-1)
     */
    private int getBucketIndex(int key) {
        // 使用 Math.floorMod 正确处理负数
        return Math.floorMod(key, numBuckets);
    }
    
    /**
     * 获取指定桶的内容
     * @param bucketIndex 桶索引
     * @return 桶中所有键的列表
     */
    public List<Integer> getBucket(int bucketIndex) {
        validateBucketIndex(bucketIndex);
        return new ArrayList<>(bucketMap.get(bucketIndex));
    }
    
    /**
     * 获取指定桶的碰撞数量
     * 碰撞数量 = 桶中元素数量 - 1（如果有元素的话）
     * @param bucketIndex 桶索引
     * @return 碰撞数量
     */
    public int getCollisionCount(int bucketIndex) {
        validateBucketIndex(bucketIndex);
        int size = bucketMap.get(bucketIndex).size();
        return size > 0 ? size - 1 : 0;
    }
    
    /**
     * 获取指定桶的最后一个键（终止键）
     * @param bucketIndex 桶索引
     * @return 最后一个键，如果桶为空则返回 null
     */
    public Integer getLastKey(int bucketIndex) {
        validateBucketIndex(bucketIndex);
        List<Integer> bucket = bucketMap.get(bucketIndex);
        return bucket.isEmpty() ? null : bucket.get(bucket.size() - 1);
    }
    
    /**
     * 获取所有桶的碰撞总数
     * @return 碰撞总数
     */
    public int getTotalCollisions() {
        int total = 0;
        for (int i = 0; i < numBuckets; i++) {
            total += getCollisionCount(i);
        }
        return total;
    }
    
    /**
     * 获取所有桶的总键数
     * @return 总键数
     */
    public int getTotalKeys() {
        int total = 0;
        for (int i = 0; i < numBuckets; i++) {
            total += bucketMap.get(i).size();
        }
        return total;
    }
    
    /**
     * 验证桶索引是否有效
     */
    private void validateBucketIndex(int bucketIndex) {
        if (bucketIndex < 0 || bucketIndex >= numBuckets) {
            throw new IllegalArgumentException("Invalid bucket index: " + bucketIndex);
        }
    }
    
    /**
     * 生成完整的碰撞桶报告
     * @return 格式化的报告字符串
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("=== Collision Bucket Report ===\n");
        report.append("Number of buckets: ").append(numBuckets).append("\n");
        report.append("Total keys: ").append(getTotalKeys()).append("\n");
        report.append("Total collisions: ").append(getTotalCollisions()).append("\n");
        report.append("\n");
        
        // 统计非空桶数量
        int nonEmptyBuckets = 0;
        for (int i = 0; i < numBuckets; i++) {
            if (!bucketMap.get(i).isEmpty()) {
                nonEmptyBuckets++;
            }
        }
        report.append("Non-empty buckets: ").append(nonEmptyBuckets).append("/").append(numBuckets).append("\n");
        report.append("\n");
        
        // 生成每个桶的详细报告
        report.append("Bucket Details:\n");
        report.append("Bucket | Keys                          | Collisions | Last Key\n");
        report.append("-------|-------------------------------|------------|----------\n");
        
        for (int i = 0; i < numBuckets; i++) {
            List<Integer> bucket = bucketMap.get(i);
            String keysStr = bucket.isEmpty() ? "[]" : bucket.toString();
            
            // 格式化输出
            report.append(String.format("%6d | %-29s | %10d | %8s\n",
                    i,
                    keysStr.length() > 29 ? keysStr.substring(0, 26) + "..." : keysStr,
                    getCollisionCount(i),
                    getLastKey(i) != null ? getLastKey(i).toString() : "null"));
        }
        
        return report.toString();
    }
    
    /**
     * 打印碰撞桶报告
     */
    public void printReport() {
        System.out.println(generateReport());
    }
    
    /**
     * 重置所有桶
     */
    public void clear() {
        for (int i = 0; i < numBuckets; i++) {
            bucketMap.put(i, new ArrayList<>());
        }
    }
    
    /**
     * 获取桶的分布统计
     */
    public Map<Integer, Integer> getDistribution() {
        Map<Integer, Integer> distribution = new HashMap<>();
        for (int i = 0; i < numBuckets; i++) {
            distribution.put(i, bucketMap.get(i).size());
        }
        return distribution;
    }
    
    /**
     * 主方法：测试和验证
     */
    public static void main(String[] args) {
        System.out.println("=== Collision Bucket Report System Test ===\n");
        
        // 测试1：基本功能测试
        testBasicFunctionality();
        
        // 测试2：负数键测试
        testNegativeKeys();
        
        // 测试3：重复键测试
        testDuplicateKeys();
        
        // 测试4：空输入测试
        testEmptyInput();
        
        // 测试5：综合测试
        testComprehensiveScenario();
    }
    
    /**
     * 测试基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- Test 1: Basic Functionality ---");
        CollisionBucketReport report = new CollisionBucketReport(5);
        
        System.out.println("Adding keys: 10, 15, 20, 25, 30, 35, 40, 45, 50");
        report.addKeys(10, 15, 20, 25, 30, 35, 40, 45, 50);
        
        report.printReport();
        System.out.println();
    }
    
    /**
     * 测试负数键
     */
    private static void testNegativeKeys() {
        System.out.println("--- Test 2: Negative Keys ---");
        CollisionBucketReport report = new CollisionBucketReport(7);
        
        System.out.println("Adding keys with negatives: -10, 5, -15, 20, -25, 30, -35");
        report.addKeys(-10, 5, -15, 20, -25, 30, -35);
        
        report.printReport();
        System.out.println();
    }
    
    /**
     * 测试重复键
     */
    private static void testDuplicateKeys() {
        System.out.println("--- Test 3: Duplicate Keys ---");
        CollisionBucketReport report = new CollisionBucketReport(4);
        
        System.out.println("Adding keys with duplicates: 10, 10, 20, 20, 20, 30, 40, 40");
        report.addKeys(10, 10, 20, 20, 20, 30, 40, 40);
        
        report.printReport();
        System.out.println();
    }
    
    /**
     * 测试空输入
     */
    private static void testEmptyInput() {
        System.out.println("--- Test 4: Empty Input ---");
        CollisionBucketReport report = new CollisionBucketReport(3);
        
        System.out.println("No keys added (empty input)");
        report.printReport();
        System.out.println();
    }
    
    /**
     * 综合测试
     */
    private static void testComprehensiveScenario() {
        System.out.println("--- Test 5: Comprehensive Scenario ---");
        CollisionBucketReport report = new CollisionBucketReport(6);
        
        // 包含正数、负数、重复键的综合测试
        int[] testData = {
            10, -5, 20, -15, 30, 10, -25, 40, 50, -35, 60, 20, -10, 70, 80, 90, 100, -45
        };
        
        System.out.println("Comprehensive test data:");
        System.out.println(Arrays.toString(testData));
        System.out.println();
        
        report.addKeys(testData);
        
        // 显示详细报告
        report.printReport();
        
        // 显示额外统计信息
        System.out.println("Additional Statistics:");
        System.out.println("Distribution: " + report.getDistribution());
        System.out.println("Average keys per bucket: " + 
                          String.format("%.2f", (double) report.getTotalKeys() / 6));
        System.out.println("Load factor: " + 
                          String.format("%.2f%%", (double) report.getTotalKeys() / 6 * 100));
        System.out.println();
        
        // 测试获取特定桶
        System.out.println("Testing getBucket() and getCollisionCount():");
        for (int i = 0; i < 6; i++) {
            System.out.println("Bucket " + i + ": " + report.getBucket(i) + 
                             " | Collisions: " + report.getCollisionCount(i) +
                             " | Last key: " + report.getLastKey(i));
        }
        System.out.println();
        
        // 测试边界情况
        testEdgeCases();
    }
    
    /**
     * 测试边界情况
     */
    private static void testEdgeCases() {
        System.out.println("--- Test 6: Edge Cases ---");
        
        // 测试单桶
        System.out.println("Test 6.1: Single bucket (numBuckets=1)");
        CollisionBucketReport singleBucket = new CollisionBucketReport(1);
        singleBucket.addKeys(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        singleBucket.printReport();
        System.out.println();
        
        // 测试大量桶
        System.out.println("Test 6.2: Many buckets (numBuckets=10)");
        CollisionBucketReport manyBuckets = new CollisionBucketReport(10);
        for (int i = 0; i < 100; i++) {
            manyBuckets.addKey(i);
        }
        manyBuckets.printReport();
        System.out.println();
        
        // 测试负数取模的正确性
        System.out.println("Test 6.3: Negative mod verification");
        System.out.println("With numBuckets=5:");
        System.out.println("  -1 mod 5 = " + Math.floorMod(-1, 5));
        System.out.println("  -6 mod 5 = " + Math.floorMod(-6, 5));
        System.out.println("  -11 mod 5 = " + Math.floorMod(-11, 5));
        System.out.println("  -16 mod 5 = " + Math.floorMod(-16, 5));
        
        CollisionBucketReport negTest = new CollisionBucketReport(5);
        negTest.addKeys(-1, -6, -11, -16);
        System.out.println("Bucket assignments: " + negTest.getDistribution());
        System.out.println();
    }
}