package com.amu.oauth2.server.service;

import com.amu.oauth2.server.domain.TbPermission;

import java.util.List;

public interface TbPermissionService {
    default List<TbPermission> selectByUserId(Long userId) {
        return null;
    }
}