public class RecursiveResultDemo {
    static int sumTo(int number) {
        if (number <= 0) {
            return 0;
        }
        return number + sumTo(number - 1);
    }

    static long factorial(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("negative number");
        }
        if (number <= 1) {
            return 1;
        }
        return number * factorial(number - 1);
    }

    public static void main(String[] args) {
        System.out.println("sum=" + sumTo(5));
        System.out.println("5!=" + factorial(5));
        System.out.println("0!=" + factorial(0));
    }
}