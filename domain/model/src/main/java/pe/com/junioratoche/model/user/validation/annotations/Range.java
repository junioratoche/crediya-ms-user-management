package pe.com.junioratoche.model.user.validation.annotations;

import pe.com.junioratoche.model.shared.error.ErrorCode;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Range {
    String min() default "";
    String max() default "";
    ErrorCode code();
    String field() default "";
}
