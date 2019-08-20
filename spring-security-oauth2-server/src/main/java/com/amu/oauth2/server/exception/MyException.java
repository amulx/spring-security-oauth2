package com.amu.oauth2.server.exception;

import org.springframework.security.core.AuthenticationException;

public class MyException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public MyException(String msg) {
        super(msg);
    }
}
