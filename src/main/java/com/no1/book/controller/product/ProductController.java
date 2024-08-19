package com.no1.book.controller.product;

import com.no1.book.domain.product.AuthorDto;
import com.no1.book.domain.product.CategoryDto;
import com.no1.book.domain.product.PageHandler;
import com.no1.book.domain.product.ProductDto;
import com.no1.book.domain.product.SearchCondition;
import com.no1.book.service.product.AuthorService;
import com.no1.book.service.product.CategoryService;
import com.no1.book.service.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AuthorService authorService;

    @GetMapping("/list")
    public String list(HttpSession session, Integer page, String keyword, Integer pageSize, String sortKey, String sortOrder, String cateKey, Model m) throws Exception {

        // 권한이 있는 id인지 확인 후 권한이 있으면 모델에 넘기기 (관리자 페이지 용도)
        String id = (String) session.getAttribute("id");
        validateAdmin(id, m);

        // 페이징 정보 초기값 설정
        if (page == null) page = 1;
        if (pageSize == null) pageSize = 12;
        if (sortKey == null) sortKey = "date";
        if (sortOrder == null) sortOrder = "desc";
        if (keyword == null) keyword = "";

        // 페이징에 정보 맵에 저장
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("sortKey", sortKey);
        map.put("sortOrder", sortOrder);
        map.put("cateKey", cateKey);
        map.put("keyword", keyword);

        SearchCondition sc = new SearchCondition(map);

        // 한 페이지 정보 가져오기
        List<ProductDto> prodList = productService.getPage(sc);

        // 카테고리화된 상품의 크기 반환 후 페이지 핸들러에 전달
//        int filteredTotalCnt = productService.getFilteredAndSortedTotalSize(map);
        int getPageSize = productService.listSize(sc);
        PageHandler pageHandler = new PageHandler(getPageSize, page, pageSize);


        // 모든 카테고리 정보 가져오기 (카테고리 선택 버튼)
        List<CategoryDto> cateList = categoryService.getAllCategories();

        // (카테고리화, 정렬, 페이징이 된) 한 페이지의 상품 리스트
        m.addAttribute("prodList", prodList);
        // 전체 카테고리 정보
        m.addAttribute("cateList", cateList);
        // 페이지 핸들러
        m.addAttribute("ph", pageHandler);
        // 카테고리 키
        m.addAttribute("cateKey", cateKey);
        // 정렬 키와 정렬 순서 모델에 추가
        m.addAttribute("sortKey", sortKey);
        m.addAttribute("sortOrder", sortOrder);
        // 키워드
        m.addAttribute("keyword", keyword);

        return "product/productList";
    }

    private void validateAdmin(String id, Model m) {
        if (true) { // 권한 확인하는 조건
            m.addAttribute("admin", "admin");
        }
    }

    @GetMapping("/detail")
    public String detail(HttpServletRequest request, Integer custId, String prodId, Model m) throws Exception {
        // 클라이언트로부터 받아온 상품id로 상품 dto 읽어와서 모델에 담기 (상세 페이지에 출력 용도)
        ProductDto pdto = productService.readProductDetail(prodId);
        m.addAttribute("pdto", pdto);

        // 클라이언트로 받아온 상품id로 저자 dto 읽어와서 모델에 담기 (상세 페이지에 출력 용도)
        AuthorDto adto = productService.getAuthorInfo(prodId);
        m.addAttribute("adto", adto);

        // 상품의 카테고리 코드로 카테고리 네임 반환 후 모델에 담기 (상세 페이지에 출력 용도)
        String cateCode = pdto.getCateCode();
        String cateName = productService.getCateName(cateCode);
        m.addAttribute("cateName", cateName);

        // 세션 받아서 id 모델에 담기 (장바구니 이동 용도)
        HttpSession session = request.getSession();
        System.out.println("session = " + session.getId());
        m.addAttribute("custId", custId);

        return "product/productDetail";
    }

    @PostMapping("/detail")
    public void detail(@RequestBody Map map) throws Exception {

        String prodId = map.get("prodId").toString();
        int itemQty = Integer.parseInt(map.get("itemQty").toString());

        for (int i = 0; i < itemQty; i++) {
            productService.plusSales(prodId);
        }
    }


    @GetMapping("/manage")
    public String manage(Model m) throws Exception {
        // 모든 카테고리 정보 모델에 담기 (셀렉트 버튼에 띄우기 용)
        List<CategoryDto> cateList = categoryService.getAllFinalCategories();
        m.addAttribute("cateList", cateList);

        // 모든 저자 정보 모델에 담기 (셀렉트 버튼에 띄우기 용)
        List<AuthorDto> authList = authorService.getAllAuthorOrderedByName();
        m.addAttribute("authList", authList);

        return "product/manage";
    }

    @PostMapping("/manage/add")
    public String addProduct(@ModelAttribute ProductDto productDto) throws Exception {
        // 클라이언트로부터 받아온 상품정보를 db에 추가
        productService.addProduct(productDto);
        return "redirect:/product/manage";
    }

    @PostMapping("/manage/view")
    @ResponseBody
    public ProductDto viewProduct(@RequestParam("prodId") String prodId) throws Exception {
        // 클라이언트로부터 받아온 상품 id로 상품 조회
        return productService.select(prodId);
    }

    @PostMapping("/manage/update")
    public String updateProduct(@ModelAttribute ProductDto productDto) throws Exception {
        // 클라이언트로부터 받아온 상품정보로 업데이트
        productService.updateProduct(productDto);
        return "redirect:/product/manage";
    }

    @PostMapping("/manage/delete")
    public String deleteProduct(@RequestParam("prodId") String prodId) throws Exception {
        // 클라이언트로부터 받아온 상품 id로 해당 상품 삭제
        productService.removeProduct(prodId);
        return "redirect:/product/manage";
    }




    @ExceptionHandler(DataIntegrityViolationException.class)
    public String dataInteVioEx(Exception ex, Model m) {
        m.addAttribute("message", "데이터 무결성을 위반했습니다. 확인하고 다시 요청해주세요.");
        return "product/error";
    }
    @ExceptionHandler(SQLException.class)
    public String sqlEx(Exception ex, Model m) {
        m.addAttribute("message", "쿼리 실행 중 예외가 발생했습니다.");
        return "product/error";
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String mthArgNotValiEx(Exception ex, Model m) {
        m.addAttribute("message", "올바르지 않은 값을 입력했습니다. 확인하고 다시 요청해주세요.");
        return "product/error";
    }
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        System.err.println("예외 발생: " + ex.getClass().getName());
        ex.printStackTrace();

        model.addAttribute("message", "알 수 없는 에러가 발생했습니다.");

        return "product/error";
    }

}
