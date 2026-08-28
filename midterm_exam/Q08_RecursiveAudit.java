/**
 * 檔名：Q08_RecursiveAudit.java
 * 功能：遞迴資料稽核
 * 說明：三個 method 都必須使用 recursion
 *       不可使用 loop、Stream 或額外 collection
 */

public class Q08_RecursiveAudit {

    // ========== 1. sumValid: 加總有效值 ==========

    /**
     * 從 index 開始加總 0 到 100 之間的值（包含 0 和 100）
     * @param data 整數陣列
     * @param index 起始索引
     * @return 有效值的總和
     */
    public static int sumValid(int[] data, int index) {
        // data 為 null 時回傳 0
        if (data == null) {
            return 0;
        }

        // 負數 index 從 0 開始
        if (index < 0) {
            index = 0;
        }

        // index 超過範圍時回傳 0
        if (index >= data.length) {
            return 0;
        }

        // 遞迴：檢查當前值 + 繼續處理下一個
        int current = 0;
        if (data[index] >= 0 && data[index] <= 100) {
            current = data[index];
        }

        return current + sumValid(data, index + 1);
    }

    // ========== 2. countOccurrences: 計算出現次數 ==========

    /**
     * 從 index 開始計算 target 出現的次數
     * @param data 整數陣列
     * @param index 起始索引
     * @param target 要計算的目標值
     * @return target 出現的次數
     */
    public static int countOccurrences(int[] data, int index, int target) {
        // data 為 null 時回傳 0
        if (data == null) {
            return 0;
        }

        // 負數 index 從 0 開始
        if (index < 0) {
            index = 0;
        }

        // index 超過範圍時回傳 0
        if (index >= data.length) {
            return 0;
        }

        // 遞迴：檢查當前值是否等於 target + 繼續處理下一個
        int count = 0;
        if (data[index] == target) {
            count = 1;
        }

        return count + countOccurrences(data, index + 1, target);
    }

    // ========== 3. isValid: 檢查回文（忽略大小寫） ==========

    /**
     * 檢查字串是否為回文（忽略大小寫）
     * @param text 要檢查的字串
     * @param left 左指標
     * @param right 右指標
     * @return true 表示為回文，false 表示不是回文或 text 為 null
     */
    public static boolean isValid(String text, int left, int right) {
        // text 為 null 回傳 false
        if (text == null) {
            return false;
        }

        // 當 left >= right 回傳 true
        if (left >= right) {
            return true;
        }

        // 忽略大小寫比較
        char leftChar = Character.toLowerCase(text.charAt(left));
        char rightChar = Character.toLowerCase(text.charAt(right));

        // 如果不相等，不是回文
        if (leftChar != rightChar) {
            return false;
        }

        // 遞迴檢查內層
        return isValid(text, left + 1, right - 1);
    }

    /**
     * 方便使用的多載方法（從頭開始檢查）
     * @param text 要檢查的字串
     * @return true 表示為回文
     */
    public static boolean isValid(String text) {
        if (text == null) {
            return false;
        }
        return isValid(text, 0, text.length() - 1);
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試範例 =====");
        int[] data = {10, -1, 20, 101, 20};
        System.out.println(Q08_RecursiveAudit.sumValid(data, 0));        // 50 (10 + 20 + 20)
        System.out.println(Q08_RecursiveAudit.countOccurrences(data, 0, 20)); // 2
        System.out.println(Q08_RecursiveAudit.isValid("Level", 0, 4));   // true
        System.out.println();

        // ===== sumValid 測試 =====
        System.out.println("===== sumValid 測試 =====");

        // 測試正常情況
        int[] data1 = {10, -1, 20, 101, 20};
        System.out.println("sumValid(data1, 0) → " + sumValid(data1, 0));   // 50
        System.out.println("sumValid(data1, 1) → " + sumValid(data1, 1));   // 40 (20 + 20)
        System.out.println("sumValid(data1, 2) → " + sumValid(data1, 2));   // 40 (20 + 20)
        System.out.println("sumValid(data1, 3) → " + sumValid(data1, 3));   // 20 (20)
        System.out.println("sumValid(data1, 4) → " + sumValid(data1, 4));   // 20
        System.out.println("sumValid(data1, 5) → " + sumValid(data1, 5));   // 0（超出範圍）

        // 測試邊界值
        int[] data2 = {0, 50, 100, 101, -1, -5};
        System.out.println("sumValid(data2, 0) → " + sumValid(data2, 0));   // 150 (0+50+100)

        // 測試負數索引
        System.out.println("sumValid(data1, -1) → " + sumValid(data1, -1));  // 50（從 0 開始）

        // 測試 null
        System.out.println("sumValid(null, 0) → " + sumValid(null, 0));      // 0
        System.out.println();

        // ===== countOccurrences 測試 =====
        System.out.println("===== countOccurrences 測試 =====");

        int[] data3 = {5, 10, 5, 20, 5, 30, 5, 40};
        System.out.println("countOccurrences(data3, 0, 5) → " + countOccurrences(data3, 0, 5));   // 4
        System.out.println("countOccurrences(data3, 0, 10) → " + countOccurrences(data3, 0, 10)); // 1
        System.out.println("countOccurrences(data3, 0, 99) → " + countOccurrences(data3, 0, 99)); // 0
        System.out.println("countOccurrences(data3, 2, 5) → " + countOccurrences(data3, 2, 5));   // 3（從索引 2 開始）
        System.out.println("countOccurrences(data3, 5, 5) → " + countOccurrences(data3, 5, 5));   // 2（從索引 5 開始）
        System.out.println("countOccurrences(data3, -1, 5) → " + countOccurrences(data3, -1, 5));  // 4（從 0 開始）

        // 測試 null
        System.out.println("countOccurrences(null, 0, 5) → " + countOccurrences(null, 0, 5));     // 0

        // 測試空陣列
        int[] data4 = {};
        System.out.println("countOccurrences(data4, 0, 5) → " + countOccurrences(data4, 0, 5));   // 0
        System.out.println();

        // ===== isValid（回文）測試 =====
        System.out.println("===== isValid 回文測試 =====");

        // 測試回文（忽略大小寫）
        System.out.println("isValid(\"Level\") → " + isValid("Level", 0, 4));  // true
        System.out.println("isValid(\"Radar\") → " + isValid("Radar", 0, 4));  // true
        System.out.println("isValid(\"racecar\") → " + isValid("racecar", 0, 6)); // true
        System.out.println("isValid(\"A man a plan a canal Panama\") → " + 
                           isValid("A man a plan a canal Panama", 0, 24));  // false（包含空格）
        System.out.println("isValid(\"able was I ere I saw elba\") → " + 
                           isValid("able was I ere I saw elba", 0, 24));   // false（包含空格）

        // 測試非回文
        System.out.println("isValid(\"Hello\") → " + isValid("Hello", 0, 4));   // false
        System.out.println("isValid(\"Java\") → " + isValid("Java", 0, 3));     // false
        System.out.println("isValid(\"abc\") → " + isValid("abc", 0, 2));       // false

        // 測試邊界情況
        System.out.println("isValid(\"a\") → " + isValid("a", 0, 0));           // true
        System.out.println("isValid(\"\") → " + isValid("", 0, -1));            // true（left >= right）
        System.out.println("isValid(null) → " + isValid(null, 0, 0));          // false
        System.out.println();

        // 測試多載方法
        System.out.println("===== isValid 多載測試 =====");
        System.out.println("isValid(\"Level\") → " + isValid("Level"));         // true
        System.out.println("isValid(\"radar\") → " + isValid("radar"));         // true
        System.out.println("isValid(\"hello\") → " + isValid("hello"));         // false
        System.out.println("isValid(null) → " + isValid((String) null));        // false
        System.out.println();

        // ===== 綜合測試 =====
        System.out.println("===== 綜合測試 =====");
        int[] data5 = {15, 30, 45, 60, 75, 90, 105, 120};
        System.out.println("sumValid(data5, 0) → " + sumValid(data5, 0));     // 15+30+45+60+75+90 = 315
        System.out.println("countOccurrences(data5, 0, 30) → " + countOccurrences(data5, 0, 30)); // 1
        System.out.println("countOccurrences(data5, 0, 100) → " + countOccurrences(data5, 0, 100)); // 0
        System.out.println("isValid(\"Never odd or even\") → " + 
                           isValid("Never odd or even", 0, 16));  // false（包含空格）
        System.out.println("isValid(\"NeverOddOrEven\") → " + 
                           isValid("NeverOddOrEven", 0, 13));     // true
    }
}