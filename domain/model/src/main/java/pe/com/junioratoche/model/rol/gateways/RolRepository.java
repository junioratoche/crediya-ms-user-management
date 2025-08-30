package pe.com.junioratoche.model.rol.gateways;

import pe.com.junioratoche.model.rol.Rol;
import reactor.core.publisher.Mono;

public interface RolRepository {
    Mono<Rol> findById(String id);
}
