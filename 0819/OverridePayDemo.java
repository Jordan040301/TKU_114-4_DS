class PayEmployee {
    private String name;

    PayEmployee(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    int calculatePay() {
        return 0;
    }
}

class MonthlyEmployee extends PayEmployee {
    private int salary;

    MonthlyEmployee(String name, int salary) {
        super(name);
        this.salary = salary;
    }

    @Override
    int calculatePay() {
        return salary;
    }
}

class HourlyEmployee extends PayEmployee {
    private int hours;
    private int hourlyRate;

    HourlyEmployee(String name, int hours, int hourlyRate) {
        super(name);
        this.hours = hours;
        this.hourlyRate = hourlyRate;
    }

    @Override
    int calculatePay() {
        return hours * hourlyRate;
    }
}

public class OverridePayDemo {
    public static void main(String[] args) {
        MonthlyEmployee amy = new MonthlyEmployee("Amy", 50000);
        HourlyEmployee ben = new HourlyEmployee("Ben", 80, 220);

        System.out.println(amy.getName() + "：" + amy.calculatePay());
        System.out.println(ben.getName() + "：" + ben.calculatePay());
    }
}