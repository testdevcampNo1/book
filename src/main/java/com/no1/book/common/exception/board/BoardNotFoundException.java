package com.no1.book.common.exception.board;

import com.no1.book.domain.board.BoardDto;
import org.springframework.dao.DataAccessException;

public class BoardNotFoundException extends BoardException {


    public BoardNotFoundException(String message) {
        super(message);
    }
    public BoardNotFoundException(String message, BoardDto boardDto){
        super(message, boardDto);
    }
    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
