# Local Development Stack

This directory provides tooling to stand up the EventStream Nexus dependencies for development either via Docker Compose or Kubernetes manifests.

## Docker Compose
Use `docker-compose.yaml` for a single-node setup of PostgreSQL, Liquibase migrations, Kafka, and the Spring Boot application.

### Prerequisites
- Docker Engine with Compose plugin
- `.env` file or exported shell variables supplying:
  - `EVENTSTREAM_LIQUIBASE_PASSWORD`
  - `EVENTSTREAM_APP_PASSWORD`

### Start the stack

```sh
cd config/local-dev
docker compose up --build
```

The services include:
- `postgres`: PostgreSQL 16 with init SQL to create the `eventstream` schema.
- `liquibase`: Runs migrations once, then exits.
- `kafka`: Single-node Kraft broker with the `client-created`, `client-updated`, and `client-deleted` topics.
- `app`: EventStream Nexus Spring Boot service.

### Common Commands
- Tail logs: `docker compose logs -f app`
- Re-run migrations: `docker compose run --rm liquibase update`
- Stop stack: `docker compose down`

### Calling the REST API
Once the `app` container is healthy, the REST endpoints are reachable on `http://localhost:8080`.

- Liveness probe: `curl http://localhost:8080/actuator/health/liveness`
- Sample clients call: `curl http://localhost:8080/clients`
- Create client (include idempotency header): `curl -X POST http://localhost:8080/clients -H 'Content-Type: application/json' -H 'X-Idempotency-Key: 123e4567-e89b-12d3-a456-426614174000' -d '{"name":"Acme","email":"acme@example.com"}'`

## Kubernetes (Optional)
See `k8s/README.md` for translating the same setup into Kubernetes manifests.
