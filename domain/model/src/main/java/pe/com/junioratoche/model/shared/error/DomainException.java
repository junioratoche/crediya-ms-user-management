package pe.com.junioratoche.model.shared.error;

import lombok.Getter;

import java.util.Map;

@Getter
public class DomainException extends RuntimeException {

    private final String code;
    private final int httpStatus;
    private final Map<String, Object> metadata;

    public DomainException(String code, String message, int httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.metadata = null;
    }

    public DomainException(String code, String message, int httpStatus, Map<String, Object> metadata) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.metadata = metadata;
    }
}
