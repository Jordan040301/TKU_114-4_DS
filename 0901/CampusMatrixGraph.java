import java.util.*;

/**
 * 校園矩陣圖
 * 使用鄰接矩陣管理無向圖
 */
public class CampusMatrixGraph {
    
    // 節點名稱列表
    private List<String> vertices;
    
    // 鄰接矩陣 (使用 boolean 避免重複計數)
    private boolean[][] adjacencyMatrix;
    
    // 節點名稱到索引的映射
    private Map<String, Integer> vertexIndexMap;
    
    /**
     * 建構子
     */
    public CampusMatrixGraph() {
        this.vertices = new ArrayList<>();
        this.adjacencyMatrix = new boolean[0][0];
        this.vertexIndexMap = new HashMap<>();
    }
    
    /**
     * 新增節點 (頂點)
     * @param name 節點名稱
     * @return true 如果成功新增
     */
    public boolean addVertex(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("節點名稱不能為空");
        }
        
        if (vertexIndexMap.containsKey(name)) {
            System.out.printf("⚠️ 節點 '%s' 已存在%n", name);
            return false;
        }
        
        // 擴展鄰接矩陣
        int newSize = vertices.size() + 1;
        boolean[][] newMatrix = new boolean[newSize][newSize];
        
        // 複製原有矩陣
        for (int i = 0; i < vertices.size(); i++) {
            System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, vertices.size());
        }
        
        adjacencyMatrix = newMatrix;
        vertexIndexMap.put(name, vertices.size());
        vertices.add(name);
        
        System.out.printf("✅ 新增節點: %s (索引: %d)%n", name, vertices.size() - 1);
        return true;
    }
    
    /**
     * 新增無向邊
     * @param name1 節點1
     * @param name2 節點2
     * @return true 如果成功新增
     */
    public boolean addEdge(String name1, String name2) {
        if (name1 == null || name2 == null) {
            throw new IllegalArgumentException("節點名稱不能為 null");
        }
        
        if (name1.equals(name2)) {
            System.out.println("⚠️ 不能新增自環 (同一節點)");
            return false;
        }
        
        Integer index1 = vertexIndexMap.get(name1);
        Integer index2 = vertexIndexMap.get(name2);
        
        if (index1 == null || index2 == null) {
            System.out.printf("⚠️ 節點不存在: %s 或 %s%n", name1, name2);
            return false;
        }
        
        // 檢查是否已存在邊
        if (adjacencyMatrix[index1][index2]) {
            System.out.printf("⚠️ 邊已存在: %s -- %s%n", name1, name2);
            return false;
        }
        
        // 新增無向邊 (對稱)
        adjacencyMatrix[index1][index2] = true;
        adjacencyMatrix[index2][index1] = true;
        
        System.out.printf("✅ 新增邊: %s -- %s%n", name1, name2);
        return true;
    }
    
    /**
     * 刪除無向邊
     * @param name1 節點1
     * @param name2 節點2
     * @return true 如果成功刪除
     */
    public boolean removeEdge(String name1, String name2) {
        if (name1 == null || name2 == null) {
            throw new IllegalArgumentException("節點名稱不能為 null");
        }
        
        Integer index1 = vertexIndexMap.get(name1);
        Integer index2 = vertexIndexMap.get(name2);
        
        if (index1 == null || index2 == null) {
            System.out.printf("⚠️ 節點不存在: %s 或 %s%n", name1, name2);
            return false;
        }
        
        // 檢查邊是否存在
        if (!adjacencyMatrix[index1][index2]) {
            System.out.printf("⚠️ 邊不存在: %s -- %s%n", name1, name2);
            return false;
        }
        
        // 刪除無向邊 (對稱)
        adjacencyMatrix[index1][index2] = false;
        adjacencyMatrix[index2][index1] = false;
        
        System.out.printf("🗑️ 刪除邊: %s -- %s%n", name1, name2);
        return true;
    }
    
    /**
     * 刪除節點及其所有關聯邊
     * @param name 節點名稱
     * @return true 如果成功刪除
     */
    public boolean removeVertex(String name) {
        Integer index = vertexIndexMap.get(name);
        if (index == null) {
            System.out.printf("⚠️ 節點不存在: %s%n", name);
            return false;
        }
        
        // 刪除節點
        vertexIndexMap.remove(name);
        vertices.remove((int) index);
        
        // 重建鄰接矩陣 (移除該行和列)
        int newSize = vertices.size();
        boolean[][] newMatrix = new boolean[newSize][newSize];
        
        // 複製除了被刪除行和列以外的數據
        int newI = 0;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (i == index) continue;
            
            int newJ = 0;
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                if (j == index) continue;
                newMatrix[newI][newJ] = adjacencyMatrix[i][j];
                newJ++;
            }
            newI++;
        }
        
        adjacencyMatrix = newMatrix;
        
        // 更新索引映射
        vertexIndexMap.clear();
        for (int i = 0; i < vertices.size(); i++) {
            vertexIndexMap.put(vertices.get(i), i);
        }
        
        System.out.printf("🗑️ 刪除節點: %s (及其所有關聯邊)%n", name);
        return true;
    }
    
    /**
     * 查詢節點的度 (鄰居數量)
     * @param name 節點名稱
     * @return 度數
     */
    public int getDegree(String name) {
        Integer index = vertexIndexMap.get(name);
        if (index == null) {
            System.out.printf("⚠️ 節點不存在: %s%n", name);
            return -1;
        }
        
        int degree = 0;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[index][i]) {
                degree++;
            }
        }
        return degree;
    }
    
    /**
     * 查詢節點的鄰居列表
     * @param name 節點名稱
     * @return 鄰居列表
     */
    public List<String> getNeighbors(String name) {
        Integer index = vertexIndexMap.get(name);
        if (index == null) {
            System.out.printf("⚠️ 節點不存在: %s%n", name);
            return new ArrayList<>();
        }
        
        List<String> neighbors = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[index][i]) {
                neighbors.add(vertices.get(i));
            }
        }
        return neighbors;
    }
    
    /**
     * 查詢節點的逆度 (其他節點連接到該節點的數量)
     * 在無向圖中，度 = 逆度
     * @param name 節點名稱
     * @return 逆度
     */
    public int getInDegree(String name) {
        // 在無向圖中，度 = 逆度
        return getDegree(name);
    }
    
    /**
     * 查詢節點是否連接到另一個節點
     * @param name1 節點1
     * @param name2 節點2
     * @return true 如果存在邊
     */
    public boolean hasEdge(String name1, String name2) {
        Integer index1 = vertexIndexMap.get(name1);
        Integer index2 = vertexIndexMap.get(name2);
        
        if (index1 == null || index2 == null) {
            return false;
        }
        
        return adjacencyMatrix[index1][index2];
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
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i + 1; j < adjacencyMatrix[i].length; j++) {
                if (adjacencyMatrix[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * 取得所有節點名稱
     * @return 節點名稱列表
     */
    public List<String> getVertices() {
        return new ArrayList<>(vertices);
    }
    
    /**
     * 檢查節點是否存在
     * @param name 節點名稱
     * @return true 如果存在
     */
    public boolean containsVertex(String name) {
        return vertexIndexMap.containsKey(name);
    }
    
    /**
     * 印出鄰接矩陣
     */
    public void printAdjacencyMatrix() {
        System.out.println("\n=== 鄰接矩陣 ===");
        
        if (vertices.isEmpty()) {
            System.out.println("圖為空");
            return;
        }
        
        // 印出標頭
        System.out.print("     ");
        for (String v : vertices) {
            System.out.printf("%6s", v);
        }
        System.out.println();
        
        System.out.print("     ");
        for (int i = 0; i < vertices.size(); i++) {
            System.out.print("------");
        }
        System.out.println();
        
        // 印出矩陣
        for (int i = 0; i < vertices.size(); i++) {
            System.out.printf("%4s ", vertices.get(i));
            for (int j = 0; j < vertices.size(); j++) {
                System.out.printf("%6s", adjacencyMatrix[i][j] ? "1" : "0");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 印出圖的詳細資訊
     */
    public void printGraphInfo() {
        System.out.println("\n=== 校園矩陣圖資訊 ===");
        System.out.printf("節點總數: %d%n", getVertexCount());
        System.out.printf("邊總數: %d%n", getEdgeCount());
        
        if (vertices.isEmpty()) {
            System.out.println("圖為空");
            return;
        }
        
        System.out.println("\n節點詳細資訊:");
        System.out.printf("%-12s | %-8s | %-8s | %s%n", 
                         "節點名稱", "度", "逆度", "鄰居");
        System.out.println("------------|----------|----------|------------------------------");
        
        for (String vertex : vertices) {
            int degree = getDegree(vertex);
            int inDegree = getInDegree(vertex);
            List<String> neighbors = getNeighbors(vertex);
            
            System.out.printf("%-12s | %8d | %8d | %s%n",
                             vertex, degree, inDegree, 
                             neighbors.isEmpty() ? "無" : neighbors.toString());
        }
        System.out.println();
    }
    
    /**
     * 印出圖的結構 (類似鄰接串列)
     */
    public void printGraphStructure() {
        System.out.println("\n=== 圖結構 (鄰接表示) ===");
        
        if (vertices.isEmpty()) {
            System.out.println("圖為空");
            return;
        }
        
        for (String vertex : vertices) {
            List<String> neighbors = getNeighbors(vertex);
            System.out.printf("%s → %s%n", vertex, 
                             neighbors.isEmpty() ? "無" : neighbors.toString());
        }
        System.out.println();
    }
    
    /**
     * 計算節點的逆度（在無向圖中與度相同，此方法僅為符合題目要求）
     * 實際上無向圖中入度 = 出度 = 度
     */
    public int getOutDegree(String name) {
        return getDegree(name);
    }
    
    /**
     * 印出所有節點的度統計
     */
    public void printDegreeStatistics() {
        System.out.println("\n=== 度統計 ===");
        
        if (vertices.isEmpty()) {
            System.out.println("圖為空");
            return;
        }
        
        System.out.printf("%-12s | %-8s | %-8s%n", "節點名稱", "度", "逆度");
        System.out.println("------------|----------|----------");
        
        int totalDegree = 0;
        int maxDegree = 0;
        String maxDegreeVertex = "";
        int minDegree = Integer.MAX_VALUE;
        String minDegreeVertex = "";
        
        for (String vertex : vertices) {
            int degree = getDegree(vertex);
            int inDegree = getInDegree(vertex);
            
            totalDegree += degree;
            
            if (degree > maxDegree) {
                maxDegree = degree;
                maxDegreeVertex = vertex;
            }
            if (degree < minDegree) {
                minDegree = degree;
                minDegreeVertex = vertex;
            }
            
            System.out.printf("%-12s | %8d | %8d%n", vertex, degree, inDegree);
        }
        
        System.out.println("\n統計摘要:");
        System.out.printf("  平均度: %.2f%n", (double) totalDegree / vertices.size());
        System.out.printf("  最大度: %d (%s)%n", maxDegree, maxDegreeVertex);
        System.out.printf("  最小度: %d (%s)%n", minDegree, minDegreeVertex);
        System.out.println();
    }
    
    /**
     * 清空圖
     */
    public void clear() {
        vertices.clear();
        adjacencyMatrix = new boolean[0][0];
        vertexIndexMap.clear();
        System.out.println("🔄 圖已清空");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 校園矩陣圖測試 ===\n");
        
        // 測試 1：基本操作
        testBasicOperations();
        
        // 測試 2：進階操作
        testAdvancedOperations();
        
        // 測試 3：邊界情況
        testEdgeCases();
        
        // 測試 4：校園場景模擬
        testCampusScenario();
        
        // 測試 5：完整功能展示
        testFullFunctionality();
    }
    
    /**
     * 測試基本操作
     */
    private static void testBasicOperations() {
        System.out.println("--- 測試 1: 基本操作 ---");
        
        CampusMatrixGraph graph = new CampusMatrixGraph();
        
        // 新增節點
        System.out.println("新增節點:");
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        
        // 新增邊
        System.out.println("\n新增邊:");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "E");
        graph.addEdge("D", "E");
        
        graph.printGraphInfo();
        graph.printAdjacencyMatrix();
        graph.printGraphStructure();
        graph.printDegreeStatistics();
    }
    
    /**
     * 測試進階操作
     */
    private static void testAdvancedOperations() {
        System.out.println("--- 測試 2: 進階操作 ---");
        
        CampusMatrixGraph graph = new CampusMatrixGraph();
        
        // 建立圖
        String[] nodes = {"甲", "乙", "丙", "丁", "戊", "己"};
        for (String node : nodes) {
            graph.addVertex(node);
        }
        
        // 新增邊
        graph.addEdge("甲", "乙");
        graph.addEdge("甲", "丙");
        graph.addEdge("乙", "丁");
        graph.addEdge("丙", "丁");
        graph.addEdge("丁", "戊");
        graph.addEdge("戊", "己");
        graph.addEdge("己", "甲");
        
        graph.printGraphInfo();
        
        // 查詢
        System.out.println("\n查詢操作:");
        System.out.println("  getDegree('甲'): " + graph.getDegree("甲"));
        System.out.println("  getNeighbors('甲'): " + graph.getNeighbors("甲"));
        System.out.println("  getInDegree('甲'): " + graph.getInDegree("甲"));
        System.out.println("  hasEdge('甲', '丙'): " + graph.hasEdge("甲", "丙"));
        System.out.println("  hasEdge('甲', '丁'): " + graph.hasEdge("甲", "丁"));
        
        // 刪除邊
        System.out.println("\n刪除邊:");
        graph.removeEdge("甲", "丙");
        graph.removeEdge("戊", "己");
        
        graph.printGraphInfo();
        graph.printAdjacencyMatrix();
        
        // 刪除節點
        System.out.println("刪除節點:");
        graph.removeVertex("丁");
        graph.printGraphInfo();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 3: 邊界情況 ---");
        
        CampusMatrixGraph graph = new CampusMatrixGraph();
        
        // 測試 3.1: 空圖
        System.out.println("測試 3.1: 空圖");
        graph.printGraphInfo();
        graph.printAdjacencyMatrix();
        System.out.println("  getDegree('A'): " + graph.getDegree("A"));
        System.out.println("  getNeighbors('A'): " + graph.getNeighbors("A"));
        System.out.println();
        
        // 測試 3.2: 單一節點
        System.out.println("測試 3.2: 單一節點");
        graph.addVertex("單一節點");
        graph.printGraphInfo();
        System.out.println("  getDegree('單一節點'): " + graph.getDegree("單一節點"));
        System.out.println("  getNeighbors('單一節點'): " + graph.getNeighbors("單一節點"));
        System.out.println();
        
        // 測試 3.3: 重複新增
        System.out.println("測試 3.3: 重複新增");
        graph.addVertex("重複節點");
        graph.addVertex("重複節點");
        graph.addEdge("單一節點", "重複節點");
        graph.addEdge("單一節點", "重複節點");
        graph.printGraphInfo();
        System.out.println();
        
        // 測試 3.4: 自環和 null
        System.out.println("測試 3.4: 自環和 null 處理");
        try {
            graph.addEdge("單一節點", "單一節點");
        } catch (Exception e) {
            System.out.println("  ✓ 正確處理自環: " + e.getMessage());
        }
        
        try {
            graph.addVertex(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確處理 null: " + e.getMessage());
        }
        System.out.println();
        
        // 測試 3.5: 不存在的節點
        System.out.println("測試 3.5: 不存在的節點");
        graph.addEdge("不存在", "節點");
        graph.removeEdge("不存在", "節點");
        graph.getDegree("不存在");
        graph.getNeighbors("不存在");
        graph.removeVertex("不存在");
        System.out.println();
    }
    
    /**
     * 測試校園場景模擬
     */
    private static void testCampusScenario() {
        System.out.println("--- 測試 4: 校園場景模擬 ---");
        System.out.println("🏫 校園建築物連接圖");
        
        CampusMatrixGraph campus = new CampusMatrixGraph();
        
        // 校園建築物
        String[] buildings = {
            "圖書館", "教學大樓", "行政大樓", "體育館", "餐廳", 
            "宿舍A", "宿舍B", "綜合大樓", "停車場", "禮堂"
        };
        
        for (String building : buildings) {
            campus.addVertex(building);
        }
        
        // 校園道路連接
        campus.addEdge("圖書館", "教學大樓");
        campus.addEdge("圖書館", "行政大樓");
        campus.addEdge("圖書館", "餐廳");
        campus.addEdge("教學大樓", "綜合大樓");
        campus.addEdge("教學大樓", "體育館");
        campus.addEdge("行政大樓", "綜合大樓");
        campus.addEdge("行政大樓", "停車場");
        campus.addEdge("體育館", "宿舍A");
        campus.addEdge("體育館", "宿舍B");
        campus.addEdge("餐廳", "宿舍A");
        campus.addEdge("餐廳", "宿舍B");
        campus.addEdge("綜合大樓", "禮堂");
        campus.addEdge("禮堂", "停車場");
        campus.addEdge("宿舍A", "宿舍B");
        
        campus.printGraphInfo();
        campus.printAdjacencyMatrix();
        campus.printDegreeStatistics();
        
        // 查詢特定建築物的連接
        System.out.println("\n📍 查詢建築物連接:");
        String[] queryBuildings = {"圖書館", "體育館", "停車場"};
        for (String building : queryBuildings) {
            System.out.printf("  %s: 度=%d, 鄰居=%s%n", 
                             building, 
                             campus.getDegree(building),
                             campus.getNeighbors(building));
        }
        
        // 尋找中心建築 (度最高)
        System.out.println("\n📊 校園中心分析:");
        int maxDegree = 0;
        String center = "";
        for (String building : campus.getVertices()) {
            int degree = campus.getDegree(building);
            if (degree > maxDegree) {
                maxDegree = degree;
                center = building;
            }
        }
        System.out.printf("  中心建築: %s (連接 %d 個建築)%n", center, maxDegree);
    }
    
    /**
     * 測試完整功能展示
     */
    private static void testFullFunctionality() {
        System.out.println("--- 測試 5: 完整功能展示 ---");
        System.out.println("🎯 完整 API 功能展示");
        
        CampusMatrixGraph graph = new CampusMatrixGraph();
        
        // 建立測試圖
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("F");
        
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D");
        graph.addEdge("B", "E");
        graph.addEdge("C", "F");
        graph.addEdge("D", "E");
        graph.addEdge("D", "F");
        graph.addEdge("E", "F");
        
        graph.printGraphInfo();
        
        // 測試所有查詢方法
        System.out.println("\n📖 所有查詢方法測試:");
        for (String vertex : graph.getVertices()) {
            System.out.printf("  %s:%n", vertex);
            System.out.printf("    度: %d%n", graph.getDegree(vertex));
            System.out.printf("    逆度: %d%n", graph.getInDegree(vertex));
            System.out.printf("    鄰居: %s%n", graph.getNeighbors(vertex));
        }
        
        // 測試邊的查詢
        System.out.println("\n🔍 邊查詢測試:");
        String[][] edges = {{"A", "B"}, {"A", "C"}, {"A", "D"}, {"B", "D"}, {"B", "C"}};
        for (String[] edge : edges) {
            System.out.printf("  hasEdge('%s', '%s'): %s%n", 
                             edge[0], edge[1], graph.hasEdge(edge[0], edge[1]));
        }
        
        // 圖統計
        System.out.println("\n📊 圖統計:");
        System.out.printf("  節點數: %d%n", graph.getVertexCount());
        System.out.printf("  邊數: %d%n", graph.getEdgeCount());
        
        // 找出孤立節點 (度為 0)
        System.out.println("  孤立節點:");
        boolean hasIsolated = false;
        for (String vertex : graph.getVertices()) {
            if (graph.getDegree(vertex) == 0) {
                System.out.printf("    %s%n", vertex);
                hasIsolated = true;
            }
        }
        if (!hasIsolated) {
            System.out.println("    無");
        }
        
        // 測試清空
        System.out.println("\n🗑️ 清空圖:");
        graph.clear();
        graph.printGraphInfo();
        System.out.println();
    }
}