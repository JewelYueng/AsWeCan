package org.k2.processmining.controller.admin;

import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.service.RawLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by nyq on 2017/7/3.
 */
@Controller
@RequestMapping("/admin/rawLog")
public class AdminRawLogController extends CommonAdminLogController<RawLog> {

    @Autowired
    public AdminRawLogController(RawLogService logService) {
        super(logService);
    }
}
