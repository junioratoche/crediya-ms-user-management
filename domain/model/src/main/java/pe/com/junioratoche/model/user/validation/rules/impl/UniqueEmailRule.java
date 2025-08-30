package pe.com.junioratoche.model.user.validation.rules.impl;

import lombok.RequiredArgsConstructor;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.ports.EmailUniquenessPort;
import pe.com.junioratoche.model.user.validation.rules.Rule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static pe.com.junioratoche.model.user.validation.provider.UserValidationConstants.UNIQUE_EMAIL_RULE_NAME_FORMAT;

@RequiredArgsConstructor
public final class UniqueEmailRule implements Rule<User> {
    private final String field;
    private final EmailUniquenessPort port;
    private final ErrorCode code;

    @Override public String name() { return String.format(UNIQUE_EMAIL_RULE_NAME_FORMAT, field); }

    @Override
    public Flux<ErrorDetail> validate(User user) {
      return Mono.justOrEmpty(user.getEmail())
        .filter(email -> !email.isBlank())
        .flatMap(port::exists)
        .filter(Boolean::booleanValue)
        .map(exists -> new ErrorDetail(code.code(), field, code.message()))
        .flux();
    }
}
