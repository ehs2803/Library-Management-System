package com.ehs.library.bookreservation.exception;

public class BookReservationAlreadyLoanException extends RuntimeException{
    public BookReservationAlreadyLoanException(String message){
        super(message);
    }
}
