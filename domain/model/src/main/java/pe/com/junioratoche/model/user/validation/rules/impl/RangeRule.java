package pe.com.junioratoche.model.user.validation.rules.impl;

import lombok.RequiredArgsConstructor;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import pe.com.junioratoche.model.user.validation.rules.Rule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.function.Function;

import static pe.com.junioratoche.model.user.validation.provider.UserValidationConstants.RANGE_RULE_NAME_FORMAT;

@RequiredArgsConstructor
public final class RangeRule<T> implements Rule<T> {
    private final String field;
    private final Function<T, BigDecimal> getter;
    private final BigDecimal min;
    private final BigDecimal max;
    private final ErrorCode code;

    @Override public String name() { return String.format(RANGE_RULE_NAME_FORMAT, field); }

    @Override
    public Flux<ErrorDetail> validate(T target) {
      return Mono.justOrEmpty(getter.apply(target))
        .filter(value -> (min != null && value.compareTo(min) < 0) || (max != null && value.compareTo(max) > 0))
        .map(value -> new ErrorDetail(code.code(), field, code.message()))
        .flux();
    }
}
