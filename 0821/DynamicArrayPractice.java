/**
 * 課堂實踐題五：動態陣列插入與刪除
 * 指定檔名：DynamicArrayPractice.java
 * 
 * 建立通用的 DynamicArray<T>，使用 Object[] 作為底層儲存結構，完成：
 * 1. void add(T value)              - 尾端新增
 * 2. void add(int index, T value)   - 指定位置插入
 * 3. T get(int index)               - 取得指定位置元素
 * 4. T set(int index, T value)      - 取代指定位置元素
 * 5. T remove(int index)            - 刪除指定位置元素
 * 6. int size()                     - 取得目前元素個數
 * 7. int capacity()                 - 取得目前陣列容量
 * 
 * 特性：
 * - 容量滿時增量為兩倍
 * - 刪除後將最後一個無效位置設為 null
 * - 分別使用 String 和 Integer 測試
 * - 測試索引 -1、size 與空結構刪除
 */
public class DynamicArrayPractice {

    public static void main(String[] args) {
        System.out.println("=== 動態陣列測試（String）===\n");
        測試字串動態陣列();

        System.out.println("\n=== 動態陣列測試（Integer）===\n");
        測試整數動態陣列();

        System.out.println("\n=== 邊界狀況測試 ===\n");
        測試邊界狀況();
    }

    /**
     * 測試 String 型別的動態陣列
     */
    public static void 測試字串動態陣列() {
        DynamicArray<String> 字串陣列 = new DynamicArray<>();

        System.out.println("--- 尾端新增 ---");
        字串陣列.add("蘋果");
        字串陣列.add("香蕉");
        字串陣列.add("橘子");
        顯示陣列狀態(字串陣列);

        System.out.println("--- 指定位置插入（索引 1 插入「西瓜」）---");
        字串陣列.add(1, "西瓜");
        顯示陣列狀態(字串陣列);

        System.out.println("--- 取得元素 ---");
        System.out.println("get(0) = " + 字串陣列.get(0));
        System.out.println("get(2) = " + 字串陣列.get(2));

        System.out.println("--- 取代元素（索引 2 設為「草莓」）---");
        String 舊值 = 字串陣列.set(2, "草莓");
        System.out.println("原值：「" + 舊值 + "」→ 新值：「草莓」");
        顯示陣列狀態(字串陣列);

        System.out.println("--- 刪除元素（刪除索引 1）---");
        String 刪除值 = 字串陣列.remove(1);
        System.out.println("刪除值：「" + 刪除值 + "」");
        顯示陣列狀態(字串陣列);
    }

    /**
     * 測試 Integer 型別的動態陣列
     */
    public static void 測試整數動態陣列() {
        DynamicArray<Integer> 整數陣列 = new DynamicArray<>();

        System.out.println("--- 尾端新增 ---");
        整數陣列.add(10);
        整數陣列.add(20);
        整數陣列.add(30);
        整數陣列.add(40);
        整數陣列.add(50);
        顯示陣列狀態(整數陣列);

        System.out.println("--- 指定位置插入（索引 2 插入 99）---");
        整數陣列.add(2, 99);
        顯示陣列狀態(整數陣列);

        System.out.println("--- 取得元素 ---");
        System.out.println("get(0) = " + 整數陣列.get(0));
        System.out.println("get(3) = " + 整數陣列.get(3));

        System.out.println("--- 取代元素（索引 4 設為 88）---");
        Integer 舊整數 = 整數陣列.set(4, 88);
        System.out.println("原值：" + 舊整數 + " → 新值：88");
        顯示陣列狀態(整數陣列);

        System.out.println("--- 刪除元素（刪除索引 2）---");
        Integer 刪除整數 = 整數陣列.remove(2);
        System.out.println("刪除值：" + 刪除整數);
        顯示陣列狀態(整數陣列);
    }

    /**
     * 測試邊界狀況（索引 -1、size 與空結構刪除）
     */
    public static void 測試邊界狀況() {
        DynamicArray<String> 邊界陣列 = new DynamicArray<>();

        System.out.println("--- 空結構測試 ---");
        System.out.println("size() = " + 邊界陣列.size());
        System.out.println("capacity() = " + 邊界陣列.capacity());
        顯示陣列狀態(邊界陣列);

        System.out.println("--- 測試索引 -1（無效索引）---");
        try {
            邊界陣列.get(-1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("   ✅ 正確捕捉例外：" + e.getMessage());
        }

        System.out.println("--- 測試索引 size（超出範圍）---");
        try {
            邊界陣列.get(0); // size 為 0，索引 0 超出範圍
        } catch (IndexOutOfBoundsException e) {
            System.out.println("   ✅ 正確捕捉例外：" + e.getMessage());
        }

        System.out.println("--- 測試空結構刪除（索引 0）---");
        try {
            邊界陣列.remove(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("   ✅ 正確捕捉例外：" + e.getMessage());
        }

        System.out.println("--- 測試空結構刪除（索引 -1）---");
        try {
            邊界陣列.remove(-1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("   ✅ 正確捕捉例外：" + e.getMessage());
        }

        System.out.println("--- 容量擴充測試（新增超過初始容量）---");
        DynamicArray<Integer> 擴充陣列 = new DynamicArray<>();
        System.out.println("初始容量：" + 擴充陣列.capacity());
        for (int i = 1; i <= 15; i++) {
            擴充陣列.add(i);
            if (i == 5 || i == 10 || i == 15) {
                System.out.println("  新增 " + i + " 個元素後，容量：" + 擴充陣列.capacity() + "，size：" + 擴充陣列.size());
            }
        }
        顯示陣列狀態(擴充陣列);

        System.out.println("--- 刪除後最後一個位置設為 null（驗證）---");
        DynamicArray<String> 刪除驗證陣列 = new DynamicArray<>();
        刪除驗證陣列.add("A");
        刪除驗證陣列.add("B");
        刪除驗證陣列.add("C");
        System.out.println("刪除前：" + 刪除驗證陣列);
        刪除驗證陣列.remove(1);
        System.out.println("刪除索引 1（B）後：" + 刪除驗證陣列);
        // 注意：toString 不會顯示 null，但內部陣列最後一個位置已設為 null
    }

    /**
     * 顯示陣列完整狀態
     */
    public static <T> void 顯示陣列狀態(DynamicArray<T> 陣列) {
        System.out.println("   size：" + 陣列.size() + "，capacity：" + 陣列.capacity());
        System.out.println("   內容：" + 陣列.toString());
        System.out.println("   ------------------------------------");
    }
}

/**
 * 通用動態陣列類別（泛型）
 * 底層使用 Object[] 儲存元素
 * 
 * @param <T> 儲存元素的型別
 */
class DynamicArray<T> {
    private Object[] 底層陣列; // 儲存元素的底層陣列
    private int 元素個數;      // 目前實際儲存的元素個數
    private static final int 初始容量 = 5; // 初始容量設定為 5（便於觀察擴充）

    /**
     * 建構子：建立初始容量為 5 的動態陣列
     */
    public DynamicArray() {
        底層陣列 = new Object[初始容量];
        元素個數 = 0;
    }

    /**
     * 1. 尾端新增元素
     */
    public void add(T value) {
        確保容量(元素個數 + 1);
        底層陣列[元素個數] = value;
        元素個數++;
    }

    /**
     * 2. 指定位置插入元素
     */
    public void add(int index, T value) {
        檢查索引範圍(index, 0, 元素個數); // 允許插入到最後一個位置（index == size）
        確保容量(元素個數 + 1);
        
        // 將從 index 開始的元素往後移動一位
        for (int i = 元素個數; i > index; i--) {
            底層陣列[i] = 底層陣列[i - 1];
        }
        底層陣列[index] = value;
        元素個數++;
    }

    /**
     * 3. 取得指定位置元素
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        檢查索引範圍(index, 0, 元素個數 - 1);
        return (T) 底層陣列[index];
    }

    /**
     * 4. 取代指定位置元素，傳回原值
     */
    @SuppressWarnings("unchecked")
    public T set(int index, T value) {
        檢查索引範圍(index, 0, 元素個數 - 1);
        T 舊值 = (T) 底層陣列[index];
        底層陣列[index] = value;
        return 舊值;
    }

    /**
     * 5. 刪除指定位置元素，傳回刪除值
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        檢查索引範圍(index, 0, 元素個數 - 1);
        T 刪除值 = (T) 底層陣列[index];
        
        // 將 index 之後的元素往前移動一位
        for (int i = index; i < 元素個數 - 1; i++) {
            底層陣列[i] = 底層陣列[i + 1];
        }
        
        元素個數--;
        // ★ 刪除後將最後一個無效位置設為 null（釋放參考）
        底層陣列[元素個數] = null;
        
        return 刪除值;
    }

    /**
     * 6. 取得目前元素個數
     */
    public int size() {
        return 元素個數;
    }

    /**
     * 7. 取得目前陣列容量
     */
    public int capacity() {
        return 底層陣列.length;
    }

    /**
     * 確保容量足夠，若不足則擴充為兩倍
     */
    private void 確保容量(int 需要容量) {
        if (需要容量 > 底層陣列.length) {
            // 容量滿時增量為兩倍
            int 新容量 = 底層陣列.length * 2;
            Object[] 新陣列 = new Object[新容量];
            // 複製舊陣列元素到新陣列
            System.arraycopy(底層陣列, 0, 新陣列, 0, 元素個數);
            底層陣列 = 新陣列;
            System.out.println("   🔄 容量擴充：" + 新容量 / 2 + " → " + 新容量);
        }
    }

    /**
     * 檢查索引是否在有效範圍內
     * @param index 要檢查的索引
     * @param min 最小允許值（包含）
     * @param max 最大允許值（包含）
     */
    private void 檢查索引範圍(int index, int min, int max) {
        if (index < min || index > max) {
            throw new IndexOutOfBoundsException(
                "索引 " + index + " 超出範圍，有效範圍為 [" + min + ", " + max + "]"
            );
        }
    }

    /**
     * 傳回陣列內容的字串表示
     */
    @Override
    public String toString() {
        if (元素個數 == 0) {
            return "[]（空陣列）";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < 元素個數; i++) {
            if (i > 0) sb.append(", ");
            sb.append(底層陣列[i]);
        }
        sb.append("]（共 ").append(元素個數).append(" 個元素）");
        return sb.toString();
    }
}