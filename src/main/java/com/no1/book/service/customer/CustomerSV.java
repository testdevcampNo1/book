package com.no1.book.service.customer;

import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.mapper.customer.CustomerDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerSV {
    public CustomerDto signUp(CustomerDto customerDto);
    public CustomerDto login(String custId, String pwd);
    public List<CustomerDto> getCustomerList();
    public CustomerDto getCustomerById(String custId);
    public CustomerDto selectCustomer(String custId,String pwd);
    public void editInfo(String custId);
    public boolean isIdAvailable(String custId);
    public boolean isLoggedIn(HttpSession session);

}
