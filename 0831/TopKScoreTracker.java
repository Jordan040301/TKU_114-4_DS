import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TopKScoreTracker {
    static List<Integer> topK(List<Integer> scores, int k) {
        if (scores == null || k <= 0) return List.of();
        PriorityQueue<Integer> top = new PriorityQueue<>();

        for (Integer score : scores) {
            if (score == null) continue;
            top.offer(score);
            if (top.size() > k) top.poll();
            System.out.println("score=" + score + " heap=" + top);
        }

        List<Integer> result = new ArrayList<>(top);
        result.sort(Comparator.reverseOrder());
        return result;
    }

    public static void main(String[] args) {
        List<Integer> scores = List.of(70, 90, 60, 85, 100, 75);
        System.out.println("top3=" + topK(scores, 3));
        System.out.println("top0=" + topK(scores, 0));
        System.out.println("null=" + topK(null, 3));
    }
}