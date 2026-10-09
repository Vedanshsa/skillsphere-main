# Security

## Controls

| Area | Control |
|---|---|
| Authentication | Spring Security, short-lived access token (~15 min), refresh token rotation, refresh tokens stored hashed |
| Browser sessions | Prefer `HttpOnly`, `Secure`, `SameSite` cookies where appropriate |
| Passwords | BCrypt or Argon2; never plaintext |
| Authorization | Roles `USER`, `ADMIN` plus **object-level checks** on every resource |
| CORS | Explicit allow-list; never `*` for authenticated flows |
| Rate limiting | Login, register, password reset, AI endpoints, uploads, messages |
| Input | Bean Validation; parameterized queries only |
| WebSocket | JWT on CONNECT; membership check on every subscribe/send |
| Uploads (post-MVP) | Size/type validation, signed expiring URLs, malware scan where available |
| AI | Secret redaction, data minimization, output validation |
| Secrets | `.env` locally, secret manager / env vars in deployment; never committed or baked into image layers |
| Logging | Never log passwords, tokens, API keys, or private chat content |

## Object-level authorization

User A must not be able to modify User B's project just because the endpoint exists. Every service method that takes an ID loads the resource and checks ownership or membership. Return `404` when existence should not leak.

## Threat checklist

| Threat | Mitigation |
|---|---|
| Credential stuffing | Rate limit, generic login errors |
| Token theft | Short TTL, rotation, revocation on logout |
| IDOR | Object-level checks + tests |
| Injection | Validation, parameterized queries |
| XSS | Escape/sanitize chat and profile content; CSP headers |
| CSRF | SameSite cookies / CSRF tokens if cookies used |
| Prompt injection | Treat user text as data, validate outputs, no tool access for SkillBot |
| Secret leakage | Pre-commit scanning, CI secret scan |
| Dependency CVEs | Dependabot + CI security scan |

## Reporting
Report vulnerabilities privately to the maintainer; do not open public issues for security bugs.
