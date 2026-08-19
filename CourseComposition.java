public class CourseComposition {
    
    public static void main(String[] args) {
        // 创建一位讲师
        Instructor instructor1 = new Instructor("I001", "張教授");
        
        // 创建两门课程，共用同一位讲师
        Course course1 = new Course("CS101", "Java程序設計", instructor1);
        Course course2 = new Course("CS102", "數據結構与算法", instructor1);
        
        // 再创建另一位讲师
        Instructor instructor2 = new Instructor("I002", "李教授");
        Course course3 = new Course("CS103", "數據庫系統", instructor2);
        
        // 输出课程摘要
        System.out.println("========== 課程信息 ==========");
        System.out.println(course1.summary());
        System.out.println(course2.summary());
        System.out.println(course3.summary());
        
        System.out.println("\n========== 驗證組合關係 ==========");
        // 验证两门课共享同一个讲师对象
        System.out.println("course1 的講師是否與 course2 相同？" + 
                          (course1.getInstructor() == course2.getInstructor()));
        System.out.println("course1 的講師ID：" + course1.getInstructor().getId());
        System.out.println("course2 的講師ID：" + course2.getInstructor().getId());
        System.out.println("是否為同一個對象？" + 
                          (course1.getInstructor() == course2.getInstructor()));
        
        // 修改讲师姓名，验证两门课同步更新（因为引用同一个对象）
        instructor1.setName("張博士");
        System.out.println("\n========== 修改講師姓名后 ==========");
        System.out.println(course1.summary());
        System.out.println(course2.summary());
    }
}

/**
 * 讲师类 - 存储讲师的基本信息
 */
class Instructor {
    private String id;
    private String name;
    
    // 构造函数
    public Instructor(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getter 方法
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    // Setter 方法（允许修改姓名）
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "講師ID：" + id + "，姓名：" + name;
    }
}

/**
 * 课程类 - 通过组合方式包含讲师对象
 */
class Course {
    private String courseCode;      // 课程代码
    private String title;           // 课程标题
    private Instructor instructor;  // 讲师引用（组合关系）
    
    // 构造函数
    public Course(String courseCode, String title, Instructor instructor) {
        this.courseCode = courseCode;
        this.title = title;
        this.instructor = instructor;  // 直接引用讲师对象，不复制讲师姓名
    }
    
    /**
     * 回传完整课程信息
     * 通过 instructor 对象取得讲师姓名，而不是直接存储姓名
     */
    public String summary() {
        return "課程代碼：" + courseCode + 
               "，標題：" + title + 
               "，授課講師：" + instructor.getName() +
               "（講師ID：" + instructor.getId() + "）";
    }
    
    // Getter 方法（方便外部访问）
    public String getCourseCode() {
        return courseCode;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Instructor getInstructor() {
        return instructor;
    }
}