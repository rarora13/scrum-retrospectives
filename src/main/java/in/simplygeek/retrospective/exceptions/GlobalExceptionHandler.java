package in.simplygeek.retrospective.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import in.simplygeek.retrospective.beans.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
    	logger.error("Exception occurred {}",e.getMessage(),e);
        return new ResponseEntity<>(new ErrorResponse("error","An error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(IncorrectRequestException.class)
    public ResponseEntity<?> handleException(IncorrectRequestException e) {
    	logger.error("Incorrect user data {}",e.getMessage());
        return new ResponseEntity<>(new ErrorResponse("error","Invalid Request: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleException(EntityNotFoundException e) {
    	logger.error("Incorrect entity id {}",e.getMessage());
        return new ResponseEntity<>(new ErrorResponse("error","Incorrect Id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException e) {
    	logger.error("Exception occurred {}",e.getMessage(),e);
        return new ResponseEntity<>(new ErrorResponse("error","Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}