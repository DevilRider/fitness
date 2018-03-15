package org.gorgeous.fitness.domain.mapper

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.gorgeous.fitness.domain.entity.Area

/**
 *  区域测试mapper.
 * @project danakilat
 * @author l.yang on 2018/3/15 下午2:17
 */
interface AreaMapper {

    @Select("select * from area where pid=#{pid}")
    List<Area> findByParentId(@Param("pid") int pid)
}