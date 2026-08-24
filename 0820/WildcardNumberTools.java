import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通配符數值工具類別
 * 展示 extends 和 super 通配符的實際應用
 */
public class WildcardNumberTools {

    /**
     * 計算數值列表的平均值
     * 使用 ? extends Number 可接收 List<Integer>、List<Double> 等
     * 
     * @param values 數值列表（可為 null 或空列表）
     * @return 平均值，空列表回傳 0.0
     */
    public static double average(List<? extends Number> values) {
        // 處理 null 或空列表
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        int count = 0;

        for (Number value : values) {
            if (value != null) {
                sum += value.doubleValue();
                count++;
            }
        }

        // 如果所有元素都是 null，回傳 0.0
        if (count == 0) {
            return 0.0;
        }

        return sum / count;
    }

    /**
     * 找出數值列表中的最大值
     * 使用 ? extends Number 可接收 List<Integer>、List<Double> 等
     * 
     * @param values 數值列表（可為 null 或空列表）
     * @return 最大值，空列表回傳 Double.NaN
     */
    public static double maximum(List<? extends Number> values) {
        // 處理 null 或空列表
        if (values == null || values.isEmpty()) {
            return Double.NaN;
        }

        Double max = null;

        for (Number value : values) {
            if (value != null) {
                double current = value.doubleValue();
                if (max == null || current > max) {
                    max = current;
                }
            }
        }

        // 如果所有元素都是 null，回傳 Double.NaN
        if (max == null) {
            return Double.NaN;
        }

        return max;
    }

    /**
     * 將指定範圍的整數加入目標列表
     * 使用 ? super Integer 可接收 List<Integer>、List<Number>、List<Object> 等
     * 
     * @param target 目標列表（不可為 null）
     * @param start  起始值（包含）
     * @param end    結束值（不包含）
     * @throws IllegalArgumentException 當 target 為 null 時拋出
     */
    public static void addRange(List<? super Integer> target, int start, int end) {
        // 檢查 target 是否為 null
        if (target == null) {
            throw new IllegalArgumentException("目標列表不能為 null");
        }

        // 如果 start >= end，不加入任何資料
        if (start >= end) {
            return;
        }

        // 將範圍內的整數加入列表
        for (int i = start; i < end; i++) {
            target.add(i);
        }
    }

    // ========== 以下為測試程式碼 ==========

    public static void main(String[] args) {
        System.out.println("========== 測試 average 方法 ==========");
        testAverage();

        System.out.println("\n========== 測試 maximum 方法 ==========");
        testMaximum();

        System.out.println("\n========== 測試 addRange 方法 ==========");
        testAddRange();

        System.out.println("\n========== 通配符靈活性測試 ==========");
        testWildcardFlexibility();

        System.out.println("\n========== 邊界情況測試 ==========");
        testEdgeCases();
    }

    /**
     * 測試 average 方法
     */
    private static void testAverage() {
        // 測試 List<Integer>
        List<Integer> intList = Arrays.asList(10, 20, 30, 40, 50);
        System.out.println("整數列表: " + intList);
        System.out.println("平均值: " + average(intList));  // 30.0

        // 測試 List<Double>
        List<Double> doubleList = Arrays.asList(1.5, 2.5, 3.5, 4.5);
        System.out.println("\n浮點數列表: " + doubleList);
        System.out.println("平均值: " + average(doubleList));  // 3.0

        // 測試混合型態（List<Number>）
        List<Number> mixedList = new ArrayList<>();
        mixedList.add(10);
        mixedList.add(20.5);
        mixedList.add(30L);
        mixedList.add(40.0f);
        System.out.println("\n混合數值列表: " + mixedList);
        System.out.println("平均值: " + average(mixedList));  // 25.125
    }

    /**
     * 測試 maximum 方法
     */
    private static void testMaximum() {
        // 測試 List<Integer>
        List<Integer> intList = Arrays.asList(10, 50, 30, 90, 20);
        System.out.println("整數列表: " + intList);
        System.out.println("最大值: " + maximum(intList));  // 90.0

        // 測試 List<Double>
        List<Double> doubleList = Arrays.asList(1.5, 9.8, 3.2, 7.1);
        System.out.println("\n浮點數列表: " + doubleList);
        System.out.println("最大值: " + maximum(doubleList));  // 9.8

        // 測試負數
        List<Integer> negativeList = Arrays.asList(-10, -50, -30, -20);
        System.out.println("\n負數列表: " + negativeList);
        System.out.println("最大值: " + maximum(negativeList));  // -10.0

        // 測試單一元素
        List<Integer> singleList = Arrays.asList(100);
        System.out.println("\n單一元素列表: " + singleList);
        System.out.println("最大值: " + maximum(singleList));  // 100.0
    }

    /**
     * 測試 addRange 方法
     */
    private static void testAddRange() {
        // 測試 List<Integer>
        List<Integer> intList = new ArrayList<>();
        System.out.println("List<Integer> 加入前: " + intList);
        addRange(intList, 0, 5);
        System.out.println("加入 0~4 後: " + intList);  // [0, 1, 2, 3, 4]

        // 測試 List<Number>
        List<Number> numberList = new ArrayList<>();
        numberList.add(100.5);
        System.out.println("\nList<Number> 加入前: " + numberList);
        addRange(numberList, 10, 15);
        System.out.println("加入 10~14 後: " + numberList);  // [100.5, 10, 11, 12, 13, 14]

        // 測試 List<Object>
        List<Object> objectList = new ArrayList<>();
        objectList.add("字串");
        System.out.println("\nList<Object> 加入前: " + objectList);
        addRange(objectList, 20, 23);
        System.out.println("加入 20~22 後: " + objectList);  // [字串, 20, 21, 22]

        // 測試 start >= end（不加入任何資料）
        List<Integer> emptyRangeList = new ArrayList<>();
        System.out.println("\n測試 start > end（加入前）: " + emptyRangeList);
        addRange(emptyRangeList, 10, 5);
        System.out.println("start(10) > end(5)，結果: " + emptyRangeList);  // []
    }

    /**
     * 測試通配符的靈活性 - 同一方法可接收不同型態
     */
    private static void testWildcardFlexibility() {
        System.out.println("--- average 可以接收多種型態 ---");
        
        // ✅ 可以接收 List<Integer>
        List<Integer> ints = Arrays.asList(1, 2, 3);
        System.out.println("List<Integer> 平均值: " + average(ints));
        
        // ✅ 可以接收 List<Double>
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        System.out.println("List<Double> 平均值: " + average(doubles));
        
        // ✅ 可以接收 List<Float>
        List<Float> floats = Arrays.asList(1.5f, 2.5f, 3.5f);
        System.out.println("List<Float> 平均值: " + average(floats));
        
        // ✅ 可以接收 List<Long>
        List<Long> longs = Arrays.asList(10L, 20L, 30L);
        System.out.println("List<Long> 平均值: " + average(longs));

        System.out.println("\n--- addRange 可以加入多種型態的列表 ---");
        
        // ✅ 可以加入 List<Integer>
        List<Integer> intList = new ArrayList<>();
        addRange(intList, 0, 3);
        System.out.println("List<Integer> 加入後: " + intList);
        
        // ✅ 可以加入 List<Number>
        List<Number> numList = new ArrayList<>();
        addRange(numList, 5, 8);
        System.out.println("List<Number> 加入後: " + numList);
        
        // ✅ 可以加入 List<Object>
        List<Object> objList = new ArrayList<>();
        addRange(objList, 10, 13);
        System.out.println("List<Object> 加入後: " + objList);
    }

    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- average 邊界測試 ---");
        
        // 測試 null
        System.out.println("null 列表平均值: " + average(null));  // 0.0
        
        // 測試空列表
        List<Integer> emptyList = new ArrayList<>();
        System.out.println("空列表平均值: " + average(emptyList));  // 0.0
        
        // 測試包含 null 的列表
        List<Integer> listWithNull = new ArrayList<>();
        listWithNull.add(10);
        listWithNull.add(null);
        listWithNull.add(20);
        System.out.println("包含 null 的列表: " + listWithNull);
        System.out.println("平均值（忽略 null）: " + average(listWithNull));  // 15.0

        System.out.println("\n--- maximum 邊界測試 ---");
        
        // 測試 null
        System.out.println("null 列表最大值: " + maximum(null));  // NaN
        
        // 測試空列表
        List<Integer> emptyList2 = new ArrayList<>();
        System.out.println("空列表最大值: " + maximum(emptyList2));  // NaN
        
        // 測試全部為 null 的列表
        List<Integer> allNullList = new ArrayList<>();
        allNullList.add(null);
        allNullList.add(null);
        System.out.println("全部為 null 的列表: " + allNullList);
        System.out.println("最大值: " + maximum(allNullList));  // NaN

        System.out.println("\n--- addRange 邊界測試 ---");
        
        // 測試 null target
        try {
            addRange(null, 0, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("target 為 null: " + e.getMessage());
        }
        
        // 測試 start == end（不加入）
        List<Integer> equalRange = new ArrayList<>();
        System.out.println("start(5) == end(5)，加入前: " + equalRange);
        addRange(equalRange, 5, 5);
        System.out.println("加入後: " + equalRange);  // []
        
        // 測試負數範圍
        List<Integer> negativeRange = new ArrayList<>();
        addRange(negativeRange, -5, 0);
        System.out.println("加入 -5~-1 後: " + negativeRange);  // [-5, -4, -3, -2, -1]
    }
}