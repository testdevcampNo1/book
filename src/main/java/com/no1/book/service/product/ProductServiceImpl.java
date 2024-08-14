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

import com.no1.book.dao.product.AuthorDao;
import com.no1.book.domain.product.AuthorDto;
import com.no1.book.domain.product.ProductDto;
import com.no1.book.dao.product.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private AuthorDao authorDao;

    @Override
    public int addProduct(ProductDto dto) throws Exception {
        String prodId = "PROD" + (productDao.count() + 1);
        dto.setProdId(prodId);
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
    public ProductDto readProductDetail(String prodId) throws Exception {
        return productDao.select(prodId);
    }

    @Override
    public List<ProductDto> getSortedPage(Map<String, Object> map) throws Exception{
        return productDao.getFilteredAndSortedPage(map);
    }

    @Override
    public int getFilteredAndSortedTotalSize(Map<String, Object> map) throws Exception{
        return productDao.getFilteredAndSortedTotalSize(map);
    }

    @Override
    public AuthorDto getAuthorInfo(String prodId) throws Exception {
        return authorDao.getAuthorInfo(prodId);
    }

    @Override
    public String getCateName(String prodId) throws Exception {
        return productDao.getCateName(prodId);
    }

    @Override
    public int idChk(String prodId) throws Exception {
        return productDao.idChk(prodId);
    }


//    @Override
//    public List<ProductDto> getSortedPage(Map map) throws Exception {
//        String sortKey = (String) map.get("sortKey");
//        String sortOrder = (String) map.get("sortOrder");
//
//        switch (sortKey) {
//            case "sales":
//                if (sortOrder.equals("asc")) {
//                    return productDao.sortByTotalSalesAsc(map);
//                }
//                else if (sortOrder.equals("desc")) {
//                    return productDao.sortByTotalSalesDesc(map);
//                }
//                break;
//            case "date":
//                if (sortOrder.equals("asc")) {
//                    return productDao.sortByRegDateAsc(map);
//                }
//                else if (sortOrder.equals("desc")) {
//                    return productDao.sortByRegDateDesc(map);
//                }
//                break;
//            case "price":
//                if (sortOrder.equals("asc")) {
//                    return productDao.sortByPriceAsc(map);
//                }
//                else if (sortOrder.equals("desc")) {
//                    return productDao.sortByPriceDesc(map);
//                }
//                break;
//            default:
//                return productDao.selectPage(map);
//        }
//        return productDao.selectPage(map);
//
//    }

}