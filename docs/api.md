# API Reference

> **Note:** The implemented API currently lives under `/api/...` (e.g. `/api/projects`, `/api/users/me`, `/api/skills/me`). The `/api/v1` prefix and some paths below are the **target** contract; update this page as endpoints change.

Base path (target): `/api/v1`. The live contract is generated OpenAPI (Swagger UI at `/swagger-ui.html`); this page is the human-readable summary.

## Conventions

| Topic | Rule |
|---|---|
| Versioning | All routes under `/api/v1` |
| Auth | `Authorization: Bearer <access token>` unless marked public |
| Validation | Bean Validation on every request DTO |
| Pagination | `?page=0&size=20&sort=field,desc`; response includes `content`, `page`, `size`, `totalElements` |
| Idempotency | `Idempotency-Key` header on retry-sensitive POSTs (connections, roadmap generation) |
| IDs | UUIDs |

## Endpoints

### Authentication (public unless noted)
| Method | Path | Description |
|---|---|---|
| POST | `/auth/register` | Create account |
| POST | `/auth/login` | Returns access + refresh tokens |
| POST | `/auth/refresh` | Rotates refresh token |
| POST | `/auth/logout` | Revokes refresh token (auth) |

### Profile
| Method | Path | Description |
|---|---|---|
| GET | `/profile/me` | Current profile |
| PUT | `/profile/me` | Update profile |
| POST | `/profile/me/skills` | Add/update a skill |
| DELETE | `/profile/me/skills/{skillId}` | Remove a skill |

### Skills
| Method | Path | Description |
|---|---|---|
| GET | `/skills/search?q=` | Search skill catalog |
| GET | `/skills/{id}` | Skill detail |

### Matching
| Method | Path | Description |
|---|---|---|
| GET | `/matches` | Ranked peers for current user (paginated) |
| GET | `/matches/{userId}` | Compatibility breakdown with one user |

### Connections
| Method | Path | Description |
|---|---|---|
| POST | `/connections/{userId}` | Send request |
| PATCH | `/connections/{connectionId}` | Accept / reject / cancel |
| GET | `/connections` | List (filter by status) |

### Conversations
| Method | Path | Description |
|---|---|---|
| GET | `/conversations` | My conversations |
| GET | `/conversations/{id}/messages` | Paginated history (members only) |

### Roadmaps
| Method | Path | Description |
|---|---|---|
| POST | `/roadmaps` | Generate roadmap for a target role |
| GET | `/roadmaps` | List mine |
| GET | `/roadmaps/{id}` | Detail with nodes and prerequisites |

### DSA tracker (post-MVP)
| Method | Path | Description |
|---|---|---|
| GET | `/dsa/sheets` | Available sheets |
| GET | `/dsa/sheets/{id}/questions` | Questions with my status |
| PATCH | `/dsa/questions/{id}` | Update my status |

### Notifications
| Method | Path | Description |
|---|---|---|
| GET | `/notifications` | Mine, paginated |
| PATCH | `/notifications/{id}/read` | Mark read |

## Error format

```json
{
  "timestamp": "2026-10-05T18:30:00Z",
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "Invalid request",
  "path": "/api/v1/profile/me",
  "validationErrors": { "bio": "Bio must not exceed 500 characters" }
}
```

| Code | HTTP | Meaning |
|---|---|---|
| VALIDATION_ERROR | 400 | Bad input |
| UNAUTHENTICATED | 401 | Missing/invalid/expired token |
| FORBIDDEN | 403 | Authenticated but not allowed (object-level) |
| NOT_FOUND | 404 | Resource missing or not visible to caller |
| CONFLICT | 409 | Duplicate or version conflict |
| RATE_LIMITED | 429 | Too many requests; see `Retry-After` |
| AI_UNAVAILABLE | 503 | AI dependency failed or timed out |

## WebSocket (STOMP)

| Item | Value |
|---|---|
| Endpoint | `/ws` (JWT sent on CONNECT; rejected if invalid) |
| Send message | `SEND /app/conversations/{id}/messages` |
| Receive messages | `SUBSCRIBE /user/queue/conversations/{id}` |
| Typing | `SEND /app/conversations/{id}/typing` |
| Read receipt | `SEND /app/conversations/{id}/read` |
| Presence | `SUBSCRIBE /user/queue/presence` |

Message payload: `{ "clientMessageId": "uuid", "content": "string", "type": "TEXT" }`. The server acks with the persisted `id` and `sentAt`; resending the same `clientMessageId` is a no-op (duplicate handling for reconnects). Membership is checked on every subscribe and send.

## AI service (internal only, not public)
`POST /ai/v1/embeddings`, `/match`, `/chat-assist`, `/roadmap`, `/repository-analysis`. Called by the backend with a service credential; never exposed to browsers.
