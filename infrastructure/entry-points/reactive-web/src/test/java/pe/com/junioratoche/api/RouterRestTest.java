package pe.com.junioratoche.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.junioratoche.api.config.UserPath;
import pe.com.junioratoche.usecase.registeruser.RegisterUserUseCase;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

class RouterRestTest {

    private WebTestClient client;
    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = Mockito.mock(RegisterUserUseCase.class);
        // Mock para devolver un User válido y completo
        var rol = pe.com.junioratoche.model.rol.Rol.builder()
                .id(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .name("ADMIN")
                .description("Administrador")
                .build();
        var user = pe.com.junioratoche.model.user.User.builder()
                .id(java.util.UUID.randomUUID())
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan.perez@example.com")
                .documentNumber("12345678")
                .phoneNumber("+51987654321")
                .salary(java.math.BigDecimal.valueOf(1200))
                .role(rol)
                .build();
        Mockito.when(useCase.saveUser(any())).thenReturn(Mono.just(user));
        var userPath = new UserPath();
        userPath.setUsers("/api/users");
        var handler = new Handler(useCase);
        var router = new RouterRest(userPath, handler);
        RouterFunction<ServerResponse> routes = router.routerFunction();
        client = WebTestClient.bindToRouterFunction(routes)
                .configureClient()
                .filter((req, next) -> {
                    System.out.println(">> " + req.method() + " " + req.url());
                    return next.exchange(req).doOnNext(res ->
                            System.out.println("<< " + res.statusCode().value() + " " + res.headers().asHttpHeaders())
                    );
                })
                .baseUrl("/")
                .build();
    }

    @Test
    @DisplayName("Router Function Returns Created When Request Is Valid")
    void routerFunctionReturnsCreatedWhenRequestIsValid() {
        var body = """
          {
            "firstName": "Juan",
            "lastName": "Pérez",
            "email": "juan.perez@example.com",
            "documentNumber": "12345678",
            "phoneNumber": "+51987654321",
            "roleId": "00000000-0000-0000-0000-000000000000",
            "salary": 1200
          }
        """;
        client.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectHeader().valueEquals("Location", "/api/users");
        Mockito.verify(useCase, Mockito.times(1)).saveUser(any());
    }
}
