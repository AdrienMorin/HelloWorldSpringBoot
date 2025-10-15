# 📚 Library Management REST API - Complete Documentation

## 📋 Table of Contents
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Request/Response Examples](#requestresponse-examples)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

RESTful API for managing a book library with full CRUD operations, built following industry best practices and clean architecture principles.

### Key Features
- ✅ Full CRUD operations for books
- ✅ Advanced search capabilities (by title and author)
- ✅ Input validation with detailed error messages
- ✅ Dockerized development environment with hot-reload
- ✅ PostgreSQL database with health checks
- ✅ OpenAPI/Swagger documentation
- ✅ Comprehensive exception handling
- ✅ Production-ready configuration

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Programming language |
| Spring Boot | 3.5.6 | Application framework |
| PostgreSQL | 16-alpine | Database |
| Maven | 3.9.9 | Build tool |
| Docker | Latest | Containerization |
| Lombok | 1.18.36 | Boilerplate reduction |
| MapStruct | 1.6.3 | DTO mapping |
| SpringDoc OpenAPI | 2.7.0 | API documentation |

---

## 🚀 Getting Started

### Prerequisites
- Docker & Docker Compose installed
- Java 21 (for local development without Docker)
- Maven 3.9+ (for local development without Docker)

### Quick Start with Docker

```bash
# Clone the repository
git clone <your-repo-url>
cd library-api

# Start the application (development mode with hot-reload)
docker compose up

# Or run in background
docker compose up -d

# View logs
docker compose logs -f library-api-dev

# Stop everything
docker compose down

# Stop and remove volumes (clean database)
docker compose down -v
```

### Application URLs

| Service | URL | Description |
|---------|-----|-------------|
| API Base | http://localhost:8080 | REST API endpoints |
| Swagger UI | http://localhost:8080/swagger-ui.html | Interactive API documentation |
| OpenAPI JSON | http://localhost:8080/v3/api-docs | OpenAPI specification |
| Health Check | http://localhost:8080/actuator/health | Application health status |
| Database | localhost:5432 | PostgreSQL (user: library_user, pass: library_pass) |

---

## 📡 API Endpoints

### Base URL: `/api/v1/books`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/books` | Create a new book |
| GET | `/api/v1/books` | Get all books |
| GET | `/api/v1/books/{id}` | Get book by ID |
| PUT | `/api/v1/books/{id}` | Update existing book |
| DELETE | `/api/v1/books/{id}` | Delete book |
| GET | `/api/v1/books/search/title?title={title}` | Search books by title |
| GET | `/api/v1/books/search/author?author={author}` | Search books by author |

---

## 🔥 Request/Response Examples

### 1. Create a Book

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "publishedYear": 2008,
    "genre": "Programming",
    "description": "A handbook of agile software craftsmanship"
  }'
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "publishedYear": 2008,
  "genre": "Programming",
  "description": "A handbook of agile software craftsmanship",
  "createdAt": "2025-01-15T20:43:19.123Z",
  "updatedAt": "2025-01-15T20:43:19.123Z"
}
```

---

### 2. Get All Books

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/books
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "publishedYear": 2008,
    "genre": "Programming",
    "description": "A handbook of agile software craftsmanship",
    "createdAt": "2025-01-15T20:43:19.123Z",
    "updatedAt": "2025-01-15T20:43:19.123Z"
  },
  {
    "id": 2,
    "title": "Design Patterns",
    "author": "Gang of Four",
    "isbn": "978-0201633610",
    "publishedYear": 1994,
    "genre": "Programming",
    "description": "Elements of Reusable Object-Oriented Software",
    "createdAt": "2025-01-15T20:45:30.456Z",
    "updatedAt": "2025-01-15T20:45:30.456Z"
  }
]
```

---

### 3. Get Book by ID

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/books/1
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "publishedYear": 2008,
  "genre": "Programming",
  "description": "A handbook of agile software craftsmanship",
  "createdAt": "2025-01-15T20:43:19.123Z",
  "updatedAt": "2025-01-15T20:43:19.123Z"
}
```

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2025-01-15T20:50:00.000Z",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 999",
  "path": "/api/v1/books/999"
}
```

---

### 4. Update a Book

**Request:**
```bash
curl -X PUT http://localhost:8080/api/v1/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "publishedYear": 2008,
    "genre": "Software Engineering",
    "description": "Updated description with more details"
  }'
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "publishedYear": 2008,
  "genre": "Software Engineering",
  "description": "Updated description with more details",
  "createdAt": "2025-01-15T20:43:19.123Z",
  "updatedAt": "2025-01-15T20:55:00.789Z"
}
```

---

### 5. Delete a Book

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/v1/books/1
```

**Response:** `204 No Content`

**Error Response (if already deleted):** `404 Not Found`
```json
{
  "timestamp": "2025-01-15T21:00:00.000Z",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 1",
  "path": "/api/v1/books/1"
}
```

---

### 6. Search Books by Title

**Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/books/search/title?title=clean"
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "publishedYear": 2008,
    "genre": "Programming",
    "description": "A handbook of agile software craftsmanship",
    "createdAt": "2025-01-15T20:43:19.123Z",
    "updatedAt": "2025-01-15T20:43:19.123Z"
  },
  {
    "id": 5,
    "title": "Clean Architecture",
    "author": "Robert C. Martin",
    "isbn": "978-0134494166",
    "publishedYear": 2017,
    "genre": "Software Engineering",
    "description": "A Craftsman's Guide to Software Structure",
    "createdAt": "2025-01-15T21:05:00.123Z",
    "updatedAt": "2025-01-15T21:05:00.123Z"
  }
]
```

---

### 7. Search Books by Author

**Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/books/search/author?author=martin"
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "publishedYear": 2008,
    "genre": "Programming",
    "description": "A handbook of agile software craftsmanship",
    "createdAt": "2025-01-15T20:43:19.123Z",
    "updatedAt": "2025-01-15T20:43:19.123Z"
  },
  {
    "id": 5,
    "title": "Clean Architecture",
    "author": "Robert C. Martin",
    "isbn": "978-0134494166",
    "publishedYear": 2017,
    "genre": "Software Engineering",
    "description": "A Craftsman's Guide to Software Structure",
    "createdAt": "2025-01-15T21:05:00.123Z",
    "updatedAt": "2025-01-15T21:05:00.123Z"
  }
]
```

---

## ❌ Validation Error Examples

### Invalid Book Creation (Missing Required Fields)

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "author": "J",
    "isbn": "invalid",
    "publishedYear": 2050
  }'
```

**Response:** `400 Bad Request`
```json
{
  "timestamp": "2025-01-15T21:10:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "title": "Title is required and cannot be blank",
    "author": "Author name must be between 2 and 100 characters",
    "isbn": "ISBN must follow the format XXX-XXXXXXXXXX",
    "publishedYear": "Published year cannot be in the future"
  },
  "path": "/api/v1/books"
}
```

---

## 🧪 Testing with Postman/Insomnia

### Complete Test Scenario

```bash
# 1. Create several books
curl -X POST http://localhost:8080/api/v1/books -H "Content-Type: application/json" \
-d '{"title":"The Pragmatic Programmer","author":"Andrew Hunt","isbn":"978-0135957059","publishedYear":2019,"genre":"Programming","description":"Your journey to mastery"}'

curl -X POST http://localhost:8080/api/v1/books -H "Content-Type: application/json" \
-d '{"title":"Domain-Driven Design","author":"Eric Evans","isbn":"978-0321125217","publishedYear":2003,"genre":"Software Architecture","description":"Tackling Complexity in the Heart of Software"}'

curl -X POST http://localhost:8080/api/v1/books -H "Content-Type: application/json" \
-d '{"title":"Refactoring","author":"Martin Fowler","isbn":"978-0134757599","publishedYear":2018,"genre":"Programming","description":"Improving the Design of Existing Code"}'

# 2. Get all books
curl -X GET http://localhost:8080/api/v1/books

# 3. Search by keyword
curl -X GET "http://localhost:8080/api/v1/books/search/title?title=design"

# 4. Update a book (replace ID 1 with actual ID from response)
curl -X PUT http://localhost:8080/api/v1/books/1 -H "Content-Type: application/json" \
-d '{"title":"The Pragmatic Programmer (2nd Edition)","author":"Andrew Hunt","isbn":"978-0135957059","publishedYear":2019,"genre":"Programming","description":"Updated edition"}'

# 5. Delete a book
curl -X DELETE http://localhost:8080/api/v1/books/1

# 6. Verify deletion
curl -X GET http://localhost:8080/api/v1/books/1
```

---

## 🏗️ Project Structure

```
library-api/
├── src/
│   ├── main/
│   │   ├── java/com/library/api/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── exception/        # Custom exceptions & handlers
│   │   │   ├── mapper/           # MapStruct mappers
│   │   │   ├── repository/       # Spring Data repositories
│   │   │   ├── service/          # Business logic
│   │   │   └── LibraryApiApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-dev.yml
│   └── test/                     # Unit & integration tests
├── Dockerfile                    # Production Docker image
├── Dockerfile.dev                # Development Docker image
├── docker-compose.yml            # Docker Compose configuration
├── pom.xml                       # Maven configuration
└── README.md                     # This file
```

---

## 🐛 Troubleshooting

### Container Issues

```bash
# View logs
docker compose logs -f library-api-dev

# Restart services
docker compose restart

# Full rebuild
docker compose down -v
docker compose build --no-cache
docker compose up
```

### Database Connection Issues

```bash
# Check database is healthy
docker compose ps

# Connect to database directly
docker compose exec library-db-dev psql -U library_user -d library_db

# View database tables
\dt

# Exit psql
\q
```

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process (replace PID)
kill -9 <PID>

# Or change port in docker-compose.yml
ports:
  - "8081:8080"  # Change 8081 to any available port
```

---

## 📊 Health Check

```bash
# Application health
curl http://localhost:8080/actuator/health

# Expected response
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

---

## 🔒 Production Deployment

### Build Production Image

```bash
# Build production image
docker build -t library-api:1.0.0 .

# Run production container
docker run -d \
  --name library-api-prod \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/library_db \
  -e SPRING_DATASOURCE_USERNAME=prod_user \
  -e SPRING_DATASOURCE_PASSWORD=prod_password \
  library-api:1.0.0
```

---

## 📝 License

MIT License - Feel free to use this project for learning and commercial purposes.

---

## 👨‍💻 Author

Built with ❤️ following Spring Boot and Clean Architecture best practices.