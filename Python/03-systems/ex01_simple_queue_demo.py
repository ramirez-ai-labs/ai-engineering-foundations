"""Queue - FIFO. Used for task queues, message passing, and BFS.

In AI systems: job queues for inference, event queues for pipelines,
or any "process in order" workload.

``collections.deque`` is the single-threaded FIFO. For producer/consumer work
across threads use ``queue.Queue``; across processes or services use a broker
(SQS, Redis Streams, Kafka).
"""

from collections import deque


def main() -> None:
    queue: deque[str] = deque()

    # Enqueue.
    queue.append("first")
    queue.append("second")
    queue.append("third")
    print(f"Queue: {list(queue)}")

    # Peek (don't remove).
    print(f"Peek: {queue[0]}")

    # Dequeue - O(1) from the left end.
    print(f"Poll: {queue.popleft()}")
    print(f"Poll: {queue.popleft()}")
    print(f"Queue after 2 polls: {list(queue)}")

    # Add two more items, then process the remaining queue until empty.
    queue.append("A")
    queue.append("B")
    print("Process in order:", end=" ")
    while len(queue) > 0:
        print(queue.popleft(), end=" ")
    print()


if __name__ == "__main__":
    main()
