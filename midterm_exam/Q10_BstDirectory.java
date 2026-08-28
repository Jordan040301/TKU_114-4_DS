/**
 * 檔名：Q10_BstDirectory.java
 * 功能：BST 目錄系統
 * 說明：實作 BST 的插入、搜尋、路徑追蹤、中序遍歷和驗證
 */

import java.util.*;

public class Q10_BstDirectory {

    // ========== Node 類別 ==========
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    // ========== BST 欄位 ==========
    private Node root;
    private int size;

    /**
     * 建構子
     */
    public Q10_BstDirectory() {
        this.root = null;
        this.size = 0;
    }

    // ========== 1. add() - 插入 ==========

    /**
     * 插入值到 BST
     * @param value 要插入的值
     * @return true 表示插入成功，false 表示值已存在
     */
    public boolean add(int value) {
        // 如果 root 為空，直接建立
        if (root == null) {
            root = new Node(value);
            size++;
            return true;
        }

        // 嘗試插入
        boolean inserted = addRec(root, value);
        if (inserted) {
            size++;
        }
        return inserted;
    }

    private boolean addRec(Node node, int value) {
        if (value < node.value) {
            if (node.left == null) {
                node.left = new Node(value);
                return true;
            } else {
                return addRec(node.left, value);
            }
        } else if (value > node.value) {
            if (node.right == null) {
                node.right = new Node(value);
                return true;
            } else {
                return addRec(node.right, value);
            }
        } else {
            // 重複值，不加入
            return false;
        }
    }

    // ========== 2. contains() - 搜尋 ==========

    /**
     * 檢查是否包含某值
     * @param value 要搜尋的值
     * @return true 表示找到，false 表示找不到
     */
    public boolean contains(int value) {
        return containsRec(root, value);
    }

    private boolean containsRec(Node node, int value) {
        if (node == null) {
            return false;
        }

        if (value == node.value) {
            return true;
        } else if (value < node.value) {
            return containsRec(node.left, value);
        } else {
            return containsRec(node.right, value);
        }
    }

    // ========== 3. searchPath() - 搜尋路徑 ==========

    /**
     * 搜尋目標值，並記錄比較過的節點值
     * @param target 目標值
     * @return 比較過的節點值列表（找不到時不加入 null）
     */
    public List<Integer> searchPath(int target) {
        List<Integer> path = new ArrayList<>();
        searchPathRec(root, target, path);
        return path;
    }

    private boolean searchPathRec(Node node, int target, List<Integer> path) {
        if (node == null) {
            return false;
        }

        // 記錄當前節點值（實際比較過的 node value）
        path.add(node.value);

        if (target == node.value) {
            return true;
        } else if (target < node.value) {
            return searchPathRec(node.left, target, path);
        } else {
            return searchPathRec(node.right, target, path);
        }
    }

    // ========== 4. inorder() - 中序走訪 ==========

    /**
     * 中序走訪（回傳升冪資料）
     * @return 升冪列表（empty tree 回傳 empty List）
     */
    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }

    private void inorderRec(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }
        inorderRec(node.left, result);
        result.add(node.value);
        inorderRec(node.right, result);
    }

    // ========== 5. isValid() - 驗證 BST ==========

    /**
     * 驗證 BST 是否正確（使用 ancestor low/high boundary）
     * @return true 表示是有效的 BST
     */
    public boolean isValid() {
        return isValidRec(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * 使用祖先邊界驗證 BST
     * @param node 當前節點
     * @param min 允許的最小值（不包含）
     * @param max 允許的最大值（不包含）
     * @return true 表示符合 BST 性質
     */
    private boolean isValidRec(Node node, long min, long max) {
        // 空節點視為合法
        if (node == null) {
            return true;
        }

        // 檢查當前節點值是否在 (min, max) 範圍內
        if (node.value <= min || node.value >= max) {
            return false;
        }

        // 遞迴檢查左右子樹
        // 左子樹：值必須小於當前節點值（max = node.value）
        // 右子樹：值必須大於當前節點值（min = node.value）
        return isValidRec(node.left, min, node.value) &&
               isValidRec(node.right, node.value, max);
    }

    // ========== 6. size() - 取得節點數量 ==========

    /**
     * 取得 BST 的節點數量
     * @return 節點數量
     */
    public int size() {
        return size;
    }

    // ========== 輔助方法（方便測試） ==========

    /**
     * 取得樹的高度
     */
    public int height() {
        return heightRec(root);
    }

    private int heightRec(Node node) {
        if (node == null) {
            return -1;
        }
        return Math.max(heightRec(node.left), heightRec(node.right)) + 1;
    }

    /**
     * 前序走訪（輔助觀察）
     */
    public List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorderRec(root, result);
        return result;
    }

    private void preorderRec(Node node, List<Integer> result) {
        if (node == null) return;
        result.add(node.value);
        preorderRec(node.left, result);
        preorderRec(node.right, result);
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試範例 =====");
        Q10_BstDirectory tree = new Q10_BstDirectory();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int value : values) {
            System.out.println("add(" + value + ") → " + tree.add(value));
        }
        System.out.println();

        // 測試重複插入
        System.out.println("add(40) → " + tree.add(40));  // false（重複）

        System.out.println("searchPath(60) → " + tree.searchPath(60));  // [50, 70, 60]
        System.out.println("searchPath(65) → " + tree.searchPath(65));  // [50, 70, 60]
        System.out.println("searchPath(20) → " + tree.searchPath(20));  // [50, 30, 20]
        System.out.println("searchPath(99) → " + tree.searchPath(99));  // [50, 70, 80]

        System.out.println("inorder() → " + tree.inorder());  // [20, 30, 40, 50, 60, 70, 80]
        System.out.println("isValid() → " + tree.isValid());  // true
        System.out.println("size() → " + tree.size());        // 7
        System.out.println();

        // ===== 測試 contains =====
        System.out.println("===== contains 測試 =====");
        System.out.println("contains(40) → " + tree.contains(40));  // true
        System.out.println("contains(65) → " + tree.contains(65));  // false
        System.out.println("contains(80) → " + tree.contains(80));  // true
        System.out.println("contains(99) → " + tree.contains(99));  // false
        System.out.println();

        // ===== 測試空樹 =====
        System.out.println("===== 空樹測試 =====");
        Q10_BstDirectory emptyTree = new Q10_BstDirectory();
        System.out.println("emptyTree.size() → " + emptyTree.size());  // 0
        System.out.println("emptyTree.inorder() → " + emptyTree.inorder());  // []
        System.out.println("emptyTree.searchPath(10) → " + emptyTree.searchPath(10));  // []
        System.out.println("emptyTree.contains(10) → " + emptyTree.contains(10));  // false
        System.out.println("emptyTree.isValid() → " + emptyTree.isValid());  // true
        System.out.println();

        // ===== 測試 isValid - 違規樹 =====
        System.out.println("===== isValid 違規樹測試 =====");

        // 測試 1：左子樹有大於根的值
        Q10_BstDirectory invalidTree1 = new Q10_BstDirectory();
        // 手動建立違規樹（不透過 add）
        Node root1 = new Node(10);
        root1.left = new Node(15);  // 違規！左子樹有 15 > 10
        invalidTree1.root = root1;
        invalidTree1.size = 2;
        System.out.println("左子樹有 15 > 10:");
        System.out.println("  isValid() → " + invalidTree1.isValid());  // false
        System.out.println("  inorder() → " + invalidTree1.inorder());  // [15, 10]（不是升冪）

        // 測試 2：右子樹有小於根的值
        Q10_BstDirectory invalidTree2 = new Q10_BstDirectory();
        Node root2 = new Node(10);
        root2.right = new Node(5);  // 違規！右子樹有 5 < 10
        invalidTree2.root = root2;
        invalidTree2.size = 2;
        System.out.println("右子樹有 5 < 10:");
        System.out.println("  isValid() → " + invalidTree2.isValid());  // false
        System.out.println("  inorder() → " + invalidTree2.inorder());  // [10, 5]（不是升冪）

        // 測試 3：深層違規
        Q10_BstDirectory invalidTree3 = new Q10_BstDirectory();
        Node root3 = new Node(10);
        root3.left = new Node(5);
        root3.left.right = new Node(7);
        root3.left.right.left = new Node(6);   // 合法
        root3.left.right.right = new Node(12); // 違規！12 > 5 且 12 > 7
        invalidTree3.root = root3;
        invalidTree3.size = 5;
        System.out.println("深層違規（12 在 5 的右子樹但大於 10）:");
        System.out.println("  isValid() → " + invalidTree3.isValid());  // false
        System.out.println("  inorder() → " + invalidTree3.inorder());  // [5, 6, 7, 10, 12]（有問題）
        System.out.println();

        // ===== 測試較大的樹 =====
        System.out.println("===== 較大的樹測試 =====");
        Q10_BstDirectory bigTree = new Q10_BstDirectory();
        int[] bigValues = {50, 25, 75, 12, 37, 62, 87, 6, 18, 31, 43, 56, 68, 81, 93};
        for (int v : bigValues) {
            bigTree.add(v);
        }
        System.out.println("size() → " + bigTree.size());  // 15
        System.out.println("inorder() 前 10 個: " + bigTree.inorder().subList(0, 10));
        System.out.println("searchPath(68) → " + bigTree.searchPath(68));  // [50, 75, 62, 68]
        System.out.println("searchPath(100) → " + bigTree.searchPath(100)); // [50, 75, 87, 93]
        System.out.println("isValid() → " + bigTree.isValid());  // true
        System.out.println();

        // ===== 測試重複插入 =====
        System.out.println("===== 重複插入測試 =====");
        Q10_BstDirectory dupTree = new Q10_BstDirectory();
        System.out.println("add(10) → " + dupTree.add(10));  // true
        System.out.println("add(10) → " + dupTree.add(10));  // false
        System.out.println("add(20) → " + dupTree.add(20));  // true
        System.out.println("add(20) → " + dupTree.add(20));  // false
        System.out.println("size() → " + dupTree.size());    // 2
        System.out.println("inorder() → " + dupTree.inorder()); // [10, 20]
    }
}