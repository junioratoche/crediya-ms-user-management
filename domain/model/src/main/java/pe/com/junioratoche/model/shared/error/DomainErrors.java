package pe.com.junioratoche.model.shared.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class DomainErrors {
    private DomainErrors() {}

    /** Campo reservado para errores no asociados a un atributo. */
    public static final String GLOBAL_FIELD = "_global";

    // ---------- Factory helpers (prácticos) ----------

    /** Un solo error "global" con el mensaje por defecto del código. */
    public static Aggregated of(ErrorCode code) {
        return aggregated(List.of(new ErrorDetail(code.code(), GLOBAL_FIELD, code.message())));
    }

    /** Un solo error "global" con mensaje custom. */
    public static Aggregated of(ErrorCode code, String message) {
        return aggregated(List.of(new ErrorDetail(code.code(), GLOBAL_FIELD, message)));
    }

    /** Un solo error de campo con el mensaje por defecto del código. */
    public static Aggregated ofField(ErrorCode code, String field) {
        return aggregated(List.of(new ErrorDetail(code.code(), field, code.message())));
    }

    /** Un solo error de campo con mensaje custom. */
    public static Aggregated ofField(ErrorCode code, String field, String message) {
        return aggregated(List.of(new ErrorDetail(code.code(), field, message)));
    }

    /** A partir de un único ErrorDetail. */
    public static Aggregated of(ErrorDetail error) {
        return aggregated(List.of(error));
    }

    /** A partir de varios ErrorDetail. */
    public static Aggregated of(ErrorDetail... errors) {
        return aggregated(List.of(errors));
    }

    /** Crea el agregado validando que haya al menos 1 error. */
    public static Aggregated aggregated(List<ErrorDetail> errors) {
        Objects.requireNonNull(errors, "errors");
        if (errors.isEmpty()) {
            throw new IllegalArgumentException("errors must not be empty");
        }
        return new Aggregated(errors);
    }

    /** Combina múltiples agregados en uno solo. */
    public static Aggregated merge(Aggregated first, Aggregated second, Aggregated... rest) {
        List<ErrorDetail> acc = new ArrayList<>(first.errors);
        acc.addAll(second.errors);
        if (rest != null) {
            for (Aggregated a : rest) acc.addAll(a.errors);
        }
        return aggregated(acc);
    }

    /** Builder para ir acumulando errores de forma fluida. */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final List<ErrorDetail> buffer = new ArrayList<>();

        public Builder add(ErrorDetail e) {
            buffer.add(Objects.requireNonNull(e, "error"));
            return this;
        }

        public Builder addGlobal(ErrorCode code) {
            return add(new ErrorDetail(code.code(), GLOBAL_FIELD, code.message()));
        }

        public Builder addGlobal(ErrorCode code, String message) {
            return add(new ErrorDetail(code.code(), GLOBAL_FIELD, message));
        }

        public Builder addField(ErrorCode code, String field) {
            return add(new ErrorDetail(code.code(), field, code.message()));
        }

        public Builder addField(ErrorCode code, String field, String message) {
            return add(new ErrorDetail(code.code(), field, message));
        }

        public boolean isEmpty() { return buffer.isEmpty(); }

        public Aggregated build() {
            return aggregated(buffer);
        }
    }

    // ---------- Excepción de dominio ----------

    public static final class Aggregated extends RuntimeException implements Serializable {
        private static final long serialVersionUID = 1L;
        private final List<ErrorDetail> errors;

        private Aggregated(List<ErrorDetail> errors) {
            super("Validation failed with %d error(s)".formatted(errors.size()));
            this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
        }

        /** Lista inmutable de errores. */
        public List<ErrorDetail> getErrors() {
            return errors;
        }
    }
}
