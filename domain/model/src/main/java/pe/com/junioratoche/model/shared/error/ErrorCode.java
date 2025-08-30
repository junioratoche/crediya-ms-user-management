package pe.com.junioratoche.model.shared.error;

public enum ErrorCode {
    USER_FIRST_NAME_REQUIRED ("USR_001", 400, "firstName is required"),
    USER_LAST_NAME_REQUIRED  ("USR_002", 400, "lastName is required"),
    USER_EMAIL_REQUIRED      ("USR_003", 400, "email is required"),
    USER_EMAIL_INVALID       ("USR_004", 400, "email has invalid format"),
    USER_SALARY_REQUIRED     ("USR_005", 400, "salary is required"),
    USER_SALARY_OUT_OF_RANGE ("USR_006", 400, "salary out of range [0..15000000]"),
    USER_ROLE_ID_REQUIRED    ("USR_007", 400, "roleId is required"),
    USER_EMAIL_ALREADY_USED  ("USR_008", 400, "email already registered"),
    VALIDATION_FAILED        ("USR_000", 400, "Validation failed"),
    USER_SALARY_INVALID("USR_009", 400, "salary has invalid format"),
    USER_ROLE_ID_INVALID ("USR_010", 400, "roleId must be a valid UUID");

    private final String code;
    private final int httpStatus;
    private final String message;

    ErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String code()    { return code; }
    public int    http()    { return httpStatus; }
    public String message() { return message; }
}
