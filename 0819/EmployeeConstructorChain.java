/**
 * 員工建構鏈與薪資計算
 * 指定檔名：EmployeeConstructorChain.java
 * 
 * 建立 abstract EmployeeBase，建構子接收 id 與 name
 * 建立 FullTimeEmployee 與 PartTimeEmployee
 * 每個建構函式輸出自己的類別名稱
 * 兩個子類別都必須使用 super(...)
 * calculatePay() 由子類別重寫
 */
public class EmployeeConstructorChain {
    
    public static void main(String[] args) {
        System.out.println("========== 員工建構鏈示範 ==========");
        
        System.out.println("\n---------- 建立 FullTimeEmployee ----------");
        FullTimeEmployee fullTime = new FullTimeEmployee("E001", "張大明", 45000.0);
        
        System.out.println("\n---------- 建立 PartTimeEmployee ----------");
        PartTimeEmployee partTime = new PartTimeEmployee("E002", "李小美", 250.0, 80);
        
        System.out.println("\n---------- 建立 FullTimeEmployee（負數薪資測試） ----------");
        FullTimeEmployee fullTimeNegative = new FullTimeEmployee("E003", "王大華", -5000.0);
        
        System.out.println("\n---------- 建立 PartTimeEmployee（負數時薪測試） ----------");
        PartTimeEmployee partTimeNegative = new PartTimeEmployee("E004", "陳小芳", -150.0, -40);
        
        System.out.println("\n========== 員工資訊與薪資計算 ==========");
        System.out.println("\n正職員工資訊：");
        System.out.println("  " + fullTime);
        System.out.printf("  月薪：%.2f 元%n", fullTime.calculatePay());
        
        System.out.println("\n兼職員工資訊：");
        System.out.println("  " + partTime);
        System.out.printf("  月薪：%.2f 元%n", partTime.calculatePay());
        
        System.out.println("\n正職員工（負數薪資修正後）資訊：");
        System.out.println("  " + fullTimeNegative);
        System.out.printf("  月薪：%.2f 元%n", fullTimeNegative.calculatePay());
        
        System.out.println("\n兼職員工（負數時薪/時數修正後）資訊：");
        System.out.println("  " + partTimeNegative);
        System.out.printf("  月薪：%.2f 元%n", partTimeNegative.calculatePay());
        
        System.out.println("\n========== 建構函數執行順序分析 ==========");
        System.out.println("從輸出可以看到建構順序為：");
        System.out.println("  1. EmployeeBase（父類別）建構子");
        System.out.println("  2. FullTimeEmployee / PartTimeEmployee（子類別）建構子");
        System.out.println("驗證：super() 必須在子類別建構子的第一行執行");
        
        System.out.println("\n========== 多型測試 ==========");
        EmployeeBase[] employees = new EmployeeBase[] {
            fullTime,
            partTime,
            fullTimeNegative,
            partTimeNegative
        };
        
        System.out.println("使用 EmployeeBase 陣列（多型）計算薪資：");
        for (EmployeeBase emp : employees) {
            System.out.printf("  %s（%s）：%.2f 元%n", 
                            emp.getName(), 
                            emp.getClass().getSimpleName(),
                            emp.calculatePay());
        }
    }
}

/**
 * 抽象員工基礎類別
 * 建構子接收 id 與 name
 */
abstract class EmployeeBase {
    private String id;       // 員工編號
    private String name;     // 員工姓名
    
    /**
     * 父類別建構子
     * 輸出自己的類別名稱
     */
    public EmployeeBase(String id, String name) {
        System.out.println("  【建構】" + this.getClass().getSimpleName() + " 開始");
        System.out.println("    super() 呼叫：" + getClass().getSuperclass().getSimpleName() + " 建構子");
        this.id = id;
        this.name = name;
        System.out.println("  【建構完成】" + this.getClass().getSimpleName() + " 結束");
    }
    
    /**
     * 取得員工編號
     */
    public String getId() {
        return id;
    }
    
    /**
     * 取得員工姓名
     */
    public String getName() {
        return name;
    }
    
    /**
     * 抽象方法：計算薪資
     * 由子類別實作
     */
    public abstract double calculatePay();
    
    @Override
    public String toString() {
        return String.format("員工編號：%s，姓名：%s", id, name);
    }
}

/**
 * 正職員工類別 - 繼承 EmployeeBase
 * 月薪制
 */
class FullTimeEmployee extends EmployeeBase {
    private double monthlySalary;    // 月薪
    
    /**
     * 建構子 - 使用 super(...) 呼叫父類別建構子
     * @param id 員工編號
     * @param name 員工姓名
     * @param monthlySalary 月薪（負數時轉為 0）
     */
    public FullTimeEmployee(String id, String name, double monthlySalary) {
        // 必須在第一行呼叫 super(...)
        super(id, name);
        
        System.out.println("    " + this.getClass().getSimpleName() + " 建構子開始");
        // 邊界條件：負數薪資轉為 0
        this.monthlySalary = (monthlySalary < 0) ? 0 : monthlySalary;
        System.out.println("    月薪設定為：" + this.monthlySalary);
        System.out.println("    " + this.getClass().getSimpleName() + " 建構子結束");
    }
    
    /**
     * 計算薪資 - 回傳月薪
     */
    @Override
    public double calculatePay() {
        return monthlySalary;
    }
    
    /**
     * 取得月薪
     */
    public double getMonthlySalary() {
        return monthlySalary;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format("，月薪：%.2f 元", monthlySalary);
    }
}

/**
 * 兼職員工類別 - 繼承 EmployeeBase
 * 時薪制
 */
class PartTimeEmployee extends EmployeeBase {
    private double hourlyRate;    // 時薪
    private int hoursWorked;      // 工作時數
    
    /**
     * 建構子 - 使用 super(...) 呼叫父類別建構子
     * @param id 員工編號
     * @param name 員工姓名
     * @param hourlyRate 時薪（負數時轉為 0）
     * @param hoursWorked 工作時數（負數時轉為 0）
     */
    public PartTimeEmployee(String id, String name, double hourlyRate, int hoursWorked) {
        // 必須在第一行呼叫 super(...)
        super(id, name);
        
        System.out.println("    " + this.getClass().getSimpleName() + " 建構子開始");
        // 邊界條件：負數時薪轉為 0
        this.hourlyRate = (hourlyRate < 0) ? 0 : hourlyRate;
        // 邊界條件：負數時數轉為 0
        this.hoursWorked = (hoursWorked < 0) ? 0 : hoursWorked;
        System.out.println("    時薪設定為：" + this.hourlyRate);
        System.out.println("    工作時數設定為：" + this.hoursWorked);
        System.out.println("    " + this.getClass().getSimpleName() + " 建構子結束");
    }
    
    /**
     * 計算薪資 - 時薪 × 工作時數
     */
    @Override
    public double calculatePay() {
        return hourlyRate * hoursWorked;
    }
    
    /**
     * 取得時薪
     */
    public double getHourlyRate() {
        return hourlyRate;
    }
    
    /**
     * 取得工作時數
     */
    public int getHoursWorked() {
        return hoursWorked;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format("，時薪：%.2f 元，工作時數：%d 小時", 
                                               hourlyRate, hoursWorked);
    }
}