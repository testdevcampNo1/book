package com.no1.book.dao.product;

import com.no1.book.domain.product.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/*
-- 테스트의 3 단계 --
1. 어떤 데이터를 입력 할 것인지
2. 데이터에 어떤 처리를 할 것인지
3. 확인을 어떻게  할 것인지
이 세 가지 단계가 하나의 Test 안에서 일어남

+ 모든 테스트 코드는 독립적으로 작성할 것
 */

@SpringBootTest
class ProductDaoTest {
    @Autowired
    private ProductDao productDao;

    // 전체 삭제 테스트
    @Test
    void deleteAll() throws Exception {
        // 1단계 데이터 선택 -> product의 모든 데이터

        // 2단계 데이터 처리 -> 삭제
        productDao.deleteAll();

        // 3단계 검증 -> count가 0이면 통과
        int count = productDao.count();
        assertEquals(0, count);
    }

    // 삭제 테스트 1
    @Test
    void deleteTest1() throws Exception {
        // 1단계 데이터 선택 -> prodId가 PROD_IMSI인 데이터
        // DB에 ProdId가 PROD_IMSI인 데이터가 있다면 그거 그대로 사용하고 없다면 추가로 삽입
        ProductDto dto = productDao.select("PROD_IMSI");
        if (dto == null) {
            dto = ProductDto.builder()
                    .prodId("PROD_IMSI")
                    .build();
            productDao.insert(dto);
        }

        // 2단계 데이터 처리 -> 제거
        int deletedRow  = productDao.delete("PROD_IMSI");

        // 3단계 검증 1차 -> deletedRow가 1이면 통과
        assertEquals(1, deletedRow);
        // 3단계 검증 2차 -> prodId가 PROD_IMSI인 dto가 null이면 통과
        ProductDto selectedDto = productDao.select("PROD_IMSI");
        assertEquals(null, selectedDto);
    }

    // 삽입 테스트 1 (일반적인 경우)
    @Test
    void insertTest() throws Exception {
        // 1단계: 데이터 선택 -> 삽입할 새로운 Dto
            // 1-1단계 : 삽입할 데이터의
        // prodId와 동일한 데이터 제거
        productDao.delete("PROD_IMSI");
            // 1-2단계 : 새로로운 Dto 생성
        ProductDto dto = ProductDto.builder()
                .prodId("PROD_IMSI")
                .isEbook(false)
                .prodName("테스트 상품")
                .prodBasePrice(20000)
                .discRate(10)
                .totalSales(0)
                .tableOfContent("테스트 목차")
                .smry("테스트 요약")
                .pblcr("테스트 출판사")
                .pblcrReview("테스트 리뷰")
                .imageId("테스트 이미지")
                .isbn("1234567890123")
                .pblshDate("2023-08-07")
                .totalPages("200")
                .totalBooks("30")
                .trlr("테스트 트레일러")
                .dawnDeliChk("N")
                .authorInfoId("AUTH100")
                .ordChkCode("AVBL")
                .codeType("202")
                .build();

        // 2단계 데이터 처리 -> 삽입
        productDao.insert(dto);

        // 3단계 검증 -> 조회해서 몇몇 속성들이 일치하는지 확인
        ProductDto insertedDto = productDao.select("PROD_IMSI");
            // 3-1단계 불러온 Dto의 기본 가격이 20000원이면 통과
        assertEquals(20000, insertedDto.getProdBasePrice());
            // 3-2단계 불러온 목차가 "테스트 목차"라면 통과
        assertEquals("테스트 목차", insertedDto.getTableOfContent());
            // 3-3단계 불러온 판매가격이 18000원이면 통과 (계산된 컬럼)
        assertEquals(18000, insertedDto.getSalePrice());

    }

    // 삽입 테스트 2 (Not Null에 값을 안채웠지만 Dto의 Default로 값이 들어가는 경우)
    @Test
    void insertTest2() throws Exception {
        // 1단계: 데이터 선택 -> 삽입할 새로운 Dto
        // 1-1단계 : 삽입할 데이터의 prodId와 동일한 데이터 제거
        productDao.delete("PROD_IMSI");
        // 1-2단계 : 새로로운 Dto 생성
        ProductDto dto = ProductDto.builder()
                .prodId("PROD_IMSI")
                .prodName("테스트 상품")
                .prodBasePrice(20000)
                .discRate(10)
                .totalSales(0)
                .tableOfContent("테스트 목차")
                .smry("테스트 요약")
                .pblcr("테스트 출판사")
                .pblcrReview("테스트 리뷰")
                .imageId("테스트 이미지")
                .isbn("1234567890123")
                .pblshDate("2023-08-07")
                .totalPages("200")
                .totalBooks("30")
                .trlr("테스트 트레일러")
                .dawnDeliChk("N")
                .build();

        // 2단계 데이터 처리 -> 삽입
        productDao.insert(dto);

        // 3단계 검증 -> 조회해서 Not Null 데이터들이 default로 잘 들어갔는지 확인
        ProductDto insertedDto = productDao.select("PROD_IMSI");
        // 3-1단계 is_book이 false면 통과
        assertEquals(false, insertedDto.getIsEbook());
        // 3-2단계 authorInfoId가 "0"이면 통과
        assertEquals("0", insertedDto.getOrdChkCode());
        // 3-3단계 ordChkCode가 "0"이면 통과
        assertEquals("0", insertedDto.getCodeType());

    }


    // 전체 상품 갯수 카운트 테스트
    @Test
    void countTest() throws Exception {
        // 1단계 데이터 선택 -> product의 모든 데이터
            // 1-1단계 : 데이터 비우기
        productDao.deleteAll();
            // 1-2단계 : 임의 데이터 30개 채우기
        for (int i = 1; i < 31; i++) {
            ProductDto imsiDto = ProductDto.builder()
                    .prodId("PROD" + i)
                    .build();

            productDao.insert(imsiDto);
        }

        // 2단계 데이터 처리 -> 셈
        int count = productDao.count();

        // 3단계 검증 -> count가 30이면 통과
        assertEquals(30, count);
    }

    // 선택 테스트 1
    @Test
    void selecTest1() throws Exception {
        // 1단계 데이터 선택 -> PROD_IMSI인 데이터
        // DB에 prodId가 PROD_IMSI인 데이터가 있으면 그거 쓰고 없으면 추가
        ProductDto dto = productDao.select("PROD_IMSI");
        if (dto == null) {
            dto = ProductDto.builder()
                    .prodId("PROD_IMSI")
                    .build();
            productDao.insert(dto);
        }

        // 2단계 데이터 처리 -> 객체 선택
        ProductDto selectedDto = productDao.select("PROD_IMSI");

        // 3단계 검증 -> 선택한 객체의 prodId가 PROD_IMSI이면 통과
        assertEquals("PROD_IMSI", dto.getProdId());
    }

    // 업데이트 테스트 1
    @Test
    void updateTest1() throws Exception {
        // 1단계 데이터 선택 -> prodID가 PROD_IMSI인 임의 데이터
            // 1-1 단계 : DB에 prodId가 PROD_IMSI인 데이터가 있으면 그거 쓰고 없으면 생성 후 삽입
        ProductDto dto = productDao.select("PROD_IMSI");
        if (dto == null) {
            dto = ProductDto.builder()
                    .prodId("PROD_IMSI")
                    .isEbook(false)
                    .prodName("테스트 상품")
                    .prodBasePrice(20000)
                    .discRate(10)
                    .totalSales(0)
                    .tableOfContent("테스트 목차")
                    .smry("테스트 요약")
                    .pblcr("테스트 출판사")
                    .pblcrReview("테스트 리뷰")
                    .imageId("테스트 이미지")
                    .isbn("1234567890123")
                    .pblshDate("2023-08-07")
                    .totalPages("200")
                    .totalBooks("30")
                    .trlr("번역가1")
                    .dawnDeliChk("N")
                    .authorInfoId("AUTH100")
                    .ordChkCode("AVBL")
                    .codeType("202")
                    .build();
            productDao.insert(dto);
        }
            // 1-2단계 : 삽입된 PROD_IMSI 데이터를 select
        ProductDto selectedDto = productDao.select("PROD_IMSI");

        // 2단계 데이터 처리 -> 상품 기본가격 변경
        selectedDto.setProdBasePrice(30000);
        productDao.update(selectedDto);

        // 3단계 -> 업데이트한 항목이 일치하는지, 업데이트하지 않은 항목은 그대로인지 확인
        ProductDto testDto = productDao.select("PROD_IMSI");
            // 3-1 단계 : 상품의 가격이 30,000원이면 통과
        assertEquals(30000, testDto.getProdBasePrice());
            // 3-2 단계 : 상품의 이름이 "테스트 상품"이면 통과
        assertEquals("테스트 상품", testDto.getProdName());
            // 3-3 단계 : 상품의 요약이 "테스트 요약"이면 통과
        assertEquals("테스트 요약", testDto.getSmry());
    }

    // 업데이트 테스트 2 (할인율을 변경하면 할인가와 판매가격까지 알맞게 변경되는지)
    @Test
    void updateTest2() throws Exception {
        // 1단계 데이터 선택 -> prodID가 PROD_IMSI인 임의 데이터
        // 1-1 단계 : DB에 prodId가 PROD_IMSI인 데이터가 있든 없든 삭제 후 생성 및 삽입
        productDao.delete("PROD_IMSI");
        ProductDto dto = ProductDto.builder()
                .prodId("PROD_IMSI")
                .isEbook(false)
                .prodName("테스트 상품")
                .prodBasePrice(20000)
                .discRate(10)
                .totalSales(0)
                .tableOfContent("테스트 목차")
                .smry("테스트 요약")
                .pblcr("테스트 출판사")
                .pblcrReview("테스트 리뷰")
                .imageId("테스트 이미지")
                .isbn("1234567890123")
                .pblshDate("2023-08-07")
                .totalPages("200")
                .totalBooks("30")
                .trlr("테스트 번역가")
                .dawnDeliChk("N")
                .authorInfoId("AUTH100")
                .ordChkCode("AVBL")
                .codeType("202")
                .build();
        productDao.insert(dto);

        // 1-2단계 : 삽입된 PROD_IMSI 데이터를 select
        ProductDto selectedDto = productDao.select("PROD_IMSI");

        // 2단계 데이터 처리 -> 상품 할인율 변경
        selectedDto.setDiscRate(20);
        productDao.update(selectedDto);

        // 3단계 검증 1 -> 상품의 할인율이 20이면 통과
        ProductDto testDto = productDao.select("PROD_IMSI");
        assertEquals(20, testDto.getDiscRate());
        // 3단계 검증 2 -> 상품의 할인 가격이 4,000원이면 통과
        assertEquals(4000, testDto.getDiscPrice());
        // 3단계 검증 3 -> 상품의 판매 가격이 16,000원이면 통과
        assertEquals(16000, testDto.getSalePrice());
    }

    @Test
    void getFilteredAndCategoryTest1() throws Exception {
        productDao.deleteAll();
        // 1단계 데이터 선택 -> 임의의 30개 상품을 담고있는 리스트
            // 1-1 단계 : 데이터 비우기
        productDao.deleteAll();
            // 1-2 단계 : 임의의 30개 상품 데이터 삽입
        for (int i = 1; i <= 30; i++) {
            ProductDto dto = ProductDto.builder()
                    .prodId("PROD" + i)
                    .build();
            productDao.insert(dto);
        }

        Map map = new HashMap();
        map.put("offset", 1);
        map.put("pageSize", 10);
        map.put("sortKey", "price");
        map.put("sortOrder", "asc");
        map.put("cateKey", null);
        System.out.println("size : " + productDao.getFilteredAndSortedPage(map).size());
        for (int i = 0; i < 50; i++) {
            System.out.println(i + " : " + productDao.getFilteredAndSortedPage(map).get(i));
        }
    }

//    // 가격 오름차순 정렬 테스트
//    @Test
//    void sortByPriceAscTest() throws Exception {
//        // 1단계 데이터 선택 -> 임의의 30개 상품을 담고있는 리스트
//            // 1-1 단계 : 데이터 비우기
//        productDao.deleteAll();
//            // 1-2 단계 : 임의의 30개 상품 데이터 삽입
//        for (int i = 1; i <= 30; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + i)
//                    .prodBasePrice(5000 * ((int) (Math.random() * 19) + 1)) // 가격: 5,000원 ~ 100,000원 사이 5,000 단위 랜덤
//                    .build();
//            productDao.insert(dto);
//        }
//
//        // 2단계 데이터 처리 -> 가격 기준 오름차순 정렬
//        List<ProductDto> sortedProdList = productDao.sortByPriceAsc();
//
//        // 3단계 검증 -> 모든 상품에 대해서 현재 항목의 가격이 다음 항목의 가격보다 크지 않으면 통과
//        for (int i = 0; i < sortedProdList.size() - 1; i++) {
//            assertTrue(sortedProdList.get(i).getProdBasePrice() <= sortedProdList.get(i + 1).getProdBasePrice());
//            System.out.println("id : + " + sortedProdList.get(i).getProdId() + "price : " + sortedProdList.get(i).getProdBasePrice());
//        }
//    }
//
//    // 가격 내림차순 정렬 테스트
//    @Test
//    void sortByPriceDescTest() throws Exception {
//        // 1단계 데이터 선택 -> 임의의 30개 상품을 담고있는 리스트
//            // 1-1 단계 : 데이터 비우기
//        productDao.deleteAll();
//            // 1-2 단계 : 임의의 30개 상품 데이터 삽입
//        for (int i = 1; i <= 30; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + i)
//                    .prodBasePrice(5000 * ((int) (Math.random() * 19) + 1)) // 가격: 5,000원 ~ 100,000원 사이 5,000 단위 랜덤
//                    .build();
//            productDao.insert(dto);
//        }
//
//        // 2단계 데이터 처리 -> 가격 기준 내림차순 정렬
//        List<ProductDto> sortedProdList = productDao.sortByPriceDesc();
//
//        // 3단계 검증 -> 모든 상품에 대해서 현재 항목의 가격이 다음 항목의 가격보다 작지 않으면 통과
//        for (int i = 0; i < sortedProdList.size() - 1; i++) {
//            assertTrue(sortedProdList.get(i).getProdBasePrice() >= sortedProdList.get(i + 1).getProdBasePrice());
//            System.out.println("id : + " + sortedProdList.get(i).getProdId() + "price : " + sortedProdList.get(i).getProdBasePrice());
//        }
//    }
//
//    // 총 판매량 오름차순 정렬 테스트
//    @Test
//    void sortByTotalSalesAscTest() throws Exception {
//        // 1단계 데이터 선택 -> 임의의 30개 상품을 담고있는 리스트
//            // 1-1 단계 : 데이터 비우기
//        productDao.deleteAll();
//            // 1-2 단계 : 임의의 30개 상품 데이터 삽입
//        for (int i = 1; i <= 30; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + i)
//                    .build();
//            productDao.insert(dto);
//
//            // 1-3 : 상품 데이터를 삽입할 떄 마다 총 판매량 랜덤으로 업데이트 (상품 insert할 때는 판매량 0이라 업데이트 해줘야 함)
//            dto.setTotalSales((int) (Math.random() * 1_000_000)); // 총 판매량: 0 ~ 1,000,000 사이 랜덤
//            productDao.update(dto);
//        }
//
//        // 2단계 데이터 처리 -> 총 판매량 기준 오름차순 정렬
//        List<ProductDto> sortedProdList = productDao.sortByTotalSalesAsc();
//
//        // 3단계 검증 -> 모든 상품에 대해서 현재 항목의 판매량이 다음 항목의 판매량보다 크지 않으면 통과
//        for (int i = 0; i < sortedProdList.size() - 1; i++) {
//            assertTrue(sortedProdList.get(i).getTotalSales() <= sortedProdList.get(i + 1).getTotalSales());
//            System.out.println("id : " + sortedProdList.get(i).getProdId() + ", total sales : " + sortedProdList.get(i).getTotalSales());
//        }
//    }
//
//    // 총 판매량 내림차순 정렬 테스트
//    @Test
//    void sortByTotalSalesDescTest() throws Exception {
//        // 1단계 데이터 선택 -> 임의의 30개 상품을 담고있는 리스트
//            // 1-1 단계 : 데이터 비우기
//        productDao.deleteAll();
//            // 1-2 단계 : 임의의 30개 상품 데이터 삽입
//        for (int i = 1; i <= 30; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + i)
//                    .totalSales((int) (Math.random() * 1_000_000)) // 총 판매량: 0 ~ 1,000,000 사이 랜덤
//                    .build();
//            productDao.insert(dto);
//
//            // 1-3 : 상품 데이터를 삽입할 떄 마다 총 판매량 랜덤으로 업데이트 (상품 insert할 때는 판매량 0이라 업데이트 해줘야 함)
//            dto.setTotalSales((int) (Math.random() * 1_000_000)); // 총 판매량: 0 ~ 1,000,000 사이 랜덤
//            productDao.update(dto);
//        }
//
//        // 2단계 데이터 처리 -> 총 판매량 기준 내림차순 정렬
//        List<ProductDto> sortedProdList = productDao.sortByTotalSalesDesc();
//
//        // 3단계 검증 -> 모든 상품에 대해서 현재 판매량이 다음 항목의 판매량보다 작지 않으면 통과
//        for (int i = 0; i < sortedProdList.size() - 1; i++) {
//            assertTrue(sortedProdList.get(i).getTotalSales() >= sortedProdList.get(i + 1).getTotalSales());
//            System.out.println("id : " + sortedProdList.get(i).getProdId() + ", total sales : " + sortedProdList.get(i).getTotalSales());
//        }
//    }
//
//    // 등록 날짜 오름차순 정렬 테스트 -> 검증을 할 수 없어요
//    @Test
//    void sortByRegDateAscTest() throws Exception {
//        // 1단계 데이터 선택 -> 임의의 30개 상품을 담고있는 리스트
//            // 1-1 단계 : 데이터 비우기
//        productDao.deleteAll();
//            // 1-2 단계 : 임의의 30개 상품 데이터 삽입
//        for (int i = 1; i <= 30; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + i)
//                    .regDate("2023-08-" + (i % 30 + 1)) // 날짜를 임의로 설정
//                    .build();
//            productDao.insert(dto);
//        }
//
//        // 2단계 데이터 처리 -> 등록 날짜 기준 오름차순 정렬
//        List<ProductDto> sortedProdList = productDao.sortByRegDateAsc();
//
//        // 3단계 검증 -> 모든 상품에 대해서 현재 상품의 등록 날짜와 다음 상품의 등록 날짜를 compareTo로 비교해서 양수면 통과
//        for (int i = 0; i < sortedProdList.size() - 1; i++) {
//            assertTrue(sortedProdList.get(i).getRegDate().compareTo(sortedProdList.get(i + 1).getRegDate()) <= 0);
//            System.out.println("상품 id : " + sortedProdList.get(i).getProdId() + "상품 등록 날짜 : " + sortedProdList.get(i).getRegDate());
//        }
//    }
//
//    // 등록 날짜 내림차순 정렬 테스트
//    @Test
//    void sortByRegDateDescTest() throws Exception {
//        // 1단계 데이터 선택 -> 임의의 30개 상품을 담고있는 리스트
//            // 1-1 단계 : 데이터 비우기
//        productDao.deleteAll();
//            // 1-2 단계 : 임의의 상품 데이터 삽입
//        for (int i = 1; i <= 30; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + i)
//                    .regDate("2023-08-" + (i % 30 + 1)) // 날짜를 임의로 설정
//                    .build();
//            productDao.insert(dto);
//        }
//
//        // 2단계 데이터 처리 -> 등록 날짜 기준 내림차순 정렬
//        List<ProductDto> sortedProdList = productDao.sortByRegDateDesc();
//
//        // 3단계 검증 -> 모든 상품에 대해 현재 상품의 등록 날짜와 다음 상품의 등록 날짜를 compareTo로 비교해서 음수면 통과
//        for (int i = 0; i < sortedProdList.size() - 1; i++) {
//            assertTrue(sortedProdList.get(i).getRegDate().compareTo(sortedProdList.get(i + 1).getRegDate()) >= 0);
//            System.out.println("상품 id : " + sortedProdList.get(i).getProdId() + "상품 등록 날짜 : " + sortedProdList.get(i).getRegDate());
//        }
//    }

//    // 카테고리 필터링 테스트 (lv1 까지 잘 필터링 하는지)
//    @Test
//    void filterTest1() throws Exception {
//        // 1단계 데이터 선택 -> 모든 상품
//            // 1-1. : 데이터 비우기
//        productDao.deleteAll();
//            // 1-2. : 임의 데이터 50개 삽입
//        for (int i = 0; i < 50; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + (i + 1))
//                    .cateCode("0" + (int)(Math.random() * 2 + 1) + "0" +
//                            (int)(Math.random() * 3 + 1) + "0" + (int)(Math.random() * 5 + 1) ) // 010305 이런 형태로 랜덤 카테고리코드 부여
//                    .build();
//            productDao.insert(dto);
//        }
//
//        // 2단계 데이터 처리 -> 카테고리 키에 따른 리스트 필터링
//        List<ProductDto> filteredList =  productDao.filterByCategory("01%"); // 카테고리 id가 01로 시작하는 상품들만 필터링
//
//        // 3단계 -> filteredList 각 항목의 cateCode 앞 두자리가 "01"이면 통과
//        for (int i = 0; i < filteredList.size(); i++) {
//            assertEquals("01", filteredList.get(i).getCateCode().substring(0,2));
//            System.out.println("id : " + filteredList.get(i).getProdId() + "  , cateCode : " + filteredList.get(i).getCateCode());
//        }
//    }

//    // 카테고리 필터링 테스트 (lv2까지 잘 필터링 하는지)
//    @Test
//    void filterTest2() throws Exception {
//        // 1단계 데이터 선택 -> 모든 상품
//        // 1-1. : 데이터 비우기
//        productDao.deleteAll();
//        // 1-2. : 임의 데이터 50개 삽입
//        for (int i = 0; i < 50; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + (i + 1))
//                    .cateCode("0" + (int)(Math.random() * 2 + 1) + "0" +
//                            (int)(Math.random() * 3 + 1) + "0" + (int)(Math.random() * 5 + 1) ) // 010305 이런 형태로 랜덤 카테고리코드 부여
//                    .build();
//            productDao.insert(dto);
//        }
//
//        // 2단계 데이터 처리 -> 카테고리 키에 따른 리스트 필터링
//        List<ProductDto> filteredList =  productDao.filterByCategory("0201%"); // 카테고리 id가 0201로 시작하는 상품들만 필터링
//
//        // 3단계 -> filteredList 각 항목의 cateCode 앞 네자리가 "0201"이면 통과
//        for (int i = 0; i < filteredList.size(); i++) {
//            assertEquals("0201", filteredList.get(i).getCateCode().substring(0,4));
//            System.out.println("id : " + filteredList.get(i).getProdId() + "  , cateCode : " + filteredList.get(i).getCateCode());
//        }
//    }
//
//    // 카테고리 필터링 테스트 (lv3까지 잘 필터링 하는지)
//    @Test
//    void filterTest3() throws Exception {
//        // 1단계 데이터 선택 -> 모든 상품
//        // 1-1. : 데이터 비우기
//        productDao.deleteAll();
//        // 1-2. : 임의 데이터 50개 삽입
//        for (int i = 0; i < 50; i++) {
//            ProductDto dto = ProductDto.builder()
//                    .prodId("PROD" + (i + 1))
//                    .cateCode("0" + (int)(Math.random() * 2 + 1) + "0" +
//                            (int)(Math.random() * 3 + 1) + "0" + (int)(Math.random() * 5 + 1) ) // 010305 이런 형태로 랜덤 카테고리코드 부여
//                    .build();
//            productDao.insert(dto);
//        }
//
//        // 2단계 데이터 처리 -> 카테고리 키에 따른 리스트 필터링
//        List<ProductDto> filteredList =  productDao.filterByCategory("010203"); // 카테고리 id가 010203인 상품들만 필터링
//
//        // 3단계 -> filteredList 각 항목의 cateCode가 010203이면 통과
//        for (int i = 0; i < filteredList.size(); i++) {
//            assertEquals("010203", filteredList.get(i).getCateCode());
//            System.out.println("id : " + filteredList.get(i).getProdId() + "  , cateCode : " + filteredList.get(i).getCateCode());
//        }
//    }


}













    // --------------- 휴지통 --------------- (제출 전에 정리할 예정)

//    // 정렬 테스트 1 (날짜 오름차순)
//    @Test
//    void getListTest() throws Exception {
//        // 1단계 데이터 선택 -> Product의 모든 데이터
//            // 1-1 단계 : 데이터 비우기
//        productDao.deleteAll();
//            // 1-2 단계 : 임의 데이터 30개 채우기
//        for (int i = 1; i < 31; i++) {
//            ProductDto imsiDto = ProductDto.builder()
//                    .prodId("PROD" + i)
//                    .build();
//
//            productDao.insert(imsiDto);
//        }
//
//        // 2단계 데이터 처리 -> 리스트에 담기
//        List<ProductDto> productDtoList = productDao.sortByRegDateAsc();
//
//        // 3단계 검증 ->
//        assertEquals(30, productDtoList.size());
//    }


//    // 삽입 테스트 2
//    @Test
//    void insertTest2() throws Exception {
//        // 1단계 데이터 선택 -> 기존 데이터 지우고 새로운 30개의 dto
//        productDao.deleteAll();
//        for (int i = 1; i <= 30; i++) {
//            ProductDto dto = new ProductDto(
//                    "PROD" + i,
//                    (i % 5 != 0 ? false : true), // 5개 중 1개는 전자책
//                    "임시 제목" + i,
//                    5000 * ((int)(Math.random() * 19) + 1), // 가격 : 5,000원 ~ 100,000원 사이 5,000단위 랜덤
//                    (int)(Math.random() * 100000), // 판매량 : 0~100,000 사이 랜덤
//                    "CAT" + i,
//                    "AUTH" + i,
//                    "AVBL" + i,
//                    "202"
//            );
//
//            // 2단계 데이터 처리 -> 삽입
//            productDao.insert(dto);
//        }
//
//        // 3단계 검증 -> 30개 추가했으니 count가 30이면 통과
//        int count = productDao.count();
//        assertEquals(30, count);
//    }

    // 전체 선택 테스트
//    @Test
//    void selectAll() throws Exception {
//        // 1단계 데이터 선택 -> 모든 ProductDto를 담고있는 리스트
//        List<ProductDto> dtoList = productDao.selectAll();
//
//        // 2단계 데이터 처리 -> 리스트의 크기 구하기
//        int dtoLstSize = dtoList.size();
//
//        // 3단계 검증 -> 리스트의 크기가 30이면 통과 (앞선 테스트에서 데이터 30개로 맞춰놓음)
//        assertEquals(30, dtoLstSize);
//    }



