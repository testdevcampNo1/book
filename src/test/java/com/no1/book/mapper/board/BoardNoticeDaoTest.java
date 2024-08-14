package com.no1.book.mapper.board;

import com.no1.book.dao.board.BoardNoticeDao;
import com.no1.book.domain.board.BoardNoticeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BoardNoticeDaoTest {
    @Autowired
    BoardNoticeDao noticeDao;



    /*
        공지게시판

        기능 정의
        1. 목록 조회
        2. 페이징
        3. 상세 CRUD
        4. 검색(키워드, 유형)

        기능 요구사항

        목록(페이징)
        기본 페이지 사이즈 10
        게시글 제목 클릭 시 상세로 이동
        목록 최상단에 관리자가 지정한 글 별도로 bold 표시
        특별공지 순서는 지정한 순위 대로 나와야 함
        관리자로 로그인하면 게시글 작성 버튼 표시

        상세
        관리자로 로그인하면 수정, 삭제 등록 버튼 표시

        검색
        키워드로 검색 시 키워드가 제목, 내용에 포함 되는 게시글 검색
        유형으로 검색 시 유형에 해당 하는 게시글 검색

        Mapper CRUD 테스트
        기능 테스트
        1. 목록 조회
        2. 페이징

        1. 목록 조회
        목록이 리스트 형식으로 반환되는가

        2. 페이징

        상세 CRUD 테스트
        insertNotice
        저장이 잘 되는가
        저장이 실패하면 예외가 발생하는가
        저장한 데이터가 유효한가

        selectNotice
        저장한 개수 만큼 조회되는가

        updateNotice
        변경 내용이 저장이 됐는가

        deleteNotice
        삭제
     */

    // 테스트 대상 빈이 정상 등록 됐는가
    @DisplayName("빈 주입 테스트")
    @Test
    public void initTest() {
        assertTrue(Objects.nonNull(noticeDao));
    }

    // 모든 테스트 마다 테이블 전체 삭제
    @BeforeEach
    public void 환경초기화(){
        noticeDao.deleteNoticeAll();
        assertTrue(noticeDao.count() == 0);
    }

    /*
        INSERT TEST
        데이터 삽입 테스트

        ※ 실패하면 예외 발생

        성공 조건
            1. PK가 중복 되지 않아야한다.
            2. NOT NULL 제약조건을 지켜야한다.
            3. 타입이 일치하거나, 자동 형변환이 가능해야한다.
            4. 길이를 초과하지 않아야한다.

        실패 조건
            1. 동일한 PK 값을 삽입한다.                          Error Code: 1062. Duplicate entry '[값]' for key '[테이블명.PRIMARY]'
            2. NOT NULL 컬럼에 NULL이 들어간다.                 Error Code: 1364. Field '[컬럼명]' doesn't have a default value
            3. 다른 타입이 들어가거나, 자동 형변환이 불가능하다.     Error Code: 1366. Incorrect integer value: '[값]' for column '[컬럼명]' at row 1
            4. 길이를 초과한다.                                Error Code: 1406. Data too long for column '[컬럼명]' at row 1
                                                            Error Code: 1264. Out of range value for column '[컬럼명]' at row 1

        시나리오
            1. 성공 조건을 모두 만족
            2. 실패 조건을 모두 만족
            3. 실패 조건을 n개만 만족
            4. 각 시나리오의 수량을 점차적으로 증가

        테스트 방식
            1. 비교 기준값(DB에 저장하기 전 값)과 대상값(DB에 저장된 값)을 비교
            2. 성공, 실패의 반환 값이 예상한 값인지 확인 (0, 1, n)
            3. isEmpty, equals 등의 메서드 사용 권장
    */
    @DisplayName("게시물 등록 테스트")
    @Test
    public void insertTest(){
        // 저장할 객체
        BoardNoticeDto sourceDto = new BoardNoticeDto("긴급공지", "공지제목", "공지내용", "공지이미지", "Y", "1", 0, "702", "POST", "게시중");

        // DB에 저장
        assertTrue(noticeDao.insertNotice(sourceDto) == 1);
        assertTrue(noticeDao.count() == 1);

        int notcNum = noticeDao.selectNoticeAll().get(0).getNotcNum();

        // DB에서 조회
        BoardNoticeDto targetDto = noticeDao.selectNotice(notcNum);

        // 모든 내용이 저장 됐는가
        assertTrue(Objects.equals(sourceDto,targetDto));

        // 동일한 키로 저장하면 중복키 예외가 발생하는가
//        assertThrows(DuplicateKeyException.class, () -> noticeDao.insertNotice(sourceDto));

        // NotNull 컬럼에 Null이 들어가면 무결성 예외가 발생하는가
        assertThrows(DataIntegrityViolationException.class, () -> noticeDao.insertNotice(new BoardNoticeDto()));
    }

    /*
        SELECT TEST
        데이터 조회 테스트

        ※ 실패해도 에러가 발생하지 않음(결과가 null).

        성공조건
            1. 존재하는 주문번호로 조회

        실패조건
            2. 존재하지 않는 주문번호로 조회

        시나리오
            1. 1개 넣고 1개 조회
            2. 여러개 넣고 1개 조회
            3. 여러개 넣고 여러개 조회

        테스트 방식
            1. 비교기준값(DB에 저장 전)과 비교대상값(DB에 저장된 값)이 일치하는지 비교
            2. 실제 저장한 개수와 조회한 개수가 일치하는지 확인
    */

    @DisplayName("게시물 상세 조회 테스트")
    @Test
    public void selectNoticeTest() {
        BoardNoticeDto sourceDto = new BoardNoticeDto("긴급공지", "공지제목", "공지내용", "공지이미지", "Y", "1", 0, "702", "POST", "게시중");

        // DB에 저장
        assertTrue(noticeDao.insertNotice(sourceDto) == 1);
        assertTrue(noticeDao.count() == 1);
        // 0번째 게시글 조회
        int notcNum = noticeDao.selectNoticeAll().get(0).getNotcNum();

        // DB에서 조회
        BoardNoticeDto targetDto = noticeDao.selectNotice(notcNum);
        System.out.println(targetDto);

        // 정상적으로 조회 되는가
        assertTrue(Objects.equals(sourceDto, targetDto));
    }

    /*
        UPDATE TEST
        데이터 갱신 테스트
        ※ 실패하면 예외 또는 0 발생

        성공 조건
            1. 주문번호(PK)가 존재해야한다. (where ord_num = #{ord_num)
            2. NOT NULL 제약조건을 지켜야한다.
            3. 타입이 일치하거나, 자동 형변환이 가능해야한다.
            4. 길이를 초과하지 않아야한다.

        실패 조건
            1. 주문번호가 존재해야한다.                          결과 : 0
            2. NOT NULL 컬럼에 NULL이 들어간다.                 Error Code: 1048. Column '[컬럼명]' cannot be null
            3. 다른 타입이 들어가거나, 자동 형변환이 불가능하다.     Error Code: 1366. Incorrect integer value: '[값]' for column '[컬럼명]' at row 1
            4. 길이를 초과한다.                                Error Code: 1406. Data too long for column '[컬럼명]' at row 1
                                                            Error Code: 1264. Out of range value for column '[컬럼명]' at row 1

        시나리오 - 성공, 실패
            1. 1개 넣고 1개 업데이트
            2. 여러개 넣고 1개 업데이트
            3. 여러개 넣고 여러개 업데이트

        테스트 방식
            1. 비교기준값(DB에 업데이트 하려는 값)과 비교대상값(DB에 업데이트 한 값)이 일치하는지 비교
            2. 실제 업데이트가 된 개수가 예상한 결과와 같은지 확인
    */
    @DisplayName("게시물 수정 테스트")
    @Test
    public void updateNoticeTest() {
        BoardNoticeDto sourceDto = new BoardNoticeDto("긴급공지", "공지제목", "공지내용", "공지이미지", "Y", "1", 0, "702", "POST", "게시중");

        // DB에 저장
        assertTrue(noticeDao.insertNotice(sourceDto) == 1);
        assertTrue(noticeDao.count() == 1);
        int notcNum = noticeDao.selectNoticeAll().get(0).getNotcNum();

        // DB에서 조회
        sourceDto = noticeDao.selectNotice(notcNum);

        // 게시글 제목과 내용 수정
        String sourceTitle = "게시물 제목 수정 완료";
        String sourceContent = "게시물 내용 수정 완료";
        sourceDto.setNotcTitle(sourceTitle);
        sourceDto.setNotcContent(sourceContent);

        // 수정된 게시물 등록
        assertTrue(noticeDao.updateNotice(sourceDto) == 1);

        // 수정 됐는지 확인
        BoardNoticeDto targetDto = noticeDao.selectNotice(notcNum);
        assertTrue(Objects.equals(sourceDto, targetDto));
    }

    /*
        DELETE TEST
        데이터 삭제 테스트

        ※ 실패해도 에러가 발생하지 않음(결과가 0).

        성공조건
            1. 존재하는 주문번호로 삭제 시도

        실패조건
            2. 존재하지 않는 주문번호로 삭제 시도

        시나리오
            1. 1개 넣고 1개 삭제
            2. 여러개 넣고 1개 삭제
            3. 여러개 넣고 여러개 삭제

        테스트 방식
            1. 삭제 시도한 개수와 삭제된 결과 개수가 같은지 확인
            2. 삭제 시도한 값이 정상적으로 삭제 되었는지 확인
    */
    @DisplayName("게시물 삭제 테스트")
    @Test
    public void deleteNoticeTest() {
        // 저장할 객체
        BoardNoticeDto sourceDto = new BoardNoticeDto("긴급공지", "공지제목", "공지내용", "공지이미지", "Y", "1", 0, "702", "POST", "게시중");
        BoardNoticeDto sourceDto1 = new BoardNoticeDto("긴급공지", "공지제목", "공지내용", "공지이미지", "Y", "1", 0, "702", "POST", "게시중");

        // DB에 저장
        assertTrue(noticeDao.insertNotice(sourceDto) == 1);
        assertTrue(noticeDao.insertNotice(sourceDto1) == 1);
        assertTrue(noticeDao.count() == 2);


        // 게시물 번호로 삭제되는가
        int notcNum = noticeDao.selectNoticeAll().get(0).getNotcNum();
        assertTrue(noticeDao.deleteNotice(notcNum) == 1);

        // 하나의 게시물만 삭제됐는가
        assertTrue(noticeDao.count() == 1);
        BoardNoticeDto targetDto = noticeDao.selectNotice(noticeDao.selectNoticeAll().get(0).getNotcNum());
        assertTrue(Objects.equals(sourceDto, targetDto));
    }



    // 게시물 전체 삭제 테스트
    @DisplayName("전체 게시물 삭제 테스트")
    @Test
    public void deleteAllTest() {
        noticeDao.deleteNoticeAll();
        assertTrue(noticeDao.count() == 0);
    }

    @DisplayName("목록 조회 테스트")
    @Test
    public void selectAllTest() {
    }

    @Test
    public void queryTest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("offset", 1);
        map.put("pageSize", 10);
        map.put("category", "121");
        map.put("searchOption", "");
        map.put("keyword", "1");

//        List<BoardNoticeDto> list = noticeDao.queryTest(map);
//        for(BoardNoticeDto dto : list){
//            System.out.println(dto);
//        }
    }

    @Test
    public void test() {
        List<BoardNoticeDto> list = noticeDao.selectNoticeSpecial();
        for (BoardNoticeDto dto : list) {
            System.out.println(dto);

        }
    }

    @Test
    public void dummyDataInsert(){
        BoardNoticeDto dto = new BoardNoticeDto();
        for(int i=1; i<=200; i++){
            dto.setNotcType("" + i);
            dto.setNotcTitle("공지 제목 " + i);
            dto.setNotcContent("공지 내용 " + i);
            dto.setSpeclChk("N");
            dto.setWriter("작성자" + i);
            dto.setViewCnt(i);
            dto.setNotcStus1("702");
            dto.setNotcStus2("POST");
            noticeDao.insertNotice(dto);
        }
    }
}