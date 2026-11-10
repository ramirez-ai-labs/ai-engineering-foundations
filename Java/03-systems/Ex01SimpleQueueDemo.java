import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Queue - FIFO. Used for task queues, message passing, and BFS.
 *
 * In AI systems: job queues for inference, event queues for pipelines,
 * or any "process in order" workload.
 *
 * ArrayDeque is the recommended single-threaded Queue: a circular array with
 * O(1) offer and poll, and better memory locality than LinkedList, which
 * allocates a node per element. It matches Python's collections.deque. For
 * producer/consumer work across threads use a BlockingQueue; across processes
 * or services use a broker (SQS, Redis Streams, Kafka).
 */
public class Ex01SimpleQueueDemo {

    public static void main(String[] args) {
        Queue<String> queue = new ArrayDeque<>();

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
