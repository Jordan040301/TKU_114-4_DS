import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

class WorkTask {
    private String id;
    private String title;
    private boolean completed;

    WorkTask(String id, String title) {
        this.id = id;
        this.title = title;
    }

    String getId() {
        return id;
    }

    void complete() {
        completed = true;
    }

    void reopen() {
        completed = false;
    }

    @Override
    public String toString() {
        return id + " " + title + " completed=" + completed;
    }
}

public class WorkflowCollectionsDemo {
    public static void main(String[] args) {
        Map<String, WorkTask> tasksById = new LinkedHashMap<>();
        Deque<WorkTask> waiting = new ArrayDeque<>();
        Deque<WorkTask> completedHistory = new ArrayDeque<>();

        WorkTask first = new WorkTask("T101", "Backup");
        WorkTask second = new WorkTask("T102", "Update");

        tasksById.put(first.getId(), first);
        tasksById.put(second.getId(), second);
        waiting.offerLast(first);
        waiting.offerLast(second);

        WorkTask processed = waiting.pollFirst();
        processed.complete();
        completedHistory.push(processed);

        WorkTask undone = completedHistory.pollFirst();
        undone.reopen();
        waiting.offerFirst(undone);

        System.out.println("查詢：" + tasksById.get("T101"));
        System.out.println("下一筆：" + waiting.peekFirst());
        System.out.println("等待數：" + waiting.size());
    }
}