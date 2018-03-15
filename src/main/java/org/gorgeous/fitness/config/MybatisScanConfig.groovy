package org.gorgeous.fitness.config

import groovy.transform.CompileStatic
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration

/**
 * Mybatis 配置.
 * @project fitness
 * @author l.yang on 2018/3/15 上午11:07
 */
@Configuration
@CompileStatic
@MapperScan(basePackages = ['org.gorgeous.*.mapper'])
class MybatisScanConfig {
}
