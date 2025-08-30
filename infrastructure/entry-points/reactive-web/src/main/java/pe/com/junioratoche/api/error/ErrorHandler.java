package pe.com.junioratoche.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import pe.com.junioratoche.model.shared.error.DomainException;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global de errores para la API reactiva.
 * Convierte DomainException en respuestas JSON estructuradas.
 */
public final class ErrorHandler {
    private ErrorHandler() {}

    /**
     * Filtro para transformar excepciones de dominio en respuestas HTTP JSON.
     * @return HandlerFilterFunction para usar en el router
     */
    public static HandlerFilterFunction<ServerResponse, ServerResponse> asFilter() {
        return (request, next) ->
            next.handle(request)
                .onErrorResume(ex -> {
                    DomainException de = unwrap(ex);
                    if (de == null) return Mono.error(ex);

                    HttpStatus status = resolveStatus(de);
                    Map<String, Object> body = buildErrorBody(de, status);

                    return ServerResponse.status(status)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(body));
                });
    }

    /**
     * Construye el cuerpo de la respuesta de error.
     */
    private static Map<String, Object> buildErrorBody(DomainException de, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ErrorConstants.CODE, de.getCode());
        body.put(ErrorConstants.MESSAGE, de.getMessage());
        body.put(ErrorConstants.STATUS, status.value());
        Object errors = (de.getMetadata() != null) ? de.getMetadata().get(ErrorConstants.ERRORS) : null;
        if (errors != null) {
            body.put(ErrorConstants.ERRORS, errors);
        }
        return body;
    }

    /**
     * Obtiene el HttpStatus a partir del DomainException.
     */
    private static HttpStatus resolveStatus(DomainException de) {
        return HttpStatus.valueOf(de.getStatusCode());
    }

    /**
     * Busca una DomainException en la cadena de causas de la excepción.
     * @param ex Excepción original
     * @return DomainException si existe, null si no
     */
    private static DomainException unwrap(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            if (current instanceof DomainException d) return d;
            current = current.getCause();
        }
        return null;
    }
}
