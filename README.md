# SkillSphere

A peer-to-peer skill exchange platform. The idea is simple: instead of a one-way learning platform, people list what they can teach and what they want to learn, and SkillSphere matches them up — you get someone who teaches Python and wants to learn design, paired with someone who teaches design and wants to learn Python.

Built as a full-stack project to get real practice with backend architecture, auth, and matching logic beyond basic CRUD.

**Stack:** Java 17 · Spring Boot 3.2 · Spring Security (JWT) · Spring Data JPA · React 18 + Vite · Tailwind CSS

---

## What it does

- Register and log in with JWT-based auth (passwords hashed with BCrypt)
- Add skills you can teach or want to learn — any user can be a tutor, a student, or both
- Browse and search skills by category
- Look up who's teaching or learning a given skill
- A matching algorithm that finds people whose "teach" and "learn" lists complement yours, not just a keyword match
- Get recommended teachers based on what you're trying to learn
- View and edit your own profile, search other users

## How it's put together

Standard layered backend: `controller → service → repository`, with DTOs so the API layer never leaks JPA entities directly. Auth is stateless JWT — a filter validates the token on each request instead of using sessions. The matching logic lives in a couple of JPQL queries that join on teach/learn relationships rather than doing the matching in application code.

Frontend is a separate React SPA talking to the API over Axios, with auth state held in a Context provider so it doesn't get prop-drilled through every page.

```
skillsphere/
├── src/main/java/com/skillsphere/
│   ├── config/          # security, JWT, dev data seeding
│   ├── controller/       # REST endpoints
│   ├── dto/               # request/response objects
│   ├── entity/            # JPA entities
│   ├── exception/         # custom exceptions + global handler
│   ├── repository/
│   └── service/
└── frontend/
    └── src/
        ├── components/
        ├── context/
        ├── pages/
        └── services/
```

## Running it locally

You'll need Java 17+, Node 18+, and either Maven or the bundled `mvnw`.

**Backend**
```bash
git clone https://github.com/Vedanshsa/skillsphere-main.git
cd skillsphere-main
./mvnw spring-boot:run
```

**Frontend** (separate terminal)
```bash
cd frontend
npm install
npm run dev
```

| Service     | URL                                |
|-------------|------------------------------------|
| Frontend    | http://localhost:3000              |
| Backend API | http://localhost:8080              |
| H2 console  | http://localhost:8080/h2-console   |

By default this runs on an in-memory H2 database, which resets every restart — fine for development, not for anything real.

### Running against PostgreSQL

There's a `prod` Spring profile that switches to Postgres and pulls config from environment variables instead of hardcoding anything:

```bash
export SPRING_PROFILES_ACTIVE=prod
export JWT_SECRET=<a-real-256-bit-secret>
export DATABASE_URL=jdbc:postgresql://<host>:5432/<db>
export DATABASE_USERNAME=<user>
export DATABASE_PASSWORD=<password>
./mvnw spring-boot:run
```

Worth noting: there's no Flyway/Liquibase migration tool wired up yet, so the schema is still created automatically by Hibernate even under this profile. That's fine early on, but a migration tool should go in before this touches real user data.

## API

**Auth**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Log in, get a JWT |

**Skills — public**

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/skills/browse` | List all skills |
| GET | `/api/skills/browse/{id}` | Skill details |
| GET | `/api/skills/categories` | List categories |
| GET | `/api/skills/browse/category/{category}` | Skills by category |
| GET | `/api/skills/search?q=python` | Search |

**Skills — requires JWT**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/skills` | Create a skill |
| POST | `/api/skills/me` | Add a skill to your profile |
| DELETE | `/api/skills/me/{skillId}?type=TEACHING` | Remove a skill |
| GET | `/api/skills/{skillId}/teachers` | Who teaches this |
| GET | `/api/skills/{skillId}/learners` | Who's learning this |
| GET | `/api/skills/recommendations` | Recommended teachers for you |

**Users — requires JWT**

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/users/me` | Your profile |
| PUT | `/api/users/me` | Update your profile |
| GET | `/api/users/{userId}` | Someone else's profile |
| GET | `/api/users/search?name=john` | Search users |
| GET | `/api/users/matches` | Your mutual skill matches |

## On scaling

I haven't load-tested this, so instead of making up a number, here's the honest capacity math based on the stack's defaults:

Tomcat's embedded thread pool defaults to 200, so the web layer can hold 200 concurrent requests before queueing. But HikariCP's default connection pool is only 10 — and since almost every endpoint hits the database, that pool is the real ceiling, not Tomcat's thread count. With a 10-connection pool and ~20–50ms queries, you're realistically looking at sustained concurrency closer to 10 than to 200, unless the pool is resized.

Fix is straightforward: bump `hikari.maximum-pool-size` (20–50 is reasonable for a small deployment) and add a read cache — Redis, for something like `/api/skills/browse` — so browsing traffic doesn't hit the database pool at all. It's a common default-config gap, and one I'd point to directly if asked "how would you scale this?"

## Roadmap

Not built yet, in rough priority order:

- Session booking and scheduling
- Reviews and ratings
- Real-time messaging
- Email notifications
- Docker deployment

## What building this taught me

Mostly the stuff that doesn't show up until you're past the tutorial stage: wiring a separate frontend and backend together (CORS, a real API client layer instead of scattered fetch calls), implementing JWT auth from scratch instead of copy-pasting a snippet, keeping business logic out of controllers, and writing JPQL that actually expresses "find people who complement me" instead of a flat WHERE clause. The Context API bit for auth state was also the first time I felt the difference prop-drilling makes on a multi-page app.

## License

MIT — see `LICENSE`.