package pe.com.junioratoche.model.user.validation;

import lombok.RequiredArgsConstructor;
import pe.com.junioratoche.model.shared.error.DomainErrors;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.rules.Rule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Validador reactivo para entidades User usando reglas personalizadas.
 */
@RequiredArgsConstructor
public final class ReactiveValidator {
    private final List<Rule<User>> rules;

    /**
     * Valida el objeto User usando las reglas configuradas.
     *
     * @param target objeto User a validar
     * @return Mono<User> con el objeto validado o error agregado
     */
    public Mono<User> validate(final User target) {
        Mono<User> subject = Mono.just(target);

        Flux<ErrorDetail> errorFlux = Flux.merge(
            rules.stream()
                .map(rule -> subject.flatMapMany(rule::validate))
                .toList()
        );

        return errorFlux
            .collectList()
            .flatMap(errors -> errors.isEmpty()
                ? Mono.just(target)
                : Mono.error(DomainErrors.aggregated(errors)));
    }
}
