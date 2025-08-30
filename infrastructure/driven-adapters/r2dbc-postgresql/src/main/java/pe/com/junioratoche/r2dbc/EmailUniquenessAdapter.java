package pe.com.junioratoche.r2dbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.junioratoche.model.user.validation.ports.EmailUniquenessPort;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EmailUniquenessAdapter implements EmailUniquenessPort {
    private final UserReactiveRepository repository;

    @Override
    public Mono<Boolean> exists(String email) {
        return repository.existsByEmail(email);
    }
}
