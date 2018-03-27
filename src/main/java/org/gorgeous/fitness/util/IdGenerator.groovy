package org.gorgeous.fitness.util

import groovy.transform.CompileStatic

@CompileStatic
@Singleton
class IdGenerator {


    private static final Random random = new Random()

    String generateId(String prefix) {
        long time = System.currentTimeMillis()
        return String.format(Locale.US, "${prefix}%d%04d", time, random.nextInt(10000))
    }
}
