package org.gorgeous.fitness.util

import groovy.transform.CompileStatic
import org.junit.Test

@CompileStatic
class RandomCodeTest {

    @Test(expected = Exception.class)
    void testRandomException() {
        int length = 4
        int size = 30
        RandomCode.newInstance(length, size).random()
    }

    @Test
    void testRandomAll() {
        int length = 4
        int size = 30
        List<String> codes = RandomCode.newInstance(length, size)
                .useLowercase()
                .useNumeric(true)
                .useUppercase()
                .random()

        assert codes.size() == size
        assert codes.first().length() == length
    }

    @Test
    void testRandom() {
        int length = 4
        int size = 10
        List<String> codes = RandomCode.newInstance(length, size)
                .useNumeric(true)
                .useLowercase(true)
                .exclude('0','o','l','i','z')
                .random()

        codes.each {
            println(it)
        }

        assert codes.size() == size
        assert codes.first().length() == length
    }
}
