/**
 * 課後作業一：迴回字符串工具
 * 指定檔名：RecursiveTextTools.java
 * 
 * 完成 reverse、isPalindrome 與 countCharacter。
 * 回文忽略英文大小寫與空白，測試空、單一字元、Level 與一般字串。
 */
public class RecursiveTextTools {

    /**
     * 遞迴反轉字串
     * @param str 輸入字串
     * @return 反轉後的字串
     */
    public static String reverse(String str) {
        // 基本情況：空字串或單一字元
        if (str == null || str.length() <= 1) {
            return str;
        }
        // 遞迴：最後一個字元 + 反轉其餘部分
        return str.charAt(str.length() - 1) + reverse(str.substring(0, str.length() - 1));
    }

    /**
     * 檢查字串是否為回文（忽略英文大小寫與空白）
     * @param str 輸入字串
     * @return 若為回文則回傳 true，否則回傳 false
     */
    public static boolean isPalindrome(String str) {
        // 處理 null 或空字串
        if (str == null || str.isEmpty()) {
            return true;
        }
        // 移除空白並轉為小寫進行比較
        String cleaned = str.replaceAll("\\s+", "").toLowerCase();
        return isPalindromeHelper(cleaned, 0, cleaned.length() - 1);
    }

    /**
     * 遞迴輔助方法：檢查回文
     * @param str 已清理的字串（無空白、小寫）
     * @param left 左指標
     * @param right 右指標
     * @return 若為回文則回傳 true
     */
    private static boolean isPalindromeHelper(String str, int left, int right) {
        // 基本情況：指標交錯或相同
        if (left >= right) {
            return true;
        }
        // 檢查兩端字元是否相同
        if (str.charAt(left) != str.charAt(right)) {
            return false;
        }
        // 遞迴檢查內層
        return isPalindromeHelper(str, left + 1, right - 1);
    }

    /**
     * 計算指定字元在字串中出現的次數（遞迴版本）
     * @param str 輸入字串
     * @param ch 要計算的字元
     * @return 字元出現次數
     */
    public static int countCharacter(String str, char ch) {
        // 處理 null 或空字串
        if (str == null || str.isEmpty()) {
            return 0;
        }
        return countCharacterHelper(str, ch, 0);
    }

    /**
     * 遞迴輔助方法：計算字元出現次數
     * @param str 輸入字串
     * @param ch 要計算的字元
     * @param index 當前索引
     * @return 字元出現次數
     */
    private static int countCharacterHelper(String str, char ch, int index) {
        // 基本情況：已到達字串末尾
        if (index >= str.length()) {
            return 0;
        }
        // 檢查當前字元是否匹配，加上剩餘部分的結果
        int count = (str.charAt(index) == ch) ? 1 : 0;
        return count + countCharacterHelper(str, ch, index + 1);
    }

    /**
     * 計算指定字元在字串中出現的次數（忽略大小寫版本）
     * @param str 輸入字串
     * @param ch 要計算的字元
     * @param ignoreCase 是否忽略大小寫
     * @return 字元出現次數
     */
    public static int countCharacter(String str, char ch, boolean ignoreCase) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        if (ignoreCase) {
            char lowerCh = Character.toLowerCase(ch);
            return countCharacterHelperIgnoreCase(str, lowerCh, 0);
        } else {
            return countCharacter(str, ch);
        }
    }

    /**
     * 遞迴輔助方法：計算字元出現次數（忽略大小寫）
     */
    private static int countCharacterHelperIgnoreCase(String str, char ch, int index) {
        if (index >= str.length()) {
            return 0;
        }
        char currentChar = Character.toLowerCase(str.charAt(index));
        int count = (currentChar == ch) ? 1 : 0;
        return count + countCharacterHelperIgnoreCase(str, ch, index + 1);
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        System.out.println("===== RecursiveTextTools 測試 =====");
        System.out.println();

        // ===== 測試 reverse 方法 =====
        System.out.println("===== reverse 方法測試 =====");
        String[] reverseTests = {"Hello", "Java", "Recursive", "A", "", "12345"};
        for (String test : reverseTests) {
            System.out.println("reverse(\"" + test + "\") = \"" + reverse(test) + "\"");
        }
        System.out.println();

        // ===== 測試 isPalindrome 方法 =====
        System.out.println("===== isPalindrome 方法測試 =====");
        String[] palindromeTests = {
            "",                    // 空字串
            "A",                   // 單一字元
            "Level",               // 忽略大小寫
            "level",               // 小寫
            "racecar",             // 一般回文
            "never odd or even",   // 包含空白
            "A man a plan a canal Panama", // 經典回文（忽略空白和大小寫）
            "hello",               // 非回文
            "abca",                // 非回文
            "Madam",               // 忽略大小寫
            "Was it a car or a cat I saw", // 經典回文
            "No lemon no melon"    // 回文
        };
        
        for (String test : palindromeTests) {
            System.out.println("isPalindrome(\"" + test + "\") = " + isPalindrome(test));
        }
        System.out.println();

        // ===== 測試 countCharacter 方法 =====
        System.out.println("===== countCharacter 方法測試 =====");
        String testStr = "Hello World! Hello Java!";
        char[] charsToCount = {'l', 'o', 'H', '!', 'x', ' '};
        
        System.out.println("字串: \"" + testStr + "\"");
        for (char ch : charsToCount) {
            System.out.println("  字元 '" + ch + "' 出現 " + countCharacter(testStr, ch) + " 次");
        }
        System.out.println();

        // ===== 測試 countCharacter（忽略大小寫） =====
        System.out.println("===== countCharacter（忽略大小寫）測試 =====");
        String testStr2 = "Hello World! Hello Java!";
        System.out.println("字串: \"" + testStr2 + "\"");
        System.out.println("  字元 'h' 出現 " + countCharacter(testStr2, 'h', true) + " 次 (忽略大小寫)");
        System.out.println("  字元 'H' 出現 " + countCharacter(testStr2, 'H', true) + " 次 (忽略大小寫)");
        System.out.println("  字元 'l' 出現 " + countCharacter(testStr2, 'l', true) + " 次 (忽略大小寫)");
        System.out.println("  字元 'W' 出現 " + countCharacter(testStr2, 'W', true) + " 次 (忽略大小寫)");
        System.out.println();

        // ===== 邊界條件測試 =====
        System.out.println("===== 邊界條件測試 =====");
        System.out.println("reverse(null) = " + reverse(null));
        System.out.println("isPalindrome(null) = " + isPalindrome(null));
        System.out.println("countCharacter(null, 'a') = " + countCharacter(null, 'a'));
        System.out.println("countCharacter(\"\", 'a') = " + countCharacter("", 'a'));
        System.out.println();

        // ===== 詳細回文測試 =====
        System.out.println("===== 詳細回文測試 =====");
        System.out.println("isPalindrome(\"Level\") = " + isPalindrome("Level") + " (忽略大小寫)");
        System.out.println("isPalindrome(\"level\") = " + isPalindrome("level"));
        System.out.println("isPalindrome(\"A\") = " + isPalindrome("A"));
        System.out.println("isPalindrome(\"\") = " + isPalindrome(""));
        System.out.println("isPalindrome(\"  \") = " + isPalindrome("  ") + " (僅空白)");
        System.out.println("isPalindrome(\"Never odd or even\") = " + isPalindrome("Never odd or even"));
        System.out.println("isPalindrome(\"not palindrome\") = " + isPalindrome("not palindrome"));
        System.out.println();

        // ===== 遞迴過程展示 =====
        System.out.println("===== 遞迴過程展示 =====");
        String demoStr = "Hello";
        System.out.println("reverse(\"" + demoStr + "\") 的遞迴過程:");
        System.out.println("  reverse(\"Hello\") = 'o' + reverse(\"Hell\")");
        System.out.println("  reverse(\"Hell\") = 'l' + reverse(\"Hel\")");
        System.out.println("  reverse(\"Hel\") = 'l' + reverse(\"He\")");
        System.out.println("  reverse(\"He\") = 'e' + reverse(\"H\")");
        System.out.println("  reverse(\"H\") = \"H\"");
        System.out.println("  結果: " + reverse(demoStr));
    }
}