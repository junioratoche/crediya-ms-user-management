package pe.com.junioratoche.model.shared.error;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {
    @Test
    void testConstructorAndGetMessage() {
        DomainException ex = new DomainException("CODE", "Error message", 400);
        assertEquals("Error message", ex.getMessage());
        assertEquals("CODE", ex.getCode());
        assertEquals(400, ex.getStatusCode());
        assertNull(ex.getMetadata());
    }

    @Test
    void testConstructorWithNullMessage() {
        DomainException ex = new DomainException("CODE", null, 400);
        assertNull(ex.getMessage());
        assertEquals("CODE", ex.getCode());
        assertEquals(400, ex.getStatusCode());
    }

    @Test
    void testConstructorWithNullCause() {
        DomainException ex = new DomainException("CODE", "Error", 400, null);
        assertEquals("Error", ex.getMessage());
        assertEquals("CODE", ex.getCode());
        assertEquals(400, ex.getStatusCode());
        assertNull(ex.getMetadata());
        assertNull(ex.getCause());
    }
}
