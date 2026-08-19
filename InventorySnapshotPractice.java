import java.util.Arrays;

public class InventorySnapshotPractice {
    
    public static void main(String[] args) {
        System.out.println("========== 建立庫存快照 ==========");
        
        // 測試正常情況
        int[] quantities = {5, 0, 3, 0};
        InventorySnapshot snapshot1 = new InventorySnapshot("WH001", quantities);
        
        System.out.println("倉庫 ID：" + snapshot1.getWarehouseId());
        System.out.println("庫存數量陣列：" + Arrays.toString(snapshot1.getQuantities()));
        System.out.println("總數量：" + snapshot1.totalQuantity());
        System.out.println("缺貨品項數：" + snapshot1.outOfStockCount());
        
        System.out.println("\n========== 測試不可變性 ==========");
        
        // 測試外部修改不會影響內部
        int[] originalQuantities = {5, 0, 3, 0};
        InventorySnapshot snapshot2 = new InventorySnapshot("WH002", originalQuantities);
        
        System.out.println("原始陣列：" + Arrays.toString(originalQuantities));
        System.out.println("快照內部：" + Arrays.toString(snapshot2.getQuantities()));
        
        // 修改原始陣列
        originalQuantities[0] = 100;
        originalQuantities[1] = 200;
        
        System.out.println("修改後原始陣列：" + Arrays.toString(originalQuantities));
        System.out.println("快照內部（不變）：" + Arrays.toString(snapshot2.getQuantities()));
        
        // 測試 getter 回傳的陣列被修改不影響內部
        int[] getterArray = snapshot2.getQuantities();
        getterArray[2] = 999;
        
        System.out.println("修改 getter 回傳的陣列後：" + Arrays.toString(getterArray));
        System.out.println("快照內部（仍然不變）：" + Arrays.toString(snapshot2.getQuantities()));
        
        System.out.println("\n========== 測試邊界條件 ==========");
        
        // 測試 null 陣列
        InventorySnapshot snapshot3 = new InventorySnapshot("WH003", null);
        System.out.println("倉庫 ID：" + snapshot3.getWarehouseId());
        System.out.println("null 陣列轉為空陣列：" + Arrays.toString(snapshot3.getQuantities()));
        System.out.println("總數量：" + snapshot3.totalQuantity());
        System.out.println("缺貨品項數：" + snapshot3.outOfStockCount());
        
        System.out.println("\n========== 驗證題目數據 ==========");
        
        // 使用題目指定的 {5, 0, 3, 0}
        InventorySnapshot snapshot4 = new InventorySnapshot("WH004", new int[]{5, 0, 3, 0});
        System.out.println("庫存數量：" + Arrays.toString(snapshot4.getQuantities()));
        System.out.println("總數量（應為 8）：" + snapshot4.totalQuantity());
        System.out.println("缺貨品項數（應為 2）：" + snapshot4.outOfStockCount());
    }
}

/**
 * 不可變庫存快照類別
 * 建立後無法修改內部狀態
 */
class InventorySnapshot {
    private final String warehouseId;  // 倉庫 ID（不可變）
    private final int[] quantities;    // 庫存數量陣列（不可變）
    
    /**
     * 建構函式 - 使用防禦性副本
     * @param warehouseId 倉庫 ID
     * @param quantities 庫存數量陣列
     */
    public InventorySnapshot(String warehouseId, int[] quantities) {
        this.warehouseId = warehouseId;
        
        // 防禦性副本：如果傳入 null，建立空陣列；否則複製一份
        if (quantities == null) {
            this.quantities = new int[0];
        } else {
            // 建立獨立副本，不直接使用傳入的參考
            this.quantities = Arrays.copyOf(quantities, quantities.length);
        }
    }
    
    /**
     * 取得倉庫 ID
     * 字串本來就是不可變的，直接回傳即可
     */
    public String getWarehouseId() {
        return warehouseId;
    }
    
    /**
     * 取得庫存數量陣列 - 使用防禦性副本
     * @return 內部陣列的獨立副本
     */
    public int[] getQuantities() {
        // 防禦性副本：回傳一份獨立的副本，而非內部陣列的參考
        return Arrays.copyOf(quantities, quantities.length);
    }
    
    /**
     * 計算總數量
     * @return 所有庫存數量的總和
     */
    public int totalQuantity() {
        int total = 0;
        for (int quantity : quantities) {
            total += quantity;
        }
        return total;
    }
    
    /**
     * 計算缺貨品項數（數量為 0 的品項）
     * @return 數量為 0 的品項數量
     */
    public int outOfStockCount() {
        int count = 0;
        for (int quantity : quantities) {
            if (quantity == 0) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 覆寫 toString() 方便顯示
     */
    @Override
    public String toString() {
        return "倉庫 ID：" + warehouseId + 
               "，庫存：" + Arrays.toString(quantities) +
               "，總數量：" + totalQuantity() +
               "，缺貨項：" + outOfStockCount();
    }
}