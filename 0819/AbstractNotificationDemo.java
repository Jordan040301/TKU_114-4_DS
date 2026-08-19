abstract class Notification {
    private String receiver;

    Notification(String receiver) {
        this.receiver = receiver;
    }

    final void send(String message) {
        if (message == null || message.isBlank()) {
            System.out.println("訊息不可為空");
            return;
        }
        deliver(receiver, message);
    }

    protected abstract void deliver(String receiver, String message);
}

class EmailNotification extends Notification {
    EmailNotification(String receiver) {
        super(receiver);
    }

    @Override
    protected void deliver(String receiver, String message) {
        System.out.println("EMAIL to " + receiver + "：" + message);
    }
}

class SmsNotification extends Notification {
    SmsNotification(String receiver) {
        super(receiver);
    }

    @Override
    protected void deliver(String receiver, String message) {
        System.out.println("SMS to " + receiver + "：" + message);
    }
}

public class AbstractNotificationDemo {
    public static void main(String[] args) {
        Notification email = new EmailNotification("amy@example.com");
        Notification sms = new SmsNotification("0912345678");

        email.send("Assignment uploaded");
        sms.send("Class starts at 10:10");
        sms.send("   ");
    }
}