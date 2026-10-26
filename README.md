# Microservices

Docker Compose commands for running and managing the services in this repository. Run commands from the directory containing `compose.yaml` (`D:\MICROSERVICES` on this machine).

## Services

| Compose service | Purpose | Port |
| --- | --- | --- |
| `eureka` | Service registry | 8761 |
| `admin-server` | Spring Boot Admin | 1111 |
| `gateway-service` | API gateway | 8888 |
| `welcome-service` | Welcome API | 8081 |
| `greet-api` | Greeting API | 8082 |
| `auth-service` | Authentication API | 8083 |
| `kafka` | Message broker | 9092 |
| `postgres` | Database for `auth-service` | 5432 |

## Start services

Build the application images and start the full stack in the background:

```powershell
docker compose up --build -d
```

Start a single service and any services it depends on:

```powershell
docker compose up -d welcome-service
```

Build images without starting containers:

```powershell
docker compose build
```

Build or rebuild one application's image:

```powershell
docker compose build auth-service
```

## Check status and logs

List running services:

```powershell
docker compose ps
```

Follow logs for all services, or for one service:

```powershell
docker compose logs -f
docker compose logs -f auth-service
```

Show the last 100 lines for one service:

```powershell
docker compose logs --tail=100 welcome-service
```

## Stop and restart services

Stop one application while leaving the other containers running:

```powershell
docker compose stop auth-service
```

Replace `auth-service` with any service name in the table above. Stopping a service does not stop its dependencies.

Start a stopped service again:

```powershell
docker compose start auth-service
```

Restart a service:

```powershell
docker compose restart auth-service
```

Stop the full stack, keeping containers and the database volume:

```powershell
docker compose stop
```

Bring the stopped stack back up:

```powershell
docker compose start
```

## Remove containers

Stop and remove the stack's containers and Compose network. The PostgreSQL data volume is retained:

```powershell
docker compose down
```

Remove containers, network, and the PostgreSQL data volume (this deletes database data):

```powershell
docker compose down --volumes
```

Remove one stopped service's container without removing its image:

```powershell
docker compose rm -s -f auth-service
```

## Configuration

Compose uses these defaults for PostgreSQL:

- Database: `postgres`
- Username: `postgres`
- Password: `postgres`

Override them in the current PowerShell session before starting Compose:

```powershell
$env:POSTGRES_DB = "mydb"
$env:POSTGRES_USER = "myuser"
$env:POSTGRES_PASSWORD = "change-this-password"
$env:JWT_SECRET = "your-base64-encoded-secret"
docker compose up --build -d
```

The PostgreSQL container stores its data in the `postgres_data` volume, so data remains when containers are stopped or removed with `docker compose down`. Use `docker compose down --volumes` only when you also want to delete that data.
