package com.no1.book.common.exception.board;

import com.no1.book.domain.board.BoardDto;
import org.springframework.dao.DataAccessException;

public class BoardUpdateException extends BoardException {


    public BoardUpdateException(String message) {
        super(message);
    }
    public BoardUpdateException(String message, BoardDto boardDto){
        super(message, boardDto);
    }
    public BoardUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}