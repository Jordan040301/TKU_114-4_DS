/**
 * 檔名：Q06_EnrollmentIndex.java
 * 功能：選課索引系統
 * 說明：使用 Map<String, Set<String>> 管理 courseCode 與 studentId
 *       支援選課、退選、查詢和摘要
 */

import java.util.*;

public class Q06_EnrollmentIndex {

    // ========== 內部資料結構 ==========
    // Map: courseCode -> Set of studentId
    private final Map<String, Set<String>> enrollments;

    /**
     * 建構子
     */
    public Q06_EnrollmentIndex() {
        this.enrollments = new HashMap<>();
    }

    // ========== 主要方法 ==========

    /**
     * 選課
     * @param courseCode 課程代碼
     * @param studentId 學生編號
     * @return true 表示選課成功，false 表示參數無效或已選過該課程
     */
    public boolean enroll(String courseCode, String studentId) {
        // Code 或 id 為 null、blank 時回傳 false
        if (isBlank(courseCode) || isBlank(studentId)) {
            return false;
        }

        // 取得課程的學生集合（若不存在則建立）
        Set<String> students = enrollments.computeIfAbsent(courseCode, k -> new HashSet<>());

        // 重複選課不增加資料並回傳 false
        if (students.contains(studentId)) {
            return false;
        }

        // 加入學生
        students.add(studentId);
        return true;
    }

    /**
     * 退選
     * @param courseCode 課程代碼
     * @param studentId 學生編號
     * @return true 表示退選成功，false 表示參數無效或該學生未選該課程
     */
    public boolean drop(String courseCode, String studentId) {
        // Code 或 id 為 null、blank 時回傳 false
        if (isBlank(courseCode) || isBlank(studentId)) {
            return false;
        }

        // 檢查課程是否存在
        Set<String> students = enrollments.get(courseCode);
        if (students == null) {
            return false;
        }

        // 檢查學生是否存在於該課程
        if (!students.contains(studentId)) {
            return false;
        }

        // 移除學生
        students.remove(studentId);

        // Drop 成功後若該課程已無人選課，從 Map 移除該 courseCode
        if (students.isEmpty()) {
            enrollments.remove(courseCode);
        }

        return true;
    }

    /**
     * 取得課程的選課人數
     * @param courseCode 課程代碼
     * @return 選課人數（課程不存在時回傳 0）
     */
    public int courseSize(String courseCode) {
        if (isBlank(courseCode)) {
            return 0;
        }

        Set<String> students = enrollments.get(courseCode);
        return (students == null) ? 0 : students.size();
    }

    /**
     * 取得某課程的所有學生（依字典順序）
     * @param courseCode 課程代碼
     * @return 學生編號列表（課程不存在時回傳空列表）
     */
    public List<String> studentsOf(String courseCode) {
        if (isBlank(courseCode)) {
            return Collections.emptyList();
        }

        Set<String> students = enrollments.get(courseCode);
        if (students == null || students.isEmpty()) {
            return Collections.emptyList();
        }

        // 複製並排序（依字典順序）
        List<String> result = new ArrayList<>(students);
        Collections.sort(result);
        
        // 回傳不可修改的列表，防止暴露內部 collection
        return Collections.unmodifiableList(result);
    }

    /**
     * 取得某學生選的所有課程（依字典順序）
     * @param studentId 學生編號
     * @return 課程代碼列表（學生不存在時回傳空列表）
     */
    public List<String> coursesOf(String studentId) {
        if (isBlank(studentId)) {
            return Collections.emptyList();
        }

        Set<String> courses = new HashSet<>();

        // 遍歷所有課程，找出包含該學生的課程
        for (Map.Entry<String, Set<String>> entry : enrollments.entrySet()) {
            if (entry.getValue().contains(studentId)) {
                courses.add(entry.getKey());
            }
        }

        if (courses.isEmpty()) {
            return Collections.emptyList();
        }

        // 排序（依字典順序）
        List<String> result = new ArrayList<>(courses);
        Collections.sort(result);

        // 回傳不可修改的列表，防止暴露內部 collection
        return Collections.unmodifiableList(result);
    }

    /**
     * 取得所有課程的選課摘要（依字典順序）
     * @return Map<String, Integer> 課程代碼 -> 選課人數
     */
    public Map<String, Integer> summary() {
        // 使用 TreeMap 自動依字典順序排序
        Map<String, Integer> summary = new TreeMap<>();

        for (Map.Entry<String, Set<String>> entry : enrollments.entrySet()) {
            summary.put(entry.getKey(), entry.getValue().size());
        }

        // 回傳不可修改的 Map，防止暴露內部 collection
        return Collections.unmodifiableMap(summary);
    }

    // ========== 輔助方法 ==========

    /**
     * 檢查字串是否為 null 或空白
     * @param str 要檢查的字串
     * @return true 表示為 null 或空白
     */
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 取得內部資料的深層複製（僅供測試/除錯用）
     * 注意：此方法僅用於測試，不應在正式程式碼中暴露內部結構
     */
    Map<String, Set<String>> getInternalDataForTest() {
        Map<String, Set<String>> copy = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : enrollments.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試範例 =====");
        Q06_EnrollmentIndex index = new Q06_EnrollmentIndex();
        index.enroll("DS", "S01");
        index.enroll("DS", "S01");  // 重複選課
        index.enroll("JAVA", "S01");
        index.enroll("DS", "S02");
        index.enroll("DS", "S03");
        System.out.println(index.studentsOf("DS"));    // [S01, S02, S03]
        System.out.println(index.coursesOf("S01"));    // [DS, JAVA]
        System.out.println(index.summary());           // {DS=3, JAVA=1}
        System.out.println();

        // ===== 測試 null / blank 處理 =====
        System.out.println("===== null / blank 處理測試 =====");
        Q06_EnrollmentIndex index2 = new Q06_EnrollmentIndex();

        System.out.println("enroll(null, 'S01') → " + index2.enroll(null, "S01"));   // false
        System.out.println("enroll('DS', null) → " + index2.enroll("DS", null));     // false
        System.out.println("enroll('', 'S01') → " + index2.enroll("", "S01"));       // false
        System.out.println("enroll('DS', '') → " + index2.enroll("DS", ""));         // false
        System.out.println("enroll('  ', 'S01') → " + index2.enroll("  ", "S01"));   // false

        System.out.println("drop(null, 'S01') → " + index2.drop(null, "S01"));       // false
        System.out.println("drop('DS', null) → " + index2.drop("DS", null));         // false

        System.out.println("courseSize(null) → " + index2.courseSize(null));         // 0
        System.out.println("studentsOf(null) → " + index2.studentsOf(null));         // []
        System.out.println("coursesOf(null) → " + index2.coursesOf(null));           // []
        System.out.println();

        // ===== 測試重複選課 =====
        System.out.println("===== 重複選課測試 =====");
        Q06_EnrollmentIndex index3 = new Q06_EnrollmentIndex();
        System.out.println("enroll('DS', 'S01') → " + index3.enroll("DS", "S01"));   // true
        System.out.println("enroll('DS', 'S01') → " + index3.enroll("DS", "S01"));   // false（重複）
        System.out.println("courseSize('DS') → " + index3.courseSize("DS"));         // 1
        System.out.println("studentsOf('DS') → " + index3.studentsOf("DS"));         // [S01]
        System.out.println();

        // ===== 測試 Drop 及空課程清理 =====
        System.out.println("===== Drop 及空課程清理測試 =====");
        Q06_EnrollmentIndex index4 = new Q06_EnrollmentIndex();
        index4.enroll("DS", "S01");
        index4.enroll("DS", "S02");
        index4.enroll("JAVA", "S01");
        System.out.println("初始 summary: " + index4.summary());  // {DS=2, JAVA=1}

        System.out.println("drop('DS', 'S01') → " + index4.drop("DS", "S01"));       // true
        System.out.println("DS 課程人數: " + index4.courseSize("DS"));               // 1
        System.out.println("summary: " + index4.summary());                          // {DS=1, JAVA=1}

        System.out.println("drop('DS', 'S02') → " + index4.drop("DS", "S02"));       // true
        System.out.println("DS 課程人數: " + index4.courseSize("DS"));               // 0
        System.out.println("summary: " + index4.summary());                          // {JAVA=1}
        System.out.println("DS 是否還在 Map 中: " + index4.courseSize("DS"));       // 0（已移除）

        // 測試 drop 不存在的學生
        System.out.println("drop('JAVA', 'S99') → " + index4.drop("JAVA", "S99"));   // false
        System.out.println();

        // ===== 測試排序 =====
        System.out.println("===== 排序測試 =====");
        Q06_EnrollmentIndex index5 = new Q06_EnrollmentIndex();
        index5.enroll("ZOO", "S05");
        index5.enroll("AAA", "S02");
        index5.enroll("MATH", "S01");
        index5.enroll("ZOO", "S01");
        index5.enroll("AAA", "S01");
        index5.enroll("MATH", "S03");

        System.out.println("studentsOf('ZOO'): " + index5.studentsOf("ZOO"));  // [S01, S05]（排序後）
        System.out.println("coursesOf('S01'): " + index5.coursesOf("S01"));    // [AAA, MATH, ZOO]（排序後）
        System.out.println("summary: " + index5.summary());  // {AAA=2, MATH=2, ZOO=2}（依字典序）
        System.out.println();

        // ===== 測試封裝（防止暴露內部 collection） =====
        System.out.println("===== 封裝測試 =====");
        Q06_EnrollmentIndex index6 = new Q06_EnrollmentIndex();
        index6.enroll("DS", "S01");
        index6.enroll("DS", "S02");

        // 測試 studentsOf 回傳的 List 不可修改
        List<String> students = index6.studentsOf("DS");
        System.out.println("studentsOf('DS'): " + students);  // [S01, S02]
        try {
            students.add("S03");
            System.out.println("不應該執行到這裡");
        } catch (UnsupportedOperationException e) {
            System.out.println("✅ studentsOf 回傳的 List 不可修改");
        }

        // 測試 coursesOf 回傳的 List 不可修改
        List<String> courses = index6.coursesOf("S01");
        System.out.println("coursesOf('S01'): " + courses);  // [DS]
        try {
            courses.add("JAVA");
            System.out.println("不應該執行到這裡");
        } catch (UnsupportedOperationException e) {
            System.out.println("✅ coursesOf 回傳的 List 不可修改");
        }

        // 測試 summary 回傳的 Map 不可修改
        Map<String, Integer> summary = index6.summary();
        System.out.println("summary: " + summary);  // {DS=2}
        try {
            summary.put("JAVA", 1);
            System.out.println("不應該執行到這裡");
        } catch (UnsupportedOperationException e) {
            System.out.println("✅ summary 回傳的 Map 不可修改");
        }

        // 驗證內部資料未被修改
        System.out.println("內部資料未被修改: " + index6.studentsOf("DS"));  // [S01, S02]
        System.out.println();

        // ===== 測試邊界情況 =====
        System.out.println("===== 邊界情況測試 =====");
        Q06_EnrollmentIndex index7 = new Q06_EnrollmentIndex();

        System.out.println("studentsOf('NONEXIST'): " + index7.studentsOf("NONEXIST"));  // []
        System.out.println("coursesOf('S99'): " + index7.coursesOf("S99"));              // []
        System.out.println("courseSize('NONEXIST'): " + index7.courseSize("NONEXIST"));  // 0
        System.out.println("summary: " + index7.summary());                             // {}

        // 退選不存在的課程
        System.out.println("drop('NONEXIST', 'S01') → " + index7.drop("NONEXIST", "S01")); // false
    }
}