package com.ehs.library.bookreservation.exception;

import lombok.Getter;

public class BookReservationAlreadyException extends RuntimeException{
    public BookReservationAlreadyException(String message){
        super(message);
    }
}
