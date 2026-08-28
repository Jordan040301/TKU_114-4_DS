import java.util.ArrayList;
import java.util.List;

class ReviewNode {
    int value;
    ReviewNode left;
    ReviewNode right;

    ReviewNode(int value) {
        this.value = value;
    }
}

class ReviewBst {
    private ReviewNode root;

    boolean add(int value) {
        if (root == null) {
            root = new ReviewNode(value);
            return true;
        }

        ReviewNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new ReviewNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ReviewNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int target) {
        ReviewNode current = root;
        while (current != null) {
            if (target == current.value) return true;
            current = target < current.value ? current.left : current.right;
        }
        return false;
    }

    List<Integer> searchPath(int target) {
        List<Integer> path = new ArrayList<>();
        ReviewNode current = root;
        while (current != null) {
            path.add(current.value);
            if (target == current.value) break;
            current = target < current.value ? current.left : current.right;
        }
        return path;
    }

    List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(ReviewNode node, List<Integer> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    private void preorder(ReviewNode node, List<Integer> result) {
        if (node == null) return;
        result.add(node.value);
        preorder(node.left, result);
        preorder(node.right, result);
    }

    List<Integer> postorder() {
        List<Integer> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    private void postorder(ReviewNode node, List<Integer> result) {
        if (node == null) return;
        postorder(node.left, result);
        postorder(node.right, result);
        result.add(node.value);
    }

    int size() {
        return size(root);
    }

    private int size(ReviewNode node) {
        if (node == null) return 0;
        return 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(ReviewNode node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    int leafCount() {
        return leafCount(root);
    }

    private int leafCount(ReviewNode node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;
        return leafCount(node.left) + leafCount(node.right);
    }

    String deleteCase(int target) {
        ReviewNode node = findNode(target);
        if (node == null) return "MISSING";
        if (node.left == null && node.right == null) return "LEAF";
        if (node.left == null || node.right == null) return "ONE_CHILD";
        return "TWO_CHILDREN";
    }

    private ReviewNode findNode(int target) {
        ReviewNode current = root;
        while (current != null) {
            if (target == current.value) return current;
            current = target < current.value ? current.left : current.right;
        }
        return null;
    }

    boolean remove(int target) {
        if (!contains(target)) return false;
        root = remove(root, target);
        return true;
    }

    private ReviewNode remove(ReviewNode node, int target) {
        if (target < node.value) {
            node.left = remove(node.left, target);
        } else if (target > node.value) {
            node.right = remove(node.right, target);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            ReviewNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private ReviewNode minimumNode(ReviewNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    List<Integer> range(int low, int high) {
        List<Integer> result = new ArrayList<>();
        if (low <= high) range(root, low, high, result);
        return result;
    }

    private void range(ReviewNode node, int low, int high,
                       List<Integer> result) {
        if (node == null) return;
        if (low < node.value) range(node.left, low, high, result);
        if (low <= node.value && node.value <= high) result.add(node.value);
        if (node.value < high) range(node.right, low, high, result);
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(ReviewNode node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return isValid(node.left, low, node.value)
                && isValid(node.right, node.value, high);
    }

    void forceDeepViolationForTest() {
        if (root != null && root.left != null && root.left.right != null) {
            root.left.right.value = root.value + 5;
        }
    }
}

public class SearchInsertTraceReview {
    private static ReviewBst standardTree() {
        ReviewBst tree = new ReviewBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        return tree;
    }

    public static void main(String[] args) {
        ReviewBst tree = standardTree();
        System.out.println("before=" + tree.searchPath(65));
        System.out.println("add65=" + tree.add(65));
        System.out.println("after=" + tree.searchPath(65));
        System.out.println("duplicate=" + tree.add(65));
        System.out.println("inorder=" + tree.inorder());
    }
}