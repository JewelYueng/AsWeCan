package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.mergemethod.MergeMethod;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nyq on 2017/6/17.
 */
@Repository
public interface MergeMethodMapper {
    List<MergeMethod> listMethodByState(@Param("state") int state);
    List<MergeMethod> listMethods();
    MergeMethod getMethodById(@Param("id") String id);
    void save(MergeMethod mergeMethod);

    void delete(@Param("ids")List<String> ids);
    void updateState(@Param("ids")List<String> ids, @Param("state")int state);
}
