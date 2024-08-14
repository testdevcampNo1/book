package com.no1.book.common.exception.order;

// key가 중복될 때 던지는 예외
public class DuplicateKeyException extends OrderException {
    public DuplicateKeyException(String message) {
        super(message);
    }

    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
