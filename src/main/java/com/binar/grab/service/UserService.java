package com.binar.grab.service;

import com.binar.grab.dao.request.LoginModel;
import com.binar.grab.dao.request.RegisterModel;

import java.util.Map;

public interface UserService {
    Map registerManual(RegisterModel objModel) ;

    public Map login(LoginModel objLogin);

}
