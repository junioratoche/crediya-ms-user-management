package pe.com.junioratoche.model.shared.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorCodeTest {
    @Test
    void testErrorCodeValues() {
        ErrorCode code = ErrorCode.USER_EMAIL_REQUIRED;
        assertEquals("USR_003", code.code());
        assertEquals(400, code.statusCode());
        assertEquals("email is required", code.message());
    }

    @Test
    void testAllErrorCodesAreUnique() {
        long uniqueCodes = java.util.Arrays.stream(ErrorCode.values())
                .map(ErrorCode::code)
                .distinct()
                .count();
        assertEquals(ErrorCode.values().length, uniqueCodes);
    }

    @Test
    void testAllErrorCodeMethods() {
        for (ErrorCode code : ErrorCode.values()) {
            assertNotNull(code.code());
            assertTrue(code.statusCode() > 0);
            assertNotNull(code.message());
        }
    }
}
