# ADR-0001: Modular monolith before microservices

**Status:** Accepted

**Context:** Solo/small team, domain boundaries not yet proven, need fast iteration and debuggability.

**Decision:** Build one Spring Boot application with strict module boundaries. Extract a service only for a real boundary, scaling need, or deployment need.

**Alternatives:** Microservices from day one (rejected: operational overhead, distributed failures before product fit).

**Consequences:** Simpler deployment and transactions now; discipline needed to keep modules decoupled so extraction stays possible.
