import java.util.*;

public class Q01_PriorityRecord {

    // 用普通类替代 record（兼容 JDK 11）
    public static class Job {
        public final String id;
        public final int priority;
        public final long sequence;

        public Job(String id, int priority, long sequence) {
            this.id = id;
            this.priority = priority;
            this.sequence = sequence;
        }

        public String id() { return id; }
        public int priority() { return priority; }
        public long sequence() { return sequence; }
    }

    public static List<String> processOrder(List<Job> jobs) {
        // 处理 null 或 empty 输入
        if (jobs == null || jobs.isEmpty()) {
            return new ArrayList<>();
        }

        // 定义 Comparator：priority 升序 -> sequence 升序 -> id 字典序
        Comparator<Job> comparator = Comparator
                .comparingInt(Job::priority)
                .thenComparingLong(Job::sequence)
                .thenComparing(Job::id);

        // 使用 PriorityQueue
        PriorityQueue<Job> pq = new PriorityQueue<>(comparator);

        // 过滤掉 null job 并加入队列
        for (Job job : jobs) {
            if (job != null) {
                pq.offer(job);
            }
        }

        // 按顺序取出并收集 id
        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll().id());
        }

        return result;
    }

    // 测试
    public static void main(String[] args) {
        List<Job> jobs = Arrays.asList(
                new Job("b", 2, 100),
                new Job("a", 1, 100),
                new Job("c", 1, 99),
                null,
                new Job("d", 1, 100)
        );

        System.out.println(processOrder(jobs));
        // 输出: [c, a, d, b]
    }
}