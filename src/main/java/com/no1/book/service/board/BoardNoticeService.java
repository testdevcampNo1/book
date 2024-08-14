package com.no1.book.service.board;

import com.no1.book.common.util.PageHandler;
import com.no1.book.common.util.SearchCondition;
import com.no1.book.domain.board.BoardNoticeDto;

import java.util.List;

public interface BoardNoticeService {
    int count();
    int countNoticeSearch(SearchCondition sc);
    // 공지 목록 조회
    List<BoardNoticeDto> findNoticeList();
    // 특별 공지 목록 조회
    List<BoardNoticeDto> findNoticeSpecial();
    List<BoardNoticeDto> findNoticePage(PageHandler ph);
    List<BoardNoticeDto> findNoticeSearch(PageHandler ph);
    // 공지 상세 조회
    BoardNoticeDto findNotice(int notcNum);
    // 공지 수정
    int modifyNotice(BoardNoticeDto boardNoticeDto);
    // 공지 삭제
    int removeNotice(int notcNum);
    // 공지 등록
    int addNotice(BoardNoticeDto boardNoticeDto);
}
