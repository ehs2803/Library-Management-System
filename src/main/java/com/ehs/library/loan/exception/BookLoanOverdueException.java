package com.ehs.library.loan.exception;

public class BookLoanOverdueException extends RuntimeException{
    public BookLoanOverdueException(String message){
        super(message);
    }
}
