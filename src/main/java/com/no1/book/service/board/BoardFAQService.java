package com.no1.book.service.board;

import com.no1.book.common.util.board.BoardPageHandler;
import com.no1.book.common.util.board.BoardSearchCondition;
import com.no1.book.domain.board.BoardFAQDto;
import com.no1.book.domain.board.CategoryFAQDto;

import java.util.List;

public interface BoardFAQService {
    // 게시물 검색 결과 개수
    int getSearchCount(BoardSearchCondition sc);

    // 게시물 결과
    List<BoardFAQDto> getFAQPage(BoardPageHandler ph);

    // 게시물 상세조회
    BoardFAQDto getFAQ(int faqNum);

    // 게시물 삭제
    int removeFAQ(int faqNum);

    // 게시물 수정
    int modifyFAQ(BoardFAQDto boardFAQDto);

    // 게시물 등록
    int addFAQ(BoardFAQDto boardFAQDto);

    // 카테고리 조회
    List<CategoryFAQDto> getCategory();
}
