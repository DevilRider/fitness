package org.gorgeous.demo.observable
/**
 * TODO 添加描述信息.
 * @project danakilat
 * @author l.yang on 2018/4/12 下午3:23
 */
class Demo1Observable extends Observable<Map> {

    @Override
    protected void subscribeActual(Observer<? super Map> observer) {
        log.info("${observer['data']}")
        observer.onComplete()
    }
}
