import java.util.ArrayDeque;
import java.util.Queue;

class OrgNode {
    String name;
    OrgNode left;
    OrgNode right;

    OrgNode(String name) {
        this.name = name;
    }
}

class OrganizationTree {
    private final OrgNode root;

    OrganizationTree(OrgNode root) {
        this.root = root;
    }

    void preorder() {
        preorder(root);
        System.out.println();
    }

    private void preorder(OrgNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.name + " ");
        preorder(node.left);
        preorder(node.right);
    }

    void levelOrder() {
        if (root == null) {
            System.out.println("empty");
            return;
        }
        Queue<OrgNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            OrgNode node = queue.poll();
            System.out.print(node.name + " ");
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        System.out.println();
    }

    boolean contains(String target) {
        return contains(root, target);
    }

    private boolean contains(OrgNode node, String target) {
        if (node == null || target == null) return false;
        return node.name.equals(target)
                || contains(node.left, target)
                || contains(node.right, target);
    }

    int size() {
        return size(root);
    }

    private int size(OrgNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(OrgNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }
}

public class OrganizationTreeSystem {
    public static void main(String[] args) {
        OrgNode root = new OrgNode("HeadOffice");
        root.left = new OrgNode("Sales");
        root.right = new OrgNode("Technology");
        root.left.left = new OrgNode("Domestic");
        root.left.right = new OrgNode("Export");
        root.right.left = new OrgNode("Platform");
        root.right.right = new OrgNode("Support");

        OrganizationTree tree = new OrganizationTree(root);
        tree.preorder();
        tree.levelOrder();
        System.out.println("Support=" + tree.contains("Support"));
        System.out.println("HR=" + tree.contains("HR"));
        System.out.println("size=" + tree.size());
        System.out.println("height=" + tree.height());
    }
}