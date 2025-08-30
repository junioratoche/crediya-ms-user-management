package pe.com.junioratoche.model.rol;
import lombok.*;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import pe.com.junioratoche.model.user.validation.annotations.Required;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Rol {
    @Required(code = ErrorCode.USER_ROLE_ID_REQUIRED, field = "roleId")
    private UUID id;
    private String name;
    private String description;
}
