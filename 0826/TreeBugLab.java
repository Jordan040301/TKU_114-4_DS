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
 * 樹蟲實驗室 (Tree Bug Lab)
 * 為四種錯誤各建立最小失敗案例，再完成修正
 * 
 * 四種錯誤:
 * 1. search 方向相反 (在 BST 中往錯誤的方向搜尋)
 * 2. inorder 順序錯誤 (中序走訪順序不正確)
 * 3. delete 缺失 child (刪除時沒有正確處理子節點)
 * 4. validation 只檢查直接 child (驗證有效性時只檢查直接子節點，而非整個子樹)
 */
public class TreeBugLab {

    // ============================================================
    // 第一種錯誤: search 方向相反
    // ============================================================
    static class BuggySearchBST {
        BSTNode root;

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
            return node;
        }

        // BUG: search 方向相反 - 當要找的值小於當前節點時，往右子樹找 (應該是往左)
        // 當要找的值大於當前節點時，往左子樹找 (應該是往右)
        public boolean searchBuggy(int value) {
            return searchBuggyRecursive(root, value);
        }

        private boolean searchBuggyRecursive(BSTNode node, int value) {
            if (node == null) {
                return false;
            }
            if (node.value == value) {
                return true;
            }
            // BUG: 方向相反
            if (value < node.value) {
                return searchBuggyRecursive(node.right, value);  // 錯誤: 應為 node.left
            } else {
                return searchBuggyRecursive(node.left, value);   // 錯誤: 應為 node.right
            }
        }

        // 修正後的 search
        public boolean searchFixed(int value) {
            return searchFixedRecursive(root, value);
        }

        private boolean searchFixedRecursive(BSTNode node, int value) {
            if (node == null) {
                return false;
            }
            if (node.value == value) {
                return true;
            }
            if (value < node.value) {
                return searchFixedRecursive(node.left, value);
            } else {
                return searchFixedRecursive(node.right, value);
            }
        }

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
    }

    // ============================================================
    // 第二種錯誤: inorder 順序錯誤
    // ============================================================
    static class BuggyInorderBST {
        BSTNode root;

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
            return node;
        }

        // BUG: inorder 順序錯誤 - 先走訪右子樹，然後根，然後左子樹 (這是反中序)
        public List<Integer> inorderBuggy() {
            List<Integer> result = new ArrayList<>();
            inorderBuggyRecursive(root, result);
            return result;
        }

        private void inorderBuggyRecursive(BSTNode node, List<Integer> result) {
            if (node != null) {
                // BUG: 順序錯誤 - 先右再根再左 (應為 左->根->右)
                inorderBuggyRecursive(node.right, result);  // 錯誤: 應先走訪左子樹
                result.add(node.value);
                inorderBuggyRecursive(node.left, result);   // 錯誤: 應最後走訪右子樹
            }
        }

        // 修正後的 inorder
        public List<Integer> inorderFixed() {
            List<Integer> result = new ArrayList<>();
            inorderFixedRecursive(root, result);
            return result;
        }

        private void inorderFixedRecursive(BSTNode node, List<Integer> result) {
            if (node != null) {
                inorderFixedRecursive(node.left, result);
                result.add(node.value);
                inorderFixedRecursive(node.right, result);
            }
        }
    }

    // ============================================================
    // 第三種錯誤: delete 缺失 child
    // ============================================================
    static class BuggyDeleteBST {
        BSTNode root;

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
            return node;
        }

        // BUG: delete 缺失 child - 刪除節點時沒有正確處理子節點
        // 當節點有一個子節點時，直接回傳 null (遺失子節點)
        public boolean removeBuggy(int value) {
            if (!contains(value)) {
                return false;
            }
            root = removeBuggyRecursive(root, value);
            return true;
        }

        private BSTNode removeBuggyRecursive(BSTNode node, int value) {
            if (node == null) {
                return null;
            }

            if (value < node.value) {
                node.left = removeBuggyRecursive(node.left, value);
            } else if (value > node.value) {
                node.right = removeBuggyRecursive(node.right, value);
            } else {
                // 找到要刪除的節點

                // Case 1: 葉節點 (正確)
                if (node.left == null && node.right == null) {
                    return null;
                }

                // Case 2: 只有一個子節點 (BUG: 沒有保留子節點)
                if (node.left == null) {
                    // BUG: 應該 return node.right，但錯誤地 return null
                    return null;  // 錯誤: 遺失右子節點
                }
                if (node.right == null) {
                    // BUG: 應該 return node.left，但錯誤地 return null
                    return null;  // 錯誤: 遺失左子節點
                }

                // Case 3: 有兩個子節點 (正確)
                int successorValue = findMin(node.right);
                node.value = successorValue;
                node.right = removeBuggyRecursive(node.right, successorValue);
            }
            return node;
        }

        private int findMin(BSTNode node) {
            while (node.left != null) {
                node = node.left;
            }
            return node.value;
        }

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

        // 修正後的 delete
        public boolean removeFixed(int value) {
            if (!contains(value)) {
                return false;
            }
            root = removeFixedRecursive(root, value);
            return true;
        }

        private BSTNode removeFixedRecursive(BSTNode node, int value) {
            if (node == null) {
                return null;
            }

            if (value < node.value) {
                node.left = removeFixedRecursive(node.left, value);
            } else if (value > node.value) {
                node.right = removeFixedRecursive(node.right, value);
            } else {
                if (node.left == null && node.right == null) {
                    return null;
                }
                if (node.left == null) {
                    return node.right;  // 修正: 保留右子節點
                }
                if (node.right == null) {
                    return node.left;   // 修正: 保留左子節點
                }
                int successorValue = findMin(node.right);
                node.value = successorValue;
                node.right = removeFixedRecursive(node.right, successorValue);
            }
            return node;
        }

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
    }

    // ============================================================
    // 第四種錯誤: validation 只檢查直接 child
    // ============================================================
    static class BuggyValidationBST {
        BSTNode root;

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
            return node;
        }

        // BUG: validation 只檢查直接 child (沒有遞迴檢查整個子樹)
        public boolean isValidBuggy() {
            if (root == null) {
                return true;
            }
            return isValidBuggyRecursive(root);
        }

        private boolean isValidBuggyRecursive(BSTNode node) {
            if (node == null) {
                return true;
            }

            // BUG: 只檢查直接子節點，沒有檢查整個子樹
            // 例如: 根節點 50，左子節點 30，30 的右子節點 40 (合理)
            // 但 30 的右子節點如果是 60 (不合理，因為 60 > 50)
            // 這個錯誤只檢查 direct child，所以不會發現 60 在左子樹中違反規則
            if (node.left != null && node.left.value > node.value) {
                return false;
            }
            if (node.right != null && node.right.value < node.value) {
                return false;
            }

            // 遞迴檢查子節點 (但沒有傳遞範圍限制)
            return isValidBuggyRecursive(node.left) && isValidBuggyRecursive(node.right);
        }

        // 修正後的 validation (使用範圍限制)
        public boolean isValidFixed() {
            return isValidFixedRecursive(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        private boolean isValidFixedRecursive(BSTNode node, int min, int max) {
            if (node == null) {
                return true;
            }
            if (node.value < min || node.value > max) {
                return false;
            }
            return isValidFixedRecursive(node.left, min, node.value - 1) &&
                   isValidFixedRecursive(node.right, node.value + 1, max);
        }

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

        // 建立一個無效的 BST (用於測試)
        // 樹結構: 50 的左子樹中包含 60 (違反 BST 規則)
        public void buildInvalidTree() {
            // 手動建立一個無效的 BST
            //       50
            //      /  \
            //     30   70
            //    /  \
            //   20   60   <- 60 在左子樹中，但大於 50，違反 BST 規則
            root = new BSTNode(50);
            root.left = new BSTNode(30);
            root.right = new BSTNode(70);
            root.left.left = new BSTNode(20);
            root.left.right = new BSTNode(60);  // 這個節點違反規則
        }
    }

    // ============================================================
    // 主程式測試
    // ============================================================
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("         樹蟲實驗室 (Tree Bug Lab)");
        System.out.println("========================================\n");

        // =========================================================
        // 測試一: search 方向相反
        // =========================================================
        System.out.println("【錯誤一】search 方向相反");
        System.out.println("----------------------------------------");
        BuggySearchBST searchBst = new BuggySearchBST();
        int[] searchValues = {50, 30, 70, 20, 40};
        for (int val : searchValues) {
            searchBst.add(val);
        }
        System.out.println("BST (中序走訪): " + searchBst.inorder());
        System.out.println();

        int testSearch = 20;
        System.out.println("搜尋值: " + testSearch);
        System.out.println("  錯誤版本 (方向相反): " + searchBst.searchBuggy(testSearch) + " (應該為 true)");
        System.out.println("  修正版本:             " + searchBst.searchFixed(testSearch) + " (正確)");
        System.out.println();

        testSearch = 40;
        System.out.println("搜尋值: " + testSearch);
        System.out.println("  錯誤版本 (方向相反): " + searchBst.searchBuggy(testSearch) + " (應該為 true)");
        System.out.println("  修正版本:             " + searchBst.searchFixed(testSearch) + " (正確)");
        System.out.println();

        testSearch = 60;
        System.out.println("搜尋值: " + testSearch + " (不存在)");
        System.out.println("  錯誤版本 (方向相反): " + searchBst.searchBuggy(testSearch) + " (應該為 false)");
        System.out.println("  修正版本:             " + searchBst.searchFixed(testSearch) + " (正確)");
        System.out.println();

        // =========================================================
        // 測試二: inorder 順序錯誤
        // =========================================================
        System.out.println("【錯誤二】inorder 順序錯誤");
        System.out.println("----------------------------------------");
        BuggyInorderBST inorderBst = new BuggyInorderBST();
        int[] inorderValues = {50, 30, 70, 20, 40, 60, 80};
        for (int val : inorderValues) {
            inorderBst.add(val);
        }
        System.out.println("正確的 inorder 應為: [20, 30, 40, 50, 60, 70, 80]");
        System.out.println("  錯誤版本 (先右再左): " + inorderBst.inorderBuggy());
        System.out.println("  修正版本:             " + inorderBst.inorderFixed());
        System.out.println();

        // =========================================================
        // 測試三: delete 缺失 child
        // =========================================================
        System.out.println("【錯誤三】delete 缺失 child");
        System.out.println("----------------------------------------");
        BuggyDeleteBST deleteBst = new BuggyDeleteBST();
        int[] deleteValues = {50, 30, 70, 20, 40, 60, 80, 25};
        for (int val : deleteValues) {
            deleteBst.add(val);
        }
        System.out.println("原始 BST (中序走訪): " + deleteBst.inorder());
        System.out.println();

        // 刪除只有左子節點的節點 (30 有左子節點 20，但 20 有右子節點 25)
        // 所以刪除 20 時，20 只有右子節點 25 (只有一個子節點)
        System.out.println("刪除節點 20 (只有一個子節點 25)");
        deleteBst.removeBuggy(20);
        System.out.println("  錯誤版本 (遺失子節點): " + deleteBst.inorder() + " (遺失了 25)");
        System.out.println();

        // 重新建立樹
        deleteBst = new BuggyDeleteBST();
        for (int val : deleteValues) {
            deleteBst.add(val);
        }
        System.out.println("重新建立 BST (中序走訪): " + deleteBst.inorder());
        System.out.println("刪除節點 20 (使用修正版本)");
        deleteBst.removeFixed(20);
        System.out.println("  修正版本 (保留子節點): " + deleteBst.inorder() + " (25 被保留)");
        System.out.println();

        // 測試刪除只有右子節點的節點
        System.out.println("\n刪除節點 80 (只有一個子節點)");
        deleteBst = new BuggyDeleteBST();
        int[] deleteValues2 = {50, 30, 70, 20, 40, 60, 80};
        for (int val : deleteValues2) {
            deleteBst.add(val);
        }
        System.out.println("原始 BST (中序走訪): " + deleteBst.inorder());
        System.out.println("刪除節點 80 (使用錯誤版本)");
        deleteBst.removeBuggy(80);
        System.out.println("  錯誤版本 (遺失子節點): " + deleteBst.inorder());
        System.out.println();

        // =========================================================
        // 測試四: validation 只檢查直接 child
        // =========================================================
        System.out.println("【錯誤四】validation 只檢查直接 child");
        System.out.println("----------------------------------------");
        BuggyValidationBST validationBst = new BuggyValidationBST();
        validationBst.buildInvalidTree();
        System.out.println("無效的 BST (中序走訪): " + validationBst.inorder());
        System.out.println("  錯誤版本 (只檢查直接 child): " + validationBst.isValidBuggy() + " (應該為 false)");
        System.out.println("  修正版本 (檢查整個子樹):    " + validationBst.isValidFixed() + " (正確)");
        System.out.println();

        // 建立有效的 BST 測試
        BuggyValidationBST validBst = new BuggyValidationBST();
        int[] validValues = {50, 30, 70, 20, 40, 60, 80};
        for (int val : validValues) {
            validBst.add(val);
        }
        System.out.println("有效的 BST (中序走訪): " + validBst.inorder());
        System.out.println("  錯誤版本 (只檢查直接 child): " + validBst.isValidBuggy() + " (應該為 true)");
        System.out.println("  修正版本 (檢查整個子樹):    " + validBst.isValidFixed() + " (正確)");
        System.out.println();

        // =========================================================
        // 總結
        // =========================================================
        System.out.println("========================================");
        System.out.println("         錯誤修正總結");
        System.out.println("========================================");
        System.out.println("1. search 方向相反:");
        System.out.println("   - 錯誤: 值小於當前節點時往右找，值大於時往左找");
        System.out.println("   - 修正: 值小於時往左找，值大於時往右找");
        System.out.println();
        System.out.println("2. inorder 順序錯誤:");
        System.out.println("   - 錯誤: 先走訪右子樹、再根、最後左子樹");
        System.out.println("   - 修正: 先走訪左子樹、再根、最後右子樹");
        System.out.println();
        System.out.println("3. delete 缺失 child:");
        System.out.println("   - 錯誤: 刪除只有一個子節點的節點時，沒有保留該子節點");
        System.out.println("   - 修正: 只有左子節點時回傳左子節點，只有右子節點時回傳右子節點");
        System.out.println();
        System.out.println("4. validation 只檢查直接 child:");
        System.out.println("   - 錯誤: 只檢查直接子節點是否符合 BST 規則");
        System.out.println("   - 修正: 遞迴檢查整個子樹，並傳遞範圍限制");
        System.out.println();
        System.out.println("========================================");
        System.out.println("         所有錯誤已修正完成！");
        System.out.println("========================================");
    }
}