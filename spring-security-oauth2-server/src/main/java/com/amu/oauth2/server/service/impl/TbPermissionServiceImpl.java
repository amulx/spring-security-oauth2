package com.amu.oauth2.server.service.impl;

import com.amu.oauth2.server.domain.TbPermission;
import com.amu.oauth2.server.mapper.TbPermissionMapper;
import com.amu.oauth2.server.service.TbPermissionService;
import org.springframework.stereotype.Service;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TbPermissionServiceImpl implements TbPermissionService {

    @Resource
    private TbPermissionMapper tbPermissionMapper;

    @Override
    public List<TbPermission> selectByUserId(Long userId) {
        return tbPermissionMapper.selectByUserId(userId);
    }
}