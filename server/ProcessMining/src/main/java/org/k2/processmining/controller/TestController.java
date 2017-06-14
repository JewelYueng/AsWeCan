package org.k2.processmining.controller;

import org.k2.processmining.model.testPojo;
import org.k2.processmining.service.MyTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Aria on 2017/6/14.
 */

@Controller
public class TestController {

    @Autowired
    MyTestService testService;

    @RequestMapping(value = "/test")
    public void test(){
        System.out.println("test");
        testService.insert(new testPojo(1111,"id2300"));

    }
}
