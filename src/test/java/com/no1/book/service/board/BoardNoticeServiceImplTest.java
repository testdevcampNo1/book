package com.no1.book.service.board;

import com.no1.book.domain.board.BoardNoticeDto;
import com.no1.book.dao.board.BoardNoticeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardNoticeServiceImplTest {
    BoardNoticeServiceImpl noticeService;
    BoardNoticeDao noticeDao;

    @Autowired
    public BoardNoticeServiceImplTest(BoardNoticeServiceImpl noticeService, BoardNoticeDao noticeDao) {
        this.noticeService = noticeService;
        this.noticeDao = noticeDao;
    }

    /*
            예외 발생 테스트
            1. DuplicateKeyException
            2. DataIntegrityViolationException
            3. BadSqlGrammarException
            4. ### DataAccessException
         */

    @DisplayName("예외 캐치 테스트")
    @Test
    public void exceptionCatchTest() {
        // 환경 초기화
        noticeDao.deleteNoticeAll();
        assertTrue(noticeDao.count() == 0);

        // 테스트 공지글 생성
        BoardNoticeDto sourceDto = new BoardNoticeDto("긴급공지", "공지제목", "공지내용", "공지이미지", "Y", "1", 0, "702", "POST", "게시중");

        // 공지 등록
        assertTrue(noticeService.addNotice(sourceDto) == 1);
        assertTrue(noticeDao.count() == 1);

        // 키가 중복되면 DuplicateKeyException 에러가 발생
        // 에러가 발생하는지 확인
//        assertThrows(DuplicateKeyException.class, () -> noticeService.addNotice(sourceDto));

        // 공지가 재등록 되지 않았는지 확인
//        assertTrue(noticeDao.count() == 1);

        // 무결성 제약조건을 위배하거나 형변환이 실패하면 DataIntegrityViolationException 예외 발생
        // 환경 초기화
        noticeDao.deleteNoticeAll();
        assertTrue(noticeDao.count() == 0);

        // 테스트 공지글 생성
        // null을 등록하면 에러가 발생하는지 확인
        assertThrows(DataIntegrityViolationException.class, () -> noticeService.addNotice(null));

        // 카운트가 증가하지 않았는지 확인
        assertTrue(noticeDao.count() == 0);

        // 각 필드가 null인 공지를 등록하면 에러가 발생하는지 확인
        assertThrows(DataIntegrityViolationException.class, () -> noticeService.addNotice(new BoardNoticeDto()));
        assertTrue(noticeDao.count() == 0);

        // 테이블이 없거나, SQL 구문이 잘못된 경우 BadSqlGrammarException 에러 발생하는지 확인
    }

      /*
        select for update 테스트
        1. 1번 Tx 시작(select 하며 Lock)
            1. 나머지 Tx 시작 읽기 불가
        2. 1번 Tx 수정
        3. 1번 Tx 종료
            1. 나머지 Tx 시작 읽기 가능

        1. 1번 Tx 시작(select Lock)
            1. 나머지 Tx 시작 수정 불가
        2. 1번 Tx 수정
        3. 1번 Tx 종료
            1. 나머지 Tx 시작 : 읽기 가능
     */
    @DisplayName("Select for Update 테스트")
    @Test
    public void selectForUpdateTest() {
        // 테이블 초기화
        noticeDao.deleteNoticeAll();
        assertTrue(noticeDao.count() == 0);

        // DB에 저장할 객체
        BoardNoticeDto insertDto = new BoardNoticeDto("00", "제목 없음", "내용 없음", "N", "test", 0, "702", "POST");

        // DB에 저장
        assertTrue(noticeDao.insertNotice(insertDto) == 1);

        // 여러 쓰레드 생성
        int threadCnt = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);

        // 모든 쓰레드가 동작했는지 검증할 Set
        Set<String> resultSet = new HashSet<>();

        // 모든 Tx가 Repeatable 하게 실행 되었는지 확인하기 위한 리스트
        List<String> startList = new ArrayList<>();
        List<String> endList = new ArrayList<>();


        // 각 쓰레드 실행
        for (int i = 0; i < threadCnt; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // 쓰레드 마다 개별로 Tx 실행
                    noticeService.selectForUpdateTx(resultSet, startList, endList);
                }
            });
        }

        // executorService 종료
        executorService.shutdown();

        // 모든 쓰레드 종료 까지 대기
        while (!executorService.isTerminated()) {
            // 대기
        }

        // 실행 횟수와 결과 개수가 같은가
        assertTrue(resultSet.size() == threadCnt);

        // 모든 Tx가 서로의 Tx에 침범하지 않았는지 확인
        // 시작 순서와 종료 순서가 같은가
        for (int i = 0; i < startList.size(); i++) {
            String start = startList.get(0);
            String end = endList.get(0);

            // 시작 쓰레드와 종료 쓰레드가 같은가
            assertTrue(start.equals(end));
        }
        // 모든 Tx의 실행 횟수와 종료 횟수가 같은가
        assertTrue(startList.size() == endList.size());
        assertTrue(startList.toString().equals(endList.toString()));
    }

//    // 업데이트 테스트
//    @DisplayName("Select for Update 테스트")
//    @Test
//    public void selectForUpdateTest1() throws InterruptedException {
//        // 테이블 초기화
//        noticeDao.deleteNoticeAll();
//        assertTrue(noticeDao.count() == 0);
//
//        // 데이터 1개 저장
//        BoardNoticeDto dto = new BoardNoticeDto();
//        Integer notcNum = 1;
//        String notcType = "01";
//        String notcTitle = "없음";
//        String notcContent = "content01";
//        String notcImg = "img01";
//        String speclChk = "N";
//        String writer = "writer01";
//        Integer viewCnt = 0;
//        String notcStus1 = "702";
//        String notcStus2 = "POST";
//        String codeName = "";
//
//        dto.setNotcNum(notcNum);
//        dto.setNotcType(notcType);
//        dto.setNotcTitle(notcTitle);
//        dto.setNotcContent(notcContent);
//        dto.setWriter(writer);
//        dto.setViewCnt(viewCnt);
//        dto.setNotcStus1(notcStus1);
//        dto.setNotcStus2(notcStus2);
//
//        int result = noticeDao.insertNotice(dto);
//        assertTrue(result == 1);
//
//        // 결과 저장 리스트 생성
//        List<BoardNoticeDto> resultList = new ArrayList<>();
//
//
//        // 쓰레드 2개 생성(first, second)
//        int numbersOfThreads = 10;
//        ExecutorService executorService = Executors.newFixedThreadPool(numbersOfThreads);
//
//        // 쓰레드 실행
//        for (int i = 0; i < numbersOfThreads; i++){
//            // 쓰레드 실행
//            int finalI = i;
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    // Lock 획득
//                    // 10초 슬립
//                    // 결과 저장
////                    System.out.println("My Name is : " + Thread.currentThread().getName());
//                    // select for update 호출
//                    BoardNoticeDto result = noticeService.selectForUpdateTx(dto, finalI);
//                    resultList.add(result);
//                    System.out.println("현재 " + finalI + "번이 수정중 = " + result.getNotcContent());
//                }
//            });
//        }
//
//        // executorService 종료
//        executorService.shutdown();
//        // 종료되지 않았으면 대기
//        while (!executorService.isTerminated()) {
//            // 대기
//        }
//    }
//

//    // 업데이트 테스트
//    @DisplayName("Select for Update 테스트")
//    @Test
//    public void selectForUpdateTest() throws InterruptedException {
//        // 테이블 초기화
//        noticeDao.deleteNoticeAll();
//        assertTrue(noticeDao.count() == 0);
//
//        // 데이터 1개 저장
//        BoardNoticeDto dto = new BoardNoticeDto();
//        Integer notcNum = 1;
//        String notcType = "01";
//        String notcTitle = "title01";
//        String notcContent = "content01";
//        String notcImg = "img01";
//        String speclChk = "N";
//        String writer = "writer01";
//        Integer viewCnt = 0;
//        String notcStus1 = "702";
//        String notcStus2 = "POST";
//        String codeName = "";
//
//        dto.setNotcNum(notcNum);
//        dto.setNotcType(notcType);
//        dto.setNotcTitle(notcTitle);
//        dto.setNotcContent(notcContent);
//        dto.setWriter(writer);
//        dto.setViewCnt(viewCnt);
//        dto.setNotcStus1(notcStus1);
//        dto.setNotcStus2(notcStus2);
//
//        int result = noticeDao.insertNotice(dto);
//        assertTrue(result == 1);
//
//        // 결과 저장 리스트 생성
//        List<BoardNoticeDto> resultList = new ArrayList<>();
//
//        // first select 후 10초 대기
//        // second select 결과 있는지 확인
//        Thread th1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // 수정
//                dto.setNotcTitle("제목 수정!!");
//                // select for update 호출
//                BoardNoticeDto result = noticeService.selectForUpdateTx(dto);
//
//                resultList.add(result);
//                System.out.println(Thread.currentThread() + " : ");
//                for (BoardNoticeDto noticeDto : resultList) {
//                    System.out.println(noticeDto);
//                }
//            }
//        });
//
//        Thread th2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // 수정
//                dto.setNotcContent("내용 수정!!");
//                // select for update 호출
//                BoardNoticeDto result = noticeService.selectForUpdateTx(dto);
//
//                resultList.add(result);
//                System.out.println(Thread.currentThread() + " : ");
//                for (BoardNoticeDto noticeDto : resultList) {
//                    System.out.println(noticeDto);
//                }
//            }
//        });
//
//        th1.setName("1번");
//        th2.setName("2번");
//
//        th1.start();
//        th2.start();
//    }

//    // 업데이트 테스트
//    @DisplayName("Select for Update 테스트")
//    @Test
//    public void selectForUpdateTest() {
//
//        // 결과 저장 리스트 생성
//        List<BoardNoticeDto> resultList = new ArrayList<>();
//
//        // first select 후 10초 대기
//        // second select 결과 있는지 확인
//
//        // 쓰레드 2개 생성(first, second)
//        int numbersOfThreads = 2;
//        ExecutorService executorService = Executors.newFixedThreadPool(numbersOfThreads);
//
//        // 쓰레드 실행
//        for (int i = 0; i < numbersOfThreads; i++){
//            // 1번째 쓰레드 Lock 획득
//            if (i==0){
//                // 쓰레드 실행
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Lock 획득
//                        // 10초 슬립
//                        // 결과 저장
//                    }
//                });
//            } else {    // 나머지 쓰레드 조회 시도
//                // 쓰레드 실행
//                executorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        // select 시도
//                        // 결과 저장
//                    }
//                });
//            }
//        }
//
//        // executorService 종료
//        executorService.shutdown();
//        // 종료되지 않았으면 대기
//        while (!executorService.isTerminated()) {
//            // 대기
//        }
//
//        // 결과 출력
//        System.out.println(resultList);
//    }
}