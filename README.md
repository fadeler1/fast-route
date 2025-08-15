# Fast Route API

API REST en **Java 21** / **Spring Boot 3** para calcular la ruta más rápida (tiempo total) entre dos ubicaciones, a partir de un grafo cargado por CSV.

## Características
- Carga de datos por **CSV** (multipart).
- **Swagger UI** (springdoc).
- **Docker multi-stage**.
- Funciona con grafos de hasta **10.000 aristas**.

## Requisitos
- **Java 21** (Temurin recomendado)
- **Maven Wrapper** (incluido) o **Maven 3.5+**
- **Docker** (opcional)

## Endpoints principales

### 1. Cargar CSV (multipart)
**POST** `/upload`  
Campo: `file=@archivo.csv`

Ejemplo:
```bash
curl -X POST -F "file=@tiempos.csv" http://localhost:8080/graph/upload
```

### 2. Ruta más rápida
**GET** `/routes?from=CP1&to=R20`

Ejemplo:
```bash
curl "http://localhost:8080/routes?from=CP1&to=R20"
```

Respuesta:
```json
{"ruta":["CP1","CP2","R20"],"tiempoTotal":74}
```

## Swagger UI
- `http://localhost:8080/docs`

## Uso con Docker

### Ejecutar
```bash
docker run --name fast-route -p 8080:8080 fast-route:latest
```

### Logs
```bash
docker logs -f fast-route
```

### Detener
```bash
docker stop fast-route
```

### Eliminar
```bash
docker rm fast-route
```
