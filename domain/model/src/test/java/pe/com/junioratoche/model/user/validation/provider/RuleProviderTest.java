package pe.com.junioratoche.model.user.validation.provider;

import org.junit.jupiter.api.Test;
import pe.com.junioratoche.model.user.User;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RuleProviderTest {
    @Test
    void testRulesForCreateReturnsList() {
        RuleProvider<User> provider = () -> java.util.Collections.singletonList(
            new pe.com.junioratoche.model.user.validation.rules.Rule<User>() {
                @Override
                public String name() { return "test"; }
                @Override
                public reactor.core.publisher.Flux<pe.com.junioratoche.model.shared.error.ErrorDetail> validate(User u) {
                    return reactor.core.publisher.Flux.empty();
                }
            }
        );
        List<?> rules = provider.rulesForCreate();
        assertNotNull(rules);
        assertEquals(1, rules.size());
    }
}
