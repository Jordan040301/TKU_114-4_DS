import java.util.*;

public class Q11_BstHashDirectory {
    // HashMap：id -> name（快速查找）
    private Map<Integer, String> idMap;
    // BST 根节点
    private TreeNode root;
    // 元素数量
    private int size;

    /**
     * BST 节点类
     */
    private static class TreeNode {
        int id;
        String name;
        TreeNode left;
        TreeNode right;

        TreeNode(int id, String name) {
            this.id = id;
            this.name = name;
            this.left = null;
            this.right = null;
        }
    }

    public Q11_BstHashDirectory() {
        this.idMap = new HashMap<>();
        this.root = null;
        this.size = 0;
    }

    /**
     * 添加记录
     * @param id ID（必须大于0）
     * @param name 姓名（trim后非空）
     * @return 添加成功返回 true，失败返回 false
     */
    public boolean add(int id, String name) {
        // 验证 id > 0
        if (id <= 0) {
            return false;
        }

        // 验证 name 不为 null 且 trim 后非空
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        String trimmedName = name.trim();

        // 检查 id 是否已存在（重复）
        if (idMap.containsKey(id)) {
            return false;
        }

        // 添加到 HashMap
        idMap.put(id, trimmedName);

        // 添加到 BST
        root = addRecursive(root, id, trimmedName);
        size++;

        return true;
    }

    /**
     * 递归添加节点到 BST
     */
    private TreeNode addRecursive(TreeNode node, int id, String name) {
        if (node == null) {
            return new TreeNode(id, name);
        }

        if (id < node.id) {
            node.left = addRecursive(node.left, id, name);
        } else if (id > node.id) {
            node.right = addRecursive(node.right, id, name);
        }
        // id 相等的情况已在外部检查，不会发生
        return node;
    }

    /**
     * 根据 id 查找姓名
     * @param id ID
     * @return 对应的姓名，不存在返回 null
     */
    public String findName(int id) {
        // 直接从 HashMap 查找
        return idMap.get(id);
    }

    /**
     * 删除记录
     * @param id ID
     * @return 删除成功返回 true，失败返回 false
     */
    public boolean remove(int id) {
        // 检查 id 是否存在
        if (!idMap.containsKey(id)) {
            return false;
        }

        // 从 HashMap 删除
        idMap.remove(id);

        // 从 BST 删除
        root = removeRecursive(root, id);
        size--;

        return true;
    }

    /**
     * 递归删除 BST 节点
     */
    private TreeNode removeRecursive(TreeNode node, int id) {
        if (node == null) {
            return null;
        }

        if (id < node.id) {
            node.left = removeRecursive(node.left, id);
        } else if (id > node.id) {
            node.right = removeRecursive(node.right, id);
        } else {
            // 找到要删除的节点

            // 情况1：叶子节点
            if (node.left == null && node.right == null) {
                return null;
            }

            // 情况2：只有一个子节点
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            // 情况3：有两个子节点
            // 找到右子树中的最小节点（中序后继）
            TreeNode successor = findMin(node.right);
            // 复制 successor 的值到当前节点
            node.id = successor.id;
            node.name = successor.name;
            // 删除 successor
            node.right = removeRecursive(node.right, successor.id);
        }

        return node;
    }

    /**
     * 查找子树中的最小节点
     */
    private TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * 获取 id 在 [low, high] 范围内的所有 id（递增顺序）
     * @param low 下限
     * @param high 上限
     * @return 范围内的 id 列表（递增），无效输入返回空列表
     */
    public List<Integer> idsBetween(int low, int high) {
        // low > high 返回空列表
        if (low > high) {
            return new ArrayList<>();
        }

        List<Integer> result = new ArrayList<>();
        idsBetweenRecursive(root, low, high, result);
        return result;
    }

    /**
     * 递归中序遍历获取范围内的 id
     */
    private void idsBetweenRecursive(TreeNode node, int low, int high, List<Integer> result) {
        if (node == null) {
            return;
        }

        // 中序遍历：左 -> 根 -> 右
        // 如果当前 id > low，才需要遍历左子树
        if (node.id > low) {
            idsBetweenRecursive(node.left, low, high, result);
        }

        // 检查当前节点是否在范围内
        if (node.id >= low && node.id <= high) {
            result.add(node.id);
        }

        // 如果当前 id < high，才需要遍历右子树
        if (node.id < high) {
            idsBetweenRecursive(node.right, low, high, result);
        }
    }

    /**
     * 返回记录总数
     */
    public int size() {
        return size;
    }

    /**
     * 验证双索引一致性（用于测试）
     */
    public boolean isConsistent() {
        // 检查 size 是否一致
        if (idMap.size() != size) {
            return false;
        }

        // 检查 BST 节点数是否与 size 一致
        if (countNodes(root) != size) {
            return false;
        }

        // 检查每个 id 在 BST 中是否存在且 name 一致
        for (Map.Entry<Integer, String> entry : idMap.entrySet()) {
            int id = entry.getKey();
            String name = entry.getValue();
            String bstName = findInBST(root, id);
            if (bstName == null || !bstName.equals(name)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 在 BST 中查找 id 对应的 name
     */
    private String findInBST(TreeNode node, int id) {
        while (node != null) {
            if (id == node.id) {
                return node.name;
            } else if (id < node.id) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    /**
     * 计算 BST 节点数
     */
    private int countNodes(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    /**
     * 中序遍历打印 BST（用于调试）
     */
    public void printInOrder() {
        System.out.print("BST in-order: ");
        printInOrderRecursive(root);
        System.out.println();
    }

    private void printInOrderRecursive(TreeNode node) {
        if (node == null) {
            return;
        }
        printInOrderRecursive(node.left);
        System.out.print("(" + node.id + ":" + node.name + ") ");
        printInOrderRecursive(node.right);
    }

    // ========== main 测试 ==========

    public static void main(String[] args) {
        Q11_BstHashDirectory directory = new Q11_BstHashDirectory();

        System.out.println("=== Test add ===");
        System.out.println("Add (1, Alice): " + directory.add(1, "Alice"));
        System.out.println("Add (3, Bob): " + directory.add(3, "Bob"));
        System.out.println("Add (2, Charlie): " + directory.add(2, "Charlie"));
        System.out.println("Add (4, David): " + directory.add(4, "David"));
        System.out.println("Add (1, Eve) (duplicate): " + directory.add(1, "Eve"));
        System.out.println("Add (0, Zero) (invalid id): " + directory.add(0, "Zero"));
        System.out.println("Add (5, null) (null name): " + directory.add(5, null));
        System.out.println("Add (5, '  ') (blank name): " + directory.add(5, "  "));

        directory.printInOrder();
        System.out.println("Size: " + directory.size());

        System.out.println("\n=== Test findName ===");
        System.out.println("Find 1: " + directory.findName(1));    // Alice
        System.out.println("Find 2: " + directory.findName(2));    // Charlie
        System.out.println("Find 3: " + directory.findName(3));    // Bob
        System.out.println("Find 4: " + directory.findName(4));    // David
        System.out.println("Find 5 (not exist): " + directory.findName(5)); // null

        System.out.println("\n=== Test idsBetween ===");
        System.out.println("idsBetween(2, 3): " + directory.idsBetween(2, 3));   // [2, 3]
        System.out.println("idsBetween(1, 4): " + directory.idsBetween(1, 4));   // [1, 2, 3, 4]
        System.out.println("idsBetween(5, 10): " + directory.idsBetween(5, 10)); // []
        System.out.println("idsBetween(3, 1) (low > high): " + directory.idsBetween(3, 1)); // []

        System.out.println("\n=== Test remove ===");
        System.out.println("Remove 3: " + directory.remove(3));    // true
        System.out.println("Size after remove: " + directory.size());
        directory.printInOrder();
        System.out.println("Find 3 after remove: " + directory.findName(3)); // null

        System.out.println("\n=== Test consistency ===");
        System.out.println("Is consistent: " + directory.isConsistent());

        // 删除更多
        System.out.println("Remove 1: " + directory.remove(1));
        System.out.println("Remove 4: " + directory.remove(4));
        System.out.println("Size: " + directory.size());
        directory.printInOrder();

        // 测试删除根节点（2）
        System.out.println("Remove 2: " + directory.remove(2));
        System.out.println("Size: " + directory.size());
        directory.printInOrder();

        System.out.println("\n=== Test remove from empty ===");
        System.out.println("Remove 10 from empty: " + directory.remove(10)); // false

        // 测试大规模数据
        System.out.println("\n=== Test Large Data ===");
        Q11_BstHashDirectory largeDir = new Q11_BstHashDirectory();
        int[] ids = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45, 55, 65, 75, 90};
        for (int id : ids) {
            largeDir.add(id, "Name" + id);
        }
        System.out.println("Size: " + largeDir.size());
        System.out.println("idsBetween(30, 60): " + largeDir.idsBetween(30, 60));
        System.out.println("Is consistent: " + largeDir.isConsistent());
    }
}