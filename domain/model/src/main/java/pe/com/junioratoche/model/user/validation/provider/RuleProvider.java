package pe.com.junioratoche.model.user.validation.provider;

import pe.com.junioratoche.model.user.validation.rules.Rule;
import java.util.List;

public interface RuleProvider<T> {
    List<Rule<T>> rulesForCreate();
}
