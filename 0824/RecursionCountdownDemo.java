public class RecursionCountdownDemo {
    static void countDown(int number) {
        if (number <= 0) {
            System.out.println("GO");
            return;
        }
        System.out.println(number);
        countDown(number - 1);
    }

    public static void main(String[] args) {
        countDown(3);
        countDown(0);
    }
}