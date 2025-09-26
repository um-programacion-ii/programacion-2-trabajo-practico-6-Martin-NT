# Prompts de Docker
- Prompts para configurar Docker Compose
- Prompts para resolver problemas de containerización
- Prompts para optimizar la configuración
- Aprendizajes sobre DevOps y containerización

## Prompt 1: Docker-Compose

### Prompt Utilizado:
```bash
mi profesor me paso este docker-compose para usar como base, quiero que me expliques primero como funciona docker compose, que hace, explicame el codigo que te paso y como deberia adaptarlo a mi proyecto:

# docker-compose.yml
services:
  mysql:
    image: mysql:8.4
    container_name: microservices_mysql
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: microservices_db
      MYSQL_USER: microservices_user
      MYSQL_PASSWORD: microservices_pass
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - microservices_network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 20s
      retries: 10

  postgres:
    image: postgres:16
    container_name: microservices_postgres
    environment:
      POSTGRES_DB: microservices_db
      POSTGRES_USER: microservices_user
      POSTGRES_PASSWORD: microservices_pass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - microservices_network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U microservices_user -d microservices_db"]
      timeout: 20s
      retries: 10

volumes:
  mysql_data:
  postgres_data:

networks:
  microservices_network:
    driver: bridge 
```

### Respuesta Recibida:
```bash
Perfecto, te lo explico paso a paso 🐳.

---

## 🔹 ¿Qué es **Docker Compose**?

* Es una herramienta de **Docker** que permite definir y correr múltiples contenedores con un solo archivo (`docker-compose.yml`).
* En lugar de levantar manualmente cada contenedor con `docker run ...`, escribís toda la configuración en YAML y lo levantás con:

  ```bash
  docker compose up -d
  ```
* Ideal cuando tenés **varios servicios que dependen entre sí** (ej: MySQL + PostgreSQL + microservicios).

---

## 🔹 Explicación del archivo que te pasó tu profesor

### Estructura General

```yaml
services:      # Lista de servicios a levantar
volumes:       # Volúmenes (persistencia de datos)
networks:      # Redes para comunicar servicios
```

---

### Servicio: **MySQL**

```yaml
mysql:
  image: mysql:8.4                   # Imagen de MySQL (versión 8.4)
  container_name: microservices_mysql # Nombre del contenedor
  environment:                       # Variables de entorno para inicializar MySQL
    MYSQL_ROOT_PASSWORD: root_password
    MYSQL_DATABASE: microservices_db
    MYSQL_USER: microservices_user
    MYSQL_PASSWORD: microservices_pass
  ports:
    - "3306:3306"                    # Mapea el puerto 3306 del host al contenedor
  volumes:
    - mysql_data:/var/lib/mysql      # Persiste los datos aunque apagues el contenedor
  networks:
    - microservices_network          # Conecta a la red compartida
  healthcheck:                       # Comprueba si MySQL está listo
    test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
    timeout: 20s
    retries: 10
```

👉 Esto levanta una base MySQL lista con:

* Usuario: `microservices_user`
* Password: `microservices_pass`
* Base: `microservices_db`

---

### Servicio: **PostgreSQL**

```yaml
postgres:
  image: postgres:16                 # Imagen oficial de PostgreSQL (v16)
  container_name: microservices_postgres
  environment:                       # Variables de entorno para inicializar Postgres
    POSTGRES_DB: microservices_db
    POSTGRES_USER: microservices_user
    POSTGRES_PASSWORD: microservices_pass
  ports:
    - "5432:5432"                    # Expone PostgreSQL al host
  volumes:
    - postgres_data:/var/lib/postgresql/data
  networks:
    - microservices_network
  healthcheck:                       # Comprueba que PostgreSQL está listo
    test: ["CMD-SHELL", "pg_isready -U microservices_user -d microservices_db"]
    timeout: 20s
    retries: 10
```

👉 Esto levanta una base PostgreSQL lista con los mismos credenciales que MySQL, lo que te facilita cambiar de DB en tu microservicio sin modificar usuarios ni passwords.

---

### Volúmenes

```yaml
volumes:
  mysql_data:
  postgres_data:
```

👉 Guardan los datos en el disco del host, así aunque hagas `docker compose down`, la info persiste.
Si querés borrar todo (resetear datos), tenés que hacer:

```bash
docker compose down -v
```

---

### Red

```yaml
networks:
  microservices_network:
    driver: bridge
```

👉 Crea una **red bridge** compartida para que los servicios se comuniquen usando su **nombre de servicio** (por ej. `mysql` o `postgres`) en lugar de `localhost`.

---

## 🔹 ¿Cómo adaptarlo a tu proyecto?

Tu proyecto tiene dos microservicios:

* **data-service** (maneja las bases de datos)
* **business-service** (usa Feign para comunicarse con data-service)

Para integrarlos:

1. **Agregar tus microservicios al `docker-compose.yml`:**

   ```yaml
   data-service:
     build: ./data-service
     container_name: data_service
     ports:
       - "8081:8081"
     environment:
       SPRING_PROFILES_ACTIVE: mysql  # o postgres según quieras
     networks:
       - microservices_network
     depends_on:
       mysql:
         condition: service_healthy

   business-service:
     build: ./business-service
     container_name: business_service
     ports:
       - "8082:8082"
     environment:
       DATA_SERVICE_URL: http://data-service:8081
       SPRING_PROFILES_ACTIVE: mysql
     networks:
       - microservices_network
     depends_on:
       data-service:
         condition: service_started
   ```

   🔑 Puntos clave:

    * `build: ./carpeta` indica que se construya la imagen de tu microservicio desde su Dockerfile.
    * `depends_on` asegura que tu microservicio espere a que la DB esté lista.
    * En lugar de `localhost`, vas a usar `mysql` o `postgres` como **host** en tu `application.yml`.

2. **En tu `application.yml`** para MySQL:

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://mysql:3306/microservices_db
       username: microservices_user
       password: microservices_pass
   ```

   Para PostgreSQL:

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://postgres:5432/microservices_db
       username: microservices_user
       password: microservices_pass
   ```

---

📌 Resumen:

* Docker Compose levanta **ambas bases de datos** listas para usar.
* Tus microservicios se van a conectar a esas DB usando el **nombre del servicio** como host (`mysql`, `postgres`).
* Podés extender el `docker-compose.yml` para incluir también `data-service` y `business-service`.

---

¿Querés que te prepare la **versión extendida de tu docker-compose** ya con `data-service` y `business-service` incluidos?

```

### Modificaciones Realizadas:
- Se agregó la configuración de los microservicios data-service y business-service al docker-compose.yml, con build apuntando a sus carpetas respectivas.
- Se reemplazó localhost por mysql o postgres en las URLs de conexión dentro de application.yml, ya que en Docker Compose los servicios se comunican por nombre de contenedor.
- Se configuró depends_on para asegurar que los microservicios esperen a que las bases de datos estén listas antes de iniciar.
- Se agregó la variable de entorno SPRING_PROFILES_ACTIVE para seleccionar fácilmente el profile (mysql o postgres) al levantar los microservicios.

### Explicación del Prompt:
Este prompt se usó porque era necesario comprender cómo funciona Docker Compose y cómo adaptar el archivo base del profesor al proyecto de microservicios. La explicación permitió entender cada parte del archivo y cómo extenderlo para incluir tanto bases de datos como microservicios.

### Aprendizajes Obtenidos:
- Concepto clave de Docker Compose: levantar múltiples contenedores con un solo archivo de configuración.
- Healthchecks: sirven para asegurarse de que los servicios de base de datos estén listos antes de que los microservicios intenten conectarse.
- Red compartida: permite que los contenedores se comuniquen usando su nombre de servicio en lugar de localhost.
- Persistencia con volúmenes: evita perder datos entre reinicios de los contenedores.
- Buenas prácticas: usar depends_on y SPRING_PROFILES_ACTIVE para manejar dependencias y perfiles de configuración en Spring Boot.

---