import java.util.Arrays;

public class CustomerOrderSystem {
    
    public static void main(String[] args) {
        System.out.println("========== 建立顧客 ==========");
        Customer customer1 = new Customer("C001", "張大明", "0912-345-678");
        System.out.println(customer1);
        
        System.out.println("\n========== 建立訂單品項 ==========");
        OrderItem item1 = new OrderItem("P001", "筆記型電腦", 25000.0, 1);
        OrderItem item2 = new OrderItem("P002", "無線滑鼠", 800.0, 2);
        OrderItem item3 = new OrderItem("P003", "機械鍵盤", 1500.0, 3);
        
        System.out.println(item1);
        System.out.println(item2);
        System.out.println(item3);
        
        System.out.println("\n========== 建立訂單（固定長度陣列） ==========");
        // 使用固定長度陣列（長度為 3）
        Order order1 = new Order("O001", customer1, new OrderItem[]{item1, item2, item3});
        System.out.println(order1);
        
        System.out.println("\n========== 建立第二筆訂單（不同顧客） ==========");
        Customer customer2 = new Customer("C002", "李小美", "0923-456-789");
        OrderItem item4 = new OrderItem("P004", "智慧型手機", 18000.0, 1);
        OrderItem item5 = new OrderItem("P005", "手機保護殼", 300.0, 2);
        
        Order order2 = new Order("O002", customer2, new OrderItem[]{item4, item5});
        System.out.println(order2);
        
        System.out.println("\n========== 測試訂單摘要輸出 ==========");
        System.out.println("訂單 O001 摘要：");
        order1.printSummary();
        
        System.out.println("\n訂單 O002 摘要：");
        order2.printSummary();
        
        System.out.println("\n========== 測試訂單總金額 ==========");
        System.out.printf("訂單 O001 總金額：%.2f 元%n", order1.calculateTotal());
        System.out.printf("訂單 O002 總金額：%.2f 元%n", order2.calculateTotal());
        
        System.out.println("\n========== 測試品項數量 ==========");
        System.out.println("訂單 O001 品項數量：" + order1.getItemCount());
        System.out.println("訂單 O002 品項數量：" + order2.getItemCount());
        
        System.out.println("\n========== 測試邊界條件（空訂單） ==========");
        Order emptyOrder = new Order("O003", customer1, new OrderItem[0]);
        System.out.println(emptyOrder);
        System.out.println("空訂單總金額：" + emptyOrder.calculateTotal());
        System.out.println("空訂單品項數量：" + emptyOrder.getItemCount());
        emptyOrder.printSummary();
        
        System.out.println("\n========== 測試 null 品項陣列 ==========");
        Order nullOrder = new Order("O004", customer2, null);
        System.out.println(nullOrder);
        System.out.println("null 品項陣列總金額：" + nullOrder.calculateTotal());
        System.out.println("null 品項陣列品項數量：" + nullOrder.getItemCount());
        nullOrder.printSummary();
    }
}

/**
 * 顧客類別 - 儲存顧客基本資訊
 */
class Customer {
    private final String customerId;    // 顧客編號
    private final String name;          // 顧客姓名
    private final String phone;         // 電話號碼
    
    /**
     * 建構子
     */
    public Customer(String customerId, String name, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
    }
    
    /**
     * Getter 方法
     */
    public String getCustomerId() {
        return customerId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    @Override
    public String toString() {
        return String.format("顧客編號：%s，姓名：%s，電話：%s", 
                           customerId, name, phone);
    }
}

/**
 * 訂單品項類別 - 儲存商品資訊
 */
class OrderItem {
    private final String productId;     // 商品編號
    private final String productName;   // 商品名稱
    private final double price;         // 單價
    private final int quantity;         // 數量
    
    /**
     * 建構子
     */
    public OrderItem(String productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
    
    /**
     * 計算該品項小計
     * @return 單價 × 數量
     */
    public double getSubtotal() {
        return price * quantity;
    }
    
    /**
     * Getter 方法
     */
    public String getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    @Override
    public String toString() {
        return String.format("商品編號：%s，名稱：%s，單價：%.2f 元，數量：%d，小計：%.2f 元",
                           productId, productName, price, quantity, getSubtotal());
    }
}

/**
 * 訂單類別 - 包含顧客及多個訂單品項（固定長度陣列）
 * 訂單不得將顧客與品項資料拆成平行陣列
 */
class Order {
    private final String orderId;           // 訂單編號
    private final Customer customer;        // 顧客（完整物件，非平行陣列）
    private final OrderItem[] items;        // 訂單品項陣列（完整物件，非平行陣列）
    
    /**
     * 建構子 - 使用固定長度陣列
     * @param orderId 訂單編號
     * @param customer 顧客物件
     * @param items 訂單品項陣列
     */
    public Order(String orderId, Customer customer, OrderItem[] items) {
        this.orderId = orderId;
        this.customer = customer;
        
        // 防禦性複製：如果傳入 null，建立空陣列
        if (items == null) {
            this.items = new OrderItem[0];
        } else {
            // 複製陣列，確保外部修改不影響內部
            this.items = Arrays.copyOf(items, items.length);
        }
    }
    
    /**
     * 計算訂單總金額
     * @return 所有品項小計的總和
     */
    public double calculateTotal() {
        double total = 0.0;
        for (OrderItem item : items) {
            if (item != null) {
                total += item.getSubtotal();
            }
        }
        return total;
    }
    
    /**
     * 取得訂單品項數量
     * @return 品項數量
     */
    public int getItemCount() {
        return items.length;
    }
    
    /**
     * 取得訂單編號
     */
    public String getOrderId() {
        return orderId;
    }
    
    /**
     * 取得顧客物件
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * 取得品項陣列（防禦性複製）
     */
    public OrderItem[] getItems() {
        return Arrays.copyOf(items, items.length);
    }
    
    /**
     * 輸出訂單摘要
     * 包含顧客資訊、所有品項、總金額
     */
    public void printSummary() {
        System.out.println("訂單編號：" + orderId);
        System.out.println("顧客資訊：" + customer);
        System.out.println("訂單品項：");
        
        if (items.length == 0) {
            System.out.println("  （此訂單無品項）");
        } else {
            for (int i = 0; i < items.length; i++) {
                System.out.println("  " + (i + 1) + ". " + items[i]);
            }
        }
        System.out.printf("訂單總金額：%.2f 元%n", calculateTotal());
        System.out.println("品項總數：" + items.length + " 項");
    }
    
    @Override
    public String toString() {
        return String.format("訂單編號：%s，顧客：%s，品項數：%d 項，總金額：%.2f 元",
                           orderId, customer.getName(), items.length, calculateTotal());
    }
}