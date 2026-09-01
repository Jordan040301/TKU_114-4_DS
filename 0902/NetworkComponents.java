import java.util.*;

/**
 * 網路連通分量分析
 * 輸出分量、分量個數與最大分量
 */
public class NetworkComponents {
    
    /**
     * 圖的鄰接表表示法
     */
    private Map<String, Set<String>> adjacencyList;
    
    /**
     * 分量類別
     */
    public static class Component {
        private final int id;
        private final Set<String> vertices;
        private final int size;
        
        public Component(int id, Set<String> vertices) {
            this.id = id;
            this.vertices = new HashSet<>(vertices);
            this.size = vertices.size();
        }
        
        public int getId() {
            return id;
        }
        
        public Set<String> getVertices() {
            return new HashSet<>(vertices);
        }
        
        public int getSize() {
            return size;
        }
        
        public boolean contains(String vertex) {
            return vertices.contains(vertex);
        }
        
        @Override
        public String toString() {
            return String.format("分量 %d (%d 個節點): %s", id, size, vertices);
        }
        
        public String toShortString() {
            return String.format("C%d[%d]", id, size);
        }
    }
    
    /**
     * 分量分析結果類別
     */
    public static class ComponentResult {
        private final List<Component> components;
        private final int componentCount;
        private final Component largestComponent;
        private final Component smallestComponent;
        private final double averageSize;
        private final int isolatedCount;
        
        public ComponentResult(List<Component> components) {
            this.components = new ArrayList<>(components);
            this.componentCount = components.size();
            
            Component largest = null;
            Component smallest = null;
            int isolatedCount = 0;
            int totalSize = 0;
            
            for (Component comp : components) {
                totalSize += comp.getSize();
                if (comp.getSize() == 1) {
                    isolatedCount++;
                }
                if (largest == null || comp.getSize() > largest.getSize()) {
                    largest = comp;
                }
                if (smallest == null || comp.getSize() < smallest.getSize()) {
                    smallest = comp;
                }
            }
            
            this.largestComponent = largest;
            this.smallestComponent = smallest;
            this.isolatedCount = isolatedCount;
            this.averageSize = componentCount > 0 ? (double) totalSize / componentCount : 0;
        }
        
        public List<Component> getComponents() {
            return components;
        }
        
        public int getComponentCount() {
            return componentCount;
        }
        
        public Component getLargestComponent() {
            return largestComponent;
        }
        
        public Component getSmallestComponent() {
            return smallestComponent;
        }
        
        public double getAverageSize() {
            return averageSize;
        }
        
        public int getIsolatedCount() {
            return isolatedCount;
        }
        
        public boolean isConnected() {
            return componentCount == 1;
        }
        
        public boolean isEmpty() {
            return componentCount == 0;
        }
        
        public int getTotalVertices() {
            int total = 0;
            for (Component comp : components) {
                total += comp.getSize();
            }
            return total;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== 連通分量分析結果 ===\n");
            sb.append("分量總數: ").append(componentCount).append("\n");
            sb.append("總節點數: ").append(getTotalVertices()).append("\n");
            sb.append("平均分量大小: ").append(String.format("%.2f", averageSize)).append("\n");
            sb.append("孤立節點數: ").append(isolatedCount).append("\n");
            sb.append("最大分量: ").append(largestComponent != null ? 
                         largestComponent.toShortString() : "無").append("\n");
            sb.append("最小分量: ").append(smallestComponent != null ? 
                         smallestComponent.toShortString() : "無").append("\n");
            sb.append("網路是否連通: ").append(isConnected() ? "是 ✅" : "否 ❌").append("\n");
            
            sb.append("\n所有分量:\n");
            for (Component comp : components) {
                sb.append("  ").append(comp).append("\n");
            }
            
            return sb.toString();
        }
    }
    
    /**
     * 建構子
     */
    public NetworkComponents() {
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
     * 新增有向邊 (轉換為無向邊用於連通分量分析)
     * @param from 起點
     * @param to 終點
     */
    public void addDirectedEdge(String from, String to) {
        addUndirectedEdge(from, to);
    }
    
    /**
     * 分析連通分量
     * @return 分量分析結果
     */
    public ComponentResult findComponents() {
        if (adjacencyList.isEmpty()) {
            return new ComponentResult(new ArrayList<>());
        }
        
        Set<String> visited = new HashSet<>();
        List<Component> components = new ArrayList<>();
        int componentId = 0;
        
        for (String vertex : adjacencyList.keySet()) {
            if (!visited.contains(vertex)) {
                Set<String> component = new HashSet<>();
                bfs(vertex, visited, component);
                components.add(new Component(componentId++, component));
            }
        }
        
        return new ComponentResult(components);
    }
    
    /**
     * BFS 找出連通分量
     */
    private void bfs(String start, Set<String> visited, Set<String> component) {
        Queue<String> queue = new LinkedList<>();
        queue.offer(start);
        visited.add(start);
        component.add(start);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            for (String neighbor : adjacencyList.getOrDefault(current, new HashSet<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    component.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
    }
    
    /**
     * 印出分量報告
     */
    public void printReport() {
        ComponentResult result = findComponents();
        System.out.println(result);
        printDetailedStatistics(result);
    }
    
    /**
     * 印出詳細統計 (靜態方法)
     */
    private static void printDetailedStatistics(ComponentResult result) {
        if (result.isEmpty()) {
            return;
        }
        
        System.out.println("\n📊 詳細統計:");
        
        // 分量大小分佈
        System.out.println("  分量大小分佈:");
        Map<Integer, Integer> sizeDistribution = new TreeMap<>();
        for (Component comp : result.getComponents()) {
            int size = comp.getSize();
            sizeDistribution.put(size, sizeDistribution.getOrDefault(size, 0) + 1);
        }
        
        for (Map.Entry<Integer, Integer> entry : sizeDistribution.entrySet()) {
            System.out.printf("    大小 %d: %d 個分量%n", entry.getKey(), entry.getValue());
        }
        
        // 最大分量細節
        if (result.getLargestComponent() != null) {
            Component largest = result.getLargestComponent();
            System.out.printf("\n  最大分量 (ID: %d, 大小: %d):%n", 
                             largest.getId(), largest.getSize());
            System.out.println("    節點: " + sortVerticesStatic(largest.getVertices()));
        }
        
        // 最小分量細節
        if (result.getSmallestComponent() != null && result.getComponentCount() > 1) {
            Component smallest = result.getSmallestComponent();
            System.out.printf("\n  最小分量 (ID: %d, 大小: %d):%n", 
                             smallest.getId(), smallest.getSize());
            System.out.println("    節點: " + sortVerticesStatic(smallest.getVertices()));
        }
        
        // 孤立節點
        if (result.getIsolatedCount() > 0) {
            System.out.printf("\n  孤立節點 (%d 個):%n", result.getIsolatedCount());
            List<String> isolated = new ArrayList<>();
            for (Component comp : result.getComponents()) {
                if (comp.getSize() == 1) {
                    isolated.addAll(comp.getVertices());
                }
            }
            System.out.println("    " + sortVerticesStatic(new HashSet<>(isolated)));
        }
    }
    
    /**
     * 排序頂點 (靜態方法)
     */
    private static String sortVerticesStatic(Set<String> vertices) {
        List<String> sorted = new ArrayList<>(vertices);
        Collections.sort(sorted);
        return sorted.toString();
    }
    
    /**
     * 排序頂點 (實例方法 - 供內部使用)
     */
    private String sortVertices(Set<String> vertices) {
        return sortVerticesStatic(vertices);
    }
    
    /**
     * 印出分量視覺化
     */
    public void printComponentVisualization() {
        ComponentResult result = findComponents();
        
        if (result.isEmpty()) {
            System.out.println("網路為空");
            return;
        }
        
        System.out.println("\n=== 分量視覺化 ===");
        
        List<Component> sortedComponents = new ArrayList<>(result.getComponents());
        sortedComponents.sort((a, b) -> b.getSize() - a.getSize());
        
        for (Component comp : sortedComponents) {
            System.out.printf("分量 %d (大小 %d): ", comp.getId(), comp.getSize());
            
            int barLength = Math.min(comp.getSize(), 50);
            System.out.print("[");
            for (int i = 0; i < barLength; i++) {
                System.out.print("█");
            }
            if (comp.getSize() > 50) {
                System.out.print("…");
            }
            System.out.printf("] %d 個節點%n", comp.getSize());
            
            List<String> vertices = new ArrayList<>(comp.getVertices());
            Collections.sort(vertices);
            if (vertices.size() <= 5) {
                System.out.println("  節點: " + vertices);
            } else {
                System.out.println("  節點: " + vertices.subList(0, 5) + " ... (共 " + vertices.size() + " 個)");
            }
        }
        System.out.println();
    }
    
    /**
     * 印出連通性摘要
     */
    public void printConnectivitySummary() {
        ComponentResult result = findComponents();
        
        System.out.println("\n=== 連通性摘要 ===");
        System.out.printf("總節點數: %d%n", adjacencyList.size());
        System.out.printf("分量數: %d%n", result.getComponentCount());
        System.out.printf("連通: %s%n", result.isConnected() ? "是" : "否");
        
        if (!result.isConnected()) {
            System.out.printf("最大分量佔總節點比例: %.1f%%%n", 
                             (double) result.getLargestComponent().getSize() / 
                             adjacencyList.size() * 100);
        }
        System.out.println();
    }
    
    /**
     * 清空圖
     */
    public void clear() {
        adjacencyList.clear();
        System.out.println("🔄 已清空網路");
    }
    
    /**
     * 取得鄰接表 (供測試使用)
     */
    public Map<String, Set<String>> getAdjacencyList() {
        return adjacencyList;
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 網路連通分量分析測試 ===\n");
        
        testBasicComponents();
        testMultipleComponents();
        testLargeNetwork();
        testEdgeCases();
        testRealWorldScenario();
    }
    
    /**
     * 測試基本分量
     */
    private static void testBasicComponents() {
        System.out.println("--- 測試 1: 基本分量 ---");
        
        NetworkComponents network = new NetworkComponents();
        
        network.addUndirectedEdge("A", "B");
        network.addUndirectedEdge("B", "C");
        network.addUndirectedEdge("C", "D");
        network.addUndirectedEdge("E", "F");
        network.addUndirectedEdge("F", "G");
        network.addVertex("H");
        
        network.printReport();
        network.printComponentVisualization();
        network.printConnectivitySummary();
    }
    
    /**
     * 測試多個分量
     */
    private static void testMultipleComponents() {
        System.out.println("\n--- 測試 2: 多個分量 ---");
        
        NetworkComponents network = new NetworkComponents();
        
        // 分量 1: 星狀
        network.addUndirectedEdge("中心1", "A1");
        network.addUndirectedEdge("中心1", "A2");
        network.addUndirectedEdge("中心1", "A3");
        network.addUndirectedEdge("A1", "A4");
        
        // 分量 2: 環狀
        network.addUndirectedEdge("B1", "B2");
        network.addUndirectedEdge("B2", "B3");
        network.addUndirectedEdge("B3", "B4");
        network.addUndirectedEdge("B4", "B1");
        
        // 分量 3: 鏈狀
        network.addUndirectedEdge("C1", "C2");
        network.addUndirectedEdge("C2", "C3");
        network.addUndirectedEdge("C3", "C4");
        network.addUndirectedEdge("C4", "C5");
        
        // 分量 4: 孤立節點
        network.addVertex("D1");
        network.addVertex("D2");
        network.addVertex("D3");
        
        network.printReport();
        network.printComponentVisualization();
        network.printConnectivitySummary();
    }
    
    /**
     * 測試大型網路
     */
    private static void testLargeNetwork() {
        System.out.println("\n--- 測試 3: 大型網路 ---");
        
        NetworkComponents network = new NetworkComponents();
        
        Random random = new Random(42);
        int vertexCount = 100;
        double edgeProbability = 0.08;
        
        System.out.printf("建立 %d 個節點, 邊機率 %.2f%n", vertexCount, edgeProbability);
        
        for (int i = 0; i < vertexCount; i++) {
            network.addVertex("V" + i);
        }
        
        int edgeCount = 0;
        for (int i = 0; i < vertexCount; i++) {
            for (int j = i + 1; j < vertexCount; j++) {
                if (random.nextDouble() < edgeProbability) {
                    network.addUndirectedEdge("V" + i, "V" + j);
                    edgeCount++;
                }
            }
        }
        
        System.out.printf("實際新增 %d 條邊%n", edgeCount);
        System.out.println();
        
        network.printReport();
        network.printConnectivitySummary();
        network.printComponentVisualization();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("\n--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空網路
        System.out.println("測試 4.1: 空網路");
        NetworkComponents network = new NetworkComponents();
        network.printReport();
        System.out.println();
        
        // 測試 4.2: 單一節點
        System.out.println("測試 4.2: 單一節點");
        network.addVertex("A");
        network.printReport();
        System.out.println();
        
        // 測試 4.3: 完全連通的圖
        System.out.println("測試 4.3: 完全連通的圖");
        NetworkComponents network2 = new NetworkComponents();
        network2.addUndirectedEdge("A", "B");
        network2.addUndirectedEdge("B", "C");
        network2.addUndirectedEdge("C", "D");
        network2.addUndirectedEdge("D", "E");
        network2.addUndirectedEdge("E", "A");
        network2.addUndirectedEdge("A", "C");
        network2.addUndirectedEdge("B", "D");
        network2.printReport();
        System.out.println();
        
        // 測試 4.4: 全部孤立節點
        System.out.println("測試 4.4: 全部孤立節點");
        NetworkComponents network3 = new NetworkComponents();
        for (int i = 0; i < 10; i++) {
            network3.addVertex("I" + i);
        }
        network3.printReport();
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("\n--- 測試 5: 實際應用場景 ---");
        System.out.println("🌐 社交網路分量分析");
        
        NetworkComponents socialNetwork = new NetworkComponents();
        
        // 建立社交網路
        String[] users = {
            "Alice", "Bob", "Charlie", "David", "Eve", "Frank",
            "Grace", "Henry", "Ivy", "Jack", "Kevin", "Lisa",
            "Mary", "Nancy", "Oscar", "Paul", "Quinn", "Rose",
            "Sam", "Tina", "Uma", "Victor", "Wendy", "Xavier",
            "Yvonne", "Zack"
        };
        
        for (String user : users) {
            socialNetwork.addVertex(user);
        }
        
        // 好友關係
        // 社群 1: 程式設計師社群
        socialNetwork.addUndirectedEdge("Alice", "Bob");
        socialNetwork.addUndirectedEdge("Alice", "Charlie");
        socialNetwork.addUndirectedEdge("Bob", "David");
        socialNetwork.addUndirectedEdge("Bob", "Eve");
        socialNetwork.addUndirectedEdge("Charlie", "Frank");
        socialNetwork.addUndirectedEdge("David", "Grace");
        socialNetwork.addUndirectedEdge("Eve", "Grace");
        socialNetwork.addUndirectedEdge("Frank", "Henry");
        
        // 社群 2: 設計師社群
        socialNetwork.addUndirectedEdge("Ivy", "Jack");
        socialNetwork.addUndirectedEdge("Ivy", "Kevin");
        socialNetwork.addUndirectedEdge("Jack", "Lisa");
        socialNetwork.addUndirectedEdge("Kevin", "Lisa");
        socialNetwork.addUndirectedEdge("Lisa", "Mary");
        socialNetwork.addUndirectedEdge("Mary", "Nancy");
        
        // 社群 3: 管理層社群
        socialNetwork.addUndirectedEdge("Oscar", "Paul");
        socialNetwork.addUndirectedEdge("Oscar", "Quinn");
        socialNetwork.addUndirectedEdge("Paul", "Rose");
        socialNetwork.addUndirectedEdge("Quinn", "Rose");
        socialNetwork.addUndirectedEdge("Rose", "Sam");
        
        // 社群 4: 跨社群連結
        socialNetwork.addUndirectedEdge("Henry", "Ivy");
        socialNetwork.addUndirectedEdge("Grace", "Oscar");
        socialNetwork.addUndirectedEdge("Sam", "Tina");
        
        // 孤立節點
        socialNetwork.addVertex("Uma");
        socialNetwork.addVertex("Victor");
        socialNetwork.addVertex("Wendy");
        socialNetwork.addVertex("Xavier");
        socialNetwork.addVertex("Yvonne");
        socialNetwork.addVertex("Zack");
        
        socialNetwork.printReport();
        socialNetwork.printComponentVisualization();
        socialNetwork.printConnectivitySummary();
        
        // 社群分析
        System.out.println("\n📊 社群分析:");
        ComponentResult result = socialNetwork.findComponents();
        
        System.out.println("  主要社群:");
        List<Component> sorted = new ArrayList<>(result.getComponents());
        sorted.sort((a, b) -> b.getSize() - a.getSize());
        
        for (int i = 0; i < Math.min(3, sorted.size()); i++) {
            Component comp = sorted.get(i);
            // 使用靜態方法 sortVerticesStatic
            System.out.printf("    社群 %d: %d 人 - %s%n", 
                             i + 1, comp.getSize(), 
                             sortVerticesStatic(comp.getVertices()));
        }
        
        // 網路結構分析
        System.out.println("\n📈 網路結構分析:");
        System.out.printf("  總用戶數: %d%n", socialNetwork.getAdjacencyList().size());
        System.out.printf("  社群數量: %d%n", result.getComponentCount());
        if (result.getLargestComponent() != null) {
            System.out.printf("  最大社群佔比: %.1f%%%n", 
                             (double) result.getLargestComponent().getSize() / 
                             socialNetwork.getAdjacencyList().size() * 100);
        }
        System.out.printf("  孤立用戶數: %d%n", result.getIsolatedCount());
        
        // 建議
        System.out.println("\n💡 網路優化建議:");
        if (!result.isConnected()) {
            System.out.println("  ⚠️ 網路未完全連通，建議增加跨社群連結");
        }
        if (result.getIsolatedCount() > 0) {
            System.out.printf("  ⚠️ 有 %d 個孤立用戶，建議鼓勵他們加入社群%n", 
                             result.getIsolatedCount());
        }
        if (result.getComponentCount() > 3) {
            System.out.println("  ⚠️ 社群數量較多，建議促進社群間交流");
        }
        if (result.isConnected()) {
            System.out.println("  ✅ 網路完全連通，結構良好");
        }
    }
}