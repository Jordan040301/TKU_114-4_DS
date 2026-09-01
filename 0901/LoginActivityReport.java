import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 登入記錄分析系統
 * 使用 HashMap 統計帳號登入次數，HashSet 找出不同 IP 數量
 */
public class LoginActivityReport {
    
    /**
     * 登入記錄類別
     */
    public static class LoginRecord {
        private final String username;
        private final String ipAddress;
        private final LocalDateTime loginTime;
        
        public LoginRecord(String username, String ipAddress, LocalDateTime loginTime) {
            this.username = username;
            this.ipAddress = ipAddress;
            this.loginTime = loginTime;
        }
        
        public LoginRecord(String username, String ipAddress) {
            this(username, ipAddress, LocalDateTime.now());
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getIpAddress() {
            return ipAddress;
        }
        
        public LocalDateTime getLoginTime() {
            return loginTime;
        }
        
        @Override
        public String toString() {
            return String.format("%s | %s | %s", 
                               username, 
                               ipAddress,
                               loginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
    
    // 所有登入記錄
    private List<LoginRecord> loginRecords;
    
    // 帳號登入次數統計
    private Map<String, Integer> loginCountMap;
    
    // 帳號登入 IP 集合
    private Map<String, Set<String>> userIpMap;
    
    // 所有 IP 集合
    private Set<String> allIpSet;
    
    // 帳號登入時間記錄
    private Map<String, List<LocalDateTime>> userLoginTimes;
    
    /**
     * 建構子
     */
    public LoginActivityReport() {
        this.loginRecords = new ArrayList<>();
        this.loginCountMap = new HashMap<>();
        this.userIpMap = new HashMap<>();
        this.allIpSet = new HashSet<>();
        this.userLoginTimes = new HashMap<>();
    }
    
    /**
     * 新增登入記錄
     * @param username 帳號
     * @param ipAddress IP 地址
     */
    public void addLogin(String username, String ipAddress) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("帳號不能為空");
        }
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("IP 地址不能為空");
        }
        
        LoginRecord record = new LoginRecord(username, ipAddress);
        loginRecords.add(record);
        
        // 統計登入次數
        loginCountMap.put(username, loginCountMap.getOrDefault(username, 0) + 1);
        
        // 記錄 IP
        userIpMap.computeIfAbsent(username, k -> new HashSet<>()).add(ipAddress);
        allIpSet.add(ipAddress);
        
        // 記錄登入時間
        userLoginTimes.computeIfAbsent(username, k -> new ArrayList<>()).add(record.getLoginTime());
    }
    
    /**
     * 批次新增登入記錄
     * @param records 登入記錄陣列
     */
    public void addLogins(LoginRecord... records) {
        for (LoginRecord record : records) {
            addLogin(record.getUsername(), record.getIpAddress());
        }
    }
    
    /**
     * 取得總登入次數
     * @return 總登入次數
     */
    public int getTotalLogins() {
        return loginRecords.size();
    }
    
    /**
     * 取得不同帳號數量
     * @return 帳號數量
     */
    public int getDistinctUsers() {
        return loginCountMap.size();
    }
    
    /**
     * 取得不同 IP 數量
     * @return IP 數量
     */
    public int getDistinctIps() {
        return allIpSet.size();
    }
    
    /**
     * 取得帳號登入次數
     * @param username 帳號
     * @return 登入次數
     */
    public int getLoginCount(String username) {
        return loginCountMap.getOrDefault(username, 0);
    }
    
    /**
     * 取得帳號使用的 IP 清單
     * @param username 帳號
     * @return IP 集合
     */
    public Set<String> getUserIps(String username) {
        return userIpMap.getOrDefault(username, new HashSet<>());
    }
    
    /**
     * 取得帳號登入時間清單
     * @param username 帳號
     * @return 登入時間列表
     */
    public List<LocalDateTime> getUserLoginTimes(String username) {
        return userLoginTimes.getOrDefault(username, new ArrayList<>());
    }
    
    /**
     * 取得特定 IP 的使用者
     * @param ipAddress IP 地址
     * @return 使用者集合
     */
    public Set<String> getUsersByIp(String ipAddress) {
        Set<String> users = new HashSet<>();
        for (LoginRecord record : loginRecords) {
            if (record.getIpAddress().equals(ipAddress)) {
                users.add(record.getUsername());
            }
        }
        return users;
    }
    
    /**
     * 找出異常重複登入 (短時間內多次登入)
     * @param minutes 時間窗口 (分鐘)
     * @param threshold 臨界次數
     * @return 異常登入報告
     */
    public Map<String, List<LocalDateTime>> findSuspiciousLogins(int minutes, int threshold) {
        Map<String, List<LocalDateTime>> suspicious = new HashMap<>();
        
        for (Map.Entry<String, List<LocalDateTime>> entry : userLoginTimes.entrySet()) {
            String username = entry.getKey();
            List<LocalDateTime> times = entry.getValue();
            
            if (times.size() < threshold) {
                continue;
            }
            
            // 排序時間
            Collections.sort(times);
            
            // 滑動窗口檢測
            for (int i = 0; i <= times.size() - threshold; i++) {
                LocalDateTime start = times.get(i);
                LocalDateTime end = times.get(i + threshold - 1);
                
                // 計算時間差 (分鐘)
                long diffMinutes = ChronoUnit.MINUTES.between(start, end);
                
                if (diffMinutes <= minutes) {
                    // 異常重複登入
                    List<LocalDateTime> suspiciousTimes = new ArrayList<>();
                    for (int j = i; j < i + threshold; j++) {
                        suspiciousTimes.add(times.get(j));
                    }
                    suspicious.put(username, suspiciousTimes);
                    break;
                }
            }
        }
        
        return suspicious;
    }
    
    /**
     * 找出多人共用 IP 的情況
     * @param threshold 臨界人數
     * @return 共用 IP 報告
     */
    public Map<String, Set<String>> findSharedIps(int threshold) {
        Map<String, Set<String>> sharedIpReport = new HashMap<>();
        
        for (String ip : allIpSet) {
            Set<String> users = getUsersByIp(ip);
            if (users.size() >= threshold) {
                sharedIpReport.put(ip, users);
            }
        }
        
        return sharedIpReport;
    }
    
    /**
     * 生成完整登入報告
     * @return 格式化的報告
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("\n=== 登入活動分析報告 ===\n");
        report.append("報告時間: ").append(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        report.append("\n📊 基本統計:\n");
        report.append("  總登入次數: ").append(getTotalLogins()).append("\n");
        report.append("  不同帳號數: ").append(getDistinctUsers()).append("\n");
        report.append("  不同 IP 數: ").append(getDistinctIps()).append("\n");
        
        // 登入次數排名
        report.append("\n📈 登入次數排名 (前 10 名):\n");
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(loginCountMap.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        report.append("  排名 | 帳號 | 登入次數 | 使用 IP 數\n");
        report.append("  -----|------|----------|-----------\n");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : sorted) {
            if (rank > 10) break;
            String user = entry.getKey();
            int count = entry.getValue();
            int ipCount = userIpMap.getOrDefault(user, new HashSet<>()).size();
            report.append(String.format("  %4d | %-4s | %8d | %9d%n", 
                       rank++, user, count, ipCount));
        }
        
        // 帳號詳細資訊
        report.append("\n📋 帳號詳細資訊:\n");
        List<String> sortedUsers = new ArrayList<>(loginCountMap.keySet());
        Collections.sort(sortedUsers);
        
        report.append("  帳號 | 登入次數 | IP 清單\n");
        report.append("  -----|----------|------------------------------\n");
        for (String user : sortedUsers) {
            int count = loginCountMap.get(user);
            Set<String> ips = userIpMap.getOrDefault(user, new HashSet<>());
            report.append(String.format("  %-4s | %8d | %s%n", 
                       user, count, ips.toString()));
        }
        
        // 異常重複登入
        report.append("\n🔍 異常重複登入分析 (5分鐘內登入3次以上):\n");
        Map<String, List<LocalDateTime>> suspicious = findSuspiciousLogins(5, 3);
        if (suspicious.isEmpty()) {
            report.append("  未發現異常重複登入\n");
        } else {
            for (Map.Entry<String, List<LocalDateTime>> entry : suspicious.entrySet()) {
                report.append(String.format("  ⚠️ %s: %d 次登入 (時間間隔 %d 分鐘內)%n",
                           entry.getKey(), entry.getValue().size(), 5));
                for (LocalDateTime time : entry.getValue()) {
                    report.append(String.format("      %s%n", 
                               time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
                }
            }
        }
        
        // 共用 IP 分析
        report.append("\n🖥️ 共用 IP 分析 (3人以上共用):\n");
        Map<String, Set<String>> sharedIps = findSharedIps(3);
        if (sharedIps.isEmpty()) {
            report.append("  未發現多人共用 IP\n");
        } else {
            for (Map.Entry<String, Set<String>> entry : sharedIps.entrySet()) {
                report.append(String.format("  IP %s: %d 人共用 - %s%n", 
                           entry.getKey(), entry.getValue().size(), entry.getValue()));
            }
        }
        
        return report.toString();
    }
    
    /**
     * 印出報告
     */
    public void printReport() {
        System.out.println(generateReport());
    }
    
    /**
     * 匯出 CSV 格式報告
     */
    public String exportCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("帳號,IP,登入時間\n");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (LoginRecord record : loginRecords) {
            csv.append(String.format("%s,%s,%s\n", 
                       record.getUsername(),
                       record.getIpAddress(),
                       record.getLoginTime().format(formatter)));
        }
        
        return csv.toString();
    }
    
    /**
     * 清除所有數據
     */
    public void clear() {
        loginRecords.clear();
        loginCountMap.clear();
        userIpMap.clear();
        allIpSet.clear();
        userLoginTimes.clear();
        System.out.println("🔄 已清除所有登入記錄");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 登入記錄分析系統測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：異常檢測
        testSuspiciousDetection();
        
        // 測試 3：共用 IP 分析
        testSharedIpAnalysis();
        
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
        
        LoginActivityReport report = new LoginActivityReport();
        
        // 模擬登入記錄
        System.out.println("新增登入記錄:");
        report.addLogin("user1", "192.168.1.1");
        report.addLogin("user2", "192.168.1.2");
        report.addLogin("user1", "192.168.1.1");
        report.addLogin("user3", "192.168.1.3");
        report.addLogin("user2", "192.168.1.4");
        report.addLogin("user1", "192.168.1.5");
        report.addLogin("user4", "192.168.1.1");
        report.addLogin("user3", "192.168.1.2");
        report.addLogin("user2", "192.168.1.2");
        report.addLogin("user1", "192.168.1.1");
        
        System.out.println("\n統計資訊:");
        System.out.println("  總登入次數: " + report.getTotalLogins());
        System.out.println("  不同帳號數: " + report.getDistinctUsers());
        System.out.println("  不同 IP 數: " + report.getDistinctIps());
        System.out.println("  user1 登入次數: " + report.getLoginCount("user1"));
        System.out.println("  user1 IP 清單: " + report.getUserIps("user1"));
        System.out.println("  使用 IP 192.168.1.1 的用戶: " + report.getUsersByIp("192.168.1.1"));
        
        report.printReport();
    }
    
    /**
     * 測試異常檢測
     */
    private static void testSuspiciousDetection() {
        System.out.println("--- 測試 2: 異常重複登入檢測 ---");
        
        LoginActivityReport report = new LoginActivityReport();
        
        System.out.println("模擬異常登入行為:");
        // user1 在短時間內多次登入
        report.addLogin("user1", "192.168.1.1");
        report.addLogin("user1", "192.168.1.1");
        report.addLogin("user1", "192.168.1.2");
        report.addLogin("user1", "192.168.1.3");
        report.addLogin("user1", "192.168.1.1");
        report.addLogin("user1", "192.168.1.4");
        
        // user2 正常登入
        report.addLogin("user2", "192.168.1.5");
        report.addLogin("user2", "192.168.1.5");
        
        // user3 在短時間內多次登入
        report.addLogin("user3", "192.168.1.6");
        report.addLogin("user3", "192.168.1.6");
        report.addLogin("user3", "192.168.1.7");
        report.addLogin("user3", "192.168.1.6");
        
        report.printReport();
    }
    
    /**
     * 測試共用 IP 分析
     */
    private static void testSharedIpAnalysis() {
        System.out.println("--- 測試 3: 共用 IP 分析 ---");
        
        LoginActivityReport report = new LoginActivityReport();
        
        System.out.println("模擬多人共用 IP:");
        // IP 192.168.1.1 被多人使用
        report.addLogin("user1", "192.168.1.1");
        report.addLogin("user2", "192.168.1.1");
        report.addLogin("user3", "192.168.1.1");
        report.addLogin("user4", "192.168.1.1");
        
        // IP 192.168.1.2 被多人使用
        report.addLogin("user1", "192.168.1.2");
        report.addLogin("user2", "192.168.1.2");
        report.addLogin("user3", "192.168.1.2");
        
        // IP 192.168.1.3 僅一人使用
        report.addLogin("user1", "192.168.1.3");
        
        // 其他登入
        report.addLogin("user5", "192.168.1.4");
        report.addLogin("user5", "192.168.1.4");
        report.addLogin("user6", "192.168.1.5");
        
        report.printReport();
        
        // 測試共用 IP 檢測
        System.out.println("\n🔍 共用 IP 檢測 (3人以上):");
        Map<String, Set<String>> shared = report.findSharedIps(3);
        for (Map.Entry<String, Set<String>> entry : shared.entrySet()) {
            System.out.println("  IP " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 4: 邊界情況 ---");
        
        LoginActivityReport report = new LoginActivityReport();
        
        // 測試 4.1: 空報告
        System.out.println("測試 4.1: 空報告");
        report.printReport();
        System.out.println("  getTotalLogins: " + report.getTotalLogins());
        System.out.println("  getDistinctUsers: " + report.getDistinctUsers());
        System.out.println("  getDistinctIps: " + report.getDistinctIps());
        System.out.println();
        
        // 測試 4.2: 單一用戶
        System.out.println("測試 4.2: 單一用戶");
        report.addLogin("user1", "192.168.1.1");
        report.addLogin("user1", "192.168.1.1");
        report.addLogin("user1", "192.168.1.2");
        System.out.println("  user1 登入次數: " + report.getLoginCount("user1"));
        System.out.println("  user1 IP 清單: " + report.getUserIps("user1"));
        System.out.println("  不同 IP 數: " + report.getDistinctIps());
        System.out.println();
        
        // 測試 4.3: null 處理
        System.out.println("測試 4.3: null 處理");
        try {
            report.addLogin(null, "192.168.1.1");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲 null 帳號例外: " + e.getMessage());
        }
        
        try {
            report.addLogin("user1", null);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲 null IP 例外: " + e.getMessage());
        }
        System.out.println();
        
        // 測試 4.4: 大量重複 IP
        System.out.println("測試 4.4: 大量重複 IP");
        LoginActivityReport report2 = new LoginActivityReport();
        for (int i = 0; i < 100; i++) {
            report2.addLogin("user" + (i % 10), "192.168.1.1");
        }
        System.out.println("  總登入: " + report2.getTotalLogins());
        System.out.println("  不同帳號: " + report2.getDistinctUsers());
        System.out.println("  不同 IP: " + report2.getDistinctIps());
        System.out.println("  user0 登入次數: " + report2.getLoginCount("user0"));
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 5: 實際應用場景 ---");
        System.out.println("🔐 企業登入安全監控系統");
        
        LoginActivityReport monitor = new LoginActivityReport();
        
        // 模擬一週的登入記錄
        String[] users = {"alice", "bob", "charlie", "diana", "eve", "frank"};
        String[] ips = {"10.0.1.1", "10.0.1.2", "10.0.1.3", "10.0.2.1", "10.0.2.2", "192.168.1.1"};
        
        System.out.println("\n📝 模擬一週登入記錄:");
        
        // 隨機產生登入記錄
        Random random = new Random(42);
        for (int day = 0; day < 7; day++) {
            int loginsPerDay = 5 + random.nextInt(15);
            for (int i = 0; i < loginsPerDay; i++) {
                String user = users[random.nextInt(users.length)];
                String ip = ips[random.nextInt(ips.length)];
                monitor.addLogin(user, ip);
            }
        }
        
        // 模擬異常情況
        // 1. 短時間多次登入
        monitor.addLogin("alice", "10.0.1.1");
        monitor.addLogin("alice", "10.0.1.2");
        monitor.addLogin("alice", "10.0.1.1");
        monitor.addLogin("alice", "10.0.1.3");
        monitor.addLogin("alice", "10.0.1.1");
        
        // 2. 多人共用 IP
        monitor.addLogin("bob", "192.168.1.100");
        monitor.addLogin("charlie", "192.168.1.100");
        monitor.addLogin("diana", "192.168.1.100");
        monitor.addLogin("eve", "192.168.1.100");
        
        // 3. 陌生 IP 登入
        monitor.addLogin("frank", "203.0.113.1");
        monitor.addLogin("frank", "203.0.113.1");
        
        System.out.println("  總登入次數: " + monitor.getTotalLogins());
        System.out.println("  不同帳號數: " + monitor.getDistinctUsers());
        System.out.println("  不同 IP 數: " + monitor.getDistinctIps());
        
        // 生成完整報告
        monitor.printReport();
        
        // 異常檢測摘要
        System.out.println("\n🚨 安全告警摘要:");
        
        // 1. 異常重複登入
        Map<String, List<LocalDateTime>> suspicious = monitor.findSuspiciousLogins(5, 3);
        if (!suspicious.isEmpty()) {
            System.out.println("  ⚠️ 偵測到異常重複登入:");
            for (Map.Entry<String, List<LocalDateTime>> entry : suspicious.entrySet()) {
                System.out.printf("     帳號 %s: %d 次登入 (5分鐘內)%n", 
                                 entry.getKey(), entry.getValue().size());
            }
        }
        
        // 2. 共用 IP
        Map<String, Set<String>> shared = monitor.findSharedIps(3);
        if (!shared.isEmpty()) {
            System.out.println("  ⚠️ 偵測到多人共用 IP:");
            for (Map.Entry<String, Set<String>> entry : shared.entrySet()) {
                System.out.printf("     IP %s: %d 人共用%n", 
                                 entry.getKey(), entry.getValue().size());
            }
        }
        
        // 3. 高登入次數帳號
        System.out.println("  📊 高登入次數帳號 (前 3 名):");
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(monitor.loginCountMap.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        for (int i = 0; i < Math.min(3, sorted.size()); i++) {
            System.out.printf("     %s: %d 次%n", sorted.get(i).getKey(), sorted.get(i).getValue());
        }
        
        // CSV 匯出示範
        System.out.println("\n📄 CSV 匯出 (前 5 筆):");
        String[] lines = monitor.exportCsv().split("\n");
        for (int i = 0; i < Math.min(6, lines.length); i++) {
            System.out.println("  " + lines[i]);
        }
        if (lines.length > 6) {
            System.out.println("  ... (其餘 " + (lines.length - 6) + " 筆省略)");
        }
    }
}