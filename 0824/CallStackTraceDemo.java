public class CallStackTraceDemo {
    static void trace(int level) {
        System.out.println("enter " + level);
        if (level == 0) {
            System.out.println("base");
        } else {
            trace(level - 1);
        }
        System.out.println("leave " + level);
    }

    public static void main(String[] args) {
        trace(3);
    }
}