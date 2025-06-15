# Room Reservation System

A microservices-based system for managing room reservations.

## Components

- **User Service** (port 8081): Manages users
- **Room Service** (port 8082): Manages rooms
- **Reservation Service** (port 8083): Manages reservations
- **API Gateway** (port 8080): Routes requests between services and provides web interface
- **PostgreSQL**: Database
- **Kafka**: Messaging between services

## Quick Start

### Prerequisites
- Docker
- Docker Compose

### Running the Application

```bash
# Start all services
docker compose up -d

# Check services status
docker compose ps

# View logs
docker compose logs -f
```

Visit http://localhost:8080 to use the application. This will redirect to http://localhost:8080/reservations which is the main interface for the system.

## Swagger & API Documentation

Each service exposes its own Swagger (OpenAPI) documentation. You can access the interactive API docs at:

- **API Gateway:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **User Service:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **Room Service:** [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- **Reservation Service:** [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)

> **Note:**
> - The `/swagger-ui.html` endpoint provides the interactive Swagger UI for each service.
> - The `/api-docs` endpoint (e.g., `http://localhost:8080/api-docs`) returns the raw OpenAPI JSON specification.
> - There is no service running on port 3000 by default.

## Important Notes

### Database Initialization

The system creates three PostgreSQL databases automatically:
- `user_db`: For user data
- `room_db`: For room data
- `reservation_db`: For reservation data

If you encounter database connection errors, ensure:
1. The `init-multiple-dbs.sh` script is executable: `chmod +x init-multiple-dbs.sh`
2. The POSTGRES_DB is set to "postgres" in docker-compose.yml
3. POSTGRES_MULTIPLE_DATABASES environment variable contains all required databases

## API Endpoints

### Users API (port 8081)
- `GET /api/users` - List users
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Rooms API (port 8082)
- `GET /api/rooms` - List rooms
- `POST /api/rooms` - Create room
- `PUT /api/rooms/{id}` - Update room
- `DELETE /api/rooms/{id}` - Delete room

### Reservations API (port 8083)
- `GET /api/reservations` - List reservations
- `POST /api/reservations` - Create reservation
- `DELETE /api/reservations/{id}` - Delete reservation

## Technologies

- **Backend**: Java 17, Spring Boot 3.x, Spring Data JPA, Spring Cloud Gateway
- **Frontend**: HTML/CSS/JavaScript, Bootstrap 5
- **Infrastructure**: Docker, PostgreSQL, Kafka, Nginx

## Troubleshooting

If you encounter issues:

1. **Database Connection Errors**: Ensure all databases are created properly. Check logs with `docker compose logs postgres`.
2. **Service Dependencies**: Services depend on database and Kafka. Ensure they're running with `docker compose ps`.
3. **Container Restart**: If needed, restart specific services: `docker compose restart [service-name]`. 