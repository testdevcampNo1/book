package com.no1.book.service.customer;

import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.dao.customer.CustomerDao;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CustomerService implements CustomerSV{


    private static final Logger log = LoggerFactory.getLogger(CustomerService.class); //로그 남기기
    @Autowired
    private final JavaMailSender javaMailSender;
    private static final String senderEmail="gldjfh05723@gmail.com";
    public static String number;

    private static final int MAX_FAILED_ATTEMPTS = 3;  //최대 로그인 실패 횟수
    private static final int LOCK_TIME_DURATION = 15; //계정 잠금 시간
    public final CustomerDao customerDao;



    //회원가입
    @Override
    public CustomerDto signUp(CustomerDto customerDto) {
        customerDao.insertCustomer(customerDto);
        log.info("Customer signUp",customerDto);
        return customerDto;
    }

    //로그인
    @Transactional
    public CustomerDto login(String custId, String pwd) {
        log.info("============= member login service");
        CustomerDto customerDto=customerDao.selectCustomer(custId);
        log.info("custId",customerDto.getCustId());
        if(customerDto!=null) {
            if (customerDto.getPwd().equals(pwd)) { //같은 경우 성공
                resetFailedAttempts(customerDto);
                return customerDto;
                //회원이 입력한 비밀번호랑 조회한 값의 비밀번호가 같으면 성공
            }
        }
       return null; //1번

    }


    public String handleFailedAttempt(CustomerDto customerDto) {
        int newFailedAttempts = customerDto.getFailedAttempts() + 1;
        customerDto.setFailedAttempts(newFailedAttempts);

        if (newFailedAttempts >= MAX_FAILED_ATTEMPTS) {
            customerDto.setLockUntil(LocalDateTime.now().plus(LOCK_TIME_DURATION, ChronoUnit.MINUTES));
            customerDao.updateFailedAttempts(customerDto);
            return "Account locked due to too many failed login attempts.";
        } else {
            customerDao.updateFailedAttempts(customerDto);
            return "Incorrect password. ";
        }
    }
    private void resetFailedAttempts(CustomerDto customerDto) {
        customerDto.setFailedAttempts(0);
        customerDto.setLockUntil(null);
        customerDao.updateFailedAttempts(customerDto);
    }

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
    @Transactional
    @Override
    public void editInfo(CustomerDto customerDto) {
        // Assuming updateCustomer() performs the necessary update in the database
        customerDao.updateCustomer(customerDto);
        log.info("Updated customer: {}", customerDto);
    }

    //id 사용 가능여부
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
    // 이메일 인증을 위한 코드생성
    public static void createNumber(){
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<8; i++) { // 총 8자리 인증 번호 생성
            int idx = random.nextInt(3); // 0~2 사이의 값을 랜덤하게 받아와 idx에 집어넣습니다

            // 0,1,2 값을 switchcase를 통해 꼬아버립니다.
            // 숫자와 ASCII 코드를 이용합니다.
            switch (idx) {
                case 0 :
                    // 0일 때, a~z 까지 랜덤 생성 후 key에 추가
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    // 1일 때, A~Z 까지 랜덤 생성 후 key에 추가
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 2일 때, 0~9 까지 랜덤 생성 후 key에 추가
                    key.append(random.nextInt(9));
                    break;
            }
        }
        number = key.toString();
    }

    //메일생성
    @Override
    public MimeMessage CreateMail(String email) {
        createNumber();
        log.info("Number : {}",number);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true); // Helper 사용
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(email);
            messageHelper.setSubject("[정석문고] 이메일 인증 번호 발송");

            String body = "<html><body style='background-color: #000000 !important; margin: 0 auto; max-width: 600px; word-break: break-all; padding-top: 50px; color: #ffffff;'>";
            body += "<img class='logo' src='cid:image'>";
            body += "<h1 style='padding-top: 50px; font-size: 30px;'>이메일 주소 인증</h1>";
            body += "<p style='padding-top: 20px; font-size: 18px; opacity: 0.6; line-height: 30px; font-weight: 400;'>안녕하세요? 정석문고 관리자 입니다.<br />";
            body += "정석문고 서비스 사용을 위해 회원가입시 고객님께서 입력하신 이메일 주소의 인증이 필요합니다.<br />";
            body += "하단의 인증 번호로 이메일 인증을 완료하시면, 정상적으로 정석문고 서비스를 이용하실 수 있습니다.<br />";
            body += "항상 최선의 노력을 다하는 정석문고가 되겠습니다.<br />";
            body += "감사합니다.</p>";
            body += "<div class='code-box' style='margin-top: 50px; padding-top: 20px; color: #000000; padding-bottom: 20px; font-size: 25px; text-align: center; background-color: #f4f4f4; border-radius: 10px;'>" + number + "</div>";
            body += "</body></html>";
            messageHelper.setText(body, true);
        }catch (MessagingException e){
            e.printStackTrace();
        }
        return mimeMessage;
    }

    //메일 전송
    public String sendMail(String email) {
        MimeMessage mimeMessage = CreateMail(email);
        log.info("[Mail 전송 시작]");
        javaMailSender.send(mimeMessage);
        log.info("[Mail 전송 완료]");
        return number;
    }
    private static Map<String,CustomerDto> store=new HashMap<>();
    private static long sequence=0L;

    public CustomerDto findById(String id){
        return store.get(id);
    }


    public Optional<CustomerDto> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getCustId().equals(loginId))
                .findFirst();



    }
    public List<CustomerDto> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }



}