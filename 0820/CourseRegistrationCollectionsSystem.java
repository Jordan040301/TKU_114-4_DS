import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CourseEnrollment {
    private final String studentId;
    private final String name;
    private int score;
    private final Set<String> tags = new HashSet<>();

    CourseEnrollment(String studentId, String name, int score) {
        this.studentId = studentId;
        this.name = name;
        this.score = Math.max(0, Math.min(100, score));
    }

    String getStudentId() {
        return studentId;
    }

    int getScore() {
        return score;
    }

    void addTag(String tag) {
        if (tag != null && !tag.isBlank()) {
            tags.add(tag.toLowerCase());
        }
    }

    boolean hasTag(String tag) {
        return tag != null && tags.contains(tag.toLowerCase());
    }

    @Override
    public String toString() {
        return studentId + " " + name + " score=" + score + " tags=" + tags;
    }
}

class RegistrationBook {
    private final List<CourseEnrollment> order = new ArrayList<>();
    private final Set<String> registeredIds = new HashSet<>();
    private final Map<String, CourseEnrollment> byId = new HashMap<>();

    boolean enroll(CourseEnrollment enrollment) {
        if (enrollment == null
                || !registeredIds.add(enrollment.getStudentId())) {
            return false;
        }
        order.add(enrollment);
        byId.put(enrollment.getStudentId(), enrollment);
        return true;
    }

    CourseEnrollment find(String studentId) {
        return byId.get(studentId);
    }

    List<CourseEnrollment> ranking() {
        List<CourseEnrollment> result = new ArrayList<>(order);
        result.sort(Comparator.comparingInt(CourseEnrollment::getScore)
                .reversed()
                .thenComparing(CourseEnrollment::getStudentId));
        return result;
    }

    void removeBelow(int minimum) {
        order.removeIf(enrollment -> enrollment.getScore() < minimum);
        registeredIds.clear();
        byId.clear();
        for (CourseEnrollment enrollment : order) {
            registeredIds.add(enrollment.getStudentId());
            byId.put(enrollment.getStudentId(), enrollment);
        }
    }
}

public class CourseRegistrationCollectionsSystem {
    public static void main(String[] args) {
        RegistrationBook book = new RegistrationBook();
        CourseEnrollment amy = new CourseEnrollment("S101", "Amy", 88);
        CourseEnrollment ben = new CourseEnrollment("S102", "Ben", 55);
        CourseEnrollment cara = new CourseEnrollment("S103", "Cara", 92);

        amy.addTag("Java");
        amy.addTag("java");
        cara.addTag("Tree");

        System.out.println("enroll Amy=" + book.enroll(amy));
        System.out.println("duplicate=" + book.enroll(
                new CourseEnrollment("S101", "Amy2", 100)));
        book.enroll(ben);
        book.enroll(cara);

        System.out.println("find=" + book.find("S102"));
        System.out.println("ranking=" + book.ranking());
        book.removeBelow(60);
        System.out.println("after cleanup=" + book.ranking());
    }
}