package com.no1.book.service.board;

import com.no1.book.common.exception.board.BoardUpdateException;
import com.no1.book.common.util.board.BoardPageHandler;
import com.no1.book.common.util.board.BoardSearchCondition;
import com.no1.book.dao.board.BoardFAQDao;
import com.no1.book.domain.board.BoardFAQDto;
import com.no1.book.domain.board.CategoryFAQDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardFAQServiceImpl implements BoardFAQService {
    BoardFAQDao boardFAQDao;

    @Autowired
    public BoardFAQServiceImpl(BoardFAQDao boardFAQDao) {
        this.boardFAQDao = boardFAQDao;
    }

    // 검색 결과 카운트
    @Override
    public int getSearchCount(BoardSearchCondition sc){
        return boardFAQDao.searchFAQCount(sc);
    }
    // 검색 결과 페이징
    @Override
    public List<BoardFAQDto> getFAQPage(BoardPageHandler ph) {
        // 결과 리턴
        return boardFAQDao.searchFAQPage(ph);
    }

    @Override
    public BoardFAQDto getFAQ(int faqNum) {
        return boardFAQDao.selectFAQ(faqNum);
    }

    // 게시물 삭제
    @Override
    public int removeFAQ(int faqNum) {
        return boardFAQDao.deleteFAQ(faqNum);
    }

    // 게시물 수정
    @Override
    @Transactional      // select for update
    public int modifyFAQ(BoardFAQDto boardFAQDto) {
        // 게시글 수정 대상 조회
        BoardFAQDto updateDto = boardFAQDao.selectFAQ(boardFAQDto.getFaqNum());

        // 제목, 내용 변경
        updateDto.setFaqTitle(boardFAQDto.getFaqTitle());
        updateDto.setFaqContent(boardFAQDto.getFaqContent());

        // 게시글 수정
        int result = boardFAQDao.updateFAQ(boardFAQDto);

        // 결과가 0보다 작거나 같으면 실패
        if (result <= 0)
            // 게시글 수정 예외 발생
            throw new BoardUpdateException("MOD_ERR", boardFAQDto);

        // 수정
        return result;
    }

    // 게시물 저장
    @Override
    public int addFAQ(BoardFAQDto boardFAQDto) {
        return boardFAQDao.insertFAQ(boardFAQDto);
    }

    // 카테고리 조회
    @Override
    public List<CategoryFAQDto> getCategory() {return boardFAQDao.selectCategory();}
}
