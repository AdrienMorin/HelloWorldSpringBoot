# Docker Development Environment

Complete guide for running the Library API with Docker and PostgreSQL.

## Prerequisites

- Docker 20.10+
- Docker Compose 2.0+
- Make (optional, but recommended)

## Quick Start

### 1. Clone and Setup

```bash
# Clone the repository
git clone <repository-url>
cd library-api

# Copy environment variables
cp .env.example .env

# Edit .env file with your configurations
nano .env
