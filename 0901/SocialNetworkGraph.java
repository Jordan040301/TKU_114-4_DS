import java.util.*;

/**
 * 社群網絡圖
 * 使用鄰接表管理用戶和好友關係
 */
public class SocialNetworkGraph {
    
    // 鄰接表：用戶 -> 好友列表
    private Map<String, Set<String>> adjacencyList;
    
    /**
     * 建構子
     */
    public SocialNetworkGraph() {
        this.adjacencyList = new HashMap<>();
    }
    
    /**
     * 新增用戶
     * @param username 用戶名稱
     * @return true 如果成功新增
     */
    public boolean addUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用戶名稱不能為空");
        }
        
        if (adjacencyList.containsKey(username)) {
            System.out.printf("⚠️ 用戶 '%s' 已存在%n", username);
            return false;
        }
        
        adjacencyList.put(username, new HashSet<>());
        System.out.printf("✅ 新增用戶: %s%n", username);
        return true;
    }
    
    /**
     * 新增好友關係 (無向)
     * @param user1 用戶1
     * @param user2 用戶2
     * @return true 如果成功新增
     */
    public boolean addFriendship(String user1, String user2) {
        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("用戶名稱不能為 null");
        }
        
        if (user1.equals(user2)) {
            System.out.println("⚠️ 不能將自己加為好友");
            return false;
        }
        
        if (!adjacencyList.containsKey(user1)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", user1);
            return false;
        }
        
        if (!adjacencyList.containsKey(user2)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", user2);
            return false;
        }
        
        // 檢查是否已是好友
        if (adjacencyList.get(user1).contains(user2)) {
            System.out.printf("⚠️ '%s' 和 '%s' 已是好友%n", user1, user2);
            return false;
        }
        
        // 新增雙向好友關係
        adjacencyList.get(user1).add(user2);
        adjacencyList.get(user2).add(user1);
        
        System.out.printf("✅ 新增好友: %s ↔ %s%n", user1, user2);
        return true;
    }
    
    /**
     * 解除好友關係
     * @param user1 用戶1
     * @param user2 用戶2
     * @return true 如果成功解除
     */
    public boolean removeFriendship(String user1, String user2) {
        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("用戶名稱不能為 null");
        }
        
        if (!adjacencyList.containsKey(user1)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", user1);
            return false;
        }
        
        if (!adjacencyList.containsKey(user2)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", user2);
            return false;
        }
        
        // 檢查是否為好友
        if (!adjacencyList.get(user1).contains(user2)) {
            System.out.printf("⚠️ '%s' 和 '%s' 不是好友%n", user1, user2);
            return false;
        }
        
        // 移除雙向好友關係
        adjacencyList.get(user1).remove(user2);
        adjacencyList.get(user2).remove(user1);
        
        System.out.printf("🗑️ 解除好友: %s ↔ %s%n", user1, user2);
        return true;
    }
    
    /**
     * 查詢用戶的好友列表
     * @param username 用戶名稱
     * @return 好友列表
     */
    public Set<String> getFriends(String username) {
        if (!adjacencyList.containsKey(username)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", username);
            return new HashSet<>();
        }
        
        return new HashSet<>(adjacencyList.get(username));
    }
    
    /**
     * 查詢兩個用戶的共同好友
     * @param user1 用戶1
     * @param user2 用戶2
     * @return 共同好友列表
     */
    public Set<String> getCommonFriends(String user1, String user2) {
        if (!adjacencyList.containsKey(user1)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", user1);
            return new HashSet<>();
        }
        
        if (!adjacencyList.containsKey(user2)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", user2);
            return new HashSet<>();
        }
        
        Set<String> friends1 = adjacencyList.get(user1);
        Set<String> friends2 = adjacencyList.get(user2);
        
        Set<String> common = new HashSet<>(friends1);
        common.retainAll(friends2);
        
        // 排除自己和對方
        common.remove(user1);
        common.remove(user2);
        
        return common;
    }
    
    /**
     * 查詢孤立用戶 (沒有好友的用戶)
     * @return 孤立用戶列表
     */
    public Set<String> getIsolatedUsers() {
        Set<String> isolated = new HashSet<>();
        
        for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
            if (entry.getValue().isEmpty()) {
                isolated.add(entry.getKey());
            }
        }
        
        return isolated;
    }
    
    /**
     * 查詢用戶的度 (好友數量)
     * @param username 用戶名稱
     * @return 好友數量
     */
    public int getFriendCount(String username) {
        if (!adjacencyList.containsKey(username)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", username);
            return -1;
        }
        
        return adjacencyList.get(username).size();
    }
    
    /**
     * 檢查兩個用戶是否為好友
     * @param user1 用戶1
     * @param user2 用戶2
     * @return true 如果是好友
     */
    public boolean areFriends(String user1, String user2) {
        if (!adjacencyList.containsKey(user1) || !adjacencyList.containsKey(user2)) {
            return false;
        }
        
        return adjacencyList.get(user1).contains(user2);
    }
    
    /**
     * 檢查用戶是否存在
     * @param username 用戶名稱
     * @return true 如果存在
     */
    public boolean containsUser(String username) {
        return adjacencyList.containsKey(username);
    }
    
    /**
     * 取得所有用戶
     * @return 用戶集合
     */
    public Set<String> getAllUsers() {
        return new HashSet<>(adjacencyList.keySet());
    }
    
    /**
     * 取得用戶總數
     * @return 用戶數量
     */
    public int getUserCount() {
        return adjacencyList.size();
    }
    
    /**
     * 取得好友關係總數
     * @return 好友關係數量
     */
    public int getFriendshipCount() {
        int count = 0;
        for (Set<String> friends : adjacencyList.values()) {
            count += friends.size();
        }
        return count / 2; // 無向圖，每個關係被計算兩次
    }
    
    /**
     * 刪除用戶及其所有好友關係
     * @param username 用戶名稱
     * @return true 如果成功刪除
     */
    public boolean removeUser(String username) {
        if (!adjacencyList.containsKey(username)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", username);
            return false;
        }
        
        // 從所有好友的列表中移除該用戶
        for (String friend : adjacencyList.get(username)) {
            adjacencyList.get(friend).remove(username);
        }
        
        // 移除該用戶
        adjacencyList.remove(username);
        
        System.out.printf("🗑️ 刪除用戶: %s (及其所有好友關係)%n", username);
        return true;
    }
    
    /**
     * 推薦潛在好友 (共同好友最多的用戶)
     * @param username 用戶名稱
     * @param limit 推薦數量
     * @return 推薦列表
     */
    public List<String> suggestFriends(String username, int limit) {
        if (!adjacencyList.containsKey(username)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", username);
            return new ArrayList<>();
        }
        
        Set<String> existingFriends = adjacencyList.get(username);
        Map<String, Integer> candidateScores = new HashMap<>();
        
        // 計算每個潛在好友的共同好友數
        for (String friend : existingFriends) {
            for (String potentialFriend : adjacencyList.get(friend)) {
                // 排除自己、已是好友的人
                if (potentialFriend.equals(username) || existingFriends.contains(potentialFriend)) {
                    continue;
                }
                
                candidateScores.put(potentialFriend, 
                                   candidateScores.getOrDefault(potentialFriend, 0) + 1);
            }
        }
        
        // 排序並取前 limit 個
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(candidateScores.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, sorted.size()); i++) {
            suggestions.add(sorted.get(i).getKey());
        }
        
        return suggestions;
    }
    
    /**
     * 計算好友推薦評分
     * @param username 用戶名稱
     * @param candidate 候選用戶
     * @return 推薦分數 (共同好友數)
     */
    public int getSuggestionScore(String username, String candidate) {
        if (!adjacencyList.containsKey(username) || !adjacencyList.containsKey(candidate)) {
            return 0;
        }
        
        if (username.equals(candidate) || adjacencyList.get(username).contains(candidate)) {
            return 0;
        }
        
        Set<String> friends1 = adjacencyList.get(username);
        Set<String> friends2 = adjacencyList.get(candidate);
        
        Set<String> common = new HashSet<>(friends1);
        common.retainAll(friends2);
        
        return common.size();
    }
    
    /**
     * 印出網絡統計資訊
     */
    public void printNetworkStats() {
        System.out.println("\n=== 社群網絡統計 ===");
        System.out.printf("用戶總數: %d%n", getUserCount());
        System.out.printf("好友關係總數: %d%n", getFriendshipCount());
        
        if (adjacencyList.isEmpty()) {
            System.out.println("網絡為空");
            return;
        }
        
        // 計算平均好友數
        int totalFriends = 0;
        int maxFriends = 0;
        String maxUser = "";
        int minFriends = Integer.MAX_VALUE;
        String minUser = "";
        
        for (Map.Entry<String, Set<String>> entry : adjacencyList.entrySet()) {
            int count = entry.getValue().size();
            totalFriends += count;
            
            if (count > maxFriends) {
                maxFriends = count;
                maxUser = entry.getKey();
            }
            if (count < minFriends) {
                minFriends = count;
                minUser = entry.getKey();
            }
        }
        
        double avgFriends = (double) totalFriends / adjacencyList.size();
        
        System.out.printf("平均好友數: %.2f%n", avgFriends);
        System.out.printf("最多好友: %s (%d 位好友)%n", maxUser, maxFriends);
        System.out.printf("最少好友: %s (%d 位好友)%n", minUser, minFriends);
        
        // 孤立用戶
        Set<String> isolated = getIsolatedUsers();
        if (!isolated.isEmpty()) {
            System.out.printf("孤立用戶: %d 位%n", isolated.size());
            System.out.println("   " + isolated);
        }
        
        System.out.println();
    }
    
    /**
     * 印出完整網絡結構
     */
    public void printNetwork() {
        System.out.println("\n=== 社群網絡結構 ===");
        
        if (adjacencyList.isEmpty()) {
            System.out.println("網絡為空");
            return;
        }
        
        // 依用戶名稱排序
        List<String> sortedUsers = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(sortedUsers);
        
        for (String user : sortedUsers) {
            Set<String> friends = adjacencyList.get(user);
            System.out.printf("%s → %s%n", user, 
                             friends.isEmpty() ? "無好友" : friends.toString());
        }
        System.out.println();
    }
    
    /**
     * 印出好友詳細資訊
     */
    public void printUserInfo(String username) {
        System.out.println("\n=== 用戶詳細資訊 ===");
        
        if (!adjacencyList.containsKey(username)) {
            System.out.printf("⚠️ 用戶 '%s' 不存在%n", username);
            return;
        }
        
        Set<String> friends = adjacencyList.get(username);
        System.out.printf("用戶: %s%n", username);
        System.out.printf("好友數: %d%n", friends.size());
        System.out.printf("好友列表: %s%n", friends.isEmpty() ? "無" : friends.toString());
        
        // 共同好友推薦
        if (!friends.isEmpty()) {
            System.out.println("\n推薦潛在好友 (基於共同好友):");
            List<String> suggestions = suggestFriends(username, 5);
            if (suggestions.isEmpty()) {
                System.out.println("  目前無推薦");
            } else {
                for (String suggestion : suggestions) {
                    int score = getSuggestionScore(username, suggestion);
                    System.out.printf("  %s (共同好友: %d 人)%n", suggestion, score);
                }
            }
        }
        System.out.println();
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 社群網絡圖測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：共同好友
        testCommonFriends();
        
        // 測試 3：好友推薦
        testFriendRecommendations();
        
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
        
        SocialNetworkGraph network = new SocialNetworkGraph();
        
        // 新增用戶
        System.out.println("新增用戶:");
        network.addUser("Alice");
        network.addUser("Bob");
        network.addUser("Charlie");
        network.addUser("David");
        network.addUser("Eve");
        
        // 新增好友關係
        System.out.println("\n新增好友關係:");
        network.addFriendship("Alice", "Bob");
        network.addFriendship("Alice", "Charlie");
        network.addFriendship("Bob", "Charlie");
        network.addFriendship("Bob", "David");
        network.addFriendship("Charlie", "Eve");
        network.addFriendship("David", "Eve");
        
        network.printNetwork();
        network.printNetworkStats();
        
        // 查詢
        System.out.println("📋 查詢:");
        System.out.println("  getFriends('Alice'): " + network.getFriends("Alice"));
        System.out.println("  getFriends('David'): " + network.getFriends("David"));
        System.out.println("  getFriendCount('Alice'): " + network.getFriendCount("Alice"));
        System.out.println("  areFriends('Alice', 'Bob'): " + network.areFriends("Alice", "Bob"));
        System.out.println("  areFriends('Alice', 'David'): " + network.areFriends("Alice", "David"));
        System.out.println();
    }
    
    /**
     * 測試共同好友
     */
    private static void testCommonFriends() {
        System.out.println("--- 測試 2: 共同好友 ---");
        
        SocialNetworkGraph network = new SocialNetworkGraph();
        
        // 建立測試網絡
        String[] users = {"A", "B", "C", "D", "E", "F", "G"};
        for (String user : users) {
            network.addUser(user);
        }
        
        network.addFriendship("A", "B");
        network.addFriendship("A", "C");
        network.addFriendship("A", "D");
        network.addFriendship("B", "C");
        network.addFriendship("B", "E");
        network.addFriendship("C", "D");
        network.addFriendship("C", "F");
        network.addFriendship("D", "E");
        network.addFriendship("D", "G");
        network.addFriendship("E", "F");
        network.addFriendship("F", "G");
        
        network.printNetwork();
        
        // 查詢共同好友
        System.out.println("\n👥 共同好友查詢:");
        System.out.println("  getCommonFriends('A', 'B'): " + network.getCommonFriends("A", "B"));
        System.out.println("  getCommonFriends('A', 'C'): " + network.getCommonFriends("A", "C"));
        System.out.println("  getCommonFriends('A', 'E'): " + network.getCommonFriends("A", "E"));
        System.out.println("  getCommonFriends('C', 'G'): " + network.getCommonFriends("C", "G"));
        System.out.println("  getCommonFriends('E', 'G'): " + network.getCommonFriends("E", "G"));
        System.out.println();
    }
    
    /**
     * 測試好友推薦
     */
    private static void testFriendRecommendations() {
        System.out.println("--- 測試 3: 好友推薦 ---");
        
        SocialNetworkGraph network = new SocialNetworkGraph();
        
        // 建立測試網絡
        network.addUser("小明");
        network.addUser("小華");
        network.addUser("小美");
        network.addUser("小強");
        network.addUser("小麗");
        network.addUser("小芳");
        network.addUser("小剛");
        
        network.addFriendship("小明", "小華");
        network.addFriendship("小明", "小美");
        network.addFriendship("小明", "小強");
        network.addFriendship("小華", "小美");
        network.addFriendship("小華", "小麗");
        network.addFriendship("小美", "小麗");
        network.addFriendship("小美", "小芳");
        network.addFriendship("小強", "小剛");
        network.addFriendship("小麗", "小芳");
        network.addFriendship("小芳", "小剛");
        
        network.printNetwork();
        
        // 推薦測試
        System.out.println("\n💡 好友推薦:");
        System.out.println("  小明的推薦: " + network.suggestFriends("小明", 3));
        System.out.println("  小華的推薦: " + network.suggestFriends("小華", 3));
        System.out.println("  小強的推薦: " + network.suggestFriends("小強", 3));
        System.out.println("  小芳的推薦: " + network.suggestFriends("小芳", 3));
        
        // 推薦評分
        System.out.println("\n📊 推薦評分:");
        System.out.println("  小明 vs 小麗: " + network.getSuggestionScore("小明", "小麗") + " 個共同好友");
        System.out.println("  小華 vs 小強: " + network.getSuggestionScore("小華", "小強") + " 個共同好友");
        System.out.println("  小美 vs 小剛: " + network.getSuggestionScore("小美", "小剛") + " 個共同好友");
        System.out.println();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 4: 邊界情況 ---");
        
        SocialNetworkGraph network = new SocialNetworkGraph();
        
        // 測試 4.1: 空網絡
        System.out.println("測試 4.1: 空網絡");
        network.printNetwork();
        network.printNetworkStats();
        System.out.println("  getIsolatedUsers(): " + network.getIsolatedUsers());
        System.out.println();
        
        // 測試 4.2: 單一用戶
        System.out.println("測試 4.2: 單一用戶");
        network.addUser("孤獨");
        network.printNetwork();
        network.printNetworkStats();
        System.out.println("  getIsolatedUsers(): " + network.getIsolatedUsers());
        System.out.println();
        
        // 測試 4.3: 自己加自己
        System.out.println("測試 4.3: 自己加自己");
        network.addFriendship("孤獨", "孤獨");
        System.out.println();
        
        // 測試 4.4: 不存在的用戶
        System.out.println("測試 4.4: 不存在的用戶");
        network.addFriendship("不存在", "孤獨");
        network.removeFriendship("不存在", "孤獨");
        network.getFriends("不存在");
        network.removeUser("不存在");
        System.out.println();
        
        // 測試 4.5: 重複操作
        System.out.println("測試 4.5: 重複操作");
        network.addUser("A");
        network.addUser("B");
        network.addFriendship("A", "B");
        network.addFriendship("A", "B");
        network.removeFriendship("A", "B");
        network.removeFriendship("A", "B");
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 5: 實際應用場景 ---");
        System.out.println("📱 社群媒體好友網絡分析");
        
        SocialNetworkGraph facebook = new SocialNetworkGraph();
        
        // 模擬真實用戶
        System.out.println("\n建立用戶群:");
        String[] users = {
            "王小明", "李小華", "張大志", "陳美玲", "林建國",
            "吳美麗", "周志強", "劉佳欣", "黃俊明", "楊雅婷"
        };
        for (String user : users) {
            facebook.addUser(user);
        }
        
        // 模擬好友關係
        System.out.println("\n建立好友關係:");
        String[][] friendships = {
            {"王小明", "李小華"}, {"王小明", "張大志"}, {"王小明", "陳美玲"},
            {"李小華", "陳美玲"}, {"李小華", "林建國"},
            {"張大志", "吳美麗"}, {"張大志", "周志強"},
            {"陳美玲", "吳美麗"}, {"陳美玲", "劉佳欣"},
            {"林建國", "劉佳欣"}, {"林建國", "黃俊明"},
            {"吳美麗", "黃俊明"}, {"吳美麗", "楊雅婷"},
            {"周志強", "劉佳欣"},
            {"劉佳欣", "楊雅婷"},
            {"黃俊明", "楊雅婷"}
        };
        
        for (String[] friendship : friendships) {
            facebook.addFriendship(friendship[0], friendship[1]);
        }
        
        facebook.printNetwork();
        facebook.printNetworkStats();
        
        // 分析特定用戶
        System.out.println("\n📊 用戶分析:");
        String[] analyzeUsers = {"王小明", "陳美玲", "吳美麗"};
        for (String user : analyzeUsers) {
            facebook.printUserInfo(user);
        }
        
        // 共同好友分析
        System.out.println("\n👥 共同好友分析:");
        String[][] pairs = {
            {"王小明", "林建國"},
            {"張大志", "陳美玲"},
            {"李小華", "吳美麗"},
            {"周志強", "楊雅婷"}
        };
        
        for (String[] pair : pairs) {
            Set<String> common = facebook.getCommonFriends(pair[0], pair[1]);
            System.out.printf("  %s 和 %s 的共同好友: %s%n", 
                             pair[0], pair[1], 
                             common.isEmpty() ? "無" : common.toString());
        }
        
        // 孤立用戶查詢
        System.out.println("\n🔍 孤立用戶:");
        Set<String> isolated = facebook.getIsolatedUsers();
        if (isolated.isEmpty()) {
            System.out.println("  沒有孤立用戶");
        } else {
            System.out.println("  孤立用戶: " + isolated);
        }
        
        // 解除好友關係
        System.out.println("\n💔 解除好友關係:");
        facebook.removeFriendship("王小明", "張大志");
        facebook.removeFriendship("陳美玲", "吳美麗");
        
        facebook.printNetworkStats();
        facebook.printNetwork();
    }
}