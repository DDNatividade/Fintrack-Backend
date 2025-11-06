package com.apis.fintrack.Configuration;

import com.apis.fintrack.DTO.ErrorManagerClass;
import com.apis.fintrack.Exception.TransactionNotFoundException;
import com.apis.fintrack.Exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorManagerClass> handleUserNotFoundException(UserNotFoundException ex){
        ErrorManagerClass err = new ErrorManagerClass(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorManagerClass> handleTransactionNotFoundException(TransactionNotFoundException ex){
        ErrorManagerClass err = new ErrorManagerClass(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorManagerClass> handleException(Exception ex){
        ErrorManagerClass err = new ErrorManagerClass(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorManagerClass>
    handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ErrorManagerClass err = new ErrorManagerClass(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                errores
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }





}
