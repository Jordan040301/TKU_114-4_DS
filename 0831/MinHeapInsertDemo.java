import java.util.ArrayList;
import java.util.List;

public class MinHeapInsertDemo {
    private final List<Integer> data = new ArrayList<>();

    public void add(int value) {
        data.add(value);
        int index = data.size() - 1;
        System.out.println("append " + value + " -> " + data);

        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data.get(parent) <= data.get(index)) break;
            swap(parent, index);
            System.out.println("swap  " + parent + "," + index + " -> " + data);
            index = parent;
        }
    }

    public Integer peek() {
        return data.isEmpty() ? null : data.get(0);
    }

    public List<Integer> snapshot() {
        return List.copyOf(data);
    }

    private void swap(int first, int second) {
        int temp = data.get(first);
        data.set(first, data.get(second));
        data.set(second, temp);
    }

    public static void main(String[] args) {
        MinHeapInsertDemo heap = new MinHeapInsertDemo();
        for (int value : new int[]{30, 10, 20, 50, 40, 15}) {
            heap.add(value);
        }
        System.out.println("heap=" + heap.snapshot());
        System.out.println("min=" + heap.peek());
    }
}