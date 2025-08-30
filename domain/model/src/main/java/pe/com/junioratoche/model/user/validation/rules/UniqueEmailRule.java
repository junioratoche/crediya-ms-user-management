package pe.com.junioratoche.model.user.validation.rules;

import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.ports.EmailUniquenessPort;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public final class UniqueEmailRule implements Rule<User> {
    private final String field;
    private final Function<User, String> getter;
    private final EmailUniquenessPort port;
    private final ErrorCode code;

    public UniqueEmailRule(String field, Function<User, String> getter,
                           EmailUniquenessPort port, ErrorCode code) {
        this.field = field; this.getter = getter; this.port = port; this.code = code;
    }

    @Override public String name() { return "UniqueEmailRule(" + field + ")"; }

    @Override public Flux<ErrorDetail> validate(User u) {
        String email = getter.apply(u);
        if (email == null || email.isBlank()) return Flux.empty();
        return port.exists(email)
                .filter(Boolean::booleanValue)
                .map(__ -> new ErrorDetail(code.code(), field, code.message()))
                .flux();
    }
}
