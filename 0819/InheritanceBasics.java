class Employee {
    private String id;
    private String name;

    Employee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String label() {
        return id + " " + name;
    }
}

class FullTimeEmployee extends Employee {
    private int monthlySalary;

    FullTimeEmployee(String id, String name, int monthlySalary) {
        super(id, name);
        this.monthlySalary = Math.max(0, monthlySalary);
    }

    int annualSalary() {
        return monthlySalary * 12;
    }
}

public class InheritanceBasics {
    public static void main(String[] args) {
        FullTimeEmployee employee =
            new FullTimeEmployee("E101", "Amy", 50000);

        System.out.println(employee.label());
        System.out.println("年薪：" + employee.annualSalary());
    }
}