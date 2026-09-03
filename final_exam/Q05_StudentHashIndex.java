import java.util.*;

public class Q05_StudentHashIndex {
    // 學生 -> 課程集合 (正向索引)
    private Map<String, Set<String>> studentCourses;
    // 課程 -> 學生集合 (反向索引)
    private Map<String, Set<String>> courseStudents;

    public Q05_StudentHashIndex() {
        this.studentCourses = new HashMap<>();
        this.courseStudents = new HashMap<>();
    }

    /**
     * 學生註冊課程
     * @param studentId 學生ID
     * @param courseId 課程ID
     * @return 註冊成功返回 true，失敗返回 false
     */
    public boolean enroll(String studentId, String courseId) {
        // 輸入正規化：trim 並轉大寫
        String normalizedStudent = normalize(studentId);
        String normalizedCourse = normalize(courseId);
        
        // 檢查 null 或 blank
        if (isBlank(normalizedStudent) || isBlank(normalizedCourse)) {
            return false;
        }

        // 檢查是否已存在該選課記錄
        Set<String> courses = studentCourses.get(normalizedStudent);
        if (courses != null && courses.contains(normalizedCourse)) {
            return false; // 重複選課返回 false
        }

        // 更新正向索引：學生 -> 課程
        if (courses == null) {
            courses = new HashSet<>();
            studentCourses.put(normalizedStudent, courses);
        }
        courses.add(normalizedCourse);

        // 更新反向索引：課程 -> 學生
        Set<String> students = courseStudents.get(normalizedCourse);
        if (students == null) {
            students = new HashSet<>();
            courseStudents.put(normalizedCourse, students);
        }
        students.add(normalizedStudent);

        return true;
    }

    /**
     * 學生退選課程
     * @param studentId 學生ID
     * @param courseId 課程ID
     * @return 退選成功返回 true，失敗返回 false
     */
    public boolean drop(String studentId, String courseId) {
        // 輸入正規化
        String normalizedStudent = normalize(studentId);
        String normalizedCourse = normalize(courseId);
        
        // 檢查 null 或 blank
        if (isBlank(normalizedStudent) || isBlank(normalizedCourse)) {
            return false;
        }

        // 檢查是否已存在該選課記錄
        Set<String> courses = studentCourses.get(normalizedStudent);
        if (courses == null || !courses.contains(normalizedCourse)) {
            return false; // 不存在該記錄
        }

        // 從正向索引移除
        courses.remove(normalizedCourse);
        // 如果學生沒有其他課程，移除該學生鍵
        if (courses.isEmpty()) {
            studentCourses.remove(normalizedStudent);
        }

        // 從反向索引移除
        Set<String> students = courseStudents.get(normalizedCourse);
        if (students != null) {
            students.remove(normalizedStudent);
            // 如果課程沒有其他學生，移除該課程鍵
            if (students.isEmpty()) {
                courseStudents.remove(normalizedCourse);
            }
        }

        return true;
    }

    /**
     * 查詢某學生選修的所有課程
     * @param studentId 學生ID
     * @return 不可修改的課程集合，不存在則返回空集合
     */
    public Set<String> coursesOf(String studentId) {
        String normalizedStudent = normalize(studentId);
        if (isBlank(normalizedStudent)) {
            return Collections.emptySet();
        }

        Set<String> courses = studentCourses.get(normalizedStudent);
        if (courses == null) {
            return Collections.emptySet();
        }
        
        // 返回不可修改的獨立 Set 副本
        return Collections.unmodifiableSet(new HashSet<>(courses));
    }

    /**
     * 查詢某課程的所有學生
     * @param courseId 課程ID
     * @return 不可修改的學生集合，不存在則返回空集合
     */
    public Set<String> studentsIn(String courseId) {
        String normalizedCourse = normalize(courseId);
        if (isBlank(normalizedCourse)) {
            return Collections.emptySet();
        }

        Set<String> students = courseStudents.get(normalizedCourse);
        if (students == null) {
            return Collections.emptySet();
        }
        
        // 返回不可修改的獨立 Set 副本
        return Collections.unmodifiableSet(new HashSet<>(students));
    }

    /**
     * 返回總選課記錄數（學生-課程配對總數）
     * @return 選課總數
     */
    public int enrollmentCount() {
        int count = 0;
        for (Set<String> courses : studentCourses.values()) {
            count += courses.size();
        }
        return count;
    }

    /**
     * 輸入正規化：trim 並轉大寫
     */
    private String normalize(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().toUpperCase();
    }

    /**
     * 檢查是否為 null 或空白字串
     */
    private boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    // 測試代碼
    public static void main(String[] args) {
        Q05_StudentHashIndex index = new Q05_StudentHashIndex();

        // 測試 enroll
        System.out.println("=== Test enroll ===");
        System.out.println("Enroll S001, C001: " + index.enroll("S001", "C001"));
        System.out.println("Enroll S001, C002: " + index.enroll("S001", "C002"));
        System.out.println("Enroll S002, C001: " + index.enroll("S002", "C001"));
        System.out.println("Enroll S001, C001 (duplicate): " + index.enroll("S001", "C001")); // false
        System.out.println("Enroll null, C001: " + index.enroll(null, "C001")); // false
        System.out.println("Enroll S001, '  ': " + index.enroll("S001", "  ")); // false

        // 測試查詢
        System.out.println("\n=== Test queries ===");
        System.out.println("Courses of S001: " + index.coursesOf("S001"));
        System.out.println("Students in C001: " + index.studentsIn("C001"));
        System.out.println("Total enrollments: " + index.enrollmentCount());

        // 測試正規化（大小寫、空白）
        System.out.println("\n=== Test normalization ===");
        System.out.println("Enroll ' s001 ', ' c001 ' (with spaces): " + 
                          index.enroll(" s001 ", " c001 ")); // false (already enrolled)
        System.out.println("Courses of 's001' (lowercase): " + index.coursesOf("s001"));
        System.out.println("Students in 'c001' (lowercase): " + index.studentsIn("c001"));

        // 測試 drop
        System.out.println("\n=== Test drop ===");
        System.out.println("Drop S001, C001: " + index.drop("S001", "C001"));
        System.out.println("Courses of S001: " + index.coursesOf("S001"));
        System.out.println("Students in C001: " + index.studentsIn("C001"));
        System.out.println("Drop S001, C001 (already dropped): " + index.drop("S001", "C001")); // false
        System.out.println("Total enrollments: " + index.enrollmentCount());

        // 測試自動移除空鍵
        System.out.println("\n=== Test auto-remove empty keys ===");
        System.out.println("Drop S001, C002: " + index.drop("S001", "C002"));
        System.out.println("Courses of S001: " + index.coursesOf("S001")); // 空集合
        System.out.println("Students in C001: " + index.studentsIn("C001")); // 應該還有 S002

        // 測試查詢不存在
        System.out.println("\n=== Test non-existent queries ===");
        System.out.println("Courses of S999: " + index.coursesOf("S999")); // 空集合
        System.out.println("Students in C999: " + index.studentsIn("C999")); // 空集合

        // 測試不可修改性
        System.out.println("\n=== Test immutability ===");
        Set<String> courses = index.coursesOf("S002");
        System.out.println("Courses of S002 before: " + courses);
        try {
            courses.add("C999"); // 應該拋出異常
            System.out.println("Modified! (should not happen)");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify returned set (as expected)");
        }
    }
}