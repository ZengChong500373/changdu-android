package com.linyi.reader.net;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/1 0001 下午 5:15
 */
public interface API {

    /** 章节数据 **/
    String CHAPTER = "v1/book/chapter";

    /** 目录列表 **/
    String DIRECTORY = "v1/book/chapterIndex";

    /** 管理书架 **/
    String MANAGER_BOOKSHELF = "v1/book/addOrRemoveBookShelf";

    /** 书籍详情 **/
    String BOOK_DETAIL = "v1/book/detail";

}
