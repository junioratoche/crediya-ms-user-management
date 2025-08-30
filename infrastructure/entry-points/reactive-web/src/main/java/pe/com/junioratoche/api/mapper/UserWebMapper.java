package pe.com.junioratoche.api.mapper;

import pe.com.junioratoche.api.dto.UserRequestDTO;
import pe.com.junioratoche.api.dto.UserResponseDTO;
import pe.com.junioratoche.model.rol.Rol;
import pe.com.junioratoche.model.shared.error.DomainErrors;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.user.User;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Mapper utilitario para convertir entre DTOs web y entidades de dominio User.
 */
public final class UserWebMapper {
    private UserWebMapper() {}

    /**
     * Convierte un UserRequestDTO en un User de dominio.
     * @param dto DTO recibido en la petición web
     * @return User de dominio
     * @throws DomainErrors si la validación falla
     */
    public static User toDomain(UserRequestDTO dto) {
        if (dto == null) {
            throw DomainErrors.of(ErrorCode.VALIDATION_FAILED, "request body is null");
        }
        Rol rol = toRol(dto.getRoleId());
        return User.builder()
                .firstName(transform(dto.getFirstName(), String::trim))
                .lastName(transform(dto.getLastName(), String::trim))
                .email(transform(dto.getEmail(), s -> lower(trim(s))))
                .documentNumber(transform(dto.getDocumentNumber(), String::trim))
                .phoneNumber(transform(dto.getPhoneNumber(), String::trim))
                .salary(dto.getSalary())
                .role(rol)
                .build();
    }

    /**
     * Convierte un User de dominio en un UserResponseDTO para respuesta web.
     * @param u User de dominio
     * @return UserResponseDTO para la respuesta
     */
    public static UserResponseDTO toResponse(User u) {
        return UserResponseDTO.builder()
                .id(Optional.ofNullable(u.getId()).map(UUID::toString).orElse(null))
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .documentNumber(u.getDocumentNumber())
                .phoneNumber(u.getPhoneNumber())
                .roleId(Optional.ofNullable(u.getRole())
                        .map(Rol::getId)
                        .map(UUID::toString)
                        .orElse(null))
                .salary(u.getSalary())
                .build();
    }

    /**
     * Valida y convierte el roleId recibido en el DTO a un objeto Rol.
     * @param roleId Cadena UUID del rol
     * @return Rol o null si no se provee un roleId válido
     * @throws DomainErrors si el UUID es inválido
     */
    private static Rol toRol(String roleId) {
        return Optional.ofNullable(roleId)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(raw -> {
                    try {
                        return Rol.builder().id(UUID.fromString(raw)).build();
                    } catch (IllegalArgumentException ex) {
                        throw DomainErrors.ofField(ErrorCode.USER_ROLE_ID_INVALID, "roleId", "Invalid UUID");
                    }
                })
                .orElse(null);
    }

    /**
     * Aplica una transformación funcional a una cadena, retorna null si la entrada es null.
     */
    private static String transform(String s, Function<String, String> fn) {
        return s == null ? null : fn.apply(s);
    }

    /**
     * Convierte una cadena a minúsculas, retorna null si la entrada es null.
     */
    private static String lower(String s) { return s == null ? null : s.toLowerCase(); }

    /**
     * Elimina espacios en blanco de una cadena, retorna null si la entrada es null.
     */
    private static String trim(String s) { return s == null ? null : s.trim(); }
}
