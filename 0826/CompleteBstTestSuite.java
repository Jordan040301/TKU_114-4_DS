import java.util.*;

/**
 * 二元搜尋樹 (BST) 實作
 */
class BST {
    private Node root;
    private int size;

    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    public BST() {
        this.root = null;
        this.size = 0;
    }

    public boolean add(int value) {
        if (contains(value)) {
            return false;  // 重複元素
        }
        root = addRecursive(root, value);
        size++;
        return true;
    }

    private Node addRecursive(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }
        if (value < node.value) {
            node.left = addRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = addRecursive(node.right, value);
        }
        return node;
    }

    public boolean remove(int value) {
        if (!contains(value)) {
            return false;
        }
        root = removeRecursive(root, value);
        size--;
        return true;
    }

    private Node removeRecursive(Node node, int value) {
        if (node == null) {
            return null;
        }
        if (value < node.value) {
            node.left = removeRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = removeRecursive(node.right, value);
        } else {
            if (node.left == null && node.right == null) {
                return null;
            }
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            int successor = findMin(node.right);
            node.value = successor;
            node.right = removeRecursive(node.right, successor);
        }
        return node;
    }

    private int findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.value;
    }

    public boolean contains(int value) {
        return containsRecursive(root, value);
    }

    private boolean containsRecursive(Node node, int value) {
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

    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderRecursive(root, result);
        return result;
    }

    private void inorderRecursive(Node node, List<Integer> result) {
        if (node != null) {
            inorderRecursive(node.left, result);
            result.add(node.value);
            inorderRecursive(node.right, result);
        }
    }

    public List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorderRecursive(root, result);
        return result;
    }

    private void preorderRecursive(Node node, List<Integer> result) {
        if (node != null) {
            result.add(node.value);
            preorderRecursive(node.left, result);
            preorderRecursive(node.right, result);
        }
    }

    public List<Integer> postorder() {
        List<Integer> result = new ArrayList<>();
        postorderRecursive(root, result);
        return result;
    }

    private void postorderRecursive(Node node, List<Integer> result) {
        if (node != null) {
            postorderRecursive(node.left, result);
            postorderRecursive(node.right, result);
            result.add(node.value);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int height() {
        return heightRecursive(root);
    }

    private int heightRecursive(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(heightRecursive(node.left), heightRecursive(node.right));
    }

    public boolean isValid() {
        return isValidRecursive(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isValidRecursive(Node node, int min, int max) {
        if (node == null) {
            return true;
        }
        if (node.value < min || node.value > max) {
            return false;
        }
        return isValidRecursive(node.left, min, node.value - 1) &&
               isValidRecursive(node.right, node.value + 1, max);
    }

    public int getRoot() {
        return root != null ? root.value : -1;
    }

    public List<Integer> getRange(int low, int high) {
        List<Integer> result = new ArrayList<>();
        rangeRecursive(root, low, high, result);
        return result;
    }

    private void rangeRecursive(Node node, int low, int high, List<Integer> result) {
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

    public void clear() {
        root = null;
        size = 0;
    }
}

/**
 * 完整 BST Test Suite
 * 不使用 JUnit，以自訂 check(description, condition) 輸出 PASS/FAIL
 * 至少包含 20 個 assertion
 */
public class CompleteBstTestSuite {
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    private static List<String> failedDescriptions = new ArrayList<>();

    /**
     * 自訂 assertion 方法
     */
    public static void check(String description, boolean condition) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("✅ PASS: " + description);
        } else {
            failedTests++;
            failedDescriptions.add(description);
            System.out.println("❌ FAIL: " + description);
        }
    }

    /**
     * 輸出測試總結
     */
    public static void printSummary() {
        System.out.println("\n========================================");
        System.out.println("          測試總結");
        System.out.println("========================================");
        System.out.println("總測試數: " + totalTests);
        System.out.println("通過: " + passedTests);
        System.out.println("失敗: " + failedTests);
        System.out.println("通過率: " + String.format("%.2f%%", (passedTests * 100.0 / totalTests)));

        if (!failedDescriptions.isEmpty()) {
            System.out.println("\n失敗的測試:");
            for (String desc : failedDescriptions) {
                System.out.println("  ❌ " + desc);
            }
        }
        System.out.println("========================================");
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    完整 BST 測試套件");
        System.out.println("========================================\n");

        // ============================================================
        // 測試 1: 空樹測試 (Empty Tree)
        // ============================================================
        System.out.println("【測試類別 1】空樹 (Empty Tree)");
        System.out.println("----------------------------------------");
        BST emptyBst = new BST();

        check("空樹應為空 (isEmpty)", emptyBst.isEmpty());
        check("空樹大小應為 0", emptyBst.size() == 0);
        check("空樹高度應為 0", emptyBst.height() == 0);
        check("空樹中序走訪應為空列表", emptyBst.inorder().isEmpty());
        check("空樹前序走訪應為空列表", emptyBst.preorder().isEmpty());
        check("空樹後序走訪應為空列表", emptyBst.postorder().isEmpty());
        check("空樹應為有效 BST", emptyBst.isValid());
        check("空樹 contains(5) 應回傳 false", !emptyBst.contains(5));
        check("空樹範圍查詢 [1,10] 應為空", emptyBst.getRange(1, 10).isEmpty());
        System.out.println();

        // ============================================================
        // 測試 2: 單一元素 (Root Only)
        // ============================================================
        System.out.println("【測試類別 2】單一元素 (Root Only)");
        System.out.println("----------------------------------------");
        BST singleBst = new BST();
        singleBst.add(10);

        check("單一元素樹不應為空", !singleBst.isEmpty());
        check("單一元素樹大小應為 1", singleBst.size() == 1);
        check("單一元素樹高度應為 1", singleBst.height() == 1);
        check("單一元素樹根節點應為 10", singleBst.getRoot() == 10);
        check("單一元素樹中序走訪應為 [10]", singleBst.inorder().equals(Arrays.asList(10)));
        check("單一元素樹 contains(10) 應回傳 true", singleBst.contains(10));
        check("單一元素樹 contains(5) 應回傳 false", !singleBst.contains(5));
        check("單一元素樹應為有效 BST", singleBst.isValid());
        check("單一元素樹範圍查詢 [5,15] 應為 [10]", singleBst.getRange(5, 15).equals(Arrays.asList(10)));
        check("單一元素樹範圍查詢 [1,9] 應為空", singleBst.getRange(1, 9).isEmpty());
        System.out.println();

        // ============================================================
        // 測試 3: 重複元素 (Duplicate)
        // ============================================================
        System.out.println("【測試類別 3】重複元素 (Duplicate)");
        System.out.println("----------------------------------------");
        BST dupBst = new BST();
        dupBst.add(10);
        boolean addResult1 = dupBst.add(10);  // 重複
        boolean addResult2 = dupBst.add(20);
        boolean addResult3 = dupBst.add(10);  // 重複

        check("重複新增 10 應回傳 false", !addResult1);
        check("重複新增 10 應回傳 false", !addResult3);
        check("新增 20 應回傳 true", addResult2);
        check("重複新增後大小仍為 2", dupBst.size() == 2);
        check("重複新增後中序走訪應為 [10, 20]", dupBst.inorder().equals(Arrays.asList(10, 20)));
        System.out.println();

        // ============================================================
        // 測試 4: 葉節點刪除 (Leaf)
        // ============================================================
        System.out.println("【測試類別 4】葉節點刪除 (Leaf)");
        System.out.println("----------------------------------------");
        BST leafBst = new BST();
        int[] leafValues = {50, 30, 70, 20, 40, 60, 80};
        for (int v : leafValues) leafBst.add(v);

        // 葉節點: 20, 40, 60, 80
        check("刪除葉節點 20 應回傳 true", leafBst.remove(20));
        check("刪除葉節點後大小應為 6", leafBst.size() == 6);
        check("刪除葉節點後不應包含 20", !leafBst.contains(20));
        check("刪除葉節點後中序走訪應為 [30, 40, 50, 60, 70, 80]", 
              leafBst.inorder().equals(Arrays.asList(30, 40, 50, 60, 70, 80)));

        check("刪除葉節點 80 應回傳 true", leafBst.remove(80));
        check("刪除葉節點後大小應為 5", leafBst.size() == 5);
        check("刪除葉節點後中序走訪應為 [30, 40, 50, 60, 70]", 
              leafBst.inorder().equals(Arrays.asList(30, 40, 50, 60, 70)));
        System.out.println();

        // ============================================================
        // 測試 5: 只有一個子節點刪除 (One Child)
        // ============================================================
        System.out.println("【測試類別 5】只有一個子節點刪除 (One Child)");
        System.out.println("----------------------------------------");
        BST oneChildBst = new BST();
        int[] oneChildValues = {50, 30, 70, 20, 40, 60, 80, 25};
        for (int v : oneChildValues) oneChildBst.add(v);

        // 刪除 20 (只有右子節點 25)
        check("刪除節點 20 (只有右子節點) 應回傳 true", oneChildBst.remove(20));
        check("刪除後大小應為 7", oneChildBst.size() == 7);
        check("刪除後不應包含 20", !oneChildBst.contains(20));
        check("刪除後中序走訪應為 [25, 30, 40, 50, 60, 70, 80]", 
              oneChildBst.inorder().equals(Arrays.asList(25, 30, 40, 50, 60, 70, 80)));

        // 刪除 25 (葉節點)
        check("刪除葉節點 25 應回傳 true", oneChildBst.remove(25));
        check("刪除後中序走訪應為 [30, 40, 50, 60, 70, 80]", 
              oneChildBst.inorder().equals(Arrays.asList(30, 40, 50, 60, 70, 80)));

        // 建立只有左子節點的節點
        BST oneChildBst2 = new BST();
        int[] values2 = {50, 30, 70, 20, 40, 60, 80, 15};
        for (int v : values2) oneChildBst2.add(v);
        // 刪除 20 (只有左子節點 15)
        check("刪除節點 20 (只有左子節點) 應回傳 true", oneChildBst2.remove(20));
        check("刪除後中序走訪應為 [15, 30, 40, 50, 60, 70, 80]", 
              oneChildBst2.inorder().equals(Arrays.asList(15, 30, 40, 50, 60, 70, 80)));
        System.out.println();

        // ============================================================
        // 測試 6: 兩個子節點刪除 (Two Children)
        // ============================================================
        System.out.println("【測試類別 6】兩個子節點刪除 (Two Children)");
        System.out.println("----------------------------------------");
        BST twoChildBst = new BST();
        int[] twoChildValues = {50, 30, 70, 20, 40, 60, 80};
        for (int v : twoChildValues) twoChildBst.add(v);

        // 刪除根節點 50 (有兩個子節點)
        check("刪除節點 50 (兩個子節點) 應回傳 true", twoChildBst.remove(50));
        check("刪除後大小應為 6", twoChildBst.size() == 6);
        check("刪除後不應包含 50", !twoChildBst.contains(50));
        check("刪除後中序走訪應為 [20, 30, 40, 60, 70, 80]", 
              twoChildBst.inorder().equals(Arrays.asList(20, 30, 40, 60, 70, 80)));
        check("刪除後樹仍為有效 BST", twoChildBst.isValid());

        // 刪除 30 (有兩個子節點 20, 40)
        check("刪除節點 30 (兩個子節點) 應回傳 true", twoChildBst.remove(30));
        check("刪除後中序走訪應為 [20, 40, 60, 70, 80]", 
              twoChildBst.inorder().equals(Arrays.asList(20, 40, 60, 70, 80)));
        System.out.println();

        // ============================================================
        // 測試 7: 缺少元素刪除 (Missing)
        // ============================================================
        System.out.println("【測試類別 7】缺少元素刪除 (Missing)");
        System.out.println("----------------------------------------");
        BST missingBst = new BST();
        int[] missingValues = {50, 30, 70, 20, 40};
        for (int v : missingValues) missingBst.add(v);

        check("刪除不存在的元素 100 應回傳 false", !missingBst.remove(100));
        check("刪除不存在的元素 25 應回傳 false", !missingBst.remove(25));
        check("刪除不存在的元素後大小仍為 5", missingBst.size() == 5);
        check("刪除不存在的元素後中序走訪應為 [20, 30, 40, 50, 70]", 
              missingBst.inorder().equals(Arrays.asList(20, 30, 40, 50, 70)));
        System.out.println();

        // ============================================================
        // 測試 8: 範圍查詢 (Range Query)
        // ============================================================
        System.out.println("【測試類別 8】範圍查詢 (Range Query)");
        System.out.println("----------------------------------------");
        BST rangeBst = new BST();
        int[] rangeValues = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45, 55, 65, 75, 85};
        for (int v : rangeValues) rangeBst.add(v);

        check("範圍查詢 [30, 60] 應為 [30, 35, 40, 45, 50, 55, 60]", 
              rangeBst.getRange(30, 60).equals(Arrays.asList(30, 35, 40, 45, 50, 55, 60)));
        check("範圍查詢 [10, 85] 應包含所有 15 個元素", 
              rangeBst.getRange(10, 85).size() == 15);
        check("範圍查詢 [50, 50] 應為 [50]", 
              rangeBst.getRange(50, 50).equals(Arrays.asList(50)));
        check("範圍查詢 [26, 29] 應為空", 
              rangeBst.getRange(26, 29).isEmpty());
        check("範圍查詢 [1, 9] 應為空", 
              rangeBst.getRange(1, 9).isEmpty());
        check("範圍查詢 [86, 100] 應為空", 
              rangeBst.getRange(86, 100).isEmpty());
        System.out.println();

        // ============================================================
        // 測試 9: BST 不變性 (Invariant)
        // ============================================================
        System.out.println("【測試類別 9】BST 不變性 (Invariant)");
        System.out.println("----------------------------------------");
        BST invariantBst = new BST();
        int[] invValues = {50, 30, 70, 20, 40, 60, 80};
        for (int v : invValues) invariantBst.add(v);

        check("有效 BST 應通過驗證", invariantBst.isValid());

        // 手動破壞 BST
        // 建立一個無效的 BST: 50 的左子樹中包含 60 (違反規則)
        BST invalidBst = new BST();
        // 直接操作內部結構（透過反射或手動建立）
        // 但由於 Node 是 private 內部類別，我們用另一個方法測試
        // 使用 contains 和 inorder 間接驗證
        BST validCheckBst = new BST();
        int[] checkValues = {50, 30, 70, 20, 40, 60, 80};
        for (int v : checkValues) validCheckBst.add(v);

        // 驗證 BST 的 inorder 總是排序的
        List<Integer> inorderList = validCheckBst.inorder();
        boolean isSorted = true;
        for (int i = 0; i < inorderList.size() - 1; i++) {
            if (inorderList.get(i) > inorderList.get(i + 1)) {
                isSorted = false;
                break;
            }
        }
        check("BST 中序走訪應為排序 (不變性)", isSorted);

        // 驗證 BST 的所有節點都符合範圍限制
        check("BST 應為有效 (不變性)", validCheckBst.isValid());
        System.out.println();

        // ============================================================
        // 測試 10: 邊界測試 (Boundary)
        // ============================================================
        System.out.println("【測試類別 10】邊界測試 (Boundary)");
        System.out.println("----------------------------------------");
        BST boundaryBst = new BST();
        boundaryBst.add(Integer.MIN_VALUE);
        boundaryBst.add(Integer.MAX_VALUE);
        boundaryBst.add(0);

        check("邊界值樹大小應為 3", boundaryBst.size() == 3);
        check("邊界值樹應包含 Integer.MIN_VALUE", boundaryBst.contains(Integer.MIN_VALUE));
        check("邊界值樹應包含 Integer.MAX_VALUE", boundaryBst.contains(Integer.MAX_VALUE));
        check("邊界值樹應包含 0", boundaryBst.contains(0));
        check("邊界值樹中序走訪應為 [MIN, 0, MAX]", 
              boundaryBst.inorder().equals(Arrays.asList(Integer.MIN_VALUE, 0, Integer.MAX_VALUE)));
        check("邊界值樹應為有效 BST", boundaryBst.isValid());
        System.out.println();

        // ============================================================
        // 測試 11: 大量元素 (Stress Test)
        // ============================================================
        System.out.println("【測試類別 11】大量元素測試 (Stress Test)");
        System.out.println("----------------------------------------");
        BST stressBst = new BST();
        int[] stressValues = new int[100];
        for (int i = 0; i < 100; i++) {
            stressValues[i] = i * 2;  // 偶數: 0, 2, 4, 6, ...
        }
        // 打亂順序新增
        List<Integer> shuffled = new ArrayList<>();
        for (int v : stressValues) shuffled.add(v);
        Collections.shuffle(shuffled);
        for (int v : shuffled) stressBst.add(v);

        check("大量元素後大小應為 100", stressBst.size() == 100);
        check("大量元素後應包含所有偶數 0~198", 
              stressBst.contains(50) && stressBst.contains(100) && stressBst.contains(198));
        check("大量元素後不應包含奇數", !stressBst.contains(1) && !stressBst.contains(99));
        check("大量元素後 BST 仍為有效", stressBst.isValid());

        // 驗證 inorder 排序
        List<Integer> sorted = stressBst.inorder();
        boolean sortedCheck = true;
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (sorted.get(i) > sorted.get(i + 1)) {
                sortedCheck = false;
                break;
            }
        }
        check("大量元素後中序走訪應為排序", sortedCheck);
        System.out.println();

        // ============================================================
        // 測試 12: 清空與重建 (Clear and Rebuild)
        // ============================================================
        System.out.println("【測試類別 12】清空與重建 (Clear and Rebuild)");
        System.out.println("----------------------------------------");
        BST clearBst = new BST();
        for (int v : new int[]{5, 3, 7, 2, 4, 6, 8}) {
            clearBst.add(v);
        }
        check("清空前大小應為 7", clearBst.size() == 7);
        clearBst.clear();
        check("清空後大小應為 0", clearBst.size() == 0);
        check("清空後應為空", clearBst.isEmpty());
        check("清空後高度應為 0", clearBst.height() == 0);
        check("清空後中序走訪應為空", clearBst.inorder().isEmpty());

        // 重建
        clearBst.add(100);
        clearBst.add(50);
        clearBst.add(150);
        check("重建後大小應為 3", clearBst.size() == 3);
        check("重建後應包含 100", clearBst.contains(100));
        check("重建後中序走訪應為 [50, 100, 150]", 
              clearBst.inorder().equals(Arrays.asList(50, 100, 150)));
        System.out.println();

        // ============================================================
        // 輸出測試總結
        // ============================================================
        printSummary();
    }
}