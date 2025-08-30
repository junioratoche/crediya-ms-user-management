package pe.com.junioratoche.model.shared.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorDetailTest {
    @Test
    void testErrorDetailGettersAndSetters() {
        ErrorDetail detail = new ErrorDetail("code", "field", "message");
        assertEquals("field", detail.getField());
        assertEquals("message", detail.getMessage());
    }

    @Test
    void testErrorDetailEqualsAndHashCode() {
        ErrorDetail d1 = new ErrorDetail("code", "field", "msg");
        ErrorDetail d2 = new ErrorDetail("code", "field", "msg");
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void testErrorDetailWithNullValues() {
        ErrorDetail detail1 = new ErrorDetail(null, null, null);
        ErrorDetail detail2 = new ErrorDetail(null, null, null);
        assertEquals(detail1, detail2);
        assertEquals(detail1.hashCode(), detail2.hashCode());
        assertNull(detail1.getField());
        assertNull(detail1.getMessage());
    }

    @Test
    void testErrorDetailWithEmptyValues() {
        ErrorDetail detail = new ErrorDetail("", "", "");
        assertEquals("", detail.getField());
        assertEquals("", detail.getMessage());
    }

    @Test
    void testErrorDetailToString() {
        ErrorDetail detail = new ErrorDetail("code", "field", "message");
        String str = detail.toString();
        assertTrue(str.contains("code"));
        assertTrue(str.contains("field"));
        assertTrue(str.contains("message"));
    }
}
