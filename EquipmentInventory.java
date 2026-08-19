public class EquipmentInventory {
    
    public static void main(String[] args) {
        // 创建两个设备
        Equipment laptop = new Equipment("E001", "筆記本電腦", 3);
        Equipment projector = new Equipment("", "", -5); // 测试边界情况
        
        System.out.println("========== 初始狀態 ==========");
        System.out.println(laptop);
        System.out.println(projector);
        
        System.out.println("\n========== 測試借用 ==========");
        // 测试借用成功
        System.out.println("借用筆記本電腦 (庫存3)：" + laptop.borrowOne());
        System.out.println(laptop);
        
        // 连续借用到库存为0
        laptop.borrowOne();
        laptop.borrowOne();
        System.out.println("借完3次后：" + laptop);
        
        // 测试借用失败（库存为0时）
        System.out.println("再次借用筆記本電腦：" + laptop.borrowOne());
        System.out.println(laptop);
        
        System.out.println("\n========== 測試歸還 ==========");
        // 测试归还正数
        laptop.returnItems(2);
        System.out.println("歸還2台后：" + laptop);
        
        // 测试归还负数（不应改变库存）
        laptop.returnItems(-1);
        System.out.println("嘗試歸還-1台后：" + laptop);
        
        // 测试归还0（不应改变库存）
        laptop.returnItems(0);
        System.out.println("嘗試歸還0台后：" + laptop);
        
        System.out.println("\n========== 測試另一个設備 ==========");
        projector.returnItems(10);
        System.out.println("投影機歸還10台后：" + projector);
        System.out.println("借用投影機：" + projector.borrowOne());
        System.out.println(projector);
    }
}

class Equipment {
    // 私有字段
    private String id;
    private String name;
    private int availableCount;
    
    // 构造子（构造函数）
    public Equipment(String id, String name, int availableCount) {
        // 空白 id 或 name 改为 "unknown"
        this.id = (id == null || id.trim().isEmpty()) ? "unknown" : id;
        this.name = (name == null || name.trim().isEmpty()) ? "unknown" : name;
        // 负数数量改为 0
        this.availableCount = (availableCount < 0) ? 0 : availableCount;
    }
    
    // 借用一台：有库存时减1并回传 true，否则回传 false
    public boolean borrowOne() {
        if (availableCount > 0) {
            availableCount--;
            return true;
        }
        return false;
    }
    
    // 归还指定数量：正数才加入库存
    public void returnItems(int quantity) {
        if (quantity > 0) {
            availableCount += quantity;
        }
        // 负数或0不做任何事（符合题目要求）
    }
    
    // toString()：输出设备编号、名称与可借数
    @Override
    public String toString() {
        return "設備編號：" + id + "，名稱：" + name + "，可借數量：" + availableCount;
    }
}