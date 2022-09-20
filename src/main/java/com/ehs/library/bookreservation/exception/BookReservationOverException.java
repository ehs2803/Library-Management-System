package com.ehs.library.bookreservation.exception;

import lombok.Getter;

public class BookReservationOverException extends RuntimeException {
    public BookReservationOverException(String message){
        super(message);
    }
}
