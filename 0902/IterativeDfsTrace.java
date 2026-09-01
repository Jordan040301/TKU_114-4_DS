import java.util.*;

/**
 * 迭代式 DFS 追蹤
 * 每次 push 和 pop 都輸出 Stack 與 visited 狀態
 */
public class IterativeDfsTrace {
    
    /**
     * 圖的鄰接表表示法
     */
    private Map<String, Set<String>> adjacencyList;
    
    /**
     * 建構子
     */
    public IterativeDfsTrace() {
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
     * 執行迭代式 DFS 並追蹤每一步
     * @param start 起點
     */
    public void dfsTrace(String start) {
        if (start == null || start.trim().isEmpty()) {
            throw new IllegalArgumentException("起點不能為空");
        }
        
        if (!adjacencyList.containsKey(start)) {
            System.out.printf("⚠️ 起點 '%s' 不存在%n", start);
            return;
        }
        
        System.out.println("\n=== 迭代式 DFS 追蹤 ===");
        System.out.println("起點: " + start);
        System.out.println("鄰接表: " + adjacencyList);
        System.out.println();
        System.out.println("步驟 | 動作 | Stack | Visited");
        System.out.println("-----|------|-------|--------------------");
        
        int step = 0;
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        
        // 起點入棧
        stack.push(start);
        System.out.printf("%4d | push %s | %-20s | %s%n", 
                         ++step, start, stack.toString(), visited.toString());
        
        while (!stack.isEmpty()) {
            String current = stack.pop();
            System.out.printf("%4d | pop %s  | %-20s | %s%n", 
                             ++step, current, stack.toString(), visited.toString());
            
            if (!visited.contains(current)) {
                visited.add(current);
                System.out.printf("%4d | visit %s | %-20s | %s%n", 
                                 ++step, current, stack.toString(), visited.toString());
                
                // 取得鄰居並排序 (為了輸出穩定)
                List<String> neighbors = new ArrayList<>(adjacencyList.getOrDefault(current, new HashSet<>()));
                Collections.sort(neighbors);
                Collections.reverse(neighbors); // 反轉以保持字典序入棧
                
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor) && !stack.contains(neighbor)) {
                        stack.push(neighbor);
                        System.out.printf("%4d | push %s | %-20s | %s%n", 
                                         ++step, neighbor, stack.toString(), visited.toString());
                    }
                }
            }
        }
        
        System.out.println("\n✅ DFS 完成!");
        System.out.println("最終 Stack: " + stack);
        System.out.println("最終 Visited: " + visited);
        System.out.println("訪問順序: " + visited);
        System.out.println();
    }
    
    /**
     * 執行迭代式 DFS 並回傳訪問順序
     * @param start 起點
     * @return 訪問順序列表
     */
    public List<String> dfsOrder(String start) {
        List<String> order = new ArrayList<>();
        
        if (start == null || start.trim().isEmpty()) {
            return order;
        }
        
        if (!adjacencyList.containsKey(start)) {
            return order;
        }
        
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        
        stack.push(start);
        
        while (!stack.isEmpty()) {
            String current = stack.pop();
            
            if (!visited.contains(current)) {
                visited.add(current);
                order.add(current);
                
                List<String> neighbors = new ArrayList<>(adjacencyList.getOrDefault(current, new HashSet<>()));
                Collections.sort(neighbors);
                Collections.reverse(neighbors);
                
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor) && !stack.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        
        return order;
    }
    
    /**
     * 以圖形方式顯示 DFS 樹
     * @param start 起點
     */
    public void printDfsTree(String start) {
        List<String> order = dfsOrder(start);
        if (order.isEmpty()) {
            System.out.println("無法生成 DFS 樹");
            return;
        }
        
        System.out.println("\n=== DFS 樹 ===");
        System.out.println("起點: " + start);
        System.out.println("訪問順序: " + order);
        
        // 建立 DFS 樹
        Map<String, List<String>> tree = new HashMap<>();
        for (String vertex : adjacencyList.keySet()) {
            tree.put(vertex, new ArrayList<>());
        }
        
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();
        Map<String, String> parent = new HashMap<>();
        
        stack.push(start);
        parent.put(start, null);
        
        while (!stack.isEmpty()) {
            String current = stack.pop();
            
            if (!visited.contains(current)) {
                visited.add(current);
                
                List<String> neighbors = new ArrayList<>(adjacencyList.getOrDefault(current, new HashSet<>()));
                Collections.sort(neighbors);
                Collections.reverse(neighbors);
                
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor) && !stack.contains(neighbor)) {
                        stack.push(neighbor);
                        parent.put(neighbor, current);
                    }
                }
            }
        }
        
        // 建立樹結構
        for (String vertex : adjacencyList.keySet()) {
            String p = parent.get(vertex);
            if (p != null && tree.containsKey(p)) {
                tree.get(p).add(vertex);
            }
        }
        
        // 印出樹
        printTree(tree, start, 0);
        System.out.println();
    }
    
    /**
     * 遞迴印出樹結構
     */
    private void printTree(Map<String, List<String>> tree, String vertex, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        if (level > 0) {
            System.out.print("└─ ");
        }
        System.out.println(vertex);
        
        List<String> children = tree.getOrDefault(vertex, new ArrayList<>());
        Collections.sort(children);
        for (String child : children) {
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
        System.out.println("=== 迭代式 DFS 追蹤測試 ===\n");
        
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
        
        IterativeDfsTrace graph = new IterativeDfsTrace();
        
        // 建立圖
        String[] vertices = {"A", "B", "C", "D", "E"};
        for (String v : vertices) {
            graph.addVertex(v);
        }
        
        graph.addUndirectedEdge("A", "B");
        graph.addUndirectedEdge("A", "C");
        graph.addUndirectedEdge("B", "D");
        graph.addUndirectedEdge("B", "E");
        graph.addUndirectedEdge("C", "E");
        graph.addUndirectedEdge("D", "E");
        
        System.out.println("圖結構:");
        System.out.println("  頂點: " + Arrays.toString(vertices));
        System.out.println("  邊: A-B, A-C, B-D, B-E, C-E, D-E");
        System.out.println();
        
        graph.dfsTrace("A");
        graph.printDfsTree("A");
    }
    
    /**
     * 測試有向圖
     */
    private static void testDirectedGraph() {
        System.out.println("\n--- 測試 2: 有向圖 ---");
        
        IterativeDfsTrace graph = new IterativeDfsTrace();
        
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
        
        System.out.println("有向圖:");
        System.out.println("  邊: A→B, A→C, B→D, B→E, C→E, C→F, D→G, E→G, F→G, G→H");
        System.out.println();
        
        graph.dfsTrace("A");
        graph.printDfsTree("A");
    }
    
    /**
     * 測試複雜圖
     */
    private static void testComplexGraph() {
        System.out.println("\n--- 測試 3: 複雜圖 (社群網路) ---");
        
        IterativeDfsTrace socialNetwork = new IterativeDfsTrace();
        
        // 建立社群網路
        String[] users = {
            "Alice", "Bob", "Charlie", "David", "Eve", 
            "Frank", "Grace", "Henry", "Ivy"
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
        socialNetwork.addUndirectedEdge("Frank", "Grace");
        socialNetwork.addUndirectedEdge("Henry", "Ivy");
        
        System.out.println("社群網路 (從 Alice 開始):");
        socialNetwork.dfsTrace("Alice");
        socialNetwork.printDfsTree("Alice");
        
        System.out.println("\n社群網路 (從 Frank 開始):");
        socialNetwork.dfsTrace("Frank");
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("\n--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空圖
        System.out.println("測試 4.1: 空圖");
        IterativeDfsTrace graph = new IterativeDfsTrace();
        graph.dfsTrace("A");
        System.out.println();
        
        // 測試 4.2: 單一頂點
        System.out.println("測試 4.2: 單一頂點");
        graph.addVertex("A");
        graph.dfsTrace("A");
        System.out.println();
        
        // 測試 4.3: 孤立頂點
        System.out.println("測試 4.3: 孤立頂點");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addUndirectedEdge("A", "B");
        graph.dfsTrace("A");
        System.out.println();
        
        // 測試 4.4: 不存在的起點
        System.out.println("測試 4.4: 不存在的起點");
        graph.dfsTrace("不存在");
        System.out.println();
        
        // 測試 4.5: 線性圖
        System.out.println("測試 4.5: 線性圖");
        IterativeDfsTrace graph2 = new IterativeDfsTrace();
        graph2.addUndirectedEdge("1", "2");
        graph2.addUndirectedEdge("2", "3");
        graph2.addUndirectedEdge("3", "4");
        graph2.addUndirectedEdge("4", "5");
        graph2.dfsTrace("1");
        System.out.println();
        
        // 測試 4.6: 星狀圖
        System.out.println("測試 4.6: 星狀圖");
        IterativeDfsTrace graph3 = new IterativeDfsTrace();
        graph3.addVertex("中心");
        for (int i = 1; i <= 5; i++) {
            String leaf = "葉" + i;
            graph3.addVertex(leaf);
            graph3.addUndirectedEdge("中心", leaf);
        }
        graph3.dfsTrace("中心");
    }
}