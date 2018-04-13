package org.gorgeous.fitness.domain.mapper

import org.gorgeous.AbstractTest
import org.gorgeous.fitness.domain.entity.Area
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * 地区mapper 测试.
 * @project danakilat
 * @author l.yang on 2018/3/15 下午2:21
 */
class SubAreaMapperTest extends AbstractTest {

    @Autowired
    private SubAreaMapper areaMapper

    @Test
    void testFindByParentId(){
        int pid = 1
        List<Area> areas = areaMapper.findByParentId(pid)
        assert areas != null
    }
}
