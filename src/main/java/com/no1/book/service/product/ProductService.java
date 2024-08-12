package com.no1.book.service.product;

import com.no1.book.domain.product.AuthorDto;
import com.no1.book.domain.product.ProductDto;

import java.util.List;
import java.util.Map;

/* 서비스 기능 정리

1. 상품 등록 v
2. 상품 제거 v
3. 상품 업데이트 v
(웹에서는 x)

4. 상품 전체 개수 조회 v
5. 상품 전체 목록 조회 (가격, 별점 표시 -> 필수) v
6. 상품 페이지에 맞는 목록 조회 v
7. 목록 정렬 (판매량, 등록 일자, 가격) -> 필수 v  // 각각의 메서드를 만드는게 나을까 하나의 메서드에 매개변수를 받는게 나을까? v
8. 카테고리로 필터링 가능? v
9. 선택하면 상세 페이지로 -> 필수

(위에꺼 다 하면)
10. 검색
11. 리뷰 등록 (리뷰 등록하면서 별점까지 부여하도록 -> 구매자들의 평균 별점을 목록에서 보여주는 것 까지)
12. 찜

 */

public interface ProductService {
    int addProduct(ProductDto dto) throws Exception;

    int removeProduct(String prodId) throws Exception;

    int updateProduct(ProductDto dto) throws Exception;

    int getProductCount() throws Exception;

    ProductDto readProductDetail(String prodId) throws Exception;

    List<ProductDto> getSortedPage(Map<String, Object> map) throws Exception;

    int getFilteredAndSortedTotalSize(Map<String, Object> map) throws Exception;

    AuthorDto getAuthorInfo(String prodId) throws Exception;

    String getCateName(String prodId) throws Exception;
}
