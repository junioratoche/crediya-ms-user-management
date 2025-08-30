package pe.com.junioratoche.model.user.validation.provider;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import static lombok.AccessLevel.PRIVATE;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.ports.EmailUniquenessPort;
import pe.com.junioratoche.model.user.validation.rules.impl.EmailFormatRule;
import pe.com.junioratoche.model.user.validation.rules.impl.RequiredRule;
import pe.com.junioratoche.model.user.validation.rules.Rule;
import pe.com.junioratoche.model.user.validation.rules.impl.UniqueEmailRule;
import pe.com.junioratoche.model.user.validation.rules.impl.RangeRule;

import java.util.List;

import static pe.com.junioratoche.model.user.validation.provider.UserValidationConstants.*;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public final class UserCreateRuleProvider implements RuleProvider<User> {
    EmailUniquenessPort emailPort;

    @Override
    public List<Rule<User>> rulesForCreate() {
        return List.of(
            new RequiredRule<>(EMAIL_FIELD, User::getEmail, ErrorCode.USER_EMAIL_REQUIRED),
            new EmailFormatRule(EMAIL_FIELD, ErrorCode.USER_EMAIL_INVALID),
            new UniqueEmailRule(EMAIL_FIELD, emailPort, ErrorCode.USER_EMAIL_ALREADY_USED),
            new RequiredRule<>(SALARY_FIELD, User::getSalary, ErrorCode.USER_SALARY_REQUIRED),
            new RangeRule<>(SALARY_FIELD, User::getSalary, SALARY_MIN, SALARY_MAX, ErrorCode.USER_SALARY_OUT_OF_RANGE)
        );
    }
}
