# SkillSphere 🎯

**Peer-to-Peer Skill Exchange Platform**

SkillSphere connects people who want to learn with people who can teach. Post skills you can teach, browse skills you want to learn, and find your perfect skill exchange partner.

## Tech Stack

- **Backend:** Spring Boot 3.2, Java 17
- **Security:** Spring Security + JWT
- **Database:** H2 (dev) / PostgreSQL (prod)
- **Build:** Maven

## Quick Start

```bash
# Clone the repository
git clone https://gitlab.com/vedanshsa-group/skillsphere.git
cd skillsphere

# Run the application
./mvnw spring-boot:run

# App runs on http://localhost:8080
# H2 Console: http://localhost:8080/h2-console
```

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

## Example Usage

### 1. Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "bio": "Software developer eager to learn guitar"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 3. Add a skill you can teach
```bash
curl -X POST http://localhost:8080/api/skills/me \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "skillId": 1,
    "skillType": "TEACHING",
    "proficiencyNote": "5 years of Java experience"
  }'
```

### 4. Add a skill you want to learn
```bash
curl -X POST http://localhost:8080/api/skills/me \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "skillId": 13,
    "skillType": "LEARNING",
    "proficiencyNote": "Complete beginner"
  }'
```

### 5. Find your matches
```bash
curl http://localhost:8080/api/users/matches \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Project Structure

```
src/main/java/com/skillsphere/
├── config/          # Security, JWT, Data seeding
├── controller/      # REST API endpoints
├── dto/             # Request/Response objects
├── entity/          # JPA entities
├── exception/       # Custom exceptions & handlers
├── repository/      # Data access layer
└── service/         # Business logic
```

## Features

- ✅ User registration & JWT authentication
- ✅ Skill browsing and search
- ✅ Add skills you teach / want to learn
- ✅ Find teachers for any skill
- ✅ Smart matching (find mutual exchanges)
- ✅ User profiles with skill lists

## Coming Soon

- 📅 Session booking & scheduling
- ⭐ Reviews and ratings
- 💬 Real-time messaging
- 📧 Email notifications

---

Built with ❤️ for learning Spring Boot