package com.linyi.viva.extra.entity.comm;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/29 0029 上午 11:05
 */
public class DoubleEntity {

    private String one;
    private String two;
    private int img;

    public DoubleEntity(String one, String two) {
        this.one = one;
        this.two = two;
    }

    public DoubleEntity(String one, String two, int img) {
        this.one = one;
        this.two = two;
        this.img = img;
    }

    public String getOne() {
        return one;
    }

    public String getTwo() {
        return two;
    }

    public int getImg() {
        return img;
    }
}
