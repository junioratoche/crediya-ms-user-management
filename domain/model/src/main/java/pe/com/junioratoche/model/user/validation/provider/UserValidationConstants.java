package pe.com.junioratoche.model.user.validation.provider;

import java.math.BigDecimal;

public final class UserValidationConstants {
    public static final String ID_FIELD = "id";
    public static final String FIRST_NAME_FIELD = "firstName";
    public static final String LAST_NAME_FIELD = "lastName";
    public static final String EMAIL_FIELD = "email";
    public static final String DOCUMENT_NUMBER_FIELD = "documentNumber";
    public static final String PHONE_NUMBER_FIELD = "phoneNumber";
    public static final String SALARY_FIELD = "salary";
    public static final String ROLE_ID_FIELD = "roleId";

    public static final BigDecimal SALARY_MIN = BigDecimal.ZERO;
    public static final BigDecimal SALARY_MAX = new BigDecimal("15000000");
    public static final String SALARY_MIN_STRING = "0";
    public static final String SALARY_MAX_STRING = "15000000";
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String UNIQUE_EMAIL_RULE_NAME_FORMAT = "UniqueEmailRule(%s)";
    public static final String REQUIRED_RULE_NAME_FORMAT = "RequiredRule(%s)";
    public static final String RANGE_RULE_NAME_FORMAT = "RangeRule(%s)";
    public static final String EMAIL_FORMAT_RULE_NAME_FORMAT = "EmailFormatRule(%s)";

    private UserValidationConstants() {}
}
