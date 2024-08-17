package com.no1.book.domain.board;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Objects;

@Data
public class BoardNoticeDto implements BoardDto {
    Integer notcNum;
    String notcType;

    @Size(min = 0, max = 50, message = "제목의 길이는 0~50 입니다.")
    @NotBlank(message = "제목은 필수 값입니다.")
    String notcTitle;

    @Size(min = 0, max = 2000, message = "내용의 길이는 2000자를 초과할 수 없습니다.")
    @NotBlank(message = "내용은 필수 값입니다.")
    String notcContent;

    String notcImg;
    String speclChk = "N";
    String writer;
    Integer viewCnt;
    String wrtDate;
    String notcStus1;
    String notcStus2;
    String codeName;

    public BoardNoticeDto() {}

    public BoardNoticeDto(String notcType, String notcTitle, String notcContent, String speclChk, String writer, Integer viewCnt, String notcStus1, String notcStus2) {
        this.notcType = notcType;
        this.notcTitle = notcTitle;
        this.notcContent = notcContent;
        this.speclChk = speclChk;
        this.writer = writer;
        this.viewCnt = viewCnt;
        this.notcStus1 = notcStus1;
        this.notcStus2 = notcStus2;
    }

    public BoardNoticeDto(String notcType, String notcTitle, String notcContent, String notcImg, String speclChk, String writer, int viewCnt, String notcStus1, String notcStus2, String codeName) {
        this.notcType = notcType;
        this.notcTitle = notcTitle;
        this.notcContent = notcContent;
        this.notcImg = notcImg;
        this.speclChk = speclChk;
        this.writer = writer;
        this.viewCnt = viewCnt;
        this.notcStus1 = notcStus1;
        this.notcStus2 = notcStus2;
        this.codeName = codeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardNoticeDto noticeDto = (BoardNoticeDto) o;
        return Objects.equals(getNotcType(), noticeDto.getNotcType()) && Objects.equals(getNotcTitle(), noticeDto.getNotcTitle()) && Objects.equals(getNotcContent(), noticeDto.getNotcContent()) && Objects.equals(getNotcImg(), noticeDto.getNotcImg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNotcType(), getNotcTitle(), getNotcContent(), getNotcImg());
    }
}
