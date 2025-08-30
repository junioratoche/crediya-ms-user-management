package pe.com.junioratoche.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import pe.com.junioratoche.model.shared.error.DomainException;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorHandler {

    public static HandlerFilterFunction<ServerResponse, ServerResponse> asFilter() {
        return (request, next) ->
                next.handle(request)
                        .onErrorResume(ex -> {
                            DomainException de = unwrap(ex);
                            if (de == null) return Mono.error(ex);

                            HttpStatus status = HttpStatus.valueOf(de.getHttpStatus());

                            Map<String,Object> body = new LinkedHashMap<>();
                            body.put("code", de.getCode());
                            body.put("message", de.getMessage());
                            if (de.getMetadata() != null && de.getMetadata().get("errors") != null) {
                                body.put("errors", de.getMetadata().get("errors"));
                            }

                            return ServerResponse.status(status)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(BodyInserters.fromValue(body));
                        });
    }


    private static DomainException unwrap(Throwable ex) {
        if (ex instanceof DomainException d) return d;
        Throwable cause = ex.getCause();
        return (cause instanceof DomainException d2) ? d2 : null;
    }
}
