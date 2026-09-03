import java.util.*;

public class Q10_UnweightedShortestPath {

    /**
     * 使用 BFS 查找从 start 到 target 的最短路徑
     * @param graph 邻接表表示的有向图
     * @param start 起始顶点
     * @param target 目标顶点
     * @return 最短路徑列表（包含 start 和 target），不可达返回空列表
     */
    public static List<String> shortestPath(Map<String, List<String>> graph, 
                                           String start, String target) {
        // 处理无效输入
        if (graph == null || start == null || target == null) {
            return new ArrayList<>();
        }
        if (!graph.containsKey(start) || !graph.containsKey(target)) {
            return new ArrayList<>();
        }
        
        // start == target：返回单元素列表
        if (start.equals(target)) {
            return Arrays.asList(start);
        }

        // BFS 数据结构
        Queue<String> queue = new LinkedList<>();
        Map<String, String> predecessor = new HashMap<>();  // 记录前驱顶点
        Set<String> visited = new HashSet<>();

        // 初始化：从 start 开始
        visited.add(start);
        queue.offer(start);

        // BFS 遍历
        while (!queue.isEmpty()) {
            String current = queue.poll();

            // 获取邻居列表
            List<String> neighbors = graph.get(current);
            if (neighbors != null) {
                // 按 adjacency List 顺序遍历
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        predecessor.put(neighbor, current);  // 记录前驱
                        
                        // 如果找到 target，立即重建路径并返回
                        if (neighbor.equals(target)) {
                            return reconstructPath(predecessor, start, target);
                        }
                        
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // 不可达：返回空列表
        return new ArrayList<>();
    }

    /**
     * 重建路径（从 target 回溯到 start）
     * @param predecessor 前驱映射
     * @param start 起始顶点
     * @param target 目标顶点
     * @return 完整路径列表
     */
    private static List<String> reconstructPath(Map<String, String> predecessor, 
                                                String start, String target) {
        List<String> path = new ArrayList<>();
        String current = target;

        // 从 target 回溯到 start
        while (current != null && !current.equals(start)) {
            path.add(0, current);  // 添加到开头
            current = predecessor.get(current);
        }

        // 添加 start
        path.add(0, start);
        return path;
    }

    // ========== 测试辅助方法 ==========

    private static Map<String, List<String>> createTestGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("D"));
        graph.put("C", Arrays.asList("D", "E"));
        graph.put("D", Arrays.asList("E"));
        graph.put("E", Arrays.asList());
        return graph;
    }

    private static Map<String, List<String>> createComplexGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("D", "E"));
        graph.put("C", Arrays.asList("F"));
        graph.put("D", Arrays.asList("F"));
        graph.put("E", Arrays.asList("G"));
        graph.put("F", Arrays.asList("H"));
        graph.put("G", Arrays.asList());
        graph.put("H", Arrays.asList());
        return graph;
    }

    private static Map<String, List<String>> createCyclicGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("D"));
        graph.put("C", Arrays.asList("A"));  // 环
        graph.put("D", Arrays.asList("C"));
        graph.put("E", Arrays.asList());
        return graph;
    }

    private static Map<String, List<String>> createDisconnectedGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B"));
        graph.put("B", Arrays.asList("A"));
        graph.put("C", Arrays.asList("D"));
        graph.put("D", Arrays.asList("C"));
        return graph;
    }

    private static void printGraph(Map<String, List<String>> graph) {
        System.out.println("Graph structure:");
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }

    // ========== main 测试 ==========

    public static void main(String[] args) {
        // 1. 基本测试图
        System.out.println("=== Test Basic Graph ===");
        Map<String, List<String>> graph = createTestGraph();
        printGraph(graph);
        System.out.println("Shortest path A -> E: " + shortestPath(graph, "A", "E"));
        System.out.println("Shortest path A -> D: " + shortestPath(graph, "A", "D"));
        System.out.println("Shortest path A -> A: " + shortestPath(graph, "A", "A"));

        // 2. 复杂图测试（多条路径）
        System.out.println("\n=== Test Complex Graph (Multiple Paths) ===");
        Map<String, List<String>> complexGraph = createComplexGraph();
        printGraph(complexGraph);
        System.out.println("Shortest path A -> F: " + shortestPath(complexGraph, "A", "F"));
        System.out.println("Shortest path A -> H: " + shortestPath(complexGraph, "A", "H"));
        System.out.println("Shortest path A -> G: " + shortestPath(complexGraph, "A", "G"));

        // 3. 有环图测试
        System.out.println("\n=== Test Cyclic Graph ===");
        Map<String, List<String>> cyclicGraph = createCyclicGraph();
        printGraph(cyclicGraph);
        System.out.println("Shortest path A -> D: " + shortestPath(cyclicGraph, "A", "D"));
        System.out.println("Shortest path A -> E: " + shortestPath(cyclicGraph, "A", "E"));

        // 4. 不连通图测试
        System.out.println("\n=== Test Disconnected Graph ===");
        Map<String, List<String>> disconnectedGraph = createDisconnectedGraph();
        printGraph(disconnectedGraph);
        System.out.println("Shortest path A -> C: " + shortestPath(disconnectedGraph, "A", "C"));

        // 5. 无效输入测试
        System.out.println("\n=== Test Invalid Input ===");
        System.out.println("Shortest path null graph: " + shortestPath(null, "A", "B"));
        System.out.println("Shortest path null start: " + shortestPath(graph, null, "B"));
        System.out.println("Shortest path null target: " + shortestPath(graph, "A", null));
        System.out.println("Shortest path missing start: " + shortestPath(graph, "X", "B"));
        System.out.println("Shortest path missing target: " + shortestPath(graph, "A", "Y"));

        // 6. 测试多条最短路徑（按 adjacency list 顺序）
        System.out.println("\n=== Test Multiple Shortest Paths ===");
        Map<String, List<String>> multiPathGraph = createMultiPathGraph();
        printGraph(multiPathGraph);
        System.out.println("Shortest path A -> D: " + shortestPath(multiPathGraph, "A", "D"));
        System.out.println("Shortest path A -> E: " + shortestPath(multiPathGraph, "A", "E"));
    }

    /**
     * 创建多条最短路徑的图（测试顺序）
     * A -> D 有两条路径：A-B-D 和 A-C-D，长度都是 2
     */
    private static Map<String, List<String>> createMultiPathGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B", "C"));  // B 在 C 前面
        graph.put("B", Arrays.asList("D"));
        graph.put("C", Arrays.asList("D"));
        graph.put("D", Arrays.asList());
        graph.put("E", Arrays.asList());
        return graph;
    }
}