package com.seatmanage.exception;

import com.seatmanage.dto.response.ApiResponse;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Getter
public class GlobalExceptionHandle extends RuntimeException {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleGlobalException(RuntimeException e) {
        ApiResponse apiResponse = ApiResponse.builder().code(1001).msg(e.getMessage()).build();
        return  ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.valueOf(e.getBindingResult().getFieldError().getDefaultMessage());
        ApiResponse apiResponse = ApiResponse.builder().code(errorCode.getCode()).msg(errorCode.getMessage()).build();
        return  ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = AppExceptionHandle.class)
    ResponseEntity<ApiResponse> handleAppException(AppExceptionHandle e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = ApiResponse.builder().code(e.getErrorCode().getCode()).msg(e.getMessage()).build();
        return  ResponseEntity.badRequest().body(apiResponse);
    }

}


