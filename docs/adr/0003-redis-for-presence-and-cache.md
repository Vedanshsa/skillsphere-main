# ADR-0003: Redis for presence, typing state, and caching

**Status:** Accepted

**Context:** Presence and typing are ephemeral and high-churn; they should expire automatically.

**Decision:** Store `presence:user:{id}` and `typing:{conv}:{user}` in Redis with TTLs. Cache hot reads there.

**Alternatives:** Store in PostgreSQL (rejected: write amplification, manual cleanup).

**Consequences:** Presence is best-effort; chat correctness never depends on Redis.
