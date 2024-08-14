package com.no1.book.common.util.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
@ToString
public class PageHandler {
    private SearchCondition sc;
    // 한 페이지 당 게시물 갯수
//    private int pageSize;
    // 현재 페이지
//    private int page;
    // 페이지 네비게이션 사이즈
    public Integer navSize = 10;
    // 게시물의 총 갯수
    private Integer totalCnt;
    // 전체 페이지의 갯수
    private Integer totalPage;
    // 화면에 보여줄 첫 페이지
    private Integer startPage;
    // 화면에 보여줄 마지막 페이지
    private Integer endPage;
    // 이후를 보여 줄지의 여부
    private boolean showPrev;
    // 이전을 보여 줄지의 여부
    private boolean showNext;

    public PageHandler(){}

    public PageHandler(int totalCnt, Integer page) {
        this(totalCnt, new SearchCondition(page, 10));
    }

    public PageHandler(int totalCnt, Integer page, Integer pageSize) {
        this(totalCnt, new SearchCondition(page, pageSize));
    }

    public PageHandler(int totalCnt, SearchCondition sc) {
        this.totalCnt = totalCnt;
        this.sc = sc;

        doPaging(totalCnt, sc);
    }


    private void doPaging(int totalCnt, SearchCondition sc) {
        this.totalPage = totalCnt / sc.getPageSize() + (totalCnt % sc.getPageSize()==0? 0:1);
        this.sc.setPage(Math.min(sc.getPage(), totalPage));  // page가 totalPage보다 크지 않게
        this.startPage = (this.sc.getPage() - 1) / navSize * navSize + 1; // 11 -> 11, 10 -> 1, 15->11. 따로 떼어내서 테스트
        this.endPage = Math.min(startPage + navSize - 1, totalPage);
        this.showPrev = sc.getPage()!=1;
        this.showNext = sc.getPage()!=totalPage;
    }

    public int getPageSize(){
        return sc.getPageSize();
    }

    public int getPage() {
        return sc.getPage();
    }

    public String getKeyword(){
        return sc.getKeyword();
    }

    public String getSearchOption(){
        return sc.getSearchOption();
    }

    public String getCategory() {
        return sc.getCategory();
    }

    public int getOffset() {
        return (sc.page-1)*sc.pageSize;
    }

    public String getQueryString(int page) {
        // ?page=10&pageSize=10&option=A&keyword=title
        return UriComponentsBuilder.newInstance()
                .queryParam("page",     page)
                .queryParam("pageSize", this.sc.getPageSize())
                .queryParam("searchOption", this.sc.getSearchOption())
                .queryParam("category", this.sc.getCategory())
                .queryParam("keyword", this.sc.getKeyword())
                .build().toString();
    }

    public String showPageNavi() {
        StringBuilder pageNum = new StringBuilder();
        for (int i = startPage; i <= endPage; i++) { // 수정된 부분
            if (sc.page != i) {
                pageNum.append(i).append(" ");
            } else {
                pageNum.append("[").append(i).append("] ");
            }
        }
        return (showPrev ? "< " : "") + pageNum + (showNext ? ">" : "");
    }
}
