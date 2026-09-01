import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ConnectedComponentsDemo {
    static List<List<String>> components(Map<String, List<String>> graph) {
        List<List<String>> result = new ArrayList<>();
        if (graph == null) return result;
        Set<String> visited = new HashSet<>();
        for (String start : graph.keySet()) {
            if (visited.contains(start)) continue;
            List<String> component = new ArrayList<>();
            Queue<String> queue = new ArrayDeque<>();
            queue.offer(start);
            visited.add(start);
            while (!queue.isEmpty()) {
                String current = queue.poll();
                component.add(current);
                for (String next : graph.getOrDefault(current, List.of())) {
                    if (graph.containsKey(next) && visited.add(next)) queue.offer(next);
                }
            }
            result.add(component);
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B")); graph.put("B", List.of("A"));
        graph.put("C", List.of("D")); graph.put("D", List.of("C"));
        graph.put("E", List.of());
        System.out.println(components(graph));
    }
}