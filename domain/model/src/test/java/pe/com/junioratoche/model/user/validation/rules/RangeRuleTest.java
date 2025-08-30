package pe.com.junioratoche.model.user.validation.rules;

import org.junit.jupiter.api.Test;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.rules.impl.RangeRule;
import reactor.test.StepVerifier;

class RangeRuleTest {
    @Test
    void testValueWithinRange() {
        User user = new User();
        user.setSalary(new java.math.BigDecimal("5000"));
        RangeRule<User> rule = new RangeRule<>(
            "salary",
            User::getSalary,
            new java.math.BigDecimal("0"),
            new java.math.BigDecimal("10000"),
            pe.com.junioratoche.model.shared.error.ErrorCode.USER_SALARY_OUT_OF_RANGE
        );
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }

    @Test
    void testValueOutOfRange() {
        User user = new User();
        user.setSalary(new java.math.BigDecimal("20000"));
        RangeRule<User> rule = new RangeRule<>(
            "salary",
            User::getSalary,
            new java.math.BigDecimal("0"),
            new java.math.BigDecimal("10000"),
            pe.com.junioratoche.model.shared.error.ErrorCode.USER_SALARY_OUT_OF_RANGE
        );
        StepVerifier.create(rule.validate(user))
                .expectNextMatches(e -> e.getMessage().contains("salary out of range"))
                .verifyComplete();
    }

    @Test
    void testNullSalary() {
        User user = new User();
        user.setSalary(null);
        RangeRule<User> rule = new RangeRule<>(
            "salary",
            User::getSalary,
            new java.math.BigDecimal("0"),
            new java.math.BigDecimal("10000"),
            pe.com.junioratoche.model.shared.error.ErrorCode.USER_SALARY_OUT_OF_RANGE
        );
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }

    @Test
    void testSalaryAtMin() {
        User user = new User();
        user.setSalary(new java.math.BigDecimal("0"));
        RangeRule<User> rule = new RangeRule<>(
            "salary",
            User::getSalary,
            new java.math.BigDecimal("0"),
            new java.math.BigDecimal("10000"),
            pe.com.junioratoche.model.shared.error.ErrorCode.USER_SALARY_OUT_OF_RANGE
        );
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }

    @Test
    void testSalaryAtMax() {
        User user = new User();
        user.setSalary(new java.math.BigDecimal("10000"));
        RangeRule<User> rule = new RangeRule<>(
            "salary",
            User::getSalary,
            new java.math.BigDecimal("0"),
            new java.math.BigDecimal("10000"),
            pe.com.junioratoche.model.shared.error.ErrorCode.USER_SALARY_OUT_OF_RANGE
        );
        StepVerifier.create(rule.validate(user))
                .verifyComplete();
    }
}
