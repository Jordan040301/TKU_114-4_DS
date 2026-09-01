import java.util.*;

/**
 * 課程相依圖
 * 使用有向圖鄰接表管理課程先決條件關係
 */
public class CourseDependencyGraph {
    
    // 鄰接表：課程 -> 後續課程列表 (出邊)
    private Map<String, Set<String>> adjacencyList;
    
    // 反向鄰接表：課程 -> 先決條件列表 (入邊)
    private Map<String, Set<String>> reverseAdjacencyList;
    
    /**
     * 建構子
     */
    public CourseDependencyGraph() {
        this.adjacencyList = new HashMap<>();
        this.reverseAdjacencyList = new HashMap<>();
    }
    
    /**
     * 新增課程
     * @param courseName 課程名稱
     * @return true 如果成功新增
     */
    public boolean addCourse(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("課程名稱不能為空");
        }
        
        if (adjacencyList.containsKey(courseName)) {
            System.out.printf("⚠️ 課程 '%s' 已存在%n", courseName);
            return false;
        }
        
        adjacencyList.put(courseName, new HashSet<>());
        reverseAdjacencyList.put(courseName, new HashSet<>());
        
        System.out.printf("✅ 新增課程: %s%n", courseName);
        return true;
    }
    
    /**
     * 新增先決條件關係 (course1 是 course2 的先決條件)
     * @param prerequisite 先決條件課程
     * @param course 需要先決條件的課程
     * @return true 如果成功新增
     */
    public boolean addPrerequisite(String prerequisite, String course) {
        if (prerequisite == null || course == null) {
            throw new IllegalArgumentException("課程名稱不能為 null");
        }
        
        if (prerequisite.equals(course)) {
            System.out.println("⚠️ 不能將課程設為自己的先決條件");
            return false;
        }
        
        if (!adjacencyList.containsKey(prerequisite)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", prerequisite);
            return false;
        }
        
        if (!adjacencyList.containsKey(course)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", course);
            return false;
        }
        
        // 檢查是否已存在
        if (adjacencyList.get(prerequisite).contains(course)) {
            System.out.printf("⚠️ 先決條件關係已存在: %s → %s%n", prerequisite, course);
            return false;
        }
        
        // 檢查是否會形成循環相依
        if (wouldCreateCycle(prerequisite, course)) {
            System.out.printf("⚠️ 會形成循環相依: %s → %s%n", prerequisite, course);
            return false;
        }
        
        // 新增關係
        adjacencyList.get(prerequisite).add(course);
        reverseAdjacencyList.get(course).add(prerequisite);
        
        System.out.printf("✅ 新增先決條件: %s → %s%n", prerequisite, course);
        return true;
    }
    
    /**
     * 檢查是否會形成循環相依
     * @param from 起始課程
     * @param to 目標課程
     * @return true 如果會形成循環
     */
    private boolean wouldCreateCycle(String from, String to) {
        // 檢查 to 是否能到達 from (形成循環)
        return canReach(to, from, new HashSet<>());
    }
    
    /**
     * DFS 檢查可達性
     */
    private boolean canReach(String start, String target, Set<String> visited) {
        if (start.equals(target)) {
            return true;
        }
        
        visited.add(start);
        Set<String> nextCourses = adjacencyList.get(start);
        
        if (nextCourses == null) {
            return false;
        }
        
        for (String next : nextCourses) {
            if (!visited.contains(next)) {
                if (canReach(next, target, visited)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 移除先決條件關係
     * @param prerequisite 先決條件課程
     * @param course 課程
     * @return true 如果成功移除
     */
    public boolean removePrerequisite(String prerequisite, String course) {
        if (prerequisite == null || course == null) {
            throw new IllegalArgumentException("課程名稱不能為 null");
        }
        
        if (!adjacencyList.containsKey(prerequisite)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", prerequisite);
            return false;
        }
        
        if (!adjacencyList.containsKey(course)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", course);
            return false;
        }
        
        if (!adjacencyList.get(prerequisite).contains(course)) {
            System.out.printf("⚠️ 先決條件關係不存在: %s → %s%n", prerequisite, course);
            return false;
        }
        
        adjacencyList.get(prerequisite).remove(course);
        reverseAdjacencyList.get(course).remove(prerequisite);
        
        System.out.printf("🗑️ 移除先決條件: %s → %s%n", prerequisite, course);
        return true;
    }
    
    /**
     * 取得課程的先決條件 (入邊)
     * @param courseName 課程名稱
     * @return 先決條件集合
     */
    public Set<String> getPrerequisites(String courseName) {
        if (!adjacencyList.containsKey(courseName)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", courseName);
            return new HashSet<>();
        }
        
        return new HashSet<>(reverseAdjacencyList.get(courseName));
    }
    
    /**
     * 取得課程的後續課程 (出邊)
     * @param courseName 課程名稱
     * @return 後續課程集合
     */
    public Set<String> getSuccessors(String courseName) {
        if (!adjacencyList.containsKey(courseName)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", courseName);
            return new HashSet<>();
        }
        
        return new HashSet<>(adjacencyList.get(courseName));
    }
    
    /**
     * 取得課程的入度 (先決條件數量)
     * @param courseName 課程名稱
     * @return 入度
     */
    public int getInDegree(String courseName) {
        if (!adjacencyList.containsKey(courseName)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", courseName);
            return -1;
        }
        
        return reverseAdjacencyList.get(courseName).size();
    }
    
    /**
     * 取得課程的出度 (後續課程數量)
     * @param courseName 課程名稱
     * @return 出度
     */
    public int getOutDegree(String courseName) {
        if (!adjacencyList.containsKey(courseName)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", courseName);
            return -1;
        }
        
        return adjacencyList.get(courseName).size();
    }
    
    /**
     * 取得所有課程名稱
     * @return 課程名稱集合
     */
    public Set<String> getAllCourses() {
        return new HashSet<>(adjacencyList.keySet());
    }
    
    /**
     * 取得課程總數
     * @return 課程數量
     */
    public int getCourseCount() {
        return adjacencyList.size();
    }
    
    /**
     * 取得先決條件關係總數
     * @return 關係數量
     */
    public int getDependencyCount() {
        int count = 0;
        for (Set<String> successors : adjacencyList.values()) {
            count += successors.size();
        }
        return count;
    }
    
    /**
     * 檢查課程是否存在
     * @param courseName 課程名稱
     * @return true 如果存在
     */
    public boolean containsCourse(String courseName) {
        return adjacencyList.containsKey(courseName);
    }
    
    /**
     * 取得所有先決條件 (沒有先決條件的課程)
     * @return 先決條件課程集合
     */
    public Set<String> getRootCourses() {
        Set<String> roots = new HashSet<>();
        for (String course : adjacencyList.keySet()) {
            if (reverseAdjacencyList.get(course).isEmpty()) {
                roots.add(course);
            }
        }
        return roots;
    }
    
    /**
     * 取得所有終端課程 (沒有後續課程的課程)
     * @return 終端課程集合
     */
    public Set<String> getLeafCourses() {
        Set<String> leaves = new HashSet<>();
        for (String course : adjacencyList.keySet()) {
            if (adjacencyList.get(course).isEmpty()) {
                leaves.add(course);
            }
        }
        return leaves;
    }
    
    /**
     * 取得課程的拓撲順序 (依照先決條件排序)
     * @return 拓撲排序列表
     */
    public List<String> getTopologicalOrder() {
        List<String> result = new ArrayList<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        
        // 計算每個課程的入度
        for (String course : adjacencyList.keySet()) {
            inDegree.put(course, getInDegree(course));
        }
        
        // 找出所有入度為 0 的課程
        for (String course : inDegree.keySet()) {
            if (inDegree.get(course) == 0) {
                queue.offer(course);
            }
        }
        
        // 拓撲排序
        while (!queue.isEmpty()) {
            String course = queue.poll();
            result.add(course);
            
            for (String successor : adjacencyList.get(course)) {
                inDegree.put(successor, inDegree.get(successor) - 1);
                if (inDegree.get(successor) == 0) {
                    queue.offer(successor);
                }
            }
        }
        
        // 檢查是否有循環相依
        if (result.size() != adjacencyList.size()) {
            System.out.println("⚠️ 圖中存在循環相依，無法完成拓撲排序");
            return new ArrayList<>();
        }
        
        return result;
    }
    
    /**
     * 取得課程的完整相依路徑 (從根課程到該課程)
     * @param courseName 課程名稱
     * @return 相依路徑列表
     */
    public List<List<String>> getDependencyPaths(String courseName) {
        if (!adjacencyList.containsKey(courseName)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", courseName);
            return new ArrayList<>();
        }
        
        List<List<String>> paths = new ArrayList<>();
        findPaths(courseName, new ArrayList<>(), paths);
        return paths;
    }
    
    /**
     * DFS 查找所有路徑
     */
    private void findPaths(String current, List<String> path, List<List<String>> paths) {
        Set<String> prerequisites = reverseAdjacencyList.get(current);
        
        if (prerequisites.isEmpty()) {
            // 到達根節點
            List<String> completePath = new ArrayList<>(path);
            Collections.reverse(completePath);
            completePath.add(current);
            paths.add(completePath);
            return;
        }
        
        path.add(current);
        for (String prereq : prerequisites) {
            findPaths(prereq, path, paths);
        }
        path.remove(path.size() - 1);
    }
    
    /**
     * 移除課程及其所有關聯
     * @param courseName 課程名稱
     * @return true 如果成功移除
     */
    public boolean removeCourse(String courseName) {
        if (!adjacencyList.containsKey(courseName)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", courseName);
            return false;
        }
        
        // 從所有後續課程中移除該課程作為先決條件
        for (String successor : adjacencyList.get(courseName)) {
            reverseAdjacencyList.get(successor).remove(courseName);
        }
        
        // 從所有先決條件中移除該課程作為後續課程
        for (String prerequisite : reverseAdjacencyList.get(courseName)) {
            adjacencyList.get(prerequisite).remove(courseName);
        }
        
        adjacencyList.remove(courseName);
        reverseAdjacencyList.remove(courseName);
        
        System.out.printf("🗑️ 刪除課程: %s%n", courseName);
        return true;
    }
    
    /**
     * 印出課程相依圖
     */
    public void printGraph() {
        System.out.println("\n=== 課程相依圖 ===");
        
        if (adjacencyList.isEmpty()) {
            System.out.println("無課程資料");
            return;
        }
        
        List<String> sortedCourses = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(sortedCourses);
        
        System.out.printf("課程總數: %d%n", getCourseCount());
        System.out.printf("先決條件關係總數: %d%n", getDependencyCount());
        
        System.out.println("\n課程相依關係:");
        System.out.printf("%-15s | %-6s | %-6s | %-20s | %-20s%n", 
                         "課程名稱", "入度", "出度", "先決條件", "後續課程");
        System.out.println("-----------------|--------|--------|----------------------|----------------------");
        
        for (String course : sortedCourses) {
            Set<String> prerequisites = reverseAdjacencyList.get(course);
            Set<String> successors = adjacencyList.get(course);
            int inDegree = prerequisites.size();
            int outDegree = successors.size();
            
            System.out.printf("%-15s | %6d | %6d | %-20s | %-20s%n",
                             course, inDegree, outDegree,
                             prerequisites.isEmpty() ? "無" : prerequisites.toString(),
                             successors.isEmpty() ? "無" : successors.toString());
        }
        
        // 顯示根課程和葉課程
        Set<String> roots = getRootCourses();
        Set<String> leaves = getLeafCourses();
        
        System.out.println("\n📌 根課程 (無先決條件): " + (roots.isEmpty() ? "無" : roots.toString()));
        System.out.println("📌 終端課程 (無後續課程): " + (leaves.isEmpty() ? "無" : leaves.toString()));
        
        // 顯示拓撲順序
        List<String> topoOrder = getTopologicalOrder();
        if (!topoOrder.isEmpty()) {
            System.out.println("\n📊 建議修課順序 (拓撲排序):");
            int order = 1;
            for (String course : topoOrder) {
                System.out.printf("  %2d. %s%n", order++, course);
            }
        }
        System.out.println();
    }
    
    /**
     * 印出課程詳細資訊
     */
    public void printCourseDetails(String courseName) {
        System.out.println("\n=== 課程詳細資訊 ===");
        
        if (!adjacencyList.containsKey(courseName)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", courseName);
            return;
        }
        
        Set<String> prerequisites = reverseAdjacencyList.get(courseName);
        Set<String> successors = adjacencyList.get(courseName);
        
        System.out.printf("課程名稱: %s%n", courseName);
        System.out.printf("先決條件數量 (入度): %d%n", prerequisites.size());
        System.out.printf("後續課程數量 (出度): %d%n", successors.size());
        
        System.out.printf("\n先決條件: %s%n", 
                         prerequisites.isEmpty() ? "無" : prerequisites.toString());
        System.out.printf("後續課程: %s%n", 
                         successors.isEmpty() ? "無" : successors.toString());
        
        // 顯示完整路徑
        if (!prerequisites.isEmpty()) {
            System.out.println("\n📚 完整相依路徑:");
            List<List<String>> paths = getDependencyPaths(courseName);
            for (int i = 0; i < paths.size(); i++) {
                System.out.printf("  路徑 %d: %s%n", i + 1, 
                                 String.join(" → ", paths.get(i)));
            }
        }
        System.out.println();
    }
    
    /**
     * 印出統計資訊
     */
    public void printStatistics() {
        System.out.println("\n=== 課程統計資訊 ===");
        System.out.printf("課程總數: %d%n", getCourseCount());
        System.out.printf("先決條件關係總數: %d%n", getDependencyCount());
        
        if (adjacencyList.isEmpty()) {
            System.out.println("無課程資料");
            return;
        }
        
        // 計算度數統計
        int totalInDegree = 0;
        int totalOutDegree = 0;
        int maxInDegree = 0;
        int maxOutDegree = 0;
        String maxInCourse = "";
        String maxOutCourse = "";
        
        for (String course : adjacencyList.keySet()) {
            int inDeg = getInDegree(course);
            int outDeg = getOutDegree(course);
            
            totalInDegree += inDeg;
            totalOutDegree += outDeg;
            
            if (inDeg > maxInDegree) {
                maxInDegree = inDeg;
                maxInCourse = course;
            }
            if (outDeg > maxOutDegree) {
                maxOutDegree = outDeg;
                maxOutCourse = course;
            }
        }
        
        System.out.printf("平均入度: %.2f%n", (double) totalInDegree / getCourseCount());
        System.out.printf("平均出度: %.2f%n", (double) totalOutDegree / getCourseCount());
        System.out.printf("最大入度: %d (%s)%n", maxInDegree, maxInCourse);
        System.out.printf("最大出度: %d (%s)%n", maxOutDegree, maxOutCourse);
        
        Set<String> roots = getRootCourses();
        Set<String> leaves = getLeafCourses();
        System.out.printf("根課程數: %d%n", roots.size());
        System.out.printf("終端課程數: %d%n", leaves.size());
        System.out.println();
    }
    
    /**
     * 清空所有課程
     */
    public void clear() {
        adjacencyList.clear();
        reverseAdjacencyList.clear();
        System.out.println("🔄 已清空所有課程");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 課程相依圖測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：先決條件管理
        testPrerequisiteManagement();
        
        // 測試 3：拓撲排序
        testTopologicalSort();
        
        // 測試 4：循環相依檢測
        testCycleDetection();
        
        // 測試 5：實際應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- 測試 1: 基本功能 ---");
        
        CourseDependencyGraph graph = new CourseDependencyGraph();
        
        // 新增課程
        System.out.println("新增課程:");
        graph.addCourse("程式設計");
        graph.addCourse("資料結構");
        graph.addCourse("演算法");
        graph.addCourse("作業系統");
        graph.addCourse("資料庫系統");
        graph.addCourse("網路概論");
        
        // 新增先決條件關係
        System.out.println("\n新增先決條件:");
        graph.addPrerequisite("程式設計", "資料結構");
        graph.addPrerequisite("程式設計", "演算法");
        graph.addPrerequisite("資料結構", "演算法");
        graph.addPrerequisite("資料結構", "資料庫系統");
        graph.addPrerequisite("程式設計", "作業系統");
        graph.addPrerequisite("網路概論", "作業系統");
        
        graph.printGraph();
        graph.printStatistics();
        
        // 查詢特定課程
        graph.printCourseDetails("演算法");
    }
    
    /**
     * 測試先決條件管理
     */
    private static void testPrerequisiteManagement() {
        System.out.println("--- 測試 2: 先決條件管理 ---");
        
        CourseDependencyGraph graph = new CourseDependencyGraph();
        
        // 建立課程
        graph.addCourse("微積分");
        graph.addCourse("線性代數");
        graph.addCourse("機率論");
        graph.addCourse("統計學");
        graph.addCourse("資料分析");
        
        // 新增關係
        graph.addPrerequisite("微積分", "線性代數");
        graph.addPrerequisite("微積分", "機率論");
        graph.addPrerequisite("線性代數", "資料分析");
        graph.addPrerequisite("機率論", "統計學");
        graph.addPrerequisite("統計學", "資料分析");
        
        graph.printGraph();
        
        // 測試查詢
        System.out.println("\n📋 查詢測試:");
        System.out.println("  getPrerequisites('資料分析'): " + graph.getPrerequisites("資料分析"));
        System.out.println("  getSuccessors('微積分'): " + graph.getSuccessors("微積分"));
        System.out.println("  getInDegree('資料分析'): " + graph.getInDegree("資料分析"));
        System.out.println("  getOutDegree('微積分'): " + graph.getOutDegree("微積分"));
        System.out.println("  getRootCourses(): " + graph.getRootCourses());
        System.out.println("  getLeafCourses(): " + graph.getLeafCourses());
        
        // 移除關係
        System.out.println("\n移除關係:");
        graph.removePrerequisite("線性代數", "資料分析");
        graph.printGraph();
    }
    
    /**
     * 測試拓撲排序
     */
    private static void testTopologicalSort() {
        System.out.println("--- 測試 3: 拓撲排序 ---");
        
        CourseDependencyGraph graph = new CourseDependencyGraph();
        
        // 建立計算機科學課程體系
        String[] courses = {
            "計算機概論", "程式設計", "資料結構", "演算法", 
            "作業系統", "編譯器設計", "資料庫系統", "網路概論",
            "軟體工程", "人工智慧", "機器學習"
        };
        
        for (String course : courses) {
            graph.addCourse(course);
        }
        
        graph.addPrerequisite("計算機概論", "程式設計");
        graph.addPrerequisite("程式設計", "資料結構");
        graph.addPrerequisite("資料結構", "演算法");
        graph.addPrerequisite("演算法", "編譯器設計");
        graph.addPrerequisite("資料結構", "資料庫系統");
        graph.addPrerequisite("演算法", "人工智慧");
        graph.addPrerequisite("人工智慧", "機器學習");
        graph.addPrerequisite("程式設計", "作業系統");
        graph.addPrerequisite("作業系統", "網路概論");
        graph.addPrerequisite("程式設計", "軟體工程");
        
        graph.printGraph();
        
        // 顯示拓撲順序
        List<String> topoOrder = graph.getTopologicalOrder();
        if (!topoOrder.isEmpty()) {
            System.out.println("\n📅 建議修課順序:");
            int semester = 1;
            for (String course : topoOrder) {
                System.out.printf("  學期 %d: %s%n", semester++, course);
            }
        }
    }
    
    /**
     * 測試循環相依檢測
     */
    private static void testCycleDetection() {
        System.out.println("--- 測試 4: 循環相依檢測 ---");
        
        CourseDependencyGraph graph = new CourseDependencyGraph();
        
        graph.addCourse("A");
        graph.addCourse("B");
        graph.addCourse("C");
        graph.addCourse("D");
        
        // 建立正常關係
        graph.addPrerequisite("A", "B");
        graph.addPrerequisite("B", "C");
        graph.addPrerequisite("C", "D");
        
        graph.printGraph();
        
        // 嘗試建立循環相依
        System.out.println("\n嘗試建立循環相依:");
        graph.addPrerequisite("D", "A");
        
        System.out.println("\n嘗試建立循環相依 (B → D):");
        graph.addPrerequisite("B", "D");
        
        System.out.println("\n最終狀態:");
        graph.printGraph();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 5: 實際應用場景 ---");
        System.out.println("🏫 資訊工程學系課程規劃");
        
        CourseDependencyGraph csDept = new CourseDependencyGraph();
        
        // 建立資工系核心課程
        String[] coreCourses = {
            "計算機概論", "程式設計", "資料結構", "演算法",
            "離散數學", "線性代數", "機率論", "統計學",
            "作業系統", "計算機組織", "編譯器設計",
            "資料庫系統", "網路概論", "軟體工程",
            "人工智慧", "機器學習", "資安概論",
            "物件導向程式設計", "網頁程式設計", "行動應用程式開發"
        };
        
        for (String course : coreCourses) {
            csDept.addCourse(course);
        }
        
        // 建立先決條件關係
        String[][] prerequisites = {
            {"計算機概論", "程式設計"},
            {"程式設計", "資料結構"},
            {"程式設計", "物件導向程式設計"},
            {"資料結構", "演算法"},
            {"離散數學", "演算法"},
            {"線性代數", "機率論"},
            {"機率論", "統計學"},
            {"資料結構", "作業系統"},
            {"計算機組織", "作業系統"},
            {"演算法", "編譯器設計"},
            {"資料結構", "資料庫系統"},
            {"程式設計", "網頁程式設計"},
            {"程式設計", "行動應用程式開發"},
            {"演算法", "人工智慧"},
            {"人工智慧", "機器學習"},
            {"程式設計", "軟體工程"},
            {"網路概論", "資安概論"},
            {"作業系統", "網路概論"}
        };
        
        for (String[] prereq : prerequisites) {
            csDept.addPrerequisite(prereq[0], prereq[1]);
        }
        
        csDept.printGraph();
        csDept.printStatistics();
        
        // 分析特定課程
        System.out.println("\n📚 進階課程分析:");
        String[] advancedCourses = {"編譯器設計", "機器學習", "資安概論"};
        for (String course : advancedCourses) {
            csDept.printCourseDetails(course);
        }
        
        // 顯示修課建議
        System.out.println("\n💡 四年修課規劃建議:");
        List<String> topoOrder = csDept.getTopologicalOrder();
        if (!topoOrder.isEmpty()) {
            int semester = 1;
            int year = 1;
            for (int i = 0; i < topoOrder.size(); i++) {
                if (i > 0 && i % 10 == 0) {
                    year++;
                    semester = 1;
                }
                if (semester == 1) {
                    System.out.printf("  第 %d 學年 上學期: ", year);
                } else {
                    System.out.printf("  第 %d 學年 下學期: ", year);
                }
                System.out.println(topoOrder.get(i));
                semester++;
            }
        }
    }
}