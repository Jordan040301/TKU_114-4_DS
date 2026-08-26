class PostorderNode {
    String value;
    PostorderNode left;
    PostorderNode right;

    PostorderNode(String value) {
        this.value = value;
    }
}

public class PostorderTraversalDemo {
    static void postorder(PostorderNode node) {
        if (node == null) {
            return;
        }
        postorder(node.left);
        postorder(node.right);
        System.out.print(node.value + " ");
    }

    public static void main(String[] args) {
        PostorderNode root = new PostorderNode("A");
        root.left = new PostorderNode("B");
        root.right = new PostorderNode("C");
        root.left.left = new PostorderNode("D");
        root.left.right = new PostorderNode("E");
        postorder(root);
        System.out.println();
    }
}