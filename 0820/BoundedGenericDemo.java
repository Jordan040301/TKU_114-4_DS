public class BoundedGenericDemo {
    static <T extends Number> double sum(T[] data) {
        double total = 0;
        for (T value : data) {
            total += value.doubleValue();
        }
        return total;
    }

    static <T extends Comparable<T>> T max(T first, T second) {
        return first.compareTo(second) >= 0 ? first : second;
    }

    public static void main(String[] args) {
        Integer[] integers = {10, 20, 30};
        Double[] doubles = {1.5, 2.5, 3.0};

        System.out.println("整數總和：" + sum(integers));
        System.out.println("小數總和：" + sum(doubles));
        System.out.println("較大字串：" + max("Java", "Graph"));
    }
}