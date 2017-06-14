package org.k2.processmining.service.impl;

import org.k2.processmining.mapper.testMapper;
import org.k2.processmining.model.testPojo;
import org.k2.processmining.service.MyTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Aria on 2017/6/14.
 */

@Service
public class MyTestServiceImpl implements MyTestService{

    @Autowired
    testMapper mapper;

    @Override
    public void insert(testPojo pojo) {
        mapper.add(pojo);
    }
}
