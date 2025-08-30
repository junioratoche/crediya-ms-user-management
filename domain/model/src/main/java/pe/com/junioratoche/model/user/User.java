package pe.com.junioratoche.model.user;

import lombok.*;
import pe.com.junioratoche.model.rol.Rol;
import pe.com.junioratoche.model.user.validation.annotations.*;
import pe.com.junioratoche.model.shared.error.ErrorCode;
import static pe.com.junioratoche.model.user.validation.provider.UserValidationConstants.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class User {

    private UUID id;
    @Required(code = ErrorCode.USER_FIRST_NAME_REQUIRED, field = FIRST_NAME_FIELD)
    private String firstName;

    @Required(code = ErrorCode.USER_LAST_NAME_REQUIRED, field = LAST_NAME_FIELD)
    private String lastName;

    @Required(code = ErrorCode.USER_EMAIL_REQUIRED, field = EMAIL_FIELD)
    @EmailFormat(code = ErrorCode.USER_EMAIL_INVALID)
    private String email;

    private String documentNumber;
    private String phoneNumber;

    @Required(code = ErrorCode.USER_SALARY_REQUIRED, field = SALARY_FIELD)
    @Range(min = SALARY_MIN_STRING, max = SALARY_MAX_STRING, code = ErrorCode.USER_SALARY_OUT_OF_RANGE, field = SALARY_FIELD)
    private BigDecimal salary;

    @Required(code = ErrorCode.USER_ROLE_ID_REQUIRED, field = ROLE_ID_FIELD)
    @ValidateNested
    private Rol role;
}
