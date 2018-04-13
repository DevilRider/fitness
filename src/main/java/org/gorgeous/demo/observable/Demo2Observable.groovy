package org.gorgeous.demo.observable

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * TODO 添加描述信息.
 * @project danakilat
 * @author l.yang on 2018/4/12 下午3:23
 */
@CompileStatic
@Slf4j
class Demo2Observable extends Observable<Map> {

    @Override
    protected void subscribeActual(Observer<? super Map> observer) {
        log.info("${observer['data']}")
        observer.onNext(['data': "demo2"])
        observer.onComplete()
    }
}
