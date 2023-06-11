package learning.way.lecturer.management.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_COURSE_REQUEST("invalid_course_request");

    private final String value;

    ErrorCode(String value) {
        this.value = value;
    }
}
