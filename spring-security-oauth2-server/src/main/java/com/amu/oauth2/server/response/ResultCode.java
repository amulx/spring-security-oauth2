package com.amu.oauth2.server.response;

public interface ResultCode {
    boolean success();

    int code();

    String message();
}
