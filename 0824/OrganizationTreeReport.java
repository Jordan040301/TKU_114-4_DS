/**
 * 課後作業五：組織架構報表
 * 指定檔名：OrganizationTreeReport.java
 * 
 * 新增 findParent、findDepth、pathFromRoot 與 printByLevel。
 * 找不到單位時回傳空結果，不得發生異常。
 */
public class OrganizationTreeReport {

    /**
     * 組織節點類別
     */
    static class OrgNode {
        String name;                // 單位名稱
        java.util.List<OrgNode> children;  // 子單位列表

        OrgNode(String name) {
            this.name = name;
            this.children = new java.util.ArrayList<>();
        }

        OrgNode(String name, java.util.List<OrgNode> children) {
            this.name = name;
            this.children = children != null ? children : new java.util.ArrayList<>();
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 尋找指定單位的父節點
     * @param root 根節點
     * @param target 要找的單位名稱
     * @return 父節點，若找不到則回傳 null
     */
    public static OrgNode findParent(OrgNode root, String target) {
        if (root == null || target == null) {
            return null;
        }
        // 如果根節點就是要找的目標，沒有父節點
        if (root.name.equals(target)) {
            return null;
        }
        return findParentHelper(root, target);
    }

    /**
     * 遞迴輔助方法：尋找父節點
     */
    private static OrgNode findParentHelper(OrgNode node, String target) {
        if (node == null) {
            return null;
        }

        // 檢查當前節點的子節點中是否有目標
        for (OrgNode child : node.children) {
            if (child.name.equals(target)) {
                return node; // 找到父節點
            }
        }

        // 遞迴檢查子節點
        for (OrgNode child : node.children) {
            OrgNode result = findParentHelper(child, target);
            if (result != null) {
                return result;
            }
        }

        return null; // 找不到
    }

    /**
     * 尋找指定單位的深度（根節點深度為 0）
     * @param root 根節點
     * @param target 要找的單位名稱
     * @return 深度，若找不到則回傳 -1
     */
    public static int findDepth(OrgNode root, String target) {
        if (root == null || target == null) {
            return -1;
        }
        return findDepthHelper(root, target, 0);
    }

    /**
     * 遞迴輔助方法：尋找深度
     */
    private static int findDepthHelper(OrgNode node, String target, int currentDepth) {
        if (node == null) {
            return -1;
        }

        if (node.name.equals(target)) {
            return currentDepth;
        }

        for (OrgNode child : node.children) {
            int result = findDepthHelper(child, target, currentDepth + 1);
            if (result != -1) {
                return result;
            }
        }

        return -1;
    }

    /**
     * 取得從根節點到指定單位的路徑
     * @param root 根節點
     * @param target 要找的單位名稱
     * @return 路徑的 List（包含根節點到目標節點），若找不到則回傳空 List
     */
    public static java.util.List<String> pathFromRoot(OrgNode root, String target) {
        java.util.List<String> path = new java.util.ArrayList<>();
        if (root == null || target == null) {
            return path; // 回傳空 List
        }

        if (buildPath(root, target, path)) {
            return path;
        }
        return new java.util.ArrayList<>(); // 找不到回傳空 List
    }

    /**
     * 遞迴輔助方法：建立路徑
     */
    private static boolean buildPath(OrgNode node, String target, java.util.List<String> path) {
        if (node == null) {
            return false;
        }

        // 將當前節點加入路徑
        path.add(node.name);

        // 如果找到目標，回傳 true
        if (node.name.equals(target)) {
            return true;
        }

        // 遞迴檢查子節點
        for (OrgNode child : node.children) {
            if (buildPath(child, target, path)) {
                return true;
            }
        }

        // 如果找不到，移除當前節點
        path.remove(path.size() - 1);
        return false;
    }

    /**
     * 逐層輸出組織架構（使用 Queue）
     * @param root 根節點
     * @return 逐層輸出的字串
     */
    public static String printByLevel(OrgNode root) {
        if (root == null) {
            return "組織為空";
        }

        StringBuilder result = new StringBuilder();
        java.util.Queue<OrgNode> queue = new java.util.LinkedList<>();
        queue.offer(root);

        int level = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            result.append("第 ").append(level).append(" 層: ");

            for (int i = 0; i < levelSize; i++) {
                OrgNode current = queue.poll();
                result.append(current.name);

                // 將所有子節點加入佇列
                for (OrgNode child : current.children) {
                    queue.offer(child);
                }

                if (i < levelSize - 1) {
                    result.append(", ");
                }
            }
            result.append("\n");
            level++;
        }

        return result.toString();
    }

    /**
     * 顯示組織架構樹（前序顯示）
     */
    public static void displayTree(OrgNode node, String indent) {
        if (node == null) {
            System.out.println(indent + "（空）");
            return;
        }

        System.out.println(indent + node.name);
        for (OrgNode child : node.children) {
            displayTree(child, indent + "  ");
        }
    }

    /**
     * 輸出完整的組織報告
     */
    public static void printReport(OrgNode root, String orgName) {
        System.out.println("===== " + orgName + " =====");
        if (root == null) {
            System.out.println("組織為空");
            System.out.println();
            return;
        }

        System.out.println("組織結構：");
        displayTree(root, "");
        System.out.println();

        System.out.println("逐層輸出：");
        System.out.println(printByLevel(root));

        // 測試幾個單位的查詢
        String[] testUnits = {"總經理", "業務部", "工程部", "行銷部", "不存在", "人事部"};
        for (String unit : testUnits) {
            System.out.println("--- 查詢單位: " + unit + " ---");
            
            OrgNode parent = findParent(root, unit);
            System.out.println("  父節點: " + (parent == null ? "無（可能是根節點或不存在）" : parent.name));
            
            int depth = findDepth(root, unit);
            System.out.println("  深度: " + (depth == -1 ? "不存在" : depth));
            
            java.util.List<String> path = pathFromRoot(root, unit);
            System.out.println("  路徑: " + (path.isEmpty() ? "不存在" : String.join(" → ", path)));
            System.out.println();
        }

        System.out.println();
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        System.out.println("===== 組織架構報表系統測試 =====\n");

        // ===== 建立組織架構 =====
        // 結構：
        //                    總經理
        //           /          |          \
        //      業務部       工程部       行銷部
        //      /   \        /   \        /   \
        //  國內業務 國外業務 軟體 硬體  廣告 公關
        //            /       / \
        //        美洲業務 前端 後端

        OrgNode frontEnd = new OrgNode("前端");
        OrgNode backEnd = new OrgNode("後端");
        OrgNode software = new OrgNode("軟體", java.util.Arrays.asList(frontEnd, backEnd));
        OrgNode hardware = new OrgNode("硬體");
        OrgNode engineering = new OrgNode("工程部", java.util.Arrays.asList(software, hardware));

        OrgNode domestic = new OrgNode("國內業務");
        OrgNode overseas = new OrgNode("國外業務");
        OrgNode america = new OrgNode("美洲業務");
        overseas.children.add(america);
        OrgNode sales = new OrgNode("業務部", java.util.Arrays.asList(domestic, overseas));

        OrgNode advertising = new OrgNode("廣告");
        OrgNode pr = new OrgNode("公關");
        OrgNode marketing = new OrgNode("行銷部", java.util.Arrays.asList(advertising, pr));

        OrgNode ceo = new OrgNode("總經理", java.util.Arrays.asList(sales, engineering, marketing));

        printReport(ceo, "完整組織架構");

        // ===== 測試案例 2：單節點組織 =====
        OrgNode single = new OrgNode("單一部門");
        printReport(single, "單節點組織");

        // ===== 測試案例 3：空組織 =====
        printReport(null, "空組織");

        // ===== 測試案例 4：只有兩層的組織 =====
        OrgNode dept1 = new OrgNode("部門一");
        OrgNode dept2 = new OrgNode("部門二");
        OrgNode dept3 = new OrgNode("部門三");
        OrgNode root = new OrgNode("總部", java.util.Arrays.asList(dept1, dept2, dept3));
        printReport(root, "兩層組織");

        // ===== 測試案例 5：更深層的組織 =====
        OrgNode team1 = new OrgNode("團隊A");
        OrgNode team2 = new OrgNode("團隊B");
        OrgNode team3 = new OrgNode("團隊C");
        OrgNode deptA = new OrgNode("部門A", java.util.Arrays.asList(team1, team2));
        OrgNode deptB = new OrgNode("部門B", java.util.Arrays.asList(team3));
        OrgNode head = new OrgNode("總部", java.util.Arrays.asList(deptA, deptB));
        
        // 添加更深層
        OrgNode subTeam1 = new OrgNode("子團隊1");
        OrgNode subTeam2 = new OrgNode("子團隊2");
        team1.children.add(subTeam1);
        team2.children.add(subTeam2);
        
        printReport(head, "多層組織架構");

        // ===== 邊界條件測試 =====
        System.out.println("===== 邊界條件測試 =====");
        System.out.println("findParent(null, \"任何\") = " + findParent(null, "任何"));
        System.out.println("findDepth(null, \"任何\") = " + findDepth(null, "任何"));
        System.out.println("pathFromRoot(null, \"任何\") = " + pathFromRoot(null, "任何"));
        System.out.println("printByLevel(null) = " + printByLevel(null));
        System.out.println();

        // ===== 測試找不到單位的情況 =====
        System.out.println("===== 找不到單位測試 =====");
        System.out.println("findParent(ceo, \"不存在\") = " + findParent(ceo, "不存在"));
        System.out.println("findDepth(ceo, \"不存在\") = " + findDepth(ceo, "不存在"));
        System.out.println("pathFromRoot(ceo, \"不存在\") = " + pathFromRoot(ceo, "不存在"));
    }
}