# CHALLENGE TEST

## How to execute the project

### Requirements
- Docker y Docker Compose installed
- Ports 9080 y 5532 free

### Executions
```bash
# Builds and start both db and users-service (-d is optional)
docker-compose up --build -d
```

```bash
# Starts (without building) the users-service (-d is optional)
docker-compose up -d users-service
```

### Important Urls
- API: http://localhost:9080/api/users
- Swagger: http://localhost:9080/swagger-ui.html
- Postgresql Database: localhost:5532 (user: user, password: password)

### Architecture
- Ports and Adapters architecture with Spring Boot
- PostgreSQL as Database
- Swagger for documentation
- Unit tests by JUnit and Mockito

### Comandos útiles
```bash
# See logs (-f is optional)
docker-compose logs users-service -f
```

```bash
# Stop everything
docker-compose down
```

```bash
# Execute tests
cd msvc-users
./mvnw test
```

```bash
# Execute tests + coverage
cd msvc-users
./mvnw test jacoco:report
# then open in a web browser the file: target/site/jacoco/index.html
```

### Local development

1. **Start Postgresql database:**
```bash
docker-compose up -d db
```

2. **Start application:**
```bash
cd msvc-users
./mvnw spring-boot:run
```

To change anything of the local configuration go to application.properties, application-local.properties