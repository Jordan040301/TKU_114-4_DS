import java.util.*;

/**
 * 校園導航系統
 * 使用 HashMap 儲存地點，鄰接表儲存路徑，BFS 還原最少邊路徑
 */
public class CampusNavigationSystem {
    
    /**
     * 地點類別
     */
    public static class Location {
        private final String name;
        private final String description;
        private final String building;
        private final double latitude;
        private final double longitude;
        
        public Location(String name, String description, String building, 
                        double latitude, double longitude) {
            this.name = name;
            this.description = description;
            this.building = building;
            this.latitude = latitude;
            this.longitude = longitude;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public String getBuilding() {
            return building;
        }
        
        public double getLatitude() {
            return latitude;
        }
        
        public double getLongitude() {
            return longitude;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%s)", name, building);
        }
        
        public String toDetailedString() {
            return String.format("📍 %s%n   建築: %s%n   描述: %s%n   位置: (%.6f, %.6f)",
                               name, building, description, latitude, longitude);
        }
    }
    
    /**
     * 路徑結果類別
     */
    public static class PathResult {
        private final List<Location> path;
        private final int totalSteps;
        private final List<String> directions;
        private final int totalDistance;  // 估算距離 (單位: 公尺)
        
        public PathResult(List<Location> path, List<String> directions, int totalDistance) {
            this.path = path;
            this.totalSteps = path.size() - 1;
            this.directions = directions;
            this.totalDistance = totalDistance;
        }
        
        public List<Location> getPath() {
            return path;
        }
        
        public int getTotalSteps() {
            return totalSteps;
        }
        
        public List<String> getDirections() {
            return directions;
        }
        
        public int getTotalDistance() {
            return totalDistance;
        }
        
        public boolean isReachable() {
            return !path.isEmpty();
        }
        
        @Override
        public String toString() {
            if (!isReachable()) {
                return "無法到達";
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== 導航路徑 ===\n");
            sb.append("起點: ").append(path.get(0).getName()).append("\n");
            sb.append("終點: ").append(path.get(path.size() - 1).getName()).append("\n");
            sb.append("總步數: ").append(totalSteps).append(" 步\n");
            sb.append("總距離: 約 ").append(totalDistance).append(" 公尺\n");
            
            sb.append("\n詳細路徑:\n");
            for (int i = 0; i < path.size(); i++) {
                Location loc = path.get(i);
                if (i == 0) {
                    sb.append("  🟢 起點: ").append(loc.getName()).append("\n");
                } else if (i == path.size() - 1) {
                    sb.append("  🔴 終點: ").append(loc.getName()).append("\n");
                } else {
                    sb.append("  ⚪ 經過: ").append(loc.getName()).append("\n");
                }
                sb.append("       ").append(loc.getDescription()).append("\n");
            }
            
            if (!directions.isEmpty()) {
                sb.append("\n📋 轉乘指引:\n");
                for (String dir : directions) {
                    sb.append("  ").append(dir).append("\n");
                }
            }
            
            return sb.toString();
        }
        
        public String toShortString() {
            if (!isReachable()) {
                return "無法到達";
            }
            List<String> names = new ArrayList<>();
            for (Location loc : path) {
                names.add(loc.getName());
            }
            return String.format("%s (共 %d 站)", String.join(" → ", names), totalSteps);
        }
    }
    
    // 地點儲存
    private Map<String, Location> locations;
    
    // 鄰接表 (地點名稱 → 相鄰地點名稱列表)
    private Map<String, Set<String>> adjacencyList;
    
    // 地點間距離 (用於估算)
    private Map<String, Map<String, Integer>> distanceMap;
    
    /**
     * 建構子
     */
    public CampusNavigationSystem() {
        this.locations = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.distanceMap = new HashMap<>();
    }
    
    /**
     * 新增地點
     * @param name 地點名稱
     * @param description 描述
     * @param building 建築名稱
     * @param latitude 緯度
     * @param longitude 經度
     */
    public void addLocation(String name, String description, String building,
                           double latitude, double longitude) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("地點名稱不能為空");
        }
        
        String normalized = name.trim();
        
        if (locations.containsKey(normalized)) {
            System.out.printf("⚠️ 地點 '%s' 已存在%n", normalized);
            return;
        }
        
        Location location = new Location(normalized, description, building, latitude, longitude);
        locations.put(normalized, location);
        adjacencyList.put(normalized, new HashSet<>());
        distanceMap.put(normalized, new HashMap<>());
        
        System.out.printf("✅ 新增地點: %s (%s)%n", normalized, building);
    }
    
    /**
     * 新增路徑 (雙向)
     * @param name1 地點1
     * @param name2 地點2
     * @param distance 距離 (公尺)
     */
    public void addPath(String name1, String name2, int distance) {
        if (name1 == null || name2 == null) {
            throw new IllegalArgumentException("地點名稱不能為 null");
        }
        
        String n1 = name1.trim();
        String n2 = name2.trim();
        
        if (!locations.containsKey(n1)) {
            System.out.printf("⚠️ 地點 '%s' 不存在%n", n1);
            return;
        }
        if (!locations.containsKey(n2)) {
            System.out.printf("⚠️ 地點 '%s' 不存在%n", n2);
            return;
        }
        
        if (n1.equals(n2)) {
            System.out.println("⚠️ 不能新增自環");
            return;
        }
        
        // 新增雙向路徑
        adjacencyList.get(n1).add(n2);
        adjacencyList.get(n2).add(n1);
        
        distanceMap.get(n1).put(n2, distance);
        distanceMap.get(n2).put(n1, distance);
        
        System.out.printf("✅ 新增路徑: %s ↔ %s (%.0f 公尺)%n", n1, n2, (double) distance);
    }
    
    /**
     * 尋找最短路徑 (BFS)
     * @param startName 起點名稱
     * @param endName 終點名稱
     * @return 路徑結果
     */
    public PathResult findShortestPath(String startName, String endName) {
        if (startName == null || endName == null) {
            return new PathResult(new ArrayList<>(), new ArrayList<>(), 0);
        }
        
        String start = startName.trim();
        String end = endName.trim();
        
        if (!locations.containsKey(start) || !locations.containsKey(end)) {
            System.out.printf("⚠️ 地點不存在: %s 或 %s%n", start, end);
            return new PathResult(new ArrayList<>(), new ArrayList<>(), 0);
        }
        
        if (start.equals(end)) {
            List<Location> path = Arrays.asList(locations.get(start));
            return new PathResult(path, new ArrayList<>(), 0);
        }
        
        // BFS
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Map<String, Integer> distance = new HashMap<>();
        
        queue.offer(start);
        visited.add(start);
        parent.put(start, null);
        distance.put(start, 0);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (current.equals(end)) {
                break;
            }
            
            for (String neighbor : adjacencyList.getOrDefault(current, new HashSet<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    distance.put(neighbor, distance.get(current) + 1);
                    queue.offer(neighbor);
                }
            }
        }
        
        if (!visited.contains(end)) {
            return new PathResult(new ArrayList<>(), new ArrayList<>(), 0);
        }
        
        // 重建路徑
        List<Location> path = new ArrayList<>();
        List<String> directions = new ArrayList<>();
        int totalDistance = 0;
        
        String current = end;
        while (current != null) {
            String prev = parent.get(current);
            if (prev != null) {
                // 計算距離
                int dist = distanceMap.get(prev).getOrDefault(current, 0);
                totalDistance += dist;
                
                // 生成方向指引
                String direction = generateDirection(prev, current);
                directions.add(0, direction);
            }
            path.add(0, locations.get(current));
            current = parent.get(current);
        }
        
        return new PathResult(path, directions, totalDistance);
    }
    
    /**
     * 生成方向指引
     */
    private String generateDirection(String from, String to) {
        Location fromLoc = locations.get(from);
        Location toLoc = locations.get(to);
        int dist = distanceMap.get(from).getOrDefault(to, 0);
        
        return String.format("從 %s 前往 %s (約 %d 公尺)", 
                           fromLoc.getName(), toLoc.getName(), dist);
    }
    
    /**
     * 查詢地點資訊
     * @param name 地點名稱
     * @return 地點資訊
     */
    public Location getLocation(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return locations.get(name.trim());
    }
    
    /**
     * 查詢地點的鄰居
     * @param name 地點名稱
     * @return 鄰居列表
     */
    public Set<String> getNeighbors(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new HashSet<>();
        }
        return adjacencyList.getOrDefault(name.trim(), new HashSet<>());
    }
    
    /**
     * 取得所有地點名稱
     * @return 地點名稱集合
     */
    public Set<String> getAllLocationNames() {
        return new HashSet<>(locations.keySet());
    }
    
    /**
     * 取得所有地點
     * @return 地點列表
     */
    public List<Location> getAllLocations() {
        return new ArrayList<>(locations.values());
    }
    
    /**
     * 搜尋地點 (關鍵字)
     * @param keyword 關鍵字
     * @return 符合的地點列表
     */
    public List<Location> searchLocation(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerKeyword = keyword.toLowerCase().trim();
        List<Location> results = new ArrayList<>();
        
        for (Location loc : locations.values()) {
            if (loc.getName().toLowerCase().contains(lowerKeyword) ||
                loc.getBuilding().toLowerCase().contains(lowerKeyword) ||
                loc.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(loc);
            }
        }
        
        return results;
    }
    
    /**
     * 印出校園地圖
     */
    public void printCampusMap() {
        System.out.println("\n=== 校園地圖 ===");
        
        if (locations.isEmpty()) {
            System.out.println("尚無地點資料");
            return;
        }
        
        System.out.printf("總地點數: %d%n", locations.size());
        System.out.printf("總路徑數: %d%n", getTotalPaths());
        System.out.println();
        
        System.out.println("📍 地點清單:");
        List<String> sortedNames = new ArrayList<>(locations.keySet());
        Collections.sort(sortedNames);
        
        for (String name : sortedNames) {
            Location loc = locations.get(name);
            Set<String> neighbors = adjacencyList.get(name);
            System.out.printf("  %s (%s)%n", name, loc.getBuilding());
            System.out.printf("    描述: %s%n", loc.getDescription());
            if (!neighbors.isEmpty()) {
                System.out.printf("    連接: %s%n", neighbors);
            } else {
                System.out.println("    連接: 無");
            }
        }
        System.out.println();
    }
    
    /**
     * 取得總路徑數
     */
    private int getTotalPaths() {
        int count = 0;
        for (Set<String> neighbors : adjacencyList.values()) {
            count += neighbors.size();
        }
        return count / 2;
    }
    
    /**
     * 印出路徑統計
     */
    public void printPathStatistics() {
        System.out.println("\n=== 路徑統計 ===");
        
        if (locations.isEmpty()) {
            System.out.println("尚無地點資料");
            return;
        }
        
        // 計算每個地點的度
        Map<String, Integer> degrees = new HashMap<>();
        for (String name : locations.keySet()) {
            degrees.put(name, adjacencyList.get(name).size());
        }
        
        // 找出中心和邊緣地點
        int maxDegree = 0;
        int minDegree = Integer.MAX_VALUE;
        String center = "";
        String edge = "";
        
        for (Map.Entry<String, Integer> entry : degrees.entrySet()) {
            if (entry.getValue() > maxDegree) {
                maxDegree = entry.getValue();
                center = entry.getKey();
            }
            if (entry.getValue() < minDegree) {
                minDegree = entry.getValue();
                edge = entry.getKey();
            }
        }
        
        System.out.printf("總地點數: %d%n", locations.size());
        System.out.printf("總路徑數: %d%n", getTotalPaths());
        System.out.printf("平均連接數: %.2f%n", (double) getTotalPaths() * 2 / locations.size());
        System.out.printf("中心地點: %s (連接 %d 個地點)%n", center, maxDegree);
        System.out.printf("邊緣地點: %s (連接 %d 個地點)%n", edge, minDegree);
        System.out.println();
    }
    
    /**
     * 清除所有資料
     */
    public void clear() {
        locations.clear();
        adjacencyList.clear();
        distanceMap.clear();
        System.out.println("🔄 已清空校園導航系統");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 校園導航系統測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：路徑規劃
        testPathPlanning();
        
        // 測試 3：地點搜尋
        testLocationSearch();
        
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
        
        CampusNavigationSystem campus = new CampusNavigationSystem();
        
        // 新增地點
        System.out.println("新增校園地點:");
        campus.addLocation("校門口", "學校正門", "正門", 25.0, 121.0);
        campus.addLocation("行政大樓", "學校行政中心", "行政大樓", 25.01, 121.01);
        campus.addLocation("圖書館", "學校圖書館", "圖書資訊大樓", 25.02, 121.02);
        campus.addLocation("教學大樓", "主要教學大樓", "教學大樓", 25.03, 121.03);
        campus.addLocation("體育館", "學校體育館", "體育館", 25.04, 121.04);
        campus.addLocation("宿舍區", "學生宿舍", "宿舍", 25.05, 121.05);
        campus.addLocation("餐廳", "學生餐廳", "餐廳", 25.015, 121.015);
        campus.addLocation("停車場", "停車場", "停車場", 25.025, 121.025);
        
        // 新增路徑
        System.out.println("\n新增校園路徑:");
        campus.addPath("校門口", "行政大樓", 50);
        campus.addPath("校門口", "停車場", 80);
        campus.addPath("行政大樓", "圖書館", 60);
        campus.addPath("行政大樓", "餐廳", 40);
        campus.addPath("圖書館", "教學大樓", 70);
        campus.addPath("圖書館", "餐廳", 50);
        campus.addPath("教學大樓", "體育館", 90);
        campus.addPath("教學大樓", "宿舍區", 100);
        campus.addPath("體育館", "宿舍區", 60);
        campus.addPath("餐廳", "停車場", 45);
        campus.addPath("宿舍區", "停車場", 110);
        
        campus.printCampusMap();
        campus.printPathStatistics();
    }
    
    /**
     * 測試路徑規劃
     */
    private static void testPathPlanning() {
        System.out.println("\n--- 測試 2: 路徑規劃 ---");
        
        CampusNavigationSystem campus = new CampusNavigationSystem();
        
        // 建立校園地圖
        campus.addLocation("校門口", "學校正門", "正門", 25.0, 121.0);
        campus.addLocation("行政大樓", "學校行政中心", "行政大樓", 25.01, 121.01);
        campus.addLocation("圖書館", "學校圖書館", "圖書資訊大樓", 25.02, 121.02);
        campus.addLocation("教學大樓", "主要教學大樓", "教學大樓", 25.03, 121.03);
        campus.addLocation("體育館", "學校體育館", "體育館", 25.04, 121.04);
        campus.addLocation("宿舍區", "學生宿舍", "宿舍", 25.05, 121.05);
        campus.addLocation("餐廳", "學生餐廳", "餐廳", 25.015, 121.015);
        campus.addLocation("停車場", "停車場", "停車場", 25.025, 121.025);
        
        campus.addPath("校門口", "行政大樓", 50);
        campus.addPath("校門口", "停車場", 80);
        campus.addPath("行政大樓", "圖書館", 60);
        campus.addPath("行政大樓", "餐廳", 40);
        campus.addPath("圖書館", "教學大樓", 70);
        campus.addPath("圖書館", "餐廳", 50);
        campus.addPath("教學大樓", "體育館", 90);
        campus.addPath("教學大樓", "宿舍區", 100);
        campus.addPath("體育館", "宿舍區", 60);
        campus.addPath("餐廳", "停車場", 45);
        campus.addPath("宿舍區", "停車場", 110);
        
        // 多組路徑查詢
        System.out.println("\n📋 路徑規劃查詢:");
        String[][] queries = {
            {"校門口", "教學大樓"},
            {"校門口", "宿舍區"},
            {"圖書館", "體育館"},
            {"餐廳", "宿舍區"},
            {"行政大樓", "體育館"},
            {"校門口", "體育館"}
        };
        
        for (String[] query : queries) {
            PathResult result = campus.findShortestPath(query[0], query[1]);
            if (result.isReachable()) {
                System.out.printf("  %s → %s: %s%n", 
                                 query[0], query[1], result.toShortString());
            } else {
                System.out.printf("  %s → %s: 無法到達%n", query[0], query[1]);
            }
        }
        
        // 顯示詳細路徑範例
        System.out.println("\n📖 詳細路徑範例 (校門口 → 宿舍區):");
        PathResult detail = campus.findShortestPath("校門口", "宿舍區");
        System.out.println(detail);
    }
    
    /**
     * 測試地點搜尋
     */
    private static void testLocationSearch() {
        System.out.println("\n--- 測試 3: 地點搜尋 ---");
        
        CampusNavigationSystem campus = new CampusNavigationSystem();
        
        campus.addLocation("校門口", "學校正門", "正門", 25.0, 121.0);
        campus.addLocation("行政大樓", "學校行政中心", "行政大樓", 25.01, 121.01);
        campus.addLocation("圖書館", "學校圖書館", "圖書資訊大樓", 25.02, 121.02);
        campus.addLocation("教學大樓", "主要教學大樓", "教學大樓", 25.03, 121.03);
        campus.addLocation("體育館", "學校體育館", "體育館", 25.04, 121.04);
        campus.addLocation("宿舍區", "學生宿舍", "宿舍", 25.05, 121.05);
        campus.addLocation("餐廳", "學生餐廳", "餐廳", 25.015, 121.015);
        
        System.out.println("🔍 關鍵字搜尋:");
        
        String[] keywords = {"大樓", "館", "宿舍", "餐廳", "門"};
        for (String keyword : keywords) {
            List<Location> results = campus.searchLocation(keyword);
            System.out.printf("  搜尋 '%s': %d 個結果%n", keyword, results.size());
            for (Location loc : results) {
                System.out.printf("    - %s (%s)%n", loc.getName(), loc.getBuilding());
            }
        }
        
        // 地點詳細資訊
        System.out.println("\n📌 地點詳細資訊:");
        System.out.println(campus.getLocation("圖書館").toDetailedString());
        System.out.println();
        System.out.println(campus.getLocation("體育館").toDetailedString());
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("\n--- 測試 4: 邊界情況 ---");
        
        CampusNavigationSystem campus = new CampusNavigationSystem();
        
        // 測試 4.1: 空系統
        System.out.println("測試 4.1: 空系統");
        campus.printCampusMap();
        PathResult result = campus.findShortestPath("A", "B");
        System.out.println("  路徑: " + result.toShortString());
        System.out.println();
        
        // 測試 4.2: 單一地點
        System.out.println("測試 4.2: 單一地點");
        campus.addLocation("單一地點", "只有這裡", "單一建築", 25.0, 121.0);
        campus.printCampusMap();
        result = campus.findShortestPath("單一地點", "單一地點");
        System.out.println("  相同地點路徑: " + result.toShortString());
        System.out.println();
        
        // 測試 4.3: 不存在的起點/終點
        System.out.println("測試 4.3: 不存在的起點/終點");
        campus.addLocation("A", "地點A", "建築A", 25.0, 121.0);
        campus.addLocation("B", "地點B", "建築B", 25.1, 121.1);
        campus.addPath("A", "B", 100);
        
        result = campus.findShortestPath("A", "不存在");
        System.out.println("  A → 不存在: " + result.toShortString());
        
        result = campus.findShortestPath("不存在", "A");
        System.out.println("  不存在 → A: " + result.toShortString());
        System.out.println();
        
        // 測試 4.4: 無法到達
        System.out.println("測試 4.4: 無法到達");
        campus.addLocation("C", "地點C", "建築C", 25.2, 121.2);
        result = campus.findShortestPath("A", "C");
        System.out.println("  A → C: " + result.toShortString());
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("\n--- 測試 5: 實際應用場景 ---");
        System.out.println("🏫 大學校園導航系統");
        
        CampusNavigationSystem university = new CampusNavigationSystem();
        
        // 建立完整校園地圖
        System.out.println("\n📝 建立校園地圖:");
        
        // 主要建築物
        university.addLocation("正門", "學校主要出入口", "正門", 25.0, 121.0);
        university.addLocation("圖書館", "學校圖書館", "圖書資訊大樓", 25.02, 121.01);
        university.addLocation("行政大樓", "學校行政中心", "行政大樓", 25.01, 121.015);
        university.addLocation("教學大樓A", "理學院教學大樓", "教學大樓A", 25.03, 121.02);
        university.addLocation("教學大樓B", "工學院教學大樓", "教學大樓B", 25.04, 121.03);
        university.addLocation("教學大樓C", "管理學院教學大樓", "教學大樓C", 25.025, 121.03);
        university.addLocation("體育館", "綜合體育館", "體育館", 25.05, 121.04);
        university.addLocation("學生活動中心", "學生社團活動中心", "活動中心", 25.015, 121.02);
        university.addLocation("餐廳", "學生餐廳", "餐廳", 25.01, 121.02);
        university.addLocation("宿舍A", "男生宿舍", "宿舍A", 25.06, 121.05);
        university.addLocation("宿舍B", "女生宿舍", "宿舍B", 25.07, 121.06);
        university.addLocation("停車場", "校園停車場", "停車場", 25.005, 121.005);
        university.addLocation("醫護室", "校園醫護室", "健康中心", 25.02, 121.025);
        university.addLocation("咖啡廳", "校園咖啡廳", "咖啡廳", 25.015, 121.025);
        
        // 校園路徑
        university.addPath("正門", "停車場", 30);
        university.addPath("正門", "行政大樓", 50);
        university.addPath("正門", "餐廳", 80);
        
        university.addPath("行政大樓", "圖書館", 60);
        university.addPath("行政大樓", "餐廳", 40);
        university.addPath("行政大樓", "學生活動中心", 70);
        
        university.addPath("圖書館", "教學大樓A", 50);
        university.addPath("圖書館", "咖啡廳", 30);
        university.addPath("圖書館", "醫護室", 45);
        university.addPath("圖書館", "學生活動中心", 60);
        
        university.addPath("教學大樓A", "教學大樓B", 80);
        university.addPath("教學大樓A", "教學大樓C", 60);
        
        university.addPath("教學大樓B", "體育館", 70);
        university.addPath("教學大樓B", "教學大樓C", 50);
        
        university.addPath("教學大樓C", "學生活動中心", 55);
        university.addPath("教學大樓C", "咖啡廳", 40);
        
        university.addPath("體育館", "宿舍A", 80);
        university.addPath("體育館", "宿舍B", 90);
        
        university.addPath("學生活動中心", "餐廳", 35);
        university.addPath("學生活動中心", "咖啡廳", 25);
        
        university.addPath("宿舍A", "宿舍B", 60);
        university.addPath("宿舍B", "停車場", 120);
        
        university.addPath("餐廳", "咖啡廳", 30);
        university.addPath("餐廳", "醫護室", 50);
        
        university.printCampusMap();
        university.printPathStatistics();
        
        // 常用路線
        System.out.println("\n📋 常用校園路線:");
        String[][] commonRoutes = {
            {"正門", "教學大樓A"},
            {"正門", "宿舍B"},
            {"圖書館", "體育館"},
            {"餐廳", "教學大樓B"},
            {"宿舍A", "教學大樓C"},
            {"醫護室", "停車場"}
        };
        
        for (String[] route : commonRoutes) {
            PathResult result = university.findShortestPath(route[0], route[1]);
            if (result.isReachable()) {
                System.out.printf("  %s → %s: %s%n", 
                                 route[0], route[1], result.toShortString());
            }
        }
        
        // 顯示詳細路徑
        System.out.println("\n📖 詳細導航範例 (正門 → 宿舍B):");
        PathResult detail = university.findShortestPath("正門", "宿舍B");
        System.out.println(detail);
        
        // 校園中心分析
        System.out.println("\n📊 校園中心分析:");
        Map<String, Integer> degrees = new HashMap<>();
        for (String name : university.getAllLocationNames()) {
            degrees.put(name, university.getNeighbors(name).size());
        }
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(degrees.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        System.out.println("  連接最多地點的建築:");
        for (int i = 0; i < Math.min(3, sorted.size()); i++) {
            System.out.printf("    #%d: %s (%d 個連接)%n", 
                             i + 1, sorted.get(i).getKey(), sorted.get(i).getValue());
        }
        
        // 地標推薦
        System.out.println("\n💡 校園地標推薦:");
        List<Location> allLoc = university.getAllLocations();
        Collections.sort(allLoc, (a, b) -> 
            Integer.compare(university.getNeighbors(b.getName()).size(),
                           university.getNeighbors(a.getName()).size()));
        
        for (int i = 0; i < Math.min(5, allLoc.size()); i++) {
            Location loc = allLoc.get(i);
            int degree = university.getNeighbors(loc.getName()).size();
            System.out.printf("  %s: %s (連接 %d 個地點)%n", 
                             loc.getName(), loc.getDescription(), degree);
        }
    }
}