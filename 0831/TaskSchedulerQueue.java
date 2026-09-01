import java.util.Comparator;
import java.util.PriorityQueue;

public class TaskSchedulerQueue {
    record Task(String id, int priority, long sequence) {}

    public static void main(String[] args) {
        Comparator<Task> order = Comparator
                .comparingInt(Task::priority)
                .thenComparingLong(Task::sequence)
                .thenComparing(Task::id);

        PriorityQueue<Task> tasks = new PriorityQueue<>(order);

        tasks.offer(new Task("normal-1", 3, 1));
        tasks.offer(new Task("urgent-2", 1, 4));
        tasks.offer(new Task("urgent-1", 1, 2));
        tasks.offer(new Task("medium-1", 2, 3));

        while (!tasks.isEmpty()) {
            Task task = tasks.poll();
            System.out.println(task.id() + " | priority=" + task.priority()
                    + ", sequence=" + task.sequence());
        }
    }
}