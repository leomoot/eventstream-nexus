# Local Kubernetes Deployment

This directory contains manifests that mirror the local `docker-compose` setup using Kubernetes primitives (StatefulSets, Jobs, and Deployments).

## Prerequisites
- `kubectl` configured against a cluster or local runtime such as `kind` or `minikube`.
- `envsubst` available (usually provided by `gettext`).
- The following environment variables exported in your shell:
  - `EVENTSTREAM_LIQUIBASE_PASSWORD`
  - `EVENTSTREAM_APP_PASSWORD`

## Apply Manifests
Render the manifest with environment values and apply:

```sh
envsubst < manifests.yaml | kubectl apply -f -
```

The Liquibase Job runs automatically; wait for completion if you need confirmation:

```sh
kubectl wait --namespace eventstream-local --for=condition=complete job/liquibase-migrate --timeout=120s
```

## Access the Application
Port-forward to reach the Spring Boot service locally:

```sh
kubectl port-forward --namespace eventstream-local svc/eventstream-app 8080:8080
```

Check the health endpoint after forwarding:

```sh
curl http://localhost:8080/actuator/health/liveness
```
