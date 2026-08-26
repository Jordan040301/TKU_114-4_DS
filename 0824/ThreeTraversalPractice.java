/**
 * 課堂實作題四：清晰DFS遍歷
 * 指定檔名：ThreeTraversalPractice.java
 * 
 * 對 M(F(B,null),T(R,Z)) 完成前序、中序、後序。
 * 三種方法都要處理 null，不得寫死結果串。
 */
public class ThreeTraversalPractice {

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
     * @param node 當前節點
     * @param sb 字串建構器
     */
    private static void preorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("null");
            return;
        }
        sb.append(node.value);
        sb.append("(");
        preorder(node.left, sb);
        sb.append(",");
        preorder(node.right, sb);
        sb.append(")");
    }

    /**
     * 中序遍歷（左 → 根 → 右）
     * @param node 當前節點
     * @param sb 字串建構器
     */
    private static void inorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("null");
            return;
        }
        sb.append("(");
        inorder(node.left, sb);
        sb.append(",");
        sb.append(node.value);
        sb.append(",");
        inorder(node.right, sb);
        sb.append(")");
    }

    /**
     * 後序遍歷（左 → 右 → 根）
     * @param node 當前節點
     * @param sb 字串建構器
     */
    private static void postorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("null");
            return;
        }
        sb.append("(");
        postorder(node.left, sb);
        sb.append(",");
        postorder(node.right, sb);
        sb.append(",");
        sb.append(node.value);
        sb.append(")");
    }

    /**
     * 公共方法：執行前序遍歷
     * @param root 樹根節點
     * @return 前序遍歷結果字串
     */
    public static String getPreorder(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        preorder(root, sb);
        return sb.toString();
    }

    /**
     * 公共方法：執行中序遍歷
     * @param root 樹根節點
     * @return 中序遍歷結果字串
     */
    public static String getInorder(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        inorder(root, sb);
        return sb.toString();
    }

    /**
     * 公共方法：執行後序遍歷
     * @param root 樹根節點
     * @return 後序遍歷結果字串
     */
    public static String getPostorder(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        postorder(root, sb);
        return sb.toString();
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        // 建立樹：M(F(B,null),T(R,Z))
        // 結構：
        //        M
        //       / \
        //      F   T
        //     /   / \
        //    B   R   Z
        //     \
        //     null
        
        TreeNode B = new TreeNode("B", null, null);
        TreeNode F = new TreeNode("F", B, null);
        TreeNode R = new TreeNode("R");
        TreeNode Z = new TreeNode("Z");
        TreeNode T = new TreeNode("T", R, Z);
        TreeNode M = new TreeNode("M", F, T);

        System.out.println("===== 三種 DFS 遍歷結果 =====");
        System.out.println("樹結構: M(F(B,null),T(R,Z))");
        System.out.println();
        
        System.out.println("前序遍歷 (Preorder): " + getPreorder(M));
        System.out.println("中序遍歷 (Inorder):   " + getInorder(M));
        System.out.println("後序遍歷 (Postorder): " + getPostorder(M));
        
        System.out.println();
        System.out.println("===== 詳細遞迴過程展示 =====");
        System.out.println("前序遍歷詳細: " + getPreorderWithNull(M));
        
        // 測試空樹
        System.out.println();
        System.out.println("===== 空樹測試 =====");
        System.out.println("前序遍歷 (null): " + getPreorder(null));
        System.out.println("中序遍歷 (null): " + getInorder(null));
        System.out.println("後序遍歷 (null): " + getPostorder(null));
    }

    /**
     * 前序遍歷的詳細版本（顯示 null 節點）
     * 用於展示遞迴過程
     */
    private static String getPreorderWithNull(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        preorderDetail(root, sb, 0);
        return sb.toString();
    }

    /**
     * 前序遍歷詳細版本（顯示縮排和 null）
     */
    private static void preorderDetail(TreeNode node, StringBuilder sb, int depth) {
        String indent = "  ".repeat(depth);
        if (node == null) {
            sb.append(indent).append("null\n");
            return;
        }
        sb.append(indent).append(node.value).append("\n");
        preorderDetail(node.left, sb, depth + 1);
        preorderDetail(node.right, sb, depth + 1);
    }
}