package learning.way.lecturer.management.exceptions;

import learning.way.lecturer.management.enums.ErrorCode;
import lombok.Getter;

@Getter
public class TimeOutException extends BaseBusinessException{

    public TimeOutException(ErrorCode errorCode) {
        super(errorCode);
    }
}
