package com.seatmanage.exception;

import lombok.Getter;

@Getter
public class AppExceptionHandle extends RuntimeException {
    private  ErrorCode errorCode;
    public AppExceptionHandle(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
