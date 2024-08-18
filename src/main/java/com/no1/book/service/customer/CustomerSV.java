package com.no1.book.service.customer;

import com.no1.book.domain.customer.CustomerDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.List;

public interface CustomerSV {
    public CustomerDto signUp(CustomerDto customerDto);
    public CustomerDto login(String custId, String pwd);
    public List<CustomerDto> getCustomerList();
    public CustomerDto getCustomerById(String custId);
    public CustomerDto selectCustomer(String custId,String pwd);
    public void editInfo(CustomerDto customerDto);
    public boolean isIdAvailable(String custId);
    public boolean isLoggedIn(HttpSession session);
    public MimeMessage CreateMail(String email);
}
