import java.util.Arrays;

class IntDynamicArray {
    private int[] data;
    private int size;

    IntDynamicArray(int initialCapacity) {
        data = new int[Math.max(1, initialCapacity)];
    }

    void add(int value) {
        ensureCapacity();
        data[size] = value;
        size++;
    }

    int get(int index) {
        checkIndex(index);
        return data[index];
    }

    int remove(int index) {
        checkIndex(index);
        int removed = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        data[size] = 0;
        return removed;
    }

    int size() {
        return size;
    }

    int capacity() {
        return data.length;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
            System.out.println("resize -> " + data.length);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(data, size));
    }
}

public class CustomDynamicArrayDemo {
    public static void main(String[] args) {
        IntDynamicArray values = new IntDynamicArray(2);
        values.add(10);
        values.add(20);
        values.add(30);

        System.out.println(values);
        System.out.println("removed=" + values.remove(1));
        System.out.println(values);
        System.out.println("size=" + values.size()
                + ", capacity=" + values.capacity());
    }
}