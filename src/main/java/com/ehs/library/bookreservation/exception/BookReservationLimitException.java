package com.ehs.library.bookreservation.exception;

import lombok.Getter;

public class BookReservationLimitException extends RuntimeException {
    public BookReservationLimitException(String message){
        super(message);
    }
}
