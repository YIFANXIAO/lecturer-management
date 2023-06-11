package learning.way.lecturer.management.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVOKE_TIMEOUT("invoke_timeout"),
    UNKNOWN_ERROR("unknown_error"),
    INVALID_COURSE_REQUEST("invalid_course_request");

    private final String value;

    ErrorCode(String value) {
        this.value = value;
    }
}
