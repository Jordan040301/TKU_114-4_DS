import java.util.*;

/**
 * 捷運轉乘路徑
 * 輸出最少站數路徑及轉乘次數
 */
public class MetroTransferPath {
    
    /**
     * 捷運站點類別
     */
    public static class Station {
        private final String name;
        private final Set<String> lines;  // 該站點所屬的路線
        
        public Station(String name) {
            this.name = name;
            this.lines = new HashSet<>();
        }
        
        public Station(String name, String line) {
            this.name = name;
            this.lines = new HashSet<>();
            this.lines.add(line);
        }
        
        public String getName() {
            return name;
        }
        
        public Set<String> getLines() {
            return lines;
        }
        
        public void addLine(String line) {
            lines.add(line);
        }
        
        public boolean isTransferStation() {
            return lines.size() >= 2;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Station station = (Station) obj;
            return Objects.equals(name, station.name);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
        
        @Override
        public String toString() {
            return name + (isTransferStation() ? " (轉乘站)" : "");
        }
    }
    
    /**
     * 路徑結果類別
     */
    public static class PathResult {
        private final List<String> stations;
        private final int totalStations;
        private final int transfers;
        private final List<String> linesUsed;
        private final Map<String, String> lineAtStation;
        
        public PathResult(List<String> stations, int transfers, List<String> linesUsed, 
                         Map<String, String> lineAtStation) {
            this.stations = stations;
            this.totalStations = stations.size() - 1;
            this.transfers = transfers;
            this.linesUsed = linesUsed;
            this.lineAtStation = lineAtStation;
        }
        
        public List<String> getStations() {
            return stations;
        }
        
        public int getTotalStations() {
            return totalStations;
        }
        
        public int getTransfers() {
            return transfers;
        }
        
        public List<String> getLinesUsed() {
            return linesUsed;
        }
        
        public Map<String, String> getLineAtStation() {
            return lineAtStation;
        }
        
        public boolean isReachable() {
            return !stations.isEmpty();
        }
        
        @Override
        public String toString() {
            if (!isReachable()) {
                return "無法到達";
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== 捷運路徑結果 ===\n");
            sb.append("起點: ").append(stations.get(0)).append("\n");
            sb.append("終點: ").append(stations.get(stations.size() - 1)).append("\n");
            sb.append("總站數: ").append(totalStations).append(" 站\n");
            sb.append("轉乘次數: ").append(transfers).append(" 次\n");
            sb.append("使用路線: ").append(String.join(" → ", linesUsed)).append("\n");
            
            sb.append("\n詳細路徑:\n");
            for (int i = 0; i < stations.size(); i++) {
                String station = stations.get(i);
                String line = lineAtStation.get(station);
                if (i == 0) {
                    sb.append("  🟢 上車: ").append(station).append(" (").append(line).append(")\n");
                } else if (i == stations.size() - 1) {
                    sb.append("  🔴 下車: ").append(station).append(" (").append(line).append(")\n");
                } else {
                    // 檢查是否為轉乘站
                    String prevLine = lineAtStation.get(stations.get(i - 1));
                    if (!line.equals(prevLine)) {
                        sb.append("  🔄 轉乘: ").append(station).append(" (")
                          .append(prevLine).append(" → ").append(line).append(")\n");
                    } else {
                        sb.append("  ⚪ 經過: ").append(station).append(" (").append(line).append(")\n");
                    }
                }
            }
            
            return sb.toString();
        }
        
        public String toShortString() {
            if (!isReachable()) {
                return "無法到達";
            }
            return String.format("%s (共 %d 站, 轉乘 %d 次)", 
                               String.join(" → ", stations), totalStations, transfers);
        }
    }
    
    // 捷運網絡
    private Map<String, Station> stations;
    private Map<String, Set<String>> adjacencyList;  // 站點相鄰關係
    private Map<String, Map<String, String>> lineMap; // 站點間的路線資訊
    
    /**
     * 建構子
     */
    public MetroTransferPath() {
        this.stations = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.lineMap = new HashMap<>();
    }
    
    /**
     * 新增捷運站點
     * @param name 站點名稱
     * @param line 所屬路線
     */
    public void addStation(String name, String line) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("站點名稱不能為空");
        }
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("路線名稱不能為空");
        }
        
        String stationName = name.trim();
        String lineName = line.trim();
        
        if (!stations.containsKey(stationName)) {
            stations.put(stationName, new Station(stationName, lineName));
            adjacencyList.put(stationName, new HashSet<>());
            lineMap.put(stationName, new HashMap<>());
        } else {
            stations.get(stationName).addLine(lineName);
        }
    }
    
    /**
     * 新增捷運站點 (轉乘站，多條路線)
     * @param name 站點名稱
     * @param lines 所屬路線列表
     */
    public void addStation(String name, String... lines) {
        for (String line : lines) {
            addStation(name, line);
        }
    }
    
    /**
     * 新增相鄰站點 (同一路線上的相鄰站)
     * @param station1 站點1
     * @param station2 站點2
     * @param line 路線名稱
     */
    public void addAdjacentStations(String station1, String station2, String line) {
        if (station1 == null || station2 == null || line == null) {
            throw new IllegalArgumentException("參數不能為 null");
        }
        
        String s1 = station1.trim();
        String s2 = station2.trim();
        String l = line.trim();
        
        if (!stations.containsKey(s1)) {
            addStation(s1, l);
        }
        if (!stations.containsKey(s2)) {
            addStation(s2, l);
        }
        
        // 新增雙向連接
        adjacencyList.get(s1).add(s2);
        adjacencyList.get(s2).add(s1);
        
        // 記錄站點間的路線
        lineMap.get(s1).put(s2, l);
        lineMap.get(s2).put(s1, l);
    }
    
    /**
     * 新增完整路線
     * @param line 路線名稱
     * @param stationNames 站點名稱列表 (依序)
     */
    public void addLine(String line, String... stationNames) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("路線名稱不能為空");
        }
        if (stationNames.length < 2) {
            throw new IllegalArgumentException("路線至少需要 2 個站點");
        }
        
        String lineName = line.trim();
        
        for (int i = 0; i < stationNames.length; i++) {
            String station = stationNames[i].trim();
            addStation(station, lineName);
            
            if (i > 0) {
                String prev = stationNames[i - 1].trim();
                addAdjacentStations(prev, station, lineName);
            }
        }
        
        System.out.printf("✅ 新增路線 %s: %s%n", lineName, 
                         String.join(" → ", stationNames));
    }
    
    /**
     * 尋找最少站數路徑 (BFS)
     * @param start 起點
     * @param end 終點
     * @return 路徑結果
     */
    public PathResult findShortestPath(String start, String end) {
        if (start == null || end == null) {
            return new PathResult(new ArrayList<>(), -1, new ArrayList<>(), new HashMap<>());
        }
        
        String s = start.trim();
        String e = end.trim();
        
        if (!stations.containsKey(s) || !stations.containsKey(e)) {
            System.out.printf("⚠️ 站點不存在: %s 或 %s%n", s, e);
            return new PathResult(new ArrayList<>(), -1, new ArrayList<>(), new HashMap<>());
        }
        
        if (s.equals(e)) {
            List<String> path = Arrays.asList(s);
            Map<String, String> lineAtStation = new HashMap<>();
            lineAtStation.put(s, stations.get(s).getLines().iterator().next());
            return new PathResult(path, 0, new ArrayList<>(), lineAtStation);
        }
        
        // BFS
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Map<String, String> parentLine = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(s);
        visited.add(s);
        parent.put(s, null);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (current.equals(e)) {
                break;
            }
            
            for (String neighbor : adjacencyList.getOrDefault(current, new HashSet<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    
                    // 記錄從 current 到 neighbor 的路線
                    String line = lineMap.get(current).get(neighbor);
                    parentLine.put(neighbor, line);
                    
                    queue.offer(neighbor);
                }
            }
        }
        
        if (!visited.contains(e)) {
            return new PathResult(new ArrayList<>(), -1, new ArrayList<>(), new HashMap<>());
        }
        
        // 重建路徑
        List<String> path = new ArrayList<>();
        Map<String, String> lineAtStation = new HashMap<>();
        String current = e;
        while (current != null) {
            path.add(0, current);
            if (parentLine.containsKey(current)) {
                lineAtStation.put(current, parentLine.get(current));
            }
            current = parent.get(current);
        }
        
        // 設置起點的路線
        String startLine = lineAtStation.get(path.get(1));
        lineAtStation.put(path.get(0), startLine);
        
        // 計算轉乘次數和使用的路線
        int transfers = 0;
        List<String> linesUsed = new ArrayList<>();
        String currentLine = startLine;
        linesUsed.add(currentLine);
        
        for (int i = 1; i < path.size(); i++) {
            String station = path.get(i);
            String line = lineAtStation.get(station);
            
            if (!line.equals(currentLine)) {
                transfers++;
                currentLine = line;
                linesUsed.add(line);
            }
        }
        
        return new PathResult(path, transfers, linesUsed, lineAtStation);
    }
    
    /**
     * 輸出轉乘路徑報告
     * @param start 起點
     * @param end 終點
     */
    public void printPath(String start, String end) {
        PathResult result = findShortestPath(start, end);
        if (result.isReachable()) {
            System.out.println(result);
        } else {
            System.out.printf("⚠️ 無法從 %s 到達 %s%n", start, end);
        }
    }
    
    /**
     * 執行多組路徑查詢
     * @param queries 查詢陣列 [start1, end1, start2, end2, ...]
     */
    public void queryPaths(String... queries) {
        if (queries.length % 2 != 0) {
            throw new IllegalArgumentException("查詢參數必須為成對的 (起點, 終點)");
        }
        
        System.out.println("\n=== 捷運路徑查詢 ===");
        System.out.println("查詢 | 起點 → 終點 | 站數 | 轉乘次數 | 路徑");
        System.out.println("-----|-------------|------|----------|------------------------------");
        
        int queryId = 1;
        for (int i = 0; i < queries.length; i += 2) {
            String start = queries[i];
            String end = queries[i + 1];
            
            PathResult result = findShortestPath(start, end);
            
            if (result.isReachable()) {
                System.out.printf("%4d | %s → %s | %4d | %8d | %s%n",
                                 queryId++, start, end,
                                 result.getTotalStations(),
                                 result.getTransfers(),
                                 String.join(" → ", result.getStations()));
            } else {
                System.out.printf("%4d | %s → %s | %4s | %8s | %s%n",
                                 queryId++, start, end,
                                 "✗", "✗", "無法到達");
            }
        }
        System.out.println();
    }
    
    /**
     * 印出捷運網絡資訊
     */
    public void printNetworkInfo() {
        System.out.println("\n=== 捷運網絡資訊 ===");
        System.out.printf("總站點數: %d%n", stations.size());
        
        // 統計各路線站點數
        Map<String, Set<String>> lineStations = new HashMap<>();
        for (Station station : stations.values()) {
            for (String line : station.getLines()) {
                lineStations.computeIfAbsent(line, k -> new HashSet<>()).add(station.getName());
            }
        }
        
        System.out.println("\n路線資訊:");
        for (Map.Entry<String, Set<String>> entry : lineStations.entrySet()) {
            System.out.printf("  %s: %d 站%n", entry.getKey(), entry.getValue().size());
        }
        
        // 轉乘站
        System.out.println("\n轉乘站:");
        for (Station station : stations.values()) {
            if (station.isTransferStation()) {
                System.out.printf("  %s: %s%n", station.getName(), station.getLines());
            }
        }
        System.out.println();
    }
    
    /**
     * 清空所有資料
     */
    public void clear() {
        stations.clear();
        adjacencyList.clear();
        lineMap.clear();
        System.out.println("🔄 已清空捷運網絡");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 捷運轉乘路徑系統測試 ===\n");
        
        // 測試 1：基本路線
        testBasicLines();
        
        // 測試 2：轉乘路徑
        testTransferPaths();
        
        // 測試 3：多條路線
        testMultipleLines();
        
        // 測試 4：邊界情況
        testEdgeCases();
        
        // 測試 5：實際應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試基本路線
     */
    private static void testBasicLines() {
        System.out.println("--- 測試 1: 基本路線 ---");
        
        MetroTransferPath metro = new MetroTransferPath();
        
        // 紅線
        metro.addLine("紅線", "台北車站", "中山", "雙連", "民權西路", "圓山", "劍潭");
        
        // 藍線
        metro.addLine("藍線", "台北車站", "西門", "龍山寺", "板橋", "府中");
        
        metro.printNetworkInfo();
        
        // 查詢
        System.out.println("📋 路徑查詢:");
        metro.printPath("台北車站", "劍潭");
        metro.printPath("台北車站", "板橋");
        metro.printPath("中山", "西門");
    }
    
    /**
     * 測試轉乘路徑
     */
    private static void testTransferPaths() {
        System.out.println("\n--- 測試 2: 轉乘路徑 ---");
        
        MetroTransferPath metro = new MetroTransferPath();
        
        // 紅線
        metro.addLine("紅線", "台北車站", "中山", "雙連", "民權西路", "圓山", "劍潭");
        
        // 藍線
        metro.addLine("藍線", "板橋", "府中", "西門", "台北車站", "善導寺", "忠孝新生");
        
        // 綠線
        metro.addLine("綠線", "西門", "小南門", "中正紀念堂", "古亭", "台電大樓");
        
        // 橘線
        metro.addLine("橘線", "忠孝新生", "東門", "古亭", "頂溪", "永安市場");
        
        metro.printNetworkInfo();
        
        // 多組查詢
        metro.queryPaths(
            "台北車站", "劍潭",
            "台北車站", "板橋",
            "台北車站", "古亭",
            "西門", "永安市場",
            "圓山", "古亭",
            "中山", "台電大樓",
            "板橋", "忠孝新生"
        );
    }
    
    /**
     * 測試多條路線
     */
    private static void testMultipleLines() {
        System.out.println("\n--- 測試 3: 多條路線 ---");
        
        MetroTransferPath metro = new MetroTransferPath();
        
        // 淡水信義線 (紅線)
        metro.addLine("淡水信義線", "淡水", "紅樹林", "竹圍", "關渡", "忠義", "復興崗", 
                     "北投", "奇岩", "唭哩岸", "石牌", "明德", "芝山", "士林", "劍潭", 
                     "圓山", "民權西路", "雙連", "中山", "台北車站", "台大醫院", 
                     "中正紀念堂", "東門", "大安森林公園", "大安", "信義安和", 
                     "台北101/世貿", "象山");
        
        // 板南線 (藍線)
        metro.addLine("板南線", "頂埔", "永寧", "土城", "海山", "亞東醫院", "府中", 
                     "板橋", "龍山寺", "西門", "台北車站", "善導寺", "忠孝新生", 
                     "忠孝復興", "忠孝敦化", "國父紀念館", "市政府", "永春", "後山埤", 
                     "昆陽", "南港", "南港展覽館");
        
        // 中和新蘆線 (橘線)
        metro.addLine("中和新蘆線", "蘆洲", "三民高中", "徐匯中學", "三和國中", "三重國小",
                     "大橋頭", "民權西路", "中山國小", "行天宮", "松江南京", "忠孝新生",
                     "東門", "古亭", "頂溪", "永安市場", "景安", "南勢角");
        
        // 松山新店線 (綠線)
        metro.addLine("松山新店線", "松山", "南京三民", "台北小巨蛋", "南京復興", "松江南京",
                     "忠孝新生", "東門", "古亭", "台電大樓", "公館", "萬隆", "景美",
                     "大坪林", "七張", "新店區公所", "新店");
        
        // 文湖線 (棕線)
        metro.addLine("文湖線", "南港展覽館", "南港軟體園區", "東湖", "葫洲", "大湖公園",
                     "內湖", "文德", "港墘", "西湖", "劍南路", "大直", "松山機場",
                     "中山國中", "南京復興", "忠孝復興", "大安", "科技大樓", "六張犁",
                     "麟光", "辛亥", "萬芳醫院", "萬芳社區", "木柵", "動物園");
        
        metro.printNetworkInfo();
        
        // 查詢
        System.out.println("\n📋 跨線路徑查詢:");
        metro.queryPaths(
            "淡水", "象山",
            "淡水", "南港展覽館",
            "淡水", "動物園",
            "台北車站", "松山",
            "忠孝復興", "古亭",
            "南京復興", "西門",
            "南港展覽館", "新店"
        );
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("\n--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空網絡
        System.out.println("測試 4.1: 空網絡");
        MetroTransferPath metro = new MetroTransferPath();
        metro.printPath("A", "B");
        System.out.println();
        
        // 測試 4.2: 單一路線
        System.out.println("測試 4.2: 單一路線");
        metro.addLine("A線", "站1", "站2", "站3", "站4", "站5");
        metro.printPath("站1", "站5");
        metro.printPath("站3", "站1");
        System.out.println();
        
        // 測試 4.3: 相同站點
        System.out.println("測試 4.3: 相同站點");
        metro.printPath("站1", "站1");
        System.out.println();
        
        // 測試 4.4: 不存在的站點
        System.out.println("測試 4.4: 不存在的站點");
        metro.printPath("站1", "不存在");
        metro.printPath("不存在", "站1");
        System.out.println();
        
        // 測試 4.5: 無法到達
        System.out.println("測試 4.5: 無法到達");
        MetroTransferPath metro2 = new MetroTransferPath();
        metro2.addLine("紅線", "A", "B", "C");
        metro2.addLine("藍線", "D", "E", "F");
        metro2.printPath("A", "F");
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("\n--- 測試 5: 實際應用場景 ---");
        System.out.println("🚇 台北捷運路線規劃 (簡化版)");
        
        MetroTransferPath taipeiMetro = new MetroTransferPath();
        
        // 淡水信義線 (紅線) - 簡化版
        taipeiMetro.addLine("淡水信義線", 
            "淡水", "北投", "士林", "劍潭", "民權西路", "台北車站", 
            "中正紀念堂", "東門", "大安", "台北101", "象山");
        
        // 板南線 (藍線) - 簡化版
        taipeiMetro.addLine("板南線",
            "板橋", "西門", "台北車站", "忠孝新生", "忠孝復興", "南港");
        
        // 中和新蘆線 (橘線) - 簡化版
        taipeiMetro.addLine("中和新蘆線",
            "蘆洲", "民權西路", "忠孝新生", "東門", "古亭", "南勢角");
        
        // 松山新店線 (綠線) - 簡化版
        taipeiMetro.addLine("松山新店線",
            "松山", "南京復興", "忠孝新生", "東門", "古亭", "新店");
        
        // 文湖線 (棕線) - 簡化版
        taipeiMetro.addLine("文湖線",
            "南港展覽館", "南京復興", "忠孝復興", "大安", "動物園");
        
        taipeiMetro.printNetworkInfo();
        
        // 實際路徑規劃
        System.out.println("\n📋 台北捷運路徑規劃:");
        taipeiMetro.queryPaths(
            "淡水", "象山",
            "淡水", "南港",
            "淡水", "動物園",
            "板橋", "象山",
            "北投", "動物園",
            "西門", "南港展覽館",
            "台北101", "新店"
        );
        
        // 詳細路徑
        System.out.println("\n📖 詳細路徑範例:");
        System.out.println("1. 從淡水到動物園:");
        taipeiMetro.printPath("淡水", "動物園");
        
        System.out.println("\n2. 從板橋到象山:");
        taipeiMetro.printPath("板橋", "象山");
        
        System.out.println("\n3. 從西門到南港展覽館:");
        taipeiMetro.printPath("西門", "南港展覽館");
    }
}