package com.no1.book.common.exception.board;

import org.springframework.dao.DataAccessException;

public class BoardUpdateException extends DataAccessException {

    public BoardUpdateException(String message) {
        super(message);
    }

    public BoardUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}