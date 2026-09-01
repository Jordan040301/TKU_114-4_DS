import java.util.Arrays;

/**
 * 可調整容量 Min Heap (最小堆)
 * 使用 int[] 陣列管理，容量不足時自動擴增為兩倍
 */
public class ArrayMinHeap {
    private int[] heap;      // 儲存堆元素的陣列
    private int size;        // 當前堆中元素數量
    private int capacity;    // 當前陣列容量
    
    /**
     * 建構子：初始化容量為 10
     */
    public ArrayMinHeap() {
        this.capacity = 10;
        this.heap = new int[capacity];
        this.size = 0;
    }
    
    /**
     * 建構子：指定初始容量
     * @param initialCapacity 初始容量
     */
    public ArrayMinHeap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("初始容量必須大於 0");
        }
        this.capacity = initialCapacity;
        this.heap = new int[capacity];
        this.size = 0;
    }
    
    /**
     * 新增元素到堆中
     * @param value 要新增的值
     */
    public void add(int value) {
        // 檢查容量是否足夠
        if (size >= capacity) {
            resize(capacity * 2);  // 容量不足時擴增為兩倍
        }
        
        // 將新元素放到最後
        heap[size] = value;
        size++;
        
        // 上浮調整，維護最小堆性質
        bubbleUp(size - 1);
    }
    
    /**
     * 批量新增元素
     * @param values 要新增的值陣列
     */
    public void addAll(int... values) {
        for (int value : values) {
            add(value);
        }
    }
    
    /**
     * 上浮操作：維護最小堆性質
     * @param index 要上浮的索引位置
     */
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            
            // 如果當前節點小於父節點，則交換（最小堆）
            if (heap[index] < heap[parentIndex]) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;  // 已滿足最小堆性質
            }
        }
    }
    
    /**
     * 刪除並回傳最小值（根節點）
     * @return 最小值
     * @throws IllegalStateException 如果堆為空
     */
    public int removeMin() {
        if (isEmpty()) {
            throw new IllegalStateException("堆為空，無法刪除元素");
        }
        
        int minValue = heap[0];
        
        // 將最後一個元素移到根節點
        heap[0] = heap[size - 1];
        size--;
        
        // 如果堆不為空，進行下沉調整
        if (size > 0) {
            bubbleDown(0);
        }
        
        return minValue;
    }
    
    /**
     * 下沉操作：維護最小堆性質
     * @param index 要下沉的索引位置
     */
    private void bubbleDown(int index) {
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;
            
            // 找出左右子節點中最小的
            if (leftChild < size && heap[leftChild] < heap[smallest]) {
                smallest = leftChild;
            }
            if (rightChild < size && heap[rightChild] < heap[smallest]) {
                smallest = rightChild;
            }
            
            // 如果當前節點已經是最小的，停止
            if (smallest == index) {
                break;
            }
            
            // 交換並繼續下沉
            swap(index, smallest);
            index = smallest;
        }
    }
    
    /**
     * 檢視最小值（不移除）
     * @return 最小值
     * @throws IllegalStateException 如果堆為空
     */
    public int peekMin() {
        if (isEmpty()) {
            throw new IllegalStateException("堆為空，無法檢視最小值");
        }
        return heap[0];
    }
    
    /**
     * 取得堆的大小
     * @return 元素數量
     */
    public int size() {
        return size;
    }
    
    /**
     * 檢查堆是否為空
     * @return true 如果為空
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * 取得當前容量
     * @return 容量大小
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * 擴增容量為指定大小
     * @param newCapacity 新容量
     */
    private void resize(int newCapacity) {
        // 至少保留 1 個元素空間
        if (newCapacity <= 0) {
            newCapacity = 1;
        }
        
        int[] newHeap = new int[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
        capacity = newCapacity;
        
        System.out.printf("  🔄 容量擴增: %d → %d%n", capacity / 2, capacity);
    }
    
    /**
     * 交換陣列中兩個位置的值
     */
    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    /**
     * 取得堆的快照（複製當前堆的狀態）
     * @return 包含所有元素的陣列（已排序）
     */
    public int[] snapshot() {
        if (isEmpty()) {
            return new int[0];
        }
        
        // 複製當前堆的內容
        int[] snapshot = new int[size];
        System.arraycopy(heap, 0, snapshot, 0, size);
        
        // 建立臨時堆來排序
        int[] sortedSnapshot = new int[size];
        System.arraycopy(heap, 0, sortedSnapshot, 0, size);
        
        // 使用堆排序：依序取出最小值
        int[] result = new int[size];
        int resultIndex = 0;
        
        // 複製堆狀態進行排序（不影響原始堆）
        int[] tempHeap = new int[size];
        System.arraycopy(heap, 0, tempHeap, 0, size);
        int tempSize = size;
        
        while (tempSize > 0) {
            // 取出最小值
            result[resultIndex] = tempHeap[0];
            resultIndex++;
            
            // 將最後一個元素移到根
            tempHeap[0] = tempHeap[tempSize - 1];
            tempSize--;
            
            // 下沉調整（使用臨時大小）
            if (tempSize > 0) {
                tempBubbleDown(tempHeap, tempSize, 0);
            }
        }
        
        return result;
    }
    
    /**
     * 臨時下沉操作（用於快照排序）
     */
    private void tempBubbleDown(int[] arr, int size, int index) {
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;
            
            if (leftChild < size && arr[leftChild] < arr[smallest]) {
                smallest = leftChild;
            }
            if (rightChild < size && arr[rightChild] < arr[smallest]) {
                smallest = rightChild;
            }
            
            if (smallest == index) {
                break;
            }
            
            int temp = arr[index];
            arr[index] = arr[smallest];
            arr[smallest] = temp;
            index = smallest;
        }
    }
    
    /**
     * 顯示當前堆的狀態
     */
    public void printHeap() {
        System.out.println("堆內容 (陣列形式): " + arrayToString());
        System.out.println("堆內容 (樹狀結構):");
        printTree(0, 0);
        System.out.println();
    }
    
    /**
     * 將陣列轉為字串
     */
    private String arrayToString() {
        if (isEmpty()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * 遞迴列印樹狀結構
     */
    private void printTree(int index, int level) {
        if (index >= size) {
            return;
        }
        
        // 先印右子樹
        printTree(2 * index + 2, level + 1);
        
        // 縮排
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        
        // 印當前節點
        System.out.println(heap[index]);
        
        // 印左子樹
        printTree(2 * index + 1, level + 1);
    }
    
    /**
     * 顯示詳細狀態
     */
    public void showStatus() {
        System.out.println("=== 堆狀態 ===");
        System.out.println("大小: " + size);
        System.out.println("容量: " + capacity);
        System.out.println("空: " + isEmpty());
        if (!isEmpty()) {
            System.out.println("最小值: " + peekMin());
        }
        System.out.println("內容: " + arrayToString());
        System.out.println();
    }
    
    /**
     * 清空堆
     */
    public void clear() {
        // 重置為初始容量
        capacity = 10;
        heap = new int[capacity];
        size = 0;
        System.out.println("🔄 堆已清空");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 可調整容量 Min Heap 測試 ===\n");
        
        // 測試 1：基本功能測試
        testBasicFunctionality();
        
        // 測試 2：容量擴增測試
        testResize();
        
        // 測試 3：快照測試
        testSnapshot();
        
        // 測試 4：邊界情況測試
        testEdgeCases();
        
        // 測試 5：大量資料測試（20 筆以上）
        testLargeData();
        
        // 測試 6：綜合操作測試
        testComprehensiveOperations();
    }
    
    /**
     * 測試基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- 測試 1: 基本功能測試 ---");
        
        ArrayMinHeap minHeap = new ArrayMinHeap();
        
        System.out.println("新增元素: 5, 3, 8, 1, 4, 7, 6, 2");
        minHeap.addAll(5, 3, 8, 1, 4, 7, 6, 2);
        
        minHeap.showStatus();
        minHeap.printHeap();
        
        System.out.println("刪除最小值 (應為 1): " + minHeap.removeMin());
        System.out.println("刪除最小值 (應為 2): " + minHeap.removeMin());
        
        minHeap.showStatus();
        System.out.println();
    }
    
    /**
     * 測試容量擴增
     */
    private static void testResize() {
        System.out.println("--- 測試 2: 容量擴增測試 ---");
        
        ArrayMinHeap minHeap = new ArrayMinHeap(4);
        System.out.println("初始容量: 4");
        
        System.out.println("新增 10 個元素: 15, 12, 18, 10, 14, 16, 13, 11, 17, 19");
        minHeap.addAll(15, 12, 18, 10, 14, 16, 13, 11, 17, 19);
        
        minHeap.showStatus();
        
        System.out.println("繼續新增 5 個元素: 20, 9, 21, 8, 22");
        minHeap.addAll(20, 9, 21, 8, 22);
        
        minHeap.showStatus();
        System.out.println();
    }
    
    /**
     * 測試快照功能
     */
    private static void testSnapshot() {
        System.out.println("--- 測試 3: 快照測試 ---");
        
        ArrayMinHeap minHeap = new ArrayMinHeap();
        int[] testData = {8, 3, 1, 7, 5, 2, 6, 4};
        
        System.out.println("原始資料: " + Arrays.toString(testData));
        minHeap.addAll(testData);
        
        System.out.println("堆狀態:");
        minHeap.printHeap();
        
        int[] snapshot = minHeap.snapshot();
        System.out.println("快照 (已排序): " + Arrays.toString(snapshot));
        
        // 驗證快照是否正確排序
        boolean isSorted = true;
        for (int i = 1; i < snapshot.length; i++) {
            if (snapshot[i] < snapshot[i - 1]) {
                isSorted = false;
                break;
            }
        }
        System.out.println("快照是否正確排序: " + (isSorted ? "✓ PASS" : "✗ FAIL"));
        
        // 驗證快照不影響原始堆
        System.out.println("快照後原始堆內容: " + minHeap.snapshot());
        System.out.println();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 4: 邊界情況測試 ---");
        
        // 測試 4.1: 空堆操作
        System.out.println("測試 4.1: 空堆操作");
        ArrayMinHeap minHeap = new ArrayMinHeap();
        minHeap.showStatus();
        
        try {
            minHeap.peekMin();
        } catch (IllegalStateException e) {
            System.out.println("✓ peekMin() 正確拋出例外: " + e.getMessage());
        }
        
        try {
            minHeap.removeMin();
        } catch (IllegalStateException e) {
            System.out.println("✓ removeMin() 正確拋出例外: " + e.getMessage());
        }
        System.out.println();
        
        // 測試 4.2: 單一元素
        System.out.println("測試 4.2: 單一元素");
        minHeap.add(42);
        System.out.println("新增 42 後:");
        minHeap.showStatus();
        System.out.println("最小值: " + minHeap.peekMin());
        System.out.println("刪除最小值: " + minHeap.removeMin());
        System.out.println("刪除後為空: " + minHeap.isEmpty());
        System.out.println();
        
        // 測試 4.3: 相同元素
        System.out.println("測試 4.3: 相同元素");
        minHeap.addAll(5, 5, 5, 5, 5);
        System.out.println("新增 5 個相同的元素 (5):");
        minHeap.showStatus();
        
        System.out.println("依序刪除所有 5:");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.removeMin() + " ");
        }
        System.out.println("\n");
    }
    
    /**
     * 測試大量資料（至少 20 筆）
     */
    private static void testLargeData() {
        System.out.println("--- 測試 5: 大量資料測試 (20+ 筆) ---");
        
        ArrayMinHeap minHeap = new ArrayMinHeap(5);
        System.out.println("初始容量: 5");
        System.out.println("新增 25 筆資料 (含亂數):");
        
        int[] testData = new int[25];
        java.util.Random random = new java.util.Random(42);
        
        System.out.print("資料: ");
        for (int i = 0; i < 25; i++) {
            testData[i] = random.nextInt(100) + 1;
            System.out.print(testData[i] + " ");
            if ((i + 1) % 10 == 0) {
                System.out.println();
                System.out.print("      ");
            }
        }
        System.out.println("\n");
        
        // 新增資料
        for (int i = 0; i < testData.length; i++) {
            minHeap.add(testData[i]);
            System.out.printf("  [%2d] 新增 %2d, 目前大小: %2d, 容量: %2d%n",
                             i + 1, testData[i], minHeap.size(), minHeap.getCapacity());
        }
        
        System.out.println("\n最終狀態:");
        minHeap.showStatus();
        
        // 驗證最小堆性質
        int[] snapshot = minHeap.snapshot();
        System.out.println("排序後前 10 個元素: " + Arrays.toString(
                          Arrays.copyOf(snapshot, Math.min(10, snapshot.length))));
        
        // 依序刪除所有元素，驗證非遞減順序
        System.out.println("\n依序刪除所有元素 (應為非遞減):");
        int previous = Integer.MIN_VALUE;
        boolean isNonDecreasing = true;
        int count = 0;
        
        while (!minHeap.isEmpty()) {
            int current = minHeap.removeMin();
            System.out.printf("%3d ", current);
            count++;
            if ((count) % 15 == 0) {
                System.out.println();
            }
            
            if (current < previous) {
                isNonDecreasing = false;
            }
            previous = current;
        }
        
        System.out.println("\n\n刪除順序是否非遞減: " + (isNonDecreasing ? "✓ PASS" : "✗ FAIL"));
        System.out.println("總共刪除元素數: " + count);
        System.out.println();
    }
    
    /**
     * 測試綜合操作
     */
    private static void testComprehensiveOperations() {
        System.out.println("--- 測試 6: 綜合操作測試 ---");
        
        ArrayMinHeap minHeap = new ArrayMinHeap(8);
        
        System.out.println("初始容量: 8");
        System.out.println("\n操作序列:");
        System.out.println("1. 新增 10, 20, 15, 30, 25");
        minHeap.addAll(10, 20, 15, 30, 25);
        System.out.println("  堆內容: " + minHeap.snapshot());
        
        System.out.println("2. 刪除最小值 (應為 10): " + minHeap.removeMin());
        System.out.println("  堆內容: " + minHeap.snapshot());
        
        System.out.println("3. 新增 5, 35, 12, 28");
        minHeap.addAll(5, 35, 12, 28);
        System.out.println("  堆內容: " + minHeap.snapshot());
        
        System.out.println("4. 刪除最小值 (應為 5): " + minHeap.removeMin());
        System.out.println("  堆內容: " + minHeap.snapshot());
        
        System.out.println("5. 新增 8, 18, 22");
        minHeap.addAll(8, 18, 22);
        System.out.println("  堆內容: " + minHeap.snapshot());
        
        System.out.println("6. 檢視最小值: " + minHeap.peekMin());
        System.out.println("  堆內容: " + minHeap.snapshot());
        
        System.out.println("\n最終狀態:");
        minHeap.showStatus();
        minHeap.printHeap();
        
        System.out.println("7. 依序刪除所有元素:");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.removeMin() + " ");
        }
        System.out.println("\n");
        
        System.out.println("8. 清空後狀態:");
        minHeap.showStatus();
    }
}