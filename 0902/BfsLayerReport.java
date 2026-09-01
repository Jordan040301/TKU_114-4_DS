import java.util.*;

/**
 * BFS 層級報告
 * 使用廣度優先搜尋 (BFS) 計算每個頂點距離起點的最少邊數
 */
public class BfsLayerReport {
    
    /**
     * 圖的鄰接表表示法
     */
    private Map<String, Set<String>> adjacencyList;
    
    /**
     * BFS 結果類別
     */
    public static class BfsResult {
        private final Map<String, Integer> distances;      // 頂點 → 距離
        private final Map<String, String> predecessors;    // 頂點 → 前驅節點
        private final Map<Integer, List<String>> layers;   // 層級 → 頂點列表
        private final int maxDistance;                     // 最大距離
        
        public BfsResult(Map<String, Integer> distances, 
                        Map<String, String> predecessors,
                        Map<Integer, List<String>> layers,
                        int maxDistance) {
            this.distances = distances;
            this.predecessors = predecessors;
            this.layers = layers;
            this.maxDistance = maxDistance;
        }
        
        public Map<String, Integer> getDistances() {
            return distances;
        }
        
        public Map<String, String> getPredecessors() {
            return predecessors;
        }
        
        public Map<Integer, List<String>> getLayers() {
            return layers;
        }
        
        public int getMaxDistance() {
            return maxDistance;
        }
        
        public int getDistance(String vertex) {
            return distances.getOrDefault(vertex, -1);
        }
        
        public String getPredecessor(String vertex) {
            return predecessors.get(vertex);
        }
        
        public List<String> getLayer(int layer) {
            return layers.getOrDefault(layer, new ArrayList<>());
        }
        
        public boolean isReachable(String vertex) {
            return distances.containsKey(vertex) && distances.get(vertex) >= 0;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== BFS 層級報告 ===\n");
            
            for (int layer = 0; layer <= maxDistance; layer++) {
                List<String> vertices = layers.getOrDefault(layer, new ArrayList<>());
                sb.append(String.format("層級 %d (距離 %d): %s%n", 
                                       layer, layer, 
                                       vertices.isEmpty() ? "無" : vertices.toString()));
            }
            
            sb.append("\n詳細距離:\n");
            List<String> sorted = new ArrayList<>(distances.keySet());
            Collections.sort(sorted);
            for (String vertex : sorted) {
                int dist = distances.get(vertex);
                String pred = predecessors.get(vertex);
                sb.append(String.format("  %s → 距離 %d", vertex, dist));
                if (pred != null) {
                    sb.append(String.format(" (經由 %s)", pred));
                }
                sb.append("\n");
            }
            
            return sb.toString();
        }
    }
    
    /**
     * 建構子
     */
    public BfsLayerReport() {
        this.adjacencyList = new HashMap<>();
    }
    
    /**
     * 新增頂點
     * @param vertex 頂點名稱
     */
    public void addVertex(String vertex) {
        if (vertex == null || vertex.trim().isEmpty()) {
            throw new IllegalArgumentException("頂點名稱不能為空");
        }
        adjacencyList.putIfAbsent(vertex, new HashSet<>());
    }
    
    /**
     * 新增無向邊
     * @param v1 頂點1
     * @param v2 頂點2
     */
    public void addUndirectedEdge(String v1, String v2) {
        if (v1 == null || v2 == null) {
            throw new IllegalArgumentException("頂點名稱不能為 null");
        }
        
        if (!adjacencyList.containsKey(v1)) {
            addVertex(v1);
        }
        if (!adjacencyList.containsKey(v2)) {
            addVertex(v2);
        }
        
        adjacencyList.get(v1).add(v2);
        adjacencyList.get(v2).add(v1);
    }
    
    /**
     * 新增有向邊
     * @param from 起點
     * @param to 終點
     */
    public void addDirectedEdge(String from, String to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("頂點名稱不能為 null");
        }
        
        if (!adjacencyList.containsKey(from)) {
            addVertex(from);
        }
        if (!adjacencyList.containsKey(to)) {
            addVertex(to);
        }
        
        adjacencyList.get(from).add(to);
    }
    
    /**
     * 執行 BFS 並生成層級報告
     * @param start 起點
     * @return BFS 結果
     */
    public BfsResult bfs(String start) {
        if (start == null || start.trim().isEmpty()) {
            throw new IllegalArgumentException("起點不能為空");
        }
        
        if (!adjacencyList.containsKey(start)) {
            System.out.printf("⚠️ 起點 '%s' 不存在%n", start);
            return null;
        }
        
        // 初始化
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        Map<Integer, List<String>> layers = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        // 起點初始化
        distances.put(start, 0);
        predecessors.put(start, null);
        visited.add(start);
        queue.offer(start);
        
        // 層級 0
        layers.put(0, new ArrayList<>());
        layers.get(0).add(start);
        
        int maxDistance = 0;
        
        // BFS 主迴圈
        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDist = distances.get(current);
            
            for (String neighbor : adjacencyList.getOrDefault(current, new HashSet<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    distances.put(neighbor, currentDist + 1);
                    predecessors.put(neighbor, current);
                    queue.offer(neighbor);
                    
                    // 更新層級
                    int layer = currentDist + 1;
                    layers.computeIfAbsent(layer, k -> new ArrayList<>()).add(neighbor);
                    
                    if (layer > maxDistance) {
                        maxDistance = layer;
                    }
                }
            }
        }
        
        // 處理未到達的頂點
        for (String vertex : adjacencyList.keySet()) {
            if (!distances.containsKey(vertex)) {
                distances.put(vertex, -1);
                predecessors.put(vertex, null);
            }
        }
        
        return new BfsResult(distances, predecessors, layers, maxDistance);
    }
    
    /**
     * 印出層級報告
     * @param start 起點
     */
    public void printLayerReport(String start) {
        BfsResult result = bfs(start);
        if (result == null) {
            return;
        }
        
        System.out.println(result);
        
        // 額外統計
        printStatistics(result);
    }
    
    /**
     * 印出統計資訊
     */
    private void printStatistics(BfsResult result) {
        System.out.println("\n📊 統計資訊:");
        
        int reachable = 0;
        int unreachable = 0;
        int totalVertices = result.getDistances().size();
        
        for (int dist : result.getDistances().values()) {
            if (dist >= 0) {
                reachable++;
            } else {
                unreachable++;
            }
        }
        
        System.out.printf("  總頂點數: %d%n", totalVertices);
        System.out.printf("  可到達頂點: %d (%.1f%%)%n", reachable, 
                         (double) reachable / totalVertices * 100);
        System.out.printf("  不可到達頂點: %d (%.1f%%)%n", unreachable, 
                         (double) unreachable / totalVertices * 100);
        System.out.printf("  最大距離: %d%n", result.getMaxDistance());
        System.out.printf("  層級數: %d%n", result.getMaxDistance() + 1);
        
        // 計算平均距離
        int totalDist = 0;
        for (int dist : result.getDistances().values()) {
            if (dist >= 0) {
                totalDist += dist;
            }
        }
        if (reachable > 0) {
            System.out.printf("  平均距離: %.2f%n", (double) totalDist / reachable);
        }
        
        // 顯示每一層的節點數
        System.out.println("\n📈 層級分布:");
        for (int layer = 0; layer <= result.getMaxDistance(); layer++) {
            List<String> vertices = result.getLayer(layer);
            System.out.printf("  層級 %d: %d 個節點%n", layer, vertices.size());
        }
    }
    
    /**
     * 印出 BFS 樹
     */
    public void printBfsTree(String start) {
        BfsResult result = bfs(start);
        if (result == null) {
            return;
        }
        
        System.out.println("\n=== BFS 樹 ===");
        System.out.println("起點: " + start);
        
        // 建立父子關係
        Map<String, List<String>> tree = new HashMap<>();
        for (String vertex : result.getDistances().keySet()) {
            tree.put(vertex, new ArrayList<>());
        }
        
        for (String vertex : result.getDistances().keySet()) {
            String pred = result.getPredecessor(vertex);
            if (pred != null) {
                tree.get(pred).add(vertex);
            }
        }
        
        // 遞迴印出樹
        printTree(tree, start, 0);
        System.out.println();
    }
    
    /**
     * 遞迴印出樹結構
     */
    private void printTree(Map<String, List<String>> tree, String vertex, int level) {
        // 縮排
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        if (level > 0) {
            System.out.print("└─ ");
        }
        System.out.println(vertex);
        
        for (String child : tree.getOrDefault(vertex, new ArrayList<>())) {
            printTree(tree, child, level + 1);
        }
    }
    
    /**
     * 清空圖
     */
    public void clear() {
        adjacencyList.clear();
        System.out.println("🔄 已清空圖");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== BFS 層級報告測試 ===\n");
        
        // 測試 1：基本無向圖
        testBasicUndirectedGraph();
        
        // 測試 2：有向圖
        testDirectedGraph();
        
        // 測試 3：複雜圖
        testComplexGraph();
        
        // 測試 4：邊界情況
        testEdgeCases();
    }
    
    /**
     * 測試基本無向圖
     */
    private static void testBasicUndirectedGraph() {
        System.out.println("--- 測試 1: 基本無向圖 ---");
        
        BfsLayerReport graph = new BfsLayerReport();
        
        // 建立圖
        String[] vertices = {"A", "B", "C", "D", "E", "F", "G"};
        for (String v : vertices) {
            graph.addVertex(v);
        }
        
        graph.addUndirectedEdge("A", "B");
        graph.addUndirectedEdge("A", "C");
        graph.addUndirectedEdge("B", "D");
        graph.addUndirectedEdge("B", "E");
        graph.addUndirectedEdge("C", "F");
        graph.addUndirectedEdge("C", "G");
        graph.addUndirectedEdge("D", "E");
        graph.addUndirectedEdge("F", "G");
        
        System.out.println("圖結構:");
        graph.printLayerReport("A");
        graph.printBfsTree("A");
    }
    
    /**
     * 測試有向圖
     */
    private static void testDirectedGraph() {
        System.out.println("\n--- 測試 2: 有向圖 ---");
        
        BfsLayerReport graph = new BfsLayerReport();
        
        // 建立有向圖
        graph.addDirectedEdge("A", "B");
        graph.addDirectedEdge("A", "C");
        graph.addDirectedEdge("B", "D");
        graph.addDirectedEdge("B", "E");
        graph.addDirectedEdge("C", "E");
        graph.addDirectedEdge("C", "F");
        graph.addDirectedEdge("D", "G");
        graph.addDirectedEdge("E", "G");
        graph.addDirectedEdge("F", "G");
        graph.addDirectedEdge("G", "H");
        graph.addDirectedEdge("H", "I");
        
        System.out.println("有向圖 BFS (從 A 開始):");
        graph.printLayerReport("A");
        graph.printBfsTree("A");
        
        System.out.println("\n有向圖 BFS (從 D 開始):");
        graph.printLayerReport("D");
    }
    
    /**
     * 測試複雜圖
     */
    private static void testComplexGraph() {
        System.out.println("\n--- 測試 3: 複雜圖 (社群網路) ---");
        
        BfsLayerReport socialNetwork = new BfsLayerReport();
        
        // 建立社群網路
        String[] users = {
            "Alice", "Bob", "Charlie", "David", "Eve", "Frank",
            "Grace", "Henry", "Ivy", "Jack", "Kevin", "Lisa"
        };
        
        for (String user : users) {
            socialNetwork.addVertex(user);
        }
        
        // 好友關係
        socialNetwork.addUndirectedEdge("Alice", "Bob");
        socialNetwork.addUndirectedEdge("Alice", "Charlie");
        socialNetwork.addUndirectedEdge("Bob", "David");
        socialNetwork.addUndirectedEdge("Bob", "Eve");
        socialNetwork.addUndirectedEdge("Charlie", "Frank");
        socialNetwork.addUndirectedEdge("Charlie", "Grace");
        socialNetwork.addUndirectedEdge("David", "Henry");
        socialNetwork.addUndirectedEdge("Eve", "Ivy");
        socialNetwork.addUndirectedEdge("Frank", "Jack");
        socialNetwork.addUndirectedEdge("Grace", "Kevin");
        socialNetwork.addUndirectedEdge("Henry", "Lisa");
        socialNetwork.addUndirectedEdge("Ivy", "Lisa");
        socialNetwork.addUndirectedEdge("Jack", "Lisa");
        socialNetwork.addUndirectedEdge("Kevin", "Lisa");
        socialNetwork.addUndirectedEdge("Lisa", "Alice");
        
        System.out.println("社群網路 BFS (從 Alice 開始):");
        socialNetwork.printLayerReport("Alice");
        socialNetwork.printBfsTree("Alice");
        
        // 從不同起點
        System.out.println("\n社群網路 BFS (從 Lisa 開始):");
        socialNetwork.printLayerReport("Lisa");
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("\n--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空圖
        System.out.println("測試 4.1: 空圖");
        BfsLayerReport graph = new BfsLayerReport();
        graph.printLayerReport("A");
        System.out.println();
        
        // 測試 4.2: 單一頂點
        System.out.println("測試 4.2: 單一頂點");
        graph.addVertex("A");
        graph.printLayerReport("A");
        System.out.println();
        
        // 測試 4.3: 孤立頂點
        System.out.println("測試 4.3: 孤立頂點");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addUndirectedEdge("A", "B");
        graph.printLayerReport("A");
        System.out.println();
        
        // 測試 4.4: 不存在的起點
        System.out.println("測試 4.4: 不存在的起點");
        graph.printLayerReport("不存在");
        System.out.println();
        
        // 測試 4.5: 多個連通分量
        System.out.println("測試 4.5: 多個連通分量");
        BfsLayerReport graph2 = new BfsLayerReport();
        graph2.addUndirectedEdge("A", "B");
        graph2.addUndirectedEdge("B", "C");
        graph2.addUndirectedEdge("D", "E");
        graph2.addUndirectedEdge("E", "F");
        graph2.addUndirectedEdge("G", "H");
        graph2.printLayerReport("A");
        System.out.println();
        
        // 測試 4.6: 有向圖的不可達節點
        System.out.println("測試 4.6: 有向圖不可達節點");
        BfsLayerReport graph3 = new BfsLayerReport();
        graph3.addDirectedEdge("A", "B");
        graph3.addDirectedEdge("B", "C");
        graph3.addDirectedEdge("D", "E");  // D 無法從 A 到達
        graph3.addDirectedEdge("E", "F");
        graph3.printLayerReport("A");
    }
}