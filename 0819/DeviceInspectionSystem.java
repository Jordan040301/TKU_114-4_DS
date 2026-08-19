/**
 * 設備檢測系統
 * 指定檔名：DeviceInspectionSystem.java
 */
public class DeviceInspectionSystem {
    
    public static void main(String[] args) {
        System.out.println("========== 設備檢測系統 ==========");
        
        // 使用 Device[] 保存至少四個物件
        Device[] devices = new Device[] {
            new Laptop("ASUS ZenBook", "Intel i7", 16),
            new Printer("Epson L3250", "噴墨", 1200),
            new Router("TP-Link AX73", "Wi-Fi 6", 5400),
            new Laptop("Dell XPS 13", "Intel i5", 8),
            new Printer("HP LaserJet", "雷射", 2400),
            new Router("Asus RT-AC86U", "Wi-Fi 5", 2900)
        };
        
        System.out.println("\n========== 執行所有設備檢測 ==========");
        // 每個物件都以多型執行 runDiagnostic()
        for (Device device : devices) {
            System.out.println("\n--- 檢測設備：" + device.getDeviceName() + " ---");
            device.runDiagnostic();
            
            // 使用 instanceof 僅對印表機執行清潔
            // Java 11 寫法：先檢查型別，再強制轉型
            if (device instanceof Printer) {
                Printer printer = (Printer) device;
                printer.cleanPrintHead();
            }
        }
        
        System.out.println("\n========== 設備清單總覽 ==========");
        printDeviceSummary(devices);
        
        System.out.println("\n========== 印表機清潔狀態報告 ==========");
        printPrinterStatus(devices);
    }
    
    /**
     * 輸出所有設備摘要
     */
    public static void printDeviceSummary(Device[] devices) {
        for (int i = 0; i < devices.length; i++) {
            Device d = devices[i];
            System.out.printf("%d. %s - %s%n", 
                            (i + 1), 
                            d.getClass().getSimpleName(), 
                            d.getDeviceName());
        }
    }
    
    /**
     * 輸出所有印表機的清潔狀態
     * 只針對 Printer 類型處理
     */
    public static void printPrinterStatus(Device[] devices) {
        boolean hasPrinter = false;
        for (Device device : devices) {
            // Java 11 寫法：先檢查型別，再強制轉型
            if (device instanceof Printer) {
                Printer printer = (Printer) device;
                hasPrinter = true;
                System.out.printf("印表機：%s - 列印頁數：%d - 需要清潔：%s%n",
                                printer.getDeviceName(),
                                printer.getPagesPrinted(),
                                printer.needsCleaning() ? "是" : "否");
            }
        }
        if (!hasPrinter) {
            System.out.println("沒有印表機設備");
        }
    }
}

/**
 * 抽象設備類別
 */
abstract class Device {
    private String deviceName;
    private String model;
    
    public Device(String deviceName, String model) {
        this.deviceName = deviceName;
        this.model = model;
    }
    
    public String getDeviceName() {
        return deviceName;
    }
    
    public String getModel() {
        return model;
    }
    
    public abstract void runDiagnostic();
}

/**
 * 筆記型電腦類別
 */
class Laptop extends Device {
    private String processor;
    private int memory;
    
    public Laptop(String deviceName, String processor, int memory) {
        super(deviceName, deviceName);
        this.processor = processor;
        this.memory = memory;
    }
    
    @Override
    public void runDiagnostic() {
        System.out.println("  【筆記型電腦診斷】");
        System.out.println("    處理器：" + processor);
        System.out.println("    記憶體：" + memory + " GB");
        if (memory < 4) {
            System.out.println("    ⚠️ 警告：記憶體不足，建議升級");
        } else {
            System.out.println("    ✅ 系統狀態良好");
        }
    }
}

/**
 * 印表機類別 - 只有 Printer 有 cleanPrintHead()
 */
class Printer extends Device {
    private String printType;
    private int pagesPrinted;
    private static final int CLEAN_THRESHOLD = 500;
    
    public Printer(String deviceName, String printType, int pagesPrinted) {
        super(deviceName, deviceName);
        this.printType = printType;
        this.pagesPrinted = pagesPrinted;
    }
    
    @Override
    public void runDiagnostic() {
        System.out.println("  【印表機診斷】");
        System.out.println("    列印類型：" + printType);
        System.out.println("    已列印頁數：" + pagesPrinted + " 頁");
        if (pagesPrinted > 1000) {
            System.out.println("    ⚠️ 建議更換耗材");
        } else if (pagesPrinted > 500) {
            System.out.println("    ⚠️ 耗材即將用盡");
        } else {
            System.out.println("    ✅ 列印狀態良好");
        }
    }
    
    /**
     * 清潔列印頭（只有 Printer 有此方法）
     */
    public void cleanPrintHead() {
        System.out.println("  【清潔列印頭】");
        System.out.println("    ✅ " + getDeviceName() + " 列印頭清潔完成！");
    }
    
    public boolean needsCleaning() {
        return pagesPrinted > CLEAN_THRESHOLD;
    }
    
    public String getPrintType() {
        return printType;
    }
    
    public int getPagesPrinted() {
        return pagesPrinted;
    }
}

/**
 * 路由器類別
 */
class Router extends Device {
    private String wifiStandard;
    private int speed;
    
    public Router(String deviceName, String wifiStandard, int speed) {
        super(deviceName, deviceName);
        this.wifiStandard = wifiStandard;
        this.speed = speed;
    }
    
    @Override
    public void runDiagnostic() {
        System.out.println("  【路由器診斷】");
        System.out.println("    Wi-Fi 標準：" + wifiStandard);
        System.out.println("    傳輸速度：" + speed + " Mbps");
        if (speed < 1000) {
            System.out.println("    ⚠️ 速度較慢，建議升級");
        } else if (speed < 3000) {
            System.out.println("    ✅ 速度正常");
        } else {
            System.out.println("    ✅ 速度優良");
        }
    }
}