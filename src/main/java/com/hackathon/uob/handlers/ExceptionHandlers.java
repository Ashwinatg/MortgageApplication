package com.hackathon.uob.handlers;

import com.hackathon.uob.dto.ApiReponse;
import com.hackathon.uob.exceptions.CustomerAccountNotFoundException;
import com.hackathon.uob.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> customerNotFound(CustomerNotFoundException customerNotFoundException) {
        ApiReponse reponse = new ApiReponse();
        reponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
        reponse.setMessage(customerNotFoundException.getMessage());
        return ResponseEntity.internalServerError()
                .body(reponse);
    }

    @ExceptionHandler(CustomerAccountNotFoundException.class)
    public ResponseEntity<Object> customerNotFound(CustomerAccountNotFoundException customerAccountNotFoundException) {
        ApiReponse reponse = new ApiReponse();
        reponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
        reponse.setMessage(customerAccountNotFoundException.getMessage());
        return ResponseEntity.internalServerError()
                .body(reponse);
    }
}
