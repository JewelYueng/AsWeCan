package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.AdminMapper;
import org.k2.processmining.model.user.Administrator;
import org.k2.processmining.service.AdminService;
import org.k2.processmining.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Aria on 2017/6/22.
 */


@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    AdminMapper adminMapper;

    @Override
    public int checkoutAdminByWorkIdAndPwd(String workId, String password) {
        if (adminMapper.getAdminByWorkId(workId) == null){
            return 400; //工号不存在
        }
        if (adminMapper.getAdminByWorkIdAndPwd(workId, Util.encryptStr(password))==null){
            return 401; //密码错误
        }
        return 200; //验证成功
    }

    @Override
    public Administrator getAdminByWorkId(String workId) {
        return adminMapper.getAdminByWorkId(workId);
    }
}
