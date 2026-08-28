/**
 * 檔名：bstSearchTrace.java
 * 功能：二元搜尋樹（BST）搜尋追蹤
 * 說明：每次比較時輸出目前節點值、方向與比較計數
 */

class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;

    public TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

class BinarySearchTree {
    private TreeNode root;

    public BinarySearchTree() {
        this.root = null;
    }

    // 插入節點
    public void insert(int value) {
        root = insertRec(root, value);
    }

    private TreeNode insertRec(TreeNode node, int value) {
        if (node == null) {
            return new TreeNode(value);
        }
        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        }
        return node;
    }

    /**
     * 搜尋追蹤方法
     * @param target 要搜尋的目標值
     * @return 若找到回傳 true，否則回傳 false
     */
    public boolean searchWithTrace(int target) {
        System.out.println("========================================");
        System.out.println("開始搜尋目標值：" + target);
        System.out.println("----------------------------------------");

        if (root == null) {
            System.out.println("樹為空，無法搜尋！");
            return false;
        }

        TreeNode current = root;
        int compareCount = 0;

        while (current != null) {
            compareCount++;
            String direction = "";

            // 判斷方向（第一次比較視為根節點）
            if (current == root && compareCount == 1) {
                direction = "根節點";
            } else if (target < current.value) {
                direction = "往左";
            } else if (target > current.value) {
                direction = "往右";
            } else {
                // 相等時，已經找到，方向顯示為「找到」
                direction = "找到！";
            }

            // 輸出比較追蹤訊息
            System.out.println("比較次數：" + compareCount + 
                               ", 目前節點值：" + current.value + 
                               ", 方向：" + direction);

            // 如果找到目標值
            if (target == current.value) {
                System.out.println("----------------------------------------");
                System.out.println("✅ 成功找到目標值 " + target + "！");
                System.out.println("總共比較次數：" + compareCount);
                System.out.println("========================================");
                return true;
            }

            // 決定下一步移動方向
            if (target < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        // 如果離開迴圈，表示找不到
        System.out.println("----------------------------------------");
        System.out.println("❌ 找不到目標值 " + target + "（已到達葉子節點）");
        System.out.println("總共比較次數：" + compareCount);
        System.out.println("========================================");
        return false;
    }

    // 印出中序遍歷（用於顯示樹的結構）
    public void inorder() {
        System.out.print("中序遍歷（排序結果）：");
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(TreeNode node) {
        if (node != null) {
            inorderRec(node.left);
            System.out.print(node.value + " ");
            inorderRec(node.right);
        }
    }
}

public class BstSearchTrace {
    public static void main(String[] args) {
        // 建立一棵 BST
        BinarySearchTree bst = new BinarySearchTree();
        int[] data = {50, 30, 70, 20, 40, 60, 80, 35, 45};

        System.out.println("===== 建立二元搜尋樹 =====");
        for (int val : data) {
            bst.insert(val);
            System.out.print("插入 " + val + " → ");
        }
        System.out.println();
        bst.inorder();
        System.out.println();

        // ========== 測試一：搜尋根節點 ==========
        bst.searchWithTrace(50);

        // ========== 測試二：搜尋內部節點 ==========
        bst.searchWithTrace(30);

        // ========== 測試三：搜尋葉子節點 ==========
        bst.searchWithTrace(45);

        // ========== 測試四：搜尋缺失值 ==========
        bst.searchWithTrace(100);

        // ========== 測試五：搜尋另一個缺失值 ==========
        bst.searchWithTrace(25);
    }
}