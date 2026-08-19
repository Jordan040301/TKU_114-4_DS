/**
 * 媒體文件處理系統
 * 指定檔名：MediaProcessingSystem.java
 * 
 * 建立抽象 MediaFile，子類別包含 ImageFile、AudioFile、VideoFile
 * 建立 Playable、Compressible、Exportable 等接口
 * 由適合的子類別實作
 * 輸出每個物件支援的操作結果
 */
public class MediaProcessingSystem {
    
    public static void main(String[] args) {
        System.out.println("========== 媒體文件處理系統 ==========");
        
        // 建立各種媒體文件
        MediaFile[] mediaFiles = new MediaFile[] {
            new ImageFile("IMG_001", "夕陽照片", 2048, 1536, "JPEG"),
            new ImageFile("IMG_002", "風景照片", 3840, 2160, "PNG"),
            new AudioFile("AUD_001", "古典音樂", "MP3", 320, 245),
            new AudioFile("AUD_002", "播客節目", "AAC", 128, 180),
            new VideoFile("VID_001", "教學影片", "MP4", 1920, 1080, 600),
            new VideoFile("VID_002", "短片", "AVI", 1280, 720, 180)
        };
        
        System.out.println("\n========== 媒體文件列表 ==========");
        printMediaList(mediaFiles);
        
        System.out.println("\n========== 執行各媒體操作 ==========");
        for (MediaFile media : mediaFiles) {
            System.out.println("\n--- " + media.getFileType() + "：" + media.getFileName() + " ---");
            
            // 基本操作（所有媒體都有）
            media.displayInfo();
            media.getSize();
            
            // Playable 接口操作
            if (media instanceof Playable) {
                Playable playable = (Playable) media;
                playable.play();
                playable.pause();
                playable.stop();
                playable.getDuration();
            }
            
            // Compressible 接口操作
            if (media instanceof Compressible) {
                Compressible compressible = (Compressible) media;
                compressible.compress();
                compressible.decompress();
                System.out.println("  壓縮率：" + compressible.getCompressionRatio() + "%");
            }
            
            // Exportable 接口操作
            if (media instanceof Exportable) {
                Exportable exportable = (Exportable) media;
                exportable.export("PDF");
                exportable.export("PNG");
            }
            
            System.out.println("  ✅ " + media.getFileType() + " 操作完成");
        }
        
        System.out.println("\n========== 能力統計 ==========");
        printCapabilityStatistics(mediaFiles);
        
        System.out.println("\n========== 支援能力清單 ==========");
        printSupportedCapabilities(mediaFiles);
    }
    
    /**
     * 輸出媒體列表
     */
    public static void printMediaList(MediaFile[] mediaFiles) {
        System.out.println("編號\t類型\t\t名稱\t\t\t大小");
        System.out.println("----\t----\t\t----\t\t\t----");
        for (int i = 0; i < mediaFiles.length; i++) {
            MediaFile m = mediaFiles[i];
            System.out.printf("%d\t%-10s\t%-16s\t%.2f KB%n",
                            (i + 1),
                            m.getFileType(),
                            m.getFileName(),
                            m.getFileSize() / 1024.0);
        }
    }
    
    /**
     * 輸出能力統計
     */
    public static void printCapabilityStatistics(MediaFile[] mediaFiles) {
        int playableCount = 0;
        int compressibleCount = 0;
        int exportableCount = 0;
        
        for (MediaFile media : mediaFiles) {
            if (media instanceof Playable) playableCount++;
            if (media instanceof Compressible) compressibleCount++;
            if (media instanceof Exportable) exportableCount++;
        }
        
        System.out.println("支援 Playable 的檔案：" + playableCount + " 個");
        System.out.println("支援 Compressible 的檔案：" + compressibleCount + " 個");
        System.out.println("支援 Exportable 的檔案：" + exportableCount + " 個");
    }
    
    /**
     * 輸出每個檔案支援的能力
     */
    public static void printSupportedCapabilities(MediaFile[] mediaFiles) {
        for (MediaFile media : mediaFiles) {
            StringBuilder capabilities = new StringBuilder();
            capabilities.append(media.getFileName());
            capabilities.append("：");
            
            if (media instanceof Playable) capabilities.append(" 可播放");
            if (media instanceof Compressible) capabilities.append(" 可壓縮");
            if (media instanceof Exportable) capabilities.append(" 可匯出");
            
            System.out.println(capabilities.toString());
        }
    }
}

/**
 * 抽象媒體文件類別
 */
abstract class MediaFile {
    private String fileId;          // 檔案編號
    private String fileName;        // 檔案名稱
    private long fileSize;          // 檔案大小（bytes）
    
    /**
     * 建構子
     */
    public MediaFile(String fileId, String fileName, long fileSize) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = (fileSize < 0) ? 0 : fileSize;
    }
    
    /**
     * 取得檔案編號
     */
    public String getFileId() {
        return fileId;
    }
    
    /**
     * 取得檔案名稱
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * 取得檔案大小（bytes）
     */
    public long getFileSize() {
        return fileSize;
    }
    
    /**
     * 取得檔案類型（抽象方法，由子類別實作）
     */
    public abstract String getFileType();
    
    /**
     * 顯示檔案資訊
     */
    public abstract void displayInfo();
    
    /**
     * 取得檔案大小（KB）
     */
    public double getSize() {
        double sizeInKB = fileSize / 1024.0;
        System.out.printf("  檔案大小：%.2f KB%n", sizeInKB);
        return sizeInKB;
    }
}

/**
 * 可播放介面
 * 適用於音訊和影片檔案
 */
interface Playable {
    
    /**
     * 播放
     */
    void play();
    
    /**
     * 暫停
     */
    void pause();
    
    /**
     * 停止
     */
    void stop();
    
    /**
     * 取得播放時間（秒）
     */
    int getDuration();
}

/**
 * 可壓縮介面
 * 適用於圖片、音訊和影片檔案
 */
interface Compressible {
    
    /**
     * 壓縮檔案
     */
    void compress();
    
    /**
     * 解壓縮檔案
     */
    void decompress();
    
    /**
     * 取得壓縮率（%）
     */
    double getCompressionRatio();
}

/**
 * 可匯出介面
 * 適用於圖片檔案
 */
interface Exportable {
    
    /**
     * 匯出為指定格式
     * @param format 匯出格式
     */
    void export(String format);
}

/**
 * 圖片檔案類別 - 繼承 MediaFile
 * 實作 Compressible 和 Exportable
 */
class ImageFile extends MediaFile implements Compressible, Exportable {
    private int width;          // 寬度（像素）
    private int height;         // 高度（像素）
    private String format;      // 圖片格式（JPEG、PNG、GIF）
    private double compressionRatio;  // 壓縮率
    
    /**
     * 建構子
     */
    public ImageFile(String fileId, String fileName, int width, int height, String format) {
        super(fileId, fileName, width * height * 3);  // 模擬檔案大小
        this.width = (width < 0) ? 0 : width;
        this.height = (height < 0) ? 0 : height;
        this.format = format;
        this.compressionRatio = 60 + Math.random() * 30;  // 隨機壓縮率 60-90%
    }
    
    @Override
    public String getFileType() {
        return "圖片檔案";
    }
    
    @Override
    public void displayInfo() {
        System.out.println("  【圖片資訊】");
        System.out.println("    檔案名稱：" + getFileName());
        System.out.println("    圖片格式：" + format);
        System.out.println("    解析度：" + width + " x " + height + " 像素");
        System.out.println("    總像素：" + (width * height) + " 像素");
    }
    
    // ========== Compressible 介面實作 ==========
    
    @Override
    public void compress() {
        System.out.println("  壓縮圖片中...");
        System.out.println("    使用 JPEG 壓縮演算法");
        System.out.println("    壓縮前大小：" + getFileSize() + " bytes");
        System.out.println("    壓縮後大小：" + (long)(getFileSize() * (1 - compressionRatio / 100)) + " bytes");
        System.out.println("    圖片壓縮完成！");
    }
    
    @Override
    public void decompress() {
        System.out.println("  解壓縮圖片中...");
        System.out.println("    還原原始圖片品質");
        System.out.println("    圖片解壓縮完成！");
    }
    
    @Override
    public double getCompressionRatio() {
        return compressionRatio;
    }
    
    // ========== Exportable 介面實作 ==========
    
    @Override
    public void export(String format) {
        System.out.println("  匯出圖片為 " + format + " 格式...");
        System.out.println("    匯出檔案：" + getFileName() + "." + format.toLowerCase());
        System.out.println("    匯出成功！");
    }
    
    /**
     * 取得寬度
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * 取得高度
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * 取得格式
     */
    public String getFormat() {
        return format;
    }
}

/**
 * 音訊檔案類別 - 繼承 MediaFile
 * 實作 Playable 和 Compressible
 */
class AudioFile extends MediaFile implements Playable, Compressible {
    private String audioFormat;     // 音訊格式（MP3、AAC、WAV）
    private int bitrate;            // 位元率（kbps）
    private int duration;           // 播放時間（秒）
    private boolean isPlaying;      // 播放狀態
    private double compressionRatio;  // 壓縮率
    
    /**
     * 建構子
     */
    public AudioFile(String fileId, String fileName, String audioFormat, int bitrate, int duration) {
        super(fileId, fileName, bitrate * duration * 125);  // 模擬檔案大小
        this.audioFormat = audioFormat;
        this.bitrate = (bitrate < 0) ? 0 : bitrate;
        this.duration = (duration < 0) ? 0 : duration;
        this.isPlaying = false;
        this.compressionRatio = 50 + Math.random() * 40;  // 隨機壓縮率 50-90%
    }
    
    @Override
    public String getFileType() {
        return "音訊檔案";
    }
    
    @Override
    public void displayInfo() {
        System.out.println("  【音訊資訊】");
        System.out.println("    檔案名稱：" + getFileName());
        System.out.println("    音訊格式：" + audioFormat);
        System.out.println("    位元率：" + bitrate + " kbps");
        System.out.println("    播放時間：" + formatDuration(duration));
    }
    
    /**
     * 格式化播放時間
     */
    private String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return minutes + "分" + secs + "秒";
    }
    
    // ========== Playable 介面實作 ==========
    
    @Override
    public void play() {
        if (isPlaying) {
            System.out.println("  音訊已在播放中");
            return;
        }
        isPlaying = true;
        System.out.println("  ▶ 播放音訊：" + getFileName());
        System.out.println("    音訊格式：" + audioFormat);
        System.out.println("    播放時間：" + formatDuration(duration));
    }
    
    @Override
    public void pause() {
        if (!isPlaying) {
            System.out.println("  音訊已暫停");
            return;
        }
        isPlaying = false;
        System.out.println("  ⏸ 暫停音訊：" + getFileName());
    }
    
    @Override
    public void stop() {
        isPlaying = false;
        System.out.println("  ⏹ 停止音訊：" + getFileName());
    }
    
    @Override
    public int getDuration() {
        System.out.println("  播放時間：" + formatDuration(duration));
        return duration;
    }
    
    // ========== Compressible 介面實作 ==========
    
    @Override
    public void compress() {
        System.out.println("  壓縮音訊中...");
        System.out.println("    使用 MP3 壓縮演算法");
        System.out.println("    位元率從 " + bitrate + " kbps 降低壓縮");
        System.out.println("    音訊壓縮完成！");
    }
    
    @Override
    public void decompress() {
        System.out.println("  解壓縮音訊中...");
        System.out.println("    還原原始音訊品質");
        System.out.println("    音訊解壓縮完成！");
    }
    
    @Override
    public double getCompressionRatio() {
        return compressionRatio;
    }
    
    /**
     * 取得音訊格式
     */
    public String getAudioFormat() {
        return audioFormat;
    }
    
    /**
     * 取得位元率
     */
    public int getBitrate() {
        return bitrate;
    }
}

/**
 * 影片檔案類別 - 繼承 MediaFile
 * 實作 Playable 和 Compressible
 */
class VideoFile extends MediaFile implements Playable, Compressible {
    private String videoFormat;     // 影片格式（MP4、AVI、MOV）
    private int width;              // 寬度（像素）
    private int height;             // 高度（像素）
    private int duration;           // 播放時間（秒）
    private boolean isPlaying;      // 播放狀態
    private double compressionRatio;  // 壓縮率
    
    /**
     * 建構子
     */
    public VideoFile(String fileId, String fileName, String videoFormat, 
                    int width, int height, int duration) {
        super(fileId, fileName, width * height * duration / 10);  // 模擬檔案大小
        this.videoFormat = videoFormat;
        this.width = (width < 0) ? 0 : width;
        this.height = (height < 0) ? 0 : height;
        this.duration = (duration < 0) ? 0 : duration;
        this.isPlaying = false;
        this.compressionRatio = 40 + Math.random() * 50;  // 隨機壓縮率 40-90%
    }
    
    @Override
    public String getFileType() {
        return "影片檔案";
    }
    
    @Override
    public void displayInfo() {
        System.out.println("  【影片資訊】");
        System.out.println("    檔案名稱：" + getFileName());
        System.out.println("    影片格式：" + videoFormat);
        System.out.println("    解析度：" + width + " x " + height + " 像素");
        System.out.println("    播放時間：" + formatDuration(duration));
        System.out.println("    總畫格數：" + (duration * 30) + " 格（30fps）");
    }
    
    /**
     * 格式化播放時間
     */
    private String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return minutes + "分" + secs + "秒";
    }
    
    // ========== Playable 介面實作 ==========
    
    @Override
    public void play() {
        if (isPlaying) {
            System.out.println("  影片已在播放中");
            return;
        }
        isPlaying = true;
        System.out.println("  ▶ 播放影片：" + getFileName());
        System.out.println("    影片格式：" + videoFormat);
        System.out.println("    解析度：" + width + " x " + height);
        System.out.println("    播放時間：" + formatDuration(duration));
    }
    
    @Override
    public void pause() {
        if (!isPlaying) {
            System.out.println("  影片已暫停");
            return;
        }
        isPlaying = false;
        System.out.println("  ⏸ 暫停影片：" + getFileName());
    }
    
    @Override
    public void stop() {
        isPlaying = false;
        System.out.println("  ⏹ 停止影片：" + getFileName());
    }
    
    @Override
    public int getDuration() {
        System.out.println("  播放時間：" + formatDuration(duration));
        return duration;
    }
    
    // ========== Compressible 介面實作 ==========
    
    @Override
    public void compress() {
        System.out.println("  壓縮影片中...");
        System.out.println("    使用 H.264 壓縮演算法");
        System.out.println("    解析度從 " + width + "x" + height + " 優化壓縮");
        System.out.println("    影片壓縮完成！");
    }
    
    @Override
    public void decompress() {
        System.out.println("  解壓縮影片中...");
        System.out.println("    還原原始影片品質");
        System.out.println("    影片解壓縮完成！");
    }
    
    @Override
    public double getCompressionRatio() {
        return compressionRatio;
    }
    
    /**
     * 取得影片格式
     */
    public String getVideoFormat() {
        return videoFormat;
    }
    
    /**
     * 取得寬度
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * 取得高度
     */
    public int getHeight() {
        return height;
    }
}