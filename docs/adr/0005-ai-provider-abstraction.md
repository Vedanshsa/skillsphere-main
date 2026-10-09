# ADR-0005: AI behind a provider interface in a separate FastAPI service

**Status:** Accepted

**Context:** AI tooling is Python-centric and models change quickly; the core API should not depend on one vendor.

**Decision:** Run AI as a FastAPI service exposing `LLMProvider` with a Gemini implementation. Store embedding model name and dimension with each vector.

**Alternatives:** Call Gemini directly from Spring Boot (rejected: vendor coupling, weaker ML ecosystem).

**Consequences:** One more service to operate; swapping models needs a re-index job.
