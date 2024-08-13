package com.no1.book.common.exception;

// DB 연결 오류시 던지는 예외
public class SystemException extends OrderException{
    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
