package org.gorgeous.fitness.util;

import org.gorgeous.AbstractTest;
import org.gorgeous.fitness.domain.mapper.AreaMapper;
import org.junit.Assert;
import org.junit.Test;

public class SpringBeanHelperTest extends AbstractTest {

    @Test
    public void getBean() {
        AreaMapper mapper = SpringBeanHelper.getBean(AreaMapper.class);
        Assert.assertNotNull(mapper);
    }
}