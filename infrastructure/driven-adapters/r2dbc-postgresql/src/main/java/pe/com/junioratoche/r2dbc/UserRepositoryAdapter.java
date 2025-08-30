package pe.com.junioratoche.r2dbc;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.com.junioratoche.model.rol.Rol;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.gateways.UserRepository;
import pe.com.junioratoche.r2dbc.entity.UserEntity;
import pe.com.junioratoche.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
 User,
 UserEntity,
 UUID,
 UserReactiveRepository
 > implements UserRepository {

    private final UserReactiveRepository repository;
    private final ObjectMapper mapper;

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> {
            User domain = mapper.map(entity, User.class);
            if (entity.getRoleId() != null) {
                domain.setRole(Rol.builder().id(entity.getRoleId()).build());
            }
            return domain;
        });
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<User> save(User user) {
        UserEntity entity = mapper.map(user, UserEntity.class);

        if (user.getRole() != null && user.getRole().getId() != null) {
            entity.setRoleId(user.getRole().getId());
        }

        return repository.save(entity)
           .map(saved -> {
               User mapped = mapper.map(saved, User.class);
               if (saved.getRoleId() != null) {
                   mapped.setRole(Rol.builder().id(saved.getRoleId()).build()); // <<< UUID
               }
               return mapped;
           });
    }
}
