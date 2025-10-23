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
    "title": "TEST",
    "author": "Gang of Four",
    "isbn": "978-0-20163-361-1",
    "price": 12,
    "publicationDate": "2002-05-12",
    "genre": "Programming"
  }'
```

**Response:** `201 Created`
```json
{
  "id": 4,
  "title": "TEST",
  "author": "Gang of Four",
  "isbn": "978-0-20163-361-4",
  "publicationDate": "2002-05-12",
  "price": 12,
  "description": null,
  "pages": null,
  "publisher": null,
  "createdAt": "2025-10-23T19:59:52",
  "updatedAt": "2025-10-23T19:59:52"
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
    "title": "Design Patterns",
    "author": "Gang of Four",
    "isbn": "978-0-20163-361-0",
    "publicationDate": "2002-05-12",
    "price": 12.00,
    "description": null,
    "pages": null,
    "publisher": null,
    "createdAt": "2025-10-15T20:59:38",
    "updatedAt": "2025-10-15T20:59:38"
  },
  {
    "id": 3,
    "title": "TEST",
    "author": "Gang of Four",
    "isbn": "978-0-20163-361-1",
    "publicationDate": "2002-05-12",
    "price": 12.00,
    "description": null,
    "pages": null,
    "publisher": null,
    "createdAt": "2025-10-23T19:57:11",
    "updatedAt": "2025-10-23T19:57:11"
  },
  {
    "id": 4,
    "title": "TEST",
    "author": "Gang of Four",
    "isbn": "978-0-20163-361-4",
    "publicationDate": "2002-05-12",
    "price": 12.00,
    "description": null,
    "pages": null,
    "publisher": null,
    "createdAt": "2025-10-23T19:59:52",
    "updatedAt": "2025-10-23T19:59:52"
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
  "title": "Design Patterns",
  "author": "Gang of Four",
  "isbn": "978-0-20163-361-0",
  "publicationDate": "2002-05-12",
  "price": 12.00,
  "description": null,
  "pages": null,
  "publisher": null,
  "createdAt": "2025-10-15T20:59:38",
  "updatedAt": "2025-10-15T20:59:38"
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
    "title": "TEST",
    "author": "Gang of Four",
    "isbn": "978-0-20163-361-1",
    "price": 12,
    "publicationDate": "2002-05-12",
    "genre": "Programming"
  }'
```

**Response:** `200 OK`
```json
{
  "title": "TEST",
  "author": "Gang of Four",
  "isbn": "978-0-20163-361-1",
  "price": 12,
  "publicationDate": "2002-05-12",
  "genre": "Programming"
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