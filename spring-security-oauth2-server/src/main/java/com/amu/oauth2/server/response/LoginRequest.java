package com.amu.oauth2.server.response;


import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class LoginRequest{

    String username;
    String password;
    String verifycode;

}