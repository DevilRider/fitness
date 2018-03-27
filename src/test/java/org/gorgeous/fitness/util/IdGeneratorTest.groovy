package org.gorgeous.fitness.util

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.junit.Test

/**
 * TODO 添加描述信息.
 * @project danakilat
 * @author l.yang on 2018/3/27 下午2:37
 */
@CompileStatic
@Slf4j
class IdGeneratorTest {

    @Test
    void testGenerateId() {
        String id = IdGenerator.instance.generateId("U")
        log.debug("${id}")
    }
}
