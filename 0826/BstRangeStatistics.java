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
 * 二元搜尋樹範圍統計
 * 完成 valuesBetween(low, high)、countBetween(low, high) 與 sumBetween(low, high)
 * 使用 BST 方向剪枝 (Pruning)
 */
public class BstRangeStatistics {
    private BSTNode root;

    public BstRangeStatistics() {
        this.root = null;
    }

    /**
     * 新增元素
     */
    public void add(int value) {
        root = addRecursive(root, value);
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
        // 等於則不新增 (不允許重複)
        return node;
    }

    /**
     * 取得範圍內的所有值 (排序後)
     * 使用 BST 方向剪枝
     */
    public List<Integer> valuesBetween(int low, int high) {
        List<Integer> result = new ArrayList<>();
        
        // 處理 low > high 的情況
        if (low > high) {
            System.out.println("警告: low (" + low + ") > high (" + high + ")，範圍無效，回傳空列表");
            return result;
        }

        valuesBetweenRecursive(root, low, high, result);
        return result;
    }

    private void valuesBetweenRecursive(BSTNode node, int low, int high, List<Integer> result) {
        if (node == null) {
            return;
        }

        // 剪枝策略:
        // 1. 如果當前節點值 < low，則左子樹的所有值都 < low，不需要走訪左子樹
        // 2. 如果當前節點值 > high，則右子樹的所有值都 > high，不需要走訪右子樹

        // 檢查左子樹 (如果當前節點值 > low，左子樹可能有值在範圍內)
        if (node.value > low) {
            valuesBetweenRecursive(node.left, low, high, result);
        }

        // 檢查當前節點
        if (node.value >= low && node.value <= high) {
            result.add(node.value);
        }

        // 檢查右子樹 (如果當前節點值 < high，右子樹可能有值在範圍內)
        if (node.value < high) {
            valuesBetweenRecursive(node.right, low, high, result);
        }
    }

    /**
     * 計算範圍內的元素數量
     * 使用 BST 方向剪枝
     */
    public int countBetween(int low, int high) {
        // 處理 low > high 的情況
        if (low > high) {
            System.out.println("警告: low (" + low + ") > high (" + high + ")，範圍無效，回傳 0");
            return 0;
        }

        return countBetweenRecursive(root, low, high);
    }

    private int countBetweenRecursive(BSTNode node, int low, int high) {
        if (node == null) {
            return 0;
        }

        int count = 0;

        // 剪枝策略: 同 valuesBetween

        // 走訪左子樹 (如果可能有值在範圍內)
        if (node.value > low) {
            count += countBetweenRecursive(node.left, low, high);
        }

        // 檢查當前節點
        if (node.value >= low && node.value <= high) {
            count++;
        }

        // 走訪右子樹 (如果可能有值在範圍內)
        if (node.value < high) {
            count += countBetweenRecursive(node.right, low, high);
        }

        return count;
    }

    /**
     * 計算範圍內元素的總和
     * 使用 BST 方向剪枝
     */
    public int sumBetween(int low, int high) {
        // 處理 low > high 的情況
        if (low > high) {
            System.out.println("警告: low (" + low + ") > high (" + high + ")，範圍無效，回傳 0");
            return 0;
        }

        return sumBetweenRecursive(root, low, high);
    }

    private int sumBetweenRecursive(BSTNode node, int low, int high) {
        if (node == null) {
            return 0;
        }

        int sum = 0;

        // 剪枝策略: 同 valuesBetween

        // 走訪左子樹 (如果可能有值在範圍內)
        if (node.value > low) {
            sum += sumBetweenRecursive(node.left, low, high);
        }

        // 檢查當前節點
        if (node.value >= low && node.value <= high) {
            sum += node.value;
        }

        // 走訪右子樹 (如果可能有值在範圍內)
        if (node.value < high) {
            sum += sumBetweenRecursive(node.right, low, high);
        }

        return sum;
    }

    /**
     * 中序走訪 (顯示所有元素)
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
     * 建立測試用的 BST
     */
    public static BstRangeStatistics buildSampleTree() {
        BstRangeStatistics bst = new BstRangeStatistics();
        int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45, 55, 65, 75, 85};
        for (int val : values) {
            bst.add(val);
        }
        return bst;
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("      二元搜尋樹範圍統計系統");
        System.out.println("========================================\n");

        BstRangeStatistics bst = buildSampleTree();

        System.out.println("建立的 BST (中序走訪): " + bst.inorder());
        System.out.println("BST 元素: 10, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85");
        System.out.println();

        // ========== 測試各種範圍 ==========
        System.out.println("【測試一】正常範圍: low = 30, high = 60");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(30, 60) = " + bst.valuesBetween(30, 60));
        System.out.println("countBetween(30, 60)  = " + bst.countBetween(30, 60));
        System.out.println("sumBetween(30, 60)    = " + bst.sumBetween(30, 60));
        System.out.println("  驗算: 30+35+40+45+50+55+60 = " + (30+35+40+45+50+55+60));
        System.out.println();

        System.out.println("【測試二】範圍包含所有元素: low = 10, high = 85");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(10, 85) = " + bst.valuesBetween(10, 85));
        System.out.println("countBetween(10, 85)  = " + bst.countBetween(10, 85));
        System.out.println("sumBetween(10, 85)    = " + bst.sumBetween(10, 85));
        System.out.println("  驗算: 所有元素總和 = " + (10+20+25+30+35+40+45+50+55+60+65+70+75+80+85));
        System.out.println();

        System.out.println("【測試三】範圍只包含一個元素: low = 50, high = 50");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(50, 50) = " + bst.valuesBetween(50, 50));
        System.out.println("countBetween(50, 50)  = " + bst.countBetween(50, 50));
        System.out.println("sumBetween(50, 50)    = " + bst.sumBetween(50, 50));
        System.out.println();

        System.out.println("【測試四】範圍不包含任何元素 (間隙): low = 26, high = 29");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(26, 29) = " + bst.valuesBetween(26, 29));
        System.out.println("countBetween(26, 29)  = " + bst.countBetween(26, 29));
        System.out.println("sumBetween(26, 29)    = " + bst.sumBetween(26, 29));
        System.out.println();

        System.out.println("【測試五】範圍在最小值以下: low = 1, high = 9");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(1, 9)   = " + bst.valuesBetween(1, 9));
        System.out.println("countBetween(1, 9)    = " + bst.countBetween(1, 9));
        System.out.println("sumBetween(1, 9)      = " + bst.sumBetween(1, 9));
        System.out.println();

        System.out.println("【測試六】範圍在最大值以上: low = 86, high = 100");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(86, 100) = " + bst.valuesBetween(86, 100));
        System.out.println("countBetween(86, 100)  = " + bst.countBetween(86, 100));
        System.out.println("sumBetween(86, 100)    = " + bst.sumBetween(86, 100));
        System.out.println();

        System.out.println("【測試七】空範圍 (low > high): low = 60, high = 30");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(60, 30) = " + bst.valuesBetween(60, 30));
        System.out.println("countBetween(60, 30)  = " + bst.countBetween(60, 30));
        System.out.println("sumBetween(60, 30)    = " + bst.sumBetween(60, 30));
        System.out.println();

        System.out.println("【測試八】較小範圍: low = 35, high = 45");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(35, 45) = " + bst.valuesBetween(35, 45));
        System.out.println("countBetween(35, 45)  = " + bst.countBetween(35, 45));
        System.out.println("sumBetween(35, 45)    = " + bst.sumBetween(35, 45));
        System.out.println("  驗算: 35+40+45 = " + (35+40+45));
        System.out.println();

        System.out.println("【測試九】大範圍: low = 15, high = 75");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(15, 75) = " + bst.valuesBetween(15, 75));
        System.out.println("countBetween(15, 75)  = " + bst.countBetween(15, 75));
        System.out.println("sumBetween(15, 75)    = " + bst.sumBetween(15, 75));
        System.out.println("  驗算: 20+25+30+35+40+45+50+55+60+65+70+75 = " + 
                          (20+25+30+35+40+45+50+55+60+65+70+75));
        System.out.println();

        // ========== 測試邊界值 ==========
        System.out.println("【測試十】邊界測試: low = -10, high = 100");
        System.out.println("----------------------------------------");
        System.out.println("valuesBetween(-10, 100) = " + bst.valuesBetween(-10, 100));
        System.out.println("countBetween(-10, 100)  = " + bst.countBetween(-10, 100));
        System.out.println("sumBetween(-10, 100)    = " + bst.sumBetween(-10, 100));
        System.out.println();

        // ========== 空樹測試 ==========
        System.out.println("【測試十一】空樹測試");
        System.out.println("----------------------------------------");
        BstRangeStatistics emptyBst = new BstRangeStatistics();
        System.out.println("空樹中序走訪: " + emptyBst.inorder());
        System.out.println("valuesBetween(10, 50) = " + emptyBst.valuesBetween(10, 50));
        System.out.println("countBetween(10, 50)  = " + emptyBst.countBetween(10, 50));
        System.out.println("sumBetween(10, 50)    = " + emptyBst.sumBetween(10, 50));
        System.out.println();

        System.out.println("========================================");
        System.out.println("        統計完成！");
        System.out.println("========================================");

        // 顯示剪枝效果 (說明)
        System.out.println("\n【剪枝說明】");
        System.out.println("使用 BST 方向剪枝策略:");
        System.out.println("  1. 如果當前節點值 > low，才走訪左子樹 (否則左子樹所有值都 < low)");
        System.out.println("  2. 如果當前節點值 < high，才走訪右子樹 (否則右子樹所有值都 > high)");
        System.out.println("  3. 這樣可以跳過許多不需要檢查的節點，提升效率");
    }
}