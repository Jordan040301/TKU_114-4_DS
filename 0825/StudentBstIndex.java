/**
 * 檔名：studentbstindex.java
 * 功能：學生索引系統（使用 BST 儲存學生資料）
 * 說明：Node 儲存 Student 物件，依 StudentId 搜尋/插入/刪除
 *       重複 ID 不得加入
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 學生類別
 */
class Student {
    private String studentId;   // 學號（唯一識別）
    private String name;        // 姓名
    private String department;  // 科系
    private int grade;          // 年級

    public Student(String studentId, String name, String department, int grade) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.grade = grade;
    }

    // Getter 方法
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public int getGrade() {
        return grade;
    }

    // Setter 方法
    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "學號：" + studentId + 
               "，姓名：" + name + 
               "，科系：" + department + 
               "，年級：" + grade;
    }
}

/**
 * BST 節點（儲存 Student 物件）
 */
class StudentNode {
    Student student;
    StudentNode left;
    StudentNode right;

    public StudentNode(Student student) {
        this.student = student;
        this.left = null;
        this.right = null;
    }

    // 取得學號（方便比較）
    public String getStudentId() {
        return student.getStudentId();
    }
}

/**
 * 學生 BST 索引
 */
class StudentBST {
    private StudentNode root;
    private int size;  // 學生總數

    public StudentBST() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 取得學生總數
     */
    public int getSize() {
        return size;
    }

    /**
     * 檢查是否為空
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * 插入學生（依 StudentId）
     * @param student 要插入的學生物件
     * @return true 表示插入成功，false 表示學號重複
     */
    public boolean insert(Student student) {
        // 先檢查學號是否已存在
        if (search(student.getStudentId()) != null) {
            System.out.println("⚠️ 插入失敗：學號 " + student.getStudentId() + " 已存在！");
            return false;
        }

        root = insertRec(root, student);
        size++;
        System.out.println("✅ 插入成功：" + student);
        return true;
    }

    private StudentNode insertRec(StudentNode node, Student student) {
        if (node == null) {
            return new StudentNode(student);
        }

        String newId = student.getStudentId();
        String currentId = node.getStudentId();

        if (newId.compareTo(currentId) < 0) {
            node.left = insertRec(node.left, student);
        } else if (newId.compareTo(currentId) > 0) {
            node.right = insertRec(node.right, student);
        }
        // 重複 ID 已在外部檢查，此處不會發生
        return node;
    }

    /**
     * 搜尋學生（依 StudentId）
     * @param studentId 要搜尋的學號
     * @return 找到的學生物件，若找不到則回傳 null
     */
    public Student search(String studentId) {
        StudentNode result = searchRec(root, studentId);
        return result != null ? result.student : null;
    }

    private StudentNode searchRec(StudentNode node, String studentId) {
        if (node == null) {
            return null;
        }

        String currentId = node.getStudentId();
        int compare = studentId.compareTo(currentId);

        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return searchRec(node.left, studentId);
        } else {
            return searchRec(node.right, studentId);
        }
    }

    /**
     * 刪除學生（依 StudentId）
     * @param studentId 要刪除的學號
     * @return 被刪除的學生物件，若找不到則回傳 null
     */
    public Student delete(String studentId) {
        // 先確認學生是否存在
        Student target = search(studentId);
        if (target == null) {
            System.out.println("⚠️ 刪除失敗：學號 " + studentId + " 不存在！");
            return null;
        }

        root = deleteRec(root, studentId);
        size--;
        System.out.println("🗑️ 刪除成功：" + target);
        return target;
    }

    private StudentNode deleteRec(StudentNode node, String studentId) {
        if (node == null) {
            return null;
        }

        String currentId = node.getStudentId();
        int compare = studentId.compareTo(currentId);

        if (compare < 0) {
            node.left = deleteRec(node.left, studentId);
        } else if (compare > 0) {
            node.right = deleteRec(node.right, studentId);
        } else {
            // 找到要刪除的節點

            // 情況 1：葉子節點
            if (node.left == null && node.right == null) {
                return null;
            }

            // 情況 2：只有右子樹
            if (node.left == null) {
                return node.right;
            }

            // 情況 3：只有左子樹
            if (node.right == null) {
                return node.left;
            }

            // 情況 4：有兩個子樹
            // 找右子樹的最小值作為繼承者
            StudentNode successor = findMin(node.right);
            node.student = successor.student;  // 複製學生資料
            node.right = deleteRec(node.right, successor.getStudentId());
        }

        return node;
    }

    /**
     * 尋找某子樹中的最小節點（最左邊的節點）
     */
    private StudentNode findMin(StudentNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * 中序遍歷（依學號排序輸出所有學生）
     */
    public void printInOrder() {
        System.out.println("===== 學生索引（依學號排序） =====");
        if (root == null) {
            System.out.println("（目前無學生資料）");
        } else {
            printInOrderRec(root);
        }
        System.out.println("=====================================");
        System.out.println("總計：" + size + " 位學生");
        System.out.println();
    }

    private void printInOrderRec(StudentNode node) {
        if (node != null) {
            printInOrderRec(node.left);
            System.out.println("  " + node.student);
            printInOrderRec(node.right);
        }
    }

    /**
     * 前序遍歷（輔助觀察樹結構）
     */
    public void printPreOrder() {
        System.out.print("前序遍歷（樹結構）：");
        preOrderRec(root);
        System.out.println();
    }

    private void preOrderRec(StudentNode node) {
        if (node != null) {
            System.out.print(node.getStudentId() + " ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }

    /**
     * 依科系查詢學生
     */
    public List<Student> searchByDepartment(String department) {
        List<Student> result = new ArrayList<>();
        searchByDepartmentRec(root, department, result);
        return result;
    }

    private void searchByDepartmentRec(StudentNode node, String department, List<Student> result) {
        if (node != null) {
            searchByDepartmentRec(node.left, department, result);
            if (node.student.getDepartment().equals(department)) {
                result.add(node.student);
            }
            searchByDepartmentRec(node.right, department, result);
        }
    }

    /**
     * 顯示樹的結構（輔助方法）
     */
    public void printTreeStructure() {
        System.out.println("樹的結構（學號）：");
        printTreeStructureRec(root, 0, "根");
        System.out.println();
    }

    private void printTreeStructureRec(StudentNode node, int level, String direction) {
        if (node == null) {
            return;
        }
        String indent = "  ".repeat(level);
        System.out.println(indent + direction + ": " + node.getStudentId() + 
                           " (" + node.student.getName() + ")");
        printTreeStructureRec(node.left, level + 1, "左");
        printTreeStructureRec(node.right, level + 1, "右");
    }
}

/**
 * 主程式
 */
public class StudentBstIndex {
    public static void main(String[] args) {
        StudentBST bst = new StudentBST();

        System.out.println("=========================================");
        System.out.println("       學生索引系統（Student BST Index）");
        System.out.println("=========================================");
        System.out.println();

        // =========================================================
        // 測試一：插入學生
        // =========================================================
        System.out.println("【測試一：插入學生】");
        System.out.println("-----------------------------------------");

        // 建立學生資料
        Student s1 = new Student("B10721001", "王小明", "資訊工程學系", 4);
        Student s2 = new Student("B10721002", "陳小華", "資訊管理學系", 3);
        Student s3 = new Student("B10721003", "林小美", "資訊工程學系", 2);
        Student s4 = new Student("B10721004", "張小強", "電機工程學系", 4);
        Student s5 = new Student("B10721005", "李小雨", "資訊管理學系", 1);
        Student s6 = new Student("B10721006", "黃小光", "資訊工程學系", 3);

        // 插入學生
        bst.insert(s1);
        bst.insert(s2);
        bst.insert(s3);
        bst.insert(s4);
        bst.insert(s5);
        bst.insert(s6);

        System.out.println();
        bst.printInOrder();
        bst.printTreeStructure();

        // =========================================================
        // 測試二：嘗試插入重複學號
        // =========================================================
        System.out.println("【測試二：嘗試插入重複學號】");
        System.out.println("-----------------------------------------");

        Student duplicate = new Student("B10721002", "趙小華", "資訊管理學系", 3);
        bst.insert(duplicate);

        System.out.println();

        // =========================================================
        // 測試三：搜尋學生
        // =========================================================
        System.out.println("【測試三：搜尋學生】");
        System.out.println("-----------------------------------------");

        // 搜尋存在的學號
        Student found = bst.search("B10721003");
        if (found != null) {
            System.out.println("🔍 搜尋學號 B10721003 → 找到：" + found);
        } else {
            System.out.println("🔍 搜尋學號 B10721003 → 找不到");
        }

        // 搜尋不存在的學號
        Student notFound = bst.search("B10721099");
        if (notFound != null) {
            System.out.println("🔍 搜尋學號 B10721099 → 找到：" + notFound);
        } else {
            System.out.println("🔍 搜尋學號 B10721099 → 找不到");
        }

        System.out.println();

        // =========================================================
        // 測試四：依科系查詢
        // =========================================================
        System.out.println("【測試四：依科系查詢】");
        System.out.println("-----------------------------------------");

        List<Student> csStudents = bst.searchByDepartment("資訊工程學系");
        System.out.println("資訊工程學系學生：");
        for (Student s : csStudents) {
            System.out.println("  " + s);
        }

        System.out.println();

        List<Student> misStudents = bst.searchByDepartment("資訊管理學系");
        System.out.println("資訊管理學系學生：");
        for (Student s : misStudents) {
            System.out.println("  " + s);
        }

        System.out.println();

        // =========================================================
        // 測試五：刪除學生
        // =========================================================
        System.out.println("【測試五：刪除學生】");
        System.out.println("-----------------------------------------");

        // 刪除葉子節點
        bst.delete("B10721006");
        bst.printInOrder();

        // 刪除單子節點
        bst.delete("B10721004");
        bst.printInOrder();

        // 刪除二子節點
        bst.delete("B10721002");
        bst.printInOrder();

        // 刪除不存在的學號
        bst.delete("B10721099");

        System.out.println();

        // =========================================================
        // 測試六：顯示最終結果
        // =========================================================
        System.out.println("【測試六：最終學生索引】");
        System.out.println("-----------------------------------------");
        bst.printInOrder();
        bst.printTreeStructure();

        // =========================================================
        // 總結報告
        // =========================================================
        System.out.println("=========================================");
        System.out.println("        📊 操作總結");
        System.out.println("=========================================");
        System.out.println("初始插入學生數：" + 6);
        System.out.println("嘗試重複插入：" + 1 + "（被拒絕）");
        System.out.println("成功刪除學生數：" + 3);
        System.out.println("最終學生總數：" + bst.getSize());
        System.out.println("=========================================");
    }
}