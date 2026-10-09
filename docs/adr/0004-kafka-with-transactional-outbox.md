# ADR-0004: Kafka events published through a transactional outbox

**Status:** Accepted

**Context:** Domain events must not be lost or published for rolled-back transactions.

**Decision:** Write events to an `outbox_event` table in the same transaction as the state change; a relay publishes to Kafka. Consumers are idempotent; failures go to a DLQ.

**Alternatives:** Publish directly after commit (rejected: dual-write gap).

**Consequences:** At-least-once delivery, hence idempotent consumers; small added latency.
