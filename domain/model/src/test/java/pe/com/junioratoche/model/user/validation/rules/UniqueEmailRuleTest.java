package pe.com.junioratoche.model.user.validation.rules;

import org.junit.jupiter.api.Test;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.ports.EmailUniquenessPort;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.user.validation.rules.impl.UniqueEmailRule;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class UniqueEmailRuleTest {
    @Test
    void testEmailIsUnique() {
        User user = new User();
        user.setEmail("unique@email.com");
        EmailUniquenessPort port = mock(EmailUniquenessPort.class);
        when(port.exists("unique@email.com")).thenReturn(Mono.just(false));
        UniqueEmailRule rule = new UniqueEmailRule("email", port, ErrorCode.USER_EMAIL_ALREADY_USED);
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }

    @Test
    void testEmailIsNotUnique() {
        User user = new User();
        user.setEmail("used@email.com");
        EmailUniquenessPort port = mock(EmailUniquenessPort.class);
        when(port.exists("used@email.com")).thenReturn(Mono.just(true));
        UniqueEmailRule rule = new UniqueEmailRule("email", port, ErrorCode.USER_EMAIL_ALREADY_USED);
        StepVerifier.create(rule.validate(user))
                .expectNextMatches(e -> e.getMessage().contains("email already registered"))
                .verifyComplete();
    }

    @Test
    void testNullEmail() {
        User user = new User();
        user.setEmail(null);
        EmailUniquenessPort port = mock(EmailUniquenessPort.class);
        when(port.exists(null)).thenReturn(Mono.just(false));
        UniqueEmailRule rule = new UniqueEmailRule("email", port, ErrorCode.USER_EMAIL_ALREADY_USED);
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }

    @Test
    void testEmptyEmail() {
        User user = new User();
        user.setEmail("");
        EmailUniquenessPort port = mock(EmailUniquenessPort.class);
        when(port.exists("")).thenReturn(Mono.just(false));
        UniqueEmailRule rule = new UniqueEmailRule("email", port, ErrorCode.USER_EMAIL_ALREADY_USED);
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }
}
