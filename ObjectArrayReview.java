class StudentRecord {
    private String id;
    private String name;
    private int score;

    StudentRecord(String id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    boolean passed() {
        return score >= 60;
    }

    int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return id + " " + name + " score=" + score;
    }
}

public class ObjectArrayReview {
    public static void main(String[] args) {
        StudentRecord[] records = {
            new StudentRecord("S101", "Amy", 82),
            new StudentRecord("S102", "Ben", 55),
            new StudentRecord("S103", "Cara", 91)
        };

        int total = 0;
        for (StudentRecord record : records) {
            total += record.getScore();
            System.out.println(record + " passed=" + record.passed());
        }

        System.out.println("平均：" + total / records.length);
    }
}