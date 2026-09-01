import java.util.List;

public class HeapIndexExplorer {
    static int parentIndex(int index) {
        return index <= 0 ? -1 : (index - 1) / 2;
    }

    static int leftIndex(int index) {
        return index * 2 + 1;
    }

    static int rightIndex(int index) {
        return index * 2 + 2;
    }

    static String valueAt(List<Integer> heap, int index) {
        return index >= 0 && index < heap.size()
                ? String.valueOf(heap.get(index)) : "none";
    }

    public static void main(String[] args) {
        List<Integer> heap = List.of(10, 20, 30, 40, 50, 60);

        for (int i = 0; i < heap.size(); i++) {
            int parent = parentIndex(i);
            int left = leftIndex(i);
            int right = rightIndex(i);
            System.out.printf(
                    "index=%d value=%d parent=%s left=%s right=%s%n",
                    i, heap.get(i), valueAt(heap, parent),
                    valueAt(heap, left), valueAt(heap, right));
        }
    }
}