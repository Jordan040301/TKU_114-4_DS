import java.util.*;

/**
 * 課程規劃圖
 * 有向圖保存先修關係，DFS 判斷可達並輸出所有修課路徑
 */
public class CoursePlanningGraph {
    
    /**
     * 課程節點類別
     */
    public static class Course {
        private final String id;
        private final String name;
        private final int credits;
        private final String department;
        
        public Course(String id, String name, int credits, String department) {
            this.id = id;
            this.name = name;
            this.credits = credits;
            this.department = department;
        }
        
        public String getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
        
        public int getCredits() {
            return credits;
        }
        
        public String getDepartment() {
            return department;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%s, %d 學分)", id, name, credits);
        }
        
        public String toShortString() {
            return id;
        }
    }
    
    /**
     * 路徑結果類別
     */
    public static class PathResult {
        private final List<Course> path;
        private final int totalCredits;
        private final List<String> prerequisites;
        
        public PathResult(List<Course> path) {
            this.path = new ArrayList<>(path);
            int credits = 0;
            for (Course c : path) {
                credits += c.getCredits();
            }
            this.totalCredits = credits;
            this.prerequisites = new ArrayList<>();
        }
        
        public PathResult(List<Course> path, List<String> prerequisites) {
            this.path = new ArrayList<>(path);
            int credits = 0;
            for (Course c : path) {
                credits += c.getCredits();
            }
            this.totalCredits = credits;
            this.prerequisites = prerequisites;
        }
        
        public List<Course> getPath() {
            return path;
        }
        
        public int getTotalCredits() {
            return totalCredits;
        }
        
        public List<String> getPrerequisites() {
            return prerequisites;
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
            List<String> names = new ArrayList<>();
            for (Course c : path) {
                names.add(c.getId());
            }
            sb.append("修課路徑: ").append(String.join(" → ", names));
            sb.append("\n總學分: ").append(totalCredits);
            if (!prerequisites.isEmpty()) {
                sb.append("\n先修課程: ").append(String.join(", ", prerequisites));
            }
            return sb.toString();
        }
    }
    
    // 鄰接表：課程 → 後續課程 (出邊)
    private Map<String, Set<String>> adjacencyList;
    
    // 反向鄰接表：課程 → 先修課程 (入邊)
    private Map<String, Set<String>> reverseAdjacencyList;
    
    // 課程儲存
    private Map<String, Course> courseMap;
    
    // 所有課程
    private Set<String> allCourses;
    
    /**
     * 建構子
     */
    public CoursePlanningGraph() {
        this.adjacencyList = new HashMap<>();
        this.reverseAdjacencyList = new HashMap<>();
        this.courseMap = new HashMap<>();
        this.allCourses = new HashSet<>();
    }
    
    /**
     * 新增課程
     * @param id 課程代碼
     * @param name 課程名稱
     * @param credits 學分數
     * @param department 系所
     */
    public void addCourse(String id, String name, int credits, String department) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("課程代碼不能為空");
        }
        
        String normalizedId = id.trim();
        
        if (courseMap.containsKey(normalizedId)) {
            System.out.printf("⚠️ 課程 '%s' 已存在%n", normalizedId);
            return;
        }
        
        Course course = new Course(normalizedId, name, credits, department);
        courseMap.put(normalizedId, course);
        allCourses.add(normalizedId);
        adjacencyList.put(normalizedId, new HashSet<>());
        reverseAdjacencyList.put(normalizedId, new HashSet<>());
        
        System.out.printf("✅ 新增課程: %s (%s, %d 學分)%n", normalizedId, name, credits);
    }
    
    /**
     * 新增先修關係 (course1 是 course2 的先修課程)
     * @param prerequisite 先修課程
     * @param course 需要先修的課程
     * @return true 如果成功新增
     */
    public boolean addPrerequisite(String prerequisite, String course) {
        if (prerequisite == null || course == null) {
            throw new IllegalArgumentException("課程代碼不能為 null");
        }
        
        String prereq = prerequisite.trim();
        String courseId = course.trim();
        
        if (!courseMap.containsKey(prereq)) {
            System.out.printf("⚠️ 先修課程 '%s' 不存在%n", prereq);
            return false;
        }
        
        if (!courseMap.containsKey(courseId)) {
            System.out.printf("⚠️ 課程 '%s' 不存在%n", courseId);
            return false;
        }
        
        if (prereq.equals(courseId)) {
            System.out.println("⚠️ 不能將課程設為自己的先修");
            return false;
        }
        
        // 檢查是否會形成循環
        if (wouldCreateCycle(prereq, courseId)) {
            System.out.printf("⚠️ 會形成循環依賴: %s → %s%n", prereq, courseId);
            return false;
        }
        
        adjacencyList.get(prereq).add(courseId);
        reverseAdjacencyList.get(courseId).add(prereq);
        
        System.out.printf("✅ 新增先修關係: %s → %s%n", prereq, courseId);
        return true;
    }
    
    /**
     * 檢查是否會形成循環 (DFS)
     */
    private boolean wouldCreateCycle(String from, String to) {
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
        for (String neighbor : adjacencyList.getOrDefault(start, new HashSet<>())) {
            if (!visited.contains(neighbor)) {
                if (canReach(neighbor, target, visited)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * DFS 判斷從起點到終點是否可達
     * @param start 起點課程
     * @param end 終點課程
     * @return true 如果可達
     */
    public boolean isReachable(String start, String end) {
        if (start == null || end == null) {
            return false;
        }
        
        String s = start.trim();
        String e = end.trim();
        
        if (!courseMap.containsKey(s) || !courseMap.containsKey(e)) {
            return false;
        }
        
        if (s.equals(e)) {
            return true;
        }
        
        return canReach(s, e, new HashSet<>());
    }
    
    /**
     * 找出一條從起點到終點的路徑 (DFS)
     * @param start 起點課程
     * @param end 終點課程
     * @return 路徑結果
     */
    public PathResult findPath(String start, String end) {
        if (start == null || end == null) {
            return new PathResult(new ArrayList<>());
        }
        
        String s = start.trim();
        String e = end.trim();
        
        if (!courseMap.containsKey(s) || !courseMap.containsKey(e)) {
            System.out.printf("⚠️ 課程不存在: %s 或 %s%n", s, e);
            return new PathResult(new ArrayList<>());
        }
        
        if (s.equals(e)) {
            List<Course> path = Arrays.asList(courseMap.get(s));
            return new PathResult(path);
        }
        
        List<Course> path = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        
        if (dfsFindPath(s, e, visited, path)) {
            return new PathResult(path);
        }
        
        return new PathResult(new ArrayList<>());
    }
    
    /**
     * DFS 尋找路徑
     */
    private boolean dfsFindPath(String current, String target, 
                                Set<String> visited, List<Course> path) {
        if (current.equals(target)) {
            path.add(courseMap.get(current));
            return true;
        }
        
        visited.add(current);
        path.add(courseMap.get(current));
        
        for (String neighbor : adjacencyList.getOrDefault(current, new HashSet<>())) {
            if (!visited.contains(neighbor)) {
                if (dfsFindPath(neighbor, target, visited, path)) {
                    return true;
                }
            }
        }
        
        path.remove(path.size() - 1);
        return false;
    }
    
    /**
     * 找出所有從起點到終點的路徑 (DFS 回溯)
     * @param start 起點課程
     * @param end 終點課程
     * @return 所有路徑列表
     */
    public List<PathResult> findAllPaths(String start, String end) {
        if (start == null || end == null) {
            return new ArrayList<>();
        }
        
        String s = start.trim();
        String e = end.trim();
        
        if (!courseMap.containsKey(s) || !courseMap.containsKey(e)) {
            System.out.printf("⚠️ 課程不存在: %s 或 %s%n", s, e);
            return new ArrayList<>();
        }
        
        List<PathResult> allPaths = new ArrayList<>();
        List<Course> currentPath = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        
        dfsFindAllPaths(s, e, visited, currentPath, allPaths);
        return allPaths;
    }
    
    /**
     * DFS 尋找所有路徑 (回溯法)
     */
    private void dfsFindAllPaths(String current, String target,
                                 Set<String> visited, List<Course> currentPath,
                                 List<PathResult> allPaths) {
        currentPath.add(courseMap.get(current));
        
        if (current.equals(target)) {
            allPaths.add(new PathResult(new ArrayList<>(currentPath)));
            currentPath.remove(currentPath.size() - 1);
            return;
        }
        
        visited.add(current);
        
        for (String neighbor : adjacencyList.getOrDefault(current, new HashSet<>())) {
            if (!visited.contains(neighbor)) {
                dfsFindAllPaths(neighbor, target, visited, currentPath, allPaths);
            }
        }
        
        visited.remove(current);
        currentPath.remove(currentPath.size() - 1);
    }
    
    /**
     * 取得課程的所有先修課程
     * @param courseId 課程代碼
     * @return 先修課程集合
     */
    public Set<String> getPrerequisites(String courseId) {
        if (courseId == null || !courseMap.containsKey(courseId.trim())) {
            return new HashSet<>();
        }
        return reverseAdjacencyList.get(courseId.trim());
    }
    
    /**
     * 取得課程的所有後續課程
     * @param courseId 課程代碼
     * @return 後續課程集合
     */
    public Set<String> getSuccessors(String courseId) {
        if (courseId == null || !courseMap.containsKey(courseId.trim())) {
            return new HashSet<>();
        }
        return adjacencyList.get(courseId.trim());
    }
    
    /**
     * 取得所有課程
     * @return 課程列表
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courseMap.values());
    }
    
    /**
     * 取得沒有先修課程的課程 (根課程)
     * @return 根課程列表
     */
    public List<Course> getRootCourses() {
        List<Course> roots = new ArrayList<>();
        for (String courseId : allCourses) {
            if (reverseAdjacencyList.get(courseId).isEmpty()) {
                roots.add(courseMap.get(courseId));
            }
        }
        return roots;
    }
    
    /**
     * 取得沒有後續課程的課程 (葉課程)
     * @return 葉課程列表
     */
    public List<Course> getLeafCourses() {
        List<Course> leaves = new ArrayList<>();
        for (String courseId : allCourses) {
            if (adjacencyList.get(courseId).isEmpty()) {
                leaves.add(courseMap.get(courseId));
            }
        }
        return leaves;
    }
    
    /**
     * 取得課程的入度 (先修課程數量)
     */
    public int getInDegree(String courseId) {
        if (courseId == null || !courseMap.containsKey(courseId.trim())) {
            return -1;
        }
        return reverseAdjacencyList.get(courseId.trim()).size();
    }
    
    /**
     * 取得課程的出度 (後續課程數量)
     */
    public int getOutDegree(String courseId) {
        if (courseId == null || !courseMap.containsKey(courseId.trim())) {
            return -1;
        }
        return adjacencyList.get(courseId.trim()).size();
    }
    
    /**
     * 拓撲排序 (建議修課順序)
     * @return 拓撲排序結果
     */
    public List<Course> topologicalSort() {
        List<Course> result = new ArrayList<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        
        // 計算入度
        for (String courseId : allCourses) {
            inDegree.put(courseId, getInDegree(courseId));
        }
        
        // 找到所有入度為 0 的課程
        for (String courseId : allCourses) {
            if (inDegree.get(courseId) == 0) {
                queue.offer(courseId);
            }
        }
        
        // 拓撲排序
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(courseMap.get(current));
            
            for (String neighbor : adjacencyList.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // 檢查是否有循環
        if (result.size() != allCourses.size()) {
            System.out.println("⚠️ 圖中存在循環，無法完成拓撲排序");
            return new ArrayList<>();
        }
        
        return result;
    }
    
    /**
     * 印出課程規劃圖
     */
    public void printGraph() {
        System.out.println("\n=== 課程規劃圖 ===");
        
        if (allCourses.isEmpty()) {
            System.out.println("尚無課程資料");
            return;
        }
        
        System.out.printf("課程總數: %d%n", allCourses.size());
        System.out.printf("先修關係總數: %d%n", getTotalPrerequisites());
        System.out.println();
        
        List<String> sortedCourses = new ArrayList<>(allCourses);
        Collections.sort(sortedCourses);
        
        System.out.printf("%-10s | %-20s | %-8s | %-20s | %-15s%n", 
                         "課程代碼", "課程名稱", "學分", "先修課程", "後續課程");
        System.out.println("-----------|----------------------|----------|----------------------|----------------------");
        
        for (String courseId : sortedCourses) {
            Course course = courseMap.get(courseId);
            Set<String> prereqs = reverseAdjacencyList.get(courseId);
            Set<String> successors = adjacencyList.get(courseId);
            
            System.out.printf("%-10s | %-20s | %8d | %-20s | %-20s%n",
                             courseId,
                             truncate(course.getName(), 20),
                             course.getCredits(),
                             prereqs.isEmpty() ? "無" : prereqs.toString(),
                             successors.isEmpty() ? "無" : successors.toString());
        }
        System.out.println();
    }
    
    /**
     * 取得總先修關係數
     */
    private int getTotalPrerequisites() {
        int count = 0;
        for (Set<String> successors : adjacencyList.values()) {
            count += successors.size();
        }
        return count;
    }
    
    /**
     * 截斷字串
     */
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * 印出所有修課路徑 (從根課程到所有葉課程)
     */
    public void printAllPaths() {
        System.out.println("\n=== 所有修課路徑 (從根課程到葉課程) ===");
        
        List<Course> roots = getRootCourses();
        List<Course> leaves = getLeafCourses();
        
        if (roots.isEmpty() || leaves.isEmpty()) {
            System.out.println("無法生成修課路徑 (無根課程或無葉課程)");
            return;
        }
        
        int pathCount = 0;
        for (Course root : roots) {
            for (Course leaf : leaves) {
                if (!root.getId().equals(leaf.getId())) {
                    List<PathResult> paths = findAllPaths(root.getId(), leaf.getId());
                    for (PathResult path : paths) {
                        pathCount++;
                        System.out.printf("路徑 %d: %s%n", pathCount, path);
                    }
                }
            }
        }
        
        if (pathCount == 0) {
            System.out.println("沒有找到任何修課路徑");
        } else {
            System.out.printf("\n共找到 %d 條修課路徑%n", pathCount);
        }
        System.out.println();
    }
    
    /**
     * 印出推薦修課順序
     */
    public void printRecommendedSequence() {
        System.out.println("\n=== 推薦修課順序 (拓撲排序) ===");
        
        List<Course> sequence = topologicalSort();
        if (sequence.isEmpty()) {
            System.out.println("無法生成推薦順序 (圖中存在循環)");
            return;
        }
        
        int semester = 1;
        int creditsThisSemester = 0;
        int maxCreditsPerSemester = 18;
        
        System.out.println("學期 | 課程代碼 | 課程名稱 | 學分");
        System.out.println("-----|----------|----------|------");
        
        for (Course course : sequence) {
            if (creditsThisSemester + course.getCredits() > maxCreditsPerSemester) {
                semester++;
                creditsThisSemester = 0;
                System.out.println("-----|----------|----------|------");
            }
            
            System.out.printf("  %2d  | %-8s | %-8s | %4d%n",
                             semester,
                             course.getId(),
                             truncate(course.getName(), 8),
                             course.getCredits());
            
            creditsThisSemester += course.getCredits();
        }
        System.out.println();
    }
    
    /**
     * 清空所有資料
     */
    public void clear() {
        adjacencyList.clear();
        reverseAdjacencyList.clear();
        courseMap.clear();
        allCourses.clear();
        System.out.println("🔄 已清空課程規劃圖");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 課程規劃圖測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：DFS 可達性
        testReachability();
        
        // 測試 3：所有路徑
        testAllPaths();
        
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
        
        CoursePlanningGraph graph = new CoursePlanningGraph();
        
        // 新增課程
        System.out.println("新增課程:");
        graph.addCourse("CS101", "程式設計入門", 3, "CS");
        graph.addCourse("CS201", "資料結構", 3, "CS");
        graph.addCourse("CS301", "演算法", 3, "CS");
        graph.addCourse("CS401", "編譯器設計", 3, "CS");
        graph.addCourse("CS202", "物件導向程式設計", 3, "CS");
        graph.addCourse("CS302", "作業系統", 3, "CS");
        graph.addCourse("CS303", "資料庫系統", 3, "CS");
        graph.addCourse("CS402", "軟體工程", 3, "CS");
        
        // 新增先修關係
        System.out.println("\n新增先修關係:");
        graph.addPrerequisite("CS101", "CS201");
        graph.addPrerequisite("CS201", "CS301");
        graph.addPrerequisite("CS301", "CS401");
        graph.addPrerequisite("CS101", "CS202");
        graph.addPrerequisite("CS202", "CS302");
        graph.addPrerequisite("CS201", "CS303");
        graph.addPrerequisite("CS202", "CS303");
        graph.addPrerequisite("CS301", "CS402");
        graph.addPrerequisite("CS302", "CS402");
        graph.addPrerequisite("CS303", "CS402");
        
        graph.printGraph();
        
        // 查詢
        System.out.println("📋 查詢:");
        System.out.println("  getPrerequisites('CS401'): " + graph.getPrerequisites("CS401"));
        System.out.println("  getSuccessors('CS101'): " + graph.getSuccessors("CS101"));
        System.out.println("  getInDegree('CS401'): " + graph.getInDegree("CS401"));
        System.out.println("  getOutDegree('CS101'): " + graph.getOutDegree("CS101"));
        System.out.println("  getRootCourses(): " + graph.getRootCourses());
        System.out.println("  getLeafCourses(): " + graph.getLeafCourses());
    }
    
    /**
     * 測試 DFS 可達性
     */
    private static void testReachability() {
        System.out.println("\n--- 測試 2: DFS 可達性 ---");
        
        CoursePlanningGraph graph = new CoursePlanningGraph();
        
        graph.addCourse("MATH101", "微積分", 3, "MATH");
        graph.addCourse("MATH201", "線性代數", 3, "MATH");
        graph.addCourse("MATH301", "機率論", 3, "MATH");
        graph.addCourse("CS101", "程式設計", 3, "CS");
        graph.addCourse("CS201", "資料結構", 3, "CS");
        graph.addCourse("CS301", "演算法", 3, "CS");
        
        graph.addPrerequisite("MATH101", "MATH201");
        graph.addPrerequisite("MATH201", "MATH301");
        graph.addPrerequisite("CS101", "CS201");
        graph.addPrerequisite("CS201", "CS301");
        graph.addPrerequisite("MATH101", "CS201");
        
        graph.printGraph();
        
        // 可達性測試
        System.out.println("\n🔍 可達性測試 (DFS):");
        String[][] tests = {
            {"CS101", "CS301"},
            {"CS101", "CS201"},
            {"MATH101", "CS301"},
            {"CS201", "MATH101"},
            {"MATH301", "CS201"},
            {"CS101", "CS101"}
        };
        
        for (String[] test : tests) {
            boolean reachable = graph.isReachable(test[0], test[1]);
            System.out.printf("  %s → %s: %s%n", test[0], test[1], 
                             reachable ? "✅ 可達" : "❌ 不可達");
        }
        
        // 顯示路徑
        System.out.println("\n🗺️ 路徑範例:");
        PathResult path = graph.findPath("MATH101", "CS301");
        if (path.isReachable()) {
            System.out.println("  MATH101 → CS301: " + path);
        }
        
        path = graph.findPath("CS101", "MATH301");
        if (!path.isReachable()) {
            System.out.println("  CS101 → MATH301: 無法到達");
        }
    }
    
    /**
     * 測試所有路徑
     */
    private static void testAllPaths() {
        System.out.println("\n--- 測試 3: 所有修課路徑 ---");
        
        CoursePlanningGraph graph = new CoursePlanningGraph();
        
        graph.addCourse("A", "課程A", 3, "DEPT");
        graph.addCourse("B", "課程B", 3, "DEPT");
        graph.addCourse("C", "課程C", 3, "DEPT");
        graph.addCourse("D", "課程D", 3, "DEPT");
        graph.addCourse("E", "課程E", 3, "DEPT");
        
        graph.addPrerequisite("A", "B");
        graph.addPrerequisite("A", "C");
        graph.addPrerequisite("B", "D");
        graph.addPrerequisite("C", "D");
        graph.addPrerequisite("D", "E");
        
        graph.printGraph();
        
        // 所有路徑
        System.out.println("\n🗺️ 從 A 到 E 的所有路徑:");
        List<PathResult> paths = graph.findAllPaths("A", "E");
        for (int i = 0; i < paths.size(); i++) {
            System.out.printf("  路徑 %d: %s%n", i + 1, paths.get(i));
        }
        
        // 從根到葉的所有路徑
        graph.printAllPaths();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("\n--- 測試 4: 邊界情況 ---");
        
        // 測試 4.1: 空圖
        System.out.println("測試 4.1: 空圖");
        CoursePlanningGraph graph = new CoursePlanningGraph();
        graph.printGraph();
        graph.printAllPaths();
        System.out.println("  isReachable('A', 'B'): " + graph.isReachable("A", "B"));
        System.out.println();
        
        // 測試 4.2: 單一課程
        System.out.println("測試 4.2: 單一課程");
        graph.addCourse("SINGLE", "單一課程", 3, "DEPT");
        graph.printGraph();
        System.out.println("  getRootCourses(): " + graph.getRootCourses());
        System.out.println("  getLeafCourses(): " + graph.getLeafCourses());
        System.out.println("  isReachable('SINGLE', 'SINGLE'): " + graph.isReachable("SINGLE", "SINGLE"));
        System.out.println();
        
        // 測試 4.3: 線性路徑
        System.out.println("測試 4.3: 線性路徑");
        CoursePlanningGraph graph2 = new CoursePlanningGraph();
        graph2.addCourse("1", "課程1", 3, "DEPT");
        graph2.addCourse("2", "課程2", 3, "DEPT");
        graph2.addCourse("3", "課程3", 3, "DEPT");
        graph2.addCourse("4", "課程4", 3, "DEPT");
        graph2.addPrerequisite("1", "2");
        graph2.addPrerequisite("2", "3");
        graph2.addPrerequisite("3", "4");
        
        graph2.printAllPaths();
        graph2.printRecommendedSequence();
        System.out.println();
        
        // 測試 4.4: 循環檢測
        System.out.println("測試 4.4: 循環檢測");
        CoursePlanningGraph graph3 = new CoursePlanningGraph();
        graph3.addCourse("X", "課程X", 3, "DEPT");
        graph3.addCourse("Y", "課程Y", 3, "DEPT");
        graph3.addCourse("Z", "課程Z", 3, "DEPT");
        
        graph3.addPrerequisite("X", "Y");
        graph3.addPrerequisite("Y", "Z");
        System.out.println("嘗試建立循環 X→Y, Y→Z, Z→X:");
        graph3.addPrerequisite("Z", "X");
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("\n--- 測試 5: 實際應用場景 ---");
        System.out.println("🏫 資訊工程學系課程規劃");
        
        CoursePlanningGraph csDept = new CoursePlanningGraph();
        
        // 建立資工系課程
        String[][] courses = {
            {"CS101", "計算機概論", "3", "CS"},
            {"CS102", "程式設計", "3", "CS"},
            {"CS201", "資料結構", "3", "CS"},
            {"CS202", "物件導向程式設計", "3", "CS"},
            {"CS203", "離散數學", "3", "CS"},
            {"CS204", "數位邏輯設計", "3", "CS"},
            {"CS301", "演算法", "3", "CS"},
            {"CS302", "作業系統", "3", "CS"},
            {"CS303", "資料庫系統", "3", "CS"},
            {"CS304", "計算機組織", "3", "CS"},
            {"CS305", "程式語言結構", "3", "CS"},
            {"CS401", "編譯器設計", "3", "CS"},
            {"CS402", "軟體工程", "3", "CS"},
            {"CS403", "網路概論", "3", "CS"},
            {"CS404", "人工智慧", "3", "CS"},
            {"CS405", "機器學習", "3", "CS"},
            {"CS406", "資訊安全", "3", "CS"},
            {"CS407", "大數據分析", "3", "CS"},
            {"CS408", "物聯網應用", "3", "CS"},
            {"CS409", "雲端運算", "3", "CS"}
        };
        
        for (String[] c : courses) {
            csDept.addCourse(c[0], c[1], Integer.parseInt(c[2]), c[3]);
        }
        
        // 建立先修關係
        String[][] prerequisites = {
            {"CS101", "CS102"},
            {"CS102", "CS201"},
            {"CS102", "CS202"},
            {"CS102", "CS203"},
            {"CS203", "CS301"},
            {"CS201", "CS301"},
            {"CS202", "CS301"},
            {"CS201", "CS302"},
            {"CS204", "CS302"},
            {"CS201", "CS303"},
            {"CS202", "CS303"},
            {"CS204", "CS304"},
            {"CS201", "CS305"},
            {"CS202", "CS305"},
            {"CS301", "CS401"},
            {"CS302", "CS401"},
            {"CS301", "CS402"},
            {"CS302", "CS402"},
            {"CS303", "CS402"},
            {"CS102", "CS403"},
            {"CS102", "CS404"},
            {"CS301", "CS404"},
            {"CS404", "CS405"},
            {"CS102", "CS406"},
            {"CS101", "CS407"},
            {"CS301", "CS407"},
            {"CS102", "CS408"},
            {"CS102", "CS409"}
        };
        
        for (String[] prereq : prerequisites) {
            csDept.addPrerequisite(prereq[0], prereq[1]);
        }
        
        csDept.printGraph();
        
        // 可達性測試
        System.out.println("\n🔍 可達性測試:");
        String[][] reachTests = {
            {"CS102", "CS401"},
            {"CS102", "CS405"},
            {"CS101", "CS404"},
            {"CS301", "CS102"}
        };
        
        for (String[] test : reachTests) {
            boolean reachable = csDept.isReachable(test[0], test[1]);
            System.out.printf("  %s → %s: %s%n", test[0], test[1], 
                             reachable ? "✅ 可達" : "❌ 不可達");
        }
        
        // 所有路徑範例
        System.out.println("\n🗺️ 從 CS102 到 CS401 的所有路徑:");
        List<PathResult> paths = csDept.findAllPaths("CS102", "CS401");
        for (int i = 0; i < Math.min(5, paths.size()); i++) {
            System.out.printf("  路徑 %d: %s%n", i + 1, paths.get(i));
        }
        if (paths.size() > 5) {
            System.out.printf("  ... (共 %d 條路徑)%n", paths.size());
        }
        
        // 推薦修課順序
        csDept.printRecommendedSequence();
        
        // 所有修課路徑
        csDept.printAllPaths();
        
        // 課程統計
        System.out.println("\n📊 課程統計:");
        List<Course> roots = csDept.getRootCourses();
        List<Course> leaves = csDept.getLeafCourses();
        System.out.printf("  根課程 (無先修): %d 門%n", roots.size());
        System.out.printf("  葉課程 (無後續): %d 門%n", leaves.size());
        
        // 找出最受歡迎的先修課程 (被最多課程需要)
        System.out.println("\n🏆 最常被需要的先修課程:");
        Map<String, Integer> prereqCount = new HashMap<>();
        for (String courseId : csDept.allCourses) {
            int outDegree = csDept.getOutDegree(courseId);
            if (outDegree > 0) {
                prereqCount.put(courseId, outDegree);
            }
        }
        
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(prereqCount.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        for (int i = 0; i < Math.min(5, sorted.size()); i++) {
            String id = sorted.get(i).getKey();
            Course course = csDept.courseMap.get(id);
            System.out.printf("    #%d: %s (%s) - %d 門後續課程%n", 
                             i + 1, id, course.getName(), sorted.get(i).getValue());
        }
    }
}