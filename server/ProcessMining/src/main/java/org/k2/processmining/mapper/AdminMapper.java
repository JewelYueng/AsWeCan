package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.user.Administrator;
import org.springframework.stereotype.Repository;

/**
 * Created by Aria on 2017/6/22.
 */

@Repository
public interface AdminMapper {

    Administrator getAdminByWorkId(@Param("workId") String workId);
    Administrator getAdminByWorkIdAndPwd(@Param("workId")String workId,@Param("password")String password);
}
