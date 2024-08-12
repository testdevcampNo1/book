package com.no1.book.dao.product;

import com.no1.book.domain.product.AuthorDto;
import com.no1.book.domain.product.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorDaoTest {

    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private ProductDao productDao;

    @Test
    void deletAllTest() {
        // 1. 데이터 선택 -> 모든 저자 데이터

        // 2. 데이터 저치 -> 삭제
        authorDao.deleteAll();

        // 3. 검증 -> count가 0이면 통과
        int count = authorDao.count();
        assertEquals(0, count);
    }

    @Test
    void insertTest() {
        // 1. 데이터 선택 -> 임의의 저자 dto
            // 1-1. 데이터 비우기
        authorDao.deleteAll();
            // 1-2. 저자정보 id가 AUTH_IMSI인 임의 데이터 생성
        AuthorDto dto = AuthorDto.builder()
                .authorInfoId("AUTH_IMSI")
                .authorName("김저자")
                .build();

        // 2. 데이터 처리 -> 삽입
        authorDao.insert(dto);

        // 3. 검증 -> 카운트가 1, id가 AUTH_IMSI인 저자의 이름이 "김저자"라면 통과
            // 3-1. 카운트
        assertEquals(1, authorDao.count());
        assertEquals("김저자", authorDao.select("AUTH_IMSI").getAuthorName());
    }

    @Test
    void getAtuhorInfoTest() throws Exception {
        // 1. 데이터 선택 -> 임의의 저자 dto
            // 1-1. 데이터 비우기
        productDao.deleteAll();
        authorDao.deleteAll();
            // 1-2. 저자 id가 AUTH_IMSI인 product와 저자 dto 생성
        ProductDto pdto = ProductDto.builder()
                .prodId("PROD_IMSI")
                .prodName("임시 상품")
                .authorInfoId("AUTH_IMSI")
                .build();
        AuthorDto adto = AuthorDto.builder()
                .authorInfoId("AUTH_IMSI")
                .authorBornDate("1971-07-13")
                .authorBio("저자의 삶")
                .authorName("김저자")
                .build();
            // 1-3. 삽입
        productDao.insert(pdto);
        authorDao.insert(adto);

        // 2. 데이터 처리 -> product의 저자정보 id와 일치하는 저자 정보 가져오기
        AuthorDto selectedAdto = authorDao.getAuthorInfo(pdto.getAuthorInfoId());

        // 3. 검증 -> selectedAdto의 이름이 "김저자"라면 통과
//        System.out.println(selectedAdto);
        assertEquals("김저자", selectedAdto.getAuthorName());
    }
}