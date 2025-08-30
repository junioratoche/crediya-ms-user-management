package pe.com.junioratoche.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "pe.com.junioratoche.r2dbc")
public class ReactiveRepositoriesConfig {
    // Configuración para habilitar los repositorios reactivos en el paquete r2dbc
}

