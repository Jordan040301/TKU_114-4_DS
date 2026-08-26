/**
 * 課堂實作題五：逐層分行輸出
 * 指定檔名：LevelOrderByLine.java
 * 
 * 使用 Queue 將每層輸出在不同的行，並輸出每層節點數。
 * 處理空樹，不得用 DFS 假裝 level-order。
 */
public class LevelOrderByLine {

    /**
     * 二元樹節點類別
     */
    static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        TreeNode(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        TreeNode(int value, TreeNode left, TreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /**
     * 逐層分行輸出層級順序遍歷
     * @param root 樹根節點
     */
    public static void levelOrderByLine(TreeNode root) {
        if (root == null) {
            System.out.println("空樹：無節點可輸出");
            return;
        }

        // 使用 Java 內建 Queue (LinkedList 實作)
        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.offer(root);
        
        int level = 0;
        
        while (!queue.isEmpty()) {
            // 當前層級的節點數量
            int levelSize = queue.size();
            System.out.print("第 " + level + " 層 (節點數: " + levelSize + "): ");
            
            // 處理當前層級的所有節點
            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                System.out.print(current.value);
                
                // 將下一層的節點加入佇列
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
                
                // 在同一層級中加上分隔符號（除了最後一個節點）
                if (i < levelSize - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(); // 換行
            level++;
        }
    }

    /**
     * 取得逐層輸出的字串結果（用於測試驗證）
     * @param root 樹根節點
     * @return 逐層輸出的字串
     */
    public static String getLevelOrderString(TreeNode root) {
        if (root == null) {
            return "空樹";
        }

        StringBuilder result = new StringBuilder();
        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.offer(root);
        
        int level = 0;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            result.append("第 ").append(level).append(" 層 (節點數: ").append(levelSize).append("): ");
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                result.append(current.value);
                
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
                
                if (i < levelSize - 1) {
                    result.append(", ");
                }
            }
            result.append("\n");
            level++;
        }
        
        return result.toString();
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        System.out.println("===== 逐層分行輸出測試 =====");
        
        // ===== 測試案例 1：完整二元樹 =====
        // 建立結構：
        //        1
        //       / \
        //      2   3
        //     / \ / \
        //    4  5 6  7
        
        TreeNode t1_leaf4 = new TreeNode(4);
        TreeNode t1_leaf5 = new TreeNode(5);
        TreeNode t1_leaf6 = new TreeNode(6);
        TreeNode t1_leaf7 = new TreeNode(7);
        TreeNode t1_node2 = new TreeNode(2, t1_leaf4, t1_leaf5);
        TreeNode t1_node3 = new TreeNode(3, t1_leaf6, t1_leaf7);
        TreeNode t1_root = new TreeNode(1, t1_node2, t1_node3);
        
        System.out.println("測試 1：完整二元樹 (7 個節點)");
        System.out.println("結構：");
        System.out.println("        1");
        System.out.println("       / \\");
        System.out.println("      2   3");
        System.out.println("     / \\ / \\");
        System.out.println("    4  5 6  7");
        System.out.println();
        levelOrderByLine(t1_root);
        System.out.println();

        // ===== 測試案例 2：傾斜二元樹（只有左子樹） =====
        // 建立結構：
        //        1
        //       /
        //      2
        //     /
        //    3
        //   /
        //  4
        
        TreeNode t2_node4 = new TreeNode(4);
        TreeNode t2_node3 = new TreeNode(3, t2_node4, null);
        TreeNode t2_node2 = new TreeNode(2, t2_node3, null);
        TreeNode t2_root = new TreeNode(1, t2_node2, null);
        
        System.out.println("測試 2：傾斜二元樹 (只有左子樹)");
        System.out.println("結構：");
        System.out.println("        1");
        System.out.println("       /");
        System.out.println("      2");
        System.out.println("     /");
        System.out.println("    3");
        System.out.println("   /");
        System.out.println("  4");
        System.out.println();
        levelOrderByLine(t2_root);
        System.out.println();

        // ===== 測試案例 3：不完整二元樹 =====
        // 建立結構：
        //        1
        //       / \
        //      2   3
        //     /     \
        //    4       5
        //   / \     /
        //  6   7   8
        
        TreeNode t3_leaf6 = new TreeNode(6);
        TreeNode t3_leaf7 = new TreeNode(7);
        TreeNode t3_node4 = new TreeNode(4, t3_leaf6, t3_leaf7);
        TreeNode t3_node8 = new TreeNode(8);
        TreeNode t3_node5 = new TreeNode(5, t3_node8, null);
        TreeNode t3_node2 = new TreeNode(2, t3_node4, null);
        TreeNode t3_node3 = new TreeNode(3, null, t3_node5);
        TreeNode t3_root = new TreeNode(1, t3_node2, t3_node3);
        
        System.out.println("測試 3：不完整二元樹");
        System.out.println("結構：");
        System.out.println("        1");
        System.out.println("       / \\");
        System.out.println("      2   3");
        System.out.println("     /     \\");
        System.out.println("    4       5");
        System.out.println("   / \\     /");
        System.out.println("  6   7   8");
        System.out.println();
        levelOrderByLine(t3_root);
        System.out.println();

        // ===== 測試案例 4：單節點樹 =====
        TreeNode t4_root = new TreeNode(42);
        System.out.println("測試 4：單節點樹");
        System.out.println("結構：");
        System.out.println("  42");
        System.out.println();
        levelOrderByLine(t4_root);
        System.out.println();

        // ===== 測試案例 5：空樹 =====
        System.out.println("測試 5：空樹");
        levelOrderByLine(null);
        System.out.println();

        // ===== 使用字串版本進行驗證 =====
        System.out.println("===== 字串輸出驗證 =====");
        System.out.println("完整樹的字串輸出：");
        System.out.println(getLevelOrderString(t1_root));
        
        System.out.println("單節點樹的字串輸出：");
        System.out.println(getLevelOrderString(t4_root));
        
        System.out.println("空樹的字串輸出：");
        System.out.println(getLevelOrderString(null));
    }
}