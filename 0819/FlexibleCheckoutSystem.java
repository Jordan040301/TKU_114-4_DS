/**
 * 靈活結帳系統
 * 指定檔名：FlexibleCheckoutSystem.java
 * 
 * 延伸結帳通知系統：
 * 1. 定價策略：原價、VIP八五折、滿2000折300
 * 2. 通知頻道：電子郵件、簡訊、控制台
 * 3. CheckoutResult 儲存訂單號碼、原價、最終價格和通知狀態
 * 4. checkout() 回傳 CheckoutResult，不僅僅回傳布林值
 * 5. 至少六種測試組合
 */
public class FlexibleCheckoutSystem {
    
    public static void main(String[] args) {
        System.out.println("========== 靈活結帳系統 ==========");
        
        // 建立定價策略
        PricingPolicy originalPrice = new OriginalPricing();
        PricingPolicy vipDiscount = new VipDiscountPricing();
        PricingPolicy thresholdDiscount = new ThresholdDiscountPricing();
        
        // 建立通知頻道
        NotificationChannel emailChannel = new EmailNotification();
        NotificationChannel smsChannel = new SmsNotification();
        NotificationChannel consoleChannel = new ConsoleNotification();
        
        // 建立結帳服務
        CheckoutService service = new CheckoutService();
        
        // ========== 測試六種組合 ==========
        
        System.out.println("\n========== 測試組合 1：原價 + 電子郵件 ==========");
        CheckoutResult result1 = service.checkout("ORD-001", 1500.0, originalPrice, emailChannel);
        printResult(result1);
        
        System.out.println("\n========== 測試組合 2：VIP八五折 + 簡訊 ==========");
        CheckoutResult result2 = service.checkout("ORD-002", 5000.0, vipDiscount, smsChannel);
        printResult(result2);
        
        System.out.println("\n========== 測試組合 3：滿2000折300 + 控制台 ==========");
        CheckoutResult result3 = service.checkout("ORD-003", 2500.0, thresholdDiscount, consoleChannel);
        printResult(result3);
        
        System.out.println("\n========== 測試組合 4：VIP八五折 + 電子郵件 ==========");
        CheckoutResult result4 = service.checkout("ORD-004", 3200.0, vipDiscount, emailChannel);
        printResult(result4);
        
        System.out.println("\n========== 測試組合 5：原價 + 控制台 ==========");
        CheckoutResult result5 = service.checkout("ORD-005", 800.0, originalPrice, consoleChannel);
        printResult(result5);
        
        System.out.println("\n========== 測試組合 6：滿2000折300 + 簡訊 ==========");
        CheckoutResult result6 = service.checkout("ORD-006", 1800.0, thresholdDiscount, smsChannel);
        printResult(result6);
        
        // ========== 額外測試：邊界條件 ==========
        
        System.out.println("\n========== 額外測試：滿2000折300（正好2000） ==========");
        CheckoutResult result7 = service.checkout("ORD-007", 2000.0, thresholdDiscount, consoleChannel);
        printResult(result7);
        
        System.out.println("\n========== 額外測試：VIP八五折（小額） ==========");
        CheckoutResult result8 = service.checkout("ORD-008", 100.0, vipDiscount, emailChannel);
        printResult(result8);
        
        System.out.println("\n========== 測試所有結果統計 ==========");
        CheckoutResult[] allResults = {result1, result2, result3, result4, result5, result6, result7, result8};
        printAllResultsSummary(allResults);
    }
    
    /**
     * 輸出結帳結果
     */
    public static void printResult(CheckoutResult result) {
        System.out.println("訂單編號：" + result.getOrderId());
        System.out.println("原價：" + result.getOriginalPrice() + " 元");
        System.out.println("最終價格：" + result.getFinalPrice() + " 元");
        System.out.println("省下金額：" + result.getSavings() + " 元");
        System.out.println("折扣說明：" + result.getDiscountDescription());
        System.out.println("通知頻道：" + result.getChannelName());
        System.out.println("通知狀態：" + (result.isNotified() ? "✅ 已發送" : "❌ 發送失敗"));
        System.out.println("時間：" + result.getTimestamp());
        System.out.println("-".repeat(40));
    }
    
    /**
     * 輸出所有結果統計
     */
    public static void printAllResultsSummary(CheckoutResult[] results) {
        double totalOriginal = 0;
        double totalFinal = 0;
        double totalSavings = 0;
        
        System.out.println("訂單編號\t原價\t最終價格\t省下金額\t折扣方式\t\t通知頻道");
        System.out.println("--------\t----\t--------\t--------\t--------\t\t--------");
        
        for (CheckoutResult r : results) {
            totalOriginal += r.getOriginalPrice();
            totalFinal += r.getFinalPrice();
            totalSavings += r.getSavings();
            
            System.out.printf("%s\t%.0f\t%.0f\t\t%.0f\t%-10s\t\t%s%n",
                            r.getOrderId(),
                            r.getOriginalPrice(),
                            r.getFinalPrice(),
                            r.getSavings(),
                            r.getDiscountDescription(),
                            r.getChannelName());
        }
        
        System.out.println("-".repeat(65));
        System.out.printf("總計\t%.0f\t%.0f\t\t%.0f%n", totalOriginal, totalFinal, totalSavings);
        System.out.printf("平均\t%.0f\t%.0f\t\t%.0f%n", 
                         totalOriginal / results.length, 
                         totalFinal / results.length, 
                         totalSavings / results.length);
    }
}

/**
 * 定價策略介面
 * 定義價格計算的標準契約
 */
interface PricingPolicy {
    
    /**
     * 計算最終價格
     * @param originalPrice 原始價格
     * @return 最終價格（套用折扣後）
     */
    double calculateFinalPrice(double originalPrice);
    
    /**
     * 取得折扣說明
     * @return 折扣方式描述
     */
    String getDescription();
}

/**
 * 原價策略 - 不折扣
 */
class OriginalPricing implements PricingPolicy {
    
    @Override
    public double calculateFinalPrice(double originalPrice) {
        return originalPrice;
    }
    
    @Override
    public String getDescription() {
        return "原價";
    }
}

/**
 * VIP八五折策略
 */
class VipDiscountPricing implements PricingPolicy {
    private static final double DISCOUNT_RATE = 0.85;  // 八五折
    private static final double MINIMUM_PRICE = 0;      // 最低價格
    
    @Override
    public double calculateFinalPrice(double originalPrice) {
        if (originalPrice < 0) return 0;
        double finalPrice = originalPrice * DISCOUNT_RATE;
        return Math.max(finalPrice, MINIMUM_PRICE);
    }
    
    @Override
    public String getDescription() {
        return "VIP八五折";
    }
}

/**
 * 滿2000折300策略
 */
class ThresholdDiscountPricing implements PricingPolicy {
    private static final double THRESHOLD = 2000.0;    // 門檻金額
    private static final double DISCOUNT = 300.0;       // 折扣金額
    private static final double MINIMUM_PRICE = 0;      // 最低價格
    
    @Override
    public double calculateFinalPrice(double originalPrice) {
        if (originalPrice < 0) return 0;
        double finalPrice = originalPrice;
        if (originalPrice >= THRESHOLD) {
            finalPrice = originalPrice - DISCOUNT;
        }
        return Math.max(finalPrice, MINIMUM_PRICE);
    }
    
    @Override
    public String getDescription() {
        return "滿" + (int)THRESHOLD + "折" + (int)DISCOUNT;
    }
}

/**
 * 通知頻道介面
 * 定義發送通知的標準契約
 */
interface NotificationChannel {
    
    /**
     * 發送通知
     * @param orderId 訂單編號
     * @param amount 金額
     * @return 是否發送成功
     */
    boolean send(String orderId, double amount);
    
    /**
     * 取得頻道名稱
     * @return 頻道名稱
     */
    String getChannelName();
}

/**
 * 電子郵件通知
 */
class EmailNotification implements NotificationChannel {
    
    @Override
    public boolean send(String orderId, double amount) {
        // 模擬發送電子郵件
        System.out.println("  📧 發送電子郵件至：customer@example.com");
        System.out.println("  📧 主旨：【結帳通知】訂單 " + orderId + " 已完成");
        System.out.println("  📧 內容：您已成功結帳，金額 " + amount + " 元");
        return true;
    }
    
    @Override
    public String getChannelName() {
        return "電子郵件";
    }
}

/**
 * 簡訊通知
 */
class SmsNotification implements NotificationChannel {
    
    @Override
    public boolean send(String orderId, double amount) {
        // 模擬發送簡訊
        System.out.println("  📱 發送簡訊至：0912-345-678");
        System.out.println("  📱 簡訊內容：【" + orderId + "】結帳成功 " + amount + " 元");
        return true;
    }
    
    @Override
    public String getChannelName() {
        return "簡訊";
    }
}

/**
 * 控制台通知
 */
class ConsoleNotification implements NotificationChannel {
    
    @Override
    public boolean send(String orderId, double amount) {
        // 模擬輸出到控制台
        System.out.println("  💻 [控制台通知] 訂單 " + orderId + " 結帳成功，金額 " + amount + " 元");
        return true;
    }
    
    @Override
    public String getChannelName() {
        return "控制台";
    }
}

/**
 * 結帳結果類別
 * 儲存訂單號碼、原價、最終價格和通知狀態
 */
class CheckoutResult {
    private final String orderId;            // 訂單編號
    private final double originalPrice;      // 原價
    private final double finalPrice;         // 最終價格
    private final double savings;            // 省下金額
    private final String discountDescription; // 折扣說明
    private final String channelName;        // 通知頻道名稱
    private final boolean notified;          // 通知狀態
    private final String timestamp;          // 時間戳記
    
    /**
     * 建構子
     */
    public CheckoutResult(String orderId, double originalPrice, double finalPrice,
                         String discountDescription, String channelName, boolean notified) {
        this.orderId = orderId;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.savings = originalPrice - finalPrice;
        this.discountDescription = discountDescription;
        this.channelName = channelName;
        this.notified = notified;
        this.timestamp = java.time.LocalDateTime.now().toString().substring(0, 19);
    }
    
    /**
     * Getter 方法
     */
    public String getOrderId() { return orderId; }
    public double getOriginalPrice() { return originalPrice; }
    public double getFinalPrice() { return finalPrice; }
    public double getSavings() { return savings; }
    public String getDiscountDescription() { return discountDescription; }
    public String getChannelName() { return channelName; }
    public boolean isNotified() { return notified; }
    public String getTimestamp() { return timestamp; }
}

/**
 * 結帳服務類別
 * 整合定價策略和通知頻道，執行結帳流程
 */
class CheckoutService {
    
    /**
     * 執行結帳
     * @param orderId 訂單編號
     * @param originalPrice 原價
     * @param pricingPolicy 定價策略
     * @param notificationChannel 通知頻道
     * @return CheckoutResult 包含完整結帳資訊
     */
    public CheckoutResult checkout(String orderId, double originalPrice,
                                   PricingPolicy pricingPolicy,
                                   NotificationChannel notificationChannel) {
        // 參數驗證
        if (orderId == null || orderId.trim().isEmpty()) {
            orderId = "UNKNOWN";
        }
        if (originalPrice < 0) {
            originalPrice = 0;
        }
        
        // 計算最終價格（使用定價策略）
        double finalPrice = pricingPolicy.calculateFinalPrice(originalPrice);
        String discountDescription = pricingPolicy.getDescription();
        
        // 發送通知
        boolean notified = notificationChannel.send(orderId, finalPrice);
        String channelName = notificationChannel.getChannelName();
        
        // 回傳結帳結果
        return new CheckoutResult(orderId, originalPrice, finalPrice,
                                 discountDescription, channelName, notified);
    }
}