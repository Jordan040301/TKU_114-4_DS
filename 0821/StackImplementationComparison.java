import java.util.ArrayDeque;
import java.util.Deque;

class FixedIntStack {
    private int[] data;
    private int size;

    FixedIntStack(int capacity) {
        data = new int[Math.max(1, capacity)];
    }

    boolean push(int value) {
        if (size == data.length) {
            return false;
        }
        data[size++] = value;
        return true;
    }

    Integer pop() {
        return size == 0 ? null : data[--size];
    }
}

public class StackImplementationComparison {
    public static void main(String[] args) {
        FixedIntStack custom = new FixedIntStack(2);
        custom.push(10);
        custom.push(20);

        Deque<Integer> builtIn = new ArrayDeque<>();
        builtIn.push(10);
        builtIn.push(20);

        System.out.println("自訂 pop：" + custom.pop());
        System.out.println("內建 pop：" + builtIn.pop());
    }
}