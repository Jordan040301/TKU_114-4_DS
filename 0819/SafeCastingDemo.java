/**
 * 媒體資產父類別
 */
class MediaAsset {
    private String title;
    
    public MediaAsset(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void play() {
        System.out.println("播放媒體：" + title);
    }
}

/**
 * 影片資產子類別
 */
class VideoAsset extends MediaAsset {
    private String resolution;
    
    public VideoAsset(String title, String resolution) {
        super(title);
        this.resolution = resolution;
    }
    
    public void changeResolution(String resolution) {
        this.resolution = resolution;
        System.out.println("解析度已更改為：" + resolution);
    }
    
    @Override
    public void play() {
        System.out.println("播放影片：" + getTitle() + "（解析度：" + resolution + "）");
    }
}

/**
 * 音訊資產子類別
 */
class AudioAsset extends MediaAsset {
    public AudioAsset(String title) {
        super(title);
    }
    
    @Override
    public void play() {
        System.out.println("播放音訊：" + getTitle());
    }
}

/**
 * 安全型別轉換示範
 */
public class SafeCastingDemo {
    
    static void prepare(MediaAsset asset) {
        // Java 11 相容寫法：先檢查型別，再強制轉型
        if (asset instanceof VideoAsset) {
            VideoAsset video = (VideoAsset) asset;
            video.changeResolution("1080p");
        }
        asset.play();
    }
    
    public static void main(String[] args) {
        System.out.println("========== 測試影片資產 ==========");
        prepare(new VideoAsset("Tree Tutorial", "720p"));
        
        System.out.println("\n========== 測試音訊資產 ==========");
        prepare(new AudioAsset("Queue Podcast"));
    }
}