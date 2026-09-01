import java.util.*;

/**
 * 課程成績統計
 * 使用 Map<String, List<Integer>> 管理課程與成績
 */
public class CourseGradeMap {
    
    // 課程名稱 -> 成績列表
    private final Map<String, List<Integer>> courseGrades;
    
    /**
     * 建構子
     */
    public CourseGradeMap() {
        this.courseGrades = new HashMap<>();
    }
    
    /**
     * 新增成績到指定課程
     * @param courseName 課程名稱
     * @param grade 成績 (0-100)
     */
    public void addGrade(String courseName, int grade) {
        // 驗證成績範圍
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("成績必須在 0 到 100 之間");
        }
        
        // 取得或建立課程的成績列表
        List<Integer> grades = courseGrades.computeIfAbsent(courseName, k -> new ArrayList<>());
        grades.add(grade);
        
        System.out.printf("✅ 新增成績: %s → %d 分%n", courseName, grade);
    }
    
    /**
     * 批量新增成績
     * @param courseName 課程名稱
     * @param grades 成績陣列
     */
    public void addGrades(String courseName, int... grades) {
        for (int grade : grades) {
            addGrade(courseName, grade);
        }
    }
    
    /**
     * 新增多個課程的成績
     * @param courseGradesData 課程成績資料
     */
    public void addGrades(Map<String, int[]> courseGradesData) {
        for (Map.Entry<String, int[]> entry : courseGradesData.entrySet()) {
            addGrades(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * 取得指定課程的所有成績
     * @param courseName 課程名稱
     * @return 成績列表，若課程不存在則回傳空列表
     */
    public List<Integer> getGrades(String courseName) {
        return courseGrades.getOrDefault(courseName, new ArrayList<>());
    }
    
    /**
     * 計算指定課程的平均分數
     * @param courseName 課程名稱
     * @return 平均分數，若無成績則回傳 0
     */
    public double getAverage(String courseName) {
        List<Integer> grades = courseGrades.get(courseName);
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        
        int sum = 0;
        for (int grade : grades) {
            sum += grade;
        }
        return (double) sum / grades.size();
    }
    
    /**
     * 取得指定課程的最高分
     * @param courseName 課程名稱
     * @return 最高分，若無成績則回傳 -1
     */
    public int getMaxGrade(String courseName) {
        List<Integer> grades = courseGrades.get(courseName);
        if (grades == null || grades.isEmpty()) {
            return -1;
        }
        
        int max = Integer.MIN_VALUE;
        for (int grade : grades) {
            if (grade > max) {
                max = grade;
            }
        }
        return max;
    }
    
    /**
     * 取得指定課程的最低分
     * @param courseName 課程名稱
     * @return 最低分，若無成績則回傳 -1
     */
    public int getMinGrade(String courseName) {
        List<Integer> grades = courseGrades.get(courseName);
        if (grades == null || grades.isEmpty()) {
            return -1;
        }
        
        int min = Integer.MAX_VALUE;
        for (int grade : grades) {
            if (grade < min) {
                min = grade;
            }
        }
        return min;
    }
    
    /**
     * 取得指定課程的成績總數
     * @param courseName 課程名稱
     * @return 成績數量
     */
    public int getGradeCount(String courseName) {
        List<Integer> grades = courseGrades.get(courseName);
        return grades == null ? 0 : grades.size();
    }
    
    /**
     * 取得所有課程名稱
     * @return 課程名稱集合
     */
    public Set<String> getCourseNames() {
        return new HashSet<>(courseGrades.keySet());
    }
    
    /**
     * 取得所有課程的成績統計
     * @return 課程統計資訊的 Map
     */
    public Map<String, CourseStatistics> getAllStatistics() {
        Map<String, CourseStatistics> stats = new LinkedHashMap<>();
        
        for (String courseName : courseGrades.keySet()) {
            stats.put(courseName, new CourseStatistics(
                courseName,
                getGradeCount(courseName),
                getAverage(courseName),
                getMaxGrade(courseName),
                getMinGrade(courseName)
            ));
        }
        
        return stats;
    }
    
    /**
     * 依課程名稱排序輸出成績報告
     */
    public void printReportSortedByCourse() {
        System.out.println("\n=== 課程成績報告 (依課程名稱排序) ===");
        
        if (courseGrades.isEmpty()) {
            System.out.println("尚無任何課程資料");
            return;
        }
        
        // 依課程名稱排序
        List<String> sortedCourses = new ArrayList<>(courseGrades.keySet());
        Collections.sort(sortedCourses);
        
        System.out.printf("%-15s | %-6s | %-6s | %-6s | %-10s%n", 
                         "課程名稱", "人數", "平均", "最高", "成績");
        System.out.println("-----------------|--------|--------|--------|-----------");
        
        for (String courseName : sortedCourses) {
            List<Integer> grades = courseGrades.get(courseName);
            double avg = getAverage(courseName);
            int max = getMaxGrade(courseName);
            int count = grades.size();
            
            System.out.printf("%-15s | %6d | %6.1f | %6d | %s%n",
                             courseName, count, avg, max, formatGrades(grades));
        }
        
        // 顯示總結
        printSummary();
        System.out.println();
    }
    
    /**
     * 依平均分數排序輸出成績報告
     */
    public void printReportSortedByAverage() {
        System.out.println("\n=== 課程成績報告 (依平均分數排序) ===");
        
        if (courseGrades.isEmpty()) {
            System.out.println("尚無任何課程資料");
            return;
        }
        
        // 建立課程統計資訊並排序
        List<CourseStatistics> statsList = new ArrayList<>(getAllStatistics().values());
        statsList.sort((a, b) -> Double.compare(b.average, a.average));
        
        System.out.printf("%-15s | %-6s | %-6s | %-6s | %-10s%n", 
                         "課程名稱", "人數", "平均", "最高", "成績");
        System.out.println("-----------------|--------|--------|--------|-----------");
        
        for (CourseStatistics stat : statsList) {
            List<Integer> grades = courseGrades.get(stat.courseName);
            System.out.printf("%-15s | %6d | %6.1f | %6d | %s%n",
                             stat.courseName, stat.count, stat.average, 
                             stat.maxGrade, formatGrades(grades));
        }
        
        System.out.println();
    }
    
    /**
     * 格式化成績列表顯示
     */
    private String formatGrades(List<Integer> grades) {
        if (grades == null || grades.isEmpty()) {
            return "無";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grades.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(grades.get(i));
        }
        return sb.toString();
    }
    
    /**
     * 列印總結資訊
     */
    private void printSummary() {
        if (courseGrades.isEmpty()) {
            return;
        }
        
        int totalCourses = courseGrades.size();
        int totalStudents = 0;
        double totalAverage = 0;
        int totalGrades = 0;
        
        for (String courseName : courseGrades.keySet()) {
            int count = getGradeCount(courseName);
            totalStudents += count;
            totalGrades += count;
            totalAverage += getAverage(courseName);
        }
        
        System.out.println("\n--- 總結 ---");
        System.out.printf("課程總數: %d%n", totalCourses);
        System.out.printf("總成績筆數: %d%n", totalGrades);
        System.out.printf("所有課程平均: %.2f%n", totalAverage / totalCourses);
        
        // 找出最佳課程
        String bestCourse = "";
        double bestAverage = 0;
        for (String courseName : courseGrades.keySet()) {
            double avg = getAverage(courseName);
            if (avg > bestAverage) {
                bestAverage = avg;
                bestCourse = courseName;
            }
        }
        System.out.printf("最佳課程: %s (平均 %.1f 分)%n", bestCourse, bestAverage);
    }
    
    /**
     * 顯示單一課程的詳細統計
     */
    public void printCourseDetails(String courseName) {
        System.out.println("\n=== " + courseName + " 課程詳細統計 ===");
        
        List<Integer> grades = courseGrades.get(courseName);
        if (grades == null || grades.isEmpty()) {
            System.out.println("此課程尚無成績資料");
            return;
        }
        
        System.out.printf("課程名稱: %s%n", courseName);
        System.out.printf("成績筆數: %d%n", grades.size());
        System.out.printf("平均分數: %.1f%n", getAverage(courseName));
        System.out.printf("最高分數: %d%n", getMaxGrade(courseName));
        System.out.printf("最低分數: %d%n", getMinGrade(courseName));
        
        // 成績分佈統計
        System.out.println("\n成績分佈:");
        System.out.println("  90-100: " + countInRange(grades, 90, 100) + " 人");
        System.out.println("  80-89:  " + countInRange(grades, 80, 89) + " 人");
        System.out.println("  70-79:  " + countInRange(grades, 70, 79) + " 人");
        System.out.println("  60-69:  " + countInRange(grades, 60, 69) + " 人");
        System.out.println("  0-59:   " + countInRange(grades, 0, 59) + " 人");
        
        // 排序後顯示所有成績
        Collections.sort(grades);
        System.out.println("\n所有成績 (已排序): " + grades);
        System.out.println();
    }
    
    /**
     * 計算在指定範圍內的成績數量
     */
    private int countInRange(List<Integer> grades, int low, int high) {
        int count = 0;
        for (int grade : grades) {
            if (grade >= low && grade <= high) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 課程統計資訊內部類別
     */
    public static class CourseStatistics {
        public final String courseName;
        public final int count;
        public final double average;
        public final int maxGrade;
        public final int minGrade;
        
        public CourseStatistics(String courseName, int count, double average, int maxGrade, int minGrade) {
            this.courseName = courseName;
            this.count = count;
            this.average = average;
            this.maxGrade = maxGrade;
            this.minGrade = minGrade;
        }
    }
    
    /**
     * 清空所有資料
     */
    public void clear() {
        courseGrades.clear();
        System.out.println("🔄 已清空所有資料");
    }
    
    /**
     * 移除指定課程
     * @param courseName 課程名稱
     * @return 是否成功移除
     */
    public boolean removeCourse(String courseName) {
        if (courseGrades.containsKey(courseName)) {
            courseGrades.remove(courseName);
            System.out.printf("🗑️ 已移除課程: %s%n", courseName);
            return true;
        }
        System.out.printf("⚠️ 課程不存在: %s%n", courseName);
        return false;
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 課程成績統計系統測試 ===\n");
        
        // 測試 1：基本功能
        testBasicOperations();
        
        // 測試 2：進階統計
        testAdvancedStatistics();
        
        // 測試 3：排序報告
        testSortedReports();
        
        // 測試 4：邊界情況
        testEdgeCases();
        
        // 測試 5：實務應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試基本操作
     */
    private static void testBasicOperations() {
        System.out.println("--- 測試 1: 基本操作 ---");
        
        CourseGradeMap gradeMap = new CourseGradeMap();
        
        // 新增成績
        System.out.println("新增成績:");
        gradeMap.addGrade("資料結構", 85);
        gradeMap.addGrade("資料結構", 92);
        gradeMap.addGrade("資料結構", 78);
        gradeMap.addGrade("演算法", 90);
        gradeMap.addGrade("演算法", 88);
        gradeMap.addGrade("作業系統", 75);
        
        // 查詢
        System.out.println("\n查詢:");
        System.out.println("  getGrades('資料結構'): " + gradeMap.getGrades("資料結構"));
        System.out.println("  getAverage('資料結構'): " + gradeMap.getAverage("資料結構"));
        System.out.println("  getMaxGrade('資料結構'): " + gradeMap.getMaxGrade("資料結構"));
        System.out.println("  getGradeCount('資料結構'): " + gradeMap.getGradeCount("資料結構"));
        
        System.out.println("\n  getAverage('演算法'): " + gradeMap.getAverage("演算法"));
        System.out.println("  getMaxGrade('演算法'): " + gradeMap.getMaxGrade("演算法"));
        
        System.out.println("\n  getAllStatistics():");
        Map<String, CourseStatistics> stats = gradeMap.getAllStatistics();
        for (Map.Entry<String, CourseStatistics> entry : stats.entrySet()) {
            CourseStatistics s = entry.getValue();
            System.out.printf("    %s: 人數=%d, 平均=%.1f, 最高=%d%n",
                             s.courseName, s.count, s.average, s.maxGrade);
        }
    }
    
    /**
     * 測試進階統計
     */
    private static void testAdvancedStatistics() {
        System.out.println("\n--- 測試 2: 進階統計 ---");
        
        CourseGradeMap gradeMap = new CourseGradeMap();
        
        // 使用批量新增
        System.out.println("批量新增成績:");
        gradeMap.addGrades("程式設計", 95, 87, 93, 78, 92, 88, 96, 85, 90, 82);
        gradeMap.addGrades("線性代數", 72, 68, 85, 90, 75, 80, 88, 70, 65, 92);
        gradeMap.addGrades("離散數學", 88, 92, 76, 84, 90, 95, 78, 85, 89, 93);
        
        // 顯示詳細統計
        gradeMap.printCourseDetails("程式設計");
        gradeMap.printCourseDetails("線性代數");
        
        System.out.println("\n所有課程統計:");
        gradeMap.printReportSortedByCourse();
    }
    
    /**
     * 測試排序報告
     */
    private static void testSortedReports() {
        System.out.println("--- 測試 3: 排序報告 ---");
        
        CourseGradeMap gradeMap = new CourseGradeMap();
        
        // 新增多個課程的成績
        gradeMap.addGrades("計算機概論", 78, 85, 92, 70, 88);
        gradeMap.addGrades("網路概論", 95, 88, 76, 92, 85, 90);
        gradeMap.addGrades("資料庫系統", 82, 79, 91, 86, 88, 75, 93);
        gradeMap.addGrades("軟體工程", 88, 92, 85, 79, 90);
        gradeMap.addGrades("計算機組織", 70, 85, 78, 92, 88, 75);
        
        // 依課程名稱排序
        gradeMap.printReportSortedByCourse();
        
        // 依平均分數排序
        gradeMap.printReportSortedByAverage();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 4: 邊界情況 ---");
        
        CourseGradeMap gradeMap = new CourseGradeMap();
        
        // 測試 4.1: 空課程
        System.out.println("測試 4.1: 空課程");
        gradeMap.addGrade("空課程", 85);
        System.out.println("  getGrades('不存在'): " + gradeMap.getGrades("不存在"));
        System.out.println("  getAverage('不存在'): " + gradeMap.getAverage("不存在"));
        System.out.println("  getMaxGrade('不存在'): " + gradeMap.getMaxGrade("不存在"));
        System.out.println("  getGradeCount('不存在'): " + gradeMap.getGradeCount("不存在"));
        gradeMap.removeCourse("空課程");
        System.out.println();
        
        // 測試 4.2: 無效成績
        System.out.println("測試 4.2: 無效成績");
        try {
            gradeMap.addGrade("測試", -5);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲負值例外: " + e.getMessage());
        }
        
        try {
            gradeMap.addGrade("測試", 105);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲超範圍例外: " + e.getMessage());
        }
        System.out.println();
        
        // 測試 4.3: 單一成績
        System.out.println("測試 4.3: 單一成績");
        gradeMap.addGrade("單一課程", 100);
        System.out.println("  getAverage('單一課程'): " + gradeMap.getAverage("單一課程"));
        System.out.println("  getMaxGrade('單一課程'): " + gradeMap.getMaxGrade("單一課程"));
        gradeMap.printCourseDetails("單一課程");
    }
    
    /**
     * 測試實務應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("\n--- 測試 5: 實務應用場景 ---");
        System.out.println("模擬學期成績管理系統");
        
        CourseGradeMap gradeMap = new CourseGradeMap();
        
        // 模擬不同課程的成績
        Map<String, int[]> semesterGrades = new HashMap<>();
        semesterGrades.put("Java程式設計", new int[]{88, 92, 78, 95, 85, 90, 87, 93});
        semesterGrades.put("資料結構", new int[]{75, 85, 90, 82, 88, 95, 79, 91, 86, 84});
        semesterGrades.put("演算法", new int[]{92, 88, 76, 95, 85, 90, 82});
        semesterGrades.put("資料庫系統", new int[]{85, 79, 91, 88, 76, 93, 87, 90, 82});
        semesterGrades.put("網路技術", new int[]{70, 85, 78, 92, 88, 75, 90, 82, 86});
        semesterGrades.put("人工智慧", new int[]{95, 92, 88, 96, 85, 90, 93, 87});
        
        gradeMap.addGrades(semesterGrades);
        
        // 顯示完整報告
        System.out.println("\n=== 學期成績總報告 ===");
        gradeMap.printReportSortedByAverage();
        
        // 顯示前三名課程
        System.out.println("\n=== 學期最佳課程 TOP 3 ===");
        List<CourseStatistics> stats = new ArrayList<>(gradeMap.getAllStatistics().values());
        stats.sort((a, b) -> Double.compare(b.average, a.average));
        
        for (int i = 0; i < Math.min(3, stats.size()); i++) {
            CourseStatistics s = stats.get(i);
            System.out.printf("  #%d: %s (平均 %.1f 分, %d 人)%n",
                             i + 1, s.courseName, s.average, s.count);
        }
        
        // 顯示最難課程 (平均最低)
        System.out.println("\n=== 最難課程 (平均最低) ===");
        if (!stats.isEmpty()) {
            CourseStatistics lowest = stats.get(stats.size() - 1);
            System.out.printf("  %s (平均 %.1f 分)%n", lowest.courseName, lowest.average);
        }
        
        // 分析特定課程
        gradeMap.printCourseDetails("Java程式設計");
        
        // 清空測試
        System.out.println("清空所有資料:");
        gradeMap.clear();
        System.out.println("  isEmpty: " + gradeMap.courseGrades.isEmpty());
    }
}