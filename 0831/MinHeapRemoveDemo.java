
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class MinHeapRemoveDemo {
    private final List<Integer> data = new ArrayList<>();

    public void add(int value) {
        data.add(value);
        int index = data.size() - 1;
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data.get(parent) <= data.get(index)) break;
            swap(parent, index);
            index = parent;
        }
    }

    public int removeMin() {
        if (data.isEmpty()) throw new NoSuchElementException("heap is empty");
        int result = data.get(0);
        int last = data.remove(data.size() - 1);
        if (!data.isEmpty()) {
            data.set(0, last);
            bubbleDown(0);
        }
        return result;
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            if (left >= data.size()) return;

            int smaller = left;
            if (right < data.size() && data.get(right) < data.get(left)) {
                smaller = right;
            }
            if (data.get(index) <= data.get(smaller)) return;
            swap(index, smaller);
            index = smaller;
        }
    }

    private void swap(int first, int second) {
        int temp = data.get(first);
        data.set(first, data.get(second));
        data.set(second, temp);
    }

    public List<Integer> snapshot() {
        return List.copyOf(data);
    }

    public static void main(String[] args) {
        MinHeapRemoveDemo heap = new MinHeapRemoveDemo();
        for (int value : new int[]{30, 10, 20, 50, 40, 15}) heap.add(value);

        while (!heap.data.isEmpty()) {
            System.out.println("remove=" + heap.removeMin()
                    + " remaining=" + heap.snapshot());
        }
    }
}
