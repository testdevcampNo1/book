package com.no1.book.controller.customer;

import com.no1.book.dao.customer.CustomerDao;
import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.service.customer.CustomerService;
import com.no1.book.service.customer.EmailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerController {
    // ------------------- 수정 된 부분 ----------------------
    //@Autowired
    //FlaskService flaskService;

    private final CustomerService customerService;
    private String number;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/")
    public String home(@SessionAttribute(name=SessionConst.LOGIN_MEMBER,required = false) CustomerDto loginCustomer, Model model) {
       if(loginCustomer == null) {
           return "/";
       }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("loginCustomer", loginCustomer);
        return "customer/mypage"; // Replace with your actual homepage view name
    }


    //로그인
    @GetMapping("/login")

    public void login()throws Exception{

    }
    @PostMapping("/login")
    public String login(@RequestParam("custId") String custId, String pwd, Model model, HttpServletRequest request) {
        CustomerDto customerDto = customerService.login(custId, pwd);

        if (customerDto != null) {
            HttpSession session1 = request.getSession();
           // session1.setAttribute(SessionConst.LOGIN_MEMBER, customerDto);
            session1.setAttribute("custId", customerDto.getCustId());

            HashMap toFlask = new HashMap();
            toFlask.put("custId", customerDto.getCustId());
            // ------------------- 수정 된 부분 ----------------------
            //flaskService.sendDataToFlask(toFlask, "receive-cust-id");

            return "redirect:/";
        }

        CustomerDto customerDto1 = customerService.getCustomerById(custId);
        String failureMessage = customerService.handleFailedAttempt(customerDto1);

        if (failureMessage.equals("Incorrect password.")) {
            model.addAttribute("errorMessage", "Incorrect password.");
        } else if (failureMessage.equals("Account locked due to too many failed login attempts.")) {
            model.addAttribute("errorMessage", "Account locked due to too many failed login attempts.");
        }

        return "customer/login";
    }

    @GetMapping("/loginHome")
    public String loginHome(HttpServletRequest request){

        return "customer/loginHome";
    }


    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response) {

        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }

        return "customer/login";
    }



    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkIdAvailability(@RequestParam String custId) {
        boolean isAvailable = customerService.isIdAvailable(custId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return ResponseEntity.ok(response);
    }



    // 회원가입
    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("customer", new CustomerDto());
        return "customer/signup";
    }


    // 회원가입
    @PostMapping("/signup")
    public String signUp(@ModelAttribute("customer") CustomerDto customerDto, Model model) {
        try {
            customerService.signUp(customerDto);
            return "redirect:/customer/login";
        } catch (Exception e) {
            log.error("Error during signup", e);
            model.addAttribute("error", "An error occurred during registration. Please try again.");
            return "customer/signup";
        }
    }
    // 마이페이지
    @GetMapping("/mypage")
    public String showMyPage(HttpSession session, Model model,HttpServletRequest request) {
        // Use session attribute instead of request parameter
        String customerId = (String) session.getAttribute("custId");

        if (customerId == null) {
            // Redirect to login if no user is logged in
            return "redirect:/customer/login";
        }

        // Fetch the customer from the service layer
        CustomerDto customer = customerService.getCustomerById(customerId);

        // Check if the customer object is null
        if (customer == null) {
            // Handle the case where the customer is not found
            model.addAttribute("error", "Customer not found.");
            return "error";
        }

        // Add the customer to the model
        model.addAttribute("customer", customer);

        // Return the Thymeleaf template name
        return "customer/mypage";
    }
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("custId") String custId, Model model) {
        CustomerDto customerDto = customerService.getCustomerById(custId);
        model.addAttribute("customer", customerDto);
        return "customer/edit";
    }

    // 회원 정보 수정
    @PostMapping("/edit")
    public String updateCustomerInfo(@ModelAttribute CustomerDto customerDto, Model model) {
        customerService.editInfo(customerDto);
        model.addAttribute("message", "Customer information updated successfully");
        return "redirect:/customer/mypage";
    }




    @GetMapping("/pagetemplate")
    public String showTemplate() {
        return "customer/pagetemplate";
    }


    // Fetch list of customers
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomerList() {
        try {
            List<CustomerDto> customers = customerService.getCustomerList();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching customer list", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Fetch customer details by ID
    @GetMapping("/{custId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable String custId) {
        try {
            CustomerDto customer = customerService.getCustomerById(custId);
            if (customer != null) {
                return new ResponseEntity<>(customer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error fetching customer by ID", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Update customer information
    @PutMapping("/{custId}")
    public ResponseEntity<String> editInfo(@PathVariable String custId, @RequestBody CustomerDto customerDto) {
        try {
            customerService.editInfo();
            return new ResponseEntity<>("Customer information updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating customer information", e);
            return new ResponseEntity<>("Error updating customer information", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
    @PostMapping("/sendMail")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> requestBody,HttpSession session) {
        String email = requestBody.get("email");
        log.info("이메일 이거임1", email);
        // 이메일로 인증 번호 전송
        try {
            String verificationCode = customerService.sendMail(email);
            session.setAttribute("verificationCode", verificationCode);
            log.info("이메일 이거임2", email);


            return ResponseEntity.ok("Verification code sent successfully to " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send verification code to " + email);
        }
    }
    // 인증번호 일치여부 확인
    @GetMapping("/mailCheck")
    public ResponseEntity<String> mailCheck(@RequestParam String userNumber, HttpSession session) {
        log.info("Verifying user code: {}", userNumber);
        String storedCode = (String) session.getAttribute("verificationCode");

        //코드 일치 여부 확인
        boolean isMatch = userNumber.equals(storedCode);

        if (isMatch) {
            log.info("Verification successful!");
            return ResponseEntity.ok("Verification successful!");
        } else {
            log.info("Verification failed. Provided code: {}, Expected code: {}", userNumber, storedCode);
        }
        return ResponseEntity.ok("Verification failed");
    }




}
