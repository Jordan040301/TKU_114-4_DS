/**
 * 檔名：Q12_StudentBstSystem.java
 * 功能：學籍 BST 綜合系統
 * 說明：Student 資料驗證、BST 以 id 為 key
 *       支援新增、查詢、更新、刪除、範圍查詢、中序走訪
 */

import java.util.*;

public class Q12_StudentBstSystem {

    // ========== Student 類別 ==========
    public static class Student {
        private final int id;        // 學生編號（不可變）
        private String name;         // 姓名
        private int score;           // 分數

        /**
         * 建構子
         * @param id 學生編號（必須大於 0）
         * @param name 姓名（不得為 null 或 blank）
         * @param score 分數（小於 0 時存為 0，大於 100 時存為 100）
         * @throws IllegalArgumentException 當 id <= 0 或 name 為 null/blank 時
         */
        public Student(int id, String name, int score) {
            // id 必須大於 0
            if (id <= 0) {
                throw new IllegalArgumentException("id 必須大於 0");
            }

            // name 不得為 null 或 blank
            String trimmedName = (name != null) ? name.trim() : null;
            if (trimmedName == null || trimmedName.isEmpty()) {
                throw new IllegalArgumentException("name 不得為 null 或空白字串");
            }

            this.id = id;
            this.name = trimmedName;
            // score 小於 0 時存為 0，大於 100 時存為 100
            this.score = clampScore(score);
        }

        /**
         * 將分數限制在 0 到 100 之間
         */
        private static int clampScore(int score) {
            if (score < 0) return 0;
            if (score > 100) return 100;
            return score;
        }

        /**
         * 取得學生編號
         */
        public int getId() {
            return id;
        }

        /**
         * 取得姓名
         */
        public String getName() {
            return name;
        }

        /**
         * 取得分數
         */
        public int getScore() {
            return score;
        }

        /**
         * 設定姓名
         */
        public void setName(String name) {
            String trimmedName = (name != null) ? name.trim() : null;
            if (trimmedName == null || trimmedName.isEmpty()) {
                throw new IllegalArgumentException("name 不得為 null 或空白字串");
            }
            this.name = trimmedName;
        }

        /**
         * 設定分數（自動限制在 0 到 100）
         */
        public void setScore(int score) {
            this.score = clampScore(score);
        }

        /**
         * 格式化輸出：id|name|score
         */
        @Override
        public String toString() {
            return id + "|" + name + "|" + score;
        }
    }

    // ========== BST Node 類別 ==========
    private static class Node {
        Student student;
        Node left;
        Node right;

        Node(Student student) {
            this.student = student;
            this.left = null;
            this.right = null;
        }

        int getId() {
            return student.getId();
        }
    }

    // ========== BST 欄位 ==========
    private Node root;
    private int size;

    public Q12_StudentBstSystem() {
        this.root = null;
        this.size = 0;
    }

    // ========== 1. add() - 新增學生 ==========

    /**
     * 新增學生（以 id 為 key）
     * @param student 要新增的學生
     * @return true 表示新增成功，false 表示 student 為 null 或 id 重複
     */
    public boolean add(Student student) {
        // add(null) 回傳 false
        if (student == null) {
            return false;
        }

        // 檢查 duplicate id
        if (find(student.getId()) != null) {
            return false;
        }

        root = addRec(root, student);
        size++;
        return true;
    }

    private Node addRec(Node node, Student student) {
        if (node == null) {
            return new Node(student);
        }

        if (student.getId() < node.getId()) {
            node.left = addRec(node.left, student);
        } else if (student.getId() > node.getId()) {
            node.right = addRec(node.right, student);
        }
        // duplicate 已在外部檢查
        return node;
    }

    // ========== 2. find() - 查詢學生 ==========

    /**
     * 依 id 查詢學生
     * @param id 學生編號
     * @return 找到的學生，找不到回傳 null
     */
    public Student find(int id) {
        Node result = findRec(root, id);
        return result != null ? result.student : null;
    }

    private Node findRec(Node node, int id) {
        if (node == null) {
            return null;
        }

        if (id == node.getId()) {
            return node;
        } else if (id < node.getId()) {
            return findRec(node.left, id);
        } else {
            return findRec(node.right, id);
        }
    }

    // ========== 3. updateScore() - 更新分數 ==========

    /**
     * 更新學生分數
     * @param id 學生編號
     * @param score 新分數（自動限制在 0 到 100）
     * @return true 表示更新成功，false 表示找不到該學生
     */
    public boolean updateScore(int id, int score) {
        Student student = find(id);
        if (student == null) {
            return false;
        }
        // score 限制在 0 到 100
        student.setScore(score);
        return true;
    }

    // ========== 4. remove() - 刪除學生（三種刪除情況） ==========

    /**
     * 刪除學生（支援 leaf、one child、two children）
     * @param id 要刪除的學生編號
     * @return true 表示刪除成功，false 表示找不到
     */
    public boolean remove(int id) {
        if (find(id) == null) {
            return false;
        }

        root = removeRec(root, id);
        size--;
        return true;
    }

    private Node removeRec(Node node, int id) {
        if (node == null) {
            return null;
        }

        if (id < node.getId()) {
            node.left = removeRec(node.left, id);
        } else if (id > node.getId()) {
            node.right = removeRec(node.right, id);
        } else {
            // 找到要刪除的節點

            // === 情況 1：葉子節點 ===
            if (node.left == null && node.right == null) {
                return null;
            }

            // === 情況 2：只有一個子節點 ===
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            // === 情況 3：有兩個子節點 ===
            // 使用 right subtree minimum
            Node successor = findMin(node.right);
            node.student = successor.student;
            node.right = removeRec(node.right, successor.getId());
        }

        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // ========== 5. studentsBetween() - 範圍查詢 ==========

    /**
     * 查詢 id 在 [lowId, highId] 範圍內的學生（依 id 升序）
     * @param lowId 範圍下限（包含）
     * @param highId 範圍上限（包含）
     * @return 符合條件的學生列表（依 id 升序）
     */
    public List<Student> studentsBetween(int lowId, int highId) {
        // lowId > highId 回傳 empty List
        if (lowId > highId) {
            return Collections.emptyList();
        }

        List<Student> result = new ArrayList<>();
        studentsBetweenRec(root, lowId, highId, result);
        return Collections.unmodifiableList(result);
    }

    private void studentsBetweenRec(Node node, int lowId, int highId, List<Student> result) {
        if (node == null) {
            return;
        }

        // 利用 BST 特性剪枝
        if (node.getId() > lowId) {
            studentsBetweenRec(node.left, lowId, highId, result);
        }

        if (node.getId() >= lowId && node.getId() <= highId) {
            result.add(node.student);
        }

        if (node.getId() < highId) {
            studentsBetweenRec(node.right, lowId, highId, result);
        }
    }

    // ========== 6. inorder() - 中序走訪 ==========

    /**
     * 依 id 升序回傳所有學生
     * @return 所有學生列表（依 id 升序）
     */
    public List<Student> inorder() {
        List<Student> result = new ArrayList<>();
        inorderRec(root, result);
        return Collections.unmodifiableList(result);
    }

    private void inorderRec(Node node, List<Student> result) {
        if (node == null) {
            return;
        }
        inorderRec(node.left, result);
        result.add(node.student);
        inorderRec(node.right, result);
    }

    // ========== 7. toString() - 輸出樹結構 ==========

    /**
     * 輸出樹的結構（輔助觀察）
     * 格式：tree [id] | name| score, ...
     */
    @Override
    public String toString() {
        if (root == null) {
            return "tree";
        }
        StringBuilder sb = new StringBuilder("tree");
        toStringRec(root, sb);
        return sb.toString();
    }

    private void toStringRec(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        toStringRec(node.left, sb);
        sb.append(" [").append(node.getId()).append("] ")
          .append(node.student.getName()).append("|")
          .append(node.student.getScore());
        toStringRec(node.right, sb);
    }

    // ========== 輔助方法 ==========

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(int id) {
        return find(id) != null;
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試範例 =====");
        Q12_StudentBstSystem system = new Q12_StudentBstSystem();
        system.add(new Q12_StudentBstSystem.Student(300, "Alice", 75));
        system.add(new Q12_StudentBstSystem.Student(100, "Bob", 54));
        system.add(new Q12_StudentBstSystem.Student(200, "Carol", 105));  // 105 → 100
        system.add(new Q12_StudentBstSystem.Student(300, "Daisy", 80));   // duplicate → false
        System.out.println("系統: " + system);
        System.out.println("inorder: " + system.inorder());
        System.out.println();

        // ===== 測試 Student 建構子驗證 =====
        System.out.println("===== Student 建構子驗證測試 =====");

        try {
            new Q12_StudentBstSystem.Student(0, "Test", 50);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ id=0 拋出例外: " + e.getMessage());
        }

        try {
            new Q12_StudentBstSystem.Student(-1, "Test", 50);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ id=-1 拋出例外: " + e.getMessage());
        }

        try {
            new Q12_StudentBstSystem.Student(1, null, 50);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ name=null 拋出例外: " + e.getMessage());
        }

        try {
            new Q12_StudentBstSystem.Student(1, "", 50);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ name=空字串 拋出例外: " + e.getMessage());
        }

        try {
            new Q12_StudentBstSystem.Student(1, "   ", 50);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ name=空白 拋出例外: " + e.getMessage());
        }
        System.out.println();

        // ===== 測試 Score 邊界 =====
        System.out.println("===== Score 邊界測試 =====");
        Q12_StudentBstSystem.Student s1 = new Q12_StudentBstSystem.Student(10, "Test1", -5);
        System.out.println("score=-5 → " + s1.getScore());  // 0

        Q12_StudentBstSystem.Student s2 = new Q12_StudentBstSystem.Student(11, "Test2", 150);
        System.out.println("score=150 → " + s2.getScore());  // 100

        Q12_StudentBstSystem.Student s3 = new Q12_StudentBstSystem.Student(12, "Test3", 75);
        System.out.println("score=75 → " + s3.getScore());   // 75
        System.out.println();

        // ===== 測試 add、find、duplicate =====
        System.out.println("===== Add、Find、Duplicate 測試 =====");
        Q12_StudentBstSystem system2 = new Q12_StudentBstSystem();

        Student alice = new Q12_StudentBstSystem.Student(100, "Alice", 85);
        Student bob = new Q12_StudentBstSystem.Student(200, "Bob", 90);
        Student carol = new Q12_StudentBstSystem.Student(300, "Carol", 78);
        Student dupAlice = new Q12_StudentBstSystem.Student(100, "Dup", 95);

        System.out.println("add(Alice) → " + system2.add(alice));      // true
        System.out.println("add(Bob) → " + system2.add(bob));          // true
        System.out.println("add(Carol) → " + system2.add(carol));      // true
        System.out.println("add(Dup Alice) → " + system2.add(dupAlice)); // false
        System.out.println("size: " + system2.size());                 // 3

        System.out.println("find(100): " + system2.find(100));  // 100|Alice|85
        System.out.println("find(200): " + system2.find(200));  // 200|Bob|90
        System.out.println("find(999): " + system2.find(999));  // null
        System.out.println("inorder: " + system2.inorder());
        System.out.println();

        // ===== 測試 updateScore =====
        System.out.println("===== updateScore 測試 =====");
        Q12_StudentBstSystem system3 = new Q12_StudentBstSystem();
        system3.add(new Q12_StudentBstSystem.Student(100, "Alice", 85));
        system3.add(new Q12_StudentBstSystem.Student(200, "Bob", 90));

        System.out.println("更新前: " + system3.inorder());
        System.out.println("updateScore(100, 95) → " + system3.updateScore(100, 95)); // true
        System.out.println("updateScore(100, 150) → " + system3.updateScore(100, 150)); // true（變 100）
        System.out.println("updateScore(100, -10) → " + system3.updateScore(100, -10)); // true（變 0）
        System.out.println("updateScore(999, 50) → " + system3.updateScore(999, 50));   // false
        System.out.println("更新後: " + system3.inorder());
        System.out.println();

        // ===== 測試三種刪除情況 =====
        System.out.println("===== 三種刪除情況測試 =====");

        // 葉子節點刪除
        Q12_StudentBstSystem leafTree = new Q12_StudentBstSystem();
        leafTree.add(new Q12_StudentBstSystem.Student(50, "Root", 80));
        leafTree.add(new Q12_StudentBstSystem.Student(30, "Left", 70));
        leafTree.add(new Q12_StudentBstSystem.Student(70, "Right", 90));
        leafTree.add(new Q12_StudentBstSystem.Student(20, "Leaf", 60));
        System.out.println("葉子樹: " + leafTree.inorder());
        System.out.println("remove(20) → " + leafTree.remove(20));  // true（葉子）
        System.out.println("刪除後: " + leafTree.inorder());
        System.out.println();

        // 單子節點刪除
        Q12_StudentBstSystem oneChildTree = new Q12_StudentBstSystem();
        oneChildTree.add(new Q12_StudentBstSystem.Student(50, "Root", 80));
        oneChildTree.add(new Q12_StudentBstSystem.Student(30, "Left", 70));
        oneChildTree.add(new Q12_StudentBstSystem.Student(20, "Child", 60));
        System.out.println("單子樹: " + oneChildTree.inorder());
        System.out.println("remove(30) → " + oneChildTree.remove(30));  // true（單子節點）
        System.out.println("刪除後: " + oneChildTree.inorder());
        System.out.println();

        // 二子節點刪除
        Q12_StudentBstSystem twoChildTree = new Q12_StudentBstSystem();
        twoChildTree.add(new Q12_StudentBstSystem.Student(50, "Root", 80));
        twoChildTree.add(new Q12_StudentBstSystem.Student(30, "Left", 70));
        twoChildTree.add(new Q12_StudentBstSystem.Student(40, "RightChild", 75));
        twoChildTree.add(new Q12_StudentBstSystem.Student(20, "LeftChild", 65));
        System.out.println("二子樹: " + twoChildTree.inorder());
        System.out.println("remove(30) → " + twoChildTree.remove(30));  // true（二子節點）
        System.out.println("刪除後: " + twoChildTree.inorder());
        System.out.println();

        // ===== 測試 studentsBetween（範圍查詢） =====
        System.out.println("===== 範圍查詢測試 =====");
        Q12_StudentBstSystem rangeTree = new Q12_StudentBstSystem();
        int[] ids = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45, 55, 65, 75, 90};
        for (int id : ids) {
            rangeTree.add(new Q12_StudentBstSystem.Student(id, "Student" + id, 50 + id % 50));
        }

        System.out.println("inorder: " + rangeTree.inorder());

        System.out.println("studentsBetween(20, 40):");
        for (Student s : rangeTree.studentsBetween(20, 40)) {
            System.out.println("  " + s);
        }

        System.out.println("studentsBetween(55, 75):");
        for (Student s : rangeTree.studentsBetween(55, 75)) {
            System.out.println("  " + s);
        }

        System.out.println("studentsBetween(100, 200): " + rangeTree.studentsBetween(100, 200));  // []
        System.out.println("studentsBetween(50, 30): " + rangeTree.studentsBetween(50, 30));      // []
        System.out.println();

        // ===== 測試 toString 格式 =====
        System.out.println("===== toString 格式測試 =====");
        Q12_StudentBstSystem formatTree = new Q12_StudentBstSystem();
        formatTree.add(new Q12_StudentBstSystem.Student(200, "Ivy", 88));
        formatTree.add(new Q12_StudentBstSystem.Student(300, "Alice", 75));
        formatTree.add(new Q12_StudentBstSystem.Student(500, "Maria", 100));
        System.out.println("toString: " + formatTree);
        // 預期: tree [200] Ivy|88 [300] Alice|75 [500] Maria|100
        System.out.println();

        // ===== 測試 remove 不存在的 id =====
        System.out.println("===== remove 不存在 id 測試 =====");
        Q12_StudentBstSystem missingTree = new Q12_StudentBstSystem();
        missingTree.add(new Q12_StudentBstSystem.Student(100, "Alice", 80));
        System.out.println("remove(999) → " + missingTree.remove(999));  // false
        System.out.println("size: " + missingTree.size());  // 1
    }
}