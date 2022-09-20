package com.ehs.library.member.exception;

import com.ehs.library.base.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message){
        super(message);
    }
}
