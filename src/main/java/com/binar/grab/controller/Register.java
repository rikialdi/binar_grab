package com.binar.grab.controller;


import com.binar.grab.config.Config;
import com.binar.grab.config.Oauth2AccessTokenConverter;
import com.binar.grab.dao.request.RegisterModel;
import com.binar.grab.model.oauth.User;
import com.binar.grab.repository.oauth.UserRepository;
import com.binar.grab.service.UserService;
import com.binar.grab.service.email.EmailSender;
import com.binar.grab.util.EmailTemplate;
import com.binar.grab.util.SimpleStringUtils;
import com.binar.grab.util.TemplateResponse;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/user-register/")
public class Register {
    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserService serviceReq;

    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
    private int expiredToken;

    @Autowired
    public TemplateResponse templateCRUD;


    @Autowired
    public EmailTemplate emailTemplate;

    @Autowired
    public EmailSender emailSender;

    //step 1
    @PostMapping("/register")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> saveRegisterManual(@Valid @RequestBody RegisterModel objModel) throws RuntimeException {
        Map map = new HashMap();

        User user = userRepository.checkExistingEmail(objModel.getEmail());
        if (null != user) {
            return new ResponseEntity<Map>(templateCRUD.notFound("Username sudah ada"), HttpStatus.OK);

        }
        map = serviceReq.registerManual(objModel);

        Map sendOTP = sendEmailegister(objModel);

        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }


    // Step 2: sendp OTP berupa URL: guna updeta enable agar bisa login:
    @PostMapping("/send-otp")//send OTP
    public Map sendEmailegister(@RequestBody RegisterModel user) {
        String message = "Thanks, please check your email for activation.";

        if (user.getEmail() == null) return templateCRUD.templateEror("No email provided");
        User found = userRepository.findOneByUsername(user.getEmail());
        if (found == null) return templateCRUD.notFound("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getRegisterTemplate();
        if (StringUtils.isEmpty(found.getOtp())) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findOneByOTP(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getFullname() == null ? found.getUsername1() : found.getFullname()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getFullname() == null ? found.getUsername1() : found.getFullname()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), "Register", template);
        return templateCRUD.templateSukses(message);
    }

    //step 3 : konfirmasi OTP
    @GetMapping("/register-confirm-otp/{token}")
    public ResponseEntity<Map> saveRegisterManual(@PathVariable(value = "token") String tokenOtp) throws RuntimeException {


        User user = userRepository.findOneByOTP(tokenOtp);
        if (null == user) {
            return new ResponseEntity<Map>(templateCRUD.templateEror("OTP tidak ditemukan"), HttpStatus.OK);
        }
        String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if (Long.parseLong(today) > Long.parseLong(dateToken)) {
            return new ResponseEntity<Map>(templateCRUD.templateEror("Your token is expired. Please Get token again."), HttpStatus.OK);
        }
        //update user
        user.setEnabled(true);
        userRepository.save(user);

        return new ResponseEntity<Map>(templateCRUD.templateEror("Sukses, Silahkan Melakukan Login"), HttpStatus.OK);
    }


    @Value("${BASEURL:}")//FILE_SHOW_RUL
    private String BASEURL;

    @PostMapping("/register-tymeleaf")
    public ResponseEntity<Map> saveRegisterManualTymeleaf(@RequestBody RegisterModel objModel) throws RuntimeException {
        Map map = new HashMap();

        User user = userRepository.checkExistingEmail(objModel.getEmail());
        if (null != user) {
            return new ResponseEntity<Map>(templateCRUD.notFound("Username sudah ada"), HttpStatus.OK);

        }
        map = serviceReq.registerManual(objModel);

        Map sendOTP = sendEmailegisterTymeleaf(objModel);

        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PostMapping("/send-otp-tymeleaf")//send OTP
    public Map sendEmailegisterTymeleaf(@RequestBody RegisterModel user) {
        String message = "Thanks, please check your email for activation.";

        if (user.getEmail() == null) return templateCRUD.templateEror("No email provided");
        User found = userRepository.findOneByUsername(user.getEmail());
        if (found == null) return templateCRUD.notFound("Email not found"); //throw new BadRequest("Email not found");

        String template = emailTemplate.getRegisterTemplate();
        if (StringUtils.isEmpty(found.getOtp())) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findOneByOTP(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();

            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getFullname() == null ? found.getUsername1() : found.getFullname()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", BASEURL + "user-register/web/index/" + otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getFullname() == null ? found.getUsername1() : found.getFullname()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", BASEURL + "user-register/web/index/" + found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), "Register", template);
        return templateCRUD.templateSukses(message);
    }

//    @Autowired(required = false)
//    private DefaultTokenServices tokenStore;
    @Autowired
    DefaultTokenServices  tokenStore;

    @Autowired
    private Oauth2AccessTokenConverter accessTokenConverter;

    @RequestMapping(method = RequestMethod.POST, value = "/tokens/revoke/{tokenId:.*}")
    @ResponseBody
    public Object revokeToken(@PathVariable String tokenId) {
//        tokenServices.revokeToken(tokenId);
        String tokenValue = null;
        final Authentication authenticationObject = SecurityContextHolder.getContext().getAuthentication();
        final Object detailObject = authenticationObject.getDetails();
        if (detailObject instanceof OAuth2AuthenticationDetails) {
            final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) detailObject;
            tokenValue = details.getTokenValue();
//            details.
            System.out.println("masuk disini 1");
            return detailObject;
        } else if (detailObject instanceof OAuth2AccessToken) {
//        return detailObject;
        /*
        isi detailObject
        {
    "remoteAddress": "0:0:0:0:0:0:0:1",
    "sessionId": null,
    "tokenValue": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6ImFkbWluQG1haWwuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTY0OTI4OTIyMywiYXV0aG9yaXRpZXMiOlsiUk9MRV9TVVBFUlVTRVIiLCJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sImp0aSI6ImYzZDg2ODRhLWEzY2UtNDMwMS05NmFkLTlkZGI5MGFkZDg4NyIsImNsaWVudF9pZCI6Im15LWNsaWVudC1hcHBzIn0.WDXabEJ8gKcy0lxEOyJMUBGTr6oTUa3LwYRG7ngQ9SM",
    "tokenType": "Bearer",
    "decodedDetails": null
}
         */
            final OAuth2AccessToken token = (OAuth2AccessToken) detailObject;
            tokenValue = token.getValue();
            tokenStore.revokeToken(tokenValue);
        } else {
            tokenValue = null;
        }

//        System.out.println("token 2";
        return detailObject;
    }
    @PostMapping("/oauth/logout")
    public ResponseEntity<String> revoke(HttpServletRequest request) {
//        try {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.contains("Bearer")) {
                String tokenValue = authorization.replace("Bearer", "").trim();

                OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
                tokenStore.revokeToken(tokenValue);


//                OAuth2Authentication auth = tokenStore.revokeToken(tokenValue);
//                auth.setDetails(accessToken);
//                auth.setAuthenticated(true);

                //OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(tokenValue);
//                OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
//                tokenStore.removeRefreshToken(refreshToken);
            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Invalid access token");
//        }

        return ResponseEntity.ok().body("Access token invalidated successfully");
    }

//    protected Authentication authenticate(Authentication authentication, String  tokenTEST) {
//        if (authentication == null) {
//            throw new InvalidTokenException("Invalid token (token not found)");
//        } else {
//            String token = (String) authentication.getPrincipal();
//
////            OAuth2Authentication auth = tokenStore.revokeToken(tokenTEST);
//            if (auth == null) {
//                throw new InvalidTokenException("Invalid token: " + token);
//            } else {
//                if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
//                    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
//                    if (!details.equals(auth.getDetails())) {
//                        details.setDecodedDetails(auth.getDetails());
//                    }
//                }
//                auth.setDetails(authentication.getDetails());
//                auth.setAuthenticated(true);
//                return auth;
//            }
//        }
//    }
}
