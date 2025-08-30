package pe.com.junioratoche.usecase.registeruser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.gateways.UserRepository;
import pe.com.junioratoche.model.user.validation.ReactiveValidator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterUserUseCaseTest {
    private UserRepository userRepository;
    private ReactiveValidator userCreateValidator;
    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userCreateValidator = mock(ReactiveValidator.class);
        useCase = new RegisterUserUseCase(userRepository, userCreateValidator);
    }

    @Test
    void saveUser_validUser_success() {
        User user = new User();
        when(userCreateValidator.validate(any(User.class))).thenReturn(Mono.just(user));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        Mono<User> result = useCase.saveUser(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        verify(userCreateValidator).validate(user);
        verify(userRepository).save(user);
    }

    @Test
    void saveUser_invalidUser_error() {
        User user = new User();
        RuntimeException error = new RuntimeException("Validation failed");
        when(userCreateValidator.validate(any(User.class))).thenReturn(Mono.error(error));

        Mono<User> result = useCase.saveUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e.getMessage().equals("Validation failed"))
                .verify();

        verify(userCreateValidator).validate(user);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveUser_nullUser_error() {
        when(userCreateValidator.validate(null)).thenReturn(Mono.error(new NullPointerException("User is null")));
        Mono<User> result = useCase.saveUser(null);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof NullPointerException && e.getMessage().equals("User is null"))
                .verify();
        verify(userCreateValidator).validate(null);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveUser_repositoryError() {
        User user = new User();
        RuntimeException repoError = new RuntimeException("Repository error");
        when(userCreateValidator.validate(any(User.class))).thenReturn(Mono.just(user));
        when(userRepository.save(any(User.class))).thenReturn(Mono.error(repoError));

        Mono<User> result = useCase.saveUser(user);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e.getMessage().equals("Repository error"))
                .verify();

        verify(userCreateValidator).validate(user);
        verify(userRepository).save(user);
    }

    @Test
    void saveUser_validatorReturnsEmpty() {
        User user = new User();
        when(userCreateValidator.validate(any(User.class))).thenReturn(Mono.empty());

        Mono<User> result = useCase.saveUser(user);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(userCreateValidator).validate(user);
        verify(userRepository, never()).save(any(User.class));
    }
}
