class InorderNode {
    int value;
    InorderNode left;
    InorderNode right;

    InorderNode(int value) {
        this.value = value;
    }
}

public class InorderTraversalDemo {
    static void inorder(InorderNode node) {
        if (node == null) {
            return;
        }
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }

    public static void main(String[] args) {
        InorderNode root = new InorderNode(40);
        root.left = new InorderNode(10);
        root.right = new InorderNode(30);
        root.left.left = new InorderNode(20);

        inorder(root);
        System.out.println();
        System.out.println("ordinary binary tree");
    }
}