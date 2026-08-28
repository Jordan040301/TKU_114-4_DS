
import java.util.*;

/**
 * 檔案系統節點 (File or Directory)
 */
class FileSystemNode {
    private String name;
    private boolean isDirectory;
    private long size;                  // 如果是檔案: 檔案大小 (bytes)；如果是目錄: 累計總容量
    private List<FileSystemNode> children;  // 只有目錄才有子節點
    private FileSystemNode parent;

    public FileSystemNode(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = 0;
        this.children = isDirectory ? new ArrayList<>() : null;
        this.parent = null;
    }

    public FileSystemNode(String name, boolean isDirectory, long size) {
        this(name, isDirectory);
        if (!isDirectory) {
            this.size = size;
        }
    }

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<FileSystemNode> getChildren() {
        return children;
    }

    public FileSystemNode getParent() {
        return parent;
    }

    public void setParent(FileSystemNode parent) {
        this.parent = parent;
    }

    public void addChild(FileSystemNode child) {
        if (isDirectory && children != null) {
            child.setParent(this);
            children.add(child);
        }
    }

    public boolean hasChildren() {
        return isDirectory && children != null && !children.isEmpty();
    }

    public int getChildCount() {
        return isDirectory && children != null ? children.size() : 0;
    }

    @Override
    public String toString() {
        return (isDirectory ? "[目錄] " : "[檔案] ") + name + 
               " (" + formatSize(size) + ")";
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }
}

/**
 * 目錄樹統計報告
 * 使用 postorder 計算每個目錄的總容量
 * 輸出: total node、file count、directory count、height 與最大檔案
 */
public class DirectoryTreeReport {
    private FileSystemNode root;
    private int totalNodes;
    private int fileCount;
    private int directoryCount;
    private int height;
    private FileSystemNode largestFile;

    public DirectoryTreeReport() {
        this.root = null;
        this.totalNodes = 0;
        this.fileCount = 0;
        this.directoryCount = 0;
        this.height = 0;
        this.largestFile = null;
    }

    /**
     * 建立範例檔案系統樹
     */
    public void buildSampleFileSystem() {
        // 根目錄: "/"
        root = new FileSystemNode("/", true);
        directoryCount++;

        // 目錄: /home
        FileSystemNode home = new FileSystemNode("home", true);
        directoryCount++;
        root.addChild(home);

        // 目錄: /home/user
        FileSystemNode user = new FileSystemNode("user", true);
        directoryCount++;
        home.addChild(user);

        // 檔案: /home/user/document.txt (100 KB)
        FileSystemNode doc = new FileSystemNode("document.txt", false, 102400);
        fileCount++;
        user.addChild(doc);

        // 檔案: /home/user/photo.jpg (2 MB)
        FileSystemNode photo = new FileSystemNode("photo.jpg", false, 2 * 1024 * 1024);
        fileCount++;
        user.addChild(photo);

        // 檔案: /home/user/music.mp3 (5 MB)
        FileSystemNode music = new FileSystemNode("music.mp3", false, 5 * 1024 * 1024);
        fileCount++;
        user.addChild(music);

        // 目錄: /home/user/projects
        FileSystemNode projects = new FileSystemNode("projects", true);
        directoryCount++;
        user.addChild(projects);

        // 檔案: /home/user/projects/readme.md (2 KB)
        FileSystemNode readme = new FileSystemNode("readme.md", false, 2048);
        fileCount++;
        projects.addChild(readme);

        // 檔案: /home/user/projects/main.java (15 KB)
        FileSystemNode main = new FileSystemNode("main.java", false, 15360);
        fileCount++;
        projects.addChild(main);

        // 檔案: /home/user/projects/config.json (1 KB)
        FileSystemNode config = new FileSystemNode("config.json", false, 1024);
        fileCount++;
        projects.addChild(config);

        // 目錄: /home/user/projects/docs
        FileSystemNode docs = new FileSystemNode("docs", true);
        directoryCount++;
        projects.addChild(docs);

        // 檔案: /home/user/projects/docs/api.pdf (3 MB)
        FileSystemNode api = new FileSystemNode("api.pdf", false, 3 * 1024 * 1024);
        fileCount++;
        docs.addChild(api);

        // 檔案: /home/user/projects/docs/guide.pdf (1.5 MB)
        FileSystemNode guide = new FileSystemNode("guide.pdf", false, 1_500_000);
        fileCount++;
        docs.addChild(guide);

        // 目錄: /etc
        FileSystemNode etc = new FileSystemNode("etc", true);
        directoryCount++;
        root.addChild(etc);

        // 檔案: /etc/hosts (0.5 KB)
        FileSystemNode hosts = new FileSystemNode("hosts", false, 512);
        fileCount++;
        etc.addChild(hosts);

        // 檔案: /etc/passwd (1.2 KB)
        FileSystemNode passwd = new FileSystemNode("passwd", false, 1228);
        fileCount++;
        etc.addChild(passwd);

        // 檔案: /etc/group (0.8 KB)
        FileSystemNode group = new FileSystemNode("group", false, 819);
        fileCount++;
        etc.addChild(group);

        // 目錄: /var
        FileSystemNode var = new FileSystemNode("var", true);
        directoryCount++;
        root.addChild(var);

        // 目錄: /var/log
        FileSystemNode log = new FileSystemNode("log", true);
        directoryCount++;
        var.addChild(log);

        // 檔案: /var/log/syslog (10 MB)
        FileSystemNode syslog = new FileSystemNode("syslog", false, 10 * 1024 * 1024);
        fileCount++;
        log.addChild(syslog);

        // 檔案: /var/log/auth.log (8 MB)
        FileSystemNode auth = new FileSystemNode("auth.log", false, 8 * 1024 * 1024);
        fileCount++;
        log.addChild(auth);

        // 檔案: /var/log/access.log (12 MB)
        FileSystemNode access = new FileSystemNode("access.log", false, 12 * 1024 * 1024);
        fileCount++;
        log.addChild(access);

        // 目錄: /tmp
        FileSystemNode tmp = new FileSystemNode("tmp", true);
        directoryCount++;
        root.addChild(tmp);

        // 檔案: /tmp/temp1.tmp (5 KB)
        FileSystemNode temp1 = new FileSystemNode("temp1.tmp", false, 5120);
        fileCount++;
        tmp.addChild(temp1);

        // 檔案: /tmp/temp2.tmp (3 KB)
        FileSystemNode temp2 = new FileSystemNode("temp2.tmp", false, 3072);
        fileCount++;
        tmp.addChild(temp2);

        totalNodes = fileCount + directoryCount;
    }

    /**
     * 建立第二個範例檔案系統 (較簡單)
     */
    public void buildSimpleFileSystem() {
        root = new FileSystemNode("root", true);
        directoryCount++;

        FileSystemNode docs = new FileSystemNode("documents", true);
        directoryCount++;
        root.addChild(docs);

        FileSystemNode report = new FileSystemNode("report.txt", false, 5000);
        fileCount++;
        docs.addChild(report);

        FileSystemNode data = new FileSystemNode("data.csv", false, 12000);
        fileCount++;
        docs.addChild(data);

        FileSystemNode photos = new FileSystemNode("photos", true);
        directoryCount++;
        root.addChild(photos);

        FileSystemNode pic1 = new FileSystemNode("pic1.jpg", false, 800000);
        fileCount++;
        photos.addChild(pic1);

        FileSystemNode pic2 = new FileSystemNode("pic2.jpg", false, 1200000);
        fileCount++;
        photos.addChild(pic2);

        FileSystemNode pic3 = new FileSystemNode("pic3.png", false, 2500000);
        fileCount++;
        photos.addChild(pic3);

        totalNodes = fileCount + directoryCount;
    }

    /**
     * 使用後序走訪 (Postorder) 計算每個目錄的總容量
     * 並同時收集統計資訊
     */
    public void calculateDirectorySizes() {
        if (root == null) {
            return;
        }
        // 重置統計
        totalNodes = 0;
        fileCount = 0;
        directoryCount = 0;
        height = 0;
        largestFile = null;

        postorderCalculate(root, 1);
    }

    private long postorderCalculate(FileSystemNode node, int currentHeight) {
        if (node == null) {
            return 0;
        }

        // 更新高度
        height = Math.max(height, currentHeight);

        // 如果是檔案
        if (!node.isDirectory()) {
            totalNodes++;
            fileCount++;
            // 檢查是否為最大檔案
            if (largestFile == null || node.getSize() > largestFile.getSize()) {
                largestFile = node;
            }
            return node.getSize();
        }

        // 如果是目錄，計算所有子節點的容量
        long totalSize = 0;
        directoryCount++;

        for (FileSystemNode child : node.getChildren()) {
            long childSize = postorderCalculate(child, currentHeight + 1);
            totalSize += childSize;
        }

        // 設定目錄的總容量
        node.setSize(totalSize);
        totalNodes++;

        return totalSize;
    }

    /**
     * 列印目錄樹 (格式化)
     */
    public void printTree() {
        if (root == null) {
            System.out.println("目錄樹為空");
            return;
        }
        System.out.println("===== 目錄樹結構 =====");
        printTreeRecursive(root, 0);
        System.out.println("======================");
    }

    private void printTreeRecursive(FileSystemNode node, int depth) {
        String indent = "  ".repeat(depth);
        String prefix = node.isDirectory() ? "📁 " : "📄 ";
        System.out.println(indent + prefix + node.getName() + 
                          " (" + formatSize(node.getSize()) + ")");

        if (node.isDirectory() && node.hasChildren()) {
            for (FileSystemNode child : node.getChildren()) {
                printTreeRecursive(child, depth + 1);
            }
        }
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * 列印統計報告
     */
    public void printReport() {
        if (root == null) {
            System.out.println("目錄樹為空，無法產生報告");
            return;
        }

        // 先計算 (如果尚未計算)
        if (totalNodes == 0) {
            calculateDirectorySizes();
        }

        System.out.println("========================================");
        System.out.println("       檔案系統統計報告");
        System.out.println("========================================");
        System.out.println("總節點數 (Total Nodes):       " + totalNodes);
        System.out.println("檔案數量 (File Count):        " + fileCount);
        System.out.println("目錄數量 (Directory Count):    " + directoryCount);
        System.out.println("樹高度 (Height):              " + height);
        
        if (largestFile != null) {
            System.out.println("最大檔案 (Largest File):       " + 
                               largestFile.getName() + " (" + 
                               formatSize(largestFile.getSize()) + ")");
            System.out.println("  完整路徑: " + getFullPath(largestFile));
        } else {
            System.out.println("最大檔案 (Largest File):       無");
        }
        System.out.println("========================================");
        
        // 顯示根目錄總容量
        System.out.println("根目錄總容量: " + formatSize(root.getSize()));
        System.out.println();
    }

    /**
     * 取得節點的完整路徑
     */
    private String getFullPath(FileSystemNode node) {
        if (node == null) {
            return "";
        }
        if (node.getParent() == null) {
            return node.getName();
        }
        return getFullPath(node.getParent()) + "/" + node.getName();
    }

    /**
     * 印出每個目錄的容量明細
     */
    public void printDirectoryDetails() {
        System.out.println("===== 目錄容量明細 =====");
        printDirectoryDetailsRecursive(root, 0);
        System.out.println("========================");
    }

    private void printDirectoryDetailsRecursive(FileSystemNode node, int depth) {
        if (node == null) {
            return;
        }

        if (node.isDirectory()) {
            String indent = "  ".repeat(depth);
            System.out.println(indent + "📁 " + node.getName() + 
                              " → 總容量: " + formatSize(node.getSize()) + 
                              " (包含 " + node.getChildCount() + " 個子項目)");
            
            for (FileSystemNode child : node.getChildren()) {
                printDirectoryDetailsRecursive(child, depth + 1);
            }
        }
        // 檔案不印出詳細資訊 (避免過於冗長)
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       檔案系統統計工具");
        System.out.println("========================================\n");

        // ========== 測試一: 範例檔案系統 ==========
        System.out.println("【測試一】完整範例檔案系統");
        System.out.println("----------------------------------------");
        DirectoryTreeReport report1 = new DirectoryTreeReport();
        report1.buildSampleFileSystem();
        report1.printTree();
        System.out.println();

        // 計算統計
        report1.calculateDirectorySizes();
        report1.printReport();
        report1.printDirectoryDetails();
        System.out.println();

        // ========== 測試二: 簡單檔案系統 ==========
        System.out.println("【測試二】簡單檔案系統");
        System.out.println("----------------------------------------");
        DirectoryTreeReport report2 = new DirectoryTreeReport();
        report2.buildSimpleFileSystem();
        report2.printTree();
        System.out.println();

        report2.calculateDirectorySizes();
        report2.printReport();
        report2.printDirectoryDetails();
        System.out.println();

        // ========== 測試三: 空目錄 ==========
        System.out.println("【測試三】空目錄");
        System.out.println("----------------------------------------");
        DirectoryTreeReport report3 = new DirectoryTreeReport();
        report3.root = new FileSystemNode("empty_dir", true);
        report3.directoryCount = 1;
        report3.totalNodes = 1;
        report3.printTree();
        System.out.println();

        report3.calculateDirectorySizes();
        report3.printReport();
        System.out.println();

        // ========== 測試四: 單一檔案 ==========
        System.out.println("【測試四】單一檔案");
        System.out.println("----------------------------------------");
        DirectoryTreeReport report4 = new DirectoryTreeReport();
        report4.root = new FileSystemNode("/", true);
        report4.directoryCount = 1;
        FileSystemNode file = new FileSystemNode("hello.txt", false, 1024);
        report4.fileCount = 1;
        report4.root.addChild(file);
        report4.totalNodes = 2;
        report4.printTree();
        System.out.println();

        report4.calculateDirectorySizes();
        report4.printReport();
        System.out.println();

        System.out.println("========================================");
        System.out.println("         統計完成！");
        System.out.println("========================================");

        System.out.println("\n【功能說明】");
        System.out.println("1. 使用後序走訪 (Postorder) 計算每個目錄的總容量");
        System.out.println("2. 支援檔案和目錄兩種節點類型");
        System.out.println("3. 統計資訊包含:");
        System.out.println("   - 總節點數 (Total Nodes)");
        System.out.println("   - 檔案數量 (File Count)");
        System.out.println("   - 目錄數量 (Directory Count)");
        System.out.println("   - 樹高度 (Height)");
        System.out.println("   - 最大檔案 (Largest File)");
        System.out.println("4. 可顯示完整的目錄樹結構");
        System.out.println("5. 可顯示每個目錄的容量明細");
    }
}