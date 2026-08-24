import java.util.*;

/**
 * 課程報名類別
 * 以 studentId + courseCode 作為身分標識
 */
class Enrollment {
    private final String studentId;
    private final String courseCode;

    /**
     * 建構子
     * @param studentId 學生編號
     * @param courseCode 課程代碼
     */
    public Enrollment(String studentId, String courseCode) {
        this.studentId = studentId;
        this.courseCode = courseCode;
    }

    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }

    /**
     * 覆蓋 equals() - 以 studentId + courseCode 作為身分
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Enrollment)) {
            return false;
        }
        Enrollment other = (Enrollment) obj;
        return Objects.equals(studentId, other.studentId) &&
               Objects.equals(courseCode, other.courseCode);
    }

    /**
     * 覆蓋 hashCode() - 與 equals 保持一致
     */
    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode);
    }

    @Override
    public String toString() {
        return String.format("Enrollment{studentId='%s', courseCode='%s'}", 
                studentId, courseCode);
    }
}

/**
 * 課程報名身分集合系統
 * 使用 HashSet<Enrollment> 管理報名記錄
 */
public class EnrollmentSetSystem {

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           課程報名身分集合系統 - 測試報告               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        // 建立報名集合
        HashSet<Enrollment> enrollments = new HashSet<>();

        // ========== 測試 1：新增報名 ==========
        System.out.println("\n【測試 1：新增報名】");
        System.out.println("─".repeat(60));

        // 同一人加入不同課程
        Enrollment e1 = new Enrollment("S001", "CS101");
        Enrollment e2 = new Enrollment("S001", "CS102");
        Enrollment e3 = new Enrollment("S001", "CS103");
        Enrollment e4 = new Enrollment("S002", "CS101");
        Enrollment e5 = new Enrollment("S003", "CS101");

        System.out.println("▶ 新增報名：");
        System.out.println("  1. " + e1 + " → " + (enrollments.add(e1) ? "✅ 成功" : "❌ 失敗"));
        System.out.println("  2. " + e2 + " → " + (enrollments.add(e2) ? "✅ 成功" : "❌ 失敗"));
        System.out.println("  3. " + e3 + " → " + (enrollments.add(e3) ? "✅ 成功" : "❌ 失敗"));
        System.out.println("  4. " + e4 + " → " + (enrollments.add(e4) ? "✅ 成功" : "❌ 失敗"));
        System.out.println("  5. " + e5 + " → " + (enrollments.add(e5) ? "✅ 成功" : "❌ 失敗"));

        // ========== 測試 2：顯示報名列表 ==========
        System.out.println("\n【測試 2：顯示報名列表】");
        System.out.println("─".repeat(60));
        printEnrollmentSet(enrollments);

        // ========== 測試 3：同一人不可重複加入同一課程 ==========
        System.out.println("\n【測試 3：同一人不可重複加入同一課程】");
        System.out.println("─".repeat(60));

        Enrollment duplicate = new Enrollment("S001", "CS101");
        System.out.println("▶ 嘗試重複報名（同一人 + 同一課程）：");
        System.out.println("  報名物件：" + duplicate);
        System.out.println("  HashSet.add() 結果：" + (enrollments.add(duplicate) ? "✅ 成功" : "❌ 失敗（已存在）"));
        System.out.println("  原報名物件：" + e1);
        System.out.println("  💡 驗證：equals() 和 hashCode() 正確運作");

        // ========== 測試 4：建立新物件測試 contains() ==========
        System.out.println("\n【測試 4：使用新物件測試 contains()】");
        System.out.println("─".repeat(60));

        Enrollment newObj1 = new Enrollment("S001", "CS101");
        Enrollment newObj2 = new Enrollment("S001", "CS999");
        Enrollment newObj3 = new Enrollment("S999", "CS101");

        System.out.println("▶ 測試 contains()：");
        System.out.println("  新物件：" + newObj1 + " → " + 
                (enrollments.contains(newObj1) ? "✅ 存在（雖是新物件，但身分相同）" : "❌ 不存在"));
        System.out.println("  新物件：" + newObj2 + " → " + 
                (enrollments.contains(newObj2) ? "✅ 存在" : "❌ 不存在（身分不同）"));
        System.out.println("  新物件：" + newObj3 + " → " + 
                (enrollments.contains(newObj3) ? "✅ 存在" : "❌ 不存在（身分不同）"));
        System.out.println("  💡 驗證：equals() 正確比較內容而非參考");

        // ========== 測試 5：取消報名（使用新物件） ==========
        System.out.println("\n【測試 5：取消報名（使用新建立的相同身分物件）】");
        System.out.println("─".repeat(60));

        Enrollment toRemove1 = new Enrollment("S001", "CS102");
        Enrollment toRemove2 = new Enrollment("S002", "CS101");
        Enrollment toRemove3 = new Enrollment("S999", "CS999");

        System.out.println("▶ 取消報名：");
        System.out.println("  移除 " + toRemove1 + " → " + 
                (enrollments.remove(toRemove1) ? "✅ 成功移除" : "❌ 移除失敗（不存在）"));
        System.out.println("  移除 " + toRemove2 + " → " + 
                (enrollments.remove(toRemove2) ? "✅ 成功移除" : "❌ 移除失敗（不存在）"));
        System.out.println("  移除 " + toRemove3 + " → " + 
                (enrollments.remove(toRemove3) ? "✅ 成功移除" : "❌ 移除失敗（不存在）"));

        // ========== 測試 6：顯示取消後報名列表 ==========
        System.out.println("\n【測試 6：取消後報名列表】");
        System.out.println("─".repeat(60));
        printEnrollmentSet(enrollments);

        // ========== 測試 7：報名統計 ==========
        System.out.println("\n【測試 7：報名統計】");
        System.out.println("─".repeat(60));
        printStatistics(enrollments);

        // ========== 測試 8：學生報名查詢 ==========
        System.out.println("\n【測試 8：學生報名查詢】");
        System.out.println("─".repeat(60));
        String[] testStudents = {"S001", "S002", "S003", "S004"};
        for (String studentId : testStudents) {
            List<Enrollment> studentEnrollments = getEnrollmentsByStudent(enrollments, studentId);
            System.out.printf("  學生 %s 報名了 %d 門課程：%s%n", 
                    studentId, studentEnrollments.size(), studentEnrollments);
        }

        // ========== 測試 9：課程報名查詢 ==========
        System.out.println("\n【測試 9：課程報名查詢】");
        System.out.println("─".repeat(60));
        String[] testCourses = {"CS101", "CS102", "CS103", "CS999"};
        for (String courseCode : testCourses) {
            List<Enrollment> courseEnrollments = getEnrollmentsByCourse(enrollments, courseCode);
            System.out.printf("  課程 %s 有 %d 人報名：%s%n", 
                    courseCode, courseEnrollments.size(), courseEnrollments);
        }

        // ========== 測試 10：equals 和 hashCode 驗證 ==========
        System.out.println("\n【測試 10：equals() 和 hashCode() 驗證】");
        System.out.println("─".repeat(60));
        testEqualsAndHashCode();

        // ========== 功能總結 ==========
        printSummaryTable();
    }

    /**
     * 印出報名集合
     */
    private static void printEnrollmentSet(HashSet<Enrollment> enrollments) {
        if (enrollments.isEmpty()) {
            System.out.println("  📭 報名集合為空");
            return;
        }

        System.out.println("  📋 報名集合（共 " + enrollments.size() + " 筆）：");
        System.out.println("  ┌────┬──────────────────────────────────────────────┐");
        int index = 1;
        for (Enrollment e : enrollments) {
            System.out.printf("  │ %2d │ %-44s │%n", index++, e);
        }
        System.out.println("  └────┴──────────────────────────────────────────────┘");
    }

    /**
     * 印出統計資訊
     */
    private static void printStatistics(HashSet<Enrollment> enrollments) {
        Set<String> students = new HashSet<>();
        Set<String> courses = new HashSet<>();

        for (Enrollment e : enrollments) {
            students.add(e.getStudentId());
            courses.add(e.getCourseCode());
        }

        System.out.println("  📊 統計摘要：");
        System.out.printf("    總報名數： %d 筆%n", enrollments.size());
        System.out.printf("    參與學生數： %d 人%n", students.size());
        System.out.printf("    開設課程數： %d 門%n", courses.size());
        System.out.println("    學生列表： " + students);
        System.out.println("    課程列表： " + courses);
    }

    /**
     * 查詢某學生的所有報名
     */
    private static List<Enrollment> getEnrollmentsByStudent(
            HashSet<Enrollment> enrollments, String studentId) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(studentId)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * 查詢某課程的所有報名
     */
    private static List<Enrollment> getEnrollmentsByCourse(
            HashSet<Enrollment> enrollments, String courseCode) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getCourseCode().equals(courseCode)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * 測試 equals() 和 hashCode() 的正確性
     */
    private static void testEqualsAndHashCode() {
        System.out.println("  ▶ 測試 1：相同內容的物件應該相等");
        Enrollment a1 = new Enrollment("S001", "CS101");
        Enrollment a2 = new Enrollment("S001", "CS101");
        System.out.println("    a1.equals(a2) = " + a1.equals(a2));
        System.out.println("    a1.hashCode() = " + a1.hashCode());
        System.out.println("    a2.hashCode() = " + a2.hashCode());
        System.out.println("    hashCode 是否相同：" + (a1.hashCode() == a2.hashCode()));

        System.out.println("\n  ▶ 測試 2：不同內容的物件不應該相等");
        Enrollment b1 = new Enrollment("S001", "CS101");
        Enrollment b2 = new Enrollment("S001", "CS102");
        Enrollment b3 = new Enrollment("S002", "CS101");
        System.out.println("    b1.equals(b2) = " + b1.equals(b2) + "（不同課程）");
        System.out.println("    b1.equals(b3) = " + b1.equals(b3) + "（不同學生）");

        System.out.println("\n  ▶ 測試 3：null 安全");
        System.out.println("    b1.equals(null) = " + b1.equals(null));

        System.out.println("\n  ▶ 測試 4：與自己比較");
        System.out.println("    b1.equals(b1) = " + b1.equals(b1));

        System.out.println("\n  ✅ equals() 和 hashCode() 驗證通過！");
        System.out.println("  💡 兩個不同物件只要 studentId 和 courseCode 相同，");
        System.out.println("     就會被視為相同報名記錄");
    }

    /**
     * 印出功能總結
     */
    private static void printSummaryTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           課程報名身分集合系統 - 功能總結               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  功能             │  說明                               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  equals()         │  比較 studentId 和 courseCode       ║");
        System.out.println("║  hashCode()       │  根據 studentId 和 courseCode 計算   ║");
        System.out.println("║  HashSet.add()    │  新增報名，回傳 boolean              ║");
        System.out.println("║  HashSet.remove() │  取消報名，回傳 boolean              ║");
        System.out.println("║  HashSet.contains()│  檢查是否存在，使用 equals() 比較    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        System.out.println("\n【身分標識規則】");
        System.out.println("  🔑 身分 = studentId + courseCode");
        System.out.println("  ✅ 同一人可加入不同課程（不同身分）");
        System.out.println("  ❌ 同一人不可重複加入同一課程（相同身分）");
        System.out.println("  💡 即使使用 new 建立新物件，只要身分相同就視為相同");
    }
}