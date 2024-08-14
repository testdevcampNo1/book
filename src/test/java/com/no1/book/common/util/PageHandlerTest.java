package com.no1.book.common.util;

import com.no1.book.common.util.board.PageHandler;
import com.no1.book.common.util.board.SearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PageHandlerTest {

    @Test
    public void testPagingCalculation() {
        SearchCondition sc = new SearchCondition(1, 10);
        PageHandler ph = new PageHandler(105, sc);

        assertEquals(11, ph.getTotalPage(), "105개의 항목에 대해 페이지 크기가 10일 때 전체 페이지는 11이어야 합니다.");
        assertEquals(1, ph.getStartPage(), "시작 페이지는 1이어야 합니다.");
        assertEquals(10, ph.getEndPage(), "네비게이션 크기가 10일 때 종료 페이지는 10이어야 합니다.");
        assertTrue(ph.isShowNext(), "더 많은 페이지가 있을 경우 '다음' 버튼을 보여줘야 합니다.");
        assertFalse(ph.isShowPrev(), "첫 번째 페이지일 때 '이전' 버튼은 표시되지 않아야 합니다.");
    }

    @Test
    public void testPagingWithDifferentPage() {
        SearchCondition sc = new SearchCondition(11, 10);
        PageHandler ph = new PageHandler(105, sc);

        assertEquals(11, ph.getTotalPage(), "105개의 항목에 대해 페이지 크기가 10일 때 전체 페이지는 11이어야 합니다.");
        assertEquals(11, ph.getStartPage(), "11페이지일 때 시작 페이지는 11이어야 합니다.");
        assertEquals(11, ph.getEndPage(), "마지막 페이지일 때 종료 페이지는 11이어야 합니다.");
        assertFalse(ph.isShowNext(), "마지막 페이지일 때 '다음' 버튼은 표시되지 않아야 합니다.");
        assertTrue(ph.isShowPrev(), "'이전' 버튼은 첫 페이지가 아닐 때 표시되어야 합니다.");
    }

    @Test
    public void testQueryStringGeneration() {
        SearchCondition sc = new SearchCondition(5, 10, "A", "", "title");
        PageHandler ph = new PageHandler(50, sc);

        String expectedQuery = "?page=5&pageSize=10&searchOption=A&category=&keyword=title";
        assertEquals(expectedQuery, ph.getQueryString(5), "쿼리 문자열이 올바르게 생성되어야 합니다.");
    }

    @Test
    public void testPageNavigationDisplay() {
        SearchCondition sc = new SearchCondition(5, 10);
        PageHandler ph = new PageHandler(100, sc);

        String expectedNavigation = "< 1 2 3 4 [5] 6 7 8 9 10 >";
        assertEquals(expectedNavigation, ph.showPageNavi(), "페이지 네비게이션이 올바르게 표시되어야 합니다.");
    }

    @Test
    public void testSearchConditionDefaultConstructor() {
        SearchCondition sc = new SearchCondition();
        assertNull(sc.getPage(), "기본 생성자에서는 페이지가 null이어야 합니다.");
        assertNull(sc.getPageSize(), "기본 생성자에서는 페이지 크기가 null이어야 합니다.");
        assertNull(sc.getSearchOption(), "기본 생성자에서는 검색 옵션이 null이어야 합니다.");
        assertNull(sc.getCategory(), "기본 생성자에서는 카테고리가 null이어야 합니다.");
        assertNull(sc.getKeyword(), "기본 생성자에서는 키워드가 null이어야 합니다.");
    }

    @Test
    public void testSearchConditionCustomConstructor() {
        SearchCondition sc = new SearchCondition(1, 10, "option", "category", "keyword");
        assertEquals(1, sc.getPage(), "페이지는 1이어야 합니다.");
        assertEquals(10, sc.getPageSize(), "페이지 크기는 10이어야 합니다.");
        assertEquals("option", sc.getSearchOption(), "검색 옵션은 'option'이어야 합니다.");
        assertEquals("category", sc.getCategory(), "카테고리는 'category'이어야 합니다.");
        assertEquals("keyword", sc.getKeyword(), "키워드는 'keyword'이어야 합니다.");
    }
}
