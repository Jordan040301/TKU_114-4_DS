/**
 * 課堂實踐題六：循環隊列狀態追蹤
 * 指定檔名：CircularQueuePractice.java
 * 
 * 以容量 4 建立 CircularQueue<String>，連續執行以下操作：
 * enqueue A, enqueue B, enqueue C,
 * dequeue, dequeue,
 * enqueue D, enqueue E, enqueue F,
 * dequeue, enqueue G
 * 
 * 操作後輸出內部陣列 front、rear、size。
 * 最後依照 FIFO 順序取出所有元素。
 * 
 * 完成標準：在出隊時不可搬移全部元素，必須使用模擬環索引。
 */
public class CircularQueuePractice {

    public static void main(String[] args) {
        System.out.println("=== 循環隊列狀態追蹤測試 ===\n");

        // 以容量 4 建立 CircularQueue<String>
        CircularQueue<String> 隊列 = new CircularQueue<>(4);

        // 執行指定操作序列
        System.out.println("--- 執行指定操作序列 ---");
        
        隊列.enqueue("A");
        顯示隊列狀態(隊列, "enqueue A");
        
        隊列.enqueue("B");
        顯示隊列狀態(隊列, "enqueue B");
        
        隊列.enqueue("C");
        顯示隊列狀態(隊列, "enqueue C");
        
        隊列.dequeue();
        顯示隊列狀態(隊列, "dequeue");
        
        隊列.dequeue();
        顯示隊列狀態(隊列, "dequeue");
        
        隊列.enqueue("D");
        顯示隊列狀態(隊列, "enqueue D");
        
        隊列.enqueue("E");
        顯示隊列狀態(隊列, "enqueue E");
        
        隊列.enqueue("F");
        顯示隊列狀態(隊列, "enqueue F");
        
        隊列.dequeue();
        顯示隊列狀態(隊列, "dequeue");
        
        隊列.enqueue("G");
        顯示隊列狀態(隊列, "enqueue G");

        // 輸出最終內部狀態
        System.out.println("\n=== 最終內部狀態 ===");
        隊列.顯示內部狀態();

        // 依照 FIFO 順序取出所有元素
        System.out.println("\n=== 依照 FIFO 順序取出所有元素 ===");
        while (!隊列.isEmpty()) {
            String 元素 = 隊列.dequeue();
            System.out.println("取出：「" + 元素 + "」");
        }
        
        System.out.println("\n=== 測試完成 ===");
    }

    /**
     * 顯示隊列狀態（含操作名稱）
     */
    public static void 顯示隊列狀態(CircularQueue<String> 隊列, String 操作) {
        System.out.println("操作：" + 操作);
        System.out.println("   " + 隊列.取得狀態描述());
        System.out.println("   ------------------------------------");
    }
}

/**
 * 循環隊列類別（泛型）
 * 使用模擬環索引，出隊時不搬移元素
 * 
 * @param <T> 儲存元素的型別
 */
class CircularQueue<T> {
    private Object[] 底層陣列; // 儲存元素的底層陣列
    private int 前端索引;      // 指向隊首元素的位置
    private int 後端索引;      // 指向下一個可放入元素的位置
    private int 元素個數;      // 目前實際儲存的元素個數
    private int 容量;          // 隊列最大容量

    /**
     * 建構子：設定固定容量
     */
    public CircularQueue(int 容量) {
        if (容量 <= 0) {
            throw new IllegalArgumentException("容量必須大於 0");
        }
        this.容量 = 容量;
        this.底層陣列 = new Object[容量];
        this.前端索引 = 0;
        this.後端索引 = 0;
        this.元素個數 = 0;
    }

    /**
     * 入隊：將元素加入隊列尾端
     * 若隊列已滿，顯示警告訊息並回傳 false
     */
    public boolean enqueue(T 元素) {
        if (isFull()) {
            System.out.println("   ⚠ 警告：隊列已滿，無法入隊「" + 元素 + "」");
            return false;
        }
        底層陣列[後端索引] = 元素;
        後端索引 = (後端索引 + 1) % 容量; // 使用模擬環索引
        元素個數++;
        return true;
    }

    /**
     * 出隊：移除並傳回隊首元素
     * 若隊列為空，顯示警告訊息並回傳 null
     * ★ 出隊時不可搬移全部元素，只移動前端索引
     */
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("   ⚠ 警告：隊列為空，無法出隊");
            return null;
        }
        @SuppressWarnings("unchecked")
        T 元素 = (T) 底層陣列[前端索引];
        底層陣列[前端索引] = null; // 釋放參考（可選）
        前端索引 = (前端索引 + 1) % 容量; // 使用模擬環索引
        元素個數--;
        return 元素;
    }

    /**
     * 查看隊首元素（不移除）
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return (T) 底層陣列[前端索引];
    }

    /**
     * 檢查是否為空
     */
    public boolean isEmpty() {
        return 元素個數 == 0;
    }

    /**
     * 檢查是否已滿
     */
    public boolean isFull() {
        return 元素個數 == 容量;
    }

    /**
     * 取得目前元素個數
     */
    public int size() {
        return 元素個數;
    }

    /**
     * 取得隊列容量
     */
    public int getCapacity() {
        return 容量;
    }

    /**
     * 顯示內部狀態：front、rear、size 及陣列內容
     */
    public void 顯示內部狀態() {
        System.out.println("   📊 內部狀態：");
        System.out.println("      front（前端索引）= " + 前端索引);
        System.out.println("      rear（後端索引） = " + 後端索引);
        System.out.println("      size（元素個數）= " + 元素個數);
        System.out.println("      capacity（容量）= " + 容量);
        System.out.println("   📋 底層陣列內容（索引 0 ~ " + (容量 - 1) + "）：");
        for (int i = 0; i < 容量; i++) {
            String 標記 = "";
            if (i == 前端索引 && i == 後端索引 && 元素個數 == 0) {
                標記 = "  ← front/rear（空）";
            } else if (i == 前端索引 && 元素個數 > 0) {
                標記 = "  ← front（隊首）";
            } else if (i == 後端索引) {
                標記 = "  ← rear（下一個可放入位置）";
            }
            System.out.println("      底層陣列[" + i + "] = " + 底層陣列[i] + 標記);
        }
        System.out.println("   📌 有效元素順序（FIFO）：" + 取得有效元素順序());
    }

    /**
     * 取得狀態描述（用於即時輸出）
     */
    public String 取得狀態描述() {
        return String.format(
            "front=%d, rear=%d, size=%d, 內容=%s",
            前端索引, 後端索引, 元素個數, 取得有效元素順序()
        );
    }

    /**
     * 取得有效元素順序（由 front 到 rear，FIFO 順序）
     */
    private String 取得有效元素順序() {
        if (isEmpty()) {
            return "[]（空）";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int 當前索引 = 前端索引;
        for (int i = 0; i < 元素個數; i++) {
            if (i > 0) sb.append(", ");
            sb.append(底層陣列[當前索引]);
            當前索引 = (當前索引 + 1) % 容量;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 傳回隊列內容的字串表示（FIFO 順序）
     */
    @Override
    public String toString() {
        return 取得有效元素順序();
    }
}