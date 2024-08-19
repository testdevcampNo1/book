package com.no1.book.common.exception.order;

// Database 작업 실패시 던지는 예외
public class SystemException extends OrderException {
    public SystemException(String message) { super(message); }
    public SystemException(String message, Throwable cause) { super(message, cause); }
}