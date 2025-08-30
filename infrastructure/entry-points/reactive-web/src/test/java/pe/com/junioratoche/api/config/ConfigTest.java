package pe.com.junioratoche.api.config;

import pe.com.junioratoche.api.Handler;
import pe.com.junioratoche.api.RouterRest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.junioratoche.usecase.registeruser.RegisterUserUseCase;
import org.mockito.Mockito;
import reactor.core.publisher.Hooks;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, ConfigTest.TestConfig.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {


    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    static void enableReactorDebug() {
        Hooks.onOperatorDebug();
    }

    @BeforeEach
    void enableLogging() {
        webTestClient = webTestClient.mutate()
                .filter((req, next) -> {
                    System.out.println(">> " + req.method() + " " + req.url());
                    System.out.println(">> req headers: " + req.headers().toString());
                    return next.exchange(req).doOnNext(res -> {
                        System.out.println("<< status: " + res.statusCode().value());
                        System.out.println("<< res headers: " + res.headers().toString());
                    });
                })
                .build();
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        String body = """
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
        webTestClient.post()
                .uri("/api/users")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Content-Security-Policy", ".*default-src 'self'.*frame-ancestors 'self'.*form-action 'self'.*")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

    static class TestConfig {
        @Bean
        public UserPath userPath() {
            UserPath path = new UserPath();
            try {
                java.lang.reflect.Field field = UserPath.class.getDeclaredField("users");
                field.setAccessible(true);
                field.set(path, "/api/users");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return path;
        }
        @Bean
        public RegisterUserUseCase registerUserUseCase() {
            RegisterUserUseCase useCase = Mockito.mock(RegisterUserUseCase.class);
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
            Mockito.when(useCase.saveUser(Mockito.any())).thenReturn(reactor.core.publisher.Mono.just(user));
            return useCase;
        }
    }
}