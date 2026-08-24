public class GenericMethodDemo {
    static <T> void printArray(T[] data) {
        for (T value : data) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    static <T> T first(T[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        return data[0];
    }

    public static void main(String[] args) {
        String[] names = {"Amy", "Ben", "Cara"};
        Integer[] scores = {82, 75, 91};

        printArray(names);
        printArray(scores);
        System.out.println("第一個名字：" + first(names));
        System.out.println("第一個分數：" + first(scores));
    }
}