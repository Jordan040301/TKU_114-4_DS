import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class UnweightedShortestPath {
    static List<String> shortestPath(Map<String, List<String>> graph,
                                     String start, String target) {
        if (graph == null || !graph.containsKey(start) || !graph.containsKey(target)) {
            return List.of();
        }
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> previous = new HashMap<>();
        queue.offer(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(target)) break;
            for (String next : graph.getOrDefault(current, List.of())) {
                if (graph.containsKey(next) && visited.add(next)) {
                    previous.put(next, current);
                    queue.offer(next);
                }
            }
        }
        if (!visited.contains(target)) return List.of();
        List<String> path = new ArrayList<>();
        for (String at = target; at != null; at = previous.get(at)) path.add(at);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = Map.of(
                "A", List.of("B", "C"), "B", List.of("D"),
                "C", List.of("D", "E"), "D", List.of("F"),
                "E", List.of("F"), "F", List.of(), "X", List.of());
        System.out.println(shortestPath(graph, "A", "F"));
        System.out.println(shortestPath(graph, "A", "X"));
        System.out.println(shortestPath(graph, "A", "A"));
    }
}