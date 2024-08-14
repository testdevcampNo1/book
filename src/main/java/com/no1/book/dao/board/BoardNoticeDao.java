package com.no1.book.dao.board;

import com.no1.book.common.util.board.PageHandler;
import com.no1.book.common.util.board.SearchCondition;
import com.no1.book.domain.board.BoardNoticeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardNoticeDao {
    // 게시물 개수 조회
    int count();
    int countNoticeSearch(SearchCondition sc);

    // 전체 조회
    List<BoardNoticeDto> selectNoticeAll();

    // 특별 공지 조회
    List<BoardNoticeDto> selectNoticeSpecial();

    // 페이징 결과 조회
    List<BoardNoticeDto> selectNoticePage(PageHandler ph);

    // 검색
    List<BoardNoticeDto> selectNoticeSearch(PageHandler ph);

    // 상세 조회
    BoardNoticeDto selectNotice(int notcNum);

    // 전체 삭제
    int deleteNoticeAll();

    // 삭제
    int deleteNotice(int notcNum);

    // 수정
    int updateNotice(BoardNoticeDto boardNoticeDto);

    // 등록
    int insertNotice(BoardNoticeDto boardNoticeDto);

}