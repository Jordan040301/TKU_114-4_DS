class ScoreRecord {
    private String name;
    private int score;

    ScoreRecord(String name, int score) {
        this.name = name;
        this.score = score;
    }

    void addBonus(int points) {
        if (points > 0) {
            score += points;
        }
    }

    int getScore() {
        return score;
    }

    ScoreRecord copy() {
        return new ScoreRecord(name, score);
    }
}

public class ObjectReferenceReview {
    public static void main(String[] args) {
        ScoreRecord first = new ScoreRecord("Amy", 70);
        ScoreRecord alias = first;
        ScoreRecord copy = first.copy();

        alias.addBonus(10);

        System.out.println("first：" + first.getScore());
        System.out.println("alias：" + alias.getScore());
        System.out.println("copy：" + copy.getScore());
        System.out.println("first == alias：" + (first == alias));
        System.out.println("first == copy：" + (first == copy));
    }
}