import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrixGraph {
    private final List<String> vertices;
    private final boolean[][] edges;

    public AdjacencyMatrixGraph(List<String> vertices) {
        if (vertices == null || vertices.isEmpty()) {
            throw new IllegalArgumentException("vertices");
        }
        this.vertices = List.copyOf(vertices);
        this.edges = new boolean[vertices.size()][vertices.size()];
    }

    private int indexOf(String vertex) {
        int index = vertices.indexOf(vertex);
        if (index < 0) throw new IllegalArgumentException("unknown vertex: " + vertex);
        return index;
    }

    public void addEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        edges[a][b] = true;
        edges[b][a] = true;
    }

    public void removeEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        edges[a][b] = false;
        edges[b][a] = false;
    }

    public boolean hasEdge(String first, String second) {
        return edges[indexOf(first)][indexOf(second)];
    }

    public int degree(String vertex) {
        int row = indexOf(vertex);
        int degree = 0;
        for (boolean connected : edges[row]) if (connected) degree++;
        return degree;
    }

    public List<String> neighbors(String vertex) {
        int row = indexOf(vertex);
        List<String> result = new ArrayList<>();
        for (int column = 0; column < vertices.size(); column++) {
            if (edges[row][column]) result.add(vertices.get(column));
        }
        return result;
    }

    public static void main(String[] args) {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(List.of("A", "B", "C", "D"));
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("C", "D");
        System.out.println("A neighbors=" + graph.neighbors("A"));
        System.out.println("C degree=" + graph.degree("C"));
        System.out.println("B-C=" + graph.hasEdge("B", "C"));
        graph.removeEdge("A", "B");
        System.out.println("A-B=" + graph.hasEdge("A", "B"));
    }
}