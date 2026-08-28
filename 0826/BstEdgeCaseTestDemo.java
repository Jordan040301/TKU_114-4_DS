import java.util.*;

/**
 * 二元搜尋樹 (BST) 節點
 */
class BSTNode {
    int value;
    BSTNode left;
    BSTNode right;

    public BSTNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

/**
 * 二元搜尋樹 (BST) 實作
 */
class ReviewBst {
    private BSTNode root;
    private int size;

    public ReviewBst() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 新增元素 (不允許重複)
     */
    public boolean add(int value) {
        if (contains(value)) {
            return false;  // 重複元素，新增失敗
        }
        root = addRecursive(root, value);
        size++;
        return true;
    }

    private BSTNode addRecursive(BSTNode node, int value) {
        if (node == null) {
            return new BSTNode(value);
        }
        if (value < node.value) {
            node.left = addRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = addRecursive(node.right, value);
        }
        return node;
    }

    /**
     * 移除元素
     */
    public boolean remove(int value) {
        if (!contains(value)) {
            return false;  // 元素不存在，移除失敗
        }
        root = removeRecursive(root, value);
        size--;
        return true;
    }

    private BSTNode removeRecursive(BSTNode node, int value) {
        if (node == null) {
            return null;
        }
        if (value < node.value) {
            node.left = removeRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = removeRecursive(node.right, value);
        } else {
            // Case 1: 葉節點
            if (node.left == null && node.right == null) {
                return null;
            }
            // Case 2: 只有一個子節點
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            // Case 3: 有兩個子節點
            int successor = findMin(node.right);
            node.value = successor;
            node.right = removeRecursive(node.right, successor);
        }
        return node;
    }

    /**
     * 尋找子樹中的最小值
     */
    private int findMin(BSTNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.value;
    }

    /**
     * 檢查是否包含某個值
     */
    public boolean contains(int value) {
        return containsRecursive(root, value);
    }

    private boolean containsRecursive(BSTNode node, int value) {
        if (node == null) {
            return false;
        }
        if (value == node.value) {
            return true;
        }
        if (value < node.value) {
            return containsRecursive(node.left, value);
        } else {
            return containsRecursive(node.right, value);
        }
    }

    /**
     * 中序走訪 (Inorder)
     */
    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderRecursive(root, result);
        return result;
    }

    private void inorderRecursive(BSTNode node, List<Integer> result) {
        if (node != null) {
            inorderRecursive(node.left, result);
            result.add(node.value);
            inorderRecursive(node.right, result);
        }
    }

    /**
     * 前序走訪 (Preorder)
     */
    public List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorderRecursive(root, result);
        return result;
    }

    private void preorderRecursive(BSTNode node, List<Integer> result) {
        if (node != null) {
            result.add(node.value);
            preorderRecursive(node.left, result);
            preorderRecursive(node.right, result);
        }
    }

    /**
     * 後序走訪 (Postorder)
     */
    public List<Integer> postorder() {
        List<Integer> result = new ArrayList<>();
        postorderRecursive(root, result);
        return result;
    }

    private void postorderRecursive(BSTNode node, List<Integer> result) {
        if (node != null) {
            postorderRecursive(node.left, result);
            postorderRecursive(node.right, result);
            result.add(node.value);
        }
    }

    /**
     * 取得樹的大小
     */
    public int size() {
        return size;
    }

    /**
     * 檢查樹是否為空
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * 取得樹的高度
     */
    public int height() {
        return heightRecursive(root);
    }

    private int heightRecursive(BSTNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(heightRecursive(node.left), heightRecursive(node.right));
    }

    /**
     * 驗證是否為有效的二元搜尋樹
     */
    public boolean isValid() {
        return isValidRecursive(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isValidRecursive(BSTNode node, int min, int max) {
        if (node == null) {
            return true;
        }
        if (node.value < min || node.value > max) {
            return false;
        }
        return isValidRecursive(node.left, min, node.value - 1) &&
               isValidRecursive(node.right, node.value + 1, max);
    }

    /**
     * 取得根節點值
     */
    public int getRoot() {
        return root != null ? root.value : -1;
    }

    /**
     * 範圍查詢 (Range Query)
     */
    public List<Integer> getRange(int low, int high) {
        List<Integer> result = new ArrayList<>();
        if (low > high) {
            return result;
        }
        rangeRecursive(root, low, high, result);
        return result;
    }

    private void rangeRecursive(BSTNode node, int low, int high, List<Integer> result) {
        if (node == null) {
            return;
        }
        if (node.value > low) {
            rangeRecursive(node.left, low, high, result);
        }
        if (node.value >= low && node.value <= high) {
            result.add(node.value);
        }
        if (node.value < high) {
            rangeRecursive(node.right, low, high, result);
        }
    }

    /**
     * 清空樹
     */
    public void clear() {
        root = null;
        size = 0;
    }
}

/**
 * BST 邊界情況測試示範
 */
public class BstEdgeCaseTestDemo {

    /**
     * 建立標準樹
     */
    private static ReviewBst standardTree() {
        ReviewBst tree = new ReviewBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        return tree;
    }

    /**
     * 建立隨機樹
     */
    private static ReviewBst randomTree() {
        ReviewBst tree = new ReviewBst();
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            values.add(i * 10);
        }
        Collections.shuffle(values);
        for (int value : values) {
            tree.add(value);
        }
        return tree;
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("     BST 邊界情況測試示範");
        System.out.println("========================================\n");

        // ============================================================
        // 測試 1: 空樹測試 (Empty Tree)
        // ============================================================
        System.out.println("【測試 1】空樹測試 (Empty Tree)");
        System.out.println("----------------------------------------");
        ReviewBst emptyTree = new ReviewBst();

        System.out.println("空樹是否為空: " + emptyTree.isEmpty());
        System.out.println("空樹大小: " + emptyTree.size());
        System.out.println("空樹高度: " + emptyTree.height());
        System.out.println("空樹是否有效: " + emptyTree.isValid());
        System.out.println("emptyFind (contains 10): " + emptyTree.contains(10));
        System.out.println("emptyRemove (remove 10): " + emptyTree.remove(10));
        System.out.println("emptyFind 後大小: " + emptyTree.size());
        System.out.println();

        // ============================================================
        // 測試 2: 新增/刪除根節點 (Root Operations)
        // ============================================================
        System.out.println("【測試 2】新增/刪除根節點 (Root Operations)");
        System.out.println("----------------------------------------");
        ReviewBst rootTree = new ReviewBst();

        System.out.println("addRoot (add 10): " + rootTree.add(10));
        System.out.println("新增後大小: " + rootTree.size());
        System.out.println("新增後高度: " + rootTree.height());
        System.out.println("新增後根節點: " + rootTree.getRoot());
        System.out.println("新增後中序走訪: " + rootTree.inorder());
        System.out.println("新增後是否有效: " + rootTree.isValid());

        System.out.println("duplicate (add 10): " + rootTree.add(10));
        System.out.println("重複新增後大小: " + rootTree.size());

        System.out.println("removeRoot (remove 10): " + rootTree.remove(10));
        System.out.println("刪除後大小: " + rootTree.size());
        System.out.println("刪除後高度: " + rootTree.height());
        System.out.println("刪除後中序走訪: " + rootTree.inorder());
        System.out.println("刪除後是否有效: " + rootTree.isValid());
        System.out.println();

        // ============================================================
        // 測試 3: 標準樹測試 (Standard Tree)
        // ============================================================
        System.out.println("【測試 3】標準樹測試 (Standard Tree)");
        System.out.println("----------------------------------------");
        ReviewBst standardTree = standardTree();

        System.out.println("標準樹大小: " + standardTree.size());
        System.out.println("標準樹高度: " + standardTree.height());
        System.out.println("標準樹根節點: " + standardTree.getRoot());
        System.out.println("標準樹中序走訪: " + standardTree.inorder());
        System.out.println("標準樹前序走訪: " + standardTree.preorder());
        System.out.println("標準樹後序走訪: " + standardTree.postorder());
        System.out.println("標準樹是否有效: " + standardTree.isValid());
        System.out.println("標準樹是否包含 30: " + standardTree.contains(30));
        System.out.println("標準樹是否包含 100: " + standardTree.contains(100));
        System.out.println();

        // ============================================================
        // 測試 4: 葉節點刪除 (Delete Leaf)
        // ============================================================
        System.out.println("【測試 4】葉節點刪除 (Delete Leaf)");
        System.out.println("----------------------------------------");
        ReviewBst leafTree = standardTree();
        System.out.println("原始中序走訪: " + leafTree.inorder());

        System.out.println("刪除葉節點 20: " + leafTree.remove(20));
        System.out.println("刪除後中序走訪: " + leafTree.inorder());
        System.out.println("刪除後大小: " + leafTree.size());
        System.out.println("刪除後是否有效: " + leafTree.isValid());
        System.out.println();

        System.out.println("刪除葉節點 80: " + leafTree.remove(80));
        System.out.println("刪除後中序走訪: " + leafTree.inorder());
        System.out.println("刪除後大小: " + leafTree.size());
        System.out.println("刪除後是否有效: " + leafTree.isValid());
        System.out.println();

        // ============================================================
        // 測試 5: 只有一個子節點刪除 (Delete with One Child)
        // ============================================================
        System.out.println("【測試 5】只有一個子節點刪除 (Delete with One Child)");
        System.out.println("----------------------------------------");
        ReviewBst oneChildTree = new ReviewBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80, 25}) {
            oneChildTree.add(value);
        }
        System.out.println("原始中序走訪: " + oneChildTree.inorder());

        // 刪除 20 (只有右子節點 25)
        System.out.println("刪除節點 20 (只有右子節點): " + oneChildTree.remove(20));
        System.out.println("刪除後中序走訪: " + oneChildTree.inorder());
        System.out.println("刪除後大小: " + oneChildTree.size());
        System.out.println("刪除後是否有效: " + oneChildTree.isValid());
        System.out.println();

        // 刪除 25 (只有左子節點? 實際上 25 是葉節點)
        System.out.println("刪除葉節點 25: " + oneChildTree.remove(25));
        System.out.println("刪除後中序走訪: " + oneChildTree.inorder());
        System.out.println("刪除後大小: " + oneChildTree.size());
        System.out.println();

        // 建立只有左子節點的情況
        ReviewBst oneChildTree2 = new ReviewBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80, 15}) {
            oneChildTree2.add(value);
        }
        System.out.println("新樹中序走訪: " + oneChildTree2.inorder());
        System.out.println("刪除節點 20 (只有左子節點 15): " + oneChildTree2.remove(20));
        System.out.println("刪除後中序走訪: " + oneChildTree2.inorder());
        System.out.println("刪除後大小: " + oneChildTree2.size());
        System.out.println();

        // ============================================================
        // 測試 6: 兩個子節點刪除 (Delete with Two Children)
        // ============================================================
        System.out.println("【測試 6】兩個子節點刪除 (Delete with Two Children)");
        System.out.println("----------------------------------------");
        ReviewBst twoChildTree = standardTree();
        System.out.println("原始中序走訪: " + twoChildTree.inorder());

        System.out.println("刪除節點 50 (有兩個子節點): " + twoChildTree.remove(50));
        System.out.println("刪除後中序走訪: " + twoChildTree.inorder());
        System.out.println("刪除後大小: " + twoChildTree.size());
        System.out.println("刪除後根節點: " + twoChildTree.getRoot());
        System.out.println("刪除後是否有效: " + twoChildTree.isValid());
        System.out.println();

        System.out.println("刪除節點 30 (有兩個子節點): " + twoChildTree.remove(30));
        System.out.println("刪除後中序走訪: " + twoChildTree.inorder());
        System.out.println("刪除後大小: " + twoChildTree.size());
        System.out.println("刪除後是否有效: " + twoChildTree.isValid());
        System.out.println();

        // ============================================================
        // 測試 7: 缺少元素刪除 (Delete Missing Element)
        // ============================================================
        System.out.println("【測試 7】缺少元素刪除 (Delete Missing Element)");
        System.out.println("----------------------------------------");
        ReviewBst missingTree = standardTree();
        System.out.println("原始中序走訪: " + missingTree.inorder());

        System.out.println("刪除不存在元素 100: " + missingTree.remove(100));
        System.out.println("刪除不存在元素 25: " + missingTree.remove(25));
        System.out.println("刪除不存在元素 999: " + missingTree.remove(999));
        System.out.println("刪除後大小: " + missingTree.size());
        System.out.println("刪除後中序走訪: " + missingTree.inorder());
        System.out.println();

        // ============================================================
        // 測試 8: 範圍查詢 (Range Query)
        // ============================================================
        System.out.println("【測試 8】範圍查詢 (Range Query)");
        System.out.println("----------------------------------------");
        ReviewBst rangeTree = new ReviewBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45, 55, 65, 75, 85}) {
            rangeTree.add(value);
        }
        System.out.println("完整中序走訪: " + rangeTree.inorder());

        System.out.println("範圍查詢 [30, 60]: " + rangeTree.getRange(30, 60));
        System.out.println("範圍查詢 [10, 85]: " + rangeTree.getRange(10, 85));
        System.out.println("範圍查詢 [50, 50]: " + rangeTree.getRange(50, 50));
        System.out.println("範圍查詢 [26, 29]: " + rangeTree.getRange(26, 29));
        System.out.println("範圍查詢 [1, 9]: " + rangeTree.getRange(1, 9));
        System.out.println("範圍查詢 [86, 100]: " + rangeTree.getRange(86, 100));
        System.out.println("範圍查詢 [60, 30] (low > high): " + rangeTree.getRange(60, 30));
        System.out.println();

        // ============================================================
        // 測試 9: 重複元素測試 (Duplicate Elements)
        // ============================================================
        System.out.println("【測試 9】重複元素測試 (Duplicate Elements)");
        System.out.println("----------------------------------------");
        ReviewBst dupTree = new ReviewBst();

        System.out.println("第一次新增 5: " + dupTree.add(5));
        System.out.println("第二次新增 5 (重複): " + dupTree.add(5));
        System.out.println("第三次新增 5 (重複): " + dupTree.add(5));
        System.out.println("新增 3: " + dupTree.add(3));
        System.out.println("新增 7: " + dupTree.add(7));
        System.out.println("新增 5 (重複): " + dupTree.add(5));
        System.out.println("最終大小: " + dupTree.size());
        System.out.println("最終中序走訪: " + dupTree.inorder());
        System.out.println("是否有效: " + dupTree.isValid());
        System.out.println();

        // ============================================================
        // 測試 10: 邊界值測試 (Boundary Values)
        // ============================================================
        System.out.println("【測試 10】邊界值測試 (Boundary Values)");
        System.out.println("----------------------------------------");
        ReviewBst boundaryTree = new ReviewBst();

        System.out.println("新增 Integer.MIN_VALUE: " + boundaryTree.add(Integer.MIN_VALUE));
        System.out.println("新增 Integer.MAX_VALUE: " + boundaryTree.add(Integer.MAX_VALUE));
        System.out.println("新增 0: " + boundaryTree.add(0));
        System.out.println("新增 -100: " + boundaryTree.add(-100));
        System.out.println("新增 100: " + boundaryTree.add(100));

        System.out.println("邊界樹大小: " + boundaryTree.size());
        System.out.println("邊界樹高度: " + boundaryTree.height());
        System.out.println("邊界樹中序走訪: " + boundaryTree.inorder());
        System.out.println("邊界樹是否有效: " + boundaryTree.isValid());
        System.out.println("邊界樹包含 MIN: " + boundaryTree.contains(Integer.MIN_VALUE));
        System.out.println("邊界樹包含 MAX: " + boundaryTree.contains(Integer.MAX_VALUE));
        System.out.println("邊界樹包含 0: " + boundaryTree.contains(0));
        System.out.println("邊界樹包含 999: " + boundaryTree.contains(999));

        // 刪除邊界值
        System.out.println("刪除 Integer.MIN_VALUE: " + boundaryTree.remove(Integer.MIN_VALUE));
        System.out.println("刪除後大小: " + boundaryTree.size());
        System.out.println("刪除後中序走訪: " + boundaryTree.inorder());
        System.out.println();

        // ============================================================
        // 測試 11: 大量元素測試 (Stress Test)
        // ============================================================
        System.out.println("【測試 11】大量元素測試 (Stress Test)");
        System.out.println("----------------------------------------");
        ReviewBst stressTree = new ReviewBst();
        int stressSize = 100;

        System.out.println("新增 " + stressSize + " 個元素...");
        for (int i = 0; i < stressSize; i++) {
            stressTree.add(i * 2);  // 偶數: 0, 2, 4, 6, ...
        }

        System.out.println("大量樹大小: " + stressTree.size());
        System.out.println("大量樹高度: " + stressTree.height());
        System.out.println("大量樹是否有效: " + stressTree.isValid());
        System.out.println("大量樹是否包含 50: " + stressTree.contains(50));
        System.out.println("大量樹是否包含 99 (奇數): " + stressTree.contains(99));

        // 刪除一些元素
        System.out.println("刪除 50: " + stressTree.remove(50));
        System.out.println("刪除 100: " + stressTree.remove(100));
        System.out.println("刪除後大小: " + stressTree.size());
        System.out.println("刪除後是否有效: " + stressTree.isValid());
        System.out.println();

        // ============================================================
        // 測試 12: 清空與重建 (Clear and Rebuild)
        // ============================================================
        System.out.println("【測試 12】清空與重建 (Clear and Rebuild)");
        System.out.println("----------------------------------------");
        ReviewBst clearTree = standardTree();
        System.out.println("清空前大小: " + clearTree.size());
        System.out.println("清空前中序走訪: " + clearTree.inorder());

        clearTree.clear();
        System.out.println("清空後大小: " + clearTree.size());
        System.out.println("清空後是否為空: " + clearTree.isEmpty());
        System.out.println("清空後中序走訪: " + clearTree.inorder());

        clearTree.add(100);
        clearTree.add(50);
        clearTree.add(150);
        System.out.println("重建後大小: " + clearTree.size());
        System.out.println("重建後中序走訪: " + clearTree.inorder());
        System.out.println("重建後是否有效: " + clearTree.isValid());
        System.out.println();

        // ============================================================
        // 測試 13: 隨機樹測試 (Random Tree)
        // ============================================================
        System.out.println("【測試 13】隨機樹測試 (Random Tree)");
        System.out.println("----------------------------------------");
        ReviewBst randomTree = randomTree();
        System.out.println("隨機樹大小: " + randomTree.size());
        System.out.println("隨機樹高度: " + randomTree.height());
        System.out.println("隨機樹中序走訪: " + randomTree.inorder());
        System.out.println("隨機樹是否有效: " + randomTree.isValid());

        System.out.println("刪除根節點: " + randomTree.remove(randomTree.getRoot()));
        System.out.println("刪除後大小: " + randomTree.size());
        System.out.println("刪除後是否有效: " + randomTree.isValid());
        System.out.println();

        // ============================================================
        // 測試總結
        // ============================================================
        System.out.println("========================================");
        System.out.println("          測試總結");
        System.out.println("========================================");
        System.out.println("所有邊界情況測試完成！");
        System.out.println("測試涵蓋項目:");
        System.out.println("  ✓ 空樹 (Empty Tree)");
        System.out.println("  ✓ 新增/刪除根節點 (Root Operations)");
        System.out.println("  ✓ 葉節點刪除 (Delete Leaf)");
        System.out.println("  ✓ 只有一個子節點刪除 (Delete with One Child)");
        System.out.println("  ✓ 兩個子節點刪除 (Delete with Two Children)");
        System.out.println("  ✓ 缺少元素刪除 (Delete Missing Element)");
        System.out.println("  ✓ 範圍查詢 (Range Query)");
        System.out.println("  ✓ 重複元素 (Duplicate Elements)");
        System.out.println("  ✓ 邊界值 (Boundary Values)");
        System.out.println("  ✓ 大量元素 (Stress Test)");
        System.out.println("  ✓ 清空與重建 (Clear and Rebuild)");
        System.out.println("  ✓ 隨機樹 (Random Tree)");
        System.out.println("========================================");
    }
}