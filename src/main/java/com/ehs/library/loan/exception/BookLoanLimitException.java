package com.ehs.library.loan.exception;

public class BookLoanLimitException extends RuntimeException{
    public BookLoanLimitException(String message){
        super(message);
    }
}
