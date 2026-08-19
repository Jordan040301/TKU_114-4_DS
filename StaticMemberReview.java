class SupportTicket {
    private static int nextNumber = 1;

    private int number;
    private String issue;

    SupportTicket(String issue) {
        number = nextNumber;
        nextNumber++;
        this.issue = issue;
    }

    static int getCreatedCount() {
        return nextNumber - 1;
    }

    @Override
    public String toString() {
        return "T" + number + " " + issue;
    }
}

public class StaticMemberReview {
    public static void main(String[] args) {
        SupportTicket first = new SupportTicket("Network");
        SupportTicket second = new SupportTicket("Printer");

        System.out.println(first);
        System.out.println(second);
        System.out.println("建立數量：" + SupportTicket.getCreatedCount());
    }
}