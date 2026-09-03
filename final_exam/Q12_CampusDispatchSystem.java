import java.util.*;

public class Q12_CampusDispatchSystem {
    
    // 用普通类替代 record（兼容 JDK 11）
    public static class Request {
        private final String id;
        private final String location;
        private final int priority;
        private final long sequence;

        public Request(String id, String location, int priority, long sequence) {
            this.id = id;
            this.location = location;
            this.priority = priority;
            this.sequence = sequence;
        }

        public String id() { return id; }
        public String location() { return location; }
        public int priority() { return priority; }
        public long sequence() { return sequence; }

        @Override
        public String toString() {
            return "Request[id=" + id + ", location=" + location + 
                   ", priority=" + priority + ", sequence=" + sequence + "]";
        }
    }

    // 邻接表：无向图
    private Map<String, Set<String>> adjacencyList;
    // 请求ID去重
    private Set<String> requestIds;
    // 优先级队列（按 priority 升序，sequence 升序）
    private PriorityQueue<Request> pendingRequests;
    // 未完成请求的计数器
    private int pendingCount;

    public Q12_CampusDispatchSystem() {
        this.adjacencyList = new HashMap<>();
        this.requestIds = new HashSet<>();
        // Comparator: priority 升序 -> sequence 升序
        this.pendingRequests = new PriorityQueue<>(
            Comparator.comparingInt(Request::priority)
                      .thenComparingLong(Request::sequence)
        );
        this.pendingCount = 0;
    }

    /**
     * 添加服务地点
     */
    public boolean addLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return false;
        }
        String trimmed = location.trim();
        if (adjacencyList.containsKey(trimmed)) {
            return false;
        }
        adjacencyList.put(trimmed, new HashSet<>());
        return true;
    }

    /**
     * 添加道路（无向图）
     */
    public boolean addRoad(String first, String second) {
        if (first == null || second == null) {
            return false;
        }
        String f = first.trim();
        String s = second.trim();
        
        if (!adjacencyList.containsKey(f) || !adjacencyList.containsKey(s)) {
            return false;
        }
        if (f.equals(s)) {
            return false;
        }
        if (adjacencyList.get(f).contains(s)) {
            return false;
        }
        
        adjacencyList.get(f).add(s);
        adjacencyList.get(s).add(f);
        return true;
    }

    /**
     * 提交服务请求
     */
    public boolean submit(Request request) {
        if (request == null) {
            return false;
        }
        if (request.id() == null || request.id().trim().isEmpty()) {
            return false;
        }
        if (request.location() == null || request.location().trim().isEmpty()) {
            return false;
        }
        
        String id = request.id().trim();
        String location = request.location().trim();
        
        if (requestIds.contains(id)) {
            return false;
        }
        if (!adjacencyList.containsKey(location)) {
            return false;
        }
        
        Request newRequest = new Request(id, location, request.priority(), request.sequence());
        requestIds.add(id);
        pendingRequests.offer(newRequest);
        pendingCount++;
        
        return true;
    }

    /**
     * 获取从 serviceCenter 可达的最高优先级请求
     */
    public Request nextReachable(String serviceCenter) {
        if (serviceCenter == null || serviceCenter.trim().isEmpty()) {
            return null;
        }
        String center = serviceCenter.trim();
        
        if (!adjacencyList.containsKey(center)) {
            return null;
        }
        if (pendingRequests.isEmpty()) {
            return null;
        }
        
        List<Request> tempList = new ArrayList<>();
        Request result = null;
        
        while (!pendingRequests.isEmpty()) {
            Request req = pendingRequests.poll();
            
            if (!adjacencyList.containsKey(req.location())) {
                pendingCount--;
                requestIds.remove(req.id());
                continue;
            }
            
            if (isReachable(center, req.location())) {
                if (result == null) {
                    result = req;
                    pendingCount--;
                    requestIds.remove(req.id());
                    continue;
                }
            }
            
            tempList.add(req);
        }
        
        for (Request req : tempList) {
            pendingRequests.offer(req);
        }
        
        return result;
    }

    /**
     * 计算从 start 到 target 的最短路径（BFS）
     */
    public List<String> route(String start, String target) {
        if (start == null || target == null) {
            return new ArrayList<>();
        }
        String s = start.trim();
        String t = target.trim();
        
        if (!adjacencyList.containsKey(s) || !adjacencyList.containsKey(t)) {
            return new ArrayList<>();
        }
        if (s.equals(t)) {
            return Arrays.asList(s);
        }
        
        Queue<String> queue = new LinkedList<>();
        Map<String, String> predecessor = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        visited.add(s);
        queue.offer(s);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            List<String> neighbors = new ArrayList<>(adjacencyList.get(current));
            Collections.sort(neighbors);
            
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    predecessor.put(neighbor, current);
                    
                    if (neighbor.equals(t)) {
                        return reconstructPath(predecessor, s, t);
                    }
                    queue.offer(neighbor);
                }
            }
        }
        
        return new ArrayList<>();
    }

    /**
     * 重建路径
     */
    private List<String> reconstructPath(Map<String, String> predecessor, 
                                         String start, String target) {
        List<String> path = new ArrayList<>();
        String current = target;
        
        while (current != null && !current.equals(start)) {
            path.add(0, current);
            current = predecessor.get(current);
        }
        path.add(0, start);
        return path;
    }

    /**
     * 检查从 start 到 target 是否可达（BFS）
     */
    private boolean isReachable(String start, String target) {
        if (start.equals(target)) {
            return true;
        }
        
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        visited.add(start);
        queue.offer(start);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor)) {
                    if (neighbor.equals(target)) {
                        return true;
                    }
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        return false;
    }

    /**
     * 返回待处理请求数量
     */
    public int pendingCount() {
        return pendingCount;
    }

    /**
     * 获取待处理请求的快照（用于调试）
     */
    public List<Request> getPendingRequestsSnapshot() {
        return new ArrayList<>(pendingRequests);
    }

    // ========== main 测试 ==========

    public static void main(String[] args) {
        Q12_CampusDispatchSystem system = new Q12_CampusDispatchSystem();

        System.out.println("=== Test addLocation ===");
        System.out.println("Add 'A': " + system.addLocation("A"));
        System.out.println("Add 'B': " + system.addLocation("B"));
        System.out.println("Add 'C': " + system.addLocation("C"));
        System.out.println("Add 'D': " + system.addLocation("D"));
        System.out.println("Add 'E': " + system.addLocation("E"));
        System.out.println("Add 'A' (duplicate): " + system.addLocation("A"));

        System.out.println("\n=== Test addRoad ===");
        System.out.println("Add A-B: " + system.addRoad("A", "B"));
        System.out.println("Add B-C: " + system.addRoad("B", "C"));
        System.out.println("Add C-D: " + system.addRoad("C", "D"));
        System.out.println("Add D-E: " + system.addRoad("D", "E"));
        System.out.println("Add A-B (duplicate): " + system.addRoad("A", "B"));

        System.out.println("\n=== Test route ===");
        System.out.println("Route A -> E: " + system.route("A", "E"));
        System.out.println("Route A -> D: " + system.route("A", "D"));
        System.out.println("Route A -> A: " + system.route("A", "A"));

        System.out.println("\n=== Test submit ===");
        Request r1 = new Request("R001", "A", 3, 100);
        Request r2 = new Request("R002", "C", 1, 101);
        Request r3 = new Request("R003", "E", 2, 102);
        Request r4 = new Request("R004", "A", 1, 103);

        System.out.println("Submit R001: " + system.submit(r1));
        System.out.println("Submit R002: " + system.submit(r2));
        System.out.println("Submit R003: " + system.submit(r3));
        System.out.println("Submit R004: " + system.submit(r4));
        System.out.println("Pending count: " + system.pendingCount());

        System.out.println("\n=== Test nextReachable ===");
        Request next = system.nextReachable("A");
        System.out.println("Next reachable from A: " + (next != null ? next.id() : "null"));
        System.out.println("Pending count after: " + system.pendingCount());
        
        next = system.nextReachable("B");
        System.out.println("Next reachable from B: " + (next != null ? next.id() : "null"));
        System.out.println("Pending count after: " + system.pendingCount());

        System.out.println("\n=== Test unreachable requests ===");
        system.addLocation("Z");
        Request r5 = new Request("R005", "Z", 1, 200);
        System.out.println("Submit R005 (isolated): " + system.submit(r5));
        System.out.println("Pending count: " + system.pendingCount());
        
        next = system.nextReachable("A");
        System.out.println("Next reachable from A: " + (next != null ? next.id() : "null"));
        System.out.println("Pending count (should still have R005): " + system.pendingCount());
    }
}