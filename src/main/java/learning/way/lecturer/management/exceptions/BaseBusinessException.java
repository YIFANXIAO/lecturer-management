package learning.way.lecturer.management.exceptions;

import learning.way.lecturer.management.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BaseBusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseBusinessException(ErrorCode errorCode) {
        super(errorCode.getValue());
        this.errorCode = errorCode;
    }
}
