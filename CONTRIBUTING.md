# Contributing

## Branching
`main` is protected and deployable. Use short-lived branches: `feature/*`, `fix/*`, `docs/*`.

## Commits
Conventional style: `feat(auth): add refresh token rotation`, `fix(chat): dedupe reconnect messages`.

## Pull request checklist
- [ ] Builds and tests pass locally
- [ ] New logic has unit tests; DB/API changes have integration tests
- [ ] No secrets, tokens, or personal data committed
- [ ] DTOs validated; no JPA entities returned from APIs
- [ ] Object-level authorization tested for new endpoints
- [ ] OpenAPI and relevant docs updated
- [ ] DB changes shipped as migrations

## Code rules
Controller → Service → Repository. Business logic never lives in controllers. Every list endpoint is paginated.
