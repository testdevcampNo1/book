package com.no1.book.controller.order;

import com.no1.book.domain.order.CartDto;
import com.no1.book.domain.order.CartProdDto;
import com.no1.book.domain.order.CartRequest;
import com.no1.book.service.order.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.MapUtils;

import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/list")
    public String read(Integer custId, Model m, HttpServletRequest request, HttpServletResponse response) throws Exception{

        // 세션에서 회원 여부 조회

        // 회원이면
        // DB에서 조회

        // 회원이 아니면
        // 세션에서 조회

        // 모델에 담기

        // cart View 리턴
        HttpSession session = request.getSession();
        //System.out.println("session = " + session);

//        if(!loginCheck(request))
//            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        try{
            List<CartProdDto> cartProducts  = cartService.read(custId);
            //System.out.println("cartProduct = " + cartProducts.toString());

            session.setAttribute("cartLists",cartProducts);

            m.addAttribute("custId",custId);
            m.addAttribute("cartProdDto", cartProducts);
        } catch(Exception e){
            e.printStackTrace();
        }

        //System.out.println("session.getAttributeNames = " + session.getAttributeNames());
        //System.out.println("session.getAttribute(\"cartLists\") = " + session.getAttribute("cartLists"));

        return "cart";
    }
//    @GetMapping("/list")
//    public String read(Integer custId, Model m, HttpServletRequest request, HttpServletResponse response) throws Exception{
//
//        HttpSession session = request.getSession();
//        System.out.println("session = " + session.getId());
//
//
////        if(!loginCheck(request))
////            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동
//
//        try{
//            List<CartProdDto> cartProducts  = cartService.read(custId);
//            System.out.println("cartProduct = " + cartProducts.size());
//
//            session.setAttribute("cartLists",cartProducts);
//
//            m.addAttribute("custId",custId);
//            m.addAttribute("cartProdDto", cartProducts);
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//
//        System.out.println("session.getAttributeNames = " + session.getAttributeNames());
//        System.out.println("session.getAttribute(\"cartLists\") = " + session.getAttribute("cartLists"));
//
//        return "cart";
//    }

    @GetMapping("/remove")
    public String remove(String prodId,  Model m, HttpSession session, RedirectAttributes rattr, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer custId = (Integer)session.getAttribute("custId");
                custId = 1; // 임의로 아이디값 설정

        System.out.println("remove prodId : " + prodId);

        try{
            Map map = new HashMap();
            map.put("custId", custId);
            map.put("prodId", prodId);

            int rowCnt = cartService.remove(map);
            if( rowCnt != 1 ) { throw new Exception("cart item remove error"); }

            rattr.addAttribute("custId", custId);
            rattr.addAttribute("msg","DEL_OK");

        }catch(Exception e){
            e.printStackTrace();
            rattr.addAttribute("msg","DEL_ERR");
        }

        return "redirect:/cart/list";
    }

    @PostMapping("/add")
    @ResponseBody
    public  Map<String, Object> addItem(@RequestBody CartDto reqDto, HttpServletRequest request, HttpServletResponse response) throws Exception{

        Map map = new HashMap();
        // 객체에 prodId와 itemQty가 담겼는지 확인하기

        // 회원일 경우 - DB에서

        HttpSession session = request.getSession();
//        Integer custId  = (Integer)session.getAttribute("custId");
        Integer custId  = 1; // 임의로 아이디값 설정

        CartDto dto = reqDto;
                dto.setCustId(custId);

        System.out.println("dto = " + dto.toString());

        List<CartProdDto> cartProducts  = cartService.read(custId);
        boolean hasItem  = false;

        String dtoProd = dto.getProdId();

        for(CartProdDto product : cartProducts){
            if (product.getProdId().equals(dtoProd)) {
                hasItem   = true;
                dto.setItemQty(product.getItemQty()+dto.getItemQty());
                break;
            }
        }


        // 장바구니에 상품이 존재하면 수량만 업데이트, 존재하지 않으면 상품 추가
        int ret;

        if(hasItem == true) { ret = cartService.updateItemQty(dto); }
        else                { ret = cartService.insertItem(dto); }

        if(ret == 1) { map.put("status", "success"); }
        else         { map.put("status", "fail"); }


        // 비회원일 경우 - 세션 상품목록에서




        return map;  // status : successs, fail
    }



    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateQuantity(@RequestBody CartDto dto,HttpServletRequest request, HttpServletResponse response) throws Exception{

        //System.out.println("dto.toString() = " + dto.toString());

        Integer custId = 1; // 임의로 지정

        int updateOk = cartService.updateItemQty(dto);
        if( updateOk != 1 ) { throw new Exception("cart item quantity update error"); }

        // 결과 반환
        Map<String, Object> updateResult = new HashMap<>();
        updateResult.put("itemQty", dto.getItemQty());


        // 비회원일때 수량 처리

        return updateResult;
    }

//    @PostMapping("/order")
//    public String order(@RequestParam List<CartProdDto> dto, Model model) {
//        // 선택된 상품 IDs를 처리합니다. 실제 애플리케이션에서는 DB에서 상품 정보를 가져올 수 있습니다.
//
//        // 예시: 상품 ID를 기반으로 상품 목록을 조회하는 로직
//        // List<Product> selectedProducts = productService.findProductsByIds(productIds);
//
//        // model.addAttribute("productList", productIds);
//        return "orderForm"; // 주문 요약 페이지로 이동
//    }






    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("custId")!=null;
    }


}

