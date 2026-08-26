/**
 * 課堂實作題六：樹狀選單搜索
 * 指定檔名：MenuTreeSearch.java
 * 
 * 完成 contains、findDepth、countLeaves 與前序顯示。
 * 找不到時深度回傳 -1。
 */
public class MenuTreeSearch {

    /**
     * 樹狀選單節點類別
     */
    static class MenuNode {
        String name;
        MenuNode[] children;
        
        MenuNode(String name) {
            this.name = name;
            this.children = new MenuNode[0];
        }
        
        MenuNode(String name, MenuNode[] children) {
            this.name = name;
            this.children = children;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 檢查選單中是否包含指定的節點名稱
     * @param root 根節點
     * @param target 要搜尋的名稱
     * @return 若存在則回傳 true，否則回傳 false
     */
    public static boolean contains(MenuNode root, String target) {
        if (root == null) {
            return false;
        }
        // 檢查當前節點
        if (root.name.equals(target)) {
            return true;
        }
        // 遞迴檢查所有子節點
        for (MenuNode child : root.children) {
            if (contains(child, target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 尋找指定節點的深度（根節點深度為 0）
     * @param root 根節點
     * @param target 要尋找的名稱
     * @return 節點深度，若找不到則回傳 -1
     */
    public static int findDepth(MenuNode root, String target) {
        if (root == null) {
            return -1;
        }
        return findDepthHelper(root, target, 0);
    }

    /**
     * 遞迴輔助方法：尋找節點深度
     * @param node 當前節點
     * @param target 要尋找的名稱
     * @param currentDepth 當前深度
     * @return 節點深度，若找不到則回傳 -1
     */
    private static int findDepthHelper(MenuNode node, String target, int currentDepth) {
        if (node == null) {
            return -1;
        }
        // 檢查當前節點
        if (node.name.equals(target)) {
            return currentDepth;
        }
        // 遞迴檢查所有子節點
        for (MenuNode child : node.children) {
            int result = findDepthHelper(child, target, currentDepth + 1);
            if (result != -1) {
                return result;
            }
        }
        return -1;
    }

    /**
     * 計算樹的葉子節點數量（沒有子節點的節點）
     * @param root 根節點
     * @return 葉子節點數量
     */
    public static int countLeaves(MenuNode root) {
        if (root == null) {
            return 0;
        }
        // 如果沒有子節點，則為葉子節點
        if (root.children == null || root.children.length == 0) {
            return 1;
        }
        // 遞迴計算所有子樹的葉子節點總數
        int totalLeaves = 0;
        for (MenuNode child : root.children) {
            totalLeaves += countLeaves(child);
        }
        return totalLeaves;
    }

    /**
     * 前序顯示樹狀選單（根 → 子樹）
     * @param root 根節點
     * @param indent 縮排字串
     */
    public static void preorderDisplay(MenuNode root, String indent) {
        if (root == null) {
            System.out.println(indent + "（空節點）");
            return;
        }
        // 顯示當前節點
        System.out.println(indent + root.name);
        // 遞迴顯示所有子節點（增加縮排）
        for (MenuNode child : root.children) {
            preorderDisplay(child, indent + "  ");
        }
    }

    /**
     * 前序顯示的包裝方法（預設縮排為空字串）
     * @param root 根節點
     */
    public static void preorderDisplay(MenuNode root) {
        preorderDisplay(root, "");
    }

    /**
     * 取得前序顯示的字串結果（用於測試驗證）
     * @param root 根節點
     * @param indent 縮排字串
     * @param sb 字串建構器
     */
    private static void getPreorderString(MenuNode root, String indent, StringBuilder sb) {
        if (root == null) {
            sb.append(indent).append("（空節點）\n");
            return;
        }
        sb.append(indent).append(root.name).append("\n");
        for (MenuNode child : root.children) {
            getPreorderString(child, indent + "  ", sb);
        }
    }

    /**
     * 取得前序顯示的字串結果
     * @param root 根節點
     * @return 前序顯示的字串
     */
    public static String getPreorderString(MenuNode root) {
        StringBuilder sb = new StringBuilder();
        getPreorderString(root, "", sb);
        return sb.toString();
    }

    /**
     * 主程式測試方法
     */
    public static void main(String[] args) {
        // 建立樹狀選單結構：
        // 餐廳
        //   ├── 中式料理
        //   │   ├── 川菜
        //   │   ├── 粵菜
        //   │   └── 台菜
        //   ├── 日式料理
        //   │   ├── 壽司
        //   │   ├── 拉麵
        //   │   └── 燒烤
        //   ├── 西式料理
        //   │   ├── 義大利麵
        //   │   ├── 牛排
        //   │   └── 披薩
        //   └── 甜點
        //       ├── 蛋糕
        //       └── 冰淇淋

        MenuNode cake = new MenuNode("蛋糕");
        MenuNode iceCream = new MenuNode("冰淇淋");
        MenuNode dessert = new MenuNode("甜點", new MenuNode[]{cake, iceCream});

        MenuNode sichuan = new MenuNode("川菜");
        MenuNode cantonese = new MenuNode("粵菜");
        MenuNode taiwanese = new MenuNode("台菜");
        MenuNode chinese = new MenuNode("中式料理", new MenuNode[]{sichuan, cantonese, taiwanese});

        MenuNode sushi = new MenuNode("壽司");
        MenuNode ramen = new MenuNode("拉麵");
        MenuNode bbq = new MenuNode("燒烤");
        MenuNode japanese = new MenuNode("日式料理", new MenuNode[]{sushi, ramen, bbq});

        MenuNode pasta = new MenuNode("義大利麵");
        MenuNode steak = new MenuNode("牛排");
        MenuNode pizza = new MenuNode("披薩");
        MenuNode western = new MenuNode("西式料理", new MenuNode[]{pasta, steak, pizza});

        MenuNode restaurant = new MenuNode("餐廳", new MenuNode[]{chinese, japanese, western, dessert});

        System.out.println("===== 樹狀選單結構（前序顯示） =====");
        preorderDisplay(restaurant);
        System.out.println();

        // 測試 contains 方法
        System.out.println("===== contains 方法測試 =====");
        String[] searchItems = {"餐廳", "川菜", "壽司", "蛋糕", "漢堡", "牛排", "冰淇淋", "披薩"};
        for (String item : searchItems) {
            boolean found = contains(restaurant, item);
            System.out.println("包含 \"" + item + "\"? " + found);
        }
        System.out.println();

        // 測試 findDepth 方法
        System.out.println("===== findDepth 方法測試 =====");
        String[] depthItems = {"餐廳", "中式料理", "川菜", "壽司", "蛋糕", "漢堡", "牛排", "冰淇淋"};
        for (String item : depthItems) {
            int depth = findDepth(restaurant, item);
            System.out.println("\"" + item + "\" 的深度: " + depth);
        }
        System.out.println();

        // 測試 countLeaves 方法
        System.out.println("===== countLeaves 方法測試 =====");
        System.out.println("葉子節點總數: " + countLeaves(restaurant));
        System.out.println();

        // 測試各子樹的葉子數量
        System.out.println("各子樹葉子節點統計：");
        System.out.println("  中式料理葉子數: " + countLeaves(chinese));
        System.out.println("  日式料理葉子數: " + countLeaves(japanese));
        System.out.println("  西式料理葉子數: " + countLeaves(western));
        System.out.println("  甜點葉子數: " + countLeaves(dessert));
        System.out.println();

        // 測試空樹與單節點
        System.out.println("===== 邊界條件測試 =====");
        System.out.println("空樹 contains: " + contains(null, "任何"));
        System.out.println("空樹 findDepth: " + findDepth(null, "任何"));
        System.out.println("空樹 countLeaves: " + countLeaves(null));
        System.out.println("空樹前序顯示：");
        preorderDisplay(null);
        System.out.println();

        // 單節點樹測試
        MenuNode single = new MenuNode("單一節點");
        System.out.println("單節點樹 contains(\"單一節點\"): " + contains(single, "單一節點"));
        System.out.println("單節點樹 contains(\"其他\"): " + contains(single, "其他"));
        System.out.println("單節點樹 findDepth(\"單一節點\"): " + findDepth(single, "單一節點"));
        System.out.println("單節點樹 findDepth(\"其他\"): " + findDepth(single, "其他"));
        System.out.println("單節點樹 countLeaves: " + countLeaves(single));
        System.out.println("單節點樹前序顯示：");
        preorderDisplay(single);

        // 驗證找不到時深度回傳 -1
        System.out.println();
        System.out.println("===== 驗證找不到時深度回傳 -1 =====");
        System.out.println("findDepth(餐廳, \"漢堡\") = " + findDepth(restaurant, "漢堡") + " (預期 -1)");
        System.out.println("findDepth(餐廳, \"拉麵\") = " + findDepth(restaurant, "拉麵") + " (預期 3)");
        
        // 使用字串輸出驗證
        System.out.println();
        System.out.println("===== 前序顯示字串輸出 =====");
        System.out.println(getPreorderString(restaurant));
    }
}