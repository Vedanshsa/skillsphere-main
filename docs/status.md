# Status & Plan

Classify each feature as **IMPLEMENTED / PARTIAL / MISSING / PLANNED**. Current column reflects an audit of the repository on the GitLab repo.

## Current → Target matrix

| Feature | Current | Target |
|---|---|---|
| Authentication | PARTIAL: JWT + BCrypt work; single 24h token, no refresh/rotation, no rate limits | Hardened JWT, refresh rotation, RBAC |
| Profiles and skills | IMPLEMENTED (basic): profile, skill catalog, teach/learn types; lists unpaginated | Production-grade with validation and tests |
| Projects | IMPLEMENTED (Slice 1): CRUD with ownership checks, paginated; untested against a live DB | CRUD, ownership checks |
| Skill matching | PARTIAL: JPQL mutual-match and recommendations; no scoring, no evaluation set | Explainable score, then semantic |
| Connections | MISSING | Request/accept flow, idempotent |
| Chat | MISSING | WebSocket, persistence, read receipts |
| Redis | MISSING | Presence, typing, caching |
| Kafka | MISSING | Versioned events, DLQ |
| Gemini / pgvector | MISSING | Embeddings, semantic search |
| Docker | PARTIAL: Dockerfile + docker-compose (Postgres + backend) | Full local environment |
| CI/CD | PARTIAL: GitLab CI builds and tests (backend + frontend); no deploy | GitLab CI pipeline with deploy |
| API docs | MISSING: no OpenAPI yet | Current OpenAPI |

## Priorities

| Priority | Feature |
|---|---|
| P0 | Identity, profiles, skills, matching, auth, chat |
| P1 | Redis, Gemini matching, pgvector, Kafka |
| P2 | AI roadmap, GitHub aggregation, file sharing |
| P3 | WebRTC, microservice split |

## 12-week plan

| Week | Deliverable | Definition of done |
|---|---|---|
| 1 | Repo, Docker, DB, standards | Fresh clone runs locally |
| 2 | Auth + roles | Register/login/refresh/logout, protected APIs |
| 3 | Profile + skills | CRUD, validation, authorization, tests |
| 4 | Projects + dashboard | Portfolio UI, pagination, API docs |
| 5 | Discovery + connections | Search, recommendations v1, connection flow |
| 6 | Chat MVP | WebSocket, persistence, history |
| 7 | Redis + presence | Online/typing state, cache strategy |
| 8 | Kafka | Profile/message events, reliable consumers |
| 9 | AI service | FastAPI + Gemini, structured responses |
| 10 | pgvector matching | Embeddings, ranked complementary peers |
| 11 | Roadmap + SkillBot | End-to-end AI workflows |
| 12 | Hardening + deploy | Tests, CI/CD, observability, live demo |

## Post-MVP
GitHub aggregation, DSA tracker, file sharing, WebRTC, repository verification, selective microservice extraction.

## Definition of done (summary)
A new user can register → create profile → add skills → find a compatible peer → connect → chat → use AI → generate a roadmap, end to end, with tests, docs, and a live deployment.

## Job-facing deliverables
- [ ] Live URL
- [ ] Clean repo and README
- [ ] Architecture and ER diagrams
- [ ] OpenAPI docs and API collection
- [ ] Automated tests, Docker Compose, CI/CD
- [ ] ADRs
- [ ] Demo video
- [ ] Performance and security notes
- [ ] Known limitations
