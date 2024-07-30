package queue;

public class LinkedQueue extends AbstractQueue {
    private Node tail, head;

    protected void angelEnqueue(Object element) {
        Node x = new Node(element, null);
        if (size == 0) {
            head = x;
            tail = head;
        } else {
            tail.next = x;
            tail = x;
        }
    }

    protected Object angelElement() {
        return head.value;
    }

    protected void angelRemoveElement() {
        head = head.next();
    }

    protected void angelClear() {
        head = null;
        tail = null;
    }

    private static class Node {
        private final Object value;
        private Node next;

        private Node(Object element, Node next) {
            this.value = element;
            this.next = next;
        }

        private Node next() {
            return this.next;
        }
    }
}
