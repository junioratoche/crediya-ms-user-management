package pe.com.junioratoche.api.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class UserResponseDTO {
    String id;
    String firstName;
    String lastName;
    String email;
    String documentNumber;
    String phoneNumber;
    String roleId;
    BigDecimal salary;
}
