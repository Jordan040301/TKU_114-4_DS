/**
 * 多方式配送系統
 * 指定檔名：DeliveryStrategySystem.java
 * 
 * 建立 DeliveryMethod 介面，實作宅配、超市取貨與自取
 * OrderService 使用組合保存 DeliveryMethod
 * 計算運費及發票說明
 */
public class DeliveryStrategySystem {
    
    public static void main(String[] args) {
        System.out.println("========== 多方式配送系統 ==========");
        
        // 建立各種配送方式
        DeliveryMethod homeDelivery = new HomeDelivery(80.0, 3);      // 宅配：80元，3天
        DeliveryMethod storePickup = new StorePickup("台北信義店");    // 超市取貨
        DeliveryMethod selfPickup = new SelfPickup("台北車站1號櫃檯"); // 自取
        
        // 建立訂單服務（使用組合方式）
        System.out.println("\n========== 訂單 1：宅配 ==========");
        OrderService order1 = new OrderService("ORD-001", "張大明", homeDelivery);
        order1.addItem("筆記型電腦", 1, 25000.0);
        order1.addItem("無線滑鼠", 2, 800.0);
        order1.printInvoice();
        
        System.out.println("\n========== 訂單 2：超市取貨 ==========");
        OrderService order2 = new OrderService("ORD-002", "李小美", storePickup);
        order2.addItem("鮮奶", 3, 95.0);
        order2.addItem("麵包", 2, 45.0);
        order2.printInvoice();
        
        System.out.println("\n========== 訂單 3：自取 ==========");
        OrderService order3 = new OrderService("ORD-003", "王大華", selfPickup);
        order3.addItem("智慧型手機", 1, 18000.0);
        order3.addItem("手機保護殼", 3, 300.0);
        order3.printInvoice();
        
        System.out.println("\n========== 訂單 4：變更配送方式 ==========");
        OrderService order4 = new OrderService("ORD-004", "陳小芳", storePickup);
        order4.addItem("書籍", 5, 350.0);
        System.out.println("原配送方式：");
        order4.printInvoice();
        
        // 變更配送方式（組合的優勢：可以動態更換）
        System.out.println("\n變更為宅配：");
        order4.setDeliveryMethod(homeDelivery);
        order4.printInvoice();
        
        System.out.println("\n========== 所有訂單總覽 ==========");
        OrderService[] orders = {order1, order2, order3, order4};
        printAllOrders(orders);
    }
    
    /**
     * 輸出所有訂單摘要
     */
    public static void printAllOrders(OrderService[] orders) {
        System.out.println("訂單編號\t客戶\t\t配送方式\t總金額");
        System.out.println("--------\t----\t\t--------\t--------");
        for (OrderService order : orders) {
            System.out.printf("%s\t%-8s\t%-10s\t%.2f%n",
                            order.getOrderId(),
                            order.getCustomerName(),
                            order.getDeliveryMethod().getMethodName(),
                            order.getTotalAmount());
        }
    }
}

/**
 * 配送方式介面
 * 定義所有配送方式必須實作的方法
 */
interface DeliveryMethod {
    
    /**
     * 取得配送方式名稱
     */
    String getMethodName();
    
    /**
     * 計算運費
     * @param totalPrice 商品總金額
     * @return 運費金額
     */
    double calculateShippingFee(double totalPrice);
    
    /**
     * 取得配送說明（顯示在發票上）
     */
    String getDescription();
    
    /**
     * 取得預計送達天數
     */
    int getEstimatedDays();
}

/**
 * 宅配配送方式
 * 依商品金額計算運費
 */
class HomeDelivery implements DeliveryMethod {
    private double baseFee;         // 基本運費
    private int estimatedDays;      // 預計送達天數
    private static final double FREE_SHIPPING_THRESHOLD = 2000.0;  // 免運門檻
    
    /**
     * 建構子
     * @param baseFee 基本運費
     * @param estimatedDays 預計送達天數
     */
    public HomeDelivery(double baseFee, int estimatedDays) {
        this.baseFee = baseFee;
        this.estimatedDays = estimatedDays;
    }
    
    @Override
    public String getMethodName() {
        return "宅配到家";
    }
    
    @Override
    public double calculateShippingFee(double totalPrice) {
        // 滿額免運
        if (totalPrice >= FREE_SHIPPING_THRESHOLD) {
            return 0;
        }
        return baseFee;
    }
    
    @Override
    public String getDescription() {
        return "宅配到家服務，專人配送至指定地址";
    }
    
    @Override
    public int getEstimatedDays() {
        return estimatedDays;
    }
}

/**
 * 超市取貨配送方式
 * 門市取貨，固定運費
 */
class StorePickup implements DeliveryMethod {
    private String storeName;       // 門市名稱
    private static final double SHIPPING_FEE = 50.0;  // 固定運費
    private static final int ESTIMATED_DAYS = 2;      // 預計送達天數
    
    /**
     * 建構子
     * @param storeName 門市名稱
     */
    public StorePickup(String storeName) {
        this.storeName = storeName;
    }
    
    @Override
    public String getMethodName() {
        return "超市取貨";
    }
    
    @Override
    public double calculateShippingFee(double totalPrice) {
        // 超市取貨固定運費
        return SHIPPING_FEE;
    }
    
    @Override
    public String getDescription() {
        return "至「" + storeName + "」門市取貨，請攜帶身分證件";
    }
    
    @Override
    public int getEstimatedDays() {
        return ESTIMATED_DAYS;
    }
    
    /**
     * 取得門市名稱
     */
    public String getStoreName() {
        return storeName;
    }
}

/**
 * 自取配送方式
 * 免運費
 */
class SelfPickup implements DeliveryMethod {
    private String pickupLocation;   // 取貨地點
    private static final int ESTIMATED_DAYS = 1;  // 預計送達天數
    
    /**
     * 建構子
     * @param pickupLocation 取貨地點
     */
    public SelfPickup(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
    
    @Override
    public String getMethodName() {
        return "自取";
    }
    
    @Override
    public double calculateShippingFee(double totalPrice) {
        // 自取免運費
        return 0;
    }
    
    @Override
    public String getDescription() {
        return "至「" + pickupLocation + "」自取，請攜帶訂單編號及身分證件";
    }
    
    @Override
    public int getEstimatedDays() {
        return ESTIMATED_DAYS;
    }
    
    /**
     * 取得取貨地點
     */
    public String getPickupLocation() {
        return pickupLocation;
    }
}

/**
 * 訂單服務類別 - 使用組合方式保存 DeliveryMethod
 * 可動態變更配送方式
 */
class OrderService {
    private String orderId;                 // 訂單編號
    private String customerName;            // 客戶姓名
    private DeliveryMethod deliveryMethod;  // 配送方式（組合）
    private OrderItem[] items;              // 商品項目
    private int itemCount;                  // 商品數量
    
    private static final int MAX_ITEMS = 20;  // 最大商品數量
    
    /**
     * 建構子 - 使用組合保存 DeliveryMethod
     */
    public OrderService(String orderId, String customerName, DeliveryMethod deliveryMethod) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.deliveryMethod = deliveryMethod;
        this.items = new OrderItem[MAX_ITEMS];
        this.itemCount = 0;
    }
    
    /**
     * 新增商品項目
     */
    public void addItem(String productName, int quantity, double price) {
        if (itemCount >= MAX_ITEMS) {
            System.out.println("商品數量已達上限");
            return;
        }
        items[itemCount] = new OrderItem(productName, quantity, price);
        itemCount++;
    }
    
    /**
     * 計算商品總金額（不含運費）
     */
    public double getSubtotal() {
        double total = 0;
        for (int i = 0; i < itemCount; i++) {
            total += items[i].getTotalPrice();
        }
        return total;
    }
    
    /**
     * 計算運費 - 委派給 DeliveryMethod
     */
    public double getShippingFee() {
        return deliveryMethod.calculateShippingFee(getSubtotal());
    }
    
    /**
     * 計算總金額（含運費）
     */
    public double getTotalAmount() {
        return getSubtotal() + getShippingFee();
    }
    
    /**
     * 變更配送方式 - 展現組合的優勢
     */
    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
    
    /**
     * 取得配送方式
     */
    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }
    
    /**
     * 輸出完整發票
     */
    public void printInvoice() {
        System.out.println("=" .repeat(50));
        System.out.println("                    發    票");
        System.out.println("=" .repeat(50));
        System.out.println("訂單編號：" + orderId);
        System.out.println("客戶姓名：" + customerName);
        System.out.println("配送方式：" + deliveryMethod.getMethodName());
        System.out.println("配送說明：" + deliveryMethod.getDescription());
        System.out.println("預計天數：" + deliveryMethod.getEstimatedDays() + " 天");
        System.out.println("-" .repeat(50));
        System.out.println("商品明細：");
        System.out.println("  品名\t\t數量\t單價\t小計");
        
        for (int i = 0; i < itemCount; i++) {
            OrderItem item = items[i];
            System.out.printf("  %-10s\t%d\t%.0f\t%.0f%n",
                            item.getProductName(),
                            item.getQuantity(),
                            item.getPrice(),
                            item.getTotalPrice());
        }
        
        System.out.println("-" .repeat(50));
        System.out.printf("商品總額：%.2f 元%n", getSubtotal());
        System.out.printf("運費：%.2f 元%n", getShippingFee());
        System.out.printf("總金額：%.2f 元%n", getTotalAmount());
        System.out.println("=" .repeat(50));
    }
    
    /**
     * Getter 方法
     */
    public String getOrderId() {
        return orderId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
}

/**
 * 訂單商品項目類別
 */
class OrderItem {
    private String productName;   // 商品名稱
    private int quantity;         // 數量
    private double price;         // 單價
    
    /**
     * 建構子
     */
    public OrderItem(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = (quantity < 0) ? 0 : quantity;
        this.price = (price < 0) ? 0 : price;
    }
    
    /**
     * 計算小計
     */
    public double getTotalPrice() {
        return quantity * price;
    }
    
    /**
     * Getter 方法
     */
    public String getProductName() {
        return productName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getPrice() {
        return price;
    }
}