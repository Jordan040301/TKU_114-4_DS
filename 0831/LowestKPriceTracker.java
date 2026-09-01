import java.util.*;

/**
 * 最低K价格追踪器
 * 使用固定大小最大堆保留最低K个有效价格
 */
public class LowestKPriceTracker {
    private PriorityQueue<Double> maxHeap;  // 最大堆，保留最低K个价格
    private int k;
    
    /**
     * 构造函数
     * @param k 需要追踪的最低价格数量
     */
    public LowestKPriceTracker(int k) {
        this.k = k;
        // 使用最大堆（通过反转比较器）
        if (k > 0) {
            this.maxHeap = new PriorityQueue<>((a, b) -> Double.compare(b, a));
        } else {
            this.maxHeap = new PriorityQueue<>();
        }
    }
    
    /**
     * 添加价格到追踪器
     * @param price 要添加的价格
     */
    public void addPrice(Double price) {
        // 忽略 null 和负数
        if (price == null || price < 0) {
            return;
        }
        
        // 如果K <= 0，不存储任何价格
        if (k <= 0) {
            return;
        }
        
        // 如果堆未满，直接添加
        if (maxHeap.size() < k) {
            maxHeap.offer(price);
        } 
        // 如果堆已满，检查是否应该替换
        else if (price < maxHeap.peek()) {
            // 移除当前最大值（堆顶），添加新价格
            maxHeap.poll();
            maxHeap.offer(price);
        }
        // 如果价格 >= 堆顶（最大值），则忽略
    }
    
    /**
     * 批量添加价格
     * @param prices 价格数组
     */
    public void addPrices(Double... prices) {
        for (Double price : prices) {
            addPrice(price);
        }
    }
    
    /**
     * 批量添加价格（集合）
     * @param prices 价格集合
     */
    public void addPrices(Collection<Double> prices) {
        for (Double price : prices) {
            addPrice(price);
        }
    }
    
    /**
     * 获取最低K个价格的列表（按递增顺序排列）
     * @return 按递增顺序排列的最低K个价格列表
     */
    public List<Double> getLowestK() {
        if (k <= 0 || maxHeap.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 从最大堆中取出所有元素
        List<Double> result = new ArrayList<>(maxHeap);
        
        // 按递增顺序排序
        Collections.sort(result);
        
        return result;
    }
    
    /**
     * 获取当前追踪的价格数量
     */
    public int size() {
        return maxHeap.size();
    }
    
    /**
     * 检查是否追踪任何价格
     */
    public boolean isEmpty() {
        return maxHeap.isEmpty();
    }
    
    /**
     * 获取当前最大堆的容量（K值）
     */
    public int getK() {
        return k;
    }
    
    /**
     * 清空所有追踪的价格
     */
    public void clear() {
        maxHeap.clear();
    }
    
    /**
     * 重置K值（清空现有数据）
     * @param newK 新的K值
     */
    public void setK(int newK) {
        this.k = newK;
        maxHeap.clear();
        if (k > 0) {
            maxHeap = new PriorityQueue<>((a, b) -> Double.compare(b, a));
        } else {
            maxHeap = new PriorityQueue<>();
        }
    }
    
    /**
     * 打印当前追踪的价格
     */
    public void printLowestK() {
        List<Double> lowestPrices = getLowestK();
        if (lowestPrices.isEmpty()) {
            System.out.println("No valid prices tracked (K=" + k + ")");
            return;
        }
        
        System.out.println("Lowest " + k + " prices (K=" + k + "):");
        System.out.println("  " + lowestPrices);
        System.out.println("  Count: " + lowestPrices.size() + "/" + k);
        System.out.println("  Max price in heap: " + (maxHeap.isEmpty() ? "N/A" : maxHeap.peek()));
    }
    
    /**
     * 主方法：测试和验证
     */
    public static void main(String[] args) {
        System.out.println("=== Lowest K Price Tracker Test ===\n");
        
        // 测试1：基本功能测试
        testBasicFunctionality();
        
        // 测试2：无效值过滤测试
        testInvalidValueFiltering();
        
        // 测试3：K值边界测试
        testKBoundaryCases();
        
        // 测试4：性能测试
        testPerformance();
    }
    
    /**
     * 测试基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- Test 1: Basic Functionality ---");
        LowestKPriceTracker tracker = new LowestKPriceTracker(5);
        
        System.out.println("Adding prices: 10.5, 3.2, 7.8, 1.9, 5.6, 8.4, 2.7, 4.3, 9.1, 6.0");
        tracker.addPrices(10.5, 3.2, 7.8, 1.9, 5.6, 8.4, 2.7, 4.3, 9.1, 6.0);
        
        tracker.printLowestK();
        
        // 验证结果
        List<Double> result = tracker.getLowestK();
        System.out.println("Expected: [1.9, 2.7, 3.2, 4.3, 5.6]");
        System.out.println("Result:   " + result);
        System.out.println("Test: " + (result.equals(Arrays.asList(1.9, 2.7, 3.2, 4.3, 5.6)) ? "✓ PASS" : "✗ FAIL"));
        System.out.println();
    }
    
    /**
     * 测试无效值过滤
     */
    private static void testInvalidValueFiltering() {
        System.out.println("--- Test 2: Invalid Value Filtering ---");
        LowestKPriceTracker tracker = new LowestKPriceTracker(4);
        
        System.out.println("Adding prices with invalid values:");
        System.out.println("  null, -5.0, 12.5, null, -2.3, 3.7, 8.1, -1.0, 6.4, null");
        tracker.addPrices(null, -5.0, 12.5, null, -2.3, 3.7, 8.1, -1.0, 6.4, null);
        
        tracker.printLowestK();
        
        List<Double> result = tracker.getLowestK();
        System.out.println("Expected: [3.7, 6.4, 8.1, 12.5]");
        System.out.println("Result:   " + result);
        System.out.println("Test: " + (result.equals(Arrays.asList(3.7, 6.4, 8.1, 12.5)) ? "✓ PASS" : "✗ FAIL"));
        System.out.println();
    }
    
    /**
     * 测试K值边界情况
     */
    private static void testKBoundaryCases() {
        System.out.println("--- Test 3: K Boundary Cases ---");
        
        // 测试K=0
        System.out.println("Test 3.1: K = 0");
        LowestKPriceTracker tracker0 = new LowestKPriceTracker(0);
        tracker0.addPrices(10.0, 20.0, 30.0);
        tracker0.printLowestK();
        System.out.println("Should return empty list: " + tracker0.getLowestK());
        System.out.println("Is empty: " + tracker0.isEmpty());
        System.out.println();
        
        // 测试K为负数
        System.out.println("Test 3.2: K = -3");
        LowestKPriceTracker trackerNegative = new LowestKPriceTracker(-3);
        trackerNegative.addPrices(10.0, 20.0, 30.0);
        trackerNegative.printLowestK();
        System.out.println("Should return empty list: " + trackerNegative.getLowestK());
        System.out.println("Is empty: " + trackerNegative.isEmpty());
        System.out.println();
        
        // 测试K大于数据量
        System.out.println("Test 3.3: K > number of data points");
        LowestKPriceTracker trackerLargeK = new LowestKPriceTracker(10);
        trackerLargeK.addPrices(5.0, 3.0, 8.0, 1.0, 6.0);
        trackerLargeK.printLowestK();
        List<Double> result = trackerLargeK.getLowestK();
        System.out.println("Expected: [1.0, 3.0, 5.0, 6.0, 8.0]");
        System.out.println("Result:   " + result);
        System.out.println("Test: " + (result.equals(Arrays.asList(1.0, 3.0, 5.0, 6.0, 8.0)) ? "✓ PASS" : "✗ FAIL"));
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    private static void testPerformance() {
        System.out.println("--- Test 4: Performance Test ---");
        
        // 测试不同规模的数据
        int[] testSizes = {10, 100, 1000, 10000};
        int kValue = 100;
        
        for (int size : testSizes) {
            System.out.println("\nTest with " + size + " random prices, K=" + kValue);
            
            LowestKPriceTracker tracker = new LowestKPriceTracker(kValue);
            Random random = new Random(42); // 固定种子保证可重现
            
            long startTime = System.currentTimeMillis();
            
            // 生成并添加随机价格（0-1000范围）
            for (int i = 0; i < size; i++) {
                double price = random.nextDouble() * 1000;
                tracker.addPrice(price);
            }
            
            long addTime = System.currentTimeMillis() - startTime;
            
            // 获取并验证结果
            List<Double> result = tracker.getLowestK();
            
            // 验证结果是否正确（简单验证：检查是否按递增顺序）
            boolean isSorted = true;
            for (int i = 1; i < result.size(); i++) {
                if (result.get(i) < result.get(i - 1)) {
                    isSorted = false;
                    break;
                }
            }
            
            System.out.println("  Add time: " + addTime + "ms");
            System.out.println("  Result size: " + result.size() + "/" + Math.min(kValue, size));
            System.out.println("  Sorted: " + (isSorted ? "✓" : "✗"));
            
            // 验证是否真的是最低的K个值
            if (size >= kValue && result.size() == kValue) {
                double maxInResult = result.get(result.size() - 1);
                // 这里简单验证，实际生产环境需要更严格的验证
                System.out.println("  Highest in result: " + maxInResult);
                System.out.println("  Max heap peek: " + tracker.maxHeap.peek());
            }
        }
    }
}