# Prompts de Feign
- Prompts para configurar Feign Client
- Prompts para manejar errores de comunicación
- Prompts para optimizar la configuración de Feign
- Aprendizajes sobre comunicación entre microservicios


## Prompt 1: Explicación de Feign 

### Prompt Utilizado:
¿Qué es Feign, cómo se usa, cómo funciona y para qué sirve en una arquitectura de microservicios?

### Respuesta Recibida:
Qué es: Feign es un cliente HTTP declarativo de Spring Cloud que permite consumir APIs REST de otros microservicios de forma sencilla.
Cómo se usa: Se define una interfaz Java con anotaciones (@FeignClient, @GetMapping, @PostMapping, etc.), y Spring implementa el cliente automáticamente.
Cómo funciona: Traduciendo las llamadas al método de la interfaz en peticiones HTTP al microservicio configurado.
Para qué sirve: Facilita la comunicación entre microservicios sin necesidad de escribir código manual con RestTemplate o WebClient.

### Modificaciones Realizadas:
- Añadí la dependencia spring-cloud-starter-openfeign en el pom.xml del business-service.
- Activé Feign en el proyecto con @EnableFeignClients.

### Explicación del Prompt:
El objetivo era entender el concepto antes de implementarlo en el proyecto.

### Aprendizajes Obtenidos:
- Feign elimina mucho código repetitivo al consumir APIs.
- Permite desacoplar la lógica de negocio de la lógica de comunicación.
- Usar interfaces como contrato facilita el mantenimiento.

---

## Prompt 2: Implementar Feign Client para comunicación con Data Service

### Prompt Utilizado:
Necesito implementar un Feign Client en mi microservicio de negocio para comunicarme con el microservicio de datos.
Quiero que me muestres cómo crear la interfaz DataServiceClient y cómo configurarla.

### Respuesta Recibida:
@FeignClient(name = "data-service", url = "${data.service.url}")
public interface DataServiceClient {

    @GetMapping("/data/productos")
    List<ProductoDTO> obtenerTodosLosProductos();
    
    @GetMapping("/data/productos/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable Long id);
    
    @PostMapping("/data/productos")
    ProductoDTO crearProducto(@RequestBody ProductoRequest request);
    
    @DeleteMapping("/data/productos/{id}")
    void eliminarProducto(@PathVariable Long id);
}

Configuración en application.yml:
data:
service:
url: http://localhost:8081


### Modificaciones Realizadas:
- Cambié la url a http://data-service:8081 cuando lo levanté con Docker Compose.
- Agregué DTOs (ProductoDTO, ProductoRequest) para desacoplar las entidades del data-service.

### Explicación del Prompt:
Se usó porque era la base para que el business-service pudiera acceder a datos sin conectarse directamente a la base.

### Aprendizajes Obtenidos:
- Cada método del Feign Client corresponde a un endpoint del microservicio remoto.
- La configuración de la URL cambia según el entorno (local o Docker).
- Feign se integra fácilmente con Spring Boot usando perfiles y propiedades.

---
