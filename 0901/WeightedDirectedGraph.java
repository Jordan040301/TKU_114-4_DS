import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 加權有向圖
 * 使用鄰接串列表示法
 */
public class WeightedDirectedGraph {
    
    /**
     * 邊的記錄類別 (使用傳統類別以相容 Java 8-10)
     */
    public static class Edge {
        private final String to;
        private final int weight;
        
        public Edge(String to, int weight) {
            if (to == null || to.isBlank()) {
                throw new IllegalArgumentException("to 不能為 null 或空白");
            }
            if (weight < 0) {
                throw new IllegalArgumentException("weight 不能為負數");
            }
            this.to = to;
            this.weight = weight;
        }
        
        public String getTo() {
            return to;
        }
        
        public int getWeight() {
            return weight;
        }
        
        @Override
        public String toString() {
            return String.format("-> %s (權重: %d)", to, weight);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            return weight == edge.weight && to.equals(edge.to);
        }
        
        @Override
        public int hashCode() {
            return 31 * to.hashCode() + weight;
        }
    }
    
    // 鄰接串列：每個頂點對應一個邊的列表
    private final Map<String, List<Edge>> outgoing;
    
    /**
     * 建構子
     */
    public WeightedDirectedGraph() {
        this.outgoing = new LinkedHashMap<>();
    }
    
    /**
     * 新增頂點
     * @param vertex 頂點名稱
     */
    public void addVertex(String vertex) {
        if (vertex == null || vertex.isBlank()) {
            throw new IllegalArgumentException("vertex 不能為 null 或空白");
        }
        outgoing.putIfAbsent(vertex, new ArrayList<>());
        System.out.printf("✅ 新增頂點: %s%n", vertex);
    }
    
    /**
     * 新增邊
     * @param from 起點
     * @param to 終點
     * @param weight 權重 (必須 >= 0)
     * @return true 如果成功新增，false 如果起點或終點不存在
     */
    public boolean addEdge(String from, String to, int weight) {
        // 驗證參數
        if (from == null || from.isBlank()) {
            throw new IllegalArgumentException("from 不能為 null 或空白");
        }
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("to 不能為 null 或空白");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("weight 不能為負數");
        }
        
        // 檢查頂點是否存在
        if (!outgoing.containsKey(from) || !outgoing.containsKey(to)) {
            System.out.printf("⚠️  無法新增邊: 頂點 %s 或 %s 不存在%n", from, to);
            return false;
        }
        
        // 檢查是否已存在相同的邊
        List<Edge> edges = outgoing.get(from);
        for (Edge edge : edges) {
            if (edge.getTo().equals(to) && edge.getWeight() == weight) {
                System.out.printf("⚠️  邊已存在: %s -> %s (權重: %d)%n", from, to, weight);
                return false;
            }
        }
        
        // 新增邊
        Edge newEdge = new Edge(to, weight);
        edges.add(newEdge);
        System.out.printf("✅ 新增邊: %s -> %s (權重: %d)%n", from, to, weight);
        return true;
    }
    
    /**
     * 檢查頂點是否存在
     * @param vertex 頂點名稱
     * @return true 如果存在
     */
    public boolean hasVertex(String vertex) {
        if (vertex == null || vertex.isBlank()) {
            return false;
        }
        return outgoing.containsKey(vertex);
    }
    
    /**
     * 檢查邊是否存在
     * @param from 起點
     * @param to 終點
     * @return true 如果存在
     */
    public boolean hasEdge(String from, String to) {
        if (from == null || to == null) {
            return false;
        }
        if (!outgoing.containsKey(from)) {
            return false;
        }
        
        for (Edge edge : outgoing.get(from)) {
            if (edge.getTo().equals(to)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 取得從指定頂點出發的所有邊
     * @param vertex 頂點名稱
     * @return 邊的列表，如果頂點不存在則回傳空列表
     */
    public List<Edge> getEdges(String vertex) {
        if (vertex == null || vertex.isBlank()) {
            return new ArrayList<>();
        }
        return outgoing.getOrDefault(vertex, new ArrayList<>());
    }
    
    /**
     * 取得所有頂點
     * @return 頂點集合
     */
    public List<String> getVertices() {
        return new ArrayList<>(outgoing.keySet());
    }
    
    /**
     * 取得頂點數量
     * @return 頂點數量
     */
    public int getVertexCount() {
        return outgoing.size();
    }
    
    /**
     * 取得邊的總數
     * @return 邊的總數
     */
    public int getEdgeCount() {
        int count = 0;
        for (List<Edge> edges : outgoing.values()) {
            count += edges.size();
        }
        return count;
    }
    
    /**
     * 刪除頂點 (同時刪除所有相關的邊)
     * @param vertex 要刪除的頂點
     * @return true 如果成功刪除
     */
    public boolean removeVertex(String vertex) {
        if (vertex == null || vertex.isBlank()) {
            return false;
        }
        if (!outgoing.containsKey(vertex)) {
            return false;
        }
        
        // 刪除所有指向該頂點的邊
        for (List<Edge> edges : outgoing.values()) {
            edges.removeIf(edge -> edge.getTo().equals(vertex));
        }
        
        // 刪除頂點
        outgoing.remove(vertex);
        System.out.printf("🗑️  刪除頂點: %s%n", vertex);
        return true;
    }
    
    /**
     * 刪除邊
     * @param from 起點
     * @param to 終點
     * @return true 如果成功刪除
     */
    public boolean removeEdge(String from, String to) {
        if (from == null || to == null) {
            return false;
        }
        if (!outgoing.containsKey(from)) {
            return false;
        }
        
        List<Edge> edges = outgoing.get(from);
        boolean removed = edges.removeIf(edge -> edge.getTo().equals(to));
        if (removed) {
            System.out.printf("🗑️  刪除邊: %s -> %s%n", from, to);
        }
        return removed;
    }
    
    /**
     * 取得從 from 到 to 的邊的權重
     * @param from 起點
     * @param to 終點
     * @return 權重，如果不存在則回傳 -1
     */
    public int getWeight(String from, String to) {
        if (from == null || to == null) {
            return -1;
        }
        if (!outgoing.containsKey(from)) {
            return -1;
        }
        
        for (Edge edge : outgoing.get(from)) {
            if (edge.getTo().equals(to)) {
                return edge.getWeight();
            }
        }
        return -1;
    }
    
    /**
     * 印出圖的結構
     */
    public void printGraph() {
        System.out.println("\n=== 加權有向圖 ===");
        System.out.println("頂點數: " + getVertexCount());
        System.out.println("邊數: " + getEdgeCount());
        System.out.println("\n鄰接串列:");
        
        if (outgoing.isEmpty()) {
            System.out.println("  (空圖)");
            return;
        }
        
        for (Map.Entry<String, List<Edge>> entry : outgoing.entrySet()) {
            String vertex = entry.getKey();
            List<Edge> edges = entry.getValue();
            System.out.printf("  %s", vertex);
            if (edges.isEmpty()) {
                System.out.println(" → (無出邊)");
            } else {
                for (Edge edge : edges) {
                    System.out.printf(" → %s (權重: %d)", edge.getTo(), edge.getWeight());
                }
                System.out.println();
            }
        }
        System.out.println();
    }
    
    /**
     * 印出鄰接矩陣
     */
    public void printAdjacencyMatrix() {
        List<String> vertices = getVertices();
        if (vertices.isEmpty()) {
            System.out.println("圖為空");
            return;
        }
        
        System.out.println("\n=== 鄰接矩陣 ===");
        int n = vertices.size();
        
        // 印出標頭
        System.out.print("    ");
        for (String v : vertices) {
            System.out.printf("%6s", v);
        }
        System.out.println();
        
        // 印出分隔線
        System.out.print("    ");
        for (int i = 0; i < n; i++) {
            System.out.print("------");
        }
        System.out.println();
        
        // 印出矩陣
        for (String from : vertices) {
            System.out.printf("%3s ", from);
            for (String to : vertices) {
                int weight = getWeight(from, to);
                if (weight >= 0) {
                    System.out.printf("%6d", weight);
                } else {
                    System.out.print("     -");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 清空圖
     */
    public void clear() {
        outgoing.clear();
        System.out.println("🔄 圖已清空");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 加權有向圖測試 ===\n");
        
        // 測試 1：基本操作
        testBasicOperations();
        
        // 測試 2：完整功能測試
        testFullFunctionality();
        
        // 測試 3：錯誤處理
        testErrorHandling();
        
        // 測試 4：實務應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試基本操作
     */
    private static void testBasicOperations() {
        System.out.println("--- 測試 1: 基本操作 ---");
        
        WeightedDirectedGraph graph = new WeightedDirectedGraph();
        
        // 新增頂點
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        
        // 新增邊
        graph.addEdge("A", "B", 5);
        graph.addEdge("A", "C", 3);
        graph.addEdge("B", "C", 2);
        graph.addEdge("B", "D", 4);
        graph.addEdge("C", "D", 1);
        graph.addEdge("C", "E", 6);
        graph.addEdge("D", "E", 2);
        
        graph.printGraph();
        graph.printAdjacencyMatrix();
        
        // 查詢
        System.out.println("查詢:");
        System.out.println("  hasVertex('A'): " + graph.hasVertex("A"));
        System.out.println("  hasVertex('Z'): " + graph.hasVertex("Z"));
        System.out.println("  hasEdge('A', 'B'): " + graph.hasEdge("A", "B"));
        System.out.println("  hasEdge('A', 'D'): " + graph.hasEdge("A", "D"));
        System.out.println("  getWeight('A', 'C'): " + graph.getWeight("A", "C"));
        System.out.println("  getWeight('A', 'Z'): " + graph.getWeight("A", "Z"));
        System.out.println("  getEdges('A'): " + graph.getEdges("A"));
        System.out.println();
    }
    
    /**
     * 測試完整功能
     */
    private static void testFullFunctionality() {
        System.out.println("--- 測試 2: 完整功能測試 ---");
        
        WeightedDirectedGraph graph = new WeightedDirectedGraph();
        
        // 建立圖
        graph.addVertex("台北");
        graph.addVertex("台中");
        graph.addVertex("高雄");
        graph.addVertex("花蓮");
        graph.addVertex("台東");
        
        graph.addEdge("台北", "台中", 2);
        graph.addEdge("台北", "花蓮", 3);
        graph.addEdge("台中", "高雄", 1);
        graph.addEdge("台中", "台東", 4);
        graph.addEdge("高雄", "台東", 2);
        graph.addEdge("花蓮", "台東", 3);
        
        graph.printGraph();
        
        // 刪除操作
        System.out.println("刪除邊: 台北 -> 花蓮");
        graph.removeEdge("台北", "花蓮");
        
        System.out.println("刪除頂點: 花蓮");
        graph.removeVertex("花蓮");
        
        graph.printGraph();
        
        // 統計資訊
        System.out.println("統計資訊:");
        System.out.println("  頂點數: " + graph.getVertexCount());
        System.out.println("  邊數: " + graph.getEdgeCount());
        System.out.println("  頂點清單: " + graph.getVertices());
        System.out.println();
    }
    
    /**
     * 測試錯誤處理
     */
    private static void testErrorHandling() {
        System.out.println("--- 測試 3: 錯誤處理 ---");
        
        WeightedDirectedGraph graph = new WeightedDirectedGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        
        // 測試 null 值
        System.out.println("測試 null 值:");
        try {
            graph.addVertex(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        
        try {
            graph.addEdge("A", null, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        
        // 測試負權重
        System.out.println("測試負權重:");
        try {
            graph.addEdge("A", "B", -1);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        
        // 測試不存在的頂點
        System.out.println("測試不存在的頂點:");
        boolean result = graph.addEdge("A", "Z", 5);
        System.out.println("  addEdge('A', 'Z', 5): " + result);
        
        result = graph.addEdge("Z", "A", 5);
        System.out.println("  addEdge('Z', 'A', 5): " + result);
        System.out.println();
    }
    
    /**
     * 測試實務應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 4: 實務應用場景 ---");
        System.out.println("模擬城市交通網路");
        
        WeightedDirectedGraph cityGraph = new WeightedDirectedGraph();
        
        // 新增城市
        String[] cities = {"台北", "新竹", "台中", "嘉義", "台南", "高雄"};
        for (String city : cities) {
            cityGraph.addVertex(city);
        }
        
        // 新增高速公路
        System.out.println("\n新增高速公路:");
        cityGraph.addEdge("台北", "新竹", 70);
        cityGraph.addEdge("台北", "台中", 160);
        cityGraph.addEdge("新竹", "台中", 90);
        cityGraph.addEdge("台中", "嘉義", 80);
        cityGraph.addEdge("嘉義", "台南", 60);
        cityGraph.addEdge("台南", "高雄", 50);
        cityGraph.addEdge("台中", "高雄", 180);
        cityGraph.addEdge("台北", "高雄", 330);
        
        cityGraph.printGraph();
        cityGraph.printAdjacencyMatrix();
        
        // 尋找從台北到高雄的路徑
        System.out.println("交通路線分析:");
        System.out.println("  台北 → 高雄 直達: " + cityGraph.getWeight("台北", "高雄") + " 公里");
        System.out.println("  台北 → 台中 → 高雄: " + 
                          (cityGraph.getWeight("台北", "台中") + 
                           cityGraph.getWeight("台中", "高雄")) + " 公里");
        System.out.println("  台北 → 新竹 → 台中 → 高雄: " + 
                          (cityGraph.getWeight("台北", "新竹") + 
                           cityGraph.getWeight("新竹", "台中") + 
                           cityGraph.getWeight("台中", "高雄")) + " 公里");
        
        // 檢查可達性
        System.out.println("\n可達性檢查:");
        System.out.println("  台北可到高雄: " + cityGraph.hasEdge("台北", "高雄"));
        System.out.println("  花蓮可到台東: " + cityGraph.hasEdge("花蓮", "台東"));
        System.out.println();
    }
}