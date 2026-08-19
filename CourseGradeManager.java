public class CourseGradeManager {
    
    public static void main(String[] args) {
        System.out.println("========== 建立學生成績資料 ==========");
        
        // 建立至少五筆學生成績資料
        CourseGrade[] students = new CourseGrade[] {
            new CourseGrade("S001", "王小明", 85, 78, 92),
            new CourseGrade("S002", "張小美", 45, 55, 60),
            new CourseGrade("S003", "李大華", 92, 88, 90),
            new CourseGrade("S004", "陳小芳", 30, 40, 35),
            new CourseGrade("S005", "林小強", 78, 82, 75),
            new CourseGrade("S006", "黃小婷", 95, 90, 88),
            new CourseGrade("S007", "吳小豪", 20, 25, 30)
        };
        
        System.out.println("========== 輸出所有學生成績摘要 ==========");
        printAllStudents(students);
        
        System.out.println("\n========== 成績統計分析 ==========");
        calculateStatistics(students);
        
        System.out.println("\n========== 不及格名單（總分低於60分） ==========");
        printFailedStudents(students);
        
        System.out.println("\n========== 各等級人數統計 ==========");
        printGradeDistribution(students);
    }
    
    /**
     * 輸出所有學生成績摘要
     */
    public static void printAllStudents(CourseGrade[] students) {
        for (CourseGrade student : students) {
            System.out.println(student);
        }
    }
    
    /**
     * 計算並輸出統計資料：平均分、最高分、最低分
     */
    public static void calculateStatistics(CourseGrade[] students) {
        if (students == null || students.length == 0) {
            System.out.println("無學生資料");
            return;
        }
        
        double total = 0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        CourseGrade maxStudent = null;
        CourseGrade minStudent = null;
        
        for (CourseGrade student : students) {
            double score = student.calculateFinalScore();
            total += score;
            
            if (score > max) {
                max = score;
                maxStudent = student;
            }
            if (score < min) {
                min = score;
                minStudent = student;
            }
        }
        
        double average = total / students.length;
        
        System.out.printf("班級人數：%d 人%n", students.length);
        System.out.printf("總分平均：%.2f 分%n", average);
        System.out.printf("最高分：%.2f 分（%s）%n", max, maxStudent.getName());
        System.out.printf("最低分：%.2f 分（%s）%n", min, minStudent.getName());
    }
    
    /**
     * 輸出不及格名單（總分低於60分）
     */
    public static void printFailedStudents(CourseGrade[] students) {
        boolean found = false;
        for (CourseGrade student : students) {
            double score = student.calculateFinalScore();
            if (score < 60) {
                System.out.printf("%s - 總分：%.2f 分 - 等級：%s%n", 
                                 student.getName(), score, student.getLevel());
                found = true;
            }
        }
        if (!found) {
            System.out.println("恭喜！全班同學都及格！");
        }
    }
    
    /**
     * 輸出各等級人數統計
     */
    public static void printGradeDistribution(CourseGrade[] students) {
        int countA = 0, countB = 0, countC = 0, countD = 0, countF = 0;
        
        for (CourseGrade student : students) {
            String level = student.getLevel();
            switch (level) {
                case "A": countA++; break;
                case "B": countB++; break;
                case "C": countC++; break;
                case "D": countD++; break;
                case "F": countF++; break;
                default: break;
            }
        }
        
        System.out.println("A（90-100）：" + countA + " 人");
        System.out.println("B（80-89）：" + countB + " 人");
        System.out.println("C（70-79）：" + countC + " 人");
        System.out.println("D（60-69）：" + countD + " 人");
        System.out.println("F（0-59）：" + countF + " 人");
    }
}

/**
 * 課程成績類別 - 保存學生各項成績
 */
class CourseGrade {
    private final String studentId;      // 學號
    private final String name;            // 姓名
    private int participationScore;       // 平時成績（出席成績）
    private int midtermScore;             // 期中考成績
    private int finalScore;               // 期末考成績
    
    /**
     * 建構子 - 各成績限制為 0 到 100
     */
    public CourseGrade(String studentId, String name, 
                       int participationScore, int midtermScore, int finalScore) {
        this.studentId = studentId;
        this.name = name;
        
        // 確保各成績在 0 到 100 之間
        this.participationScore = clampScore(participationScore);
        this.midtermScore = clampScore(midtermScore);
        this.finalScore = clampScore(finalScore);
    }
    
    /**
     * 將成績限制在 0 到 100 之間
     */
    private int clampScore(int score) {
        if (score < 0) return 0;
        if (score > 100) return 100;
        return score;
    }
    
    /**
     * 計算總分
     * 平時成績 50% + 期中考 20% + 期末考 20% + 出席 10%
     */
    public double calculateFinalScore() {
        return participationScore * 0.5 +
               midtermScore * 0.2 +
               finalScore * 0.2 +
               participationScore * 0.1;  // 出席成績使用平時成績的 10%
    }
    
    /**
     * 依總分回傳等級
     * A：90-100，B：80-89，C：70-79，D：60-69，F：0-59
     */
    public String getLevel() {
        double score = calculateFinalScore();
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }
    
    /**
     * Getter 方法
     */
    public String getStudentId() {
        return studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public int getParticipationScore() {
        return participationScore;
    }
    
    public int getMidtermScore() {
        return midtermScore;
    }
    
    public int getFinalScore() {
        return finalScore;
    }
    
    /**
     * 覆寫 toString() - 輸出完整摘要
     */
    @Override
    public String toString() {
        double finalScore = calculateFinalScore();
        String level = getLevel();
        
        return String.format("學號：%s，姓名：%s，平時：%d分，期中：%d分，期末：%d分，總分：%.2f分，等級：%s",
                           studentId, name, participationScore, midtermScore, finalScore, 
                           finalScore, level);
    }
}