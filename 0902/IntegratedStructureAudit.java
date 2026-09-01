import java.util.*;

/**
 * 整合結構審計
 * 根據操作模式判斷資料結構使用是否合理，輸出診斷報告
 */
public class IntegratedStructureAudit {
    
    /**
     * 操作模式類別
     */
    public static class OperationPattern {
        private final String name;
        private final String description;
        private final List<String> operations;
        private final Map<String, Integer> operationFrequencies;
        
        public OperationPattern(String name, String description) {
            this.name = name;
            this.description = description;
            this.operations = new ArrayList<>();
            this.operationFrequencies = new HashMap<>();
        }
        
        public void addOperation(String operation, int frequency) {
            operations.add(operation);
            operationFrequencies.put(operation, frequency);
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public List<String> getOperations() {
            return operations;
        }
        
        public Map<String, Integer> getOperationFrequencies() {
            return operationFrequencies;
        }
        
        public int getTotalOperations() {
            int total = 0;
            for (int freq : operationFrequencies.values()) {
                total += freq;
            }
            return total;
        }
    }
    
    /**
     * 審計結果類別
     */
    public static class AuditResult {
        private final String structureName;
        private final boolean isRecommended;
        private final String reason;
        private final double score;
        private final List<String> issues;
        private final List<String> recommendations;
        private final String alternativeStructure;
        
        public AuditResult(String structureName, boolean isRecommended, 
                          String reason, double score, List<String> issues,
                          List<String> recommendations, String alternativeStructure) {
            this.structureName = structureName;
            this.isRecommended = isRecommended;
            this.reason = reason;
            this.score = score;
            this.issues = issues;
            this.recommendations = recommendations;
            this.alternativeStructure = alternativeStructure;
        }
        
        public String getStructureName() {
            return structureName;
        }
        
        public boolean isRecommended() {
            return isRecommended;
        }
        
        public String getReason() {
            return reason;
        }
        
        public double getScore() {
            return score;
        }
        
        public List<String> getIssues() {
            return issues;
        }
        
        public List<String> getRecommendations() {
            return recommendations;
        }
        
        public String getAlternativeStructure() {
            return alternativeStructure;
        }
        
        public String getRating() {
            if (score >= 90) return "⭐ 優秀";
            if (score >= 70) return "👍 良好";
            if (score >= 50) return "📊 尚可";
            if (score >= 30) return "⚠️ 需改善";
            return "❌ 不適合";
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n📋 審計報告: ").append(structureName).append("\n");
            sb.append("=" .repeat(60)).append("\n");
            sb.append("評分: ").append(score).append(" 分 (").append(getRating()).append(")\n");
            sb.append("推薦: ").append(isRecommended ? "✅ 是" : "❌ 否").append("\n");
            sb.append("理由: ").append(reason).append("\n");
            
            if (!issues.isEmpty()) {
                sb.append("\n⚠️ 問題:\n");
                for (String issue : issues) {
                    sb.append("  - ").append(issue).append("\n");
                }
            }
            
            if (!recommendations.isEmpty()) {
                sb.append("\n💡 建議:\n");
                for (String rec : recommendations) {
                    sb.append("  - ").append(rec).append("\n");
                }
            }
            
            if (!alternativeStructure.isEmpty()) {
                sb.append("\n🔄 替代方案: ").append(alternativeStructure).append("\n");
            }
            
            return sb.toString();
        }
    }
    
    /**
     * 審計 List
     */
    public static AuditResult auditList(OperationPattern pattern) {
        double score = 0;
        List<String> issues = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        String alternative = "";
        
        Map<String, Integer> ops = pattern.getOperationFrequencies();
        int total = pattern.getTotalOperations();
        
        // 檢查索引存取
        int indexAccess = ops.getOrDefault("get(index)", 0) + 
                         ops.getOrDefault("set(index)", 0);
        if (indexAccess > total * 0.3) {
            score += 40;
        } else {
            issues.add("索引存取頻率低 (" + (indexAccess * 100 / total) + "%)，可能不需要 List");
            recommendations.add("若不需要索引存取，考慮使用 Collection 或其他結構");
        }
        
        // 檢查頭部插入/刪除
        int headOps = ops.getOrDefault("addFirst", 0) + 
                      ops.getOrDefault("removeFirst", 0);
        if (headOps > total * 0.2) {
            score -= 20;
            issues.add("頻繁頭部操作 (" + (headOps * 100 / total) + "%)，ArrayList 不適合");
            recommendations.add("考慮使用 LinkedList 或 ArrayDeque");
            alternative = "LinkedList / ArrayDeque";
        }
        
        // 檢查尾部插入/刪除
        int tailOps = ops.getOrDefault("add", 0) + 
                      ops.getOrDefault("addLast", 0) +
                      ops.getOrDefault("removeLast", 0);
        if (tailOps > total * 0.2) {
            score += 20;
        }
        
        // 檢查搜尋操作
        int searchOps = ops.getOrDefault("contains", 0) + 
                        ops.getOrDefault("indexOf", 0);
        if (searchOps > total * 0.2) {
            score -= 10;
            issues.add("頻繁搜尋操作 (" + (searchOps * 100 / total) + "%)，List 效率低");
            recommendations.add("考慮使用 HashSet 或 HashMap");
            alternative = "HashSet / HashMap";
        }
        
        // 檢查遍歷操作
        int traverseOps = ops.getOrDefault("iterator", 0) + 
                          ops.getOrDefault("forEach", 0);
        if (traverseOps > 0) {
            score += 10;
        }
        
        // 計算總分
        score = Math.min(100, Math.max(0, score + 30));
        
        boolean recommended = score >= 60;
        String reason = recommended ? 
            "List 適合您的使用場景" : 
            "List 可能不是最佳選擇，請考慮替代方案";
        
        return new AuditResult("List", recommended, reason, score, 
                              issues, recommendations, alternative);
    }
    
    /**
     * 審計 Queue
     */
    public static AuditResult auditQueue(OperationPattern pattern) {
        double score = 0;
        List<String> issues = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        String alternative = "";
        
        Map<String, Integer> ops = pattern.getOperationFrequencies();
        int total = pattern.getTotalOperations();
        
        // 檢查 FIFO 操作
        int offerOps = ops.getOrDefault("offer", 0) + ops.getOrDefault("add", 0);
        int pollOps = ops.getOrDefault("poll", 0) + ops.getOrDefault("remove", 0);
        int peekOps = ops.getOrDefault("peek", 0) + ops.getOrDefault("element", 0);
        
        if (offerOps > 0 && pollOps > 0) {
            score += 40;
        } else {
            issues.add("缺少完整的佇列操作 (offer/poll)");
            recommendations.add("確保使用 offer() 和 poll() 進行佇列操作");
        }
        
        // 檢查是否真的需要 FIFO
        if (offerOps == 0 && pollOps == 0) {
            score -= 30;
            issues.add("未使用佇列核心操作 (offer/poll)，可能不需要 Queue");
            recommendations.add("若不需要 FIFO 行為，考慮其他結構");
            alternative = "List / Set";
        }
        
        // 檢查隨機存取
        int randomAccess = ops.getOrDefault("get(index)", 0);
        if (randomAccess > 0) {
            score -= 20;
            issues.add("使用了隨機存取，Queue 不支援");
            recommendations.add("若需要隨機存取，使用 List");
            alternative = "ArrayList";
        }
        
        // 檢查容量限制
        if (offerOps > total * 0.3) {
            score += 20;
        }
        
        // 計算總分
        score = Math.min(100, Math.max(0, score + 30));
        
        boolean recommended = score >= 60;
        String reason = recommended ? 
            "Queue 適合您的 FIFO 場景" : 
            "Queue 可能不是最佳選擇";
        
        return new AuditResult("Queue", recommended, reason, score, 
                              issues, recommendations, alternative);
    }
    
    /**
     * 審計 BST (TreeMap/TreeSet)
     */
    public static AuditResult auditBST(OperationPattern pattern) {
        double score = 0;
        List<String> issues = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        String alternative = "";
        
        Map<String, Integer> ops = pattern.getOperationFrequencies();
        int total = pattern.getTotalOperations();
        
        // 檢查排序操作
        int sortedOps = ops.getOrDefault("firstKey", 0) + 
                        ops.getOrDefault("lastKey", 0) +
                        ops.getOrDefault("headMap", 0) +
                        ops.getOrDefault("tailMap", 0) +
                        ops.getOrDefault("subMap", 0);
        
        if (sortedOps > 0) {
            score += 30;
        } else {
            issues.add("未使用範圍查詢或排序功能 (firstKey/lastKey/subMap)");
            recommendations.add("若不需要排序，考慮使用 HashMap/HashSet");
            alternative = "HashMap / HashSet";
        }
        
        // 檢查是否需要保持順序
        int putOps = ops.getOrDefault("put", 0) + ops.getOrDefault("add", 0);
        int getOps = ops.getOrDefault("get", 0) + ops.getOrDefault("contains", 0);
        
        if (putOps > total * 0.1 && getOps > total * 0.1) {
            score += 20;
        }
        
        // 檢查是否只需要快速查詢
        if (sortedOps == 0 && getOps > total * 0.3) {
            score -= 20;
            issues.add("僅需快速查詢但使用 TreeMap/TreeSet，效率較低");
            recommendations.add("考慮使用 HashMap/HashSet (O(1) vs O(log n))");
            alternative = "HashMap / HashSet";
        }
        
        // 計算總分
        score = Math.min(100, Math.max(0, score + 30));
        
        boolean recommended = score >= 60;
        String reason = recommended ? 
            "BST 適合您的排序和範圍查詢需求" : 
            "BST 可能不是最佳選擇";
        
        return new AuditResult("BST (TreeMap/TreeSet)", recommended, reason, score, 
                              issues, recommendations, alternative);
    }
    
    /**
     * 審計 Heap (PriorityQueue)
     */
    public static AuditResult auditHeap(OperationPattern pattern) {
        double score = 0;
        List<String> issues = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        String alternative = "";
        
        Map<String, Integer> ops = pattern.getOperationFrequencies();
        int total = pattern.getTotalOperations();
        
        // 檢查優先權操作
        int offerOps = ops.getOrDefault("offer", 0) + ops.getOrDefault("add", 0);
        int pollOps = ops.getOrDefault("poll", 0) + ops.getOrDefault("remove", 0);
        int peekOps = ops.getOrDefault("peek", 0);
        
        if (offerOps > 0 && pollOps > 0 && peekOps > 0) {
            score += 40;
        } else {
            issues.add("缺少完整的堆操作 (offer/poll/peek)");
            recommendations.add("確保使用 offer(), poll(), peek() 操作");
        }
        
        // 檢查優先權需求
        if (peekOps > total * 0.1) {
            score += 20;
        } else {
            issues.add("較少使用 peek() 查看最高優先權");
        }
        
        // 檢查隨機存取
        int randomAccess = ops.getOrDefault("get(index)", 0) + 
                           ops.getOrDefault("contains", 0);
        if (randomAccess > total * 0.1) {
            score -= 20;
            issues.add("使用隨機存取或搜尋，Heap 不支援");
            recommendations.add("若需要隨機存取，使用 List 或 Map");
            alternative = "ArrayList / HashMap";
        }
        
        // 計算總分
        score = Math.min(100, Math.max(0, score + 30));
        
        boolean recommended = score >= 60;
        String reason = recommended ? 
            "Heap 適合您的優先權佇列需求" : 
            "Heap 可能不是最佳選擇";
        
        return new AuditResult("Heap (PriorityQueue)", recommended, reason, score, 
                              issues, recommendations, alternative);
    }
    
    /**
     * 審計 Hash Table (HashMap/HashSet)
     */
    public static AuditResult auditHashTable(OperationPattern pattern) {
        double score = 0;
        List<String> issues = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        String alternative = "";
        
        Map<String, Integer> ops = pattern.getOperationFrequencies();
        int total = pattern.getTotalOperations();
        
        // 檢查鍵值操作
        int putOps = ops.getOrDefault("put", 0) + ops.getOrDefault("add", 0);
        int getOps = ops.getOrDefault("get", 0) + ops.getOrDefault("contains", 0);
        int removeOps = ops.getOrDefault("remove", 0);
        
        if (putOps > 0 && getOps > 0) {
            score += 40;
        } else {
            issues.add("缺少完整的鍵值操作 (put/get)");
            recommendations.add("確保使用 put() 和 get() 進行鍵值操作");
        }
        
        // 檢查查詢效率
        if (getOps > total * 0.2) {
            score += 20;
        }
        
        // 檢查是否需要順序
        int sortedOps = ops.getOrDefault("firstKey", 0) + 
                        ops.getOrDefault("lastKey", 0);
        if (sortedOps > 0) {
            score -= 20;
            issues.add("使用順序相關操作，HashMap 不保證順序");
            recommendations.add("若需要順序，考慮使用 TreeMap/LinkedHashMap");
            alternative = "TreeMap / LinkedHashMap";
        }
        
        // 檢查遍歷
        int traverseOps = ops.getOrDefault("keySet", 0) + 
                          ops.getOrDefault("entrySet", 0) +
                          ops.getOrDefault("values", 0);
        if (traverseOps > 0) {
            score += 10;
        }
        
        // 計算總分
        score = Math.min(100, Math.max(0, score + 20));
        
        boolean recommended = score >= 60;
        String reason = recommended ? 
            "Hash Table 適合您的快速查詢需求" : 
            "Hash Table 可能不是最佳選擇";
        
        return new AuditResult("Hash Table (HashMap/HashSet)", recommended, reason, score, 
                              issues, recommendations, alternative);
    }
    
    /**
     * 審計 Graph
     */
    public static AuditResult auditGraph(OperationPattern pattern) {
        double score = 0;
        List<String> issues = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        String alternative = "";
        
        Map<String, Integer> ops = pattern.getOperationFrequencies();
        int total = pattern.getTotalOperations();
        
        // 檢查圖操作
        int addEdgeOps = ops.getOrDefault("addEdge", 0);
        int getNeighborOps = ops.getOrDefault("getNeighbors", 0) + 
                            ops.getOrDefault("getAdjacent", 0);
        int traverseOps = ops.getOrDefault("BFS", 0) + 
                          ops.getOrDefault("DFS", 0);
        
        if (addEdgeOps > 0) {
            score += 20;
        } else {
            issues.add("未使用 addEdge 操作");
        }
        
        if (getNeighborOps > 0) {
            score += 20;
        } else {
            issues.add("未使用鄰居查詢操作");
        }
        
        if (traverseOps > 0) {
            score += 20;
        } else {
            issues.add("未使用圖遍歷 (BFS/DFS)");
            recommendations.add("若不需要遍歷，可能不需要 Graph");
            alternative = "其他資料結構";
        }
        
        // 檢查是否真的需要圖
        if (addEdgeOps == 0 && getNeighborOps == 0 && traverseOps == 0) {
            score -= 30;
            issues.add("未使用任何圖操作，可能不需要 Graph");
            recommendations.add("重新評估是否需要圖結構");
        }
        
        // 計算總分
        score = Math.min(100, Math.max(0, score + 30));
        
        boolean recommended = score >= 60;
        String reason = recommended ? 
            "Graph 適合您的關係操作需求" : 
            "Graph 可能不是最佳選擇";
        
        return new AuditResult("Graph", recommended, reason, score, 
                              issues, recommendations, alternative);
    }
    
    /**
     * 執行完整審計
     */
    public static void auditAll(OperationPattern pattern) {
        System.out.println("\n=== 整合結構審計報告 ===");
        System.out.println("操作模式: " + pattern.getName());
        System.out.println("描述: " + pattern.getDescription());
        System.out.println("操作頻率: " + pattern.getOperationFrequencies());
        System.out.println("=" .repeat(60));
        
        AuditResult[] results = {
            auditList(pattern),
            auditQueue(pattern),
            auditBST(pattern),
            auditHeap(pattern),
            auditHashTable(pattern),
            auditGraph(pattern)
        };
        
        // 按評分排序
        Arrays.sort(results, (a, b) -> Double.compare(b.getScore(), a.getScore()));
        
        for (AuditResult result : results) {
            System.out.println(result);
        }
        
        // 最終建議
        System.out.println("\n🎯 最終建議:");
        System.out.println("=" .repeat(60));
        
        AuditResult best = results[0];
        if (best.isRecommended()) {
            System.out.println("✅ 推薦使用: " + best.getStructureName());
            System.out.println("   評分: " + best.getScore() + " 分");
            System.out.println("   理由: " + best.getReason());
        } else {
            System.out.println("⚠️ 當前結構可能不適合，請考慮重新評估");
            System.out.println("   最佳結構: " + best.getStructureName());
            System.out.println("   評分: " + best.getScore() + " 分");
        }
        
        // 顯示所有推薦結構
        System.out.println("\n📊 評分排名:");
        for (int i = 0; i < results.length; i++) {
            String status = results[i].isRecommended() ? "✅" : "❌";
            System.out.printf("  %s %s: %.1f 分%n", 
                             status, results[i].getStructureName(), results[i].getScore());
        }
        System.out.println();
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 整合結構審計系統測試 ===\n");
        
        // 測試 1：List 場景 (頻繁索引存取)
        testListScenario();
        
        // 測試 2：Queue 場景 (FIFO)
        testQueueScenario();
        
        // 測試 3：BST 場景 (排序和範圍查詢)
        testBSTScenario();
        
        // 測試 4：Heap 場景 (優先權)
        testHeapScenario();
        
        // 測試 5：Hash Table 場景 (快速查詢)
        testHashTableScenario();
        
        // 測試 6：Graph 場景 (關係操作)
        testGraphScenario();
        
        // 測試 7：混合場景
        testMixedScenario();
    }
    
    /**
     * 測試 List 場景
     */
    private static void testListScenario() {
        System.out.println("--- 測試 1: List 場景 (索引存取為主) ---");
        
        OperationPattern pattern = new OperationPattern("索引存取場景", 
            "主要透過索引存取和更新數據");
        pattern.addOperation("get(index)", 100);
        pattern.addOperation("set(index)", 80);
        pattern.addOperation("add", 50);
        pattern.addOperation("remove(index)", 30);
        
        auditAll(pattern);
    }
    
    /**
     * 測試 Queue 場景
     */
    private static void testQueueScenario() {
        System.out.println("\n--- 測試 2: Queue 場景 (FIFO) ---");
        
        OperationPattern pattern = new OperationPattern("FIFO 佇列場景", 
            "需要先進先出的佇列行為");
        pattern.addOperation("offer", 100);
        pattern.addOperation("poll", 80);
        pattern.addOperation("peek", 60);
        pattern.addOperation("size", 30);
        
        auditAll(pattern);
    }
    
    /**
     * 測試 BST 場景
     */
    private static void testBSTScenario() {
        System.out.println("\n--- 測試 3: BST 場景 (排序和範圍查詢) ---");
        
        OperationPattern pattern = new OperationPattern("排序範圍查詢場景", 
            "需要保持順序並進行範圍查詢");
        pattern.addOperation("put", 50);
        pattern.addOperation("get", 80);
        pattern.addOperation("firstKey", 30);
        pattern.addOperation("lastKey", 30);
        pattern.addOperation("subMap", 20);
        pattern.addOperation("remove", 20);
        
        auditAll(pattern);
    }
    
    /**
     * 測試 Heap 場景
     */
    private static void testHeapScenario() {
        System.out.println("\n--- 測試 4: Heap 場景 (優先權) ---");
        
        OperationPattern pattern = new OperationPattern("優先權佇列場景", 
            "需要快速取得最高優先權元素");
        pattern.addOperation("offer", 80);
        pattern.addOperation("poll", 60);
        pattern.addOperation("peek", 100);
        pattern.addOperation("size", 30);
        
        auditAll(pattern);
    }
    
    /**
     * 測試 Hash Table 場景
     */
    private static void testHashTableScenario() {
        System.out.println("\n--- 測試 5: Hash Table 場景 (快速查詢) ---");
        
        OperationPattern pattern = new OperationPattern("快速鍵值查詢場景", 
            "需要透過鍵快速查詢和更新");
        pattern.addOperation("put", 80);
        pattern.addOperation("get", 200);
        pattern.addOperation("contains", 50);
        pattern.addOperation("remove", 30);
        pattern.addOperation("keySet", 10);
        
        auditAll(pattern);
    }
    
    /**
     * 測試 Graph 場景
     */
    private static void testGraphScenario() {
        System.out.println("\n--- 測試 6: Graph 場景 (關係操作) ---");
        
        OperationPattern pattern = new OperationPattern("圖關係操作場景", 
            "需要處理節點間的關係和遍歷");
        pattern.addOperation("addEdge", 50);
        pattern.addOperation("getNeighbors", 100);
        pattern.addOperation("BFS", 30);
        pattern.addOperation("DFS", 20);
        pattern.addOperation("addVertex", 40);
        
        auditAll(pattern);
    }
    
    /**
     * 測試混合場景
     */
    private static void testMixedScenario() {
        System.out.println("\n--- 測試 7: 混合場景 (多種操作) ---");
        
        OperationPattern pattern = new OperationPattern("混合操作場景", 
            "包含多種不同類型的操作");
        pattern.addOperation("add", 50);
        pattern.addOperation("get(index)", 30);
        pattern.addOperation("offer", 40);
        pattern.addOperation("poll", 30);
        pattern.addOperation("peek", 20);
        pattern.addOperation("put", 30);
        pattern.addOperation("get", 40);
        pattern.addOperation("contains", 20);
        pattern.addOperation("getNeighbors", 10);
        
        auditAll(pattern);
    }
}