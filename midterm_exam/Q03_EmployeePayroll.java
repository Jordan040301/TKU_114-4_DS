/**
 * 檔名：Q03_EmployeePayroll.java
 * 功能：繼承與薪資計算系統
 * 說明：Employee 抽象類別，SalariedEmployee 和 HourlyEmployee 繼承
 *       使用 polymorphism 計算總薪資
 */

import java.util.List;

public class Q03_EmployeePayroll {

    // ========== 抽象類別：Employee ==========
    public static abstract class Employee {
        private final String id;
        private final String name;

        /**
         * 建構子
         * @param id 員工編號
         * @param name 員工姓名
         * @throws IllegalArgumentException 當 id 或 name 為 null 或空白時
         */
        protected Employee(String id, String name) {
            // id 不得為 null 或 blank
            String trimmedId = (id != null) ? id.trim() : null;
            if (trimmedId == null || trimmedId.isEmpty()) {
                throw new IllegalArgumentException("id 不能為 null 或空白字串");
            }

            // name 不得為 null 或 blank
            String trimmedName = (name != null) ? name.trim() : null;
            if (trimmedName == null || trimmedName.isEmpty()) {
                throw new IllegalArgumentException("name 不能為 null 或空白字串");
            }

            this.id = trimmedId;
            this.name = trimmedName;
        }

        /**
         * 取得員工編號
         * @return 員工編號
         */
        public String getID() {
            return id;
        }

        /**
         * 取得員工姓名
         * @return 員工姓名
         */
        public String getName() {
            return name;
        }

        /**
         * 計算月薪（抽象方法，由子類別實作）
         * @return 月薪金額
         */
        public abstract int monthlyPay();

        /**
         * 產生員工摘要
         * @return 格式為 "id|name|monthlyPay"
         */
        public String summary() {
            // 必須透過 polymorphism 呼叫實際子類別的 monthlyPay()
            return id + "|" + name + "|" + monthlyPay();
        }
    }

    // ========== 子類別：SalariedEmployee（固定薪資員工） ==========
    public static class SalariedEmployee extends Employee {
        private final int salary;  // 固定月薪

        /**
         * 建構子
         * @param id 員工編號
         * @param name 員工姓名
         * @param salary 固定月薪
         */
        public SalariedEmployee(String id, String name, int salary) {
            super(id, name);
            // salary 小於 0 時以 0 計算
            this.salary = Math.max(salary, 0);
        }

        @Override
        public int monthlyPay() {
            return salary;
        }
    }

    // ========== 子類別：HourlyEmployee（時薪員工） ==========
    public static class HourlyEmployee extends Employee {
        private final int hours;        // 總工時
        private final int hourlyRate;   // 時薪

        /**
         * 建構子
         * @param id 員工編號
         * @param name 員工姓名
         * @param hours 總工時
         * @param hourlyRate 時薪
         */
        public HourlyEmployee(String id, String name, int hours, int hourlyRate) {
            super(id, name);
            // hours 小於 0 時以 0 計算
            this.hours = Math.max(hours, 0);
            // hourlyRate 小於 0 時以 0 計算
            this.hourlyRate = Math.max(hourlyRate, 0);
        }

        @Override
        public int monthlyPay() {
            // 160 小時內按原時薪計算
            // 超過 160 小時的部分按 1.5 倍計算
            // 最後轉為整數
            if (hours <= 160) {
                return hours * hourlyRate;
            } else {
                int normalPay = 160 * hourlyRate;
                int overtimeHours = hours - 160;
                int overtimePay = (int) Math.round(overtimeHours * hourlyRate * 1.5);
                return normalPay + overtimePay;
            }
        }
    }

    // ========== 靜態工具方法 ==========

    /**
     * 計算所有員工的總薪資
     * @param employees 員工列表
     * @return 總薪資（加總所有非 null Employee；List 為 null 時回傳 0）
     */
    public static int totalPayroll(List<Employee> employees) {
        // List 為 null 時回傳 0
        if (employees == null) {
            return 0;
        }

        int total = 0;
        for (Employee emp : employees) {
            // 加總所有非 null Employee
            if (emp != null) {
                total += emp.monthlyPay();
            }
        }
        return total;
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試範例 =====");
        var employees = List.of(
            new Q03_EmployeePayroll.SalariedEmployee("E1", "Amy", 50000),
            new Q03_EmployeePayroll.HourlyEmployee("E2", "Bo", 170, 200)
        );
        System.out.println(employees.get(0).summary());  // E1|Amy|50000
        System.out.println(employees.get(1).summary());  // E2|Bo|35000
        System.out.println(Q03_EmployeePayroll.totalPayroll(employees)); // 85000
        System.out.println();

        // ===== 測試建構子驗證 =====
        System.out.println("===== 建構子驗證測試 =====");
        try {
            new Q03_EmployeePayroll.SalariedEmployee(null, "Amy", 50000);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ id 為 null 時拋出例外：" + e.getMessage());
        }

        try {
            new Q03_EmployeePayroll.SalariedEmployee("", "Amy", 50000);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ id 為空字串時拋出例外：" + e.getMessage());
        }

        try {
            new Q03_EmployeePayroll.SalariedEmployee("E3", null, 50000);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ name 為 null 時拋出例外：" + e.getMessage());
        }

        try {
            new Q03_EmployeePayroll.SalariedEmployee("E3", "   ", 50000);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ name 為空白字串時拋出例外：" + e.getMessage());
        }
        System.out.println();

        // ===== 測試 SalariedEmployee 負數處理 =====
        System.out.println("===== SalariedEmployee 負數處理測試 =====");
        Q03_EmployeePayroll.SalariedEmployee emp1 = 
            new Q03_EmployeePayroll.SalariedEmployee("E3", "Tom", -1000);
        System.out.println("salary=-1000 → monthlyPay() = " + emp1.monthlyPay()); // 0
        System.out.println("summary: " + emp1.summary()); // E3|Tom|0
        System.out.println();

        // ===== 測試 HourlyEmployee 負數處理 =====
        System.out.println("===== HourlyEmployee 負數處理測試 =====");
        Q03_EmployeePayroll.HourlyEmployee emp2 = 
            new Q03_EmployeePayroll.HourlyEmployee("E4", "Jerry", -10, -200);
        System.out.println("hours=-10, hourlyRate=-200 → monthlyPay() = " + emp2.monthlyPay()); // 0
        System.out.println("summary: " + emp2.summary()); // E4|Jerry|0
        System.out.println();

        // ===== 測試時薪計算（正常工時與加班） =====
        System.out.println("===== 時薪計算測試 =====");
        
        Q03_EmployeePayroll.HourlyEmployee emp3 = 
            new Q03_EmployeePayroll.HourlyEmployee("E5", "Alice", 160, 200);
        System.out.println("工時 160，時薪 200 → " + emp3.monthlyPay()); // 32000

        Q03_EmployeePayroll.HourlyEmployee emp4 = 
            new Q03_EmployeePayroll.HourlyEmployee("E6", "Bob", 170, 200);
        System.out.println("工時 170，時薪 200 → " + emp4.monthlyPay()); 
        // 正常：160*200=32000，加班：10*200*1.5=3000，總計 35000

        Q03_EmployeePayroll.HourlyEmployee emp5 = 
            new Q03_EmployeePayroll.HourlyEmployee("E7", "Charlie", 180, 150);
        System.out.println("工時 180，時薪 150 → " + emp5.monthlyPay());
        // 正常：160*150=24000，加班：20*150*1.5=4500，總計 28500
        System.out.println();

        // ===== 測試 totalPayroll() =====
        System.out.println("===== totalPayroll() 測試 =====");
        var payrollList = List.of(
            new Q03_EmployeePayroll.SalariedEmployee("E8", "David", 40000),
            new Q03_EmployeePayroll.HourlyEmployee("E9", "Eva", 160, 250),
            new Q03_EmployeePayroll.HourlyEmployee("E10", "Frank", 200, 180),
            null  // null 應該被跳過
        );
        System.out.println("總薪資：" + Q03_EmployeePayroll.totalPayroll(payrollList));
        // 40000 + (160*250=40000) + (160*180 + 40*180*1.5 = 28800 + 10800 = 39600) = 119600
        
        // 測試 null list
        System.out.println("List 為 null 時：" + Q03_EmployeePayroll.totalPayroll(null)); // 0
        System.out.println();

        // ===== 測試 Polymorphism（多型） =====
        System.out.println("===== Polymorphism 測試 =====");
        List<Q03_EmployeePayroll.Employee> polymorphicList = List.of(
            new Q03_EmployeePayroll.SalariedEmployee("E11", "Grace", 60000),
            new Q03_EmployeePayroll.HourlyEmployee("E12", "Henry", 165, 300)
        );
        for (Q03_EmployeePayroll.Employee emp : polymorphicList) {
            // 使用 summary()，內部呼叫 monthlyPay() 會自動分派到正確的子類別
            System.out.println(emp.summary());
        }
        // 預期輸出：
        // E11|Grace|60000
        // E12|Henry|51200（160*300 + 5*300*1.5 = 48000 + 2250 = 50250）
    }
}