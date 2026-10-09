# Development Guide

## Prerequisites
Java 17+, Node 20+, Python 3.11+, Docker.

## Setup
1. `cp .env.example .env` and fill placeholders
2. `docker compose -f infra/docker-compose.yml up -d`
3. Start backend, frontend, AI service (see [deployment.md](deployment.md))

## Repository layout

```text
skillsphere/
├── frontend/      React + TypeScript + Vite
├── backend/       Spring Boot modular monolith
├── ai-service/    FastAPI
├── infra/         Docker Compose, DB/Redis/Kafka config
├── docs/          Architecture, API, ADRs
└── .github/workflows/
```

## Backend conventions
- Package by domain: `auth, profile, skill, matching, connection, chat, notification, common`
- Controller → Service → Repository; business logic only in services
- Request/response DTOs, never entities; validate with `@Valid`
- One global exception handler returning the standard error format
- Flyway migrations for every schema change
- Paginate every list endpoint

## Frontend conventions
- Zustand for client/session state, TanStack Query for server state
- Tailwind + shadcn/ui components; forms validated client-side and server-side
- No tokens in `localStorage` if cookie sessions are used

## Git
`main` protected; short-lived `feature/*` and `fix/*` branches; conventional commits.
