package org.k2.processmining.service;

import org.k2.processmining.model.LogGroup;
import org.k2.processmining.model.log.NormalLog;
import org.k2.processmining.model.log.RawLog;
import org.k2.processmining.model.user.User;
import org.k2.processmining.support.normal.transform.LogConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Aria on 2017/6/13.
 */
public interface RawLogService extends CommonLogService<RawLog> {

    @PreAuthorize("hasPermission(#rawLog, 'normalize')")
    @Transactional
    NormalLog normalize(RawLog rawLog, LogConfiguration lc);
}
