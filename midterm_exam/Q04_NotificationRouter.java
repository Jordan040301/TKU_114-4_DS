/**
 * 檔名：Q04_NotificationRouter.java
 * 功能：通知路由系統（使用 Interface）
 * 說明：EmailChannel 和 SmsChannel 實作 Channel 介面
 *       根據 destination 自動選擇支援的頻道
 */

import java.util.ArrayList;
import java.util.List;

public class Q04_NotificationRouter {

    // ========== Channel 介面 ==========
    public interface Channel {
        /**
         * 取得頻道名稱
         * @return 頻道名稱（EMAIL 或 SMS）
         */
        String name();

        /**
         * 檢查是否支援該目的地
         * @param destination 目的地（Email 地址或手機號碼）
         * @return true 表示支援，false 表示不支援
         */
        boolean supports(String destination);

        /**
         * 發送訊息
         * @param destination 目的地
         * @param message 訊息內容
         * @return 格式為 "CHANNEL|destination|message"
         */
        String send(String destination, String message);
    }

    // ========== EmailChannel 實作 ==========
    public static class EmailChannel implements Channel {
        private static final String CHANNEL_NAME = "EMAIL";

        @Override
        public String name() {
            return CHANNEL_NAME;
        }

        /**
         * 檢查 Email 目的地是否有效
         * 規則：包含 @ 且 @ 不在開頭或結尾
         */
        @Override
        public boolean supports(String destination) {
            // destination 為 null 時不支援
            if (destination == null) {
                return false;
            }

            int atIndex = destination.indexOf('@');
            // 包含 @ 且 @ 不在開頭（index > 0）且不在結尾（index < length - 1）
            return atIndex > 0 && atIndex < destination.length() - 1;
        }

        @Override
        public String send(String destination, String message) {
            // 即使 destination 或 message 為 null，也回傳正確格式
            String dest = (destination != null) ? destination : "null";
            String msg = (message != null) ? message : "null";
            return name() + "|" + dest + "|" + msg;
        }
    }

    // ========== SmsChannel 實作 ==========
    public static class SmsChannel implements Channel {
        private static final String CHANNEL_NAME = "SMS";

        @Override
        public String name() {
            return CHANNEL_NAME;
        }

        /**
         * 檢查 SMS 目的地是否有效
         * 規則：去除 '-' 後恰好 10 個數字
         */
        @Override
        public boolean supports(String destination) {
            // destination 為 null 時不支援
            if (destination == null) {
                return false;
            }

            // 去除所有 '-' 字元
            String digitsOnly = destination.replace("-", "");

            // 檢查是否全部是數字，且長度恰好為 10
            if (digitsOnly.length() != 10) {
                return false;
            }

            // 檢查是否全部為數字
            for (char c : digitsOnly.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String send(String destination, String message) {
            // 即使 destination 或 message 為 null，也回傳正確格式
            String dest = (destination != null) ? destination : "null";
            String msg = (message != null) ? message : "null";
            return name() + "|" + dest + "|" + msg;
        }
    }

    // ========== 靜態路由方法 ==========

    /**
     * 路由通知到所有支援該目的地的頻道
     * @param channels 頻道列表
     * @param destination 目的地
     * @param message 訊息內容
     * @return 所有成功發送的結果列表（依 channels 順序）
     */
    public static List<String> route(List<Channel> channels, String destination, String message) {
        // channels、destination 或 message 為 null 時回傳 empty List
        if (channels == null || destination == null || message == null) {
            return new ArrayList<>();
        }

        List<String> results = new ArrayList<>();

        for (Channel channel : channels) {
            // List 中的 null channel 要略過
            if (channel == null) {
                continue;
            }

            // 檢查該頻道是否支援此目的地
            if (channel.supports(destination)) {
                // 呼叫 send 並加入結果
                results.add(channel.send(destination, message));
            }
        }

        return results;
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試範例 =====");
        var channels = List.of(
            new Q04_NotificationRouter.EmailChannel(),
            new Q04_NotificationRouter.SmsChannel()
        );

        System.out.println(Q04_NotificationRouter.route(channels, "a8b.com", "Ready"));
        System.out.println(Q04_NotificationRouter.route(channels, "0912-345-678", "Go"));
        System.out.println();

        // ===== 測試 Email 驗證 =====
        System.out.println("===== Email 驗證測試 =====");
        Q04_NotificationRouter.EmailChannel email = new Q04_NotificationRouter.EmailChannel();

        System.out.println("Email supports('a8b.com') → " + email.supports("a8b.com"));
        // false（沒有 @）

        System.out.println("Email supports('@a8b.com') → " + email.supports("@a8b.com"));
        // false（@ 在開頭）

        System.out.println("Email supports('a8b@.com') → " + email.supports("a8b@.com"));
        // true（@ 在中間）

        System.out.println("Email supports('user@domain.com') → " + email.supports("user@domain.com"));
        // true（標準 Email）

        System.out.println("Email supports('user@domain') → " + email.supports("user@domain"));
        // true（@ 在中間）

        System.out.println("Email supports('user@') → " + email.supports("user@"));
        // false（@ 在結尾）

        System.out.println("Email supports(null) → " + email.supports(null));
        // false
        System.out.println();

        // ===== 測試 SMS 驗證 =====
        System.out.println("===== SMS 驗證測試 =====");
        Q04_NotificationRouter.SmsChannel sms = new Q04_NotificationRouter.SmsChannel();

        System.out.println("SMS supports('0912-345-678') → " + sms.supports("0912-345-678"));
        // true（去除 '-' 後為 0912345678，10 個數字）

        System.out.println("SMS supports('0912345678') → " + sms.supports("0912345678"));
        // true（10 個數字）

        System.out.println("SMS supports('0912-345-67') → " + sms.supports("0912-345-67"));
        // false（去除 '-' 後為 091234567，9 個數字）

        System.out.println("SMS supports('0912-345-6789') → " + sms.supports("0912-345-6789"));
        // false（去除 '-' 後為 09123456789，11 個數字）

        System.out.println("SMS supports('0912-345-abc') → " + sms.supports("0912-345-abc"));
        // false（包含非數字字元）

        System.out.println("SMS supports(null) → " + sms.supports(null));
        // false
        System.out.println();

        // ===== 測試 send() 格式 =====
        System.out.println("===== send() 格式測試 =====");
        System.out.println("Email.send('user@domain.com', 'Hello') → " + 
                           email.send("user@domain.com", "Hello"));
        System.out.println("SMS.send('0912-345-678', 'Hi') → " + 
                           sms.send("0912-345-678", "Hi"));
        System.out.println();

        // ===== 測試 route() 多頻道路由 =====
        System.out.println("===== 多頻道路由測試 =====");
        List<Q04_NotificationRouter.Channel> mixedChannels = List.of(
            new Q04_NotificationRouter.EmailChannel(),
            new Q04_NotificationRouter.SmsChannel(),
            new Q04_NotificationRouter.EmailChannel(),  // 第二個 Email
            null  // null channel 應該被略過
        );

        // 目的地是 Email，只有 EmailChannel 支援
        System.out.println("路由到 Email 目的地：");
        System.out.println(Q04_NotificationRouter.route(mixedChannels, "test@example.com", "Hello Email"));
        // 應該只有兩個 EmailChannel 會回傳結果

        // 目的地是 SMS，只有 SmsChannel 支援
        System.out.println("路由到 SMS 目的地：");
        System.out.println(Q04_NotificationRouter.route(mixedChannels, "0912-345-678", "Hello SMS"));
        // 只有 SmsChannel 會回傳結果
        System.out.println();

        // ===== 測試 Null Edge Cases =====
        System.out.println("===== Null Edge Cases 測試 =====");
        var channelList = List.of(
            new Q04_NotificationRouter.EmailChannel(),
            new Q04_NotificationRouter.SmsChannel()
        );

        // channels 為 null
        System.out.println("channels 為 null → " + 
                           Q04_NotificationRouter.route(null, "test@test.com", "msg"));
        // []

        // destination 為 null
        System.out.println("destination 為 null → " + 
                           Q04_NotificationRouter.route(channelList, null, "msg"));
        // []

        // message 為 null
        System.out.println("message 為 null → " + 
                           Q04_NotificationRouter.route(channelList, "test@test.com", null));
        // []

        // List 中的 null channel
        List<Q04_NotificationRouter.Channel> listWithNull = new ArrayList<>();
        listWithNull.add(null);
        listWithNull.add(new Q04_NotificationRouter.EmailChannel());
        listWithNull.add(null);
        System.out.println("List 包含 null channel → " + 
                           Q04_NotificationRouter.route(listWithNull, "test@test.com", "msg"));
        // 只會有一個結果
        System.out.println();

        // ===== 測試綜合場景 =====
        System.out.println("===== 綜合場景測試 =====");
        var allChannels = List.of(
            new Q04_NotificationRouter.EmailChannel(),
            new Q04_NotificationRouter.SmsChannel()
        );

        // 場景 1：Email 和 SMS 都支援
        System.out.println("場景 1：'user@domain.com' 只支援 Email");
        System.out.println(Q04_NotificationRouter.route(allChannels, "user@domain.com", "Welcome"));

        System.out.println("場景 2：'0912-345-678' 只支援 SMS");
        System.out.println(Q04_NotificationRouter.route(allChannels, "0912-345-678", "Welcome"));

        System.out.println("場景 3：'invalid' 兩者都不支援");
        System.out.println(Q04_NotificationRouter.route(allChannels, "invalid", "Welcome"));

        System.out.println("場景 4：'user@domain.com' 同時支援 Email，'0912-345-678' 同時支援 SMS");
        System.out.println("多目的地測試完成");
    }
}