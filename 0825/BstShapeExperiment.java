/**
 * 檔名：bstshapeExperiment.java
 * 功能：BST 樹狀實驗 - 比較不同插入順序對樹形狀與效能的影響
 * 說明：使用相同的15個值，以不同順序插入
 *       比較高度與全部搜尋比較計數
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * BST 節點
 */
class ExpNode {
    int value;
    ExpNode left;
    ExpNode right;

    public ExpNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

/**
 * 實驗用 BST（包含統計功能）
 */
class ExperimentBST {
    private ExpNode root;
    private int size;
    private int insertCount;
    private int searchComparisons;

    public ExperimentBST() {
        this.root = null;
        this.size = 0;
        this.insertCount = 0;
        this.searchComparisons = 0;
    }

    // ========== 插入方法 ==========

    public void insert(int value) {
        root = insertRec(root, value);
        size++;
        insertCount++;
    }

    private ExpNode insertRec(ExpNode node, int value) {
        if (node == null) {
            return new ExpNode(value);
        }
        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        }
        return node;
    }

    /**
     * 批量插入
     */
    public void insertAll(int[] values) {
        for (int val : values) {
            insert(val);
        }
    }

    /**
     * 批量插入（List）
     */
    public void insertAll(List<Integer> values) {
        for (int val : values) {
            insert(val);
        }
    }

    // ========== 搜尋方法 ==========

    /**
     * 搜尋並記錄比較次數
     */
    public boolean search(int value) {
        ExpNode current = root;
        while (current != null) {
            searchComparisons++;
            if (value == current.value) {
                return true;
            } else if (value < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    /**
     * 搜尋所有值並計算總比較次數
     */
    public int searchAll(int[] values) {
        searchComparisons = 0;
        for (int val : values) {
            search(val);
        }
        return searchComparisons;
    }

    /**
     * 搜尋所有值並計算總比較次數（List）
     */
    public int searchAll(List<Integer> values) {
        searchComparisons = 0;
        for (int val : values) {
            search(val);
        }
        return searchComparisons;
    }

    // ========== 高度計算 ==========

    /**
     * 計算樹的高度（從根到最深葉子的邊數）
     */
    public int getHeight() {
        return calculateHeight(root);
    }

    private int calculateHeight(ExpNode node) {
        if (node == null) {
            return -1;  // 空樹高度為 -1
        }
        int leftHeight = calculateHeight(node.left);
        int rightHeight = calculateHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    // ========== 基本資訊 ==========

    public int getSize() {
        return size;
    }

    public int getInsertCount() {
        return insertCount;
    }

    public int getSearchComparisons() {
        return searchComparisons;
    }

    public ExpNode getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return root == null;
    }

    // ========== 樹的遍歷 ==========

    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }

    private void inorderRec(ExpNode node, List<Integer> result) {
        if (node != null) {
            inorderRec(node.left, result);
            result.add(node.value);
            inorderRec(node.right, result);
        }
    }

    public List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorderRec(root, result);
        return result;
    }

    private void preorderRec(ExpNode node, List<Integer> result) {
        if (node != null) {
            result.add(node.value);
            preorderRec(node.left, result);
            preorderRec(node.right, result);
        }
    }

    // ========== 樹形狀分析 ==========

    /**
     * 計算平均搜尋比較次數
     */
    public double getAverageSearchComparisons() {
        if (size == 0) return 0;
        // 搜尋所有節點
        List<Integer> allValues = inorder();
        searchComparisons = 0;
        for (int val : allValues) {
            search(val);
        }
        return searchComparisons / (double) size;
    }

    /**
     * 計算完美平衡樹的理論高度
     */
    public static int theoreticalHeight(int n) {
        return (int) Math.ceil(Math.log(n + 1) / Math.log(2)) - 1;
    }

    // ========== 列印方法 ==========

    public void printTree(String title) {
        System.out.println("=========================================");
        System.out.println(title);
        System.out.println("-----------------------------------------");
        System.out.print("中序遍歷（排序）：");
        List<Integer> inorder = inorder();
        for (int val : inorder) {
            System.out.print(val + " ");
        }
        System.out.println();
        System.out.print("前序遍歷（結構）：");
        List<Integer> preorder = preorder();
        for (int val : preorder) {
            System.out.print(val + " ");
        }
        System.out.println();
        System.out.println("節點數量：" + size);
        System.out.println("樹的高度：" + getHeight());
        System.out.println("-----------------------------------------");
        printTreeStructure(root, 0);
        System.out.println("=========================================");
        System.out.println();
    }

    private void printTreeStructure(ExpNode node, int level) {
        if (node == null) {
            return;
        }
        String indent = "  ".repeat(level);
        System.out.println(indent + "└── " + node.value);
        if (node.left != null || node.right != null) {
            printTreeStructure(node.left, level + 1);
            printTreeStructure(node.right, level + 1);
        }
    }

    /**
     * 獲取樹的形狀描述
     */
    public String getShapeDescription() {
        int height = getHeight();
        int theoretical = theoreticalHeight(size);
        double ratio = (double) height / theoretical;
        
        if (height <= theoretical + 1) {
            return "平衡（Balanced）";
        } else if (height >= size - 1) {
            return "傾斜（Skewed）";
        } else if (ratio < 2.0) {
            return "接近平衡（Near Balanced）";
        } else {
            return "略為傾斜（Slightly Skewed）";
        }
    }
}

/**
 * 主程式
 */
public class BstShapeExperiment {
    private static final String SEPARATOR = "=========================================";

    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("        BST 樹狀實驗");
        System.out.println(SEPARATOR);
        System.out.println("實驗目標：比較不同插入順序對 BST 形狀與效能的影響");
        System.out.println("實驗資料：1 ~ 15 共 15 個數值");
        System.out.println("比較指標：樹的高度、全部搜尋比較計數總和");
        System.out.println(SEPARATOR);
        System.out.println();

        // =========================================================
        // 準備測試資料
        // =========================================================
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        List<Integer> valueList = new ArrayList<>();
        for (int v : values) {
            valueList.add(v);
        }

        System.out.println("📊 實驗資料：" + valueList);
        System.out.println("資料筆數：" + values.length);
        System.out.println("理論最小高度（完美平衡）：" + ExperimentBST.theoreticalHeight(values.length));
        System.out.println();
        System.out.println("【實驗說明】");
        System.out.println("  1. 使用相同的 15 個值，以不同順序插入 BST");
        System.out.println("  2. 計算每棵樹的高度（Height）");
        System.out.println("  3. 搜尋所有 15 個值，計算總比較次數");
        System.out.println("  4. 比較不同插入順序的效能差異");
        System.out.println(SEPARATOR);
        System.out.println();

        // =========================================================
        // 實驗一：排序插入（最差情況 - 傾斜樹）
        // =========================================================
        System.out.println("【實驗一：排序插入（由小到大）】");
        System.out.println("-----------------------------------------");
        System.out.println("插入順序：[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]");
        System.out.println("預期結果：退化成傾斜樹（只有右子樹）");
        System.out.println();

        ExperimentBST bst1 = new ExperimentBST();
        for (int val : values) {
            bst1.insert(val);
        }
        bst1.printTree("排序插入結果");

        int searchTotal1 = bst1.searchAll(values);
        double avg1 = bst1.getAverageSearchComparisons();

        System.out.println("📊 實驗一結果：");
        System.out.println("   ● 樹的高度：" + bst1.getHeight());
        System.out.println("   ● 全部搜尋比較總數：" + searchTotal1);
        System.out.println("   ● 平均搜尋比較次數：" + String.format("%.2f", avg1));
        System.out.println("   ● 樹的形狀：" + bst1.getShapeDescription());
        System.out.println();

        // =========================================================
        // 實驗二：平衡插入（最佳情況）
        // =========================================================
        System.out.println("【實驗二：平衡插入（中點分割）】");
        System.out.println("-----------------------------------------");
        System.out.println("插入順序：[8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15]");
        System.out.println("預期結果：接近完美平衡的 BST");
        System.out.println();

        int[] balancedOrder = {8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15};
        ExperimentBST bst2 = new ExperimentBST();
        for (int val : balancedOrder) {
            bst2.insert(val);
        }
        bst2.printTree("平衡插入結果");

        int searchTotal2 = bst2.searchAll(values);
        double avg2 = bst2.getAverageSearchComparisons();

        System.out.println("📊 實驗二結果：");
        System.out.println("   ● 樹的高度：" + bst2.getHeight());
        System.out.println("   ● 全部搜尋比較總數：" + searchTotal2);
        System.out.println("   ● 平均搜尋比較次數：" + String.format("%.2f", avg2));
        System.out.println("   ● 樹的形狀：" + bst2.getShapeDescription());
        System.out.println();

        // =========================================================
        // 實驗三：隨機插入（一般情況）
        // =========================================================
        System.out.println("【實驗三：隨機插入】");
        System.out.println("-----------------------------------------");
        
        // 產生 3 種不同的隨機順序
        Random rand = new Random(42);  // 固定 seed 以便重現
        
        for (int exp = 1; exp <= 3; exp++) {
            List<Integer> shuffled = new ArrayList<>(valueList);
            Collections.shuffle(shuffled, rand);
            
            System.out.println("  隨機順序 " + exp + "：" + shuffled);
            
            ExperimentBST bst = new ExperimentBST();
            bst.insertAll(shuffled);
            
            int searchTotal = bst.searchAll(values);
            double avg = bst.getAverageSearchComparisons();
            
            System.out.println("    📊 結果：");
            System.out.println("       ● 樹的高度：" + bst.getHeight());
            System.out.println("       ● 全部搜尋比較總數：" + searchTotal);
            System.out.println("       ● 平均搜尋比較次數：" + String.format("%.2f", avg));
            System.out.println("       ● 樹的形狀：" + bst.getShapeDescription());
            System.out.println();
        }

        // =========================================================
        // 實驗四：倒序插入（最差情況 - 另一種傾斜）
        // =========================================================
        System.out.println("【實驗四：倒序插入（由大到小）】");
        System.out.println("-----------------------------------------");
        System.out.println("插入順序：[15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]");
        System.out.println("預期結果：退化成傾斜樹（只有左子樹）");
        System.out.println();

        ExperimentBST bst4 = new ExperimentBST();
        for (int i = 15; i >= 1; i--) {
            bst4.insert(i);
        }
        bst4.printTree("倒序插入結果");

        int searchTotal4 = bst4.searchAll(values);
        double avg4 = bst4.getAverageSearchComparisons();

        System.out.println("📊 實驗四結果：");
        System.out.println("   ● 樹的高度：" + bst4.getHeight());
        System.out.println("   ● 全部搜尋比較總數：" + searchTotal4);
        System.out.println("   ● 平均搜尋比較次數：" + String.format("%.2f", avg4));
        System.out.println("   ● 樹的形狀：" + bst4.getShapeDescription());
        System.out.println();

        // =========================================================
        // 總結比較表格
        // =========================================================
        System.out.println(SEPARATOR);
        System.out.println("        📊 實驗結果總結比較");
        System.out.println(SEPARATOR);

        // 重新執行所有實驗以收集完整資料
        ExperimentBST summaryBst1 = new ExperimentBST();
        for (int val : values) summaryBst1.insert(val);
        
        ExperimentBST summaryBst2 = new ExperimentBST();
        for (int val : balancedOrder) summaryBst2.insert(val);
        
        ExperimentBST summaryBst4 = new ExperimentBST();
        for (int i = 15; i >= 1; i--) summaryBst4.insert(i);

        // 隨機取一組代表
        List<Integer> randomSample = new ArrayList<>(valueList);
        Collections.shuffle(randomSample, new Random(123));
        ExperimentBST summaryBstRand = new ExperimentBST();
        summaryBstRand.insertAll(randomSample);

        System.out.printf("%-12s | %-10s | %-12s | %-14s | %-12s\n", 
                         "插入方式", "節點數", "樹高度", "搜尋比較總數", "樹形狀");
        System.out.println("-----------------------------------------");
        
        printSummaryRow("排序插入", summaryBst1);
        printSummaryRow("平衡插入", summaryBst2);
        printSummaryRow("隨機插入", summaryBstRand);
        printSummaryRow("倒序插入", summaryBst4);
        
        System.out.println(SEPARATOR);
        System.out.println();

        // =========================================================
        // 結論與分析
        // =========================================================
        System.out.println(SEPARATOR);
        System.out.println("        📝 結論與分析");
        System.out.println(SEPARATOR);
        System.out.println();
        System.out.println("1. 排序插入（由小到大）：");
        System.out.println("   - 高度 = " + summaryBst1.getHeight() + "（最大，退化成鏈結串列）");
        System.out.println("   - 搜尋比較總數 = " + summaryBst1.searchAll(values));
        System.out.println("   - 時間複雜度：O(n)");
        System.out.println("   - 不適合用於已排序資料！");
        System.out.println();
        
        System.out.println("2. 平衡插入（中點分割）：");
        System.out.println("   - 高度 = " + summaryBst2.getHeight() + "（最小，接近完美平衡）");
        System.out.println("   - 搜尋比較總數 = " + summaryBst2.searchAll(values));
        System.out.println("   - 時間複雜度：O(log n)");
        System.out.println("   - 最適合用於頻繁搜尋的場景！");
        System.out.println();
        
        System.out.println("3. 隨機插入：");
        System.out.println("   - 高度 = " + summaryBstRand.getHeight() + "（介於平衡與傾斜之間）");
        System.out.println("   - 搜尋比較總數 = " + summaryBstRand.searchAll(values));
        System.out.println("   - 時間複雜度：介於 O(log n) 和 O(n) 之間");
        System.out.println("   - 實際應用中最常見的情況");
        System.out.println();
        
        System.out.println("4. 倒序插入（由大到小）：");
        System.out.println("   - 高度 = " + summaryBst4.getHeight() + "（最大，退化成鏈結串列）");
        System.out.println("   - 搜尋比較總數 = " + summaryBst4.searchAll(values));
        System.out.println("   - 時間複雜度：O(n)");
        System.out.println("   - 與排序插入同樣糟糕！");
        System.out.println();

        System.out.println(SEPARATOR);
        System.out.println("📌 關鍵發現：");
        System.out.println("   • BST 的形狀完全取決於插入順序");
        System.out.println("   • 已排序資料會造成最差情況（傾斜樹）");
        System.out.println("   • 平衡插入能確保最佳效能");
        System.out.println("   • 這就是為什麼需要平衡樹（AVL、Red-Black Tree）！");
        System.out.println(SEPARATOR);
    }

    private static void printSummaryRow(String method, ExperimentBST bst) {
        int size = bst.getSize();
        int height = bst.getHeight();
        // 需要重新搜尋以獲得比較次數
        int searchTotal = bst.searchAll(new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
        String shape = bst.getShapeDescription();
        System.out.printf("%-12s | %-10d | %-12d | %-14d | %-12s\n", 
                         method, size, height, searchTotal, shape);
    }
}