import java.util.*;

/**
 * Top-K 熱門商品
 * 保留銷售額最高 K 筆商品，銷售額相同時 id 字母順序優先
 */
class Product {
    private String id;        // 商品編號
    private long sales;       // 銷售額
    
    public Product(String id, long sales) {
        this.id = id;
        this.sales = sales;
    }
    
    public String getId() {
        return id;
    }
    
    public long getSales() {
        return sales;
    }
    
    public void addSales(long additionalSales) {
        this.sales += additionalSales;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Objects.equals(id, product.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("%s | 銷售額: %,d", id, sales);
    }
    
    /**
     * 簡短顯示格式
     */
    public String toShortString() {
        return String.format("%s(%,d)", id, sales);
    }
}

/**
 * Top-K 熱門商品追蹤器
 */
public class TopSellingProducts {
    private PriorityQueue<Product> minHeap;  // 最小堆，保留 Top-K
    private Map<String, Product> productMap; // 商品查詢表
    private int k;                           // 保留的商品數量
    
    /**
     * 建構子
     * @param k 要保留的商品數量
     */
    public TopSellingProducts(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("K 必須大於 0");
        }
        this.k = k;
        this.productMap = new HashMap<>();
        
        // 最小堆：銷售額小的優先（堆頂是最小銷售額）
        // 銷售額相同時，id 字母順序較大的優先（這樣可以保留較小的 id）
        Comparator<Product> comparator = Comparator
            .comparingLong(Product::getSales)
            .thenComparing(Product::getId, Comparator.reverseOrder());
        
        this.minHeap = new PriorityQueue<>(comparator);
    }
    
    /**
     * 新增或更新商品銷售額
     * @param id 商品編號
     * @param sales 銷售額（必須 >= 0）
     */
    public void addSales(String id, long sales) {
        if (sales < 0) {
            throw new IllegalArgumentException("銷售額不能為負數");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("商品編號不能為空");
        }
        
        // 如果商品已存在，合併銷售額
        if (productMap.containsKey(id)) {
            Product existing = productMap.get(id);
            // 從堆中移除舊的（需要重建堆）
            minHeap.remove(existing);
            existing.addSales(sales);
            minHeap.offer(existing);
            
            System.out.printf("📝 更新商品: %s (新增銷售額 %,d，總計 %,d)%n",
                             id, sales, existing.getSales());
        } else {
            // 新商品
            Product newProduct = new Product(id, sales);
            
            // 如果堆未滿，直接加入
            if (minHeap.size() < k) {
                productMap.put(id, newProduct);
                minHeap.offer(newProduct);
                System.out.printf("✅ 新增商品: %s (銷售額 %,d)%n", id, sales);
            } 
            // 如果堆已滿，檢查是否需要替換
            else if (shouldReplace(newProduct)) {
                Product removed = minHeap.poll();
                productMap.remove(removed.getId());
                
                productMap.put(id, newProduct);
                minHeap.offer(newProduct);
                
                System.out.printf("🔄 替換商品: %s (銷售額 %,d) → %s (銷售額 %,d)%n",
                                 removed.getId(), removed.getSales(), id, sales);
            } else {
                System.out.printf("⏭️  商品 %s (銷售額 %,d) 未進入 Top-%d%n", id, sales, k);
            }
        }
    }
    
    /**
     * 判斷新商品是否應該替換堆頂商品
     */
    private boolean shouldReplace(Product newProduct) {
        Product lowest = minHeap.peek();
        
        // 銷售額比較
        if (newProduct.getSales() > lowest.getSales()) {
            return true;
        }
        
        // 銷售額相同，比較 id（字母順序較小的優先）
        if (newProduct.getSales() == lowest.getSales()) {
            return newProduct.getId().compareTo(lowest.getId()) < 0;
        }
        
        return false;
    }
    
    /**
     * 批量新增商品銷售額
     * @param salesData 商品銷售資料陣列
     */
    public void addSales(SalesData... salesData) {
        for (SalesData data : salesData) {
            addSales(data.id, data.sales);
        }
    }
    
    /**
     * 批量新增商品銷售額（Map 格式）
     * @param salesMap 商品編號 -> 銷售額 的 Map
     */
    public void addSales(Map<String, Long> salesMap) {
        for (Map.Entry<String, Long> entry : salesMap.entrySet()) {
            addSales(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * 取得 Top-K 熱門商品列表
     * @return 依銷售額降序排列的商品列表
     */
    public List<Product> getTopK() {
        List<Product> result = new ArrayList<>(minHeap);
        
        // 排序：銷售額降序，銷售額相同時 id 字母升序
        Comparator<Product> comparator = Comparator
            .comparingLong(Product::getSales)
            .reversed()
            .thenComparing(Product::getId);
        
        result.sort(comparator);
        return result;
    }
    
    /**
     * 取得目前追蹤的商品數量
     */
    public int size() {
        return minHeap.size();
    }
    
    /**
     * 檢查是否為空
     */
    public boolean isEmpty() {
        return minHeap.isEmpty();
    }
    
    /**
     * 取得 K 值
     */
    public int getK() {
        return k;
    }
    
    /**
     * 顯示 Top-K 商品
     */
    public void printTopK() {
        List<Product> topProducts = getTopK();
        
        if (topProducts.isEmpty()) {
            System.out.println("目前沒有商品資料");
            return;
        }
        
        System.out.println("\n=== Top-" + k + " 熱門商品 ===");
        System.out.println("排名 | 商品編號 | 銷售額");
        System.out.println("------|----------|------------");
        
        int rank = 1;
        for (Product product : topProducts) {
            System.out.printf("%4d  | %-8s | %,10d%n",
                             rank++, product.getId(), product.getSales());
        }
        System.out.println();
    }
    
    /**
     * 顯示詳細資訊（包含堆狀態）
     */
    public void showStatus() {
        System.out.println("\n=== Top-K 熱門商品狀態 ===");
        System.out.println("K 值: " + k);
        System.out.println("目前商品數: " + minHeap.size());
        
        if (!minHeap.isEmpty()) {
            Product lowest = minHeap.peek();
            System.out.println("堆頂 (最小銷售額): " + lowest.toShortString());
            System.out.println("最小堆內容 (銷售額升序):");
            
            // 建立暫時堆來顯示
            PriorityQueue<Product> tempHeap = new PriorityQueue<>(minHeap);
            int count = 0;
            while (!tempHeap.isEmpty()) {
                Product p = tempHeap.poll();
                System.out.printf("  %d. %s%n", ++count, p.toShortString());
            }
        }
        
        System.out.println();
    }
    
    /**
     * 重置追蹤器
     */
    public void clear() {
        minHeap.clear();
        productMap.clear();
        System.out.println("🔄 已清除所有商品資料");
    }
    
    /**
     * 銷售資料輔助類
     */
    public static class SalesData {
        public String id;
        public long sales;
        
        public SalesData(String id, long sales) {
            this.id = id;
            this.sales = sales;
        }
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== Top-K 熱門商品系統測試 ===\n");
        
        // 測試 1：基本功能測試
        testBasicFunctionality();
        
        // 測試 2：重複商品合併
        testDuplicateMerging();
        
        // 測試 3：銷售額相同時 ID 排序
        testTieBreaker();
        
        // 測試 4：邊界情況
        testEdgeCases();
        
        // 測試 5：大量資料測試
        testLargeData();
        
        // 測試 6：綜合場景測試
        testComprehensiveScenario();
    }
    
    /**
     * 測試基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- 測試 1: 基本功能測試 (K=3) ---");
        
        TopSellingProducts tracker = new TopSellingProducts(3);
        
        System.out.println("新增商品銷售額:");
        tracker.addSales("A001", 1000);
        tracker.addSales("B002", 2500);
        tracker.addSales("C003", 1500);
        tracker.addSales("D004", 3000);
        tracker.addSales("E005", 2000);
        
        tracker.printTopK();
        tracker.showStatus();
    }
    
    /**
     * 測試重複商品合併
     */
    private static void testDuplicateMerging() {
        System.out.println("--- 測試 2: 重複商品合併 (K=3) ---");
        
        TopSellingProducts tracker = new TopSellingProducts(3);
        
        System.out.println("新增商品銷售額:");
        tracker.addSales("A001", 1000);
        tracker.addSales("B002", 2000);
        tracker.addSales("A001", 1500);  // 重複 A001
        tracker.addSales("C003", 2500);
        tracker.addSales("A001", 500);   // 重複 A001
        tracker.addSales("D004", 1800);
        
        tracker.printTopK();
        System.out.println();
    }
    
    /**
     * 測試銷售額相同時 ID 排序
     */
    private static void testTieBreaker() {
        System.out.println("--- 測試 3: 銷售額相同時 ID 排序 (K=3) ---");
        
        TopSellingProducts tracker = new TopSellingProducts(3);
        
        System.out.println("新增商品銷售額 (銷售額相同):");
        tracker.addSales("Z001", 1000);
        tracker.addSales("A002", 1000);
        tracker.addSales("M003", 1000);
        tracker.addSales("B004", 1000);
        tracker.addSales("C005", 1000);
        
        tracker.printTopK();
        System.out.println("預期結果: ID 順序 A002, B004, C005 (字母順序優先)");
        System.out.println();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 4: 邊界情況測試 ---");
        
        // 測試 4.1: K=1
        System.out.println("測試 4.1: K=1 (只保留第一名)");
        TopSellingProducts tracker1 = new TopSellingProducts(1);
        tracker1.addSales("P001", 500);
        tracker1.addSales("P002", 300);
        tracker1.addSales("P003", 700);
        tracker1.addSales("P004", 400);
        tracker1.printTopK();
        System.out.println();
        
        // 測試 4.2: 空追蹤器
        System.out.println("測試 4.2: 空追蹤器");
        TopSellingProducts tracker2 = new TopSellingProducts(3);
        tracker2.printTopK();
        tracker2.showStatus();
        System.out.println();
        
        // 測試 4.3: 大量重複更新
        System.out.println("測試 4.3: 大量重複更新");
        TopSellingProducts tracker3 = new TopSellingProducts(2);
        tracker3.addSales("A001", 100);
        tracker3.addSales("A001", 200);
        tracker3.addSales("A001", 300);
        tracker3.addSales("B002", 150);
        tracker3.addSales("C003", 250);
        tracker3.printTopK();
        System.out.println();
    }
    
    /**
     * 測試大量資料
     */
    private static void testLargeData() {
        System.out.println("--- 測試 5: 大量資料測試 (K=5) ---");
        
        TopSellingProducts tracker = new TopSellingProducts(5);
        Random random = new Random(42);
        
        System.out.println("新增 20 筆隨機商品銷售額:");
        for (int i = 0; i < 20; i++) {
            String id = String.format("P%03d", random.nextInt(15) + 1);
            long sales = random.nextInt(5000) + 100;
            tracker.addSales(id, sales);
        }
        
        tracker.printTopK();
        tracker.showStatus();
        
        // 驗證結果正確性
        List<Product> topProducts = tracker.getTopK();
        boolean isCorrect = true;
        for (int i = 0; i < topProducts.size() - 1; i++) {
            if (topProducts.get(i).getSales() < topProducts.get(i + 1).getSales()) {
                isCorrect = false;
                break;
            }
            if (topProducts.get(i).getSales() == topProducts.get(i + 1).getSales() &&
                topProducts.get(i).getId().compareTo(topProducts.get(i + 1).getId()) > 0) {
                isCorrect = false;
                break;
            }
        }
        System.out.println("排序正確性驗證: " + (isCorrect ? "✓ PASS" : "✗ FAIL"));
        System.out.println();
    }
    
    /**
     * 測試綜合場景
     */
    private static void testComprehensiveScenario() {
        System.out.println("--- 測試 6: 綜合場景測試 (K=4) ---");
        
        TopSellingProducts tracker = new TopSellingProducts(4);
        
        // 使用 SalesData 批量新增
        TopSellingProducts.SalesData[] salesData = {
            new TopSellingProducts.SalesData("A001", 1200),
            new TopSellingProducts.SalesData("B002", 800),
            new TopSellingProducts.SalesData("C003", 1500),
            new TopSellingProducts.SalesData("D004", 600),
            new TopSellingProducts.SalesData("E005", 2000),
            new TopSellingProducts.SalesData("A001", 500),  // 重複
            new TopSellingProducts.SalesData("F006", 900),
            new TopSellingProducts.SalesData("G007", 300),
            new TopSellingProducts.SalesData("B002", 700),  // 重複
            new TopSellingProducts.SalesData("H008", 1800),
            new TopSellingProducts.SalesData("I009", 400),
            new TopSellingProducts.SalesData("J010", 2500),
        };
        
        System.out.println("批次新增 12 筆銷售資料:");
        tracker.addSales(salesData);
        
        tracker.printTopK();
        
        // 顯示詳細資訊
        System.out.println("詳細商品資訊:");
        List<Product> topProducts = tracker.getTopK();
        for (Product product : topProducts) {
            System.out.println("  " + product);
        }
        System.out.println();
        
        // 測試完整流程
        System.out.println("完整流程測試 (新增、更新、查詢):");
        System.out.println("新增 K001 銷售額 1600");
        tracker.addSales("K001", 1600);
        tracker.printTopK();
        
        System.out.println("更新 E005 銷售額 +1000");
        tracker.addSales("E005", 1000);
        tracker.printTopK();
        
        System.out.println("更新 A001 銷售額 +2000");
        tracker.addSales("A001", 2000);
        tracker.printTopK();
        
        // 顯示最終狀態
        tracker.showStatus();
    }
}