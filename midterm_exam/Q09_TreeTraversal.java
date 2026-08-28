/**
 * 檔名：Q09_TreeTraversal.java
 * 功能：二元樹四種走訪
 * 說明：preorder、inorder、postorder 使用 recursion
 *       levelOrder 使用 Queue/Deque
 */

import java.util.*;

public class Q09_TreeTraversal {

    // ========== Node 類別 ==========
    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    // ========== 1. 前序走訪 (Preorder) ==========
    // 順序：根 → 左 → 右

    /**
     * 前序走訪（遞迴）
     * @param root 根節點
     * @return 前序走訪結果列表
     */
    public static List<Integer> preorder(Node root) {
        List<Integer> result = new ArrayList<>();
        preorderRec(root, result);
        return result;
    }

    private static void preorderRec(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }
        // 根 → 左 → 右
        result.add(node.value);
        preorderRec(node.left, result);
        preorderRec(node.right, result);
    }

    // ========== 2. 中序走訪 (Inorder) ==========
    // 順序：左 → 根 → 右

    /**
     * 中序走訪（遞迴）
     * @param root 根節點
     * @return 中序走訪結果列表
     */
    public static List<Integer> inorder(Node root) {
        List<Integer> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }

    private static void inorderRec(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }
        // 左 → 根 → 右
        inorderRec(node.left, result);
        result.add(node.value);
        inorderRec(node.right, result);
    }

    // ========== 3. 後序走訪 (Postorder) ==========
    // 順序：左 → 右 → 根

    /**
     * 後序走訪（遞迴）
     * @param root 根節點
     * @return 後序走訪結果列表
     */
    public static List<Integer> postorder(Node root) {
        List<Integer> result = new ArrayList<>();
        postorderRec(root, result);
        return result;
    }

    private static void postorderRec(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }
        // 左 → 右 → 根
        postorderRec(node.left, result);
        postorderRec(node.right, result);
        result.add(node.value);
    }

    // ========== 4. 層序走訪 (Level Order) ==========
    // 使用 Queue（BFS）

    /**
     * 層序走訪（使用 Queue）
     * @param root 根節點
     * @return 層序走訪結果列表
     */
    public static List<Integer> levelOrder(Node root) {
        List<Integer> result = new ArrayList<>();

        // root 為 null 時回傳 empty List
        if (root == null) {
            return result;
        }

        // 使用 Deque 作為 Queue
        Deque<Node> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            result.add(current.value);

            // 先左後右
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return result;
    }

    // ========== 輔助方法：建立測試樹 ==========

    /**
     * 建立測試樹：
     *        8
     *       / \
     *      4   12
     *     / \  / \
     *    2   6    14
     */
    public static Node createTestTree() {
        Node root = new Node(8);
        root.left = new Node(4);
        root.right = new Node(12);
        root.left.left = new Node(2);
        root.left.right = new Node(6);
        root.right.right = new Node(14);
        return root;
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試樹 =====");
        System.out.println("        8");
        System.out.println("       / \\");
        System.out.println("      4   12");
        System.out.println("     / \\  / \\");
        System.out.println("    2   6    14");
        System.out.println();

        Node root = createTestTree();

        // ===== 測試四種走訪 =====
        System.out.println("===== 四種走訪結果 =====");
        System.out.println("preorder  = " + preorder(root));
        System.out.println("inorder   = " + inorder(root));
        System.out.println("postorder = " + postorder(root));
        System.out.println("levelOrder= " + levelOrder(root));
        System.out.println();

        // ===== 驗證正確結果 =====
        System.out.println("===== 驗證正確結果 =====");
        List<Integer> expectedPre = Arrays.asList(8, 4, 2, 6, 12, 14);
        List<Integer> expectedIn = Arrays.asList(2, 4, 6, 8, 12, 14);
        List<Integer> expectedPost = Arrays.asList(2, 6, 4, 14, 12, 8);
        List<Integer> expectedLevel = Arrays.asList(8, 4, 12, 2, 6, 14);

        System.out.println("preorder  正確: " + preorder(root).equals(expectedPre));
        System.out.println("inorder   正確: " + inorder(root).equals(expectedIn));
        System.out.println("postorder 正確: " + postorder(root).equals(expectedPost));
        System.out.println("levelOrder正確: " + levelOrder(root).equals(expectedLevel));
        System.out.println();

        // ===== 測試 root 為 null =====
        System.out.println("===== root = null 測試 =====");
        System.out.println("preorder(null)  = " + preorder(null));
        System.out.println("inorder(null)   = " + inorder(null));
        System.out.println("postorder(null) = " + postorder(null));
        System.out.println("levelOrder(null)= " + levelOrder(null));
        System.out.println();

        // ===== 測試重複呼叫（每次建立新的 result） =====
        System.out.println("===== 重複呼叫測試 =====");
        List<Integer> result1 = preorder(root);
        List<Integer> result2 = preorder(root);
        System.out.println("第一次呼叫: " + result1);
        System.out.println("第二次呼叫: " + result2);
        System.out.println("兩次結果相同: " + result1.equals(result2));
        System.out.println();

        // ===== 測試不同形狀的樹 =====
        System.out.println("===== 不同形狀樹測試 =====");

        // 只有左子樹
        Node leftOnly = new Node(10);
        leftOnly.left = new Node(5);
        leftOnly.left.left = new Node(2);
        System.out.println("只有左子樹:");
        System.out.println("  preorder  = " + preorder(leftOnly));   // [10, 5, 2]
        System.out.println("  inorder   = " + inorder(leftOnly));    // [2, 5, 10]
        System.out.println("  postorder = " + postorder(leftOnly));  // [2, 5, 10]
        System.out.println("  levelOrder= " + levelOrder(leftOnly)); // [10, 5, 2]

        // 只有右子樹
        Node rightOnly = new Node(10);
        rightOnly.right = new Node(15);
        rightOnly.right.right = new Node(20);
        System.out.println("只有右子樹:");
        System.out.println("  preorder  = " + preorder(rightOnly));   // [10, 15, 20]
        System.out.println("  inorder   = " + inorder(rightOnly));    // [10, 15, 20]
        System.out.println("  postorder = " + postorder(rightOnly));  // [20, 15, 10]
        System.out.println("  levelOrder= " + levelOrder(rightOnly)); // [10, 15, 20]

        // 單節點
        Node single = new Node(42);
        System.out.println("單節點:");
        System.out.println("  preorder  = " + preorder(single));   // [42]
        System.out.println("  inorder   = " + inorder(single));    // [42]
        System.out.println("  postorder = " + postorder(single));  // [42]
        System.out.println("  levelOrder= " + levelOrder(single)); // [42]
        System.out.println();

        // ===== 測試完全二元樹 =====
        System.out.println("===== 完全二元樹測試 =====");
        Node fullTree = new Node(1);
        fullTree.left = new Node(2);
        fullTree.right = new Node(3);
        fullTree.left.left = new Node(4);
        fullTree.left.right = new Node(5);
        fullTree.right.left = new Node(6);
        fullTree.right.right = new Node(7);

        System.out.println("完全二元樹:");
        System.out.println("  preorder  = " + preorder(fullTree));   // [1, 2, 4, 5, 3, 6, 7]
        System.out.println("  inorder   = " + inorder(fullTree));    // [4, 2, 5, 1, 6, 3, 7]
        System.out.println("  postorder = " + postorder(fullTree));  // [4, 5, 2, 6, 7, 3, 1]
        System.out.println("  levelOrder= " + levelOrder(fullTree)); // [1, 2, 3, 4, 5, 6, 7]
    }
}