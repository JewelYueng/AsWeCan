package org.k2.processmining.mapper;

import org.k2.processmining.model.testPojo;

import java.util.List;

/**
 * Created by Aria on 2017/2/10.
 */
public interface testMapper {
    void add(testPojo pojo);

    void insert(List<testPojo> list);
    List<testPojo> showAll();
}
