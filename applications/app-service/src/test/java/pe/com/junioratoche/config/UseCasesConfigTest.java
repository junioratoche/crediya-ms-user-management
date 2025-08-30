package pe.com.junioratoche.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UseCasesConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testUseCaseBeansExist() {
        String[] beanNames = context.getBeanDefinitionNames();
        boolean useCaseBeanFound = false;
        for (String beanName : beanNames) {
            if (beanName.endsWith("UseCase")) {
                useCaseBeanFound = true;
                break;
            }
        }
        assertTrue(useCaseBeanFound, "No beans ending with 'UseCase' were found");
    }
}