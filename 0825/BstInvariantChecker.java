/**
 * 檔名：bstInvariantChecker.java
 * 功能：BST驗證（BST Invariant Checker）
 * 說明：使用最小/最大邊界驗證檢查二元樹是否符合BST性質
 *       建立有效樹與多棵違規樹進行測試
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

class BSTValidator {
    
    /**
     * 驗證二元樹是否符合BST性質（公開方法）
     * @param root 根節點
     * @return true 表示符合BST性質，false 表示不符合
     */
    public boolean isValidBST(BSTNode root) {
        return isValidBSTRec(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * 使用最小/最大邊界驗證BST性質（核心方法）
     * @param node 當前節點
     * @param min 允許的最小值（不包含）
     * @param max 允許的最大值（不包含）
     * @return true 表示符合BST性質
     */
    private boolean isValidBSTRec(BSTNode node, long min, long max) {
        // 空節點視為合法
        if (node == null) {
            return true;
        }

        // 檢查當前節點值是否在 (min, max) 範圍內
        if (node.value <= min || node.value >= max) {
            return false;
        }

        // 遞迴檢查左右子樹
        // 左子樹：值必須小於當前節點值（max = node.value）
        // 右子樹：值必須大於當前節點值（min = node.value）
        return isValidBSTRec(node.left, min, node.value) &&
               isValidBSTRec(node.right, node.value, max);
    }

    /**
     * 驗證並輸出詳細報告
     * @param root 根節點
     * @param treeName 樹的名稱
     */
    public void validateAndReport(BSTNode root, String treeName) {
        System.out.println("========================================");
        System.out.println("🌳 驗證樹：" + treeName);
        System.out.println("----------------------------------------");
        
        // 印出樹的結構
        printTreeStructure(root, 0);
        System.out.println();
        
        // 執行驗證
        boolean isValid = isValidBST(root);
        
        if (isValid) {
            System.out.println("✅ 驗證結果：這是一棵有效的 BST！");
        } else {
            System.out.println("❌ 驗證結果：這不是一棵有效的 BST！");
            System.out.println("   （違反了 BST 的 invariant 性質）");
        }
        System.out.println("========================================");
        System.out.println();
    }

    /**
     * 印出樹的結構（輔助方法）
     */
    private void printTreeStructure(BSTNode node, int level) {
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
     * 中序遍歷（輔助觀察）
     */
    public void printInOrder(BSTNode root, String treeName) {
        System.out.print(treeName + " 中序遍歷：");
        inOrderRec(root);
        System.out.println();
    }

    private void inOrderRec(BSTNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.value + " ");
            inOrderRec(node.right);
        }
    }
}

public class BstInvariantChecker {
    public static void main(String[] args) {
        BSTValidator validator = new BSTValidator();

        System.out.println("=========================================");
        System.out.println("        BST 性質驗證測試");
        System.out.println("=========================================");
        System.out.println("驗證方法：Min/Max Boundary Approach");
        System.out.println("BST 性質：左子樹 < 根 < 右子樹");
        System.out.println();

        // =========================================================
        // 測試一：有效 BST
        // =========================================================
        System.out.println("【測試一：有效 BST】");
        System.out.println("樹結構：");
        System.out.println("        8");
        System.out.println("       / \\");
        System.out.println("      3   10");
        System.out.println("     / \\    \\");
        System.out.println("    1   6    14");
        System.out.println("       / \\   /");
        System.out.println("      4   7 13");
        System.out.println();

        BSTNode validTree = new BSTNode(8);
        validTree.left = new BSTNode(3);
        validTree.right = new BSTNode(10);
        validTree.left.left = new BSTNode(1);
        validTree.left.right = new BSTNode(6);
        validTree.right.right = new BSTNode(14);
        validTree.left.right.left = new BSTNode(4);
        validTree.left.right.right = new BSTNode(7);
        validTree.right.right.left = new BSTNode(13);

        validator.printInOrder(validTree, "有效樹");
        validator.validateAndReport(validTree, "有效 BST（Valid BST）");

        // =========================================================
        // 測試二：違規樹 1 - 左子樹有大於根的值
        // =========================================================
        System.out.println("【測試二：違規樹 1 - 左子樹有大於根的值】");
        System.out.println("樹結構：");
        System.out.println("        8");
        System.out.println("       / \\");
        System.out.println("      3   10");
        System.out.println("     / \\");
        System.out.println("    1   9  ← 違規：9 大於根節點 8");
        System.out.println("   / \\");
        System.out.println("  4   7");
        System.out.println();

        BSTNode invalidTree1 = new BSTNode(8);
        invalidTree1.left = new BSTNode(3);
        invalidTree1.right = new BSTNode(10);
        invalidTree1.left.left = new BSTNode(1);
        invalidTree1.left.right = new BSTNode(9);  // 違規！左子樹不能有 9（大於根 8）
        invalidTree1.left.left.left = new BSTNode(4);
        invalidTree1.left.left.right = new BSTNode(7);

        validator.printInOrder(invalidTree1, "違規樹1");
        validator.validateAndReport(invalidTree1, "違規樹 1（左子樹有大於根的值）");

        // =========================================================
        // 測試三：違規樹 2 - 右子樹有小於根的值
        // =========================================================
        System.out.println("【測試三：違規樹 2 - 右子樹有小於根的值】");
        System.out.println("樹結構：");
        System.out.println("        8");
        System.out.println("       / \\");
        System.out.println("      3   10");
        System.out.println("         / \\");
        System.out.println("        5   14");
        System.out.println("             /");
        System.out.println("            12");
        System.out.println("  ← 違規：5 和 12 小於根節點 8（不該在右子樹）");
        System.out.println();

        BSTNode invalidTree2 = new BSTNode(8);
        invalidTree2.left = new BSTNode(3);
        invalidTree2.right = new BSTNode(10);
        invalidTree2.right.left = new BSTNode(5);   // 違規！右子樹不能有 5（小於根 8）
        invalidTree2.right.right = new BSTNode(14);
        invalidTree2.right.right.left = new BSTNode(12); // 違規！右子樹不能有 12（小於根 8）

        validator.printInOrder(invalidTree2, "違規樹2");
        validator.validateAndReport(invalidTree2, "違規樹 2（右子樹有小於根的值）");

        // =========================================================
        // 測試四：違規樹 3 - 深層違規（子樹內部違反 BST 性質）
        // =========================================================
        System.out.println("【測試四：違規樹 3 - 深層違規】");
        System.out.println("樹結構：");
        System.out.println("        8");
        System.out.println("       / \\");
        System.out.println("      3   10");
        System.out.println("     / \\");
        System.out.println("    1   6");
        System.out.println("       / \\");
        System.out.println("      9   7  ← 違規：9 在左子樹但大於 6");
        System.out.println();

        BSTNode invalidTree3 = new BSTNode(8);
        invalidTree3.left = new BSTNode(3);
        invalidTree3.right = new BSTNode(10);
        invalidTree3.left.left = new BSTNode(1);
        invalidTree3.left.right = new BSTNode(6);
        invalidTree3.left.right.left = new BSTNode(9);  // 違規！9 在 6 的左子樹，但 9 > 6
        invalidTree3.left.right.right = new BSTNode(7);

        validator.printInOrder(invalidTree3, "違規樹3");
        validator.validateAndReport(invalidTree3, "違規樹 3（深層違規：左子樹中出現大於父節點的值）");

        // =========================================================
        // 測試五：違規樹 4 - 重複值（BST 不允許重複）
        // =========================================================
        System.out.println("【測試五：違規樹 4 - 包含重複值】");
        System.out.println("樹結構：");
        System.out.println("        8");
        System.out.println("       / \\");
        System.out.println("      3   10");
        System.out.println("     / \\");
        System.out.println("    1   3  ← 違規：重複值 3（BST 不允許重複）");
        System.out.println();

        BSTNode invalidTree4 = new BSTNode(8);
        invalidTree4.left = new BSTNode(3);
        invalidTree4.right = new BSTNode(10);
        invalidTree4.left.left = new BSTNode(1);
        invalidTree4.left.right = new BSTNode(3);  // 違規！重複值

        validator.printInOrder(invalidTree4, "違規樹4");
        validator.validateAndReport(invalidTree4, "違規樹 4（包含重複值）");

        // =========================================================
        // 測試六：單節點（邊界情況）
        // =========================================================
        System.out.println("【測試六：邊界情況 - 單節點】");
        System.out.println("樹結構：");
        System.out.println("        5");
        System.out.println();

        BSTNode singleNode = new BSTNode(5);
        validator.printInOrder(singleNode, "單節點");
        validator.validateAndReport(singleNode, "單節點（Single Node）");

        // =========================================================
        // 測試七：空樹（邊界情況）
        // =========================================================
        System.out.println("【測試七：邊界情況 - 空樹】");
        System.out.println("樹結構：（空）");
        System.out.println();

        BSTNode emptyTree = null;
        validator.validateAndReport(emptyTree, "空樹（Empty Tree）");

        // =========================================================
        // 總結報告
        // =========================================================
        System.out.println("=========================================");
        System.out.println("        📊 測試總結");
        System.out.println("=========================================");
        System.out.println("測試案例 | 結果");
        System.out.println("-----------------------------------------");
        System.out.println("有效 BST     | ✅ 通過");
        System.out.println("違規樹 1     | ❌ 檢測到違規（左子樹有大於根的值）");
        System.out.println("違規樹 2     | ❌ 檢測到違規（右子樹有小於根的值）");
        System.out.println("違規樹 3     | ❌ 檢測到違規（深層違規）");
        System.out.println("違規樹 4     | ❌ 檢測到違規（重複值）");
        System.out.println("單節點       | ✅ 通過");
        System.out.println("空樹         | ✅ 通過");
        System.out.println("=========================================");
        System.out.println();
        System.out.println("📝 說明：");
        System.out.println("   - 使用 Min/Max Boundary Approach 驗證");
        System.out.println("   - 每個節點的值必須在 (min, max) 範圍內");
        System.out.println("   - 左子樹的 max = 當前節點值");
        System.out.println("   - 右子樹的 min = 當前節點值");
        System.out.println("   - 空節點視為合法");
        System.out.println("=========================================");
    }
}