package pe.com.junioratoche.model.user.validation.rules.impl;

import lombok.RequiredArgsConstructor;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import pe.com.junioratoche.model.user.validation.rules.Rule;
import reactor.core.publisher.Flux;

import java.util.function.Function;

import static pe.com.junioratoche.model.user.validation.provider.UserValidationConstants.REQUIRED_RULE_NAME_FORMAT;

@RequiredArgsConstructor
public final class RequiredRule<T> implements Rule<T> {
    private final String field;
    private final Function<T, ?> getter;
    private final ErrorCode code;

    @Override public String name() { return String.format(REQUIRED_RULE_NAME_FORMAT, field); }

    @Override
    public Flux<ErrorDetail> validate(T target) {
        Object value = getter.apply(target);
        boolean isMissing = (value == null) || (value instanceof String str && str.trim().isEmpty());
        if (isMissing) {
            return Flux.just(new ErrorDetail(code.code(), field, code.message()));
        }
        return Flux.empty();
    }
}
