/**
 * 課後作業四：目錄大小累加
 * 指定檔名：FolderSizeTree.java
 * 
 * FolderNode 保存 name、ownSize、left、right。
 * 使用後序計算子樹大小，輸出總大小、最大子樹與葉子資料夾。
 */
public class FolderSizeTree {

    /**
     * 資料夾節點類別
     */
    static class FolderNode {
        String name;        // 資料夾名稱
        int ownSize;        // 自身檔案大小
        FolderNode left;    // 左子節點
        FolderNode right;   // 右子節點

        FolderNode(String name, int ownSize) {
            this.name = name;
            this.ownSize = ownSize;
            this.left = null;
            this.right = null;
        }

        FolderNode(String name, int ownSize, FolderNode left, FolderNode right) {
            this.name = name;
            this.ownSize = ownSize;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return name + " (自身: " + ownSize + ", 總大小: " + getTotalSize(this) + ")";
        }
    }

    /**
     * 計算資料夾的總大小（自身大小 + 所有子資料夾大小）
     * 使用後序遍歷（左 → 右 → 根）
     * @param node 資料夾節點
     * @return 總大小
     */
    public static int getTotalSize(FolderNode node) {
        if (node == null) {
            return 0;
        }
        // 後序計算：先計算左右子樹，再加上自身大小
        int leftSize = getTotalSize(node.left);
        int rightSize = getTotalSize(node.right);
        return node.ownSize + leftSize + rightSize;
    }

    /**
     * 尋找總大小最大的子樹（包含根節點本身）
     * @param node 資料夾節點
     * @return 最大子樹的節點（如果為空樹則回傳 null）
     */
    public static FolderNode findMaxSubtree(FolderNode node) {
        if (node == null) {
            return null;
        }
        return findMaxSubtreeHelper(node);
    }

    /**
     * 遞迴輔助方法：尋找最大子樹
     * @param node 當前節點（保證不為 null）
     * @return 最大子樹的節點
     */
    private static FolderNode findMaxSubtreeHelper(FolderNode node) {
        // 當前節點為最大子樹的候選
        FolderNode maxNode = node;
        int maxSize = getTotalSize(node);

        // 檢查左子樹
        if (node.left != null) {
            FolderNode leftMax = findMaxSubtreeHelper(node.left);
            int leftSize = getTotalSize(leftMax);
            if (leftSize > maxSize) {
                maxNode = leftMax;
                maxSize = leftSize;
            }
        }

        // 檢查右子樹
        if (node.right != null) {
            FolderNode rightMax = findMaxSubtreeHelper(node.right);
            int rightSize = getTotalSize(rightMax);
            if (rightSize > maxSize) {
                maxNode = rightMax;
                maxSize = rightSize;
            }
        }

        return maxNode;
    }

    /**
     * 收集所有葉子資料夾（沒有子節點的節點）
     * @param node 資料夾節點
     * @return 葉子資料夾名稱的 List
     */
    public static java.util.List<String> getLeafFolders(FolderNode node) {
        java.util.List<String> leaves = new java.util.ArrayList<>();
        collectLeaves(node, leaves);
        return leaves;
    }

    /**
     * 遞迴輔助方法：收集葉子資料夾
     */
    private static void collectLeaves(FolderNode node, java.util.List<String> leaves) {
        if (node == null) {
            return;
        }
        // 如果左右子節點皆為 null，則為葉子
        if (node.left == null && node.right == null) {
            leaves.add(node.name);
            return;
        }
        // 遞迴收集左右子樹的葉子
        collectLeaves(node.left, leaves);
        collectLeaves(node.right, leaves);
    }

    /**
     * 顯示完整的目錄樹結構（前序顯示，包含大小資訊）
     */
    public static void displayTree(FolderNode node, String indent) {
        if (node == null) {
            System.out.println(indent + "（空）");
            return;
        }
        int totalSize = getTotalSize(node);
        System.out.println(indent + node.name + " [自身: " + node.ownSize + ", 總大小: " + totalSize + "]");
        if (node.left != null || node.right != null) {
            displayTree(node.left, indent + "  ");
            displayTree(node.right, indent + "  ");
        }
    }

    /**
     * 輸出完整的目錄統計報告
     */
    public static void printReport(FolderNode root, String treeName) {
        System.out.println("===== " + treeName + " =====");
        if (root == null) {
            System.out.println("目錄樹為空");
            System.out.println();
            return;
        }

        // 顯示目錄結構
        System.out.println("目錄結構：");
        displayTree(root, "");
        System.out.println();

        // 計算總大小
        int totalSize = getTotalSize(root);
        System.out.println("總大小: " + totalSize);

        // 尋找最大子樹
        FolderNode maxSubtree = findMaxSubtree(root);
        if (maxSubtree != null) {
            System.out.println("最大子樹: " + maxSubtree.name + " (大小: " + getTotalSize(maxSubtree) + ")");
        }

        // 列出所有葉子資料夾
        java.util.List<String> leaves = getLeafFolders(root);
        System.out.println("葉子資料夾: " + (leaves.isEmpty() ? "無" : String.join(", ", leaves)));
        System.out.println();
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        System.out.println("===== 目錄大小累加系統測試 =====\n");

        // ===== 測試案例 1：完整目錄樹 =====
        // 結構：
        //           root (100)
        //          /         \
        //    projects (50)  docs (30)
        //     /    \          /    \
        //  src(20) bin(15)  pdf(10) txt(5)
        //  /  \
        // java(8) cpp(7)

        FolderNode java = new FolderNode("java", 8);
        FolderNode cpp = new FolderNode("cpp", 7);
        FolderNode src = new FolderNode("src", 20, java, cpp);
        FolderNode bin = new FolderNode("bin", 15);
        FolderNode projects = new FolderNode("projects", 50, src, bin);
        
        FolderNode pdf = new FolderNode("pdf", 10);
        FolderNode txt = new FolderNode("txt", 5);
        FolderNode docs = new FolderNode("docs", 30, pdf, txt);
        
        FolderNode root1 = new FolderNode("root", 100, projects, docs);
        
        printReport(root1, "完整目錄樹");

        // ===== 測試案例 2：只有左子樹的目錄 =====
        // 結構：
        //         root (50)
        //         /
        //      sub1 (30)
        //      /
        //   sub2 (20)
        //   /
        // sub3 (10)

        FolderNode sub3 = new FolderNode("sub3", 10);
        FolderNode sub2 = new FolderNode("sub2", 20, sub3, null);
        FolderNode sub1 = new FolderNode("sub1", 30, sub2, null);
        FolderNode root2 = new FolderNode("root", 50, sub1, null);
        
        printReport(root2, "左偏目錄樹");

        // ===== 測試案例 3：只有右子樹的目錄 =====
        // 結構：
        //    root (40)
        //         \
        //         sub1 (25)
        //              \
        //              sub2 (15)
        //                   \
        //                   sub3 (5)

        FolderNode sub3_2 = new FolderNode("sub3", 5);
        FolderNode sub2_2 = new FolderNode("sub2", 15, null, sub3_2);
        FolderNode sub1_2 = new FolderNode("sub1", 25, null, sub2_2);
        FolderNode root3 = new FolderNode("root", 40, null, sub1_2);
        
        printReport(root3, "右偏目錄樹");

        // ===== 測試案例 4：單節點目錄 =====
        FolderNode root4 = new FolderNode("single", 100);
        printReport(root4, "單節點目錄樹");

        // ===== 測試案例 5：空目錄樹 =====
        printReport(null, "空目錄樹");

        // ===== 測試案例 6：不對稱目錄樹 =====
        // 結構：
        //              root (200)
        //             /         \
        //      work(80)         personal(60)
        //      /    \           /        \
        //   proj(30) tmp(20)  photos(25)  music(15)
        //   /    \            /    \
        // code(15) doc(10)  family(10) travel(8)

        FolderNode code = new FolderNode("code", 15);
        FolderNode doc = new FolderNode("doc", 10);
        FolderNode proj = new FolderNode("proj", 30, code, doc);
        FolderNode tmp = new FolderNode("tmp", 20);
        FolderNode work = new FolderNode("work", 80, proj, tmp);
        
        FolderNode family = new FolderNode("family", 10);
        FolderNode travel = new FolderNode("travel", 8);
        FolderNode photos = new FolderNode("photos", 25, family, travel);
        FolderNode music = new FolderNode("music", 15);
        FolderNode personal = new FolderNode("personal", 60, photos, music);
        
        FolderNode root5 = new FolderNode("root", 200, work, personal);
        
        printReport(root5, "不對稱目錄樹");

        // ===== 額外驗證：最大子樹結果 =====
        System.out.println("===== 最大子樹詳細驗證 =====");
        System.out.println("完整樹最大子樹: " + findMaxSubtree(root1).name + 
                          " (大小: " + getTotalSize(findMaxSubtree(root1)) + ")");
        System.out.println("左偏樹最大子樹: " + findMaxSubtree(root2).name + 
                          " (大小: " + getTotalSize(findMaxSubtree(root2)) + ")");
        System.out.println("不對稱樹最大子樹: " + findMaxSubtree(root5).name + 
                          " (大小: " + getTotalSize(findMaxSubtree(root5)) + ")");
        System.out.println();

        // ===== 葉子資料夾詳細測試 =====
        System.out.println("===== 葉子資料夾詳細測試 =====");
        System.out.println("完整樹的葉子: " + String.join(", ", getLeafFolders(root1)));
        System.out.println("左偏樹的葉子: " + String.join(", ", getLeafFolders(root2)));
        System.out.println("右偏樹的葉子: " + String.join(", ", getLeafFolders(root3)));
        System.out.println("單節點樹的葉子: " + String.join(", ", getLeafFolders(root4)));
        System.out.println("不對稱樹的葉子: " + String.join(", ", getLeafFolders(root5)));
    }
}