package pe.com.junioratoche.model.user.gateways;

import pe.com.junioratoche.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<Boolean> existsByEmail(String email);
    Mono<User> save(User user);
}