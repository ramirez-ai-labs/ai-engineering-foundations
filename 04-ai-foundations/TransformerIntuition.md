# Transformer Intuition

This note gives the minimum mental model an AI engineer needs to reason correctly about transformer-based language models — without derailing into research-level math.

The goal is not to understand how to train a transformer. The goal is to understand what the model is doing well enough to make sound decisions about context, chunking, cost, and latency.

## What A Transformer Is

A transformer is a neural network architecture that processes sequences of tokens and produces output by attending to relationships between tokens in that sequence.

Two things matter most for AI engineers:

1. The model works on tokens, not characters or words.
2. The model can attend to any token in its context window when producing each output token.

Everything else is implementation detail.

## Tokens, Not Text

Text is converted to tokens before the model sees it. Tokenization is a learned compression scheme: common words become a single token, rare words split into multiple tokens.

Practical implications:

- 1 token ≈ 4 characters in English prose
- Code, JSON, and non-English text tokenize less efficiently
- A 128k context window holds roughly 96k words of English text, less for code
- Counting tokens before submitting a request avoids surprises on latency and cost

You cannot optimize RAG chunking, prompt engineering, or inference cost without a working model of tokenization.

## Attention: Weighted Lookup, Not Magic

The attention mechanism is the core of what makes transformers work.

At each step, the model computes three things for each token in the sequence:

- **Query (Q):** what this token is looking for
- **Key (K):** what each token in the sequence offers
- **Value (V):** what each token contributes if selected

Attention scores are computed by asking how well each Query aligns with each Key:

```text
Attention(Q, K, V) = softmax(QK^T / sqrt(d_k)) * V
```

In plain terms:

- the model computes a similarity score between the current token's query and every other token's key
- it normalizes those scores into weights (softmax)
- it takes a weighted sum of values, where high-scoring tokens contribute more

This is structurally similar to the vector search retrieval in `SimpleVectorSearchDemo.java`:

- Q is the query embedding
- K is the document embedding
- V is the document content
- softmax(QK^T) is cosine similarity normalized into a probability distribution

The difference is that attention runs this lookup for every token in the sequence simultaneously, and the weights are learned rather than fixed.

## Context Window

The context window is the maximum number of tokens the model can attend to at once.

Everything inside the window is "visible" to the model. Everything outside is invisible — the model has no memory of it.

Practical consequences for AI engineers:

| Concern | What To Know |
|---------|-------------|
| RAG chunk size | Chunks that are too large consume context budget; too small loses coherence |
| System prompt cost | A long system prompt consumes tokens on every request |
| Long document handling | Documents longer than the context window must be chunked and retrieved, not passed whole |
| Conversation history | Long chat histories must be summarized or truncated before they overflow the window |
| Cost | Tokens in the context window are charged on every inference call |

The model does not automatically prioritize the most relevant parts of a long context. It attends to all tokens, but retrieval quality still degrades at extreme lengths — a phenomenon called "lost in the middle."

## Multi-Head Attention

Real transformers use multiple attention heads running in parallel.

Each head learns to attend to a different type of relationship:

- one head might track syntactic structure
- another might track coreference (which pronoun refers to which noun)
- another might track semantic similarity

The outputs of all heads are concatenated and projected back to the model's hidden dimension.

For AI engineers, the practical implication is simple: the model is simultaneously tracking multiple types of relationships in the input, which is why transformers generalize well across different reasoning tasks.

## What This Means For AI Engineering

| System Decision | How Transformer Mechanics Informs It |
|----------------|--------------------------------------|
| Chunk size | Chunks should fit comfortably in context and contain coherent units of meaning |
| Context budget | Every token in the prompt costs money; prune aggressively |
| Re-ranking | Sending top-20 retrieved chunks for cross-encoder re-ranking works because the re-ranker uses attention over the full chunk+query pair |
| Caching | Repeated prefix tokens (system prompts, few-shot examples) can be cached as KV tensors to reduce compute — see `LRUCache.java` for the eviction logic |
| Evaluation | Models that struggle with multi-hop reasoning often fail because the relevant tokens are far apart in the sequence or split across chunk boundaries |

## What This Simplifies

Transformers in production also involve:

- positional encodings (how the model knows token order)
- layer normalization and residual connections
- feedforward sublayers between attention layers
- quantization and efficient attention variants for long contexts (FlashAttention, sliding window attention)

None of those are needed to reason well about prompt design, retrieval, chunking, or system cost.

## Suggested Next Steps

1. Read `EmbeddingsAndVectorSearch.md` to see how embeddings are produced by a transformer encoder.
2. Read `RAGArchitecture.md` to see how retrieval and generation connect at the system level.
3. Try varying chunk sizes in `SimpleVectorSearchDemo.java` and think about what a real tokenizer would do.
4. Look up the tokenizer for a model you use (e.g. `tiktoken` for GPT models, `sentencepiece` for Gemini) and count how a sample document tokenizes.
