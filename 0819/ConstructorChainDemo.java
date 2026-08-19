abstract class Worker {
    private final String id;
    private final String name;

    Worker(String id, String name) {
        this.id = id;
        this.name = name;
        System.out.println("Worker constructor: " + id);
    }

    String label() {
        return id + " " + name;
    }

    abstract int monthlyPay();
}

class SalariedWorker extends Worker {
    private final int salary;

    SalariedWorker(String id, String name, int salary) {
        super(id, name);
        this.salary = Math.max(0, salary);
        System.out.println("SalariedWorker constructor: " + salary);
    }

    @Override
    int monthlyPay() {
        return salary;
    }
}

public class ConstructorChainDemo {
    public static void main(String[] args) {
        Worker worker = new SalariedWorker("E01", "Amy", 50000);
        System.out.println(worker.label() + " pay=" + worker.monthlyPay());
    }
}