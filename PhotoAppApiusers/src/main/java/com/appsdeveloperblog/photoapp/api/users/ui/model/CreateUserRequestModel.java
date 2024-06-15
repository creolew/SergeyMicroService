package com.appsdeveloperblog.photoapp.api.users.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserRequestModel {

    @NotNull(message = "First name can not be null")
    @Size(min = 2, message = "First name must not be less than 2 characters")
    private String firstName;

    @NotNull(message = "Last name can not be null")
    @Size(min = 2, message = "Last name must not be less than 2 characters")
    private String lastName;
    @NotNull(message = "Password can not be null")
    @Size(min = 2, max = 16, message = "Password must be equal or greater than 8 characters and less than 16 characters")
    private String password;
    @NotNull(message = "Email can not be null")
    @Email
    private String email;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
