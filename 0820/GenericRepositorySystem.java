import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品類別（用於測試 Repository<Product>）
 */
class Product {
    private final String id;
    private final String name;
    private final double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', price=%.1f}", id, name, price);
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
 * 通用儲存庫類別
 * 使用泛型，內部以 ArrayList 儲存資料
 * 
 * @param <T> 儲存資料的型態
 */
class Repository<T> {
    private final List<T> data;
    private int nextId;  // 用於產生唯一識別碼

    /**
     * 建構子 - 初始化儲存庫
     */
    public Repository() {
        this.data = new ArrayList<>();
        this.nextId = 1;
    }

    /**
     * 新增資料到儲存庫
     * 
     * @param item 要新增的資料（不可為 null）
     * @return 新增成功回傳 true，失敗回傳 false
     */
    public boolean add(T item) {
        if (item == null) {
            System.out.println("⚠️ 警告：無法新增 null 資料");
            return false;
        }
        data.add(item);
        nextId++;
        return true;
    }

    /**
     * 根據索引取得資料
     * 
     * @param index 索引位置（從 0 開始）
     * @return 該位置的資料，若索引非法則回傳 null
     */
    public T get(int index) {
        if (index < 0 || index >= data.size()) {
            System.out.println("⚠️ 警告：索引 " + index + " 超出範圍 [0, " + (data.size() - 1) + "]");
            return null;
        }
        return data.get(index);
    }

    /**
     * 根據索引移除資料
     * 
     * @param index 索引位置（從 0 開始）
     * @return 被移除的資料，若索引非法則回傳 null
     */
    public T remove(int index) {
        if (index < 0 || index >= data.size()) {
            System.out.println("⚠️ 警告：索引 " + index + " 超出範圍 [0, " + (data.size() - 1) + "]");
            return null;
        }
        return data.remove(index);
    }

    /**
     * 根據物件移除資料（第一次出現）
     * 
     * @param item 要移除的資料
     * @return 移除成功回傳 true，失敗回傳 false
     */
    public boolean remove(T item) {
        if (item == null) {
            System.out.println("⚠️ 警告：無法移除 null 資料");
            return false;
        }
        return data.remove(item);
    }

    /**
     * 取得儲存庫的大小
     * 
     * @return 儲存庫中的資料筆數
     */
    public int size() {
        return data.size();
    }

    /**
     * 檢查儲存庫是否為空
     * 
     * @return 空回傳 true，否則回傳 false
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * 清空儲存庫
     */
    public void clear() {
        data.clear();
        nextId = 1;
        System.out.println("✅ 儲存庫已清空");
    }

    /**
     * 檢查是否包含特定資料
     * 
     * @param item 要檢查的資料
     * @return 包含回傳 true，否則回傳 false
     */
    public boolean contains(T item) {
        return data.contains(item);
    }

    /**
     * 取得所有資料的副本（保護內部資料）
     * 
     * @return 所有資料的列表副本
     */
    public List<T> getAll() {
        return new ArrayList<>(data);
    }

    /**
     * 完整輸出儲存庫內容
     */
    public void printAll() {
        if (data.isEmpty()) {
            System.out.println("📭 儲存庫為空（筆數：0）");
            return;
        }

        System.out.println("📋 儲存庫內容（筆數：" + data.size() + "）");
        System.out.println("┌────┬──────────────────────────────────────────────┐");
        for (int i = 0; i < data.size(); i++) {
            T item = data.get(i);
            System.out.printf("│ %2d │ %-44s │%n", i, item.toString());
        }
        System.out.println("└────┴──────────────────────────────────────────────┘");
    }

    /**
     * 輸出儲存庫摘要資訊
     */
    public void printSummary() {
        System.out.println("📊 儲存庫摘要");
        System.out.println("  ├─ 資料型態： " + getTypeName());
        System.out.println("  ├─ 資料筆數： " + size());
        System.out.println("  ├─ 是否為空： " + (isEmpty() ? "是" : "否"));
        System.out.println("  └─ 下一個 ID： " + nextId);
    }

    /**
     * 取得儲存庫中資料的型態名稱
     */
    private String getTypeName() {
        if (data.isEmpty()) {
            return "未知（儲存庫為空）";
        }
        return data.get(0).getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return String.format("Repository{size=%d, type=%s}", 
                size(), data.isEmpty() ? "未知" : data.get(0).getClass().getSimpleName());
    }
}

/**
 * 主程式 - 測試通用儲存庫系統
 */
public class GenericRepositorySystem {

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║     通用儲存庫系統 - 測試程式                        ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");

        // ========== 測試 Repository<String> ==========
        System.out.println("\n【測試一：Repository<String> 字串儲存庫】");
        testStringRepository();

        // ========== 測試 Repository<Product> ==========
        System.out.println("\n\n【測試二：Repository<Product> 商品儲存庫】");
        testProductRepository();

        // ========== 邊界情況測試 ==========
        System.out.println("\n\n【測試三：邊界情況測試】");
        testEdgeCases();

        // ========== 總結對照表 ==========
        printSummaryTable();
    }

    /**
     * 測試 Repository<String>
     */
    private static void testStringRepository() {
        // 建立字串儲存庫
        Repository<String> stringRepo = new Repository<>();

        System.out.println("▶ 步驟 1：建立儲存庫並加入資料");
        stringRepo.add("Java 程式設計");
        stringRepo.add("Python 入門");
        stringRepo.add("資料結構與演算法");
        stringRepo.add("資料庫系統");
        stringRepo.add("網頁開發技術");
        stringRepo.printAll();

        System.out.println("\n▶ 步驟 2：根據索引取得資料");
        System.out.println("  索引 0 的資料： " + stringRepo.get(0));
        System.out.println("  索引 2 的資料： " + stringRepo.get(2));
        System.out.println("  索引 5 的資料（超出範圍）： " + stringRepo.get(5));

        System.out.println("\n▶ 步驟 3：根據索引移除資料");
        String removed = stringRepo.remove(2);
        System.out.println("  移除索引 2 的資料： " + removed);
        stringRepo.printAll();

        System.out.println("\n▶ 步驟 4：根據物件移除資料");
        boolean isRemoved = stringRepo.remove("資料庫系統");
        System.out.println("  移除 '資料庫系統'： " + (isRemoved ? "✅ 成功" : "❌ 失敗"));
        stringRepo.printAll();

        System.out.println("\n▶ 步驟 5：檢查包含性");
        System.out.println("  是否包含 'Python 入門'： " + stringRepo.contains("Python 入門"));
        System.out.println("  是否包含 '資料庫系統'： " + stringRepo.contains("資料庫系統"));

        System.out.println("\n▶ 步驟 6：取得所有資料（副本）");
        List<String> allData = stringRepo.getAll();
        System.out.println("  副本內容： " + allData);
        System.out.println("  副本型態： " + allData.getClass().getSimpleName());

        System.out.println("\n▶ 步驟 7：儲存庫摘要");
        stringRepo.printSummary();

        System.out.println("\n▶ 步驟 8：清空儲存庫");
        stringRepo.clear();
        System.out.println("  清空後大小： " + stringRepo.size());
        stringRepo.printAll();
    }

    /**
     * 測試 Repository<Product>
     */
    private static void testProductRepository() {
        // 建立商品儲存庫
        Repository<Product> productRepo = new Repository<>();

        System.out.println("▶ 步驟 1：建立儲存庫並加入商品資料");
        productRepo.add(new Product("P001", "機械鍵盤", 1200.0));
        productRepo.add(new Product("P002", "藍牙耳機", 800.0));
        productRepo.add(new Product("P003", "無線滑鼠", 500.0));
        productRepo.add(new Product("P004", "電競螢幕", 2500.0));
        productRepo.add(new Product("P005", "USB 集線器", 300.0));
        productRepo.printAll();

        System.out.println("\n▶ 步驟 2：根據索引取得資料");
        Product p = productRepo.get(1);
        System.out.println("  索引 1 的商品： " + p);
        System.out.println("  商品名稱： " + p.getName());
        System.out.println("  商品價格： " + p.getPrice());

        System.out.println("\n▶ 步驟 3：根據索引移除資料");
        Product removed = productRepo.remove(3);
        System.out.println("  移除索引 3 的商品： " + removed);
        productRepo.printAll();

        System.out.println("\n▶ 步驟 4：根據物件移除資料");
        Product target = new Product("P002", "藍牙耳機", 800.0);
        boolean isRemoved = productRepo.remove(target);
        System.out.println("  移除商品 " + target + "： " + (isRemoved ? "✅ 成功" : "❌ 失敗"));
        productRepo.printAll();

        System.out.println("\n▶ 步驟 5：檢查包含性");
        Product check1 = new Product("P003", "無線滑鼠", 500.0);
        Product check2 = new Product("P006", "行動電源", 600.0);
        System.out.println("  是否包含 " + check1 + "： " + productRepo.contains(check1));
        System.out.println("  是否包含 " + check2 + "： " + productRepo.contains(check2));

        System.out.println("\n▶ 步驟 6：取得所有資料（副本）");
        List<Product> allProducts = productRepo.getAll();
        System.out.println("  副本內容：");
        for (int i = 0; i < allProducts.size(); i++) {
            System.out.println("    " + i + ". " + allProducts.get(i));
        }

        System.out.println("\n▶ 步驟 7：儲存庫摘要");
        productRepo.printSummary();

        System.out.println("\n▶ 步驟 8：清空儲存庫");
        productRepo.clear();
        System.out.println("  清空後大小： " + productRepo.size());
        productRepo.printAll();
    }

    /**
     * 邊界情況測試
     */
    private static void testEdgeCases() {
        System.out.println("▶ 測試 1：新增 null 資料");
        Repository<String> repo = new Repository<>();
        repo.add("正常資料");
        boolean result = repo.add(null);
        System.out.println("  新增 null 結果： " + (result ? "✅ 成功" : "❌ 失敗"));
        repo.printAll();

        System.out.println("\n▶ 測試 2：移除 null 資料");
        result = repo.remove((String) null);
        System.out.println("  移除 null 結果： " + (result ? "✅ 成功" : "❌ 失敗"));

        System.out.println("\n▶ 測試 3：從空儲存庫取得資料");
        Repository<String> emptyRepo = new Repository<>();
        String data = emptyRepo.get(0);
        System.out.println("  從空儲存庫取得索引 0： " + data);

        System.out.println("\n▶ 測試 4：從空儲存庫移除資料");
        data = emptyRepo.remove(0);
        System.out.println("  從空儲存庫移除索引 0： " + data);

        System.out.println("\n▶ 測試 5：空儲存庫的完整輸出");
        emptyRepo.printAll();

        System.out.println("\n▶ 測試 6：儲存庫之間的獨立性");
        Repository<String> repo1 = new Repository<>();
        Repository<String> repo2 = new Repository<>();
        repo1.add("repo1 的資料");
        repo2.add("repo2 的資料");
        System.out.println("  repo1 大小： " + repo1.size());
        System.out.println("  repo2 大小： " + repo2.size());
        System.out.println("  repo1 內容： " + repo1.getAll());
        System.out.println("  repo2 內容： " + repo2.getAll());
        System.out.println("  ✅ 兩個儲存庫各自獨立，互不影響");
    }

    /**
     * 印出總結對照表
     */
    private static void printSummaryTable() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                   方法功能總結對照表                     ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  方法        │  功能說明                     │  回傳值   ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║  add(T)     │  新增資料到儲存庫             │  boolean  ║");
        System.out.println("║  get(int)   │  根據索引取得資料             │  T        ║");
        System.out.println("║  remove(int)│  根據索引移除資料             │  T        ║");
        System.out.println("║  remove(T)  │  根據物件移除資料             │  boolean  ║");
        System.out.println("║  size()     │  取得儲存庫大小               │  int      ║");
        System.out.println("║  isEmpty()  │  檢查是否為空                 │  boolean  ║");
        System.out.println("║  clear()    │  清空儲存庫                   │  void     ║");
        System.out.println("║  contains(T)│  檢查是否包含特定資料         │  boolean  ║");
        System.out.println("║  getAll()   │  取得所有資料（副本）         │  List<T>  ║");
        System.out.println("║  printAll() │  完整輸出儲存庫內容           │  void     ║");
        System.out.println("║  printSummary()│  輸出儲存庫摘要資訊        │  void     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
}