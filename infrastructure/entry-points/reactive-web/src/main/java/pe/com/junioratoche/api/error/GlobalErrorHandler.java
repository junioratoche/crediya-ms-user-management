package pe.com.junioratoche.api.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import pe.com.junioratoche.model.shared.error.DomainErrors;
import pe.com.junioratoche.model.shared.error.ErrorDetail;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);
    private final ObjectMapper om;

    public GlobalErrorHandler(ObjectMapper om) {
        this.om = om;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var response = exchange.getResponse();
        if (response.isCommitted()) return Mono.error(ex);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (ex instanceof DomainErrors.Aggregated agg) {
            log.warn("Validation errors on {} {} -> {}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI(),
                    agg.getErrors());

            List<Map<String, Object>> errs = toSimpleMaps(agg.getErrors());
            byte[] bytes;
            try {
                bytes = om.writeValueAsBytes(Map.of("errors", errs));
            } catch (Exception writeEx) {
                bytes = fallbackJson("serialization error");
            }
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
        }

        log.error("Unhandled error on {} {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(), ex);

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        byte[] bytes = fallbackJson(safeMessage(ex));
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    private static List<Map<String, Object>> toSimpleMaps(List<ErrorDetail> errors) {
        return errors.stream()
                .map(e -> Map.<String, Object>of(
                        "code", e.code(),
                        "field", e.field(),
                        "message", e.message()))
                .collect(Collectors.toList());
    }

    private static byte[] fallbackJson(String message) {
        String json = "{\"errors\":[{\"code\":\"INTERNAL\",\"field\":\"_global\",\"message\":\""
                + message.replace("\"", "'") + "\"}]}";
        return json.getBytes(StandardCharsets.UTF_8);
    }

    private static String safeMessage(Throwable ex) {
        String msg = ex.getMessage();
        return (msg == null || msg.isBlank()) ? "unexpected error" : msg.replace("\"", "'");
    }
}
