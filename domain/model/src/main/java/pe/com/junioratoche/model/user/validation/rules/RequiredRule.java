package pe.com.junioratoche.model.user.validation.rules;

import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public final class RequiredRule<T> implements Rule<T> {
    private final String field;
    private final Function<T, ?> getter;
    private final ErrorCode code;

    public RequiredRule(String field, Function<T, ?> getter, ErrorCode code) {
        this.field = field; this.getter = getter; this.code = code;
    }

    @Override public String name() { return "RequiredRule(" + field + ")"; }

    @Override public Flux<ErrorDetail> validate(T t) {
        return Mono.fromCallable(() -> {
                    Object v = getter.apply(t);
                    boolean missing = (v == null) || (v instanceof String s && s.trim().isEmpty());
                    return missing ? new ErrorDetail(code.code(), field, code.message()) : null;
                })
                .flatMapMany(Mono::justOrEmpty);
    }
}
