package com.no1.book.common.exception.board;

import com.no1.book.domain.board.BoardDto;
import com.no1.book.domain.board.BoardNoticeDto;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

public class BoardCreationException extends BoardException {
    public BoardCreationException(String message) {
        super(message);
    }

    public BoardCreationException(String message, BoardDto boardDto) {
        super(message, boardDto);
        ;
    }
}
