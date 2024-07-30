package queue;

public class ArrayQueueTestt {
    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        ArrayQueue queue1 = new ArrayQueue();
        System.out.println("enqueue() and size() are being tested: ");
        for (int i = 0; i < 5; i++) {
            queue.enqueue("q_1_" + (i + 1));
        }
        for (int i = 0; i < 6; i++) {
            queue1.enqueue("q_2_" + (i + 1));
        }
        System.out.print("sizes of these two queues are different: ");
        if (queue.size() != queue1.size()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        System.out.println("\nelement() is being tested: ");
        for (int i = 0; i < 3; i++) {
            System.out.println("first queue's (first queue) element on "
                    + (i + 1) + " call: " + queue.element());
            System.out.println("first queue's (second queue) element on "
                    + (i + 1) + " call: " + queue1.element());
        }
        System.out.println("\ndequeue() and isEmpty() are being tested: ");
        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
        System.out.println("______________");
        while (!queue1.isEmpty()) {
            System.out.println(queue1.dequeue());
        }
        System.out.println("first queue's size = " + queue.size() +
                " and second queue's size = " + queue1.size());
        System.out.println("\nclear() is being tested: ");
        System.out.println("first queue's size = " + queue.size());
        for (int i = 0; i < 3; i++) {
            System.out.println("element is added to first queue");
            queue.enqueue("q_1_" + i + 1);
        }
        System.out.println("first queue's size = " + queue.size());
        queue.clear();
        System.out.println("queue.clear()");
        System.out.println("first queue's size = " + queue.size());
    }
}
