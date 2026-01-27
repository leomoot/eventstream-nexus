# Changelog

## 2026-01-27
- Added initial Kubernetes manifests for local development, covering PostgreSQL, Liquibase job, Kafka, and the EventStream application.
- Introduced ConfigMaps for Liquibase changelogs and PostgreSQL init scripts alongside PVC-backed StatefulSets for data durability.
- Parameterized sensitive secrets using environment-driven `stringData` entries to allow `envsubst` before applying manifests.
- Ensured Kafka bootstrap scripts provision the `client-created`, `client-updated`, and `client-deleted` topics across Docker Compose and Kubernetes variants.

