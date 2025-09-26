# Prompts de Testing
Prompts para crear tests unitarios
Prompts para implementar tests de integración
Prompts para configurar testing con diferentes bases de datos
Reflexiones sobre la importancia del testing

## Prompt 1: Tests de comunicación entre microservicios

### Prompt Utilizado:
¿Cómo se hacen los tests de comunicación entre microservicios en Spring Boot?
Dame un ejemplo aplicado a mi proyecto (business-service y data-service).

### Respuesta Recibida:
Los tests de comunicación se hacen validando que el business-service pueda invocar correctamente los endpoints del data-service.
Para esto se usa WireMock o TestRestTemplate en Spring Boot.
Ejemplo usando WireMock en business-service:

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
"data.service.url=http://localhost:${wiremock.server.port}"
})
class DataServiceClientIntegrationTest {

    @Autowired
    private DataServiceClient dataServiceClient;

    @Test
    void testObtenerProductos() {
        stubFor(get(urlEqualTo("/data/productos"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[{\"id\":1,\"nombre\":\"Producto Test\",\"precio\":100}]")));

        List<ProductoDTO> productos = dataServiceClient.obtenerTodosLosProductos();

        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Producto Test", productos.get(0).getNombre());
    }
}

### Modificaciones Realizadas:
- Cambié el puerto dinámico de WireMock para que no choque con otros servicios.
- Ajusté el DTO a los campos que tengo en mi proyecto (ProductoDTO).

### Explicación del Prompt:
Se usó porque necesitaba probar la comunicación entre servicios sin depender de que data-service estuviera levantado realmente.

### Aprendizajes Obtenidos:
- WireMock permite simular respuestas de un microservicio externo.
- Los tests de comunicación validan que la integración funciona aunque el servicio real esté caído.
- Se puede probar tanto respuestas exitosas como errores (404, 500).

---

## Prompt 2: Tests de Feign Client

### Prompt Utilizado:
¿Cómo se hacen los tests de un Feign Client en Spring Boot?
Dame un ejemplo aplicado a mi proyecto (business-service usando DataServiceClient).

### Respuesta Recibida:
Los Feign Clients se testean validando que:
Se construya correctamente la petición HTTP.
Se manejen bien las respuestas y errores.
Ejemplo de test unitario con Mockito:

@ExtendWith(MockitoExtension.class)
class ProductoBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private ProductoBusinessService productoBusinessService;

    @Test
    void testObtenerTodosLosProductos() {
        List<ProductoDTO> productosMock = List.of(
            new ProductoDTO(1L, "Producto 1", "Descripción", BigDecimal.valueOf(100), "Categoría", 10, false)
        );

        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(productosMock);

        List<ProductoDTO> resultado = productoBusinessService.obtenerTodosLosProductos();

        assertEquals(1, resultado.size());
        assertEquals("Producto 1", resultado.get(0).getNombre());
    }
}

Ejemplo de test de error de Feign:
@Test
void testErrorDeFeign() {
when(dataServiceClient.obtenerProductoPorId(999L))
.thenThrow(new FeignException.NotFound("Not Found", null, null, null));

    assertThrows(ProductoNoEncontradoException.class,
        () -> productoBusinessService.obtenerProductoPorId(999L));
}


### Modificaciones Realizadas:
- Creé excepciones personalizadas (ProductoNoEncontradoException, MicroserviceCommunicationException) para mapear los errores de Feign.
- Adapté el DTO a los atributos de mi proyecto.

### Explicación del Prompt:
Se usó para validar la comunicación Feign y el manejo de errores en el business-service.

### Aprendizajes Obtenidos:
- Con Mockito se puede simular la respuesta del Feign Client.
- Es importante testear tanto el camino feliz como los errores de red.
- Los tests de Feign aseguran que las reglas de negocio no dependan directamente del microservicio remoto.

---

## Prompt 3: Testing con diferentes bases de datos

### Prompt Utilizado:
¿Cómo configuro testing con diferentes bases de datos (H2 para desarrollo, MySQL/PostgreSQL en integración)?

### Respuesta Recibida:
Se recomienda usar profiles en Spring Boot:
dev: H2 en memoria (rápido, limpio para tests unitarios).
mysql y postgres: bases reales para integración (vía Docker).
Ejemplo de configuración en application-test.yml:

spring:
datasource:
url: jdbc:h2:mem:testdb
driver-class-name: org.h2.Driver
username: sa
password:
jpa:
hibernate:
ddl-auto: create-drop
show-sql: true

En los tests:
@SpringBootTest
@ActiveProfiles("test")
class DataControllerIntegrationTest {
@Autowired
private TestRestTemplate restTemplate;

    @Test
    void testCrearProducto() {
        Producto producto = new Producto(null, "Test", "Descripción", BigDecimal.valueOf(100), null, null);
        ResponseEntity<Producto> response = restTemplate.postForEntity("/data/productos", producto, Producto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }
}


### Modificaciones Realizadas:
- Usé H2 para los tests unitarios y Docker Compose (MySQL/Postgres) solo para integración.

### Explicación del Prompt:
Se usó porque necesitaba aislar los tests unitarios (rápidos con H2) de los de integración (realistas con MySQL/Postgres).

### Aprendizajes Obtenidos:
- Es buena práctica tener profiles separados para testing.
- H2 es ideal para validar lógica rápidamente.
- MySQL/Postgres permiten validar compatibilidad real de queries.

---

### Reflexión sobre la importancia del Testing

- El testing en microservicios no solo valida que el código funcione, sino que:
- Garantiza la comunicación correcta entre servicios independientes.
- Permite detectar errores de integración antes de producción.
- Aumenta la confiabilidad de la arquitectura distribuida.
- Ayuda a documentar casos de negocio y flujos críticos