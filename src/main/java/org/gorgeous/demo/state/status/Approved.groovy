package org.gorgeous.demo.state.status

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gorgeous.demo.state.Order

/**
 * TODO 添加描述信息.
 * @project danakilat
 * @author l.yang on 2018/4/4 下午6:47
 */
@CompileStatic
@Slf4j
class Approved extends OrderStatus {
    @Override
    OrderStatus next(Order order) {
        throw new IllegalStateException("当前状态不支持下一步")
    }

    @Override
    OrderStatus back(Order order) {
        print()
        return new Apply(order: order)
    }

}
