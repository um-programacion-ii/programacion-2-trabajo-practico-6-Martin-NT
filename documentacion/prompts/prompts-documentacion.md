## Prompts de Documentación
Prompts para generar documentación técnica
Prompts para crear diagramas de secuencia
Prompts para documentar casos de uso
Reflexiones sobre la importancia de la documentación

## Prompt 1: Documentar README

### Prompt Utilizado:
Quiero que revises mi README.md con las instrucciones de instalación y ejecución de los microservicios.
¿Está bien documentado o le falta algo?
En caso de faltar, sugerime qué debería agregar para que quede completo.

## 🔧 Instrucciones de Instalación

### Clonar el Repositorio
```bash
git clone git@github.com:um-programacion-ii/programacion-2-trabajo-practico-6-Martin-NT.git
```

### Ingresar a la Carpeta del Proyecto
```bash
cd programacion-2-trabajo-practico-6
```

### Compilar los Microservicios
#### Data Service
```bash
cd dataService
mvn clean install
```
#### Business Service
```bash
cd ../businessService
mvn clean install
```
## 🚀 Ejecución de la Aplicación
### 🐳 Instrucciones para Docker
Hay dos opciones para ejecutar el sistema.

#### 🔹 Desarrollo Local (Híbrido)
##### Levantar Bases de Datos
Desde la raíz del proyecto:
```bash
# Levantar MySQL y PostgreSQL
docker compose up -d mysql postgres

# Verificar que los contenedores estén corriendo
docker compose ps

# Ver logs de los contenedores
docker compose logs -f
```
##### Ejecutar los Microservicios con Maven
- Con H2 (no requiere Docker):
```bash
# Terminal 1 - Data Service
cd dataService
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
```bash
# Terminal 2 - Business Service
cd businessService
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
- Con MySQL (requiere Docker):
```bash
# Terminal 1 - Data Service
cd dataService
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```
```bash
# Terminal 2 - Business Service
cd businessService
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```
- Con PostgreSQL (requiere Docker):
```bash
# Terminal 1 - Data Service
cd dataService
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```
```bash
# Terminal 2 - Business Service
cd businessService
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

#### 🔹 Ecosistema Completo con Docker (Todo en uno)
Esta opción esta buena para probar el sistema completo tal como funcionaría en un entorno de producción.
Docker construye las imágenes y levanta microservicios + bases de datos con un solo comando.

##### Levantar todo el sistema
```bash
# Para MySQL
SPRING_PROFILES_ACTIVE=mysql docker compose up --build
```
```bash
# Para PostgreSQL
SPRING_PROFILES_ACTIVE=postgres docker compose up --build
```
##### Detener todos los contenedores
```bash
# Detener contenedores
docker compose down
```
```bash
# Detener y eliminar volúmenes (datos)
docker compose down -v
```
## 🧪 Ejecutar Tests
- Data Service
```bash
cd dataService
mvn test
```
- Business Service
```bash
cd businessService
mvn test
```

### Respuesta Recibida:
Tu README está muy bien estructurado:

✅ Instrucciones de instalación (clonado, compilación de servicios).
✅ Ejecución con Maven y perfiles (dev, mysql, postgres).
✅ Ejecución con Docker Compose en modo híbrido y completo.
✅ Instrucciones para levantar y detener contenedores.
✅ Sección de ejecución de tests.

📌 Lo único que faltaba era la documentación de endpoints de cada microservicio.
Aquí está la sección que deberías agregar:

## 📮 Documentación de Endpoints
### 🔹 Microservicio de Datos (data-service)
| Método | Endpoint                             | Descripción                    |
| ------ | ------------------------------------ | ------------------------------ |
| GET    | `/data/productos`                    | Lista todos los productos      |
| GET    | `/data/productos/{id}`               | Obtiene producto por ID        |
| POST   | `/data/productos`                    | Crea un nuevo producto         |
| PUT    | `/data/productos/{id}`               | Actualiza un producto          |
| DELETE | `/data/productos/{id}`               | Elimina un producto            |
| GET    | `/data/productos/categoria/{nombre}` | Busca productos por categoría  |
| GET    | `/data/inventario/stock-bajo`        | Lista productos con stock bajo |

### 🔹 Microservicio de Negocio (business-service)
| Método | Endpoint                            | Descripción                                  |
| ------ | ----------------------------------- | -------------------------------------------- |
| GET    | `/api/productos`                    | Lista todos los productos (via Feign)        |
| GET    | `/api/productos/{id}`               | Obtiene producto por ID                      |
| POST   | `/api/productos`                    | Crea un producto con validaciones de negocio |
| GET    | `/api/productos/categoria/{nombre}` | Filtra productos por categoría               |
| GET    | `/api/reportes/stock-bajo`          | Consulta productos con stock bajo            |
| GET    | `/api/reportes/valor-inventario`    | Calcula valor total del inventario           |


### Modificaciones Realizadas:
- Se añadió la sección 📮 Documentación de Endpoints con tablas para ambos microservicios.
- Se aclaró qué endpoints provienen de business-service y cuáles de data-service.

### Explicación del Prompt:
Se usó porque la entrega requiere que el README tenga instrucciones completas y la documentación de endpoints, y era necesario validar si algo faltaba.

### Aprendizajes Obtenidos:
- La documentación de endpoints es esencial para que otros desarrolladores o testers sepan cómo interactuar con el sistema.
- Las tablas hacen más clara la relación entre métodos HTTP y operaciones disponibles.
- La consistencia en el README asegura que la entrega cumpla con las pautas de buenas prácticas y los requisitos académicos.

---