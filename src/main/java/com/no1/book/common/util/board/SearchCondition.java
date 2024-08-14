package com.no1.book.common.util.board;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.util.UriComponentsBuilder;

@Data
public class SearchCondition {
    Integer page;           // 페이지
    Integer pageSize;       // 페이지 사이즈
    String searchOption;    // 검색 옵션
    String category;        // 검색 카테고리
    String keyword;         // 검색 키워드

    public SearchCondition() {
        this(1, 10);
    }

    public SearchCondition(Integer page, Integer pageSize) {
        this(page, pageSize, "", "", "");
    }

    public SearchCondition(Integer page, Integer pageSize, String searchOption, String category, String keyword) {
        this.page = page;
        this.pageSize = pageSize;
        this.searchOption = searchOption;
        this.category = category;
        this.keyword = keyword;
    }

    public String getQueryString(int page) {
        // ?page=10&pageSize=10&option=A&keyword=title
        return UriComponentsBuilder.newInstance()
                .queryParam("page",     page)
                .queryParam("pageSize", getPageSize())
                .queryParam("searchOption", getSearchOption())
                .queryParam("category", getCategory())
                .queryParam("keyword", getKeyword())
                .build().toString();
    }
}
