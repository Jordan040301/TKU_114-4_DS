import java.util.ArrayDeque;
import java.util.Deque;

public class DequeEndsDemo {
    public static void main(String[] args) {
        Deque<String> tasks = new ArrayDeque<>();

        tasks.offerLast("Normal-1");
        tasks.offerLast("Normal-2");
        tasks.offerFirst("Urgent");

        System.out.println("目前：" + tasks);
        System.out.println("前端：" + tasks.peekFirst());
        System.out.println("後端：" + tasks.peekLast());
        System.out.println("處理：" + tasks.pollFirst());
        System.out.println("剩餘：" + tasks);
    }
}