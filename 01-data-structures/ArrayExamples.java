/**
 * Array basics — indexing, iteration, and why they matter for AI.
 *
 * Arrays: contiguous memory, O(1) access by index, fixed size (in Java).
 * Used everywhere: batches of embeddings, token IDs, feature vectors.
 */
public class ArrayExamples {

    public static void main(String[] args) {
        // 1. Declaration and initialization
        int[] numbers = { 10, 20, 30, 40, 50 };
        System.out.println("Length: " + numbers.length); // 5

        // 2. Indexing — O(1) access
        System.out.println("numbers[2] = " + numbers[2]); // 30

        // 3. Iteration
        System.out.print("Iteration: ");
        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i] + " ");
        }
        System.out.println();

        // 4. For-each (read-only traversal)
        System.out.print("For-each: ");
        for (int n : numbers) {
            System.out.print(n + " ");
        }
        System.out.println();

        // 5. Copy and clone (important for not mutating shared data)
        int[] copy = numbers.clone();
        copy[0] = 99;
        System.out.println("Original [0]: " + numbers[0]); // still 10
        System.out.println("Copy [0]: " + copy[0]);       // 99

        // 6. 2D array — e.g. batch of vectors (batch_size x dim)
        int[][] batch = {
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 9 }
        };
        System.out.println("Batch shape: " + batch.length + " x " + batch[0].length);
    }
}
