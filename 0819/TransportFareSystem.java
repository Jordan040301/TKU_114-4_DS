/**
 * 交通票價多型系統
 * 指定檔名：TransportFareSystem.java
 * 
 * 建立抽象類 Transport，保存路線名稱，並宣告 calculateFare(int distance)
 * 建立 Bus 與 Taxi 兩個子類，使用不同方式計算票價
 * 以 Transport[] 保存至少四個物件並輸出各自的票價
 */
public class TransportFareSystem {
    
    public static void main(String[] args) {
        System.out.println("========== 交通票價多型系統 ==========");
        
        // 建立 Transport 陣列，保存至少四個物件
        Transport[] transports = new Transport[] {
            new Bus("藍線公車", 30),           // 基本票價 30 元
            new Bus("紅線公車", 25),           // 基本票價 25 元
            new Taxi("大都會計程車", 70, 15),  // 起跳 70 元，每公里 15 元
            new Taxi("台灣大車隊", 80, 18)     // 起跳 80 元，每公里 18 元
        };
        
        // 測試不同距離
        int[] distances = {5, 10, 20, 50};
        
        System.out.println("\n========== 票價計算結果 ==========");
        for (Transport t : transports) {
            System.out.println("交通工具：" + t.getRouteName());
            System.out.println("  路線名稱：" + t.getRouteName());
            
            // 顯示不同距離的票價
            for (int distance : distances) {
                double fare = t.calculateFare(distance);
                System.out.printf("  行駛 %d 公里：%.0f 元%n", distance, fare);
            }
            System.out.println();
        }
        
        System.out.println("\n========== 詳細票價比較 ==========");
        printFareComparison(transports, 15);
        
        System.out.println("\n========== 測試邊界條件 ==========");
        // 測試負距離（應視為 0）
        System.out.println("測試負距離（-5 公里）：");
        for (Transport t : transports) {
            double fare = t.calculateFare(-5);
            System.out.printf("  %s：%.0f 元%n", t.getRouteName(), fare);
        }
    }
    
    /**
     * 輸出指定距離下所有交通工具的票價比較
     */
    public static void printFareComparison(Transport[] transports, int distance) {
        System.out.printf("行駛 %d 公里的票價比較：%n", distance);
        System.out.println("=" .repeat(40));
        
        for (Transport t : transports) {
            double fare = t.calculateFare(distance);
            System.out.printf("  %-10s：%.0f 元%n", t.getRouteName(), fare);
        }
    }
}

/**
 * 抽象運輸類別
 * 保存路線名稱，並宣告 calculateFare 抽象方法
 */
abstract class Transport {
    private String routeName;   // 路線名稱
    
    /**
     * 建構子
     */
    public Transport(String routeName) {
        this.routeName = routeName;
    }
    
    /**
     * 取得路線名稱
     */
    public String getRouteName() {
        return routeName;
    }
    
    /**
     * 設定路線名稱
     */
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    
    /**
     * 抽象方法：計算票價
     * @param distance 行駛距離（公里）
     * @return 票價金額
     */
    public abstract double calculateFare(int distance);
}

/**
 * 公車類別 - 繼承 Transport
 * 計價方式：基本票價 + 距離 × 每公里單價
 * 假設公車為分段收費，每段 5 公里收費一次
 */
class Bus extends Transport {
    private double baseFare;        // 基本票價
    private static final double PER_KM_RATE = 5.0;  // 每公里單價
    private static final int SEGMENT_DISTANCE = 5;  // 每段距離（公里）
    
    /**
     * 建構子
     * @param routeName 路線名稱
     * @param baseFare 基本票價
     */
    public Bus(String routeName, double baseFare) {
        super(routeName);
        this.baseFare = baseFare;
    }
    
    /**
     * 計算公車票價
     * 計價方式：基本票價 + (距離 / 每段距離) × 每段單價
     * 距離為 0 時只收基本票價
     */
    @Override
    public double calculateFare(int distance) {
        // 處理負距離
        if (distance < 0) {
            distance = 0;
        }
        
        // 計算段數（無條件進位）
        int segments = (distance + SEGMENT_DISTANCE - 1) / SEGMENT_DISTANCE;
        
        // 總票價 = 基本票價 + 段數 × 每段單價
        double fare = baseFare + segments * PER_KM_RATE;
        
        return fare;
    }
    
    /**
     * 取得基本票價
     */
    public double getBaseFare() {
        return baseFare;
    }
}

/**
 * 計程車類別 - 繼承 Transport
 * 計價方式：起跳價 + 距離 × 每公里單價
 */
class Taxi extends Transport {
    private double baseFare;        // 起跳價
    private double perKmRate;       // 每公里單價
    
    /**
     * 建構子
     * @param routeName 路線名稱
     * @param baseFare 起跳價（含 1.25 公里）
     * @param perKmRate 每公里單價
     */
    public Taxi(String routeName, double baseFare, double perKmRate) {
        super(routeName);
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
    }
    
    /**
     * 計算計程車票價
     * 計價方式：起跳價 + 距離 × 每公里單價
     * 距離為 0 時只收起跳價
     */
    @Override
    public double calculateFare(int distance) {
        // 處理負距離
        if (distance < 0) {
            distance = 0;
        }
        
        // 總票價 = 起跳價 + 距離 × 每公里單價
        double fare = baseFare + distance * perKmRate;
        
        return fare;
    }
    
    /**
     * 取得起跳價
     */
    public double getBaseFare() {
        return baseFare;
    }
    
    /**
     * 取得每公里單價
     */
    public double getPerKmRate() {
        return perKmRate;
    }
}