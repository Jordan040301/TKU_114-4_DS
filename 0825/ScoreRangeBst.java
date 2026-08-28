/**
 * 檔名：scoreRangeBst.java
 * 功能：成績排名範圍查詢系統
 * 說明：Key 使用 score + studentId 複合順序
 *       支援同分資料輸出並指定分數範圍查詢
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 學生分數類別（包含分數和學號）
 * 實作 Comparable 以支援複合排序
 */
class ScoreKey implements Comparable<ScoreKey> {
    private int score;          // 分數（主要排序鍵）
    private String studentId;   // 學號（次要排序鍵，用於同分區分）

    public ScoreKey(int score, String studentId) {
        this.score = score;
        this.studentId = studentId;
    }

    public int getScore() {
        return score;
    }

    public String getStudentId() {
        return studentId;
    }

    /**
     * 複合比較：先比分數（由高到低），再比學號（由小到大）
     * 注意：為了讓高分在前，分數比較時反向
     */
    @Override
    public int compareTo(ScoreKey other) {
        // 分數由高到低排序（高分在前）
        if (this.score != other.score) {
            return Integer.compare(other.score, this.score);
        }
        // 同分時，學號由小到大排序
        return this.studentId.compareTo(other.studentId);
    }

    @Override
    public String toString() {
        return "分數：" + score + "，學號：" + studentId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScoreKey that = (ScoreKey) obj;
        return score == that.score && studentId.equals(that.studentId);
    }

    @Override
    public int hashCode() {
        return 31 * score + studentId.hashCode();
    }
}

/**
 * 學生分數記錄
 */
class ScoreRecord {
    private ScoreKey key;       // 複合鍵值
    private String name;        // 姓名
    private String className;   // 班級

    public ScoreRecord(int score, String studentId, String name, String className) {
        this.key = new ScoreKey(score, studentId);
        this.name = name;
        this.className = className;
    }

    public ScoreKey getKey() {
        return key;
    }

    public int getScore() {
        return key.getScore();
    }

    public String getStudentId() {
        return key.getStudentId();
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return String.format("學號：%-10s | 姓名：%-6s | 班級：%-6s | 分數：%3d",
                             getStudentId(), name, className, getScore());
    }

    /**
     * 簡易格式（用於排名報表）
     */
    public String toRankString(int rank) {
        return String.format("%4d  | %-10s | %-6s | %-6s | %3d",
                             rank, getStudentId(), name, className, getScore());
    }
}

/**
 * BST 節點（儲存 ScoreRecord）
 */
class ScoreNode {
    ScoreRecord record;
    ScoreNode left;
    ScoreNode right;

    public ScoreNode(ScoreRecord record) {
        this.record = record;
        this.left = null;
        this.right = null;
    }

    public ScoreKey getKey() {
        return record.getKey();
    }
}

/**
 * 成績排名 BST
 */
class ScoreRankBST {
    private ScoreNode root;
    private int size;

    public ScoreRankBST() {
        this.root = null;
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    // ========== 插入方法 ==========

    /**
     * 插入學生分數記錄
     * @param record 分數記錄
     * @return true 表示插入成功，false 表示重複
     */
    public boolean insert(ScoreRecord record) {
        // 檢查是否重複
        if (search(record.getKey()) != null) {
            System.out.println("⚠️ 插入失敗：學生 " + record.getStudentId() + 
                               " 分數 " + record.getScore() + " 已存在！");
            return false;
        }

        root = insertRec(root, record);
        size++;
        System.out.println("✅ 插入成功：" + record);
        return true;
    }

    private ScoreNode insertRec(ScoreNode node, ScoreRecord record) {
        if (node == null) {
            return new ScoreNode(record);
        }

        ScoreKey newKey = record.getKey();
        ScoreKey currentKey = node.getKey();

        if (newKey.compareTo(currentKey) < 0) {
            node.left = insertRec(node.left, record);
        } else if (newKey.compareTo(currentKey) > 0) {
            node.right = insertRec(node.right, record);
        }
        return node;
    }

    // ========== 搜尋方法 ==========

    /**
     * 依複合鍵值搜尋
     */
    public ScoreRecord search(ScoreKey key) {
        ScoreNode result = searchRec(root, key);
        return result != null ? result.record : null;
    }

    private ScoreNode searchRec(ScoreNode node, ScoreKey key) {
        if (node == null) {
            return null;
        }

        int compare = key.compareTo(node.getKey());

        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return searchRec(node.left, key);
        } else {
            return searchRec(node.right, key);
        }
    }

    /**
     * 依學號搜尋（同一個學生可能有多個成績？但這裡假設一筆）
     */
    public ScoreRecord searchByStudentId(String studentId) {
        return searchByStudentIdRec(root, studentId);
    }

    private ScoreRecord searchByStudentIdRec(ScoreNode node, String studentId) {
        if (node == null) {
            return null;
        }

        // 中序遍歷搜尋（因為不是用學號當鍵值）
        ScoreRecord leftResult = searchByStudentIdRec(node.left, studentId);
        if (leftResult != null) return leftResult;

        if (node.record.getStudentId().equals(studentId)) {
            return node.record;
        }

        return searchByStudentIdRec(node.right, studentId);
    }

    // ========== 範圍查詢（核心功能） ==========

    /**
     * 查詢指定分數範圍內的學生（包含端點）
     * @param low  最低分數
     * @param high 最高分數
     * @return 符合條件的學生記錄列表（已排序）
     */
    public List<ScoreRecord> rangeQuery(int low, int high) {
        System.out.println("🔍 範圍查詢：分數 [" + low + ", " + high + "]");
        
        List<ScoreRecord> result = new ArrayList<>();
        
        if (low > high) {
            System.out.println("   ⚠️ 錯誤：最低分數大於最高分數！");
            return result;
        }

        rangeQueryRec(root, low, high, result);
        
        System.out.println("   📊 找到 " + result.size() + " 筆資料");
        return result;
    }

    private void rangeQueryRec(ScoreNode node, int low, int high, List<ScoreRecord> result) {
        if (node == null) {
            return;
        }

        int score = node.record.getScore();

        // 利用 BST 特性剪枝
        // 如果分數 > high，左子樹可能更小（因為分數由高到低排列）
        // 但因為我們是分數由高到低排序，邏輯要調整
        
        // 標準中序遍歷（因為 BST 分數由高到低）
        // 先遍歷左子樹（較低分數）→ 但這會破壞由高到低的順序
        
        // 為了讓輸出由高到低，我們先遍歷右子樹（較高分數）
        // 但這取決於 BST 的結構
        // 更簡單：全部蒐集後再排序
        
        // 這裡採用遞迴中序，但先右後左（由高到低）
        // 但要注意，為了正確性，我們使用標準中序 + 後續處理
        
        // 改為標準中序
        rangeQueryRec(node.left, low, high, result);
        
        if (score >= low && score <= high) {
            result.add(node.record);
        }
        
        rangeQueryRec(node.right, low, high, result);
    }

    /**
     * 範圍查詢並輸出詳細報表
     */
    public void printRangeReport(int low, int high) {
        System.out.println("=========================================");
        List<ScoreRecord> results = rangeQuery(low, high);
        System.out.println("-----------------------------------------");
        
        if (results.isEmpty()) {
            System.out.println("（此分數範圍內無任何學生）");
        } else {
            System.out.printf("%-4s | %-10s | %-6s | %-6s | %-3s\n",
                             "排名", "學號", "姓名", "班級", "分數");
            System.out.println("-----------------------------------------");
            
            // 計算排名從 1 開始（從高分到低分）
            // 為了正確排名，我們需要知道比當前分數高的有多少人
            // 簡單方式：把所有資料排序後再給排名
            
            // 但因為 BST 已經是排序的，我們可以遍歷取得排名
            // 更簡單：先取得所有資料（但範圍查詢已經取得）
            // 注意：範圍查詢回傳的結果是 sorted 的嗎？
            // 因為我們是用 inorder，所以從低分到高分
            // 但我們希望顯示時是高分到低分，所以反轉
            
            // 對結果進行排序（由高分到低分）
            results.sort((r1, r2) -> {
                if (r1.getScore() != r2.getScore()) {
                    return Integer.compare(r2.getScore(), r1.getScore());
                }
                return r1.getStudentId().compareTo(r2.getStudentId());
            });
            
            // 計算總排名（需要知道比當前分數高的總人數）
            int totalHigher = countHigherThan(root, high);
            
            for (int i = 0; i < results.size(); i++) {
                ScoreRecord r = results.get(i);
                int rank = totalHigher + i + 1;
                System.out.println("  " + r.toRankString(rank));
            }
        }
        System.out.println("-----------------------------------------");
        System.out.println("符合條件總數：" + results.size() + " 筆");
        System.out.println("=========================================");
        System.out.println();
    }

    /**
     * 計算分數高於指定值的節點數量
     */
    private int countHigherThan(ScoreNode node, int score) {
        if (node == null) {
            return 0;
        }
        int count = 0;
        if (node.record.getScore() > score) {
            count = 1 + countHigherThan(node.left, score) + countHigherThan(node.right, score);
        } else {
            // 如果當前分數 <= score，則右子樹（更小分數）不需要計算
            count = countHigherThan(node.left, score);
        }
        return count;
    }

    // ========== 統計方法 ==========

    /**
     * 取得最高分
     */
    public ScoreRecord getHighest() {
        if (root == null) return null;
        ScoreNode current = root;
        // 分數由高到低排序，最右邊的節點分數最低
        // 最左邊的節點分數最高（因為左子樹分數更高）
        while (current.left != null) {
            current = current.left;
        }
        return current.record;
    }

    /**
     * 取得最低分
     */
    public ScoreRecord getLowest() {
        if (root == null) return null;
        ScoreNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.record;
    }

    /**
     * 計算平均分數
     */
    public double getAverage() {
        if (root == null) return 0;
        int[] sumAndCount = new int[2];
        calculateAverage(root, sumAndCount);
        return sumAndCount[0] / (double) sumAndCount[1];
    }

    private void calculateAverage(ScoreNode node, int[] sumAndCount) {
        if (node == null) return;
        calculateAverage(node.left, sumAndCount);
        sumAndCount[0] += node.record.getScore();
        sumAndCount[1]++;
        calculateAverage(node.right, sumAndCount);
    }

    // ========== 排名查詢 ==========

    /**
     * 查詢某個學生的排名（從 1 開始，1 為最高分）
     */
    public int getRank(String studentId) {
        ScoreRecord record = searchByStudentId(studentId);
        if (record == null) {
            return -1;
        }
        
        // 計算分數高於該學生的總人數
        int higherCount = countHigherThan(root, record.getScore());
        
        // 處理同分情況：如果同分，排名相同，但為了顯示區分，我們用學號排序
        // 計算同分且學號更小的人數（因為學號由小到大）
        int sameScoreBetterId = countSameScoreWithBetterId(root, record.getScore(), record.getStudentId());
        
        return higherCount + sameScoreBetterId + 1;
    }

    private int countSameScoreWithBetterId(ScoreNode node, int score, String studentId) {
        if (node == null) return 0;
        int count = 0;
        if (node.record.getScore() == score && 
            node.record.getStudentId().compareTo(studentId) < 0) {
            count = 1;
        }
        return count + countSameScoreWithBetterId(node.left, score, studentId) +
               countSameScoreWithBetterId(node.right, score, studentId);
    }

    // ========== 報表輸出 ==========

    /**
     * 完整排名報表（所有學生由高分到低分）
     */
    public void printFullRankReport() {
        System.out.println("=========================================");
        System.out.println("        📊 成績排名總表");
        System.out.println("=========================================");
        System.out.println("排序方式：分數（高→低），同分則依學號（小→大）");
        System.out.println("-----------------------------------------");
        
        if (root == null) {
            System.out.println("（目前無任何學生資料）");
        } else {
            System.out.printf("%-4s | %-10s | %-6s | %-6s | %-3s\n",
                             "排名", "學號", "姓名", "班級", "分數");
            System.out.println("-----------------------------------------");
            
            List<ScoreRecord> allRecords = new ArrayList<>();
            collectAllRecords(root, allRecords);
            
            // 排序（由高分到低分）
            allRecords.sort((r1, r2) -> {
                if (r1.getScore() != r2.getScore()) {
                    return Integer.compare(r2.getScore(), r1.getScore());
                }
                return r1.getStudentId().compareTo(r2.getStudentId());
            });
            
            for (int i = 0; i < allRecords.size(); i++) {
                System.out.println("  " + allRecords.get(i).toRankString(i + 1));
            }
        }
        System.out.println("-----------------------------------------");
        System.out.println("總人數：" + size);
        System.out.printf("平均分數：%.2f\n", getAverage());
        System.out.println("=========================================");
        System.out.println();
    }

    private void collectAllRecords(ScoreNode node, List<ScoreRecord> list) {
        if (node == null) return;
        collectAllRecords(node.left, list);
        list.add(node.record);
        collectAllRecords(node.right, list);
    }

    /**
     * 顯示樹的結構（輔助觀察）
     */
    public void printTreeStructure() {
        System.out.println("樹的結構（分數由高到低）：");
        printTreeStructureRec(root, 0, "根");
        System.out.println();
    }

    private void printTreeStructureRec(ScoreNode node, int level, String direction) {
        if (node == null) return;
        String indent = "  ".repeat(level);
        System.out.println(indent + direction + ": " + node.record.getScore() + 
                           " (" + node.record.getStudentId() + ", " + node.record.getName() + ")");
        printTreeStructureRec(node.left, level + 1, "左(更高分)");
        printTreeStructureRec(node.right, level + 1, "右(更低分)");
    }
}

/**
 * 主程式
 */
public class ScoreRangeBst {
    public static void main(String[] args) {
        ScoreRankBST bst = new ScoreRankBST();

        System.out.println("=========================================");
        System.out.println("     成績排名範圍查詢系統");
        System.out.println("=========================================");
        System.out.println("排序鍵值：分數（高→低）+ 學號（小→大）");
        System.out.println();

        // =========================================================
        // 插入學生分數資料（包含同分情況）
        // =========================================================
        System.out.println("【插入學生資料】");
        System.out.println("-----------------------------------------");

        // 建立學生資料（包含許多同分的情況）
        ScoreRecord[] students = {
            new ScoreRecord(95, "S001", "王小明", "資工一"),
            new ScoreRecord(88, "S002", "陳小華", "資管一"),
            new ScoreRecord(92, "S003", "林小美", "資工一"),
            new ScoreRecord(88, "S004", "張小強", "電機一"),
            new ScoreRecord(76, "S005", "李小雨", "資管一"),
            new ScoreRecord(95, "S006", "黃小光", "資工一"),
            new ScoreRecord(82, "S007", "周小婷", "電機一"),
            new ScoreRecord(88, "S008", "吳小龍", "資工一"),
            new ScoreRecord(70, "S009", "鄭小華", "資管一"),
            new ScoreRecord(92, "S010", "林小芳", "電機一"),
            new ScoreRecord(76, "S011", "陳小強", "資工一"),
            new ScoreRecord(60, "S012", "王小美", "資管一"),
            new ScoreRecord(82, "S013", "李小華", "電機一"),
            new ScoreRecord(70, "S014", "張小龍", "資工一"),
            new ScoreRecord(88, "S015", "吳小美", "資管一"),
        };

        for (ScoreRecord record : students) {
            bst.insert(record);
        }

        System.out.println();
        bst.printTreeStructure();

        // =========================================================
        // 顯示完整排名報表
        // =========================================================
        System.out.println("【完整排名報表】");
        System.out.println("-----------------------------------------");
        bst.printFullRankReport();

        // =========================================================
        // 測試範圍查詢
        // =========================================================
        System.out.println("【範圍查詢測試】");
        System.out.println("-----------------------------------------");

        // 測試 1：查詢 90-100 分（高分群）
        bst.printRangeReport(90, 100);

        // 測試 2：查詢 80-89 分（中高分群）
        bst.printRangeReport(80, 89);

        // 測試 3：查詢 70-79 分（中低分群）
        bst.printRangeReport(70, 79);

        // 測試 4：查詢 60-69 分（低分群）
        bst.printRangeReport(60, 69);

        // 測試 5：查詢 50-59 分（無資料）
        bst.printRangeReport(50, 59);

        // 測試 6：查詢 95-95 分（單一分數）
        bst.printRangeReport(95, 95);

        // 測試 7：查詢 85-90 分（部分重疊）
        bst.printRangeReport(85, 90);

        // =========================================================
        // 測試錯誤處理：low > high
        // =========================================================
        System.out.println("【錯誤處理測試】");
        System.out.println("-----------------------------------------");
        bst.printRangeReport(80, 70);

        // =========================================================
        // 測試個別排名查詢
        // =========================================================
        System.out.println("【個別排名查詢】");
        System.out.println("-----------------------------------------");
        String[] testIds = {"S001", "S002", "S005", "S009", "S012", "S999"};
        for (String id : testIds) {
            int rank = bst.getRank(id);
            if (rank != -1) {
                ScoreRecord r = bst.searchByStudentId(id);
                System.out.println("  學號 " + id + "（" + r.getName() + "）→ 排名：" + rank + 
                                   "，分數：" + r.getScore());
            } else {
                System.out.println("  學號 " + id + " → 找不到該學生");
            }
        }

        System.out.println();

        // =========================================================
        // 統計資訊
        // =========================================================
        System.out.println("【統計資訊】");
        System.out.println("-----------------------------------------");
        System.out.println("  總學生數：" + bst.getSize());
        System.out.println("  最高分：" + (bst.getHighest() != null ? bst.getHighest().getScore() : "無"));
        System.out.println("  最低分：" + (bst.getLowest() != null ? bst.getLowest().getScore() : "無"));
        System.out.printf("  平均分數：%.2f\n", bst.getAverage());
        System.out.println("-----------------------------------------");
        System.out.println();

        // =========================================================
        // 總結
        // =========================================================
        System.out.println("=========================================");
        System.out.println("        📊 操作總結");
        System.out.println("=========================================");
        System.out.println("總學生數：" + bst.getSize());
        System.out.println("重複插入嘗試：0（全部成功）");
        System.out.println("同分學生數：多組（如 95 分有 2 人，88 分有 4 人）");
        System.out.println("範圍查詢測試：" + 7 + " 次");
        System.out.println("=========================================");
    }
}