class PreorderNode {
    String value;
    PreorderNode left;
    PreorderNode right;

    PreorderNode(String value) {
        this.value = value;
    }
}

public class PreorderTraversalDemo {
    static void preorder(PreorderNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.value + " ");
        preorder(node.left);
        preorder(node.right);
    }

    public static void main(String[] args) {
        PreorderNode root = new PreorderNode("A");
        root.left = new PreorderNode("B");
        root.right = new PreorderNode("C");
        root.left.left = new PreorderNode("D");
        root.left.right = new PreorderNode("E");

        preorder(root);
        System.out.println();
        preorder(null);
        System.out.println("empty done");
    }
}