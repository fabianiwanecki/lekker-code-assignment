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

### Unit Tests Coverage
#### Backend
Backend test coverage is about 70 %. All functions that have at least some business logic are tested. One-liners without any business logic are not tested (e.g. Controllers and One-Line Service methods that just call a repository.
#### Frontend
The only frontend components tested here are all services, interceptors, and guards as well as one component (BrowseTeamsComponent). This could be extended easily to the remaining components as well.

### Authentication and Authorization
#### Backend
The backend utilizes Spring Security for authentication and authorization. This is configured in `SecurityConfig.class` by adding a custom `JwtAuthenticationFilter` to the filter chain. Any request except `sign-up`, `sign-in`, and swagger-endpoints have to be called by an authenticated user. The `JwtAuthenticationFilter` reads the `Authentication`-header from the http-request to validate that. It reads the `subject` from the token, which is also the username of the user, and loads the respective user from the user table into the Spring Security Authentication Context. This context also contains information about the role in the user's team through an authority that has the format `${teamRole}_${teamUuid}`

#### Frontend
The frontend saves the JWT from the sign-in endpoint into local storage. The `authenticationInterceptor` hooks into every `HttpClient`-request to add the token to the `Authentication`-header of the request. The frontend determines if the user is signed in based on this token from local storage. As soon as the user is not logged in anymore, the `authGuard` forwards the user to the sign-in page.

### User Rank
Calculating the rank of a user utilizing its score value is not a trivial task using only the Postgres database. While fetching a list of users this can be achieved using a simple sort on the score value. We encounter a problem when fetching a singular user though. If we want to determine a user's score, we have to compare it to every other user's score in the database. This results in a very inefficient query that has to scan the whole table every time. That's why I decided to introduce `redis` into this implementation. Redis' Sorted Set is the ideal structure for this kind of task. The `UserRankRepository` provides the functions to access the Sorted Set. One issue here is that the sorted set can only return a single value per request. That's why I added the `script.lua` file in the resources folder of the backend. The script takes multiple user UUIDs and returns a list of ranks. Redis improves the performance of the whole application significantly.

### Database
Database tables are generated through `liquibase` in the backend.
#### Test Data
Liquibase also populates the database with some test data including users and teams through csv files.
