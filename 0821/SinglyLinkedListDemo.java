class ListNode<T> {
    T value;
    ListNode<T> next;

    ListNode(T value) {
        this.value = value;
    }
}

class SimpleLinkedList<T> {
    private ListNode<T> head;
    private int size;

    void addFirst(T value) {
        ListNode<T> node = new ListNode<>(value);
        node.next = head;
        head = node;
        size++;
    }

    void addLast(T value) {
        ListNode<T> node = new ListNode<>(value);
        if (head == null) {
            head = node;
        } else {
            ListNode<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        }
        size++;
    }

    T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index);
        }
        ListNode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    boolean remove(T target) {
        if (head == null) {
            return false;
        }
        if (java.util.Objects.equals(head.value, target)) {
            head = head.next;
            size--;
            return true;
        }
        ListNode<T> current = head;
        while (current.next != null) {
            if (java.util.Objects.equals(current.next.value, target)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        ListNode<T> current = head;
        while (current != null) {
            result.append(current.value);
            current = current.next;
            if (current != null) {
                result.append(", ");
            }
        }
        return result.append("]").toString();
    }
}

public class SinglyLinkedListDemo {
    public static void main(String[] args) {
        SimpleLinkedList<String> list = new SimpleLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addFirst("X");

        System.out.println(list);
        System.out.println("index 1=" + list.get(1));
        System.out.println("remove A=" + list.remove("A"));
        System.out.println(list + " size=" + list.size());
    }
}