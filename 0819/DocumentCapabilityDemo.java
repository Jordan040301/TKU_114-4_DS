/**
 * 匯出與壓縮能力示範
 * 指定檔名：DocumentCapabilityDemo.java
 * 
 * 定義 Exportable 與 Compressible 介面
 * 建立同時實作兩個介面的 BackupDocument
 * 使用不同的介面引用來呼叫其功能
 */
public class DocumentCapabilityDemo {
    
    public static void main(String[] args) {
        System.out.println("========== 文件能力示範系統 ==========");
        
        // 建立 BackupDocument 物件
        BackupDocument document = new BackupDocument("2024年度報告", "這是2024年度的財務報告內容...");
        
        System.out.println("\n========== 使用 Exportable 介面引用 ==========");
        // 使用 Exportable 介面引用
        Exportable exportableDoc = document;
        exportableDoc.exportToPDF();
        exportableDoc.exportToWord();
        exportableDoc.exportToCSV();
        
        System.out.println("\n========== 使用 Compressible 介面引用 ==========");
        // 使用 Compressible 介面引用
        Compressible compressibleDoc = document;
        compressibleDoc.compressToZip();
        compressibleDoc.compressToRar();
        
        System.out.println("\n========== 使用 BackupDocument 本身引用 ==========");
        // 使用 BackupDocument 本身引用
        BackupDocument backupDoc = document;
        backupDoc.displayInfo();
        backupDoc.exportToPDF();
        backupDoc.compressToZip();
        
        System.out.println("\n========== 驗證：兩個參考指向相同物件 ==========");
        // 證明 Exportable 和 Compressible 參考指向同一個物件
        System.out.println("exportableDoc 的 hashCode：" + exportableDoc.hashCode());
        System.out.println("compressibleDoc 的 hashCode：" + compressibleDoc.hashCode());
        System.out.println("document 的 hashCode：" + document.hashCode());
        System.out.println("是否同一個物件？ " + (exportableDoc == compressibleDoc));
        System.out.println("是否同一個物件？ " + (exportableDoc == document));
        System.out.println("是否同一個物件？ " + (compressibleDoc == document));
        
        System.out.println("\n========== 方法可見性比較 ==========");
        System.out.println("透過 Exportable 介面可見的方法：");
        System.out.println("  - exportToPDF()");
        System.out.println("  - exportToWord()");
        System.out.println("  - exportToCSV()");
        
        System.out.println("\n透過 Compressible 介面可見的方法：");
        System.out.println("  - compressToZip()");
        System.out.println("  - compressToRar()");
        
        System.out.println("\n透過 BackupDocument 類別可見的方法：");
        System.out.println("  - displayInfo()");
        System.out.println("  - 所有 Exportable 的方法");
        System.out.println("  - 所有 Compressible 的方法");
        
        System.out.println("\n========== 展示介面隔離 ==========");
        // 展示介面隔離原則
        performExportOnly(exportableDoc);
        performCompressOnly(compressibleDoc);
        performFullBackup(document);
    }
    
    /**
     * 只執行匯出功能（依賴 Exportable 介面）
     * 這個方法看不到 Compressible 的功能
     */
    public static void performExportOnly(Exportable exportable) {
        System.out.println("\n【只執行匯出功能】");
        exportable.exportToPDF();
        // exportable.compressToZip(); // 編譯錯誤！Exportable 沒有這個方法
    }
    
    /**
     * 只執行壓縮功能（依賴 Compressible 介面）
     * 這個方法看不到 Exportable 的功能
     */
    public static void performCompressOnly(Compressible compressible) {
        System.out.println("\n【只執行壓縮功能】");
        compressible.compressToZip();
        // compressible.exportToPDF(); // 編譯錯誤！Compressible 沒有這個方法
    }
    
    /**
     * 執行完整備份（依賴 BackupDocument 類別）
     * 這個方法可以看到所有功能
     */
    public static void performFullBackup(BackupDocument document) {
        System.out.println("\n【執行完整備份】");
        document.displayInfo();
        document.exportToPDF();
        document.compressToZip();
        System.out.println("完整備份完成！");
    }
}

/**
 * 匯出能力介面
 * 定義文件匯出的相關功能
 */
interface Exportable {
    
    /**
     * 匯出為 PDF 格式
     */
    void exportToPDF();
    
    /**
     * 匯出為 Word 格式
     */
    void exportToWord();
    
    /**
     * 匯出為 CSV 格式
     */
    void exportToCSV();
}

/**
 * 壓縮能力介面
 * 定義檔案壓縮的相關功能
 */
interface Compressible {
    
    /**
     * 壓縮為 ZIP 格式
     */
    void compressToZip();
    
    /**
     * 壓縮為 RAR 格式
     */
    void compressToRar();
}

/**
 * 備份文件類別 - 同時實作 Exportable 和 Compressible 介面
 * 這個類別同時具備匯出和壓縮兩種能力
 */
class BackupDocument implements Exportable, Compressible {
    private String title;        // 文件標題
    private String content;      // 文件內容
    private String backupId;     // 備份編號
    private static int idCounter = 0;
    
    /**
     * 建構子
     */
    public BackupDocument(String title, String content) {
        this.title = title;
        this.content = content;
        this.backupId = "BKUP-" + String.format("%04d", ++idCounter);
        System.out.println("建立備份文件：" + title + "（編號：" + backupId + "）");
    }
    
    /**
     * 顯示文件資訊（BackupDocument 獨有方法）
     */
    public void displayInfo() {
        System.out.println("備份文件資訊：");
        System.out.println("  標題：" + title);
        System.out.println("  內容：" + content);
        System.out.println("  備份編號：" + backupId);
        System.out.println("  建立時間：" + java.time.LocalDateTime.now().toString().substring(0, 19));
    }
    
    // ========== Exportable 介面實作 ==========
    
    @Override
    public void exportToPDF() {
        System.out.println("匯出為 PDF 格式...");
        System.out.println("  檔案名稱：" + backupId + "_" + title + ".pdf");
        System.out.println("  匯出成功！");
    }
    
    @Override
    public void exportToWord() {
        System.out.println("匯出為 Word 格式...");
        System.out.println("  檔案名稱：" + backupId + "_" + title + ".docx");
        System.out.println("  匯出成功！");
    }
    
    @Override
    public void exportToCSV() {
        System.out.println("匯出為 CSV 格式...");
        System.out.println("  檔案名稱：" + backupId + "_" + title + ".csv");
        System.out.println("  匯出成功！");
    }
    
    // ========== Compressible 介面實作 ==========
    
    @Override
    public void compressToZip() {
        System.out.println("壓縮為 ZIP 格式...");
        System.out.println("  檔案名稱：" + backupId + "_" + title + ".zip");
        System.out.println("  壓縮率：65%");
        System.out.println("  壓縮成功！");
    }
    
    @Override
    public void compressToRar() {
        System.out.println("壓縮為 RAR 格式...");
        System.out.println("  檔案名稱：" + backupId + "_" + title + ".rar");
        System.out.println("  壓縮率：72%");
        System.out.println("  壓縮成功！");
    }
    
    /**
     * 取得備份編號
     */
    public String getBackupId() {
        return backupId;
    }
    
    /**
     * 取得標題
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * 取得內容
     */
    public String getContent() {
        return content;
    }
}