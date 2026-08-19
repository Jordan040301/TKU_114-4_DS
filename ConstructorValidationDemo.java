class CourseItem {
    private String name;
    private int points;

    CourseItem(String name, int points) {
        this.name = name == null ? "Unnamed" : name.trim();
        if (this.name.isEmpty()) {
            this.name = "Unnamed";
        }
        this.points = Math.max(0, points);
    }

    String describe() {
        return name + "：" + points + " 分";
    }
}

public class ConstructorValidationDemo {
    public static void main(String[] args) {
        CourseItem first = new CourseItem("  Tree Practice  ", 20);
        CourseItem second = new CourseItem("   ", -5);

        System.out.println(first.describe());
        System.out.println(second.describe());
    }
}