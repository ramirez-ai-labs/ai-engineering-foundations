"""Array basics - indexing, iteration, and why they matter for AI.

Python's built-in ``list`` is a dynamic array: contiguous storage of references,
O(1) access by index, amortized O(1) append. Unlike a Java ``int[]``, it can grow.
Used everywhere: batches of embeddings, token IDs, feature vectors.

For dense numeric work (real embedding batches) you would use NumPy arrays,
which store raw numbers contiguously instead of references to int objects.
"""


def main() -> None:
    # 1. Declaration and initialization.
    # A list stores values in order, and each value is accessed by its index.
    numbers = [10, 20, 30, 40, 50]
    print(f"Length: {len(numbers)}")  # 5

    # 2. Indexing - O(1) access.
    # Python jumps straight to the position; no scan from the beginning.
    print(f"numbers[2] = {numbers[2]}")  # 30

    # 3. Iteration with an index.
    # range(len(numbers)) produces the positions 0, 1, 2, 3, 4.
    # Python shortcut: for index, number in enumerate(numbers)
    print("Iteration:", end=" ")
    for index in range(len(numbers)):
        print(f"[{index}]={numbers[index]}", end=" ")
    print()

    # 4. Plain for loop when you only care about the values.
    print("For-each:", end=" ")
    for number in numbers:
        print(number, end=" ")
    print()

    # 5. Copy.
    # Building a new list means changing numbers_copy[0] does not change the
    # original. This is a shallow copy: the new list refers to the same values.
    # Python shortcut: numbers.copy() or numbers[:]
    numbers_copy: list[int] = []
    for number in numbers:
        numbers_copy.append(number)
    numbers_copy[0] = 99
    print(f"Original [0]: {numbers[0]}")  # still 10
    print(f"Copy [0]: {numbers_copy[0]}")  # 99

    # 6. 2D list - a list of lists.
    # In AI work this often represents a batch of vectors or a small matrix.
    batch = [
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9],
    ]
    print(f"Batch shape: {len(batch)} x {len(batch[0])}")

    # Pitfall: this shortcut creates three references to the SAME inner list,
    # so one write appears to change every row.
    shared_rows = [[0] * 3] * 3  # shortcut-ok: this pitfall is the lesson
    shared_rows[0][0] = 1

    # Creating a new inner list on each pass gives independent rows.
    # Python shortcut: [[0] * 3 for _ in range(3)]
    independent_rows: list[list[int]] = []
    for row_index in range(3):
        row: list[int] = []
        for column_index in range(3):
            row.append(0)
        independent_rows.append(row)
    independent_rows[0][0] = 1

    print(f"Shared rows after one write: {shared_rows}")
    print(f"Independent rows after one write: {independent_rows}")


if __name__ == "__main__":
    main()
