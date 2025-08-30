package pe.com.junioratoche.model.user.validation.rules.impl;

import lombok.RequiredArgsConstructor;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.rules.Rule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

import static pe.com.junioratoche.model.user.validation.provider.UserValidationConstants.EMAIL_FORMAT_RULE_NAME_FORMAT;
import static pe.com.junioratoche.model.user.validation.provider.UserValidationConstants.EMAIL_PATTERN;

@RequiredArgsConstructor
public final class EmailFormatRule implements Rule<User> {
    private static final Pattern EMAIL = Pattern.compile(EMAIL_PATTERN);
    private final String field;
    private final ErrorCode codeInvalid;

    @Override
    public String name() {
        return String.format(EMAIL_FORMAT_RULE_NAME_FORMAT, field);
    }

    @Override
    public Flux<ErrorDetail> validate(User user) {
      return Mono.justOrEmpty(user.getEmail())
        .filter(email -> !email.isBlank())
        .filter(email -> !EMAIL.matcher(email).matches())
        .map(email -> new ErrorDetail(codeInvalid.code(), field, codeInvalid.message()))
        .flux();
    }
}
