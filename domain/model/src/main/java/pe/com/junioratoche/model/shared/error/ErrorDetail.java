package pe.com.junioratoche.model.shared.error;

import lombok.Value;

@Value
public class ErrorDetail {
    String code;
    String field;
    String message;
}
