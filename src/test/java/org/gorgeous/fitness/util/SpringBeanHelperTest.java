package org.gorgeous.fitness.util;

import org.gorgeous.AbstractTest;
import org.gorgeous.fitness.domain.mapper.AreaMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpringBeanHelperTest extends AbstractTest {

    @Test
    public void getBean() {
        AreaMapper mapper = SpringBeanHelper.getBean(AreaMapper.class);
        assert mapper != null;
    }
}