public class BookArrayReport {
    
    public static void main(String[] args) {
        // 建立 Book[] 保存至少四本书
        Book[] books = new Book[] {
            new Book("B001", "Java程式設計", 450.0, 5),
            new Book("B002", "數據結構与算法", 380.0, 2),
            new Book("B003", "數據庫系統概念", 520.0, 8),
            new Book("B004", "操作系統原理", 420.0, 1),
            new Book("B005", "計算機網路", 350.0, 3)
        };
        
        System.out.println("========== 1. 輸出所有書籍 ==========");
        printAllBooks(books);
        
        System.out.println("\n========== 2. 計算庫存總價值 ==========");
        double totalValue = calculateTotalValue(books);
        System.out.printf("庫存總價值：%.2f 元%n", totalValue);
        
        System.out.println("\n========== 3. 找出價格最高的書 ==========");
        Book mostExpensiveBook = findMostExpensiveBook(books);
        System.out.println("價格最高的書：" + mostExpensiveBook);
        
        System.out.println("\n========== 4. 輸出庫存小於或等于3的書 ==========");
        printLowStockBooks(books);
    }
    
    /**
     * 输出所有书籍
     */
    public static void printAllBooks(Book[] books) {
        for (int i = 0; i < books.length; i++) {
            System.out.println(books[i]);
        }
    }
    
    /**
     * 计算库存总价值 (price * stock)
     */
    public static double calculateTotalValue(Book[] books) {
        double total = 0.0;
        for (Book book : books) {
            total += book.getPrice() * book.getStock();
        }
        return total;
    }
    
    /**
     * 找出价格最高的书
     */
    public static Book findMostExpensiveBook(Book[] books) {
        if (books == null || books.length == 0) {
            return null;
        }
        
        Book mostExpensive = books[0];
        for (int i = 1; i < books.length; i++) {
            if (books[i].getPrice() > mostExpensive.getPrice()) {
                mostExpensive = books[i];
            }
        }
        return mostExpensive;
    }
    
    /**
     * 输出库存小于或等于3的书
     */
    public static void printLowStockBooks(Book[] books) {
        boolean found = false;
        for (Book book : books) {
            if (book.getStock() <= 3) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("沒有庫存小於或等于3的書籍");
        }
    }
}

/**
 * Book 类 - 包含书号、书名、价格及库存
 */
class Book {
    private String bookId;      // 书号
    private String title;       // 书名
    private double price;       // 价格
    private int stock;          // 库存
    
    // 构造函数
    public Book(String bookId, String title, double price, int stock) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.stock = stock;
    }
    
    // Getter 方法
    public String getBookId() {
        return bookId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getStock() {
        return stock;
    }
    
    // Setter 方法（可选）
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    @Override
    public String toString() {
        return String.format("書號：%s，書名：%s，價格：%.2f元，庫存：%d本",
                           bookId, title, price, stock);
    }
}