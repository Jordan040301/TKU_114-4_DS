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
 * 二元搜尋樹操作稽核
 * 每次 add 或 remove 後輸出操作、結果、中序走訪、大小、高度、有效性
 */
public class BstOperationAudit {
    private BSTNode root;
    private int size;

    public BstOperationAudit() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 新增元素 (add)
     */
    public boolean add(int value) {
        // 檢查是否已存在 (重複)
        if (contains(value)) {
            System.out.println("操作: ADD " + value);
            System.out.println("結果: 失敗 (重複元素)");
            printAudit();
            System.out.println();
            return false;
        }

        root = addRecursive(root, value);
        size++;
        System.out.println("操作: ADD " + value);
        System.out.println("結果: 成功");
        printAudit();
        System.out.println();
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
        // 等於的情況已在 contains 處理
        return node;
    }

    /**
     * 移除元素 (remove)
     */
    public boolean remove(int value) {
        // 檢查是否存在 (缺失)
        if (!contains(value)) {
            System.out.println("操作: REMOVE " + value);
            System.out.println("結果: 失敗 (元素不存在)");
            printAudit();
            System.out.println();
            return false;
        }

        root = removeRecursive(root, value);
        size--;
        System.out.println("操作: REMOVE " + value);
        System.out.println("結果: 成功");
        printAudit();
        System.out.println();
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
            // 找到要刪除的節點

            // Case 1: 葉節點 (無子節點)
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
            // 找到右子樹中的最小節點 (中序後繼者)
            int successorValue = findMin(node.right);
            node.value = successorValue;
            // 刪除右子樹中的最小節點
            node.right = removeRecursive(node.right, successorValue);
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
     * 中序走訪 (Inorder) - 輸出排序後的元素
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
     * 計算樹的高度
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
        // 檢查當前節點的值是否在允許範圍內
        if (node.value < min || node.value > max) {
            return false;
        }
        // 遞迴檢查左右子樹
        return isValidRecursive(node.left, min, node.value - 1) &&
               isValidRecursive(node.right, node.value + 1, max);
    }

    /**
     * 輸出稽核資訊
     */
    public void printAudit() {
        System.out.println("  中序走訪: " + inorder());
        System.out.println("  大小 (Size): " + size);
        System.out.println("  高度 (Height): " + height());
        System.out.println("  有效性 (Valid): " + (isValid() ? "有效" : "無效"));
    }

    /**
     * 清空樹
     */
    public void clear() {
        root = null;
        size = 0;
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    二元搜尋樹操作稽核系統");
        System.out.println("========================================\n");

        BstOperationAudit bst = new BstOperationAudit();

        // ========== 第一階段：新增元素 (包含重複) ==========
        System.out.println("【第一階段】新增元素");
        System.out.println("----------------------------------------");

        int[] addValues = {50, 30, 70, 20, 40, 60, 80, 30, 90, 10};

        for (int val : addValues) {
            bst.add(val);
        }

        // ========== 第二階段：刪除元素 (包含三種 Case) ==========
        System.out.println("【第二階段】刪除元素");
        System.out.println("----------------------------------------");

        // Case 1: 刪除葉節點 (Leaf) - 例如 10
        bst.remove(10);

        // Case 2: 刪除只有一個子節點的節點 - 例如 20 (只有左子節點 10)
        // 但 10 已被刪除，所以重新建立一個只有一個子節點的情境
        // 我們直接測試刪除 90 (只有左子節點)
        bst.remove(90);

        // Case 3: 刪除有兩個子節點的節點 - 例如 50 (根節點)
        bst.remove(50);

        // 再刪除一些節點展示各種 Case
        // 刪除 70 (有兩個子節點 60, 80)
        bst.remove(70);

        // 刪除 30 (有兩個子節點 20, 40)
        bst.remove(30);

        // ========== 第三階段：嘗試刪除不存在的元素 ==========
        System.out.println("【第三階段】嘗試刪除不存在的元素");
        System.out.println("----------------------------------------");

        bst.remove(100);  // 元素不存在
        bst.remove(25);   // 元素不存在
        bst.remove(999);  // 元素不存在

        // ========== 第四階段：新增更多元素測試 ==========
        System.out.println("【第四階段】新增更多元素");
        System.out.println("----------------------------------------");

        bst.add(45);
        bst.add(55);
        bst.add(85);

        // 測試重複新增
        bst.add(45);  // 重複

        // ========== 第五階段：清空後重新測試 ==========
        System.out.println("【第五階段】清空後重新測試");
        System.out.println("----------------------------------------");

        bst.clear();
        System.out.println("樹已清空");
        bst.printAudit();
        System.out.println();

        // 重新新增
        System.out.println("重新新增元素: 5, 3, 7, 2, 4, 6, 8");
        int[] newValues = {5, 3, 7, 2, 4, 6, 8};
        for (int val : newValues) {
            bst.add(val);
        }

        // 測試各種刪除案例
        System.out.println("【最終測試】各種刪除案例");
        System.out.println("----------------------------------------");

        // Case 1: 刪除葉節點 (2, 4, 6, 8)
        bst.remove(2);  // 葉節點
        bst.remove(8);  // 葉節點

        // Case 2: 刪除只有一個子節點的節點 (4 只有右子節點? 實際上 4 是葉節點)
        // 重新建立一個只有一個子節點的情境
        bst.clear();
        System.out.println("重新建立樹: 5, 3, 7, 2, 4, 6, 8");
        for (int val : newValues) {
            bst.add(val);
        }
        // 刪除 2 (葉節點) 讓 3 只有右子節點 4
        bst.remove(2);
        // 此時 3 只有右子節點 4，刪除 3 (只有一個子節點)
        bst.remove(3);  // Case 2: 只有右子節點

        // Case 3: 刪除有兩個子節點的節點 (5 有兩個子節點)
        bst.remove(5);  // Case 3: 兩個子節點

        System.out.println("========================================");
        System.out.println("        稽核完成！");
        System.out.println("========================================");
    }
}