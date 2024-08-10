package com.no1.book.service.product;

/* 서비스 기능 정리

1. 상품 등록
2. 상품 제거
3. 상품 업데이트

4. 상품 전체 개수 조회
5. 상품 전체 목록 조회 (가격, 별점 표시 -> 필수)
6. 상품 페이지에 맞는 목록 조회
7. 목록 정렬 (판매량, 등록 일자, 가격) -> 필수   // 각각의 메서드를 만드는게 나을까 하나의 메서드에 매개변수를 받는게 나을까?
8. 카테고리로 필터링 가능?
9. 선택하면 상세 페이지로 -> 필수

10. 리뷰 등록
11. 찜

 */

import com.no1.book.domain.product.ProductDto;
import com.no1.book.dao.product.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public int addProduct(ProductDto dto) throws Exception {
        return productDao.insert(dto);
    }


    @Override
    public int removeProduct(String prodId) throws Exception {
        return productDao.delete(prodId);
    }

    @Override
    public int updateProduct(ProductDto dto) throws Exception {
        return productDao.update(dto);
    }

    @Override
    public int getProductCount() throws Exception {
        return productDao.count();
    }

    @Override
    public List<ProductDto> getProductListSortedByPriceAsc() throws Exception {
        return productDao.sortByPriceAsc();
    }


    @Override
    public List<ProductDto> getProductListSortedByPriceDesc() throws Exception {
        return productDao.sortByPriceDesc();
    }

    @Override
    public List<ProductDto> getProductListSortedByTotalSalesAsc() throws Exception {
        return productDao.sortByTotalSalesAsc();
    }

    @Override
    public List<ProductDto> getProductListSortedByTotalSalesDesc() throws Exception {
        return productDao.sortByTotalSalesDesc();
    }

    @Override
    public List<ProductDto> getProductListSortedByRegDateAsc() throws Exception {
        return productDao.sortByRegDateAsc();
    }

    @Override
    public List<ProductDto> getProductListSortedByRegDateDesc() throws Exception {
        return productDao.sortByRegDateDesc();
    }

    @Override
    public List<ProductDto> getProductListByCategoryCode(String key) throws Exception {
        return productDao.filterByCategory(key);
    }

    @Override
    public List<ProductDto> getPage(Map map) throws Exception {
        return productDao.selectPage(map);
    }

    @Override
    public ProductDto readProductDetail(String prodId) throws Exception {
        ProductDto productDto = productDao.select(prodId);
        return productDto;
    }























    // ------------ 휴지통 -------------  (제출 전에 꼭 비워야 함)

    //    @Override
//    public List<ProductDto> sortItemsByPrice(int sortKey) throws Exception { // 0이면 내림차순, 1이면 오름차순
//        List<ProductDto> prodList = getProductList();
//
//        switch (sortKey) {
//            case 0:
//                // 가격을 기준으로 내림차순 정렬
//                Collections.sort(prodList, new Comparator<ProductDto>() {
//                    @Override
//                    public int compare(ProductDto p1, ProductDto p2) {
//                        return p2.getProdBasePrice() - p1.getProdBasePrice();
//                    }
//                });
//                break;
//            case 1:
//                // 가격을 기준으로 오름차순 정렬
//                Collections.sort(prodList, new Comparator<ProductDto>() {
//                    @Override
//                    public int compare(ProductDto p1, ProductDto p2) {
//                        return p1.getProdBasePrice() - p2.getProdBasePrice();
//                    }
//                });
//                break;
//
//        }
//        return prodList;
//    }
//
//    @Override
//    public List<ProductDto> sortItemsByTotalSales(int sortKey) throws Exception { // 0이면 내림차순, 1이면 오름차순
//        List<ProductDto> prodList = getProductList();
//
//        switch (sortKey) {
//            case 0:
//                // 가격을 기준으로 내림차순 정렬
//                Collections.sort(prodList, new Comparator<ProductDto>() {
//                    @Override
//                    public int compare(ProductDto p1, ProductDto p2) {
//                        return p2.getTotalSales() - p1.getTotalSales();
//                    }
//                });
//                break;
//            case 1:
//                // 가격을 기준으로 오름차순 정렬
//                Collections.sort(prodList, new Comparator<ProductDto>() {
//                    @Override
//                    public int compare(ProductDto p1, ProductDto p2) {
//                        return p1.getTotalSales() - p2.getTotalSales();
//                    }
//                });
//                break;
//
//        }
//        return prodList;
//    }
//
//    @Override
//    public List<ProductDto> sortItemsByRegDate(int sortKey) throws Exception {
//        List<ProductDto> prodList = getProductList();
//
//        switch (sortKey) {
//            case 0:
//                // 가격을 기준으로 내림차순 정렬
//                Collections.sort(prodList, new Comparator<ProductDto>() {
//                    @Override
//                    public int compare(ProductDto p1, ProductDto p2) {
//                        int numLocation[] = {0, 1, 2, 3, 5, 6, 8, 9, 11, 12, 14, 15, 17, 18}; // 문자열 형식의 날짜 데이터에서 숫자 데이터 자리 저장
//
//                        String p1Date = "";
//                        String p2Date = "";
//                        for (int i = 0; i < numLocation.length; i++) {
//                            p1Date += p1.getRegDate().charAt(numLocation[i]);
//                            p2Date += p1.getRegDate().charAt(numLocation[i]);
//                        }
//
//                        int p1
//
//                        return p2Date-p1Date;
//                    }
//                });
//                break;
//            case 1:
//                // 가격을 기준으로 오름차순 정렬
//                Collections.sort(prodList, new Comparator<ProductDto>() {
//                    @Override
//                    public int compare(ProductDto p1, ProductDto p2) {
//                        return p1.getRegDate() - p2.getRegDate();
//                    }
//                });
//                break;
//
//        }
//        return prodList;
//    }
}
