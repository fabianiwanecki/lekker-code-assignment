# Lekker Code Assignment - Spring Boot + Angular

This project is a coding challenge from Lekker Code Company. It includes a Spring Boot Backend and a Angular Frontend.

## Prerequisites

- Docker
- Docker Compose

## Project Structure
```
.
├── backend/
│   └── Dockerfile
│   └── src
├── frontend/
│   └── Dockerfile
│   └── src
├── docker-compose.yml
└── README.md
```

## Quick Start

1. Clone the repository:
```
    git clone https://github.com/fabianiwanecki/lekker-code-assignment.git
    cd [project-directory]
```
2. Build and run the project:
`docker-compose up --build`
3. Access the application:
- Frontend: http://localhost:4200
- Backend: http://localhost:8080
  - Swagger: http://localhost:8080/swagger-ui/index.html

## Services

- Backend (Spring Boot): Runs on port 8080
- Frontend (Angular): Runs on port 4200
- Database (Postgres): Runs on port 5432
- (Redis): Runs on port 6379

## Testing

### Angular
```
cd frontend
ng test
```

### Java
```
cd backend
mvn test
```

## Documentation
