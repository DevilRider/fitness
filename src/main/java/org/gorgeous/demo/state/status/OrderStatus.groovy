package org.gorgeous.demo.state.status

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gorgeous.demo.state.Order

/**
 * 状态接口.
 * @project danakilat
 * @author l.yang on 2018/4/4 下午6:44
 */
@CompileStatic
@Slf4j
abstract class OrderStatus {

    protected Order order

    abstract OrderStatus next(Order order)

    abstract OrderStatus back(Order order)

    void print() {
        log.info("当前状态: {}", this.getClass().getSimpleName())
    }
}
