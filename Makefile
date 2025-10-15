# Makefile

.PHONY: help build dev prod stop clean logs test

# Variables
PROJECT_NAME=library-api
COMPOSE_DEV=docker-compose -f docker-compose.yml
COMPOSE_PROD=docker-compose -f docker-compose.prod.yml

help: ## Show this help message
    @echo 'Usage: make [target]'
    @echo ''
    @echo 'Available targets:'
    @awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

build: ## Build the Docker images
    $(COMPOSE_DEV) build

dev: ## Start the development environment
    $(COMPOSE_DEV) up -d
    @echo "Development environment is starting..."
    @echo "API will be available at: http://localhost:8080"
    @echo "Swagger UI: http://localhost:8080/swagger-ui.html"
    @echo "pgAdmin: http://localhost:5050"
    @echo "Debug port: 5005"

dev-logs: ## Follow logs in development mode
    $(COMPOSE_DEV) logs -f app

prod: ## Start the production environment
    $(COMPOSE_PROD) up -d
    @echo "Production environment is starting..."
    @echo "API will be available at: http://localhost:8080"

prod-build: ## Build and start production environment
    $(COMPOSE_PROD) up -d --build

stop: ## Stop all containers
    $(COMPOSE_DEV) down
    $(COMPOSE_PROD) down

stop-dev: ## Stop development containers
    $(COMPOSE_DEV) down

stop-prod: ## Stop production containers
    $(COMPOSE_PROD) down

restart: ## Restart development containers
    $(COMPOSE_DEV) restart

restart-app: ## Restart only the app container (development)
    $(COMPOSE_DEV) restart app

clean: ## Stop and remove all containers, networks, and volumes
    $(COMPOSE_DEV) down -v
    $(COMPOSE_PROD) down -v
    docker system prune -f

logs: ## Show logs for all services (development)
    $(COMPOSE_DEV) logs -f

logs-app: ## Show logs for the app service only
    $(COMPOSE_DEV) logs -f app

logs-db: ## Show logs for the database
    $(COMPOSE_DEV) logs -f postgres

shell-app: ## Open a shell in the app container
    $(COMPOSE_DEV) exec app sh

shell-db: ## Open a PostgreSQL shell
    $(COMPOSE_DEV) exec postgres psql -U library_user -d librarydb

test: ## Run tests locally
    mvn clean test

test-docker: ## Run tests in Docker
    $(COMPOSE_DEV) exec app mvn test

db-backup: ## Backup the database
    mkdir -p backups
    $(COMPOSE_DEV) exec postgres pg_dump -U library_user librarydb > backups/backup_$$(date +%Y%m%d_%H%M%S).sql
    @echo "Database backup created in backups/"

db-restore: ## Restore database from backup (use BACKUP=filename)
    @if [ -z "$(BACKUP)" ]; then \
        echo "Please specify a backup file: make db-restore BACKUP=backup_file.sql"; \
        exit 1; \
    fi
    $(COMPOSE_DEV) exec -T postgres psql -U library_user librarydb < backups/$(BACKUP)

ps: ## List running containers
    $(COMPOSE_DEV) ps

stats: ## Show container resource usage
    docker stats $$(docker ps --filter name=$(PROJECT_NAME) -q)

health: ## Check health of all services
    @echo "Checking service health..."
    @curl -s http://localhost:8080/actuator/health | json_pp || echo "App not ready"
    @$(COMPOSE_DEV) exec postgres pg_isready -U library_user || echo "Database not ready"

rebuild: ## Rebuild and restart development environment
    $(COMPOSE_DEV) down
    $(COMPOSE_DEV) up -d --build
    @echo "Environment rebuilt and restarted"
