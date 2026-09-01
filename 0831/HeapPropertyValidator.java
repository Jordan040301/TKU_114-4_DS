import java.util.List;

/**
 * 堆属性验证器
 * 验证列表是否满足最小堆或最大堆的性质
 */
public class HeapPropertyValidator {
    
    /**
     * 检查列表是否为最小堆
     * @param list 要检查的整数列表
     * @return true 如果是最小堆，否则 false
     */
    public static boolean isMinHeap(List<Integer> list) {
        // null 返回 false
        if (list == null) {
            return false;
        }
        
        // 空列表或单一元素返回 true
        if (list.size() <= 1) {
            return true;
        }
        
        // 检查每个父节点是否满足最小堆性质
        // 对于索引 i 的父节点，其子节点索引为 2*i+1 和 2*i+2
        for (int i = 0; i < list.size(); i++) {
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;
            
            // 检查左子节点
            if (leftChild < list.size()) {
                if (list.get(i) > list.get(leftChild)) {
                    return false;  // 父节点大于子节点，违反最小堆性质
                }
            }
            
            // 检查右子节点
            if (rightChild < list.size()) {
                if (list.get(i) > list.get(rightChild)) {
                    return false;  // 父节点大于子节点，违反最小堆性质
                }
            }
        }
        
        return true;
    }
    
    /**
     * 检查列表是否为最大堆
     * @param list 要检查的整数列表
     * @return true 如果是最大堆，否则 false
     */
    public static boolean isMaxHeap(List<Integer> list) {
        // null 返回 false
        if (list == null) {
            return false;
        }
        
        // 空列表或单一元素返回 true
        if (list.size() <= 1) {
            return true;
        }
        
        // 检查每个父节点是否满足最大堆性质
        // 对于索引 i 的父节点，其子节点索引为 2*i+1 和 2*i+2
        for (int i = 0; i < list.size(); i++) {
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;
            
            // 检查左子节点
            if (leftChild < list.size()) {
                if (list.get(i) < list.get(leftChild)) {
                    return false;  // 父节点小于子节点，违反最大堆性质
                }
            }
            
            // 检查右子节点
            if (rightChild < list.size()) {
                if (list.get(i) < list.get(rightChild)) {
                    return false;  // 父节点小于子节点，违反最大堆性质
                }
            }
        }
        
        return true;
    }
    
    /**
     * 获取堆的详细信息（用于调试）
     */
    public static String getHeapInfo(List<Integer> list) {
        if (list == null) {
            return "null list";
        }
        
        if (list.isEmpty()) {
            return "Empty list: true for both min and max heap";
        }
        
        if (list.size() == 1) {
            return "Single element [" + list.get(0) + "]: true for both min and max heap";
        }
        
        boolean isMin = isMinHeap(list);
        boolean isMax = isMaxHeap(list);
        
        return String.format("Size: %d, Is Min Heap: %s, Is Max Heap: %s", 
                           list.size(), isMin, isMax);
    }
    
    /**
     * 打印堆的树形结构（用于可视化）
     */
    public static void printHeapTree(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("Empty or null heap");
            return;
        }
        
        System.out.println("Heap as array: " + list);
        System.out.println("Heap as tree:");
        printTreeHelper(list, 0, 0);
        System.out.println();
    }
    
    /**
     * 递归辅助方法：打印树形结构
     */
    private static void printTreeHelper(List<Integer> list, int index, int level) {
        if (index >= list.size()) {
            return;
        }
        
        // 缩进
        String indent = "  ".repeat(level);
        
        // 打印当前节点
        System.out.println(indent + "└── " + list.get(index));
        
        // 递归打印子节点
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        
        printTreeHelper(list, leftChild, level + 1);
        printTreeHelper(list, rightChild, level + 1);
    }
    
    /**
     * 主方法：测试和验证
     */
    public static void main(String[] args) {
        System.out.println("=== Heap Property Validator Test ===\n");
        
        // 测试1：空列表和单一元素
        testEmptyAndSingle();
        
        // 测试2：有效最小堆
        testValidMinHeaps();
        
        // 测试3：有效最大堆
        testValidMaxHeaps();
        
        // 测试4：无效堆
        testInvalidHeaps();
        
        // 测试5：边界情况
        testEdgeCases();
        
        // 测试6：所有测试用例综合验证
        testAllCases();
    }
    
    /**
     * 测试空列表和单一元素
     */
    private static void testEmptyAndSingle() {
        System.out.println("--- Test 1: Empty List and Single Element ---");
        
        List<Integer> nullList = null;
        List<Integer> emptyList = List.of();
        List<Integer> singleList = List.of(42);
        
        System.out.println("null list:");
        System.out.println("  isMinHeap: " + isMinHeap(nullList));
        System.out.println("  isMaxHeap: " + isMaxHeap(nullList));
        
        System.out.println("empty list:");
        System.out.println("  isMinHeap: " + isMinHeap(emptyList));
        System.out.println("  isMaxHeap: " + isMaxHeap(emptyList));
        
        System.out.println("single element [42]:");
        System.out.println("  isMinHeap: " + isMinHeap(singleList));
        System.out.println("  isMaxHeap: " + isMaxHeap(singleList));
        
        System.out.println();
    }
    
    /**
     * 测试有效的最大堆
     */
    private static void testValidMaxHeaps() {
        System.out.println("--- Test 2: Valid Max Heaps ---");
        
        // 最大堆示例
        List<Integer> maxHeap1 = List.of(50, 40, 30, 10, 20, 25, 15);
        List<Integer> maxHeap2 = List.of(100, 90, 80, 70, 60, 50);
        List<Integer> maxHeap3 = List.of(10, 5, 8);
        List<Integer> maxHeap4 = List.of(5, 3, 4, 1, 2);
        
        testHeap("Max Heap 1", maxHeap1, true, false);
        testHeap("Max Heap 2", maxHeap2, true, false);
        testHeap("Max Heap 3", maxHeap3, true, false);
        testHeap("Max Heap 4", maxHeap4, true, false);
    }
    
    /**
     * 测试有效的最小堆
     */
    private static void testValidMinHeaps() {
        System.out.println("--- Test 3: Valid Min Heaps ---");
        
        // 最小堆示例
        List<Integer> minHeap1 = List.of(10, 15, 20, 25, 30, 35, 40);
        List<Integer> minHeap2 = List.of(5, 10, 15, 20, 25);
        List<Integer> minHeap3 = List.of(8, 5, 10);
        List<Integer> minHeap4 = List.of(1, 2, 3, 4, 5);
        
        testHeap("Min Heap 1", minHeap1, false, true);
        testHeap("Min Heap 2", minHeap2, false, true);
        testHeap("Min Heap 3", minHeap3, false, true);
        testHeap("Min Heap 4", minHeap4, false, true);
    }
    
    /**
     * 测试无效堆
     */
    private static void testInvalidHeaps() {
        System.out.println("--- Test 4: Invalid Heaps ---");
        
        // 无效堆示例
        List<Integer> invalid1 = List.of(10, 20, 15, 5, 25);  // 5在20下，违反最小堆
        List<Integer> invalid2 = List.of(50, 30, 40, 60, 20); // 60在30下，违反最大堆
        List<Integer> invalid3 = List.of(5, 10, 3);           // 3在5下，违反最小堆
        List<Integer> invalid4 = List.of(20, 15, 25, 10, 30); // 30在15下，违反最大堆
        
        testHeap("Invalid 1", invalid1, false, false);
        testHeap("Invalid 2", invalid2, false, false);
        testHeap("Invalid 3", invalid3, false, false);
        testHeap("Invalid 4", invalid4, false, false);
    }
    
    /**
     * 测试边界情况
     */
    private static void testEdgeCases() {
        System.out.println("--- Test 5: Edge Cases ---");
        
        // 相同元素
        List<Integer> allSame = List.of(5, 5, 5, 5, 5);
        testHeap("All same [5,5,5,5,5]", allSame, true, true);
        
        // 两个元素
        List<Integer> twoElements1 = List.of(10, 5);
        List<Integer> twoElements2 = List.of(5, 10);
        testHeap("Two elements [10,5]", twoElements1, false, true);
        testHeap("Two elements [5,10]", twoElements2, true, false);
        
        // 完全平衡树
        List<Integer> balanced = List.of(15, 10, 20, 5, 12, 18, 25);
        testHeap("Balanced tree [15,10,20,5,12,18,25]", balanced, false, false);
        
        System.out.println();
    }
    
    /**
     * 测试所有用例的综合验证
     */
    private static void testAllCases() {
        System.out.println("--- Test 6: All Test Cases Summary ---");
        
        Object[][] testCases = {
            {null, false, false, "null"},
            {List.of(), true, true, "empty"},
            {List.of(42), true, true, "single"},
            {List.of(10, 15, 20, 25), false, true, "valid min heap"},
            {List.of(50, 40, 30, 20), false, true, "valid max heap"},
            {List.of(10, 20, 15, 30), false, false, "invalid heap"},
            {List.of(5, 5, 5, 5), true, true, "all same"}
        };
        
        System.out.println("Test Case | Min Heap | Max Heap | Description");
        System.out.println("----------|----------|----------|-------------");
        
        for (Object[] testCase : testCases) {
            @SuppressWarnings("unchecked")
            List<Integer> list = (List<Integer>) testCase[0];
            boolean expectedMin = (boolean) testCase[1];
            boolean expectedMax = (boolean) testCase[2];
            String description = (String) testCase[3];
            
            boolean actualMin = isMinHeap(list);
            boolean actualMax = isMaxHeap(list);
            
            String minResult = (actualMin == expectedMin) ? "✓ PASS" : "✗ FAIL";
            String maxResult = (actualMax == expectedMax) ? "✓ PASS" : "✗ FAIL";
            
            // 格式化显示列表内容
            String listStr = (list == null) ? "null" : list.toString();
            if (listStr.length() > 15) {
                listStr = listStr.substring(0, 15) + "...";
            }
            
            System.out.printf("%-8s | %-8s | %-8s | %s%n", 
                            listStr, minResult, maxResult, description);
        }
        
        System.out.println();
    }
    
    /**
     * 辅助方法：测试单个堆并输出结果
     */
    private static void testHeap(String name, List<Integer> list, boolean expectedMin, boolean expectedMax) {
        boolean actualMin = isMinHeap(list);
        boolean actualMax = isMaxHeap(list);
        
        String minResult = (actualMin == expectedMin) ? "✓" : "✗";
        String maxResult = (actualMax == expectedMax) ? "✓" : "✗";
        
        System.out.println(name + ": " + list);
        System.out.println("  Min Heap: " + actualMin + " [" + minResult + "]");
        System.out.println("  Max Heap: " + actualMax + " [" + maxResult + "]");
        System.out.println("  Info: " + getHeapInfo(list));
        
        // 打印树形结构（仅对较小的堆）
        if (list.size() <= 7) {
            printHeapTree(list);
        }
        System.out.println();
    }
}