import java.util.*;

public class Q09_DfsPathSearch {

    public static List<String> dfs(Map<String, List<String>> graph, String start) {
        if (graph == null || start == null || !graph.containsKey(start)) {
            return new ArrayList<>();
        }

        Set<String> visited = new HashSet<>();
        List<String> result = new ArrayList<>();
        dfsRecursive(graph, start, visited, result);
        return result;
    }

    private static void dfsRecursive(Map<String, List<String>> graph, 
                                     String current, 
                                     Set<String> visited, 
                                     List<String> result) {
        visited.add(current);
        result.add(current);

        List<String> neighbors = graph.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfsRecursive(graph, neighbor, visited, result);
                }
            }
        }
    }

    public static boolean reachable(Map<String, List<String>> graph, 
                                   String start, String target) {
        if (graph == null || start == null || target == null) {
            return false;
        }
        if (!graph.containsKey(start) || !graph.containsKey(target)) {
            return false;
        }
        if (start.equals(target)) {
            return true;
        }

        Set<String> visited = new HashSet<>();
        return reachableRecursive(graph, start, target, visited);
    }

    private static boolean reachableRecursive(Map<String, List<String>> graph,
                                             String current,
                                             String target,
                                             Set<String> visited) {
        if (current.equals(target)) {
            return true;
        }

        visited.add(current);

        List<String> neighbors = graph.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    if (reachableRecursive(graph, neighbor, target, visited)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // ========== 测试图 ==========

    private static Map<String, List<String>> createTestGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("D"));
        graph.put("C", Arrays.asList("D", "E"));
        graph.put("D", Arrays.asList("E"));
        graph.put("E", Arrays.asList());
        return graph;
    }

    private static Map<String, List<String>> createCyclicGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B", "C"));
        graph.put("B", Arrays.asList("D"));
        graph.put("C", Arrays.asList("A"));  // 环：A -> C -> A
        graph.put("D", Arrays.asList("C"));
        graph.put("E", Arrays.asList());
        return graph;
    }

    // ✅ 修正：真正的不连通图
    private static Map<String, List<String>> createDisconnectedGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", Arrays.asList("B"));  // 组件1
        graph.put("B", Arrays.asList("A"));  // 组件1
        graph.put("C", Arrays.asList("D"));  // 组件2（与组件1不连通）
        graph.put("D", Arrays.asList("C"));  // 组件2
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

    private static void printGraph(Map<String, List<String>> graph) {
        System.out.println("Graph structure:");
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }

    // ========== main ==========

    public static void main(String[] args) {
        // 复杂图测试
        System.out.println("=== Test Complex Graph ===");
        Map<String, List<String>> complexGraph = createComplexGraph();
        printGraph(complexGraph);
        System.out.println("DFS from 'A': " + dfs(complexGraph, "A"));
        System.out.println("Reachable A -> F: " + reachable(complexGraph, "A", "F"));
        System.out.println("Reachable A -> G: " + reachable(complexGraph, "A", "G"));

        // 不连通图测试
        System.out.println("\n=== Test Disconnected Graph ===");
        Map<String, List<String>> disconnectedGraph = createDisconnectedGraph();
        printGraph(disconnectedGraph);
        System.out.println("DFS from 'A': " + dfs(disconnectedGraph, "A"));
        System.out.println("Reachable A -> C: " + reachable(disconnectedGraph, "A", "C")); // false

        // 无效输入测试
        System.out.println("\n=== Test Invalid Input ===");
        System.out.println("DFS with null graph: " + dfs(null, "A"));
        System.out.println("DFS with invalid start: " + dfs(complexGraph, "X"));
        System.out.println("Reachable with null target: " + reachable(complexGraph, "A", null));
        System.out.println("Reachable with missing target: " + reachable(complexGraph, "A", "X"));
    }
}