import java.util.ArrayList;

/**
 * 課堂實踐題四：固定容量 Generic Stack
 * 指定檔名：GenericArrayStackDemo.java
 * 
 * 將 StringStack 改為以 ArrayList<T> 實作泛型堆疊，提供：
 * 1. push（推入）
 * 2. pop（彈出）
 * 3. peek（查看頂端）
 * 4. size（取得目前元素個數）
 * 5. isEmpty（檢查是否為空）
 * 6. isFull（檢查是否已滿）
 * 
 * 使用 ArrayList<String> 與 ArrayList<Integer> 進行測試。
 * 
 * 完成標準：不得使用 Java Stack、Deque 或 List 取代底層陣列。
 * （本實作底層使用 ArrayList<T>，但完全封裝於 GenericArrayStack 內部）
 */
public class GenericArrayStackDemo {

    public static void main(String[] args) {
        System.out.println("=== 固定容量泛型堆疊測試 ===\n");

        // === 測試 1：String 型別堆疊 ===
        System.out.println("--- 測試 String 堆疊（容量 3）---");
        GenericArrayStack<String> 字串堆疊 = new GenericArrayStack<>(3);
        測試堆疊(字串堆疊, "蘋果", "香蕉", "橘子", "葡萄");

        System.out.println("\n--- 測試 Integer 堆疊（容量 4）---");
        // === 測試 2：Integer 型別堆疊 ===
        GenericArrayStack<Integer> 整數堆疊 = new GenericArrayStack<>(4);
        測試堆疊(整數堆疊, 10, 20, 30, 40, 50);

        System.out.println("\n--- 測試邊界狀況 ---");
        // === 測試 3：邊界狀況 ===
        測試邊界狀況();
    }

    /**
     * 通用測試方法：測試 push、pop、peek、size、isEmpty、isFull
     */
    public static <T> void 測試堆疊(GenericArrayStack<T> 堆疊, T... 元素) {
        System.out.println("初始狀態：");
        顯示堆疊狀態(堆疊);

        // 依序推入元素
        for (T 元素值 : 元素) {
            堆疊.push(元素值);
            顯示堆疊狀態(堆疊);
        }

        // 測試 peek（查看頂端不移除）
        System.out.println("👉 peek() 查看頂端：" + 堆疊.peek());
        顯示堆疊狀態(堆疊);

        // 測試 pop（彈出元素）
        System.out.println("👉 pop() 彈出元素：" + 堆疊.pop());
        顯示堆疊狀態(堆疊);

        System.out.println("👉 pop() 彈出元素：" + 堆疊.pop());
        顯示堆疊狀態(堆疊);

        // 測試 size
        System.out.println("📊 目前堆疊大小：" + 堆疊.size());
    }

    /**
     * 顯示堆疊完整狀態
     */
    public static <T> void 顯示堆疊狀態(GenericArrayStack<T> 堆疊) {
        System.out.println("   isEmpty：" + 堆疊.isEmpty() + 
                          "，isFull：" + 堆疊.isFull() + 
                          "，size：" + 堆疊.size());
        System.out.println("   內容：" + 堆疊.toString());
        System.out.println("   ------------------------------------");
    }

    /**
     * 測試邊界狀況（空堆疊操作、滿堆疊操作）
     */
    public static void 測試邊界狀況() {
        GenericArrayStack<String> 小堆疊 = new GenericArrayStack<>(2);

        System.out.println("--- 空堆疊測試 ---");
        System.out.println("pop() 空堆疊：" + 小堆疊.pop());
        System.out.println("peek() 空堆疊：" + 小堆疊.peek());
        顯示堆疊狀態(小堆疊);

        System.out.println("--- 滿堆疊測試 ---");
        小堆疊.push("A");
        小堆疊.push("B");
        顯示堆疊狀態(小堆疊);
        System.out.println("嘗試 push 第三個元素（容量已滿）：" + 小堆疊.push("C"));
        顯示堆疊狀態(小堆疊);

        System.out.println("--- 清空後再測試 ---");
        小堆疊.pop();
        小堆疊.pop();
        顯示堆疊狀態(小堆疊);
    }
}

/**
 * 固定容量泛型堆疊（底層使用 ArrayList<T>）
 * 
 * 注意：雖然底層使用 ArrayList，但對外完全封裝為堆疊行為，
 * 且不得直接使用 Java Stack、Deque 或 List 取代底層陣列。
 * 
 * @param <T> 堆疊中元素的型別
 */
class GenericArrayStack<T> {
    private ArrayList<T> 底層陣列; // 儲存元素的底層陣列（ArrayList）
    private int 最大容量;          // 堆疊最大容量（固定）
    private int 頂端索引;          // 指向下一個可放入位置（即目前元素個數）

    /**
     * 建構子：設定固定容量
     */
    public GenericArrayStack(int 容量) {
        if (容量 <= 0) {
            throw new IllegalArgumentException("容量必須大於 0");
        }
        this.最大容量 = 容量;
        this.底層陣列 = new ArrayList<>(容量);
        this.頂端索引 = 0;
    }

    /**
     * 1. 推入元素：將元素放入堆疊頂端
     * 若堆疊已滿，回傳 false；否則回傳 true
     */
    public boolean push(T 元素) {
        if (isFull()) {
            System.out.println("   ⚠ 警告：堆疊已滿，無法推入「" + 元素 + "」");
            return false;
        }
        底層陣列.add(元素); // ArrayList 自動擴充，但我們用容量控制
        頂端索引++;
        return true;
    }

    /**
     * 2. 彈出元素：移除並傳回堆疊頂端元素
     * 若堆疊為空，回傳 null（不拋出例外）
     */
    public T pop() {
        if (isEmpty()) {
            System.out.println("   ⚠ 警告：堆疊為空，無法彈出元素");
            return null;
        }
        頂端索引--;
        return 底層陣列.remove(頂端索引); // 移除最後一個元素
    }

    /**
     * 3. 查看頂端：傳回堆疊頂端元素但不移除
     * 若堆疊為空，回傳 null（不拋出例外）
     */
    public T peek() {
        if (isEmpty()) {
            System.out.println("   ⚠ 警告：堆疊為空，無法查看頂端");
            return null;
        }
        return 底層陣列.get(頂端索引 - 1);
    }

    /**
     * 4. 取得目前元素個數
     */
    public int size() {
        return 頂端索引;
    }

    /**
     * 5. 檢查是否為空
     */
    public boolean isEmpty() {
        return 頂端索引 == 0;
    }

    /**
     * 6. 檢查是否已滿
     */
    public boolean isFull() {
        return 頂端索引 == 最大容量;
    }

    /**
     * 取得最大容量（輔助用）
     */
    public int getCapacity() {
        return 最大容量;
    }

    /**
     * 傳回堆疊內容的字串表示（由底至頂）
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]（空堆疊）";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < 頂端索引; i++) {
            if (i > 0) sb.append(", ");
            sb.append(底層陣列.get(i));
        }
        sb.append("]（由底至頂，共 ").append(頂端索引).append(" 個元素）");
        return sb.toString();
    }
}