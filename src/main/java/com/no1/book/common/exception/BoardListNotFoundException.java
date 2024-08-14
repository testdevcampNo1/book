package com.no1.book.common.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

public class BoardListNotFoundException extends DataAccessException {

    public BoardListNotFoundException(String message) {
        super(message);
    }

    public BoardListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
