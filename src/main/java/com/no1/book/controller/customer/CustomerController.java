package com.no1.book.controller.customer;

import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.service.customer.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //로그인
    @GetMapping("/login")
    public CustomerDto login(HttpSession session)throws Exception{
        CustomerDto customerDto =new CustomerDto();

        Object obj =session.getAttribute("SPRING_SECURITY_CONTEXT");

        if(obj==null) {
            customerDto.setName("member/login");
        }else {
            customerDto.setName("redirect:/");
        }
        return customerDto;
    }
    @PostMapping("/login")
    public String login(@RequestParam("custId") String custId, String pwd, HttpSession session) {
        CustomerDto customerDto = customerService.login(custId, pwd);
        log.info("==========loginMember={}", customerDto);
        if (customerDto != null) {
            session.setAttribute("custId", customerDto.getCustId());
            session.setAttribute("nickname", customerDto.getNickName());
            session.setAttribute("email", customerDto.getEmail());
            session.setAttribute("birthDate", customerDto.getBirthDate());
            session.setAttribute("address", customerDto.getMainAddr());
            session.setAttribute("mobileNum", customerDto.getMobileNum());
            return "redirect:/customer/mypage";
        }
        session.setAttribute("custId", customerDto.getCustId());
        session.setAttribute("nickname", customerDto.getNickName());

        return "customer/login";
         // Return to login page if credentials are incorrect
    }
    @RequestMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
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

    // Handle signup process
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
    public String showMyPage(HttpSession session, Model model) {
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
    @GetMapping("/pagetemplate")
    public String showTemplate() {
        return "customer/pagetemplate";
    }
    /*@GetMapping("/customer/mypage")
    public String myPage(HttpSession session, Model model) {
        String custId = (String) session.getAttribute("custId");

        if (custId != null) {
            CustomerDto customerDto = customerService.getCustomerById(custId);
            model.addAttribute("customer", customerDto);
        }

        return "customer/mypage";
    }

     */

   /* @GetMapping("/customer/mypage")public String getCustomerPage(@RequestParam("custId") String custId, Model model) {
        CustomerDto customer = customerService.getCustomerById(custId); // Fetch customer

        if (customer == null) {
            // Initialize a default customer object to avoid null references in the template
            customer = new CustomerDto();
            customer.setName("Default Name");
            // Optionally set other default fields here
        }

        model.addAttribute("customer", customer);
        return "customer/mypage";
    }
    */

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

    // Update customer information
    @PutMapping("/{custId}")
    public ResponseEntity<String> editInfo(@PathVariable String custId, @RequestBody CustomerDto customerDto) {
        try {
            customerService.editInfo(custId);
            return new ResponseEntity<>("Customer information updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating customer information", e);
            return new ResponseEntity<>("Error updating customer information", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
