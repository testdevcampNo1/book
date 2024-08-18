package com.no1.book.controller.board;

import com.no1.book.common.exception.board.*;
import com.no1.book.common.util.board.BoardPageHandler;
import com.no1.book.common.util.board.BoardSearchCondition;
import com.no1.book.domain.board.BoardFAQDto;
import com.no1.book.domain.board.CategoryFAQDto;
import com.no1.book.service.board.BoardFAQServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigInteger;
import java.util.List;
import java.util.SortedMap;

@Controller
@RequestMapping("/cscenter/faq")
public class BoardFAQController {
    BoardFAQServiceImpl boardFAQService;

    @Autowired
    public BoardFAQController(BoardFAQServiceImpl boardFAQService) {
        this.boardFAQService = boardFAQService;
    }

    // 목록 조회(검색)
    @GetMapping("/list")
    public String getFAQList(BoardSearchCondition sc, Model m) {
        // 검색 결과 수 조회
        int count = boardFAQService.getSearchCount(sc);

        // 페이지 핸들러 생성
        BoardPageHandler ph = new BoardPageHandler(count, sc);

        // 카테고리 조회
        List<CategoryFAQDto> categoryList = boardFAQService.getCategory();

        // 목록 조회
        List<BoardFAQDto> faqList = boardFAQService.getFAQPage(ph);


        // 모델에 저장
        m.addAttribute("ph", ph);
        m.addAttribute("categoryList", categoryList);
        m.addAttribute("faqList", faqList);

        // 게시글 목록 페이지로 이동
        return "/board/faqList";
    }

    // 등록 폼 이동
    @GetMapping("/write")
    public String registerForm(@SessionAttribute(name = "auth", required = false) String auth, Model m) {
        // 관리자가 아닌가
        if (!isAdmin(auth))
            // 권한 없음 예외 발생
            throw new AccessDeniedException("ACC_ERR");

        // 게시글 작성 모드
        m.addAttribute("mode", "new");
        // 게시글 작성 폼으로 이동
        return "/board/faqForm";
    }

    // 등록
    @PostMapping("/write")
    public String register(@SessionAttribute(name = "id", required = false) String id, BoardFAQDto boardFAQDto) {
        //세션의 아이디 저장
        id = "asdf";
        boardFAQDto.setWriter(id);
        // 게시글 저장
        int result = boardFAQService.addFAQ(boardFAQDto);

        // 결과가 0보다 작거나 같으면 실패
        if (result <= 0)
            // 게시글 생성 에러 발생
            throw new BoardCreationException("CRE_ERR", boardFAQDto);

        // 성공 시 게시글 목록으로 이동
        return "redirect:/cscenter/faq/list";
    }

    // 수정
    @PostMapping("/modify")
    public String modify(@SessionAttribute(name = "auth", required = false) String auth, BoardFAQDto boardFAQDto) {
        // 관리자가 아닌가
        if (!isAdmin(auth))
            // 권한 없음 예외 발생
            throw new AccessDeniedException("ACC_ERR");

        // 게시글 수정
        // 실패 시 게시글 목록으로 이동
        boardFAQService.modifyFAQ(boardFAQDto);

        // 성공 시 해당 게시글 페이지로 이동
        return "redirect:/cscenter/faq/list";
    }

    // 삭제
    @GetMapping("/remove")
    public String remove(@SessionAttribute(name = "auth", required = false) String auth, Integer faqNum) {
        // 관리자가 아닌가
        if (!isAdmin(auth))
            // 권한 없음 예외 발생
            throw new AccessDeniedException("ACC_ERR");

        // 게시글 번호로 삭제 시동
        int result = boardFAQService.removeFAQ(faqNum);

        // 결과가 0보다 작거나 같으면 실패
        if (result <= 0)
            // 게시글 삭제 예외 발생
            throw new BoardDeletionException("DEL_ERR");

        // 삭제 성공 시 게시글 목록으로 이동
        return "redirect:/cscenter/faq/list";
    }

    public boolean isAdmin(String auth) {
//        return auth.equals("Admin");
        return true;
    }

    // db 및 서버 에러
    @ExceptionHandler({Exception.class})
    public String exceptionCatcher(Exception ex, RedirectAttributes rattr) {
        rattr.addFlashAttribute("msg", "DB_ERR");
        return "redirect:/cscenter/faq/list";
    }

    // # redirect 게시글 목록으로 이동
    // 관리자 권한이 없음
    @ExceptionHandler({AccessDeniedException.class, })
    public String accessDeniedExceptionCatcher(AccessDeniedException e, RedirectAttributes rattr) {
        rattr.addFlashAttribute("msg", e.getMessage());
        return "redirect:/cscenter/faq/list";
    }

    // # redirect 게시글 목록으로 이동
    // 게시글 삭제 실패
    // 게시글 수정 실패
    @ExceptionHandler({BoardDeletionException.class, BoardUpdateException.class})
    public String boardListNotFoundExceptionCatcher(BoardException ex, RedirectAttributes rattr) {
        // 메시지 저장
        rattr.addFlashAttribute("msg", ex.getMessage());
        // 게시글 목록으로 이동
        return "redirect:/cscenter/faq/list";
    }

    // # redirect 게시글 등록 폼으로 이동
    // 게시글 등록 실패
    @ExceptionHandler({BoardCreationException.class})
    public String boardCreationUpdateExceptionCatcher(BoardException e, RedirectAttributes rattr) {
        rattr.addFlashAttribute("msg", e.getMessage());
        rattr.addFlashAttribute("faq", e.getBoardDto());

        return "redirect:/cscenter/faq/write";
    }

    // 게시글 목록 조회 실패 시
//    @ExceptionHandler({BoardListNotFoundException.class})
//    public String BoardListNotFoundExceptionCatcher(BoardException ex, Model m) {
//        // 게시글 목록 없음
//        m.addAttribute("msg", ex.getMessage());
//        // 게시글 목록 페이지로 이동
//        return "/board/faqList";
//    }
}
