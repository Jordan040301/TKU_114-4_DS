/**
 * 檔名：ProductInventoryBst.java
 * 功能：商品庫存管理系統（使用 BST 儲存商品資料）
 * 說明：完成新增、查詢、補貨、扣庫存、刪除與中序報表
 *       所有操作先依 ID 找物件
 */

/**
 * 商品類別
 */
class Product {
    private String productId;   // 商品編號（唯一識別）
    private String name;        // 商品名稱
    private String category;    // 商品類別
    private int stock;          // 庫存數量
    private double price;       // 單價

    public Product(String productId, String name, String category, int stock, double price) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
    }

    // ========== Getter 方法 ==========
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    // ========== Setter 方法 ==========
    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * 補貨（增加庫存）
     * @param amount 增加數量
     * @return 補貨後的庫存數量
     */
    public int addStock(int amount) {
        if (amount > 0) {
            this.stock += amount;
        }
        return this.stock;
    }

    /**
     * 扣庫存（減少庫存）
     * @param amount 減少數量
     * @return true 表示扣庫存成功，false 表示庫存不足
     */
    public boolean deductStock(int amount) {
        if (amount <= 0) {
            return false;
        }
        if (this.stock >= amount) {
            this.stock -= amount;
            return true;
        }
        return false;  // 庫存不足
    }

    @Override
    public String toString() {
        return String.format("商品編號：%-10s | 名稱：%-10s | 類別：%-8s | 庫存：%4d | 單價：%6.2f",
                             productId, name, category, stock, price);
    }

    /**
     * 簡易格式（用於報表）
     */
    public String toReportString() {
        return String.format("%-10s %-12s %-10s %6d %8.2f",
                             productId, name, category, stock, price);
    }
}

/**
 * BST 節點（儲存 Product 物件）
 */
class ProductNode {
    Product product;
    ProductNode left;
    ProductNode right;

    public ProductNode(Product product) {
        this.product = product;
        this.left = null;
        this.right = null;
    }

    public String getProductId() {
        return product.getProductId();
    }
}

/**
 * 商品庫存 BST
 */
class ProductInventoryBST {
    private ProductNode root;
    private int size;

    public ProductInventoryBST() {
        this.root = null;
        this.size = 0;
    }

    // ========== 基本方法 ==========

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    /**
     * 依商品 ID 搜尋商品（核心方法）
     * @param productId 商品編號
     * @return 找到的商品物件，若找不到則回傳 null
     */
    public Product findProduct(String productId) {
        ProductNode result = findProductRec(root, productId);
        return result != null ? result.product : null;
    }

    private ProductNode findProductRec(ProductNode node, String productId) {
        if (node == null) {
            return null;
        }

        int compare = productId.compareTo(node.getProductId());

        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return findProductRec(node.left, productId);
        } else {
            return findProductRec(node.right, productId);
        }
    }

    // ========== 功能一：新增商品 ==========

    /**
     * 新增商品
     * @param product 要新增的商品
     * @return true 表示新增成功，false 表示商品編號已存在
     */
    public boolean addProduct(Product product) {
        // 檢查商品編號是否已存在
        if (findProduct(product.getProductId()) != null) {
            System.out.println("⚠️ 新增失敗：商品編號 " + product.getProductId() + " 已存在！");
            return false;
        }

        root = addProductRec(root, product);
        size++;
        System.out.println("✅ 新增商品成功：" + product);
        return true;
    }

    private ProductNode addProductRec(ProductNode node, Product product) {
        if (node == null) {
            return new ProductNode(product);
        }

        String newId = product.getProductId();
        String currentId = node.getProductId();

        if (newId.compareTo(currentId) < 0) {
            node.left = addProductRec(node.left, product);
        } else if (newId.compareTo(currentId) > 0) {
            node.right = addProductRec(node.right, product);
        }
        return node;
    }

    // ========== 功能二：查詢商品 ==========

    /**
     * 查詢商品（顯示詳細資訊）
     * @param productId 商品編號
     */
    public void queryProduct(String productId) {
        System.out.println("🔍 查詢商品：" + productId);
        Product product = findProduct(productId);

        if (product == null) {
            System.out.println("   ❌ 找不到商品編號 " + productId);
        } else {
            System.out.println("   ✅ 找到商品：");
            System.out.println("      " + product);
            System.out.println("   📊 庫存狀態：" + product.getStock() + " 件");
            System.out.println("   💰 庫存總值：" + (product.getStock() * product.getPrice()));
        }
        System.out.println();
    }

    // ========== 功能三：補貨 ==========

    /**
     * 補貨（增加庫存）
     * @param productId 商品編號
     * @param amount 補貨數量
     * @return true 表示補貨成功，false 表示商品不存在或數量無效
     */
    public boolean restock(String productId, int amount) {
        System.out.println("📦 補貨：" + productId + "，數量：" + amount);

        if (amount <= 0) {
            System.out.println("   ❌ 補貨數量必須大於 0！");
            return false;
        }

        Product product = findProduct(productId);
        if (product == null) {
            System.out.println("   ❌ 找不到商品編號 " + productId + "，補貨失敗！");
            return false;
        }

        int newStock = product.addStock(amount);
        System.out.println("   ✅ 補貨成功！商品：" + product.getName());
        System.out.println("   📊 新庫存量：" + newStock);
        System.out.println();
        return true;
    }

    // ========== 功能四：扣庫存 ==========

    /**
     * 扣庫存（減少庫存）
     * @param productId 商品編號
     * @param amount 扣庫存數量
     * @return true 表示扣庫存成功，false 表示商品不存在或庫存不足
     */
    public boolean deductStock(String productId, int amount) {
        System.out.println("📤 扣庫存：" + productId + "，數量：" + amount);

        if (amount <= 0) {
            System.out.println("   ❌ 扣庫存數量必須大於 0！");
            return false;
        }

        Product product = findProduct(productId);
        if (product == null) {
            System.out.println("   ❌ 找不到商品編號 " + productId + "，扣庫存失敗！");
            return false;
        }

        if (product.deductStock(amount)) {
            System.out.println("   ✅ 扣庫存成功！商品：" + product.getName());
            System.out.println("   📊 剩餘庫存量：" + product.getStock());
            System.out.println();
            return true;
        } else {
            System.out.println("   ❌ 庫存不足！目前庫存：" + product.getStock() + "，需求：" + amount);
            System.out.println();
            return false;
        }
    }

    // ========== 功能五：刪除商品 ==========

    /**
     * 刪除商品
     * @param productId 商品編號
     * @return 被刪除的商品物件，若找不到則回傳 null
     */
    public Product deleteProduct(String productId) {
        System.out.println("🗑️ 刪除商品：" + productId);

        Product product = findProduct(productId);
        if (product == null) {
            System.out.println("   ❌ 找不到商品編號 " + productId + "，刪除失敗！");
            return null;
        }

        root = deleteProductRec(root, productId);
        size--;
        System.out.println("   ✅ 刪除成功！");
        System.out.println("   🗑️ 已刪除商品：" + product);
        System.out.println();
        return product;
    }

    private ProductNode deleteProductRec(ProductNode node, String productId) {
        if (node == null) {
            return null;
        }

        int compare = productId.compareTo(node.getProductId());

        if (compare < 0) {
            node.left = deleteProductRec(node.left, productId);
        } else if (compare > 0) {
            node.right = deleteProductRec(node.right, productId);
        } else {
            // 找到要刪除的節點

            // 情況 1：葉子節點
            if (node.left == null && node.right == null) {
                return null;
            }

            // 情況 2：只有右子樹
            if (node.left == null) {
                return node.right;
            }

            // 情況 3：只有左子樹
            if (node.right == null) {
                return node.left;
            }

            // 情況 4：有兩個子樹
            ProductNode successor = findMin(node.right);
            node.product = successor.product;
            node.right = deleteProductRec(node.right, successor.getProductId());
        }

        return node;
    }

    private ProductNode findMin(ProductNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // ========== 功能六：中序報表 ==========

    /**
     * 產生中序報表（依商品編號排序）
     */
    public void generateReport() {
        System.out.println("=========================================");
        System.out.println("        📊 商品庫存報表");
        System.out.println("=========================================");
        System.out.println("產出時間：" + java.time.LocalDateTime.now());
        System.out.println("-----------------------------------------");
        System.out.println("排序方式：依商品編號（中序）");
        System.out.println("-----------------------------------------");

        if (root == null) {
            System.out.println("（目前無任何商品資料）");
        } else {
            System.out.printf("%-10s %-12s %-10s %6s %8s\n", 
                             "商品編號", "商品名稱", "類別", "庫存", "單價");
            System.out.println("-----------------------------------------");
            generateReportRec(root);
            System.out.println("-----------------------------------------");
            System.out.printf("商品總數：%d 項\n", size);
            
            // 計算庫存總值
            double totalValue = calculateTotalValue(root);
            System.out.printf("庫存總值：%.2f 元\n", totalValue);
        }
        System.out.println("=========================================");
        System.out.println();
    }

    private void generateReportRec(ProductNode node) {
        if (node != null) {
            generateReportRec(node.left);
            System.out.println("  " + node.product.toReportString());
            generateReportRec(node.right);
        }
    }

    /**
     * 計算總庫存價值
     */
    private double calculateTotalValue(ProductNode node) {
        if (node == null) {
            return 0;
        }
        Product p = node.product;
        return (p.getStock() * p.getPrice()) + 
               calculateTotalValue(node.left) + 
               calculateTotalValue(node.right);
    }

    // ========== 輔助功能 ==========

    /**
     * 依類別查詢商品
     */
    public void queryByCategory(String category) {
        System.out.println("🔍 查詢類別：" + category);
        System.out.println("-----------------------------------------");
        boolean found = queryByCategoryRec(root, category);
        if (!found) {
            System.out.println("（此類別無任何商品）");
        }
        System.out.println("-----------------------------------------");
        System.out.println();
    }

    private boolean queryByCategoryRec(ProductNode node, String category) {
        if (node == null) {
            return false;
        }
        boolean found = false;
        found = queryByCategoryRec(node.left, category) || found;
        if (node.product.getCategory().equals(category)) {
            System.out.println("  " + node.product);
            found = true;
        }
        found = queryByCategoryRec(node.right, category) || found;
        return found;
    }

    /**
     * 顯示樹的結構（輔助觀察）
     */
    public void printTreeStructure() {
        System.out.println("樹的結構（商品編號）：");
        printTreeStructureRec(root, 0, "根");
        System.out.println();
    }

    private void printTreeStructureRec(ProductNode node, int level, String direction) {
        if (node == null) {
            return;
        }
        String indent = "  ".repeat(level);
        System.out.println(indent + direction + ": " + node.getProductId() + 
                           " (" + node.product.getName() + ", 庫存:" + node.product.getStock() + ")");
        printTreeStructureRec(node.left, level + 1, "左");
        printTreeStructureRec(node.right, level + 1, "右");
    }

    /**
     * 顯示低庫存商品（庫存低於指定數量）
     */
    public void showLowStock(int threshold) {
        System.out.println("⚠️ 低庫存警報（庫存 < " + threshold + "）：");
        boolean found = showLowStockRec(root, threshold);
        if (!found) {
            System.out.println("   ✅ 所有商品庫存充足！");
        }
        System.out.println();
    }

    private boolean showLowStockRec(ProductNode node, int threshold) {
        if (node == null) {
            return false;
        }
        boolean found = false;
        found = showLowStockRec(node.left, threshold) || found;
        if (node.product.getStock() < threshold) {
            System.out.println("   " + node.product);
            found = true;
        }
        found = showLowStockRec(node.right, threshold) || found;
        return found;
    }
}

/**
 * 主程式
 */
public class ProductInventoryBst {
    public static void main(String[] args) {
        ProductInventoryBST inventory = new ProductInventoryBST();

        System.out.println("=========================================");
        System.out.println("     商品庫存管理系統");
        System.out.println("=========================================");
        System.out.println();

        // =========================================================
        // 功能一：新增商品
        // =========================================================
        System.out.println("【功能一：新增商品】");
        System.out.println("-----------------------------------------");

        // 新增多個商品
        Product p1 = new Product("P001", "筆記型電腦", "3C電子", 50, 25000.0);
        Product p2 = new Product("P002", "無線滑鼠", "3C電子", 200, 599.0);
        Product p3 = new Product("P003", "機械鍵盤", "3C電子", 100, 1299.0);
        Product p4 = new Product("P004", "咖啡機", "家電", 30, 4500.0);
        Product p5 = new Product("P005", "除濕機", "家電", 15, 6990.0);
        Product p6 = new Product("P006", "運動鞋", "服飾", 80, 2500.0);
        Product p7 = new Product("P007", "棒球帽", "服飾", 150, 399.0);
        Product p8 = new Product("P008", "藍芽耳機", "3C電子", 60, 1890.0);

        inventory.addProduct(p1);
        inventory.addProduct(p2);
        inventory.addProduct(p3);
        inventory.addProduct(p4);
        inventory.addProduct(p5);
        inventory.addProduct(p6);
        inventory.addProduct(p7);
        inventory.addProduct(p8);

        System.out.println();
        inventory.printTreeStructure();

        // =========================================================
        // 測試新增重複商品
        // =========================================================
        System.out.println("【測試：新增重複商品】");
        System.out.println("-----------------------------------------");
        Product duplicate = new Product("P003", "機械鍵盤進階版", "3C電子", 50, 1599.0);
        inventory.addProduct(duplicate);
        System.out.println();

        // =========================================================
        // 功能二：查詢商品
        // =========================================================
        System.out.println("【功能二：查詢商品】");
        System.out.println("-----------------------------------------");
        inventory.queryProduct("P005");
        inventory.queryProduct("P009");

        // =========================================================
        // 功能三：補貨
        // =========================================================
        System.out.println("【功能三：補貨】");
        System.out.println("-----------------------------------------");
        inventory.restock("P005", 20);      // 除濕機補貨
        inventory.restock("P009", 10);      // 不存在的商品
        inventory.restock("P005", 0);       // 無效數量

        // =========================================================
        // 功能四：扣庫存
        // =========================================================
        System.out.println("【功能四：扣庫存】");
        System.out.println("-----------------------------------------");
        inventory.deductStock("P001", 5);    // 筆電出貨 5 台
        inventory.deductStock("P004", 35);   // 咖啡機庫存不足（只有 30 台）
        inventory.deductStock("P007", 20);   // 棒球帽出貨 20 頂
        inventory.deductStock("P009", 1);    // 不存在的商品

        // =========================================================
        // 功能五：中序報表
        // =========================================================
        System.out.println("【功能五：中序報表】");
        System.out.println("-----------------------------------------");
        inventory.generateReport();

        // =========================================================
        // 附加功能：依類別查詢
        // =========================================================
        System.out.println("【附加功能：依類別查詢】");
        System.out.println("-----------------------------------------");
        inventory.queryByCategory("3C電子");
        inventory.queryByCategory("服飾");
        inventory.queryByCategory("食品");

        // =========================================================
        // 附加功能：低庫存警報
        // =========================================================
        System.out.println("【附加功能：低庫存警報】");
        System.out.println("-----------------------------------------");
        inventory.showLowStock(20);

        // =========================================================
        // 功能六：刪除商品
        // =========================================================
        System.out.println("【功能六：刪除商品】");
        System.out.println("-----------------------------------------");
        inventory.deleteProduct("P008");  // 刪除耳機（葉子節點）
        inventory.deleteProduct("P006");  // 刪除運動鞋（有子節點）
        inventory.deleteProduct("P010");  // 不存在的商品

        // =========================================================
        // 最終報表
        // =========================================================
        System.out.println("【最終庫存報表】");
        System.out.println("-----------------------------------------");
        inventory.generateReport();

        // =========================================================
        // 總結
        // =========================================================
        System.out.println("=========================================");
        System.out.println("        📊 操作總結");
        System.out.println("=========================================");
        System.out.println("初始新增商品數：" + 8);
        System.out.println("重複新增嘗試：" + 1 + "（被拒絕）");
        System.out.println("成功刪除商品數：" + 2);
        System.out.println("最終商品總數：" + inventory.getSize());
        System.out.println("=========================================");
    }
}