package pe.com.junioratoche.api.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Utilidad para serializar errores y generar JSON de fallback.
 */
public final class ErrorJsonUtil {
    private static final Logger log = LoggerFactory.getLogger(ErrorJsonUtil.class);
    private ErrorJsonUtil() {}

    public static byte[] serializeOrFallback(Object obj, String fallbackMsg, ObjectMapper om) {
        try {
            return om.writeValueAsBytes(obj);
        } catch (Exception writeEx) {
            log.error("Error serializing error response", writeEx);
            return fallbackJson(fallbackMsg);
        }
    }

    public static byte[] fallbackJson(String message) {
        String json = String.format("{\"errors\":[{\"code\":\"INTERNAL\",\"field\":\"%s\",\"message\":\"%s\"}]}",
                ErrorConstants.GLOBAL_FIELD, message.replace("\"", "'"));
        return json.getBytes(StandardCharsets.UTF_8);
    }

    public static String safeMessage(Throwable ex) {
        String msg = ex.getMessage();
        return (msg == null || msg.isBlank()) ? "unexpected error" : msg.replace("\"", "'");
    }
}

