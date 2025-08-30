package pe.com.junioratoche.model.user.validation.provider;

import org.junit.jupiter.api.Test;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.ports.EmailUniquenessPort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCreateRuleProviderTest {
    @Test
    void testRulesForCreate() {
        EmailUniquenessPort port = mock(EmailUniquenessPort.class);
        UserCreateRuleProvider provider = new UserCreateRuleProvider(port);
        List<?> rules = provider.rulesForCreate();
        assertNotNull(rules);
        assertFalse(rules.isEmpty());
    }

    @Test
    void testRulesForCreateContentAndValidation() {
        EmailUniquenessPort port = mock(EmailUniquenessPort.class);
        // El email debe ser único, por lo tanto el mock debe devolver Mono.just(false)
        when(port.exists(anyString())).thenReturn(reactor.core.publisher.Mono.just(false));
        UserCreateRuleProvider provider = new UserCreateRuleProvider(port);
        List<?> rules = provider.rulesForCreate();
        assertTrue(rules.stream().allMatch(r -> r instanceof pe.com.junioratoche.model.user.validation.rules.Rule));
        // Validar que las reglas funcionan con un usuario válido
        User validUser = new User();
        validUser.setFirstName("Juan");
        validUser.setLastName("Perez");
        validUser.setEmail("juan.perez@email.com");
        validUser.setDocumentNumber("12345678");
        validUser.setPhoneNumber("999999999");
        validUser.setSalary(java.math.BigDecimal.valueOf(1000));
        for (Object rule : rules) {
            pe.com.junioratoche.model.user.validation.rules.Rule<User> r = (pe.com.junioratoche.model.user.validation.rules.Rule<User>) rule;
            assertNull(r.validate(validUser).blockFirst());
        }
    }

    @Test
    void testRulesForCreateWithInvalidUser() {
        EmailUniquenessPort port = mock(EmailUniquenessPort.class);
        when(port.exists(anyString())).thenReturn(reactor.core.publisher.Mono.just(false));
        UserCreateRuleProvider provider = new UserCreateRuleProvider(port);
        List<?> rules = provider.rulesForCreate();
        User invalidUser = new User(); // Todos los campos nulos
        boolean hasError = false;
        for (Object rule : rules) {
            pe.com.junioratoche.model.user.validation.rules.Rule<User> r = (pe.com.junioratoche.model.user.validation.rules.Rule<User>) rule;
            if (r.validate(invalidUser).blockFirst() != null) {
                hasError = true;
                break;
            }
        }
        assertTrue(hasError);
    }
}
