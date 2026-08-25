import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListPolymorphismDemo {
    static void fillAndPrint(String label, List<String> list) {
        list.add("Tree");
        list.add("Heap");
        list.add("Graph");
        list.add(1, "List");

        System.out.println(label + "：" + list);
        System.out.println("index 2：" + list.get(2));
    }

    public static void main(String[] args) {
        List<String> arrayBased = new ArrayList<>();
        List<String> linked = new LinkedList<>();

        fillAndPrint("ArrayList", arrayBased);
        fillAndPrint("LinkedList", linked);
    }
}