import java.util.*;

/**
 * 興趣集合比較
 * 計算兩個興趣集合的並集、交集、第一專屬和第三專屬
 * 不修改輸入的 Set
 */
public class InterestSetComparison {
    
    /**
     * 計算兩個集合的並集 (Union)
     * @param set1 第一個集合
     * @param set2 第二個集合
     * @return 並集結果
     */
    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        // 驗證輸入
        if (set1 == null || set2 == null) {
            throw new IllegalArgumentException("集合不能為 null");
        }
        
        // 建立新集合 (不修改原集合)
        Set<T> result = new HashSet<>(set1);
        result.addAll(set2);
        return result;
    }
    
    /**
     * 計算兩個集合的交集 (Intersection)
     * @param set1 第一個集合
     * @param set2 第二個集合
     * @return 交集結果
     */
    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        // 驗證輸入
        if (set1 == null || set2 == null) {
            throw new IllegalArgumentException("集合不能為 null");
        }
        
        // 建立新集合 (不修改原集合)
        Set<T> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }
    
    /**
     * 計算第一集合專屬 (First-Only): 在 set1 但不在 set2 中的元素
     * @param set1 第一個集合
     * @param set2 第二個集合
     * @return 第一集合專屬元素
     */
    public static <T> Set<T> firstOnly(Set<T> set1, Set<T> set2) {
        // 驗證輸入
        if (set1 == null || set2 == null) {
            throw new IllegalArgumentException("集合不能為 null");
        }
        
        // 建立新集合 (不修改原集合)
        Set<T> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }
    
    /**
     * 計算第二集合專屬 (Secondary-Only): 在 set2 但不在 set1 中的元素
     * @param set1 第一個集合
     * @param set2 第二個集合
     * @return 第二集合專屬元素
     */
    public static <T> Set<T> secondaryOnly(Set<T> set1, Set<T> set2) {
        // 驗證輸入
        if (set1 == null || set2 == null) {
            throw new IllegalArgumentException("集合不能為 null");
        }
        
        // 建立新集合 (不修改原集合)
        Set<T> result = new HashSet<>(set2);
        result.removeAll(set1);
        return result;
    }
    
    /**
     * 計算對稱差集 (Symmetric Difference): 在任一個集合但不在交集中的元素
     * @param set1 第一個集合
     * @param set2 第二個集合
     * @return 對稱差集結果
     */
    public static <T> Set<T> symmetricDifference(Set<T> set1, Set<T> set2) {
        // 驗證輸入
        if (set1 == null || set2 == null) {
            throw new IllegalArgumentException("集合不能為 null");
        }
        
        // 建立新集合 (不修改原集合)
        Set<T> result = new HashSet<>(set1);
        Set<T> temp = new HashSet<>(set2);
        result.removeAll(set2);
        temp.removeAll(set1);
        result.addAll(temp);
        return result;
    }
    
    /**
     * 生成完整的比較報告
     * @param set1 第一個集合
     * @param set2 第二個集合
     * @param setName1 第一個集合的名稱
     * @param setName2 第二個集合的名稱
     * @return 格式化的報告字串
     */
    public static <T> String generateReport(Set<T> set1, Set<T> set2, 
                                           String setName1, String setName2) {
        // 驗證輸入
        if (set1 == null || set2 == null) {
            throw new IllegalArgumentException("集合不能為 null");
        }
        
        StringBuilder report = new StringBuilder();
        
        report.append("\n=== 興趣集合比較報告 ===\n");
        report.append("\n📊 基本資訊:\n");
        report.append(String.format("  %s: %d 個興趣\n", setName1, set1.size()));
        report.append(String.format("  %s: %d 個興趣\n", setName2, set2.size()));
        
        // 顯示排序後的集合內容
        report.append("\n📋 集合內容:\n");
        report.append(String.format("  %s: %s\n", setName1, sortedSetToString(set1)));
        report.append(String.format("  %s: %s\n", setName2, sortedSetToString(set2)));
        
        // 計算各種集合操作
        Set<T> union = union(set1, set2);
        Set<T> intersection = intersection(set1, set2);
        Set<T> firstOnly = firstOnly(set1, set2);
        Set<T> secondOnly = secondaryOnly(set1, set2);
        Set<T> symDiff = symmetricDifference(set1, set2);
        
        report.append("\n📈 比較結果:\n");
        report.append(String.format("  並集 (Union): %d 個興趣\n", union.size()));
        report.append(String.format("  交集 (Intersection): %d 個興趣\n", intersection.size()));
        report.append(String.format("  第一專屬 (First-Only): %d 個興趣\n", firstOnly.size()));
        report.append(String.format("  第二專屬 (Secondary-Only): %d 個興趣\n", secondOnly.size()));
        report.append(String.format("  對稱差集 (Symmetric Difference): %d 個興趣\n", symDiff.size()));
        
        // 顯示詳細內容
        report.append("\n📝 詳細內容:\n");
        
        if (!intersection.isEmpty()) {
            report.append(String.format("  共同興趣: %s\n", sortedSetToString(intersection)));
        } else {
            report.append("  共同興趣: (無)\n");
        }
        
        if (!firstOnly.isEmpty()) {
            report.append(String.format("  %s 專屬: %s\n", setName1, sortedSetToString(firstOnly)));
        } else {
            report.append(String.format("  %s 專屬: (無)\n", setName1));
        }
        
        if (!secondOnly.isEmpty()) {
            report.append(String.format("  %s 專屬: %s\n", setName2, sortedSetToString(secondOnly)));
        } else {
            report.append(String.format("  %s 專屬: (無)\n", setName2));
        }
        
        if (!union.isEmpty()) {
            report.append(String.format("  所有興趣總和: %s\n", sortedSetToString(union)));
        }
        
        // 相似度分析
        double similarity = calculateSimilarity(set1, set2);
        report.append(String.format("\n🔍 相似度分析:\n"));
        report.append(String.format("  興趣相似度: %.2f%%\n", similarity * 100));
        report.append(String.format("  相似度評級: %s\n", getSimilarityRating(similarity)));
        
        return report.toString();
    }
    
    /**
     * 將集合排序後轉為字串
     */
    private static <T> String sortedSetToString(Set<T> set) {
        if (set == null || set.isEmpty()) {
            return "[]";
        }
        
        List<T> sorted = new ArrayList<>(set);
        Collections.sort(sorted, (a, b) -> {
            if (a instanceof Comparable && b instanceof Comparable) {
                return ((Comparable<T>) a).compareTo(b);
            }
            return a.toString().compareTo(b.toString());
        });
        
        return sorted.toString();
    }
    
    /**
     * 計算兩個集合的相似度 (Jaccard 相似度)
     */
    private static <T> double calculateSimilarity(Set<T> set1, Set<T> set2) {
        if (set1.isEmpty() && set2.isEmpty()) {
            return 1.0;
        }
        
        Set<T> intersection = intersection(set1, set2);
        Set<T> union = union(set1, set2);
        
        if (union.isEmpty()) {
            return 0.0;
        }
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * 根據相似度給予評級
     */
    private static String getSimilarityRating(double similarity) {
        if (similarity >= 0.8) {
            return "🌟 高度相似 (非常契合)";
        } else if (similarity >= 0.6) {
            return "👍 中度相似 (有一定共同興趣)";
        } else if (similarity >= 0.4) {
            return "📊 普通相似 (部分興趣重疊)";
        } else if (similarity >= 0.2) {
            return "🔍 低度相似 (興趣較為不同)";
        } else {
            return "❌ 極低相似 (興趣幾乎完全不同)";
        }
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 共同興趣比較系統測試 ===\n");
        
        // 測試 1：基本興趣比較
        testBasicInterestComparison();
        
        // 測試 2：數字集合
        testNumberSetComparison();
        
        // 測試 3：邊界情況
        testEdgeCases();
        
        // 測試 4：實際應用場景
        testRealWorldScenario();
        
        // 測試 5：多種興趣類型
        testMultipleInterestTypes();
    }
    
    /**
     * 測試基本興趣比較
     */
    private static void testBasicInterestComparison() {
        System.out.println("--- 測試 1: 基本興趣比較 ---");
        
        // 建立兩個人的興趣集合
        Set<String> person1 = new HashSet<>(Arrays.asList(
            "閱讀", "音樂", "運動", "旅遊", "攝影", "繪畫"
        ));
        
        Set<String> person2 = new HashSet<>(Arrays.asList(
            "音樂", "運動", "電影", "美食", "旅遊", "程式設計"
        ));
        
        // 顯示報告
        System.out.println(InterestSetComparison.generateReport(
            person1, person2, "張小明", "李小華"));
        
        // 驗證原始集合未被修改
        System.out.println("\n✅ 驗證原始集合未被修改:");
        System.out.println("  張小明: " + person1);
        System.out.println("  李小華: " + person2);
        System.out.println();
    }
    
    /**
     * 測試數字集合
     */
    private static void testNumberSetComparison() {
        System.out.println("--- 測試 2: 數字集合比較 ---");
        
        Set<Integer> setA = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        Set<Integer> setB = new HashSet<>(Arrays.asList(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
        
        System.out.println("集合 A: " + setA);
        System.out.println("集合 B: " + setB);
        System.out.println();
        
        // 計算各項結果
        Set<Integer> union = union(setA, setB);
        Set<Integer> intersection = intersection(setA, setB);
        Set<Integer> firstOnly = firstOnly(setA, setB);
        Set<Integer> secondOnly = secondaryOnly(setA, setB);
        Set<Integer> symDiff = symmetricDifference(setA, setB);
        
        System.out.println("計算結果:");
        System.out.println("  並集: " + union);
        System.out.println("  交集: " + intersection);
        System.out.println("  第一專屬 (A專屬): " + firstOnly);
        System.out.println("  第二專屬 (B專屬): " + secondOnly);
        System.out.println("  對稱差集: " + symDiff);
        System.out.println();
        
        // 驗證原始集合未被修改
        System.out.println("驗證原始集合未被修改:");
        System.out.println("  集合 A: " + setA);
        System.out.println("  集合 B: " + setB);
        System.out.println();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 3: 邊界情況 ---");
        
        // 測試 3.1: 空集合
        System.out.println("測試 3.1: 空集合");
        Set<String> emptySet1 = new HashSet<>();
        Set<String> emptySet2 = new HashSet<>();
        
        System.out.println("  並集: " + union(emptySet1, emptySet2));
        System.out.println("  交集: " + intersection(emptySet1, emptySet2));
        System.out.println("  第一專屬: " + firstOnly(emptySet1, emptySet2));
        System.out.println("  第二專屬: " + secondaryOnly(emptySet1, emptySet2));
        System.out.println();
        
        // 測試 3.2: 完全相同集合
        System.out.println("測試 3.2: 完全相同集合");
        Set<String> sameSet1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> sameSet2 = new HashSet<>(Arrays.asList("A", "B", "C"));
        
        System.out.println("  並集: " + union(sameSet1, sameSet2));
        System.out.println("  交集: " + intersection(sameSet1, sameSet2));
        System.out.println("  第一專屬: " + firstOnly(sameSet1, sameSet2));
        System.out.println("  第二專屬: " + secondaryOnly(sameSet1, sameSet2));
        System.out.println("  對稱差集: " + symmetricDifference(sameSet1, sameSet2));
        System.out.println("  相似度: " + String.format("%.2f%%", 
                          calculateSimilarity(sameSet1, sameSet2) * 100));
        System.out.println();
        
        // 測試 3.3: 完全不同的集合
        System.out.println("測試 3.3: 完全不同的集合");
        Set<String> diffSet1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<String> diffSet2 = new HashSet<>(Arrays.asList("X", "Y", "Z"));
        
        System.out.println("  並集: " + union(diffSet1, diffSet2));
        System.out.println("  交集: " + intersection(diffSet1, diffSet2));
        System.out.println("  第一專屬: " + firstOnly(diffSet1, diffSet2));
        System.out.println("  第二專屬: " + secondaryOnly(diffSet1, diffSet2));
        System.out.println("  對稱差集: " + symmetricDifference(diffSet1, diffSet2));
        System.out.println("  相似度: " + String.format("%.2f%%", 
                          calculateSimilarity(diffSet1, diffSet2) * 100));
        System.out.println();
        
        // 測試 3.4: null 處理
        System.out.println("測試 3.4: null 處理");
        try {
            union(null, new HashSet<>());
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲 null 例外: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 4: 實際應用場景 ---");
        System.out.println("📱 社交媒體共同興趣推薦系統");
        
        // 模擬用戶的興趣標籤
        Set<String> user1 = new HashSet<>(Arrays.asList(
            "Python", "機器學習", "深度學習", "資料科學", "大數據",
            "Java", "演算法", "資料結構", "雲端運算"
        ));
        
        Set<String> user2 = new HashSet<>(Arrays.asList(
            "Java", "Spring Boot", "微服務", "Docker", "Kubernetes",
            "雲端運算", "DevOps", "Python", "Git"
        ));
        
        Set<String> user3 = new HashSet<>(Arrays.asList(
            "攝影", "旅遊", "美食", "音樂", "電影",
            "閱讀", "運動", "繪畫", "程式設計"
        ));
        
        Set<String> user4 = new HashSet<>(Arrays.asList(
            "旅遊", "美食", "音樂", "攝影", "跑步",
            "游泳", "閱讀", "電影"
        ));
        
        // 用戶1 vs 用戶2 (技術興趣相似)
        System.out.println("\n👤 用戶A vs 用戶B (技術背景):");
        System.out.println(InterestSetComparison.generateReport(
            user1, user2, "用戶A (技術專家)", "用戶B (技術專家)"));
        
        // 用戶1 vs 用戶3 (技術 vs 興趣)
        System.out.println("\n👤 用戶A vs 用戶C (技術 vs 興趣):");
        System.out.println(InterestSetComparison.generateReport(
            user1, user3, "用戶A (技術專家)", "用戶C (生活興趣)"));
        
        // 用戶3 vs 用戶4 (生活興趣相似)
        System.out.println("\n👤 用戶C vs 用戶D (生活興趣):");
        System.out.println(InterestSetComparison.generateReport(
            user3, user4, "用戶C (生活興趣)", "用戶D (生活興趣)"));
        
        // 推薦共同朋友
        System.out.println("\n💡 推薦分析:");
        analyzeRecommendations(user1, user2, "用戶A", "用戶B");
        analyzeRecommendations(user1, user3, "用戶A", "用戶C");
        analyzeRecommendations(user3, user4, "用戶C", "用戶D");
        
        System.out.println();
    }
    
    /**
     * 分析推薦結果
     */
    private static void analyzeRecommendations(Set<String> user1, Set<String> user2,
                                              String name1, String name2) {
        Set<String> common = intersection(user1, user2);
        Set<String> only1 = firstOnly(user1, user2);
        Set<String> only2 = secondaryOnly(user1, user2);
        
        double similarity = calculateSimilarity(user1, user2);
        
        System.out.printf("  %s 與 %s:%n", name1, name2);
        System.out.printf("    共同興趣: %d 個%n", common.size());
        System.out.printf("    相似度: %.1f%%%n", similarity * 100);
        
        if (similarity >= 0.6) {
            System.out.printf("    ✅ 推薦成為朋友! (高度契合)%n");
        } else if (similarity >= 0.3) {
            System.out.printf("    📊 可考慮認識 (有部分共同興趣)%n");
        } else {
            System.out.printf("    💫 興趣較不同，但仍可交流學習%n");
        }
        
        // 如果沒共同興趣，推薦可能感興趣的項目
        if (common.isEmpty() && !only1.isEmpty() && !only2.isEmpty()) {
            System.out.printf("    建議: 可嘗試探索對方的興趣 - %s%n", 
                            only2.size() <= 3 ? only2 : 
                            new ArrayList<>(only2).subList(0, 3) + "...");
        }
    }
    
    /**
     * 測試多種興趣類型
     */
    private static void testMultipleInterestTypes() {
        System.out.println("--- 測試 5: 多種興趣類型 ---");
        
        // 字串興趣
        Set<String> hobbies1 = new HashSet<>(Arrays.asList("閱讀", "音樂", "運動"));
        Set<String> hobbies2 = new HashSet<>(Arrays.asList("音樂", "電影", "美食"));
        
        System.out.println("1. 字串興趣:");
        System.out.println("   共同興趣: " + intersection(hobbies1, hobbies2));
        System.out.println("   第一專屬: " + firstOnly(hobbies1, hobbies2));
        System.out.println("   第二專屬: " + secondaryOnly(hobbies1, hobbies2));
        System.out.println();
        
        // 整數興趣 (例如：運動編號)
        Set<Integer> sports1 = new HashSet<>(Arrays.asList(1, 3, 5, 7, 9));
        Set<Integer> sports2 = new HashSet<>(Arrays.asList(2, 3, 5, 8, 10));
        
        System.out.println("2. 數字興趣:");
        System.out.println("   共同興趣: " + intersection(sports1, sports2));
        System.out.println("   第一專屬: " + firstOnly(sports1, sports2));
        System.out.println("   第二專屬: " + secondaryOnly(sports1, sports2));
        System.out.println();
        
        // 自訂物件興趣
        class Interest {
            private final String name;
            private final String category;
            
            public Interest(String name, String category) {
                this.name = name;
                this.category = category;
            }
            
            @Override
            public String toString() {
                return name;
            }
            
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Interest that = (Interest) obj;
                return Objects.equals(name, that.name) && 
                       Objects.equals(category, that.category);
            }
            
            @Override
            public int hashCode() {
                return Objects.hash(name, category);
            }
        }
        
        Interest java = new Interest("Java", "程式設計");
        Interest python = new Interest("Python", "程式設計");
        Interest dataScience = new Interest("資料科學", "數據分析");
        Interest machineLearning = new Interest("機器學習", "人工智慧");
        
        Set<Interest> interests1 = new HashSet<>(Arrays.asList(java, python, dataScience));
        Set<Interest> interests2 = new HashSet<>(Arrays.asList(python, dataScience, machineLearning));
        
        System.out.println("3. 自訂物件興趣:");
        System.out.println("   興趣1: " + interests1);
        System.out.println("   興趣2: " + interests2);
        System.out.println("   共同興趣: " + intersection(interests1, interests2));
        System.out.println("   第一專屬: " + firstOnly(interests1, interests2));
        System.out.println("   第二專屬: " + secondaryOnly(interests1, interests2));
        System.out.println("   相似度: " + String.format("%.2f%%", 
                          calculateSimilarity(interests1, interests2) * 100));
        System.out.println();
    }
}