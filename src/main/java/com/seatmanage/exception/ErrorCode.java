package com.seatmanage.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EXISTED_USER(409,"user already exist !!!!"),
    UNVALIDATED_PASS(400,"password must be at least 8 characters"),
    EXISTED_PERMISSION(409,"permission already exist !!"),
    NOT_FOUND_USER(404,"user not found !!!!"),
    FLOOR_NOT_NULL(400,"Flood ID must be not null"),
    USERNAME_NOT_NULL(400,"username must be not null !!!!"),
    PASSWORD_NOT_NULL(400,"Password must be not null !!!!"),
    HALL_NOT_NULL(400,"Hall ID must be not null"),
    ROOM_NOT_NULL(400,"Room ID must be not null") ;
    private int code;
    private String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
