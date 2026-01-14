# EventStream Nexus

![Java](https://img.shields.io/badge/Java-25-007396?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-6DB33F?style=for-the-badge)
![Kafka](https://img.shields.io/badge/Kafka-Event%20Driven-231F20?style=for-the-badge)
![Avro](https://img.shields.io/badge/Avro-Schema%20Registry-FF6600?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge)
![Architecture](https://img.shields.io/badge/Architecture-CQRS%20%2B%20Events-8A2BE2?style=for-the-badge)
![Build](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)
![Security](https://img.shields.io/badge/Security-CodeQL%20Scan-ff0000?style=for-the-badge)

## Overview

EventStream Nexus is a modern, event‑driven CQRS platform built with:

- Spring Boot 4
- Java 25 (Virtual Threads)
- Kafka + Avro + Schema Registry
- PostgreSQL
- MapStruct
- Lombok

It cleanly separates write‑side domain logic from read‑optimized projections, synchronizing them through Kafka domain events encoded with Avro.

## Architecture

```text
                               +---------------------------+
                               |     Client / External     |
                               |         Systems           |
                               |   (REST Commands/Queries) |
                               +-------------+-------------+
                                             |
                                             v
                         +-------------------+-------------------+
                         |               WRITE API               |
                         |  POST /clients, /orders, /status      |
                         +-------------------+-------------------+
                                             |
                                             v
                 +---------------------------+---------------------------+
                 |                       WRITE MODEL                     |
                 |  +----------------+   +----------------+   +--------+ |
                 |  | Domain Entities|   | Write Services |   |  JPA   | |
                 |  | (Client,Order) |   | (Transactional)|   |Repos   | |
                 |  +----------------+   +----------------+   +--------+ |
                 +---------------------------+---------------------------+
                                             |
                                             | Publish Avro Events
                                             v
                   +---------------------------------------------------+
                   |                     KAFKA BUS                     |
                   |   (client.created, order.created, status.updated) |
                   +---------------------------+-----------------------+
                                             |
                                             v
                         +-------------------+-------------------+
                         |               SCHEMA REGISTRY         |
                         |        Avro Schemas & Compatibility   |
                         +-------------------+-------------------+
                                             |
                                             v
                 +---------------------------+---------------------------+
                 |                       READ MODEL                      |
                 |  +----------------+   +----------------+   +--------+ |
                 |  | Kafka Consumers|-->| Projections    |-->| Read   | |
                 |  | (Virtual Thr.) |   | (Optimized DB) |   |Services| |
                 |  +----------------+   +----------------+   +--------+ |
                 +---------------------------+---------------------------+
                                             |
                                             v
                         +-------------------+-------------------+
                         |                READ API               |
                         |   GET /orders/client/{id}, /status    |
                         +----------------------------------------+
...
## Project structure
eventstream-nexus/
  application/ (write/read application layer)
  domain/      (pure domain model)
  infrastructure/ (Kafka, Avro, DB, k8s)
  api/         (DTOs, mappers, validation)
  src/main/avro/ (Avro schemas)
  ...

  ...
Getting started
bash
mvn clean package
java -jar target/eventstream-nexus-0.0.1-SNAPSHOT.jar
Build Docker image:

bash
docker build -t eventstream-nexus .
Database migrations (Liquibase)
Liquibase is used for versioned schema management.

Master changelog: src/main/resources/db/changelog/changelog-master.xml

Individual changes: src/main/resources/db/changelog/changes/*.xml

Migrations run automatically on application startup.

Security
OWASP Dependency Check integrated into Maven (verify phase)

CodeQL GitHub Advanced Security workflow

Liquibase migration validation pipeline

Testing
Unit tests for domain logic

Integration tests with Testcontainers (Kafka + PostgreSQL)

Kafka contract tests for Avro events

Virtual‑thread concurrency tests

Changelog
See CHANGELOG.md.

Contributing
See CONTRIBUTING.md.

Code of Conduct
See CODE_OF_CONDUCT.md.

License
MIT License.
