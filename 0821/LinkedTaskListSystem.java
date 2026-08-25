/**
 * 課後作業五：單向連結清單
 * 指定檔名：LinkedTaskListSystem.java
 * 
 * 手動實作 TaskNode 與 TaskLinkedList，不可使用 Java List 作為基礎架構。
 * 提供：
 * 1. addFirst(Task task)     - 在清單開頭新增任務
 * 2. addLast(Task task)      - 在清單尾端新增任務
 * 3. findById(String id)     - 依 ID 查詢任務
 * 4. removeById(String id)   - 依 ID 刪除任務
 * 5. insertAfter(String existingId, Task task) - 在指定節點後插入
 * 6. size()                  - 取得清單大小
 * 7. printAll()              - 印出所有任務
 * 
 * 重複 ID 不得加入。
 * 必須測試：空 list、刪除 head、刪除 middle、刪除 tail、找不到 id。
 */
public class LinkedTaskListSystem {

    public static void main(String[] args) {
        System.out.println("=== 單向連結清單（任務管理系統）測試 ===\n");

        // 建立任務連結清單
        TaskLinkedList 任務清單 = new TaskLinkedList();

        // ============================================
        // 測試 1：空清單操作
        // ============================================
        System.out.println("--- 測試 1：空清單操作 ---");
        System.out.println("初始大小：" + 任務清單.size());
        任務清單.printAll();
        任務清單.findById("T001");
        任務清單.removeById("T001");
        System.out.println();

        // ============================================
        // 測試 2：新增任務（addFirst / addLast）
        // ============================================
        System.out.println("--- 測試 2：新增任務 ---");
        任務清單.addFirst(new Task("T001", "撰寫需求文件", "高"));
        任務清單.addFirst(new Task("T002", "系統設計", "高"));
        任務清單.addLast(new Task("T003", "資料庫建置", "中"));
        任務清單.addLast(new Task("T004", "API 開發", "中"));
        任務清單.addFirst(new Task("T005", "專案規劃", "高"));
        System.out.println("新增 5 筆任務後，大小：" + 任務清單.size());
        任務清單.printAll();
        System.out.println();

        // ============================================
        // 測試 3：重複 ID 不得加入
        // ============================================
        System.out.println("--- 測試 3：重複 ID 不得加入 ---");
        任務清單.addFirst(new Task("T001", "重複任務", "低"));  // 應被拒絕
        任務清單.addLast(new Task("T003", "重複任務", "低"));   // 應被拒絕
        System.out.println("嘗試加入重複 ID 後，大小：" + 任務清單.size());
        任務清單.printAll();
        System.out.println();

        // ============================================
        // 測試 4：依 ID 查詢（findById）
        // ============================================
        System.out.println("--- 測試 4：依 ID 查詢 ---");
        任務清單.findById("T002");
        任務清單.findById("T004");
        任務清單.findById("T999");  // 不存在的 ID
        System.out.println();

        // ============================================
        // 測試 5：在指定節點後插入（insertAfter）
        // ============================================
        System.out.println("--- 測試 5：在指定節點後插入 ---");
        任務清單.insertAfter("T002", new Task("T006", "前端開發", "中"));
        任務清單.insertAfter("T004", new Task("T007", "測試部署", "低"));
        任務清單.insertAfter("T999", new Task("T008", "不應加入", "低"));  // 找不到 existingId
        任務清單.insertAfter("T005", new Task("T009", "需求訪談", "高"));
        System.out.println("插入 3 筆任務後，大小：" + 任務清單.size());
        任務清單.printAll();
        System.out.println();

        // ============================================
        // 測試 6：刪除任務（removeById）- 刪除 head、middle、tail
        // ============================================
        System.out.println("--- 測試 6：刪除任務（head / middle / tail）---");
        
        // 刪除 head（T005）
        System.out.println("刪除 head（T005）：");
        任務清單.removeById("T005");
        任務清單.printAll();
        System.out.println();

        // 刪除 middle（T006）
        System.out.println("刪除 middle（T006）：");
        任務清單.removeById("T006");
        任務清單.printAll();
        System.out.println();

        // 刪除 tail（T007）
        System.out.println("刪除 tail（T007）：");
        任務清單.removeById("T007");
        任務清單.printAll();
        System.out.println();

        // 刪除不存在的 ID
        System.out.println("刪除不存在的 ID（T999）：");
        任務清單.removeById("T999");
        任務清單.printAll();
        System.out.println();

        // ============================================
        // 測試 7：剩餘任務操作驗證
        // ============================================
        System.out.println("--- 測試 7：驗證剩餘任務 ---");
        System.out.println("目前大小：" + 任務清單.size());
        System.out.println("查詢 T001：" + 任務清單.findById("T001"));
        System.out.println("查詢 T002：" + 任務清單.findById("T002"));
        System.out.println("查詢 T003：" + 任務清單.findById("T003"));
        System.out.println("查詢 T004：" + 任務清單.findById("T004"));
        System.out.println("查詢 T009：" + 任務清單.findById("T009"));
        任務清單.printAll();
        System.out.println();

        // ============================================
        // 測試 8：連續刪除所有節點（驗證空清單）
        // ============================================
        System.out.println("--- 測試 8：連續刪除所有節點 ---");
        任務清單.removeById("T001");
        任務清單.removeById("T002");
        任務清單.removeById("T003");
        任務清單.removeById("T004");
        任務清單.removeById("T009");
        System.out.println("刪除所有節點後，大小：" + 任務清單.size());
        任務清單.printAll();
        System.out.println();

        // ============================================
        // 測試 9：空清單再次操作（驗證穩定性）
        // ============================================
        System.out.println("--- 測試 9：空清單再次操作 ---");
        任務清單.addFirst(new Task("T010", "重新開始", "高"));
        任務清單.addLast(new Task("T011", "第二個任務", "中"));
        任務清單.printAll();
        任務清單.removeById("T010");
        任務清單.removeById("T011");
        System.out.println("清空後大小：" + 任務清單.size());
        任務清單.printAll();

        System.out.println("\n=== 測試完成 ===");
    }
}

/**
 * 任務類別：儲存任務資訊
 */
class Task {
    private String id;
    private String 名稱;
    private String 優先級;

    public Task(String id, String 名稱, String 優先級) {
        this.id = id;
        this.名稱 = 名稱;
        this.優先級 = 優先級;
    }

    public String getId() {
        return id;
    }

    public String 取得名稱() {
        return 名稱;
    }

    public String 取得優先級() {
        return 優先級;
    }

    @Override
    public String toString() {
        return id + " | " + 名稱 + " | 優先級：" + 優先級;
    }
}

/**
 * 任務節點類別：單向連結清單的節點
 */
class TaskNode {
    private Task task;
    private TaskNode next;

    public TaskNode(Task task) {
        this.task = task;
        this.next = null;
    }

    public Task getTask() {
        return task;
    }

    public TaskNode getNext() {
        return next;
    }

    public void setNext(TaskNode next) {
        this.next = next;
    }

    public String getId() {
        return task.getId();
    }

    @Override
    public String toString() {
        return task.toString();
    }
}

/**
 * 任務連結清單類別：手動實作單向連結清單
 * 不可使用 Java List 作為基礎架構
 */
class TaskLinkedList {
    private TaskNode head;
    private int size;

    public TaskLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * 1. 在清單開頭新增任務
     * 重複 ID 不得加入
     */
    public void addFirst(Task task) {
        if (findNodeById(task.getId()) != null) {
            System.out.println("⚠ 新增失敗：ID「" + task.getId() + "」已存在，不得重複加入");
            return;
        }

        TaskNode newNode = new TaskNode(task);
        newNode.setNext(head);
        head = newNode;
        size++;
        System.out.println("✅ addFirst 成功：「" + task + "」");
    }

    /**
     * 2. 在清單尾端新增任務
     * 重複 ID 不得加入
     */
    public void addLast(Task task) {
        if (findNodeById(task.getId()) != null) {
            System.out.println("⚠ 新增失敗：ID「" + task.getId() + "」已存在，不得重複加入");
            return;
        }

        TaskNode newNode = new TaskNode(task);

        if (head == null) {
            head = newNode;
        } else {
            TaskNode current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
        System.out.println("✅ addLast 成功：「" + task + "」");
    }

    /**
     * 3. 依 ID 查詢任務
     */
    public Task findById(String id) {
        TaskNode node = findNodeById(id);
        if (node == null) {
            System.out.println("🔍 查無 ID：「" + id + "」");
            return null;
        }
        System.out.println("🔍 找到 ID：「" + id + "」→ " + node.getTask());
        return node.getTask();
    }

    /**
     * 4. 依 ID 刪除任務
     * 測試情境：刪除 head、刪除 middle、刪除 tail、找不到 id
     */
    public boolean removeById(String id) {
        if (head == null) {
            System.out.println("⚠ 刪除失敗：清單為空，無法刪除 ID「" + id + "」");
            return false;
        }

        // 情況 1：刪除 head
        if (head.getId().equals(id)) {
            System.out.println("🗑 刪除 head 成功：「" + head.getTask() + "」");
            head = head.getNext();
            size--;
            return true;
        }

        // 情況 2：刪除 middle 或 tail
        TaskNode current = head;
        while (current.getNext() != null && !current.getNext().getId().equals(id)) {
            current = current.getNext();
        }

        if (current.getNext() == null) {
            System.out.println("⚠ 刪除失敗：找不到 ID「" + id + "」");
            return false;
        }

        // 判斷是否為 tail
        String 位置 = (current.getNext().getNext() == null) ? "tail" : "middle";
        System.out.println("🗑 刪除 " + 位置 + " 成功：「" + current.getNext().getTask() + "」");
        current.setNext(current.getNext().getNext());
        size--;
        return true;
    }

    /**
     * 5. 在指定節點後插入新任務
     */
    public void insertAfter(String existingId, Task task) {
        // 檢查新任務是否重複
        if (findNodeById(task.getId()) != null) {
            System.out.println("⚠ insertAfter 失敗：ID「" + task.getId() + "」已存在，不得重複加入");
            return;
        }

        TaskNode existingNode = findNodeById(existingId);
        if (existingNode == null) {
            System.out.println("⚠ insertAfter 失敗：找不到 existingId「" + existingId + "」");
            return;
        }

        TaskNode newNode = new TaskNode(task);
        newNode.setNext(existingNode.getNext());
        existingNode.setNext(newNode);
        size++;
        System.out.println("✅ insertAfter 成功：在 ID「" + existingId + "」後插入「" + task + "」");
    }

    /**
     * 6. 取得清單大小
     */
    public int size() {
        return size;
    }

    /**
     * 7. 印出所有任務
     */
    public void printAll() {
        if (head == null) {
            System.out.println("📋 任務清單：（空）");
            return;
        }

        System.out.println("📋 任務清單（共 " + size + " 筆，由頭至尾）：");
        TaskNode current = head;
        int 序號 = 1;
        while (current != null) {
            System.out.println("   " + 序號++ + ". " + current);
            current = current.getNext();
        }
    }

    /**
     * 輔助方法：依 ID 尋找節點
     */
    private TaskNode findNodeById(String id) {
        TaskNode current = head;
        while (current != null) {
            if (current.getId().equals(id)) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }
}