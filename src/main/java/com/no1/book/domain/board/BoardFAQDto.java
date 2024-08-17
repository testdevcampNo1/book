package com.no1.book.domain.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Objects;

@Data
public class BoardFAQDto implements BoardDto{
    Integer faqNum;
    String cateNum1;
    String cateNum2;
    String categoryName;

    @Size(min = 0, max = 50, message = "제목의 길이는 0~100 입니다.")
    @NotBlank(message = "제목은 필수 값입니다.")
    String faqTitle;

    @Size(min = 0, max = 2000, message = "내용의 길이는 2000을 초과할 수 없습니다.")
    @NotBlank(message = "내용은 필수 값입니다.")
    String faqContent;
    String img;
    String wrtDate;
    String writer;
    String bestFAQChk;
    Integer viewCnt;
    String faqStus1;
    String faqStus2;
    String faqStus;

    public BoardFAQDto() {}

    // 테스트 용 생성자
    public BoardFAQDto(String faqTitle, String faqContent, String writer) {
        cateNum1 = "701";
        cateNum2 = "01";
        this.faqTitle = faqTitle;
        this.faqContent = faqContent;
        this.writer = writer;
        faqStus1 = "703";
        faqStus2 = "POST";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardFAQDto that = (BoardFAQDto) o;
        return Objects.equals(getCateNum1(), that.getCateNum1()) && Objects.equals(getCateNum2(), that.getCateNum2()) && Objects.equals(getFaqTitle(), that.getFaqTitle()) && Objects.equals(getFaqContent(), that.getFaqContent()) && Objects.equals(getWriter(), that.getWriter()) && Objects.equals(getFaqStus1(), that.getFaqStus1()) && Objects.equals(getFaqStus2(), that.getFaqStus2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCateNum1(), getCateNum2(), getFaqTitle(), getFaqContent(), getWriter(), getFaqStus1(), getFaqStus2());
    }
}
