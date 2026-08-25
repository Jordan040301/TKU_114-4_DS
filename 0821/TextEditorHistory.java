import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 課後作業一：文字編輯 撤銷/重做
 * 指定檔名：TextEditorHistory.java
 * 
 * 分別使用 Deque<String> 作為 Undo 和 Redo 堆疊：
 * 1. 新增操作後清空 Redo 堆疊
 * 2. Undo 將資料移至 Redo 堆疊
 * 3. Redo 將資料移回 Undo 堆疊
 * 4. 處理空堆疊（不拋出例外）
 * 5. 輸出每一步狀態
 */
public class TextEditorHistory {

    // 使用兩個 Deque 作為堆疊（後進先出）
    private Deque<String> 撤銷堆疊;   // Undo 堆疊：儲存可撤銷的操作
    private Deque<String> 重做堆疊;   // Redo 堆疊：儲存可重做的操作
    private String 當前文字內容;      // 模擬目前編輯器的文字內容

    public TextEditorHistory() {
        撤銷堆疊 = new ArrayDeque<>();
        重做堆疊 = new ArrayDeque<>();
        當前文字內容 = ""; // 初始為空字串
    }

    /**
     * 1. 新增操作：新增文字內容（模擬使用者輸入）
     *    新增操作後必須清空 Redo 堆疊
     */
    public void 新增操作(String 新文字) {
        // 將目前的文字內容推入 Undo 堆疊（以便後續可撤銷）
        if (!當前文字內容.isEmpty()) {
            撤銷堆疊.push(當前文字內容);
        }
        
        // 更新當前文字內容
        當前文字內容 = 新文字;
        
        // ★ 新增操作後清空 Redo 堆疊
        重做堆疊.clear();
        
        System.out.println("✏️  新增操作：「" + 新文字 + "」");
        顯示狀態();
    }

    /**
     * 2. 撤銷（Undo）：將資料移至 Redo 堆疊
     *    從 Undo 堆疊彈出上一個狀態，推入 Redo 堆疊
     */
    public void 撤銷() {
        if (撤銷堆疊.isEmpty()) {
            System.out.println("⚠ 無法撤銷：沒有可撤銷的歷史記錄（撤銷堆疊為空）");
            顯示狀態();
            return;
        }
        
        // 將目前的文字內容推入 Redo 堆疊（以便後續可重做）
        重做堆疊.push(當前文字內容);
        
        // 從 Undo 堆疊彈出上一個狀態，還原為當前內容
        當前文字內容 = 撤銷堆疊.pop();
        
        System.out.println("↩️  撤銷操作：還原為「" + 當前文字內容 + "」");
        顯示狀態();
    }

    /**
     * 3. 重做（Redo）：將資料移回 Undo 堆疊
     *    從 Redo 堆疊彈出狀態，推入 Undo 堆疊
     */
    public void 重做() {
        if (重做堆疊.isEmpty()) {
            System.out.println("⚠ 無法重做：沒有可重做的歷史記錄（重做堆疊為空）");
            顯示狀態();
            return;
        }
        
        // 將目前的文字內容推入 Undo 堆疊（以便後續可撤銷）
        撤銷堆疊.push(當前文字內容);
        
        // 從 Redo 堆疊彈出狀態，還原為當前內容
        當前文字內容 = 重做堆疊.pop();
        
        System.out.println("⏩ 重做操作：還原為「" + 當前文字內容 + "」");
        顯示狀態();
    }

    /**
     * 顯示當前狀態（含堆疊內容）
     */
    public void 顯示狀態() {
        System.out.println("   📄 目前文字內容：「" + (當前文字內容.isEmpty() ? "（空）" : 當前文字內容) + "」");
        System.out.println("   ↩️  撤銷堆疊（由頂至底）：" + 撤銷堆疊);
        System.out.println("   ↪️  重做堆疊（由頂至底）：" + 重做堆疊);
        System.out.println("   ------------------------------------");
    }

    /**
     * 取得當前文字內容
     */
    public String 取得當前文字() {
        return 當前文字內容;
    }

    /**
     * 主程式：模擬文字編輯操作
     */
    public static void main(String[] args) {
        System.out.println("=== 文字編輯器 撤銷/重做 功能測試 ===\n");

        TextEditorHistory 編輯器 = new TextEditorHistory();

        // 初始狀態
        System.out.println("--- 初始狀態 ---");
        編輯器.顯示狀態();
        System.out.println();

        // === 模擬輸入文字 ===
        System.out.println("--- 開始編輯文字 ---");
        編輯器.新增操作("Hello");
        編輯器.新增操作("Hello World");
        編輯器.新增操作("Hello World!!!"); // 目前最新內容
        System.out.println();

        // === 測試撤銷（Undo） ===
        System.out.println("--- 測試撤銷功能 ---");
        編輯器.撤銷(); // 從 "Hello World!!!" → "Hello World"
        編輯器.撤銷(); // 從 "Hello World" → "Hello"
        編輯器.撤銷(); // 從 "Hello" → "" (空)
        編輯器.撤銷(); // 測試空堆疊（無法再撤銷）
        System.out.println();

        // === 測試重做（Redo） ===
        System.out.println("--- 測試重做功能 ---");
        編輯器.重做(); // 從 "" → "Hello"
        編輯器.重做(); // 從 "Hello" → "Hello World"
        編輯器.重做(); // 從 "Hello World" → "Hello World!!!"
        編輯器.重做(); // 測試空堆疊（無法再重做）
        System.out.println();

        // === 測試「新增操作後清空 Redo」 ===
        System.out.println("--- 測試新增操作後清空 Redo ---");
        編輯器.撤銷(); // 先撤銷到 "Hello World"
        編輯器.撤銷(); // 再撤銷到 "Hello"
        System.out.println("目前狀態：");
        編輯器.顯示狀態();
        
        System.out.println("執行新的新增操作（輸入「Goodbye」）...");
        編輯器.新增操作("Goodbye"); // ★ 此操作應清空 Redo 堆疊
        System.out.println();

        // === 驗證 Redo 已被清空 ===
        System.out.println("--- 驗證 Redo 已被清空（無法重做） ---");
        編輯器.重做(); // 應該顯示無法重做（因為 Redo 堆疊已被清空）
        System.out.println();

        // === 測試混合操作 ===
        System.out.println("--- 混合操作測試 ---");
        編輯器.新增操作("Goodbye World");
        編輯器.撤銷(); // 回到 "Goodbye"
        編輯器.新增操作("Hello Again"); // 應清空 Redo
        編輯器.撤銷(); // 回到 "Goodbye"
        編輯器.重做(); // 回到 "Hello Again"
        編輯器.撤銷(); // 回到 "Goodbye"
        編輯器.顯示狀態();

        System.out.println("\n=== 測試完成 ===");
    }
}