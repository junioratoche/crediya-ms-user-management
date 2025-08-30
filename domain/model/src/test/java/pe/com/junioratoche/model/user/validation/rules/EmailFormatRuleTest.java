package pe.com.junioratoche.model.user.validation.rules;

import org.junit.jupiter.api.Test;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.rules.impl.EmailFormatRule;
import reactor.test.StepVerifier;

class EmailFormatRuleTest {
    @Test
    void testValidEmailFormat() {
        User user = new User();
        user.setEmail("test@email.com");
        EmailFormatRule rule = new EmailFormatRule("email", ErrorCode.USER_EMAIL_INVALID);
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }

    @Test
    void testInvalidEmailFormat() {
        User user = new User();
        user.setEmail("invalid-email");
        EmailFormatRule rule = new EmailFormatRule("email", ErrorCode.USER_EMAIL_INVALID);
        StepVerifier.create(rule.validate(user))
                .expectNextMatches(e -> e.getMessage().contains("invalid format"))
                .verifyComplete();
    }

    @Test
    void testNullEmail() {
        User user = new User();
        user.setEmail(null);
        EmailFormatRule rule = new EmailFormatRule("email", ErrorCode.USER_EMAIL_INVALID);
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }

    @Test
    void testEmptyEmail() {
        User user = new User();
        user.setEmail("");
        EmailFormatRule rule = new EmailFormatRule("email", ErrorCode.USER_EMAIL_INVALID);
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }
}
