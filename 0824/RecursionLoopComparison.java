public class RecursionLoopComparison {
    static int loopSum(int number) {
        int total = 0;
        for (int value = 1; value <= number; value++) {
            total += value;
        }
        return total;
    }

    static int recursiveSum(int number) {
        return number <= 0 ? 0 : number + recursiveSum(number - 1);
    }

    public static void main(String[] args) {
        for (int number : new int[]{0, 1, 5, 10}) {
            int loop = loopSum(number);
            int recursion = recursiveSum(number);
            System.out.println(number + " -> " + loop + ", " + recursion
                    + ", same=" + (loop == recursion));
        }
    }
}