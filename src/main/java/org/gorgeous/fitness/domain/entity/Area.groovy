package org.gorgeous.fitness.domain.entity

import groovy.transform.CompileStatic

@CompileStatic
class Area {
    int id
    Area parent
    Integer level
    String name
    String zipCode
}
