/**
 * 檔名：Q01_InventoryItem.java
 * 功能：封裝的庫存項目類別
 * 說明：id 與 name 建立後不可更改，庫存量只能透過指定方法修改
 */

public class Q01_InventoryItem {
    // ========== 私有欄位（封裝） ==========
    private final String id;      // 商品編號（不可變）
    private final String name;    // 商品名稱（不可變）
    private int stock;            // 庫存量（只能透過方法修改）

    /**
     * 建構子
     * @param id 商品編號
     * @param name 商品名稱
     * @param stock 初始庫存量
     * @throws IllegalArgumentException 當 id 或 name 為 null 或為空白字串時
     */
    public Q01_InventoryItem(String id, String name, int stock) {
        // 1. 處理 id：去除前後空白，檢查是否為 null 或空字串
        String trimmedId = (id != null) ? id.trim() : null;
        if (trimmedId == null || trimmedId.isEmpty()) {
            throw new IllegalArgumentException("id 不能為 null 或空白字串");
        }

        // 2. 處理 name：去除前後空白，檢查是否為 null 或空字串
        String trimmedName = (name != null) ? name.trim() : null;
        if (trimmedName == null || trimmedName.isEmpty()) {
            throw new IllegalArgumentException("name 不能為 null 或空白字串");
        }

        // 3. 儲存去除前後空白後的內容
        this.id = trimmedId;
        this.name = trimmedName;

        // 4. stock 小於 0 時以 0 儲存
        this.stock = Math.max(stock, 0);
    }

    /**
     * 取得目前庫存量
     * @return 目前庫存量
     */
    public int getStock() {
        return stock;
    }

    /**
     * 補貨（增加庫存）
     * @param amount 補貨數量
     * @return true 表示補貨成功（amount > 0），false 表示補貨失敗
     */
    public boolean restock(int amount) {
        // 只有在 amount > 0 時增加庫存並回傳 true
        if (amount > 0) {
            stock += amount;
            return true;
        }
        // 否則不修改並回傳 false
        return false;
    }

    /**
     * 銷售（扣減庫存）
     * @param amount 銷售數量
     * @return true 表示銷售成功（amount > 0 且庫存足夠），false 表示銷售失敗
     */
    public boolean sell(int amount) {
        // 只有在 amount > 0 且庫存足夠時扣除並回傳 true
        if (amount > 0 && stock >= amount) {
            stock -= amount;
            return true;
        }
        // 否則不修改並回傳 false
        return false;
    }

    /**
     * 取得商品狀態字串
     * @return 格式為 "id|name|stock"
     */
    public String status() {
        return id + "|" + name + "|" + stock;
    }

    // ========== 額外的 Getter 方法（方便使用，但題目未強制要求） ==========
    
    /**
     * 取得商品編號
     * @return 商品編號
     */
    public String getId() {
        return id;
    }

    /**
     * 取得商品名稱
     * @return 商品名稱
     */
    public String getName() {
        return name;
    }

    // ========== 測試主程式（可選） ==========
    public static void main(String[] args) {
        // 測試範例
        System.out.println("===== 測試範例 =====");
        Q01_InventoryItem item = new Q01_InventoryItem("F100", "Keyboard", 5);
        System.out.println(item.restock(3));   // true
        System.out.println(item.sell(6));      // false（庫存不足）
        System.out.println(item.sell(3));      // true
        System.out.println(item.status());     // F100|Keyboard|2
        System.out.println();

        // 測試邊界情況
        System.out.println("===== 邊界情況測試 =====");
        
        // 測試 stock 小於 0
        Q01_InventoryItem item2 = new Q01_InventoryItem("F101", "Mouse", -5);
        System.out.println("stock 應為 0：" + item2.getStock());  // 0
        
        // 測試 restock 參數 <= 0
        System.out.println("restock(0) 應回傳 false：" + item2.restock(0));  // false
        System.out.println("restock(-3) 應回傳 false：" + item2.restock(-3)); // false
        System.out.println("stock 仍為 0：" + item2.getStock());  // 0
        
        // 測試 sell 參數 <= 0
        System.out.println("sell(0) 應回傳 false：" + item2.sell(0));   // false
        System.out.println("sell(-2) 應回傳 false：" + item2.sell(-2));  // false
        System.out.println("stock 仍為 0：" + item2.getStock());  // 0
        
        // 測試 sell 庫存不足
        System.out.println("sell(5) 應回傳 false：" + item2.sell(5));   // false
        System.out.println("stock 仍為 0：" + item2.getStock());  // 0
        
        System.out.println();
        
        // 測試正常流程
        System.out.println("===== 正常流程測試 =====");
        Q01_InventoryItem item3 = new Q01_InventoryItem("F102", "Monitor", 10);
        System.out.println("初始狀態：" + item3.status());  // F102|Monitor|10
        System.out.println("補貨 5：" + item3.restock(5));   // true
        System.out.println("補貨後：" + item3.status());     // F102|Monitor|15
        System.out.println("銷售 7：" + item3.sell(7));      // true
        System.out.println("銷售後：" + item3.status());     // F102|Monitor|8
        System.out.println("銷售 10（庫存不足）：" + item3.sell(10)); // false
        System.out.println("最終狀態：" + item3.status());   // F102|Monitor|8
        System.out.println();

        // 測試建構子驗證
        System.out.println("===== 建構子驗證測試 =====");
        try {
            Q01_InventoryItem item4 = new Q01_InventoryItem(null, "Product", 5);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ id 為 null 時拋出例外：" + e.getMessage());
        }

        try {
            Q01_InventoryItem item5 = new Q01_InventoryItem("", "Product", 5);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ id 為空字串時拋出例外：" + e.getMessage());
        }

        try {
            Q01_InventoryItem item6 = new Q01_InventoryItem("  ", "Product", 5);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ id 為空白字串時拋出例外：" + e.getMessage());
        }

        try {
            Q01_InventoryItem item7 = new Q01_InventoryItem("F103", null, 5);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ name 為 null 時拋出例外：" + e.getMessage());
        }

        try {
            Q01_InventoryItem item8 = new Q01_InventoryItem("F103", "   ", 5);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ name 為空白字串時拋出例外：" + e.getMessage());
        }

        System.out.println();
        System.out.println("===== 去除前後空白測試 =====");
        Q01_InventoryItem item9 = new Q01_InventoryItem("  F104  ", "  Product  ", 5);
        System.out.println("id 應去除空白：" + item9.getId());     // F104
        System.out.println("name 應去除空白：" + item9.getName()); // Product
        System.out.println("status：" + item9.status());           // F104|Product|5
    }
}