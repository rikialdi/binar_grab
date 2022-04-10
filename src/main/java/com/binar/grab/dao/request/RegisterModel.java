package com.binar.grab.dao.request;

import com.binar.grab.controller.validationpass.annotation.PasswordValueMatch;
import com.binar.grab.controller.validationpass.annotation.ValidPassword;

import javax.validation.constraints.*;

@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        )
})
public class RegisterModel {
    public Long id;
    @Size(
            min = 5,
            max = 14,
            message = "The author email '${validatedValue}' must be between {min} and {max} characters long"
    )
    public String email;

    public String username;

    @ValidPassword
    @NotNull(message =  "Password is mandatory")
    public String password;

    @ValidPassword
    @NotNull(message =  "Password is mandatory")
    private String confirmPassword;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String fullname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
