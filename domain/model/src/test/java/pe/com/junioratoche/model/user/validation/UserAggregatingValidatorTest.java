package pe.com.junioratoche.model.user.validation;

import org.junit.jupiter.api.Test;
import pe.com.junioratoche.model.user.User;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;

class UserAggregatingValidatorTest {
    @Test
    void testValidateWithValidUser() {
        UserAggregatingValidator validator = new UserAggregatingValidator();
        pe.com.junioratoche.model.rol.Rol rol = pe.com.junioratoche.model.rol.Rol.builder()
                .id(java.util.UUID.randomUUID())
                .name("Admin")
                .description("Administrador")
                .build();
        User user = User.builder()
                .firstName("Juan")
                .lastName("Perez")
                .email("juan.perez@email.com")
                .salary(new java.math.BigDecimal("5000"))
                .role(rol)
                .build();
        Mono<User> result = validator.validateForCreate(user);
        result.block(); // No debe lanzar excepción
    }

    @Test
    void testValidateWithInvalidUser() {
        UserAggregatingValidator validator = new UserAggregatingValidator();
        User user = new User(); // Campos requeridos vacíos
        Mono<User> result = validator.validateForCreate(user);
        assertThrows(Exception.class, result::block);
    }
}
