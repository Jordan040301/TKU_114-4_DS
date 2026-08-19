import java.util.Arrays;

final class ScoreSnapshot {
    private final String course;
    private final int[] scores;

    ScoreSnapshot(String course, int[] scores) {
        this.course = course == null ? "Unknown" : course;
        this.scores = scores == null
                ? new int[0]
                : Arrays.copyOf(scores, scores.length);
    }

    int[] getScores() {
        return Arrays.copyOf(scores, scores.length);
    }

    double average() {
        if (scores.length == 0) {
            return 0.0;
        }
        int total = 0;
        for (int score : scores) {
            total += score;
        }
        return (double) total / scores.length;
    }

    @Override
    public String toString() {
        return course + " " + Arrays.toString(scores);
    }
}

public class DefensiveCopyDemo {
    public static void main(String[] args) {
        int[] original = {80, 90, 70};
        ScoreSnapshot snapshot = new ScoreSnapshot("Java", original);

        original[0] = 0;
        int[] received = snapshot.getScores();
        received[1] = 0;

        System.out.println("original=" + Arrays.toString(original));
        System.out.println("received=" + Arrays.toString(received));
        System.out.println("snapshot=" + snapshot);
        System.out.printf("average=%.1f%n", snapshot.average());
    }
}