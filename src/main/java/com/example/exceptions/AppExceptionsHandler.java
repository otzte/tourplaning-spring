package com.example.exceptions;


import com.example.ui.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler {



    @ExceptionHandler(value = {UserServiceException.class}) //which eceptions to handle?
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request){
        ErrorMessage errorMessage = new ErrorMessage(new Date(),ex.getMessage());
        //                       message description                        Server Error
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        //can return errormessage.class and injects to JSON or XML
    }

    @ExceptionHandler(value = {Exception.class}) //which eceptions to handle?
    public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request){
        ErrorMessage errorMessage = new ErrorMessage(new Date(),ex.getMessage());
        //                       message description                        Server Error
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        //can return errormessage.class and injects to JSON or XML
    }

}
