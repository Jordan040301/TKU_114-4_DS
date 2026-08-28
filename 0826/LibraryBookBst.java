import java.util.*;

/**
 * 圖書 (Book) 類別
 */
class Book {
    private String isbn;          // ISBN (國際標準書號) - Key
    private String title;         // 書名
    private String author;        // 作者
    private boolean available;    // 是否可借閱 (true: 可借, false: 已借出)

    public Book(String isbn, String title, String author, boolean available) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * 借書 (借出)
     */
    public boolean borrow() {
        if (!available) {
            return false;  // 已借出
        }
        available = false;
        return true;
    }

    /**
     * 還書 (歸還)
     */
    public boolean returnBook() {
        if (available) {
            return false;  // 尚未借出
        }
        available = true;
        return true;
    }

    @Override
    public String toString() {
        return "Book{ISBN='" + isbn + "', title='" + title + 
               "', author='" + author + "', available=" + (available ? "可借" : "已借出") + "}";
    }

    /**
     * 格式化輸出 (用於報表)
     */
    public String toFormattedString() {
        String status = available ? "✓ 可借" : "✗ 已借出";
        return String.format("%-14s | %-25s | %-12s | %s", 
                             isbn, title, author, status);
    }
}

/**
 * 二元搜尋樹節點 (儲存 Book 物件)
 */
class BookNode {
    Book book;
    BookNode left;
    BookNode right;

    public BookNode(Book book) {
        this.book = book;
        this.left = null;
        this.right = null;
    }

    public String getKey() {
        return book.getIsbn();
    }
}

/**
 * 圖書館藏索引 (Library Book BST)
 * 以 ISBN 作為 key
 */
public class LibraryBookBst {
    private BookNode root;
    private int size;

    public LibraryBookBst() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 新增圖書
     */
    public boolean addBook(String isbn, String title, String author, boolean available) {
        // 檢查 ISBN 是否已存在 (不可重複)
        if (findBook(isbn) != null) {
            System.out.println("錯誤: ISBN " + isbn + " 已存在，不可重複新增");
            return false;
        }

        Book newBook = new Book(isbn, title, author, available);
        root = addRecursive(root, newBook);
        size++;
        System.out.println("成功新增圖書: " + newBook);
        return true;
    }

    private BookNode addRecursive(BookNode node, Book book) {
        if (node == null) {
            return new BookNode(book);
        }

        String key = book.getIsbn();
        if (key.compareTo(node.getKey()) < 0) {
            node.left = addRecursive(node.left, book);
        } else if (key.compareTo(node.getKey()) > 0) {
            node.right = addRecursive(node.right, book);
        }
        return node;
    }

    /**
     * 根據 ISBN 尋找圖書
     */
    public Book findBook(String isbn) {
        BookNode result = findRecursive(root, isbn);
        return result != null ? result.book : null;
    }

    private BookNode findRecursive(BookNode node, String isbn) {
        if (node == null) {
            return null;
        }

        int cmp = isbn.compareTo(node.getKey());
        if (cmp == 0) {
            return node;
        }
        if (cmp < 0) {
            return findRecursive(node.left, isbn);
        } else {
            return findRecursive(node.right, isbn);
        }
    }

    /**
     * 借書 (借出)
     */
    public boolean borrowBook(String isbn) {
        BookNode node = findRecursive(root, isbn);
        if (node == null) {
            System.out.println("錯誤: 找不到 ISBN " + isbn);
            return false;
        }

        if (!node.book.borrow()) {
            System.out.println("錯誤: 圖書 '" + node.book.getTitle() + "' (ISBN: " + isbn + ") 已借出，無法再借");
            return false;
        }

        System.out.println("成功借書: " + node.book.getTitle() + " (ISBN: " + isbn + ")");
        return true;
    }

    /**
     * 還書 (歸還)
     */
    public boolean returnBook(String isbn) {
        BookNode node = findRecursive(root, isbn);
        if (node == null) {
            System.out.println("錯誤: 找不到 ISBN " + isbn);
            return false;
        }

        if (!node.book.returnBook()) {
            System.out.println("錯誤: 圖書 '" + node.book.getTitle() + "' (ISBN: " + isbn + ") 尚未借出，無法歸還");
            return false;
        }

        System.out.println("成功還書: " + node.book.getTitle() + " (ISBN: " + isbn + ")");
        return true;
    }

    /**
     * 移除圖書 (借出中的書不得移除)
     */
    public boolean removeBook(String isbn) {
        BookNode node = findRecursive(root, isbn);
        if (node == null) {
            System.out.println("錯誤: 找不到 ISBN " + isbn);
            return false;
        }

        // 檢查是否已借出
        if (!node.book.isAvailable()) {
            System.out.println("錯誤: 圖書 '" + node.book.getTitle() + "' (ISBN: " + isbn + ") 已借出，不得移除");
            return false;
        }

        Book removedBook = node.book;
        root = removeRecursive(root, isbn);
        size--;
        System.out.println("成功移除圖書: " + removedBook);
        return true;
    }

    private BookNode removeRecursive(BookNode node, String isbn) {
        if (node == null) {
            return null;
        }

        int cmp = isbn.compareTo(node.getKey());
        if (cmp < 0) {
            node.left = removeRecursive(node.left, isbn);
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, isbn);
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
            String successorKey = findMinKey(node.right);
            BookNode successorNode = findRecursive(node.right, successorKey);
            node.book = successorNode.book;
            node.right = removeRecursive(node.right, successorKey);
        }
        return node;
    }

    private String findMinKey(BookNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.getKey();
    }

    /**
     * 檢查 ISBN 是否存在
     */
    public boolean contains(String isbn) {
        return findBook(isbn) != null;
    }

    /**
     * 取得館藏總數
     */
    public int getSize() {
        return size;
    }

    /**
     * 取得可借閱圖書數量
     */
    public int getAvailableCount() {
        return getAvailableCountRecursive(root);
    }

    private int getAvailableCountRecursive(BookNode node) {
        if (node == null) {
            return 0;
        }
        int count = node.book.isAvailable() ? 1 : 0;
        return count + getAvailableCountRecursive(node.left) + getAvailableCountRecursive(node.right);
    }

    /**
     * 取得已借出圖書數量
     */
    public int getBorrowedCount() {
        return size - getAvailableCount();
    }

    /**
     * 中序走訪 (按 ISBN 排序)
     */
    public List<Book> inorderReport() {
        List<Book> result = new ArrayList<>();
        inorderRecursive(root, result);
        return result;
    }

    private void inorderRecursive(BookNode node, List<Book> result) {
        if (node != null) {
            inorderRecursive(node.left, result);
            result.add(node.book);
            inorderRecursive(node.right, result);
        }
    }

    /**
     * ISBN 範圍查詢 (Range Query)
     * 回傳 ISBN 在 [low, high] 範圍內的所有圖書
     */
    public List<Book> rangeQuery(String low, String high) {
        List<Book> result = new ArrayList<>();
        if (low.compareTo(high) > 0) {
            System.out.println("警告: low (" + low + ") > high (" + high + ")，範圍無效");
            return result;
        }
        rangeQueryRecursive(root, low, high, result);
        return result;
    }

    private void rangeQueryRecursive(BookNode node, String low, String high, List<Book> result) {
        if (node == null) {
            return;
        }

        // 剪枝策略
        if (node.getKey().compareTo(low) > 0) {
            rangeQueryRecursive(node.left, low, high, result);
        }

        if (node.getKey().compareTo(low) >= 0 && node.getKey().compareTo(high) <= 0) {
            result.add(node.book);
        }

        if (node.getKey().compareTo(high) < 0) {
            rangeQueryRecursive(node.right, low, high, result);
        }
    }

    /**
     * 印出館藏報表 (按 ISBN 排序)
     */
    public void printInorderReport() {
        List<Book> books = inorderReport();
        System.out.println("===== 圖書館藏報表 (按 ISBN 排序) =====");
        System.out.println("總館藏數: " + size);
        System.out.println("可借數量: " + getAvailableCount());
        System.out.println("已借數量: " + getBorrowedCount());
        System.out.println();
        if (books.isEmpty()) {
            System.out.println("(館藏尚無書籍)");
        } else {
            System.out.println("ISBN           | 書名                       | 作者          | 狀態");
            System.out.println("---------------+----------------------------+---------------+------------");
            for (Book b : books) {
                System.out.println(b.toFormattedString());
            }
        }
        System.out.println("==========================================");
    }

    /**
     * 印出範圍查詢結果
     */
    public void printRangeQuery(String low, String high) {
        List<Book> result = rangeQuery(low, high);
        System.out.println("===== ISBN 範圍查詢: [" + low + ", " + high + "] =====");
        System.out.println("符合書籍數: " + result.size());
        if (result.isEmpty()) {
            System.out.println("(無符合條件的書籍)");
        } else {
            System.out.println("ISBN           | 書名                       | 作者          | 狀態");
            System.out.println("---------------+----------------------------+---------------+------------");
            for (Book b : result) {
                System.out.println(b.toFormattedString());
            }
        }
        System.out.println("==========================================");
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       圖書館藏索引系統");
        System.out.println("========================================\n");

        LibraryBookBst library = new LibraryBookBst();

        // ========== 測試新增圖書 ==========
        System.out.println("【測試一】新增圖書");
        System.out.println("----------------------------------------");

        library.addBook("978-986-123-001", "深入淺出 Java 程式設計", "王大明", true);
        library.addBook("978-986-123-002", "資料結構與演算法", "李華", true);
        library.addBook("978-986-123-003", "作業系統概念", "張志明", true);
        library.addBook("978-986-123-004", "計算機網路", "陳小芳", true);
        library.addBook("978-986-123-005", "資料庫系統概論", "林美玲", true);
        library.addBook("978-986-123-006", "軟體工程實務", "吳建國", true);
        library.addBook("978-986-123-007", "人工智慧導論", "鄭文華", true);
        library.addBook("978-986-123-008", "機器學習基礎", "蔡淑芬", true);
        library.addBook("978-986-123-009", "網頁程式設計", "黃俊傑", true);
        library.addBook("978-986-123-010", "行動應用開發", "劉家豪", true);
        System.out.println();

        library.printInorderReport();
        System.out.println();

        // ========== 測試重複 ISBN ==========
        System.out.println("【測試二】測試重複 ISBN (不可重複)");
        System.out.println("----------------------------------------");
        library.addBook("978-986-123-001", "重複測試", "測試作者", true);
        System.out.println();

        // ========== 測試尋找圖書 ==========
        System.out.println("【測試三】尋找圖書");
        System.out.println("----------------------------------------");
        String searchIsbn = "978-986-123-005";
        Book found = library.findBook(searchIsbn);
        System.out.println("尋找 ISBN " + searchIsbn + ": " + (found != null ? found : "找不到"));

        searchIsbn = "978-986-123-999";
        found = library.findBook(searchIsbn);
        System.out.println("尋找 ISBN " + searchIsbn + ": " + (found != null ? found : "找不到"));
        System.out.println();

        // ========== 測試借書 ==========
        System.out.println("【測試四】借書");
        System.out.println("----------------------------------------");
        library.borrowBook("978-986-123-001");  // 正常借書
        library.borrowBook("978-986-123-003");  // 正常借書
        library.borrowBook("978-986-123-005");  // 正常借書
        System.out.println();

        // 測試借出已借出的書
        library.borrowBook("978-986-123-001");  // 已借出
        System.out.println();

        // 測試借出不存在的書
        library.borrowBook("978-986-123-999");
        System.out.println();

        library.printInorderReport();
        System.out.println();

        // ========== 測試還書 ==========
        System.out.println("【測試五】還書");
        System.out.println("----------------------------------------");
        library.returnBook("978-986-123-001");  // 正常還書
        library.returnBook("978-986-123-003");  // 正常還書
        System.out.println();

        // 測試歸還尚未借出的書
        library.returnBook("978-986-123-002");  // 尚未借出
        System.out.println();

        // 測試歸還不存在的書
        library.returnBook("978-986-123-999");
        System.out.println();

        library.printInorderReport();
        System.out.println();

        // ========== 測試 ISBN 範圍查詢 ==========
        System.out.println("【測試六】ISBN 範圍查詢");
        System.out.println("----------------------------------------");

        library.printRangeQuery("978-986-123-003", "978-986-123-007");
        System.out.println();

        library.printRangeQuery("978-986-123-008", "978-986-123-015");
        System.out.println();

        library.printRangeQuery("978-986-123-001", "978-986-123-001");
        System.out.println();

        // ========== 測試移除圖書 ==========
        System.out.println("【測試七】移除圖書");
        System.out.println("----------------------------------------");

        // 移除可借閱的圖書 (已歸還)
        library.removeBook("978-986-123-001");  // 可移除 (已歸還)
        System.out.println();

        // 嘗試移除已借出的圖書 (不得移除)
        library.borrowBook("978-986-123-003");  // 先借出
        library.removeBook("978-986-123-003");  // 借出中，不得移除
        System.out.println();

        // 先還書再移除
        library.returnBook("978-986-123-003");
        library.removeBook("978-986-123-003");  // 現在可以移除了
        System.out.println();

        // 嘗試移除不存在的書
        library.removeBook("978-986-123-999");
        System.out.println();

        library.printInorderReport();
        System.out.println();

        // ========== 測試更多借還書操作 ==========
        System.out.println("【測試八】更多借還書操作");
        System.out.println("----------------------------------------");

        // 借出多本書
        library.borrowBook("978-986-123-002");
        library.borrowBook("978-986-123-004");
        library.borrowBook("978-986-123-006");
        System.out.println();

        library.printInorderReport();
        System.out.println();

        // 歸還部分書籍
        library.returnBook("978-986-123-002");
        library.returnBook("978-986-123-004");
        System.out.println();

        library.printInorderReport();
        System.out.println();

        // ========== 最終測試 ==========
        System.out.println("【測試九】最終綜合測試");
        System.out.println("----------------------------------------");

        System.out.println("目前館藏總數: " + library.getSize());
        System.out.println("可借數量: " + library.getAvailableCount());
        System.out.println("已借數量: " + library.getBorrowedCount());
        System.out.println();

        // 新增一本新書
        library.addBook("978-986-123-011", "區塊鏈技術應用", "周小龍", true);
        System.out.println();

        // 借出新書
        library.borrowBook("978-986-123-011");
        System.out.println();

        // 範圍查詢所有書籍
        library.printRangeQuery("978-986-123-000", "978-986-123-999");
        System.out.println();

        System.out.println("========================================");
        System.out.println("         圖書館藏索引系統執行完畢！");
        System.out.println("========================================");

        System.out.println("\n【功能總結】");
        System.out.println("1. 新增圖書 (addBook): 使用 ISBN 作為 key，不可重複");
        System.out.println("2. 尋找圖書 (findBook): 根據 ISBN 快速尋找");
        System.out.println("3. 借書 (borrowBook): 借出圖書 (需為可借閱狀態)");
        System.out.println("4. 還書 (returnBook): 歸還圖書 (需為已借出狀態)");
        System.out.println("5. 移除圖書 (removeBook): 根據 ISBN 移除 (借出中的書不得移除)");
        System.out.println("6. 範圍查詢 (rangeQuery): 查詢 ISBN 在指定範圍內的所有圖書");
        System.out.println("7. 館藏報表 (printInorderReport): 按 ISBN 排序輸出所有圖書");
        System.out.println("8. 統計資訊: 總館藏數、可借數量、已借數量");
    }
}