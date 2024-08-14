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

import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/list")
    public String read(Integer custId, Model m, HttpServletRequest request, HttpServletResponse response) throws Exception{

        HttpSession session = request.getSession();
        System.out.println("session = " + session.getId());


//        if(!loginCheck(request))
//            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        try{
            List<CartProdDto> cartProducts  = cartService.read(custId);
            System.out.println("cartProduct = " + cartProducts.size());

            session.setAttribute("cartLists",cartProducts);

            m.addAttribute("custId",custId);
            m.addAttribute("cartProdDto", cartProducts);
        } catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("session.getAttributeNames = " + session.getAttributeNames());
        System.out.println("session.getAttribute(\"cartLists\") = " + session.getAttribute("cartLists"));

        return "cart";
    }

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
    public int addItem(@RequestBody CartDto dto,HttpServletRequest request, HttpServletResponse response) throws Exception{


        // 서비스에서 이미 있는 값이면 수량만 업데이트해주고 아니면 상품 추가


        return 0;
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

        return updateResult;
    }





    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("custId")!=null;
    }
}

