import java.util.Objects;

public class MemberEqualityPractice {
    
    public static void main(String[] args) {
        System.out.println("========== 建立兩個 ID 相同但 email 不同的會員 ==========");
        
        // 建立 LibraryMember 物件
        LibraryMember member1 = new LibraryMember("M001", "張三", "zhangsan@email.com");
        LibraryMember member2 = new LibraryMember("M001", "張三", "zhangsan@different.com");
        LibraryMember member3 = new LibraryMember("M002", "李四", "lisi@email.com");
        
        System.out.println("會員 1：" + member1);
        System.out.println("會員 2：" + member2);
        System.out.println("會員 3：" + member3);
        
        System.out.println("\n========== 比較結果 ==========");
        
        // 比較 member1 和 member2（ID 相同）
        System.out.println("member1 == member2：" + (member1 == member2));
        System.out.println("member1.equals(member2)：" + member1.equals(member2));
        System.out.println("member1 的 hashCode：" + member1.hashCode());
        System.out.println("member2 的 hashCode：" + member2.hashCode());
        System.out.println("hashCode 是否相等：" + (member1.hashCode() == member2.hashCode()));
        
        System.out.println();
        
        // 比較 member1 和 member3（ID 不同）
        System.out.println("member1 == member3：" + (member1 == member3));
        System.out.println("member1.equals(member3)：" + member1.equals(member3));
        System.out.println("member1 的 hashCode：" + member1.hashCode());
        System.out.println("member3 的 hashCode：" + member3.hashCode());
        System.out.println("hashCode 是否相等：" + (member1.hashCode() == member3.hashCode()));
        
        System.out.println("\n========== 邊界條件測試 ==========");
        
        // 與 null 比較
        System.out.println("member1.equals(null)：" + member1.equals(null));
        
        // 與不同型別比較
        System.out.println("member1.equals(\"字串\")：" + member1.equals("字串"));
        
        // 與自己比較
        System.out.println("member1.equals(member1)：" + member1.equals(member1));
    }
}

/**
 * 圖書館會員類別
 * 使用 memberId 判斷身分相同與否
 */
class LibraryMember {
    private String memberId;   // 會員編號
    private String name;       // 姓名
    private String email;      // 電子郵件
    
    /**
     * 建構子
     */
    public LibraryMember(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }
    
    /**
     * 覆寫 toString() - 輸出所有欄位
     */
    @Override
    public String toString() {
        return "會員編號：" + memberId + 
               "，姓名：" + name + 
               "，電子郵件：" + email;
    }
    
    /**
     * 覆寫 equals() - 只使用 memberId 判斷身分
     */
    @Override
    public boolean equals(Object other) {
        // 自己與自己比較
        if (this == other) {
            return true;
        }
        
        // 與 null 比較回傳 false
        if (other == null) {
            return false;
        }
        
        // 檢查是否為相同型別
        if (getClass() != other.getClass()) {
            return false;
        }
        
        // 轉換型別並比較 memberId
        LibraryMember that = (LibraryMember) other;
        return Objects.equals(this.memberId, that.memberId);
    }
    
    /**
     * 覆寫 hashCode() - 只使用 memberId 計算
     */
    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}