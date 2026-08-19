interface Printable {
    void print();
}

interface Exportable {
    String export();
}

class SystemReport implements Printable, Exportable {
    private String title;
    private int recordCount;

    SystemReport(String title, int recordCount) {
        this.title = title;
        this.recordCount = recordCount;
    }

    @Override
    public void print() {
        System.out.println(title + " records=" + recordCount);
    }

    @Override
    public String export() {
        return title + "," + recordCount;
    }
}

public class MultipleInterfaceDemo {
    public static void main(String[] args) {
        SystemReport report = new SystemReport("Inventory", 42);

        Printable printable = report;
        Exportable exportable = report;

        printable.print();
        System.out.println(exportable.export());
    }
}