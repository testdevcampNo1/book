package com.no1.book.common.exception.board;

import org.springframework.dao.DataAccessException;

public class BoardNotFoundException extends DataAccessException {

    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
