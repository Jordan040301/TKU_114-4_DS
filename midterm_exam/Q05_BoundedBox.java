/**
 * 檔名：Q05_BoundedBox.java
 * 功能：有界且有限的泛型容器
 * 說明：泛型 T 必須實作 Comparable 介面
 *       支援容量限制、最小/最大值、計數和快照
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Q05_BoundedBox<T extends Comparable<T>> {
    
    // ========== 私有欄位 ==========
    private final int capacity;      // 容量上限
    private final List<T> items;     // 儲存元素（保留加入順序）

    /**
     * 建構子
     * @param capacity 容量上限
     * @throws IllegalArgumentException 當 capacity 小於 1 時
     */
    public Q05_BoundedBox(int capacity) {
        // Capacity 小於 1 時拋出 IllegalArgumentException
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity 必須大於等於 1");
        }
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    /**
     * 預設建構子（容量為 10）
     */
    public Q05_BoundedBox() {
        this(10);
    }

    // ========== 主要方法 ==========

    /**
     * 新增元素
     * @param value 要新增的元素
     * @return true 表示新增成功，false 表示 value 為 null 或已達容量上限
     */
    public boolean add(T value) {
        // 拒絕 null
        if (value == null) {
            return false;
        }

        // 拒絕超過容量
        if (items.size() >= capacity) {
            return false;
        }

        // 新增元素
        items.add(value);
        return true;
    }

    /**
     * 取得目前元素數量
     * @return 元素數量
     */
    public int size() {
        return items.size();
    }

    /**
     * 檢查是否為空
     * @return true 表示沒有元素，false 表示有元素
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * 取得最小值
     * @return 最小值（empty box 時回傳 null）
     */
    public T minimum() {
        // empty box 回傳 null
        if (isEmpty()) {
            return null;
        }

        // 使用 compareTo() 找最小值
        T min = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            T current = items.get(i);
            if (current.compareTo(min) < 0) {
                min = current;
            }
        }
        return min;
    }

    /**
     * 取得最大值
     * @return 最大值（empty box 時回傳 null）
     */
    public T maximum() {
        // empty box 回傳 null
        if (isEmpty()) {
            return null;
        }

        // 使用 compareTo() 找最大值
        T max = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            T current = items.get(i);
            if (current.compareTo(max) > 0) {
                max = current;
            }
        }
        return max;
    }

    /**
     * 計算大於 threshold 的元素數量
     * @param threshold 比較基準值
     * @return 大於 threshold 的數量（threshold 為 null 時回傳 0）
     */
    public int countGreaterThan(T threshold) {
        // threshold 為 null 時回傳 0
        if (threshold == null) {
            return 0;
        }

        int count = 0;
        for (T item : items) {
            if (item.compareTo(threshold) > 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * 取得目前所有元素的快照（保留加入順序）
     * @return 不可修改的元素列表（caller 修改回傳 List 時不能影響 box）
     */
    public List<T> snapshot() {
        // 回傳不可修改的 List，防止 caller 修改內部資料
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    // ========== 額外輔助方法（方便測試） ==========

    /**
     * 取得容量上限
     * @return 容量上限
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * 檢查是否已滿
     * @return true 表示已滿，false 表示還有空間
     */
    public boolean isFull() {
        return items.size() >= capacity;
    }

    /**
     * 清空所有元素
     */
    public void clear() {
        items.clear();
    }

    @Override
    public String toString() {
        return items.toString();
    }

    // ========== 測試主程式 ==========
    public static void main(String[] args) {
        System.out.println("===== 測試範例 =====");
        Q05_BoundedBox<Integer> box = new Q05_BoundedBox<>(3);
        System.out.println(box.add(40));   // true
        System.out.println(box.add(10));   // true
        System.out.println(box.add(30));   // true
        System.out.println(box.add(20));   // false（已滿）
        System.out.println(box.minimum()); // 10
        System.out.println(box.maximum()); // 40
        System.out.println(box.countGreaterThan(25)); // 2（40 和 30）
        System.out.println(box.snapshot()); // [40, 10, 30]
        System.out.println();

        // ===== 測試容量驗證 =====
        System.out.println("===== 容量驗證測試 =====");
        try {
            Q05_BoundedBox<String> box2 = new Q05_BoundedBox<>(0);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ capacity=0 時拋出例外：" + e.getMessage());
        }

        try {
            Q05_BoundedBox<String> box3 = new Q05_BoundedBox<>(-5);
            System.out.println("不應該執行到這裡");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ capacity=-5 時拋出例外：" + e.getMessage());
        }

        System.out.println("✅ capacity=1 正常建立");
        Q05_BoundedBox<String> box4 = new Q05_BoundedBox<>(1);
        System.out.println();

        // ===== 測試 null 處理 =====
        System.out.println("===== null 處理測試 =====");
        Q05_BoundedBox<String> box5 = new Q05_BoundedBox<>(3);
        System.out.println("add(null) → " + box5.add(null)); // false
        System.out.println("size() → " + box5.size());       // 0
        System.out.println("isEmpty() → " + box5.isEmpty()); // true
        System.out.println("minimum() → " + box5.minimum()); // null
        System.out.println("maximum() → " + box5.maximum()); // null
        System.out.println("countGreaterThan(null) → " + box5.countGreaterThan(null)); // 0
        System.out.println();

        // ===== 測試容量限制 =====
        System.out.println("===== 容量限制測試 =====");
        Q05_BoundedBox<Integer> box6 = new Q05_BoundedBox<>(3);
        System.out.println("add(1): " + box6.add(1));   // true
        System.out.println("add(2): " + box6.add(2));   // true
        System.out.println("add(3): " + box6.add(3));   // true
        System.out.println("add(4): " + box6.add(4));   // false（已滿）
        System.out.println("size(): " + box6.size());   // 3
        System.out.println("isFull(): " + box6.isFull()); // true
        System.out.println("內容: " + box6.snapshot()); // [1, 2, 3]
        System.out.println();

        // ===== 測試最小值/最大值（非數字型別） =====
        System.out.println("===== 最小值/最大值測試（字串） =====");
        Q05_BoundedBox<String> box7 = new Q05_BoundedBox<>(5);
        box7.add("banana");
        box7.add("apple");
        box7.add("cherry");
        box7.add("date");
        System.out.println("內容: " + box7.snapshot()); // [banana, apple, cherry, date]
        System.out.println("最小值（字典序）: " + box7.minimum()); // apple
        System.out.println("最大值（字典序）: " + box7.maximum()); // date
        System.out.println();

        // ===== 測試 countGreaterThan =====
        System.out.println("===== countGreaterThan 測試 =====");
        Q05_BoundedBox<Integer> box8 = new Q05_BoundedBox<>(10);
        int[] values = {5, 10, 15, 20, 25, 30, 35, 40};
        for (int v : values) {
            box8.add(v);
        }
        System.out.println("內容: " + box8.snapshot());
        System.out.println("countGreaterThan(20) → " + box8.countGreaterThan(20)); // 4（25,30,35,40）
        System.out.println("countGreaterThan(30) → " + box8.countGreaterThan(30)); // 2（35,40）
        System.out.println("countGreaterThan(50) → " + box8.countGreaterThan(50)); // 0
        System.out.println("countGreaterThan(null) → " + box8.countGreaterThan(null)); // 0
        System.out.println();

        // ===== 測試 snapshot() 封裝 =====
        System.out.println("===== snapshot() 封裝測試 =====");
        Q05_BoundedBox<Integer> box9 = new Q05_BoundedBox<>(5);
        box9.add(10);
        box9.add(20);
        box9.add(30);

        List<Integer> snapshot = box9.snapshot();
        System.out.println("原始 snapshot: " + snapshot); // [10, 20, 30]
        System.out.println("原始 box 內容: " + box9.snapshot()); // [10, 20, 30]

        // 嘗試修改 snapshot（應該拋出例外）
        try {
            snapshot.add(40);
            System.out.println("不應該執行到這裡");
        } catch (UnsupportedOperationException e) {
            System.out.println("✅ 無法修改 snapshot（UnsupportedOperationException）");
        }

        // 驗證 box 內容未被修改
        System.out.println("修改後 box 內容不變: " + box9.snapshot()); // [10, 20, 30]

        // 測試 snapshot 的獨立性（clone）
        List<Integer> snapshot2 = box9.snapshot();
        System.out.println("snapshot2: " + snapshot2);
        // 修改 box 內容
        box9.add(40);
        System.out.println("box 新增 40 後: " + box9.snapshot()); // [10, 20, 30, 40]
        System.out.println("snapshot2 不變: " + snapshot2); // [10, 20, 30]
        System.out.println();

        // ===== 測試 isEmpty() 和 size() =====
        System.out.println("===== isEmpty() 和 size() 測試 =====");
        Q05_BoundedBox<Double> box10 = new Q05_BoundedBox<>(5);
        System.out.println("新 box - isEmpty(): " + box10.isEmpty()); // true
        System.out.println("新 box - size(): " + box10.size());       // 0
        box10.add(1.5);
        box10.add(2.7);
        System.out.println("加入 2 個元素後 - isEmpty(): " + box10.isEmpty()); // false
        System.out.println("加入 2 個元素後 - size(): " + box10.size());       // 2
        System.out.println();

        // ===== 測試邊界情況 =====
        System.out.println("===== 邊界情況測試 =====");
        Q05_BoundedBox<Integer> box11 = new Q05_BoundedBox<>(1);
        System.out.println("capacity=1");
        System.out.println("add(100): " + box11.add(100)); // true
        System.out.println("add(200): " + box11.add(200)); // false
        System.out.println("size(): " + box11.size());     // 1
        System.out.println("minimum(): " + box11.minimum()); // 100
        System.out.println("maximum(): " + box11.maximum()); // 100
        System.out.println("countGreaterThan(50): " + box11.countGreaterThan(50)); // 1
        System.out.println("countGreaterThan(150): " + box11.countGreaterThan(150)); // 0
    }
}