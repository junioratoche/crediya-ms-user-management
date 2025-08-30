package pe.com.junioratoche.model.user.validation.rules;

import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.regex.Pattern;

public final class EmailFormatRule<T> implements Rule<T> {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private final String field;
    private final Function<T, String> getter;
    private final ErrorCode codeInvalid;

    public EmailFormatRule(String field, Function<T, String> getter, ErrorCode codeInvalid) {
        this.field = field; this.getter = getter; this.codeInvalid = codeInvalid;
    }

    @Override public String name() { return "EmailFormatRule(" + field + ")"; }

    @Override public Flux<ErrorDetail> validate(T t) {
        return Mono.fromCallable(() -> {
                    String email = getter.apply(t);
                    if (email == null || email.isBlank()) return null;
                    return EMAIL.matcher(email).matches()
                            ? null
                            : new ErrorDetail(codeInvalid.code(), field, codeInvalid.message());
                })
                .flatMapMany(Mono::justOrEmpty);
    }
}
