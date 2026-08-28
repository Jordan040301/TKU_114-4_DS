import java.util.ArrayList;
import java.util.List;

class StudentRecord {
    final int id;
    final String name;
    int score;

    StudentRecord(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = Math.max(0, Math.min(100, score));
    }

    @Override
    public String toString() {
        return id + " " + name + " score=" + score;
    }
}

class StudentNode {
    StudentRecord data;
    StudentNode left;
    StudentNode right;

    StudentNode(StudentRecord data) {
        this.data = data;
    }
}

class StudentBst {
    private StudentNode root;

    boolean add(StudentRecord student) {
        if (student == null) return false;
        if (root == null) {
            root = new StudentNode(student);
            return true;
        }

        StudentNode current = root;
        while (true) {
            if (student.id == current.data.id) return false;
            if (student.id < current.data.id) {
                if (current.left == null) {
                    current.left = new StudentNode(student);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new StudentNode(student);
                    return true;
                }
                current = current.right;
            }
        }
    }

    StudentRecord find(int id) {
        StudentNode current = root;
        while (current != null) {
            if (id == current.data.id) return current.data;
            current = id < current.data.id ? current.left : current.right;
        }
        return null;
    }

    boolean updateScore(int id, int score) {
        StudentRecord student = find(id);
        if (student == null) return false;
        student.score = Math.max(0, Math.min(100, score));
        return true;
    }

    boolean remove(int id) {
        if (find(id) == null) return false;
        root = remove(root, id);
        return true;
    }

    private StudentNode remove(StudentNode node, int id) {
        if (id < node.data.id) {
            node.left = remove(node.left, id);
        } else if (id > node.data.id) {
            node.right = remove(node.right, id);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            StudentNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data.id);
        }
        return node;
    }

    private StudentNode minimumNode(StudentNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    List<StudentRecord> inorder() {
        List<StudentRecord> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(StudentNode node, List<StudentRecord> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }
}

public class StudentScoreBstReviewSystem {
    public static void main(String[] args) {
        StudentBst index = new StudentBst();
        System.out.println("add=" + index.add(new StudentRecord(300, "Mina", 78)));
        System.out.println("add=" + index.add(new StudentRecord(100, "Leo", 84)));
        System.out.println("add=" + index.add(new StudentRecord(500, "Nora", 91)));
        System.out.println("add=" + index.add(new StudentRecord(200, "Ivy", 69)));
        System.out.println("duplicate="
                + index.add(new StudentRecord(100, "Other", 50)));

        System.out.println("find=" + index.find(200));
        System.out.println("update=" + index.updateScore(200, 88));
        System.out.println("remove=" + index.remove(300));
        System.out.println("missing=" + index.remove(999));

        for (StudentRecord student : index.inorder()) {
            System.out.println(student);
        }
    }
}