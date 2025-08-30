package pe.com.junioratoche.model.shared.error;

import java.util.Objects;

public final class ErrorDetail {
    private final String code;
    private final String field;
    private final String message;

    public ErrorDetail(String code, String field, String message) {
        this.code = Objects.requireNonNull(code, "code");
        this.field = Objects.requireNonNull(field, "field");
        this.message = Objects.requireNonNull(message, "message");
    }

    public String code() { return code; }
    public String field() { return field; }
    public String message() { return message; }

    @Override
    public String toString() {
        return "ErrorDetail{code='%s', field='%s', message='%s'}".formatted(code, field, message);
    }
}
