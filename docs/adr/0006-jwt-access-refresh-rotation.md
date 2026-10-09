# ADR-0006: Short-lived JWT access tokens with rotating refresh tokens

**Status:** Accepted

**Context:** Stateless API auth with the ability to revoke sessions.

**Decision:** ~15 min access tokens; refresh tokens rotated on use and stored hashed server-side; prefer HttpOnly cookies for browsers.

**Alternatives:** Long-lived JWTs (rejected: no revocation), server sessions (viable, less suited to the planned service split).

**Consequences:** Needs refresh-token table and reuse detection.
