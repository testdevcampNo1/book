package com.no1.book.common.exception.board;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

public class BoardDeletionException extends DataAccessException {

    public BoardDeletionException(String message) {
        super(message);
    }

    public BoardDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}