package com.workify.order.execption;

public class InvalidInputException extends RuntimeException{

    public InvalidInputException ( String message){
        super(message);
    }
}
