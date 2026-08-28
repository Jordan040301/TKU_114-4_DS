/**
 * 檔名：orderBstsystem.java
 * 功能：訂單索引系統（使用 BST 管理訂單）
 * 說明：訂單以 orderId 排序
 *       完成新增、尋找、取消、更新數量、範圍報告與總結
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 訂單類別
 */
class Order {
    private String orderId;      // 訂單編號（唯一識別）
    private String customerName; // 客戶姓名
    private String productName;  // 商品名稱
    private int quantity;        // 數量
    private double unitPrice;    // 單價
    private String orderDate;    // 訂單日期
    private String status;       // 狀態：待處理、已出貨、已取消

    public Order(String orderId, String customerName, String productName, 
                 int quantity, double unitPrice, String orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderDate = orderDate;
        this.status = "待處理";
    }

    // ========== Getter 方法 ==========
    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return quantity * unitPrice;
    }

    // ========== Setter 方法 ==========
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 取消訂單
     */
    public void cancel() {
        this.status = "已取消";
    }

    @Override
    public String toString() {
        return String.format("訂單編號：%-8s | 客戶：%-6s | 商品：%-10s | 數量：%3d | 單價：%7.2f | 總價：%8.2f | 狀態：%s",
                             orderId, customerName, productName, quantity, unitPrice, 
                             getTotalPrice(), status);
    }

    /**
     * 簡易格式（用於報表）
     */
    public String toReportString() {
        return String.format("%-8s %-6s %-10s %3d %7.2f %8.2f %s",
                             orderId, customerName, productName, quantity, 
                             unitPrice, getTotalPrice(), status);
    }
}

/**
 * BST 節點（儲存訂單）
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

    public String getOrderId() {
        return order.getOrderId();
    }
}

/**
 * 訂單索引 BST
 */
class OrderBST {
    private OrderNode root;
    private int size;
    private StringBuilder operationLog;

    public OrderBST() {
        this.root = null;
        this.size = 0;
        this.operationLog = new StringBuilder();
    }

    // ========== 日誌記錄 ==========

    private void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String logEntry = "[" + timestamp + "] " + message;
        operationLog.append(logEntry).append("\n");
        System.out.println("  📝 " + logEntry);
    }

    public String getOperationLog() {
        return operationLog.toString();
    }

    public void clearLog() {
        operationLog = new StringBuilder();
    }

    // ========== 基本方法 ==========

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    // ========== 功能一：新增訂單 ==========

    /**
     * 新增訂單
     * @param order 要新增的訂單
     * @return true 表示新增成功，false 表示訂單編號已存在
     */
    public boolean addOrder(Order order) {
        // 檢查訂單編號是否已存在
        if (findOrder(order.getOrderId()) != null) {
            log("新增訂單失敗：訂單編號 " + order.getOrderId() + " 已存在！");
            return false;
        }

        root = addOrderRec(root, order);
        size++;
        log("新增訂單成功：" + order.getOrderId() + " | " + order.getCustomerName() + 
            " | " + order.getProductName() + " x" + order.getQuantity());
        return true;
    }

    private OrderNode addOrderRec(OrderNode node, Order order) {
        if (node == null) {
            return new OrderNode(order);
        }

        String newId = order.getOrderId();
        String currentId = node.getOrderId();

        if (newId.compareTo(currentId) < 0) {
            node.left = addOrderRec(node.left, order);
        } else if (newId.compareTo(currentId) > 0) {
            node.right = addOrderRec(node.right, order);
        }
        return node;
    }

    // ========== 功能二：尋找訂單 ==========

    /**
     * 依訂單編號尋找訂單
     * @param orderId 訂單編號
     * @return 找到的訂單物件，若找不到則回傳 null
     */
    public Order findOrder(String orderId) {
        OrderNode result = findOrderRec(root, orderId);
        if (result != null) {
            log("尋找訂單 " + orderId + " → ✅ 找到");
            return result.order;
        } else {
            log("尋找訂單 " + orderId + " → ❌ 找不到");
            return null;
        }
    }

    private OrderNode findOrderRec(OrderNode node, String orderId) {
        if (node == null) {
            return null;
        }

        int compare = orderId.compareTo(node.getOrderId());

        if (compare == 0) {
            return node;
        } else if (compare < 0) {
            return findOrderRec(node.left, orderId);
        } else {
            return findOrderRec(node.right, orderId);
        }
    }

    /**
     * 尋找訂單並顯示詳細資訊
     */
    public void findAndDisplay(String orderId) {
        System.out.println("🔍 尋找訂單：" + orderId);
        Order order = findOrder(orderId);
        if (order != null) {
            System.out.println("   ✅ 找到訂單：");
            System.out.println("      " + order);
        } else {
            System.out.println("   ❌ 找不到訂單編號 " + orderId);
        }
        System.out.println();
    }

    // ========== 功能三：取消訂單 ==========

    /**
     * 取消訂單
     * @param orderId 訂單編號
     * @return true 表示取消成功，false 表示訂單不存在或已取消
     */
    public boolean cancelOrder(String orderId) {
        log("嘗試取消訂單：" + orderId);

        Order order = findOrder(orderId);
        if (order == null) {
            log("取消訂單失敗：訂單 " + orderId + " 不存在！");
            return false;
        }

        if (order.getStatus().equals("已取消")) {
            log("取消訂單失敗：訂單 " + orderId + " 已取消！");
            return false;
        }

        order.cancel();
        log("取消訂單成功：" + orderId + " | " + order.getCustomerName() + 
            " | " + order.getProductName() + " x" + order.getQuantity());
        return true;
    }

    // ========== 功能四：更新訂單數量 ==========

    /**
     * 更新訂單數量
     * @param orderId 訂單編號
     * @param newQuantity 新的數量
     * @return true 表示更新成功，false 表示訂單不存在或數量無效
     */
    public boolean updateQuantity(String orderId, int newQuantity) {
        log("更新訂單數量：" + orderId + " → " + newQuantity);

        if (newQuantity <= 0) {
            log("更新失敗：數量必須大於 0！");
            return false;
        }

        Order order = findOrder(orderId);
        if (order == null) {
            log("更新失敗：訂單 " + orderId + " 不存在！");
            return false;
        }

        if (order.getStatus().equals("已取消")) {
            log("更新失敗：訂單 " + orderId + " 已取消，無法修改！");
            return false;
        }

        int oldQuantity = order.getQuantity();
        order.setQuantity(newQuantity);
        log("更新成功：" + orderId + " | 數量：" + oldQuantity + " → " + newQuantity);
        return true;
    }

    // ========== 功能五：刪除訂單 ==========

    /**
     * 刪除訂單
     * @param orderId 訂單編號
     * @return 被刪除的訂單物件，若找不到則回傳 null
     */
    public Order deleteOrder(String orderId) {
        log("刪除訂單：" + orderId);

        Order order = findOrder(orderId);
        if (order == null) {
            log("刪除失敗：訂單 " + orderId + " 不存在！");
            return null;
        }

        root = deleteOrderRec(root, orderId);
        size--;
        log("刪除成功：" + orderId + " | " + order.getCustomerName() + 
            " | " + order.getProductName());
        return order;
    }

    private OrderNode deleteOrderRec(OrderNode node, String orderId) {
        if (node == null) {
            return null;
        }

        int compare = orderId.compareTo(node.getOrderId());

        if (compare < 0) {
            node.left = deleteOrderRec(node.left, orderId);
        } else if (compare > 0) {
            node.right = deleteOrderRec(node.right, orderId);
        } else {
            // 找到要刪除的節點

            // 情況 1：葉子節點
            if (node.left == null && node.right == null) {
                return null;
            }

            // 情況 2：只有右子樹
            if (node.left == null) {
                return node.right;
            }

            // 情況 3：只有左子樹
            if (node.right == null) {
                return node.left;
            }

            // 情況 4：有兩個子樹
            OrderNode successor = findMin(node.right);
            node.order = successor.order;
            node.right = deleteOrderRec(node.right, successor.getOrderId());
        }

        return node;
    }

    private OrderNode findMin(OrderNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // ========== 功能六：範圍報告 ==========

    /**
     * 範圍查詢（依訂單編號範圍）
     * @param startId 起始訂單編號（包含）
     * @param endId 結束訂單編號（包含）
     * @return 符合範圍的訂單列表
     */
    public List<Order> rangeQuery(String startId, String endId) {
        log("範圍查詢：[" + startId + " ~ " + endId + "]");

        List<Order> result = new ArrayList<>();

        if (startId.compareTo(endId) > 0) {
            log("範圍查詢失敗：起始編號大於結束編號！");
            return result;
        }

        rangeQueryRec(root, startId, endId, result);
        log("範圍查詢完成：找到 " + result.size() + " 筆訂單");
        return result;
    }

    private void rangeQueryRec(OrderNode node, String startId, String endId, List<Order> result) {
        if (node == null) {
            return;
        }

        String currentId = node.getOrderId();

        // 利用 BST 特性剪枝
        if (currentId.compareTo(startId) > 0) {
            rangeQueryRec(node.left, startId, endId, result);
        }

        if (currentId.compareTo(startId) >= 0 && currentId.compareTo(endId) <= 0) {
            result.add(node.order);
        }

        if (currentId.compareTo(endId) < 0) {
            rangeQueryRec(node.right, startId, endId, result);
        }
    }

    /**
     * 輸出範圍報告
     */
    public void printRangeReport(String startId, String endId) {
        System.out.println("=========================================");
        System.out.println("📊 訂單範圍報告：[" + startId + " ~ " + endId + "]");
        System.out.println("-----------------------------------------");

        List<Order> orders = rangeQuery(startId, endId);

        if (orders.isEmpty()) {
            System.out.println("（此範圍內無任何訂單）");
        } else {
            System.out.printf("%-8s %-6s %-10s %3s %7s %8s %s\n",
                             "訂單編號", "客戶", "商品", "數量", "單價", "總價", "狀態");
            System.out.println("-----------------------------------------");
            double total = 0;
            int totalQuantity = 0;
            for (Order order : orders) {
                System.out.println("  " + order.toReportString());
                total += order.getTotalPrice();
                totalQuantity += order.getQuantity();
            }
            System.out.println("-----------------------------------------");
            System.out.println("訂單筆數：" + orders.size());
            System.out.println("商品總數量：" + totalQuantity);
            System.out.printf("訂單總金額：%.2f\n", total);
        }
        System.out.println("=========================================");
        System.out.println();
    }

    // ========== 功能七：總結報告 ==========

    /**
     * 產生完整總結報告
     */
    public void generateSummaryReport() {
        System.out.println("=========================================");
        System.out.println("        📊 訂單系統總結報告");
        System.out.println("=========================================");
        System.out.println("報告產生時間：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println("-----------------------------------------");

        if (root == null) {
            System.out.println("（目前無任何訂單）");
        } else {
            List<Order> allOrders = new ArrayList<>();
            collectAllOrders(root, allOrders);

            // 統計資訊
            int totalOrders = allOrders.size();
            int pendingCount = 0;
            int shippedCount = 0;
            int cancelledCount = 0;
            double totalRevenue = 0;
            int totalItems = 0;

            for (Order order : allOrders) {
                switch (order.getStatus()) {
                    case "待處理":
                        pendingCount++;
                        break;
                    case "已出貨":
                        shippedCount++;
                        break;
                    case "已取消":
                        cancelledCount++;
                        break;
                }
                if (!order.getStatus().equals("已取消")) {
                    totalRevenue += order.getTotalPrice();
                }
                totalItems += order.getQuantity();
            }

            System.out.println("📋 訂單總覽（依訂單編號排序）：");
            System.out.printf("%-8s %-6s %-10s %3s %7s %8s %s\n",
                             "訂單編號", "客戶", "商品", "數量", "單價", "總價", "狀態");
            System.out.println("-----------------------------------------");
            for (Order order : allOrders) {
                System.out.println("  " + order.toReportString());
            }

            System.out.println("-----------------------------------------");
            System.out.println("📈 統計資訊：");
            System.out.println("   ● 總訂單數：" + totalOrders);
            System.out.println("   ● 待處理訂單：" + pendingCount);
            System.out.println("   ● 已出貨訂單：" + shippedCount);
            System.out.println("   ● 已取消訂單：" + cancelledCount);
            System.out.println("   ● 商品總數量：" + totalItems);
            System.out.printf("   ● 總營收（不含取消）：%.2f\n", totalRevenue);
            if (totalOrders > 0) {
                double avgOrderValue = totalRevenue / (totalOrders - cancelledCount);
                System.out.printf("   ● 平均訂單金額：%.2f\n", avgOrderValue);
            }
        }
        System.out.println("=========================================");
        System.out.println();
    }

    private void collectAllOrders(OrderNode node, List<Order> list) {
        if (node != null) {
            collectAllOrders(node.left, list);
            list.add(node.order);
            collectAllOrders(node.right, list);
        }
    }

    // ========== 顯示樹結構（輔助） ==========

    public void printTreeStructure() {
        System.out.println("樹的結構（訂單編號）：");
        printTreeStructureRec(root, 0, "根");
        System.out.println();
    }

    private void printTreeStructureRec(OrderNode node, int level, String direction) {
        if (node == null) {
            return;
        }
        String indent = "  ".repeat(level);
        System.out.println(indent + direction + ": " + node.getOrderId() + 
                           " (" + node.order.getCustomerName() + ", " + 
                           node.order.getProductName() + " x" + node.order.getQuantity() + ")");
        printTreeStructureRec(node.left, level + 1, "左");
        printTreeStructureRec(node.right, level + 1, "右");
    }

    /**
     * 顯示操作日誌
     */
    public void printOperationLog() {
        System.out.println("=========================================");
        System.out.println("📋 操作日誌");
        System.out.println("=========================================");
        System.out.println(operationLog.toString());
        System.out.println("=========================================");
        System.out.println();
    }
}

/**
 * 主程式
 */
public class OrderBstSystem {
    private static final String SEPARATOR = "=========================================";

    public static void main(String[] args) {
        OrderBST orderSystem = new OrderBST();

        System.out.println(SEPARATOR);
        System.out.println("        訂單索引系統");
        System.out.println(SEPARATOR);
        System.out.println("功能列表：");
        System.out.println("  1. 新增訂單");
        System.out.println("  2. 尋找訂單");
        System.out.println("  3. 取消訂單");
        System.out.println("  4. 更新數量");
        System.out.println("  5. 範圍報告");
        System.out.println("  6. 總結報告");
        System.out.println(SEPARATOR);
        System.out.println();

        // =========================================================
        // 功能一：新增訂單
        // =========================================================
        System.out.println("【功能一：新增訂單】");
        System.out.println("-----------------------------------------");

        // 建立訂單
        Order o1 = new Order("ORD001", "王小明", "筆記型電腦", 2, 25000.0, "2026-08-01");
        Order o2 = new Order("ORD002", "陳小華", "無線滑鼠", 5, 599.0, "2026-08-02");
        Order o3 = new Order("ORD003", "林小美", "機械鍵盤", 3, 1299.0, "2026-08-03");
        Order o4 = new Order("ORD004", "張小強", "咖啡機", 1, 4500.0, "2026-08-04");
        Order o5 = new Order("ORD005", "李小雨", "除濕機", 2, 6990.0, "2026-08-05");
        Order o6 = new Order("ORD006", "黃小光", "運動鞋", 4, 2500.0, "2026-08-06");
        Order o7 = new Order("ORD007", "周小婷", "棒球帽", 10, 399.0, "2026-08-07");
        Order o8 = new Order("ORD008", "吳小龍", "藍芽耳機", 3, 1890.0, "2026-08-08");
        Order o9 = new Order("ORD009", "鄭小華", "行動電源", 5, 899.0, "2026-08-09");
        Order o10 = new Order("ORD010", "林小芳", "隨身碟", 8, 499.0, "2026-08-10");

        // 新增訂單
        orderSystem.addOrder(o1);
        orderSystem.addOrder(o2);
        orderSystem.addOrder(o3);
        orderSystem.addOrder(o4);
        orderSystem.addOrder(o5);
        orderSystem.addOrder(o6);
        orderSystem.addOrder(o7);
        orderSystem.addOrder(o8);
        orderSystem.addOrder(o9);
        orderSystem.addOrder(o10);

        System.out.println();
        orderSystem.printTreeStructure();

        // =========================================================
        // 測試重複新增
        // =========================================================
        System.out.println("【測試：重複新增】");
        System.out.println("-----------------------------------------");
        Order duplicate = new Order("ORD005", "李小雨", "除濕機", 1, 6990.0, "2026-08-11");
        orderSystem.addOrder(duplicate);
        System.out.println();

        // =========================================================
        // 功能二：尋找訂單
        // =========================================================
        System.out.println("【功能二：尋找訂單】");
        System.out.println("-----------------------------------------");
        orderSystem.findAndDisplay("ORD003");
        orderSystem.findAndDisplay("ORD007");
        orderSystem.findAndDisplay("ORD020");

        // =========================================================
        // 功能三：更新數量
        // =========================================================
        System.out.println("【功能三：更新數量】");
        System.out.println("-----------------------------------------");
        orderSystem.updateQuantity("ORD001", 3);   // 筆電改為 3 台
        orderSystem.updateQuantity("ORD004", 2);   // 咖啡機改為 2 台
        orderSystem.updateQuantity("ORD020", 1);   // 不存在的訂單
        orderSystem.updateQuantity("ORD002", -1);  // 無效數量
        System.out.println();

        // =========================================================
        // 功能四：取消訂單
        // =========================================================
        System.out.println("【功能四：取消訂單】");
        System.out.println("-----------------------------------------");
        orderSystem.cancelOrder("ORD006");  // 取消運動鞋訂單
        orderSystem.cancelOrder("ORD008");  // 取消藍芽耳機訂單
        orderSystem.cancelOrder("ORD006");  // 重複取消
        orderSystem.cancelOrder("ORD020");  // 不存在的訂單
        System.out.println();

        // =========================================================
        // 功能五：範圍報告
        // =========================================================
        System.out.println("【功能五：範圍報告】");
        System.out.println("-----------------------------------------");

        // 查詢 ORD001 ~ ORD005
        orderSystem.printRangeReport("ORD001", "ORD005");

        // 查詢 ORD006 ~ ORD010
        orderSystem.printRangeReport("ORD006", "ORD010");

        // 查詢 ORD003 ~ ORD007
        orderSystem.printRangeReport("ORD003", "ORD007");

        // 測試錯誤範圍
        orderSystem.printRangeReport("ORD010", "ORD001");

        // =========================================================
        // 功能六：總結報告
        // =========================================================
        System.out.println("【功能六：總結報告】");
        System.out.println("-----------------------------------------");
        orderSystem.generateSummaryReport();

        // =========================================================
        // 顯示操作日誌
        // =========================================================
        System.out.println("【操作日誌】");
        System.out.println("-----------------------------------------");
        orderSystem.printOperationLog();

        // =========================================================
        // 總結
        // =========================================================
        System.out.println(SEPARATOR);
        System.out.println("        📊 系統操作總結");
        System.out.println(SEPARATOR);
        System.out.println("新增訂單數：" + 10);
        System.out.println("重複新增嘗試：" + 1 + "（被拒絕）");
        System.out.println("成功更新數量：" + 2);
        System.out.println("成功取消訂單：" + 2);
        System.out.println("目前總訂單數：" + orderSystem.getSize());
        System.out.println(SEPARATOR);
    }
}