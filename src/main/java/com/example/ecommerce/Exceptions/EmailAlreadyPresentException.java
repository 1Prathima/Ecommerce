package com.example.ecommerce.Exceptions;

public class EmailAlreadyPresentException extends Exception{
    public EmailAlreadyPresentException(String message){
        super(message);
    }
}
