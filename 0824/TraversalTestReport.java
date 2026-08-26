/**
 * 課後作業六：遍歷測驗報告
 * 指定檔名：TraversalTestReport.java
 * 
 * 建立空、單節點、唯左、唯右、完全與不規則樹，
 * 輸出四種遍歷的預期與實際結果及是否相同。
 */
public class TraversalTestReport {

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
     */
    public static java.util.List<String> preorder(TreeNode root) {
        java.util.List<String> result = new java.util.ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

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
     */
    public static java.util.List<String> inorder(TreeNode root) {
        java.util.List<String> result = new java.util.ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

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
     */
    public static java.util.List<String> postorder(TreeNode root) {
        java.util.List<String> result = new java.util.ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

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
     * 比較兩個 List 是否相同
     */
    private static boolean isSame(java.util.List<String> expected, java.util.List<String> actual) {
        if (expected == null && actual == null) {
            return true;
        }
        if (expected == null || actual == null) {
            return false;
        }
        if (expected.size() != actual.size()) {
            return false;
        }
        for (int i = 0; i < expected.size(); i++) {
            if (!expected.get(i).equals(actual.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 格式化輸出 List
     */
    private static String formatList(java.util.List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 測試並輸出遍歷結果
     */
    private static void testTraversal(TreeNode root, String treeName, 
                                      java.util.List<String> expectedPre,
                                      java.util.List<String> expectedIn,
                                      java.util.List<String> expectedPost,
                                      java.util.List<String> expectedLevel) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("測試樹: " + treeName);
        System.out.println("樹結構:");
        printTree(root, "");
        System.out.println();

        // 執行遍歷
        java.util.List<String> actualPre = preorder(root);
        java.util.List<String> actualIn = inorder(root);
        java.util.List<String> actualPost = postorder(root);
        java.util.List<String> actualLevel = levelOrder(root);

        // 輸出結果
        System.out.println("┌─────────────┬─────────────────────┬─────────────────────┬──────────┐");
        System.out.println("│ 遍歷方式    │ 預期結果            │ 實際結果            │ 是否相同 │");
        System.out.println("├─────────────┼─────────────────────┼─────────────────────┼──────────┤");

        printRow("前序", expectedPre, actualPre);
        printRow("中序", expectedIn, actualIn);
        printRow("後序", expectedPost, actualPost);
        printRow("層級", expectedLevel, actualLevel);

        System.out.println("└─────────────┴─────────────────────┴─────────────────────┴──────────┘");
        System.out.println();
    }

    /**
     * 輸出單行結果
     */
    private static void printRow(String name, java.util.List<String> expected, 
                                 java.util.List<String> actual) {
        String expStr = formatList(expected);
        String actStr = formatList(actual);
        boolean same = isSame(expected, actual);
        String sameStr = same ? "✓ 相同" : "✗ 不同";

        // 確保輸出對齊
        System.out.printf("│ %-11s │ %-19s │ %-19s │ %-8s │\n", 
                         name, expStr, actStr, sameStr);
    }

    /**
     * 列印樹結構
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
     * 建立樹的輔助方法（用於預期結果）
     */
    private static TreeNode buildTree(String... values) {
        if (values == null || values.length == 0) {
            return null;
        }
        return buildTreeHelper(values, 0);
    }

    private static TreeNode buildTreeHelper(String[] values, int index) {
        if (index >= values.length || values[index] == null) {
            return null;
        }
        TreeNode node = new TreeNode(values[index]);
        node.left = buildTreeHelper(values, 2 * index + 1);
        node.right = buildTreeHelper(values, 2 * index + 2);
        return node;
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        System.out.println("===== 遍歷測驗報告 =====\n");

        // ============================================================
        // 1. 空樹
        // ============================================================
        testTraversal(null, "空樹",
                      new java.util.ArrayList<>(),
                      new java.util.ArrayList<>(),
                      new java.util.ArrayList<>(),
                      new java.util.ArrayList<>());

        // ============================================================
        // 2. 單節點樹
        // ============================================================
        TreeNode single = new TreeNode("A");
        testTraversal(single, "單節點樹 (A)",
                      java.util.Arrays.asList("A"),
                      java.util.Arrays.asList("A"),
                      java.util.Arrays.asList("A"),
                      java.util.Arrays.asList("A"));

        // ============================================================
        // 3. 唯左樹
        // ============================================================
        // 結構：     A
        //          /
        //         B
        //        /
        //       C
        //      /
        //     D
        TreeNode D = new TreeNode("D");
        TreeNode C = new TreeNode("C", D, null);
        TreeNode B = new TreeNode("B", C, null);
        TreeNode A = new TreeNode("A", B, null);
        testTraversal(A, "唯左樹 (A→B→C→D)",
                      java.util.Arrays.asList("A", "B", "C", "D"),
                      java.util.Arrays.asList("D", "C", "B", "A"),
                      java.util.Arrays.asList("D", "C", "B", "A"),
                      java.util.Arrays.asList("A", "B", "C", "D"));

        // ============================================================
        // 4. 唯右樹
        // ============================================================
        // 結構：     A
        //              \
        //               B
        //                \
        //                 C
        //                  \
        //                   D
        TreeNode D2 = new TreeNode("D");
        TreeNode C2 = new TreeNode("C", null, D2);
        TreeNode B2 = new TreeNode("B", null, C2);
        TreeNode A2 = new TreeNode("A", null, B2);
        testTraversal(A2, "唯右樹 (A→B→C→D)",
                      java.util.Arrays.asList("A", "B", "C", "D"),
                      java.util.Arrays.asList("A", "B", "C", "D"),
                      java.util.Arrays.asList("D", "C", "B", "A"),
                      java.util.Arrays.asList("A", "B", "C", "D"));

        // ============================================================
        // 5. 完全二元樹
        // ============================================================
        // 結構：        A
        //             /   \
        //            B     C
        //           / \   / \
        //          D   E F   G
        TreeNode D3 = new TreeNode("D");
        TreeNode E = new TreeNode("E");
        TreeNode F = new TreeNode("F");
        TreeNode G = new TreeNode("G");
        TreeNode B3 = new TreeNode("B", D3, E);
        TreeNode C3 = new TreeNode("C", F, G);
        TreeNode root1 = new TreeNode("A", B3, C3);
        testTraversal(root1, "完全二元樹 (A~G)",
                      java.util.Arrays.asList("A", "B", "D", "E", "C", "F", "G"),
                      java.util.Arrays.asList("D", "B", "E", "A", "F", "C", "G"),
                      java.util.Arrays.asList("D", "E", "B", "F", "G", "C", "A"),
                      java.util.Arrays.asList("A", "B", "C", "D", "E", "F", "G"));

        // ============================================================
        // 6. 不規則樹（非完全二元樹）
        // ============================================================
        // 結構：        A
        //             /   \
        //            B     C
        //           /       \
        //          D         E
        //         / \       /
        //        F   G     H
        TreeNode F2 = new TreeNode("F");
        TreeNode G2 = new TreeNode("G");
        TreeNode D4 = new TreeNode("D", F2, G2);
        TreeNode H = new TreeNode("H");
        TreeNode E2 = new TreeNode("E", H, null);
        TreeNode B4 = new TreeNode("B", D4, null);
        TreeNode C4 = new TreeNode("C", null, E2);
        TreeNode root2 = new TreeNode("A", B4, C4);
        testTraversal(root2, "不規則樹 (非完全二元樹)",
                      java.util.Arrays.asList("A", "B", "D", "F", "G", "C", "E", "H"),
                      java.util.Arrays.asList("F", "D", "G", "B", "A", "C", "H", "E"),
                      java.util.Arrays.asList("F", "G", "D", "B", "H", "E", "C", "A"),
                      java.util.Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H"));

        // ============================================================
        // 7. 另一種不規則樹（只有左右不對稱）
        // ============================================================
        // 結構：        A
        //             /   \
        //            B     C
        //             \   /
        //              D E
        TreeNode D5 = new TreeNode("D");
        TreeNode E3 = new TreeNode("E");
        TreeNode B5 = new TreeNode("B", null, D5);
        TreeNode C5 = new TreeNode("C", E3, null);
        TreeNode root3 = new TreeNode("A", B5, C5);
        testTraversal(root3, "不對稱樹 (B只有右, C只有左)",
                      java.util.Arrays.asList("A", "B", "D", "C", "E"),
                      java.util.Arrays.asList("B", "D", "A", "E", "C"),
                      java.util.Arrays.asList("D", "B", "E", "C", "A"),
                      java.util.Arrays.asList("A", "B", "C", "D", "E"));

        // ============================================================
        // 總結報告
        // ============================================================
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("總結報告");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("已測試 7 種樹結構：");
        System.out.println("  1. 空樹");
        System.out.println("  2. 單節點樹");
        System.out.println("  3. 唯左樹");
        System.out.println("  4. 唯右樹");
        System.out.println("  5. 完全二元樹");
        System.out.println("  6. 不規則樹 (非完全)");
        System.out.println("  7. 不對稱樹 (B只有右, C只有左)");
        System.out.println();
        System.out.println("每種樹都測試了四種遍歷方式：前序、中序、後序、層級");
        System.out.println("所有測試案例的預期與實際結果皆已比對完成。");
        System.out.println("═══════════════════════════════════════════════════════════");
    }
}