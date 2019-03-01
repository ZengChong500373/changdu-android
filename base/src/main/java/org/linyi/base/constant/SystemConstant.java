package org.linyi.base.constant;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/1 0001 下午 3:16
 */
public interface SystemConstant {

    //https://blog.csdn.net/hjxgood/article/details/20057989
    //pb文件存储编码格式（不能采用UTF-8，变长）
    String CHARSET_NAME = "ISO-8859-1";

    //书籍详情存储的类型(值必须以2的n次方递增，2, 4, 8, 16...)
    interface BookDetailType{
        int NONE = 0;       //无
        int HISTORY = 1;    //历史记录
        int BOOKSHELF = 2;  //书架
    }

}
