# Performance Notes

No performance claim is made until it is measured. Record results here with date, hardware, dataset size, and tool.

## Measurement plan

| Scenario | Metric | Tool | Result |
|---|---|---|---|
| `GET /matches` with N profiles | p50/p95 latency | k6 | _not measured_ |
| Chat send → receive | end-to-end latency | custom WS client | _not measured_ |
| Concurrent WebSocket connections | max stable connections | k6 / Gatling | _not measured_ |
| Embedding generation | latency, throughput | pytest benchmark | _not measured_ |
| Kafka consumer | lag under load | Kafka metrics | _not measured_ |
| pgvector search | latency vs index type | SQL benchmark | _not measured_ |

## Optimization candidates
Redis caching of match results, pgvector HNSW/IVFFlat index, keyset pagination for messages, connection pool tuning.
