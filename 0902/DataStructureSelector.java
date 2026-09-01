/**
 * 資料結構選擇器
 * 根據需求選擇適當的資料結構
 * 相容 Java 8-11
 */
public class DataStructureSelector {
    
    // 常數定義
    private static final String UNKNOWN = "Unknown";
    
    /**
     * 需求列舉
     */
    public enum Requirement {
        INDEX_ACCESS,           // 索引存取
        FIFO,                   // 先進先出
        LIFO,                   // 後進先出
        SORTED_RANGE,           // 排序範圍查詢
        NEXT_PRIORITY,          // 取得最高優先權
        KEY_LOOKUP,             // 鍵值查詢
        RELATION_TRAVERSAL      // 關係遍歷
    }
    
    /**
     * 根據需求選擇資料結構
     * @param requirement 需求
     * @return 建議的資料結構名稱
     */
    static String choose(Requirement requirement) {
        if (requirement == null) {
            return UNKNOWN;
        }
        
        // 使用傳統 switch 語法 (相容 Java 8-11)
        switch (requirement) {
            case INDEX_ACCESS:
                return "ArrayList";
            case FIFO:
                return "ArrayDeque as Queue";
            case LIFO:
                return "ArrayDeque as Stack";
            case SORTED_RANGE:
                return "Balanced BST / TreeMap";
            case NEXT_PRIORITY:
                return "Heap / PriorityQueue";
            case KEY_LOOKUP:
                return "HashMap";
            case RELATION_TRAVERSAL:
                return "Graph adjacency list";
            default:
                return UNKNOWN;
        }
    }
    
    /**
     * 取得資料結構的詳細說明
     * @param requirement 需求
     * @return 詳細說明文字
     */
    static String getDetailedDescription(Requirement requirement) {
        if (requirement == null) {
            return "無效的需求";
        }
        
        switch (requirement) {
            case INDEX_ACCESS:
                return "需要快速隨機存取 (O(1))，適合使用 ArrayList";
            case FIFO:
                return "需要先進先出 (FIFO) 行為，適合使用 ArrayDeque 或 LinkedList";
            case LIFO:
                return "需要後進先出 (LIFO) 行為，適合使用 ArrayDeque 或 Stack";
            case SORTED_RANGE:
                return "需要排序並進行範圍查詢，適合使用 TreeMap 或 TreeSet";
            case NEXT_PRIORITY:
                return "需要快速取得最高優先權元素，適合使用 PriorityQueue (Heap)";
            case KEY_LOOKUP:
                return "需要快速鍵值查詢 (O(1))，適合使用 HashMap";
            case RELATION_TRAVERSAL:
                return "需要遍歷節點間的關係，適合使用圖的鄰接表 (Adjacency List)";
            default:
                return "未知的需求";
        }
    }
    
    /**
     * 取得時間複雜度資訊
     * @param requirement 需求
     * @return 時間複雜度說明
     */
    static String getTimeComplexity(Requirement requirement) {
        if (requirement == null) {
            return "N/A";
        }
        
        switch (requirement) {
            case INDEX_ACCESS:
                return "存取 O(1), 插入 O(n)";
            case FIFO:
                return "入隊 O(1), 出隊 O(1)";
            case LIFO:
                return "推入 O(1), 彈出 O(1)";
            case SORTED_RANGE:
                return "查詢 O(log n), 插入 O(log n)";
            case NEXT_PRIORITY:
                return "插入 O(log n), 取出 O(log n)";
            case KEY_LOOKUP:
                return "查詢 O(1) 平均, 插入 O(1) 平均";
            case RELATION_TRAVERSAL:
                return "遍歷 O(V + E), 查詢 O(1)";
            default:
                return "N/A";
        }
    }
    
    /**
     * 顯示所有需求的選擇結果 (表格格式)
     */
    static void printSelectionTable() {
        System.out.println("\n=== 資料結構選擇表 ===");
        System.out.printf("%-20s | %-25s | %-30s%n", 
                         "需求", "建議資料結構", "時間複雜度");
        System.out.println("---------------------|---------------------------|-------------------------------");
        
        for (Requirement req : Requirement.values()) {
            String structure = choose(req);
            String complexity = getTimeComplexity(req);
            System.out.printf("%-20s | %-25s | %-30s%n", 
                             req.name(), structure, complexity);
        }
    }
    
    /**
     * 顯示詳細建議
     */
    static void printDetailedRecommendations() {
        System.out.println("\n=== 詳細建議 ===");
        
        for (Requirement req : Requirement.values()) {
            System.out.printf("%n📌 %s:%n", req.name());
            System.out.printf("  建議: %s%n", choose(req));
            System.out.printf("  說明: %s%n", getDetailedDescription(req));
            System.out.printf("  複雜度: %s%n", getTimeComplexity(req));
        }
    }
    
    /**
     * 根據需求情境建議
     */
    static String suggestByScenario(String scenario) {
        if (scenario == null || scenario.trim().isEmpty()) {
            return "請輸入情境描述";
        }
        
        String lower = scenario.toLowerCase();
        
        if (lower.contains("陣列") || lower.contains("索引") || lower.contains("array") || lower.contains("index")) {
            return choose(Requirement.INDEX_ACCESS) + " - " + getDetailedDescription(Requirement.INDEX_ACCESS);
        } else if (lower.contains("佇列") || lower.contains("queue") || lower.contains("fifo") || lower.contains("先進先出")) {
            return choose(Requirement.FIFO) + " - " + getDetailedDescription(Requirement.FIFO);
        } else if (lower.contains("堆疊") || lower.contains("stack") || lower.contains("lifo") || lower.contains("後進先出")) {
            return choose(Requirement.LIFO) + " - " + getDetailedDescription(Requirement.LIFO);
        } else if (lower.contains("排序") || lower.contains("範圍") || lower.contains("sorted") || lower.contains("tree")) {
            return choose(Requirement.SORTED_RANGE) + " - " + getDetailedDescription(Requirement.SORTED_RANGE);
        } else if (lower.contains("優先") || lower.contains("priority") || lower.contains("heap")) {
            return choose(Requirement.NEXT_PRIORITY) + " - " + getDetailedDescription(Requirement.NEXT_PRIORITY);
        } else if (lower.contains("查詢") || lower.contains("map") || lower.contains("hash") || lower.contains("鍵值")) {
            return choose(Requirement.KEY_LOOKUP) + " - " + getDetailedDescription(Requirement.KEY_LOOKUP);
        } else if (lower.contains("圖") || lower.contains("graph") || lower.contains("關係") || lower.contains("鄰接")) {
            return choose(Requirement.RELATION_TRAVERSAL) + " - " + getDetailedDescription(Requirement.RELATION_TRAVERSAL);
        } else {
            return "無法根據情境判斷，請參考完整需求清單";
        }
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 資料結構選擇器 ===\n");
        
        // 基本測試：顯示所有需求的選擇結果
        System.out.println("基本測試 (所有需求):");
        for (Requirement requirement : Requirement.values()) {
            System.out.println("  " + requirement + " → " + choose(requirement));
        }
        System.out.println("  null → " + choose(null));
        System.out.println();
        
        // 顯示選擇表格
        printSelectionTable();
        
        // 顯示詳細建議
        printDetailedRecommendations();
        
        // 情境建議測試
        System.out.println("\n=== 情境建議測試 ===");
        String[] scenarios = {
            "我需要快速隨機存取大量數據",
            "我想要先進先出的排隊系統",
            "需要後進先出的功能",
            "我想要排序並查詢範圍",
            "我需要快速取得最高優先權的任務",
            "我要用學號查詢學生成績",
            "我需要建立社群網路的好友關係",
            "一個普通的應用程式"
        };
        
        for (String scenario : scenarios) {
            System.out.printf("%n情境: %s%n", scenario);
            System.out.println("  建議: " + suggestByScenario(scenario));
        }
    }
}