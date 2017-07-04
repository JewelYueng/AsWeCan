package org.k2.processmining.service;

import cern.jet.random.Normal;
import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.EventLog;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface NormalLogService extends CommonLogService<NormalLog> {

    @PreAuthorize("hasPermission(#normalLog, 'transToEvent')")
    @Transactional
    EventLog transToEventLog(NormalLog normalLog);
}
