import java.util.*;

public class Q08_BfsTraversal {

    /**
     * BFS 遍历，返回访问顺序
     * @param graph 邻接表表示的有向图（Map<String, List<String>>）
     * @param start 起始顶点
     * @return BFS 访问顺序列表，无效输入返回空列表
     */
    public static List<String> bfs(Map<String, List<String>> graph, String start) {
        // 处理无效输入
        if (graph == null || start == null || !graph.containsKey(start)) {
            return new ArrayList<>();
        }

        // 用于检查是否访问过
        Set<String> visited = new HashSet<>();
        // BFS 队列
        Queue<String> queue = new LinkedList<>();
        // 结果列表
        List<String> result = new ArrayList<>();

        // 初始化：从起始节点开始
        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // 获取当前节点的邻接列表（安全处理）
            List<String> neighbors = graph.get(current);
            if (neighbors != null) {
                // 按邻接列表顺序遍历
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 计算从 start 到各顶点的最短距离（BFS）
     * @param graph 邻接表表示的有向图（Map<String, List<String>>）
     * @param start 起始顶点
     * @return 距离映射 Map<String, Integer>，不可达顶点不放入 Map
     */
    public static Map<String, Integer> distanceFrom(Map<String, List<String>> graph, String start) {
        // 处理无效输入
        if (graph == null || start == null || !graph.containsKey(start)) {
            return new HashMap<>();
        }

        // 用于检查是否访问过
        Set<String> visited = new HashSet<>();
        // BFS 队列（存储顶点）
        Queue<String> queue = new LinkedList<>();
        // 距离映射
        Map<String, Integer> distance = new HashMap<>();

        // 初始化：起始节点的距离为 0
        visited.add(start);
        queue.offer(start);
        distance.put(start, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDist = distance.get(current);

            // 获取当前节点的邻接列表
            List<String> neighbors = graph.get(current);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                        distance.put(neighbor, currentDist + 1);
                    }
                }
            }
        }

        return distance;
    }

    /**
     * 辅助方法：判断图是否有效（用于测试）
     */
    private static boolean isValidGraph(Map<String, List<String>> graph, String start) {
        return graph != null && start != null && graph.containsKey(start);
    }

    // 测试代码
    public static void main(String[] args) {
        // 创建测试图
        System.out.println("=== Test BFS Traversal ===");
        Map<String, List<String>> graph = createTestGraph();
        printGraph(graph);

        // 测试 BFS 顺序
        System.out.println("\n=== Test BFS Order ===");
        System.out.println("BFS from 'A': " + bfs(graph, "A"));
        System.out.println("BFS from 'B': " + bfs(graph, "B"));
        System.out.println("BFS from 'D': " + bfs(graph, "D"));

        // 测试距离
        System.out.println("\n=== Test Distance ===");
        System.out.println("Distance from 'A': " + distanceFrom(graph, "A"));
        System.out.println("Distance from 'B': " + distanceFrom(graph, "B"));
        System.out.println("Distance from 'D': " + distanceFrom(graph, "D"));

        // 测试带环的图
        System.out.println("\n=== Test Cyclic Graph ===");
        Map<String, List<String>> cyclicGraph = createCyclicGraph();
        System.out.println("BFS from 'A' (cyclic): " + bfs(cyclicGraph, "A"));
        System.out.println("Distance from 'A' (cyclic): " + distanceFrom(cyclicGraph, "A"));

        // 测试无效输入
        System.out.println("\n=== Test Invalid Input ===");
        System.out.println("BFS with null graph: " + bfs(null, "A"));
        System.out.println("BFS with null start: " + bfs(graph, null));
        System.out.println("BFS with invalid start: " + bfs(graph, "X"));
        System.out.println("Distance with null graph: " + distanceFrom(null, "A"));
        System.out.println("Distance with null start: " + distanceFrom(graph, null));
        System.out.println("Distance with invalid start: " + distanceFrom(graph, "X"));

        // 测试不可达顶点
        System.out.println("\n=== Test Unreachable Vertices ===");
        Map<String, List<String>> disconnectedGraph = createDisconnectedGraph();
        System.out.println("Disconnected graph:");
        printGraph(disconnectedGraph);
        System.out.println("BFS from 'A': " + bfs(disconnectedGraph, "A"));
        System.out.println("Distance from 'A': " + distanceFrom(disconnectedGraph, "A"));

        // 测试不修改原图
        System.out.println("\n=== Test Graph Immutability ===");
        Map<String, List<String>> originalGraph = createTestGraph();
        Map<String, List<String>> originalCopy = new HashMap<>(originalGraph);
        bfs(originalGraph, "A");
        distanceFrom(originalGraph, "A");
        System.out.println("Original graph unchanged: " + originalGraph.equals(originalCopy));
    }

    /**
     * 创建测试用图
     */
    private static Map<String, List<String>> createTestGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("D"));
        graph.put("C", Arrays.asList("D", "E"));
        graph.put("D", Arrays.asList("E"));
        graph.put("E", Arrays.asList());
        return graph;
    }

    /**
     * 创建带环的图
     */
    private static Map<String, List<String>> createCyclicGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("D"));
        graph.put("C", Arrays.asList("A")); // 环：A -> C -> A
        graph.put("D", Arrays.asList("C"));
        return graph;
    }

    /**
     * 创建不连通图
     */
    private static Map<String, List<String>> createDisconnectedGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B"));
        graph.put("B", Arrays.asList("A"));
        graph.put("C", Arrays.asList("D")); // 不连通的部分
        graph.put("D", Arrays.asList("C"));
        return graph;
    }

    /**
     * 打印图结构
     */
    private static void printGraph(Map<String, List<String>> graph) {
        System.out.println("Graph structure:");
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }
}