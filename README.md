# SkillSphere 🎯

**Peer-to-Peer Skill Exchange Platform**

SkillSphere connects people who want to learn with people who can teach. Post skills you can teach, browse skills you want to learn, and find your perfect skill exchange partner.

## Tech Stack

**Backend:**
- Spring Boot 3.2, Java 17
- Spring Security + JWT Authentication
- Spring Data JPA + H2 (dev) / PostgreSQL (prod)
- Maven

**Frontend:**
- React 18 + Vite
- Tailwind CSS
- React Router
- Axios

## Quick Start

### Option 1: Run Both (Recommended)

**Terminal 1 - Backend:**
```bash
git clone https://gitlab.com/vedanshsa-group/skillsphere.git
cd skillsphere
./mvnw spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm install
npm run dev
```

- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console

### Option 2: Backend Only (API Testing)

```bash
./mvnw spring-boot:run
```

Use Postman or curl to test the API at http://localhost:8080

## Features

- ✅ User registration & JWT authentication
- ✅ Browse and search skills by category
- ✅ Add skills you can teach / want to learn
- ✅ Find teachers for any skill
- ✅ Smart matching (mutual skill exchanges)
- ✅ User profiles with skill management
- ✅ Responsive React frontend

## API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Skills (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/skills/browse` | List all skills |
| GET | `/api/skills/browse/{id}` | Get skill details |
| GET | `/api/skills/categories` | List all categories |
| GET | `/api/skills/browse/category/{category}` | Skills by category |
| GET | `/api/skills/search?q=python` | Search skills |

### Skills (Protected - Requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/skills` | Create new skill |
| POST | `/api/skills/me` | Add skill to your profile |
| DELETE | `/api/skills/me/{skillId}?type=TEACHING` | Remove skill from profile |
| GET | `/api/skills/{skillId}/teachers` | Find teachers for a skill |
| GET | `/api/skills/{skillId}/learners` | Find learners for a skill |
| GET | `/api/skills/recommendations` | Get recommended teachers |

### Users (Protected - Requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/me` | Get your profile |
| PUT | `/api/users/me` | Update your profile |
| GET | `/api/users/{userId}` | Get user profile |
| GET | `/api/users/search?name=john` | Search users |
| GET | `/api/users/matches` | Find mutual skill matches |

## Project Structure

```
skillsphere/
├── src/main/java/com/skillsphere/    # Spring Boot Backend
│   ├── config/                        # Security, JWT, Data seeding
│   ├── controller/                    # REST API endpoints
│   ├── dto/                           # Request/Response objects
│   ├── entity/                        # JPA entities
│   ├── exception/                     # Custom exceptions
│   ├── repository/                    # Data access layer
│   └── service/                       # Business logic
│
└── frontend/                          # React Frontend
    ├── src/
    │   ├── components/                # Reusable UI components
    │   ├── context/                   # Auth context
    │   ├── pages/                     # Page components
    │   └── services/                  # API client
    ├── index.html
    └── package.json
```

## Screenshots

| Home | Skills | Profile |
|------|--------|--------|
| Landing page with hero | Browse & search skills | Manage your skills |

## Coming Soon

- 📅 Session booking & scheduling
- ⭐ Reviews and ratings
- 💬 Real-time messaging
- 📧 Email notifications
- 🐳 Docker deployment

## Interview Talking Points

1. **Architecture:** Layered design (Controller → Service → Repository)
2. **Security:** Stateless JWT auth, BCrypt password hashing
3. **Matching Algorithm:** JPQL queries for skill-based user matching
4. **Frontend:** React with context API for state management
5. **Scaling:** Ready for PostgreSQL, Redis caching, Docker

---

Built with ❤️ for learning full-stack development