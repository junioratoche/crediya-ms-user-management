package pe.com.junioratoche.model.user.validation.rules;

import org.junit.jupiter.api.Test;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.rules.impl.RequiredRule;
import reactor.test.StepVerifier;

class RequiredRuleTest {
    @Test
    void testRequiredFieldPresent() {
        User user = new User();
        user.setFirstName("Juan");
        RequiredRule<User> rule = new RequiredRule<>("firstName", User::getFirstName, pe.com.junioratoche.model.shared.error.ErrorCode.USER_FIRST_NAME_REQUIRED);
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }

    @Test
    void testRequiredFieldEmptyString() {
        User user = new User();
        user.setFirstName("");
        RequiredRule<User> rule = new RequiredRule<>("firstName", User::getFirstName, pe.com.junioratoche.model.shared.error.ErrorCode.USER_FIRST_NAME_REQUIRED);
        StepVerifier.create(rule.validate(user))
                .expectNextMatches(e -> e.getMessage().contains("required"))
                .verifyComplete();
    }

    @Test
    void testRequiredFieldWhitespaceString() {
        User user = new User();
        user.setFirstName("   ");
        RequiredRule<User> rule = new RequiredRule<>("firstName", User::getFirstName, pe.com.junioratoche.model.shared.error.ErrorCode.USER_FIRST_NAME_REQUIRED);
        StepVerifier.create(rule.validate(user))
                .expectNextMatches(e -> e.getMessage().contains("required"))
                .verifyComplete();
    }

    @Test
    void testRequiredFieldMissing() {
        User user = new User();
        RequiredRule<User> rule = new RequiredRule<>("firstName", User::getFirstName, pe.com.junioratoche.model.shared.error.ErrorCode.USER_FIRST_NAME_REQUIRED);
        StepVerifier.create(rule.validate(user))
                .expectNextMatches(e -> e.getMessage().contains("required"))
                .verifyComplete();
    }
}
