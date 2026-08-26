/**
 * 課堂實作題二：返回序列統計
 * 指定檔名：RecursiveArrayStatistics.java
 * 
 * 完成 maximum、minimum 和 countAbove。
 * 公共包裝器對 null 或空陣列拋出 IllegalArgumentException，
 * 輔助方法不得被複製（使用遞迴共享邏輯）。
 */
public class RecursiveArrayStatistics {

    /**
     * 公共包裝器：尋找陣列中的最大值
     * @param arr 輸入整數陣列
     * @return 最大值
     * @throws IllegalArgumentException 若陣列為 null 或空陣列
     */
    public static int maximum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("陣列不得為 null 或空陣列");
        }
        return maximumHelper(arr, 0, arr[0]);
    }

    /**
     * 遞迴輔助方法：尋找最大值
     * @param arr 陣列
     * @param index 當前索引
     * @param currentMax 目前找到的最大值
     * @return 最大值
     */
    private static int maximumHelper(int[] arr, int index, int currentMax) {
        // 基本情況：已遍歷完整個陣列
        if (index == arr.length) {
            return currentMax;
        }
        // 更新最大值
        if (arr[index] > currentMax) {
            currentMax = arr[index];
        }
        // 遞迴處理下一個元素
        return maximumHelper(arr, index + 1, currentMax);
    }

    /**
     * 公共包裝器：尋找陣列中的最小值
     * @param arr 輸入整數陣列
     * @return 最小值
     * @throws IllegalArgumentException 若陣列為 null 或空陣列
     */
    public static int minimum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("陣列不得為 null 或空陣列");
        }
        return minimumHelper(arr, 0, arr[0]);
    }

    /**
     * 遞迴輔助方法：尋找最小值
     * @param arr 陣列
     * @param index 當前索引
     * @param currentMin 目前找到的最小值
     * @return 最小值
     */
    private static int minimumHelper(int[] arr, int index, int currentMin) {
        // 基本情況：已遍歷完整個陣列
        if (index == arr.length) {
            return currentMin;
        }
        // 更新最小值
        if (arr[index] < currentMin) {
            currentMin = arr[index];
        }
        // 遞迴處理下一個元素
        return minimumHelper(arr, index + 1, currentMin);
    }

    /**
     * 公共包裝器：計算陣列中大於指定閾值的元素數量
     * @param arr 輸入整數陣列
     * @param threshold 閾值
     * @return 大於閾值的元素數量
     * @throws IllegalArgumentException 若陣列為 null 或空陣列
     */
    public static int countAbove(int[] arr, int threshold) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("陣列不得為 null 或空陣列");
        }
        return countAboveHelper(arr, 0, threshold);
    }

    /**
     * 遞迴輔助方法：計算大於閾值的元素數量
     * @param arr 陣列
     * @param index 當前索引
     * @param threshold 閾值
     * @return 大於閾值的元素數量
     */
    private static int countAboveHelper(int[] arr, int index, int threshold) {
        // 基本情況：已遍歷完整個陣列
        if (index == arr.length) {
            return 0;
        }
        // 檢查當前元素是否大於閾值，加上剩餘部分的結果
        int count = (arr[index] > threshold) ? 1 : 0;
        return count + countAboveHelper(arr, index + 1, threshold);
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        // 測試陣列
        int[] testArray1 = {5, 2, 9, 1, 7, 3};
        int[] testArray2 = {-10, -5, 0, 5, 10};
        int[] testArray3 = {42};
        int[] testArray4 = null;
        int[] testArray5 = {};

        System.out.println("===== 陣列統計測試 =====");

        // 測試正常陣列
        System.out.println("陣列: [5, 2, 9, 1, 7, 3]");
        System.out.println("  最大值 (maximum): " + maximum(testArray1));
        System.out.println("  最小值 (minimum): " + minimum(testArray1));
        System.out.println("  大於 4 的數量 (countAbove): " + countAbove(testArray1, 4));
        System.out.println("  大於 10 的數量 (countAbove): " + countAbove(testArray1, 10));
        System.out.println();

        System.out.println("陣列: [-10, -5, 0, 5, 10]");
        System.out.println("  最大值 (maximum): " + maximum(testArray2));
        System.out.println("  最小值 (minimum): " + minimum(testArray2));
        System.out.println("  大於 0 的數量 (countAbove): " + countAbove(testArray2, 0));
        System.out.println("  大於 -5 的數量 (countAbove): " + countAbove(testArray2, -5));
        System.out.println();

        System.out.println("陣列: [42]");
        System.out.println("  最大值 (maximum): " + maximum(testArray3));
        System.out.println("  最小值 (minimum): " + minimum(testArray3));
        System.out.println("  大於 40 的數量 (countAbove): " + countAbove(testArray3, 40));
        System.out.println("  大於 42 的數量 (countAbove): " + countAbove(testArray3, 42));
        System.out.println();

        // 測試例外情況
        System.out.println("===== 例外情況測試 =====");
        try {
            System.out.println("測試 null 陣列 (maximum): ");
            maximum(testArray4);
        } catch (IllegalArgumentException e) {
            System.out.println("  正確拋出例外: " + e.getMessage());
        }

        try {
            System.out.println("測試空陣列 (minimum): ");
            minimum(testArray5);
        } catch (IllegalArgumentException e) {
            System.out.println("  正確拋出例外: " + e.getMessage());
        }

        try {
            System.out.println("測試 null 陣列 (countAbove): ");
            countAbove(testArray4, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("  正確拋出例外: " + e.getMessage());
        }
    }
}