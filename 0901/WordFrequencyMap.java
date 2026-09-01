import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyMap {
    static Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> counts = new HashMap<>();
        if (words == null) return counts;
        for (String word : words) {
            if (word == null || word.isBlank()) continue;
            counts.merge(word.trim().toLowerCase(), 1, Integer::sum);
        }
        return counts;
    }

    static List<String> sortedReport(Map<String, Integer> counts) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey()));
        List<String> report = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entries) {
            report.add(entry.getKey() + "=" + entry.getValue());
        }
        return report;
    }

    public static void main(String[] args) {
        List<String> words = List.of("Java", "heap", "JAVA", "graph", "heap", "java");
        Map<String, Integer> counts = countWords(words);
        System.out.println(counts);
        System.out.println(sortedReport(counts));
    }
}