package org.gorgeous.demo.state

import org.gorgeous.demo.state.status.OrderStatus

/**
 * @project fitness
 * @author l.yang on 2018/4/4 下午6:58
 */
class Order {

    private OrderStatus status


    OrderStatus getStatus() {
        return status
    }

    void setStatus(OrderStatus status) {
        this.status = status
    }

}
