package org.linyi.base.entity.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @fun: 本地持久化的小说需要继承该类
 * @developer: rwz
 * @date: 2019/1/23 0023 下午 4:15
 */
@Entity
public class BaseNovelDbEntity {

    //自增主键
    @Id(autoincrement = true)
    private Long keyID;
    //小说ID
    private String bookID;
    //阅读进度(在章节中的位置)
    private long position;
    //最近看的章节序号
    private int lastChapterSort;
    //上次阅读保存时间
    private long lastTime;
    //书籍详情书籍
    private String detailStr;

    public BaseNovelDbEntity(String bookID, String detailStr) {
        this.bookID = bookID;
        this.detailStr = detailStr;
    }

    @Generated(hash = 277175861)
    public BaseNovelDbEntity(Long keyID, String bookID, long position,
            int lastChapterSort, long lastTime, String detailStr) {
        this.keyID = keyID;
        this.bookID = bookID;
        this.position = position;
        this.lastChapterSort = lastChapterSort;
        this.lastTime = lastTime;
        this.detailStr = detailStr;
    }

    @Generated(hash = 1276947801)
    public BaseNovelDbEntity() {
    }

    public String getBookID() {
        return bookID;
    }

    public long getPosition() {
        return position;
    }

    public int getLastChapterSort() {
        return lastChapterSort;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public void setLastChapterSort(int lastChapterSort) {
        this.lastChapterSort = lastChapterSort;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public String getDetailStr() {
        return detailStr;
    }

    public void setDetailStr(String detailStr) {
        this.detailStr = detailStr;
    }

    public Long getKeyID() {
        return this.keyID;
    }

    public void setKeyID(Long keyID) {
        this.keyID = keyID;
    }

    @Override
    public String toString() {
        return "BaseNovelDbEntity{" +
                "keyID=" + keyID +
                ", bookID='" + bookID + '\'' +
                ", position=" + position +
                ", lastChapterSort=" + lastChapterSort +
                ", lastTime=" + lastTime +
                ", detailStr='" + detailStr + '\'' +
                '}';
    }
}
