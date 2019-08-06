package com.amu.oauth2.server.service;

import com.amu.oauth2.server.domain.TbUser;

public interface TbUserService {
    default TbUser getByUsername(String username) {
        return null;
    }
}