package pe.com.junioratoche.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.junioratoche.api.dto.UserRequestDTO;
import pe.com.junioratoche.api.mapper.UserWebMapper;
import pe.com.junioratoche.model.shared.error.DomainErrors;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.usecase.registeruser.RegisterUserUseCase;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class Handler {
    private final RegisterUserUseCase useCase;
    private final UserWebMapper mapper = new UserWebMapper();

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest req) {
        return req.bodyToMono(UserRequestDTO.class)
                .onErrorResume(ex -> {
                    Throwable cause = ex.getCause();
                    if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException ife) {
                        boolean isSalary = ife.getPath().stream()
                                .anyMatch(ref -> "salary".equals(ref.getFieldName()));
                        if (isSalary) {
                            return Mono.error(DomainErrors.of(ErrorCode.USER_SALARY_INVALID));
                        }
                    }
                    return Mono.error(DomainErrors.of(ErrorCode.VALIDATION_FAILED, "invalid request body"));
                })
                .map(mapper::toDomain)
                .flatMap(useCase::saveUser)
                .map(mapper::toResponse)
                .flatMap(dto -> ServerResponse.created(URI.create(req.path()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

}
