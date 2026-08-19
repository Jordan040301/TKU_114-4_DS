/**
 * 報表輸出工廠
 * 指定檔名：ReportExporterFactory.java
 * 
 * 定義 ReportExporter 介面，建立 CsvExporter、JsonExporter 與 TextExporter
 * createExporter(String format) 回傳 ReportExporter
 * exportReport(ReportExporter exporter, String title, int[] values) 只依賴介面
 * 不支援的格式回傳 null
 * values 為 null 時不發生例外
 * 主流程不可使用 instanceof 選擇輸出格式
 */
public class ReportExporterFactory {
    
    public static void main(String[] args) {
        System.out.println("========== 報表輸出工廠系統 ==========");
        
        // 測試資料
        String reportTitle = "2024年度銷售報告";
        int[] salesData = {120, 85, 200, 150, 90, 180, 220, 160, 195, 210, 175, 230};
        
        // 測試各種格式
        System.out.println("\n========== 測試 CSV 格式 ==========");
        testExport("CSV", reportTitle, salesData);
        
        System.out.println("\n========== 測試 JSON 格式 ==========");
        testExport("JSON", reportTitle, salesData);
        
        System.out.println("\n========== 測試 TEXT 格式 ==========");
        testExport("TEXT", reportTitle, salesData);
        
        System.out.println("\n========== 測試不支援的格式 ==========");
        testExport("XML", reportTitle, salesData);
        testExport("PDF", reportTitle, salesData);
        
        System.out.println("\n========== 測試 null 資料 ==========");
        testExport("CSV", "空資料報表", null);
        testExport("JSON", "空資料報表", null);
        
        System.out.println("\n========== 測試空陣列 ==========");
        testExport("CSV", "空陣列報表", new int[0]);
        
        System.out.println("\n========== 測試所有支援格式 ==========");
        testAllSupportedFormats(reportTitle, salesData);
    }
    
    /**
     * 測試匯出功能
     * 只依賴 ReportExporter 介面
     */
    public static void testExport(String format, String title, int[] values) {
        System.out.println("\n格式：" + format);
        
        // 使用工廠建立匯出器
        ReportExporter exporter = ReportExporterFactory.createExporter(format);
        
        if (exporter == null) {
            System.out.println("  ❌ 不支援的格式：" + format);
            return;
        }
        
        // 匯出報表（只依賴介面）
        String result = ReportExporterFactory.exportReport(exporter, title, values);
        System.out.println("  ✅ 匯出成功");
        System.out.println("  內容：\n" + result);
    }
    
    /**
     * 測試所有支援的格式
     */
    public static void testAllSupportedFormats(String title, int[] values) {
        System.out.println("\n========== 所有支援格式比較 ==========");
        
        String[] formats = {"CSV", "JSON", "TEXT"};
        
        for (String format : formats) {
            ReportExporter exporter = ReportExporterFactory.createExporter(format);
            if (exporter != null) {
                System.out.println("\n--- " + format + " 格式 ---");
                String result = ReportExporterFactory.exportReport(exporter, title, values);
                System.out.println(result);
            }
        }
    }
    
    // ========== 工廠方法 ==========
    
    /**
     * 建立報表匯出器（工廠方法）
     * @param format 匯出格式（CSV、JSON、TEXT）
     * @return ReportExporter 實例，不支援的格式回傳 null
     */
    public static ReportExporter createExporter(String format) {
        if (format == null) {
            return null;
        }
        
        String upperFormat = format.toUpperCase().trim();
        
        switch (upperFormat) {
            case "CSV":
                return new CsvExporter();
            case "JSON":
                return new JsonExporter();
            case "TEXT":
                return new TextExporter();
            default:
                // 不支援的格式回傳 null
                return null;
        }
    }
    
    /**
     * 匯出報表（只依賴 ReportExporter 介面）
     * @param exporter 報表匯出器
     * @param title 報表標題
     * @param values 資料陣列（可為 null）
     * @return 匯出的報表內容
     */
    public static String exportReport(ReportExporter exporter, String title, int[] values) {
        // 依賴介面，不依賴具體實作
        return exporter.export(title, values);
    }
}

/**
 * 報表匯出器介面
 * 定義報表匯出的標準契約
 */
interface ReportExporter {
    
    /**
     * 匯出報表
     * @param title 報表標題
     * @param values 資料陣列（可為 null）
     * @return 匯出的報表內容（字串格式）
     */
    String export(String title, int[] values);
}

/**
 * CSV 格式匯出器
 * 輸出逗號分隔的資料格式
 */
class CsvExporter implements ReportExporter {
    private static final String SEPARATOR = ",";
    private static final String LINE_BREAK = "\n";
    
    @Override
    public String export(String title, int[] values) {
        StringBuilder sb = new StringBuilder();
        
        // 處理 null 標題
        String reportTitle = (title == null) ? "無標題" : title;
        
        // 輸出標題列
        sb.append("報表標題").append(SEPARATOR).append(reportTitle).append(LINE_BREAK);
        sb.append("資料筆數").append(SEPARATOR);
        
        // 處理 null 或空陣列
        if (values == null || values.length == 0) {
            sb.append("0").append(LINE_BREAK);
            sb.append("資料").append(SEPARATOR).append("無資料").append(LINE_BREAK);
            return sb.toString();
        }
        
        sb.append(values.length).append(LINE_BREAK);
        
        // 輸出資料
        sb.append("索引").append(SEPARATOR).append("數值").append(LINE_BREAK);
        for (int i = 0; i < values.length; i++) {
            sb.append(i + 1).append(SEPARATOR).append(values[i]).append(LINE_BREAK);
        }
        
        // 輸出統計資訊
        sb.append(LINE_BREAK).append("統計資訊").append(LINE_BREAK);
        sb.append("總計").append(SEPARATOR).append(calculateSum(values)).append(LINE_BREAK);
        sb.append("平均").append(SEPARATOR).append(String.format("%.2f", calculateAverage(values))).append(LINE_BREAK);
        sb.append("最大值").append(SEPARATOR).append(findMax(values)).append(LINE_BREAK);
        sb.append("最小值").append(SEPARATOR).append(findMin(values)).append(LINE_BREAK);
        
        return sb.toString();
    }
    
    private int calculateSum(int[] values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }
    
    private double calculateAverage(int[] values) {
        if (values.length == 0) return 0;
        return (double) calculateSum(values) / values.length;
    }
    
    private int findMax(int[] values) {
        int max = values[0];
        for (int v : values) {
            if (v > max) max = v;
        }
        return max;
    }
    
    private int findMin(int[] values) {
        int min = values[0];
        for (int v : values) {
            if (v < min) min = v;
        }
        return min;
    }
}

/**
 * JSON 格式匯出器
 * 輸出 JSON 格式的資料
 */
class JsonExporter implements ReportExporter {
    
    @Override
    public String export(String title, int[] values) {
        StringBuilder sb = new StringBuilder();
        
        // 處理 null 標題
        String reportTitle = (title == null) ? "無標題" : title;
        
        sb.append("{").append("\n");
        sb.append("  \"報表標題\": \"").append(escapeJson(reportTitle)).append("\",\n");
        
        // 處理 null 或空陣列
        if (values == null || values.length == 0) {
            sb.append("  \"資料筆數\": 0,\n");
            sb.append("  \"資料\": []\n");
            sb.append("}");
            return sb.toString();
        }
        
        sb.append("  \"資料筆數\": ").append(values.length).append(",\n");
        
        // 輸出資料
        sb.append("  \"資料\": [");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(values[i]);
        }
        sb.append("],\n");
        
        // 輸出統計資訊
        sb.append("  \"統計資訊\": {\n");
        sb.append("    \"總計\": ").append(calculateSum(values)).append(",\n");
        sb.append("    \"平均\": ").append(String.format("%.2f", calculateAverage(values))).append(",\n");
        sb.append("    \"最大值\": ").append(findMax(values)).append(",\n");
        sb.append("    \"最小值\": ").append(findMin(values)).append("\n");
        sb.append("  }\n");
        sb.append("}");
        
        return sb.toString();
    }
    
    private String escapeJson(String text) {
        return text.replace("\"", "\\\"");
    }
    
    private int calculateSum(int[] values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }
    
    private double calculateAverage(int[] values) {
        if (values.length == 0) return 0;
        return (double) calculateSum(values) / values.length;
    }
    
    private int findMax(int[] values) {
        int max = values[0];
        for (int v : values) {
            if (v > max) max = v;
        }
        return max;
    }
    
    private int findMin(int[] values) {
        int min = values[0];
        for (int v : values) {
            if (v < min) min = v;
        }
        return min;
    }
}

/**
 * TEXT 格式匯出器
 * 輸出純文字格式的報表
 */
class TextExporter implements ReportExporter {
    private static final String LINE = "=".repeat(50);
    
    @Override
    public String export(String title, int[] values) {
        StringBuilder sb = new StringBuilder();
        
        // 處理 null 標題
        String reportTitle = (title == null) ? "無標題" : title;
        
        sb.append(LINE).append("\n");
        sb.append("              ").append(reportTitle).append("\n");
        sb.append(LINE).append("\n");
        
        // 處理 null 或空陣列
        if (values == null || values.length == 0) {
            sb.append("資料筆數：0\n");
            sb.append("資料：無資料\n");
            sb.append(LINE).append("\n");
            return sb.toString();  // 這裡 return 後，後面的程式碼不會執行
        }
        
        // 有資料時執行以下程式碼
        sb.append("資料筆數：").append(values.length).append("\n");
        sb.append("-".repeat(30)).append("\n");
        
        // 輸出資料
        sb.append("索引\t數值\n");
        for (int i = 0; i < values.length; i++) {
            sb.append(i + 1).append("\t").append(values[i]).append("\n");
        }
        
        sb.append("-".repeat(30)).append("\n");
        
        // 輸出統計資訊
        sb.append("統計資訊：\n");
        sb.append("  總計：").append(calculateSum(values)).append("\n");
        sb.append("  平均：").append(String.format("%.2f", calculateAverage(values))).append("\n");
        sb.append("  最大值：").append(findMax(values)).append("\n");
        sb.append("  最小值：").append(findMin(values)).append("\n");
        sb.append(LINE).append("\n");
        
        return sb.toString();
    }
    
    /**
     * 計算總和
     */
    private int calculateSum(int[] values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }
    
    /**
     * 計算平均值
     */
    private double calculateAverage(int[] values) {
        if (values.length == 0) return 0;
        return (double) calculateSum(values) / values.length;
    }
    
    /**
     * 找出最大值
     */
    private int findMax(int[] values) {
        int max = values[0];
        for (int v : values) {
            if (v > max) max = v;
        }
        return max;
    }
    
    /**
     * 找出最小值
     */
    private int findMin(int[] values) {
        int min = values[0];
        for (int v : values) {
            if (v < min) min = v;
        }
        return min;
    }
}