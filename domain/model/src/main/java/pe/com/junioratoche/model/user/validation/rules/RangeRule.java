package pe.com.junioratoche.model.user.validation.rules;

import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.function.Function;

public final class RangeRule<T> implements Rule<T> {
    private final String field;
    private final Function<T, BigDecimal> getter;
    private final BigDecimal min, max;
    private final ErrorCode code;

    public RangeRule(String field, Function<T, BigDecimal> getter, BigDecimal min, BigDecimal max, ErrorCode code) {
        this.field = field; this.getter = getter; this.min = min; this.max = max; this.code = code;
    }

    @Override public String name() { return "RangeRule(" + field + ")"; }

    @Override public Flux<ErrorDetail> validate(T t) {
        return Mono.fromCallable(() -> {
                    BigDecimal v = getter.apply(t);
                    if (v == null) return null;
                    boolean out = (min != null && v.compareTo(min) < 0) || (max != null && v.compareTo(max) > 0);
                    return out ? new ErrorDetail(code.code(), field, code.message()) : null;
                })
                .flatMapMany(Mono::justOrEmpty);
    }
}
