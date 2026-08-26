/**
 * 課堂實作題三：二元樹結構模板表
 * 指定檔名：BinaryTreeStructureReport.java
 * 
 * 建立至少7個節點，輸出根、所有葉子、大小、棵數及高度，
 * 另外測試空樹與單節點樹。
 */
public class BinaryTreeStructureReport {

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
    }

    /**
     * 二元樹類別，包含各項統計功能
     */
    static class BinaryTree {
        TreeNode root;

        BinaryTree() {
            this.root = null;
        }

        BinaryTree(TreeNode root) {
            this.root = root;
        }

        /**
         * 取得根節點的值（若樹為空則回傳 null）
         * @return 根節點的值，若空樹則回傳 null
         */
        public Integer getRoot() {
            if (root == null) {
                return null;
            }
            return root.value;
        }

        /**
         * 取得所有葉子節點的值（使用遞迴）
         * @return 葉子節點值的字串表示
         */
        public String getLeaves() {
            if (root == null) {
                return "空樹無葉子";
            }
            StringBuilder sb = new StringBuilder();
            collectLeaves(root, sb);
            return sb.toString().isEmpty() ? "無葉子" : sb.toString();
        }

        /**
         * 遞迴輔助方法：收集葉子節點
         * @param node 當前節點
         * @param sb 字串建構器
         */
        private void collectLeaves(TreeNode node, StringBuilder sb) {
            if (node == null) {
                return;
            }
            // 判斷是否為葉子（左右子節點皆為 null）
            if (node.left == null && node.right == null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(node.value);
                return;
            }
            // 遞迴遍歷左右子樹
            collectLeaves(node.left, sb);
            collectLeaves(node.right, sb);
        }

        /**
         * 取得樹的大小（節點總數）
         * @return 節點總數
         */
        public int getSize() {
            return sizeHelper(root);
        }

        /**
         * 遞迴輔助方法：計算節點總數
         * @param node 當前節點
         * @return 子樹節點總數
         */
        private int sizeHelper(TreeNode node) {
            if (node == null) {
                return 0;
            }
            return 1 + sizeHelper(node.left) + sizeHelper(node.right);
        }

        /**
         * 取得樹的高度（根到最遠葉子的邊數，空樹高度為 -1，單節點高度為 0）
         * @return 樹的高度
         */
        public int getHeight() {
            return heightHelper(root);
        }

        /**
         * 遞迴輔助方法：計算樹的高度
         * @param node 當前節點
         * @return 子樹的高度
         */
        private int heightHelper(TreeNode node) {
            if (node == null) {
                return -1; // 空樹高度為 -1
            }
            int leftHeight = heightHelper(node.left);
            int rightHeight = heightHelper(node.right);
            return 1 + Math.max(leftHeight, rightHeight);
        }

        /**
         * 取得樹的節點數（與 getSize 相同，此處為了符合題目「棵數」用語）
         * @return 節點總數
         */
        public int getNodeCount() {
            return getSize();
        }

        /**
         * 輸出樹的完整結構報告
         */
        public void printReport(String treeName) {
            System.out.println("===== " + treeName + " =====");
            System.out.println("根節點: " + (getRoot() != null ? getRoot() : "空樹"));
            System.out.println("葉子節點: " + getLeaves());
            System.out.println("大小（節點總數）: " + getSize());
            System.out.println("高度: " + getHeight());
            System.out.println();
        }
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        // ===== 建立至少7個節點的樹 =====
        // 建立結構：
        //          10
        //         /  \
        //        5    15
        //       / \     \
        //      2   7     20
        //     / \
        //    1   3
        // 共 9 個節點（符合至少7個）
        
        TreeNode leaf1 = new TreeNode(1);
        TreeNode leaf2 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2, leaf1, leaf2);
        TreeNode node7 = new TreeNode(7);
        TreeNode node5 = new TreeNode(5, node2, node7);
        TreeNode node20 = new TreeNode(20);
        TreeNode node15 = new TreeNode(15, null, node20);
        TreeNode node10 = new TreeNode(10, node5, node15);
        
        BinaryTree bigTree = new BinaryTree(node10);
        bigTree.printReport("大樹（至少7個節點）");

        // ===== 測試單節點樹 =====
        TreeNode singleNode = new TreeNode(42);
        BinaryTree singleTree = new BinaryTree(singleNode);
        singleTree.printReport("單節點樹");

        // ===== 測試空樹 =====
        BinaryTree emptyTree = new BinaryTree();
        emptyTree.printReport("空樹");

        // ===== 額外測試：另一棵至少7節點的樹（不同形狀） =====
        // 建立結構：
        //          8
        //         / \
        //        3   10
        //       / \   \
        //      1   6   14
        //         / \   \
        //        4   7   16
        // 共 10 個節點
        
        TreeNode leaf4 = new TreeNode(4);
        TreeNode leaf7 = new TreeNode(7);
        TreeNode node6 = new TreeNode(6, leaf4, leaf7);
        TreeNode node1 = new TreeNode(1);
        TreeNode node3 = new TreeNode(3, node1, node6);
        TreeNode node16 = new TreeNode(16);
        TreeNode node14 = new TreeNode(14, null, node16);
        TreeNode node10_2 = new TreeNode(10, null, node14);
        TreeNode node8 = new TreeNode(8, node3, node10_2);
        
        BinaryTree anotherTree = new BinaryTree(node8);
        anotherTree.printReport("另一棵大樹（10個節點）");

        // ===== 詳細展示葉子節點的收集 =====
        System.out.println("===== 葉子節點詳細測試 =====");
        System.out.println("大樹的葉子: " + bigTree.getLeaves());
        System.out.println("另一棵樹的葉子: " + anotherTree.getLeaves());
        System.out.println("單節點樹的葉子: " + singleTree.getLeaves());
        System.out.println("空樹的葉子: " + emptyTree.getLeaves());
    }
}