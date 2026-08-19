class Customer {
    private String id;
    private String name;

    Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String label() {
        return id + " " + name;
    }
}

class Order {
    private String orderId;
    private Customer customer;
    private int total;

    Order(String orderId, Customer customer, int total) {
        this.orderId = orderId;
        this.customer = customer;
        this.total = Math.max(0, total);
    }

    String summary() {
        return orderId + " | " + customer.label() + " | $" + total;
    }
}

public class OrderCompositionDemo {
    public static void main(String[] args) {
        Customer customer = new Customer("C101", "Amy");
        Order order = new Order("O9001", customer, 2500);

        System.out.println(order.summary());
    }
}