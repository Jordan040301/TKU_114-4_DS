import java.util.Objects;

class Member {
    private final String id;
    private String name;

    Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Member{id='" + id + "', name='" + name + "'}";
    }

    @Override
public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (!(other instanceof Member)) {   // 只做类型检查
        return false;
    }
    Member member = (Member) other;     // 显式转换
    return Objects.equals(id, member.id);
}
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

public class ObjectEqualityDemo {
    public static void main(String[] args) {
        Member a = new Member("M001", "Amy");
        Member b = new Member("M001", "Amy Chen");
        Member c = a;

        System.out.println(a);
        System.out.println("a == b: " + (a == b));
        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("a == c: " + (a == c));
    }
}