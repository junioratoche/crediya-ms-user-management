package pe.com.junioratoche.model.user.validation.provider;

import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.ports.EmailUniquenessPort;
import pe.com.junioratoche.model.user.validation.rules.Rule;
import pe.com.junioratoche.model.user.validation.rules.UniqueEmailRule;

import java.util.ArrayList;
import java.util.List;

public final class UserCreateRuleProvider implements RuleProvider<User> {
    private final RuleProvider<User> annotationProvider;
    private final EmailUniquenessPort emailPort;

    public UserCreateRuleProvider(EmailUniquenessPort emailPort) {
        this.annotationProvider = new AnnotationRuleProvider<>(User.class);
        this.emailPort = emailPort;
    }

    @Override
    public List<Rule<User>> rulesForCreate() {
        List<Rule<User>> rules = new ArrayList<>(annotationProvider.rulesForCreate());

        rules.add(new UniqueEmailRule("email", User::getEmail, emailPort, ErrorCode.USER_EMAIL_ALREADY_USED));
        return List.copyOf(rules);
    }
}
