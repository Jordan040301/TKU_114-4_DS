import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListSetMapDemo {
    public static void main(String[] args) {
        List<String> visitHistory = new ArrayList<>();
        Set<String> uniquePages = new HashSet<>();
        Map<String, Integer> pageCounts = new HashMap<>();

        String[] pages = {"Home", "Tree", "Home", "Graph", "Tree"};

        for (String page : pages) {
            visitHistory.add(page);
            uniquePages.add(page);
            pageCounts.put(page, pageCounts.getOrDefault(page, 0) + 1);
        }

        System.out.println("歷程：" + visitHistory);
        System.out.println("不重複頁面：" + uniquePages);
        System.out.println("Home 次數：" + pageCounts.get("Home"));
        System.out.println("Graph 次數：" + pageCounts.get("Graph"));
    }
}