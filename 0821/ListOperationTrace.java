import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListOperationTrace {
    static void trace(List<String> data) {
        data.add("A");
        data.add("B");
        data.add("C");
        System.out.println("尾端新增：" + data);

        data.add(1, "X");
        System.out.println("index 1 插入：" + data);

        data.remove(2);
        System.out.println("index 2 刪除：" + data);

        data.set(1, "Y");
        System.out.println("index 1 修改：" + data);
    }

    public static void main(String[] args) {
        System.out.println("ArrayList");
        trace(new ArrayList<>());

        System.out.println("LinkedList");
        trace(new LinkedList<>());
    }
}