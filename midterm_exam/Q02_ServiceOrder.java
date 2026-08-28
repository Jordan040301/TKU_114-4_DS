/**
 * 檔名：Q02_ServiceOrder.java
 * 功能：維修訂單（使用 Composition 管理多個維修項目）
 * 說明：訂單包含多個 LineItem，負責保存項目並計算總額
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Q02_ServiceOrder {
    
    // ========== 內部類別：維修項目（LineItem） ==========
    public static class LineItem {
        private final String name;       // 項目名稱（不可變）
        private final int unitPrice;     // 單價（不可變）
        private final int quantity;      // 數量（不可變）

        /**
         * 建構子
         * @param name 項目名稱
         * @param unitPrice 單價
         * @param quantity 數量
         */
        public LineItem(String name, int unitPrice, int quantity) {
            this.name = name;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        /**
         * 取得項目名稱
         * @return 項目名稱
         */
        public String getName() {
            return name;
        }

        /**
         * 取得單價
         * @return 單價
         */
        public int getUnitPrice() {
            return unitPrice;
        }

        /**
         * 取得數量
         * @return 數量
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * 計算小計（單價 × 數量）
         * @return 小計金額
         */
        public int subtotal() {
            return unitPrice * quantity;
        }
    }

    // ========== Q02_ServiceOrder 的欄位 ==========
    private final String orderId;              // 訂單編號（不可變）
    private final List<LineItem> items;        // 維修項目列表（組合）

    // ========== 建構子 ==========
    
    /**
     * 建構子
     * @param orderId 訂單編號
     * @throws IllegalArgumentException 當 orderId 為 null 或空白時
     */
    public Q02_ServiceOrder(String orderId) {
        // orderId 不得為 null 或 blank，否則丟出 IllegalArgumentException
        String trimmedOrderId = (orderId != null) ? orderId.trim() : null;
        if (trimmedOrderId == null || trimmedOrderId.isEmpty()) {
            throw new IllegalArgumentException("orderId 不能為 null 或空白字串");
        }
        this.orderId = trimmedOrderId;
        this.items = new ArrayList<>();
    }

    // ========== 主要方法 ==========

    /**
     * 新增維修項目
     * @param name 項目名稱
     * @param unitPrice 單價
     * @param quantity 數量
     * @return true 表示新增成功，false 表示參數不合法
     */
    public boolean addItem(String name, int unitPrice, int quantity) {
        // 驗證：項目名稱不得為 null 或 blank
        String trimmedName = (name != null) ? name.trim() : null;
        if (trimmedName == null || trimmedName.isEmpty()) {
            return false;
        }

        // 驗證：unitPrice 不得小於 0
        if (unitPrice < 0) {
            return false;
        }

        // 驗證：quantity 必須大於 0
        if (quantity <= 0) {
            return false;
        }

        // 所有驗證通過，新增項目
        LineItem item = new LineItem(trimmedName, unitPrice, quantity);
        items.add(item);
        return true;
    }

    /**
     * 取得項目總數
     * @return 項目數量
     */
    public int itemCount() {
        return items.size();
    }

    /**
     * 計算訂單總金額（所有項目小計的總和）
     * @return 總金額
     */
    public int totalAmount() {
        int total = 0;
        for (LineItem item : items) {
            total += item.subtotal();
        }
        return total;
    }

    /**
     * 回傳小計最高的項目名稱
     * @return 小計最高的項目名稱；平手時回傳較早加入者；沒有項目時回傳空字串
     */
    public String largestItemName() {
        if (items.isEmpty()) {
            return "";
        }

        LineItem largest = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            LineItem current = items.get(i);
            // 只有當 current 的小計嚴格大於 largest 時才更新（保留較早加入者）
            if (current.subtotal() > largest.subtotal()) {
                largest = current;
            }
        }
        return largest.getName();
    }

    /**
     * 回傳所有項目的摘要列表（依加入順序）
     * 格式：name: subtotal
     * @return 不可修改的摘要列表（防止外部修改訂單內部資料）
     */
    public List<String> itemSummaries() {
        List<String> summaries = new ArrayList<>();
        for (LineItem item : items) {
            summaries.add(item.getName() + ": " + item.subtotal());
        }
        // 回傳不可修改的 List，防止 caller 透過回傳的 List 修改訂單內部資料
        return Collections.unmodifiableList(summaries);
    }

    // ========== 額外輔助方法（方便測試，非必要） ==========

    /**
     * 取得訂單編號
     * @return 訂單編號
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 取得所有項目的原始列表（僅供內部使用，不公開）
     * 注意：此方法不回傳給外部，確保封裝性
     */
    private List<LineItem> getItems() {
        return items;
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試範例 =====");
        Q02_ServiceOrder order = new Q02_ServiceOrder("R-01");
        order.addItem("Inspection", 300, 1);
        order.addItem("Cable", 80, 4);
        order.addItem("Cleaning", 200, 1);
        
        System.out.println(order.itemCount());          // 3
        System.out.println(order.totalAmount());        // 820
        System.out.println(order.largestItemName());    // Cable
        System.out.println(order.itemSummaries());      // [Inspection: 300, Cable: 320, Cleaning: 200]
        System.out.println();

        // ===== 測試建構子驗證 =====
        System.out.println("===== 建構子驗證測試 =====");
        try {
            Q02_ServiceOrder order2 = new Q02_ServiceOrder(null);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ orderId 為 null 時拋出例外：" + e.getMessage());
        }

        try {
            Q02_ServiceOrder order3 = new Q02_ServiceOrder("");
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ orderId 為空字串時拋出例外：" + e.getMessage());
        }

        try {
            Q02_ServiceOrder order4 = new Q02_ServiceOrder("   ");
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ orderId 為空白字串時拋出例外：" + e.getMessage());
        }
        System.out.println();

        // ===== 測試 addItem 驗證 =====
        System.out.println("===== addItem 驗證測試 =====");
        Q02_ServiceOrder order5 = new Q02_ServiceOrder("R-02");
        
        // 測試名稱無效
        System.out.println("addItem(null, 100, 1) → " + order5.addItem(null, 100, 1));     // false
        System.out.println("addItem(\"\", 100, 1) → " + order5.addItem("", 100, 1));       // false
        System.out.println("addItem(\"  \", 100, 1) → " + order5.addItem("  ", 100, 1));   // false
        
        // 測試單價無效
        System.out.println("addItem(\"Test\", -1, 1) → " + order5.addItem("Test", -1, 1)); // false
        
        // 測試數量無效
        System.out.println("addItem(\"Test\", 100, 0) → " + order5.addItem("Test", 100, 0)); // false
        System.out.println("addItem(\"Test\", 100, -1) → " + order5.addItem("Test", 100, -1)); // false
        
        // 測試合法新增
        System.out.println("addItem(\"Test\", 100, 2) → " + order5.addItem("Test", 100, 2)); // true
        System.out.println("項目數量：" + order5.itemCount()); // 1
        System.out.println();

        // ===== 測試 largestItemName 平手情況 =====
        System.out.println("===== largestItemName 平手測試 =====");
        Q02_ServiceOrder order6 = new Q02_ServiceOrder("R-03");
        order6.addItem("A", 100, 2);   // 小計 200
        order6.addItem("B", 50, 4);    // 小計 200（平手）
        order6.addItem("C", 100, 3);   // 小計 300
        System.out.println("項目列表：" + order6.itemSummaries());
        System.out.println("最大項目（平手時回傳較早加入者）：" + order6.largestItemName()); // A
        System.out.println();

        // ===== 測試 empty 情況 =====
        System.out.println("===== 空訂單測試 =====");
        Q02_ServiceOrder order7 = new Q02_ServiceOrder("R-04");
        System.out.println("itemCount()：" + order7.itemCount());           // 0
        System.out.println("totalAmount()：" + order7.totalAmount());       // 0
        System.out.println("largestItemName()：" + order7.largestItemName()); // ""
        System.out.println("itemSummaries()：" + order7.itemSummaries());   // []
        System.out.println();

        // ===== 測試防止外部修改 =====
        System.out.println("===== 防止外部修改測試 =====");
        Q02_ServiceOrder order8 = new Q02_ServiceOrder("R-05");
        order8.addItem("Security", 100, 1);
        List<String> summaries = order8.itemSummaries();
        System.out.println("原始摘要：" + summaries);
        
        try {
            summaries.add("Hacked: 999");
            System.out.println("不應該執行到這裡");
        } catch (UnsupportedOperationException e) {
            System.out.println("✅ 無法透過回傳的 List 修改內部資料（UnsupportedOperationException）");
        }
        System.out.println("嘗試修改後，原始摘要不變：" + order8.itemSummaries());
    }
}