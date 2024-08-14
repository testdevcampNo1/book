package com.no1.book.controller.board;

import com.no1.book.common.exception.board.*;
import com.no1.book.common.util.board.PageHandler;
import com.no1.book.common.util.board.SearchCondition;
import com.no1.book.domain.board.BoardNoticeDto;
import com.no1.book.service.board.BoardNoticeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
/*
    사용자
    공지 목록 조회
    공지 내용 조회
    공지 검색
    공지 페이지 이동

    1. 404 : 잘못된 URL 요청
    2. 5xx : 서버 에러

    관리자
    공지 조회
    공지 등록
    공지 수정
    공지 삭제


    #공지게시판 예외 처리

    1. 없는 게시물 조회
        1. "삭제된 게시물 입니다." 오류 메시지 출력
        2. 현재 페이지 유지
    2. 관리자 권한이 없는 등록, 수정, 삭제 요청
        1. "권한이 없습니다" 오류 메시지 출력
        2. 현재 페이지 유지
    3. DB에서 발생하는 예외. 중복키, 무결성 위배
        1. "잘못된 데이터 입니다." 오류 메시지 출력
        2. 게시글 등록 폼 유지
    4. DB연결이 끊겨서 발생
        1. "서버 에러" 오류 메시지 출력
        2. 현재 페이지 유지
    5. 페이징 에러
        1. "서버 에러" 오류 메시지 출력
        2. 현재 페이지 유지
    6. 검색 에러(키워드, 유형)
        1. 검색 키워드, 유형 오류 : "검색할 수 없습니다." 오류 메시지 출력
        2.
 */

@RequestMapping({"/cscenter"})
@Controller
public class BoardNoticeController {
    BoardNoticeService noticeService;

    @Autowired
    public BoardNoticeController(BoardNoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 공지 목록 조회
    @GetMapping("/notice/list")
    public String getNoticePage(SearchCondition sc, Model m) {
        List<BoardNoticeDto> speciallList = new ArrayList<>();
        // 첫 페이지는 특별 공지 조회
        if(sc.getPage() == 1){
            speciallList = noticeService.findNoticeSpecial();
        }

        // 게시물 개수 조회
        int count = noticeService.countNoticeSearch(sc);

        // 페이지 핸들러 생성
        PageHandler ph = new PageHandler(count, sc);

        // 공지 목록 조회
        List<BoardNoticeDto> noticeList = noticeService.findNoticeSearch(ph);

        // 페이지 정보
        m.addAttribute("ph", ph);
        // 특별 공지
        m.addAttribute("specialList", speciallList);
        // 공지 목록
        m.addAttribute("noticeList", noticeList);

        // 공지 목록 이동
        return "board/noticeList";
    }

    // 게시글 상세 조회
    @GetMapping("/notice/{notcNum}")
    public String readNotice(@PathVariable int notcNum, SearchCondition sc, Model m) {
        // 게시글 번호로 조회
        BoardNoticeDto noticeDto = noticeService.findNotice(notcNum);

        // 게시글이 없으면 예외 발생
        if(noticeDto == null)
            throw new BoardNotFoundException("삭제된 공지 : " + notcNum);

        // 이전 페이지 정보
        m.addAttribute("sc", sc);
        // 게시글 모델에 담기
        m.addAttribute("notice", noticeDto);

        // 게시글 상세로 이동
        return "board/notice";
    }

    // 공지 등록 폼 이동
    @GetMapping("/notice")
    public String registerForm(SearchCondition sc, HttpSession session, Model m) {
        // 로그인한 아이디 가져오기
        String id = (String) session.getAttribute("id");

        // 관리자 권한 확인
        if(!isAdmin(id)){
            // 없으면 예외 발생
            throw new AccessDeniedException("권한이 없는 아이디 : " + id);
        }

        // 이전 페이지 정보
        m.addAttribute("sc", sc);
        // 작성 모드
        m.addAttribute("mode", "new");

        // 게시글 등록 폼으로 이동
        return "board/notice";
    }

    // 공지 등록
    @PostMapping("/notice")
    public String register(@Valid BoardNoticeDto boardNoticeDto, HttpSession session, RedirectAttributes rattr) {
        // 로그인한 아이디 가져오기
        String id = (String) session.getAttribute("id");

        // 관리자 권한 확인
        if(!isAdmin(id)){
            // 없으면 예외 발생
            throw new AccessDeniedException("권한이 없는 아이디 : " + id);
        }

        // 작성자 아이디
        boardNoticeDto.setWriter(id);

        // 공지 등록
        int rowCnt = noticeService.addNotice(boardNoticeDto);

        // 게시글 등록 실패시
        if(rowCnt != 1){
            throw new BoardCreationException("공지 등록 실패", boardNoticeDto);
        }

        // 등록 성공 메시지
        rattr.addFlashAttribute("msg", "WRT_OK");
        // 성공 시 리스트로 이동
        return "redirect:/cscenter/notice/list";
    }

    // 공지 수정
    @PostMapping("/notice/modify")
    public String modifyNotice(@Valid BoardNoticeDto updateNotice, SearchCondition sc, HttpSession session, RedirectAttributes rattr){
        // 로그인한 아이디 가져오기
        String id = (String) session.getAttribute("id");

        // 관리자 권한 확인
        if(!isAdmin(id)){
            throw new AccessDeniedException("권한이 없는 아이디 : " + id);
        }

        // 공지 수정
        int rowCnt = noticeService.modifyNotice(updateNotice);

        // 게시글 수정 실패시
        if(rowCnt != 1){
            throw new BoardUpdateException("공지 수정 실패");
        }

        // 수정 성공 메시지
        rattr.addFlashAttribute("msg", "MOD_OK");
        // 수정한 게시글 조회
        return "redirect:/cscenter/notice/" + updateNotice.getNotcNum() + sc.getQueryString(sc.getPage());
    }

    // 공지 삭제
    @GetMapping("/notice/remove/{notcNum}")
    public String removeNotice(@PathVariable int notcNum, HttpSession session, RedirectAttributes rattr){
        // 로그인한 아이디 가져오기
        String id = (String) session.getAttribute("id");

        // 관리자 권한 확인
        if(!isAdmin(id))
            throw new AccessDeniedException("권한이 없는 아이디 : " + id);

        // 공지 삭제
        int rowCnt = noticeService.removeNotice(notcNum);

        // 게시글 삭제 실패시
        if(rowCnt != 1)
            throw new BoardDeletionException("공지 삭제 실패");

        // 성공 시 리스트로 이동
        rattr.addFlashAttribute("msg", "DEL_OK");
        return "redirect:/cscenter/notice/list";
    }
    
    boolean isAdmin(String id){
        return true;
    }


    // 값 유효성 검사 예외
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public String methodArgumentNotValidExceptionCatcher(MethodArgumentNotValidException ex, RedirectAttributes rattr){
        rattr.addFlashAttribute("ex", ex);
        // 유효성 어김 메시지 전송
        rattr.addFlashAttribute("v_msg", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return "redirect:/cscenter/notice";
    }

    // 권한 없음 예외
    @ExceptionHandler({AccessDeniedException.class})
    public String accessDeniedExceptionCatcher(AccessDeniedException ex, RedirectAttributes rattr){
        rattr.addFlashAttribute("ex", ex);
        // 공지 목록으로 이동
        return "redirect:/cscenter/notice/list";
    }

    // 게시글 목록 없음 예외
    @ExceptionHandler({BoardListNotFoundException.class})
    public String boardListNotFoundExceptionCatcher(BoardListNotFoundException ex, RedirectAttributes rattr){
        rattr.addFlashAttribute("ex", ex);
        rattr.addFlashAttribute("msg", "LIST_ERR");
        return "board/noticeList";
    }

    // 게시글 없음 예외
    @ExceptionHandler({BoardNotFoundException.class})
    public String boardNotFoundExceptionCatcher(BoardNotFoundException ex, RedirectAttributes rattr){
        rattr.addFlashAttribute("ex", ex);
        // 없는 게시물 조회
        rattr.addFlashAttribute("msg", "READ_ERR");
        return "redirect:/cscenter/notice/list";
    }

    // 게시글 등록 예외, 중복 키 예외, 무결성 예외
    @ExceptionHandler({BoardCreationException.class, DuplicateKeyException.class, DataIntegrityViolationException.class})
    public String boardCreationExceptionCatcher(DataIntegrityViolationException ex, RedirectAttributes rattr){
        rattr.addFlashAttribute("ex", ex);
        // 게시글 작성 실패, 중복 키, 무결성 검사 실패
        rattr.addFlashAttribute("msg", "CRT_ERR");
        // 작성 중인 게시물 담기
        return "redirect:/cscenter/notice";
    }

    // DB 관련 모든 예외
    @ExceptionHandler({DataAccessException.class})
    public String DBExceptionCatcher(DataAccessException ex, RedirectAttributes rattr){
        rattr.addFlashAttribute("ex", ex);
        // DB 에러 발생
        rattr.addFlashAttribute("msg", "DB_ERR");
        return "redirect:/";
    }

    // 게시글 수정 예외
    @ExceptionHandler({BoardUpdateException.class})
    public String boardUpdateExceptionCatcher(BoardUpdateException ex, RedirectAttributes rattr){
        rattr.addFlashAttribute("ex", ex);
        // 게시글 수정 실패
        rattr.addFlashAttribute("msg", "MOD_ERR");
        return "redirect:/cscenter/notice/list";
    }

    // 게시글 삭제 예외
    @ExceptionHandler({BoardDeletionException.class})
    public String boardDeletionExceptionCatcher(BoardDeletionException ex, RedirectAttributes rattr){
        rattr.addFlashAttribute("ex", ex);
        // 게시글 삭제 실패
        rattr.addFlashAttribute("msg", "DEL_ERR");
        return "redirect:/cscenter/notice/list";
    }

    // 모든 예외 처리. 알 수 없는 예외
    @ExceptionHandler({Exception.class})
    public String exceptionCatcher(Exception ex, RedirectAttributes rattr){
        System.out.println("exceptionCatcher : " + ex.getClass());

        rattr.addFlashAttribute("ex", ex);
        // 알 수 없는 예외
        rattr.addFlashAttribute("msg", "UND_ERR");
        return "redirect:/";
    }
}
