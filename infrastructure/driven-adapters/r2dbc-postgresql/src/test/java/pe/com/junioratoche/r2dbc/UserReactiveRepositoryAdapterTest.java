package pe.com.junioratoche.r2dbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.com.junioratoche.model.rol.Rol;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.r2dbc.entity.UserEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    private UserRepositoryAdapter repositoryAdapter;

    @Mock
    private UserReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private static final String ROLE_ID_STR = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
    private static final UUID ROLE_ID = UUID.fromString(ROLE_ID_STR);
    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    // Lo que produce el mapper desde el dominio (sin roleId para verificar que el adapter lo setea)
    private final UserEntity entityToSaveWithoutRoleId = UserEntity.builder()
            .id(USER_ID)                       // <-- UUID, no String
            .firstName("John")
            .lastName("Doe")
            .email("jdoe@gmail.com")
            .documentNumber("12345678")
            .phoneNumber("987654321")
            .salary(BigDecimal.valueOf(1500.0))
            .roleId(null)
            .build();

    // Entidad que retorna el repo tras persistir (roleId seteado como UUID)
    private final UserEntity persistedEntity = UserEntity.builder()
            .id(entityToSaveWithoutRoleId.getId())
            .firstName(entityToSaveWithoutRoleId.getFirstName())
            .lastName(entityToSaveWithoutRoleId.getLastName())
            .email(entityToSaveWithoutRoleId.getEmail())
            .documentNumber(entityToSaveWithoutRoleId.getDocumentNumber())
            .phoneNumber(entityToSaveWithoutRoleId.getPhoneNumber())
            .salary(entityToSaveWithoutRoleId.getSalary())
            .roleId(ROLE_ID)
            .build();

    // Dominio de entrada (User.id es UUID; Rol.id es UUID)
    private final User domainInputWithRole = User.builder()
            .id(USER_ID)
            .firstName("John")
            .lastName("Doe")
            .email("jdoe@gmail.com")
            .documentNumber("12345678")
            .phoneNumber("987654321")
            .salary(BigDecimal.valueOf(1500.0))
            .role(Rol.builder().id(ROLE_ID).name("Admin").build()) // <-- UUID
            .build();

    // El mapper NO reconstruye rol; el adapter lo hará desde roleId (persistedEntity.roleId)
    private final User mappedFromDbWithoutRole = domainInputWithRole.toBuilder()
            .role(null)
            .build();

    @Test
    @DisplayName("save Should Return Domain User With Role Bridged And Preserve Doc/Phone")
    void saveShouldReturnDomainUserWithRoleBridgedAndPreserveDocPhone() {
        // domain -> entity (mapper)
        when(mapper.map(domainInputWithRole, UserEntity.class)).thenReturn(entityToSaveWithoutRoleId);

        // repo.save debe recibir una entidad con roleId = ROLE_ID (UUID)
        when(repository.save(argThat(e -> ROLE_ID.equals(e.getRoleId()))))
                .thenReturn(Mono.just(persistedEntity));

        // entity -> domain (mapper) SIN rol; el adapter lo reconstruye
        when(mapper.map(persistedEntity, User.class)).thenReturn(mappedFromDbWithoutRole);

        Mono<User> result = repositoryAdapter.save(domainInputWithRole);

        StepVerifier.create(result)
                .expectNextMatches(u ->
                        u.getId().equals(USER_ID) &&
                                u.getFirstName().equals("John") &&
                                u.getEmail().equals("jdoe@gmail.com") &&
                                u.getDocumentNumber().equals("12345678") &&
                                u.getPhoneNumber().equals("987654321") &&
                                u.getSalary().compareTo(BigDecimal.valueOf(1500.0)) == 0 &&
                                u.getRole() != null &&
                                ROLE_ID.equals(u.getRole().getId()) // <-- compara UUID con UUID
                )
                .verifyComplete();

        verify(repository, times(1))
                .save(argThat(e -> ROLE_ID.equals(e.getRoleId())));
    }

    @Test
    @DisplayName("save Should Propagate Error When Repository Fails")
    void saveShouldPropagateErrorWhenRepositoryFails() {
        when(mapper.map(domainInputWithRole, UserEntity.class)).thenReturn(entityToSaveWithoutRoleId);
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.error(new RuntimeException("DB down")));

        StepVerifier.create(repositoryAdapter.save(domainInputWithRole))
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().contains("DB down"))
                .verify();
    }
}
