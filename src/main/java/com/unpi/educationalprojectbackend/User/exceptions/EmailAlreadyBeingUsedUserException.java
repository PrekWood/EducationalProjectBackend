package com.unpi.educationalprojectbackend.User.exceptions;

public class EmailAlreadyBeingUsedUserException extends Exception{
    public EmailAlreadyBeingUsedUserException(String errorMessage) {
        super(errorMessage);
    }
}
