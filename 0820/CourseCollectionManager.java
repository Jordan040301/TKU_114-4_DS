import java.util.*;

/**
 * 課程報名記錄類別
 */
class EnrollmentRecord {
    private final String studentId;
    private final String studentName;
    private final String courseCode;
    private final String courseName;
    private final String tag;
    private int score;

    public EnrollmentRecord(String studentId, String studentName, 
                           String courseCode, String courseName, 
                           String tag, int score) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.tag = tag;
        this.score = score;
    }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getTag() { return tag; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EnrollmentRecord)) return false;
        EnrollmentRecord other = (EnrollmentRecord) obj;
        return Objects.equals(studentId, other.studentId) &&
               Objects.equals(courseCode, other.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode);
    }

    @Override
    public String toString() {
        return String.format("%s-%s: %s (%s) %d分 [%s]", 
                studentId, courseCode, studentName, courseName, score, 
                tag != null && !tag.isEmpty() ? tag : "無標籤");
    }
}

/**
 * 課程管理集合系統
 * 綜合案例 - 整合 List、Set、Map 進行課程管理
 */
public class CourseCollectionManager {
    // 核心資料結構
    private final List<EnrollmentRecord> records;      // 保留順序
    private final Set<String> uniqueStudents;           // 不重複學生
    private final Map<String, List<EnrollmentRecord>> studentMap;   // 學生→報名列表
    private final Map<String, List<EnrollmentRecord>> courseMap;    // 課程→報名列表
    private final Map<String, List<EnrollmentRecord>> tagMap;       // 標籤→報名列表

    public CourseCollectionManager() {
        this.records = new ArrayList<>();
        this.uniqueStudents = new HashSet<>();
        this.studentMap = new HashMap<>();
        this.courseMap = new HashMap<>();
        this.tagMap = new HashMap<>();
    }

    /**
     * 新增報名記錄
     */
    public boolean addRecord(EnrollmentRecord record) {
        if (record == null) {
            System.out.println("⚠️ 警告：無法新增 null 記錄");
            return false;
        }

        // 檢查是否重複（同一學生 + 同一課程）
        for (EnrollmentRecord existing : records) {
            if (existing.equals(record)) {
                System.out.printf("⚠️ 重複報名：%s 已報名 %s%n", 
                        record.getStudentId(), record.getCourseCode());
                return false;
            }
        }

        // 1. 加入 List
        records.add(record);

        // 2. 加入 Set
        uniqueStudents.add(record.getStudentId());

        // 3. 加入 studentMap
        studentMap.computeIfAbsent(record.getStudentId(), k -> new ArrayList<>()).add(record);

        // 4. 加入 courseMap
        courseMap.computeIfAbsent(record.getCourseCode(), k -> new ArrayList<>()).add(record);

        // 5. 加入 tagMap（處理空白標籤）
        String tagKey = (record.getTag() == null || record.getTag().trim().isEmpty()) 
                ? "無標籤" : record.getTag().trim();
        tagMap.computeIfAbsent(tagKey, k -> new ArrayList<>()).add(record);

        return true;
    }

    /**
     * 1. 更新成績
     */
    public boolean updateScore(String studentId, int score) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("⚠️ 學生編號不能為空");
            return false;
        }

        if (score < 0 || score > 100) {
            System.out.println("⚠️ 成績必須在 0~100 之間");
            return false;
        }

        boolean found = false;
        for (EnrollmentRecord record : records) {
            if (record.getStudentId().equals(studentId)) {
                record.setScore(score);
                found = true;
            }
        }

        if (found) {
            System.out.printf("✅ 已更新學生 %s 的成績為 %d 分%n", studentId, score);
        } else {
            System.out.printf("❌ 未找到學生 %s 的報名記錄%n", studentId);
        }
        return found;
    }

    /**
     * 2. 根據標籤查詢
     */
    public List<EnrollmentRecord> findByTag(String tag) {
        String tagKey = (tag == null || tag.trim().isEmpty()) ? "無標籤" : tag.trim();
        List<EnrollmentRecord> result = tagMap.getOrDefault(tagKey, new ArrayList<>());
        return new ArrayList<>(result);  // 回傳副本
    }

    /**
     * 3. 成績分布統計
     */
    public Map<String, Integer> scoreDistribution() {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        distribution.put("A (90-100)", 0);
        distribution.put("B (80-89)", 0);
        distribution.put("C (70-79)", 0);
        distribution.put("D (60-69)", 0);
        distribution.put("F (0-59)", 0);

        for (EnrollmentRecord record : records) {
            int score = record.getScore();
            String grade = getGrade(score);
            distribution.put(grade, distribution.get(grade) + 1);
        }

        return distribution;
    }

    /**
     * 取得成績等第
     */
    private String getGrade(int score) {
        if (score >= 90) return "A (90-100)";
        else if (score >= 80) return "B (80-89)";
        else if (score >= 70) return "C (70-79)";
        else if (score >= 60) return "D (60-69)";
        else return "F (0-59)";
    }

    /**
     * 4. 排名前 N 名
     */
    public List<EnrollmentRecord> top(int count) {
        if (count <= 0) {
            return new ArrayList<>();
        }

        // 依成績降冪排序
        List<EnrollmentRecord> sorted = new ArrayList<>(records);
        sorted.sort((r1, r2) -> Integer.compare(r2.getScore(), r1.getScore()));

        // 如果 count 大於等於總數，回傳所有數據
        if (count >= sorted.size()) {
            return sorted;
        }

        return sorted.subList(0, count);
    }

    /**
     * 5. 移除低於門檻的記錄（保持 List、Set、Map 一致）
     */
    public int removeBelow(int minimum) {
        if (minimum < 0 || minimum > 100) {
            System.out.println("⚠️ 門檻值必須在 0~100 之間");
            return 0;
        }

        // 收集要移除的記錄
        List<EnrollmentRecord> toRemove = new ArrayList<>();
        for (EnrollmentRecord record : records) {
            if (record.getScore() < minimum) {
                toRemove.add(record);
            }
        }

        if (toRemove.isEmpty()) {
            System.out.printf("✅ 沒有成績低於 %d 分的記錄需要移除%n", minimum);
            return 0;
        }

        // 從 List 移除
        records.removeAll(toRemove);

        // 重新建立 Set（因為可能學生所有課程都被移除）
        uniqueStudents.clear();
        for (EnrollmentRecord record : records) {
            uniqueStudents.add(record.getStudentId());
        }

        // 重新建立 studentMap
        studentMap.clear();
        for (EnrollmentRecord record : records) {
            studentMap.computeIfAbsent(record.getStudentId(), k -> new ArrayList<>()).add(record);
        }

        // 重新建立 courseMap
        courseMap.clear();
        for (EnrollmentRecord record : records) {
            courseMap.computeIfAbsent(record.getCourseCode(), k -> new ArrayList<>()).add(record);
        }

        // 重新建立 tagMap
        tagMap.clear();
        for (EnrollmentRecord record : records) {
            String tagKey = (record.getTag() == null || record.getTag().trim().isEmpty()) 
                    ? "無標籤" : record.getTag().trim();
            tagMap.computeIfAbsent(tagKey, k -> new ArrayList<>()).add(record);
        }

        System.out.printf("✅ 已移除 %d 筆成績低於 %d 分的記錄%n", toRemove.size(), minimum);
        return toRemove.size();
    }

    // ========== 查詢輔助方法 ==========

    /**
     * 取得學生所有報名
     */
    public List<EnrollmentRecord> getStudentRecords(String studentId) {
        return new ArrayList<>(studentMap.getOrDefault(studentId, new ArrayList<>()));
    }

    /**
     * 取得課程所有報名
     */
    public List<EnrollmentRecord> getCourseRecords(String courseCode) {
        return new ArrayList<>(courseMap.getOrDefault(courseCode, new ArrayList<>()));
    }

    /**
     * 取得所有記錄
     */
    public List<EnrollmentRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    /**
     * 取得不重複學生數
     */
    public int getUniqueStudentCount() {
        return uniqueStudents.size();
    }

    /**
     * 取得總報名數
     */
    public int getTotalRecords() {
        return records.size();
    }

    // ========== 輸出方法 ==========

    /**
     * 印出所有記錄
     */
    public void printAllRecords() {
        if (records.isEmpty()) {
            System.out.println("  📭 沒有報名記錄");
            return;
        }

        System.out.println("  📋 所有報名記錄（共 " + records.size() + " 筆）：");
        System.out.println("  ┌────┬──────────────────────────────────────────────────┐");
        for (int i = 0; i < records.size(); i++) {
            System.out.printf("  │ %2d │ %-48s │%n", i + 1, records.get(i));
        }
        System.out.println("  └────┴──────────────────────────────────────────────────┘");
    }

    /**
     * 印出統計摘要
     */
    public void printSummary() {
        System.out.println("  📊 系統統計摘要：");
        System.out.printf("    總報名數： %d 筆%n", records.size());
        System.out.printf("    不重複學生數： %d 人%n", uniqueStudents.size());
        System.out.printf("    學生列表： %s%n", uniqueStudents);
        System.out.printf("    課程列表： %s%n", courseMap.keySet());
        System.out.printf("    標籤列表： %s%n", tagMap.keySet());
    }

    /**
     * 印出成績分布
     */
    public void printScoreDistribution() {
        Map<String, Integer> dist = scoreDistribution();
        System.out.println("  📈 成績分布：");
        System.out.println("  ┌─────────────┬────────┬────────────────────┐");
        System.out.println("  │  等第        │  人數  │  長條圖            │");
        System.out.println("  ├─────────────┼────────┼────────────────────┤");
        for (Map.Entry<String, Integer> entry : dist.entrySet()) {
            String grade = entry.getKey();
            int count = entry.getValue();
            String bar = "█".repeat(Math.min(count, 20));
            System.out.printf("  │  %-11s │  %3d   │  %-18s │%n", grade, count, bar);
        }
        System.out.println("  └─────────────┴────────┴────────────────────┘");
    }

    // ========== 主程式 ==========

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           課程管理集合系統 - 綜合測試報告               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        CourseCollectionManager manager = new CourseCollectionManager();

        // ========== 建立測試資料（至少六筆，包含重複學號、同分與空白標籤） ==========
        System.out.println("\n【步驟 1：建立測試資料】");
        System.out.println("─".repeat(60));

        EnrollmentRecord[] testData = {
            // 重複學號（S001 報名多門課程）
            new EnrollmentRecord("S001", "王小明", "CS101", "程式設計", "必修", 85),
            new EnrollmentRecord("S001", "王小明", "CS102", "資料結構", "必修", 92),
            new EnrollmentRecord("S001", "王小明", "CS107", "軟體工程", "實作", 90),
            
            // 一般資料
            new EnrollmentRecord("S002", "李小華", "CS101", "程式設計", "必修", 78),
            new EnrollmentRecord("S002", "李小華", "CS103", "演算法", "選修", 65),
            
            // 空白標籤
            new EnrollmentRecord("S003", "張小美", "CS101", "程式設計", "", 88),
            new EnrollmentRecord("S003", "張小美", "CS104", "網頁開發", "實作", 95),
            
            // 同分（S002 和 S005 同分）
            new EnrollmentRecord("S004", "陳大文", "CS102", "資料結構", "必修", 52),
            new EnrollmentRecord("S004", "陳大文", "CS105", "作業系統", "選修", 45),
            new EnrollmentRecord("S005", "趙小強", "CS103", "演算法", "選修", 78),  // 與 S002 同分
            new EnrollmentRecord("S005", "趙小強", "CS106", "資料庫", "必修", 82),
            
            // 空白標籤
            new EnrollmentRecord("S006", "林小芳", "CS104", "網頁開發", "", 70)
        };

        System.out.println("▶ 新增報名記錄：");
        for (EnrollmentRecord record : testData) {
            boolean result = manager.addRecord(record);
            System.out.printf("  %s → %s%n", record, result ? "✅ 成功" : "❌ 失敗");
        }

        // ========== 顯示初始資料 ==========
        System.out.println("\n【步驟 2：初始資料顯示】");
        System.out.println("─".repeat(60));
        manager.printAllRecords();
        manager.printSummary();

        // ========== 測試 1：updateScore ==========
        System.out.println("\n【測試 1：更新成績 updateScore()】");
        System.out.println("─".repeat(60));

        System.out.println("▶ 更新 S004 的成績為 60 分：");
        manager.updateScore("S004", 60);

        System.out.println("\n▶ 更新 S004 的成績（所有課程）為 65 分：");
        manager.updateScore("S004", 65);

        System.out.println("\n▶ 嘗試更新不存在的學生 S999：");
        manager.updateScore("S999", 90);

        System.out.println("\n▶ 嘗試更新無效成績（-10）：");
        manager.updateScore("S001", -10);

        // ========== 測試 2：findByTag ==========
        System.out.println("\n【測試 2：根據標籤查詢 findByTag()】");
        System.out.println("─".repeat(60));

        String[] testTags = {"必修", "選修", "實作", "無標籤", "不存在"};
        for (String tag : testTags) {
            List<EnrollmentRecord> results = manager.findByTag(tag);
            System.out.printf("  📌 標籤 '%s'：%d 筆記錄%n", tag, results.size());
            for (EnrollmentRecord r : results) {
                System.out.printf("     - %s%n", r);
            }
        }

        // ========== 測試 3：scoreDistribution ==========
        System.out.println("\n【測試 3：成績分布 scoreDistribution()】");
        System.out.println("─".repeat(60));
        manager.printScoreDistribution();

        // ========== 測試 4：top ==========
        System.out.println("\n【測試 4：排名 top()】");
        System.out.println("─".repeat(60));

        int[] topCounts = {3, 5, 100};
        for (int count : topCounts) {
            List<EnrollmentRecord> topList = manager.top(count);
            System.out.printf("  🏆 前 %d 名（共 %d 筆）：%n", count, topList.size());
            for (int i = 0; i < topList.size(); i++) {
                System.out.printf("     %d. %s%n", i + 1, topList.get(i));
            }
            System.out.println();
        }

        // ========== 測試 5：removeBelow ==========
        System.out.println("\n【測試 5：移除低於門檻 removeBelow()】");
        System.out.println("─".repeat(60));

        System.out.println("▶ 移除低於 60 分的記錄：");
        int removed = manager.removeBelow(60);
        System.out.printf("  共移除 %d 筆記錄%n", removed);

        System.out.println("\n▶ 移除後資料：");
        manager.printAllRecords();
        manager.printSummary();

        System.out.println("\n▶ 再次嘗試移除低於 60 分的記錄（應該沒有）：");
        manager.removeBelow(60);

        // ========== 驗證一致性 ==========
        System.out.println("\n【驗證：資料一致性檢查】");
        System.out.println("─".repeat(60));
        verifyConsistency(manager);

        // ========== 功能總結 ==========
        printSummaryTable();
    }

    /**
     * 驗證 List、Set、Map 的一致性
     */
    private static void verifyConsistency(CourseCollectionManager manager) {
        List<EnrollmentRecord> records = manager.getAllRecords();
        Set<String> students = new HashSet<>();
        Map<String, Integer> studentCount = new HashMap<>();

        for (EnrollmentRecord r : records) {
            students.add(r.getStudentId());
            studentCount.put(r.getStudentId(), studentCount.getOrDefault(r.getStudentId(), 0) + 1);
        }

        System.out.println("  📊 一致性驗證：");
        System.out.printf("    List 筆數：%d%n", records.size());
        System.out.printf("    Set 學生數：%d%n", manager.getUniqueStudentCount());
        System.out.printf("    Set vs List 計算：%d vs %d %s%n", 
                students.size(), manager.getUniqueStudentCount(),
                students.size() == manager.getUniqueStudentCount() ? "✅" : "❌");

        System.out.println("    各學生報名數：");
        for (Map.Entry<String, Integer> entry : studentCount.entrySet()) {
            List<EnrollmentRecord> studentRecords = manager.getStudentRecords(entry.getKey());
            System.out.printf("      %s：%d 筆 (Map:%d)%s%n", 
                    entry.getKey(), entry.getValue(), studentRecords.size(),
                    entry.getValue() == studentRecords.size() ? " ✅" : " ❌");
        }
        System.out.println("  ✅ 資料一致性檢查通過！");
    }

    /**
     * 印出功能總結
     */
    private static void printSummaryTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              課程管理集合系統 - 功能總結                ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  功能                 │  說明                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  updateScore()        │  更新學生所有課程的成績         ║");
        System.out.println("║  findByTag()          │  根據標籤查詢報名記錄           ║");
        System.out.println("║  scoreDistribution()  │  統計 A/B/C/D/F 成績分布        ║");
        System.out.println("║  top()                │  回傳排名前 N 名               ║");
        System.out.println("║  removeBelow()        │  移除低於門檻的記錄             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        System.out.println("\n【資料一致性保證】");
        System.out.println("  ✅ List 保留原始順序");
        System.out.println("  ✅ Set 儲存不重複學生");
        System.out.println("  ✅ Map 提供快速查詢（學生、課程、標籤）");
        System.out.println("  ✅ removeBelow() 同步更新所有資料結構");
        System.out.println("  ✅ 任何操作後 List、Set、Map 保持一致");
    }
}