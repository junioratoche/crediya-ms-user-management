package pe.com.junioratoche.model.user;

import lombok.*;
import pe.com.junioratoche.model.rol.Rol;
import pe.com.junioratoche.model.user.validation.annotations.*;
import pe.com.junioratoche.model.shared.error.ErrorCode;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    private UUID id;
    @Required(code = ErrorCode.USER_FIRST_NAME_REQUIRED, field = "firstName")
    private String firstName;

    @Required(code = ErrorCode.USER_LAST_NAME_REQUIRED, field = "lastName")
    private String lastName;

    @Required(code = ErrorCode.USER_EMAIL_REQUIRED, field = "email")
    @EmailFormat(code = ErrorCode.USER_EMAIL_INVALID)
    private String email;

    private String documentNumber;
    private String phoneNumber;

    @Required(code = ErrorCode.USER_SALARY_REQUIRED, field = "salary")
    @Range(min = "0", max = "15000000", code = ErrorCode.USER_SALARY_OUT_OF_RANGE, field = "salary")
    private BigDecimal salary;

    @Required(code = ErrorCode.USER_ROLE_ID_REQUIRED, field = "roleId")
    @ValidateNested
    private Rol role;
}
