import java.util.*;

/**
 * 商品類別 - 實作 Comparable 介面提供自然排序
 */
class Product implements Comparable<Product> {
    private final String id;
    private final String name;
    private final double price;
    private final int stock;

    // 建構子
    public Product(String id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // ---------- Getter ----------
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    /**
     * 自然排序：依 id 升冪
     */
    @Override
    public int compareTo(Product other) {
        return this.id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', price=%.1f, stock=%d}", 
                id, name, price, stock);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

/**
 * 商品比較器實作練習
 * 展示自然排序與多重規則比較器
 */
public class ProductComparatorPractice {

    public static void main(String[] args) {
        // 建立至少五筆測試商品（包含同價與同庫存資料）
        List<Product> products = createProductList();

        System.out.println("========== 原始商品列表（建立順序） ==========");
        printProducts(products);

        // ========== 1. 自然排序：依 id 升冪 ==========
        System.out.println("\n========== 自然排序（依 id 升冪） ==========");
        List<Product> sortedById = new ArrayList<>(products);  // 建立副本
        Collections.sort(sortedById);
        printProducts(sortedById);
        System.out.println("✅ 自然排序：id 由小到大");

        // ========== 2. 比較器一：依價格升冪，同價時依名稱 ==========
        System.out.println("\n========== 比較器一（價格升冪，同價時依名稱） ==========");
        List<Product> sortedByPrice = new ArrayList<>(products);  // 建立副本
        sortedByPrice.sort(new PriceAscNameAscComparator());
        printProducts(sortedByPrice);
        System.out.println("✅ 排序規則：價格由低到高 → 名稱由 A 到 Z");

        // ========== 3. 比較器二：依庫存降冪，同庫存時依 id ==========
        System.out.println("\n========== 比較器二（庫存降冪，同庫存時依 id） ==========");
        List<Product> sortedByStock = new ArrayList<>(products);  // 建立副本
        sortedByStock.sort(new StockDescIdAscComparator());
        printProducts(sortedByStock);
        System.out.println("✅ 排序規則：庫存由高到低 → id 由小到大");

        // ========== 4. 使用 Lambda 表達式（進階示範） ==========
        System.out.println("\n========== Lambda 進階示範（價格降冪） ==========");
        List<Product> sortedByLambda = new ArrayList<>(products);
        sortedByLambda.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        printProducts(sortedByLambda);
        System.out.println("✅ 使用 Lambda 實現價格由高到低");

        // ========== 5. 比較器鏈（Comparator chaining） ==========
        System.out.println("\n========== Comparator 鏈式寫法（名稱降冪，同名稱時依價格） ==========");
        List<Product> sortedByChain = new ArrayList<>(products);
        sortedByChain.sort(
            Comparator.comparing(Product::getName, Comparator.reverseOrder())
                      .thenComparing(Product::getPrice)
        );
        printProducts(sortedByChain);
        System.out.println("✅ 使用 Comparator 鏈：名稱由 Z 到 A → 價格由低到高");

        // ========== 6. 驗證原始順序未被改變 ==========
        System.out.println("\n========== 驗證原始列表未被改變 ==========");
        System.out.println("原始列表（仍維持建立順序）:");
        printProducts(products);
        System.out.println("✅ 使用 new ArrayList<>(products) 副本保護了原始資料");

        // ========== 7. 排序規則總結對照表 ==========
        printSummaryTable();
    }

    /**
     * 建立測試商品資料（至少五筆）
     */
    private static List<Product> createProductList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("P005", "機械鍵盤", 1200.0, 15));
        products.add(new Product("P002", "藍牙耳機", 800.0, 30));
        products.add(new Product("P008", "無線滑鼠", 500.0, 20));
        products.add(new Product("P001", "電競螢幕", 1200.0, 10));  // 與 P005 同價
        products.add(new Product("P003", "USB 集線器", 300.0, 30)); // 與 P002 同庫存
        products.add(new Product("P006", "筆記型電腦", 25000.0, 5));
        products.add(new Product("P004", "行動電源", 800.0, 15));    // 與 P002 同價
        products.add(new Product("P007", "智慧手錶", 800.0, 20));    // 與 P002、P004 同價
        return products;
    }

    /**
     * 印出商品列表
     */
    private static void printProducts(List<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, products.get(i));
        }
    }

    /**
     * 印出排序規則總結對照表
     */
    private static void printSummaryTable() {
        System.out.println("\n========== 排序規則總結對照表 ==========");
        System.out.println("┌─────────────┬─────────────────────────────────────────────┐");
        System.out.println("│  排序方式    │  規則說明                                   │");
        System.out.println("├─────────────┼─────────────────────────────────────────────┤");
        System.out.println("│  自然排序    │  id 升冪                                   │");
        System.out.println("│  比較器一    │  價格升冪 → 名稱升冪                       │");
        System.out.println("│  比較器二    │  庫存降冪 → id 升冪                       │");
        System.out.println("│  Lambda     │  價格降冪                                   │");
        System.out.println("│  Comparator鏈│  名稱降冪 → 價格升冪                       │");
        System.out.println("└─────────────┴─────────────────────────────────────────────┘");
    }

    // ========== 比較器實作類別 ==========

    /**
     * 比較器一：依價格升冪，同價時依名稱升冪
     */
    static class PriceAscNameAscComparator implements Comparator<Product> {
        @Override
        public int compare(Product p1, Product p2) {
            // 先比較價格
            int priceCompare = Double.compare(p1.getPrice(), p2.getPrice());
            if (priceCompare != 0) {
                return priceCompare;
            }
            // 價格相同時，比較名稱
            return p1.getName().compareTo(p2.getName());
        }
    }

    /**
     * 比較器二：依庫存降冪，同庫存時依 id 升冪
     */
    static class StockDescIdAscComparator implements Comparator<Product> {
        @Override
        public int compare(Product p1, Product p2) {
            // 先比較庫存（降冪，即 p2 減 p1）
            int stockCompare = Integer.compare(p2.getStock(), p1.getStock());
            if (stockCompare != 0) {
                return stockCompare;
            }
            // 庫存相同時，比較 id
            return p1.getId().compareTo(p2.getId());
        }
    }
}