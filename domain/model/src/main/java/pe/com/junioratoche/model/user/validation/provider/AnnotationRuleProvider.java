package pe.com.junioratoche.model.user.validation.provider;

import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.user.validation.annotations.*;
import pe.com.junioratoche.model.user.validation.rules.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationRuleProvider<T> implements RuleProvider<T> {

    private final Class<T> type;

    public AnnotationRuleProvider(Class<T> type) { this.type = type; }

    @Override
    public List<Rule<T>> rulesForCreate() {
        List<Rule<T>> rules = new ArrayList<>();
        for (Field f : type.getDeclaredFields()) {
            f.setAccessible(true);

            Required req = f.getAnnotation(Required.class);
            if (req != null) {
                ErrorCode code = req.code();
                rules.add(new RequiredRule<>(fieldName(f, req.field()), bean -> get(f, bean), code));
            }

            EmailFormat email = f.getAnnotation(EmailFormat.class);
            if (email != null) {
                rules.add(new EmailFormatRule<>(fieldName(f, email.field()), bean -> (String) get(f, bean), email.code()));
            }

            Range range = f.getAnnotation(Range.class);
            if (range != null) {
                var min = range.min().isBlank() ? null : new BigDecimal(range.min());
                var max = range.max().isBlank() ? null : new BigDecimal(range.max());
                rules.add(new RangeRule<>(fieldName(f, range.field()), bean -> (BigDecimal) get(f, bean),
                        min, max, range.code()));
            }

            if (f.isAnnotationPresent(ValidateNested.class)) {
                rules.add(new NestedAnnotationRule<>(f));
            }
        }
        return List.copyOf(rules);
    }

    private static Object get(Field f, Object bean) {
        try { return f.get(bean); } catch (IllegalAccessException e) { throw new IllegalStateException(e); }
    }
    private static String fieldName(Field f, String override) {
        return (override == null || override.isBlank()) ? f.getName() : override;
    }
}
