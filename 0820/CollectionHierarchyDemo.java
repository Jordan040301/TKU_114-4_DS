import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionHierarchyDemo {
    static void printCollection(String label, Collection<String> data) {
        System.out.println(label + " size=" + data.size() + " " + data);
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Tree");
        list.add("Graph");
        list.add("Tree");

        Set<String> set = new HashSet<>();
        set.add("Tree");
        set.add("Graph");
        set.add("Tree");

        printCollection("List", list);
        printCollection("Set", set);
        System.out.println("List index 1：" + list.get(1));
    }
}