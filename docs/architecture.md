# Architecture

## 1. Principle
Start with a **modular Spring Boot monolith**. Extract a service only when there is a real domain boundary, a distinct scaling need, or an independent deployment need. Boundaries follow **business ownership**, not technical layers.

## 2. Stage A: Modular monolith (foundation)

```mermaid
flowchart TD
    FE[React + TypeScript SPA] -->|REST / WebSocket| API[Spring Boot API]
    API --> PG[(PostgreSQL)]
    API --> RD[(Redis)]
    subgraph Modules
      A[auth] --- P[profile] --- S[skill] --- M[matching] --- C[connection] --- CH[chat] --- N[notification]
    end
    API --- Modules
```

## 3. Stage B: Event-driven extension

```mermaid
flowchart LR
    API[Spring Boot] -->|domain events| K{{Kafka}}
    K --> AI[AI Service - FastAPI]
    K --> NS[Notification consumer]
    AI --> EMB[Embeddings via Gemini]
    EMB --> VEC[(pgvector)]
```

## 4. Stage C: Selective microservices (only if justified)

```mermaid
flowchart TD
    GW[API Gateway] --> AUTH[Auth Service]
    GW --> PROF[Profile Service]
    GW --> COLLAB[Collaboration Service]
    AUTH & PROF & COLLAB --> K{{Kafka}}
    K --> AI[AI Service] --> VEC[(pgvector)]
    K --> NOTIF[Notification Service]
```

## 5. Module ownership

| Module | Owns | Publishes events |
|---|---|---|
| auth | users, credentials, refresh tokens | UserRegistered, UserDeleted |
| profile | profile, projects, user skills | ProfileUpdated, ProjectUpdated |
| skill | skill catalog | none |
| matching | scores, match queries | none |
| connection | connection requests | ConnectionCreated |
| chat | conversations, messages, presence | MessageSent |
| notification | notifications | none |
| common | errors, config, security utils | none |

Modules talk through service interfaces or events, never through each other's repositories.

## 6. Layering inside each module

```text
controller  →  service  →  repository  →  entity
     ↑ DTOs (validated)       ↑ never returned from APIs
```

## 7. Chat message flow

```mermaid
sequenceDiagram
    participant C as Client
    participant W as WebSocket/STOMP
    participant S as Chat Service
    participant DB as PostgreSQL
    participant R as Redis
    participant K as Kafka
    C->>W: connect (JWT)
    W->>S: authenticate + authorize membership
    C->>S: send message (clientMessageId)
    S->>S: validate
    S->>DB: persist
    S-->>C: ack (durable)
    S->>K: MessageSent
    S->>R: update typing/presence (TTL)
    S-->>C: push to recipient
```

## 8. Failure behaviour

| Dependency down | Expected behaviour |
|---|---|
| Redis | Chat still works; presence/typing degrade; cache misses go to DB |
| Kafka | Core API keeps working; events retried via outbox (see ADR-0004) |
| AI service / Gemini | Matching falls back to rule-based score; SkillBot returns a friendly error |
| PostgreSQL | Service unhealthy; readiness probe fails |
