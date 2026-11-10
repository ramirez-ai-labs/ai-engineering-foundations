import java.util.LinkedList;
import java.util.Queue;

/**
 * Queue - FIFO. Used for task queues, message passing, and BFS.
 *
 * In AI systems: job queues for inference, event queues for pipelines,
 * or any "process in order" workload.
 */
public class Ex01SimpleQueueDemo {

    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();

        // Enqueue
        queue.offer("first");
        queue.offer("second");
        queue.offer("third");
        System.out.println("Queue: " + queue);

        // Peek (don't remove)
        System.out.println("Peek: " + queue.peek());

        // Dequeue
        System.out.println("Poll: " + queue.poll());
        System.out.println("Poll: " + queue.poll());
        System.out.println("Queue after 2 polls: " + queue);

        // Add two more items, then process the remaining queue until empty.
        queue.offer("A");
        queue.offer("B");
        System.out.print("Process in order: ");
        while (!queue.isEmpty()) {
            System.out.print(queue.poll() + " ");
        }
        System.out.println();
    }
}
