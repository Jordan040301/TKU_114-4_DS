import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 課堂實踐題一：列表實現比較
 * 指定檔名：ListImplementationLab.java
 * 
 * 撰寫一個只接收 List<Integer> 的方法，完成：
 * 1. 尾端新增
 * 2. 指定位置插入
 * 3. 搜尋（尋找元素索引）
 * 4. 刪除（依索引刪除）
 * 5. 總和（所有元素加總）
 * 
 * 分別以 ArrayList 與 LinkedList 測試，確認功能結果一致，
 * 最後以文字說明兩者內部成本差異。
 */
public class ListImplementationLab {

    public static void main(String[] args) {
        // 測試 ArrayList
        List<Integer> arrayList = new ArrayList<>();
        System.out.println("=== ArrayList 測試 ===");
        testListOperations(arrayList);

        // 測試 LinkedList
        List<Integer> linkedList = new LinkedList<>();
        System.out.println("\n=== LinkedList 測試 ===");
        testListOperations(linkedList);
    }

    /**
     * 只接收 List<Integer> 的方法，執行五項操作並印出結果
     */
    public static void testListOperations(List<Integer> list) {
        // 1. 尾端新增
        list.add(10);
        list.add(20);
        list.add(30);
        System.out.println("尾端新增 10, 20, 30 後：" + list);

        // 2. 指定位置插入（在索引 1 插入 99）
        list.add(1, 99);
        System.out.println("在索引 1 插入 99 後：" + list);

        // 3. 搜尋（尋找元素 20 的索引）
        int index = list.indexOf(20);
        System.out.println("搜尋元素 20 的索引：" + index);

        // 4. 刪除（依索引刪除，刪除索引 2 的元素）
        int removed = list.remove(2);
        System.out.println("刪除索引 2 的元素（值=" + removed + "），刪除後：" + list);

        // 5. 總和
        int sum = 0;
        for (int num : list) {
            sum += num;
        }
        System.out.println("目前列表總和：" + sum);
    }
}