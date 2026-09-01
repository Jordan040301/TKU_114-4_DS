import java.util.*;

/**
 * 有向圖可達性查詢
 * 支援多組可達查詢 (是否有路徑從起點到達終點)
 */
public class DirectedReachability {
    
    /**
     * 圖的鄰接表表示法
     */
    private Map<String, Set<String>> adjacencyList;
    
    /**
     * 可達性快取 (用於加速重複查詢)
     */
    private Map<String, Set<String>> reachabilityCache;
    
    /**
     * 建構子
     */
    public DirectedReachability() {
        this.adjacencyList = new HashMap<>();
        this.reachabilityCache = new HashMap<>();
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
        
        // 清除快取 (因為圖結構改變)
        reachabilityCache.clear();
    }
    
    /**
     * 新增無向邊 (轉換為兩條有向邊)
     * @param v1 頂點1
     * @param v2 頂點2
     */
    public void addUndirectedEdge(String v1, String v2) {
        addDirectedEdge(v1, v2);
        addDirectedEdge(v2, v1);
    }
    
    /**
     * 檢查從起點是否可以到達終點 (BFS)
     * @param start 起點
     * @param end 終點
     * @return true 如果可以到達
     */
    public boolean isReachable(String start, String end) {
        if (start == null || end == null) {
            return false;
        }
        
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return false;
        }
        
        if (start.equals(end)) {
            return true;
        }
        
        // 檢查快取
        String cacheKey = start + "→" + end;
        if (reachabilityCache.containsKey(start) && reachabilityCache.get(start).contains(end)) {
            return true;
        }
        
        // BFS
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(start);
        visited.add(start);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            for (String neighbor : adjacencyList.getOrDefault(current, new HashSet<>())) {
                if (neighbor.equals(end)) {
                    // 加入快取
                    cacheReachability(start, end);
                    return true;
                }
                
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        return false;
    }
    
    /**
     * 快取可達性結果
     */
    private void cacheReachability(String from, String to) {
        reachabilityCache.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }
    
    /**
     * 取得從起點可以到達的所有頂點
     * @param start 起點
     * @return 可到達的頂點集合
     */
    public Set<String> getReachableVertices(String start) {
        if (start == null || !adjacencyList.containsKey(start)) {
            return new HashSet<>();
        }
        
        // 檢查快取
        if (reachabilityCache.containsKey(start)) {
            return new HashSet<>(reachabilityCache.get(start));
        }
        
        Set<String> reachable = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        
        queue.offer(start);
        reachable.add(start);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            for (String neighbor : adjacencyList.getOrDefault(current, new HashSet<>())) {
                if (!reachable.contains(neighbor)) {
                    reachable.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        // 加入快取
        reachabilityCache.put(start, new HashSet<>(reachable));
        
        return reachable;
    }
    
    /**
     * 執行多組可達查詢
     * @param queries 查詢陣列 [start1, end1, start2, end2, ...]
     * @return 查詢結果列表
     */
    public List<QueryResult> queryReachability(String... queries) {
        if (queries.length % 2 != 0) {
            throw new IllegalArgumentException("查詢參數必須為成對的 (起點, 終點)");
        }
        
        List<QueryResult> results = new ArrayList<>();
        
        System.out.println("\n=== 多組可達查詢 ===");
        System.out.println("查詢 | 起點 → 終點 | 結果 | 路徑");
        System.out.println("-----|-------------|------|------");
        
        int queryId = 1;
        for (int i = 0; i < queries.length; i += 2) {
            String start = queries[i];
            String end = queries[i + 1];
            
            boolean reachable = isReachable(start, end);
            List<String> path = findPath(start, end);
            
            QueryResult result = new QueryResult(queryId, start, end, reachable, path);
            results.add(result);
            
            System.out.printf("%4d | %s → %s | %-4s | %s%n", 
                             queryId++, start, end, 
                             reachable ? "✅可達" : "❌不可達",
                             path.isEmpty() ? "無路徑" : String.join(" → ", path));
        }
        System.out.println();
        
        return results;
    }
    
    /**
     * 查詢結果類別
     */
    public static class QueryResult {
        public final int id;
        public final String start;
        public final String end;
        public final boolean reachable;
        public final List<String> path;
        
        public QueryResult(int id, String start, String end, boolean reachable, List<String> path) {
            this.id = id;
            this.start = start;
            this.end = end;
            this.reachable = reachable;
            this.path = path;
        }
        
        @Override
        public String toString() {
            return String.format("#%d: %s → %s = %s (路徑: %s)", 
                               id, start, end, 
                               reachable ? "可達" : "不可達",
                               path.isEmpty() ? "無" : String.join(" → ", path));
        }
    }
    
    /**
     * 尋找從起點到終點的路徑 (BFS)
     * @param start 起點
     * @param end 終點
     * @return 路徑列表
     */
    public List<String> findPath(String start, String end) {
        if (start == null || end == null) {
            return new ArrayList<>();
        }
        
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return new ArrayList<>();
        }
        
        if (start.equals(end)) {
            return Arrays.asList(start);
        }
        
        // BFS 尋找路徑
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(start);
        visited.add(start);
        parent.put(start, null);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (current.equals(end)) {
                break;
            }
            
            for (String neighbor : adjacencyList.getOrDefault(current, new HashSet<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }
        
        if (!visited.contains(end)) {
            return new ArrayList<>();
        }
        
        // 重建路徑
        List<String> path = new ArrayList<>();
        String current = end;
        while (current != null) {
            path.add(0, current);
            current = parent.get(current);
        }
        
        return path;
    }
    
    /**
     * 取得所有可達對 (用於生成完整可達性矩陣)
     * @return 可達對列表
     */
    public List<ReachabilityPair> getAllReachablePairs() {
        List<ReachabilityPair> pairs = new ArrayList<>();
        List<String> vertices = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(vertices);
        
        for (String from : vertices) {
            Set<String> reachable = getReachableVertices(from);
            for (String to : vertices) {
                if (reachable.contains(to) && !from.equals(to)) {
                    pairs.add(new ReachabilityPair(from, to));
                }
            }
        }
        
        return pairs;
    }
    
    /**
     * 可達性配對類別
     */
    public static class ReachabilityPair {
        public final String from;
        public final String to;
        
        public ReachabilityPair(String from, String to) {
            this.from = from;
            this.to = to;
        }
        
        @Override
        public String toString() {
            return from + " → " + to;
        }
    }
    
    /**
     * 印出可達性矩陣
     */
    public void printReachabilityMatrix() {
        System.out.println("\n=== 可達性矩陣 ===");
        
        List<String> vertices = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(vertices);
        
        if (vertices.isEmpty()) {
            System.out.println("圖為空");
            return;
        }
        
        // 印出標頭
        System.out.print("    ");
        for (String v : vertices) {
            System.out.printf("%4s", v);
        }
        System.out.println();
        
        System.out.print("    ");
        for (int i = 0; i < vertices.size(); i++) {
            System.out.print("----");
        }
        System.out.println();
        
        // 印出矩陣
        for (String from : vertices) {
            System.out.printf("%3s ", from);
            Set<String> reachable = getReachableVertices(from);
            for (String to : vertices) {
                if (from.equals(to)) {
                    System.out.printf("%4s", "●");
                } else if (reachable.contains(to)) {
                    System.out.printf("%4s", "✓");
                } else {
                    System.out.printf("%4s", "✗");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 印出圖的結構
     */
    public void printGraph() {
        System.out.println("\n=== 圖結構 ===");
        
        if (adjacencyList.isEmpty()) {
            System.out.println("圖為空");
            return;
        }
        
        List<String> sorted = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(sorted);
        
        for (String vertex : sorted) {
            Set<String> neighbors = adjacencyList.get(vertex);
            System.out.printf("%s → %s%n", vertex, 
                             neighbors.isEmpty() ? "無" : neighbors.toString());
        }
        System.out.println();
    }
    
    /**
     * 清空圖
     */
    public void clear() {
        adjacencyList.clear();
        reachabilityCache.clear();
        System.out.println("🔄 已清空圖");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 有向圖可達性查詢測試 ===\n");
        
        // 測試 1：基本有向圖
        testBasicDirectedGraph();
        
        // 測試 2：多組查詢
        testMultipleQueries();
        
        // 測試 3：包含循環的圖
        testCyclicGraph();
        
        // 測試 4：邊界情況
        testEdgeCases();
        
        // 測試 5：實際應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試基本有向圖
     */
    private static void testBasicDirectedGraph() {
        System.out.println("--- 測試 1: 基本有向圖 ---");
        
        DirectedReachability graph = new DirectedReachability();
        
        // 建立圖
        graph.addDirectedEdge("A", "B");
        graph.addDirectedEdge("A", "C");
        graph.addDirectedEdge("B", "D");
        graph.addDirectedEdge("B", "E");
        graph.addDirectedEdge("C", "E");
        graph.addDirectedEdge("C", "F");
        graph.addDirectedEdge("D", "G");
        graph.addDirectedEdge("E", "G");
        graph.addDirectedEdge("F", "G");
        
        graph.printGraph();
        graph.printReachabilityMatrix();
        
        // 單一查詢測試
        System.out.println("\n📋 單一查詢測試:");
        System.out.println("  A → G: " + graph.isReachable("A", "G"));
        System.out.println("  A → F: " + graph.isReachable("A", "F"));
        System.out.println("  B → F: " + graph.isReachable("B", "F"));
        System.out.println("  G → A: " + graph.isReachable("G", "A"));
        System.out.println("  getReachableVertices('A'): " + graph.getReachableVertices("A"));
        System.out.println("  getReachableVertices('B'): " + graph.getReachableVertices("B"));
    }
    
    /**
     * 測試多組查詢
     */
    private static void testMultipleQueries() {
        System.out.println("\n--- 測試 2: 多組查詢 ---");
        
        DirectedReachability graph = new DirectedReachability();
        
        // 建立圖
        graph.addDirectedEdge("A", "B");
        graph.addDirectedEdge("A", "C");
        graph.addDirectedEdge("B", "D");
        graph.addDirectedEdge("C", "D");
        graph.addDirectedEdge("D", "E");
        graph.addDirectedEdge("E", "F");
        graph.addDirectedEdge("F", "C");  // 形成循環
        
        graph.printGraph();
        
        // 多組查詢
        graph.queryReachability(
            "A", "F",
            "A", "D",
            "B", "F",
            "B", "C",
            "C", "A",
            "F", "A",
            "A", "A"
        );
        
        // 顯示所有可達對
        System.out.println("📋 所有可達對:");
        List<ReachabilityPair> pairs = graph.getAllReachablePairs();
        for (ReachabilityPair pair : pairs) {
            System.out.println("  " + pair);
        }
        System.out.println();
    }
    
    /**
     * 測試包含循環的圖
     */
    private static void testCyclicGraph() {
        System.out.println("--- 測試 3: 包含循環的圖 ---");
        
        DirectedReachability graph = new DirectedReachability();
        
        // 建立包含循環的圖
        graph.addDirectedEdge("1", "2");
        graph.addDirectedEdge("2", "3");
        graph.addDirectedEdge("3", "1");  // 循環
        graph.addDirectedEdge("3", "4");
        graph.addDirectedEdge("4", "5");
        graph.addDirectedEdge("5", "3");  // 另一個循環
        
        graph.printGraph();
        graph.printReachabilityMatrix();
        
        // 查詢
        System.out.println("\n📋 循環圖查詢:");
        graph.queryReachability(
            "1", "4",
            "1", "5",
            "4", "1",
            "5", "2",
            "2", "5"
        );
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("\n--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空圖
        System.out.println("測試 4.1: 空圖");
        DirectedReachability graph = new DirectedReachability();
        graph.printGraph();
        graph.printReachabilityMatrix();
        System.out.println("  isReachable('A', 'B'): " + graph.isReachable("A", "B"));
        System.out.println();
        
        // 測試 4.2: 單一頂點
        System.out.println("測試 4.2: 單一頂點");
        graph.addVertex("A");
        graph.printGraph();
        System.out.println("  isReachable('A', 'A'): " + graph.isReachable("A", "A"));
        System.out.println("  isReachable('A', 'B'): " + graph.isReachable("A", "B"));
        System.out.println();
        
        // 測試 4.3: 線性圖
        System.out.println("測試 4.3: 線性圖");
        DirectedReachability graph2 = new DirectedReachability();
        graph2.addDirectedEdge("A", "B");
        graph2.addDirectedEdge("B", "C");
        graph2.addDirectedEdge("C", "D");
        graph2.addDirectedEdge("D", "E");
        
        graph2.queryReachability(
            "A", "E",
            "A", "C",
            "B", "E",
            "E", "A",
            "C", "A"
        );
        System.out.println();
        
        // 測試 4.4: 不存在的頂點
        System.out.println("測試 4.4: 不存在的頂點");
        graph2.isReachable("A", "Z");
        graph2.isReachable("Z", "A");
        graph2.queryReachability("A", "Z", "Z", "A");
        System.out.println();
        
        // 測試 4.5: 大量頂點查詢
        System.out.println("測試 4.5: 大量頂點查詢");
        DirectedReachability graph3 = new DirectedReachability();
        for (int i = 0; i < 10; i++) {
            graph3.addVertex("V" + i);
            if (i > 0) {
                graph3.addDirectedEdge("V" + (i - 1), "V" + i);
            }
        }
        graph3.queryReachability(
            "V0", "V9",
            "V3", "V7",
            "V5", "V2",
            "V0", "V0"
        );
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("\n--- 測試 5: 實際應用場景 ---");
        System.out.println("🏢 公司部門依賴關係分析");
        
        DirectedReachability company = new DirectedReachability();
        
        // 建立部門依賴關係 (A 依賴 B 表示 A 需要 B 的服務)
        company.addDirectedEdge("前端", "後端");
        company.addDirectedEdge("前端", "設計");
        company.addDirectedEdge("後端", "資料庫");
        company.addDirectedEdge("後端", "API");
        company.addDirectedEdge("資料庫", "儲存");
        company.addDirectedEdge("API", "安全");
        company.addDirectedEdge("設計", "UX");
        company.addDirectedEdge("UX", "使用者研究");
        company.addDirectedEdge("安全", "稽核");
        company.addDirectedEdge("儲存", "備份");
        company.addDirectedEdge("備份", "災難復原");
        
        company.printGraph();
        company.printReachabilityMatrix();
        
        // 依賴查詢
        System.out.println("\n📋 部門依賴查詢:");
        company.queryReachability(
            "前端", "資料庫",
            "前端", "安全",
            "設計", "使用者研究",
            "後端", "稽核",
            "API", "災難復原",
            "使用者研究", "前端"
        );
        
        // 分析結果
        System.out.println("\n📊 依賴分析:");
        System.out.println("  getReachableVertices('前端'): " + company.getReachableVertices("前端"));
        System.out.println("  getReachableVertices('後端'): " + company.getReachableVertices("後端"));
        System.out.println("  getReachableVertices('設計'): " + company.getReachableVertices("設計"));
        
        // 找出最依賴的部門 (出度最高)
        System.out.println("\n🏆 依賴最多部門:");
        int maxOut = 0;
        String maxDept = "";
        for (String dept : company.adjacencyList.keySet()) {
            int out = company.adjacencyList.get(dept).size();
            if (out > maxOut) {
                maxOut = out;
                maxDept = dept;
            }
        }
        System.out.println("  " + maxDept + " (依賴 " + maxOut + " 個部門)");
        
        // 找出最被依賴的部門 (入度最高)
        System.out.println("\n🏆 最被依賴部門:");
        Map<String, Integer> inDegree = new HashMap<>();
        for (String dept : company.adjacencyList.keySet()) {
            inDegree.put(dept, 0);
        }
        for (Set<String> neighbors : company.adjacencyList.values()) {
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }
        int maxIn = 0;
        String maxInDept = "";
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() > maxIn) {
                maxIn = entry.getValue();
                maxInDept = entry.getKey();
            }
        }
        System.out.println("  " + maxInDept + " (被 " + maxIn + " 個部門依賴)");
    }
}