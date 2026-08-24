import java.util.*;

/**
 * 安全清理名單系統
 * 清理包含重複、空白與 null 資料的註冊名單
 */
public class EnrollmentCleanup {

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              安全清理名單系統 - 註冊清理報告            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        // 建立包含重複、空白與 null 資料的 List<String>
        List<String> enrollmentList = createEnrollmentList();

        // ========== 清理前報告 ==========
        System.out.println("\n【清理前 - 原始註冊名單】");
        System.out.println("─".repeat(60));
        printDetailedList(enrollmentList, "原始名單");
        printStatistics(enrollmentList, "清理前");

        // 找出重複名稱（清理前）
        Set<String> duplicatesBefore = findDuplicates(enrollmentList);
        System.out.println("\n【清理前 - 重複名稱報告】");
        if (duplicatesBefore.isEmpty()) {
            System.out.println("  ✅ 沒有發現重複名稱");
        } else {
            System.out.println("  ⚠️ 發現 " + duplicatesBefore.size() + " 個重複名稱：");
            for (String name : duplicatesBefore) {
                int count = countOccurrences(enrollmentList, name);
                System.out.printf("    • '%s' 出現 %d 次%n", name, count);
            }
        }

        // ========== 執行清理 ==========
        System.out.println("\n【執行清理作業】");
        System.out.println("  1. 刪除 null 資料");
        System.out.println("  2. 刪除空白字串（包含僅含空白的字串）");
        System.out.println("  3. 移除重複名稱（保留第一次出現）");
        System.out.println("─".repeat(60));

        // 使用迭代器刪除不合法數據
        List<String> cleanedList = cleanEnrollmentList(enrollmentList);

        // ========== 清理後報告 ==========
        System.out.println("\n【清理後 - 乾淨註冊名單】");
        printDetailedList(cleanedList, "清理後名單");
        printStatistics(cleanedList, "清理後");

        // ========== 重複報告（清理後） ==========
        Set<String> duplicatesAfter = findDuplicates(cleanedList);
        System.out.println("\n【清理後 - 重複名稱報告】");
        if (duplicatesAfter.isEmpty()) {
            System.out.println("  ✅ 沒有發現重複名稱（清理成功！）");
        } else {
            System.out.println("  ⚠️ 仍有 " + duplicatesAfter.size() + " 個重複名稱：");
            for (String name : duplicatesAfter) {
                int count = countOccurrences(cleanedList, name);
                System.out.printf("    • '%s' 出現 %d 次%n", name, count);
            }
        }

        // ========== 清理前後對比 ==========
        System.out.println("\n【清理前後對比】");
        printComparisonTable(enrollmentList, cleanedList);

        // ========== 清理詳細過程記錄 ==========
        System.out.println("\n【清理過程詳細記錄】");
        printCleanupProcess();

        // ========== 方法功能總結 ==========
        printSummaryTable();
    }

    /**
     * 建立測試用註冊名單（包含重複、空白與 null）
     * 
     * @return 測試用的註冊名單
     */
    private static List<String> createEnrollmentList() {
        List<String> list = new ArrayList<>();
        list.add("王小明");
        list.add("李小華");
        list.add("張小美");
        list.add("王小明");      // 重複
        list.add("陳大文");
        list.add("");            // 空白
        list.add("李小華");      // 重複
        list.add("   ");         // 僅含空白
        list.add(null);          // null
        list.add("趙小強");
        list.add("張小美");      // 重複
        list.add("林大偉");
        list.add("王小明");      // 重複（第三次）
        list.add(" 陳小安 ");    // 前後有空白（應視為有效，但需要 trim）
        list.add(null);          // null
        list.add("劉小華");
        list.add("");            // 空白
        list.add("  林小芳  ");  // 前後有空白
        list.add("陳大文");      // 重複
        return list;
    }

    /**
     * 清理註冊名單
     * 使用迭代器刪除 null、空白字串和重複名稱
     * 
     * @param originalList 原始名單
     * @return 清理後的名單（新列表）
     */
    private static List<String> cleanEnrollmentList(List<String> originalList) {
        // 建立副本以保護原始資料
        List<String> result = new ArrayList<>(originalList);
        Set<String> seenNames = new HashSet<>();

        // 使用迭代器遍歷
        Iterator<String> iterator = result.iterator();
        int removedNull = 0;
        int removedBlank = 0;
        int removedDuplicate = 0;

        while (iterator.hasNext()) {
            String name = iterator.next();
            boolean shouldRemove = false;
            String reason = "";

            // 1. 檢查是否為 null
            if (name == null) {
                shouldRemove = true;
                reason = "null";
                removedNull++;
            }
            // 2. 檢查是否為空白字串（或僅含空白）
            else if (name.trim().isEmpty()) {
                shouldRemove = true;
                reason = "空白字串";
                removedBlank++;
            }
            // 3. 檢查是否為重複（忽略前後空白後比較）
            else {
                String trimmedName = name.trim();
                if (seenNames.contains(trimmedName)) {
                    shouldRemove = true;
                    reason = "重複名稱";
                    removedDuplicate++;
                } else {
                    // 第一次看到，加入 seenNames（儲存 trim 後的值）
                    seenNames.add(trimmedName);
                    // 將目前元素更新為 trim 後的值（標準化）
                    // 注意：這裡無法直接修改，需要在迭代器外處理
                }
            }

            if (shouldRemove) {
                iterator.remove();
                System.out.printf("  🗑️  移除：'%s'（原因：%s）%n", 
                        name == null ? "null" : name, reason);
            }
        }

        // 標準化：將所有名稱去除前後空白
        for (int i = 0; i < result.size(); i++) {
            String name = result.get(i);
            if (name != null) {
                result.set(i, name.trim());
            }
        }

        System.out.println("─".repeat(60));
        System.out.printf("  📊 清理統計：移除 null %d 筆，空白 %d 筆，重複 %d 筆%n",
                removedNull, removedBlank, removedDuplicate);
        System.out.printf("  📊 最終保留：%d 筆有效資料%n", result.size());

        return result;
    }

    /**
     * 找出列表中的重複名稱
     * 
     * @param list 要檢查的列表
     * @return 重複名稱的集合
     */
    private static Set<String> findDuplicates(List<String> list) {
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();

        for (String item : list) {
            if (item == null || item.trim().isEmpty()) {
                continue;
            }
            String name = item.trim();
            if (seen.contains(name)) {
                duplicates.add(name);
            } else {
                seen.add(name);
            }
        }
        return duplicates;
    }

    /**
     * 計算某個名稱在列表中出現的次數
     * 
     * @param list 要統計的列表
     * @param name 要查找的名稱
     * @return 出現次數
     */
    private static int countOccurrences(List<String> list, String name) {
        int count = 0;
        for (String item : list) {
            if (item != null && item.trim().equals(name)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 印出詳細列表（含索引）
     * 
     * @param list 要輸出的列表
     * @param title 標題
     */
    private static void printDetailedList(List<String> list, String title) {
        if (list == null || list.isEmpty()) {
            System.out.println("  📭 " + title + " 為空");
            return;
        }

        System.out.println("  📋 " + title + "（共 " + list.size() + " 筆）：");
        System.out.println("  ┌────┬──────────────────────────────────────────────┐");
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            String display = (item == null) ? "null" : 
                            (item.isEmpty() ? "（空白）" : 
                            (item.trim().isEmpty() ? "（僅含空白）" : item));
            System.out.printf("  │ %2d │ %-44s │%n", i, display);
        }
        System.out.println("  └────┴──────────────────────────────────────────────┘");
    }

    /**
     * 印出統計資訊
     * 
     * @param list 要統計的列表
     * @param period 期間（清理前/清理後）
     */
    private static void printStatistics(List<String> list, String period) {
        if (list == null) {
            System.out.println("  📊 " + period + "統計：列表為 null");
            return;
        }

        int total = list.size();
        int nullCount = 0;
        int blankCount = 0;
        int validCount = 0;

        for (String item : list) {
            if (item == null) {
                nullCount++;
            } else if (item.trim().isEmpty()) {
                blankCount++;
            } else {
                validCount++;
            }
        }

        System.out.println("\n【" + period + "統計資訊】");
        System.out.printf("  總筆數： %d%n", total);
        System.out.printf("  有效資料： %d 筆（%.1f%%）%n", validCount, 
                total > 0 ? (double) validCount / total * 100 : 0);
        System.out.printf("  null 資料： %d 筆（%.1f%%）%n", nullCount,
                total > 0 ? (double) nullCount / total * 100 : 0);
        System.out.printf("  空白資料： %d 筆（%.1f%%）%n", blankCount,
                total > 0 ? (double) blankCount / total * 100 : 0);
    }

    /**
     * 印出清理前後對比表
     * 
     * @param before 清理前列表
     * @param after 清理後列表
     */
    private static void printComparisonTable(List<String> before, List<String> after) {
        System.out.println("  ┌──────────────┬────────────┬────────────┬────────────┐");
        System.out.println("  │  項目        │  清理前    │  清理後    │  增減      │");
        System.out.println("  ├──────────────┼────────────┼────────────┼────────────┤");
        
        int beforeTotal = before.size();
        int afterTotal = after.size();
        System.out.printf("  │  總筆數      │  %8d  │  %8d  │  %+8d  │%n",
                beforeTotal, afterTotal, afterTotal - beforeTotal);

        int beforeValid = countValid(before);
        int afterValid = countValid(after);
        System.out.printf("  │  有效資料    │  %8d  │  %8d  │  %+8d  │%n",
                beforeValid, afterValid, afterValid - beforeValid);

        int beforeNull = countNull(before);
        int afterNull = countNull(after);
        System.out.printf("  │  null 資料   │  %8d  │  %8d  │  %+8d  │%n",
                beforeNull, afterNull, afterNull - beforeNull);

        int beforeBlank = countBlank(before);
        int afterBlank = countBlank(after);
        System.out.printf("  │  空白資料    │  %8d  │  %8d  │  %+8d  │%n",
                beforeBlank, afterBlank, afterBlank - beforeBlank);

        int beforeDuplicate = findDuplicates(before).size();
        int afterDuplicate = findDuplicates(after).size();
        System.out.printf("  │  重複名稱    │  %8d  │  %8d  │  %+8d  │%n",
                beforeDuplicate, afterDuplicate, afterDuplicate - beforeDuplicate);

        System.out.println("  └──────────────┴────────────┴────────────┴────────────┘");
    }

    /**
     * 計算有效資料數量（非 null 且非空白）
     */
    private static int countValid(List<String> list) {
        int count = 0;
        for (String item : list) {
            if (item != null && !item.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 計算 null 資料數量
     */
    private static int countNull(List<String> list) {
        int count = 0;
        for (String item : list) {
            if (item == null) {
                count++;
            }
        }
        return count;
    }

    /**
     * 計算空白資料數量（包含空字串和僅含空白的字串）
     */
    private static int countBlank(List<String> list) {
        int count = 0;
        for (String item : list) {
            if (item != null && item.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 印出清理過程詳細記錄
     */
    private static void printCleanupProcess() {
        System.out.println("  ┌────────────────────────────────────────────────────────┐");
        System.out.println("  │  步驟 1：使用迭代器遍歷列表                          │");
        System.out.println("  │  步驟 2：檢查每個元素                                │");
        System.out.println("  │    ├─ 檢查是否為 null → 刪除                        │");
        System.out.println("  │    ├─ 檢查是否為空白字串 → 刪除                     │");
        System.out.println("  │    └─ 檢查是否已存在於 Set 中 → 刪除               │");
        System.out.println("  │  步驟 3：標準化名稱（去除前後空白）                  │");
        System.out.println("  │  步驟 4：返回清理後的列表                            │");
        System.out.println("  └────────────────────────────────────────────────────────┘");
        System.out.println("\n  💡 使用 Set<String> 儲存已看過的名稱，實現 O(1) 查詢");
        System.out.println("  💡 使用 Iterator.remove() 安全刪除，避免 ConcurrentModificationException");
    }

    /**
     * 印出方法功能總結
     */
    private static void printSummaryTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              安全清理名單 - 方法功能總結                 ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  方法名稱          │  功能說明                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  findDuplicates()  │  使用 Set 找出重複名稱              ║");
        System.out.println("║  cleanEnrollmentList()│  使用 Iterator 清理不合法資料    ║");
        System.out.println("║  countOccurrences()│  計算特定名稱出現次數               ║");
        System.out.println("║  countValid()      │  計算有效資料數量                   ║");
        System.out.println("║  countNull()       │  計算 null 資料數量                 ║");
        System.out.println("║  countBlank()      │  計算空白資料數量                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println("\n【清理規則】");
        System.out.println("  ❌ 刪除 null 資料");
        System.out.println("  ❌ 刪除空白字串（空字串或僅含空白）");
        System.out.println("  ❌ 刪除重複名稱（保留第一次出現）");
        System.out.println("  ✅ 保留有效名稱（自動去除前後空白）");
    }
}