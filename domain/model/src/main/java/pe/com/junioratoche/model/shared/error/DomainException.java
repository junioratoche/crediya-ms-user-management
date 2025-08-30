package pe.com.junioratoche.model.shared.error;

import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

/**
 * Excepción de dominio personalizada para errores de negocio.
 */
@Getter
public class DomainException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String code;
    private final int statusCode;
    /**
     * Información adicional sobre el error. No se serializa.
     */
    private final transient Map<String, Object> metadata;

    /**
     * Constructor para excepción sin metadata.
     * @param code Código de error
     * @param message Mensaje descriptivo
     * @param statusCode Código HTTP
     */
    public DomainException(String code, String message, int statusCode) {
        super(message);
        this.code = code;
        this.statusCode = statusCode;
        this.metadata = null;
    }

    /**
     * Constructor para excepción con metadata.
     * @param code Código de error
     * @param message Mensaje descriptivo
     * @param statusCode Código HTTP
     * @param metadata Información adicional
     */
    public DomainException(String code, String message, int statusCode, Map<String, Object> metadata) {
        super(message);
        this.code = code;
        this.statusCode = statusCode;
        this.metadata = metadata;
    }
}
