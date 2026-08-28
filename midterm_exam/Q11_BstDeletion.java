/**
 * 檔名：Q11_BstDeletion.java
 * 功能：BST 三種刪除
 * 說明：實作 leaf、one child、two children 三種刪除情況
 *       使用 right subtree minimum 作為 inorder successor
 */

import java.util.*;

public class Q11_BstDeletion {

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
    public Q11_BstDeletion() {
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
        if (root == null) {
            root = new Node(value);
            size++;
            return true;
        }
        return addRec(root, value);
    }

    private boolean addRec(Node node, int value) {
        if (value < node.value) {
            if (node.left == null) {
                node.left = new Node(value);
                size++;
                return true;
            } else {
                return addRec(node.left, value);
            }
        } else if (value > node.value) {
            if (node.right == null) {
                node.right = new Node(value);
                size++;
                return true;
            } else {
                return addRec(node.right, value);
            }
        } else {
            // 重複值，不加入
            return false;
        }
    }

    // ========== 2. remove() - 刪除 ==========

    /**
     * 刪除指定的值
     * @param value 要刪除的值
     * @return true 表示刪除成功，false 表示找不到該值
     */
    public boolean remove(int value) {
        // 先檢查是否包含該值
        if (!contains(value)) {
            return false;
        }

        root = removeRec(root, value);
        size--;
        return true;
    }

    private Node removeRec(Node node, int value) {
        if (node == null) {
            return null;
        }

        if (value < node.value) {
            node.left = removeRec(node.left, value);
        } else if (value > node.value) {
            node.right = removeRec(node.right, value);
        } else {
            // 找到要刪除的節點

            // === 情況 1：葉子節點（Leaf） ===
            if (node.left == null && node.right == null) {
                return null;
            }

            // === 情況 2：只有一個子節點（One Child） ===
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            // === 情況 3：有兩個子節點（Two Children） ===
            // 使用 right subtree minimum 作為 inorder successor
            Node successor = findMin(node.right);
            node.value = successor.value;
            node.right = removeRec(node.right, successor.value);
        }

        return node;
    }

    /**
     * 尋找子樹中的最小值（最左邊的節點）
     */
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // ========== 3. contains() - 搜尋 ==========

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

    // ========== 4. size() - 取得節點數量 ==========

    /**
     * 取得 BST 的節點數量
     * @return 節點數量
     */
    public int size() {
        return size;
    }

    /**
     * 檢查 BST 是否為空
     * @return true 表示空，false 表示非空
     */
    public boolean isEmpty() {
        return root == null;
    }

    // ========== 5. inorder() - 中序走訪 ==========

    /**
     * 中序走訪（回傳升冪資料）
     * @return 升冪列表
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

    // ========== 6. isValid() - 驗證 BST ==========

    /**
     * 驗證 BST 是否正確（使用全域 boundary）
     * @return true 表示是有效的 BST
     */
    public boolean isValid() {
        return isValidRec(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValidRec(Node node, long min, long max) {
        if (node == null) {
            return true;
        }

        if (node.value <= min || node.value >= max) {
            return false;
        }

        return isValidRec(node.left, min, node.value) &&
               isValidRec(node.right, node.value, max);
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
        Q11_BstDeletion tree = new Q11_BstDeletion();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int value : values) {
            tree.add(value);
        }

        System.out.println("初始 inorder: " + tree.inorder());  // [20, 30, 40, 50, 60, 70, 80]
        System.out.println("初始 size: " + tree.size());        // 7

        System.out.println("remove(20) → " + tree.remove(20));  // true（葉子節點）
        System.out.println("remove(30) → " + tree.remove(30));  // true（單子節點）
        System.out.println("remove(50) → " + tree.remove(50));  // true（二子節點，根）
        System.out.println("remove(999) → " + tree.remove(999)); // false（不存在）
        System.out.println("inorder: " + tree.inorder());        // [40, 60, 70, 80]
        System.out.println("size: " + tree.size());              // 4
        System.out.println("isEmpty: " + tree.isEmpty());        // false
        System.out.println("isValid: " + tree.isValid());        // true
        System.out.println();

        // ===== 測試葉子節點刪除 =====
        System.out.println("===== 葉子節點刪除測試 =====");
        Q11_BstDeletion leafTree = new Q11_BstDeletion();
        leafTree.add(10);
        leafTree.add(5);
        leafTree.add(15);
        leafTree.add(3);
        leafTree.add(7);
        System.out.println("原始: " + leafTree.inorder());  // [3, 5, 7, 10, 15]
        System.out.println("remove(3) → " + leafTree.remove(3));  // true（葉子）
        System.out.println("remove(7) → " + leafTree.remove(7));  // true（葉子）
        System.out.println("remove(15) → " + leafTree.remove(15)); // true（葉子）
        System.out.println("刪除後: " + leafTree.inorder());  // [5, 10]
        System.out.println("isValid: " + leafTree.isValid()); // true
        System.out.println();

        // ===== 測試單子節點刪除 =====
        System.out.println("===== 單子節點刪除測試 =====");
        Q11_BstDeletion oneChildTree = new Q11_BstDeletion();
        oneChildTree.add(10);
        oneChildTree.add(5);
        oneChildTree.add(3);     // 5 的左子樹
        oneChildTree.add(4);     // 3 的右子樹
        System.out.println("原始: " + oneChildTree.inorder());  // [3, 4, 5, 10]
        System.out.println("remove(3) → " + oneChildTree.remove(3));  // true（有右子節點 4）
        System.out.println("刪除後: " + oneChildTree.inorder());  // [4, 5, 10]

        // 測試只有左子樹的情況
        Q11_BstDeletion oneChildTree2 = new Q11_BstDeletion();
        oneChildTree2.add(10);
        oneChildTree2.add(5);
        oneChildTree2.add(7);    // 5 的右子樹
        oneChildTree2.add(6);    // 7 的左子樹
        System.out.println("原始: " + oneChildTree2.inorder());  // [5, 6, 7, 10]
        System.out.println("remove(7) → " + oneChildTree2.remove(7));  // true（有左子節點 6）
        System.out.println("刪除後: " + oneChildTree2.inorder());  // [5, 6, 10]
        System.out.println("isValid: " + oneChildTree2.isValid()); // true
        System.out.println();

        // ===== 測試二子節點刪除 =====
        System.out.println("===== 二子節點刪除測試 =====");
        Q11_BstDeletion twoChildTree = new Q11_BstDeletion();
        twoChildTree.add(50);
        twoChildTree.add(30);
        twoChildTree.add(70);
        twoChildTree.add(20);
        twoChildTree.add(40);
        twoChildTree.add(60);
        twoChildTree.add(80);
        System.out.println("原始: " + twoChildTree.inorder());  // [20, 30, 40, 50, 60, 70, 80]

        // 刪除 30（二子節點：左 20，右 40）
        System.out.println("remove(30) → " + twoChildTree.remove(30));  // true
        System.out.println("刪除 30 後: " + twoChildTree.inorder());  // [20, 40, 50, 60, 70, 80]

        // 刪除 70（二子節點：左 60，右 80）
        System.out.println("remove(70) → " + twoChildTree.remove(70));  // true
        System.out.println("刪除 70 後: " + twoChildTree.inorder());  // [20, 40, 50, 60, 80]

        System.out.println("isValid: " + twoChildTree.isValid()); // true
        System.out.println();

        // ===== 測試根節點刪除 =====
        System.out.println("===== 根節點刪除測試 =====");
        Q11_BstDeletion rootTree = new Q11_BstDeletion();
        rootTree.add(50);
        rootTree.add(30);
        rootTree.add(70);
        rootTree.add(20);
        rootTree.add(40);
        rootTree.add(60);
        rootTree.add(80);
        System.out.println("原始: " + rootTree.inorder());  // [20, 30, 40, 50, 60, 70, 80]

        // 刪除根節點 50（二子節點）
        System.out.println("remove(50) → " + rootTree.remove(50));  // true
        System.out.println("刪除根後: " + rootTree.inorder());  // [20, 30, 40, 60, 70, 80]
        System.out.println("新的根節點值: " + rootTree.root.value);  // 60（right subtree minimum）
        System.out.println("isValid: " + rootTree.isValid()); // true
        System.out.println();

        // ===== 測試連續刪除到空 =====
        System.out.println("===== 連續刪除到空測試 =====");
        Q11_BstDeletion emptyTree = new Q11_BstDeletion();
        emptyTree.add(10);
        emptyTree.add(5);
        emptyTree.add(15);
        System.out.println("原始: " + emptyTree.inorder());  // [5, 10, 15]

        System.out.println("remove(10) → " + emptyTree.remove(10));  // true（根，二子節點）
        System.out.println("刪除後: " + emptyTree.inorder());  // [5, 15]
        System.out.println("remove(5) → " + emptyTree.remove(5));    // true（根，單子節點）
        System.out.println("刪除後: " + emptyTree.inorder());  // [15]
        System.out.println("remove(15) → " + emptyTree.remove(15));  // true（根，葉子）
        System.out.println("刪除後: " + emptyTree.inorder());  // []
        System.out.println("isEmpty: " + emptyTree.isEmpty()); // true
        System.out.println("size: " + emptyTree.size());       // 0
        System.out.println("isValid: " + emptyTree.isValid()); // true
        System.out.println();

        // ===== 測試刪除不存在的值 =====
        System.out.println("===== 刪除不存在值測試 =====");
        Q11_BstDeletion missingTree = new Q11_BstDeletion();
        missingTree.add(10);
        missingTree.add(5);
        missingTree.add(15);
        System.out.println("原始: " + missingTree.inorder());  // [5, 10, 15]
        System.out.println("remove(100) → " + missingTree.remove(100));  // false
        System.out.println("remove(7) → " + missingTree.remove(7));      // false
        System.out.println("刪除後不變: " + missingTree.inorder());  // [5, 10, 15]
        System.out.println("size: " + missingTree.size());  // 3
        System.out.println();

        // ===== 測試刪除後 isValid =====
        System.out.println("===== 刪除後 isValid 驗證 =====");
        Q11_BstDeletion validTree = new Q11_BstDeletion();
        for (int v : new int[]{50, 25, 75, 12, 37, 62, 87, 6, 18, 31, 43, 56, 68, 81, 93}) {
            validTree.add(v);
        }
        System.out.println("原始 isValid: " + validTree.isValid());  // true

        // 刪除各種類型的節點
        validTree.remove(6);   // 葉子
        validTree.remove(25);  // 二子
        validTree.remove(62);  // 單子
        validTree.remove(50);  // 根

        System.out.println("刪除後 isValid: " + validTree.isValid());  // true
        System.out.println("刪除後 inorder: " + validTree.inorder());
        // [12, 18, 31, 37, 43, 56, 68, 75, 81, 87, 93]
    }
}