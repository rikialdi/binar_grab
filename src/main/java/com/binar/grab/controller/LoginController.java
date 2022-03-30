package com.binar.grab.controller;


import com.binar.grab.config.Config;
import com.binar.grab.dao.request.LoginModel;
import com.binar.grab.dao.request.RegisterModel;
import com.binar.grab.model.oauth.User;
import com.binar.grab.repository.oauth.UserRepository;
import com.binar.grab.service.UserService;
import com.binar.grab.service.email.EmailSender;
import com.binar.grab.util.EmailTemplate;
import com.binar.grab.util.SimpleStringUtils;
import com.binar.grab.util.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/user-login/")
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserService serviceReq;

    @Value("${expired.token.password.minute:}")//FILE_SHOW_RUL
    private int expiredToken;

    @Autowired
    public TemplateResponse templateCRUD;

    @PostMapping("/login")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map> login(@Valid @RequestBody LoginModel objModel) {
        Map map = serviceReq.login(objModel);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

}
