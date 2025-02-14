package com.seatmanage.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EXISTED_USER(1001,"user already exist !!!!"),
    UNVALIDATED_PASS(1002,"password must be at least 8 characters"),
    FLOOR_NOT_NULL(1002,"floor must be not null"),
    ;
    private int code;
    private String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
