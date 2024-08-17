package com.no1.book.dao.board;

import com.no1.book.common.exception.order.DuplicateKeyException;
import com.no1.book.common.util.board.BoardSearchCondition;
import com.no1.book.domain.board.BoardFAQDto;
import com.no1.book.domain.board.CategoryFAQDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardFAQDaoTest {
    @Autowired
    BoardFAQDao boardFAQDao;

    // 등록 성공 테스트
    @Test
    @DisplayName("faq 등록 테스트_성공")
    public void addFAQ_Success_Test() {
        // init : 테스트 환경 초기화
        boardFAQDao.deleteAll();
        assertTrue(boardFAQDao.count() == 0);
        BoardFAQDto sourceDto = null;
        boolean result;

        // # 등록이 되는지 확인
        // given
        // 저장할 게시글 생성
        sourceDto = new BoardFAQDto("no title", "no Content", "asdf");

        // when
        // 테이블에 저장
        result = boardFAQDao.insertFAQ(sourceDto) == 1;

        // then
        // 저장이 성공했는지 확인
        assertTrue(result);
        assertTrue(boardFAQDao.count() == 1);

        //----------------------------------------------------------------------------------------

        // init : 테스트 환경 초기화
        boardFAQDao.deleteAll();
        assertTrue(boardFAQDao.count() == 0);

        // # 조회 했을 때 예상한 값인지 확인
        // given
        // 저장할 게시글 생성 후 저장
        sourceDto = new BoardFAQDto("no title", "no Content", "asdf");
        assertTrue(boardFAQDao.insertFAQ(sourceDto) == 1);
        assertTrue(boardFAQDao.count() == 1);
        assertTrue(boardFAQDao.selectFAQAll().size() == 1);

        // when
        // 저장한 게시물 조회
        BoardFAQDto targetDto = boardFAQDao.selectFAQAll().get(0);

        // then
        // 저장 전과 후가 같은지 확인
        assertTrue(sourceDto.equals(targetDto));
    }

    // 등록 실패 테스트
    @Test
    @DisplayName("faq 등록 테스트_실패")
    public void addFAQ_failed_Test() {
        // init : 테스트 환경 초기화
        boardFAQDao.deleteAll();
        assertTrue(boardFAQDao.count() == 0);

        // # 제약 조건을 어긴 등록이 실패 하는지 확인
        assertThrows(DataIntegrityViolationException.class, () -> {

            // given
            // 제약 조건을 어긴 게시글 생성
            String longText = "|0123456789|0123456789|0123456789|0123456789|0123456789|0123456789|0123456789|0123456789|0123456789|0123456789";
            BoardFAQDto sourceDto = new BoardFAQDto(longText, "no content", "asdf");

            // when
            // 잘못된 게시글 저장
            assertTrue(boardFAQDao.insertFAQ(sourceDto) == 0);

            // then
            // 저장이 안 됐는지 확인
            assertTrue(boardFAQDao.count() == 0);
            assertTrue(boardFAQDao.selectFAQAll().isEmpty());
        });
    }

    // faq 조회 성공 테스트
    @Test
    @DisplayName("faq 리스트 조회")
    public void selectListTest(){
        // init : 테스트 환경 초기화
        boardFAQDao.deleteAll();
        assertTrue(boardFAQDao.count() == 0);
        assertTrue(boardFAQDao.selectFAQAll().isEmpty());

        // given
        // 게시물 리스트 100개 저장
        BoardFAQDto boardFAQDto = new BoardFAQDto("no title", "no content", "asdf");
        List<BoardFAQDto> sourceList = new ArrayList<>();
        // Source List에 저장
        for (int i=0; i<100; i++){
            sourceList.add(boardFAQDto);
        }

        // DB에 저장
        for (BoardFAQDto dto : sourceList) {
            boardFAQDao.insertFAQ(dto);
        }
        assertTrue(boardFAQDao.count() == 100);
        assertTrue(boardFAQDao.selectFAQAll().size() == 100);

        // 저장한 게시글 목록 조회
        List<BoardFAQDto> targetList = boardFAQDao.selectFAQAll();

        // 원본과 DB 데이터 비교

        // 리스트 사이즈 비교
        assertTrue(targetList.size() == sourceList.size());
        // 리스트 내용 비교
        for (int i = 0; i < targetList.size(); i++) {
            assertTrue(targetList.get(i).equals(sourceList.get(i)));
        }

        //----------------------------------------------------------------------------------------

        // init : 테스트 환경 초기화
        boardFAQDao.deleteAll();
        assertTrue(boardFAQDao.count() == 0);
        assertTrue(boardFAQDao.selectFAQAll().isEmpty());

        // 게시글 100개 SourceList 준비

        // 게시글 100개 저장

        // 100개 저장 됐는지 확인

        // 게시글 목록 조회
        // 게시글 n개 저장

        // 1페이지 10개의 pageHandler 사용, 게시물 조회
        // 조회 리스트 사이즈 10인지 확인
        // wrt_date 내림차순, faq_num 내림차순으로 정렬 됐는지 확인

        //----------------------------------------------------------------------------------------
    }

    @Test
    @DisplayName("faq 리스트 SearchCondition 사용 조회")
    public void searchFAQPageTest(){
        // init : 테스트 환경 초기화
        boardFAQDao.deleteAll();
        assertTrue(boardFAQDao.count() == 0);
        assertTrue(boardFAQDao.selectFAQAll().isEmpty());

        BoardSearchCondition sc = new BoardSearchCondition();
        assertTrue(boardFAQDao.searchFAQCount(sc) == 0);


        // 조건 없이 검색
        // 게시물 리스트 100개 저장
        BoardFAQDto boardFAQDto = new BoardFAQDto("no title", "no content", "asdf");
        List<BoardFAQDto> sourceList = new ArrayList<>();
        // Source List에 저장
        for (int i=0; i<100; i++){
            sourceList.add(boardFAQDto);
        }

        // DB에 저장
        for (BoardFAQDto dto : sourceList) {
            boardFAQDao.insertFAQ(dto);
        }
        assertTrue(boardFAQDao.count() == 100);
        assertTrue(boardFAQDao.selectFAQAll().size() == 100);

        assertTrue(boardFAQDao.searchFAQCount(sc) == 100);

        // 제목과 내용 검색 A

        // 내용만 다르게 저장
        String testSearchOption = "A";
        String testKeyword = "contentTest";
        boardFAQDto.setFaqContent(testKeyword);

        // 저장용 리스트 초기화
        sourceList = new ArrayList<>();
        // 50건 저장
        for (int i=0; i<50; i++){
            sourceList.add(boardFAQDto);
        }

        // DB에 저장
        for (BoardFAQDto dto : sourceList) {
            boardFAQDao.insertFAQ(dto);
        }
        assertTrue(boardFAQDao.count() == 150);
        assertTrue(boardFAQDao.selectFAQAll().size() == 150);

        sc.setKeyword("tentTe");
        sc.setSearchOption(testSearchOption);

        // 제목만 검색
        sc.setKeyword("o ti");
        sc.setSearchOption("T");
        assertTrue(boardFAQDao.searchFAQCount(sc) == 150);

        // 작성자 검색
        // 카테고리 검색
    }

    // 삭제 테스트
    @Test
    @DisplayName("faqNum 삭제 테스트")
    public void deleteFAQNumTest() {
        // init : 테스트 환경 초기화
        boardFAQDao.deleteAll();
        assertTrue(boardFAQDao.count() == 0);
        assertTrue(boardFAQDao.selectFAQAll().isEmpty());

        // 게시글 1개 저장
        BoardFAQDto boardFAQDto = new BoardFAQDto("no title", "no content", "asdf");
        assertTrue(boardFAQDao.insertFAQ(boardFAQDto) == 1);
        assertTrue(boardFAQDao.count() == 1);

        // 게시글 1개 저장
        assertTrue(boardFAQDao.insertFAQ(boardFAQDto) == 1);
        assertTrue(boardFAQDao.count() == 2);

        // 게시글 1개 삭제
        int faqNum = boardFAQDao.selectFAQAll().get(0).getFaqNum();
        assertTrue(boardFAQDao.deleteFAQ(faqNum) == 1);
        assertTrue(boardFAQDao.count() == 1);
    }

    // 게시글 수정 테스트
    @Test
    @DisplayName("faq 수정 테스트")
    public void updateFAQTest() {
        // init : 테스트 환경 초기화
        boardFAQDao.deleteAll();
        assertTrue(boardFAQDao.count() == 0);
        assertTrue(boardFAQDao.selectFAQAll().isEmpty());

        // 게시글 2개 저장
        BoardFAQDto boardFAQDto = new BoardFAQDto("no title", "no content", "asdf");
        assertTrue(boardFAQDao.insertFAQ(boardFAQDto) == 1);
        assertTrue(boardFAQDao.count() == 1);
        assertTrue(boardFAQDao.insertFAQ(boardFAQDto) == 1);
        assertTrue(boardFAQDao.count() == 2);

        BoardFAQDto sourceDto = boardFAQDao.selectFAQAll().get(0);
        sourceDto.setFaqTitle("Update Title!!!!");
        sourceDto.setFaqContent("Update Content@@@");

        assertTrue(boardFAQDao.updateFAQ(sourceDto) == 1);

        BoardFAQDto targetDto = boardFAQDao.selectFAQAll().get(0);
        assertTrue(sourceDto.equals(targetDto));
    }

    @Test
    public void test() {
        List<CategoryFAQDto> list = boardFAQDao.selectCategory();
        for (CategoryFAQDto dto : list) {

            System.out.println(dto);
        }
    }
}