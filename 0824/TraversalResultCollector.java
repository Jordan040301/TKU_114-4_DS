/**
 * 課後作業三：遍歷結果集合
 * 指定檔名：TraversalResultCollector.java
 * 
 * 進行四種遍歷回傳 List<String>，不直接輸出。
 * 測試空樹、單節點、左偏和完整樹。
 */
public class TraversalResultCollector {

    /**
     * 二元樹節點類別
     */
    static class TreeNode {
        String value;
        TreeNode left;
        TreeNode right;

        TreeNode(String value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        TreeNode(String value, TreeNode left, TreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * 前序遍歷（根 → 左 → 右）
     * @param root 根節點
     * @return 前序遍歷結果的 List
     */
    public static java.util.List<String> preorder(TreeNode root) {
        java.util.List<String> result = new java.util.ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    /**
     * 遞迴輔助方法：前序遍歷
     */
    private static void preorderHelper(TreeNode node, java.util.List<String> result) {
        if (node == null) {
            return;
        }
        result.add(node.value);
        preorderHelper(node.left, result);
        preorderHelper(node.right, result);
    }

    /**
     * 中序遍歷（左 → 根 → 右）
     * @param root 根節點
     * @return 中序遍歷結果的 List
     */
    public static java.util.List<String> inorder(TreeNode root) {
        java.util.List<String> result = new java.util.ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    /**
     * 遞迴輔助方法：中序遍歷
     */
    private static void inorderHelper(TreeNode node, java.util.List<String> result) {
        if (node == null) {
            return;
        }
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    /**
     * 後序遍歷（左 → 右 → 根）
     * @param root 根節點
     * @return 後序遍歷結果的 List
     */
    public static java.util.List<String> postorder(TreeNode root) {
        java.util.List<String> result = new java.util.ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

    /**
     * 遞迴輔助方法：後序遍歷
     */
    private static void postorderHelper(TreeNode node, java.util.List<String> result) {
        if (node == null) {
            return;
        }
        postorderHelper(node.left, result);
        postorderHelper(node.right, result);
        result.add(node.value);
    }

    /**
     * 層級順序遍歷（使用 Queue）
     * @param root 根節點
     * @return 層級順序遍歷結果的 List
     */
    public static java.util.List<String> levelOrder(TreeNode root) {
        java.util.List<String> result = new java.util.ArrayList<>();
        if (root == null) {
            return result;
        }

        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            result.add(current.value);

            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return result;
    }

    /**
     * 列印 List 的輔助方法（格式化輸出）
     */
    private static void printList(String traversalName, java.util.List<String> list) {
        System.out.print(traversalName + ": [");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i < list.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * 測試並輸出四種遍歷結果
     */
    private static void testTraversals(TreeNode root, String treeName) {
        System.out.println("===== " + treeName + " =====");
        System.out.println("樹結構:");
        printTree(root, "");
        System.out.println();
        
        java.util.List<String> pre = preorder(root);
        java.util.List<String> in = inorder(root);
        java.util.List<String> post = postorder(root);
        java.util.List<String> level = levelOrder(root);
        
        printList("前序遍歷", pre);
        printList("中序遍歷", in);
        printList("後序遍歷", post);
        printList("層級遍歷", level);
        System.out.println();
    }

    /**
     * 列印樹結構（輔助方法）
     */
    private static void printTree(TreeNode node, String indent) {
        if (node == null) {
            System.out.println(indent + "null");
            return;
        }
        System.out.println(indent + node.value);
        if (node.left != null || node.right != null) {
            printTree(node.left, indent + "  ");
            printTree(node.right, indent + "  ");
        }
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        System.out.println("===== 四種遍歷結果集合測試 =====\n");

        // ===== 測試案例 1：空樹 =====
        testTraversals(null, "空樹");

        // ===== 測試案例 2：單節點樹 =====
        TreeNode single = new TreeNode("A");
        testTraversals(single, "單節點樹 (A)");

        // ===== 測試案例 3：左偏樹 =====
        // 結構：
        //        A
        //       /
        //      B
        //     /
        //    C
        //   /
        //  D
        TreeNode D = new TreeNode("D");
        TreeNode C = new TreeNode("C", D, null);
        TreeNode B = new TreeNode("B", C, null);
        TreeNode A = new TreeNode("A", B, null);
        testTraversals(A, "左偏樹 (A→B→C→D)");

        // ===== 測試案例 4：完整二元樹 =====
        // 結構：
        //        A
        //       / \
        //      B   C
        //     / \ / \
        //    D  E F  G
        TreeNode D2 = new TreeNode("D");
        TreeNode E = new TreeNode("E");
        TreeNode F = new TreeNode("F");
        TreeNode G = new TreeNode("G");
        TreeNode B2 = new TreeNode("B", D2, E);
        TreeNode C2 = new TreeNode("C", F, G);
        TreeNode root = new TreeNode("A", B2, C2);
        testTraversals(root, "完整二元樹 (A~G)");

        // ===== 測試案例 5：不完整二元樹 =====
        // 結構：
        //        A
        //       / \
        //      B   C
        //     /     \
        //    D       E
        //   / \     /
        //  F   G   H
        TreeNode F2 = new TreeNode("F");
        TreeNode G2 = new TreeNode("G");
        TreeNode D3 = new TreeNode("D", F2, G2);
        TreeNode H = new TreeNode("H");
        TreeNode E2 = new TreeNode("E", H, null);
        TreeNode B3 = new TreeNode("B", D3, null);
        TreeNode C3 = new TreeNode("C", null, E2);
        TreeNode root2 = new TreeNode("A", B3, C3);
        testTraversals(root2, "不完整二元樹");

        // ===== 測試案例 6：右偏樹 =====
        // 結構：
        //        A
        //         \
        //          B
        //           \
        //            C
        //             \
        //              D
        TreeNode D4 = new TreeNode("D");
        TreeNode C4 = new TreeNode("C", null, D4);
        TreeNode B4 = new TreeNode("B", null, C4);
        TreeNode A4 = new TreeNode("A", null, B4);
        testTraversals(A4, "右偏樹 (A→B→C→D)");

        // ===== 驗證 List 內容（使用字串比較） =====
        System.out.println("===== 驗證遍歷結果 =====");
        System.out.println("完整樹前序: " + preorder(root));
        System.out.println("完整樹中序: " + inorder(root));
        System.out.println("完整樹後序: " + postorder(root));
        System.out.println("完整樹層級: " + levelOrder(root));
        System.out.println();

        // ===== 測試 getClass() 確保回傳 List<String> =====
        System.out.println("===== 型別驗證 =====");
        java.util.List<String> testList = preorder(root);
        System.out.println("preorder 回傳型別: " + testList.getClass().getName());
        System.out.println("是否為 List<String>: " + (testList instanceof java.util.List));
        System.out.println("元素型別: " + (testList.isEmpty() ? "空" : testList.get(0).getClass().getSimpleName()));
    }
}