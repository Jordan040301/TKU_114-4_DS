

/**
 * 表示運算式樹的節點
 */
class ExpressionNode {
    String value;          // 運算子或運算元
    ExpressionNode left;   // 左子樹
    ExpressionNode right;  // 右子樹

    public ExpressionNode(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public ExpressionNode(String value, ExpressionNode left, ExpressionNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }
}

/**
 * 走訪選擇器 - 以三種順序走訪運算式樹
 */
public class TraversalSelector {

    /**
     * 前序走訪 (Preorder) - 輸出前綴表示法 (Prefix)
     */
    public static String preorder(ExpressionNode node) {
        if (node == null) {
            return "";
        }
        // 根 -> 左 -> 右
        String result = node.value;
        String leftResult = preorder(node.left);
        String rightResult = preorder(node.right);

        // 如果有左右子樹，用空格分隔
        if (!leftResult.isEmpty() && !rightResult.isEmpty()) {
            return result + " " + leftResult + " " + rightResult;
        } else if (!leftResult.isEmpty()) {
            return result + " " + leftResult;
        } else if (!rightResult.isEmpty()) {
            return result + " " + rightResult;
        } else {
            return result;
        }
    }

    /**
     * 中序走訪 (Inorder) - 輸出中綴表示法 (Infix)，包含括號
     */
    public static String inorder(ExpressionNode node) {
        if (node == null) {
            return "";
        }

        // 判斷是否為運算子 (非運算元)
        boolean isOperator = isOperator(node.value);

        // 左子樹結果
        String leftResult = inorder(node.left);
        // 右子樹結果
        String rightResult = inorder(node.right);

        // 如果是葉節點 (運算元)，直接回傳值
        if (node.left == null && node.right == null) {
            return node.value;
        }

        // 建構中序表達式：左 + 根 + 右，並加上括號
        StringBuilder sb = new StringBuilder();
        if (isOperator) {
            sb.append("(");
        }

        if (!leftResult.isEmpty()) {
            sb.append(leftResult);
        }

        sb.append(" ").append(node.value).append(" ");

        if (!rightResult.isEmpty()) {
            sb.append(rightResult);
        }

        if (isOperator) {
            sb.append(")");
        }

        return sb.toString();
    }

    /**
     * 後序走訪 (Postorder) - 輸出後綴表示法 (Postfix)
     */
    public static String postorder(ExpressionNode node) {
        if (node == null) {
            return "";
        }
        // 左 -> 右 -> 根
        String leftResult = postorder(node.left);
        String rightResult = postorder(node.right);

        // 如果有左右子樹，用空格分隔
        if (!leftResult.isEmpty() && !rightResult.isEmpty()) {
            return leftResult + " " + rightResult + " " + node.value;
        } else if (!leftResult.isEmpty()) {
            return leftResult + " " + node.value;
        } else if (!rightResult.isEmpty()) {
            return rightResult + " " + node.value;
        } else {
            return node.value;
        }
    }

    /**
     * 判斷字串是否為運算子
     */
    private static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^");
    }

    /**
     * 建立範例運算式樹：
     * 表達式: (3 + 5) * (7 - 2)
     * 
     * 樹結構：
     *        *
     *       / \
     *      +   -
     *     / \ / \
     *    3  5 7  2
     */
    public static ExpressionNode buildSampleTree() {
        // 建立葉節點
        ExpressionNode leaf3 = new ExpressionNode("3");
        ExpressionNode leaf5 = new ExpressionNode("5");
        ExpressionNode leaf7 = new ExpressionNode("7");
        ExpressionNode leaf2 = new ExpressionNode("2");

        // 建立內部節點
        ExpressionNode plus = new ExpressionNode("+", leaf3, leaf5);
        ExpressionNode minus = new ExpressionNode("-", leaf7, leaf2);
        ExpressionNode multiply = new ExpressionNode("*", plus, minus);

        return multiply;
    }

    /**
     * 建立另一個範例運算式樹：
     * 表達式: (5 + 2) * (8 - 3) / 2
     * 
     * 樹結構：
     *        /
     *       / \
     *      *   2
     *     / \
     *    +   -
     *   / \ / \
     *  5  2 8  3
     */
    public static ExpressionNode buildAnotherTree() {
        ExpressionNode leaf5 = new ExpressionNode("5");
        ExpressionNode leaf2 = new ExpressionNode("2");
        ExpressionNode leaf8 = new ExpressionNode("8");
        ExpressionNode leaf3 = new ExpressionNode("3");
        ExpressionNode leaf2b = new ExpressionNode("2");

        ExpressionNode plus = new ExpressionNode("+", leaf5, leaf2);
        ExpressionNode minus = new ExpressionNode("-", leaf8, leaf3);
        ExpressionNode multiply = new ExpressionNode("*", plus, minus);
        ExpressionNode divide = new ExpressionNode("/", multiply, leaf2b);

        return divide;
    }

    /**
     * 建立單純的運算式樹：3 + 5
     */
    public static ExpressionNode buildSimpleTree() {
        ExpressionNode leaf3 = new ExpressionNode("3");
        ExpressionNode leaf5 = new ExpressionNode("5");
        return new ExpressionNode("+", leaf3, leaf5);
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("        運算式樹走訪選擇器");
        System.out.println("========================================\n");

        // 測試第一棵樹: (3 + 5) * (7 - 2)
        System.out.println("【測試一】運算式樹: (3 + 5) * (7 - 2)");
        System.out.println("----------------------------------------");
        ExpressionNode tree1 = buildSampleTree();

        System.out.println("前序走訪 (前綴表示法): " + preorder(tree1));
        System.out.println("中序走訪 (中綴表示法): " + inorder(tree1));
        System.out.println("後序走訪 (後綴表示法): " + postorder(tree1));
        System.out.println();

        // 測試第二棵樹: (5 + 2) * (8 - 3) / 2
        System.out.println("【測試二】運算式樹: (5 + 2) * (8 - 3) / 2");
        System.out.println("----------------------------------------");
        ExpressionNode tree2 = buildAnotherTree();

        System.out.println("前序走訪 (前綴表示法): " + preorder(tree2));
        System.out.println("中序走訪 (中綴表示法): " + inorder(tree2));
        System.out.println("後序走訪 (後綴表示法): " + postorder(tree2));
        System.out.println();

        // 測試簡單樹: 3 + 5
        System.out.println("【測試三】簡單運算式樹: 3 + 5");
        System.out.println("----------------------------------------");
        ExpressionNode tree3 = buildSimpleTree();

        System.out.println("前序走訪 (前綴表示法): " + preorder(tree3));
        System.out.println("中序走訪 (中綴表示法): " + inorder(tree3));
        System.out.println("後序走訪 (後綴表示法): " + postorder(tree3));
        System.out.println();

        // 測試單一節點
        System.out.println("【測試四】單一節點: 42");
        System.out.println("----------------------------------------");
        ExpressionNode tree4 = new ExpressionNode("42");

        System.out.println("前序走訪 (前綴表示法): " + preorder(tree4));
        System.out.println("中序走訪 (中綴表示法): " + inorder(tree4));
        System.out.println("後序走訪 (後綴表示法): " + postorder(tree4));
        System.out.println();

        System.out.println("========================================");
        System.out.println("        走訪完成！");
        System.out.println("========================================");
    }
}