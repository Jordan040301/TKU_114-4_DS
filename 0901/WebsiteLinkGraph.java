import java.util.*;

/**
 * 網站連結圖
 * 使用有向鄰接表管理網頁連結關係
 */
public class WebsiteLinkGraph {
    
    // 鄰接表：頁面 -> 傳出連結列表 (出邊)
    private Map<String, Set<String>> outgoingLinks;
    
    // 反向鄰接表：頁面 -> 傳入連結列表 (入邊)
    private Map<String, Set<String>> incomingLinks;
    
    /**
     * 建構子
     */
    public WebsiteLinkGraph() {
        this.outgoingLinks = new HashMap<>();
        this.incomingLinks = new HashMap<>();
    }
    
    /**
     * 新增網頁
     * @param pageUrl 網頁 URL
     * @return true 如果成功新增
     */
    public boolean addPage(String pageUrl) {
        if (pageUrl == null || pageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("頁面 URL 不能為空");
        }
        
        String normalized = pageUrl.trim();
        
        if (outgoingLinks.containsKey(normalized)) {
            System.out.printf("⚠️ 頁面 '%s' 已存在%n", normalized);
            return false;
        }
        
        outgoingLinks.put(normalized, new HashSet<>());
        incomingLinks.put(normalized, new HashSet<>());
        
        System.out.printf("✅ 新增頁面: %s%n", normalized);
        return true;
    }
    
    /**
     * 新增連結 (從 fromPage 指向 toPage)
     * @param fromPage 來源頁面
     * @param toPage 目標頁面
     * @return true 如果成功新增
     */
    public boolean addLink(String fromPage, String toPage) {
        if (fromPage == null || toPage == null) {
            throw new IllegalArgumentException("頁面 URL 不能為 null");
        }
        
        String from = fromPage.trim();
        String to = toPage.trim();
        
        if (from.equals(to)) {
            System.out.println("⚠️ 不能新增自連結 (指向自己)");
            return false;
        }
        
        if (!outgoingLinks.containsKey(from)) {
            System.out.printf("⚠️ 來源頁面 '%s' 不存在%n", from);
            return false;
        }
        
        if (!outgoingLinks.containsKey(to)) {
            System.out.printf("⚠️ 目標頁面 '%s' 不存在%n", to);
            return false;
        }
        
        // 檢查是否已存在連結
        if (outgoingLinks.get(from).contains(to)) {
            System.out.printf("⚠️ 連結已存在: %s → %s%n", from, to);
            return false;
        }
        
        // 新增連結
        outgoingLinks.get(from).add(to);
        incomingLinks.get(to).add(from);
        
        System.out.printf("✅ 新增連結: %s → %s%n", from, to);
        return true;
    }
    
    /**
     * 批次新增連結
     * @param links 連結陣列 [from, to, from, to, ...]
     */
    public void addLinks(String... links) {
        if (links.length % 2 != 0) {
            throw new IllegalArgumentException("參數必須為成對的 (來源, 目標)");
        }
        
        for (int i = 0; i < links.length; i += 2) {
            addLink(links[i], links[i + 1]);
        }
    }
    
    /**
     * 刪除連結
     * @param fromPage 來源頁面
     * @param toPage 目標頁面
     * @return true 如果成功刪除
     */
    public boolean removeLink(String fromPage, String toPage) {
        if (fromPage == null || toPage == null) {
            throw new IllegalArgumentException("頁面 URL 不能為 null");
        }
        
        String from = fromPage.trim();
        String to = toPage.trim();
        
        if (!outgoingLinks.containsKey(from)) {
            System.out.printf("⚠️ 來源頁面 '%s' 不存在%n", from);
            return false;
        }
        
        if (!outgoingLinks.containsKey(to)) {
            System.out.printf("⚠️ 目標頁面 '%s' 不存在%n", to);
            return false;
        }
        
        if (!outgoingLinks.get(from).contains(to)) {
            System.out.printf("⚠️ 連結不存在: %s → %s%n", from, to);
            return false;
        }
        
        outgoingLinks.get(from).remove(to);
        incomingLinks.get(to).remove(from);
        
        System.out.printf("🗑️ 刪除連結: %s → %s%n", from, to);
        return true;
    }
    
    /**
     * 刪除頁面及其所有連結
     * @param pageUrl 頁面 URL
     * @return true 如果成功刪除
     */
    public boolean removePage(String pageUrl) {
        if (pageUrl == null || pageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("頁面 URL 不能為空");
        }
        
        String page = pageUrl.trim();
        
        if (!outgoingLinks.containsKey(page)) {
            System.out.printf("⚠️ 頁面 '%s' 不存在%n", page);
            return false;
        }
        
        // 從所有頁面的傳出連結中移除該頁面
        for (String from : outgoingLinks.keySet()) {
            if (outgoingLinks.get(from).contains(page)) {
                outgoingLinks.get(from).remove(page);
                incomingLinks.get(page).remove(from);
            }
        }
        
        // 從所有頁面的傳入連結中移除該頁面
        for (String to : incomingLinks.keySet()) {
            if (incomingLinks.get(to).contains(page)) {
                incomingLinks.get(to).remove(page);
                outgoingLinks.get(page).remove(to);
            }
        }
        
        outgoingLinks.remove(page);
        incomingLinks.remove(page);
        
        System.out.printf("🗑️ 刪除頁面: %s%n", page);
        return true;
    }
    
    /**
     * 取得頁面的傳出連結
     * @param pageUrl 頁面 URL
     * @return 傳出連結集合
     */
    public Set<String> getOutgoingLinks(String pageUrl) {
        if (pageUrl == null || pageUrl.trim().isEmpty()) {
            return new HashSet<>();
        }
        
        String page = pageUrl.trim();
        return outgoingLinks.getOrDefault(page, new HashSet<>());
    }
    
    /**
     * 取得頁面的傳入計數 (被多少頁面連結)
     * @param pageUrl 頁面 URL
     * @return 傳入計數
     */
    public int getIncomingCount(String pageUrl) {
        if (pageUrl == null || pageUrl.trim().isEmpty()) {
            return 0;
        }
        
        String page = pageUrl.trim();
        Set<String> incoming = incomingLinks.get(page);
        return incoming == null ? 0 : incoming.size();
    }
    
    /**
     * 取得頁面的傳入連結 (哪些頁面連結到此頁面)
     * @param pageUrl 頁面 URL
     * @return 傳入連結集合
     */
    public Set<String> getIncomingLinks(String pageUrl) {
        if (pageUrl == null || pageUrl.trim().isEmpty()) {
            return new HashSet<>();
        }
        
        String page = pageUrl.trim();
        return incomingLinks.getOrDefault(page, new HashSet<>());
    }
    
    /**
     * 取得頁面的傳出計數 (連結到多少頁面)
     * @param pageUrl 頁面 URL
     * @return 傳出計數
     */
    public int getOutgoingCount(String pageUrl) {
        if (pageUrl == null || pageUrl.trim().isEmpty()) {
            return 0;
        }
        
        String page = pageUrl.trim();
        Set<String> outgoing = outgoingLinks.get(page);
        return outgoing == null ? 0 : outgoing.size();
    }
    
    /**
     * 取得所有頁面
     * @return 頁面集合
     */
    public Set<String> getAllPages() {
        return new HashSet<>(outgoingLinks.keySet());
    }
    
    /**
     * 取得頁面總數
     * @return 頁面數量
     */
    public int getPageCount() {
        return outgoingLinks.size();
    }
    
    /**
     * 取得連結總數
     * @return 連結數量
     */
    public int getLinkCount() {
        int count = 0;
        for (Set<String> links : outgoingLinks.values()) {
            count += links.size();
        }
        return count;
    }
    
    /**
     * 取得無傳入頁面 (沒有被任何頁面連結)
     * @return 無傳入頁面集合
     */
    public Set<String> getPagesWithNoIncoming() {
        Set<String> noIncoming = new HashSet<>();
        for (String page : outgoingLinks.keySet()) {
            if (incomingLinks.get(page).isEmpty()) {
                noIncoming.add(page);
            }
        }
        return noIncoming;
    }
    
    /**
     * 取得無傳出頁面 (沒有連結到任何頁面)
     * @return 無傳出頁面集合
     */
    public Set<String> getPagesWithNoOutgoing() {
        Set<String> noOutgoing = new HashSet<>();
        for (String page : outgoingLinks.keySet()) {
            if (outgoingLinks.get(page).isEmpty()) {
                noOutgoing.add(page);
            }
        }
        return noOutgoing;
    }
    
    /**
     * 檢查頁面是否存在
     * @param pageUrl 頁面 URL
     * @return true 如果存在
     */
    public boolean containsPage(String pageUrl) {
        if (pageUrl == null || pageUrl.trim().isEmpty()) {
            return false;
        }
        return outgoingLinks.containsKey(pageUrl.trim());
    }
    
    /**
     * 檢查連結是否存在
     * @param fromPage 來源頁面
     * @param toPage 目標頁面
     * @return true 如果存在
     */
    public boolean hasLink(String fromPage, String toPage) {
        if (fromPage == null || toPage == null) {
            return false;
        }
        
        String from = fromPage.trim();
        String to = toPage.trim();
        
        if (!outgoingLinks.containsKey(from) || !outgoingLinks.containsKey(to)) {
            return false;
        }
        
        return outgoingLinks.get(from).contains(to);
    }
    
    /**
     * 取得頁面的網頁排名分數 (基於傳入連結數量)
     * @param pageUrl 頁面 URL
     * @return 排名分數
     */
    public int getPageRank(String pageUrl) {
        return getIncomingCount(pageUrl);
    }
    
    /**
     * 取得所有頁面的網頁排名
     * @return 排名對應表
     */
    public Map<String, Integer> getAllPageRanks() {
        Map<String, Integer> ranks = new LinkedHashMap<>();
        for (String page : outgoingLinks.keySet()) {
            ranks.put(page, getPageRank(page));
        }
        
        // 依分數排序
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(ranks.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        Map<String, Integer> sortedRanks = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : sorted) {
            sortedRanks.put(entry.getKey(), entry.getValue());
        }
        
        return sortedRanks;
    }
    
    /**
     * 印出完整報告
     */
    public void printFullReport() {
        System.out.println("\n=== 網站連結圖報告 ===");
        
        if (outgoingLinks.isEmpty()) {
            System.out.println("無頁面資料");
            return;
        }
        
        System.out.printf("頁面總數: %d%n", getPageCount());
        System.out.printf("連結總數: %d%n", getLinkCount());
        System.out.printf("平均傳出連結數: %.2f%n", 
                         (double) getLinkCount() / getPageCount());
        System.out.println();
        
        // 頁面詳細資訊
        System.out.println("頁面詳細資訊:");
        System.out.printf("%-30s | %-6s | %-6s | %-10s%n", 
                         "頁面", "傳出", "傳入", "連結");
        System.out.println("------------------------------|--------|--------|------------");
        
        List<String> sortedPages = new ArrayList<>(outgoingLinks.keySet());
        Collections.sort(sortedPages);
        
        for (String page : sortedPages) {
            int outCount = getOutgoingCount(page);
            int inCount = getIncomingCount(page);
            Set<String> outgoing = outgoingLinks.get(page);
            
            System.out.printf("%-30s | %6d | %6d | %s%n",
                             page, outCount, inCount,
                             outgoing.isEmpty() ? "無" : outgoing.toString());
        }
        
        // 無傳入頁面
        System.out.println("\n📌 無傳入頁面 (沒有被任何頁面連結):");
        Set<String> noIncoming = getPagesWithNoIncoming();
        if (noIncoming.isEmpty()) {
            System.out.println("  無");
        } else {
            for (String page : noIncoming) {
                System.out.println("  " + page);
            }
        }
        
        // 無傳出頁面
        System.out.println("\n📌 無傳出頁面 (沒有連結到任何頁面):");
        Set<String> noOutgoing = getPagesWithNoOutgoing();
        if (noOutgoing.isEmpty()) {
            System.out.println("  無");
        } else {
            for (String page : noOutgoing) {
                System.out.println("  " + page);
            }
        }
        
        // 網頁排名
        System.out.println("\n🏆 網頁排名 (依傳入連結數):");
        Map<String, Integer> ranks = getAllPageRanks();
        int rank = 1;
        System.out.println("  排名 | 頁面 | 傳入連結數");
        System.out.println("  -----|------|------------");
        for (Map.Entry<String, Integer> entry : ranks.entrySet()) {
            if (rank > 20) break;
            System.out.printf("  %4d | %-4s | %6d%n", 
                             rank++, entry.getKey(), entry.getValue());
        }
        System.out.println();
    }
    
    /**
     * 印出鄰接表
     */
    public void printAdjacencyList() {
        System.out.println("\n=== 鄰接表 (傳出連結) ===");
        
        if (outgoingLinks.isEmpty()) {
            System.out.println("無頁面資料");
            return;
        }
        
        List<String> sortedPages = new ArrayList<>(outgoingLinks.keySet());
        Collections.sort(sortedPages);
        
        for (String page : sortedPages) {
            Set<String> outgoing = outgoingLinks.get(page);
            System.out.printf("%s → %s%n", 
                             page, outgoing.isEmpty() ? "無" : outgoing.toString());
        }
        System.out.println();
    }
    
    /**
     * 印出反向鄰接表 (傳入連結)
     */
    public void printReverseAdjacencyList() {
        System.out.println("\n=== 反向鄰接表 (傳入連結) ===");
        
        if (incomingLinks.isEmpty()) {
            System.out.println("無頁面資料");
            return;
        }
        
        List<String> sortedPages = new ArrayList<>(incomingLinks.keySet());
        Collections.sort(sortedPages);
        
        for (String page : sortedPages) {
            Set<String> incoming = incomingLinks.get(page);
            System.out.printf("%s ← %s%n", 
                             page, incoming.isEmpty() ? "無" : incoming.toString());
        }
        System.out.println();
    }
    
    /**
     * 尋找從起點到終點的路徑 (BFS)
     * @param start 起點頁面
     * @param end 終點頁面
     * @return 路徑列表
     */
    public List<String> findPath(String start, String end) {
        if (start == null || end == null) {
            return new ArrayList<>();
        }
        
        String s = start.trim();
        String e = end.trim();
        
        if (!outgoingLinks.containsKey(s) || !outgoingLinks.containsKey(e)) {
            return new ArrayList<>();
        }
        
        if (s.equals(e)) {
            return Arrays.asList(s);
        }
        
        // BFS
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(s);
        visited.add(s);
        parent.put(s, null);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (current.equals(e)) {
                break;
            }
            
            for (String next : outgoingLinks.get(current)) {
                if (!visited.contains(next)) {
                    visited.add(next);
                    parent.put(next, current);
                    queue.offer(next);
                }
            }
        }
        
        if (!visited.contains(e)) {
            return new ArrayList<>();
        }
        
        // 重建路徑
        List<String> path = new ArrayList<>();
        String current = e;
        while (current != null) {
            path.add(0, current);
            current = parent.get(current);
        }
        
        return path;
    }
    
    /**
     * 清空所有資料
     */
    public void clear() {
        outgoingLinks.clear();
        incomingLinks.clear();
        System.out.println("🔄 已清空所有頁面資料");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 網站連結圖系統測試 ===\n");
        
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
        
        WebsiteLinkGraph graph = new WebsiteLinkGraph();
        
        // 新增頁面
        System.out.println("新增頁面:");
        graph.addPage("index.html");
        graph.addPage("about.html");
        graph.addPage("contact.html");
        graph.addPage("products.html");
        graph.addPage("services.html");
        graph.addPage("blog.html");
        
        // 新增連結
        System.out.println("\n新增連結:");
        graph.addLink("index.html", "about.html");
        graph.addLink("index.html", "contact.html");
        graph.addLink("index.html", "products.html");
        graph.addLink("about.html", "index.html");
        graph.addLink("about.html", "services.html");
        graph.addLink("products.html", "services.html");
        graph.addLink("products.html", "contact.html");
        graph.addLink("services.html", "blog.html");
        graph.addLink("blog.html", "index.html");
        graph.addLink("blog.html", "products.html");
        
        graph.printAdjacencyList();
        graph.printReverseAdjacencyList();
        graph.printFullReport();
    }
    
    /**
     * 測試進階功能
     */
    private static void testAdvancedFunctionality() {
        System.out.println("--- 測試 2: 進階功能 ---");
        
        WebsiteLinkGraph graph = new WebsiteLinkGraph();
        
        // 建立測試網絡
        String[] pages = {"A", "B", "C", "D", "E", "F"};
        for (String page : pages) {
            graph.addPage(page);
        }
        
        graph.addLinks(
            "A", "B", "A", "C", "A", "D",
            "B", "C", "B", "E",
            "C", "E", "C", "F",
            "D", "A", "D", "F",
            "E", "F",
            "F", "B", "F", "D"
        );
        
        graph.printFullReport();
        
        // 測試查詢
        System.out.println("\n📋 查詢測試:");
        System.out.println("  getOutgoingLinks('A'): " + graph.getOutgoingLinks("A"));
        System.out.println("  getIncomingLinks('A'): " + graph.getIncomingLinks("A"));
        System.out.println("  getIncomingCount('F'): " + graph.getIncomingCount("F"));
        System.out.println("  getOutgoingCount('E'): " + graph.getOutgoingCount("E"));
        System.out.println("  getPagesWithNoIncoming(): " + graph.getPagesWithNoIncoming());
        System.out.println("  getPagesWithNoOutgoing(): " + graph.getPagesWithNoOutgoing());
        System.out.println();
    }
    
    /**
     * 測試路徑搜尋
     */
    private static void testPathFinding() {
        System.out.println("--- 測試 3: 路徑搜尋 ---");
        
        WebsiteLinkGraph graph = new WebsiteLinkGraph();
        
        // 建立網絡
        String[] pages = {"首頁", "產品", "服務", "關於", "聯絡", "部落格", "下載", "問答"};
        for (String page : pages) {
            graph.addPage(page);
        }
        
        graph.addLinks(
            "首頁", "產品", "首頁", "服務", "首頁", "關於",
            "產品", "服務", "產品", "下載",
            "服務", "聯絡", "服務", "部落格",
            "關於", "首頁", "關於", "問答",
            "部落格", "首頁", "部落格", "服務",
            "下載", "產品", "下載", "問答",
            "問答", "首頁"
        );
        
        graph.printAdjacencyList();
        
        // 路徑搜尋測試
        System.out.println("\n🗺️ 路徑搜尋:");
        String[][] testPaths = {
            {"首頁", "下載"},
            {"首頁", "問答"},
            {"產品", "聯絡"},
            {"關於", "服務"},
            {"聯絡", "首頁"}
        };
        
        for (String[] path : testPaths) {
            List<String> result = graph.findPath(path[0], path[1]);
            if (result.isEmpty()) {
                System.out.printf("  %s → %s: 無路徑%n", path[0], path[1]);
            } else {
                System.out.printf("  %s → %s: %s (共 %d 步)%n", 
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
        WebsiteLinkGraph graph = new WebsiteLinkGraph();
        graph.printFullReport();
        graph.printAdjacencyList();
        System.out.println("  getPageCount: " + graph.getPageCount());
        System.out.println("  getLinkCount: " + graph.getLinkCount());
        System.out.println();
        
        // 測試 4.2: 單一頁面
        System.out.println("測試 4.2: 單一頁面");
        graph.addPage("single.html");
        graph.printFullReport();
        System.out.println("  getOutgoingCount('single.html'): " + graph.getOutgoingCount("single.html"));
        System.out.println("  getIncomingCount('single.html'): " + graph.getIncomingCount("single.html"));
        System.out.println("  getPagesWithNoIncoming(): " + graph.getPagesWithNoIncoming());
        System.out.println("  getPagesWithNoOutgoing(): " + graph.getPagesWithNoOutgoing());
        System.out.println();
        
        // 測試 4.3: 自連結
        System.out.println("測試 4.3: 自連結");
        graph.addLink("single.html", "single.html");
        System.out.println();
        
        // 測試 4.4: 不存在的頁面
        System.out.println("測試 4.4: 不存在的頁面");
        graph.addLink("不存在", "single.html");
        graph.removeLink("不存在", "single.html");
        graph.removePage("不存在");
        System.out.println();
        
        // 測試 4.5: 大量頁面
        System.out.println("測試 4.5: 大量頁面");
        WebsiteLinkGraph graph2 = new WebsiteLinkGraph();
        for (int i = 0; i < 50; i++) {
            graph2.addPage("page" + i + ".html");
        }
        for (int i = 0; i < 49; i++) {
            graph2.addLink("page" + i + ".html", "page" + (i + 1) + ".html");
        }
        System.out.println("  頁面總數: " + graph2.getPageCount());
        System.out.println("  連結總數: " + graph2.getLinkCount());
        System.out.println("  無傳入頁面: " + graph2.getPagesWithNoIncoming());
        System.out.println("  無傳出頁面: " + graph2.getPagesWithNoOutgoing());
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 5: 實際應用場景 ---");
        System.out.println("🌐 網站 SEO 分析系統");
        
        WebsiteLinkGraph seo = new WebsiteLinkGraph();
        
        // 建立網站結構
        System.out.println("\n📁 建立網站結構:");
        String[] pages = {
            "/", "/about", "/products", "/services", "/blog",
            "/contact", "/faq", "/privacy", "/terms", "/sitemap",
            "/products/category1", "/products/category2", "/products/item1", "/products/item2",
            "/blog/post1", "/blog/post2", "/blog/post3", "/blog/category1",
            "/services/consulting", "/services/support", "/services/training"
        };
        
        for (String page : pages) {
            seo.addPage(page);
        }
        
        // 建立連結結構
        System.out.println("建立內部連結:");
        seo.addLink("/", "/about");
        seo.addLink("/", "/products");
        seo.addLink("/", "/services");
        seo.addLink("/", "/blog");
        seo.addLink("/", "/contact");
        seo.addLink("/", "/faq");
        seo.addLink("/", "/sitemap");
        
        seo.addLink("/about", "/privacy");
        seo.addLink("/about", "/terms");
        seo.addLink("/about", "/contact");
        
        seo.addLink("/products", "/products/category1");
        seo.addLink("/products", "/products/category2");
        seo.addLink("/products/category1", "/products/item1");
        seo.addLink("/products/category2", "/products/item2");
        seo.addLink("/products/item1", "/products/item2");
        
        seo.addLink("/services", "/services/consulting");
        seo.addLink("/services", "/services/support");
        seo.addLink("/services", "/services/training");
        seo.addLink("/services/consulting", "/contact");
        seo.addLink("/services/support", "/contact");
        seo.addLink("/services/training", "/contact");
        
        seo.addLink("/blog", "/blog/category1");
        seo.addLink("/blog", "/blog/post1");
        seo.addLink("/blog", "/blog/post2");
        seo.addLink("/blog", "/blog/post3");
        seo.addLink("/blog/category1", "/blog/post1");
        seo.addLink("/blog/category1", "/blog/post2");
        seo.addLink("/blog/category1", "/blog/post3");
        seo.addLink("/blog/post1", "/blog/post2");
        seo.addLink("/blog/post2", "/blog/post3");
        
        seo.addLink("/faq", "/contact");
        seo.addLink("/privacy", "/terms");
        seo.addLink("/terms", "/privacy");
        
        // 生成 SEO 報告
        seo.printFullReport();
        
        // SEO 分析
        System.out.println("\n🔍 SEO 分析:");
        
        // 1. 頁面排名 (PageRank 簡化版)
        System.out.println("  最高權重頁面:");
        Map<String, Integer> ranks = seo.getAllPageRanks();
        int rank = 1;
        for (Map.Entry<String, Integer> entry : ranks.entrySet()) {
            if (rank > 5) break;
            System.out.printf("    #%d: %s (傳入連結: %d)%n", 
                             rank++, entry.getKey(), entry.getValue());
        }
        
        // 2. 孤立頁面
        Set<String> noIncoming = seo.getPagesWithNoIncoming();
        Set<String> noOutgoing = seo.getPagesWithNoOutgoing();
        
        System.out.println("\n  孤立頁面分析:");
        System.out.println("    無傳入連結: " + (noIncoming.isEmpty() ? "無" : noIncoming.toString()));
        System.out.println("    無傳出連結: " + (noOutgoing.isEmpty() ? "無" : noOutgoing.toString()));
        
        // 3. 頁面深度 (最長路徑)
        System.out.println("\n  頁面深度分析:");
        List<String> pagesList = new ArrayList<>(seo.getAllPages());
        int maxDepth = 0;
        String deepestPage = "";
        
        for (String page : pagesList) {
            List<String> path = seo.findPath("/", page);
            if (!path.isEmpty() && path.size() > maxDepth) {
                maxDepth = path.size();
                deepestPage = page;
            }
        }
        System.out.printf("    最深頁面: %s (深度: %d)%n", deepestPage, maxDepth - 1);
        System.out.printf("    平均深度: %.2f%n", 
                         (double) seo.getAllPageRanks().size() / seo.getPageCount());
        
        // 4. 連結分布
        System.out.println("\n  連結分布:");
        int totalOutgoing = seo.getLinkCount();
        int totalPages = seo.getPageCount();
        System.out.printf("    總連結數: %d%n", totalOutgoing);
        System.out.printf("    平均傳出連結: %.2f%n", (double) totalOutgoing / totalPages);
        
        // 5. 建議
        System.out.println("\n💡 SEO 建議:");
        if (!noIncoming.isEmpty()) {
            System.out.printf("  ⚠️  %d 個頁面沒有傳入連結，建議新增內部連結%n", noIncoming.size());
        }
        if (!noOutgoing.isEmpty()) {
            System.out.printf("  ⚠️  %d 個頁面沒有傳出連結，建議新增內部連結%n", noOutgoing.size());
        }
        if (seo.getLinkCount() < seo.getPageCount() * 0.5) {
            System.out.println("  ⚠️  連結密度偏低，建議增加內部連結");
        }
        System.out.println("  ✅ 建議建立完整的網站地圖 (sitemap)");
        System.out.println("  ✅ 建議確保所有頁面可從首頁到達");
        System.out.println();
        
        // 路徑測試
        System.out.println("🗺️ 使用者路徑範例:");
        String[][] userPaths = {
            {"/", "/products/item2"},
            {"/", "/blog/post3"},
            {"/", "/services/training"}
        };
        
        for (String[] path : userPaths) {
            List<String> result = seo.findPath(path[0], path[1]);
            if (!result.isEmpty()) {
                System.out.printf("  %s → %s: %s (點擊 %d 次)%n", 
                                 path[0], path[1], 
                                 String.join(" → ", result), 
                                 result.size() - 1);
            }
        }
    }
}