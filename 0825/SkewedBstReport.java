/**
 * 檔名：SkewedBstReport.java
 * 功能：傾斜樹 vs 平衡樹 效能比較報告
 * 說明：分別以排序資料與平衡順序建立樹
 *       比較大小、高度與搜尋比較計數
 */

class SkewedNode {
    int value;
    SkewedNode left;
    SkewedNode right;

    public SkewedNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

class SkewedBST {
    private SkewedNode root;
    private int size;
    private int height;
    private int searchComparisons;

    public SkewedBST() {
        this.root = null;
        this.size = 0;
        this.height = 0;
        this.searchComparisons = 0;
    }

    // ========== 插入方法 ==========
    
    public void insert(int value) {
        root = insertRec(root, value);
        size++;
    }

    private SkewedNode insertRec(SkewedNode node, int value) {
        if (node == null) {
            return new SkewedNode(value);
        }
        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        }
        return node;
    }

    /**
     * 建立平衡樹（使用中點分割）
     */
    public void buildBalanced(int[] sortedArray, int start, int end) {
        if (start > end) {
            return;
        }
        int mid = (start + end) / 2;
        insert(sortedArray[mid]);
        buildBalanced(sortedArray, start, mid - 1);
        buildBalanced(sortedArray, mid + 1, end);
    }

    // ========== 搜尋方法 ==========
    
    public int searchWithComparison(int target) {
        searchComparisons = 0;
        SkewedNode current = root;

        while (current != null) {
            searchComparisons++;
            if (target == current.value) {
                return searchComparisons;
            } else if (target < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return -1;
    }

    // ========== 樹的高度計算 ==========
    
    public int getHeight() {
        height = calculateHeight(root);
        return height;
    }

    private int calculateHeight(SkewedNode node) {
        if (node == null) {
            return -1;
        }
        int leftHeight = calculateHeight(node.left);
        int rightHeight = calculateHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    // ========== 獲取基本資訊 ==========
    
    public int getSize() {
        return size;
    }

    public int getSearchComparisons() {
        return searchComparisons;
    }

    // ========== 樹的遍歷 ==========
    
    public void printInOrder() {
        System.out.print("中序遍歷（Inorder）：");
        inOrderRec(root);
        System.out.println();
    }

    private void inOrderRec(SkewedNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.value + " ");
            inOrderRec(node.right);
        }
    }

    public void printPreOrder() {
        System.out.print("前序遍歷（Preorder）：");
        preOrderRec(root);
        System.out.println();
    }

    private void preOrderRec(SkewedNode node) {
        if (node != null) {
            System.out.print(node.value + " ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }

    public void printTreeStructure() {
        System.out.println("樹的結構：");
        printTreeStructureRec(root, 0, "根");
    }

    private void printTreeStructureRec(SkewedNode node, int level, String direction) {
        if (node == null) {
            return;
        }
        String indent = "  ".repeat(level);
        System.out.println(indent + direction + ": " + node.value);
        printTreeStructureRec(node.left, level + 1, "左");
        printTreeStructureRec(node.right, level + 1, "右");
    }
}

public class SkewedBstReport {
    public static void main(String[] args) {
        // 準備測試資料（1 到 15 的排序資料）
        int[] sortedData = new int[15];
        for (int i = 0; i < 15; i++) {
            sortedData[i] = i + 1;
        }

        System.out.println("=========================================");
        System.out.println("       傾斜樹 vs 平衡樹 效能比較報告");
        System.out.println("=========================================");
        System.out.println("資料數量：" + sortedData.length);
        System.out.println("資料範圍：1 ~ " + sortedData.length);
        System.out.println();

        // ================================================
        // 第一部分：建立傾斜樹（插入排序資料）
        // ================================================
        System.out.println("-----------------------------------------");
        System.out.println("【第一部分：傾斜樹（Skewed Tree）】");
        System.out.println("建立方式：依序插入排序資料 1, 2, 3, ...");
        System.out.println("-----------------------------------------");

        SkewedBST skewedTree = new SkewedBST();
        for (int val : sortedData) {
            skewedTree.insert(val);
        }

        skewedTree.printPreOrder();
        skewedTree.printInOrder();
        // skewedTree.printTreeStructure(); // 可選擇是否顯示詳細結構

        int skewedSize = skewedTree.getSize();
        int skewedHeight = skewedTree.getHeight();

        System.out.println();
        System.out.println("📊 傾斜樹統計：");
        System.out.println("   ● 樹的大小（Size）：" + skewedSize + " 個節點");
        System.out.println("   ● 樹的高度（Height）：" + skewedHeight + "（最差情況，退化成鏈結串列）");

        System.out.println();
        System.out.println("【傾斜樹搜尋效能測試】");
        int[] testTargets = {1, 8, 15, 10, 20};
        for (int target : testTargets) {
            int comparisons = skewedTree.searchWithComparison(target);
            if (comparisons != -1) {
                System.out.println("   🔍 搜尋 " + target + " → 比較次數：" + comparisons);
            } else {
                System.out.println("   🔍 搜尋 " + target + " → 找不到（比較次數：" + skewedTree.getSearchComparisons() + "）");
            }
        }

        System.out.println();
        System.out.println("   ⚠️  傾斜樹搜尋平均比較次數 ≈ O(n)");
        System.out.println("   ⚠️  最差情況（搜尋最大值 15）需要 " + skewedTree.searchWithComparison(15) + " 次比較");
        System.out.println();

        // ================================================
        // 第二部分：建立平衡樹（插入平衡順序資料）
        // ================================================
        System.out.println("-----------------------------------------");
        System.out.println("【第二部分：平衡樹（Balanced Tree）】");
        System.out.println("建立方式：以中點分割插入平衡順序");
        System.out.println("平衡順序：以中點為根，遞迴建立左右子樹");
        System.out.println("-----------------------------------------");

        SkewedBST balancedTree = new SkewedBST();
        balancedTree.buildBalanced(sortedData, 0, sortedData.length - 1);

        balancedTree.printPreOrder();
        balancedTree.printInOrder();
        // balancedTree.printTreeStructure(); // 可選擇是否顯示詳細結構

        int balancedSize = balancedTree.getSize();
        int balancedHeight = balancedTree.getHeight();

        System.out.println();
        System.out.println("📊 平衡樹統計：");
        System.out.println("   ● 樹的大小（Size）：" + balancedSize + " 個節點");
        System.out.println("   ● 樹的高度（Height）：" + balancedHeight + "（最佳情況，接近完全二元樹）");

        System.out.println();
        System.out.println("【平衡樹搜尋效能測試】");
        for (int target : testTargets) {
            int comparisons = balancedTree.searchWithComparison(target);
            if (comparisons != -1) {
                System.out.println("   🔍 搜尋 " + target + " → 比較次數：" + comparisons);
            } else {
                System.out.println("   🔍 搜尋 " + target + " → 找不到（比較次數：" + balancedTree.getSearchComparisons() + "）");
            }
        }

        System.out.println();
        System.out.println("   ✅ 平衡樹搜尋平均比較次數 ≈ O(log n)");
        System.out.println("   ✅ 搜尋最大值 15 只需要 " + balancedTree.searchWithComparison(15) + " 次比較");

        // ================================================
        // 第三部分：效能比較總表
        // ================================================
        System.out.println();
        System.out.println("=========================================");
        System.out.println("        📊 效能比較總表");
        System.out.println("=========================================");
        System.out.printf("%-20s %-15s %-15s\n", "比較項目", "傾斜樹", "平衡樹");
        System.out.println("-----------------------------------------");
        System.out.printf("%-20s %-15d %-15d\n", "節點數量", skewedSize, balancedSize);
        System.out.printf("%-20s %-15d %-15d\n", "樹的高度", skewedHeight, balancedHeight);
        System.out.printf("%-20s %-15d %-15d\n", "搜尋 1（最小值）", 
                         skewedTree.searchWithComparison(1), 
                         balancedTree.searchWithComparison(1));
        System.out.printf("%-20s %-15d %-15d\n", "搜尋 8（中間值）", 
                         skewedTree.searchWithComparison(8), 
                         balancedTree.searchWithComparison(8));
        System.out.printf("%-20s %-15d %-15d\n", "搜尋 15（最大值）", 
                         skewedTree.searchWithComparison(15), 
                         balancedTree.searchWithComparison(15));
        System.out.printf("%-20s %-15d %-15d\n", "搜尋 10（不存在）", 
                         skewedTree.searchWithComparison(10), 
                         balancedTree.searchWithComparison(10));
        System.out.println("=========================================");

        // ================================================
        // 第四部分：結論與分析
        // ================================================
        System.out.println();
        System.out.println("=========================================");
        System.out.println("        📝 結論與分析");
        System.out.println("=========================================");
        System.out.println("1. 傾斜樹（Skewed Tree）：");
        System.out.println("   - 由排序資料建立，退化成鏈結串列");
        System.out.println("   - 高度 = n-1 = " + (sortedData.length - 1) + "（最差情況）");
        System.out.println("   - 搜尋時間複雜度：O(n)");
        System.out.println("   - 適合：極少數資料或幾乎不搜尋的情況");

        System.out.println();
        System.out.println("2. 平衡樹（Balanced Tree）：");
        System.out.println("   - 由平衡順序建立，結構接近完全二元樹");
        System.out.println("   - 高度 = ⌈log₂(n)⌉ = " + balancedHeight + "（最佳情況）");
        System.out.println("   - 搜尋時間複雜度：O(log n)");
        System.out.println("   - 適合：大量資料且頻繁搜尋的情況");

        System.out.println();
        System.out.println("3. 效能差異：");
        double heightRatio = (double) skewedHeight / balancedHeight;
        double searchRatio = (double) skewedTree.searchWithComparison(15) / 
                                   balancedTree.searchWithComparison(15);
        System.out.printf("   - 高度差異：傾斜樹是平衡樹的 %.1f 倍\n", heightRatio);
        System.out.printf("   - 搜尋最差值差異：傾斜樹是平衡樹的 %.1f 倍\n", searchRatio);
        System.out.println("   - 資料量越大，差異越明顯！");
        System.out.println("=========================================");
    }
}