import java.util.*;

public class Q02_MinHeapInsert {
    private ArrayList<Integer> heap;

    public Q02_MinHeapInsert() {
        this.heap = new ArrayList<>();
    }

    /**
     * 添加元素到最小堆，使用 bubble-up 维护堆性质
     */
    public void add(int value) {
        heap.add(value);           // 添加到末尾
        bubbleUp(heap.size() - 1); // 上浮调整
    }

    /**
     * 上浮操作：将索引 i 处的元素上浮到正确位置
     */
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            // 如果当前节点 >= 父节点，堆性质已满足，停止
            if (heap.get(index) >= heap.get(parentIndex)) {
                break;
            }
            // 交换当前节点与父节点
            swap(index, parentIndex);
            index = parentIndex;
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
     * 返回堆顶元素（最小值），空堆返回 null
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
     * 验证当前堆是否为有效的最小堆
     * 检查每个父节点是否 <= 其子节点
     */
    public boolean isValidMinHeap() {
        for (int i = 0; i < heap.size(); i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            // 检查左子节点
            if (left < heap.size() && heap.get(i) > heap.get(left)) {
                return false;
            }
            // 检查右子节点
            if (right < heap.size() && heap.get(i) > heap.get(right)) {
                return false;
            }
        }
        return true;
    }

    // 测试代码（非必须）
    public static void main(String[] args) {
        Q02_MinHeapInsert minHeap = new Q02_MinHeapInsert();
        
        // 测试 add
        minHeap.add(5);
        minHeap.add(3);
        minHeap.add(7);
        minHeap.add(1);
        minHeap.add(4);
        
        // 测试 peek
        System.out.println("Peek: " + minHeap.peek()); // 应该输出 1
        
        // 测试 size
        System.out.println("Size: " + minHeap.size()); // 应该输出 5
        
        // 测试 snapshot
        System.out.println("Snapshot: " + minHeap.snapshot()); 
        // 应该输出 [1, 3, 7, 5, 4]（或其他有效的堆顺序）
        
        // 测试 isValidMinHeap
        System.out.println("Is valid min heap: " + minHeap.isValidMinHeap()); // true
        
        // 测试重复值
        minHeap.add(3);
        System.out.println("After adding duplicate 3: " + minHeap.snapshot());
        System.out.println("Is valid: " + minHeap.isValidMinHeap()); // true
    }
}