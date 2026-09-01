import java.util.*;

/**
 * 資料結構決策報告
 * 依 12 組需求輸出選擇、理由與主要 Big-O
 */
public class DataStructureDecisionReport {
    
    /**
     * 需求類別
     */
    public static class Requirement {
        private final int id;
        private final String name;
        private final String description;
        private final String operationPattern;
        
        public Requirement(int id, String name, String description, String operationPattern) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.operationPattern = operationPattern;
        }
        
        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public String getOperationPattern() {
            return operationPattern;
        }
        
        @Override
        public String toString() {
            return String.format("#%d %s", id, name);
        }
    }
    
    /**
     * 決策結果類別
     */
    public static class Decision {
        private final Requirement requirement;
        private final String recommendedStructure;
        private final String reason;
        private final String bigO;
        private final String alternatives;
        
        public Decision(Requirement requirement, String recommendedStructure, 
                       String reason, String bigO, String alternatives) {
            this.requirement = requirement;
            this.recommendedStructure = recommendedStructure;
            this.reason = reason;
            this.bigO = bigO;
            this.alternatives = alternatives;
        }
        
        public Requirement getRequirement() {
            return requirement;
        }
        
        public String getRecommendedStructure() {
            return recommendedStructure;
        }
        
        public String getReason() {
            return reason;
        }
        
        public String getBigO() {
            return bigO;
        }
        
        public String getAlternatives() {
            return alternatives;
        }
        
        @Override
        public String toString() {
            return String.format("需求 #%d: %s%n  推薦: %s%n  理由: %s%n  Big-O: %s%n  替代方案: %s",
                               requirement.getId(), requirement.getName(),
                               recommendedStructure, reason, bigO, alternatives);
        }
    }
    
    /**
     * 12 組需求定義
     */
    private static List<Requirement> getRequirements() {
        List<Requirement> requirements = new ArrayList<>();
        
        // 1. 索引存取
        requirements.add(new Requirement(1, "索引存取 (Index Access)",
            "需要透過索引快速存取元素",
            "頻繁的 get(index), set(index, value)"));
        
        // 2. 先進先出 (FIFO)
        requirements.add(new Requirement(2, "先進先出 (FIFO)",
            "需要佇列行為，先到的先處理",
            "offer(), poll(), peek()"));
        
        // 3. 後進先出 (LIFO)
        requirements.add(new Requirement(3, "後進先出 (LIFO)",
            "需要堆疊行為，後到的先處理",
            "push(), pop(), peek()"));
        
        // 4. 排序範圍查詢
        requirements.add(new Requirement(4, "排序範圍查詢 (Sorted Range)",
            "需要排序並進行範圍查詢",
            "subSet(), headSet(), tailSet()"));
        
        // 5. 取得最高優先權
        requirements.add(new Requirement(5, "取得最高優先權 (Next Priority)",
            "需要快速取得最高/最低優先權元素",
            "offer(), poll(), peek()"));
        
        // 6. 鍵值快速查詢
        requirements.add(new Requirement(6, "鍵值快速查詢 (Key Lookup)",
            "需要快速鍵值查詢和更新",
            "put(), get(), containsKey()"));
        
        // 7. 關係遍歷 (圖)
        requirements.add(new Requirement(7, "關係遍歷 (Graph Traversal)",
            "需要遍歷節點間的關係",
            "addEdge(), getNeighbors(), BFS/DFS"));
        
        // 8. 有序鍵值查詢
        requirements.add(new Requirement(8, "有序鍵值查詢 (Sorted Map)",
            "需要保持鍵的順序並進行範圍查詢",
            "put(), get(), firstKey(), lastKey()"));
        
        // 9. 唯一性檢查
        requirements.add(new Requirement(9, "唯一性檢查 (Uniqueness)",
            "需要確保元素不重複",
            "add(), contains(), remove()"));
        
        // 10. 雙端操作
        requirements.add(new Requirement(10, "雙端操作 (Deque Operations)",
            "需要在頭尾兩端進行操作",
            "addFirst(), addLast(), removeFirst(), removeLast()"));
        
        // 11. 頻率統計
        requirements.add(new Requirement(11, "頻率統計 (Frequency Count)",
            "需要統計元素出現次數",
            "put(), get(), keySet()"));
        
        // 12. 階層關係 (樹)
        requirements.add(new Requirement(12, "階層關係 (Tree Structure)",
            "需要表示和管理階層關係",
            "addChild(), getParent(), getChildren()"));
        
        return requirements;
    }
    
    /**
     * 根據需求做出決策
     */
    private static Decision decide(Requirement req) {
        switch (req.getId()) {
            case 1: // 索引存取
                return new Decision(req,
                    "ArrayList",
                    "ArrayList 提供 O(1) 的隨機存取，最適合需要頻繁透過索引存取元素的場景",
                    "get/set: O(1), add: O(1) amortized, remove: O(n)",
                    "LinkedList (get O(n), 不適合)");
            
            case 2: // FIFO
                return new Decision(req,
                    "ArrayDeque",
                    "ArrayDeque 提供 O(1) 的入隊和出隊操作，且比 LinkedList 更高效",
                    "offer/poll: O(1), size: O(1)",
                    "LinkedList (較多記憶體開銷), PriorityQueue (會排序)");
            
            case 3: // LIFO
                return new Decision(req,
                    "ArrayDeque",
                    "ArrayDeque 提供 O(1) 的 push/pop 操作，是 Stack 的最佳替代方案",
                    "push/pop: O(1), peek: O(1), size: O(1)",
                    "Stack (同步開銷大), LinkedList (較慢)");
            
            case 4: // 排序範圍查詢
                return new Decision(req,
                    "TreeSet / TreeMap",
                    "TreeSet/TreeMap 基於紅黑樹實作，提供 O(log n) 的排序和範圍查詢",
                    "add/remove: O(log n), subSet: O(1), first/last: O(log n)",
                    "PriorityQueue (不支援範圍查詢)");
            
            case 5: // 取得最高優先權
                return new Decision(req,
                    "PriorityQueue (Heap)",
                    "PriorityQueue 使用堆積實作，O(log n) 插入和取出，O(1) 查看最高優先權",
                    "offer/poll: O(log n), peek: O(1), size: O(1)",
                    "TreeSet (O(log n) 但功能更多)");
            
            case 6: // 鍵值快速查詢
                return new Decision(req,
                    "HashMap",
                    "HashMap 使用雜湊表，提供 O(1) 平均時間的鍵值查詢和更新",
                    "put/get: O(1) average, O(n) worst, containsKey: O(1)",
                    "TreeMap (O(log n), 有序), LinkedHashMap (保留順序)");
            
            case 7: // 關係遍歷
                return new Decision(req,
                    "Adjacency List (鄰接表)",
                    "鄰接表使用 Map<String, List<String>>，適合圖的遍歷和鄰居查詢",
                    "addEdge: O(1), getNeighbors: O(1), BFS/DFS: O(V+E)",
                    "Adjacency Matrix (O(1) 邊查詢, 但記憶體較大)");
            
            case 8: // 有序鍵值查詢
                return new Decision(req,
                    "TreeMap (紅黑樹)",
                    "TreeMap 保持鍵的有序性，提供 O(log n) 的查詢和範圍操作",
                    "put/get: O(log n), firstKey/lastKey: O(log n), subMap: O(1)",
                    "LinkedHashMap (僅保留插入順序)");
            
            case 9: // 唯一性檢查
                return new Decision(req,
                    "HashSet",
                    "HashSet 使用雜湊表，O(1) 平均時間檢查元素是否存在",
                    "add/contains/remove: O(1) average, O(n) worst",
                    "TreeSet (O(log n), 有序)");
            
            case 10: // 雙端操作
                return new Decision(req,
                    "ArrayDeque",
                    "ArrayDeque 提供 O(1) 的雙端操作，比 LinkedList 更高效且記憶體更小",
                    "addFirst/addLast: O(1), removeFirst/removeLast: O(1)",
                    "LinkedList (O(1) 但記憶體開銷大)");
            
            case 11: // 頻率統計
                return new Decision(req,
                    "HashMap (計數)",
                    "HashMap 用於統計元素出現次數，O(1) 更新和查詢計數",
                    "put/get: O(1) average, keySet: O(n)",
                    "TreeMap (O(log n), 可排序統計結果)");
            
            case 12: // 階層關係
                return new Decision(req,
                    "Tree (多叉樹) 或 Adjacency List",
                    "多叉樹適合表示階層關係，每個節點儲存子節點列表",
                    "addChild: O(1), getChildren: O(1), getParent: O(1)",
                    "Composite Pattern (設計模式)");
            
            default:
                return new Decision(req,
                    "未知",
                    "無法判斷",
                    "N/A",
                    "N/A");
        }
    }
    
    /**
     * 生成完整報告
     */
    public static String generateReport() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("=" .repeat(80)).append("\n");
        sb.append("         資料結構決策報告 (Data Structure Decision Report)\n");
        sb.append("=" .repeat(80)).append("\n");
        sb.append("生成時間: ").append(new Date()).append("\n");
        sb.append("\n");
        
        List<Requirement> requirements = getRequirements();
        List<Decision> decisions = new ArrayList<>();
        
        for (Requirement req : requirements) {
            decisions.add(decide(req));
        }
        
        // 表格格式摘要
        sb.append("📊 決策摘要 (決策摘要)\n");
        sb.append("-" .repeat(80)).append("\n");
        sb.append(String.format("%-4s | %-20s | %-20s | %-25s%n", 
                               "ID", "需求名稱", "推薦結構", "主要 Big-O"));
        sb.append("-" .repeat(80)).append("\n");
        
        for (Decision d : decisions) {
            sb.append(String.format("%-4d | %-20s | %-20s | %-25s%n",
                                   d.getRequirement().getId(),
                                   truncate(d.getRequirement().getName(), 20),
                                   truncate(d.getRecommendedStructure(), 20),
                                   truncate(d.getBigO(), 25)));
        }
        
        sb.append("-" .repeat(80)).append("\n\n");
        
        // 詳細決策
        sb.append("📋 詳細決策 (詳細決策)\n");
        sb.append("-" .repeat(80)).append("\n");
        
        for (Decision d : decisions) {
            sb.append("\n");
            sb.append("【需求 ").append(d.getRequirement().getId()).append("】");
            sb.append(" ").append(d.getRequirement().getName()).append("\n");
            sb.append("  描述: ").append(d.getRequirement().getDescription()).append("\n");
            sb.append("  操作模式: ").append(d.getRequirement().getOperationPattern()).append("\n");
            sb.append("  ✅ 推薦: ").append(d.getRecommendedStructure()).append("\n");
            sb.append("  📝 理由: ").append(d.getReason()).append("\n");
            sb.append("  ⏱️  時間複雜度: ").append(d.getBigO()).append("\n");
            sb.append("  🔄 替代方案: ").append(d.getAlternatives()).append("\n");
        }
        
        sb.append("\n").append("=" .repeat(80)).append("\n");
        sb.append("📌 結論與建議 (結論與建議)\n");
        sb.append("=" .repeat(80)).append("\n");
        sb.append(getConclusion(decisions));
        sb.append("\n");
        
        return sb.toString();
    }
    
    /**
     * 截斷字串
     */
    private static String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * 生成結論
     */
    private static String getConclusion(List<Decision> decisions) {
        StringBuilder sb = new StringBuilder();
        
        // 統計推薦結構的使用次數
        Map<String, Integer> structureCount = new HashMap<>();
        for (Decision d : decisions) {
            String struct = d.getRecommendedStructure();
            structureCount.put(struct, structureCount.getOrDefault(struct, 0) + 1);
        }
        
        sb.append("  最常推薦的資料結構:\n");
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(structureCount.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        for (Map.Entry<String, Integer> entry : sorted) {
            sb.append(String.format("    - %s: %d 次%n", entry.getKey(), entry.getValue()));
        }
        
        sb.append("\n  💡 選擇資料結構的原則:\n");
        sb.append("    1. 根據主要操作的時間複雜度選擇\n");
        sb.append("    2. 考慮記憶體使用和空間開銷\n");
        sb.append("    3. 考量是否有順序性需求\n");
        sb.append("    4. 評估是否需要執行緒安全\n");
        sb.append("    5. 考慮資料的存取模式 (隨機/順序)\n");
        
        return sb.toString();
    }
    
    /**
     * 印出報告
     */
    public static void printReport() {
        System.out.println(generateReport());
    }
    
    /**
     * 比較兩個資料結構
     */
    public static void compareStructures(String struct1, String struct2) {
        System.out.println("\n=== 資料結構比較: " + struct1 + " vs " + struct2 + " ===");
        
        // 常見資料結構的特性
        Map<String, Map<String, String>> properties = getStructureProperties();
        
        Map<String, String> prop1 = properties.getOrDefault(struct1, new HashMap<>());
        Map<String, String> prop2 = properties.getOrDefault(struct2, new HashMap<>());
        
        System.out.printf("%-20s | %-20s | %-20s%n", "特性", struct1, struct2);
        System.out.println("-" .repeat(65));
        
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(prop1.keySet());
        allKeys.addAll(prop2.keySet());
        
        for (String key : allKeys) {
            String val1 = prop1.getOrDefault(key, "N/A");
            String val2 = prop2.getOrDefault(key, "N/A");
            System.out.printf("%-20s | %-20s | %-20s%n", key, val1, val2);
        }
        System.out.println();
    }
    
    /**
     * 取得資料結構特性
     */
    private static Map<String, Map<String, String>> getStructureProperties() {
        Map<String, Map<String, String>> properties = new HashMap<>();
        
        // ArrayList
        Map<String, String> arrayList = new HashMap<>();
        arrayList.put("存取", "O(1)");
        arrayList.put("插入(頭)", "O(n)");
        arrayList.put("插入(尾)", "O(1) amortized");
        arrayList.put("刪除", "O(n)");
        arrayList.put("搜尋", "O(n)");
        arrayList.put("記憶體", "連續記憶體");
        arrayList.put("順序性", "保持插入順序");
        properties.put("ArrayList", arrayList);
        
        // LinkedList
        Map<String, String> linkedList = new HashMap<>();
        linkedList.put("存取", "O(n)");
        linkedList.put("插入(頭)", "O(1)");
        linkedList.put("插入(尾)", "O(1)");
        linkedList.put("刪除", "O(1)");
        linkedList.put("搜尋", "O(n)");
        linkedList.put("記憶體", "非連續記憶體");
        linkedList.put("順序性", "保持插入順序");
        properties.put("LinkedList", linkedList);
        
        // HashMap
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("存取", "O(1) average");
        hashMap.put("插入", "O(1) average");
        hashMap.put("刪除", "O(1) average");
        hashMap.put("搜尋", "O(1) average");
        hashMap.put("記憶體", "陣列 + 鏈結");
        hashMap.put("順序性", "無特定順序");
        properties.put("HashMap", hashMap);
        
        // TreeMap
        Map<String, String> treeMap = new HashMap<>();
        treeMap.put("存取", "O(log n)");
        treeMap.put("插入", "O(log n)");
        treeMap.put("刪除", "O(log n)");
        treeMap.put("搜尋", "O(log n)");
        treeMap.put("記憶體", "樹狀結構");
        treeMap.put("順序性", "鍵值排序");
        properties.put("TreeMap", treeMap);
        
        // HashSet
        Map<String, String> hashSet = new HashMap<>();
        hashSet.put("存取", "O(1) average");
        hashSet.put("插入", "O(1) average");
        hashSet.put("刪除", "O(1) average");
        hashSet.put("搜尋", "O(1) average");
        hashSet.put("記憶體", "陣列 + 鏈結");
        hashSet.put("順序性", "無特定順序");
        hashSet.put("唯一性", "強制唯一");
        properties.put("HashSet", hashSet);
        
        // TreeSet
        Map<String, String> treeSet = new HashMap<>();
        treeSet.put("存取", "O(log n)");
        treeSet.put("插入", "O(log n)");
        treeSet.put("刪除", "O(log n)");
        treeSet.put("搜尋", "O(log n)");
        treeSet.put("記憶體", "樹狀結構");
        treeSet.put("順序性", "元素排序");
        treeSet.put("唯一性", "強制唯一");
        properties.put("TreeSet", treeSet);
        
        // PriorityQueue
        Map<String, String> priorityQueue = new HashMap<>();
        priorityQueue.put("插入", "O(log n)");
        priorityQueue.put("取出", "O(log n)");
        priorityQueue.put("查看頂部", "O(1)");
        priorityQueue.put("搜尋", "O(n)");
        priorityQueue.put("記憶體", "陣列");
        priorityQueue.put("順序性", "依優先權");
        priorityQueue.put("唯一性", "可重複");
        properties.put("PriorityQueue", priorityQueue);
        
        // ArrayDeque
        Map<String, String> arrayDeque = new HashMap<>();
        arrayDeque.put("存取(頭)", "O(1)");
        arrayDeque.put("存取(尾)", "O(1)");
        arrayDeque.put("插入(頭)", "O(1)");
        arrayDeque.put("插入(尾)", "O(1)");
        arrayDeque.put("刪除(頭)", "O(1)");
        arrayDeque.put("刪除(尾)", "O(1)");
        arrayDeque.put("記憶體", "連續記憶體");
        arrayDeque.put("順序性", "保持插入順序");
        properties.put("ArrayDeque", arrayDeque);
        
        // 圖 (鄰接表)
        Map<String, String> graph = new HashMap<>();
        graph.put("新增邊", "O(1)");
        graph.put("查詢邊", "O(1)");
        graph.put("查詢鄰居", "O(degree)");
        graph.put("BFS/DFS", "O(V+E)");
        graph.put("記憶體", "O(V+E)");
        graph.put("順序性", "無特定順序");
        properties.put("Graph adjacency list", graph);
        
        // Tree (多叉樹)
        Map<String, String> tree = new HashMap<>();
        tree.put("新增子節點", "O(1)");
        tree.put("取得父節點", "O(1)");
        tree.put("取得子節點", "O(1)");
        tree.put("遍歷", "O(n)");
        tree.put("記憶體", "O(n)");
        tree.put("順序性", "階層結構");
        properties.put("Tree (多叉樹)", tree);
        
        return properties;
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 資料結構決策報告系統 ===\n");
        
        // 生成完整報告
        printReport();
        
        // 比較常見的資料結構
        System.out.println("\n📊 資料結構比較:");
        compareStructures("ArrayList", "LinkedList");
        compareStructures("HashMap", "TreeMap");
        compareStructures("HashSet", "TreeSet");
        compareStructures("ArrayDeque", "PriorityQueue");
        
        // 輸出快速參考卡片
        printQuickReference();
    }
    
    /**
     * 印出快速參考卡片
     */
    private static void printQuickReference() {
        System.out.println("\n📋 快速參考卡片 (Quick Reference)");
        System.out.println("=" .repeat(80));
        
        System.out.printf("%-25s | %-15s | %-15s | %-15s%n", 
                         "資料結構", "存取", "插入", "刪除");
        System.out.println("-" .repeat(80));
        
        // 常用資料結構的時間複雜度
        String[][] quickRef = {
            {"ArrayList", "O(1)", "O(n) / O(1)*", "O(n)"},
            {"LinkedList", "O(n)", "O(1)", "O(1)"},
            {"HashMap", "O(1) avg", "O(1) avg", "O(1) avg"},
            {"TreeMap", "O(log n)", "O(log n)", "O(log n)"},
            {"HashSet", "O(1) avg", "O(1) avg", "O(1) avg"},
            {"TreeSet", "O(log n)", "O(log n)", "O(log n)"},
            {"PriorityQueue", "O(1)**", "O(log n)", "O(log n)"},
            {"ArrayDeque", "O(1)***", "O(1)***", "O(1)***"}
        };
        
        for (String[] row : quickRef) {
            System.out.printf("%-25s | %-15s | %-15s | %-15s%n", row[0], row[1], row[2], row[3]);
        }
        
        System.out.println("\n* O(1) amortized for add at end");
        System.out.println("** peek is O(1)");
        System.out.println("*** head/tail operations");
    }
}