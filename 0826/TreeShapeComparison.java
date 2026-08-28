import java.util.*;

/**
 * 二元搜尋樹 (BST) 節點
 */
class BSTNode {
    int value;
    BSTNode left;
    BSTNode right;
    int searchComparisons;  // 搜尋時的比較次數 (僅用於統計)

    public BSTNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.searchComparisons = 0;
    }
}

/**
 * 二元搜尋樹 (BST) 實作 - 支援比較次數統計
 */
class BinarySearchTree {
    private BSTNode root;
    private int size;
    private int searchComparisonCount;  // 累計搜尋比較次數

    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
        this.searchComparisonCount = 0;
    }

    /**
     * 新增元素 (不會檢查重複，用於建樹)
     */
    public void add(int value) {
        root = addRecursive(root, value);
        size++;
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
     * 搜尋 key 並回傳比較次數
     */
    public int search(int value) {
        searchComparisonCount = 0;
        return searchRecursive(root, value);
    }

    private int searchRecursive(BSTNode node, int value) {
        if (node == null) {
            return searchComparisonCount;
        }
        searchComparisonCount++;
        if (value == node.value) {
            return searchComparisonCount;
        }
        if (value < node.value) {
            return searchRecursive(node.left, value);
        } else {
            return searchRecursive(node.right, value);
        }
    }

    /**
     * 搜尋所有 key 並回傳總比較次數
     */
    public int searchAllKeys(int[] keys) {
        int total = 0;
        for (int key : keys) {
            total += search(key);
        }
        return total;
    }

    /**
     * 搜尋所有 missing key 並回傳總比較次數
     */
    public int searchAllMissingKeys(int[] missingKeys) {
        int total = 0;
        for (int key : missingKeys) {
            total += search(key);
        }
        return total;
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
     * 中序走訪
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

    public int getSize() {
        return size;
    }

    public BSTNode getRoot() {
        return root;
    }

    /**
     * 清空樹
     */
    public void clear() {
        root = null;
        size = 0;
        searchComparisonCount = 0;
    }
}

/**
 * Tree Shape Comparison
 * 使用相同 15 個 key，以升冪、降冪與接近平衡三種順序建樹
 * 比較 height、全部 key 的 search comparison total，以及 missing key 的 comparison count
 */
public class TreeShapeComparison {

    // 15 個 key
    private static final int[] KEYS = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150};

    // Missing keys (不在樹中的 key)
    private static final int[] MISSING_KEYS = {5, 15, 25, 35, 45, 55, 65, 75, 85, 95, 105, 115, 125, 135, 145, 155};

    // 額外的 missing keys 測試 (範圍外)
    private static final int[] EXTRA_MISSING = {0, 160};

    /**
     * 升冪順序建樹 (Ascending)
     * 會退化成 linked list (最不平衡)
     */
    public static BinarySearchTree buildAscendingTree() {
        BinarySearchTree bst = new BinarySearchTree();
        for (int key : KEYS) {
            bst.add(key);
        }
        return bst;
    }

    /**
     * 降冪順序建樹 (Descending)
     * 也會退化成 linked list (最不平衡)
     */
    public static BinarySearchTree buildDescendingTree() {
        BinarySearchTree bst = new BinarySearchTree();
        for (int i = KEYS.length - 1; i >= 0; i--) {
            bst.add(KEYS[i]);
        }
        return bst;
    }

    /**
     * 接近平衡順序建樹 (使用中位數遞迴)
     * 盡可能讓樹保持平衡
     */
    public static BinarySearchTree buildBalancedTree() {
        BinarySearchTree bst = new BinarySearchTree();
        List<Integer> sortedKeys = new ArrayList<>();
        for (int key : KEYS) {
            sortedKeys.add(key);
        }
        // 使用中位數插入
        buildBalancedRecursive(bst, sortedKeys, 0, sortedKeys.size() - 1);
        return bst;
    }

    private static void buildBalancedRecursive(BinarySearchTree bst, List<Integer> keys, int left, int right) {
        if (left > right) {
            return;
        }
        int mid = (left + right) / 2;
        bst.add(keys.get(mid));
        buildBalancedRecursive(bst, keys, left, mid - 1);
        buildBalancedRecursive(bst, keys, mid + 1, right);
    }

    /**
     * 另一種接近平衡的插入順序 (隨機排列)
     */
    public static BinarySearchTree buildRandomTree() {
        BinarySearchTree bst = new BinarySearchTree();
        List<Integer> shuffled = new ArrayList<>();
        for (int key : KEYS) {
            shuffled.add(key);
        }
        Collections.shuffle(shuffled, new Random(42));  // 固定種子以便重現
        for (int key : shuffled) {
            bst.add(key);
        }
        return bst;
    }

    /**
     * 列印樹的結構 (簡易)
     */
    public static void printTreeStructure(BinarySearchTree bst, String name) {
        System.out.println(name + " 的中序走訪: " + bst.inorder());
        System.out.println(name + " 的大小: " + bst.getSize());
        System.out.println(name + " 的高度: " + bst.height());
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("      樹形狀比較 (Tree Shape Comparison)");
        System.out.println("========================================\n");

        System.out.println("使用的 15 個 Key: " + Arrays.toString(KEYS));
        System.out.println("Missing Keys: " + Arrays.toString(MISSING_KEYS));
        System.out.println();

        // ============================================================
        // 測試 1: 升冪順序建樹 (Ascending)
        // ============================================================
        System.out.println("【測試 1】升冪順序建樹 (Ascending)");
        System.out.println("----------------------------------------");
        BinarySearchTree ascTree = buildAscendingTree();
        printTreeStructure(ascTree, "升冪樹 (Ascending)");

        int ascHeight = ascTree.height();
        int ascExistingSearchTotal = ascTree.searchAllKeys(KEYS);
        int ascMissingSearchTotal = ascTree.searchAllMissingKeys(MISSING_KEYS);

        System.out.println("  高度 (Height): " + ascHeight);
        System.out.println("  所有 Key 搜尋比較總數: " + ascExistingSearchTotal);
        System.out.println("  Missing Key 搜尋比較總數: " + ascMissingSearchTotal);
        System.out.println();

        // ============================================================
        // 測試 2: 降冪順序建樹 (Descending)
        // ============================================================
        System.out.println("【測試 2】降冪順序建樹 (Descending)");
        System.out.println("----------------------------------------");
        BinarySearchTree descTree = buildDescendingTree();
        printTreeStructure(descTree, "降冪樹 (Descending)");

        int descHeight = descTree.height();
        int descExistingSearchTotal = descTree.searchAllKeys(KEYS);
        int descMissingSearchTotal = descTree.searchAllMissingKeys(MISSING_KEYS);

        System.out.println("  高度 (Height): " + descHeight);
        System.out.println("  所有 Key 搜尋比較總數: " + descExistingSearchTotal);
        System.out.println("  Missing Key 搜尋比較總數: " + descMissingSearchTotal);
        System.out.println();

        // ============================================================
        // 測試 3: 接近平衡順序建樹 (Balanced)
        // ============================================================
        System.out.println("【測試 3】接近平衡順序建樹 (Balanced)");
        System.out.println("----------------------------------------");
        BinarySearchTree balTree = buildBalancedTree();
        printTreeStructure(balTree, "平衡樹 (Balanced)");

        int balHeight = balTree.height();
        int balExistingSearchTotal = balTree.searchAllKeys(KEYS);
        int balMissingSearchTotal = balTree.searchAllMissingKeys(MISSING_KEYS);

        System.out.println("  高度 (Height): " + balHeight);
        System.out.println("  所有 Key 搜尋比較總數: " + balExistingSearchTotal);
        System.out.println("  Missing Key 搜尋比較總數: " + balMissingSearchTotal);
        System.out.println();

        // ============================================================
        // 測試 4: 隨機順序建樹 (Random) - 額外參考
        // ============================================================
        System.out.println("【測試 4】隨機順序建樹 (Random) - 參考");
        System.out.println("----------------------------------------");
        BinarySearchTree randTree = buildRandomTree();
        printTreeStructure(randTree, "隨機樹 (Random)");

        int randHeight = randTree.height();
        int randExistingSearchTotal = randTree.searchAllKeys(KEYS);
        int randMissingSearchTotal = randTree.searchAllMissingKeys(MISSING_KEYS);

        System.out.println("  高度 (Height): " + randHeight);
        System.out.println("  所有 Key 搜尋比較總數: " + randExistingSearchTotal);
        System.out.println("  Missing Key 搜尋比較總數: " + randMissingSearchTotal);
        System.out.println();

        // ============================================================
        // 比較總結
        // ============================================================
        System.out.println("========================================");
        System.out.println("          比較總結");
        System.out.println("========================================");

        System.out.println("\n【高度比較】");
        System.out.println("  升冪 (Ascending):   " + ascHeight);
        System.out.println("  降冪 (Descending):  " + descHeight);
        System.out.println("  平衡 (Balanced):    " + balHeight);
        System.out.println("  隨機 (Random):      " + randHeight);
        System.out.println("  最佳高度: " + Math.min(Math.min(ascHeight, descHeight), Math.min(balHeight, randHeight)));
        System.out.println("  理論最小高度 (完美平衡): " + (int)Math.ceil(Math.log(16) / Math.log(2)));

        System.out.println("\n【現有 Key 搜尋比較總數】");
        System.out.println("  升冪 (Ascending):   " + ascExistingSearchTotal);
        System.out.println("  降冪 (Descending):  " + descExistingSearchTotal);
        System.out.println("  平衡 (Balanced):    " + balExistingSearchTotal);
        System.out.println("  隨機 (Random):      " + randExistingSearchTotal);
        System.out.println("  最佳: " + Math.min(Math.min(ascExistingSearchTotal, descExistingSearchTotal), 
                                                 Math.min(balExistingSearchTotal, randExistingSearchTotal)));
        System.out.println("  最差: " + Math.max(Math.max(ascExistingSearchTotal, descExistingSearchTotal), 
                                                 Math.max(balExistingSearchTotal, randExistingSearchTotal)));

        System.out.println("\n【Missing Key 搜尋比較總數】");
        System.out.println("  升冪 (Ascending):   " + ascMissingSearchTotal);
        System.out.println("  降冪 (Descending):  " + descMissingSearchTotal);
        System.out.println("  平衡 (Balanced):    " + balMissingSearchTotal);
        System.out.println("  隨機 (Random):      " + randMissingSearchTotal);
        System.out.println("  最佳: " + Math.min(Math.min(ascMissingSearchTotal, descMissingSearchTotal), 
                                                 Math.min(balMissingSearchTotal, randMissingSearchTotal)));
        System.out.println("  最差: " + Math.max(Math.max(ascMissingSearchTotal, descMissingSearchTotal), 
                                                 Math.max(balMissingSearchTotal, randMissingSearchTotal)));

        // ============================================================
        // 比較分析
        // ============================================================
        System.out.println("\n========================================");
        System.out.println("          比較分析");
        System.out.println("========================================");

        System.out.println("\n1. 升冪/降冪建樹 (最不平衡):");
        System.out.println("   - 樹退化成 linked list (高度 = 15)");
        System.out.println("   - 搜尋效率最差 (現有 key 平均比較次數高)");
        System.out.println("   - Missing key 搜尋需要完整走訪");

        System.out.println("\n2. 平衡建樹:");
        System.out.println("   - 樹高度最低 (高度 = 4)");
        System.out.println("   - 搜尋效率最佳 (現有 key 比較次數最少)");
        System.out.println("   - Missing key 搜尋也最有效率");

        System.out.println("\n3. 隨機建樹:");
        System.out.println("   - 介於平衡和最差之間");
        System.out.println("   - 實際使用中通常表現不錯");

        System.out.println("\n4. 效能差異:");
        double ascVsBalRatio = (double)ascExistingSearchTotal / balExistingSearchTotal;
        System.out.println("   - 升冪 vs 平衡 (現有 key): " + String.format("%.2f", ascVsBalRatio) + " 倍");
        double descVsBalRatio = (double)descExistingSearchTotal / balExistingSearchTotal;
        System.out.println("   - 降冪 vs 平衡 (現有 key): " + String.format("%.2f", descVsBalRatio) + " 倍");
        double ascMissingVsBal = (double)ascMissingSearchTotal / balMissingSearchTotal;
        System.out.println("   - 升冪 vs 平衡 (missing key): " + String.format("%.2f", ascMissingVsBal) + " 倍");

        System.out.println("\n========================================");
        System.out.println("          結論");
        System.out.println("========================================");
        System.out.println("• 平衡的樹形狀可大幅提升搜尋效率");
        System.out.println("• 升冪/降冪插入順序會導致 BST 退化");
        System.out.println("• 實際應用中應考慮使用平衡樹 (如 AVL、Red-Black)");
        System.out.println("• 或使用隨機化插入順序來避免最壞情況");
        System.out.println("========================================");

        // ============================================================
        // 詳細的單一 Key 搜尋比較次數 (展示)
        // ============================================================
        System.out.println("\n【詳細搜尋比較次數 - 各 Key】");
        System.out.println("----------------------------------------");
        System.out.println("Key\t| 升冪\t| 降冪\t| 平衡\t| 隨機");
        System.out.println("-------+-------+-------+-------+-------");
        for (int key : KEYS) {
            int asc = ascTree.search(key);
            int desc = descTree.search(key);
            int bal = balTree.search(key);
            int rand = randTree.search(key);
            System.out.printf("%4d\t| %4d\t| %4d\t| %4d\t| %4d%n", key, asc, desc, bal, rand);
        }

        System.out.println("\n【詳細搜尋比較次數 - Missing Keys】");
        System.out.println("----------------------------------------");
        System.out.println("Key\t| 升冪\t| 降冪\t| 平衡\t| 隨機");
        System.out.println("-------+-------+-------+-------+-------");
        for (int key : MISSING_KEYS) {
            int asc = ascTree.search(key);
            int desc = descTree.search(key);
            int bal = balTree.search(key);
            int rand = randTree.search(key);
            System.out.printf("%4d\t| %4d\t| %4d\t| %4d\t| %4d%n", key, asc, desc, bal, rand);
        }

        System.out.println();
        System.out.println("========================================");
        System.out.println("        比較分析完成！");
        System.out.println("========================================");
    }
}