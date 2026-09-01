import java.util.*;

/**
 * 物流成本網路
 * 使用加權有向圖管理物流路線和成本
 */
public class LogisticsWeightedGraph {
    
    /**
     * 邊的類別
     */
    public static class Edge {
        private final String from;
        private final String to;
        private int weight;  // 成本/距離/時間
        
        public Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        
        public String getFrom() {
            return from;
        }
        
        public String getTo() {
            return to;
        }
        
        public int getWeight() {
            return weight;
        }
        
        public void setWeight(int weight) {
            this.weight = weight;
        }
        
        @Override
        public String toString() {
            return String.format("%s → %s (成本: %d)", from, to, weight);
        }
        
        public String toShortString() {
            return String.format("%s→%s[%d]", from, to, weight);
        }
    }
    
    // 鄰接表：節點 -> 邊列表
    private Map<String, List<Edge>> adjacencyList;
    
    // 節點集合
    private Set<String> vertices;
    
    // 邊的快速查找 (用於更新和刪除)
    private Map<String, Map<String, Edge>> edgeMap;
    
    /**
     * 建構子
     */
    public LogisticsWeightedGraph() {
        this.adjacencyList = new HashMap<>();
        this.vertices = new HashSet<>();
        this.edgeMap = new HashMap<>();
    }
    
    /**
     * 新增節點 (物流中心/倉庫/配送點)
     * @param vertex 節點名稱
     * @return true 如果成功新增
     */
    public boolean addVertex(String vertex) {
        if (vertex == null || vertex.trim().isEmpty()) {
            throw new IllegalArgumentException("節點名稱不能為空");
        }
        
        String normalized = vertex.trim();
        
        if (vertices.contains(normalized)) {
            System.out.printf("⚠️ 節點 '%s' 已存在%n", normalized);
            return false;
        }
        
        vertices.add(normalized);
        adjacencyList.put(normalized, new ArrayList<>());
        edgeMap.put(normalized, new HashMap<>());
        
        System.out.printf("✅ 新增節點: %s%n", normalized);
        return true;
    }
    
    /**
     * 新增加權有向邊
     * @param from 起點
     * @param to 終點
     * @param weight 權重 (成本/距離/時間)
     * @return true 如果成功新增
     */
    public boolean addEdge(String from, String to, int weight) {
        // 驗證參數
        if (from == null || to == null) {
            throw new IllegalArgumentException("節點名稱不能為 null");
        }
        
        if (weight < 0) {
            throw new IllegalArgumentException("權重不能為負數");
        }
        
        String fromNorm = from.trim();
        String toNorm = to.trim();
        
        // 檢查節點是否存在
        if (!vertices.contains(fromNorm)) {
            System.out.printf("⚠️ 起點 '%s' 不存在%n", fromNorm);
            return false;
        }
        
        if (!vertices.contains(toNorm)) {
            System.out.printf("⚠️ 終點 '%s' 不存在%n", toNorm);
            return false;
        }
        
        if (fromNorm.equals(toNorm)) {
            System.out.println("⚠️ 不能新增自環 (同一節點)");
            return false;
        }
        
        // 檢查是否已存在邊
        if (edgeMap.get(fromNorm).containsKey(toNorm)) {
            System.out.printf("⚠️ 邊已存在: %s → %s (成本: %d)%n", 
                             fromNorm, toNorm, edgeMap.get(fromNorm).get(toNorm).getWeight());
            return false;
        }
        
        // 新增邊
        Edge edge = new Edge(fromNorm, toNorm, weight);
        adjacencyList.get(fromNorm).add(edge);
        edgeMap.get(fromNorm).put(toNorm, edge);
        
        System.out.printf("✅ 新增路線: %s → %s (成本: %d)%n", fromNorm, toNorm, weight);
        return true;
    }
    
    /**
     * 更新邊的權重
     * @param from 起點
     * @param to 終點
     * @param newWeight 新權重
     * @return true 如果成功更新
     */
    public boolean updateEdge(String from, String to, int newWeight) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("節點名稱不能為 null");
        }
        
        if (newWeight < 0) {
            throw new IllegalArgumentException("權重不能為負數");
        }
        
        String fromNorm = from.trim();
        String toNorm = to.trim();
        
        if (!vertices.contains(fromNorm)) {
            System.out.printf("⚠️ 起點 '%s' 不存在%n", fromNorm);
            return false;
        }
        
        if (!vertices.contains(toNorm)) {
            System.out.printf("⚠️ 終點 '%s' 不存在%n", toNorm);
            return false;
        }
        
        // 檢查邊是否存在
        if (!edgeMap.get(fromNorm).containsKey(toNorm)) {
            System.out.printf("⚠️ 邊不存在: %s → %s%n", fromNorm, toNorm);
            return false;
        }
        
        // 更新權重
        Edge edge = edgeMap.get(fromNorm).get(toNorm);
        int oldWeight = edge.getWeight();
        edge.setWeight(newWeight);
        
        System.out.printf("🔄 更新路線: %s → %s (成本: %d → %d)%n", 
                         fromNorm, toNorm, oldWeight, newWeight);
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
            throw new IllegalArgumentException("節點名稱不能為 null");
        }
        
        String fromNorm = from.trim();
        String toNorm = to.trim();
        
        if (!vertices.contains(fromNorm)) {
            System.out.printf("⚠️ 起點 '%s' 不存在%n", fromNorm);
            return false;
        }
        
        if (!vertices.contains(toNorm)) {
            System.out.printf("⚠️ 終點 '%s' 不存在%n", toNorm);
            return false;
        }
        
        // 檢查邊是否存在
        if (!edgeMap.get(fromNorm).containsKey(toNorm)) {
            System.out.printf("⚠️ 邊不存在: %s → %s%n", fromNorm, toNorm);
            return false;
        }
        
        // 刪除邊
        Edge edge = edgeMap.get(fromNorm).remove(toNorm);
        adjacencyList.get(fromNorm).remove(edge);
        
        System.out.printf("🗑️ 刪除路線: %s → %s (成本: %d)%n", 
                         fromNorm, toNorm, edge.getWeight());
        return true;
    }
    
    /**
     * 刪除節點及其所有相關邊
     * @param vertex 節點名稱
     * @return true 如果成功刪除
     */
    public boolean removeVertex(String vertex) {
        if (vertex == null || vertex.trim().isEmpty()) {
            throw new IllegalArgumentException("節點名稱不能為空");
        }
        
        String norm = vertex.trim();
        
        if (!vertices.contains(norm)) {
            System.out.printf("⚠️ 節點 '%s' 不存在%n", norm);
            return false;
        }
        
        // 刪除所有指向該節點的邊 (從其他節點的邊列表中移除)
        for (String from : vertices) {
            if (edgeMap.get(from).containsKey(norm)) {
                Edge edge = edgeMap.get(from).remove(norm);
                adjacencyList.get(from).remove(edge);
                System.out.printf("  🗑️ 移除相關邊: %s → %s%n", from, norm);
            }
        }
        
        // 刪除該節點的所有出邊
        adjacencyList.remove(norm);
        edgeMap.remove(norm);
        vertices.remove(norm);
        
        System.out.printf("🗑️ 刪除節點: %s%n", norm);
        return true;
    }
    
    /**
     * 查詢從指定節點出發的所有邊
     * @param from 起點
     * @return 邊列表
     */
    public List<Edge> getEdgesFrom(String from) {
        if (from == null || from.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String norm = from.trim();
        if (!vertices.contains(norm)) {
            System.out.printf("⚠️ 節點 '%s' 不存在%n", norm);
            return new ArrayList<>();
        }
        
        return new ArrayList<>(adjacencyList.get(norm));
    }
    
    /**
     * 查詢指定邊的權重
     * @param from 起點
     * @param to 終點
     * @return 權重，如果不存在則回傳 -1
     */
    public int getEdgeWeight(String from, String to) {
        if (from == null || to == null) {
            return -1;
        }
        
        String fromNorm = from.trim();
        String toNorm = to.trim();
        
        if (!vertices.contains(fromNorm) || !vertices.contains(toNorm)) {
            return -1;
        }
        
        if (!edgeMap.get(fromNorm).containsKey(toNorm)) {
            return -1;
        }
        
        return edgeMap.get(fromNorm).get(toNorm).getWeight();
    }
    
    /**
     * 查詢節點的傳出度
     * @param vertex 節點名稱
     * @return 傳出度
     */
    public int getOutDegree(String vertex) {
        if (vertex == null || vertex.trim().isEmpty()) {
            return 0;
        }
        
        String norm = vertex.trim();
        if (!vertices.contains(norm)) {
            return 0;
        }
        
        return adjacencyList.get(norm).size();
    }
    
    /**
     * 查詢節點的傳入度
     * @param vertex 節點名稱
     * @return 傳入度
     */
    public int getInDegree(String vertex) {
        if (vertex == null || vertex.trim().isEmpty()) {
            return 0;
        }
        
        String norm = vertex.trim();
        if (!vertices.contains(norm)) {
            return 0;
        }
        
        int count = 0;
        for (String from : vertices) {
            if (edgeMap.get(from).containsKey(norm)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 查詢所有節點
     * @return 節點集合
     */
    public Set<String> getVertices() {
        return new HashSet<>(vertices);
    }
    
    /**
     * 查詢所有邊
     * @return 邊列表
     */
    public List<Edge> getAllEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for (List<Edge> edges : adjacencyList.values()) {
            allEdges.addAll(edges);
        }
        return allEdges;
    }
    
    /**
     * 查詢節點是否存在
     * @param vertex 節點名稱
     * @return true 如果存在
     */
    public boolean containsVertex(String vertex) {
        if (vertex == null || vertex.trim().isEmpty()) {
            return false;
        }
        return vertices.contains(vertex.trim());
    }
    
    /**
     * 查詢邊是否存在
     * @param from 起點
     * @param to 終點
     * @return true 如果存在
     */
    public boolean containsEdge(String from, String to) {
        if (from == null || to == null) {
            return false;
        }
        
        String fromNorm = from.trim();
        String toNorm = to.trim();
        
        if (!vertices.contains(fromNorm) || !vertices.contains(toNorm)) {
            return false;
        }
        
        return edgeMap.get(fromNorm).containsKey(toNorm);
    }
    
    /**
     * 取得節點總數
     * @return 節點數量
     */
    public int getVertexCount() {
        return vertices.size();
    }
    
    /**
     * 取得邊總數
     * @return 邊數量
     */
    public int getEdgeCount() {
        int count = 0;
        for (List<Edge> edges : adjacencyList.values()) {
            count += edges.size();
        }
        return count;
    }
    
    /**
     * 計算從起點到終點的最短路徑 (Dijkstra 演算法)
     * @param start 起點
     * @param end 終點
     * @return 最短路徑結果
     */
    public PathResult findShortestPath(String start, String end) {
        if (start == null || end == null) {
            return new PathResult(null, -1);
        }
        
        String startNorm = start.trim();
        String endNorm = end.trim();
        
        if (!vertices.contains(startNorm) || !vertices.contains(endNorm)) {
            System.out.printf("⚠️ 節點不存在: %s 或 %s%n", startNorm, endNorm);
            return new PathResult(null, -1);
        }
        
        if (startNorm.equals(endNorm)) {
            List<String> path = Arrays.asList(startNorm);
            return new PathResult(path, 0);
        }
        
        // Dijkstra 演算法
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();
        
        // 初始化
        for (String vertex : vertices) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(startNorm, 0);
        pq.offer(new NodeDistance(startNorm, 0));
        
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            String currentVertex = current.vertex;
            
            if (visited.contains(currentVertex)) {
                continue;
            }
            visited.add(currentVertex);
            
            if (currentVertex.equals(endNorm)) {
                break;
            }
            
            // 檢查所有出邊
            for (Edge edge : adjacencyList.get(currentVertex)) {
                String neighbor = edge.getTo();
                int newDist = distances.get(currentVertex) + edge.getWeight();
                
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, currentVertex);
                    pq.offer(new NodeDistance(neighbor, newDist));
                }
            }
        }
        
        // 檢查是否可到達
        if (distances.get(endNorm) == Integer.MAX_VALUE) {
            return new PathResult(null, -1);
        }
        
        // 重建路徑
        List<String> path = new ArrayList<>();
        String current = endNorm;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        
        return new PathResult(path, distances.get(endNorm));
    }
    
    /**
     * 節點距離輔助類 (用於 Dijkstra)
     */
    private static class NodeDistance implements Comparable<NodeDistance> {
        String vertex;
        int distance;
        
        NodeDistance(String vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(NodeDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
    
    /**
     * 路徑結果類別
     */
    public static class PathResult {
        private final List<String> path;
        private final int totalCost;
        
        public PathResult(List<String> path, int totalCost) {
            this.path = path;
            this.totalCost = totalCost;
        }
        
        public List<String> getPath() {
            return path;
        }
        
        public int getTotalCost() {
            return totalCost;
        }
        
        public boolean isReachable() {
            return path != null && !path.isEmpty();
        }
        
        @Override
        public String toString() {
            if (!isReachable()) {
                return "無法到達";
            }
            return String.format("路徑: %s, 總成本: %d", 
                               String.join(" → ", path), totalCost);
        }
    }
    
    /**
     * 印出完整報告
     */
    public void printFullReport() {
        System.out.println("\n=== 物流成本網路報告 ===");
        
        if (vertices.isEmpty()) {
            System.out.println("無節點資料");
            return;
        }
        
        System.out.printf("節點總數: %d%n", getVertexCount());
        System.out.printf("路線總數: %d%n", getEdgeCount());
        System.out.println();
        
        // 節點詳細資訊
        System.out.println("節點詳細資訊:");
        System.out.printf("%-15s | %-8s | %-8s | %s%n", 
                         "節點", "傳出度", "傳入度", "傳出路線");
        System.out.println("---------------|----------|----------|------------------------------");
        
        List<String> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);
        
        for (String vertex : sortedVertices) {
            int outDegree = getOutDegree(vertex);
            int inDegree = getInDegree(vertex);
            List<Edge> edges = adjacencyList.get(vertex);
            
            System.out.printf("%-15s | %8d | %8d | %s%n",
                             vertex, outDegree, inDegree,
                             edges.isEmpty() ? "無" : edges.toString());
        }
        
        // 所有路線
        System.out.println("\n📋 所有路線:");
        List<Edge> allEdges = getAllEdges();
        if (allEdges.isEmpty()) {
            System.out.println("  無路線");
        } else {
            for (Edge edge : allEdges) {
                System.out.println("  " + edge);
            }
        }
        
        // 統計資訊
        System.out.println("\n📊 統計資訊:");
        int totalCost = 0;
        int maxCost = 0;
        int minCost = Integer.MAX_VALUE;
        Edge maxEdge = null;
        Edge minEdge = null;
        
        for (Edge edge : allEdges) {
            totalCost += edge.getWeight();
            if (edge.getWeight() > maxCost) {
                maxCost = edge.getWeight();
                maxEdge = edge;
            }
            if (edge.getWeight() < minCost) {
                minCost = edge.getWeight();
                minEdge = edge;
            }
        }
        
        if (!allEdges.isEmpty()) {
            System.out.printf("  總成本: %d%n", totalCost);
            System.out.printf("  平均成本: %.2f%n", (double) totalCost / allEdges.size());
            System.out.printf("  最大成本: %d (%s)%n", maxCost, maxEdge);
            System.out.printf("  最小成本: %d (%s)%n", minCost, minEdge);
        }
        System.out.println();
    }
    
    /**
     * 清空所有資料
     */
    public void clear() {
        adjacencyList.clear();
        vertices.clear();
        edgeMap.clear();
        System.out.println("🔄 已清空所有物流資料");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 物流成本網路系統測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：更新和刪除
        testUpdateAndDelete();
        
        // 測試 3：最短路徑
        testShortestPath();
        
        // 測試 4：邊界情況
        testEdgeCases();
        
        // 測試 5：實際應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- 測試 1: 基本功能 ---");
        
        LogisticsWeightedGraph logistics = new LogisticsWeightedGraph();
        
        // 新增節點
        System.out.println("新增物流節點:");
        String[] nodes = {"台北", "台中", "高雄", "新竹", "台南", "花蓮"};
        for (String node : nodes) {
            logistics.addVertex(node);
        }
        
        // 新增路線
        System.out.println("\n新增物流路線:");
        logistics.addEdge("台北", "新竹", 80);
        logistics.addEdge("台北", "台中", 160);
        logistics.addEdge("台北", "花蓮", 200);
        logistics.addEdge("新竹", "台中", 90);
        logistics.addEdge("台中", "高雄", 120);
        logistics.addEdge("台中", "台南", 100);
        logistics.addEdge("台南", "高雄", 50);
        logistics.addEdge("高雄", "花蓮", 300);
        
        logistics.printFullReport();
        
        // 查詢測試
        System.out.println("\n📋 查詢測試:");
        System.out.println("  getEdgesFrom('台北'): " + logistics.getEdgesFrom("台北"));
        System.out.println("  getEdgeWeight('台北', '台中'): " + logistics.getEdgeWeight("台北", "台中"));
        System.out.println("  getOutDegree('台中'): " + logistics.getOutDegree("台中"));
        System.out.println("  getInDegree('高雄'): " + logistics.getInDegree("高雄"));
        System.out.println("  containsEdge('台北', '花蓮'): " + logistics.containsEdge("台北", "花蓮"));
        System.out.println("  containsEdge('台北', '高雄'): " + logistics.containsEdge("台北", "高雄"));
        System.out.println();
    }
    
    /**
     * 測試更新和刪除
     */
    private static void testUpdateAndDelete() {
        System.out.println("--- 測試 2: 更新和刪除 ---");
        
        LogisticsWeightedGraph logistics = new LogisticsWeightedGraph();
        
        logistics.addVertex("A");
        logistics.addVertex("B");
        logistics.addVertex("C");
        logistics.addVertex("D");
        
        logistics.addEdge("A", "B", 10);
        logistics.addEdge("A", "C", 20);
        logistics.addEdge("B", "C", 15);
        logistics.addEdge("B", "D", 25);
        logistics.addEdge("C", "D", 10);
        
        System.out.println("原始網路:");
        logistics.printFullReport();
        
        // 更新權重
        System.out.println("\n更新路線:");
        logistics.updateEdge("A", "B", 12);
        logistics.updateEdge("B", "C", 18);
        
        // 嘗試更新不存在的邊
        System.out.println("\n嘗試更新不存在的邊:");
        logistics.updateEdge("A", "D", 30);
        
        // 刪除邊
        System.out.println("\n刪除路線:");
        logistics.removeEdge("C", "D");
        logistics.removeEdge("B", "D");
        
        // 刪除節點
        System.out.println("\n刪除節點:");
        logistics.removeVertex("C");
        
        System.out.println("\n最終網路:");
        logistics.printFullReport();
    }
    
    /**
     * 測試最短路徑
     */
    private static void testShortestPath() {
        System.out.println("--- 測試 3: 最短路徑 (Dijkstra) ---");
        
        LogisticsWeightedGraph logistics = new LogisticsWeightedGraph();
        
        // 建立物流網路
        String[] cities = {"台北", "新竹", "台中", "嘉義", "台南", "高雄", "屏東"};
        for (String city : cities) {
            logistics.addVertex(city);
        }
        
        logistics.addEdge("台北", "新竹", 80);
        logistics.addEdge("台北", "台中", 160);
        logistics.addEdge("新竹", "台中", 90);
        logistics.addEdge("台中", "嘉義", 80);
        logistics.addEdge("台中", "高雄", 180);
        logistics.addEdge("嘉義", "台南", 60);
        logistics.addEdge("嘉義", "高雄", 120);
        logistics.addEdge("台南", "高雄", 50);
        logistics.addEdge("高雄", "屏東", 30);
        logistics.addEdge("台北", "高雄", 330);
        
        System.out.println("物流網路:");
        logistics.printFullReport();
        
        // 路徑規劃
        System.out.println("\n🗺️ 最短路徑規劃:");
        String[][] paths = {
            {"台北", "高雄"},
            {"台北", "屏東"},
            {"新竹", "台南"},
            {"台中", "屏東"},
            {"台北", "花蓮"}  // 不存在的路徑
        };
        
        for (String[] path : paths) {
            PathResult result = logistics.findShortestPath(path[0], path[1]);
            System.out.printf("  %s → %s: %s%n", path[0], path[1], result);
        }
        System.out.println();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 4: 邊界情況 ---");
        
        LogisticsWeightedGraph logistics = new LogisticsWeightedGraph();
        
        // 測試 4.1: 空系統
        System.out.println("測試 4.1: 空系統");
        logistics.printFullReport();
        System.out.println("  getVertexCount: " + logistics.getVertexCount());
        System.out.println("  getEdgeCount: " + logistics.getEdgeCount());
        System.out.println();
        
        // 測試 4.2: 負權重拒絕
        System.out.println("測試 4.2: 負權重拒絕");
        try {
            logistics.addEdge("A", "B", -5);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確拒絕負權重: " + e.getMessage());
        }
        System.out.println();
        
        // 測試 4.3: 不存在節點
        System.out.println("測試 4.3: 不存在節點");
        logistics.addEdge("不存在", "A", 10);
        logistics.updateEdge("不存在", "A", 20);
        logistics.removeEdge("不存在", "A");
        logistics.getEdgesFrom("不存在");
        logistics.getEdgeWeight("不存在", "A");
        logistics.removeVertex("不存在");
        System.out.println();
        
        // 測試 4.4: 單一節點
        System.out.println("測試 4.4: 單一節點");
        logistics.addVertex("單一");
        logistics.printFullReport();
        System.out.println("  getOutDegree('單一'): " + logistics.getOutDegree("單一"));
        System.out.println("  getInDegree('單一'): " + logistics.getInDegree("單一"));
        System.out.println();
        
        // 測試 4.5: 自環拒絕
        System.out.println("測試 4.5: 自環拒絕");
        logistics.addEdge("單一", "單一", 10);
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 5: 實際應用場景 ---");
        System.out.println("🚚 全台物流配送網路");
        
        LogisticsWeightedGraph logistics = new LogisticsWeightedGraph();
        
        // 建立全台物流中心
        System.out.println("\n建立物流中心:");
        String[] centers = {
            "台北物流中心", "桃園轉運站", "新竹物流站", "台中文心站",
            "嘉義北港站", "台南永康站", "高雄前鎮站", "屏東潮州站",
            "宜蘭羅東站", "花蓮吉安站", "台東中興站"
        };
        
        for (String center : centers) {
            logistics.addVertex(center);
        }
        
        // 建立物流路線 (距離/成本)
        System.out.println("\n建立物流路線:");
        
        // 北部路線
        logistics.addEdge("台北物流中心", "桃園轉運站", 30);
        logistics.addEdge("台北物流中心", "新竹物流站", 70);
        logistics.addEdge("台北物流中心", "宜蘭羅東站", 80);
        logistics.addEdge("桃園轉運站", "新竹物流站", 40);
        
        // 中部路線
        logistics.addEdge("新竹物流站", "台中文心站", 90);
        logistics.addEdge("桃園轉運站", "台中文心站", 120);
        logistics.addEdge("台中文心站", "嘉義北港站", 80);
        
        // 南部路線
        logistics.addEdge("嘉義北港站", "台南永康站", 60);
        logistics.addEdge("嘉義北港站", "高雄前鎮站", 110);
        logistics.addEdge("台南永康站", "高雄前鎮站", 50);
        logistics.addEdge("高雄前鎮站", "屏東潮州站", 30);
        
        // 東部路線
        logistics.addEdge("宜蘭羅東站", "花蓮吉安站", 100);
        logistics.addEdge("花蓮吉安站", "台東中興站", 170);
        logistics.addEdge("高雄前鎮站", "台東中興站", 120);
        logistics.addEdge("屏東潮州站", "台東中興站", 90);
        
        logistics.printFullReport();
        
        // 物流路徑規劃
        System.out.println("\n🚚 物流路徑規劃:");
        String[][] deliveries = {
            {"台北物流中心", "高雄前鎮站"},
            {"台北物流中心", "花蓮吉安站"},
            {"新竹物流站", "屏東潮州站"},
            {"台中文心站", "台東中興站"},
            {"桃園轉運站", "高雄前鎮站"}
        };
        
        for (String[] delivery : deliveries) {
            PathResult result = logistics.findShortestPath(delivery[0], delivery[1]);
            if (result.isReachable()) {
                System.out.printf("  %s → %s%n", delivery[0], delivery[1]);
                System.out.printf("    路徑: %s%n", String.join(" → ", result.getPath()));
                System.out.printf("    總距離: %d 公里%n", result.getTotalCost());
            } else {
                System.out.printf("  %s → %s: 無法送達%n", delivery[0], delivery[1]);
            }
        }
        
        // 物流中心分析
        System.out.println("\n📊 物流中心分析:");
        
        // 找出樞紐中心 (傳出度最高)
        int maxOut = 0;
        String hub = "";
        for (String vertex : logistics.getVertices()) {
            int outDegree = logistics.getOutDegree(vertex);
            if (outDegree > maxOut) {
                maxOut = outDegree;
                hub = vertex;
            }
        }
        System.out.printf("  主要樞紐中心: %s (連接 %d 個路線)%n", hub, maxOut);
        
        // 找出最遠配送路線
        List<LogisticsWeightedGraph.Edge> allEdges = logistics.getAllEdges();
        int maxDistance = 0;
        LogisticsWeightedGraph.Edge longestEdge = null;
        for (LogisticsWeightedGraph.Edge edge : allEdges) {
            if (edge.getWeight() > maxDistance) {
                maxDistance = edge.getWeight();
                longestEdge = edge;
            }
        }
        if (longestEdge != null) {
            System.out.printf("  最長路線: %s (距離 %d 公里)%n", longestEdge, maxDistance);
        }
        
        // 成本分析
        int totalCost = 0;
        for (LogisticsWeightedGraph.Edge edge : allEdges) {
            totalCost += edge.getWeight();
        }
        System.out.printf("  總物流距離: %d 公里%n", totalCost);
        System.out.printf("  平均路線距離: %.2f 公里%n", 
                         (double) totalCost / allEdges.size());
        
        // 更新成本 (因油價上漲)
        System.out.println("\n📈 更新物流成本 (油價上漲):");
        logistics.updateEdge("台北物流中心", "新竹物流站", 85);
        logistics.updateEdge("台中文心站", "嘉義北港站", 95);
        logistics.updateEdge("高雄前鎮站", "屏東潮州站", 35);
        
        System.out.println("\n更新後的路徑規劃:");
        for (String[] delivery : deliveries) {
            PathResult result = logistics.findShortestPath(delivery[0], delivery[1]);
            if (result.isReachable()) {
                System.out.printf("  %s → %s: %d 公里%n", 
                                 delivery[0], delivery[1], result.getTotalCost());
            }
        }
    }
}