package com.no1.book.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchCondition {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer offset = 0;
    private String cateKey = "";
    private String keyword = "";
    private String sortKey = "date";
    private String sortOrder = "desc";


    public SearchCondition(Map<String, Object> map) {
        this.page = (Integer)map.getOrDefault("page", 1);
        this.pageSize = (Integer)map.getOrDefault("pageSize", 10);
        this.offset = (Integer)map.getOrDefault("offset", 0);
        this.cateKey = (String)map.getOrDefault("cateKey", "");
        this.keyword = (String)map.getOrDefault("keyword", "");
        this.sortKey = (String)map.getOrDefault("sortKey", "date");
        this.sortOrder = (String)map.getOrDefault("sortOrder", "desc");
    }
}