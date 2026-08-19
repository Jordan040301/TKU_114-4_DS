interface AlertChannel {
    boolean send(String receiver, String message);

    default boolean isValid(String receiver, String message) {
        return receiver != null && !receiver.isBlank()
                && message != null && !message.isBlank();
    }
}

class EmailAlert implements AlertChannel {
    @Override
    public boolean send(String receiver, String message) {
        if (!isValid(receiver, message) || !receiver.contains("@")) {
            return false;
        }
        System.out.println("EMAIL to " + receiver + ": " + message);
        return true;
    }
}

class ConsoleAlert implements AlertChannel {
    @Override
    public boolean send(String receiver, String message) {
        if (!isValid(receiver, message)) {
            return false;
        }
        System.out.println("CONSOLE " + receiver + ": " + message);
        return true;
    }
}

public class DefaultMethodDemo {
    public static void main(String[] args) {
        AlertChannel email = new EmailAlert();
        AlertChannel console = new ConsoleAlert();

        System.out.println(email.send("amy@example.com", "Class starts"));
        System.out.println(console.send("B113", "Class starts"));
        System.out.println(email.send("invalid", "Class starts"));
    }
}