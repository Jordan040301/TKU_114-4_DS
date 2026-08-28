/**
 * 檔名：BstRangeReport.java
 * 功能：二元搜尋樹的 Min、Max 與範圍輸出
 * 說明：完成 min、max 與 printRange(low, high)
 *       包含端點並處理 low > high 的情況
 */

class RangeNode {
    int value;
    RangeNode left;
    RangeNode right;

    public RangeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

class RangeBST {
    private RangeNode root;

    public RangeBST() {
        this.root = null;
    }

    // 插入節點
    public void insert(int value) {
        root = insertRec(root, value);
    }

    private RangeNode insertRec(RangeNode node, int value) {
        if (node == null) {
            return new RangeNode(value);
        }
        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        }
        return node;
    }

    /**
     * 找出 BST 中的最小值
     * @return 最小值，若樹為空則回傳 Integer.MIN_VALUE
     */
    public int min() {
        if (root == null) {
            System.out.println("⚠️ 樹為空，無法取得最小值！");
            return Integer.MIN_VALUE;
        }

        RangeNode current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    /**
     * 找出 BST 中的最大值
     * @return 最大值，若樹為空則回傳 Integer.MAX_VALUE
     */
    public int max() {
        if (root == null) {
            System.out.println("⚠️ 樹為空，無法取得最大值！");
            return Integer.MAX_VALUE;
        }

        RangeNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    /**
     * 印出介於 low 和 high 之間的所有節點值（包含端點）
     * @param low  範圍下限
     * @param high 範圍上限
     */
    public void printRange(int low, int high) {
        System.out.println("========================================");
        System.out.println("printRange(" + low + ", " + high + ")");

        // 處理 low > high 的情況
        if (low > high) {
            System.out.println("⚠️ 錯誤：low (" + low + ") 大於 high (" + high + ")，請重新輸入！");
            System.out.println("========================================");
            return;
        }

        if (root == null) {
            System.out.println("樹為空，無任何節點！");
            System.out.println("========================================");
            return;
        }

        System.out.print("範圍 [" + low + ", " + high + "] 內的節點值：");
        boolean hasResult = printRangeRec(root, low, high);
        
        if (!hasResult) {
            System.out.print("（無任何節點在此範圍內）");
        }
        System.out.println();
        System.out.println("========================================");
    }

    /**
     * 遞迴執行範圍查詢（中序遍歷）
     * @param node 目前節點
     * @param low  範圍下限
     * @param high 範圍上限
     * @return 是否找到至少一個符合條件的節點
     */
    private boolean printRangeRec(RangeNode node, int low, int high) {
        if (node == null) {
            return false;
        }

        boolean found = false;

        // 如果當前節點值 > low，表示左子樹可能有符合條件的節點
        if (node.value > low) {
            found = printRangeRec(node.left, low, high) || found;
        }

        // 檢查當前節點是否在範圍內（包含端點）
        if (node.value >= low && node.value <= high) {
            System.out.print(node.value + " ");
            found = true;
        }

        // 如果當前節點值 < high，表示右子樹可能有符合條件的節點
        if (node.value < high) {
            found = printRangeRec(node.right, low, high) || found;
        }

        return found;
    }

    /**
     * 中序遍歷（用於顯示完整樹結構）
     */
    public void printInOrder() {
        System.out.print("中序遍歷（完整樹）：");
        inOrderRec(root);
        System.out.println();
    }

    private void inOrderRec(RangeNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.value + " ");
            inOrderRec(node.right);
        }
    }

    /**
     * 輔助方法：印出樹的結構（前序）
     */
    public void printPreOrder() {
        System.out.print("前序遍歷（樹結構）：");
        preOrderRec(root);
        System.out.println();
    }

    private void preOrderRec(RangeNode node) {
        if (node != null) {
            System.out.print(node.value + " ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }
}

public class BstRangeReport {
    public static void main(String[] args) {
        RangeBST bst = new RangeBST();

        // 建立 BST
        int[] data = {50, 30, 70, 20, 40, 60, 80, 35, 45, 55, 65, 75, 90};

        System.out.println("===== 建立二元搜尋樹 =====");
        for (int val : data) {
            bst.insert(val);
            System.out.print(val + " ");
        }
        System.out.println();
        bst.printInOrder();
        bst.printPreOrder();
        System.out.println();

        // ========== 測試一：取得最小值與最大值 ==========
        System.out.println("===== Min 與 Max 測試 =====");
        System.out.println("最小值（Min）：" + bst.min());
        System.out.println("最大值（Max）：" + bst.max());
        System.out.println();

        // ========== 測試二：printRange 正常情況 ==========
        System.out.println("===== printRange 正常測試 =====");
        bst.printRange(35, 65);   // 範圍內有多個節點
        bst.printRange(50, 50);   // 單一節點（端點相同）
        bst.printRange(10, 25);   // 部分在範圍內
        bst.printRange(85, 95);   // 部分在範圍內
        bst.printRange(1, 10);    // 無任何節點在範圍內

        // ========== 測試三：low > high 的錯誤處理 ==========
        System.out.println("===== low > high 錯誤測試 =====");
        bst.printRange(70, 30);   // low > high
        bst.printRange(100, 50);  // low > high

        // ========== 測試四：邊界值測試 ==========
        System.out.println("===== 邊界值測試 =====");
        bst.printRange(20, 20);   // 最小值
        bst.printRange(90, 90);   // 最大值
        bst.printRange(20, 90);   // 整個樹的範圍
    }
}