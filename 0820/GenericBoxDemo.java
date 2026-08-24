class Box<T> {
    private T value;

    void set(T value) {
        this.value = value;
    }

    T get() {
        return value;
    }

    boolean isEmpty() {
        return value == null;
    }
}

public class GenericBoxDemo {
    public static void main(String[] args) {
        Box<String> textBox = new Box<>();
        Box<Integer> numberBox = new Box<>();

        textBox.set("Java");
        numberBox.set(114);

        System.out.println(textBox.get().toUpperCase());
        System.out.println(numberBox.get() + 1);
        System.out.println("textBox empty：" + textBox.isEmpty());
    }
}