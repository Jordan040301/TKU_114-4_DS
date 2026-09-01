import java.util.*;

/**
 * 圖書索引哈希表
 * 使用 ISBN 作為鍵，儲存圖書資訊
 */
public class BookIsbnHashTable {
    
    /**
     * 圖書條目節點
     */
    private static class Entry {
        private final String isbn;
        private String title;
        private String author;
        private int year;
        private String publisher;
        private Entry next;
        
        public Entry(String isbn, String title, String author, int year, String publisher) {
            this.isbn = isbn;
            this.title = title;
            this.author = author;
            this.year = year;
            this.publisher = publisher;
            this.next = null;
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
        
        public int getYear() {
            return year;
        }
        
        public void setYear(int year) {
            this.year = year;
        }
        
        public String getPublisher() {
            return publisher;
        }
        
        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }
        
        @Override
        public String toString() {
            return String.format("ISBN:%s | %s | %s | %d | %s", 
                               isbn, title, author, year, publisher);
        }
        
        public String toShortString() {
            return String.format("%s (%s)", title, isbn);
        }
    }
    
    private Entry[] buckets;      // 桶陣列
    private int size;             // 元素數量
    private int capacity;         // 桶容量
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private int resizeCount;      // 擴容次數
    
    /**
     * 建構子：使用預設容量
     */
    public BookIsbnHashTable() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * 建構子：指定初始容量
     * @param initialCapacity 初始容量
     */
    public BookIsbnHashTable(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("容量必須大於 0");
        }
        this.capacity = initialCapacity;
        this.buckets = new Entry[capacity];
        this.size = 0;
        this.resizeCount = 0;
    }
    
    /**
     * 雜湊函數
     * @param isbn ISBN 號碼
     * @return 桶索引
     */
    private int hash(String isbn) {
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN 不能為 null");
        }
        return Math.abs(isbn.hashCode()) % capacity;
    }
    
    /**
     * 驗證 ISBN 格式 (簡易驗證)
     */
    private boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        // 移除連字號
        String clean = isbn.replaceAll("-", "");
        // ISBN-10 或 ISBN-13
        return clean.matches("\\d{10}") || clean.matches("\\d{13}");
    }
    
    /**
     * 新增圖書
     * @param isbn ISBN 號碼
     * @param title 書名
     * @param author 作者
     * @param year 出版年份
     * @param publisher 出版社
     */
    public void addBook(String isbn, String title, String author, int year, String publisher) {
        if (!isValidIsbn(isbn)) {
            throw new IllegalArgumentException("無效的 ISBN 格式");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("書名不能為空");
        }
        if (year < 0 || year > 2026) {
            throw new IllegalArgumentException("無效的出版年份");
        }
        
        // 檢查是否需要擴容
        if ((double) size / capacity > LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        
        int index = hash(isbn);
        Entry current = buckets[index];
        
        // 檢查是否已存在相同的 ISBN
        while (current != null) {
            if (current.getIsbn().equals(isbn)) {
                System.out.printf("⚠️ ISBN '%s' 已存在，請使用 updateBook 更新%n", isbn);
                return;
            }
            current = current.next;
        }
        
        // 新增條目 (頭插法)
        Entry newEntry = new Entry(isbn, title, author, year, publisher);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;
        
        System.out.printf("✅ 新增圖書: %s (size=%d, 負載=%.2f)%n", 
                         newEntry.toShortString(), size, (double) size / capacity);
    }
    
    /**
     * 更新圖書資訊
     * @param isbn ISBN 號碼
     * @param title 新書名 (可為 null 表示不更新)
     * @param author 新作者 (可為 null 表示不更新)
     * @param year 新出版年份 (-1 表示不更新)
     * @param publisher 新出版社 (可為 null 表示不更新)
     * @return true 如果成功更新
     */
    public boolean updateBook(String isbn, String title, String author, int year, String publisher) {
        if (!isValidIsbn(isbn)) {
            throw new IllegalArgumentException("無效的 ISBN 格式");
        }
        
        int index = hash(isbn);
        Entry current = buckets[index];
        
        while (current != null) {
            if (current.getIsbn().equals(isbn)) {
                // 更新欄位
                if (title != null && !title.trim().isEmpty()) {
                    current.setTitle(title);
                }
                if (author != null && !author.trim().isEmpty()) {
                    current.setAuthor(author);
                }
                if (year > 0) {
                    current.setYear(year);
                }
                if (publisher != null && !publisher.trim().isEmpty()) {
                    current.setPublisher(publisher);
                }
                
                System.out.printf("🔄 更新圖書: %s%n", current.toShortString());
                return true;
            }
            current = current.next;
        }
        
        System.out.printf("⚠️ 找不到 ISBN: %s%n", isbn);
        return false;
    }
    
    /**
     * 搜尋圖書
     * @param isbn ISBN 號碼
     * @return 圖書條目，若不存在則回傳 null
     */
    public Entry searchBook(String isbn) {
        if (!isValidIsbn(isbn)) {
            throw new IllegalArgumentException("無效的 ISBN 格式");
        }
        
        int index = hash(isbn);
        Entry current = buckets[index];
        
        while (current != null) {
            if (current.getIsbn().equals(isbn)) {
                System.out.printf("🔍 找到圖書: %s%n", current.toString());
                return current;
            }
            current = current.next;
        }
        
        System.out.printf("⚠️ 找不到 ISBN: %s%n", isbn);
        return null;
    }
    
    /**
     * 刪除圖書
     * @param isbn ISBN 號碼
     * @return 被刪除的圖書條目，若不存在則回傳 null
     */
    public Entry deleteBook(String isbn) {
        if (!isValidIsbn(isbn)) {
            throw new IllegalArgumentException("無效的 ISBN 格式");
        }
        
        int index = hash(isbn);
        Entry current = buckets[index];
        Entry prev = null;
        
        while (current != null) {
            if (current.getIsbn().equals(isbn)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                System.out.printf("🗑️ 刪除圖書: %s (size=%d)%n", 
                                 current.toShortString(), size);
                return current;
            }
            prev = current;
            current = current.next;
        }
        
        System.out.printf("⚠️ 找不到 ISBN: %s%n", isbn);
        return null;
    }
    
    /**
     * 取得元素數量
     * @return 元素數量
     */
    public int size() {
        return size;
    }
    
    /**
     * 檢查是否為空
     * @return true 如果為空
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * 取得目前容量
     * @return 容量
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * 取得目前負載因數
     * @return 負載因數
     */
    public double getLoadFactor() {
        return (double) size / capacity;
    }
    
    /**
     * 取得擴容次數
     * @return 擴容次數
     */
    public int getResizeCount() {
        return resizeCount;
    }
    
    /**
     * 擴容：容量變為兩倍
     */
    private void resize() {
        int newCapacity = capacity * 2;
        Entry[] newBuckets = new Entry[newCapacity];
        
        System.out.printf("📊 擴容: %d → %d (負載: %.2f > %.2f)%n", 
                         capacity, newCapacity, getLoadFactor(), LOAD_FACTOR_THRESHOLD);
        
        // 重新雜湊所有元素
        for (int i = 0; i < capacity; i++) {
            Entry current = buckets[i];
            while (current != null) {
                Entry next = current.next;
                
                // 重新計算索引
                int newIndex = Math.abs(current.getIsbn().hashCode()) % newCapacity;
                
                // 頭插法放入新桶
                current.next = newBuckets[newIndex];
                newBuckets[newIndex] = current;
                
                current = next;
            }
        }
        
        buckets = newBuckets;
        capacity = newCapacity;
        resizeCount++;
    }
    
    /**
     * 取得所有圖書
     * @return 圖書列表
     */
    public List<Entry> getAllBooks() {
        List<Entry> books = new ArrayList<>();
        for (Entry bucket : buckets) {
            Entry current = bucket;
            while (current != null) {
                books.add(current);
                current = current.next;
            }
        }
        return books;
    }
    
    /**
     * 依書名搜尋 (模糊搜尋)
     * @param keyword 書名關鍵字
     * @return 符合的圖書列表
     */
    public List<Entry> searchByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Entry> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase().trim();
        
        for (Entry bucket : buckets) {
            Entry current = bucket;
            while (current != null) {
                if (current.getTitle().toLowerCase().contains(lowerKeyword)) {
                    results.add(current);
                }
                current = current.next;
            }
        }
        
        return results;
    }
    
    /**
     * 依作者搜尋
     * @param author 作者名稱
     * @return 符合的圖書列表
     */
    public List<Entry> searchByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Entry> results = new ArrayList<>();
        String lowerAuthor = author.toLowerCase().trim();
        
        for (Entry bucket : buckets) {
            Entry current = bucket;
            while (current != null) {
                if (current.getAuthor().toLowerCase().contains(lowerAuthor)) {
                    results.add(current);
                }
                current = current.next;
            }
        }
        
        return results;
    }
    
    /**
     * 生成桶報告
     * @return 格式化的桶報告
     */
    public String bucketReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("\n=== 桶報告 (Bucket Report) ===\n");
        report.append("容量: ").append(capacity).append("\n");
        report.append("元素數: ").append(size).append("\n");
        report.append("負載因數: ").append(String.format("%.2f", getLoadFactor())).append("\n");
        report.append("擴容次數: ").append(resizeCount).append("\n");
        report.append("臨界值: ").append(LOAD_FACTOR_THRESHOLD).append("\n");
        report.append("\n");
        
        // 統計資訊
        int maxChainLength = 0;
        int emptyBuckets = 0;
        int totalChainLength = 0;
        List<Integer> chainLengths = new ArrayList<>();
        
        for (int i = 0; i < capacity; i++) {
            int length = 0;
            Entry current = buckets[i];
            while (current != null) {
                length++;
                current = current.next;
            }
            
            chainLengths.add(length);
            totalChainLength += length;
            
            if (length > maxChainLength) {
                maxChainLength = length;
            }
            if (length == 0) {
                emptyBuckets++;
            }
        }
        
        report.append("統計資訊:\n");
        report.append("  最長鏈結長度: ").append(maxChainLength).append("\n");
        report.append("  空桶數量: ").append(emptyBuckets).append("\n");
        report.append("  平均鏈結長度: ").append(String.format("%.2f", 
                          (double) totalChainLength / capacity)).append("\n");
        report.append("\n");
        
        // 顯示每個桶的內容 (只顯示前 20 個桶)
        int displayLimit = Math.min(capacity, 20);
        report.append("桶內容 (顯示前 ").append(displayLimit).append(" 個桶):\n");
        report.append("桶索引 | 鏈長度 | 內容\n");
        report.append("-------|--------|------------------------------\n");
        
        for (int i = 0; i < displayLimit; i++) {
            Entry current = buckets[i];
            int length = 0;
            StringBuilder content = new StringBuilder();
            
            while (current != null) {
                if (length > 0) {
                    content.append(" → ");
                }
                content.append(current.toShortString());
                length++;
                current = current.next;
            }
            
            if (length == 0) {
                content.append("空");
            }
            
            report.append(String.format("%6d | %6d | %s%n", i, length, content.toString()));
        }
        
        if (capacity > 20) {
            report.append("      ... (其餘 ").append(capacity - 20).append(" 個桶省略)\n");
        }
        
        return report.toString();
    }
    
    /**
     * 印出所有圖書
     */
    public void printAllBooks() {
        System.out.println("\n=== 所有圖書 ===");
        if (isEmpty()) {
            System.out.println("圖書館為空");
            return;
        }
        
        List<Entry> books = getAllBooks();
        Collections.sort(books, (a, b) -> a.getTitle().compareTo(b.getTitle()));
        
        System.out.printf("總計: %d 本圖書%n", books.size());
        System.out.println("ISBN           | 書名 | 作者 | 年份 | 出版社");
        System.out.println("---------------|------|------|------|--------");
        
        for (Entry book : books) {
            System.out.printf("%-15s | %-20s | %-8s | %4d | %s%n",
                             book.getIsbn(),
                             book.getTitle().length() > 20 ? 
                                 book.getTitle().substring(0, 17) + "..." : book.getTitle(),
                             book.getAuthor().length() > 8 ?
                                 book.getAuthor().substring(0, 5) + "..." : book.getAuthor(),
                             book.getYear(),
                             book.getPublisher());
        }
        System.out.println();
    }
    
    /**
     * 清空哈希表
     */
    public void clear() {
        Arrays.fill(buckets, null);
        size = 0;
        resizeCount = 0;
        System.out.println("🔄 已清空圖書館");
    }
    
    /**
     * 主程式：測試與展示
     */
    public static void main(String[] args) {
        System.out.println("=== 圖書索引哈希表測試 ===\n");
        
        // 測試 1：基本功能
        testBasicFunctionality();
        
        // 測試 2：更新功能
        testUpdateFunctionality();
        
        // 測試 3：搜尋功能
        testSearchFunctionality();
        
        // 測試 4：擴容測試
        testResizeFunctionality();
        
        // 測試 5：邊界情況
        testEdgeCases();
        
        // 測試 6：實際應用場景
        testRealWorldScenario();
    }
    
    /**
     * 測試基本功能
     */
    private static void testBasicFunctionality() {
        System.out.println("--- 測試 1: 基本功能 ---");
        
        BookIsbnHashTable library = new BookIsbnHashTable(8);
        
        System.out.println("新增圖書:");
        library.addBook("9789864345", "Java程式設計", "張大明", 2023, "松崗");
        library.addBook("9789572243", "Python入門", "李小華", 2022, "碁峰");
        library.addBook("9789863125", "資料結構", "王大明", 2023, "東華");
        library.addBook("9789869852", "演算法", "陳美玲", 2024, "全華");
        library.addBook("9789572123", "作業系統", "林建國", 2022, "旗標");
        
        System.out.println("\nsize = " + library.size());
        System.out.println("isEmpty = " + library.isEmpty());
        System.out.println("capacity = " + library.getCapacity());
        System.out.println("loadFactor = " + String.format("%.2f", library.getLoadFactor()));
        
        library.printAllBooks();
        System.out.println(library.bucketReport());
    }
    
    /**
     * 測試更新功能
     */
    private static void testUpdateFunctionality() {
        System.out.println("--- 測試 2: 更新功能 ---");
        
        BookIsbnHashTable library = new BookIsbnHashTable(8);
        
        library.addBook("9789864345", "Java程式設計", "張大明", 2023, "松崗");
        library.addBook("9789572243", "Python入門", "李小華", 2022, "碁峰");
        
        System.out.println("\n更新前:");
        library.printAllBooks();
        
        System.out.println("\n更新圖書:");
        library.updateBook("9789864345", "Java程式設計(第二版)", "張大明", 2024, "松崗");
        library.updateBook("9789572243", null, "李小華", 2023, null);
        
        System.out.println("\n更新後:");
        library.printAllBooks();
        
        // 嘗試更新不存在的 ISBN
        System.out.println("\n更新不存在的 ISBN:");
        library.updateBook("9789999999", "不存在的書", "作者", 2023, "出版社");
    }
    
    /**
     * 測試搜尋功能
     */
    private static void testSearchFunctionality() {
        System.out.println("--- 測試 3: 搜尋功能 ---");
        
        BookIsbnHashTable library = new BookIsbnHashTable(8);
        
        library.addBook("9789864345", "Java程式設計", "張大明", 2023, "松崗");
        library.addBook("9789572243", "Python入門", "李小華", 2022, "碁峰");
        library.addBook("9789863125", "資料結構", "王大明", 2023, "東華");
        library.addBook("9789869852", "演算法", "陳美玲", 2024, "全華");
        library.addBook("9789572123", "作業系統", "林建國", 2022, "旗標");
        
        // 精確搜尋
        System.out.println("\n精確搜尋:");
        library.searchBook("9789864345");
        library.searchBook("9789999999");
        
        // 依書名搜尋
        System.out.println("\n依書名搜尋 (關鍵字: '程式'):");
        List<BookIsbnHashTable.Entry> titleResults = library.searchByTitle("程式");
        for (BookIsbnHashTable.Entry book : titleResults) {
            System.out.println("  " + book.toShortString());
        }
        
        // 依作者搜尋
        System.out.println("\n依作者搜尋 (關鍵字: '大明'):");
        List<BookIsbnHashTable.Entry> authorResults = library.searchByAuthor("大明");
        for (BookIsbnHashTable.Entry book : authorResults) {
            System.out.println("  " + book.toShortString());
        }
    }
    
    /**
     * 測試擴容功能
     */
    private static void testResizeFunctionality() {
        System.out.println("--- 測試 4: 擴容功能 ---");
        
        BookIsbnHashTable library = new BookIsbnHashTable(4);
        
        System.out.println("初始容量: 4");
        System.out.println("負載因數臨界值: 0.75");
        System.out.println("\n逐步新增圖書觀察擴容:");
        System.out.println("步驟 | ISBN | 容量 | 負載 | 動作");
        System.out.println("-----|------|------|------|------");
        
        String[] books = {
            "9789864345", "9789572243", "9789863125", "9789869852",
            "9789572123", "9789864346", "9789572244", "9789863126",
            "9789869853", "9789572124", "9789864347", "9789572245"
        };
        
        String[] titles = {
            "Java", "Python", "資料結構", "演算法",
            "作業系統", "C++", "JavaScript", "資料庫",
            "網路概論", "編譯器", "AI", "機器學習"
        };
        
        for (int i = 0; i < books.length && i < titles.length; i++) {
            double oldLoad = library.getLoadFactor();
            int oldCapacity = library.getCapacity();
            
            library.addBook(books[i], titles[i] + "程式設計", "作者" + (i+1), 2023, "出版社");
            
            int newCapacity = library.getCapacity();
            double newLoad = library.getLoadFactor();
            String action = (newCapacity > oldCapacity) ? "擴容!" : "正常";
            
            System.out.printf("  %2d  | %-10s | %4d | %6.2f | %s%n",
                             i + 1, books[i], newCapacity, newLoad, action);
        }
        
        System.out.println("\n最終狀態:");
        System.out.println("  size = " + library.size());
        System.out.println("  capacity = " + library.getCapacity());
        System.out.println("  loadFactor = " + String.format("%.2f", library.getLoadFactor()));
        System.out.println("  resizeCount = " + library.getResizeCount());
        
        System.out.println(library.bucketReport());
    }
    
    /**
     * 測試邊界情況
     */
    private static void testEdgeCases() {
        System.out.println("--- 測試 5: 邊界情況 ---");
        
        BookIsbnHashTable library = new BookIsbnHashTable(4);
        
        // 測試 5.1: 空圖書館
        System.out.println("測試 5.1: 空圖書館");
        library.printAllBooks();
        System.out.println("  size = " + library.size());
        System.out.println("  isEmpty = " + library.isEmpty());
        System.out.println();
        
        // 測試 5.2: 無效 ISBN
        System.out.println("測試 5.2: 無效 ISBN");
        try {
            library.addBook("invalid", "無效書名", "作者", 2023, "出版社");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        
        try {
            library.addBook("978986434", "無效書名", "作者", 2023, "出版社");
        } catch (IllegalArgumentException e) {
            System.out.println("  ✓ 正確捕獲例外: " + e.getMessage());
        }
        System.out.println();
        
        // 測試 5.3: 新增重複 ISBN
        System.out.println("測試 5.3: 新增重複 ISBN");
        library.addBook("9789864345", "Java程式設計", "張大明", 2023, "松崗");
        library.addBook("9789864345", "重複的書", "作者", 2023, "出版社");
        System.out.println();
        
        // 測試 5.4: 刪除不存在的書
        System.out.println("測試 5.4: 刪除不存在的書");
        library.deleteBook("9789999999");
        System.out.println();
        
        // 測試 5.5: 搜尋不存在的書
        System.out.println("測試 5.5: 搜尋不存在的書");
        library.searchBook("9789999999");
        System.out.println();
    }
    
    /**
     * 測試實際應用場景
     */
    private static void testRealWorldScenario() {
        System.out.println("--- 測試 6: 實際應用場景 ---");
        System.out.println("📚 圖書館管理系統");
        
        BookIsbnHashTable library = new BookIsbnHashTable(10);
        
        // 模擬圖書館藏書
        System.out.println("\n新增館藏圖書:");
        String[][] bookData = {
            {"9789864345", "Java程式設計", "張大明", "2023", "松崗"},
            {"9789572243", "Python入門", "李小華", "2022", "碁峰"},
            {"9789863125", "資料結構", "王大明", "2023", "東華"},
            {"9789869852", "演算法導論", "陳美玲", "2024", "全華"},
            {"9789572123", "作業系統概念", "林建國", "2022", "旗標"},
            {"9789864346", "資料庫系統", "張大明", "2023", "松崗"},
            {"9789572244", "網路概論", "李小華", "2023", "碁峰"},
            {"9789863126", "編譯器設計", "王大明", "2024", "東華"},
            {"9789869853", "人工智慧", "陳美玲", "2024", "全華"},
            {"9789572124", "機器學習", "林建國", "2023", "旗標"},
            {"9789864347", "軟體工程", "張大明", "2024", "松崗"},
            {"9789572245", "JavaScript程式設計", "李小華", "2023", "碁峰"}
        };
        
        for (String[] book : bookData) {
            library.addBook(book[0], book[1], book[2], Integer.parseInt(book[3]), book[4]);
        }
        
        library.printAllBooks();
        System.out.println(library.bucketReport());
        
        // 查詢功能展示
        System.out.println("\n🔍 查詢功能展示:");
        
        System.out.println("  依書名搜尋 '程式設計':");
        List<BookIsbnHashTable.Entry> results = library.searchByTitle("程式設計");
        for (BookIsbnHashTable.Entry book : results) {
            System.out.println("    " + book.toShortString());
        }
        
        System.out.println("  依作者搜尋 '張大明':");
        results = library.searchByAuthor("張大明");
        for (BookIsbnHashTable.Entry book : results) {
            System.out.println("    " + book.toShortString());
        }
        
        // 更新館藏
        System.out.println("\n📝 更新館藏:");
        library.updateBook("9789864345", "Java程式設計(第三版)", null, 2025, null);
        
        // 刪除館藏
        System.out.println("\n🗑️ 刪除館藏:");
        library.deleteBook("9789572245");
        
        // 最終狀態
        System.out.println("\n📊 最終統計:");
        System.out.println("  總館藏: " + library.size() + " 本");
        System.out.println("  容量: " + library.getCapacity());
        System.out.println("  負載因數: " + String.format("%.2f", library.getLoadFactor()));
        
        library.printAllBooks();
    }
}