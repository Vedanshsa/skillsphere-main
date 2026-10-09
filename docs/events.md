# Event Architecture (Kafka)

## Events

| Event | Producer | Consumers | Key |
|---|---|---|---|
| UserRegistered | auth | profile, notification, analytics | userId |
| ProfileUpdated | profile | AI matching | userId |
| ProjectUpdated | profile | AI matching | userId |
| ConnectionCreated | connection | notification | connectionId |
| MessageSent | chat | notification, analytics | conversationId |
| RoadmapGenerated | AI | profile/activity | userId |
| UserDeleted | account | profile, chat, storage, vector cleanup | userId |

## Envelope

```json
{
  "eventId": "uuid",
  "type": "ProfileUpdated",
  "version": 1,
  "occurredAt": "2026-10-05T18:30:00Z",
  "correlationId": "uuid",
  "producer": "profile",
  "data": { "userId": "uuid" }
}
```

Payloads carry IDs plus essential metadata, never large bodies or message text.

## Rules

| Rule | Detail |
|---|---|
| Schemas | Versioned; additive changes only within a version |
| Idempotency | Consumers dedupe on `eventId` |
| Retries | Bounded with backoff (e.g. 3 attempts) |
| Dead letters | `<topic>.dlq` after retries exhausted; alert on depth |
| Ordering | Keyed by aggregate ID so one user's events stay ordered |
| Reliability | Producer uses a transactional outbox (ADR-0004) |
| Ownership | Each topic has exactly one owning module/service |
| Retention | Defined per topic (default 7 days; DLQ 30 days) |
| Tracing | `correlationId` propagated from the originating HTTP request |

## Topics

| Topic | Owner | Partitions (initial) |
|---|---|---|
| `user.events` | auth | 3 |
| `profile.events` | profile | 3 |
| `connection.events` | connection | 3 |
| `chat.events` | chat | 6 |
| `ai.events` | ai-service | 3 |
