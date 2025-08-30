package pe.com.junioratoche.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.com.junioratoche.api.error.GlobalErrorHandler;

@Configuration
public class ErrorHandlerConfig {

    @Bean(name = "errorWebExceptionHandler")
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectMapper om) {
        return new GlobalErrorHandler(om);
    }
}
