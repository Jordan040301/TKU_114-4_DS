import java.util.ArrayList;
import java.util.List;

public class HeapifyAudit {
    static List<Integer> heapify(List<Integer> source) {
        List<Integer> heap = new ArrayList<>(source);
        for (int i = heap.size() / 2 - 1; i >= 0; i--) {
            bubbleDown(heap, i);
            System.out.println("after index " + i + " -> " + heap);
        }
        return heap;
    }

    static boolean isMinHeap(List<Integer> heap) {
        for (int parent = 0; parent < heap.size(); parent++) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            if (left < heap.size() && heap.get(parent) > heap.get(left)) return false;
            if (right < heap.size() && heap.get(parent) > heap.get(right)) return false;
        }
        return true;
    }

    private static void bubbleDown(List<Integer> heap, int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = index;
            if (left < heap.size() && heap.get(left) < heap.get(smallest)) smallest = left;
            if (right < heap.size() && heap.get(right) < heap.get(smallest)) smallest = right;
            if (smallest == index) return;
            int temp = heap.get(index);
            heap.set(index, heap.get(smallest));
            heap.set(smallest, temp);
            index = smallest;
        }
    }

    public static void main(String[] args) {
        List<Integer> source = List.of(45, 12, 30, 8, 20, 18);
        List<Integer> heap = heapify(source);
        System.out.println("result=" + heap);
        System.out.println("valid=" + isMinHeap(heap));
        System.out.println("invalid=" + isMinHeap(List.of(10, 5, 20)));
    }
}