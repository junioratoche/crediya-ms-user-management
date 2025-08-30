package pe.com.junioratoche.usecase.registeruser;

import lombok.RequiredArgsConstructor;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.gateways.UserRepository;

import pe.com.junioratoche.model.user.validation.ReactiveValidator;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final ReactiveValidator<User> userCreateValidator;

    public Mono<User> saveUser(User user) {
      return userCreateValidator.validate(user)
        .flatMap(userRepository::save);
    }
}
