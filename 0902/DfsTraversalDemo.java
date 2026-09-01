import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DfsTraversalDemo {
    static List<String> recursive(Map<String, List<String>> graph, String start) {
        List<String> result = new ArrayList<>();
        if (graph == null || start == null || !graph.containsKey(start)) return result;
        visit(graph, start, new HashSet<>(), result);
        return result;
    }

    private static void visit(Map<String, List<String>> graph, String current,
                              Set<String> visited, List<String> result) {
        if (!visited.add(current)) return;
        result.add(current);
        for (String next : graph.getOrDefault(current, List.of())) {
            if (graph.containsKey(next)) visit(graph, next, visited, result);
        }
    }

    static List<String> iterative(Map<String, List<String>> graph, String start) {
        List<String> result = new ArrayList<>();
        if (graph == null || start == null || !graph.containsKey(start)) return result;
        ArrayDeque<String> stack = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (!visited.add(current)) continue;
            result.add(current);
            List<String> neighbors = graph.getOrDefault(current, List.of());
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                String next = neighbors.get(i);
                if (graph.containsKey(next) && !visited.contains(next)) stack.push(next);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("D"));
        graph.put("C", List.of("D"));
        graph.put("D", List.of("A"));
        System.out.println(recursive(graph, "A"));
        System.out.println(iterative(graph, "A"));
    }
}