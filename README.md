# SkillSphere 🎯

**A Peer-to-Peer Skill Exchange Platform**

SkillSphere connects people who want to learn with people who can teach. Post the skills you can teach, browse the skills you want to learn, and find your perfect skill-exchange partner — from coding to design and everything in between.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)
![React](https://img.shields.io/badge/React-18-61DAFB)
![License](https://img.shields.io/badge/license-MIT-blue)

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

SkillSphere is a full-stack web application where users register as **tutors**, **students**, or both. The platform matches people based on complementary skills — if you can teach Python and want to learn UI design, SkillSphere helps you find someone who wants to learn Python and can teach design.

## Tech Stack

**Backend**
- Spring Boot 3.2, Java 17
- Spring Security + JWT Authentication
- Spring Data JPA — H2 (development) / PostgreSQL (production)
- Maven

**Frontend**
- React 18 + Vite
- Tailwind CSS
- React Router
- Axios

## Features

- ✅ User registration and JWT-based authentication
- ✅ Browse and search skills by category
- ✅ Add skills you can teach or want to learn
- ✅ Find teachers for any given skill
- ✅ Smart mutual-matching algorithm
- ✅ User profiles with skill management
- ✅ Responsive React frontend

## Project Structure

```
skillsphere/
├── src/main/java/com/skillsphere/    # Spring Boot backend
│   ├── config/                        # Security, JWT, data seeding
│   ├── controller/                    # REST API endpoints
│   ├── dto/                           # Request/response objects
│   ├── entity/                        # JPA entities
│   ├── exception/                     # Custom exceptions
│   ├── repository/                    # Data access layer
│   └── service/                       # Business logic
│
└── frontend/                          # React frontend
    ├── src/
    │   ├── components/                # Reusable UI components
    │   ├── context/                   # Auth context
    │   ├── pages/                     # Page components
    │   └── services/                  # API client
    ├── index.html
    └── package.json
```

## Getting Started

### Prerequisites

- Java 17+
- Node.js 18+ and npm
- Maven (or use the included `mvnw` wrapper)

### Run Both Frontend & Backend (Recommended)

**Terminal 1 — Backend**
```bash
git clone https://github.com/Vedanshsa/skillsphere-main.git
cd skillsphere-main
./mvnw spring-boot:run
```

**Terminal 2 — Frontend**
```bash
cd frontend
npm install
npm run dev
```

| Service     | URL                                                          |
|-------------|---------------------------------------------------------------|
| Frontend    | http://localhost:3000                                        |
| Backend API | http://localhost:8080                                         |
| H2 Console  | http://localhost:8080/h2-console                              |

### Backend Only (API Testing)

```bash
./mvnw spring-boot:run
```

Test the API with Postman, curl, or any REST client at `http://localhost:8080`.

## API Endpoints

### Authentication (Public)

| Method | Endpoint              | Description               |
|--------|-----------------------|----------------------------|
| POST   | `/api/auth/register`  | Register a new user        |
| POST   | `/api/auth/login`     | Login and receive JWT token|

### Skills (Public)

| Method | Endpoint                                  | Description           |
|--------|--------------------------------------------|------------------------|
| GET    | `/api/skills/browse`                      | List all skills       |
| GET    | `/api/skills/browse/{id}`                 | Get skill details     |
| GET    | `/api/skills/categories`                  | List all categories   |
| GET    | `/api/skills/browse/category/{category}`  | Skills by category    |
| GET    | `/api/skills/search?q=python`             | Search skills         |

### Skills (Protected — requires JWT)

| Method | Endpoint                                   | Description                  |
|--------|----------------------------------------------|--------------------------------|
| POST   | `/api/skills`                              | Create a new skill            |
| POST   | `/api/skills/me`                           | Add a skill to your profile   |
| DELETE | `/api/skills/me/{skillId}?type=TEACHING`   | Remove a skill from profile   |
| GET    | `/api/skills/{skillId}/teachers`           | Find teachers for a skill     |
| GET    | `/api/skills/{skillId}/learners`           | Find learners for a skill     |
| GET    | `/api/skills/recommendations`              | Get recommended teachers      |

### Users (Protected — requires JWT)

| Method | Endpoint                       | Description                 |
|--------|----------------------------------|-------------------------------|
| GET    | `/api/users/me`                | Get your profile             |
| PUT    | `/api/users/me`                | Update your profile          |
| GET    | `/api/users/{userId}`          | Get a user's profile         |
| GET    | `/api/users/search?name=john`  | Search users                 |
| GET    | `/api/users/matches`           | Find mutual skill matches    |

## Architecture Notes

1. **Layered design** — Controller → Service → Repository
2. **Security** — Stateless JWT authentication with BCrypt password hashing
3. **Matching algorithm** — JPQL queries power skill-based user matching
4. **Frontend state** — React Context API for auth and shared state
5. **Scaling** — Ready for PostgreSQL, Redis caching, and Docker deployment

## What This Project Can Do

- **Authentication & accounts** — secure sign-up/login with JWT-based sessions and BCrypt-hashed passwords
- **Dual-role profiles** — any user can be a tutor, a student, or both, and manage a list of skills they teach and skills they want to learn
- **Skill discovery** — browse all skills, filter by category, or run a keyword search
- **Teacher/learner lookup** — for any skill, pull the list of people currently teaching it or wanting to learn it
- **Mutual matching** — an algorithm (via JPQL queries) that surfaces users whose "teach" and "learn" lists complement yours, not just a flat skill search
- **Recommendations** — suggested teachers based on a user's stated learning interests
- **Profile management** — view and update your own profile, search other users by name
- **Responsive UI** — a React + Tailwind frontend that works across screen sizes

## What I Learned Building This

- **End-to-end full-stack integration** — wiring a Spring Boot REST API to a separate React SPA, including CORS configuration and a clean Axios-based API client layer
- **Stateless authentication** — implementing JWT auth with Spring Security from scratch: token generation/validation, filters, and securing specific endpoints while keeping others public
- **Layered backend architecture** — separating concerns cleanly across controller, service, repository, DTO, and entity layers so business logic doesn't leak into controllers
- **Relational modeling for matching** — designing JPA entities and relationships (users ↔ skills, teaching vs. learning associations) that a matching algorithm can query efficiently
- **Writing non-trivial JPQL** — moving beyond basic CRUD to queries that express "find me the people who complement my skill set"
- **Frontend state management** — using React's Context API to manage auth state and share it across pages without prop-drilling
- **Environment-aware configuration** — running on H2 in-memory for fast local development while designing the JPA layer to be portable to PostgreSQL in production
- **Thinking about production readiness early** — even at MVP stage, planning for where Redis caching, Docker, and a real database would slot in later

## Concurrency & Scalability

No formal load test has been run against this project yet, so rather than quote an unverified number, here's the capacity estimate worked out from the stack's default configuration:

**1. Web layer — Tomcat's thread pool**
Spring Boot's embedded Tomcat ships with `server.tomcat.max-threads = 200` by default. Each in-flight HTTP request occupies one thread for its duration, so the web tier alone can hold **up to 200 concurrent requests** before new requests start queueing.

**2. Data layer — the actual bottleneck**
Spring Boot's default connection pool (HikariCP) ships with `maximum-pool-size = 10`. Every request that touches the database needs a connection from this pool, so the *effective* concurrency ceiling isn't Tomcat's 200 — it's whichever layer runs out first, and with a pool of 10 that's the database:

```
effective concurrent capacity ≈ min(tomcat_threads, db_pool_size × requests_per_connection_per_sec × avg_hold_time)
```

With a 10-connection pool and a typical read query completing in ~20–50ms, the pool can cycle roughly 200–500 short queries/second — meaning **the realistic sustained concurrency is closer to the pool size (10) than the thread count (200)** unless the pool is explicitly resized.

**3. What this implies in practice**
- **H2 (dev/in-memory)**: single-file, single-JVM — treat as effectively single-user; not representative of production concurrency at all.
- **PostgreSQL (prod) with defaults**: bounded by the 10-connection pool, so sustained heavy concurrent load (many users hitting DB-backed endpoints at once) queues past ~10 simultaneous DB operations, even though Tomcat could accept 200 requests.
- **To raise the ceiling**, the fix is tuning `spring.datasource.hikari.maximum-pool-size` upward (commonly 20–50 for small production deployments) so the DB pool stops being the limiter before Tomcat does, plus adding a read cache (Redis) for hot endpoints like `/api/skills/browse` so most browsing traffic never touches the pool at all.

**Bottom line:** with default settings, the system is architected around a **~10-connection database bottleneck**, not the 200-thread web tier — a common and fixable default-config gap, and a concrete, defensible thing to point to if asked "how would you scale this?" in an interview.

## Roadmap

- 📅 Session booking & scheduling
- ⭐ Reviews and ratings
- 💬 Real-time messaging
- 📧 Email notifications
- 🐳 Docker deployment

## Contributing

Contributions are welcome! Please open an issue to discuss what you'd like to change before submitting a pull request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a pull request

## License

This project is licensed under the MIT License — see the `LICENSE` file for details.

---

Built with ❤️ for learning full-stack development.
