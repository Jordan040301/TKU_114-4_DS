import java.util.Arrays;

/**
 * 泛型陣列工具類別
 * 提供各種泛型陣列操作的方法
 */
public class GenericArrayTools {

    /**
     * 計算目標元素在陣列中出現的次數
     * 
     * @param data   要查詢的陣列（可為 null）
     * @param target 要搜尋的目標元素（可為 null）
     * @param <T>    陣列元素型態
     * @return 目標元素出現的次數
     */
    public static <T> int countMatches(T[] data, T target) {
        // 處理 null 陣列
        if (data == null) {
            return 0;
        }

        int count = 0;
        for (T element : data) {
            // 使用 Objects.equals 安全處理 null 比較
            if (java.util.Objects.equals(element, target)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 取得陣列的最後一個元素
     * 
     * @param data 要查詢的陣列（可為 null）
     * @param <T>  陣列元素型態
     * @return 最後一個元素，若陣列為 null 或空陣列則回傳 null
     */
    public static <T> T last(T[] data) {
        // 處理 null 陣列或空陣列
        if (data == null || data.length == 0) {
            return null;
        }
        return data[data.length - 1];
    }

    /**
     * 交換陣列中兩個指定位置的元素
     * 
     * @param data   要操作的陣列（可為 null）
     * @param first  第一個位置的索引
     * @param second 第二個位置的索引
     * @param <T>    陣列元素型態
     * @throws IllegalArgumentException 當陣列為 null、空陣列或索引非法時拋出
     */
    public static <T> void swap(T[] data, int first, int second) {
        // 處理 null 陣列
        if (data == null) {
            throw new IllegalArgumentException("陣列不能為 null");
        }

        // 處理空陣列
        if (data.length == 0) {
            throw new IllegalArgumentException("陣列不能為空");
        }

        // 檢查索引是否合法
        if (first < 0 || first >= data.length) {
            throw new IllegalArgumentException(
                String.format("第一個索引 %d 超出範圍 [0, %d)", first, data.length)
            );
        }
        if (second < 0 || second >= data.length) {
            throw new IllegalArgumentException(
                String.format("第二個索引 %d 超出範圍 [0, %d)", second, data.length)
            );
        }

        // 如果兩個索引相同，不需要交換
        if (first == second) {
            return;
        }

        // 執行交換
        T temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    // ========== 以下為測試程式碼 ==========

    public static void main(String[] args) {
        System.out.println("========== 測試 countMatches ==========");
        testCountMatches();

        System.out.println("\n========== 測試 last ==========");
        testLast();

        System.out.println("\n========== 測試 swap ==========");
        testSwap();

        System.out.println("\n========== 邊界情況測試 ==========");
        testEdgeCases();
    }

    /**
     * 測試 countMatches 方法
     */
    private static void testCountMatches() {
        // 測試字串陣列
        String[] names = {"小明", "小華", "小明", "小美", "小明"};
        System.out.println("字串陣列: " + Arrays.toString(names));
        System.out.println("搜尋 '小明' 出現次數: " + countMatches(names, "小明"));  // 3
        System.out.println("搜尋 '小華' 出現次數: " + countMatches(names, "小華"));  // 1
        System.out.println("搜尋 '小強' 出現次數: " + countMatches(names, "小強"));  // 0

        // 測試整數陣列
        Integer[] numbers = {1, 2, 3, 2, 4, 2, 5};
        System.out.println("\n整數陣列: " + Arrays.toString(numbers));
        System.out.println("搜尋 2 出現次數: " + countMatches(numbers, 2));  // 3
        System.out.println("搜尋 10 出現次數: " + countMatches(numbers, 10)); // 0

        // 測試 null 元素
        String[] withNull = {"A", null, "B", null, "C"};
        System.out.println("\n包含 null 的陣列: " + Arrays.toString(withNull));
        System.out.println("搜尋 null 出現次數: " + countMatches(withNull, null));  // 2
    }

    /**
     * 測試 last 方法
     */
    private static void testLast() {
        // 測試字串陣列
        String[] names = {"小明", "小華", "小美"};
        System.out.println("字串陣列: " + Arrays.toString(names));
        System.out.println("最後一個元素: " + last(names));  // 小美

        // 測試整數陣列
        Integer[] numbers = {10, 20, 30, 40};
        System.out.println("\n整數陣列: " + Arrays.toString(numbers));
        System.out.println("最後一個元素: " + last(numbers));  // 40

        // 測試單一元素陣列
        String[] single = {"唯一元素"};
        System.out.println("\n單一元素陣列: " + Arrays.toString(single));
        System.out.println("最後一個元素: " + last(single));  // 唯一元素

        // 測試 null 元素陣列（但陣列本身不是 null）
        String[] withNull = {"A", "B", null};
        System.out.println("\n包含 null 的陣列: " + Arrays.toString(withNull));
        System.out.println("最後一個元素: " + last(withNull));  // null
    }

    /**
     * 測試 swap 方法
     */
    private static void testSwap() {
        // 測試字串陣列
        String[] names = {"小明", "小華", "小美", "小強"};
        System.out.println("交換前: " + Arrays.toString(names));
        swap(names, 0, 2);
        System.out.println("交換索引 0 和 2 後: " + Arrays.toString(names));  // [小美, 小華, 小明, 小強]

        // 測試整數陣列
        Integer[] numbers = {1, 2, 3, 4, 5};
        System.out.println("\n交換前: " + Arrays.toString(numbers));
        swap(numbers, 1, 3);
        System.out.println("交換索引 1 和 3 後: " + Arrays.toString(numbers));  // [1, 4, 3, 2, 5]

        // 測試相同索引（不變）
        String[] same = {"A", "B", "C"};
        System.out.println("\n交換前: " + Arrays.toString(same));
        swap(same, 1, 1);
        System.out.println("交換相同索引 1 和 1 後: " + Arrays.toString(same));  // [A, B, C]
    }

    /**
     * 測試各種邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- countMatches 邊界測試 ---");
        
        // 測試 null 陣列
        System.out.println("null 陣列搜尋 'A': " + countMatches(null, "A"));  // 0
        
        // 測試空陣列
        String[] empty = {};
        System.out.println("空陣列搜尋 'A': " + countMatches(empty, "A"));  // 0

        System.out.println("\n--- last 邊界測試 ---");
        
        // 測試 null 陣列
        System.out.println("null 陣列取最後元素: " + last(null));  // null
        
        // 測試空陣列
        String[] empty2 = {};
        System.out.println("空陣列取最後元素: " + last(empty2));  // null

        System.out.println("\n--- swap 邊界測試 ---");
        
        // 測試 null 陣列
        try {
            swap(null, 0, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("null 陣列交換: " + e.getMessage());
        }

        // 測試空陣列
        try {
            String[] empty3 = {};
            swap(empty3, 0, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("空陣列交換: " + e.getMessage());
        }

        // 測試非法索引
        try {
            String[] data = {"A", "B", "C"};
            swap(data, -1, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("非法索引（負數）: " + e.getMessage());
        }

        try {
            String[] data = {"A", "B", "C"};
            swap(data, 0, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("非法索引（超出範圍）: " + e.getMessage());
        }
    }
}