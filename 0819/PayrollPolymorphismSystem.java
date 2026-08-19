/**
 * 員工薪資與獎金系統
 * 指定檔名：PayrollPolymorphismSystem.java
 * 
 * 建立抽象 Employee 與三個子類：月薪、時薪、業務
 * 每個子類實作 calculatePay()
 * 使用 Employee[] 計算薪資與最高薪資
 */
public class PayrollPolymorphismSystem {
    
    public static void main(String[] args) {
        System.out.println("========== 員工薪資與獎金系統 ==========");
        
        // 建立 Employee[] 陣列，包含不同類型的員工
        Employee[] employees = new Employee[] {
            new SalariedEmployee("E001", "張大明", 52000.0),
            new SalariedEmployee("E002", "王小美", 48000.0),
            new HourlyEmployee("E003", "李大華", 250.0, 160),
            new HourlyEmployee("E004", "陳小芳", 180.0, 120),
            new CommissionEmployee("E005", "林小強", 30000.0, 150000.0, 0.08),
            new CommissionEmployee("E006", "黃小婷", 25000.0, 200000.0, 0.10)
        };
        
        System.out.println("\n========== 所有員工薪資明細 ==========");
        printAllEmployeeDetails(employees);
        
        System.out.println("\n========== 薪資統計分析 ==========");
        calculatePayrollStatistics(employees);
        
        System.out.println("\n========== 薪資排序（由高到低） ==========");
        printSortedByPay(employees);
        
        System.out.println("\n========== 各類型員工薪資統計 ==========");
        printPayByType(employees);
    }
    
    /**
     * 輸出所有員工的詳細資訊
     */
    public static void printAllEmployeeDetails(Employee[] employees) {
        System.out.println("編號\t姓名\t\t職稱\t\t薪資");
        System.out.println("----\t----\t\t----\t\t----");
        for (Employee emp : employees) {
            System.out.printf("%s\t%-8s\t%-10s\t%.2f%n",
                            emp.getId(),
                            emp.getName(),
                            emp.getJobTitle(),
                            emp.calculatePay());
        }
    }
    
    /**
     * 計算薪資統計：總薪資、平均薪資、最高薪資、最低薪資
     */
    public static void calculatePayrollStatistics(Employee[] employees) {
        if (employees == null || employees.length == 0) {
            System.out.println("沒有員工資料");
            return;
        }
        
        double totalPay = 0;
        double maxPay = Double.MIN_VALUE;
        double minPay = Double.MAX_VALUE;
        Employee maxEmployee = null;
        Employee minEmployee = null;
        
        for (Employee emp : employees) {
            double pay = emp.calculatePay();
            totalPay += pay;
            
            if (pay > maxPay) {
                maxPay = pay;
                maxEmployee = emp;
            }
            if (pay < minPay) {
                minPay = pay;
                minEmployee = emp;
            }
        }
        
        double averagePay = totalPay / employees.length;
        
        System.out.printf("員工人數：%d 人%n", employees.length);
        System.out.printf("薪資總額：%.2f 元%n", totalPay);
        System.out.printf("平均薪資：%.2f 元%n", averagePay);
        System.out.printf("最高薪資：%.2f 元（%s）%n", maxPay, maxEmployee.getName());
        System.out.printf("最低薪資：%.2f 元（%s）%n", minPay, minEmployee.getName());
    }
    
    /**
     * 依薪資由高到低排序並輸出
     */
    public static void printSortedByPay(Employee[] employees) {
        // 複製陣列避免影響原始資料
        Employee[] sorted = employees.clone();
        
        // 氣泡排序（由高到低）
        for (int i = 0; i < sorted.length - 1; i++) {
            for (int j = 0; j < sorted.length - 1 - i; j++) {
                if (sorted[j].calculatePay() < sorted[j + 1].calculatePay()) {
                    Employee temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }
        
        System.out.println("排名\t姓名\t\t職稱\t\t薪資");
        System.out.println("----\t----\t\t----\t\t----");
        for (int i = 0; i < sorted.length; i++) {
            System.out.printf("%d\t%-8s\t%-10s\t%.2f%n",
                            (i + 1),
                            sorted[i].getName(),
                            sorted[i].getJobTitle(),
                            sorted[i].calculatePay());
        }
    }
    
    /**
     * 依員工類型統計薪資
     */
    public static void printPayByType(Employee[] employees) {
        double salariedTotal = 0;
        double hourlyTotal = 0;
        double commissionTotal = 0;
        int salariedCount = 0;
        int hourlyCount = 0;
        int commissionCount = 0;
        
        for (Employee emp : employees) {
            if (emp instanceof SalariedEmployee) {
                salariedTotal += emp.calculatePay();
                salariedCount++;
            } else if (emp instanceof HourlyEmployee) {
                hourlyTotal += emp.calculatePay();
                hourlyCount++;
            } else if (emp instanceof CommissionEmployee) {
                commissionTotal += emp.calculatePay();
                commissionCount++;
            }
        }
        
        System.out.println("員工類型\t人數\t薪資總額\t平均薪資");
        System.out.println("--------\t----\t--------\t--------");
        if (salariedCount > 0) {
            System.out.printf("月薪制\t\t%d\t%.2f\t\t%.2f%n",
                            salariedCount, salariedTotal, salariedTotal / salariedCount);
        }
        if (hourlyCount > 0) {
            System.out.printf("時薪制\t\t%d\t%.2f\t\t%.2f%n",
                            hourlyCount, hourlyTotal, hourlyTotal / hourlyCount);
        }
        if (commissionCount > 0) {
            System.out.printf("業務制\t\t%d\t%.2f\t\t%.2f%n",
                            commissionCount, commissionTotal, commissionTotal / commissionCount);
        }
    }
}

/**
 * 抽象員工類別
 */
abstract class Employee {
    private String id;          // 員工編號
    private String name;        // 員工姓名
    
    /**
     * 建構子
     */
    public Employee(String id, String name) {
        this.id = id;
        this.name = name;
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
     * 取得職稱（抽象方法，由子類別實作）
     */
    public abstract String getJobTitle();
    
    /**
     * 計算薪資（抽象方法，由子類別實作）
     */
    public abstract double calculatePay();
    
    @Override
    public String toString() {
        return String.format("%s（%s）", name, getJobTitle());
    }
}

/**
 * 月薪制員工 - 固定月薪
 */
class SalariedEmployee extends Employee {
    private double monthlySalary;    // 月薪
    
    /**
     * 建構子
     * @param id 員工編號
     * @param name 員工姓名
     * @param monthlySalary 月薪（負數時轉為 0）
     */
    public SalariedEmployee(String id, String name, double monthlySalary) {
        super(id, name);
        this.monthlySalary = (monthlySalary < 0) ? 0 : monthlySalary;
    }
    
    /**
     * 取得職稱
     */
    @Override
    public String getJobTitle() {
        return "月薪制";
    }
    
    /**
     * 計算薪資 - 直接回傳月薪
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
}

/**
 * 時薪制員工 - 時薪 × 工作時數
 */
class HourlyEmployee extends Employee {
    private double hourlyRate;      // 時薪
    private int hoursWorked;        // 工作時數
    
    /**
     * 建構子
     * @param id 員工編號
     * @param name 員工姓名
     * @param hourlyRate 時薪（負數時轉為 0）
     * @param hoursWorked 工作時數（負數時轉為 0）
     */
    public HourlyEmployee(String id, String name, double hourlyRate, int hoursWorked) {
        super(id, name);
        this.hourlyRate = (hourlyRate < 0) ? 0 : hourlyRate;
        this.hoursWorked = (hoursWorked < 0) ? 0 : hoursWorked;
    }
    
    /**
     * 取得職稱
     */
    @Override
    public String getJobTitle() {
        return "時薪制";
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
}

/**
 * 業務制員工 - 底薪 + 業績獎金
 */
class CommissionEmployee extends Employee {
    private double baseSalary;          // 底薪
    private double salesAmount;         // 業績金額
    private double commissionRate;      // 獎金比例
    
    /**
     * 建構子
     * @param id 員工編號
     * @param name 員工姓名
     * @param baseSalary 底薪（負數時轉為 0）
     * @param salesAmount 業績金額（負數時轉為 0）
     * @param commissionRate 獎金比例（負數時轉為 0）
     */
    public CommissionEmployee(String id, String name, 
                             double baseSalary, double salesAmount, double commissionRate) {
        super(id, name);
        this.baseSalary = (baseSalary < 0) ? 0 : baseSalary;
        this.salesAmount = (salesAmount < 0) ? 0 : salesAmount;
        this.commissionRate = (commissionRate < 0) ? 0 : commissionRate;
    }
    
    /**
     * 取得職稱
     */
    @Override
    public String getJobTitle() {
        return "業務制";
    }
    
    /**
     * 計算薪資 - 底薪 + 業績 × 獎金比例
     */
    @Override
    public double calculatePay() {
        return baseSalary + (salesAmount * commissionRate);
    }
    
    /**
     * 取得底薪
     */
    public double getBaseSalary() {
        return baseSalary;
    }
    
    /**
     * 取得業績金額
     */
    public double getSalesAmount() {
        return salesAmount;
    }
    
    /**
     * 取得獎金比例
     */
    public double getCommissionRate() {
        return commissionRate;
    }
}