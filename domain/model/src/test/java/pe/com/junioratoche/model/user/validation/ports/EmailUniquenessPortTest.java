package pe.com.junioratoche.model.user.validation.ports;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class EmailUniquenessPortTest {
    @Test
    void testExistsTrue() {
        EmailUniquenessPort port = email -> Mono.just(true);
        StepVerifier.create(port.exists("unique@email.com"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testExistsFalse() {
        EmailUniquenessPort port = email -> Mono.just(false);
        StepVerifier.create(port.exists("used@email.com"))
                .expectNext(false)
                .verifyComplete();
    }
}
