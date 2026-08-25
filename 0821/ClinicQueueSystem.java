import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 課後作業二：實習掛號系統
 * 指定檔名：ClinicQueueSystem.java
 * 
 * 建立 Patient 與診所隊列，完成：
 * 1. 一般掛號（加入等候隊列）
 * 2. 取消指定病歷號（從隊列中移除指定患者）
 * 3. 叫號（服務下一位患者，先進先出）
 * 4. 查看下一位（顯示隊首患者，不移除）
 * 5. 當日完成清單（記錄已看診完畢的患者）
 * 
 * 一般叫號必須維持先進先出（FIFO）。
 */
public class ClinicQueueSystem {

    public static void main(String[] args) {
        System.out.println("=== 診所掛號系統測試 ===\n");

        Clinic 診所 = new Clinic();

        // === 測試一般掛號 ===
        System.out.println("--- 一般掛號 ---");
        診所.一般掛號(new Patient("王小明", "A001"));
        診所.一般掛號(new Patient("陳小華", "A002"));
        診所.一般掛號(new Patient("李小英", "A003"));
        診所.一般掛號(new Patient("張大偉", "A004"));
        診所.一般掛號(new Patient("林小美", "A005"));
        診所.顯示完整狀態();
        System.out.println();

        // === 測試查看下一位 ===
        System.out.println("--- 查看下一位 ---");
        診所.查看下一位();
        System.out.println();

        // === 測試取消指定病歷號 ===
        System.out.println("--- 取消指定病歷號 ---");
        診所.取消掛號("A003"); // 取消中間的患者
        診所.取消掛號("A001"); // 取消隊首的患者
        診所.取消掛號("A999"); // 取消不存在的病歷號
        診所.顯示完整狀態();
        System.out.println();

        // === 測試叫號（先進先出） ===
        System.out.println("--- 叫號服務 ---");
        診所.叫號(); // 應服務 A002（最早掛號且未被取消）
        診所.叫號(); // 應服務 A004
        診所.叫號(); // 應服務 A005
        診所.叫號(); // 測試空隊列叫號
        診所.顯示完整狀態();
        System.out.println();

        // === 測試當日完成清單 ===
        System.out.println("--- 當日完成清單 ---");
        診所.顯示完成清單();
        System.out.println();

        // === 繼續掛號與叫號混合測試 ===
        System.out.println("--- 混合測試（掛號 + 叫號）---");
        診所.一般掛號(new Patient("吳大志", "A006"));
        診所.一般掛號(new Patient("鄭小芬", "A007"));
        診所.查看下一位();
        診所.叫號(); // 服務 A006
        診所.一般掛號(new Patient("黃小龍", "A008"));
        診所.查看下一位();
        診所.叫號(); // 服務 A007
        診所.叫號(); // 服務 A008
        診所.顯示完整狀態();
        診所.顯示完成清單();

        System.out.println("\n=== 測試完成 ===");
    }
}

/**
 * 患者類別：儲存患者基本資料
 */
class Patient {
    private String 姓名;
    private String 病歷號;

    public Patient(String 姓名, String 病歷號) {
        this.姓名 = 姓名;
        this.病歷號 = 病歷號;
    }

    public String 取得姓名() {
        return 姓名;
    }

    public String 取得病歷號() {
        return 病歷號;
    }

    @Override
    public String toString() {
        return 姓名 + "(" + 病歷號 + ")";
    }
}

/**
 * 診所類別：管理掛號、叫號、取消、完成清單
 */
class Clinic {
    // 等候隊列（先進先出）
    private Queue<Patient> 等候隊列;
    // 當日完成清單（記錄已看診患者）
    private Deque<Patient> 完成清單;

    public Clinic() {
        等候隊列 = new LinkedList<>(); // 使用 LinkedList 實作 FIFO 隊列
        完成清單 = new ArrayDeque<>(); // 使用 Deque 儲存完成記錄
    }

    /**
     * 1. 一般掛號：將患者加入等候隊列尾端
     */
    public void 一般掛號(Patient 患者) {
        等候隊列.offer(患者);
        System.out.println("✅ 掛號成功：「" + 患者 + "」已加入等候隊列");
    }

    /**
     * 2. 取消指定病歷號：從等候隊列中移除指定患者
     * 若患者不在隊列中，顯示提示訊息
     */
    public void 取消掛號(String 病歷號) {
        // 由於 Queue 不支援直接移除指定元素（需遍歷），使用暫存隊列
        Queue<Patient> 暫存隊列 = new LinkedList<>();
        boolean 找到 = false;

        while (!等候隊列.isEmpty()) {
            Patient 當前患者 = 等候隊列.poll();
            if (當前患者.取得病歷號().equals(病歷號)) {
                找到 = true;
                System.out.println("❌ 取消掛號：「" + 當前患者 + "」已從等候隊列移除");
                // 不將此患者加入暫存隊列（即移除）
            } else {
                暫存隊列.offer(當前患者);
            }
        }

        // 將暫存隊列中的患者移回等候隊列
        等候隊列 = 暫存隊列;

        if (!找到) {
            System.out.println("⚠ 取消掛號失敗：病歷號「" + 病歷號 + "」不在等候隊列中");
        }
    }

    /**
     * 3. 叫號：服務下一位患者（先進先出）
     * 將患者從等候隊列移除，加入完成清單
     */
    public void 叫號() {
        Patient 患者 = 等候隊列.poll();
        if (患者 == null) {
            System.out.println("⚠ 叫號失敗：目前沒有患者在等候");
            return;
        }
        System.out.println("🛎 叫號：「" + 患者 + "」請看診");
        // 將看診完畢的患者加入完成清單
        完成清單.offerLast(患者);
    }

    /**
     * 4. 查看下一位：顯示隊首患者（不移除）
     */
    public void 查看下一位() {
        Patient 患者 = 等候隊列.peek();
        if (患者 == null) {
            System.out.println("ℹ 目前沒有患者在等候");
        } else {
            System.out.println("👀 下一位等候患者：「" + 患者 + "」");
        }
    }

    /**
     * 5. 顯示當日完成清單
     */
    public void 顯示完成清單() {
        if (完成清單.isEmpty()) {
            System.out.println("📋 當日完成清單：（尚無患者完成看診）");
        } else {
            System.out.println("📋 當日完成清單（共 " + 完成清單.size() + " 位）：");
            int 序號 = 1;
            for (Patient 患者 : 完成清單) {
                System.out.println("   " + 序號++ + ". " + 患者);
            }
        }
    }

    /**
     * 顯示完整狀態（等候隊列 + 完成清單）
     */
    public void 顯示完整狀態() {
        System.out.println("   📊 等候人數：" + 等候隊列.size());
        System.out.print("   📋 等候隊列（由前至後）：");
        if (等候隊列.isEmpty()) {
            System.out.println("（空）");
        } else {
            for (Patient 患者 : 等候隊列) {
                System.out.print(" 「" + 患者 + "」");
            }
            System.out.println();
        }
        System.out.println("   📋 完成人數：" + 完成清單.size());
        System.out.println("   ------------------------------------");
    }
}