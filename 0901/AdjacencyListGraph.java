import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjacencyListGraph {
    private final Map<String, Set<String>> adjacency = new LinkedHashMap<>();

    public boolean addVertex(String vertex) {
        if (vertex == null || vertex.isBlank()) return false;
        return adjacency.putIfAbsent(vertex.trim(), new LinkedHashSet<>()) == null;
    }

    public boolean addEdge(String first, String second) {
        if (!adjacency.containsKey(first) || !adjacency.containsKey(second)) return false;
        if (first.equals(second)) return false;
        boolean changed = adjacency.get(first).add(second);
        adjacency.get(second).add(first);
        return changed;
    }

    public boolean removeEdge(String first, String second) {
        if (!adjacency.containsKey(first) || !adjacency.containsKey(second)) return false;
        boolean changed = adjacency.get(first).remove(second);
        adjacency.get(second).remove(first);
        return changed;
    }

    public List<String> neighbors(String vertex) {
        Set<String> neighbors = adjacency.get(vertex);
        return neighbors == null ? List.of() : new ArrayList<>(neighbors);
    }

    public int edgeCount() {
        int degreeSum = 0;
        for (Set<String> neighbors : adjacency.values()) degreeSum += neighbors.size();
        return degreeSum / 2;
    }

    public static void main(String[] args) {
        AdjacencyListGraph graph = new AdjacencyListGraph();
        for (String vertex : List.of("A", "B", "C", "D")) graph.addVertex(vertex);
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("C", "D");
        graph.addEdge("A", "B");
        System.out.println("A=" + graph.neighbors("A"));
        System.out.println("edges=" + graph.edgeCount());
        System.out.println("missing=" + graph.neighbors("X"));
    }
}