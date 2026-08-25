import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 課堂實踐題二：瀏覽器返回功能
 * 指定檔名：BrowserBackstack.java
 * 
 * 使用 Deque<String> 儲存瀏覽過程，完成：
 * 1. 存取（瀏覽新網頁）
 * 2. 返回（回到上一頁）
 * 3. 目前（顯示當前頁面）
 * 
 * 空棧時不得丟出例外，連續測試至少五組操作。
 */
public class BrowserBackStack {

    // 使用 Deque 作為瀏覽記錄的後進先出（LIFO）堆疊
    private Deque<String> 瀏覽記錄;
    private String 當前頁面;

    public BrowserBackStack() {
        瀏覽記錄 = new ArrayDeque<>();
        當前頁面 = null; // 起始無任何頁面
    }

    /**
     * 存取新網頁：將當前頁面推入堆疊，然後設定新頁面為當前頁面
     */
    public void 存取新網頁(String 網址) {
        if (當前頁面 != null) {
            瀏覽記錄.push(當前頁面); // 將當前頁面存入堆疊（上一頁）
        }
        當前頁面 = 網址;
        System.out.println("➜ 瀏覽新網頁：" + 網址);
        顯示目前狀態();
    }

    /**
     * 返回上一頁：從堆疊彈出上一頁，若堆疊為空則保持當前頁面不變（不拋出例外）
     */
    public void 返回上一頁() {
        if (瀏覽記錄.isEmpty()) {
            System.out.println("⚠ 無法返回：沒有上一頁記錄（堆疊為空）");
        } else {
            當前頁面 = 瀏覽記錄.pop();
            System.out.println("◀ 返回上一頁：" + 當前頁面);
        }
        顯示目前狀態();
    }

    /**
     * 顯示目前頁面
     */
    public void 顯示目前頁面() {
        if (當前頁面 == null) {
            System.out.println("📍 目前頁面：無（尚未瀏覽任何網頁）");
        } else {
            System.out.println("📍 目前頁面：" + 當前頁面);
        }
    }

    /**
     * 顯示完整狀態（含堆疊內容，便於觀察）
     */
    private void 顯示目前狀態() {
        顯示目前頁面();
        System.out.println("   📚 返回堆疊（由頂至底）：" + 瀏覽記錄);
        System.out.println("   ------------------------------------");
    }

    /**
     * 主程式：連續測試至少五組操作
     */
    public static void main(String[] args) {
        System.out.println("=== 瀏覽器返回功能測試（連續五組以上操作）===\n");

        BrowserBackStack 瀏覽器 = new BrowserBackStack();

        // 操作 1：存取首頁
        瀏覽器.存取新網頁("https://www.google.com");

        // 操作 2：存取第二個網頁
        瀏覽器.存取新網頁("https://www.wikipedia.org");

        // 操作 3：存取第三個網頁
        瀏覽器.存取新網頁("https://github.com");

        // 操作 4：返回上一頁（從 GitHub 回到 Wikipedia）
        瀏覽器.返回上一頁();

        // 操作 5：返回上一頁（從 Wikipedia 回到 Google）
        瀏覽器.返回上一頁();

        // 操作 6：返回上一頁（堆疊已空，不拋出例外）
        瀏覽器.返回上一頁();

        // 操作 7：再次存取新網頁（清空狀態後重新瀏覽）
        瀏覽器.存取新網頁("https://www.stackoverflow.com");

        // 操作 8：再次返回（測試堆疊只有一頁的情況）
        瀏覽器.返回上一頁();

        // 操作 9：嘗試再返回（堆疊已空，測試例外處理）
        瀏覽器.返回上一頁();

        System.out.println("\n=== 測試完成 ===");
    }
}