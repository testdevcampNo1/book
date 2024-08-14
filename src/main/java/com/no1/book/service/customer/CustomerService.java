package com.no1.book.service.customer;

import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.dao.customer.CustomerDao;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerService implements CustomerSV{
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class); //로그 남기기

    public final CustomerDao customerDao;
    //회원가입
    @Override
    public CustomerDto signUp(CustomerDto customerDto) {
        customerDao.insertCustomer(customerDto);
        log.info("Customer signUp",customerDto);
        return customerDto;
    }

    @Transactional(readOnly = true)
    public CustomerDto login(String custId, String pwd) {
        log.info("============= member login service");
        CustomerDto customerDto=customerDao.selectCustomer(custId);
        if(customerDto!=null) {
            //회원이 입력한 비밀번호랑 조회한 값의 비밀번호가 같으면 성공
            if (customerDto.getPwd().equals(pwd)) {
                return customerDto;
            }
        }
        //throw new RuntimeException("error");

       return null;

    }
    /*  Map<String, Object> params = new HashMap<>();
        params.put("cust_id", custId);
        params.put("pwd", pwd);
        try {
            CustomerDto customer = customerDao.loginCustomer(customerDto);
            if (customer == null) {
                log.info("Login failed for user: {}", custId);
                return null;
            }
            return customer;
        } catch (Exception e) {
            log.error("Error during login for user: {}", custId, e);
            throw new RuntimeException("Login failed due to system error", e);
        }  Map<String, Object> params = new HashMap<>();
        params.put("cust_id", custId);
        params.put("pwd", pwd);
        try {
            CustomerDto customer = customerDao.loginCustomer(customerDto);
            if (customer == null) {
                log.info("Login failed for user: {}", custId);
                return null;
            }
            return customer;
        } catch (Exception e) {
            log.error("Error during login for user: {}", custId, e);
            throw new RuntimeException("Login failed due to system error", e);
        }*/


    //목록조회
    @Override
    public List<CustomerDto> getCustomerList() {
        List<CustomerDto> customerDtoList=customerDao.selectCustomerList();
        log.info("Customer list {}",customerDtoList);
        return customerDao.selectCustomerList();
    }
    @Override
    public CustomerDto getCustomerById(String custId) {
        CustomerDto customerDto = customerDao.selectCustomer(custId);
        log.info("Fetched customer: {}", customerDto);
        return customerDto;
    }
    @Override
    public CustomerDto selectCustomer(String custId,String pwd) {
        CustomerDto customerDto = customerDao.selectCustomer(custId);
        log.info("Fetched customer: {}", customerDto);
        return customerDto;
    }
    //정보 수정
    @Override
    public void editInfo(String custId) {
        customerDao.updateCustomer(customerDao.selectCustomer(custId));
        log.info("Updated customer: {}", customerDao.selectCustomer(custId));
    }
    //id 체크
    @Override
    public boolean isIdAvailable(String custId) {
        int count = customerDao.idCheckCustomer(custId);
        return count == 0;
    }
    @Override
    public boolean isLoggedIn(HttpSession session) {
        CustomerDto loggedInUser = (CustomerDto) session.getAttribute("loggedInUser");

        // 만약 세션에 사용자가 저장되어 있다면 로그인 상태로 간주
        return loggedInUser != null;
    }


}