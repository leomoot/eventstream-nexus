<p align="center"> <img src="https://dummyimage.com/600x150/000/fff&text=EventStream+Nexus" alt="EventStream Nexus Logo"/> </p> <p align="center"> <strong>API‑first • Idempotent • Event‑enabled • MapStruct‑powered • Java 25</strong> </p> <p align="center"> <!-- Core stack --> <img src="https://img.shields.io/badge/Java-25-007396?style=for-the-badge" /> <img src="https://img.shields.io/badge/Spring%20Boot-4.0.0-6DB33F?style=for-the-badge" /> <img src="https://img.shields.io/badge/API-OpenAPI%203.0-6BA539?style=for-the-badge" /> <img src="https://img.shields.io/badge/Mapping-MapStruct-FF6F00?style=for-the-badge" /> <img src="https://img.shields.io/badge/Boilerplate-Lombok-CA2C92?style=for-the-badge" /> <!-- Infrastructure --> <img src="https://img.shields.io/badge/Database-PostgreSQL%2015-336791?style=for-the-badge" /> <img src="https://img.shields.io/badge/DB-Liquibase-orange?style=for-the-badge" /> <img src="https://img.shields.io/badge/Events-Kafka-231F20?style=for-the-badge" /> <!-- CI / Quality --> <img src="https://img.shields.io/badge/CI-GitHub%20Actions-2088FF?style=for-the-badge" /> <img src="https://img.shields.io/badge/Security-CodeQL-FF0000?style=for-the-badge" /> <img src="https://img.shields.io/badge/Dependencies-OWASP%20Checked-8A2BE2?style=for-the-badge" /> <!-- Optional metrics --> <img src="https://img.shields.io/badge/Tests-Coverage%20Ready-4CAF50?style=for-the-badge" /> <img src="https://img.shields.io/badge/Docker-Image%20Ready-0db7ed?style=for-the-badge" /> <!-- License --> <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" /> </p> 

EventStream Nexus is an **API‑first**, **contract‑driven**, **idempotent**, and **event‑enabled** backend service built with: 
- **Spring Boot 4**
- **Java 25**
- **OpenAPI‑first development** (single source of truth)
- **Java records** for all API/service models
- **MapStruct** for mapping between records and JPA entities
- **PostgreSQL + Liquibase** for schema evolution
- **Kafka (optional)** for outbound domain events
- **Maven** for reproducible builds
- **GitHub Actions** for CI, CodeQL, OWASP, and Liquibase validation

The OpenAPI specification defines the entire API surface. All API interfaces and record models are generated automatically.

<details>
<summary>Architecture Overview</summary>

```
openapi.yaml → generated API interfaces + record models
                         ↓
Controllers implement generated interfaces
                         ↓
Services use record models
                         ↓
MapStruct maps records ↔ JPA entities
                         ↓
Repositories persist entities
                         ↓
PostgreSQL (Liquibase migrations)
                         ↓
Kafka (optional outbound events)
```
Design goals:
- explicit contracts
- immutable data at the edges (records)
- minimal boilerplate
- deterministic, idempotent behavior
</details>

<details>
<summary>Project Structure</summary>

```
eventstream-nexus/
│
├── pom.xml
├── README.md
├── .gitignore
├── .github/
│   └── workflows/
│       ├── ci.yml
│       ├── codeql.yml
│       └── liquibase.yml
│
├── src/
│   ├── main/
│   │   ├── openapi/
│   │   │   └── eventstreamnexus.yaml
│   │   │
│   │   └── java/
│   │       └── nl/
│   │           └── leomoot/
│   │               └── eventstreamnexus/
│   │                   ├── api/
│   │                   │   ├── controllers/
│   │                   │   │   └── ClientController.java
│   │                   │   └── mappers/
│   │                   │       ├── BaseMapper.java
│   │                   │       └── ClientMapper.java
│   │                   │
│   │                   ├── service/
│   │                   │   ├── ClientService.java
│   │                   │   ├── IdempotencyService.java
│   │                   │   └── IdempotencyCleanupService.java
│   │                   │
│   │                   ├── domain/
│   │                   │   ├── model/
│   │                   │   │   ├── ClientEntity.java
│   │                   │   │   └── IdempotencyRecord.java
│   │                   │   └── rules/   (optional future domain rules)
│   │                   │
│   │                   ├── repository/
│   │                   │   ├── ClientRepository.java
│   │                   │   └── IdempotencyRecordRepository.java
│   │                   │
│   │                   └── infrastructure/
│   │                       ├── kafka/
│   │                       │   └── ClientEventProducer.java
│   │                       └── config/
│   │                           ├── AppConfig.java
│   │                           ├── KafkaConfig.java
│   │                           └── OpenApiConfig.java
│   │
│   └── resources/
│       ├── application.properties
│       └── db/
│           └── changelog/
│               ├── changelog-master.xml
│               └── changes/
│                   ├── 0001-create-client-table.xml
│                   └── 0002-idempotency-table.xml
│
└── target/
    ├── classes/
    │   └── nl/
    │       └── leomoot/
    │           └── eventstreamnexus/
    │               ├── generated/
    │               │   ├── api/      # generated API interfaces     
    │               │   └── model/    # generated record models
    │               └── (compiled handwritten code)
    │
    └── generated-sources/
        └── openapi/
            └── (raw generated sources before compilation)
```
Generated code lives in
<code>/target/classes/.../generated/...</code>
and is not committed.
</details>

<details open>
<summary>API‑first workflow</summary>
1. Edit <code>src/main/openapi/openapi.yaml</code>.
  Define:
  - endpoints
  - request/response schemas
  - required headers (including Idempotency-Key)
  - validation rules

2. Generate code
<code>
bash
mvn clean generate-sources
This generates:
</code>
API interfaces (generated.api)
    
record models (generated.model)

3. Implement generated APIs

Controllers implement the generated interfaces and delegate to services.
  
</details>

<details>
<summary>Idempotency</summary>

```  
All POST endpoints require a HTTP header named Idempotency-Key with a. UUID value.

If this header is mmissing → **400 Bad Request**.

Client must must retry with the same UUID value.

Idempotency records are stored in a database table named 'idempotency_record'.

This ensures:
- safe retries
- no duplicate DB rows  
- no duplicate Kafka events  
- deterministic behavior  

Cleanup is handled via a scheduled job or database TTL.
```
</details>

<details>
<summary>Mapping (MapStruct + Java Records)</summary>

```
EventStream Nexus uses Java records for all API and service‑level models:
- request bodies
- response bodies
- event payloads
- idempotency stored responses

Records provide:
- immutability
- built‑in constructors
- built‑in equals/hashCode
- no Lombok dependency
- perfect compatibility with OpenAPI

JPA entities remain regular classes due to JPA constraints.

MapStruct maps: Record ↔ Entity

All mappers extend a shared BaseMapper interface.
```
</details>

All mappers extend a shared `BaseMapper` interface for consistency.
