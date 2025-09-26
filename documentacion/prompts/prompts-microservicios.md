# Prompts de Microservicios
- Prompts para configurar la arquitectura de microservicios
- Prompts para implementar la separación de responsabilidades
- Prompts para configurar la comunicación entre servicios
- Aprendizajes sobre arquitectura de microservicios

## Prompt 1: Separación de la lógica de los microservicios

### Prompt Utilizado:
¿Cómo se separa la lógica de los microservicios, cómo se hace y cómo lo haría en mi proyecto?

### Respuesta Recibida:
La separación se logra definiendo dominios claros:

data-service: responsable de persistencia y acceso a datos (entidades JPA, repositorios, servicios CRUD).
business-service: responsable de reglas de negocio (validaciones, cálculos, decisiones, comunicación con data-service vía Feign).

En tu proyecto:
data-service manejará Producto, Categoria e Inventario en la base de datos.
business-service expondrá endpoints más orientados al negocio (ej: productos con stock bajo, valor total del inventario).

### Modificaciones Realizadas:
- Moví validaciones de negocio (precio > 0, stock ≥ 0) desde el data-service al business-service.
- En data-service dejé solo operaciones CRUD y consultas simples.

### Explicación del Prompt:
Se usó para entender dónde ubicar la lógica y evitar mezclar validaciones de negocio con persistencia.

### Aprendizajes Obtenidos:
- La regla de oro: un microservicio ≠ una tabla, sino un dominio con responsabilidad clara.
- Evitar duplicación de lógica entre servicios.
- El business-service debe ser el que tome decisiones, y el data-service debe limitarse a manejar datos.

---

## Prompt 2: Funcionamiento de los microservicios

### Prompt Utilizado:
Explícame cómo funcionan los microservicios.

### Respuesta Recibida:
Los microservicios son aplicaciones pequeñas e independientes que se comunican entre sí mediante APIs (normalmente HTTP/REST).
Cada microservicio tiene su propio ciclo de vida, base de datos y lógica.
Funcionan en conjunto dentro de una arquitectura distribuida.

En tu proyecto:
data-service expone endpoints REST para CRUD.
business-service consume esos endpoints con un Feign Client.
Ambos pueden escalarse o desplegarse de forma independiente.

### Modificaciones Realizadas:
- Ninguna

### Explicación del Prompt:
Sirvió para reforzar el concepto general y luego aplicarlo en la práctica del proyecto.

### Aprendizajes Obtenidos:
- Microservicios = independencia, escalabilidad y resiliencia.
- La comunicación se hace con clientes HTTP (Feign en este caso).
- Cada microservicio puede usar distintas tecnologías o bases de datos.

---

## Prompt 3: Arquitectura del proyecto

### Prompt Utilizado:
¿Cómo debería estructurar el proyecto con dos microservicios (data-service y business-service) en Spring Boot?

### Respuesta Recibida:
Te sugiero hacerlo asi
microservices-system/
├── data-service/          # Microservicio de datos
│   ├── entity/
│   ├── repository/
│   ├── service/
│   └── controller/
├── business-service/      # Microservicio de negocio
│   ├── dto/
│   ├── client/
│   ├── service/
│   └── controller/
├── docker-compose.yml     # Configuración de bases de datos y servicios
└── README.md


### Modificaciones Realizadas:
- Reorganicé los paquetes de cada servicio según la recomendación (separando dto, client, entity, etc.).

### Explicación del Prompt:
Se usó para asegurar que la estructura respete las buenas prácticas de microservicios.

### Aprendizajes Obtenidos:
- Cada microservicio debe ser autónomo y auto-contenido.
- Conviene mantener una estructura consistente para facilitar el mantenimiento.

---

## Prompt 5: Comunicación entre microservicios

### Prompt Utilizado:
¿Cómo se configuran los microservicios para que se comuniquen entre sí usando Feign?

### Respuesta Recibida:
Se agrega dependencia spring-cloud-starter-openfeign en business-service.
Se define un cliente Feign:

@FeignClient(name = "data-service", url = "${data.service.url}")
public interface DataServiceClient {
@GetMapping("/data/productos")
List<ProductoDTO> obtenerTodosLosProductos();
}

En application.yml de business-service:
data:
service:
url: http://localhost:8081


### Modificaciones Realizadas:
- Cambié el url de localhost a data-service cuando lo levanté con Docker Compose.

### Explicación del Prompt:
Era necesario para lograr que business-service consultara al data-service sin código duplicado.

### Aprendizajes Obtenidos:
- Con Feign, basta definir una interfaz Java y Spring genera la implementación HTTP.
- La URL cambia según el entorno: localhost en local, nombre del contenedor en Docker.
---
