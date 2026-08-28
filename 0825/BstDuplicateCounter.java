/**
 * 檔名：bstDuplicateCounter.java
 * 功能：二元搜尋樹支援重複鍵值計數
 * 說明：相同 key 不建立新節點，而是增加該節點的計數
 * 輸出：按順序輸出 key(count)
 */

class CountNode {
    int key;
    int count;          // 出現次數
    CountNode left;
    CountNode right;

    public CountNode(int key) {
        this.key = key;
        this.count = 1;  // 初始建立時次數為 1
        this.left = null;
        this.right = null;
    }
}

class DuplicateBST {
    private CountNode root;

    public DuplicateBST() {
        this.root = null;
    }

    /**
     * 插入節點（含重複計數功能）
     * @param key 要插入的鍵值
     */
    public void insert(int key) {
        root = insertRec(root, key);
    }

    private CountNode insertRec(CountNode node, int key) {
        if (node == null) {
            // 第一次出現，建立新節點
            System.out.println("插入 " + key + " → 建立新節點，計數：1");
            return new CountNode(key);
        }

        if (key == node.key) {
            // 相同 key，不建立新節點，計數 +1
            node.count++;
            System.out.println("插入 " + key + " → 計數增加為：" + node.count);
        } else if (key < node.key) {
            node.left = insertRec(node.left, key);
        } else {
            node.right = insertRec(node.right, key);
        }

        return node;
    }

    /**
     * 中序遍歷輸出所有節點的 key 與 count
     * 格式：key(count)
     */
    public void printInOrder() {
        System.out.print("中序遍歷輸出：");
        inOrderRec(root);
        System.out.println();
    }

    private void inOrderRec(CountNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.key + "(" + node.count + ") ");
            inOrderRec(node.right);
        }
    }

    /**
     * 前序遍歷（輔助觀察樹結構）
     */
    public void printPreOrder() {
        System.out.print("前序遍歷輸出：");
        preOrderRec(root);
        System.out.println();
    }

    private void preOrderRec(CountNode node) {
        if (node != null) {
            System.out.print(node.key + "(" + node.count + ") ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }

    /**
     * 查詢某個 key 的出現次數
     * @param key 要查詢的鍵值
     * @return 出現次數（若不存在回傳 0）
     */
    public int getCount(int key) {
        CountNode current = root;
        while (current != null) {
            if (key == current.key) {
                return current.count;
            } else if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return 0;
    }

    /**
     * 取得樹中所有不同鍵值的數量
     */
    public int getDistinctKeyCount() {
        return distinctKeyCountRec(root);
    }

    private int distinctKeyCountRec(CountNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + distinctKeyCountRec(node.left) + distinctKeyCountRec(node.right);
    }

    /**
     * 取得樹中所有節點的總數（含重複）
     */
    public int getTotalCount() {
        return totalCountRec(root);
    }

    private int totalCountRec(CountNode node) {
        if (node == null) {
            return 0;
        }
        return node.count + totalCountRec(node.left) + totalCountRec(node.right);
    }
}

public class BstDuplicateCounter {
    public static void main(String[] args) {
        DuplicateBST bst = new DuplicateBST();

        // 測試資料：包含重複的鍵值
        int[] data = {50, 30, 70, 50, 20, 50, 70, 40, 30, 60, 80, 50, 30};

        System.out.println("===== 開始插入資料（含重複鍵值） =====");
        for (int val : data) {
            bst.insert(val);
        }

        System.out.println();
        System.out.println("===== 樹的結構與計數 =====");

        // 按順序輸出（中序）
        bst.printInOrder();

        // 前序輸出（觀察樹結構）
        bst.printPreOrder();

        System.out.println();
        System.out.println("===== 統計資訊 =====");
        System.out.println("不同鍵值數量：" + bst.getDistinctKeyCount());
        System.out.println("總插入次數（含重複）：" + bst.getTotalCount());

        System.out.println();
        System.out.println("===== 個別查詢 =====");
        System.out.println("鍵值 50 出現次數：" + bst.getCount(50));
        System.out.println("鍵值 30 出現次數：" + bst.getCount(30));
        System.out.println("鍵值 70 出現次數：" + bst.getCount(70));
        System.out.println("鍵值 90 出現次數：" + bst.getCount(90));
    }
}