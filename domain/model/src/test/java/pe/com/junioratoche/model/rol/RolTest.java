package pe.com.junioratoche.model.rol;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RolTest {
    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        Rol rol = Rol.builder()
                .id(id)
                .name("Admin")
                .description("Administrador")
                .build();
        assertEquals(id, rol.getId());
        assertEquals("Admin", rol.getName());
        assertEquals("Administrador", rol.getDescription());
    }

    @Test
    void testSettersAndEquals() {
        UUID id = UUID.randomUUID();
        Rol rol1 = new Rol();
        rol1.setId(id);
        rol1.setName("User");
        rol1.setDescription("Usuario");
        Rol rol2 = new Rol();
        rol2.setId(id);
        rol2.setName("User");
        rol2.setDescription("Usuario");
        assertEquals(rol1, rol2);
        assertEquals(rol1.hashCode(), rol2.hashCode());
    }
}
