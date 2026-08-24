import java.util.*;

/**
 * 課程標籤統計報告
 * 展示 List、Set、Map 三種集合的實際應用
 */
public class CourseTagReport {

    public static void main(String[] args) {
        // 輸入一組可能重複的課程標籤
        String[] rawTags = {
            "Java程式設計", "Python入門", "Java程式設計", 
            "資料結構", "演算法", "Python入門",
            "網頁開發", "Java程式設計", "資料庫系統",
            "演算法", "作業系統", "Python入門"
        };

        System.out.println("========== 課程標籤統計報告 ==========");
        System.out.println("原始資料筆數: " + rawTags.length);
        System.out.println();

        // 建立三種集合
        List<String> tagList = new ArrayList<>();
        Set<String> tagSet = new HashSet<>();
        Map<String, Integer> tagCountMap = new HashMap<>();

        // 處理每一筆標籤資料
        for (String tag : rawTags) {
            // 1. List：保存原始順序（允許重複）
            tagList.add(tag);

            // 2. Set：儲存不重複標籤（自動去重）
            tagSet.add(tag);

            // 3. Map：統計次數
            tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
        }

        // ========== 輸出結果 ==========

        // 1. 顯示 List（原始順序）
        System.out.println("【List<String>】- 保存原始順序");
        System.out.println("用途：保留輸入順序，允許重複，適合需要依序處理的場景");
        System.out.println("內容：" + tagList);
        System.out.println("大小：" + tagList.size() + " 筆");
        System.out.println();

        // 2. 顯示 Set（不重複標籤）
        System.out.println("【Set<String>】- 儲存不重複標籤");
        System.out.println("用途：自動去重，快速檢查是否存在，適合需要唯一值的場景");
        System.out.println("內容：" + tagSet);
        System.out.println("大小：" + tagSet.size() + " 個獨特標籤");
        System.out.println();

        // 3. 顯示 Map（統計次數）
        System.out.println("【Map<String, Integer>】- 統計次數");
        System.out.println("用途：記錄每個標籤出現的次數，適合需要計數或分組的場景");
        System.out.println("統計結果：");

        // 依出現次數排序輸出（從高到低）
        List<Map.Entry<String, Integer>> sortedEntries = 
            new ArrayList<>(tagCountMap.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String tag = entry.getKey();
            int count = entry.getValue();
            // 用長條圖視覺化
            String bar = "█".repeat(Math.min(count, 10));
            System.out.printf("  %-12s : %d 次 %s%n", tag, count, bar);
        }
        System.out.println();

        // ========== 集合用途對比總結 ==========
        System.out.println("【三種集合的用途對比】");
        System.out.println("┌─────────────┬──────────────┬─────────────┬──────────────┐");
        System.out.println("│  集合類型    │  是否有序     │  是否允許重複 │  主要用途     │");
        System.out.println("├─────────────┼──────────────┼─────────────┼──────────────┤");
        System.out.println("│  List       │  是（索引順序）│  是         │  保持順序     │");
        System.out.println("│  Set        │  否（無序）   │  否         │  去除重複     │");
        System.out.println("│  Map        │  否（鍵值對） │  鍵不重複   │  計數/分組    │");
        System.out.println("└─────────────┴──────────────┴─────────────┴──────────────┘");

        // ========== 額外示範：Set 的快速查詢 ==========
        System.out.println();
        System.out.println("【Set 快速查詢示範】");
        String searchTag = "演算法";
        System.out.println("是否存在標籤 '" + searchTag + "'？" + 
            (tagSet.contains(searchTag) ? "✅ 是" : "❌ 否"));

        searchTag = "機器學習";
        System.out.println("是否存在標籤 '" + searchTag + "'？" + 
            (tagSet.contains(searchTag) ? "✅ 是" : "❌ 否"));

        // ========== 額外示範：Map 的資料提取 ==========
        System.out.println();
        System.out.println("【Map 資料提取示範】");
        String queryTag = "Java程式設計";
        System.out.println("標籤 '" + queryTag + "' 出現了 " + 
            tagCountMap.getOrDefault(queryTag, 0) + " 次");

        queryTag = "資料結構";
        System.out.println("標籤 '" + queryTag + "' 出現了 " + 
            tagCountMap.getOrDefault(queryTag, 0) + " 次");

        // ========== 實際應用情境 ==========
        System.out.println();
        System.out.println("【實際應用情境】");
        System.out.println("1. List：顯示課程標籤的原始輸入順序（例如：匯入記錄）");
        System.out.println("2. Set：產生課程標籤雲（不重複的標籤列表）");
        System.out.println("3. Map：分析熱門標籤排名（哪些標籤最受歡迎）");
    }
}