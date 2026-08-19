/**
 * 訊息傳遞接口系統
 * 指定檔名：MessageSenderSystem.java
 * 
 * 定義 MessageSender 介面，包含 send(String receiver, String message)
 * 建立 EmailSender、SmsSender 與 ConsoleSender
 * 編寫只依賴 MessageSender 的 notify() 方法
 */
public class MessageSenderSystem {
    
    public static void main(String[] args) {
        System.out.println("========== 訊息傳遞接口系統 ==========");
        
        // 建立各種發送器
        MessageSender emailSender = new EmailSender();
        MessageSender smsSender = new SmsSender();
        MessageSender consoleSender = new ConsoleSender();
        
        // 測試正常發送
        System.out.println("\n========== 測試正常發送 ==========");
        sendNotification(emailSender, "user@example.com", "您的訂單已出貨！");
        sendNotification(smsSender, "0912-345-678", "您的驗證碼是 123456");
        sendNotification(consoleSender, "系統管理員", "伺服器即將進行維護");
        
        // 測試空白接收者
        System.out.println("\n========== 測試空白接收者 ==========");
        sendNotification(emailSender, "", "這是一封測試郵件");
        sendNotification(smsSender, "   ", "這是一封測試簡訊");
        sendNotification(consoleSender, null, "這是一則測試訊息");
        
        // 測試空白訊息
        System.out.println("\n========== 測試空白訊息 ==========");
        sendNotification(emailSender, "user@example.com", "");
        sendNotification(smsSender, "0912-345-678", "   ");
        sendNotification(consoleSender, "系統管理員", null);
        
        // 測試兩者皆空白
        System.out.println("\n========== 測試兩者皆空白 ==========");
        sendNotification(emailSender, "", "");
        sendNotification(smsSender, null, null);
        
        System.out.println("\n========== 測試完成 ==========");
    }
    
    /**
     * 發送通知 - 只依賴 MessageSender 介面
     * 新增發送者時不需要修改此方法
     * 
     * @param sender 訊息發送器（依賴介面，不依賴具體類別）
     * @param receiver 接收者
     * @param message 訊息內容
     */
    public static void sendNotification(MessageSender sender, String receiver, String message) {
        // 委派給 sender 處理
        sender.send(receiver, message);
    }
}

/**
 * 訊息發送器介面
 * 定義訊息發送的標準契約
 */
interface MessageSender {
    
    /**
     * 發送訊息
     * @param receiver 接收者（空白時應處理）
     * @param message 訊息內容（空白時應處理）
     */
    void send(String receiver, String message);
}

/**
 * 電子郵件發送器 - 實作 MessageSender 介面
 */
class EmailSender implements MessageSender {
    private static final String EMAIL_DOMAIN = "@example.com";
    
    @Override
    public void send(String receiver, String message) {
        // 處理空白接收者
        if (receiver == null || receiver.trim().isEmpty()) {
            System.out.println("【電子郵件】發送失敗：接收者為空白");
            return;
        }
        
        // 處理空白訊息
        if (message == null || message.trim().isEmpty()) {
            System.out.println("【電子郵件】發送失敗：訊息內容為空白");
            return;
        }
        
        // 模擬發送電子郵件
        String email = receiver.contains("@") ? receiver : receiver + EMAIL_DOMAIN;
        System.out.println("【電子郵件】發送成功！");
        System.out.println("  收件人：" + email);
        System.out.println("  內容：" + message);
        System.out.println("  狀態：已寄出");
    }
}

/**
 * 簡訊發送器 - 實作 MessageSender 介面
 */
class SmsSender implements MessageSender {
    private static final int MAX_SMS_LENGTH = 160;
    
    @Override
    public void send(String receiver, String message) {
        // 處理空白接收者
        if (receiver == null || receiver.trim().isEmpty()) {
            System.out.println("【簡訊】發送失敗：接收者為空白");
            return;
        }
        
        // 處理空白訊息
        if (message == null || message.trim().isEmpty()) {
            System.out.println("【簡訊】發送失敗：訊息內容為空白");
            return;
        }
        
        // 檢查簡訊長度限制
        String smsContent = message;
        if (smsContent.length() > MAX_SMS_LENGTH) {
            smsContent = smsContent.substring(0, MAX_SMS_LENGTH) + "...";
        }
        
        // 模擬發送簡訊
        System.out.println("【簡訊】發送成功！");
        System.out.println("  收件人：" + receiver);
        System.out.println("  內容：" + smsContent);
        System.out.println("  字數：" + smsContent.length() + "/" + MAX_SMS_LENGTH + " 字");
        System.out.println("  狀態：已發送");
    }
}

/**
 * 控制台發送器 - 實作 MessageSender 介面
 * 用於測試或日誌記錄
 */
class ConsoleSender implements MessageSender {
    
    @Override
    public void send(String receiver, String message) {
        // 處理空白接收者
        if (receiver == null || receiver.trim().isEmpty()) {
            System.out.println("【控制台】發送失敗：接收者為空白");
            return;
        }
        
        // 處理空白訊息
        if (message == null || message.trim().isEmpty()) {
            System.out.println("【控制台】發送失敗：訊息內容為空白");
            return;
        }
        
        // 模擬輸出到控制台
        System.out.println("【控制台】訊息輸出成功！");
        System.out.println("  接收者：" + receiver);
        System.out.println("  內容：" + message);
        System.out.println("  時間：" + java.time.LocalDateTime.now().toString().substring(0, 19));
        System.out.println("  狀態：已輸出至控制台");
    }
}

/**
 * 新增的推送通知發送器 - 示範擴充性
 * 新增發送者時不需要修改 notify() 方法
 */
class PushNotificationSender implements MessageSender {
    
    @Override
    public void send(String receiver, String message) {
        // 處理空白接收者
        if (receiver == null || receiver.trim().isEmpty()) {
            System.out.println("【推播通知】發送失敗：接收者為空白");
            return;
        }
        
        // 處理空白訊息
        if (message == null || message.trim().isEmpty()) {
            System.out.println("【推播通知】發送失敗：訊息內容為空白");
            return;
        }
        
        // 模擬發送推播通知
        System.out.println("【推播通知】發送成功！");
        System.out.println("  裝置 ID：" + receiver);
        System.out.println("  內容：" + message);
        System.out.println("  狀態：已推播");
    }
}