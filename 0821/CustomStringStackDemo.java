class StringStack {
    private String[] data;
    private int size;

    StringStack(int capacity) {
        data = new String[Math.max(1, capacity)];
    }

    boolean push(String value) {
        if (value == null || size == data.length) {
            return false;
        }
        data[size] = value;
        size++;
        return true;
    }

    String pop() {
        if (size == 0) {
            return null;
        }
        size--;
        String value = data[size];
        data[size] = null;
        return value;
    }

    String peek() {
        return size == 0 ? null : data[size - 1];
    }

    int size() {
        return size;
    }
}

public class CustomStringStackDemo {
    public static void main(String[] args) {
        StringStack stack = new StringStack(2);

        System.out.println("push A：" + stack.push("A"));
        System.out.println("push B：" + stack.push("B"));
        System.out.println("push C：" + stack.push("C"));
        System.out.println("peek：" + stack.peek());
        System.out.println("pop：" + stack.pop());
        System.out.println("size：" + stack.size());
    }
}