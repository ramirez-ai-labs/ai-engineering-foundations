import java.util.HashMap;
import java.util.Map;

/**
 * Hash map - O(1) average lookup/insert/delete by key.
 *
 * In AI: vocabulary (token -> id), feature maps, caching embeddings by key.
 */
public class HashMapExamples {

    public static void main(String[] args) {
        // 1. Create and put.
        // A map stores key/value pairs instead of values at numeric positions.
        Map<String, Integer> wordToId = new HashMap<>();
        wordToId.put("hello", 1);
        wordToId.put("world", 2);
        wordToId.put("ai", 3);
        System.out.println("wordToId: " + wordToId);

        // 2. Get - O(1) average lookup.
        // We ask for a value by key instead of by index.
        System.out.println("id of 'ai': " + wordToId.get("ai"));
        System.out.println("id of 'missing': " + wordToId.get("missing")); // null

        // 3. getOrDefault.
        // This avoids null checks when a key may not exist yet.
        System.out.println("getOrDefault('foo', 0): " + wordToId.getOrDefault("foo", 0));

        // 4. containsKey checks whether the key exists before reading it.
        System.out.println("containsKey('world'): " + wordToId.containsKey("world"));

        // 5. Iterate through every key/value pair currently stored in the map.
        System.out.print("Entries: ");
        for (Map.Entry<String, Integer> e : wordToId.entrySet()) {
            System.out.print(e.getKey() + "=" + e.getValue() + " ");
        }
        System.out.println();

        // 6. Count frequency.
        // This is a classic map pattern: if a word has been seen before, increase
        // its count; otherwise start the count at 1.
        String[] tokens = { "the", "cat", "the", "dog", "the" };
        Map<String, Integer> freq = new HashMap<>();
        for (String t : tokens) {
            freq.put(t, freq.getOrDefault(t, 0) + 1);
        }
        System.out.println("Token frequencies: " + freq);
    }
}
