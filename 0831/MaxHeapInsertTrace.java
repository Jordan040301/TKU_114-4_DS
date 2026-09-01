import java.util.ArrayList;
import java.util.List;

public class MaxHeapInsertTrace {
    private List<Integer> heap;
    
    public MaxHeapInsertTrace() {
        heap = new ArrayList<>();
    }
    
    /**
     * 添加元素到最大堆中
     */
    public void add(int value) {
        heap.add(value);  // 添加到末尾
        int index = heap.size() - 1;
        bubbleUp(index);  // 上浮调整
    }
    
    /**
     * 上浮操作：维护最大堆性质
     */
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            // 如果当前节点大于父节点，则交换
            if (heap.get(index) > heap.get(parentIndex)) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;  // 已经满足最大堆性质
            }
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
     * 获取最大值（不移除）
     */
    public int peekMax() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0);
    }
    
    /**
     * 获取当前堆的快照
     */
    public List<Integer> snapshot() {
        return new ArrayList<>(heap);
    }
    
    /**
     * 打印当前堆的状态
     */
    public void printHeap() {
        System.out.println(heap);
    }
    
    /**
     * 检查堆是否为空
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    /**
     * 获取堆的大小
     */
    public int size() {
        return heap.size();
    }
    
    public static void main(String[] args) {
        MaxHeapInsertTrace maxHeap = new MaxHeapInsertTrace();
        int[] testData = {25, 40, 10, 50, 30, 50};
        
        System.out.println("=== Max Heap Insert Trace ===");
        System.out.println("Testing with: " + java.util.Arrays.toString(testData));
        System.out.println();
        
        for (int i = 0; i < testData.length; i++) {
            int value = testData[i];
            maxHeap.add(value);
            
            System.out.println("After inserting " + value + ":");
            System.out.print("  Heap: ");
            maxHeap.printHeap();
            System.out.println("  Max: " + maxHeap.peekMax());
            System.out.println();
        }
        
        // 验证最终结果
        System.out.println("=== Final Result ===");
        System.out.println("Final heap: " + maxHeap.snapshot());
        System.out.println("Root (max): " + maxHeap.peekMax());
        System.out.println("Root must be 50: " + (maxHeap.peekMax() == 50 ? "✓ PASS" : "✗ FAIL"));
    }
}