package org.gorgeous.demo.state

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gorgeous.demo.state.status.Apply
import org.gorgeous.demo.state.status.OrderStatus

/**
 * @project fitness
 * @author l.yang on 2018/4/4 下午6:46
 */
@CompileStatic
@Slf4j
class Main {

    static void main(String[] args) {
        Order order = new Order()
        order.setStatus(new Apply(order: order))
        OrderStatus status = order.getStatus()
        status = status.next(order)
        log.info("==========")
        status = status.back(order)
        log.info("==========")
        status = status.next(order)
    }
}