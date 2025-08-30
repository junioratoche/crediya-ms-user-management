package pe.com.junioratoche.api.mapper;

import pe.com.junioratoche.api.dto.UserRequestDTO;
import pe.com.junioratoche.api.dto.UserResponseDTO;
import pe.com.junioratoche.model.rol.Rol;
import pe.com.junioratoche.model.shared.error.DomainErrors;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.user.User;

import java.util.UUID;

public class UserWebMapper {

    public User toDomain(UserRequestDTO dto) {
        Rol rol = null;
        if (dto.getRoleId() != null) {
            String raw = dto.getRoleId().trim();
            if (!raw.isEmpty()) {
                try {
                    rol = Rol.builder().id(UUID.fromString(raw)).build();
                } catch (IllegalArgumentException ex) {
                    throw DomainErrors.ofField(ErrorCode.USER_ROLE_ID_INVALID, "roleId", "Invalid UUID");
                }
            }
        }

        return User.builder()
                .firstName(trim(dto.getFirstName()))
                .lastName(trim(dto.getLastName()))
                .email(lower(trim(dto.getEmail())))
                .documentNumber(trim(dto.getDocumentNumber()))
                .phoneNumber(trim(dto.getPhoneNumber()))
                .salary(dto.getSalary())
                .role(rol)
                .build();
    }

    public UserResponseDTO toResponse(User u) {
        return UserResponseDTO.builder()
                .id(u.getId() != null ? u.getId().toString() : null)
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .documentNumber(u.getDocumentNumber())
                .phoneNumber(u.getPhoneNumber())
                .roleId(u.getRole() != null && u.getRole().getId() != null ? u.getRole().getId().toString() : null)
                .salary(u.getSalary())
                .build();
    }

    private static String trim(String s){ return s == null ? null : s.trim(); }
    private static String lower(String s){ return s == null ? null : s.toLowerCase(); }
}
