/**
 * 課堂實作題一：返回數位統計
 * 指定檔名：RecursiveDigitReport.java
 * 
 * 完成 digitsum、digitcount 與 countdigit。
 * 負數先轉為絕對值，digitCount(a) 回傳 1。
 * 使用 50205、0、-731 測試。
 * 核心計算不得使用循環或轉成字串。
 */
public class RecursiveDigitReport {

    /**
     * 計算數字的各位數字總和（遞迴版本）
     * @param n 輸入整數（可為負數）
     * @return 各位數字總和（絕對值後計算）
     */
    public static int digitSum(int n) {
        // 負數先轉為絕對值
        n = Math.abs(n);
        // 基本情況：如果 n 為 0，總和為 0
        if (n == 0) {
            return 0;
        }
        // 遞迴：取出最後一位數字，加上剩餘數字的總和
        return (n % 10) + digitSum(n / 10);
    }

    /**
     * 計算數字的位數（遞迴版本）
     * @param n 輸入整數（可為負數）
     * @return 位數（絕對值後計算，0 的位數定義為 1）
     */
    public static int digitCount(int n) {
        // 負數先轉為絕對值
        n = Math.abs(n);
        // 基本情況：如果 n 為 0，位數為 1（符合題目要求 digitCount(a) 回傳 1）
        if (n == 0) {
            return 1;
        }
        // 遞迴：每次除以 10，計算剩餘位數，最後加 1
        return 1 + digitCount(n / 10);
    }

    /**
     * 計算數字中特定數字（digit）出現的次數（遞迴版本）
     * @param n 輸入整數（可為負數）
     * @param digit 要統計的數字（0-9）
     * @return 該數字出現的次數
     */
    public static int countDigit(int n, int digit) {
        // 負數先轉為絕對值
        n = Math.abs(n);
        // 基本情況：如果 n 為 0，檢查是否為我們要找的數字（但要注意 0 的處理）
        // 這裡的特殊處理：當 n==0 時，若 digit==0 則算一次，否則為 0
        if (n == 0) {
            // 如果 n 本身就是 0，且 digit 也是 0，則算一次
            // 但因為我們在遞迴中會逐位處理，這個情況只有在最初傳入 0 時會發生
            // 為了避免重複計算，這裡直接判斷 digit 是否為 0
            return (digit == 0) ? 1 : 0;
        }
        // 取出最後一位數字
        int lastDigit = n % 10;
        // 遞迴：計算剩餘部分中該數字出現的次數，加上當前位是否相符
        return (lastDigit == digit ? 1 : 0) + countDigit(n / 10, digit);
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        // 測試數字
        int[] testNumbers = {50205, 0, -731};

        System.out.println("===== 數位統計測試 =====");
        for (int num : testNumbers) {
            System.out.println("數字: " + num);
            System.out.println("  數字總和 (digitSum): " + digitSum(num));
            System.out.println("  位數 (digitCount): " + digitCount(num));
            // 測試 countDigit 針對不同數字 0-9
            System.out.println("  各數字出現次數 (countDigit):");
            for (int d = 0; d <= 9; d++) {
                int count = countDigit(num, d);
                if (count > 0) {
                    System.out.println("    數字 " + d + " 出現 " + count + " 次");
                }
            }
            System.out.println();
        }

        // 額外測試：驗證 digitCount(0) 回傳 1
        System.out.println("===== 額外驗證 =====");
        System.out.println("digitCount(0) = " + digitCount(0) + " (預期 1)");
        // 驗證 countDigit 對 0 的處理（0 中數字 0 出現 1 次）
        System.out.println("countDigit(0, 0) = " + countDigit(0, 0) + " (預期 1)");
        System.out.println("countDigit(0, 5) = " + countDigit(0, 5) + " (預期 0)");
    }
}