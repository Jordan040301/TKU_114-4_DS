class NumberTreeNode {
    int value;
    NumberTreeNode left;
    NumberTreeNode right;

    NumberTreeNode(int value) {
        this.value = value;
    }
}

public class TreeTerminologyDemo {
    static int size(NumberTreeNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    static int leaves(NumberTreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return leaves(node.left) + leaves(node.right);
    }

    static int height(NumberTreeNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    public static void main(String[] args) {
        NumberTreeNode root = new NumberTreeNode(10);
        root.left = new NumberTreeNode(5);
        root.right = new NumberTreeNode(20);
        root.left.left = new NumberTreeNode(3);

        System.out.println("size=" + size(root));
        System.out.println("leaves=" + leaves(root));
        System.out.println("height=" + height(root));
        System.out.println("empty height=" + height(null));
    }
}