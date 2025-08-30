package pe.com.junioratoche.api.error;

import pe.com.junioratoche.model.shared.error.ErrorDetail;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utilidad para construir el cuerpo de error en respuestas JSON.
 */
public final class ErrorBodyBuilder {
    private ErrorBodyBuilder() {}

    public static Map<String, Object> buildErrorBody(List<ErrorDetail> errors) {
        List<Map<String, Object>> errs = errors.stream()
                .map(ErrorBodyBuilder::errorDetailToMap)
                .collect(Collectors.toList());
        return Map.of(ErrorConstants.ERRORS, errs);
    }

    public static Map<String, Object> errorDetailToMap(ErrorDetail e) {
        return Map.of(
                ErrorConstants.CODE, Optional.ofNullable(e.getCode()).orElse("UNKNOWN"),
                ErrorConstants.FIELD, Optional.ofNullable(e.getField()).orElse(ErrorConstants.GLOBAL_FIELD),
                ErrorConstants.MESSAGE, Optional.ofNullable(e.getMessage()).orElse("")
        );
    }
}

