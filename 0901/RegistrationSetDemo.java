import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegistrationSetDemo {
    static List<String> duplicates(List<String> ids) {
        Set<String> accepted = new HashSet<>();
        Set<String> reported = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        if (ids == null) return duplicates;

        for (String id : ids) {
            if (id == null || id.isBlank()) continue;
            String normalized = id.trim().toUpperCase();
            if (!accepted.add(normalized) && reported.add(normalized)) {
                duplicates.add(normalized);
            }
        }
        return duplicates;
    }

    public static void main(String[] args) {
        List<String> ids = List.of("A01", "B02", " a01 ", "C03", "B02", "B02");
        System.out.println("duplicates=" + duplicates(ids));
    }
}