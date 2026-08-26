/**
 * 課後作業二：二元樹統計系統
 * 指定檔名：BinaryTreeStatistics.java
 * 
 * 完成大小、總和、最大值、葉數、高度與包含。
 * maximum 要明確處理空樹，不可一律用 0 代表空樹頂。
 */
public class BinaryTreeStatistics {

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
     * 計算二元樹的大小（節點總數）
     * @param root 根節點
     * @return 節點總數
     */
    public static int size(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + size(root.left) + size(root.right);
    }

    /**
     * 計算二元樹所有節點值的總和
     * @param root 根節點
     * @return 節點值總和
     */
    public static int sum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return root.value + sum(root.left) + sum(root.right);
    }

    /**
     * 尋找二元樹中的最大值
     * @param root 根節點
     * @return 最大值，若為空樹則回傳 null（表示無最大值）
     */
    public static Integer maximum(TreeNode root) {
        if (root == null) {
            return null; // 明確處理空樹，回傳 null 表示無最大值
        }
        return maximumHelper(root);
    }

    /**
     * 遞迴輔助方法：尋找最大值
     * @param node 當前節點（保證不為 null）
     * @return 子樹中的最大值
     */
    private static int maximumHelper(TreeNode node) {
        int max = node.value;
        
        if (node.left != null) {
            int leftMax = maximumHelper(node.left);
            if (leftMax > max) {
                max = leftMax;
            }
        }
        
        if (node.right != null) {
            int rightMax = maximumHelper(node.right);
            if (rightMax > max) {
                max = rightMax;
            }
        }
        
        return max;
    }

    /**
     * 計算二元樹的葉子節點數量
     * @param root 根節點
     * @return 葉子節點數量
     */
    public static int countLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 如果左右子節點皆為 null，則為葉子節點
        if (root.left == null && root.right == null) {
            return 1;
        }
        return countLeaves(root.left) + countLeaves(root.right);
    }

    /**
     * 計算二元樹的高度（根到最遠葉子的邊數）
     * @param root 根節點
     * @return 高度，空樹回傳 -1
     */
    public static int height(TreeNode root) {
        if (root == null) {
            return -1; // 空樹高度為 -1
        }
        int leftHeight = height(root.left);
        int rightHeight = height(root.right);
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * 檢查二元樹是否包含指定的值
     * @param root 根節點
     * @param target 要尋找的值
     * @return 若包含則回傳 true，否則回傳 false
     */
    public static boolean contains(TreeNode root, int target) {
        if (root == null) {
            return false;
        }
        if (root.value == target) {
            return true;
        }
        return contains(root.left, target) || contains(root.right, target);
    }

    /**
     * 輸出樹的完整統計報告
     */
    public static void printStatistics(TreeNode root, String treeName) {
        System.out.println("===== " + treeName + " =====");
        System.out.println("大小 (節點總數): " + size(root));
        System.out.println("總和: " + sum(root));
        
        Integer maxVal = maximum(root);
        if (maxVal == null) {
            System.out.println("最大值: 空樹 (無最大值)");
        } else {
            System.out.println("最大值: " + maxVal);
        }
        
        System.out.println("葉子節點數: " + countLeaves(root));
        System.out.println("高度: " + height(root));
        System.out.println();
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        System.out.println("===== 二元樹統計系統測試 =====\n");

        // ===== 測試案例 1：完整二元樹 =====
        // 結構：
        //        10
        //       /  \
        //      5    15
        //     / \   / \
        //    2   7 12  20
        
        TreeNode leaf2 = new TreeNode(2);
        TreeNode leaf7 = new TreeNode(7);
        TreeNode node5 = new TreeNode(5, leaf2, leaf7);
        TreeNode leaf12 = new TreeNode(12);
        TreeNode leaf20 = new TreeNode(20);
        TreeNode node15 = new TreeNode(15, leaf12, leaf20);
        TreeNode root1 = new TreeNode(10, node5, node15);
        
        printStatistics(root1, "完整二元樹 (7 個節點)");

        // ===== 測試案例 2：傾斜二元樹（只有左子樹） =====
        // 結構：
        //        8
        //       /
        //      4
        //     /
        //    2
        //   /
        //  1
        
        TreeNode leaf1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2, leaf1, null);
        TreeNode node4 = new TreeNode(4, node2, null);
        TreeNode root2 = new TreeNode(8, node4, null);
        
        printStatistics(root2, "傾斜二元樹 (只有左子樹)");

        // ===== 測試案例 3：不完整二元樹 =====
        // 結構：
        //        6
        //       / \
        //      3   9
        //     /     \
        //    1       12
        //   / \     /
        //  0   2   10
        
        TreeNode leaf0 = new TreeNode(0);
        TreeNode leaf2_2 = new TreeNode(2);
        TreeNode node1 = new TreeNode(1, leaf0, leaf2_2);
        TreeNode node3 = new TreeNode(3, node1, null);
        TreeNode leaf10 = new TreeNode(10);
        TreeNode node12 = new TreeNode(12, leaf10, null);
        TreeNode node9 = new TreeNode(9, null, node12);
        TreeNode root3 = new TreeNode(6, node3, node9);
        
        printStatistics(root3, "不完整二元樹 (8 個節點)");

        // ===== 測試案例 4：單節點樹 =====
        TreeNode root4 = new TreeNode(42);
        printStatistics(root4, "單節點樹");

        // ===== 測試案例 5：空樹 =====
        printStatistics(null, "空樹");

        // ===== 測試案例 6：負數節點 =====
        // 結構：
        //        -5
        //       /  \
        //     -10   3
        //     / \    \
        //   -15  7   -2
        
        TreeNode leafNeg15 = new TreeNode(-15);
        TreeNode leaf7_2 = new TreeNode(7);
        TreeNode nodeNeg10 = new TreeNode(-10, leafNeg15, leaf7_2);
        TreeNode leafNeg2 = new TreeNode(-2);
        TreeNode node3_2 = new TreeNode(3, null, leafNeg2);
        TreeNode root5 = new TreeNode(-5, nodeNeg10, node3_2);
        
        printStatistics(root5, "負數節點的樹");

        // ===== 進一步測試 maximum 方法 =====
        System.out.println("===== maximum 方法詳細測試 =====");
        System.out.println("空樹的 maximum: " + maximum(null) + " (預期 null)");
        System.out.println("單節點樹 [42] 的 maximum: " + maximum(root4) + " (預期 42)");
        System.out.println("完整樹 [10,5,15,2,7,12,20] 的 maximum: " + maximum(root1) + " (預期 20)");
        System.out.println("負數樹 [-5,-10,3,-15,7,-2] 的 maximum: " + maximum(root5) + " (預期 7)");
        System.out.println();

        // ===== 測試 contains 方法 =====
        System.out.println("===== contains 方法測試 =====");
        System.out.println("完整樹 contains(10): " + contains(root1, 10));
        System.out.println("完整樹 contains(7): " + contains(root1, 7));
        System.out.println("完整樹 contains(20): " + contains(root1, 20));
        System.out.println("完整樹 contains(99): " + contains(root1, 99));
        System.out.println("空樹 contains(5): " + contains(null, 5));
        System.out.println();

        // ===== 所有統計資訊彙整 =====
        System.out.println("===== 所有樹統計彙整 =====");
        TreeNode[] trees = {root1, root2, root3, root4, null, root5};
        String[] names = {
            "完整二元樹 (7 節點)",
            "傾斜二元樹 (4 節點)",
            "不完整二元樹 (8 節點)",
            "單節點樹 (1 節點)",
            "空樹 (0 節點)",
            "負數節點樹 (6 節點)"
        };
        
        System.out.printf("%-20s %6s %6s %8s %8s %6s%n", 
                         "樹名稱", "大小", "總和", "最大值", "葉數", "高度");
        System.out.println("-------------------------------------------------------------");
        
        for (int i = 0; i < trees.length; i++) {
            TreeNode tree = trees[i];
            String name = names[i];
            int sz = size(tree);
            int sm = sum(tree);
            Integer max = maximum(tree);
            String maxStr = (max == null) ? "null" : String.valueOf(max);
            int leaves = countLeaves(tree);
            int h = height(tree);
            
            System.out.printf("%-20s %6d %6d %8s %8d %6d%n", 
                             name, sz, sm, maxStr, leaves, h);
        }
    }
}