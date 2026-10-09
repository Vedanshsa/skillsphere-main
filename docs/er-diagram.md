# Data Model

```mermaid
erDiagram
    USER ||--|| PROFILE : has
    USER ||--o{ USER_SKILL : has
    SKILL ||--o{ USER_SKILL : referenced_by
    USER ||--o{ PROJECT : owns
    USER ||--o{ CONNECTION : requests
    USER ||--o{ CONNECTION : receives
    CONVERSATION ||--o{ CONVERSATION_MEMBER : has
    USER ||--o{ CONVERSATION_MEMBER : joins
    CONVERSATION ||--o{ MESSAGE : contains
    USER ||--o{ MESSAGE : sends
    USER ||--o{ NOTIFICATION : receives
    USER ||--o{ ROADMAP : owns
    ROADMAP ||--o{ ROADMAP_NODE : contains
    USER ||--o{ USER_QUESTION : tracks
    DSA_QUESTION ||--o{ USER_QUESTION : referenced_by

    USER { uuid id PK
      string email UK
      string password_hash
      string role
      string status
      timestamp created_at
      timestamp updated_at
      int version }
    PROFILE { uuid user_id PK,FK
      string headline
      string bio
      string experience_level
      string goals
      string github_username
      string avatar_url }
    SKILL { uuid id PK
      string name UK
      string category }
    USER_SKILL { uuid user_id FK
      uuid skill_id FK
      string level
      decimal years
      bool wants_to_teach
      bool wants_to_learn }
    PROJECT { uuid id PK
      uuid user_id FK
      string name
      string description
      string repo_url
      string live_url
      string tech_stack
      timestamp created_at }
    CONNECTION { uuid id PK
      uuid requester_id FK
      uuid receiver_id FK
      string status
      timestamp created_at }
    CONVERSATION { uuid id PK
      string type
      timestamp created_at }
    CONVERSATION_MEMBER { uuid conversation_id FK
      uuid user_id FK }
    MESSAGE { uuid id PK
      uuid conversation_id FK
      uuid sender_id FK
      string client_message_id
      text content
      string type
      timestamp sent_at
      timestamp read_at }
    NOTIFICATION { uuid id PK
      uuid user_id FK
      string type
      jsonb payload
      timestamp read_at
      timestamp created_at }
    ROADMAP { uuid id PK
      uuid user_id FK
      string target_role
      int version
      timestamp generated_at }
    ROADMAP_NODE { uuid id PK
      uuid roadmap_id FK
      string title
      int sort_order
      string status }
    DSA_QUESTION { uuid id PK
      string sheet
      string title
      string difficulty
      string url }
    USER_QUESTION { uuid user_id FK
      uuid question_id FK
      string status
      timestamp solved_at }
```

Additional tables (not shown): `profile_embedding(user_id, embedding vector, model, updated_at)`, `roadmap_node_prerequisite(node_id, prerequisite_id)`, `refresh_token(id, user_id, token_hash, expires_at, revoked_at)`, `outbox_event(id, aggregate, type, payload, created_at, published_at)`.

## Database rules

| Rule | Detail |
|---|---|
| IDs | UUIDs for externally visible identifiers |
| Uniqueness | `user.email`, `skill.name`, `(requester_id, receiver_id)` connection pair, `(conversation_id, client_message_id)` |
| Indexes | All foreign keys; discovery filters (skill, level, wants_to_teach/learn); `message(conversation_id, sent_at)` |
| Transactions | Required for multi-table writes |
| Concurrency | Optimistic locking (`version`) where concurrent updates are possible |
| Pagination | Required on every list query |
| Secrets | No raw passwords; refresh tokens stored hashed |
| Migrations | Flyway or Liquibase only; no manual production schema edits |
