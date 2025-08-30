package pe.com.junioratoche.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String documentNumber;
    private String phoneNumber;
    private String roleId;
    private BigDecimal salary;
}
