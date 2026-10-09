# ADR-0002: PostgreSQL with pgvector for relational data and embeddings

**Status:** Accepted

**Context:** Need relational integrity plus similarity search over profile embeddings at modest scale.

**Decision:** Use PostgreSQL as the source of truth and the pgvector extension for embeddings.

**Alternatives:** Dedicated vector DB (rejected for now: extra infrastructure, consistency issues).

**Consequences:** One datastore to run and back up; revisit if vector volume or latency outgrows pgvector.
