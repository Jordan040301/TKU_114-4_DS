import java.util.Arrays;

public class RecursiveArrayDemo {
    static int sum(int[] values, int index) {
        if (values == null || index >= values.length) {
            return 0;
        }
        return values[index] + sum(values, index + 1);
    }

    static int countEven(int[] values, int index) {
        if (values == null || index >= values.length) {
            return 0;
        }
        int current = values[index] % 2 == 0 ? 1 : 0;
        return current + countEven(values, index + 1);
    }

    static void printReverse(int[] values, int index) {
        if (values == null || index >= values.length) {
            return;
        }
        printReverse(values, index + 1);
        System.out.print(values[index] + " ");
    }

    public static void main(String[] args) {
        int[] values = {4, 7, 2, 9};
        System.out.println(Arrays.toString(values));
        System.out.println("sum=" + sum(values, 0));
        System.out.println("even=" + countEven(values, 0));
        printReverse(values, 0);
        System.out.println();
    }
}