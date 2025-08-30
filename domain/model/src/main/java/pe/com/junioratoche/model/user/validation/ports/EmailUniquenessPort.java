package pe.com.junioratoche.model.user.validation.ports;

import reactor.core.publisher.Mono;

public interface EmailUniquenessPort {
    Mono<Boolean> exists(String email);
}
