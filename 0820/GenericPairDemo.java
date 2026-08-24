class Pair<K, V> {
    private K key;
    private V value;

    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    K getKey() {
        return key;
    }

    V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + " -> " + value;
    }
}

public class GenericPairDemo {
    public static void main(String[] args) {
        Pair<String, Integer> score = new Pair<>("Amy", 92);
        Pair<Integer, String> course = new Pair<>(101, "Data Structures");

        System.out.println(score);
        System.out.println(course);
        System.out.println(score.getValue() + 8);
    }
}