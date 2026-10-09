# Testing Strategy

| Level | Examples | Tools | Goal |
|---|---|---|---|
| Unit | Services, validators, scoring | JUnit, Mockito | Business logic |
| Repository | JPA queries, constraints | Testcontainers (PostgreSQL) | Persistence correctness |
| Integration | API + PostgreSQL + Redis | Spring Boot Test, Testcontainers | Real dependency behaviour |
| WebSocket | Connect, auth, send, reconnect | Spring test client | Realtime correctness |
| Contract | REST vs OpenAPI | OpenAPI diff in CI | Frontend/backend compatibility |
| AI evaluation | Fixed profile set | pytest | Regression detection |
| Security | AuthZ, injection, uploads | Integration tests, scanners | Vulnerability prevention |
| Load | Concurrent REST and WebSocket | k6 / Gatling | Find bottlenecks |
| Frontend | Components, flows | Vitest, Testing Library | UI correctness |

## Must-have test cases
- User A cannot read or modify User B's data (per endpoint)
- Expired and revoked refresh tokens are rejected
- Duplicate `clientMessageId` does not create two messages
- Non-members cannot subscribe to or fetch a conversation
- Consumers process the same event twice without double effects
- Matching falls back when the AI service is down
- Roadmap output with cycles is rejected

## Release quality gate
- [ ] Clean build passes
- [ ] Unit and integration tests pass
- [ ] No secrets in repo or image layers
- [ ] Migrations reproducible from empty DB
- [ ] OpenAPI current
- [ ] Authorization paths tested
- [ ] Frontend production build succeeds
- [ ] `docker compose up` starts all dependencies
