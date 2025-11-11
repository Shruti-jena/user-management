package com.shruti.user_management.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.shruti.user_management.DTO.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionhandler {
    
    //Handles 404 Not found errors thrown from the service layer
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
        ErrorResponse errorResponse =  new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=","")
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    //Handles 400 Bad Request resulting form failed @valid checks on DTO's
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request){
        Map<String,String> errors = new HashMap<>();
        //Extracts all field errors and their messages
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName=((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    //Handles 400 bad request for business logic failures(duplicate mail id)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST.getReasonPhrase(),ex.getMessage(),request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    } 

    //Fallback handler for 500 internal server error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request){
        System.err.println("UNHANDLED SERVER ERROR: "+ ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
            );
            return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
