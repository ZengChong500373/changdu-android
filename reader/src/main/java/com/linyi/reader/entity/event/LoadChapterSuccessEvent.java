package com.linyi.reader.entity.event;

import com.api.changdu.proto.BookApi;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/20 0020 下午 6:04
 */
public class LoadChapterSuccessEvent {

    private BookApi.AckChapter.Data data;

    public LoadChapterSuccessEvent(BookApi.AckChapter.Data data) {
        this.data = data;
    }

    public BookApi.AckChapter.Data getData() {
        return data;
    }
}
