package com.mbacms.config;

import com.mbacms.exception.OwnershipInvalidException;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.util.ResponseUtility;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {


    private final ResponseUtility responseUtility;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseUtility> handleResourceNotFoundException(ResourceNotFoundException e){
        responseUtility.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(responseUtility);
    }

    @ExceptionHandler(OwnershipInvalidException.class)
    public ResponseEntity<ResponseUtility> handleOwnershipInvalidException(OwnershipInvalidException e){
        responseUtility.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(responseUtility);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult=e.getBindingResult();
        List<FieldError> errors =bindingResult.getFieldErrors();
        Map<String,String> map=new HashMap<>();
        for(FieldError error:errors){
            map.put(error.getField(), error.getDefaultMessage());
        }
return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseUtility> handleIllegalArgumentException(IllegalArgumentException e){
        responseUtility.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(responseUtility);
    }

}
