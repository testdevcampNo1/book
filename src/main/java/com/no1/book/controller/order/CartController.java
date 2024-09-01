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
    public String read(Model m, HttpServletRequest request, HttpServletResponse response) throws Exception{

        HttpSession session = request.getSession();
        session.setAttribute("isUser","Y");
        // session.setAttribute("isUser","N");

        // 세션에서 회원 여부 조회
        if(session.getAttribute("isUser") == "Y"){ // 회원일 경우 - DB조회

            Integer custId = 1;

            try{
                List<CartProdDto> cartProducts  = cartService.read(custId);

                m.addAttribute("custId",custId);
                m.addAttribute("cartProdDto", cartProducts);

            } catch(Exception e){
                e.printStackTrace();
            }

        }else{ // 비회원일 경우 - 세션 조회

            try{

                System.out.println("비회원 session = " + session.getId());
                System.out.println("session.getAttributeNames = " + session.getAttributeNames());
                System.out.println("session.getAttribute(\"cartLists\") = " + session.getAttribute("cartLists"));

                m.addAttribute("sessionId",session.getId());
                m.addAttribute("cartProdDto", session.getAttribute("cartLists"));

            } catch(Exception e){
                e.printStackTrace();
            }
        }


        return "cart";
    }


    @GetMapping("/remove")
    public String remove(String prodId,  Model m, RedirectAttributes rattr, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        session.setAttribute("isUser","Y");
        // session.setAttribute("isUser","N");

        Integer custId = (Integer)session.getAttribute("custId");
                custId = 1; // 임의로 아이디값 설정

        if(session.getAttribute("isUser") == "Y") { // 회원일 경우 - DB조회
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
        }else{
            try{
                List<CartProdDto> cartProducts = (List<CartProdDto>) session.getAttribute("cartLists");
                cartProducts.removeIf(obj -> obj.getProdId() == prodId);
                session.setAttribute("cartLists", cartProducts);

                rattr.addAttribute("custId", session.getId());
                rattr.addAttribute("msg","DEL_OK");

            }catch(Exception e){
                e.printStackTrace();
                rattr.addAttribute("msg","DEL_ERR");
            }
        }


        return "redirect:/cart/list";
    }

    @PostMapping("/add")
    @ResponseBody
    public  Map<String, Object> addItem(@RequestBody CartDto reqDto, HttpServletRequest request, HttpServletResponse response) throws Exception{

        Map map = new HashMap();
        HttpSession session = request.getSession();
        session.setAttribute("isUser","Y");
        // session.setAttribute("isUser","N");

        if(session.getAttribute("isUser") == "Y"){

            // Integer custId  = (Integer)session.getAttribute("custId");
            Integer custId  = 1; // 임의로 아이디값 설정

            CartDto dto = reqDto;
            dto.setCustId(custId);

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

        }else{   // 비회원일 경우 - 세션 상품목록에서

            List<CartProdDto> cartProducts = (List<CartProdDto>) session.getAttribute("cartLists");

            // 장바구니에 추가할 상품 객체 생성
            CartProdDto newProduct = new CartProdDto();
            newProduct.setProdId(reqDto.getProdId());
            newProduct.setItemQty(reqDto.getItemQty());


            // 장바구니에 상품 추가
            boolean productExists = false;
            for (CartProdDto product : cartProducts) {
                if (product.getProdId().equals(newProduct.getProdId())) {
                    product.setItemQty(product.getItemQty() + newProduct.getItemQty());
                    productExists = true;
                    break;
                }
            }

            if (!productExists) { cartProducts.add(newProduct); }

            session.setAttribute("cartLists", cartProducts);
            map.put("status","success");
        }

        return map;  // status : successs, fail
    }


    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateQuantity(@RequestBody CartDto dto,HttpServletRequest request, HttpServletResponse response) throws Exception{

        Map map = new HashMap();
        HttpSession session = request.getSession();
        session.setAttribute("isUser","Y");
        // session.setAttribute("isUser","N");

        Map<String, Object> updateResult = new HashMap<>();

        if(session.getAttribute("isUser") == "Y") {
            Integer custId = 1; // 임의로 지정

            int updateOk = cartService.updateItemQty(dto);
            if( updateOk != 1 ) { throw new Exception("cart item quantity update error"); }

            // 결과 반환
            updateResult.put("itemQty", dto.getItemQty());

        }else{ // 비회원일때 수량 처리

            List<CartProdDto> cartProducts = (List<CartProdDto>) session.getAttribute("cartLists");

            // 장바구니에 추가할 상품 객체 생성
            CartProdDto newProduct = new CartProdDto();
            newProduct.setProdId(dto.getProdId());
            newProduct.setItemQty(dto.getItemQty());


            // 장바구니에 상품 추가
            boolean productExists = false;
            for (CartProdDto product : cartProducts) {
                if (product.getProdId().equals(newProduct.getProdId())) {
                    product.setItemQty(product.getItemQty() + newProduct.getItemQty());
                    break;
                }
            }

            session.setAttribute("cartLists", cartProducts);
            updateResult.put("itemQty", newProduct.getItemQty());
        }

        return updateResult;
    }


    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("custId")!=null;
    }

}

