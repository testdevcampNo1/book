package com.no1.book.service.customer;

import com.no1.book.domain.customer.CustomerDto;
import jakarta.servlet.http.HttpSession;

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
