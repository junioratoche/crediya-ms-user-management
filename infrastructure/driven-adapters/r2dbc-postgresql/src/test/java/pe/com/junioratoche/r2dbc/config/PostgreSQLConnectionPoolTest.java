package pe.com.junioratoche.r2dbc.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class PostgreSQLConnectionPoolTest {

    @InjectMocks
    private PostgreSQLConnectionPool connectionPool;

    @Mock
    private PostgresqlConnectionProperties properties;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(properties.host()).thenReturn("localhost");
        when(properties.port()).thenReturn(5432);
        when(properties.database()).thenReturn("authentication");
        when(properties.schema()).thenReturn("public");
        when(properties.username()).thenReturn("postgres");
        when(properties.password()).thenReturn("postgres");
    }

    @Test
    void getConnectionConfigSuccess() {
        assertNotNull(connectionPool.getConnectionConfig(properties));
    }
}
