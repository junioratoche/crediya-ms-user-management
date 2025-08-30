package pe.com.junioratoche.model.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class UserTest {
    @Test
    void testUserGettersAndSetters() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        user.setFirstName("Juan");
        user.setLastName("Perez");
        user.setEmail("juan@correo.com");
        user.setSalary(new java.math.BigDecimal("1000"));
        user.setRole(null);
        assertEquals(uuid, user.getId());
        assertEquals("Juan", user.getFirstName());
        assertEquals("Perez", user.getLastName());
        assertEquals("juan@correo.com", user.getEmail());
        assertEquals(new java.math.BigDecimal("1000"), user.getSalary());
        assertNull(user.getRole());
    }

    @Test
    void testUserEqualsAndHashCode() {
        UUID uuid = UUID.randomUUID();
        User user1 = new User();
        user1.setId(uuid);
        User user2 = new User();
        user2.setId(uuid);
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
