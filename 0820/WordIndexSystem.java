import java.util.*;

/**
 * 文字索引系統
 * 統計文章中的單字出現次數，並找出出現至少兩次的單字
 */
public class WordIndexSystem {

    public static void main(String[] args) {
        // 程式內建句子陣列
        String[] sentences = {
            "Java is a programming language.",
            "Java is widely used for building enterprise applications.",
            "The Java language is object-oriented and platform-independent.",
            "Many developers love Java because it is versatile and powerful.",
            "Java, Python, and C++ are popular programming languages.",
            "Learning Java can open many career opportunities."
        };

        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║             文字索引系統 - 單字統計報告                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        // 顯示原始文章
        System.out.println("\n【原始文章內容】");
        System.out.println("─".repeat(60));
        for (int i = 0; i < sentences.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, sentences[i]);
        }
        System.out.println("─".repeat(60));

        // 建立資料結構
        Map<String, Integer> wordCountMap = new HashMap<>();
        Set<String> uniqueWordsSet = new HashSet<>();
        List<String> allWordsList = new ArrayList<>();

        // 處理每一句
        for (String sentence : sentences) {
            // 1. 清除標點符號並轉為小寫
            String cleaned = cleanText(sentence);
            
            // 2. 分割成單字陣列
            String[] words = cleaned.split("\\s+");
            
            // 3. 處理每個單字
            for (String word : words) {
                if (word.isEmpty()) continue;
                
                // 加入 List（保存所有單字）
                allWordsList.add(word);
                
                // 加入 Set（不重複單字）
                uniqueWordsSet.add(word);
                
                // 統計次數（Map）
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }

        // ========== 輸出統計結果 ==========

        // 1. 統計摘要
        System.out.println("\n【統計摘要】");
        System.out.printf("  總單字數（含重複）： %d 個%n", allWordsList.size());
        System.out.printf("  不重複單字數：       %d 個%n", uniqueWordsSet.size());
        System.out.printf("  出現至少兩次的單字： %d 個%n", 
                countWordsWithFrequencyAtLeast(wordCountMap, 2));

        // 2. 所有不重複單字（Set）
        System.out.println("\n【不重複單字列表（Set<String>）】");
        System.out.println("  用途：快速查詢某個單字是否存在於文章中");
        List<String> sortedUnique = new ArrayList<>(uniqueWordsSet);
        Collections.sort(sortedUnique);
        System.out.println("  共 " + sortedUnique.size() + " 個單字：");
        printInColumns(sortedUnique, 6);

        // 3. 單字次數統計（Map）
        System.out.println("\n【單字出現次數統計（Map<String, Integer>）】");
        System.out.println("  用途：分析每個單字在文章中出現的頻率");
        System.out.println("  依出現次數排序（從高到低）：");
        
        // 排序輸出
        List<Map.Entry<String, Integer>> sortedEntries = 
            new ArrayList<>(wordCountMap.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        
        System.out.println("  ┌─────────────┬────────┬────────────────────┐");
        System.out.println("  │  單字        │  次數  │  長條圖            │");
        System.out.println("  ├─────────────┼────────┼────────────────────┤");
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String word = entry.getKey();
            int count = entry.getValue();
            String bar = "█".repeat(Math.min(count, 10));
            System.out.printf("  │  %-11s │  %3d   │  %-18s │%n", word, count, bar);
        }
        System.out.println("  └─────────────┴────────┴────────────────────┘");

        // 4. 出現至少兩次的單字（符合題目要求）
        System.out.println("\n【出現至少兩次的單字（頻率 ≥ 2）】");
        System.out.println("  篩選條件：出現次數 >= 2");
        
        List<String> frequentWords = getWordsWithFrequencyAtLeast(wordCountMap, 2);
        if (frequentWords.isEmpty()) {
            System.out.println("  ⚠️ 沒有出現至少兩次的單字");
        } else {
            // 依出現次數排序顯示
            System.out.println("  ┌─────────────┬────────┐");
            System.out.println("  │  單字        │  次數  │");
            System.out.println("  ├─────────────┼────────┤");
            for (String word : frequentWords) {
                int count = wordCountMap.get(word);
                System.out.printf("  │  %-11s │  %3d   │%n", word, count);
            }
            System.out.println("  └─────────────┴────────┘");
            System.out.printf("  ✅ 共 %d 個單字出現至少兩次%n", frequentWords.size());
        }

        // 5. Set 快速查詢示範
        System.out.println("\n【Set 快速查詢示範】");
        String[] testWords = {"java", "python", "javascript", "language"};
        for (String testWord : testWords) {
            boolean exists = uniqueWordsSet.contains(testWord);
            System.out.printf("  '%s' 是否存在於文章中？ %s%n", 
                    testWord, exists ? "✅ 是" : "❌ 否");
        }

        // 6. 所有單字的詳細索引（按字母排序）
        System.out.println("\n【完整單字索引（依字母順序）】");
        System.out.println("  顯示每個單字的出現次數");
        System.out.println("  ┌─────────────┬────────┐");
        System.out.println("  │  單字        │  次數  │");
        System.out.println("  ├─────────────┼────────┤");
        for (String word : sortedUnique) {
            int count = wordCountMap.get(word);
            System.out.printf("  │  %-11s │  %3d   │%n", word, count);
        }
        System.out.println("  └─────────────┴────────┘");

        // 7. 方法功能總結
        printSummaryTable();
    }

    /**
     * 清理文字：移除標點符號並轉為小寫
     * 忽略句點（.）和逗號（,）
     * 
     * @param text 原始文字
     * @return 清理後的文字
     */
    private static String cleanText(String text) {
        // 移除句點和逗號
        String cleaned = text.replaceAll("[.,]", "");
        // 轉為小寫
        return cleaned.toLowerCase();
    }

    /**
     * 計算出現次數至少為指定門檻的單字數量
     * 
     * @param wordCountMap 單字統計 Map
     * @param threshold 門檻值
     * @return 符合條件的單字數量
     */
    private static int countWordsWithFrequencyAtLeast(
            Map<String, Integer> wordCountMap, int threshold) {
        int count = 0;
        for (int value : wordCountMap.values()) {
            if (value >= threshold) {
                count++;
            }
        }
        return count;
    }

    /**
     * 取得出現次數至少為指定門檻的單字列表
     * 
     * @param wordCountMap 單字統計 Map
     * @param threshold 門檻值
     * @return 符合條件的單字列表（依出現次數降冪排序）
     */
    private static List<String> getWordsWithFrequencyAtLeast(
            Map<String, Integer> wordCountMap, int threshold) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            if (entry.getValue() >= threshold) {
                result.add(entry.getKey());
            }
        }
        // 依出現次數降冪排序
        result.sort((w1, w2) -> 
            wordCountMap.get(w2).compareTo(wordCountMap.get(w1)));
        return result;
    }

    /**
     * 將列表以欄位方式輸出
     * 
     * @param list 要輸出的列表
     * @param columns 欄位數
     */
    private static void printInColumns(List<String> list, int columns) {
        int rows = (int) Math.ceil((double) list.size() / columns);
        for (int row = 0; row < rows; row++) {
            System.out.print("    ");
            for (int col = 0; col < columns; col++) {
                int index = row + col * rows;
                if (index < list.size()) {
                    System.out.printf("%-14s ", list.get(index));
                }
            }
            System.out.println();
        }
    }

    /**
     * 印出方法功能總結
     */
    private static void printSummaryTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              文字索引系統 - 功能總結                     ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  資料結構    │  用途                     │  特點         ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  List<String>│  保存所有單字（含重複）   │  保留順序     ║");
        System.out.println("║  Set<String> │  儲存不重複單字           │  自動去重     ║");
        System.out.println("║  Map<String, │  統計每個單字出現次數     │  鍵值對映射   ║");
        System.out.println("║  Integer>    │                           │               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println("\n【處理流程】");
        System.out.println("  1. 讀取句子陣列");
        System.out.println("  2. 移除標點符號（. 和 ,）");
        System.out.println("  3. 轉換為小寫（忽略大小寫）");
        System.out.println("  4. 分割成單字陣列");
        System.out.println("  5. 分別存入 List、Set、Map");
        System.out.println("  6. 輸出統計報告");
    }
}