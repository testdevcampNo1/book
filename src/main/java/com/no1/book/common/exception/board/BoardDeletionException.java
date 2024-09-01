package com.no1.book.common.exception.board;

import com.no1.book.domain.board.BoardDto;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

public class BoardDeletionException extends BoardException {


    public BoardDeletionException(String message) {
        super(message);
    }
    public BoardDeletionException(String message, BoardDto boardDto){
        super(message, boardDto);
    }
    public BoardDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}