package pe.com.junioratoche.model.user.validation;

import pe.com.junioratoche.model.shared.error.DomainErrors;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import pe.com.junioratoche.model.user.validation.provider.RuleProvider;
import pe.com.junioratoche.model.user.validation.rules.Rule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ReactiveValidator<T> {

    private final Supplier<List<Rule<T>>> rulesSupplier;
    private final int concurrency;

    public ReactiveValidator(Supplier<List<Rule<T>>> rulesSupplier, int concurrency) {
        this.rulesSupplier = rulesSupplier;
        this.concurrency = Math.max(1, concurrency);
    }

    public Mono<T> validate(T target) {
        Mono<T> subject = Mono.just(target).cache();

        // List<Flux<ErrorDetail>>
        var streams = rulesSupplier.get().stream()
                .<Flux<ErrorDetail>>map(rule ->
                        subject.flatMapMany(rule::validate)
                                .subscribeOn(Schedulers.parallel()))
                .toList();

        // Merguea con delayError, controlando concurrencia y prefetch
        return Flux.fromIterable(streams)
                .flatMapDelayError(Function.identity(), concurrency, 32)
                .collectList()
                .flatMap(errors -> errors.isEmpty()
                        ? Mono.just(target)
                        : Mono.error(DomainErrors.aggregated(errors)));
    }


}
