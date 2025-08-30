package pe.com.junioratoche.r2dbc;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pe.com.junioratoche.model.user.validation.ports.EmailUniquenessPort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class EmailUniquenessAdapterTest {
    @Test
    void exists_returnsTrue_whenEmailExists() {
        UserReactiveRepository repository = Mockito.mock(UserReactiveRepository.class);
        Mockito.when(repository.existsByEmail("test@email.com")).thenReturn(Mono.just(true));
        EmailUniquenessPort adapter = new EmailUniquenessAdapter(repository);
        StepVerifier.create(adapter.exists("test@email.com"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void exists_returnsFalse_whenEmailDoesNotExist() {
        UserReactiveRepository repository = Mockito.mock(UserReactiveRepository.class);
        Mockito.when(repository.existsByEmail("no@email.com")).thenReturn(Mono.just(false));
        EmailUniquenessPort adapter = new EmailUniquenessAdapter(repository);
        StepVerifier.create(adapter.exists("no@email.com"))
                .expectNext(false)
                .verifyComplete();
    }
}

