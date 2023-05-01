package com.example.ecommerce.Exceptions;

public class CartIsEmptyException extends Exception{
    public CartIsEmptyException(String message){
        super(message);
    }
}
