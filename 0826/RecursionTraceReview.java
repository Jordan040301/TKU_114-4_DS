public class RecursionTraceReview {
    static int factorial(int number, int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "enter factorial(" + number + ")");

        if (number <= 1) {
            System.out.println(indent + "return 1");
            return 1;
        }

        int smallerResult = factorial(number - 1, depth + 1);
        int result = number * smallerResult;
        System.out.println(indent + "return " + number + " * "
                + smallerResult + " = " + result);
        return result;
    }

    public static void main(String[] args) {
        int answer = factorial(4, 0);
        System.out.println("answer=" + answer);
    }
}