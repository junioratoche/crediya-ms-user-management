package pe.com.junioratoche.model.user.validation.rules;

import pe.com.junioratoche.model.shared.error.ErrorDetail;
import reactor.core.publisher.Flux;

public interface Rule<T> {
    String name();
    Flux<ErrorDetail> validate(T target);
}
