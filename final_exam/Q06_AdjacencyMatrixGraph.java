import java.util.*;

public class Q06_AdjacencyMatrixGraph {
    private List<String> vertices;           // 頂點列表（按原始順序）
    private Map<String, Integer> vertexIndex; // 頂點到索引的映射
    private boolean[][] adjacencyMatrix;     // 鄰接矩陣

    /**
     * 建構函數：初始化圖
     * @param vertices 頂點列表
     */
    public Q06_AdjacencyMatrixGraph(List<String> vertices) {
        this.vertices = new ArrayList<>();
        this.vertexIndex = new HashMap<>();
        
        // 初始化頂點列表和索引映射
        if (vertices != null) {
            for (String vertex : vertices) {
                if (vertex != null && !vertexIndex.containsKey(vertex)) {
                    this.vertices.add(vertex);
                    vertexIndex.put(vertex, this.vertices.size() - 1);
                }
            }
        }
        
        // 初始化鄰接矩陣
        int n = this.vertices.size();
        adjacencyMatrix = new boolean[n][n];
    }

    /**
     * 添加邊（無向圖）
     * @param first 第一個頂點
     * @param second 第二個頂點
     * @return 添加成功返回 true，失敗返回 false
     */
    public boolean addEdge(String first, String second) {
        // 檢查頂點是否存在
        if (!vertexIndex.containsKey(first) || !vertexIndex.containsKey(second)) {
            return false;
        }
        
        // 檢查是否為 self-loop
        if (first.equals(second)) {
            return false;
        }
        
        int i = vertexIndex.get(first);
        int j = vertexIndex.get(second);
        
        // 檢查邊是否已存在（重複邊）
        if (adjacencyMatrix[i][j]) {
            return false;
        }
        
        // 添加邊（無向圖：對稱矩陣）
        adjacencyMatrix[i][j] = true;
        adjacencyMatrix[j][i] = true;
        return true;
    }

    /**
     * 移除邊
     * @param first 第一個頂點
     * @param second 第二個頂點
     * @return 移除成功返回 true，失敗返回 false
     */
    public boolean removeEdge(String first, String second) {
        // 檢查頂點是否存在
        if (!vertexIndex.containsKey(first) || !vertexIndex.containsKey(second)) {
            return false;
        }
        
        int i = vertexIndex.get(first);
        int j = vertexIndex.get(second);
        
        // 檢查邊是否存在
        if (!adjacencyMatrix[i][j]) {
            return false;
        }
        
        // 移除邊
        adjacencyMatrix[i][j] = false;
        adjacencyMatrix[j][i] = false;
        return true;
    }

    /**
     * 檢查邊是否存在
     * @param first 第一個頂點
     * @param second 第二個頂點
     * @return 存在返回 true，不存在返回 false
     */
    public boolean hasEdge(String first, String second) {
        // 檢查頂點是否存在（安全處理）
        if (!vertexIndex.containsKey(first) || !vertexIndex.containsKey(second)) {
            return false;
        }
        
        int i = vertexIndex.get(first);
        int j = vertexIndex.get(second);
        return adjacencyMatrix[i][j];
    }

    /**
     * 計算頂點的度數
     * @param vertex 頂點
     * @return 度數，若頂點不存在返回 0
     */
    public int degree(String vertex) {
        // 檢查頂點是否存在（安全處理）
        if (!vertexIndex.containsKey(vertex)) {
            return 0;
        }
        
        int index = vertexIndex.get(vertex);
        int degree = 0;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[index][i]) {
                degree++;
            }
        }
        return degree;
    }

    /**
     * 獲取頂點的鄰居列表（按頂點原始順序）
     * @param vertex 頂點
     * @return 鄰居列表，若頂點不存在返回空列表
     */
    public List<String> neighbors(String vertex) {
        // 檢查頂點是否存在（安全處理）
        if (!vertexIndex.containsKey(vertex)) {
            return new ArrayList<>();
        }
        
        int index = vertexIndex.get(vertex);
        List<String> neighbors = new ArrayList<>();
        
        // 按頂點原始順序遍歷
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[index][i]) {
                neighbors.add(vertices.get(i));
            }
        }
        return neighbors;
    }

    /**
     * 獲取頂點列表（用於測試）
     */
    public List<String> getVertices() {
        return new ArrayList<>(vertices);
    }

    /**
     * 獲取鄰接矩陣（用於測試）
     */
    public boolean[][] getAdjacencyMatrix() {
        return adjacencyMatrix.clone();
    }

    // 測試代碼
    public static void main(String[] args) {
        // 測試建構函數
        System.out.println("=== Test Constructor ===");
        List<String> vertices = Arrays.asList("A", "B", "C", "D");
        Q06_AdjacencyMatrixGraph graph = new Q06_AdjacencyMatrixGraph(vertices);
        System.out.println("Vertices: " + graph.getVertices());

        // 測試 addEdge
        System.out.println("\n=== Test addEdge ===");
        System.out.println("Add A-B: " + graph.addEdge("A", "B")); // true
        System.out.println("Add A-C: " + graph.addEdge("A", "C")); // true
        System.out.println("Add B-C: " + graph.addEdge("B", "C")); // true
        System.out.println("Add A-A (self-loop): " + graph.addEdge("A", "A")); // false
        System.out.println("Add A-B (duplicate): " + graph.addEdge("A", "B")); // false
        System.out.println("Add A-E (missing vertex): " + graph.addEdge("A", "E")); // false

        // 測試 hasEdge
        System.out.println("\n=== Test hasEdge ===");
        System.out.println("Has A-B: " + graph.hasEdge("A", "B")); // true
        System.out.println("Has B-C: " + graph.hasEdge("B", "C")); // true
        System.out.println("Has A-D: " + graph.hasEdge("A", "D")); // false
        System.out.println("Has A-E: " + graph.hasEdge("A", "E")); // false (missing vertex)

        // 測試 degree
        System.out.println("\n=== Test degree ===");
        System.out.println("Degree of A: " + graph.degree("A")); // 2 (B, C)
        System.out.println("Degree of B: " + graph.degree("B")); // 2 (A, C)
        System.out.println("Degree of D: " + graph.degree("D")); // 0
        System.out.println("Degree of E (missing): " + graph.degree("E")); // 0

        // 測試 neighbors
        System.out.println("\n=== Test neighbors ===");
        System.out.println("Neighbors of A: " + graph.neighbors("A")); // [B, C]
        System.out.println("Neighbors of B: " + graph.neighbors("B")); // [A, C]
        System.out.println("Neighbors of D: " + graph.neighbors("D")); // []
        System.out.println("Neighbors of E: " + graph.neighbors("E")); // []

        // 測試 removeEdge
        System.out.println("\n=== Test removeEdge ===");
        System.out.println("Remove A-B: " + graph.removeEdge("A", "B")); // true
        System.out.println("Has A-B after remove: " + graph.hasEdge("A", "B")); // false
        System.out.println("Degree of A after remove: " + graph.degree("A")); // 1 (only C)
        System.out.println("Neighbors of A after remove: " + graph.neighbors("A")); // [C]
        System.out.println("Remove A-B (already removed): " + graph.removeEdge("A", "B")); // false
        System.out.println("Remove A-E (missing): " + graph.removeEdge("A", "E")); // false

        // 測試邊界情況
        System.out.println("\n=== Test Edge Cases ===");
        System.out.println("Add edge between missing vertices: " + graph.addEdge("X", "Y")); // false
        System.out.println("Has edge between missing vertices: " + graph.hasEdge("X", "Y")); // false
        System.out.println("Degree of missing vertex: " + graph.degree("X")); // 0
        System.out.println("Neighbors of missing vertex: " + graph.neighbors("X")); // []

        // 測試鄰接矩陣結構
        System.out.println("\n=== Test Matrix Structure ===");
        boolean[][] matrix = graph.getAdjacencyMatrix();
        System.out.println("Matrix size: " + matrix.length + "x" + matrix[0].length);
        System.out.println("Matrix is symmetric: " + isSymmetric(matrix));
    }

    /**
     * 檢查矩陣是否對稱（用於測試）
     */
    private static boolean isSymmetric(boolean[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] != matrix[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }
}