package com.binar.grab.controller;


import com.binar.grab.config.Config;
import com.binar.grab.dao.request.RegisterModel;
import com.binar.grab.model.oauth.User;
import com.binar.grab.repository.oauth.UserRepository;
import com.binar.grab.service.UserService;
import com.binar.grab.util.TemplateResponse;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user-register/")
public class Register {
    @Autowired
    private UserRepository userRepository;

    Config config = new Config();

    @Autowired
    public UserService serviceReq;

    @Autowired
    public TemplateResponse templateCRUD;
    @PostMapping("/register")
    public ResponseEntity<Map> saveRegisterManual(@RequestBody RegisterModel objModel) throws RuntimeException {
        Map map = new HashMap();

        User user = userRepository.checkExistingEmail(objModel.getEmail());
        if (null != user) {
            return new ResponseEntity<Map>(templateCRUD.notFound("Username sudah ada"), HttpStatus.OK);

        }
        map = serviceReq.registerManual(objModel);


        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }
}
