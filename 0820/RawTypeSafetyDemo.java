import java.util.ArrayList;
import java.util.List;

public class RawTypeSafetyDemo {
    @SuppressWarnings({"rawtypes", "unchecked"})
    static void rawTypeExample() {
        List raw = new ArrayList();
        raw.add("Amy");
        raw.add(100);

        try {
            String value = (String) raw.get(1);
            System.out.println(value);
        } catch (ClassCastException exception) {
            System.out.println("raw type error: Integer cannot become String");
        }
    }

    static void genericExample() {
        List<String> names = new ArrayList<>();
        names.add("Amy");
        names.add("Ben");
        System.out.println(names);
    }

    public static void main(String[] args) {
        rawTypeExample();
        genericExample();
    }
}