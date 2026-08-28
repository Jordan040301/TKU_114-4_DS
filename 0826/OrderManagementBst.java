import java.util.*;
import java.util.stream.Collectors;

/**
 * 訂單狀態列舉
 */
enum OrderStatus {
    PENDING("待處理"),
    PROCESSING("處理中"),
    SHIPPED("已出貨"),
    DELIVERED("已送達"),
    CANCELLED("已取消");

    private String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}

/**
 * 訂單 (Order) 類別
 */
class Order {
    private int orderId;          // 訂單編號 (Key)
    private String customer;      // 客戶名稱
    private double amount;        // 訂單金額 (不得為負數)
    private OrderStatus status;   // 訂單狀態

    public Order(int orderId, String customer, double amount, OrderStatus status) {
        this.orderId = orderId;
        this.customer = customer;
        this.amount = amount;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public double getAmount() {
        return amount;
    }

    public boolean setAmount(double amount) {
        if (amount < 0) {
            return false;  // 金額不得為負數
        }
        this.amount = amount;
        return true;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return "Order{id=" + orderId + ", customer='" + customer + 
               "', amount=" + String.format("%.2f", amount) + 
               ", status=" + status + "}";
    }

    /**
     * 格式化輸出 (用於報表)
     */
    public String toFormattedString() {
        return String.format("%6d | %-12s | %10.2f | %s", 
                             orderId, customer, amount, status);
    }
}

/**
 * 二元搜尋樹節點 (儲存 Order 物件)
 */
class OrderNode {
    Order order;
    OrderNode left;
    OrderNode right;

    public OrderNode(Order order) {
        this.order = order;
        this.left = null;
        this.right = null;
    }

    public int getKey() {
        return order.getOrderId();
    }
}

/**
 * 訂單管理綜合系統 (Order Management BST)
 * 以 orderId 作為 key
 */
public class OrderManagementBst {
    private OrderNode root;
    private int size;

    public OrderManagementBst() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 新增訂單 (amount 不得為負數)
     */
    public boolean addOrder(int orderId, String customer, double amount, OrderStatus status) {
        // 檢查金額是否為負數
        if (amount < 0) {
            System.out.println("錯誤: 訂單金額 " + amount + " 不得為負數");
            return false;
        }

        // 檢查 orderId 是否已存在
        if (findOrder(orderId) != null) {
            System.out.println("錯誤: 訂單編號 " + orderId + " 已存在，不可重複新增");
            return false;
        }

        Order newOrder = new Order(orderId, customer, amount, status);
        root = addRecursive(root, newOrder);
        size++;
        System.out.println("成功新增訂單: " + newOrder);
        return true;
    }

    private OrderNode addRecursive(OrderNode node, Order order) {
        if (node == null) {
            return new OrderNode(order);
        }

        int key = order.getOrderId();
        if (key < node.getKey()) {
            node.left = addRecursive(node.left, order);
        } else if (key > node.getKey()) {
            node.right = addRecursive(node.right, order);
        }
        return node;
    }

    /**
     * 根據 orderId 尋找訂單
     */
    public Order findOrder(int orderId) {
        OrderNode result = findRecursive(root, orderId);
        return result != null ? result.order : null;
    }

    private OrderNode findRecursive(OrderNode node, int orderId) {
        if (node == null) {
            return null;
        }

        if (orderId == node.getKey()) {
            return node;
        }
        if (orderId < node.getKey()) {
            return findRecursive(node.left, orderId);
        } else {
            return findRecursive(node.right, orderId);
        }
    }

    /**
     * 更新訂單狀態
     */
    public boolean updateStatus(int orderId, OrderStatus newStatus) {
        OrderNode node = findRecursive(root, orderId);
        if (node == null) {
            System.out.println("錯誤: 找不到訂單編號 " + orderId);
            return false;
        }

        OrderStatus oldStatus = node.order.getStatus();
        node.order.setStatus(newStatus);
        System.out.println("成功更新狀態: 訂單 " + orderId + " 從 '" + oldStatus + "' 改為 '" + newStatus + "'");
        return true;
    }

    /**
     * 更新訂單金額 (amount 不得為負數)
     */
    public boolean updateAmount(int orderId, double newAmount) {
        if (newAmount < 0) {
            System.out.println("錯誤: 訂單金額 " + newAmount + " 不得為負數");
            return false;
        }

        OrderNode node = findRecursive(root, orderId);
        if (node == null) {
            System.out.println("錯誤: 找不到訂單編號 " + orderId);
            return false;
        }

        double oldAmount = node.order.getAmount();
        node.order.setAmount(newAmount);
        System.out.println("成功更新金額: 訂單 " + orderId + " 從 " + 
                           String.format("%.2f", oldAmount) + " 改為 " + 
                           String.format("%.2f", newAmount));
        return true;
    }

    /**
     * 更新客戶名稱
     */
    public boolean updateCustomer(int orderId, String newCustomer) {
        OrderNode node = findRecursive(root, orderId);
        if (node == null) {
            System.out.println("錯誤: 找不到訂單編號 " + orderId);
            return false;
        }

        String oldCustomer = node.order.getCustomer();
        node.order.setCustomer(newCustomer);
        System.out.println("成功更新客戶: 訂單 " + orderId + " 從 '" + oldCustomer + "' 改為 '" + newCustomer + "'");
        return true;
    }

    /**
     * 取消訂單 (將狀態設為 CANCELLED)
     */
    public boolean cancelOrder(int orderId) {
        return updateStatus(orderId, OrderStatus.CANCELLED);
    }

    /**
     * 移除訂單 (只有 CANCELLED 訂單可以移除)
     */
    public boolean removeOrder(int orderId) {
        OrderNode node = findRecursive(root, orderId);
        if (node == null) {
            System.out.println("錯誤: 找不到訂單編號 " + orderId);
            return false;
        }

        // 檢查是否為 CANCELLED 狀態
        if (!node.order.isCancelled()) {
            System.out.println("錯誤: 訂單 " + orderId + " 狀態為 '" + node.order.getStatus() + 
                               "'，只有 CANCELLED 訂單可以移除");
            return false;
        }

        Order removedOrder = node.order;
        root = removeRecursive(root, orderId);
        size--;
        System.out.println("成功移除訂單: " + removedOrder);
        return true;
    }

    private OrderNode removeRecursive(OrderNode node, int orderId) {
        if (node == null) {
            return null;
        }

        if (orderId < node.getKey()) {
            node.left = removeRecursive(node.left, orderId);
        } else if (orderId > node.getKey()) {
            node.right = removeRecursive(node.right, orderId);
        } else {
            // Case 1: 葉節點
            if (node.left == null && node.right == null) {
                return null;
            }
            // Case 2: 只有一個子節點
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            // Case 3: 有兩個子節點
            int successorKey = findMinKey(node.right);
            OrderNode successorNode = findRecursive(node.right, successorKey);
            node.order = successorNode.order;
            node.right = removeRecursive(node.right, successorKey);
        }
        return node;
    }

    private int findMinKey(OrderNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.getKey();
    }

    /**
     * 檢查訂單是否存在
     */
    public boolean contains(int orderId) {
        return findOrder(orderId) != null;
    }

    /**
     * 取得訂單總數
     */
    public int getSize() {
        return size;
    }

    /**
     * 計算所有訂單總金額
     */
    public double getTotalAmount() {
        return getTotalAmountRecursive(root);
    }

    private double getTotalAmountRecursive(OrderNode node) {
        if (node == null) {
            return 0;
        }
        return node.order.getAmount() + 
               getTotalAmountRecursive(node.left) + 
               getTotalAmountRecursive(node.right);
    }

    /**
     * 計算指定狀態的訂單總金額
     */
    public double getTotalAmountByStatus(OrderStatus status) {
        return getTotalAmountByStatusRecursive(root, status);
    }

    private double getTotalAmountByStatusRecursive(OrderNode node, OrderStatus status) {
        if (node == null) {
            return 0;
        }
        double amount = (node.order.getStatus() == status) ? node.order.getAmount() : 0;
        return amount + 
               getTotalAmountByStatusRecursive(node.left, status) + 
               getTotalAmountByStatusRecursive(node.right, status);
    }

    /**
     * 中序走訪 (按 orderId 排序)
     */
    public List<Order> inorderReport() {
        List<Order> result = new ArrayList<>();
        inorderRecursive(root, result);
        return result;
    }

    private void inorderRecursive(OrderNode node, List<Order> result) {
        if (node != null) {
            inorderRecursive(node.left, result);
            result.add(node.order);
            inorderRecursive(node.right, result);
        }
    }

    /**
     * orderId 範圍查詢 (Range Query)
     * 回傳 orderId 在 [low, high] 範圍內的所有訂單
     */
    public List<Order> rangeQuery(int low, int high) {
        List<Order> result = new ArrayList<>();
        if (low > high) {
            System.out.println("警告: low (" + low + ") > high (" + high + ")，範圍無效");
            return result;
        }
        rangeQueryRecursive(root, low, high, result);
        return result;
    }

    private void rangeQueryRecursive(OrderNode node, int low, int high, List<Order> result) {
        if (node == null) {
            return;
        }

        // 剪枝策略
        if (node.getKey() > low) {
            rangeQueryRecursive(node.left, low, high, result);
        }

        if (node.getKey() >= low && node.getKey() <= high) {
            result.add(node.order);
        }

        if (node.getKey() < high) {
            rangeQueryRecursive(node.right, low, high, result);
        }
    }

    /**
     * 印出訂單報表 (按 orderId 排序)
     */
    public void printInorderReport() {
        List<Order> orders = inorderReport();
        System.out.println("===== 訂單管理報表 (按訂單編號排序) =====");
        System.out.println("總訂單數: " + size);
        System.out.println("總金額: " + String.format("%.2f", getTotalAmount()));
        System.out.println();
        if (orders.isEmpty()) {
            System.out.println("(尚無訂單)");
        } else {
            System.out.println("訂單編號 | 客戶名稱      |      金額 | 狀態");
            System.out.println("---------+--------------+-----------+--------------");
            for (Order o : orders) {
                System.out.println(o.toFormattedString());
            }
        }
        System.out.println("==========================================");
    }

    /**
     * 印出範圍查詢結果
     */
    public void printRangeQuery(int low, int high) {
        List<Order> result = rangeQuery(low, high);
        System.out.println("===== 訂單編號範圍查詢: [" + low + ", " + high + "] =====");
        System.out.println("符合訂單數: " + result.size());
        System.out.println("範圍內總金額: " + String.format("%.2f", 
                           result.stream().mapToDouble(Order::getAmount).sum()));
        System.out.println();
        if (result.isEmpty()) {
            System.out.println("(無符合條件的訂單)");
        } else {
            System.out.println("訂單編號 | 客戶名稱      |      金額 | 狀態");
            System.out.println("---------+--------------+-----------+--------------");
            for (Order o : result) {
                System.out.println(o.toFormattedString());
            }
        }
        System.out.println("==========================================");
    }

    /**
     * 印出狀態統計
     */
    public void printStatusStatistics() {
        System.out.println("===== 訂單狀態統計 =====");
        for (OrderStatus status : OrderStatus.values()) {
            int count = countByStatus(status);
            double total = getTotalAmountByStatus(status);
            System.out.printf("  %s: %d 筆, 總金額 %.2f%n", status, count, total);
        }
        System.out.println("========================");
    }

    private int countByStatus(OrderStatus status) {
        return countByStatusRecursive(root, status);
    }

    private int countByStatusRecursive(OrderNode node, OrderStatus status) {
        if (node == null) {
            return 0;
        }
        int count = (node.order.getStatus() == status) ? 1 : 0;
        return count + countByStatusRecursive(node.left, status) + 
               countByStatusRecursive(node.right, status);
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       訂單管理綜合系統");
        System.out.println("========================================\n");

        OrderManagementBst orderMgr = new OrderManagementBst();

        // ========== 測試新增訂單 ==========
        System.out.println("【測試一】新增訂單");
        System.out.println("----------------------------------------");

        orderMgr.addOrder(1001, "王小明", 1500.00, OrderStatus.PENDING);
        orderMgr.addOrder(1002, "陳小華", 2300.50, OrderStatus.PENDING);
        orderMgr.addOrder(1003, "林小美", 3200.00, OrderStatus.PROCESSING);
        orderMgr.addOrder(1004, "張小強", 4100.75, OrderStatus.SHIPPED);
        orderMgr.addOrder(1005, "李小英", 5500.00, OrderStatus.DELIVERED);
        orderMgr.addOrder(1006, "吳大偉", 1800.00, OrderStatus.PENDING);
        orderMgr.addOrder(1007, "鄭小芳", 2900.00, OrderStatus.PROCESSING);
        orderMgr.addOrder(1008, "蔡小婷", 3600.50, OrderStatus.SHIPPED);
        orderMgr.addOrder(1009, "周小龍", 4700.00, OrderStatus.DELIVERED);
        orderMgr.addOrder(1010, "孫小鳳", 6200.00, OrderStatus.PENDING);
        System.out.println();

        orderMgr.printInorderReport();
        System.out.println();
        orderMgr.printStatusStatistics();
        System.out.println();

        // ========== 測試重複訂單編號 ==========
        System.out.println("【測試二】測試重複訂單編號 (不可重複)");
        System.out.println("----------------------------------------");
        orderMgr.addOrder(1001, "重複測試", 1000.00, OrderStatus.PENDING);
        System.out.println();

        // ========== 測試負數金額 ==========
        System.out.println("【測試三】測試負數金額 (不得為負數)");
        System.out.println("----------------------------------------");
        orderMgr.addOrder(1011, "測試負數", -500.00, OrderStatus.PENDING);
        System.out.println();

        // ========== 測試尋找訂單 ==========
        System.out.println("【測試四】尋找訂單");
        System.out.println("----------------------------------------");
        int searchId = 1005;
        Order found = orderMgr.findOrder(searchId);
        System.out.println("尋找訂單 " + searchId + ": " + (found != null ? found : "找不到"));

        searchId = 1099;
        found = orderMgr.findOrder(searchId);
        System.out.println("尋找訂單 " + searchId + ": " + (found != null ? found : "找不到"));
        System.out.println();

        // ========== 測試更新訂單狀態 ==========
        System.out.println("【測試五】更新訂單狀態");
        System.out.println("----------------------------------------");
        orderMgr.updateStatus(1001, OrderStatus.PROCESSING);
        orderMgr.updateStatus(1002, OrderStatus.SHIPPED);
        orderMgr.updateStatus(1003, OrderStatus.DELIVERED);
        orderMgr.updateStatus(1004, OrderStatus.DELIVERED);
        System.out.println();

        orderMgr.printInorderReport();
        System.out.println();

        // ========== 測試更新金額 ==========
        System.out.println("【測試六】更新訂單金額");
        System.out.println("----------------------------------------");
        orderMgr.updateAmount(1001, 1800.00);
        orderMgr.updateAmount(1005, 6000.00);
        System.out.println();

        // 測試更新為負數金額
        orderMgr.updateAmount(1001, -100.00);
        System.out.println();

        // ========== 測試更新客戶名稱 ==========
        System.out.println("【測試七】更新客戶名稱");
        System.out.println("----------------------------------------");
        orderMgr.updateCustomer(1001, "王大明");
        orderMgr.updateCustomer(1006, "吳建國");
        System.out.println();

        // ========== 測試取消訂單 ==========
        System.out.println("【測試八】取消訂單");
        System.out.println("----------------------------------------");
        orderMgr.cancelOrder(1008);
        orderMgr.cancelOrder(1009);
        orderMgr.cancelOrder(1010);
        System.out.println();

        orderMgr.printInorderReport();
        System.out.println();
        orderMgr.printStatusStatistics();
        System.out.println();

        // ========== 測試範圍查詢 ==========
        System.out.println("【測試九】訂單編號範圍查詢");
        System.out.println("----------------------------------------");

        orderMgr.printRangeQuery(1003, 1007);
        System.out.println();

        orderMgr.printRangeQuery(1001, 1002);
        System.out.println();

        orderMgr.printRangeQuery(1010, 1015);
        System.out.println();

        // ========== 測試移除訂單 (只有 CANCELLED 可移除) ==========
        System.out.println("【測試十】移除訂單 (只有 CANCELLED 可移除)");
        System.out.println("----------------------------------------");

        // 嘗試移除非 CANCELLED 訂單 (應失敗)
        orderMgr.removeOrder(1001);  // PROCESSING 狀態
        System.out.println();

        orderMgr.removeOrder(1002);  // SHIPPED 狀態
        System.out.println();

        // 移除 CANCELLED 訂單 (應成功)
        orderMgr.removeOrder(1008);  // CANCELLED 狀態
        System.out.println();

        orderMgr.removeOrder(1009);  // CANCELLED 狀態
        System.out.println();

        // 嘗試移除不存在的訂單
        orderMgr.removeOrder(1099);
        System.out.println();

        orderMgr.printInorderReport();
        System.out.println();
        orderMgr.printStatusStatistics();
        System.out.println();

        // ========== 新增更多訂單測試 ==========
        System.out.println("【測試十一】新增更多訂單");
        System.out.println("----------------------------------------");
        orderMgr.addOrder(1011, "劉家豪", 7800.00, OrderStatus.PENDING);
        orderMgr.addOrder(1012, "黃俊傑", 3200.00, OrderStatus.PENDING);
        orderMgr.addOrder(1013, "林美玲", 5400.00, OrderStatus.PROCESSING);
        System.out.println();

        orderMgr.printInorderReport();
        System.out.println();

        // ========== 最終測試 ==========
        System.out.println("【測試十二】最終綜合測試");
        System.out.println("----------------------------------------");

        System.out.println("當前總訂單數: " + orderMgr.getSize());
        System.out.println("總訂單金額: " + String.format("%.2f", orderMgr.getTotalAmount()));
        System.out.println();

        // 取消所有 PENDING 訂單
        List<Order> pendingOrders = orderMgr.inorderReport().stream()
            .filter(o -> o.getStatus() == OrderStatus.PENDING)
            .collect(Collectors.toList());
        for (Order o : pendingOrders) {
            orderMgr.cancelOrder(o.getOrderId());
        }
        System.out.println();

        // 移除所有 CANCELLED 訂單
        List<Order> cancelledOrders = orderMgr.inorderReport().stream()
            .filter(o -> o.getStatus() == OrderStatus.CANCELLED)
            .collect(Collectors.toList());
        for (Order o : cancelledOrders) {
            orderMgr.removeOrder(o.getOrderId());
        }
        System.out.println();

        orderMgr.printInorderReport();
        System.out.println();
        orderMgr.printStatusStatistics();
        System.out.println();

        System.out.println("========================================");
        System.out.println("         訂單管理系統執行完畢！");
        System.out.println("========================================");

        System.out.println("\n【功能總結】");
        System.out.println("1. 新增訂單 (addOrder): 使用 orderId 作為 key，amount 不得為負數");
        System.out.println("2. 尋找訂單 (findOrder): 根據 orderId 快速尋找");
        System.out.println("3. 更新狀態 (updateStatus): 更新指定訂單的狀態");
        System.out.println("4. 更新金額 (updateAmount): 更新指定訂單的金額 (不得為負數)");
        System.out.println("5. 更新客戶 (updateCustomer): 更新指定訂單的客戶名稱");
        System.out.println("6. 取消訂單 (cancelOrder): 將訂單狀態設為 CANCELLED");
        System.out.println("7. 移除訂單 (removeOrder): 只有 CANCELLED 訂單可以移除");
        System.out.println("8. 範圍查詢 (rangeQuery): 查詢 orderId 在指定範圍內的所有訂單");
        System.out.println("9. 總金額計算 (getTotalAmount): 計算所有訂單的總金額");
        System.out.println("10. 狀態統計: 按狀態分類統計訂單數量與金額");
    }
}