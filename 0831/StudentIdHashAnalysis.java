import java.util.*;

/**
 * 學號碰撞分析系統
 * 分析不同桶數量下的雜湊碰撞情況
 */
public class StudentIdHashAnalysis {
    
    /**
     * 分析結果類別
     */
    public static class AnalysisResult {
        public int bucketCount;
        public int totalStudents;
        public int[] bucketSizes;
        public int totalCollisions;
        public int maxChainLength;
        public double averageChainLength;
        public double collisionRate;
        public int emptyBuckets;
        public int maxOccupiedBucket;
        public int minOccupiedBucket;
        public Map<Integer, List<Integer>> bucketContent;
        
        public AnalysisResult(int bucketCount) {
            this.bucketCount = bucketCount;
            this.bucketSizes = new int[bucketCount];
            this.bucketContent = new HashMap<>();
            for (int i = 0; i < bucketCount; i++) {
                bucketContent.put(i, new ArrayList<>());
            }
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== 分析結果 (桶數: ").append(bucketCount).append(") ===\n");
            sb.append("學生總數: ").append(totalStudents).append("\n");
            sb.append("桶數量: ").append(bucketCount).append("\n");
            sb.append("總碰撞次數: ").append(totalCollisions).append("\n");
            sb.append("最大鏈結長度: ").append(maxChainLength).append("\n");
            sb.append("平均鏈結長度: ").append(String.format("%.2f", averageChainLength)).append("\n");
            sb.append("碰撞率: ").append(String.format("%.2f%%", collisionRate * 100)).append("\n");
            sb.append("空桶數量: ").append(emptyBuckets).append("\n");
            sb.append("最大佔用桶索引: ").append(maxOccupiedBucket).append("\n");
            sb.append("最小佔用桶索引: ").append(minOccupiedBucket).append("\n");
            
            // 顯示每個桶的分佈
            sb.append("\n桶分佈:\n");
            sb.append("桶索引 | 學生數 | 學號清單\n");
            sb.append("-------|--------|------------------------------\n");
            
            for (int i = 0; i < bucketCount; i++) {
                List<Integer> students = bucketContent.get(i);
                String ids = students.isEmpty() ? "空" : students.toString();
                sb.append(String.format("%6d | %6d | %s%n", i, students.size(), ids));
            }
            
            return sb.toString();
        }
        
        /**
         * 簡短摘要
         */
        public String getSummary() {
            return String.format("桶數=%d | 總碰撞=%d | 最大鏈=%d | 平均=%.2f | 空桶=%d | 碰撞率=%.2f%%",
                               bucketCount, totalCollisions, maxChainLength, 
                               averageChainLength, emptyBuckets, collisionRate * 100);
        }
    }
    
    /**
     * 雜湊函數：將學號映射到桶索引
     * @param studentId 學號
     * @param bucketCount 桶數量
     * @return 桶索引
     */
    private static int hash(int studentId, int bucketCount) {
        return Math.abs(Integer.hashCode(studentId)) % bucketCount;
    }
    
    /**
     * 分析學號分佈
     * @param studentIds 學號陣列
     * @param bucketCount 桶數量
     * @return 分析結果
     */
    public static AnalysisResult analyze(int[] studentIds, int bucketCount) {
        if (studentIds == null || studentIds.length == 0) {
            throw new IllegalArgumentException("學號陣列不能為空");
        }
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("桶數量必須大於 0");
        }
        
        AnalysisResult result = new AnalysisResult(bucketCount);
        result.totalStudents = studentIds.length;
        
        // 分配到各個桶
        for (int studentId : studentIds) {
            int bucketIndex = hash(studentId, bucketCount);
            result.bucketSizes[bucketIndex]++;
            result.bucketContent.get(bucketIndex).add(studentId);
        }
        
        // 計算統計資訊
        int maxSize = 0;
        int minSize = Integer.MAX_VALUE;
        int emptyCount = 0;
        int maxOccupied = -1;
        int minOccupied = Integer.MAX_VALUE;
        int totalCollisions = 0;
        
        for (int i = 0; i < bucketCount; i++) {
            int size = result.bucketSizes[i];
            
            if (size > maxSize) maxSize = size;
            if (size < minSize) minSize = size;
            if (size == 0) emptyCount++;
            
            // 碰撞次數 = 桶中元素數量 - 1 (如果有元素)
            if (size > 0) {
                totalCollisions += (size - 1);
                if (i > maxOccupied) maxOccupied = i;
                if (i < minOccupied) minOccupied = i;
            }
        }
        
        result.totalCollisions = totalCollisions;
        result.maxChainLength = maxSize;
        result.averageChainLength = (double) result.totalStudents / bucketCount;
        result.collisionRate = (double) totalCollisions / result.totalStudents;
        result.emptyBuckets = emptyCount;
        result.maxOccupiedBucket = maxOccupied;
        result.minOccupiedBucket = minOccupied == Integer.MAX_VALUE ? -1 : minOccupied;
        
        return result;
    }
    
    /**
     * 比較兩個不同桶數的結果
     * @param studentIds 學號陣列
     * @param bucketCount1 第一個桶數
     * @param bucketCount2 第二個桶數
     */
    public static void compareAnalysis(int[] studentIds, int bucketCount1, int bucketCount2) {
        System.out.println("\n=== 比較分析 ===");
        System.out.println("學號總數: " + studentIds.length);
        System.out.println("比較桶數: " + bucketCount1 + " vs " + bucketCount2);
        System.out.println("=" .repeat(60));
        
        AnalysisResult result1 = analyze(studentIds, bucketCount1);
        AnalysisResult result2 = analyze(studentIds, bucketCount2);
        
        // 比較表格
        System.out.println("\n指標                | " + String.format("桶數 %-6d | 桶數 %-6d | 差異", bucketCount1, bucketCount2));
        System.out.println("-------------------|----------|----------|----------");
        System.out.println(String.format("總碰撞次數         | %8d | %8d | %+8d", 
                                        result1.totalCollisions, 
                                        result2.totalCollisions, 
                                        result2.totalCollisions - result1.totalCollisions));
        System.out.println(String.format("最大鏈結長度       | %8d | %8d | %+8d",
                                        result1.maxChainLength,
                                        result2.maxChainLength,
                                        result2.maxChainLength - result1.maxChainLength));
        System.out.println(String.format("平均鏈結長度       | %8.2f | %8.2f | %+8.2f",
                                        result1.averageChainLength,
                                        result2.averageChainLength,
                                        result2.averageChainLength - result1.averageChainLength));
        System.out.println(String.format("碰撞率            | %7.2f%% | %7.2f%% | %+7.2f%%",
                                        result1.collisionRate * 100,
                                        result2.collisionRate * 100,
                                        (result2.collisionRate - result1.collisionRate) * 100));
        System.out.println(String.format("空桶數量          | %8d | %8d | %+8d",
                                        result1.emptyBuckets,
                                        result2.emptyBuckets,
                                        result2.emptyBuckets - result1.emptyBuckets));
        
        // 顯示各桶分佈圖
        System.out.println("\n桶分佈比較 (直方圖):");
        System.out.println("桶索引 | " + String.format("桶數 %-4d", bucketCount1) + " | " + 
                          String.format("桶數 %-4d", bucketCount2));
        System.out.println("-------|--------|--------");
        
        int maxBuckets = Math.max(bucketCount1, bucketCount2);
        for (int i = 0; i < maxBuckets; i++) {
            int size1 = (i < bucketCount1) ? result1.bucketSizes[i] : 0;
            int size2 = (i < bucketCount2) ? result2.bucketSizes[i] : 0;
            
            if (size1 == 0 && size2 == 0 && i > Math.min(bucketCount1, bucketCount2) / 2) {
                // 跳過後面的空桶以節省空間
                if (i > maxBuckets - 5) {
                    System.out.printf("%6d | %6d | %6d%n", i, size1, size2);
                }
                continue;
            }
            
            String bar1 = "█".repeat(Math.min(size1, 20));
            String bar2 = "█".repeat(Math.min(size2, 20));
            System.out.printf("%6d | %-6s | %-6s%n", i, 
                             size1 > 0 ? size1 + " " + bar1 : "0",
                             size2 > 0 ? size2 + " " + bar2 : "0");
        }
        
        // 摘要比較
        System.out.println("\n摘要比較:");
        System.out.println("  桶數 " + bucketCount1 + ": " + result1.getSummary());
        System.out.println("  桶數 " + bucketCount2 + ": " + result2.getSummary());
        System.out.println("  結論: " + getConclusion(result1, result2));
    }
    
    /**
     * 根據分析結果得出結論
     */
    private static String getConclusion(AnalysisResult r1, AnalysisResult r2) {
        StringBuilder conclusion = new StringBuilder();
        
        if (r1.totalCollisions < r2.totalCollisions) {
            conclusion.append("桶數 ").append(r1.bucketCount)
                     .append(" 的碰撞較少 (較佳)");
        } else if (r1.totalCollisions > r2.totalCollisions) {
            conclusion.append("桶數 ").append(r2.bucketCount)
                     .append(" 的碰撞較少 (較佳)");
        } else {
            conclusion.append("兩者碰撞次數相同");
        }
        
        conclusion.append("，");
        
        if (r1.maxChainLength < r2.maxChainLength) {
            conclusion.append("桶數 ").append(r1.bucketCount)
                     .append(" 的最大鏈結較短 (較均勻)");
        } else if (r1.maxChainLength > r2.maxChainLength) {
            conclusion.append("桶數 ").append(r2.bucketCount)
                     .append(" 的最大鏈結較短 (較均勻)");
        } else {
            conclusion.append("兩者最大鏈結相同");
        }
        
        return conclusion.toString();
    }
    
    /**
     * 生成詳細的分佈圖
     */
    public static void printDistributionChart(int[] studentIds, int bucketCount, String title) {
        System.out.println("\n=== " + title + " ===");
        AnalysisResult result = analyze(studentIds, bucketCount);
        
        // 顯示統計摘要
        System.out.println(result.getSummary());
        
        // 顯示詳細分佈
        System.out.println("\n詳細分佈:");
        System.out.println("桶索引 | 學生數 | 比例 | 直方圖");
        System.out.println("-------|--------|------|--------");
        
        int maxSize = result.maxChainLength;
        for (int i = 0; i < bucketCount; i++) {
            int size = result.bucketSizes[i];
            double percentage = (double) size / result.totalStudents * 100;
            int barLength = maxSize > 0 ? (size * 40 / maxSize) : 0;
            String bar = "█".repeat(barLength);
            
            System.out.printf("%6d | %6d | %5.1f%% | %s%n", 
                             i, size, percentage, bar);
        }
        
        System.out.println("\n" + result.toString());
    }
    
    /**
     * 生成測試學號
     */
    private static int[] generateStudentIds(int count) {
        Random random = new Random(42);
        int[] ids = new int[count];
        Set<Integer> usedIds = new HashSet<>();
        
        for (int i = 0; i < count; i++) {
            int id;
            do {
                // 生成 8 位數學號 (1xxxxxxx)
                id = 10000000 + random.nextInt(90000000);
            } while (usedIds.contains(id));
            usedIds.add(id);
            ids[i] = id;
        }
        
        return ids;
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 學號碰撞分析系統 ===\n");
        
        // 測試 1：基本分析
        testBasicAnalysis();
        
        // 測試 2：不同桶數比較
        testComparison();
        
        // 測試 3：多組桶數比較
        testMultipleBuckets();
        
        // 測試 4：實際學號範例
        testRealStudentIds();
        
        // 測試 5：邊界情況
        testEdgeCases();
        
        // 測試 6：完整分析報告
        testFullReport();
    }
    
    /**
     * 測試基本分析
     */
    private static void testBasicAnalysis() {
        System.out.println("--- 測試 1: 基本分析 ---");
        
        int[] studentIds = {20241001, 20241002, 20241003, 20241004, 20241005,
                           20241006, 20241007, 20241008, 20241009, 20241010,
                           20241011, 20241012, 20241013, 20241014, 20241015};
        
        System.out.println("學號清單: " + Arrays.toString(studentIds));
        System.out.println("學號總數: " + studentIds.length);
        
        // 分析不同桶數
        printDistributionChart(studentIds, 5, "桶數 = 5");
        printDistributionChart(studentIds, 10, "桶數 = 10");
        
        compareAnalysis(studentIds, 5, 10);
    }
    
    /**
     * 測試不同桶數比較
     */
    private static void testComparison() {
        System.out.println("\n--- 測試 2: 不同桶數比較 ---");
        
        int[] studentIds = generateStudentIds(30);
        System.out.println("產生的 30 個學號:");
        for (int i = 0; i < studentIds.length; i++) {
            System.out.print(studentIds[i] + " ");
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println("\n");
        
        compareAnalysis(studentIds, 5, 10);
        compareAnalysis(studentIds, 10, 20);
        compareAnalysis(studentIds, 20, 50);
    }
    
    /**
     * 測試多組桶數
     */
    private static void testMultipleBuckets() {
        System.out.println("\n--- 測試 3: 多組桶數比較 ---");
        
        int[] studentIds = generateStudentIds(50);
        int[] bucketSizes = {5, 10, 20, 30, 50, 100};
        
        System.out.println("學號總數: " + studentIds.length);
        System.out.println("\n不同桶數的比較:");
        System.out.println("桶數 | 總碰撞 | 最大鏈 | 平均鏈 | 空桶數 | 碰撞率");
        System.out.println("-----|--------|--------|--------|--------|--------");
        
        for (int bucketCount : bucketSizes) {
            AnalysisResult result = analyze(studentIds, bucketCount);
            System.out.printf("%4d | %6d | %6d | %6.2f | %6d | %6.2f%%%n",
                             bucketCount,
                             result.totalCollisions,
                             result.maxChainLength,
                             result.averageChainLength,
                             result.emptyBuckets,
                             result.collisionRate * 100);
        }
        
        // 找出最佳桶數
        System.out.println("\n最佳桶數分析:");
        AnalysisResult best = null;
        int bestBucketCount = 0;
        double bestScore = Double.MAX_VALUE;
        
        for (int bucketCount : bucketSizes) {
            AnalysisResult result = analyze(studentIds, bucketCount);
            // 綜合評分：碰撞率 + 空桶率
            double score = result.collisionRate + (double) result.emptyBuckets / bucketCount;
            if (score < bestScore) {
                bestScore = score;
                best = result;
                bestBucketCount = bucketCount;
            }
        }
        
        if (best != null) {
            System.out.println("  最佳桶數: " + bestBucketCount);
            System.out.println("  該桶數的碰撞率: " + String.format("%.2f%%", best.collisionRate * 100));
            System.out.println("  該桶數的空桶率: " + String.format("%.2f%%", 
                            (double) best.emptyBuckets / best.bucketCount * 100));
        }
    }
    
    /**
     * 測試實際學號範例
     */
    private static void testRealStudentIds() {
        System.out.println("\n--- 測試 4: 實際學號範例 ---");
        
        // 模擬實際學號 (不同系所的學號)
        int[] studentIds = {
            20241001, 20241002, 20241003, 20241004, 20241005,
            20242001, 20242002, 20242003, 20242004, 20242005,
            20243001, 20243002, 20243003, 20243004, 20243005,
            20244001, 20244002, 20244003, 20244004, 20244005,
            20245001, 20245002, 20245003, 20245004, 20245005,
            20246001, 20246002, 20246003, 20246004, 20246005,
            20247001, 20247002, 20247003, 20247004, 20247005,
            20248001, 20248002, 20248003, 20248004, 20248005
        };
        
        System.out.println("實際學號範例 (40 位學生):");
        for (int i = 0; i < studentIds.length; i++) {
            System.out.print(studentIds[i] + " ");
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println("\n");
        
        // 比較不同桶數
        System.out.println("分析不同桶數對實際學號的影響:");
        compareAnalysis(studentIds, 8, 16);
        compareAnalysis(studentIds, 10, 20);
        
        // 顯示最佳桶數
        System.out.println("\n尋找最佳桶數:");
        int[] testBuckets = {4, 8, 12, 16, 20, 24, 32, 40};
        int bestBucket = 4;
        double bestCollisionRate = 1.0;
        
        for (int b : testBuckets) {
            AnalysisResult result = analyze(studentIds, b);
            System.out.printf("  桶數 %2d: 碰撞率 = %6.2f%%, 最大鏈 = %2d%n",
                             b, result.collisionRate * 100, result.maxChainLength);
            if (result.collisionRate < bestCollisionRate) {
                bestCollisionRate = result.collisionRate;
                bestBucket = b;
            }
        }
        
        System.out.println("\n  最佳桶數: " + bestBucket);
        System.out.println("  建議: 使用桶數 " + bestBucket + " 可獲得最低碰撞率");
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("\n--- 測試 5: 邊界情況測試 ---");
        
        // 測試 5.1: 最小學號
        System.out.println("測試 5.1: 最小學號");
        int[] minIds = {20240001, 20240002};
        printDistributionChart(minIds, 3, "最小學號分析");
        
        // 測試 5.2: 所有學號相同 (極端情況)
        System.out.println("\n測試 5.2: 所有學號相同 (極端情況)");
        int[] sameIds = new int[10];
        Arrays.fill(sameIds, 20240001);
        printDistributionChart(sameIds, 5, "相同學號分析");
        
        // 測試 5.3: 連續學號
        System.out.println("\n測試 5.3: 連續學號");
        int[] consecutiveIds = new int[15];
        for (int i = 0; i < 15; i++) {
            consecutiveIds[i] = 20240001 + i;
        }
        printDistributionChart(consecutiveIds, 5, "連續學號分析");
        
        // 測試 5.4: 隨機學號
        System.out.println("\n測試 5.4: 隨機學號");
        int[] randomIds = generateStudentIds(8);
        System.out.println("隨機學號: " + Arrays.toString(randomIds));
        printDistributionChart(randomIds, 4, "隨機學號分析");
    }
    
    /**
     * 測試完整分析報告
     */
    private static void testFullReport() {
        System.out.println("\n--- 測試 6: 完整分析報告 ---");
        
        // 產生 25 個學號
        int[] studentIds = generateStudentIds(25);
        
        System.out.println("學號清單 (25 位學生):");
        for (int i = 0; i < studentIds.length; i++) {
            System.out.print(studentIds[i] + " ");
            if ((i + 1) % 5 == 0) System.out.println();
        }
        System.out.println("\n");
        
        // 完整分析
        int bucketCount = 10;
        AnalysisResult result = analyze(studentIds, bucketCount);
        
        System.out.println(result.toString());
        
        // 額外統計資訊
        System.out.println("\n=== 額外統計 ===");
        System.out.println("桶數: " + bucketCount);
        
        // 計算標準差
        double mean = result.averageChainLength;
        double sumSquaredDiff = 0;
        for (int size : result.bucketSizes) {
            sumSquaredDiff += Math.pow(size - mean, 2);
        }
        double variance = sumSquaredDiff / bucketCount;
        double stdDev = Math.sqrt(variance);
        System.out.println("標準差: " + String.format("%.2f", stdDev));
        
        // 判斷分佈均勻度
        String uniformity;
        if (stdDev < 1.0) {
            uniformity = "非常均勻";
        } else if (stdDev < 2.0) {
            uniformity = "均勻";
        } else if (stdDev < 3.0) {
            uniformity = "尚可";
        } else {
            uniformity = "不均勻 (建議調整桶數)";
        }
        System.out.println("分佈均勻度: " + uniformity);
        
        // 碰撞分析建議
        System.out.println("\n=== 建議 ===");
        if (result.collisionRate < 0.1) {
            System.out.println("✅ 碰撞率低，分佈良好");
        } else if (result.collisionRate < 0.2) {
            System.out.println("⚠️  碰撞率中等，可考慮增加桶數");
        } else {
            System.out.println("❌ 碰撞率高，建議大幅增加桶數");
        }
        
        if (result.emptyBuckets > bucketCount * 0.3) {
            System.out.println("⚠️  空桶過多，建議減少桶數或調整雜湊函數");
        }
    }
}