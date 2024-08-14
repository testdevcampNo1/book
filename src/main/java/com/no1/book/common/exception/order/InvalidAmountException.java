package com.no1.book.common.exception.order;

public class InvalidAmountException extends OrderException {
    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
