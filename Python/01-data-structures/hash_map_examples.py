"""Hash map - O(1) average lookup/insert/delete by key.

Python's ``dict`` is a hash map. Since Python 3.7 it also preserves insertion
order (Java's HashMap does not).
In AI: vocabulary (token -> id), feature maps, caching embeddings by key.
"""


def main() -> None:
    # 1. Create and put.
    # A dict stores key/value pairs instead of values at numeric positions.
    word_to_id: dict[str, int] = {}
    word_to_id["hello"] = 1
    word_to_id["world"] = 2
    word_to_id["ai"] = 3
    print(f"word_to_id: {word_to_id}")

    # 2. Get - O(1) average lookup.
    # d[key] raises KeyError for a missing key; d.get(key) returns None instead.
    print(f"id of 'ai': {word_to_id['ai']}")
    print(f"id of 'missing': {word_to_id.get('missing')}")  # None

    # 3. get with a default avoids None checks when a key may not exist yet.
    print(f"get('foo', 0): {word_to_id.get('foo', 0)}")

    # 4. Membership checks whether the key exists before reading it.
    print(f"'world' in word_to_id: {'world' in word_to_id}")

    # 5. Iterate through every key currently stored, then look up its value.
    # Python shortcut: for key, value in word_to_id.items()
    print("Entries:", end=" ")
    for key in word_to_id:
        value = word_to_id[key]
        print(f"{key}={value}", end=" ")
    print()

    # 6. Count frequency.
    # Classic map pattern: if a word has been seen before, increase its count;
    # otherwise start the count at 1.
    # Python shortcuts: freq[token] = freq.get(token, 0) + 1, or collections.Counter
    tokens = ["the", "cat", "the", "dog", "the"]
    freq: dict[str, int] = {}
    for token in tokens:
        if token in freq:
            freq[token] = freq[token] + 1
        else:
            freq[token] = 1
    print(f"Token frequencies: {freq}")


if __name__ == "__main__":
    main()
