package com.no1.book.common.exception.order;

public class MissingRequiredOrderInfoException extends OrderException {
    public MissingRequiredOrderInfoException(String message) {
        super(message);
    }

    public MissingRequiredOrderInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
