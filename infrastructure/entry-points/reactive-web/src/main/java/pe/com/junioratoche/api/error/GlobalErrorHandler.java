package pe.com.junioratoche.api.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import pe.com.junioratoche.model.shared.error.DomainErrors;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Manejador global de errores para aplicaciones WebFlux.
 * Convierte excepciones de negocio en respuestas JSON estructuradas.
 */
public final class GlobalErrorHandler implements ErrorWebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);
    private final ObjectMapper om;

    public GlobalErrorHandler(ObjectMapper om) {
        this.om = om;
    }

    /**
     * Orquesta el manejo global de errores en WebFlux.
     */
    @Override
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        var response = exchange.getResponse();
        if (response.isCommitted()) return Mono.error(ex);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (ex instanceof DomainErrors.Aggregated agg) {
            logValidationError(exchange, agg);
            return writeValidationErrors(response, agg.getErrors());
        }

        logUnhandledError(exchange, ex);
        return writeInternalError(response, ErrorJsonUtil.safeMessage(ex));
    }

    /**
     * Logging de errores de validación.
     */
    private void logValidationError(ServerWebExchange exchange, DomainErrors.Aggregated agg) {
        log.warn("Validation errors on {} {} -> {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                agg.getErrors());
    }

    /**
     * Logging de errores no controlados.
     */
    private void logUnhandledError(ServerWebExchange exchange, Throwable ex) {
        log.error("Unhandled error on {} {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(), ex);
    }

    /**
     * Escribe los errores de validación en la respuesta HTTP.
     */
    private Mono<Void> writeValidationErrors(org.springframework.http.server.reactive.ServerHttpResponse response, List<ErrorDetail> errors) {
        byte[] bytes = ErrorJsonUtil.serializeOrFallback(ErrorBodyBuilder.buildErrorBody(errors), "serialization error", om);
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    /**
     * Escribe un error interno en la respuesta HTTP.
     */
    private Mono<Void> writeInternalError(org.springframework.http.server.reactive.ServerHttpResponse response, String message) {
        byte[] bytes = ErrorJsonUtil.fallbackJson(message);
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}
