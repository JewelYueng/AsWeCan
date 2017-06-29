package org.k2.processmining.service;

import org.k2.processmining.model.user.Administrator;

import java.util.List;

/**
 * Created by Aria on 2017/6/22.
 */
public interface AdminService {

    int checkoutAdminByWorkIdAndPwd(String workId,String password);
    List<Administrator> getAllAdmins();
    Administrator getAdminByWorkId(String workId);
}