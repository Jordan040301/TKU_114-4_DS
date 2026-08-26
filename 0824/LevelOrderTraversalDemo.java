import java.util.ArrayDeque;
import java.util.Queue;

class LevelNode {
    String value;
    LevelNode left;
    LevelNode right;

    LevelNode(String value) {
        this.value = value;
    }
}

public class LevelOrderTraversalDemo {
    static void levelOrder(LevelNode root) {
        if (root == null) {
            return;
        }
        Queue<LevelNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            LevelNode current = queue.poll();
            System.out.print(current.value + " ");
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
    }

    public static void main(String[] args) {
        LevelNode root = new LevelNode("A");
        root.left = new LevelNode("B");
        root.right = new LevelNode("C");
        root.left.left = new LevelNode("D");
        root.left.right = new LevelNode("E");
        levelOrder(root);
        System.out.println();
    }
}