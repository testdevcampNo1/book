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

        // 세션에서 회원 여부 조회
        if(loginCheck(request) == true){ // 회원일 경우 - DB조회

            String custId = (String)session.getAttribute("custId");

            try{
                List<CartProdDto> cartProducts  = cartService.read(custId);

                m.addAttribute("custId",custId);
                m.addAttribute("cartProdDto", cartProducts);

            } catch(Exception e){
                e.printStackTrace();
            }

        }else{ // 비회원일 경우 - 세션 조회

            try{
                m.addAttribute("custId",session.getId());
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

        if(loginCheck(request) == true) { // 회원일 경우 - DB조회

            try{
                String custId = (String)session.getAttribute("custId");

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

                cartProducts.removeIf(obj -> obj.getProdId().equals(prodId));
                
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
    public  Map<String, Object> addItem(@RequestBody CartProdDto reqDto, HttpServletRequest request, HttpServletResponse response) throws Exception{

        Map map = new HashMap();
        HttpSession session = request.getSession();

        if(loginCheck(request) == true){

            String custId  = (String)session.getAttribute("custId");

            CartDto dto = new CartDto(reqDto.getCustId(), reqDto.getProdId(), reqDto.getItemQty());
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

            int ret;

            // 장바구니에 상품이 존재하면 수량만 업데이트, 존재하지 않으면 상품 추가
            if(hasItem == true) { ret = cartService.updateItemQty(dto); }
            else                { ret = cartService.insertItem(dto); }

            if(ret == 1) { map.put("status", "success"); }
            else         { map.put("status", "fail"); }

        }else{   // 비회원일 경우 - 세션 상품목록에서

            List<CartProdDto> cartProducts = (List<CartProdDto>) session.getAttribute("cartLists");

            if(cartProducts != null){

                // 장바구니에 상품 추가
                boolean productExists = false;
                for (CartProdDto product : cartProducts) {
                    if (product.getProdId().equals(reqDto.getProdId())) {
                        product.setItemQty(product.getItemQty() + reqDto.getItemQty());
                        productExists = true;
                        break;
                    }
                }

                if (!productExists) { cartProducts.add(reqDto); }
                session.setAttribute("cartLists", cartProducts);

            }else{
                List<CartProdDto> cartProducts2 = new ArrayList<CartProdDto>();
                cartProducts2.add(reqDto);

                session.setAttribute("cartLists", cartProducts2);
            }
            map.put("status","success");
        }
        return map;  // status : successs, fail
    }


    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateQuantity(@RequestBody CartDto dto,HttpServletRequest request, HttpServletResponse response) throws Exception{

        Map map = new HashMap();
        HttpSession session = request.getSession();

        Map<String, Object> updateResult = new HashMap<>();

        if(loginCheck(request) == true) {
            CartDto newDto = dto;
            String custId = (String)session.getAttribute("custId");
            newDto.setCustId(custId);

            int updateOk = cartService.updateItemQty(newDto);
            if( updateOk != 1 ) { throw new Exception("cart item quantity update error"); }

            // 결과 반환
            updateResult.put("itemQty", newDto.getItemQty());

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
                    product.setItemQty(newProduct.getItemQty());
                    break;
                }
            }
            session.setAttribute("cartLists", cartProducts);
            updateResult.put("itemQty", newProduct.getItemQty());
        }
        return updateResult;
    }

    private boolean loginCheck(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute("custId")!=null;
    }
}