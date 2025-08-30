package pe.com.junioratoche.model.user.validation;

import org.junit.jupiter.api.Test;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.rules.Rule;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class ReactiveValidatorTest {
    @Test
    void testValidateSuccess() {
        @SuppressWarnings("unchecked")
        Rule<User> rule = (Rule<User>) mock(Rule.class);
        User user = new User();
        when(rule.validate(user)).thenReturn(reactor.core.publisher.Flux.empty());
        ReactiveValidator validator = new ReactiveValidator(Collections.singletonList(rule));
        StepVerifier.create(validator.validate(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void testValidateError() {
        @SuppressWarnings("unchecked")
        Rule<User> rule = (Rule<User>) mock(Rule.class);
        User user = new User();
        when(rule.validate(user)).thenReturn(reactor.core.publisher.Flux.error(new RuntimeException("Validation error")));
        ReactiveValidator validator = new ReactiveValidator(Collections.singletonList(rule));
        StepVerifier.create(validator.validate(user))
                .expectErrorMatches(e -> e.getMessage().equals("Validation error"))
                .verify();
    }
}
