package pe.com.junioratoche.model.user.validation.rules;

import pe.com.junioratoche.model.shared.error.ErrorDetail;
import pe.com.junioratoche.model.user.validation.provider.AnnotationRuleProvider;
import reactor.core.publisher.Flux;

import java.lang.reflect.Field;
import java.util.Collection;

public final class NestedAnnotationRule<T> implements Rule<T> {
    private final Field field;

    public NestedAnnotationRule(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    @Override public String name() { return "Nested(" + field.getName() + ")"; }

    @Override
    public Flux<ErrorDetail> validate(T root) {
        final Object value;
        try {
            value = field.get(root);
        } catch (IllegalAccessException e) {
            return Flux.error(e);
        }
        if (value == null) return Flux.empty();

        if (value instanceof Collection<?> col) {
            return Flux.fromIterable(col)
                    .flatMap(NestedAnnotationRule::validateDynamically);
        }
        return validateDynamically(value);
    }

    @SuppressWarnings("unchecked")
    private static <X> Flux<ErrorDetail> validateDynamically(Object obj) {
        X value = (X) obj;
        Class<X> type = (Class<X>) value.getClass();

        AnnotationRuleProvider<X> provider = new AnnotationRuleProvider<>(type);
        return Flux.fromIterable(provider.rulesForCreate())
                .flatMap(rule -> rule.validate(value));
    }
}
