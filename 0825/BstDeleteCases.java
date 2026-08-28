/**
 * 檔名：BstDeleteCases.java
 * 功能：二元搜尋樹的三種刪除案例展示
 * 說明：依序刪除葉子節點、單子節點、二子節點
 *       每次輸出 inorder、大小與刪除結果
 */

class DeleteNode {
    int value;
    DeleteNode left;
    DeleteNode right;

    public DeleteNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

class DeleteBST {
    private DeleteNode root;
    private int size;  // 樹的節點數量

    public DeleteBST() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 取得樹的大小（節點總數）
     */
    public int getSize() {
        return size;
    }

    /**
     * 插入節點
     */
    public void insert(int value) {
        root = insertRec(root, value);
        size++;
    }

    private DeleteNode insertRec(DeleteNode node, int value) {
        if (node == null) {
            return new DeleteNode(value);
        }
        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        }
        return node;
    }

    /**
     * 刪除節點（主要方法）
     * @param value 要刪除的值
     * @return true 表示刪除成功，false 表示找不到該值
     */
    public boolean delete(int value) {
        System.out.println("========================================");
        System.out.println("🗑️  刪除目標值：" + value);

        if (root == null) {
            System.out.println("❌ 樹為空，無法刪除！");
            System.out.println("========================================");
            return false;
        }

        // 先確認該值是否存在
        if (!search(value)) {
            System.out.println("❌ 找不到目標值 " + value + "，刪除失敗！");
            System.out.println("========================================");
            return false;
        }

        // 執行刪除
        root = deleteRec(root, value);
        size--;
        System.out.println("✅ 成功刪除節點 " + value + "！");
        return true;
    }

    /**
     * 遞迴刪除節點（核心邏輯）
     */
    private DeleteNode deleteRec(DeleteNode node, int value) {
        if (node == null) {
            return null;
        }

        if (value < node.value) {
            node.left = deleteRec(node.left, value);
        } else if (value > node.value) {
            node.right = deleteRec(node.right, value);
        } else {
            // 找到要刪除的節點

            // 判斷刪除類型並輸出訊息
            String nodeType = getNodeType(node);
            System.out.println("📍 找到節點：" + node.value + "（" + nodeType + "）");

            // === 案例一：葉子節點（沒有子節點） ===
            if (node.left == null && node.right == null) {
                System.out.println("   → 刪除方式：直接移除（葉子節點）");
                return null;
            }

            // === 案例二：只有一個子節點（單子節點） ===
            if (node.left == null) {
                System.out.println("   → 刪除方式：用右子節點取代（只有右子樹）");
                return node.right;
            }
            if (node.right == null) {
                System.out.println("   → 刪除方式：用左子節點取代（只有左子樹）");
                return node.left;
            }

            // === 案例三：有兩個子節點（二子節點） ===
            System.out.println("   → 刪除方式：用右子樹的最小值取代（二子節點）");

            // 找到右子樹中的最小值（繼承者）
            DeleteNode successor = findMin(node.right);
            System.out.println("   → 繼承者（右子樹最小值）：" + successor.value);

            // 複製繼承者的值到當前節點
            node.value = successor.value;

            // 刪除右子樹中的繼承者（注意：繼承者一定是葉子或只有右子樹）
            node.right = deleteRec(node.right, successor.value);
        }

        return node;
    }

    /**
     * 判斷節點類型（用於輸出資訊）
     */
    private String getNodeType(DeleteNode node) {
        if (node.left == null && node.right == null) {
            return "葉子節點";
        } else if (node.left != null && node.right != null) {
            return "二子節點";
        } else {
            return "單子節點";
        }
    }

    /**
     * 尋找某棵樹中的最小值
     */
    private DeleteNode findMin(DeleteNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * 搜尋某個值是否存在
     */
    public boolean search(int value) {
        DeleteNode current = root;
        while (current != null) {
            if (value == current.value) {
                return true;
            } else if (value < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    /**
     * 中序遍歷（輸出排序結果）
     */
    public void printInOrder() {
        System.out.print("📊 中序遍歷（Inorder）：");
        if (root == null) {
            System.out.print("（樹為空）");
        } else {
            inOrderRec(root);
        }
        System.out.println();
        System.out.println("📏 樹的大小（節點數）：" + size);
    }

    private void inOrderRec(DeleteNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.value + " ");
            inOrderRec(node.right);
        }
    }

    /**
     * 前序遍歷（輔助觀察樹結構）
     */
    public void printPreOrder() {
        System.out.print("🌳 前序遍歷（Preorder）：");
        if (root == null) {
            System.out.print("（樹為空）");
        } else {
            preOrderRec(root);
        }
        System.out.println();
    }

    private void preOrderRec(DeleteNode node) {
        if (node != null) {
            System.out.print(node.value + " ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }

    /**
     * 顯示完整的樹結構（含前序+中序）
     */
    public void displayTree() {
        System.out.println("----------------------------------------");
        printPreOrder();
        printInOrder();
        System.out.println("----------------------------------------");
    }
}

public class BstDeleteCases {
    public static void main(String[] args) {
        DeleteBST bst = new DeleteBST();

        // 建立一棵包含各種節點類型的樹
        // 目標：同時包含葉子節點、單子節點、二子節點
        int[] data = {50, 30, 70, 20, 40, 60, 80, 35, 45, 55, 65, 75, 90, 25, 85};

        System.out.println("===== 建立二元搜尋樹 =====");
        System.out.print("插入資料：");
        for (int val : data) {
            bst.insert(val);
            System.out.print(val + " ");
        }
        System.out.println();
        bst.displayTree();
        System.out.println();

        // ============================================
        // 測試一：刪除葉子節點（例如：25）
        // ============================================
        System.out.println("【測試一：刪除葉子節點】");
        bst.delete(25);
        bst.displayTree();
        System.out.println();

        // ============================================
        // 測試二：刪除單子節點（例如：20）
        // 此時 20 只有右子節點 25（或樹中其他單子節點）
        // ============================================
        System.out.println("【測試二：刪除單子節點】");
        bst.delete(20);
        bst.displayTree();
        System.out.println();

        // ============================================
        // 測試三：刪除二子節點（例如：30）
        // 30 有左子樹（40,35,45）和右子樹（...）
        // ============================================
        System.out.println("【測試三：刪除二子節點】");
        bst.delete(30);
        bst.displayTree();
        System.out.println();

        // ============================================
        // 測試四：刪除根節點（二子節點）
        // ============================================
        System.out.println("【測試四：刪除根節點（二子節點）】");
        bst.delete(50);
        bst.displayTree();
        System.out.println();

        // ============================================
        // 測試五：嘗試刪除不存在的值
        // ============================================
        System.out.println("【測試五：刪除不存在的值】");
        bst.delete(999);
        bst.displayTree();

        // ============================================
        // 測試六：繼續刪除到樹為空
        // ============================================
        System.out.println("【測試六：連續刪除所有節點】");
        int[] deleteAll = {70, 40, 35, 45, 60, 55, 65, 80, 75, 85, 90};
        for (int val : deleteAll) {
            bst.delete(val);
            bst.displayTree();
            System.out.println();
        }
    }
}