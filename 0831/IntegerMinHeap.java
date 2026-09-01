import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class IntegerMinHeap {
    private List<Integer> heap;
    
    public IntegerMinHeap() {
        heap = new ArrayList<>();
    }
    
    /**
     * 添加元素到最小堆中
     * 时间复杂度: O(log n)
     */
    public void add(int value) {
        heap.add(value);
        int index = heap.size() - 1;
        bubbleUp(index);
    }
    
    /**
     * 上浮操作：维护最小堆性质
     */
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            // 如果当前节点小于父节点，则交换（最小堆）
            if (heap.get(index) < heap.get(parentIndex)) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }
    
    /**
     * 查看最小值（不移除）
     * 时间复杂度: O(1)
     * @throws NoSuchElementException 如果堆为空
     */
    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return heap.get(0);
    }
    
    /**
     * 移除并返回最小值
     * 时间复杂度: O(log n)
     * @throws NoSuchElementException 如果堆为空
     */
    public int removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        
        int minValue = heap.get(0);
        int lastIndex = heap.size() - 1;
        
        // 将最后一个元素移到根位置
        heap.set(0, heap.get(lastIndex));
        heap.remove(lastIndex);
        
        // 如果不是空堆，执行下沉操作
        if (!isEmpty()) {
            bubbleDown(0);
        }
        
        return minValue;
    }
    
    /**
     * 下沉操作：维护最小堆性质
     */
    private void bubbleDown(int index) {
        int size = heap.size();
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;
            
            // 找到左右子节点中最小的
            if (leftChild < size && heap.get(leftChild) < heap.get(smallest)) {
                smallest = leftChild;
            }
            if (rightChild < size && heap.get(rightChild) < heap.get(smallest)) {
                smallest = rightChild;
            }
            
            // 如果当前节点已经是最小的，停止
            if (smallest == index) {
                break;
            }
            
            // 交换并继续下沉
            swap(index, smallest);
            index = smallest;
        }
    }
    
    /**
     * 交换两个位置的值
     */
    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    
    /**
     * 返回堆的大小
     * 时间复杂度: O(1)
     */
    public int size() {
        return heap.size();
    }
    
    /**
     * 检查堆是否为空
     * 时间复杂度: O(1)
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    /**
     * 获取当前堆的快照（用于测试）
     */
    public List<Integer> snapshot() {
        return new ArrayList<>(heap);
    }
    
    /**
     * 打印堆的状态
     */
    public void printHeap() {
        System.out.println(heap);
    }
    
    /**
     * 主方法：测试和验证
     */
    public static void main(String[] args) {
        System.out.println("=== IntegerMinHeap Test ===\n");
        
        // 测试1：基本功能测试
        testBasicOperations();
        
        // 测试2：异常处理测试
        testExceptionHandling();
        
        // 测试3：非递减顺序验证
        testNonDecreasingOrder();
        
        // 测试4：大规模数据测试
        testLargeScale();
    }
    
    /**
     * 测试基本操作
     */
    private static void testBasicOperations() {
        System.out.println("--- Test 1: Basic Operations ---");
        IntegerMinHeap minHeap = new IntegerMinHeap();
        int[] testData = {5, 3, 8, 1, 4, 7, 6, 2};
        
        System.out.println("Inserting: " + java.util.Arrays.toString(testData));
        for (int value : testData) {
            minHeap.add(value);
            System.out.print("After adding " + value + ": ");
            minHeap.printHeap();
        }
        
        System.out.println("Heap size: " + minHeap.size());
        System.out.println("Is empty: " + minHeap.isEmpty());
        System.out.println("Current min (peek): " + minHeap.peek());
        System.out.println();
    }
    
    /**
     * 测试异常处理
     */
    private static void testExceptionHandling() {
        System.out.println("--- Test 2: Exception Handling ---");
        IntegerMinHeap emptyHeap = new IntegerMinHeap();
        
        System.out.println("Empty heap created.");
        System.out.println("Size: " + emptyHeap.size());
        System.out.println("Is empty: " + emptyHeap.isEmpty());
        
        // 测试 peek() 异常
        try {
            emptyHeap.peek();
            System.out.println("ERROR: peek() should throw exception on empty heap");
        } catch (NoSuchElementException e) {
            System.out.println("✓ peek() correctly threw: " + e.getMessage());
        }
        
        // 测试 removeMin() 异常
        try {
            emptyHeap.removeMin();
            System.out.println("ERROR: removeMin() should throw exception on empty heap");
        } catch (NoSuchElementException e) {
            System.out.println("✓ removeMin() correctly threw: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * 验证移除结果为非递减顺序
     */
    private static void testNonDecreasingOrder() {
        System.out.println("--- Test 3: Non-Decreasing Order Verification ---");
        IntegerMinHeap minHeap = new IntegerMinHeap();
        int[] testData = {8, 3, 1, 7, 5, 2, 6, 4};
        
        System.out.println("Test data: " + java.util.Arrays.toString(testData));
        
        // 插入所有元素
        for (int value : testData) {
            minHeap.add(value);
        }
        
        System.out.println("Heap after all insertions: " + minHeap.snapshot());
        
        // 依次移除所有元素，验证非递减顺序
        System.out.println("Removing elements (should be non-decreasing):");
        List<Integer> removedElements = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            int min = minHeap.removeMin();
            removedElements.add(min);
            System.out.print(min + " ");
        }
        System.out.println();
        
        // 验证是否为非递减顺序
        boolean isNonDecreasing = true;
        for (int i = 1; i < removedElements.size(); i++) {
            if (removedElements.get(i) < removedElements.get(i - 1)) {
                isNonDecreasing = false;
                break;
            }
        }
        
        System.out.println("Removed sequence: " + removedElements);
        System.out.println("Is non-decreasing: " + (isNonDecreasing ? "✓ PASS" : "✗ FAIL"));
        
        // 验证堆已经为空
        System.out.println("Heap is empty: " + minHeap.isEmpty());
        System.out.println();
    }
    
    /**
     * 大规模数据测试
     */
    private static void testLargeScale() {
        System.out.println("--- Test 4: Large Scale Test ---");
        IntegerMinHeap minHeap = new IntegerMinHeap();
        int dataSize = 100;
        
        System.out.println("Adding " + dataSize + " random values...");
        java.util.Random random = new java.util.Random(42); // 固定种子使测试可重现
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < dataSize; i++) {
            int value = random.nextInt(1000);
            minHeap.add(value);
        }
        long addTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Add time: " + addTime + "ms");
        System.out.println("Heap size: " + minHeap.size());
        System.out.println("Min value: " + minHeap.peek());
        
        // 移除所有元素并验证
        List<Integer> removed = new ArrayList<>();
        startTime = System.currentTimeMillis();
        while (!minHeap.isEmpty()) {
            removed.add(minHeap.removeMin());
        }
        long removeTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Remove time: " + removeTime + "ms");
        System.out.println("Removed " + removed.size() + " elements");
        
        // 验证非递减
        boolean isNonDecreasing = true;
        for (int i = 1; i < removed.size(); i++) {
            if (removed.get(i) < removed.get(i - 1)) {
                isNonDecreasing = false;
                break;
            }
        }
        System.out.println("All elements removed in non-decreasing order: " + 
                          (isNonDecreasing ? "✓ PASS" : "✗ FAIL"));
    }
}