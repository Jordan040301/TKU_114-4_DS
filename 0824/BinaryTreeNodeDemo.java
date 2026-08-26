class TextTreeNode {
    String value;
    TextTreeNode left;
    TextTreeNode right;

    TextTreeNode(String value) {
        this.value = value;
    }
}

public class BinaryTreeNodeDemo {
    public static void main(String[] args) {
        TextTreeNode root = new TextTreeNode("A");
        root.left = new TextTreeNode("B");
        root.right = new TextTreeNode("C");
        root.left.left = new TextTreeNode("D");

        System.out.println("root=" + root.value);
        System.out.println("left=" + root.left.value);
        System.out.println("right=" + root.right.value);
        System.out.println("left-left=" + root.left.left.value);
        System.out.println("left-right=" + root.left.right);
    }
}