
# SQL Murder Mystery — Gamified SQL Learning Platform

Microservices-based implementation of the project proposal (Spring Boot + Spring Cloud +
React/Tailwind). This repo is a **working scaffold**:

- **Fully implemented — all 6 microservices, end to end:**
  - `auth-service` — register/login/JWT/roles
  - `case-content-service` — **Learning Phase** (timed quizzes, now with seed data) and
    **Case-Solving Phase** case metadata + accusation grading
  - `query-execution-service` — safe, read-only SQL sandbox for the case databases
  - `progress-tracking-service` — points, streaks, activity log; calls leaderboard +
    notification services after each completion
  - `leaderboard-service` — real-time rankings via a Redis sorted set
  - `notification-service` — in-memory achievement/streak/completion alerts
  - Full React frontend (Login, Register, Dashboard, Learning Phase + quiz attempts,
    Case-Solving with a live query editor, Leaderboard)
- **Infra (functional):** `service-registry` (Eureka), `api-gateway` (Spring Cloud
  Gateway, all routes + CORS configured), `docker-compose.yml` (MySQL, Redis, all 6
  services, gateway, registry, frontend), `mysql-init/` (seeds two case databases + the
  read-only DB account query-execution-service uses).

### Running it

```
docker compose up --build
```

Then open http://localhost:5173. First run only: `mysql-init/` scripts execute
automatically on a fresh MySQL volume; if you've run this before with an older volume,
`docker compose down -v` first or the two demo case schemas won't exist.

### How a learner's progress actually gets recorded

There's no message broker in this stack (docker-compose only provisions MySQL + Redis),
so the "event-driven coordination" the proposal calls for is implemented as small,
best-effort REST calls rather than a queue: the **frontend** calls
`POST /api/progress/events` right after a quiz is passed or a case accusation comes back
correct; **progress-tracking-service** is the single place that write lands, and it then
calls leaderboard-service and notification-service with the learner's own forwarded JWT.
Either of those two calls failing never blocks or loses the learner's actual progress —
it's logged and the request still succeeds.

### How the Case-Solving Phase fits together

`case-content-service` owns case *metadata* only (title, briefing, difficulty, which
suspect is correct). The actual explorable mystery data — `person`, `crime_scene_report`,
`witness_statement`, `security_log` — lives in its own dedicated MySQL schema per case
(`case_gallery_theft`, `case_archive_heist`), seeded by `mysql-init/`. `query-execution-service`
connects to those schemas with a SELECT-only `smm_readonly` MySQL account and never touches
the app's own tables. Flow: `GET /api/cases/{id}` → briefing + `targetSchema` → frontend
sends the learner's SQL to `POST /api/query/execute` with that `targetSchema` → learner
forms a theory → `POST /api/cases/{id}/accusations` grades it against the (never-exposed)
solution suspect id.

## Repo layout

```
SQL-Murder-Mystery/
├── service-registry/         # Eureka service discovery
├── api-gateway/               # Spring Cloud Gateway (routes below)
├── auth-service/               # FULL: register, login, JWT, roles
├── case-content-service/       # FULL: Learning Phase quizzes | SKELETON: case/clue storage
├── query-execution-service/    # SKELETON: safe SQL execution
├── progress-tracking-service/  # SKELETON: scores, timers, completion
├── leaderboard-service/        # SKELETON: rankings
├── notification-service/       # SKELETON: achievement/streak alerts
├── frontend/                   # React + Tailwind (Vite)
└── docker-compose.yml
```

## Ports & Gateway routes

| Service | Port | Gateway route |
|---|---|---|
| service-registry (Eureka) | 8761 | — |
| api-gateway | 8080 | entry point for everything below |
| auth-service | 8081 | `/api/auth/**` |
| case-content-service | 8082 | `/api/quizzes/**`, `/api/cases/**` |
| query-execution-service | 8083 | `/api/query/**` |
| progress-tracking-service | 8084 | `/api/progress/**` |
| leaderboard-service | 8085 | `/api/leaderboard/**` |
| notification-service | 8086 | `/api/notifications/**` |
| frontend (Vite dev) | 5173 | — |

## Running it

### 1. Backend (each service is a standard Maven Spring Boot app)

```bash
# start infra first
docker compose up mysql redis -d

# in separate terminals, or use `docker compose up --build` for everything
cd service-registry && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
cd auth-service && mvn spring-boot:run
cd case-content-service && mvn spring-boot:run
# skeleton services build & boot too, they just return TODO/placeholder responses
cd query-execution-service && mvn spring-boot:run
cd progress-tracking-service && mvn spring-boot:run
cd leaderboard-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

Or simply:

```bash
docker compose up --build
```

### 2. Frontend

```bash
cd frontend
npm install
npm run dev      # http://localhost:5173
```

The frontend talks to `http://localhost:8080/api/...` (the gateway) by default — see
`frontend/src/api/client.js`.

## What's implemented right now

- **Register/Login** — `POST /api/auth/register`, `POST /api/auth/login` issue a JWT;
  `GET /api/auth/me` reads the current user off the token. Passwords are BCrypt-hashed.
  Roles: `LEARNER`, `ADMIN`.
- **Learning Phase** — quizzes are seeded per SQL topic (SELECT, WHERE, JOIN, GROUP BY,
  subqueries...). `GET /api/quizzes` lists topics, `GET /api/quizzes/{id}` fetches
  questions (answers stripped), `POST /api/quizzes/{id}/attempts` submits answers within
  a time limit and returns a score. Attempts are stored per user.
- Everything else (Case-Solving query editor, secure query sandbox, progress tracking,
  live leaderboard, notifications) is scaffolded with real Spring Boot apps, entities,
  repositories and controller signatures, but the business logic is `// TODO` — filling
  these in is the next milestone per the project plan's Month 2 timeline.

## Tech stack

Spring Boot 3 / Spring Cloud 2023.x / Spring Security + JJWT / MySQL 8 / Redis /
Spring Cloud Gateway / Eureka / React 18 + Vite + Tailwind CSS / Docker & docker-compose.
=======
# SpringBoot_SQLMurderMystery
>>>>>>> d45d07c1080ac3315b74869d269b909278c12837
