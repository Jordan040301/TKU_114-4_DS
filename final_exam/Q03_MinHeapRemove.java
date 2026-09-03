import java.util.*;

public class Q03_MinHeapRemove {
    private ArrayList<Integer> heap;

    /**
     * 构造函数：使用 bottom-up heapify 构建最小堆
     */
    public Q03_MinHeapRemove(List<Integer> values) {
        this.heap = new ArrayList<>();
        
        // 忽略 null 值并添加到堆中
        if (values != null) {
            for (Integer val : values) {
                if (val != null) {
                    heap.add(val);
                }
            }
        }
        
        // Bottom-up heapify：从最后一个非叶子节点开始向下调整
        for (int i = (heap.size() / 2) - 1; i >= 0; i--) {
            bubbleDown(i);
        }
    }

    /**
     * 移除并返回最小元素（堆顶），空堆返回 null
     */
    public Integer removeMin() {
        if (heap.isEmpty()) {
            return null;
        }
        
        // 保存堆顶元素（最小值）
        Integer min = heap.get(0);
        
        // 如果堆中只有一个元素
        if (heap.size() == 1) {
            heap.remove(0);
        } else {
            // 用最后一个元素替换堆顶
            heap.set(0, heap.remove(heap.size() - 1));
            // 向下调整
            bubbleDown(0);
        }
        
        return min;
    }

    /**
     * 查看堆顶元素（最小值），空堆返回 null
     */
    public Integer peek() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    /**
     * 返回堆中元素个数
     */
    public int size() {
        return heap.size();
    }

    /**
     * 返回堆的快照（副本），不暴露内部 List
     */
    public List<Integer> snapshot() {
        return new ArrayList<>(heap);
    }

    /**
     * 向下调整（bubble-down）：将索引 i 处的元素下沉到正确位置
     */
    private void bubbleDown(int index) {
        int size = heap.size();
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;
            
            // 找到当前节点、左子节点、右子节点中的最小值
            if (left < size && heap.get(left) < heap.get(smallest)) {
                smallest = left;
            }
            if (right < size && heap.get(right) < heap.get(smallest)) {
                smallest = right;
            }
            
            // 如果当前节点已经是最小的，停止
            if (smallest == index) {
                break;
            }
            
            // 交换当前节点与最小的子节点
            swap(index, smallest);
            index = smallest;
        }
    }

    /**
     * 交换堆中两个位置的值
     */
    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * 验证是否为有效的堆（辅助方法，用于测试）
     */
    public boolean isValidMinHeap() {
        for (int i = 0; i < heap.size(); i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            if (left < heap.size() && heap.get(i) > heap.get(left)) {
                return false;
            }
            if (right < heap.size() && heap.get(i) > heap.get(right)) {
                return false;
            }
        }
        return true;
    }

    // 测试代码
    public static void main(String[] args) {
        // 测试 heapify
        List<Integer> values = Arrays.asList(5, 3, 7, 1, 4, 2, 6, null, 0);
        Q03_MinHeapRemove heap = new Q03_MinHeapRemove(values);
        
        System.out.println("Initial heap: " + heap.snapshot());
        System.out.println("Is valid: " + heap.isValidMinHeap());
        System.out.println("Size: " + heap.size());
        System.out.println("Peek: " + heap.peek());
        
        // 测试 removeMin
        System.out.println("\n=== Testing removeMin ===");
        while (heap.size() > 0) {
            System.out.println("Remove: " + heap.removeMin() + ", Heap: " + heap.snapshot());
            System.out.println("Is valid: " + heap.isValidMinHeap());
        }
        
        // 测试空堆
        System.out.println("\n=== Testing empty heap ===");
        System.out.println("Remove from empty: " + heap.removeMin()); // null
        System.out.println("Peek from empty: " + heap.peek());       // null
        System.out.println("Size: " + heap.size());
    }
}