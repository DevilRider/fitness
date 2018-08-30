package org.gorgeous.fitness.util

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.junit.Test

@CompileStatic
@Slf4j
class IdGeneratorTest {

    @Test
    void testGenerateId() {
        String id = IdGenerator.instance.generateId("U")
        log.debug("${id}")
    }
}
