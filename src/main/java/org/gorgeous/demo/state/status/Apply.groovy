package org.gorgeous.demo.state.status

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gorgeous.demo.state.Order

/**
 * @project fitness
 * @author l.yang on 2018/4/4 下午6:48
 */
@CompileStatic
@Slf4j
class Apply extends OrderStatus {

    @Override
    OrderStatus next(Order order) {
        print()
        return new Approved(order: order)
    }

    @Override
    OrderStatus back(Order order) {
        throw new IllegalStateException("当前状态不支持回退")
    }

}
