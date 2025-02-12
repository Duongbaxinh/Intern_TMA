package com.seatmanage.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EXISTED_USER(1001,"user already exist !!!!"),
    UNVALIDATED_PASS(1002,"password must be at least 8 characters"),
    ;
    private int code;
    private String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
