package pe.com.junioratoche.r2dbc.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.r2dbc.entity.UserEntity;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    @Mapping(target = "role.id", source = "roleId")
    User toDomain(UserEntity entity);

    @InheritInverseConfiguration
    UserEntity toEntity(User user);
}
