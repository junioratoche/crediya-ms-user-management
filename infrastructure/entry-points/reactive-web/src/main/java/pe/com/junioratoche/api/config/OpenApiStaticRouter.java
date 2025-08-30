package pe.com.junioratoche.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class OpenApiStaticRouter {

    @Bean
    public RouterFunction<ServerResponse> openApiRoute() {
        var res = new ClassPathResource("static/openapi/user-management.yaml");

        return route(GET("/openapi/user-management.yaml"), req ->
                ServerResponse.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"user-management.yaml\"")
                        .contentType(MediaType.parseMediaType("text/plain; charset=UTF-8"))
                        .body(BodyInserters.fromResource(res))
        );
    }
}
