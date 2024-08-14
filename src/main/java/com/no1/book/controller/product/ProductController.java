package com.no1.book.controller.product;

import com.no1.book.domain.product.AuthorDto;
import com.no1.book.domain.product.CategoryDto;
import com.no1.book.domain.product.PageHandler;
import com.no1.book.domain.product.ProductDto;
import com.no1.book.service.product.CategoryService;
import com.no1.book.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
1. 리스트부터 띄워 보자 v
2. 띄운 리스트를 선택한 기준에 따라 정렬하도록 해보자 v
3. 선택한 카테고리에 대해 필터링 되도록 해보자 (필터링 된 리스트가 앞서 구현한 정렬 기능과 호환이 되어야 한다)

 */

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

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

    @GetMapping("/list")
    public String list(Integer page, Integer pageSize, String sortKey, String sortOrder, String cateKey, Model m) throws Exception {
        if (page==null) page=1;
        if (pageSize==null) pageSize=10;
        if (sortKey == null) sortKey = "date";
        if (sortOrder == null) sortOrder = "desc";
//        if (cateKey == null) cateKey = "";

        Map map = new HashMap();
        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);
        map.put("sortKey", sortKey);
        map.put("sortOrder", sortOrder);
        map.put("cateKey", cateKey);

        List<ProductDto> prodList = productService.getSortedPage(map);
        List<CategoryDto> cateList = categoryService.getAllCategories();

        int filteredTotalCnt = productService.getFilteredAndSortedTotalSize(map);
        PageHandler pageHandler = new PageHandler(filteredTotalCnt, page, pageSize);

        m.addAttribute("prodList", prodList);
        m.addAttribute("cateList", cateList);
        m.addAttribute("ph", pageHandler);
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("sortKey", sortKey);
        m.addAttribute("sortOrder", sortOrder);
        m.addAttribute("cateKey", cateKey);

        return "product/productList";
    }

    @GetMapping("/detail")
    public String detail(String prodId, Model m) throws Exception {
        ProductDto pdto = productService.readProductDetail(prodId);
        m.addAttribute("pdto", pdto);

        AuthorDto adto = productService.getAuthorInfo(prodId);
        m.addAttribute("adto", adto);

        String cateCode = pdto.getCateCode();
        String cateName = productService.getCateName(cateCode);
        m.addAttribute("cateName", cateName);

        return "product/productDetail";
    }

    @GetMapping("/manage")
    public String manage(Model m) throws Exception {
        m.addAttribute("productDto", new ProductDto());

        List<CategoryDto> cateList = categoryService.getAllFinalCategories();
        m.addAttribute("cateList", cateList);

        return "product/manage";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute ProductDto productDto, Model m) throws Exception {
        productService.addProduct(productDto);

        return "redirect:/product/manage";
    }


}
