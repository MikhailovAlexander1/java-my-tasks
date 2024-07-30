package queue;

public class ArrayQueueModuleTest {
    public static void main(String[] args) {
        int x = 1;
        while (ArrayQueueModule.size() != 5) {
            ArrayQueueModule.enqueue("_q_1_" + x);
            x++;
        }
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.dequeue());
        }
        for (int i = 1; i < 4; i++) {
            ArrayQueueModule.enqueue("_c_1_" + i);
        }
        for (int i = 0; i < 2; i++) {
            System.out.println("first element in queue is " + ArrayQueueModule.element());
        }
        System.out.println(ArrayQueueModule.size());
        ArrayQueueModule.clear();
        System.out.println("ArrayQueueModule.clear()");
        System.out.println("Is queue empty?: " + ArrayQueueModule.isEmpty() + " and size is: " + ArrayQueueModule.size());
    }
}