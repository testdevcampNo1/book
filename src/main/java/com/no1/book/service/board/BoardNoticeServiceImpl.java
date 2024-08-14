package com.no1.book.service.board;

import com.no1.book.common.util.board.BoardPageHandler;
import com.no1.book.common.util.board.SearchCondition;
import com.no1.book.domain.board.BoardNoticeDto;
import com.no1.book.dao.board.BoardNoticeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class BoardNoticeServiceImpl implements BoardNoticeService {
    BoardNoticeDao noticeDao;

    @Autowired
    public BoardNoticeServiceImpl(BoardNoticeDao noticeDao) {
        this.noticeDao = noticeDao;
    }

    // 전체 게시글 개수
    @Override
    public int count() {
        return noticeDao.count();
    }

    // 검색 결과 개수
    @Override
    public int countNoticeSearch(SearchCondition sc) {
        return noticeDao.countNoticeSearch(sc);
    }

    // 공지 전체 목록 조회
    @Override
    public List<BoardNoticeDto> findNoticeList(){
        return noticeDao.selectNoticeAll();
    }

    // 특별 공지 목록 조회
    @Override
    public List<BoardNoticeDto> findNoticeSpecial() {
        return noticeDao.selectNoticeSpecial();
    }

    // 페이지 결과 조회
    @Override
    public List<BoardNoticeDto> findNoticePage(BoardPageHandler ph) {
        return noticeDao.selectNoticePage(ph);
    }

    // 검색 결과 조회
    @Override
    public List<BoardNoticeDto> findNoticeSearch(BoardPageHandler ph) {
        return noticeDao.selectNoticeSearch(ph);
    }

    // 공지 상세 조회
    @Override
    public BoardNoticeDto findNotice(int notcNum) {
        return noticeDao.selectNotice(notcNum);
    }

    @Transactional
    public void selectForUpdateTx(Set<String> resultSet, List<String> startList, List<String> endList){
        // Select : 저장한 원본으로 조회
        int notcNum = noticeDao.selectNoticeAll().get(0).getNotcNum();

        // Tx 시작
        BoardNoticeDto selectDto = noticeDao.selectNotice(notcNum);

        // Tx 시작 목록에 저장
        startList.add(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getName() + "번 Tx 시작");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 제목 수정
        selectDto.setNotcTitle(Thread.currentThread().getName());

        // Update : 수정
        // Tx 종료
        noticeDao.updateNotice(selectDto);

        // Tx 종료 목록에 저장
        endList.add(Thread.currentThread().getName());

        // 수정 내용 조회
        BoardNoticeDto updateDto = noticeDao.selectNotice(selectDto.getNotcNum());
        resultSet.add(updateDto.getNotcTitle());

        // Tx 종료
        System.out.println(Thread.currentThread().getName() + "번 Tx 종료");
    }

    // 공지 수정
    @Override
    @Transactional
    public int modifyNotice(BoardNoticeDto boardNoticeDto) {
        // 공지 번호로 원본 조회
        BoardNoticeDto noticeDto = noticeDao.selectNotice(boardNoticeDto.getNotcNum());

        // 변경된 내용 수정
        noticeDto.setNotcTitle(boardNoticeDto.getNotcTitle());
        noticeDto.setNotcContent(boardNoticeDto.getNotcContent());
        noticeDto.setSpeclChk(boardNoticeDto.getSpeclChk());

        return noticeDao.updateNotice(noticeDto);
    }

    // 공지 삭제
    @Override
    public int removeNotice(int notcNum) {
        return noticeDao.deleteNotice(notcNum);
    }

    // 공지 등록
    @Override
    public int addNotice(BoardNoticeDto boardNoticeDto) {
        return noticeDao.insertNotice(boardNoticeDto);
    }


//    @Transactional
//    public BoardNoticeDto selectForUpdateTx(BoardNoticeDto boardNoticeDto, int threadNum) {
//        BoardNoticeDto beforeDto = noticeDao.selectNotice(boardNoticeDto.getNotcNum());
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        // 업데이트
//        boardNoticeDto.setNotcTitle(threadNum + "번 수정");
//        boardNoticeDto.setNotcContent("직전 : " + beforeDto.getNotcTitle());
//
//        int result = noticeDao.updateNotice(boardNoticeDto);
//
//        if (result != 1)
//            throw new BoardUpdateException("수정 실패!!");
//        return noticeDao.selectNotice(boardNoticeDto.getNotcNum());
//    }
}
