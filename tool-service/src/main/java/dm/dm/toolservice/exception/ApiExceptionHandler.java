package dm.dm.toolservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiExceptionResponse> catchMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiExceptionResponse> entityNotFoundException(EntityNotFoundException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiExceptionResponse>  userNotUniqueEmailException(DuplicateException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiExceptionResponse(e.getMessage()));
    }


}
