# TODO

## Repository Architecture Cleanup

- Create a clear separation between **domain/model repositories** and **infrastructure repositories**.
  - Domain layer should expose pure repository **interfaces** only.
  - Infrastructure layer should contain all **JPA/Spring Data implementations**.
  - Ensure no JPA annotations or persistence concerns leak into the domain model.
  - Verify that all application services depend on **domain repositories**, not infrastructure ones.
  - Move any accidental persistence logic out of the domain layer into the infrastructure adapter layer.
  - Confirm naming conventions:
    - `*Repository` → domain interface
    - `Jpa*Repository` → infrastructure adapter
    - `SpringData*Repository` → Spring Data JPA interface
