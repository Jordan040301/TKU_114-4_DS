import java.util.ArrayDeque;
import java.util.Deque;

public class ServiceQueueDemo {
    static String serveNext(Deque<String> waiting) {
        String customer = waiting.pollFirst();
        return customer == null ? "EMPTY" : customer;
    }

    public static void main(String[] args) {
        Deque<String> waiting = new ArrayDeque<>();

        waiting.offerLast("A101 Amy");
        waiting.offerLast("A102 Ben");
        waiting.offerLast("A103 Cara");

        System.out.println("下一位：" + waiting.peekFirst());
        System.out.println("服務：" + serveNext(waiting));
        System.out.println("服務：" + serveNext(waiting));
        System.out.println("剩餘：" + waiting);
    }
}