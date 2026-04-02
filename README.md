# Sistema de Suscripciones (Arquitectura Orientada a Eventos)

Este repositorio es una prueba de concepto (PoC) donde armé un sistema de suscripciones distribuido. La idea principal fue resolver los problemas comunes que surgen al separar una aplicación en microservicios, enfocándome en la resiliencia y el manejo de pagos.

El proyecto está dividido en dos microservicios principales usando **Arquitectura Hexagonal**:
- **Payment API**: Se encarga de procesar los cobros conectándose a una pasarela externa.
- **Membership API**: Mantiene el estado de las cuentas de los usuarios y activa sus suscripciones.

## Patrones y tecnologías clave

Quise salir del típico CRUD simple, así que metí un par de patrones de arquitectura útiles para sistemas de mayor concurrencia:

- **Apache Kafka**: Desacoplé la comunicación. Cuando un pago se aprueba, no llamo al servicio de membresía directo, sino que emito un evento a Kafka.
- **Patrón Outbox**: Para evitar inconsistencias si Kafka se cae justo cuando guardé el pago en Postgres, implementé un Outbox transaccional. Guardo el evento en base de datos primero y luego lo envío.
- **Circuit Breaker (Resilience4j)**: Envolví la llamada a la pasarela de pagos externa. Si la pasarela falla o se pone lenta, el circuito se abre y evita que mi sistema empiece a encolar peticiones y se cuelgue.
- **Idempotencia con Redis**: Como los brokers de mensajes operan bajo "At least once delivery", existe el riesgo de procesar un mismo pago dos veces en el MS de Membresías. Usé Redis como lock temporal; antes de procesar el evento de Kafka pregunto en memoria si ya procesamos ese ID. 
- **Validación Síncrona**: Antes de llegar al proceso de pago en sí, el Payment API hace una consulta rápida HTTP al MS de usuarios para verificar que el UUID exista, evitando cobrarle a fantasmas.

## Stack 

- Java 17 + Spring Boot 3
- PostgreSQL
- Apache Kafka + Zookeeper
- Redis
- Docker & Docker Compose
- Maven (Multi-Módulo)

## ¿Cómo levantarlo?

Todo está configurado en Docker para que levante junto (bases de datos, message brokers y los dos servicios Java). Solo necesitas tener Docker corriendo.

1. Clona el proyecto y entrá a la carpeta base.
2. Construí y levantá los contenedores usando compose:
   ```bash
   docker-compose up -d --build
   ```

Si querés ver los logs de los servicios en vivo:
```bash
docker logs -f payment_api
docker logs -f membership_api
```

## Probando el flujo completo con Postman

En la raíz del proyecto vas a encontrar el archivo `Postman_Collection.json`. Importalo en Postman y seguí estos tres pasos:

1. **Crear el usuario:** Ejecutá una petición POST al endpoint de crear usuarios en el puerto 8080. Copiate el UUID que te devuelve el sistema.
2. **Realizar el pago:** Ejecutá el POST de cobrar suscripción apuntando al puerto 8081. Vas a notar que tenés que pegar tu UUID en el body.
3. **Verificar estado:** Hace un GET al endpoint de estado del puerto 8080. Deberías ver que la membresía figura como activa gracias a que el servicio atrapó el evento por Kafka bajo el capó.
