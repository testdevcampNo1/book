package com.no1.book.common.exception;

import com.no1.book.domain.board.BoardNoticeDto;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

public class BoardCreationException extends DataIntegrityViolationException {
    BoardNoticeDto boardNoticeDto;

    public BoardCreationException(String message) {
        super(message);
    }

    public BoardCreationException(String message, BoardNoticeDto boardNoticeDto) {
        super(message);
        this.boardNoticeDto = boardNoticeDto;
    }

    public BoardCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoardNoticeDto getBoardNoticeDto() {
        return boardNoticeDto;
    }
}
