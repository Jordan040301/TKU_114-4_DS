import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 課後作業四：集合選擇報告與實務
 * 指定檔名：CollectionChoiceReport.java
 * 
 * 重點關注需求選擇結構並實現主要操作：
 * 1. 保留搜尋記錄並允許重複 → List (ArrayList)
 * 2. 儲存不重複的會員號碼 → Set (HashSet)
 * 3. 以學號查詢成績 → Map (HashMap)
 * 4. 依照順序處理印刷工作 → Queue (LinkedList)
 * 5. 最近多次操作 → Stack (ArrayDeque)
 * 
 * 方案輸出每個需求選擇的介面、實作與操作結果。
 */
public class CollectionChoiceReport {

    public static void main(String[] args) {
        System.out.println("=== 集合選擇報告與實務 ===\n");

        // ============================================
        // 需求 1：保留搜尋記錄並允許重複
        // 選擇：List (ArrayList)
        // ============================================
        System.out.println("【需求一】保留搜尋記錄並允許重複");
        System.out.println("選擇介面：List");
        System.out.println("選擇實作：ArrayList");
        System.out.println("說明：List 允許重複元素，ArrayList 提供快速隨機存取，適合記錄搜尋歷史");
        System.out.println("--- 操作示範 ---");
        
        List<String> 搜尋記錄 = new ArrayList<>();
        搜尋記錄.add("Java 教學");
        搜尋記錄.add("資料結構");
        搜尋記錄.add("Java 教學");  // 允許重複
        搜尋記錄.add("演算法");
        搜尋記錄.add("資料結構");  // 允許重複
        
        System.out.println("新增搜尋記錄：Java 教學, 資料結構, Java 教學, 演算法, 資料結構");
        System.out.println("搜尋記錄內容：" + 搜尋記錄);
        System.out.println("搜尋記錄筆數：" + 搜尋記錄.size());
        System.out.println("第 2 筆搜尋：" + 搜尋記錄.get(1));
        System.out.println("移除第 3 筆搜尋（索引 2）：" + 搜尋記錄.remove(2));
        System.out.println("移除後內容：" + 搜尋記錄);
        System.out.println("搜尋「Java 教學」出現次數：" + 
                          java.util.Collections.frequency(搜尋記錄, "Java 教學"));
        System.out.println();

        // ============================================
        // 需求 2：儲存不重複的會員號碼
        // 選擇：Set (HashSet)
        // ============================================
        System.out.println("【需求二】儲存不重複的會員號碼");
        System.out.println("選擇介面：Set");
        System.out.println("選擇實作：HashSet");
        System.out.println("說明：Set 自動拒絕重複元素，HashSet 提供 O(1) 查詢與新增效能");
        System.out.println("--- 操作示範 ---");
        
        Set<String> 會員號碼 = new HashSet<>();
        會員號碼.add("M001");
        會員號碼.add("M002");
        會員號碼.add("M003");
        會員號碼.add("M001");  // 重複，不會加入
        會員號碼.add("M004");
        會員號碼.add("M002");  // 重複，不會加入
        
        System.out.println("新增會員號碼：M001, M002, M003, M001(重複), M004, M002(重複)");
        System.out.println("會員號碼內容：" + 會員號碼);
        System.out.println("會員人數：" + 會員號碼.size());
        System.out.println("是否包含 M003：" + 會員號碼.contains("M003"));
        System.out.println("是否包含 M005：" + 會員號碼.contains("M005"));
        會員號碼.remove("M003");
        System.out.println("移除 M003 後內容：" + 會員號碼);
        System.out.println();

        // ============================================
        // 需求 3：以學號查詢成績
        // 選擇：Map (HashMap)
        // ============================================
        System.out.println("【需求三】以學號查詢成績");
        System.out.println("選擇介面：Map");
        System.out.println("選擇實作：HashMap");
        System.out.println("說明：Map 提供 Key-Value 對應，HashMap 提供 O(1) 查詢效能");
        System.out.println("--- 操作示範 ---");
        
        Map<String, Integer> 學號成績 = new HashMap<>();
        學號成績.put("S001", 95);
        學號成績.put("S002", 87);
        學號成績.put("S003", 92);
        學號成績.put("S004", 78);
        學號成績.put("S005", 88);
        
        System.out.println("新增學號成績：S001=95, S002=87, S003=92, S004=78, S005=88");
        System.out.println("學號成績內容：" + 學號成績);
        System.out.println("S003 的成績：" + 學號成績.get("S003"));
        System.out.println("S005 的成績：" + 學號成績.get("S005"));
        System.out.println("S006 的成績（不存在）：" + 學號成績.get("S006"));
        System.out.println("是否包含 S002：" + 學號成績.containsKey("S002"));
        System.out.println("更新 S004 成績為 82");
        學號成績.put("S004", 82);
        System.out.println("更新後 S004 成績：" + 學號成績.get("S004"));
        System.out.println("移除 S005 成績：" + 學號成績.remove("S005"));
        System.out.println("移除後內容：" + 學號成績);
        System.out.println("所有成績：" + 學號成績.values());
        System.out.println();

        // ============================================
        // 需求 4：依照順序處理印刷工作
        // 選擇：Queue (LinkedList)
        // ============================================
        System.out.println("【需求四】依照順序處理印刷工作");
        System.out.println("選擇介面：Queue");
        System.out.println("選擇實作：LinkedList");
        System.out.println("說明：Queue 提供先進先出 (FIFO) 特性，適合工作排程");
        System.out.println("--- 操作示範 ---");
        
        Queue<String> 印刷佇列 = new LinkedList<>();
        印刷佇列.offer("文件A - 5頁");
        印刷佇列.offer("文件B - 3頁");
        印刷佇列.offer("文件C - 8頁");
        印刷佇列.offer("文件D - 2頁");
        印刷佇列.offer("文件E - 6頁");
        
        System.out.println("加入印刷工作：文件A(5頁), 文件B(3頁), 文件C(8頁), 文件D(2頁), 文件E(6頁)");
        System.out.println("印刷佇列內容：" + 印刷佇列);
        System.out.println("目前等待工作數：" + 印刷佇列.size());
        System.out.println("查看下一個工作（不移除）：" + 印刷佇列.peek());
        System.out.println("--- 開始處理 ---");
        System.out.println("處理：「" + 印刷佇列.poll() + "」");
        System.out.println("處理：「" + 印刷佇列.poll() + "」");
        System.out.println("處理：「" + 印刷佇列.poll() + "」");
        System.out.println("剩餘工作：" + 印刷佇列);
        System.out.println("加入新工作：文件F - 4頁");
        印刷佇列.offer("文件F - 4頁");
        System.out.println("剩餘工作：" + 印刷佇列);
        System.out.println();

        // ============================================
        // 需求 5：最近多次操作（復原/重做）
        // 選擇：Stack (ArrayDeque)
        // ============================================
        System.out.println("【需求五】最近多次操作（復原/重做）");
        System.out.println("選擇介面：Deque（作為 Stack）");
        System.out.println("選擇實作：ArrayDeque");
        System.out.println("說明：Deque 提供 LIFO 特性，ArrayDeque 效能優於 Stack 類別");
        System.out.println("--- 操作示範 ---");
        
        Deque<String> 操作紀錄 = new ArrayDeque<>();
        Deque<String> 復原記錄 = new ArrayDeque<>();
        
        System.out.println("執行操作：打字「Hello」");
        操作紀錄.push("Hello");
        System.out.println("執行操作：打字「 World」");
        操作紀錄.push("Hello World");
        System.out.println("執行操作：打字「!!!」");
        操作紀錄.push("Hello World!!!");
        System.out.println("目前內容：" + 操作紀錄.peek());
        System.out.println("操作堆疊（由頂至底）：" + 操作紀錄);
        
        System.out.println("--- 復原操作 (Undo) ---");
        System.out.println("復原前內容：" + 操作紀錄.peek());
        復原記錄.push(操作紀錄.pop());
        System.out.println("復原後內容：" + 操作紀錄.peek());
        System.out.println("復原記錄：" + 復原記錄);
        
        System.out.println("--- 復原操作 (Undo) ---");
        System.out.println("復原前內容：" + 操作紀錄.peek());
        復原記錄.push(操作紀錄.pop());
        System.out.println("復原後內容：" + 操作紀錄.peek());
        System.out.println("復原記錄：" + 復原記錄);
        
        System.out.println("--- 重做操作 (Redo) ---");
        System.out.println("重做前內容：" + 操作紀錄.peek());
        操作紀錄.push(復原記錄.pop());
        System.out.println("重做後內容：" + 操作紀錄.peek());
        System.out.println("復原記錄：" + 復原記錄);
        
        System.out.println("執行新操作：打字「 Goodbye」");
        System.out.println("（新操作會清空復原記錄）");
        操作紀錄.push("Hello World!!! Goodbye");
        復原記錄.clear();  // 新操作清空復原記錄
        System.out.println("目前內容：" + 操作紀錄.peek());
        System.out.println("操作堆疊：" + 操作紀錄);
        System.out.println("復原記錄：" + 復原記錄);
        System.out.println();

        // ============================================
        // 總結報告
        // ============================================
        System.out.println("=== 總結報告 ===");
        System.out.println("┌──────────────┬─────────────┬─────────────────┬─────────────────┐");
        System.out.println("│ 需求編號     │ 需求說明     │ 選擇介面        │ 選擇實作        │");
        System.out.println("├──────────────┼─────────────┼─────────────────┼─────────────────┤");
        System.out.println("│ 1            │ 保留搜尋記錄 │ List            │ ArrayList       │");
        System.out.println("│              │ 允許重複     │                 │                 │");
        System.out.println("├──────────────┼─────────────┼─────────────────┼─────────────────┤");
        System.out.println("│ 2            │ 不重複會員   │ Set             │ HashSet         │");
        System.out.println("│              │ 號碼儲存     │                 │                 │");
        System.out.println("├──────────────┼─────────────┼─────────────────┼─────────────────┤");
        System.out.println("│ 3            │ 學號查詢成績 │ Map             │ HashMap         │");
        System.out.println("├──────────────┼─────────────┼─────────────────┼─────────────────┤");
        System.out.println("│ 4            │ 順序處理印刷 │ Queue           │ LinkedList      │");
        System.out.println("│              │ 工作         │                 │                 │");
        System.out.println("├──────────────┼─────────────┼─────────────────┼─────────────────┤");
        System.out.println("│ 5            │ 最近多次操作 │ Deque (Stack)   │ ArrayDeque      │");
        System.out.println("└──────────────┴─────────────┴─────────────────┴─────────────────┘");
        System.out.println();
        
        System.out.println("【選擇原則說明】");
        System.out.println("1. List (ArrayList)：需要保持順序且允許重複，適合記錄歷史搜尋");
        System.out.println("2. Set (HashSet)：需要自動去重，適合儲存唯一會員編號");
        System.out.println("3. Map (HashMap)：需要 Key-Value 快速查詢，適合學號對應成績");
        System.out.println("4. Queue (LinkedList)：需要先進先出，適合工作排程處理");
        System.out.println("5. Deque (ArrayDeque)：需要後進先出，適合復原/重做功能");
        System.out.println();
        
        System.out.println("=== 測試完成 ===");
    }
}