package com.no1.book.common.exception.board;

import com.no1.book.domain.board.BoardDto;
import org.springframework.dao.DataAccessException;

public class BoardException extends DataAccessException {
    BoardDto boardDto;

    public BoardException(String message, BoardDto boardDto){
        this(message);
        this.boardDto = boardDto;
    }

    public BoardException(String message) {
        super(message);
    }

    public BoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoardDto getBoardDto() {
        return boardDto;
    }

    public void setBoardDto(BoardDto boardDto) {
        this.boardDto = boardDto;
    }
}