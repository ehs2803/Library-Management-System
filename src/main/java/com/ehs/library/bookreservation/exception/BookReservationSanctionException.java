package com.ehs.library.bookreservation.exception;

import lombok.Getter;

public class BookReservationSanctionException extends RuntimeException{
    public BookReservationSanctionException(String message){
        super(message);
    }
}
