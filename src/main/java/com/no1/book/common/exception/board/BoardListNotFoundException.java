package com.no1.book.common.exception.board;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

public class BoardListNotFoundException extends BoardException {

    public BoardListNotFoundException(String message) {
        super(message);
    }

    public BoardListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
