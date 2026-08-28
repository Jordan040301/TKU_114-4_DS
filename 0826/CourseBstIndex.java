import java.util.*;

/**
 * 課程 (Course) 類別
 */
class Course {
    private String courseCode;      // 課程代碼 (Key)
    private String courseName;      // 課程名稱
    private int credit;             // 學分數 (1~6)
    private String instructor;      // 授課教師

    public Course(String courseCode, String courseName, int credit, String instructor) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
        this.instructor = instructor;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "Course{code='" + courseCode + "', name='" + courseName + 
               "', credit=" + credit + ", instructor='" + instructor + "'}";
    }

    // 格式化輸出用
    public String toFormattedString() {
        return String.format("%-10s | %-20s | %2d 學分 | %-10s", 
                             courseCode, courseName, credit, instructor);
    }
}

/**
 * 二元搜尋樹節點 (儲存 Course 物件)
 */
class CourseNode {
    Course course;
    CourseNode left;
    CourseNode right;

    public CourseNode(Course course) {
        this.course = course;
        this.left = null;
        this.right = null;
    }

    public String getKey() {
        return course.getCourseCode();
    }
}

/**
 * 課程 BST 索引
 * 以 courseCode 作為 key
 */
public class CourseBstIndex {
    private CourseNode root;
    private int size;

    public CourseBstIndex() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 新增課程 (課程代碼不可重複，學分數須在 1~6 之間)
     */
    public boolean addCourse(String courseCode, String courseName, int credit, String instructor) {
        // 檢查學分數是否在 1~6 之間
        if (credit < 1 || credit > 6) {
            System.out.println("錯誤: 學分數 " + credit + " 不在 1~6 的範圍內");
            return false;
        }

        // 檢查課程代碼是否已存在 (不可重複)
        if (findCourse(courseCode) != null) {
            System.out.println("錯誤: 課程代碼 " + courseCode + " 已存在，不可重複新增");
            return false;
        }

        Course newCourse = new Course(courseCode, courseName, credit, instructor);
        root = addRecursive(root, newCourse);
        size++;
        System.out.println("成功新增課程: " + newCourse);
        return true;
    }

    private CourseNode addRecursive(CourseNode node, Course course) {
        if (node == null) {
            return new CourseNode(course);
        }

        String key = course.getCourseCode();
        if (key.compareTo(node.getKey()) < 0) {
            node.left = addRecursive(node.left, course);
        } else if (key.compareTo(node.getKey()) > 0) {
            node.right = addRecursive(node.right, course);
        }
        // 等於的情況已在外部處理
        return node;
    }

    /**
     * 根據課程代碼尋找課程
     */
    public Course findCourse(String courseCode) {
        CourseNode result = findRecursive(root, courseCode);
        return result != null ? result.course : null;
    }

    private CourseNode findRecursive(CourseNode node, String courseCode) {
        if (node == null) {
            return null;
        }

        int cmp = courseCode.compareTo(node.getKey());
        if (cmp == 0) {
            return node;
        }

        if (cmp < 0) {
            return findRecursive(node.left, courseCode);
        } else {
            return findRecursive(node.right, courseCode);
        }
    }

    /**
     * 更新課程學分數 (學分數須在 1~6 之間)
     */
    public boolean updateCredit(String courseCode, int newCredit) {
        // 檢查學分數是否在 1~6 之間
        if (newCredit < 1 || newCredit > 6) {
            System.out.println("錯誤: 學分數 " + newCredit + " 不在 1~6 的範圍內");
            return false;
        }

        CourseNode node = findRecursive(root, courseCode);
        if (node == null) {
            System.out.println("錯誤: 找不到課程代碼 " + courseCode);
            return false;
        }

        int oldCredit = node.course.getCredit();
        node.course.setCredit(newCredit);
        System.out.println("成功更新學分: 課程 " + courseCode + " 的學分從 " + 
                           oldCredit + " 改為 " + newCredit);
        return true;
    }

    /**
     * 更新課程名稱
     */
    public boolean updateCourseName(String courseCode, String newName) {
        CourseNode node = findRecursive(root, courseCode);
        if (node == null) {
            System.out.println("錯誤: 找不到課程代碼 " + courseCode);
            return false;
        }

        String oldName = node.course.getCourseName();
        node.course.setCourseName(newName);
        System.out.println("成功更新名稱: 課程 " + courseCode + " 的名稱從 '" + 
                           oldName + "' 改為 '" + newName + "'");
        return true;
    }

    /**
     * 更新授課教師
     */
    public boolean updateInstructor(String courseCode, String newInstructor) {
        CourseNode node = findRecursive(root, courseCode);
        if (node == null) {
            System.out.println("錯誤: 找不到課程代碼 " + courseCode);
            return false;
        }

        String oldInstructor = node.course.getInstructor();
        node.course.setInstructor(newInstructor);
        System.out.println("成功更新教師: 課程 " + courseCode + " 的教師從 '" + 
                           oldInstructor + "' 改為 '" + newInstructor + "'");
        return true;
    }

    /**
     * 移除課程
     */
    public boolean removeCourse(String courseCode) {
        if (!contains(courseCode)) {
            System.out.println("錯誤: 找不到課程代碼 " + courseCode);
            return false;
        }

        Course removedCourse = findCourse(courseCode);
        root = removeRecursive(root, courseCode);
        size--;
        System.out.println("成功移除課程: " + removedCourse);
        return true;
    }

    private CourseNode removeRecursive(CourseNode node, String courseCode) {
        if (node == null) {
            return null;
        }

        int cmp = courseCode.compareTo(node.getKey());
        if (cmp < 0) {
            node.left = removeRecursive(node.left, courseCode);
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, courseCode);
        } else {
            // 找到要刪除的節點

            // Case 1: 葉節點
            if (node.left == null && node.right == null) {
                return null;
            }

            // Case 2: 只有一個子節點
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            // Case 3: 有兩個子節點
            String successorKey = findMinKey(node.right);
            CourseNode successorNode = findRecursive(node.right, successorKey);
            node.course = successorNode.course;
            node.right = removeRecursive(node.right, successorKey);
        }
        return node;
    }

    private String findMinKey(CourseNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.getKey();
    }

    /**
     * 檢查課程是否存在
     */
    public boolean contains(String courseCode) {
        return findCourse(courseCode) != null;
    }

    /**
     * 取得樹的大小
     */
    public int getSize() {
        return size;
    }

    /**
     * 中序走訪 (按課程代碼排序)
     */
    public List<Course> inorderReport() {
        List<Course> result = new ArrayList<>();
        inorderRecursive(root, result);
        return result;
    }

    private void inorderRecursive(CourseNode node, List<Course> result) {
        if (node != null) {
            inorderRecursive(node.left, result);
            result.add(node.course);
            inorderRecursive(node.right, result);
        }
    }

    /**
     * 課程代碼範圍查詢 (Range Query)
     * 回傳 courseCode 在 [low, high] 範圍內的所有課程 (按 code 排序)
     */
    public List<Course> rangeQuery(String low, String high) {
        List<Course> result = new ArrayList<>();
        // 處理 low > high 的情況
        if (low.compareTo(high) > 0) {
            System.out.println("警告: low (" + low + ") > high (" + high + ")，範圍無效");
            return result;
        }
        rangeQueryRecursive(root, low, high, result);
        return result;
    }

    private void rangeQueryRecursive(CourseNode node, String low, String high, List<Course> result) {
        if (node == null) {
            return;
        }

        // 剪枝策略
        // 如果當前節點的 key > low，左子樹可能有符合條件的節點
        if (node.getKey().compareTo(low) > 0) {
            rangeQueryRecursive(node.left, low, high, result);
        }

        // 檢查當前節點是否在範圍內
        if (node.getKey().compareTo(low) >= 0 && node.getKey().compareTo(high) <= 0) {
            result.add(node.course);
        }

        // 如果當前節點的 key < high，右子樹可能有符合條件的節點
        if (node.getKey().compareTo(high) < 0) {
            rangeQueryRecursive(node.right, low, high, result);
        }
    }

    /**
     * 印出排序報表
     */
    public void printSortedReport() {
        List<Course> courses = inorderReport();
        System.out.println("===== 課程排序報表 (按課程代碼排序) =====");
        System.out.println("總課程數: " + size);
        if (courses.isEmpty()) {
            System.out.println("(尚無課程)");
        } else {
            System.out.println("課程代碼    | 課程名稱              | 學分 | 授課教師");
            System.out.println("------------+----------------------+------+------------");
            for (Course c : courses) {
                System.out.println(c.toFormattedString());
            }
        }
        System.out.println("=============================================");
    }

    /**
     * 印出範圍查詢結果
     */
    public void printRangeQuery(String low, String high) {
        List<Course> result = rangeQuery(low, high);
        System.out.println("===== 範圍查詢: [" + low + ", " + high + "] =====");
        System.out.println("符合課程數: " + result.size());
        if (result.isEmpty()) {
            System.out.println("(無符合條件的課程)");
        } else {
            System.out.println("課程代碼    | 課程名稱              | 學分 | 授課教師");
            System.out.println("------------+----------------------+------+------------");
            for (Course c : result) {
                System.out.println(c.toFormattedString());
            }
        }
        System.out.println("=============================================");
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       課程 BST 索引系統");
        System.out.println("========================================\n");

        CourseBstIndex courseIndex = new CourseBstIndex();

        // ========== 測試新增課程 ==========
        System.out.println("【測試一】新增課程");
        System.out.println("----------------------------------------");

        courseIndex.addCourse("CS101", "程式設計入門", 3, "張教授");
        courseIndex.addCourse("CS201", "資料結構", 3, "李教授");
        courseIndex.addCourse("CS301", "演算法", 3, "王教授");
        courseIndex.addCourse("MA101", "微積分", 4, "陳教授");
        courseIndex.addCourse("MA201", "線性代數", 3, "林教授");
        courseIndex.addCourse("PH101", "普通物理", 4, "黃教授");
        courseIndex.addCourse("EE101", "電路學", 3, "劉教授");
        courseIndex.addCourse("CS401", "作業系統", 3, "吳教授");
        courseIndex.addCourse("CS501", "資料庫系統", 3, "鄭教授");
        courseIndex.addCourse("CS601", "計算機網路", 3, "蔡教授");
        System.out.println();

        courseIndex.printSortedReport();
        System.out.println();

        // ========== 測試重複課程代碼 ==========
        System.out.println("【測試二】測試重複課程代碼 (不可重複)");
        System.out.println("----------------------------------------");
        courseIndex.addCourse("CS101", "重複測試", 3, "測試教師");
        System.out.println();

        // ========== 測試學分數限制 ==========
        System.out.println("【測試三】測試學分數限制 (1~6)");
        System.out.println("----------------------------------------");
        courseIndex.addCourse("CS999", "學分測試1", 0, "測試教師");
        courseIndex.addCourse("CS998", "學分測試2", 7, "測試教師");
        courseIndex.addCourse("CS997", "學分測試3", -1, "測試教師");
        System.out.println();

        // ========== 測試尋找課程 ==========
        System.out.println("【測試四】尋找課程");
        System.out.println("----------------------------------------");
        String searchCode = "CS301";
        Course found = courseIndex.findCourse(searchCode);
        System.out.println("尋找課程 " + searchCode + ": " + (found != null ? found : "找不到"));
        
        searchCode = "CS999";
        found = courseIndex.findCourse(searchCode);
        System.out.println("尋找課程 " + searchCode + ": " + (found != null ? found : "找不到"));
        System.out.println();

        // ========== 測試更新學分 ==========
        System.out.println("【測試五】更新學分");
        System.out.println("----------------------------------------");
        courseIndex.updateCredit("CS101", 4);
        courseIndex.updateCredit("MA101", 3);
        courseIndex.updateCredit("CS301", 2);
        System.out.println();

        // 測試更新不存在的課程
        courseIndex.updateCredit("CS999", 3);
        System.out.println();

        // 測試更新為無效學分
        courseIndex.updateCredit("CS101", 0);
        courseIndex.updateCredit("CS101", 7);
        System.out.println();

        // ========== 測試更新課程名稱 ==========
        System.out.println("【測試六】更新課程名稱");
        System.out.println("----------------------------------------");
        courseIndex.updateCourseName("CS101", "程式設計基礎");
        courseIndex.updateCourseName("MA101", "微積分(一)");
        System.out.println();

        // ========== 測試更新授課教師 ==========
        System.out.println("【測試七】更新授課教師");
        System.out.println("----------------------------------------");
        courseIndex.updateInstructor("CS201", "張教授");
        courseIndex.updateInstructor("EE101", "陳教授");
        System.out.println();

        courseIndex.printSortedReport();
        System.out.println();

        // ========== 測試範圍查詢 ==========
        System.out.println("【測試八】課程代碼範圍查詢");
        System.out.println("----------------------------------------");

        courseIndex.printRangeQuery("CS101", "CS301");
        System.out.println();

        courseIndex.printRangeQuery("CS401", "CS999");
        System.out.println();

        courseIndex.printRangeQuery("MA101", "PH101");
        System.out.println();

        courseIndex.printRangeQuery("AA000", "ZZ999");
        System.out.println();

        courseIndex.printRangeQuery("CS999", "CS100");  // low > high
        System.out.println();

        // ========== 測試移除課程 ==========
        System.out.println("【測試九】移除課程");
        System.out.println("----------------------------------------");

        // 移除葉節點 (例如 EE101)
        courseIndex.removeCourse("EE101");
        System.out.println();

        // 移除只有一個子節點的節點
        courseIndex.removeCourse("MA201");
        System.out.println();

        // 移除有兩個子節點的節點 (例如 CS201)
        courseIndex.removeCourse("CS201");
        System.out.println();

        // 測試移除不存在的課程
        courseIndex.removeCourse("CS999");
        System.out.println();

        courseIndex.printSortedReport();
        System.out.println();

        // ========== 新增更多課程測試 ==========
        System.out.println("【測試十】新增更多課程");
        System.out.println("----------------------------------------");
        courseIndex.addCourse("CS701", "人工智慧", 3, "王教授");
        courseIndex.addCourse("CS702", "機器學習", 3, "李教授");
        courseIndex.addCourse("MA301", "離散數學", 3, "林教授");
        courseIndex.addCourse("PH201", "電磁學", 4, "黃教授");
        System.out.println();

        courseIndex.printSortedReport();
        System.out.println();

        // ========== 最終測試 ==========
        System.out.println("【測試十一】綜合操作測試");
        System.out.println("----------------------------------------");
        System.out.println("當前課程數: " + courseIndex.getSize());
        System.out.println("更新 CS401 的學分為 4");
        courseIndex.updateCredit("CS401", 4);
        System.out.println("更新 CS501 的名稱為 '資料庫系統設計'");
        courseIndex.updateCourseName("CS501", "資料庫系統設計");
        System.out.println("查詢範圍 [CS301, CS601]");
        courseIndex.printRangeQuery("CS301", "CS601");
        System.out.println();

        System.out.println("========================================");
        System.out.println("         課程索引系統執行完畢！");
        System.out.println("========================================");

        System.out.println("\n【功能總結】");
        System.out.println("1. 新增課程 (addCourse): 使用 courseCode 作為 key，學分數限制 1~6");
        System.out.println("2. 尋找課程 (findCourse): 根據 courseCode 快速尋找");
        System.out.println("3. 更新學分 (updateCredit): 更新指定課程的學分數，限制 1~6");
        System.out.println("4. 更新課程名稱 (updateCourseName): 更新指定課程的名稱");
        System.out.println("5. 更新授課教師 (updateInstructor): 更新指定課程的教師");
        System.out.println("6. 移除課程 (removeCourse): 根據 courseCode 移除課程 (支援三種刪除情況)");
        System.out.println("7. 範圍查詢 (rangeQuery): 查詢課程代碼在 [low, high] 範圍內的所有課程");
        System.out.println("8. 排序報表 (printSortedReport): 按課程代碼排序輸出所有課程");
    }
}