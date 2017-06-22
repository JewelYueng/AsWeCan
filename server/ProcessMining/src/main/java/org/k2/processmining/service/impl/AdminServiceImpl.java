package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.AdminMapper;
import org.k2.processmining.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

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
            return 0;
        }
        if (adminMapper.getAdminByWorkIdAndPwd(workId,password) == null){
            return 1;
        }
        return 2;
    }
}
