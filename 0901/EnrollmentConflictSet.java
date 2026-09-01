import java.util.*;

/**
 * 選課重複檢查系統
 * 使用複合鍵 (學號 + 課號) 檢查重複選課
 */
public class EnrollmentConflictSet {
    
    /**
     * 選課記錄複合鍵
     */
    public static class EnrollmentKey {
        private final String studentId;
        private final String courseId;
        
        public EnrollmentKey(String studentId, String courseId) {
            if (studentId == null || studentId.trim().isEmpty()) {
                throw new IllegalArgumentException("學號不能為空");
            }
            if (courseId == null || courseId.trim().isEmpty()) {
                throw new IllegalArgumentException("課號不能為空");
            }
            this.studentId = studentId.trim().toUpperCase();
            this.courseId = courseId.trim().toUpperCase();
        }
        
        public String getStudentId() {
            return studentId;
        }
        
        public String getCourseId() {
            return courseId;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            EnrollmentKey that = (EnrollmentKey) obj;
            return Objects.equals(studentId, that.studentId) &&
                   Objects.equals(courseId, that.courseId);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(studentId, courseId);
        }
        
        @Override
        public String toString() {
            return studentId + " - " + courseId;
        }
    }
    
    // 所有選課記錄 (使用 Set 自動去重)
    private Set<EnrollmentKey> enrollmentSet;
    
    // 所有原始記錄 (用於檢測重複)
    private List<EnrollmentKey> allRecords;
    
    // 重複記錄列表
    private List<EnrollmentKey> duplicateRecords;
    
    // 學生 -> 課程集合
    private Map<String, Set<String>> studentCoursesMap;
    
    // 課程 -> 修課人數
    private Map<String, Integer> courseStudentCountMap;
    
    // 課程 -> 修課學生集合
    private Map<String, Set<String>> courseStudentsMap;
    
    /**
     * 建構子
     */
    public EnrollmentConflictSet() {
        this.enrollmentSet = new HashSet<>();
        this.allRecords = new ArrayList<>();
        this.duplicateRecords = new ArrayList<>();
        this.studentCoursesMap = new HashMap<>();
        this.courseStudentCountMap = new HashMap<>();
        this.courseStudentsMap = new HashMap<>();
    }
    
    /**
     * 新增選課記錄
     * @param studentId 學號
     * @param courseId 課號
     * @return true 如果是重複記錄
     */
    public boolean addEnrollment(String studentId, String courseId) {
        EnrollmentKey key = new EnrollmentKey(studentId, courseId);
        allRecords.add(key);
        
        // 檢查是否重複
        if (enrollmentSet.contains(key)) {
            duplicateRecords.add(key);
            System.out.printf("⚠️ 重複選課記錄: %s%n", key);
            return true;
        }
        
        // 新增記錄
        enrollmentSet.add(key);
        
        // 更新學生課程集合
        studentCoursesMap.computeIfAbsent(key.getStudentId(), k -> new HashSet<>())
                         .add(key.getCourseId());
        
        // 更新課程學生集合
        courseStudentsMap.computeIfAbsent(key.getCourseId(), k -> new HashSet<>())
                         .add(key.getStudentId());
        
        System.out.printf("✅ 新增選課: %s%n", key);
        return false;
    }
    
    /**
     * 批次新增選課記錄
     * @param enrollments 選課記錄陣列
     */
    public void addEnrollments(String... enrollments) {
        if (enrollments.length % 2 != 0) {
            throw new IllegalArgumentException("參數必須為成對的 (學號, 課號)");
        }
        
        for (int i = 0; i < enrollments.length; i += 2) {
            addEnrollment(enrollments[i], enrollments[i + 1]);
        }
    }
    
    /**
     * 批次新增選課記錄 (使用陣列)
     * @param studentIds 學號陣列
     * @param courseIds 課號陣列
     */
    public void addEnrollments(String[] studentIds, String[] courseIds) {
        if (studentIds.length != courseIds.length) {
            throw new IllegalArgumentException("學號陣列和課號陣列長度必須相同");
        }
        
        for (int i = 0; i < studentIds.length; i++) {
            addEnrollment(studentIds[i], courseIds[i]);
        }
    }
    
    /**
     * 取得所有選課記錄 (不重複)
     * @return 選課記錄集合
     */
    public Set<EnrollmentKey> getAllEnrollments() {
        return new HashSet<>(enrollmentSet);
    }
    
    /**
     * 取得重複記錄列表
     * @return 重複記錄列表
     */
    public List<EnrollmentKey> getDuplicateRecords() {
        return new ArrayList<>(duplicateRecords);
    }
    
    /**
     * 取得學生的課程集合
     * @param studentId 學號
     * @return 課程集合
     */
    public Set<String> getStudentCourses(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return new HashSet<>();
        }
        String id = studentId.trim().toUpperCase();
        return studentCoursesMap.getOrDefault(id, new HashSet<>());
    }
    
    /**
     * 取得所有學生的課程總覽
     * @return 學生課程對應表
     */
    public Map<String, Set<String>> getAllStudentCourses() {
        Map<String, Set<String>> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : studentCoursesMap.entrySet()) {
            result.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return result;
    }
    
    /**
     * 取得課程修課人數
     * @param courseId 課號
     * @return 修課人數
     */
    public int getCourseStudentCount(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            return 0;
        }
        String id = courseId.trim().toUpperCase();
        Set<String> students = courseStudentsMap.get(id);
        return students == null ? 0 : students.size();
    }
    
    /**
     * 取得所有課程的修課人數
     * @return 課程人數對應表
     */
    public Map<String, Integer> getAllCourseStudentCounts() {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : courseStudentsMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }
        return result;
    }
    
    /**
     * 取得課程的修課學生集合
     * @param courseId 課號
     * @return 學生集合
     */
    public Set<String> getCourseStudents(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            return new HashSet<>();
        }
        String id = courseId.trim().toUpperCase();
        return courseStudentsMap.getOrDefault(id, new HashSet<>());
    }
    
    /**
     * 檢查是否已選課
     * @param studentId 學號
     * @param courseId 課號
     * @return true 如果已選課
     */
    public boolean isEnrolled(String studentId, String courseId) {
        EnrollmentKey key = new EnrollmentKey(studentId, courseId);
        return enrollmentSet.contains(key);
    }
    
    /**
     * 取得重複記錄數量
     * @return 重複記錄數量
     */
    public int getDuplicateCount() {
        return duplicateRecords.size();
    }
    
    /**
     * 取得總選課記錄數量 (含重複)
     * @return 總記錄數量
     */
    public int getTotalRecords() {
        return allRecords.size();
    }
    
    /**
     * 取得不重複選課記錄數量
     * @return 不重複記錄數量
     */
    public int getUniqueRecords() {
        return enrollmentSet.size();
    }
    
    /**
     * 取得學生總數
     * @return 學生數量
     */
    public int getStudentCount() {
        return studentCoursesMap.size();
    }
    
    /**
     * 取得課程總數
     * @return 課程數量
     */
    public int getCourseCount() {
        return courseStudentsMap.size();
    }
    
    /**
     * 生成完整報告
     * @return 格式化的報告
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("\n=== 選課重複檢查報告 ===\n");
        report.append("總記錄數: ").append(getTotalRecords()).append("\n");
        report.append("不重複記錄數: ").append(getUniqueRecords()).append("\n");
        report.append("重複記錄數: ").append(getDuplicateCount()).append("\n");
        report.append("學生總數: ").append(getStudentCount()).append("\n");
        report.append("課程總數: ").append(getCourseCount()).append("\n");
        
        // 重複記錄清單
        report.append("\n🔍 重複選課記錄:\n");
        if (duplicateRecords.isEmpty()) {
            report.append("  無重複記錄\n");
        } else {
            for (EnrollmentKey key : duplicateRecords) {
                report.append("  ⚠️ ").append(key).append("\n");
            }
        }
        
        // 每位學生的課程清單
        report.append("\n📚 每位學生選課清單:\n");
        if (studentCoursesMap.isEmpty()) {
            report.append("  無學生資料\n");
        } else {
            List<String> sortedStudents = new ArrayList<>(studentCoursesMap.keySet());
            Collections.sort(sortedStudents);
            
            for (String studentId : sortedStudents) {
                Set<String> courses = studentCoursesMap.get(studentId);
                report.append(String.format("  %s: %d 門課 - %s%n", 
                           studentId, courses.size(), courses));
            }
        }
        
        // 每門課修課人數
        report.append("\n📊 每門課修課人數:\n");
        if (courseStudentsMap.isEmpty()) {
            report.append("  無課程資料\n");
        } else {
            List<String> sortedCourses = new ArrayList<>(courseStudentsMap.keySet());
            Collections.sort(sortedCourses);
            
            report.append("  課號 | 修課人數 | 學生清單\n");
            report.append("  -----|----------|------------------------------\n");
            for (String courseId : sortedCourses) {
                Set<String> students = courseStudentsMap.get(courseId);
                report.append(String.format("  %-4s | %8d | %s%n", 
                           courseId, students.size(), students));
            }
        }
        
        return report.toString();
    }
    
    /**
     * 印出報告
     */
    public void printReport() {
        System.out.println(generateReport());
    }
    
    /**
     * 清除所有數據
     */
    public void clear() {
        enrollmentSet.clear();
        allRecords.clear();
        duplicateRecords.clear();
        studentCoursesMap.clear();
        courseStudentCountMap.clear();
        courseStudentsMap.clear();
        System.out.println("🔄 已清除所有選課資料");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 選課重複檢查系統測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：重複檢測
        testDuplicateDetection();
        
        // 測試 3：統計查詢
        testStatisticsQuery();
        
        // 測試 4：邊界情況
        testEdgeCases();
        
        // 測試 5：實際應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- 測試 1: 基本功能 ---");
        
        EnrollmentConflictSet checker = new EnrollmentConflictSet();
        
        System.out.println("新增選課記錄:");
        checker.addEnrollment("S001", "CS101");
        checker.addEnrollment("S001", "CS102");
        checker.addEnrollment("S002", "CS101");
        checker.addEnrollment("S002", "CS103");
        checker.addEnrollment("S003", "CS102");
        checker.addEnrollment("S003", "CS103");
        
        System.out.println("\n統計資訊:");
        System.out.println("  總記錄數: " + checker.getTotalRecords());
        System.out.println("  不重複記錄數: " + checker.getUniqueRecords());
        System.out.println("  重複記錄數: " + checker.getDuplicateCount());
        System.out.println("  學生總數: " + checker.getStudentCount());
        System.out.println("  課程總數: " + checker.getCourseCount());
        
        checker.printReport();
    }
    
    /**
     * 測試重複檢測
     */
    private static void testDuplicateDetection() {
        System.out.println("--- 測試 2: 重複檢測 ---");
        
        EnrollmentConflictSet checker = new EnrollmentConflictSet();
        
        System.out.println("新增選課記錄 (含重複):");
        checker.addEnrollment("S001", "CS101");
        checker.addEnrollment("S001", "CS101");  // 重複
        checker.addEnrollment("S002", "CS102");
        checker.addEnrollment("S001", "CS102");
        checker.addEnrollment("S002", "CS101");
        checker.addEnrollment("S002", "CS102");  // 重複
        checker.addEnrollment("S003", "CS103");
        checker.addEnrollment("S003", "CS103");  // 重複
        checker.addEnrollment("S001", "CS103");
        
        System.out.println("\n檢測結果:");
        System.out.println("  總記錄數: " + checker.getTotalRecords());
        System.out.println("  不重複記錄數: " + checker.getUniqueRecords());
        System.out.println("  重複記錄數: " + checker.getDuplicateCount());
        
        System.out.println("\n重複記錄清單:");
        for (EnrollmentConflictSet.EnrollmentKey key : checker.getDuplicateRecords()) {
            System.out.println("  " + key);
        }
        
        checker.printReport();
    }
    
    /**
     * 測試統計查詢
     */
    private static void testStatisticsQuery() {
        System.out.println("--- 測試 3: 統計查詢 ---");
        
        EnrollmentConflictSet checker = new EnrollmentConflictSet();
        
        // 建立測試資料
        String[][] data = {
            {"S001", "CS101"}, {"S001", "CS102"}, {"S001", "CS103"},
            {"S002", "CS101"}, {"S002", "CS103"},
            {"S003", "CS102"}, {"S003", "CS104"},
            {"S004", "CS101"}, {"S004", "CS104"}, {"S004", "CS105"},
            {"S005", "CS103"}, {"S005", "CS105"}
        };
        
        for (String[] pair : data) {
            checker.addEnrollment(pair[0], pair[1]);
        }
        
        System.out.println("\n📊 查詢測試:");
        
        // 查詢學生課程
        System.out.println("  S001 的課程: " + checker.getStudentCourses("S001"));
        System.out.println("  S003 的課程: " + checker.getStudentCourses("S003"));
        System.out.println("  S006 的課程: " + checker.getStudentCourses("S006"));
        
        // 查詢課程學生
        System.out.println("  CS101 的修課學生: " + checker.getCourseStudents("CS101"));
        System.out.println("  CS104 的修課學生: " + checker.getCourseStudents("CS104"));
        
        // 查詢修課人數
        System.out.println("  CS101 修課人數: " + checker.getCourseStudentCount("CS101"));
        System.out.println("  CS103 修課人數: " + checker.getCourseStudentCount("CS103"));
        System.out.println("  CS106 修課人數: " + checker.getCourseStudentCount("CS106"));
        
        // 檢查是否已選課
        System.out.println("  S001 是否選 CS101: " + checker.isEnrolled("S001", "CS101"));
        System.out.println("  S001 是否選 CS104: " + checker.isEnrolled("S001", "CS104"));
        
        checker.printReport();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空系統
        System.out.println("測試 4.1: 空系統");
        EnrollmentConflictSet checker = new EnrollmentConflictSet();
        checker.printReport();
        System.out.println("  getStudentCount: " + checker.getStudentCount());
        System.out.println("  getCourseCount: " + checker.getCourseCount());
        System.out.println("  getDuplicateCount: " + checker.getDuplicateCount());
        System.out.println();
        
        // 測試 4.2: 單一學生選多門課
        System.out.println("測試 4.2: 單一學生選多門課");
        checker.addEnrollment("S001", "CS101");
        checker.addEnrollment("S001", "CS102");
        checker.addEnrollment("S001", "CS103");
        checker.addEnrollment("S001", "CS104");
        System.out.println("  S001 的課程: " + checker.getStudentCourses("S001"));
        System.out.println("  S001 選課數: " + checker.getStudentCourses("S001").size());
        checker.printReport();
        System.out.println();
        
        // 測試 4.3: 多學生選同一門課
        System.out.println("測試 4.3: 多學生選同一門課");
        EnrollmentConflictSet checker2 = new EnrollmentConflictSet();
        for (int i = 1; i <= 10; i++) {
            checker2.addEnrollment("S" + String.format("%03d", i), "CS101");
        }
        System.out.println("  CS101 修課人數: " + checker2.getCourseStudentCount("CS101"));
        System.out.println("  CS101 修課學生: " + checker2.getCourseStudents("CS101"));
        System.out.println();
        
        // 測試 4.4: 大小寫處理
        System.out.println("測試 4.4: 大小寫處理");
        EnrollmentConflictSet checker3 = new EnrollmentConflictSet();
        checker3.addEnrollment("s001", "cs101");
        checker3.addEnrollment("S001", "CS101");  // 應該被視為重複
        System.out.println("  總記錄數: " + checker3.getTotalRecords());
        System.out.println("  不重複記錄數: " + checker3.getUniqueRecords());
        System.out.println("  重複記錄數: " + checker3.getDuplicateCount());
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 5: 實際應用場景 ---");
        System.out.println("🏫 大學選課系統");
        
        EnrollmentConflictSet system = new EnrollmentConflictSet();
        
        // 模擬學生選課資料
        System.out.println("\n📝 匯入選課資料:");
        
        // 學生選課資料 (學號, 課號)
        String[][] enrollmentData = {
            // 資工系學生
            {"S001", "CS101"}, {"S001", "CS102"}, {"S001", "CS103"}, {"S001", "CS201"},
            {"S002", "CS101"}, {"S002", "CS103"}, {"S002", "CS202"},
            {"S003", "CS102"}, {"S003", "CS104"}, {"S003", "CS201"}, {"S003", "CS203"},
            
            // 資管系學生
            {"S004", "CS101"}, {"S004", "CS104"}, {"S004", "CS301"},
            {"S005", "CS102"}, {"S005", "CS105"}, {"S005", "CS301"},
            
            // 電機系學生
            {"S006", "CS101"}, {"S006", "CS202"}, {"S006", "CS302"},
            {"S007", "CS103"}, {"S007", "CS203"}, {"S007", "CS302"},
            
            // 重複選課 (模擬系統錯誤)
            {"S001", "CS101"},  // S001 重複選 CS101
            {"S004", "CS101"},  // S004 重複選 CS101
            {"S006", "CS302"},  // S006 重複選 CS302
            
            // 更多學生
            {"S008", "CS104"}, {"S008", "CS105"},
            {"S009", "CS201"}, {"S009", "CS202"},
            {"S010", "CS203"}, {"S010", "CS301"}
        };
        
        for (String[] pair : enrollmentData) {
            system.addEnrollment(pair[0], pair[1]);
        }
        
        // 生成完整報告
        system.printReport();
        
        // 統計分析
        System.out.println("\n📊 統計分析:");
        System.out.println("  總學生數: " + system.getStudentCount());
        System.out.println("  總課程數: " + system.getCourseCount());
        System.out.println("  總選課數: " + system.getUniqueRecords());
        System.out.println("  平均每人選課數: " + 
                          String.format("%.2f", (double) system.getUniqueRecords() / system.getStudentCount()));
        System.out.println("  平均每門課人數: " + 
                          String.format("%.2f", (double) system.getUniqueRecords() / system.getCourseCount()));
        
        // 熱門課程排行
        System.out.println("\n🏆 熱門課程排行 (修課人數):");
        Map<String, Integer> courseCounts = system.getAllCourseStudentCounts();
        List<Map.Entry<String, Integer>> sortedCourses = new ArrayList<>(courseCounts.entrySet());
        sortedCourses.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        System.out.println("  排名 | 課號 | 修課人數");
        System.out.println("  -----|------|----------");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : sortedCourses) {
            if (rank > 10) break;
            System.out.printf("  %4d | %-4s | %8d%n", 
                             rank++, entry.getKey(), entry.getValue());
        }
        
        // 選課最多的學生
        System.out.println("\n📚 選課最多學生:");
        Map<String, Set<String>> studentCourses = system.getAllStudentCourses();
        List<Map.Entry<String, Set<String>>> sortedStudents = new ArrayList<>(studentCourses.entrySet());
        sortedStudents.sort((a, b) -> b.getValue().size() - a.getValue().size());
        
        System.out.println("  排名 | 學號 | 選課數");
        System.out.println("  -----|------|--------");
        rank = 1;
        for (Map.Entry<String, Set<String>> entry : sortedStudents) {
            if (rank > 5) break;
            System.out.printf("  %4d | %-4s | %6d%n", 
                             rank++, entry.getKey(), entry.getValue().size());
        }
        
        // 檢查特定學生的選課
        System.out.println("\n🔍 特定學生查詢:");
        String[] targetStudents = {"S001", "S004", "S007", "S011"};
        for (String studentId : targetStudents) {
            Set<String> courses = system.getStudentCourses(studentId);
            System.out.printf("  %s: %d 門課 - %s%n", 
                             studentId, courses.size(), courses.isEmpty() ? "無選課" : courses.toString());
        }
        
        // 檢查特定課程
        System.out.println("\n📖 特定課程查詢:");
        String[] targetCourses = {"CS101", "CS301", "CS999"};
        for (String courseId : targetCourses) {
            int count = system.getCourseStudentCount(courseId);
            Set<String> students = system.getCourseStudents(courseId);
            System.out.printf("  %s: %d 人 - %s%n", 
                             courseId, count, students.isEmpty() ? "無人選修" : students.toString());
        }
    }
}