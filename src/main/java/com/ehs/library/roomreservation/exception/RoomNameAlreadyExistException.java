package com.ehs.library.roomreservation.exception;

public class RoomNameAlreadyExistException extends RuntimeException{
    public RoomNameAlreadyExistException(String message){
        super(message);
    }
}
