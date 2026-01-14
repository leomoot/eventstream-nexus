<p align="center"> <img src="https://dummyimage.com/600x150/000/fff&text=EventStream+Nexus" alt="EventStream Nexus Logo"/> </p> <p align="center"> <strong>API‑first • Idempotent • Event‑enabled • MapStruct‑powered • Java 25</strong> </p> <p align="center"> <!-- Core stack --> <img src="https://img.shields.io/badge/Java-25-007396?style=for-the-badge" /> <img src="https://img.shields.io/badge/Spring%20Boot-4.0.0-6DB33F?style=for-the-badge" /> <img src="https://img.shields.io/badge/API-OpenAPI%203.0-6BA539?style=for-the-badge" /> <img src="https://img.shields.io/badge/Mapping-MapStruct-FF6F00?style=for-the-badge" /> <img src="https://img.shields.io/badge/Boilerplate-Lombok-CA2C92?style=for-the-badge" /> <!-- Infrastructure --> <img src="https://img.shields.io/badge/Database-PostgreSQL%2015-336791?style=for-the-badge" /> <img src="https://img.shields.io/badge/DB-Liquibase-orange?style=for-the-badge" /> <img src="https://img.shields.io/badge/Events-Kafka-231F20?style=for-the-badge" /> <!-- CI / Quality --> <img src="https://img.shields.io/badge/CI-GitHub%20Actions-2088FF?style=for-the-badge" /> <img src="https://img.shields.io/badge/Security-CodeQL-FF0000?style=for-the-badge" /> <img src="https://img.shields.io/badge/Dependencies-OWASP%20Checked-8A2BE2?style=for-the-badge" /> <!-- Optional metrics --> <img src="https://img.shields.io/badge/Tests-Coverage%20Ready-4CAF50?style=for-the-badge" /> <img src="https://img.shields.io/badge/Docker-Image%20Ready-0db7ed?style=for-the-badge" /> <!-- License --> <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" /> </p> 

EventStream Nexus is an **API‑first**, **contract‑driven**, **idempotent**, and **event‑enabled** backend service built with: 
- **Spring Boot 4**
- **Java 25**
- **OpenAPI‑first development** (single source of truth)
- **MapStruct + Lombok** for zero‑boilerplate mapping
- **PostgreSQL + Liquibase** for schema evolution
- **Kafka (optional)** for outbound domain events
- **Maven** for reproducible builds
- **GitHub Actions** for CI, CodeQL, OWASP, and Liquibase validation

The OpenAPI specification defines the entire API surface. All API interfaces and models are generated automatically.
