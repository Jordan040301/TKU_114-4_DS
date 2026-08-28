public class RecursiveCallReport {
    // 遞迴方法：計算 data[index] + sum(data, index + 1)
    public static int sum(int[] data, int index) {
        // 基本情況 (base case): 當 index 超出陣列長度，回傳 0
        if (index >= data.length) {
            // 輸出遞迴報告 (無 current value，因為沒有元素)
            System.out.printf("index = %d, current < N/A >, recursive result = 0, return = 0%n", index);
            return 0;
        }

        // 遞迴步驟：先呼叫後面的總和
        int recursiveResult = sum(data, index + 1);
        // 計算 current value 與回傳值
        int currentValue = data[index];
        int returnValue = currentValue + recursiveResult;

        // 輸出報告
        System.out.printf("index = %d, current = %d, recursive result = %d, return = %d%n",
                          index, currentValue, recursiveResult, returnValue);

        return returnValue;
    }

    // 輔助方法：簡化呼叫，自動從 index 0 開始
    public static int sum(int[] data) {
        return sum(data, 0);
    }

    public static void main(String[] args) {
        System.out.println("===== 測試一般陣列: {2, 4, 6} =====");
        int[] normalArray = {2, 4, 6};
        int resultNormal = sum(normalArray);
        System.out.println("總和 = " + resultNormal);
        System.out.println();

        System.out.println("===== 測試單一元素陣列: {10} =====");
        int[] singleArray = {10};
        int resultSingle = sum(singleArray);
        System.out.println("總和 = " + resultSingle);
        System.out.println();

        System.out.println("===== 測試空陣列: {} =====");
        int[] emptyArray = {};
        int resultEmpty = sum(emptyArray);
        System.out.println("總和 = " + resultEmpty);
    }
}