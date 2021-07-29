package com.example.exceptions;

public class UserServiceException extends RuntimeException{


    private static final long serialVersionUID = -297162518308288264L;
    public UserServiceException(String message){
        super(message);
    }
}
