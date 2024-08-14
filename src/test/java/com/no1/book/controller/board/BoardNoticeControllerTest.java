package com.no1.book.controller.board;

import com.no1.book.common.util.board.SearchCondition;
import com.no1.book.domain.board.BoardNoticeDto;
import com.no1.book.dao.board.BoardNoticeDao;
import com.no1.book.service.board.BoardNoticeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(BoardNoticeController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BoardNoticeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BoardNoticeService noticeService;

    @Autowired
    BoardNoticeDao noticeDao;

    /*
        Controller 테스트

        1. URL 매핑이 잘 되는지 확인
        2. 원하는 타입으로 데이터가 들어오는지 확인
        3. Input 데이터의 Validation이 잘 동작 하는지 확인
        4. 예상한 비즈니스 로직이 실행 됐는지 확인
        5. 응답 본문에 json 형태의 유효한 값으로 포함 되는지 확인
        6. 예외가 발생 했을 때 예상한 대로 처리 되는지 확인

     */

    @Test
    @DisplayName("공지 목록 조회 : 공지 100개가 있는 경우")
    public void noticeListSuccessTest() throws Exception {
        // # init
        // 테이블 초기화
        noticeDao.deleteNoticeAll();
        assertTrue(noticeDao.count() == 0);

        // # given
        // 공지 100개 생성
        int insertCnt = 100;
        BoardNoticeDto dto = new BoardNoticeDto("00", "", "content", "N", "writer", 0, "702", "POST");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < insertCnt; i++) {
            dto.setNotcTitle(sb.append("title").append(i).toString());
            noticeDao.insertNotice(dto);
            sb.setLength(0);
        }
        assertTrue(noticeDao.count() == insertCnt);

        // # when
        // # then
        mockMvc.perform(get("/cscenter/notice/list?page=1&pageSize=10"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("공지 목록 조회 : 공지가 없는 경우")
    public void noticeControllerTest() throws Exception {
        // # init
        // 테이블 초기화
        noticeDao.deleteNoticeAll();
        assertTrue(noticeDao.count() == 0);

        // # then
        mockMvc.perform(get("/cscenter/notice/list"))
                // 응답 성공
                .andExpect(status().isOk())
                // 모델에 담겼는지 확인
                .andExpect(model().attribute("searchCondition", new SearchCondition(1, 10)))
                // 빈 리스트인지 확인
                .andExpect(model().attribute("noticeList", new ArrayList<>()))
                // 문구가 생성 됐는지 확인
                .andExpect(content().string(Matchers.containsString("게시물이 없습니다.")))
                .andDo(print());
    }

    @Test
    @DisplayName("공지 전체 조회 실패")
    public void noticeListFailedTest() throws Exception {
        mockMvc.perform(get("/cscenter/notice/list"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("공지 상세 조회 Test")
    public void readNoticeTest() throws Exception {
        mockMvc.perform(get("/cscenter/notice/-1"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }



//    @Test
//    @DisplayName("공지 목록 조회 : 공지가 없는 경우")
//    public void noticeControllerTest() throws Exception {
//        // # init
//        // 테이블 초기화
//        noticeDao.deleteNoticeAll();
//        assertTrue(noticeDao.count() == 0);
//
//        // # then
//        mockMvc.perform(get("/cscenter/notice/list"))
//                // 응답 성공
//                .andExpect(status().isOk())
//                // 모델에 담겼는지 확인
//                .andExpect(model().attribute("searchCondition", new SearchCondition(1, 10)))
//                // 빈 리스트인지 확인
//                .andExpect(model().attribute("noticeList", new ArrayList<>()))
//                // 문구가 생성 됐는지 확인
//                .andExpect(content().string(Matchers.containsString("게시물이 없습니다.")))
//                .andDo(print());
//
//        System.out.println("mvcResult : " + mvcResult.getResponse().getContentAsString());
//        // # init
//        // 테이블 초기화
//        noticeDao.deleteNoticeAll();
//        assertTrue(noticeDao.count() == 0);
//
//        // # given
//        String page = "1";
//        String pageSize = "10";
//
//        // # when
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.get("/cscenter/notice/list")
//                        .param("page", page)
//                        .param("pageSize", pageSize)
//                        .contentType(MediaType.APPLICATION_JSON));
//
//        // # then
//        MvcResult mvcResult = resultActions
//                // 응답 성공
//                .andExpect(status().isOk())
//                // 모델에 담겼는지 확인
//                .andExpect(model().attribute("searchCondition", new SearchCondition(1, 10)))
//                // 빈 리스트인지 확인
//                .andExpect(model().attribute("noticeList", new ArrayList<>()))
//                // 문구가 생성 됐는지 확인
//                .andExpect(content().string(Matchers.containsString("게시물이 없습니다.")))
//                // 결과 출력
//                .andDo(print())
//                .andReturn();
////        System.out.println("mvcResult : " + mvcResult.getResponse().getContentAsString());

}