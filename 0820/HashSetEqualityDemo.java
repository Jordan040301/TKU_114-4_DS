import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class EnrollmentKey {
    private final String studentId;
    private final String studentName;

    EnrollmentKey(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
    }

  @Override
public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (!(other instanceof EnrollmentKey)) {
        return false;
    }
    EnrollmentKey key = (EnrollmentKey) other;  // 显式强转
    return Objects.equals(studentId, key.studentId);
}

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return studentId + " " + studentName;
    }
}

public class HashSetEqualityDemo {
    public static void main(String[] args) {
        Set<EnrollmentKey> enrollments = new HashSet<>();

        System.out.println(enrollments.add(
                new EnrollmentKey("S101", "Amy")));
        System.out.println(enrollments.add(
                new EnrollmentKey("S101", "Amy Chen")));
        System.out.println(enrollments.add(
                new EnrollmentKey("S102", "Ben")));
        System.out.println("size=" + enrollments.size());
    }
}