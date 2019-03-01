package org.linyi.base.entity.entity;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/26 0026 上午 10:23
 */
public class WrapData<T> {

    private T data;

    public WrapData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
