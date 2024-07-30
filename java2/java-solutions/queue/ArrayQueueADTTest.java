package queue;

import static queue.ArrayQueueADT.*;

public class ArrayQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = create();
        while (size(queue1) < 5) {
            enqueue(queue1, "_q_1_" + 3);
        }
        for (int i = 1; i < 5; i++) {
            enqueue(queue2, "_q_1_" + i);
        }
        System.out.print("Size is different: ");
        System.out.println(size(queue1) != size(queue2));
        int min = Math.min(size(queue1), size(queue2)) - 1;
        for (int i = 0; i < min; i++) {
            System.out.println("queue1's first element is " + element(queue1));
            System.out.println("queue2's first element is " + element(queue2));
            if (!dequeue(queue1).equals(dequeue(queue2))) {
                System.out.println("elements are different: true");
            } else {
                System.out.println("elements are different: false");
            }
        }
        while (!isEmpty(queue1)) {
            System.out.println(dequeue(queue1) + " left in queue1");
        }
        while (!isEmpty(queue2)) {
            System.out.println(dequeue(queue2) + " left in queue2");
        }
        enqueue(queue1, 2);
        enqueue(queue1, 6);
        System.out.println("size of queue1: " + size(queue1));
        clear(queue1);
        System.out.println("Is queue empty?: " + isEmpty(queue1) + " and size is: " + size(queue1));
    }
}