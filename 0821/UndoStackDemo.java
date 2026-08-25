import java.util.ArrayDeque;
import java.util.Deque;

public class UndoStackDemo {
    static String undo(Deque<String> history) {
        String action = history.pollFirst();
        return action == null ? "EMPTY" : action;
    }

    public static void main(String[] args) {
        Deque<String> history = new ArrayDeque<>();

        history.push("Open file");
        history.push("Type title");
        history.push("Delete line");

        System.out.println("最近操作：" + history.peek());
        System.out.println("復原：" + undo(history));
        System.out.println("復原：" + undo(history));
        System.out.println("剩餘：" + history);
    }
}