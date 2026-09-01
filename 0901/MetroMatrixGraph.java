import java.util.*;

/**
 * 捷運矩陣圖
 * 使用無向鄰接矩陣管理捷運站點和路線
 */
public class MetroMatrixGraph {
    
    // 站點名稱列表
    private List<String> stations;
    
    // 鄰接矩陣 (無向圖)
    private int[][] adjacencyMatrix;
    
    // 站點名稱到索引的映射
    private Map<String, Integer> stationIndexMap;
    
    // 站點別名映射 (用於模糊搜尋)
    private Map<String, String> stationAliasMap;
    
    /**
     * 建構子
     */
    public MetroMatrixGraph() {
        this.stations = new ArrayList<>();
        this.adjacencyMatrix = new int[0][0];
        this.stationIndexMap = new HashMap<>();
        this.stationAliasMap = new HashMap<>();
    }
    
    /**
     * 新增捷運站點
     * @param stationName 站點名稱
     * @return true 如果成功新增
     */
    public boolean addStation(String stationName) {
        if (stationName == null || stationName.trim().isEmpty()) {
            throw new IllegalArgumentException("站點名稱不能為空");
        }
        
        String normalized = stationName.trim();
        
        if (stationIndexMap.containsKey(normalized)) {
            System.out.printf("⚠️ 站點 '%s' 已存在%n", normalized);
            return false;
        }
        
        // 擴展鄰接矩陣
        int newSize = stations.size() + 1;
        int[][] newMatrix = new int[newSize][newSize];
        
        // 複製原有矩陣
        for (int i = 0; i < stations.size(); i++) {
            System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, stations.size());
        }
        
        adjacencyMatrix = newMatrix;
        stationIndexMap.put(normalized, stations.size());
        stations.add(normalized);
        
        System.out.printf("✅ 新增捷運站: %s (索引: %d)%n", normalized, stations.size() - 1);
        return true;
    }
    
    /**
     * 新增站點別名 (用於方便查詢)
     * @param alias 別名
     * @param stationName 實際站名
     */
    public void addStationAlias(String alias, String stationName) {
        if (alias == null || alias.trim().isEmpty()) {
            throw new IllegalArgumentException("別名不能為空");
        }
        if (stationName == null || stationName.trim().isEmpty()) {
            throw new IllegalArgumentException("站點名稱不能為空");
        }
        
        String normalizedAlias = alias.trim().toUpperCase();
        String normalizedStation = stationName.trim();
        
        if (!stationIndexMap.containsKey(normalizedStation)) {
            System.out.printf("⚠️ 站點 '%s' 不存在%n", normalizedStation);
            return;
        }
        
        stationAliasMap.put(normalizedAlias, normalizedStation);
        System.out.printf("✅ 新增別名: %s → %s%n", normalizedAlias, normalizedStation);
    }
    
    /**
     * 新增無向邊 (捷運路線)
     * @param station1 站點1
     * @param station2 站點2
     * @return true 如果成功新增
     */
    public boolean addEdge(String station1, String station2) {
        if (station1 == null || station2 == null) {
            throw new IllegalArgumentException("站點名稱不能為 null");
        }
        
        String s1 = resolveStationName(station1);
        String s2 = resolveStationName(station2);
        
        if (s1 == null || s2 == null) {
            System.out.printf("⚠️ 站點不存在: %s 或 %s%n", station1, station2);
            return false;
        }
        
        if (s1.equals(s2)) {
            System.out.println("⚠️ 不能新增自環 (同一站點)");
            return false;
        }
        
        Integer index1 = stationIndexMap.get(s1);
        Integer index2 = stationIndexMap.get(s2);
        
        // 檢查是否已存在邊
        if (adjacencyMatrix[index1][index2] > 0) {
            System.out.printf("⚠️ 路線已存在: %s ↔ %s%n", s1, s2);
            return false;
        }
        
        // 新增無向邊 (對稱)
        adjacencyMatrix[index1][index2] = 1;
        adjacencyMatrix[index2][index1] = 1;
        
        System.out.printf("✅ 新增捷運路線: %s ↔ %s%n", s1, s2);
        return true;
    }
    
    /**
     * 新增帶有權重的邊 (用於距離或時間)
     * @param station1 站點1
     * @param station2 站點2
     * @param weight 權重 (距離或時間)
     * @return true 如果成功新增
     */
    public boolean addEdgeWithWeight(String station1, String station2, int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("權重必須大於 0");
        }
        
        if (station1 == null || station2 == null) {
            throw new IllegalArgumentException("站點名稱不能為 null");
        }
        
        String s1 = resolveStationName(station1);
        String s2 = resolveStationName(station2);
        
        if (s1 == null || s2 == null) {
            System.out.printf("⚠️ 站點不存在: %s 或 %s%n", station1, station2);
            return false;
        }
        
        if (s1.equals(s2)) {
            System.out.println("⚠️ 不能新增自環 (同一站點)");
            return false;
        }
        
        Integer index1 = stationIndexMap.get(s1);
        Integer index2 = stationIndexMap.get(s2);
        
        // 檢查是否已存在邊
        if (adjacencyMatrix[index1][index2] > 0) {
            System.out.printf("⚠️ 路線已存在: %s ↔ %s (權重: %d)%n", s1, s2, weight);
            return false;
        }
        
        // 新增無向邊 (對稱)
        adjacencyMatrix[index1][index2] = weight;
        adjacencyMatrix[index2][index1] = weight;
        
        System.out.printf("✅ 新增捷運路線: %s ↔ %s (權重: %d)%n", s1, s2, weight);
        return true;
    }
    
    /**
     * 解析站點名稱 (支援別名)
     */
    private String resolveStationName(String name) {
        if (name == null) return null;
        
        String normalized = name.trim();
        
        // 先檢查是否為別名
        String aliasKey = normalized.toUpperCase();
        if (stationAliasMap.containsKey(aliasKey)) {
            return stationAliasMap.get(aliasKey);
        }
        
        // 檢查是否為站點名稱
        if (stationIndexMap.containsKey(normalized)) {
            return normalized;
        }
        
        return null;
    }
    
    /**
     * 刪除捷運路線
     * @param station1 站點1
     * @param station2 站點2
     * @return true 如果成功刪除
     */
    public boolean removeEdge(String station1, String station2) {
        if (station1 == null || station2 == null) {
            throw new IllegalArgumentException("站點名稱不能為 null");
        }
        
        String s1 = resolveStationName(station1);
        String s2 = resolveStationName(station2);
        
        if (s1 == null || s2 == null) {
            System.out.printf("⚠️ 站點不存在: %s 或 %s%n", station1, station2);
            return false;
        }
        
        Integer index1 = stationIndexMap.get(s1);
        Integer index2 = stationIndexMap.get(s2);
        
        // 檢查邊是否存在
        if (adjacencyMatrix[index1][index2] == 0) {
            System.out.printf("⚠️ 路線不存在: %s ↔ %s%n", s1, s2);
            return false;
        }
        
        // 刪除無向邊 (對稱)
        adjacencyMatrix[index1][index2] = 0;
        adjacencyMatrix[index2][index1] = 0;
        
        System.out.printf("🗑️ 刪除捷運路線: %s ↔ %s%n", s1, s2);
        return true;
    }
    
    /**
     * 取得站點的度 (連接數)
     * @param stationName 站點名稱
     * @return 度數
     */
    public int getDegree(String stationName) {
        String s = resolveStationName(stationName);
        if (s == null) {
            System.out.printf("⚠️ 站點不存在: %s%n", stationName);
            return -1;
        }
        
        Integer index = stationIndexMap.get(s);
        int degree = 0;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[index][i] > 0) {
                degree++;
            }
        }
        return degree;
    }
    
    /**
     * 取得站點的鄰居列表
     * @param stationName 站點名稱
     * @return 鄰居列表
     */
    public List<String> getNeighbors(String stationName) {
        String s = resolveStationName(stationName);
        if (s == null) {
            System.out.printf("⚠️ 站點不存在: %s%n", stationName);
            return new ArrayList<>();
        }
        
        Integer index = stationIndexMap.get(s);
        List<String> neighbors = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[index][i] > 0) {
                neighbors.add(stations.get(i));
            }
        }
        return neighbors;
    }
    
    /**
     * 取得站點的連接權重 (距離或時間)
     * @param station1 站點1
     * @param station2 站點2
     * @return 權重，如果沒有連接則回傳 -1
     */
    public int getEdgeWeight(String station1, String station2) {
        String s1 = resolveStationName(station1);
        String s2 = resolveStationName(station2);
        
        if (s1 == null || s2 == null) {
            return -1;
        }
        
        Integer index1 = stationIndexMap.get(s1);
        Integer index2 = stationIndexMap.get(s2);
        
        return adjacencyMatrix[index1][index2];
    }
    
    /**
     * 檢查兩個站點是否相連
     * @param station1 站點1
     * @param station2 站點2
     * @return true 如果相連
     */
    public boolean areConnected(String station1, String station2) {
        return getEdgeWeight(station1, station2) > 0;
    }
    
    /**
     * 取得所有站點
     * @return 站點列表
     */
    public List<String> getStations() {
        return new ArrayList<>(stations);
    }
    
    /**
     * 取得站點總數
     * @return 站點數量
     */
    public int getStationCount() {
        return stations.size();
    }
    
    /**
     * 取得路線總數
     * @return 路線數量
     */
    public int getEdgeCount() {
        int count = 0;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = i + 1; j < adjacencyMatrix[i].length; j++) {
                if (adjacencyMatrix[i][j] > 0) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * 檢查站點是否存在
     * @param stationName 站點名稱
     * @return true 如果存在
     */
    public boolean containsStation(String stationName) {
        return resolveStationName(stationName) != null;
    }
    
    /**
     * 取得所有站點的度
     * @return 站點度數對應表
     */
    public Map<String, Integer> getAllDegrees() {
        Map<String, Integer> degrees = new LinkedHashMap<>();
        for (String station : stations) {
            degrees.put(station, getDegree(station));
        }
        return degrees;
    }
    
    /**
     * 找出轉乘站 (度 >= 2)
     * @return 轉乘站列表
     */
    public List<String> getTransferStations() {
        List<String> transferStations = new ArrayList<>();
        for (String station : stations) {
            if (getDegree(station) >= 2) {
                transferStations.add(station);
            }
        }
        return transferStations;
    }
    
    /**
     * 找出終點站 (度 == 1)
     * @return 終點站列表
     */
    public List<String> getTerminalStations() {
        List<String> terminalStations = new ArrayList<>();
        for (String station : stations) {
            if (getDegree(station) == 1) {
                terminalStations.add(station);
            }
        }
        return terminalStations;
    }
    
    /**
     * 找出孤立站 (度 == 0)
     * @return 孤立站列表
     */
    public List<String> getIsolatedStations() {
        List<String> isolatedStations = new ArrayList<>();
        for (String station : stations) {
            if (getDegree(station) == 0) {
                isolatedStations.add(station);
            }
        }
        return isolatedStations;
    }
    
    /**
     * 計算兩站點之間的最短路徑 (BFS)
     * @param start 起點
     * @param end 終點
     * @return 路徑列表
     */
    public List<String> findShortestPath(String start, String end) {
        String s1 = resolveStationName(start);
        String s2 = resolveStationName(end);
        
        if (s1 == null || s2 == null) {
            System.out.printf("⚠️ 站點不存在: %s 或 %s%n", start, end);
            return new ArrayList<>();
        }
        
        if (s1.equals(s2)) {
            return Arrays.asList(s1);
        }
        
        int startIndex = stationIndexMap.get(s1);
        int endIndex = stationIndexMap.get(s2);
        
        // BFS
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[stations.size()];
        int[] parent = new int[stations.size()];
        Arrays.fill(parent, -1);
        
        queue.offer(startIndex);
        visited[startIndex] = true;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            if (current == endIndex) {
                break;
            }
            
            for (int i = 0; i < stations.size(); i++) {
                if (adjacencyMatrix[current][i] > 0 && !visited[i]) {
                    visited[i] = true;
                    parent[i] = current;
                    queue.offer(i);
                }
            }
        }
        
        // 重建路徑
        List<String> path = new ArrayList<>();
        if (!visited[endIndex]) {
            return path;
        }
        
        int current = endIndex;
        while (current != -1) {
            path.add(0, stations.get(current));
            current = parent[current];
        }
        
        return path;
    }
    
    /**
     * 印出鄰接矩陣
     */
    public void printAdjacencyMatrix() {
        System.out.println("\n=== 捷運鄰接矩陣 ===");
        
        if (stations.isEmpty()) {
            System.out.println("無捷運站點資料");
            return;
        }
        
        // 印出標頭
        System.out.print("     ");
        for (String station : stations) {
            System.out.printf("%6s", station);
        }
        System.out.println();
        
        System.out.print("     ");
        for (int i = 0; i < stations.size(); i++) {
            System.out.print("------");
        }
        System.out.println();
        
        // 印出矩陣
        for (int i = 0; i < stations.size(); i++) {
            System.out.printf("%4s ", stations.get(i));
            for (int j = 0; j < stations.size(); j++) {
                if (adjacencyMatrix[i][j] > 0) {
                    System.out.printf("%6d", adjacencyMatrix[i][j]);
                } else {
                    System.out.print("     -");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 印出站點詳細報告
     */
    public void printStationReport() {
        System.out.println("\n=== 捷運站點報告 ===");
        
        if (stations.isEmpty()) {
            System.out.println("無捷運站點資料");
            return;
        }
        
        System.out.printf("總站點數: %d%n", getStationCount());
        System.out.printf("總路線數: %d%n", getEdgeCount());
        System.out.println();
        
        // 站點詳細資訊
        System.out.printf("%-12s | %-8s | %-8s | %s%n", 
                         "站點名稱", "度數", "類型", "連接站點");
        System.out.println("------------|----------|----------|------------------------------");
        
        List<String> sortedStations = new ArrayList<>(stations);
        Collections.sort(sortedStations);
        
        for (String station : sortedStations) {
            int degree = getDegree(station);
            List<String> neighbors = getNeighbors(station);
            
            String type;
            if (degree == 0) {
                type = "孤立站";
            } else if (degree == 1) {
                type = "終點站";
            } else if (degree >= 3) {
                type = "轉乘站";
            } else {
                type = "一般站";
            }
            
            System.out.printf("%-12s | %8d | %-8s | %s%n",
                             station, degree, type, 
                             neighbors.isEmpty() ? "無" : neighbors.toString());
        }
        System.out.println();
        
        // 特殊站點統計
        List<String> terminals = getTerminalStations();
        List<String> transfers = getTransferStations();
        List<String> isolated = getIsolatedStations();
        
        System.out.println("📌 終點站 (" + terminals.size() + " 個): " + 
                          (terminals.isEmpty() ? "無" : terminals.toString()));
        System.out.println("📌 轉乘站 (" + transfers.size() + " 個): " + 
                          (transfers.isEmpty() ? "無" : transfers.toString()));
        System.out.println("📌 孤立站 (" + isolated.size() + " 個): " + 
                          (isolated.isEmpty() ? "無" : isolated.toString()));
        System.out.println();
    }
    
    /**
     * 印出完整報告
     */
    public void printFullReport() {
        printStationReport();
        printAdjacencyMatrix();
        
        // 路線列表
        System.out.println("=== 捷運路線列表 ===");
        if (getEdgeCount() == 0) {
            System.out.println("無捷運路線");
            return;
        }
        
        System.out.println("路線 | 權重");
        System.out.println("-----|------");
        for (int i = 0; i < stations.size(); i++) {
            for (int j = i + 1; j < stations.size(); j++) {
                if (adjacencyMatrix[i][j] > 0) {
                    System.out.printf("%s ↔ %s | %d%n", 
                                     stations.get(i), stations.get(j), 
                                     adjacencyMatrix[i][j]);
                }
            }
        }
        System.out.println();
    }
    
    /**
     * 清空所有資料
     */
    public void clear() {
        stations.clear();
        adjacencyMatrix = new int[0][0];
        stationIndexMap.clear();
        stationAliasMap.clear();
        System.out.println("🔄 已清空捷運矩陣");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 捷運矩陣系統測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：進階功能
        testAdvancedFunctionality();
        
        // 測試 3：路徑搜尋
        testPathFinding();
        
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
        
        MetroMatrixGraph metro = new MetroMatrixGraph();
        
        // 新增捷運站
        System.out.println("新增捷運站:");
        String[] stations = {"台北車站", "西門", "中正紀念堂", "古亭", "南京復興", "忠孝新生"};
        for (String station : stations) {
            metro.addStation(station);
        }
        
        // 新增路線
        System.out.println("\n新增捷運路線:");
        metro.addEdgeWithWeight("台北車站", "西門", 2);
        metro.addEdgeWithWeight("台北車站", "中正紀念堂", 3);
        metro.addEdgeWithWeight("西門", "中正紀念堂", 2);
        metro.addEdgeWithWeight("中正紀念堂", "古亭", 2);
        metro.addEdgeWithWeight("台北車站", "南京復興", 4);
        metro.addEdgeWithWeight("南京復興", "忠孝新生", 3);
        metro.addEdgeWithWeight("忠孝新生", "古亭", 3);
        
        metro.printFullReport();
        
        // 查詢特定站點
        System.out.println("\n📋 查詢測試:");
        System.out.println("  getDegree('台北車站'): " + metro.getDegree("台北車站"));
        System.out.println("  getNeighbors('台北車站'): " + metro.getNeighbors("台北車站"));
        System.out.println("  getEdgeWeight('台北車站', '西門'): " + metro.getEdgeWeight("台北車站", "西門"));
        System.out.println("  areConnected('台北車站', '古亭'): " + metro.areConnected("台北車站", "古亭"));
        System.out.println();
    }
    
    /**
     * 測試進階功能
     */
    private static void testAdvancedFunctionality() {
        System.out.println("--- 測試 2: 進階功能 ---");
        
        MetroMatrixGraph metro = new MetroMatrixGraph();
        
        // 建立捷運網絡
        String[] stations = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String station : stations) {
            metro.addStation(station);
        }
        
        metro.addEdgeWithWeight("A", "B", 5);
        metro.addEdgeWithWeight("A", "C", 3);
        metro.addEdgeWithWeight("B", "D", 4);
        metro.addEdgeWithWeight("B", "E", 6);
        metro.addEdgeWithWeight("C", "E", 2);
        metro.addEdgeWithWeight("C", "F", 5);
        metro.addEdgeWithWeight("D", "G", 3);
        metro.addEdgeWithWeight("E", "G", 4);
        metro.addEdgeWithWeight("E", "H", 2);
        metro.addEdgeWithWeight("F", "H", 5);
        
        metro.printStationReport();
        
        // 測試特殊站點
        System.out.println("\n🔍 特殊站點分析:");
        System.out.println("  終點站: " + metro.getTerminalStations());
        System.out.println("  轉乘站: " + metro.getTransferStations());
        System.out.println("  孤立站: " + metro.getIsolatedStations());
        
        // 測試刪除路線
        System.out.println("\n🗑️ 刪除路線:");
        metro.removeEdge("A", "C");
        metro.removeEdge("E", "H");
        
        metro.printStationReport();
    }
    
    /**
     * 測試路徑搜尋
     */
    private static void testPathFinding() {
        System.out.println("--- 測試 3: 路徑搜尋 ---");
        
        MetroMatrixGraph metro = new MetroMatrixGraph();
        
        // 建立捷運網絡
        String[] stations = {"台北車站", "西門", "中正紀念堂", "古亭", "大安", 
                            "忠孝復興", "南京復興", "松山", "南港"};
        for (String station : stations) {
            metro.addStation(station);
        }
        
        metro.addEdge("台北車站", "西門");
        metro.addEdge("台北車站", "中正紀念堂");
        metro.addEdge("西門", "中正紀念堂");
        metro.addEdge("中正紀念堂", "古亭");
        metro.addEdge("古亭", "大安");
        metro.addEdge("大安", "忠孝復興");
        metro.addEdge("忠孝復興", "南京復興");
        metro.addEdge("南京復興", "松山");
        metro.addEdge("松山", "南港");
        metro.addEdge("忠孝復興", "南港");
        metro.addEdge("台北車站", "忠孝復興");
        
        metro.printAdjacencyMatrix();
        
        // 測試路徑搜尋
        System.out.println("\n🗺️ 最短路徑搜尋:");
        String[][] testPaths = {
            {"台北車站", "南港"},
            {"西門", "松山"},
            {"古亭", "南京復興"},
            {"大安", "西門"},
            {"松山", "台北車站"}
        };
        
        for (String[] path : testPaths) {
            List<String> result = metro.findShortestPath(path[0], path[1]);
            if (result.isEmpty()) {
                System.out.printf("  %s → %s: 無路徑%n", path[0], path[1]);
            } else {
                System.out.printf("  %s → %s: %s (共 %d 站)%n", 
                                 path[0], path[1], 
                                 String.join(" → ", result), 
                                 result.size() - 1);
            }
        }
        System.out.println();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空系統
        System.out.println("測試 4.1: 空系統");
        MetroMatrixGraph metro = new MetroMatrixGraph();
        metro.printStationReport();
        metro.printAdjacencyMatrix();
        System.out.println("  getStationCount: " + metro.getStationCount());
        System.out.println("  getEdgeCount: " + metro.getEdgeCount());
        System.out.println();
        
        // 測試 4.2: 單一站點
        System.out.println("測試 4.2: 單一站點");
        metro.addStation("單一站");
        metro.printStationReport();
        System.out.println("  getDegree('單一站'): " + metro.getDegree("單一站"));
        System.out.println("  getNeighbors('單一站'): " + metro.getNeighbors("單一站"));
        System.out.println("  getTerminalStations: " + metro.getTerminalStations());
        System.out.println();
        
        // 測試 4.3: 別名功能
        System.out.println("測試 4.3: 別名功能");
        metro.addStationAlias("SZ", "單一站");
        metro.addEdgeWithWeight("單一站", "單一站", 5);  // 自環測試
        System.out.println("  透過別名查詢: getDegree('SZ') = " + metro.getDegree("SZ"));
        System.out.println();
        
        // 測試 4.4: 不存在的站點
        System.out.println("測試 4.4: 不存在的站點");
        metro.addEdge("不存在", "單一站");
        metro.removeEdge("不存在", "單一站");
        metro.getDegree("不存在");
        metro.getNeighbors("不存在");
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 5: 實際應用場景 ---");
        System.out.println("🚇 台北捷運路網簡化版");
        
        MetroMatrixGraph taipeiMetro = new MetroMatrixGraph();
        
        // 台北捷運主要站點
        String[] stations = {
            "台北車站", "西門", "中正紀念堂", "古亭", "大安", "忠孝復興",
            "南京復興", "松山", "南港", "市政府", "國父紀念館", "忠孝敦化",
            "台北101", "象山", "大安森林公園", "科技大樓", "六張犁", "麟光",
            "辛亥", "萬芳醫院", "萬芳社區", "木柵", "動物園"
        };
        
        for (String station : stations) {
            taipeiMetro.addStation(station);
        }
        
        // 新增別名 (方便查詢)
        taipeiMetro.addStationAlias("TPE", "台北車站");
        taipeiMetro.addStationAlias("101", "台北101");
        taipeiMetro.addStationAlias("ZOO", "動物園");
        
        // 建立捷運路線 (簡化版)
        // 紅線 (淡水信義線)
        taipeiMetro.addEdge("台北車站", "中正紀念堂");
        taipeiMetro.addEdge("中正紀念堂", "古亭");
        taipeiMetro.addEdge("古亭", "大安");
        taipeiMetro.addEdge("大安", "大安森林公園");
        taipeiMetro.addEdge("大安森林公園", "台北101");
        taipeiMetro.addEdge("台北101", "象山");
        
        // 藍線 (板南線)
        taipeiMetro.addEdge("台北車站", "西門");
        taipeiMetro.addEdge("西門", "忠孝敦化");
        taipeiMetro.addEdge("忠孝敦化", "忠孝復興");
        taipeiMetro.addEdge("忠孝復興", "國父紀念館");
        taipeiMetro.addEdge("國父紀念館", "市政府");
        taipeiMetro.addEdge("市政府", "南港");
        
        // 棕線 (文湖線)
        taipeiMetro.addEdge("忠孝復興", "南京復興");
        taipeiMetro.addEdge("南京復興", "科技大樓");
        taipeiMetro.addEdge("科技大樓", "六張犁");
        taipeiMetro.addEdge("六張犁", "麟光");
        taipeiMetro.addEdge("麟光", "辛亥");
        taipeiMetro.addEdge("辛亥", "萬芳醫院");
        taipeiMetro.addEdge("萬芳醫院", "萬芳社區");
        taipeiMetro.addEdge("萬芳社區", "木柵");
        taipeiMetro.addEdge("木柵", "動物園");
        
        // 連接線
        taipeiMetro.addEdge("古亭", "中正紀念堂");
        taipeiMetro.addEdge("大安", "忠孝復興");
        taipeiMetro.addEdge("松山", "南京復興");
        taipeiMetro.addEdge("南港", "松山");
        
        taipeiMetro.printFullReport();
        
        // 路線分析
        System.out.println("\n📊 捷運路網分析:");
        System.out.println("  總站點數: " + taipeiMetro.getStationCount());
        System.out.println("  總路線數: " + taipeiMetro.getEdgeCount());
        System.out.println("  轉乘站: " + taipeiMetro.getTransferStations());
        System.out.println("  終點站: " + taipeiMetro.getTerminalStations());
        
        // 路徑規劃
        System.out.println("\n🗺️ 路徑規劃範例:");
        String[][] travelPlans = {
            {"台北車站", "動物園"},
            {"西門", "台北101"},
            {"忠孝復興", "象山"},
            {"市政府", "動物園"},
            {"台北車站", "南港"}
        };
        
        for (String[] plan : travelPlans) {
            List<String> path = taipeiMetro.findShortestPath(plan[0], plan[1]);
            if (!path.isEmpty()) {
                System.out.printf("  %s → %s: %s (經過 %d 站)%n", 
                                 plan[0], plan[1], 
                                 String.join(" → ", path), 
                                 path.size() - 1);
            } else {
                System.out.printf("  %s → %s: 無法到達%n", plan[0], plan[1]);
            }
        }
        
        // 中心站點分析
        System.out.println("\n🎯 中心站點分析:");
        Map<String, Integer> degrees = taipeiMetro.getAllDegrees();
        List<Map.Entry<String, Integer>> sortedDegrees = new ArrayList<>(degrees.entrySet());
        sortedDegrees.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        System.out.println("  排名 | 站點 | 度數");
        System.out.println("  -----|------|------");
        for (int i = 0; i < Math.min(5, sortedDegrees.size()); i++) {
            System.out.printf("  %4d | %-4s | %4d%n", 
                             i + 1, sortedDegrees.get(i).getKey(), 
                             sortedDegrees.get(i).getValue());
        }
    }
}