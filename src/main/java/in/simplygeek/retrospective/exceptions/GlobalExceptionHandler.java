package in.simplygeek.retrospective.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(IncorrectRequestException.class)
    public ResponseEntity<String> handleException(IncorrectRequestException e) {
        return new ResponseEntity<>("Invalid Request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException e) {
        return new ResponseEntity<>("Incorrect Id: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
}