class CourseSection {
    private String code;
    private String title;
    private int capacity;
    private int enrolled;

    CourseSection(String code, String title) {
        this(code, title, 30);
    }

    CourseSection(String code, String title, int capacity) {
        this.code = code == null || code.isBlank() ? "UNKNOWN" : code;
        this.title = title == null || title.isBlank() ? "Untitled" : title;
        this.capacity = capacity > 0 ? capacity : 30;
        this.enrolled = 0;
    }

    boolean enroll() {
        if (enrolled >= capacity) {
            return false;
        }
        enrolled++;
        return true;
    }

    @Override
    public String toString() {
        return code + " " + title + " " + enrolled + "/" + capacity;
    }
}

public class ConstructorOverloadingDemo {
    public static void main(String[] args) {
        CourseSection regular = new CourseSection("CS101", "Java");
        CourseSection workshop = new CourseSection("DS201", "Tree Lab", 2);

        regular.enroll();
        workshop.enroll();
        workshop.enroll();

        System.out.println(regular);
        System.out.println(workshop);
        System.out.println("third enrollment=" + workshop.enroll());
    }
}