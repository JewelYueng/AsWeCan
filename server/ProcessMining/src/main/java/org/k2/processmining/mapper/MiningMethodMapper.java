package org.k2.processmining.mapper;

import org.apache.ibatis.annotations.Param;
import org.k2.processmining.model.miningmethod.MiningMethod;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nyq on 2017/6/17.
 */
@Repository
public interface MiningMethodMapper {
    List<MiningMethod> listMethodByState(@Param("state") int state);
    List<MiningMethod> listMethods();
    MiningMethod getMethodById(@Param("id") String id);
    void save(MiningMethod miningMethod);
    void delete(@Param("ids")List<String> ids);
    void updateState(@Param("ids")List<String> ids, @Param("state")int state);
}
