import java.util.*;

public class Q07_AdjacencyListGraph {
    // 邻接表：顶点 -> 出边列表（保持加入顺序）
    private Map<String, List<String>> adjacencyList;
    // 入度统计：顶点 -> 入度
    private Map<String, Integer> inDegreeMap;
    // 边计数器
    private int edgeCount;

    public Q07_AdjacencyListGraph() {
        this.adjacencyList = new LinkedHashMap<>(); // 保持顶点加入顺序
        this.inDegreeMap = new HashMap<>();
        this.edgeCount = 0;
    }

    /**
     * 添加顶点
     * @param vertex 顶点名称
     * @return 添加成功返回 true，已存在或 null 返回 false
     */
    public boolean addVertex(String vertex) {
        // 检查 null 或已存在
        if (vertex == null || adjacencyList.containsKey(vertex)) {
            return false;
        }
        
        adjacencyList.put(vertex, new ArrayList<>());
        inDegreeMap.put(vertex, 0);
        return true;
    }

    /**
     * 添加有向边
     * @param from 起点
     * @param to 终点
     * @return 添加成功返回 true，失败返回 false
     */
    public boolean addEdge(String from, String to) {
        // 检查顶点是否存在
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            return false;
        }
        
        // 检查 self-loop
        if (from.equals(to)) {
            return false;
        }
        
        // 检查重复边
        List<String> outgoing = adjacencyList.get(from);
        if (outgoing.contains(to)) {
            return false;
        }
        
        // 添加边
        outgoing.add(to);
        inDegreeMap.put(to, inDegreeMap.get(to) + 1);
        edgeCount++;
        return true;
    }

    /**
     * 移除有向边
     * @param from 起点
     * @param to 终点
     * @return 移除成功返回 true，失败返回 false
     */
    public boolean removeEdge(String from, String to) {
        // 检查顶点是否存在
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            return false;
        }
        
        List<String> outgoing = adjacencyList.get(from);
        if (!outgoing.contains(to)) {
            return false;
        }
        
        // 移除边
        outgoing.remove(to);
        inDegreeMap.put(to, inDegreeMap.get(to) - 1);
        edgeCount--;
        return true;
    }

    /**
     * 获取顶点的出边列表（按加入顺序）
     * @param vertex 顶点
     * @return 出边列表，顶点不存在返回空列表
     */
    public List<String> outgoing(String vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            return new ArrayList<>();
        }
        // 返回副本以保护内部数据
        return new ArrayList<>(adjacencyList.get(vertex));
    }

    /**
     * 计算顶点的入度
     * @param vertex 顶点
     * @return 入度，顶点不存在返回 0
     */
    public int inDegree(String vertex) {
        if (!inDegreeMap.containsKey(vertex)) {
            return 0;
        }
        return inDegreeMap.get(vertex);
    }

    /**
     * 返回总边数
     * @return 边总数
     */
    public int edgeCount() {
        return edgeCount;
    }

    /**
     * 获取所有顶点列表（用于测试）
     */
    public Set<String> getVertices() {
        return new HashSet<>(adjacencyList.keySet());
    }

    /**
     * 获取完整邻接表（用于测试）
     */
    public Map<String, List<String>> getAdjacencyList() {
        Map<String, List<String>> copy = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    // 测试代码
    public static void main(String[] args) {
        Q07_AdjacencyListGraph graph = new Q07_AdjacencyListGraph();

        // 测试 addVertex
        System.out.println("=== Test addVertex ===");
        System.out.println("Add A: " + graph.addVertex("A")); // true
        System.out.println("Add B: " + graph.addVertex("B")); // true
        System.out.println("Add C: " + graph.addVertex("C")); // true
        System.out.println("Add A (duplicate): " + graph.addVertex("A")); // false
        System.out.println("Add null: " + graph.addVertex(null)); // false
        System.out.println("Vertices: " + graph.getVertices());

        // 测试 addEdge
        System.out.println("\n=== Test addEdge ===");
        System.out.println("Add A->B: " + graph.addEdge("A", "B")); // true
        System.out.println("Add A->C: " + graph.addEdge("A", "C")); // true
        System.out.println("Add B->C: " + graph.addEdge("B", "C")); // true
        System.out.println("Add C->A: " + graph.addEdge("C", "A")); // true
        System.out.println("Add A->A (self-loop): " + graph.addEdge("A", "A")); // false
        System.out.println("Add A->B (duplicate): " + graph.addEdge("A", "B")); // false
        System.out.println("Add A->D (missing vertex): " + graph.addEdge("A", "D")); // false
        System.out.println("Add D->A (missing vertex): " + graph.addEdge("D", "A")); // false

        // 测试 outgoing
        System.out.println("\n=== Test outgoing ===");
        System.out.println("Outgoing of A: " + graph.outgoing("A")); // [B, C]
        System.out.println("Outgoing of B: " + graph.outgoing("B")); // [C]
        System.out.println("Outgoing of C: " + graph.outgoing("C")); // [A]
        System.out.println("Outgoing of D (missing): " + graph.outgoing("D")); // []

        // 测试 inDegree
        System.out.println("\n=== Test inDegree ===");
        System.out.println("In-degree of A: " + graph.inDegree("A")); // 1 (from C)
        System.out.println("In-degree of B: " + graph.inDegree("B")); // 1 (from A)
        System.out.println("In-degree of C: " + graph.inDegree("C")); // 2 (from A, B)
        System.out.println("In-degree of D (missing): " + graph.inDegree("D")); // 0

        // 测试 edgeCount
        System.out.println("\n=== Test edgeCount ===");
        System.out.println("Total edges: " + graph.edgeCount()); // 4

        // 测试 removeEdge
        System.out.println("\n=== Test removeEdge ===");
        System.out.println("Remove A->B: " + graph.removeEdge("A", "B")); // true
        System.out.println("Outgoing of A after remove: " + graph.outgoing("A")); // [C]
        System.out.println("In-degree of B after remove: " + graph.inDegree("B")); // 0
        System.out.println("Remove A->B (already removed): " + graph.removeEdge("A", "B")); // false
        System.out.println("Remove A->D (missing): " + graph.removeEdge("A", "D")); // false
        System.out.println("Total edges after remove: " + graph.edgeCount()); // 3

        // 测试传入顺序
        System.out.println("\n=== Test outgoing order ===");
        graph.addEdge("A", "D");
        graph.addEdge("A", "E");
        System.out.println("Outgoing of A after adding D, E: " + graph.outgoing("A")); // [C, D, E]

        // 测试边缘情况
        System.out.println("\n=== Test Edge Cases ===");
        System.out.println("Outgoing of missing vertex: " + graph.outgoing("X")); // []
        System.out.println("In-degree of missing vertex: " + graph.inDegree("X")); // 0
        System.out.println("Add edge between missing vertices: " + graph.addEdge("X", "Y")); // false
        System.out.println("Remove edge between missing vertices: " + graph.removeEdge("X", "Y")); // false

        // 测试保护性
        System.out.println("\n=== Test Protection ===");
        List<String> outgoingA = graph.outgoing("A");
        System.out.println("Outgoing of A before modification: " + outgoingA);
        try {
            outgoingA.add("Z"); // 尝试修改副本
            System.out.println("Modified copy (should not affect internal): " + outgoingA);
            System.out.println("Outgoing of A after modifying copy: " + graph.outgoing("A"));
        } catch (Exception e) {
            // 如果返回的是不可修改列表，也会被捕获
            System.out.println("Caught exception: " + e.getMessage());
        }
    }
}