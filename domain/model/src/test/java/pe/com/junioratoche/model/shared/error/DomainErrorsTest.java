package pe.com.junioratoche.model.shared.error;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DomainErrorsTest {
    @Test
    void testOfGlobalError() {
        ErrorCode code = ErrorCode.USER_EMAIL_INVALID;
        DomainErrors.Aggregated agg = DomainErrors.of(code);
        ErrorDetail error = agg.getErrors().get(0);
        assertEquals(code.code(), error.getCode());
        assertEquals(DomainErrors.GLOBAL_FIELD, error.getField());
    }

    @Test
    void testOfGlobalErrorWithMessage() {
        ErrorCode code = ErrorCode.USER_EMAIL_INVALID;
        DomainErrors.Aggregated agg = DomainErrors.of(code, "Custom message");
        ErrorDetail error = agg.getErrors().get(0);
        assertEquals("Custom message", error.getMessage());
    }

    @Test
    void testOfFieldError() {
        ErrorCode code = ErrorCode.USER_EMAIL_INVALID;
        DomainErrors.Aggregated agg = DomainErrors.ofField(code, "email");
        ErrorDetail error = agg.getErrors().get(0);
        assertEquals("email", error.getField());
    }

    @Test
    void testOfFieldErrorWithNullField() {
        ErrorCode code = ErrorCode.USER_EMAIL_INVALID;
        DomainErrors.Aggregated agg = DomainErrors.ofField(code, null);
        ErrorDetail error = agg.getErrors().get(0);
        assertNull(error.getField());
    }

    @Test
    void testOfFieldErrorWithEmptyField() {
        ErrorCode code = ErrorCode.USER_EMAIL_INVALID;
        DomainErrors.Aggregated agg = DomainErrors.ofField(code, "");
        ErrorDetail error = agg.getErrors().get(0);
        assertEquals("", error.getField());
    }

    @Test
    void testOfFieldErrorWithCustomMessage() {
        ErrorCode code = ErrorCode.USER_EMAIL_INVALID;
        DomainErrors.Aggregated agg = DomainErrors.ofField(code, "email", "Mensaje personalizado");
        ErrorDetail error = agg.getErrors().get(0);
        assertEquals("Mensaje personalizado", error.getMessage());
        assertEquals("email", error.getField());
    }

    @Test
    void testAggregatedThrowsOnEmpty() {
        assertThrows(IllegalArgumentException.class, DomainErrorsTest::callAggregatedWithEmptyList);
    }

    private static void callAggregatedWithEmptyList() {
        DomainErrors.aggregated(List.of());
    }

    @Test
    void testMergeAggregated() {
        ErrorDetail e1 = new ErrorDetail("C1", "f1", "m1");
        ErrorDetail e2 = new ErrorDetail("C2", "f2", "m2");
        DomainErrors.Aggregated a1 = DomainErrors.of(e1);
        DomainErrors.Aggregated a2 = DomainErrors.of(e2);
        DomainErrors.Aggregated merged = DomainErrors.merge(a1, a2);
        assertEquals(2, merged.getErrors().size());
    }

    @Test
    void testBuilder() {
        ErrorDetail e1 = new ErrorDetail("C1", "f1", "m1");
        ErrorDetail e2 = new ErrorDetail("C2", "f2", "m2");
        DomainErrors.Builder builder = DomainErrors.builder();
        builder.add(e1).add(e2);
        DomainErrors.Aggregated agg = builder.build();
        assertEquals(2, agg.getErrors().size());
    }

    @Test
    void testBuilderWithoutErrors() {
        DomainErrors.Builder builder = DomainErrors.builder();
        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void testMergeWithEmptyAggregated() {
        ErrorDetail e1 = new ErrorDetail("C1", "f1", "m1");
        DomainErrors.Aggregated a1 = DomainErrors.of(e1);
        assertThrows(IllegalArgumentException.class, DomainErrorsTest::callMergeWithEmptyAggregated);
    }

    private static void callMergeWithEmptyAggregated() {
        DomainErrors.Aggregated a2 = DomainErrors.aggregated(List.of());
        ErrorDetail e1 = new ErrorDetail("C1", "f1", "m1");
        DomainErrors.Aggregated a1 = DomainErrors.of(e1);
        DomainErrors.merge(a1, a2);
    }

    @Test
    void testOfErrorDetailVarargs() {
        ErrorDetail e1 = new ErrorDetail("C1", "f1", "m1");
        ErrorDetail e2 = new ErrorDetail("C2", "f2", "m2");
        DomainErrors.Aggregated agg = DomainErrors.of(e1, e2);
        assertEquals(2, agg.getErrors().size());
        assertEquals("f1", agg.getErrors().get(0).getField());
        assertEquals("f2", agg.getErrors().get(1).getField());
    }

    @Test
    void testBuilderAddGlobalAndAddFieldCustomMessage() {
        DomainErrors.Builder builder = DomainErrors.builder();
        builder.addGlobal(ErrorCode.USER_EMAIL_INVALID);
        builder.addGlobal(ErrorCode.USER_EMAIL_INVALID, "Mensaje global");
        builder.addField(ErrorCode.USER_EMAIL_INVALID, "campo");
        builder.addField(ErrorCode.USER_EMAIL_INVALID, "campo", "Mensaje campo");
        DomainErrors.Aggregated agg = builder.build();
        assertEquals(4, agg.getErrors().size());
        assertEquals("Mensaje global", agg.getErrors().get(1).getMessage());
        assertEquals("campo", agg.getErrors().get(2).getField());
        assertEquals("Mensaje campo", agg.getErrors().get(3).getMessage());
    }

    @Test
    void testBuilderIsEmpty() {
        DomainErrors.Builder builder = DomainErrors.builder();
        assertTrue(builder.isEmpty());
        builder.add(new ErrorDetail("C1", "f1", "m1"));
        assertFalse(builder.isEmpty());
    }

    @Test
    void testMergeMultipleAggregated() {
        ErrorDetail e1 = new ErrorDetail("C1", "f1", "m1");
        ErrorDetail e2 = new ErrorDetail("C2", "f2", "m2");
        ErrorDetail e3 = new ErrorDetail("C3", "f3", "m3");
        DomainErrors.Aggregated a1 = DomainErrors.of(e1);
        DomainErrors.Aggregated a2 = DomainErrors.of(e2);
        DomainErrors.Aggregated a3 = DomainErrors.of(e3);
        DomainErrors.Aggregated merged = DomainErrors.merge(a1, a2, a3);
        assertEquals(3, merged.getErrors().size());
        assertEquals("f3", merged.getErrors().get(2).getField());
    }
}
