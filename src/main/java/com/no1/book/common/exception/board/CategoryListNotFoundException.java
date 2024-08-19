package com.no1.book.common.exception.board;

import org.springframework.dao.DataAccessException;

public class CategoryListNotFoundException extends DataAccessException {

    public CategoryListNotFoundException(String message) {
        super(message);
    }

    public CategoryListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}