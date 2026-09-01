import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持工单类
 */
class Ticket {
    private String id;
    private int severity;
    private int createdOrder;
    
    public Ticket(String id, int severity, int createdOrder) {
        this.id = id;
        this.severity = severity;
        this.createdOrder = createdOrder;
    }
    
    public String getId() {
        return id;
    }
    
    public int getSeverity() {
        return severity;
    }
    
    public int getCreatedOrder() {
        return createdOrder;
    }
    
    @Override
    public String toString() {
        return id + "|" + severity + "|" + createdOrder;
    }
}

/**
 * 支持工单队列 - 工作排程系统
 */
public class SupportTicketQueue {
    private PriorityQueue<Ticket> ticketQueue;
    
    public SupportTicketQueue() {
        // 创建比较器：严重性越大越优先，严重性相同时创建顺序越小越优先
        Comparator<Ticket> ticketComparator = Comparator
            .comparingInt(Ticket::getSeverity)
            .reversed()  // 严重性降序（越大越优先）
            .thenComparingInt(Ticket::getCreatedOrder);  // 创建顺序升序（越小越早）
        
        ticketQueue = new PriorityQueue<>(ticketComparator);
    }
    
    /**
     * 添加工单到队列
     */
    public void addTicket(Ticket ticket) {
        ticketQueue.offer(ticket);
    }
    
    /**
     * 添加工单（便捷方法）
     */
    public void addTicket(String id, int severity, int createdOrder) {
        Ticket ticket = new Ticket(id, severity, createdOrder);
        ticketQueue.offer(ticket);
    }
    
    /**
     * 获取并移除最高优先级的工单
     */
    public Ticket pollTicket() {
        return ticketQueue.poll();
    }
    
    /**
     * 查看最高优先级的工单（不移除）
     */
    public Ticket peekTicket() {
        return ticketQueue.peek();
    }
    
    /**
     * 检查队列是否为空
     */
    public boolean isEmpty() {
        return ticketQueue.isEmpty();
    }
    
    /**
     * 获取队列大小
     */
    public int size() {
        return ticketQueue.size();
    }
    
    /**
     * 获取所有工单（按优先级顺序）
     */
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        PriorityQueue<Ticket> tempQueue = new PriorityQueue<>(ticketQueue);
        
        while (!tempQueue.isEmpty()) {
            tickets.add(tempQueue.poll());
        }
        
        return tickets;
    }
    
    /**
     * 打印当前队列状态
     */
    public void printQueue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }
        
        System.out.println("Current queue (by priority):");
        System.out.println("id|severity|createdOrder");
        System.out.println("-------------------------");
        
        List<Ticket> tickets = getAllTickets();
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
        System.out.println();
    }
    
    /**
     * 处理所有工单并输出
     */
    public void processAllTickets() {
        System.out.println("Processing all tickets (by priority):");
        System.out.println("id|severity|createdOrder");
        System.out.println("-------------------------");
        
        int processedCount = 0;
        while (!isEmpty()) {
            Ticket ticket = pollTicket();
            System.out.println(ticket);
            processedCount++;
        }
        
        System.out.println("\nTotal tickets processed: " + processedCount);
    }
    
    public static void main(String[] args) {
        System.out.println("=== Support Ticket Queue System ===\n");
        
        // 创建工单队列
        SupportTicketQueue queue = new SupportTicketQueue();
        
        // 添加测试工单
        System.out.println("Adding tickets:");
        System.out.println("Ticket: T001 | Severity: 3 | Order: 101");
        System.out.println("Ticket: T002 | Severity: 5 | Order: 102");
        System.out.println("Ticket: T003 | Severity: 2 | Order: 103");
        System.out.println("Ticket: T004 | Severity: 5 | Order: 104");
        System.out.println("Ticket: T005 | Severity: 4 | Order: 105");
        System.out.println("Ticket: T006 | Severity: 3 | Order: 106");
        System.out.println("Ticket: T007 | Severity: 5 | Order: 107");
        System.out.println("Ticket: T008 | Severity: 1 | Order: 108");
        System.out.println();
        
        // 实际添加工单
        queue.addTicket("T001", 3, 101);
        queue.addTicket("T002", 5, 102);
        queue.addTicket("T003", 2, 103);
        queue.addTicket("T004", 5, 104);
        queue.addTicket("T005", 4, 105);
        queue.addTicket("T006", 3, 106);
        queue.addTicket("T007", 5, 107);
        queue.addTicket("T008", 1, 108);
        
        // 显示当前队列状态
        queue.printQueue();
        
        // 测试 peek 操作
        System.out.println("Peek highest priority ticket:");
        Ticket peekTicket = queue.peekTicket();
        if (peekTicket != null) {
            System.out.println("  " + peekTicket);
        }
        System.out.println();
        
        // 处理所有工单
        queue.processAllTickets();
        
        // 验证队列为空
        System.out.println("\nQueue is empty: " + queue.isEmpty());
        
        // 测试边界情况
        System.out.println("\n=== Edge Case Tests ===");
        testEdgeCases();
    }
    
    /**
     * 测试边界情况
     */
    private static void testEdgeCases() {
        SupportTicketQueue queue = new SupportTicketQueue();
        
        System.out.println("Test 1: Empty queue operations");
        System.out.println("  Is empty: " + queue.isEmpty());
        System.out.println("  Size: " + queue.size());
        System.out.println("  Poll from empty: " + queue.pollTicket());
        System.out.println("  Peek from empty: " + queue.peekTicket());
        
        System.out.println("\nTest 2: Single ticket");
        queue.addTicket("SINGLE", 10, 999);
        System.out.println("  Before processing: " + queue.getAllTickets());
        queue.processAllTickets();
        System.out.println("  After processing: " + queue.isEmpty());
        
        System.out.println("\nTest 3: Same severity, different order");
        queue = new SupportTicketQueue();
        queue.addTicket("A001", 5, 200);
        queue.addTicket("A002", 5, 201);
        queue.addTicket("A003", 5, 202);
        System.out.println("  Same severity (5), different orders:");
        queue.printQueue();
    }
}