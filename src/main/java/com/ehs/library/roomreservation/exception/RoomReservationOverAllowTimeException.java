package com.ehs.library.roomreservation.exception;

public class RoomReservationOverAllowTimeException extends RuntimeException{
    public RoomReservationOverAllowTimeException(String message){
        super(message);
    }
}
