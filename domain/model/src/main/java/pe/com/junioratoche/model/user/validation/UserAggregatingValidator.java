package pe.com.junioratoche.model.user.validation;

import pe.com.junioratoche.model.shared.error.*;
import pe.com.junioratoche.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

public class UserAggregatingValidator {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Mono<User> validateForCreate(User u) {
        List<Mono<ErrorDetail>> checks = List.of(
                required(u.getFirstName(), "firstName", ErrorCode.USER_FIRST_NAME_REQUIRED),
                required(u.getLastName(),  "lastName",  ErrorCode.USER_LAST_NAME_REQUIRED),
                required(u.getEmail(),     "email",     ErrorCode.USER_EMAIL_REQUIRED),
                required(u.getSalary(),    "salary",    ErrorCode.USER_SALARY_REQUIRED),
                emailFormat(u.getEmail()),
                salaryRange(u.getSalary()),
                roleIdRequired(u)
        );

        return Flux.merge(checks)
                .collectList()
                .flatMap(errors -> errors.isEmpty()
                        ? Mono.just(u)
                        : Mono.error(DomainErrors.aggregated(errors)));
    }

    private Mono<ErrorDetail> required(Object v, String field, ErrorCode code) {
        return (v == null || (v instanceof String s && s.trim().isEmpty()))
                ? Mono.just(new ErrorDetail(code.code(), field, code.message()))
                : Mono.empty();
    }

    private Mono<ErrorDetail> emailFormat(String email) {
        if (email == null || email.isBlank()) return Mono.empty();
        return EMAIL.matcher(email).matches()
                ? Mono.empty()
                : Mono.just(new ErrorDetail(ErrorCode.USER_EMAIL_INVALID.code(), "email", ErrorCode.USER_EMAIL_INVALID.message()));
    }

    private Mono<ErrorDetail> salaryRange(BigDecimal s) {
        if (s == null) return Mono.empty();
        boolean out = s.compareTo(BigDecimal.ZERO) < 0 || s.compareTo(new BigDecimal("15000000")) > 0;
        return out
                ? Mono.just(new ErrorDetail(ErrorCode.USER_SALARY_OUT_OF_RANGE.code(), "salary", ErrorCode.USER_SALARY_OUT_OF_RANGE.message()))
                : Mono.empty();
    }

    private Mono<ErrorDetail> roleIdRequired(User u) {
        boolean missing = u.getRole() == null || u.getRole().getId() == null;
        return missing
                ? Mono.just(new ErrorDetail(ErrorCode.USER_ROLE_ID_REQUIRED.code(), "roleId", ErrorCode.USER_ROLE_ID_REQUIRED.message()))
                : Mono.empty();
    }
}
