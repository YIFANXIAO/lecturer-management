package learning.way.lecturer.management.common;

import learning.way.lecturer.management.dtos.ErrorResultDTO;
import learning.way.lecturer.management.exceptions.BaseBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({BaseBusinessException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorResultDTO handleBaseBusinessException(BaseBusinessException e) {
        printErrorException(e);
        return new ErrorResultDTO(e.getErrorCode().getValue(), e.getMessage());
    }

    private void printErrorException(Exception e) {
        String exceptionName = e.getClass().getSimpleName();
        log.error(exceptionName, e);
    }
}
