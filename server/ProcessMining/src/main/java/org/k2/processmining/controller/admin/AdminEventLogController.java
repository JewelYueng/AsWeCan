package org.k2.processmining.controller.admin;


import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.service.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by nyq on 2017/7/3.
 */
@Controller
@RequestMapping("/admin/eventLog")
public class AdminEventLogController extends CommonAdminLogController<EventLog> {

    @Autowired
    public AdminEventLogController(EventLogService logService) {
        super(logService);
    }
}
