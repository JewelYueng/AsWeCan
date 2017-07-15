package org.k2.processmining.controller.admin;

import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.service.NormalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by nyq on 2017/7/3.
 */
@Controller
@RequestMapping("/admin/normalLog")
public class AdminNormalLogController extends CommonAdminLogController<NormalLog> {

    @Autowired
    public AdminNormalLogController(NormalLogService logService) {
        super(logService);
    }
}
