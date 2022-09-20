package com.ehs.library.loan.exception;

public class BookLoanExtensionLimitException extends RuntimeException{
    public BookLoanExtensionLimitException(String message){
        super(message);
    }
}
